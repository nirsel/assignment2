package bgu.spl.mics;
import bgu.spl.mics.application.services.LeiaMicroservice;


import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {


	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServiceMap;
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> messageMap;
	private ConcurrentHashMap<Event, Future> resultMap;




	private static class MessageBusImplHolder {
		private static MessageBusImpl instance=new MessageBusImpl();
	}

	private MessageBusImpl(){
		microServiceMap = new ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>>();
		messageMap = new ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>>();
		resultMap = new ConcurrentHashMap<Event, Future>();

	}

	public static MessageBusImpl getInstance(){
		return MessageBusImplHolder.instance;

	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (type.getClass()) {
			if (messageMap.containsKey(type)) { // if this type of event already exists
				ConcurrentLinkedQueue<MicroService> queue = messageMap.get(type);
				queue.add(m); // add m to this event queue
			} else { // this type of event doesn't exist
				ConcurrentLinkedQueue<MicroService> newQueue = new ConcurrentLinkedQueue<>(); // create a new queue for this type
				newQueue.add(m);
				messageMap.put(type, newQueue); // add the type and its queue to the map
			}
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (type.getClass()) {
			if (messageMap.containsKey(type)) { // if this type of broadcast already exists
				ConcurrentLinkedQueue<MicroService> queue = messageMap.get(type);
				queue.add(m);
			} else { // this type of broadcast doesn't exist
				ConcurrentLinkedQueue<MicroService> newQueue = new ConcurrentLinkedQueue<>();
				newQueue.add(m);
				messageMap.put(type, newQueue); // add the type and its queue to the map
			}
		}
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future<T> promisedResult= resultMap.get(e); // retrieves the promised result associated with the event
		promisedResult.resolve(result); //resolve this event
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized(b.getClass()) {
			if (messageMap.containsKey(b.getClass()) && messageMap.get(b.getClass()).size() > 0) { // if this type of BC is registered
				ConcurrentLinkedQueue<MicroService> queue = messageMap.get(b.getClass());
				for (MicroService m : queue) {
					LinkedBlockingQueue<Message> mesQueue = microServiceMap.get(m);
					mesQueue.add(b);
				}
			}
		}
	}

	
	@Override
	public  <T> Future<T> sendEvent(Event<T> e) {
		synchronized (e.getClass()) {
			if (messageMap.containsKey(e.getClass()) && messageMap.get(e.getClass()).size() > 0) {
				ConcurrentLinkedQueue<MicroService> microQueue = messageMap.get(e.getClass());
				MicroService ms = microQueue.poll();
				microQueue.add(ms); //round robin - removes the first and adds him to the tail of the queue
				Future<T> result = new Future<T>();
				resultMap.put(e, result);
				LinkedBlockingQueue<Message> messageQueue = microServiceMap.get(ms);
				messageQueue.add(e);
				return result;
			}
		}

        return null;
	}

	@Override
	public void register(MicroService m) {
		if (!microServiceMap.containsKey(m)) { // if m is not already in the MicroServiceMap
			microServiceMap.put(m, new LinkedBlockingQueue<Message>());
		}
	}

	@Override
	public void unregister(MicroService m) {
			if (microServiceMap.containsKey(m)) {
				microServiceMap.remove(m);
				messageMap.forEach((key, value) -> value.remove(m)); //removes m from every subscription he had
			}

	}

	@Override
	public  Message awaitMessage(MicroService m) throws InterruptedException{
		if (!microServiceMap.containsKey(m))
			throw new IllegalStateException();
		LinkedBlockingQueue<Message> messageQueue=microServiceMap.get(m);
		return messageQueue.take();
	}

}

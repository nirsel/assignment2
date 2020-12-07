package bgu.spl.mics;
import bgu.spl.mics.application.services.LeiaMicroservice;


import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {


	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> microServiceMap;
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> messageMap;
	private HashMap<Event, Future> resultMap;
	private Object eventLock=new Object();
	private Object broadLock=new Object();



	private static class MessageBusImplHolder {
		private static MessageBusImpl instance=new MessageBusImpl();
	}

	private MessageBusImpl(){
		microServiceMap = new ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>>();
		messageMap = new ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>>();
		resultMap = new HashMap<Event, Future>();

	}

	public static MessageBusImpl getInstance(){
		return MessageBusImplHolder.instance;

	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (eventLock) {
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
		synchronized (broadLock) {
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
		if (messageMap.containsKey(b.getClass()) && messageMap.get(b.getClass()).size() > 0) { // if this type of BC is registered
			ConcurrentLinkedQueue<MicroService> queue = messageMap.get(b.getClass());
			for (MicroService m : queue) {
				ConcurrentLinkedQueue<Message> mesQueue = microServiceMap.get(m);
				mesQueue.add(b);
			}
			synchronized (this){notifyAll();}
		}
	}

	
	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		if (messageMap.containsKey(e.getClass()) && messageMap.get(e.getClass()).size() > 0) {
			ConcurrentLinkedQueue<MicroService> microQueue = messageMap.get(e.getClass());
			MicroService ms = microQueue.poll();
			microQueue.add(ms); //round robin - removes the first and adds him to the tail of the queue
			ConcurrentLinkedQueue<Message> messageQueue = microServiceMap.get(ms);
			messageQueue.add(e);
			Future<T> result = new Future<T>();
			resultMap.put(e, result);
			notifyAll();
			return result;
		}
        return null;
	}

	@Override
	public void register(MicroService m) {
		if (!microServiceMap.containsKey(m)) { // if m is not already in the MicroServiceMap
			microServiceMap.put(m, new ConcurrentLinkedQueue<>());
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
	public synchronized Message awaitMessage(MicroService m) throws InterruptedException{
		if (!microServiceMap.containsKey(m))
			throw new IllegalStateException();
		Queue<Message> messageQueue=microServiceMap.get(m);
		while (messageQueue.isEmpty())
			wait();
		return messageQueue.poll();
	}

}

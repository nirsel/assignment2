package bgu.spl.mics;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {


	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> microServiceMap;
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> messageMap;
	private ConcurrentHashMap<Event, Future> resultMap; //concurrent?

	private static class MessageBusImplHolder {
		private static MessageBusImpl instance=new MessageBusImpl();
	}

	private MessageBusImpl(){
		microServiceMap = new ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>>();
		messageMap = new ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>>();
		resultMap = new ConcurrentHashMap<Event, Future>();

	}

	public static MessageBusImpl getInstance(){
		return MessageBusImplHolder.instance;

	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if (messageMap.containsKey(type)){ // if this type of event already exists
			ConcurrentLinkedQueue<MicroService> queue=messageMap.get(type); 
			queue.add(m); // add m to this event queue
		}
		else { // this type of event doesn't exist
			ConcurrentLinkedQueue<MicroService> newQueue = new ConcurrentLinkedQueue<MicroService>(); // create a new queue for this type
			newQueue.add(m);
			messageMap.put(type,newQueue); // add the type and its queue to the map
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (messageMap.containsKey(type)){ // if this type of broadcast already exists
			ConcurrentLinkedQueue<MicroService> queue=messageMap.get(type);
			queue.add(m);
		}
		else { // this type of broadcast doesn't exist
			ConcurrentLinkedQueue<MicroService> newQueue = new ConcurrentLinkedQueue<MicroService>();
			newQueue.add(m);
			messageMap.put(type,newQueue); // add the type and its queue to the map
		}
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future<T> promisedResult= resultMap.get(e); // retrieve the promised result associated with the event
		promisedResult.resolve(result);

	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		if (messageMap.containsKey(b.getClass())){
			ConcurrentLinkedQueue<MicroService> queue=messageMap.get(b.getClass());
			for (MicroService m : queue){
				ConcurrentLinkedQueue<Message> mesQueue=microServiceMap.get(m);
				mesQueue.add(b);
			}
			notifyAll();
		}
	}

	
	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		if (messageMap.containsKey(e.getClass())) {
			ConcurrentLinkedQueue<MicroService> microQueue = messageMap.get(e.getClass());
			MicroService ms = microQueue.poll();
			microQueue.add(ms); //round robin - removes the first and adds him to the tail of the queue
			ConcurrentLinkedQueue<Message> messageQueue = microServiceMap.get(ms);
			messageQueue.add(e);
			Future<T> result= new Future<T>();
			resultMap.put(e,result);
			notifyAll();
			return  result;
		}
        return null;
	}

	@Override
	public void register(MicroService m) {
		if (!microServiceMap.containsKey(m)) {
			ConcurrentLinkedQueue<Message> newQueue = new ConcurrentLinkedQueue<Message>();
			microServiceMap.put(m, newQueue);
		}
	}

	@Override
	public void unregister(MicroService m) {
		if (microServiceMap.containsKey(m)) {
			ConcurrentLinkedQueue<Message> queue = microServiceMap.get(m);
			microServiceMap.remove(m);
			messageMap.forEach((key,value)-> value.remove(m)); //removes m from every subscription he had
		}
	}

	@Override
	public synchronized Message awaitMessage(MicroService m) throws InterruptedException{
		if (!microServiceMap.containsKey(m))
			throw new IllegalStateException();
		ConcurrentLinkedQueue<Message> messageQueue=microServiceMap.get(m);
		if (!messageQueue.isEmpty()) {
			return messageQueue.poll();
		}
		else {
			while (messageQueue.isEmpty()){
				try {wait();}
				catch (InterruptedException e){}
			}
		}

		//todo: check how to block until he has message
		return messageQueue.poll();
	}

}

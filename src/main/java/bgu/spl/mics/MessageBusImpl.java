package bgu.spl.mics;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static MessageBusImpl instance=null; //singleton
	private Vector<ConcurrentLinkedQueue<Message>> messageQueues;
	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> microServiceMap;
	private Vector<ConcurrentLinkedQueue<MicroService>> subscribeQueue;
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> messageMap;

	private MessageBusImpl(){ //todo:constructor

	}

	public static MessageBusImpl getInstance(){ //singleton getInstance
		if (instance==null)
			instance=new MessageBusImpl();
		return instance;
	}
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		
        return null;
	}

	@Override
	public void register(MicroService m) {
		ConcurrentLinkedQueue<Message> newQueue=new ConcurrentLinkedQueue<Message>();
		microServiceMap.put(m,newQueue);
		messageQueues.add(newQueue);
	}

	@Override
	public void unregister(MicroService m) {
		ConcurrentLinkedQueue<Message> queue=microServiceMap.get(m);
		messageQueues.remove(queue);
		microServiceMap.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		
		return null;
	}

	private boolean isSubscribed(Class<? extends Message> type, MicroService m){
		ConcurrentLinkedQueue<MicroService> queue=messageMap.get(type);
		return queue.contains(m);
	}
}

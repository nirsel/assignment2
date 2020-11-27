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

	private Vector<ConcurrentLinkedQueue<Message>> messageQueues;
	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> microServiceMap;
	private Vector<ConcurrentLinkedQueue<MicroService>> subscribeQueue;
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> messageMap;
	private ConcurrentHashMap<Class<? extends Event>, Future> resultMap;
	private static class MessageBusImplHolder {
		private static MessageBusImpl instance=new MessageBusImpl();
	}

	private MessageBusImpl(){
		messageQueues=new Vector<ConcurrentLinkedQueue<Message>>();
		microServiceMap = new ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>>();
		subscribeQueue = new Vector<ConcurrentLinkedQueue<MicroService>>();
		messageMap = new ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>>();
		resultMap = new ConcurrentHashMap<Class<? extends Event>, Future>();

	}

	public static MessageBusImpl getInstance(){
		return MessageBusImplHolder.instance;

	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future<T> promisedResult= resultMap.get(e);
		promisedResult.resolve(result);
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

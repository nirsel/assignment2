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

	private static MessageBus instance=null; //singleton
	private Vector<ConcurrentLinkedQueue<Message>> VecOfQueues;
	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> microServiceMap;
	private Vector<ConcurrentLinkedQueue<MicroService>> subscribeQueue;
	private ConcurrentHashMap<Message,ConcurrentLinkedQueue<MicroService>> messageMap;

	private MessageBusImpl(){ //todo:constructor

	}

	public static MessageBus getInstance(){ //singleton getInstance
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
		VecOfQueues.add(newQueue);
	}

	@Override
	public void unregister(MicroService m) {
		ConcurrentLinkedQueue<Message> queue=microServiceMap.get(m);
		VecOfQueues.remove(queue);
		microServiceMap.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		
		return null;
	}
}

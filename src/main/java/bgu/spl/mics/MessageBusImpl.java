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


	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> microServiceMap;
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> messageMap;
	private ConcurrentHashMap<Event, Future> resultMap;




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
	/**
	 * Subscribes {@code m} to receive {@link Event}s of type {@code type}.
	 * <p>
	 * @param <T>  The type of the result expected by the completed event.
	 * @param type The type to subscribe to,
	 * @param m    The subscribing micro-service.
	 */

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if (messageMap.containsKey(type)) { // if this type of event already exists
			ConcurrentLinkedQueue<MicroService> queue = messageMap.get(type);
			queue.add(m); // add m to this event queue
			}
		else { synchronized (type.getClass())  { // this type of event doesn't exist
				if (messageMap.containsKey(type)) { // if this type of event already exists
					ConcurrentLinkedQueue<MicroService> queue = messageMap.get(type);
					queue.add(m); // add m to this event queue
				}
				ConcurrentLinkedQueue<MicroService> newQueue = new ConcurrentLinkedQueue<>(); // create a new queue for this type
				newQueue.add(m);
				messageMap.put(type, newQueue); // add the type and its queue to the map
			}
		  }
		}

	/**
	 * Subscribes {@code m} to receive {@link Broadcast}s of type {@code type}.
	 * <p>
	 * @param type 	The type to subscribe to.
	 * @param m    	The subscribing micro-service.
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
			if (messageMap.containsKey(type)) { // if this type of broadcast already exists
				ConcurrentLinkedQueue<MicroService> queue = messageMap.get(type);
				queue.add(m);
			} else { synchronized (type.getClass()) {// this type of broadcast doesn't exist
				if (messageMap.containsKey(type)) { // if this type of broadcast already exists
					ConcurrentLinkedQueue<MicroService> queue = messageMap.get(type);
					queue.add(m);
				}
					ConcurrentLinkedQueue<MicroService> newQueue = new ConcurrentLinkedQueue<>();
					newQueue.add(m);
					messageMap.put(type, newQueue); // add the type and its queue to the map
				}
			}
		}
	/**
	 * Notifies the MessageBus that the event {@code e} is completed and its
	 * result was {@code result}.
	 * When this method is called, the message-bus will resolve the {@link Future}
	 * object associated with {@link Event} {@code e}.
	 * <p>
	 * @param <T>    The type of the result expected by the completed event.
	 * @param e      The completed event.
	 * @param result The resolved result of the completed event.
	 */
	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future<T> promisedResult= resultMap.get(e); // retrieves the promised result associated with the event
		promisedResult.resolve(result); //resolve this event
	}
	/**
	 * Adds the {@link Broadcast} {@code b} to the message queues of all the
	 * micro-services subscribed to {@code b.getClass()}.
	 * <p>
	 * @param b 	The message to added to the queues.
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized(b.getClass()) {
			if (messageMap.containsKey(b.getClass()) && messageMap.get(b.getClass()).size() > 0) { // if this type of BC is registered
				ConcurrentLinkedQueue<MicroService> queue = messageMap.get(b.getClass());
				for (MicroService m : queue) {
					ConcurrentLinkedQueue<Message> mesQueue = microServiceMap.get(m);
					mesQueue.add(b);
					synchronized (mesQueue){mesQueue.notifyAll();}
				}
			}
		}
	}
	/**
	 * Adds the {@link Event} {@code e} to the message queue of one of the
	 * micro-services subscribed to {@code e.getClass()} in a round-robin
	 * fashion. This method should be non-blocking.
	 * <p>
	 * @param <T>    	The type of the result expected by the event and its corresponding future object.
	 * @param e     	The event to add to the queue.
	 * @return {@link Future<T>} object to be resolved once the processing is complete,
	 * 	       null in case no micro-service has subscribed to {@code e.getClass()}.
	 */
	@Override
	public  <T> Future<T> sendEvent(Event<T> e) {
		synchronized (e.getClass()) {
			if (messageMap.containsKey(e.getClass()) && messageMap.get(e.getClass()).size() > 0) {
				ConcurrentLinkedQueue<MicroService> microQueue = messageMap.get(e.getClass());
				MicroService ms = microQueue.poll();
				microQueue.add(ms); //round robin - removes the first and adds him to the tail of the queue
				ConcurrentLinkedQueue<Message> messageQueue = microServiceMap.get(ms);
				messageQueue.add(e);
				Future<T> result = new Future<T>();
				resultMap.put(e, result);
				synchronized (messageQueue){messageQueue.notifyAll();}
				return result;
			}
		}

        return null;
	}
	/**
	 * Allocates a message-queue for the {@link MicroService} {@code m}.
	 * <p>
	 * @param m the micro-service to create a queue for.
	 */
	@Override
	public void register(MicroService m) {
		if (!microServiceMap.containsKey(m)) { // if m is not already in the MicroServiceMap
			microServiceMap.put(m, new ConcurrentLinkedQueue<>());
		}
	}
	/**
	 * Removes the message queue allocated to {@code m} via the call to
	 * {@link #register(bgu.spl.mics.MicroService)} and cleans all references
	 * related to {@code m} in this message-bus. If {@code m} was not
	 * registered, nothing should happen.
	 * <p>
	 * @param m the micro-service to unregister.
	 */
	@Override
	public void unregister(MicroService m) {
			if (microServiceMap.containsKey(m)) {
				microServiceMap.remove(m);
				messageMap.forEach((key, value) -> value.remove(m)); //removes m from every subscription he had
			}

	}
	/**
	 * Using this method, a <b>registered</b> micro-service can take message
	 * from its allocated queue.
	 * This method is blocking meaning that if no messages
	 * are available in the micro-service queue it
	 * should wait until a message becomes available.
	 * The method should throw the {@link IllegalStateException} in the case
	 * where {@code m} was never registered.
	 * <p>
	 * @param m The micro-service requesting to take a message from its message
	 *          queue.
	 * @return The next message in the {@code m}'s queue (blocking).
	 * @throws InterruptedException if interrupted while waiting for a message
	 *                              to became available.
	 */
	@Override
	public  Message awaitMessage(MicroService m) throws InterruptedException{
		if (!microServiceMap.containsKey(m))
			throw new IllegalStateException();
		Queue<Message> messageQueue=microServiceMap.get(m);
		synchronized (messageQueue) {
			while (messageQueue.isEmpty())
				messageQueue.wait();
			return messageQueue.poll();
		}
	}

}

package bgu.spl.mics.application.services;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    long duration;
    CountDownLatch latch;


    public R2D2Microservice(long duration, CountDownLatch latch){
        super("R2D2");
        this.duration=duration;
        this.latch=latch;
    }

    @Override
    protected void initialize() { //todo:complete
        MessageBus bus= MessageBusImpl.getInstance();
        bus.register(this);
        subscribeEvent(DeactivationEvent.class,(Callback)->{
            Thread.sleep(duration);
            sendEvent(new BombDestroyerEvent());});
        subscribeBroadcast(TerminateBroadcast.class,(broad)-> {
            bus.unregister(this);
            terminate();
        });
        latch.countDown();
    }
}

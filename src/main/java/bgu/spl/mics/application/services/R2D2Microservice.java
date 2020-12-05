package bgu.spl.mics.application.services;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
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
    CountDownLatch latch=null;


    public R2D2Microservice(long duration){
        super("R2D2");
        this.duration=duration;
    }

    public void setLatch(CountDownLatch latch){
        this.latch=latch;
    }

    @Override
    protected void initialize() { //todo:complete
        subscribeEvent(DeactivationEvent.class,(event)->{
            Thread.sleep(duration);
            complete(event,true);
            Diary.getInstance().setR2D2Deactivate(System.currentTimeMillis());
            sendEvent(new BombDestroyerEvent());});
        subscribeBroadcast(TerminateBroadcast.class,(broad)-> {
            Diary.getInstance().setR2D2Terminate(System.currentTimeMillis());
            terminate();
        });
        latch.countDown();
    }
}

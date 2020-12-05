package bgu.spl.mics.application.services;


import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    long duration;
    CountDownLatch latch=null;

    public LandoMicroservice(long duration){
        super("R2D2");
        this.duration=duration;
    }

    public void setLatch(CountDownLatch latch){
        this.latch=latch;
    }


    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyerEvent.class,(event)->{
            Thread.sleep(duration); //check
            complete(event,true);
            sendBroadcast(new TerminateBroadcast());
        });
        subscribeBroadcast(TerminateBroadcast.class,(broad)-> {
            Diary.getInstance().setLandoTerminate(System.currentTimeMillis());
            terminate();
        });
        latch.countDown();
    }
}

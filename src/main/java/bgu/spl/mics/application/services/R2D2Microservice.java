package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;

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
    private long duration;
    private CountDownLatch latch=null;


    public R2D2Microservice(long duration){
        super("R2D2");
        this.duration=duration;
    }
    /**
     * Sets the countdown latch data member.
     * @param latch the countdown latch.
     */
    public void setLatch(CountDownLatch latch){
        this.latch=latch;
    }
    /**
     * initialize the relevant information of this MS.
     */
    @Override
    protected void initialize() {
        subscribeEvent(DeactivationEvent.class,(event)-> {
            Thread.sleep(duration);
            complete(event, true);
            Diary.getInstance().setR2D2Deactivate(System.currentTimeMillis());});
            subscribeBroadcast(TerminateBroadcast.class, (broad) -> {
                Diary.getInstance().setR2D2Terminate(System.currentTimeMillis());
                terminate();
            });
            latch.countDown();
        }
}

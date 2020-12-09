package bgu.spl.mics.application.services;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private CountDownLatch latch=null;
    public HanSoloMicroservice() {
        super("Han");
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

        subscribeEvent(AttackEvent.class,(event)->{
            Attack info=event.getInfo();
            List<Integer> resources=info.getSerials();
            Ewoks ewoks=Ewoks.getInstance();
            ewoks.acquireEwoks(resources);
            Thread.sleep(info.getDuration());
            ewoks.releaseEwoks(resources);
            complete(event,true);
            Diary diary=Diary.getInstance();
            diary.setHanSoloFinish(System.currentTimeMillis());
            diary.setTotalAttacks();
        });
        subscribeBroadcast(TerminateBroadcast.class,(broad)-> {
            Diary.getInstance().setHanSoloTerminate(System.currentTimeMillis());
            terminate();
        });
        latch.countDown();
    }
}

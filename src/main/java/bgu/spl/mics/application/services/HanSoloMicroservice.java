package bgu.spl.mics.application.services;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
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
    CountDownLatch latch;
    public HanSoloMicroservice(CountDownLatch latch) {
        super("Han");
        this.latch=latch;
    }


    @Override
    protected void initialize() {

        subscribeEvent(AttackEvent.class,(event)->{
            Attack info=event.getInfo();
            List<Integer> resources=info.getSerials();
            Ewoks ewoks=Ewoks.getInstance();
            ewoks.acquireEwoks(resources);
            Thread.sleep(info.getDuration());
            complete(event,true);
        });
        subscribeBroadcast(TerminateBroadcast.class,(broad)-> {
            terminate();
        });
        latch.countDown();
    }
}

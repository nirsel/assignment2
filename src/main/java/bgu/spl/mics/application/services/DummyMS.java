package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

public class DummyMS extends MicroService {

    protected static Integer num=0; // todo: check

    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public DummyMS(String name) {
        super(name);
    }

    public Integer getNum() {
        return num;
    }


    @Override
    public void initialize() {
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(this);
        subscribeEvent(AttackEvent.class, new DummyMS.CallbackTest2());
        subscribeBroadcast(TerminateBroadcast.class, new DummyMS.CallbackTest());
    }

    public static class CallbackTest implements Callback<TerminateBroadcast>{

        @Override
        public void call(TerminateBroadcast c) throws InterruptedException {
                DummyMS.num = 2;
        }
    }

    public static class CallbackTest2 implements Callback<AttackEvent>{

        @Override
        public void call(AttackEvent c) throws InterruptedException {
            DummyMS.num = 3;
        }
    }
}

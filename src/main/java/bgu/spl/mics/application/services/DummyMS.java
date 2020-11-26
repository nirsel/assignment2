package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.ExampleBroadcast;
import bgu.spl.mics.application.messages.ExampleEvent;
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
    public void initialize() throws InterruptedException {
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(this);
        subscribeEvent(ExampleEvent.class, (c)->{});
        subscribeBroadcast(ExampleBroadcast.class, (c)->{});

    }
    /*
    public static class CallbackTest implements Callback<ExampleBroadcast>{

        @Override
        public void call(ExampleBroadcast c) throws InterruptedException {
                DummyMS.num = 2;
                c.setFlag(true);
        }
    }

    public static class CallbackTest2 implements Callback<AttackEvent>{

        @Override
        public void call(AttackEvent c) throws InterruptedException {
            DummyMS.num = 3;
        }
    } */
}

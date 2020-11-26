package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.ExampleBroadcast;
import bgu.spl.mics.application.messages.ExampleEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

public class DummyMS extends MicroService {


    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public DummyMS(String name) {
        super(name);
    }

    @Override
    public void initialize() throws InterruptedException {
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(this);
        subscribeEvent(ExampleEvent.class, (c)->{});
        subscribeBroadcast(ExampleBroadcast.class, (c)->{});

    }

}

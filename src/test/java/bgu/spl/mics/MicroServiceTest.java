package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MicroServiceTest {

    MicroService m;
    @BeforeEach
    void setUp() {
        m=new HanSoloMicroService();
    }

    @AfterEach
    void tearDown() {
        //m=null;
    }

    @Test
    void testSubscribeEvent() {
        /*
        create some kind of event
        check if not alrealy subscribed?
        subscribe him using message bus
        check if he is subscribed (using message bus method?)
         */
    }

    @Test
    void testSubscribeBroadcast() {
        /*
        create some kind of Broadcast
        check if not alrealy subscribed?
        subscribe him using message bus
        check if he is subscribed (using message bus method?)
         */
    }

    @Test
    void testSendEvent() {
        AttackEvent ev = new AttackEvent();
        MicroService m2 = new HanSoloMicroservice();
        MessageBus bus=MessageBusImpl.getInstance();
        bus.register(m);
        bus.register(m2);
        m2.subscribeEvent(ev.getClass(),(c)->{System.out.println("sendevent test1");});
        m.sendEvent(ev);
        //check if m2 got the event somehow
    }

    @Test
    void testSendBroadcast() {
        /*
        create some kind of broadcast
        send broadcast
        check if all of the registered MS got the broadcast (in their message queue)
         */
        Broadcast broad = new TerminateBroadcast();
        MicroService m2 = new HanSoloMicroservice();
        MessageBus bus=MessageBusImpl.getInstance();
        bus.register(m);
        bus.register(m2);
        m2.subscribeBroadcast(broad.getClass(),(c)->{System.out.println("sendbroadcast test1");});
        m.sendBroadcast(broad);
        //check if m2 got the broadcast somehow
    }

    @Test
    void testComplete() {
    }

    @Test
    void testInitialize() {
    }

    @Test
    void testTerminate() {
    }

    @Test
    void testGetName() {
        assertEquals(m.getName(),"Han");
    }

    @Test
    void testRun() {
    }
}
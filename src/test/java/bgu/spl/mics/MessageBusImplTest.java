package bgu.spl.mics;

import bgu.spl.mics.application.AttackEventCallback;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.DummyMS;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    MessageBusImpl bus;

    @BeforeEach
    void setUp() {
        bus=MessageBusImpl.getInstance();

    }

    @AfterEach
    void tearDown() { // todo ?
        //destroying all fields?

    }

    @Test
    void testSubscribeEvent() {
         /*
        we are not checking this method since the test "testSendEvent" is calling "SubscribeEvent"
        of MicroService and the latter is calling "SubscribeEvent" of MessageBus. Therefore, "testSendEvent"
        checks the correctness of this method as well. Also, testing this method is duplicating "testSendEvent" code".
        */

    }

    @Test
    void testSubscribeBroadcast() {
        /*
        we are not checking this method since the test "testSendBroadcast" is calling "SubscribeBroadcast"
        of MicroService and the latter is calling "SubscribeBroadCast" of MessageBus. Therefore, "testSendBroadcast"
        checks the correctness of this method as well. Also, testing this method is duplicating "testSendBroadcast" code".
        */
    }

    @Test
    void testComplete() {
        DummyMS m1 = new DummyMS("solo");
        AttackEvent ev1 =  new AttackEvent();
        m1.initialize();
        bus.awaitMessage(m1);
    }

    @Test
    void testSendBroadcast() throws InterruptedException {
        DummyMS m1 = new DummyMS("solo");
        DummyMS m2 = new DummyMS("han");
        TerminateBroadcast broad = new TerminateBroadcast();
        bus.sendBroadcast(broad);
        m1.initialize();
        m2.initialize();
        bus.awaitMessage(m1);
        bus.awaitMessage(m2); // assuming there is  a message in the queue according to forum
        assertEquals(m1.getNum(), 2);
        assertEquals(m2.getNum(), 2);
    }

    @Test
    void testSendEvent() throws InterruptedException {

        DummyMS m1 = new DummyMS("han");
        DummyMS m2 = new DummyMS("solo");
        AttackEvent ev1 =  new AttackEvent();
        AttackEvent ev2 =  new AttackEvent();
        bus.sendEvent(ev1);
        bus.sendEvent(ev2);
        m1.initialize();
        m2.initialize();
        bus.awaitMessage(m1);
        bus.awaitMessage(m2);
        assertEquals(m1.getNum(), 3);
        assertEquals(m2.getNum(), 3);
    }

    @Test
    void testRegister() {
        // don't need to check this function according to the forum.

    }

    @Test
    void testUnregister() {
        // don't need to check this function according to the forum.
    }

    @Test
    void testAwaitMessage() { // todo

    }


    @Test
    void testGetInstance() { //todo: check
        assertEquals(bus.getClass(),MessageBusImpl.class.getClass());
    }
}
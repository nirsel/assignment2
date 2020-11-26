package bgu.spl.mics;

import bgu.spl.mics.application.AttackEventCallback;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.ExampleBroadcast;
import bgu.spl.mics.application.messages.ExampleEvent;
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
        bus = MessageBusImpl.getInstance();

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
    void testComplete() throws InterruptedException {
        DummyMS m1 = new DummyMS("solo");
        ExampleEvent ev1 = new ExampleEvent();
        m1.initialize();
        DummyMS m2 = new DummyMS("luke");
        Future<Boolean> result = m2.sendEvent(ev1);
        m2.complete(ev1, result.get());
        assertTrue(result.isDone());
        assertNotEquals(null, result.get());

    }

    @Test
    void testSendBroadcast() throws InterruptedException {
        DummyMS m1 = new DummyMS("solo");
        DummyMS m2 = new DummyMS("han");
        DummyMS m3 = new DummyMS("yoda");
        Broadcast broad = new ExampleBroadcast();
        m1.initialize();
        m2.initialize();
        m3.sendBroadcast(broad);
        Message bro1 = bus.awaitMessage(m1);
        Message bro2 = bus.awaitMessage(m2);
        assertEquals(broad, bro1);
        assertEquals(broad, bro2);
    }

    @Test
    void testSendEvent() throws InterruptedException {

        DummyMS m1 = new DummyMS("han");
        ExampleEvent ev1 = new ExampleEvent();
        m1.initialize();
        DummyMS m2 = new DummyMS("han");
        m2.sendEvent(ev1);
        Message ev2 = bus.awaitMessage(m1);
        assertEquals(ev1, ev2);
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
    void testAwaitMessage() throws InterruptedException { // todo
        DummyMS m1 = new DummyMS("han");
        ExampleEvent ev1 = new ExampleEvent();
        m1.initialize();
        DummyMS m2 = new DummyMS("han");
        m2.sendEvent(ev1);
        Message ev2 = bus.awaitMessage(m1);
        assertEquals(ev1, ev2);
    }

}
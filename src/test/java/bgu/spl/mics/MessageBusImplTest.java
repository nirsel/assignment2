package bgu.spl.mics;

import bgu.spl.mics.application.messages.ExampleBroadcast;
import bgu.spl.mics.application.messages.ExampleEvent;
import bgu.spl.mics.application.services.DummyMS;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    MessageBusImpl bus;
    DummyMS m1;
    DummyMS m2;
    DummyMS m3;
    @BeforeEach
    void setUp() {
        bus = MessageBusImpl.getInstance();
        m1 = new DummyMS("solo");
        m2 = new DummyMS("han");
        m3 = new DummyMS("yoda");
        m1.initialize(); //registers and subscribes the microservice to exampleEvent and exampleBroadcast
        m2.initialize();
    }

    @AfterEach
    void tearDown() {
        bus.unregister(m1);
        bus.unregister(m2);
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
    void testComplete()  {

        ExampleEvent ev1 = new ExampleEvent();
        Future<Boolean> f =m2.sendEvent(ev1); //m2 sends the event ev1, f holds the future object of that event from the resultMap
        m1.complete(ev1,true); // m1 completes the event using complete of MessageBus. awaitMessage is not needed here because the callback function does nothing in the example
        assertTrue(f.isDone()); //we expect the event to be resolved
        assertEquals(true,f.get());

    }

    @Test
    void testSendBroadcast() throws InterruptedException {
        Broadcast broad = new ExampleBroadcast();
        m3.sendBroadcast(broad); //m3 sends ExampleBroadcast using sendBroadcast of MessageBus
        Message bro1 = bus.awaitMessage(m1);
        Message bro2 = bus.awaitMessage(m2);
        assertEquals(broad, bro1); //we expect both microservices to get the broadcast from m3
        assertEquals(broad, bro2);
    }

    @Test
    void testSendEvent() throws InterruptedException {

        ExampleEvent ev1 = new ExampleEvent();
        m2.sendEvent(ev1); //m2 sends ExampleEvent using sendEvent of MessageBus
        Message ev2 = bus.awaitMessage(m1); //expect m1 to get ev1
        assertEquals(ev2, ev1); //expect ev2 to be equal to ev1
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
    void testAwaitMessage() throws InterruptedException {
        ExampleEvent ev1 = new ExampleEvent();
        m2.sendEvent(ev1); //m2 sends ExampleEvent using sendEvent of MessageBus
        Message ev2 = bus.awaitMessage(m1); //m1 should get ev1
        assertEquals(ev2, ev1); //expect ev1 and ev2 to be equals, meaning that m1 got the message using await message
    }

}
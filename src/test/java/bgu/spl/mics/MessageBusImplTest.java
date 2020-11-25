package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
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
    void tearDown() {
        //destroying all fields?

    }

    @Test
    void testSubscribeEvent() throws InterruptedException {
        /*
        Create new microservice, register him to event
        check if he is subscribed
         */
        MicroService m1 = new HanSoloMicroservice();
        bus.register(m1);
        AttackEvent ev = new AttackEvent();
        bus.subscribeEvent(ev.getClass(),m1);
        bus.sendEvent(ev);
        bus.awaitMessage(m1);
        //check if m1 got the event somehow

    }

    @Test
    void testSubscribeBroadcast() {
        /*
        create new microservice, register him to broadcast
        check if he is subscribed
         */
        MicroService m1 = new HanSoloMicroservice();
        bus.register(m1);
        TerminateBroadcast broad = new TerminateBroadcast();
        m1.subscribeBroadcast(broad.getClass(), (c) -> {System.out.println("hey, broadcast test1");});
        bus.subscribeBroadcast(broad.getClass(),m1);
    }

    @Test
    void testComplete() {
    }

    @Test
    void testSendBroadcast() throws InterruptedException {
        /*
        create microservice, send new broadcast
        check if all the relevant microservices got the broadcast (it is in their message queue)
         */
        MicroService m1 = new HanSoloMicroservice();
        MicroService m2 = new HanSoloMicroservice();
        bus.register(m1);
        bus.register(m2);
        TerminateBroadcast broad = new TerminateBroadcast();
        m1.subscribeBroadcast(broad.getClass(), (c) -> {System.out.println("hey, broadcast test1");});
        m2.subscribeBroadcast(broad.getClass(), (c) -> {System.out.println("hey, broadcast test1");});
        bus.sendBroadcast(broad);
        bus.awaitMessage(m1);
        bus.awaitMessage(m2);
        //check if m1 and m2 got the broadcast
    }

    @Test
    void testSendEvent() throws InterruptedException {
        /*

         */
        MicroService m1 = new HanSoloMicroservice();
        MicroService m2 = new HanSoloMicroservice();
        bus.register(m1);
        bus.register(m2);
        AttackEvent ev =  new AttackEvent();
        m1.subscribeEvent(ev.getClass(), (c) -> {System.out.println("hey, event test1");});
        m2.subscribeEvent(ev.getClass(), (c) -> {System.out.println("hey, event test2");});
        bus.sendEvent(ev);
        bus.sendEvent(ev);
        bus.awaitMessage(m1);
        bus.awaitMessage(m2);
    }

    @Test
    void testRegister() {
        /*
        create new microservice
        check if he is not already registered (exception?)
        register
        check if he is registered
         */

    }

    @Test
    void testUnregister() {
        /*
        create new microservice
        register him
        check if he is registered
        unregister
        check if he is unregistered
         */
    }

    @Test
    void testAwaitMessage() {
        MicroService m1 = new HanSoloMicroservice();
        try {
            bus.awaitMessage(m1);
            assertTrue(false);                    }
        catch(Exception e) {
            bus.register(m1);
            //need to subscribe him, send him message and check if he got it
        }
    }


    @Test
    void testGetInstance() {
        assertEquals(bus.getClass(),MessageBusImpl.class.getClass());
    }
}
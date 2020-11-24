package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    Vector<ConcurrentLinkedQueue<Message>> VecOfQueues;

    @BeforeEach
    void setUp() {
        VecOfQueues=new Vector<ConcurrentLinkedQueue<Message>>();
    }

    @AfterEach
    void tearDown() {
        //destroying all fields?
    }

    @Test
    void testSubscribeEvent() {
        /*
        Create new microservice, register him to event
        check if he is subscribed
         */
    }

    @Test
    void testSubscribeBroadcast() {
        /*
        create new microservice, register him to broadcast
        check if he is subscribed
         */
    }

    @Test
    void testComplete() {
    }

    @Test
    void testSendBroadcast() {
        /*
        create microservice, send new broadcast
        check if all the relevant microservices got the broadcast (it is in their message queue)
         */
    }

    @Test
    void testSendEvent() {
        /*

         */
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
    }


    @Test
    void getInstance() {
    }
}
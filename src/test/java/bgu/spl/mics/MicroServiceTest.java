package bgu.spl.mics;

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
    }

    @Test
    void testSendBroadcast() {
        /*
        create some kind of broadcast
        send broadcast
        check if all of the registered MS got the broadcast (in their message queue)
         */
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
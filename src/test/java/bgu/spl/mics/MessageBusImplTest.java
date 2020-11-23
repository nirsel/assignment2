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
    }

    @Test
    void testSubscribeEvent() {
    }

    @Test
    void testSubscribeBroadcast() {
    }

    @Test
    void testComplete() {
    }

    @Test
    void testSendBroadcast() {
    }

    @Test
    void testSendEvent() {
    }

    @Test
    void testRegister() {
        MicroService ms=new LeiaMicroService("test");

    }

    @Test
    void testUnregister() {
    }

    @Test
    void testAwaitMessage() {
    }
}
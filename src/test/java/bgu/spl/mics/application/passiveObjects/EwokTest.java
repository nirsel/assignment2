package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    Ewok ewok;
    @BeforeEach
    void setUp() {
        ewok=new Ewok(0);
    }

    @AfterEach
    void tearDown() { //todo: check if needed
    }

    @Test
    void testAcquire() {
        assertTrue(ewok.isAvailable());
        ewok.acquire();
        assertFalse(ewok.isAvailable());
    }

    @Test
    void testRelease() {
        assertFalse(ewok.isAvailable());
        ewok.acquire();
        assertTrue(ewok.isAvailable());
    }

    @Test
    void testisAvailable() {
        assertEquals(true,ewok.isAvailable());
        ewok.acquire();
        assertEquals(false,ewok.isAvailable());
        ewok.release();
        assertEquals(true,ewok.isAvailable());
    }
}
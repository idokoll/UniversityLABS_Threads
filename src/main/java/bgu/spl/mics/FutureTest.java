package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FutureTest {
    private static Future fut;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        fut = new Future();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void get() {
        int res = 7;
        fut.resolve(res);
        assertEquals(res, fut.get());
    }

    @org.junit.jupiter.api.Test
    void resolve() {
        int res = 7;
        fut.resolve(res);
        assertEquals(res, fut.get());

    }

    @org.junit.jupiter.api.Test
    void isDone() {
        assertTrue(!fut.isDone());
        int res = 7;
        fut.resolve(res);
        assertTrue(fut.isDone());
    }

    @org.junit.jupiter.api.Test
    void testGet() {
        int res = 7;
        fut.resolve(res);
        assertEquals(res, fut.get(10, TimeUnit.SECONDS));
    }
}
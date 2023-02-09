package bgu.spl.mics;

import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.services.ExampleMessageSenderService;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    MessageBusImpl msg;
    Student tests;
    StudentService testservice;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        msg = MessageBusImpl.getInstance();
        tests = new Student();
        testservice = new StudentService("manyak",tests);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void subscribeEvent() {
        ExampleEvent e = new ExampleEvent("name");
        msg.register(testservice);
        msg.subscribeEvent(e.getClass(),testservice);
        assertNotNull(msg.getSubscribedEventMsMap().get(e.getClass()));

    }

    @org.junit.jupiter.api.Test
    void subscribeBroadcast() throws InterruptedException {
        ExampleBroadcast b = new ExampleBroadcast("1");
        msg.register(testservice);
        msg.subscribeBroadcast(b.getClass(),testservice);
        msg.sendBroadcast(b);
        assertEquals(msg.awaitMessage(testservice).getClass(),b.getClass());
    }

    @org.junit.jupiter.api.Test
    void complete() {
        ExampleEvent e = new ExampleEvent("name");
        String res="result";
        msg.register(testservice);
        msg.subscribeEvent(e.getClass(),testservice);
        Future<String> f = msg.sendEvent(e);
        msg.complete(e,res);
        assertTrue(f.isDone());
        assertEquals(f.get(),res);
    }

    @org.junit.jupiter.api.Test
    void sendBroadcast() throws InterruptedException {
        ExampleBroadcast b = new ExampleBroadcast("1");
        msg.register(testservice);
        msg.subscribeBroadcast(b.getClass(),testservice);
        msg.sendBroadcast(b);
        assertEquals(msg.awaitMessage(testservice).getClass(),b.getClass());
    }

    @org.junit.jupiter.api.Test
    void sendEvent() {
        ExampleEvent e = new ExampleEvent("name");
        msg.register(testservice);
        msg.subscribeEvent(e.getClass(),testservice);
        msg.sendEvent(e);
        assertEquals(msg.getMsQueueMap().get(testservice).size(),1);
    }

    @org.junit.jupiter.api.Test
    void register() {
        msg.register(testservice);
        assertNotNull(msg.getMsQueueMap().get(testservice));
    }

    @org.junit.jupiter.api.Test
    void unregister() {
        ExampleEvent e = new ExampleEvent("name");
        msg.register(testservice);
        msg.subscribeEvent(e.getClass(), testservice);
        msg.unregister(testservice);
        assertNull(msg.getMsQueueMap().get(testservice));
    }

    @org.junit.jupiter.api.Test
    void awaitMessage() throws InterruptedException {
        ExampleBroadcast b = new ExampleBroadcast("1");
        msg.register(testservice);
        msg.subscribeBroadcast(b.getClass(),testservice);
        msg.sendBroadcast(b);
        assertEquals(msg.awaitMessage(testservice).getClass(),b.getClass());
    }
}
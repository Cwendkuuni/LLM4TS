package com.mentorgen.tools.profile.runtime;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProfileTest {

    private static Profile _profile;

    @BeforeClass
    public static void initProfiler() {
        Profile.initProfiler();
    }

    @AfterClass
    public static void shutdownProfiler() {
        Profile.shutdown();
    }

    @Test
    public void testInitAndShutdown() {
        Profile.init();
        assertEquals(0, Profile.getThreadTotalTime(Thread.currentThread().getId()));
        Profile.shutdown();
        assertNull(Profile.getThreadTotalTime(Thread.currentThread().getId()));
        Profile.init();
    }

    @Test
    public void testThreadsAndInteractions() {
        Profile.init();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            // some code here
            Profile.end("TestClass1", "testMethod1");
        });
        thread1.start();
        Thread thread2 = new Thread(() -> {
            Profile.start("TestClass2", "testMethod2");
            // some code here
            Profile.end("TestClass2", "testMethod2");
        });
        thread2.start();

        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
        assertNotNull(threads.get(0));
        assertNotNull(threads.get(1));

        List<Frame> interactionsThread1 = Profile.interactions(threads.get(0));
        List<Frame> interactionsThread2 = Profile.interactions(threads.get(1));
        assertEquals(2, interactionsThread1.size());
        assertEquals(2, interactionsThread2.size());

        Profile.stop();
        Profile.clear();
    }

    @Test(expected = AssertionError.class)
    public void testInvalidMethodName() {
        Profile.init();
        Profile.start("TestClass", "invalid method name");
        Profile.end("TestClass", "testMethod");
    }

    @Test(expected = AssertionError.class)
    public void testEmptyMethodName() {
        Profile.init();
        Profile.start("TestClass", "");
        Profile.end("TestClass", "");
    }

    @Test
    public void testStartAndEnd() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Thread.yield();
        Thread.yield();
        Profile.end("TestClass", "testMethod");

        List<Frame> frameList = Profile.frameList();
        assertEquals(1, frameList.size());

        Frame frame = frameList.get(0);
        assertEquals("TestClass", frame.getClassName());
        assertEquals("testMethod", frame.getMethodName());

        Profile.stop();
        Profile.clear();
    }

    @Test
    public void testBeginWaitAndEndWait() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Thread.yield();
        Thread.yield();
        Profile.beginWait("TestClass", "waitMethod");
        Thread.yield();
        Thread.yield();
        Profile.endWait("TestClass", "waitMethod");

        List<Frame> frameList = Profile.frameList();
        assertEquals(2, frameList.size());

        Frame frame1 = frameList.get(0);
        assertEquals("TestClass", frame1.getClassName());
        assertEquals("testMethod", frame1.getMethodName());

        Frame frame2 = frameList.get(1);
        assertEquals("TestClass", frame2.getClassName());
        assertEquals("waitMethod", frame2.getMethodName());

        Profile.stop();
        Profile.clear();
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        new TestClass();
        Profile.end("TestClass", "testMethod");

        List<ClassAllocation> allocations = Profile.allocations();
        assertEquals(1, allocations.size());
        assertEquals("TestClass", allocations.get(0).getClassName());

        Profile.stop();
        Profile.clear();
    }

    @Test
    public void testMultipleAllocations() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        new TestClass();
        new TestClass();
        Profile.end("TestClass", "testMethod");

        List<ClassAllocation> allocations = Profile.allocations();
        assertEquals(1, allocations.size());
        assertEquals(2, allocations.get(0).getAllocCount());

        Profile.stop();
        Profile.clear();
    }

    static class TestClass {
        TestClass() {
            Profile.alloc("TestClass");
        }
    }
}
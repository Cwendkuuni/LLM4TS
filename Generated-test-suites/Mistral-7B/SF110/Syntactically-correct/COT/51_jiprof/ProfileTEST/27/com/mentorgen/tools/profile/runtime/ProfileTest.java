package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {
    // Test initialization and cleanup methods

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
        // Add more assertions as needed
    }

    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadDictionary);
        // Add more assertions as needed
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.threads();
        Profile.clear();
        assertEquals(0, Profile.threads().size());
    }

    // Test thread-related methods

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            Profile.end("TestClass1", "testMethod1");
        });
        thread1.start();
        Thread thread2 = new Thread(() -> {
            Profile.start("TestClass2", "testMethod2");
            Profile.end("TestClass2", "testMethod2");
        });
        thread2.start();
        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
        // Add more assertions as needed
    }

    @Test(expected = IllegalStateException.class)
    public void testThreadsNoProfile() {
        Profile.clear();
        Profile.threads();
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            Profile.end("TestClass1", "testMethod1");
        });
        thread1.start();
        Long threadId1 = thread1.getId();
        List<Frame> interactions = Profile.interactions(threadId1);
        assertEquals(1, interactions.size());
        // Add more assertions as needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInteractionsInvalidThreadId() {
        Profile.init();
        Profile.start();
        List<Frame> interactions = Profile.interactions(-1L);
    }

    // Test framework list methods

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            Profile.end("TestClass1", "testMethod1");
        });
        thread1.start();
        Thread thread2 = new Thread(() -> {
            Profile.start("TestClass2", "testMethod2");
            Profile.end("TestClass2", "testMethod2");
        });
        thread2.start();
        List<Frame> frameList = Profile.frameList();
        assertEquals(4, frameList.size());
        // Add more assertions as needed
    }

    // Test allocation methods

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        Profile.alloc("TestClass1");
        Profile.alloc("TestClass1");
        List<ClassAllocation> allocations = new ArrayList<>(Profile.allocations());
        assertEquals(1, allocations.size());
        // Add more assertions as needed
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            long start = System.nanoTime();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Do nothing
            }
            Profile.end("TestClass1", "testMethod1");
        });
        thread1.start();
        long threadId1 = thread1.getId();
        long threadTotalTime = Profile.getThreadTotalTime(threadId1);
        assertTrue(threadTotalTime > 0);
        // Add more assertions as needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetThreadTotalTimeInvalidThreadId() {
        Profile.init();
        Profile.start();
        long threadTotalTime = Profile.getThreadTotalTime(-1L);
    }

    // Test sorting methods

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // Do nothing
            }
            Profile.end("TestClass1", "testMethod1");
        });
        thread1.start();
        Thread thread2 = new Thread(() -> {
            Profile.start("TestClass2", "testMethod2");
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                // Do nothing
            }
            Profile.end("TestClass2", "testMethod2");
        });
        thread2.start();
        Comparator<Frame> comparator = (f1, f2) -> f1.getBeginTime() - f2.getBeginTime();
        Profile.sortFrameList(comparator);
        List<Frame> frameList = Profile.frameList();
        Frame frame1 = frameList.get(0);
        Frame frame2 = frameList.get(1);
        long delta = frame1.getBeginTime() - frame2.getBeginTime();
        assertTrue(delta < 0);
    }

    // Test start, end, beginWait, and endWait methods

    @Test
    public void testStartEnd() {
        Profile.init();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        long start = System.nanoTime();
        Profile.start(className, methodName);
        long end = System.nanoTime();
        long timeElapsed = end - start;
        assertTrue(timeElapsed > 0);
        Profile.end(className, methodName);
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        long start = System.nanoTime();
        Profile.beginWait(className, methodName);
        Thread.yield();
        long end = System.nanoTime();
        long timeElapsed = end - start;
        assertTrue(timeElapsed > 0);
        Profile.endWait(className, methodName);
    }

    @Test
    public void testUnwind() {
        Profile.init();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        String exception = "TestException";
        long start = System.nanoTime();
        try {
            throw new TestException();
        } finally {
            Profile.unwind(className, methodName, exception);
        }
        // Add assertions as needed to check the unwinding behavior
    }

    @Test(expected = TestException.class)
    public void testTestException() throws TestException {
        Profile.init();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        throw new TestException();
    }

    // Test alloc method

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start();
        String className = "TestClass";
        Profile.alloc(className);
        String className2 = "TestClass2";
        Profile.alloc(className2);
        Map<String, ClassAllocation> allocations = Profile._allocList;
        ClassAllocation ca1 = allocations.get(className);
        ClassAllocation ca2 = allocations.get(className2);
        assertEquals(2, allocations.size());
        assertEquals(2, ca1.getAllocCount());
        assertEquals(1, ca2.getAllocCount());
    }

    // Test run method

    @Test
    public void testRun() {
        Profile.init();
        Profile.start();
        Profile.run();
        // Add assertions as needed to check the shutdown behavior
    }
}

class TestException extends Exception {
    public TestException() {
        super();
    }
}
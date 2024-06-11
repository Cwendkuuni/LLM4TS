package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.mentorgen.tools.profile.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class ProfileTest {

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
        if (Profile._controllerThread != null) {
            assertEquals(State.RUNNING, Profile._controller.getState());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testInitProfilerTwice() {
        Profile.initProfiler();
        Profile.initProfiler();
    }

    @Test
    public void testClear() {
        Profile.initProfiler();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertEmpty(Profile.interactions(Thread.currentThread().getId()));
    }

    @Test
    public void testStart() {
        Profile.initProfiler();
        Profile.start();
        assertTrue(Profile._controller.isProfiling());
    }

    @Test
    public void testStop() {
        Profile.initProfiler();
        Profile.start();
        Profile.stop();
        assertFalse(Profile._controller.isProfiling());
    }

    @Test
    public void testSetFileName() {
        Profile.initProfiler();
        String fileName = "test.txt";
        Profile.setFileName(fileName);
        assertEquals(fileName, Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() throws Exception {
        Profile.initProfiler();
        Profile.start();
        Thread.sleep(100); // Give the profiler some time to run
        Profile.shutdown();
        assertTrue(Profile._controller.isClosed());
    }

    @Test
    public void testThreads() {
        Profile.initProfiler();
        Profile.start();
        Thread newThread = new Thread(() -> {});
        newThread.start();
        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
        assertTrue(threads.contains(Thread.currentThread().getId()));
        assertTrue(threads.contains(newThread.getId()));
    }

    @Test
    public void testInteractions() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
        assertEquals(1, interactions.size());
        Frame frame = interactions.get(0);
        assertEquals(className, frame.getClassName());
        assertEquals(methodName, frame.getMethodName());
    }

    @Test
    public void testFrameList() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName1 = "testMethod1";
        String methodName2 = "testMethod2";
        Profile.start(className, methodName1);
        Profile.end(className, methodName1);
        Profile.start(className, methodName2);
        Profile.end(className, methodName2);
        List<Frame> frameList = Profile.frameList();
        assertEquals(4, frameList.size());
    }

    @Test
    public void testAllocations() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        Profile.alloc(className);
        List<ClassAllocation> allocations = Profile.allocations();
        assertEquals(1, allocations.size());
        ClassAllocation ca = allocations.get(0);
        assertEquals(className, ca.getClassName());
        assertEquals(1, ca.getAllocationCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        long threadId = Thread.currentThread().getId();
        long startTime = System.nanoTime();
        Profile.start(className, "testMethod");
        Profile.end(className, "testMethod");
        long endTime = System.nanoTime();
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(endTime - startTime, threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName1 = "testMethod1";
        String methodName2 = "testMethod2";
        String methodName3 = "testMethod3";
        Collections.sort(Profile.frameList());
        Frame frame1 = Profile.frameList().get(0);
        Frame frame2 = Profile.frameList().get(1);
        Frame frame3 = Profile.frameList().get(2);
        assertEquals(methodName1, frame1.getMethodName());
        assertEquals(methodName2, frame2.getMethodName());
        assertEquals(methodName3, frame3.getMethodName());
    }

    @Test
    public void testStartEnd() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        long startTime = System.nanoTime();
        Profile.start(className, methodName);
        long endTime = System.nanoTime();
        Profile.end(className, methodName);
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertEquals(startTime, frame.getBeginTime());
        assertEquals(endTime, frame.getEndTime());
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        long startTime = System.nanoTime();
        Profile.beginWait(className, methodName);
        Profile.endWait(className, methodName);
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertEquals(startTime, frame.getBeginWaitTime());
        assertEquals(System.nanoTime(), frame.getEndWaitTime());
    }

    @Test
    public void testUnwind() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        String exception = "TestException";
        long startTime = System.nanoTime();
        Profile.start(className, methodName);
        try {
            throw new TestException("Test");
        } catch (TestException e) {
            Profile.unwind(className, methodName, exception.getClass().getName());
        }
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertEquals(startTime, frame.getBeginTime());
        assertEquals(System.nanoTime(), frame.getEndTime());
    }

    @Test
    public void testAlloc() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        long threadId = Thread.currentThread().getId();
        Profile.alloc(className);
        Profile.alloc(className);
        ClassAllocation ca = Profile._allocList.get(className);
        assertEquals(2, ca.getAllocationCount());
    }

    @Test
    public void testRun() throws Exception {
        Profile.initProfiler();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Runnable runnable = () -> {
            try {
                Profile.start();
                Thread.sleep(100);
                Profile.end("TestClass", "testMethod");
            } finally {
                Profile.shutdown();
            }
        };
        executor.submit(runnable);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        List<Frame> frameList = Profile.frameList();
        assertEquals(1, frameList.size());
    }
}
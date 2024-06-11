package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ThreadLocalRandom;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.ThreadDictionary;

public class ProfileTest {

    private static Profile underTest;
    private static ThreadDictionary threadDictionary;
    private static List<Frame> frameList;

    @BeforeClass
    public static void setUpClass() {
        underTest = new Profile();
        threadDictionary = new ThreadDictionary();
        frameList = new ArrayList<>();
    }

    @AfterClass
    public static void tearDownClass() {
        underTest = null;
        threadDictionary = null;
        frameList = null;
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(underTest.isInstanceOf(Profile.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testInitProfilerTwice() {
        Profile.initProfiler();
        Profile.initProfiler();
    }

    @Test
    public void testInit() {
        Profile.init();
        assertEquals(1001, threadDictionary.size());
        assertEquals(0, frameList.size());
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertTrue(threadDictionary.isEmpty());
        assertTrue(frameList.isEmpty());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        assertTrue(underTest._controller.isStarted());
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        assertFalse(underTest._controller.isStarted());
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        String fileName = "test.txt";
        Profile.setFileName(fileName);
        assertEquals(fileName, underTest._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        assertFalse(underTest._controller.isStarted());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        assertTrue(Profile.threads().contains(threadId));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Frame frame = new Frame(null, new Method("TestClass", "testMethod"), threadId);
        frameList.add(frame);
        threadDictionary.add(threadId, frame);
        List<Frame> interactions = Profile.interactions(threadId);
        assertEquals(1, interactions.size());
        assertEquals(frame, interactions.get(0));
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), threadId);
        frameList.add(frame1);
        threadDictionary.add(threadId, frame1);
        Profile.start("TestClass2", "testMethod2");
        Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), threadId);
        frameList.add(frame2);
        threadDictionary.add(threadId, frame2);
        List<Frame> frameList = Profile.frameList();
        assertEquals(2, frameList.size());
        assertTrue(frameList.contains(frame1));
        assertTrue(frameList.contains(frame2));
    }

    @Test
    public void testAllocations() {
        Profile.init();
        ClassAllocation allocation = new ClassAllocation("TestClass");
        Profile.alloc("TestClass");
        assertEquals(1, allocation.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        Frame frame = new Frame(null, new Method("TestClass", "testMethod"), threadId);
        frameList.add(frame);
        threadDictionary.add(threadId, frame);
        long startTime = System.nanoTime();
        Thread.sleep(100);
        long endTime = System.nanoTime();
        frame.setBeginTime(startTime);
        frame.setEndTime(endTime);
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(endTime - startTime, threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), threadId);
        frameList.add(frame1);
        Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), threadId);
        frameList.add(frame2);
        Collections.sort(frameList, Frame.COMPARATOR);
        List<Frame> sortedFrameList = Profile.frameList();
        assertEquals(frame1, sortedFrameList.get(0));
        assertEquals(frame2, sortedFrameList.get(1));
    }

    @Test
    public void testStartWithException() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        try {
            throw new RuntimeException("Test Exception");
        } catch (Exception e) {
            Profile.start(className, methodName);
            Profile.end(className, methodName, e.getClass().getName());
        }
        Frame frame = findFrame(threadId, className, methodName);
        assertEquals("Test Exception", frame.getException());
    }

    @Test
    public void testStartWithMultipleExceptions() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        try {
            throw new RuntimeException("Test Exception 1");
            throw new IOException("Test Exception 2");
        } catch (Exception e1) {
            Profile.start(className, methodName);
            try {
                Profile.start(className, methodName);
                fail("Expected an exception");
            } catch (IllegalStateException e2) {
                Profile.end(className, methodName, e1.getClass().getName());
                Frame frame = findFrame(threadId, className, methodName);
                assertEquals("Test Exception 1", frame.getException());
                assertEquals(2, frame.getExceptionCount());
            }
        }
    }

    @Test
    public void testBeginWaitAndEndWait() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        long startTime = System.nanoTime();
        Profile.beginWait(className, methodName);
        Thread.sleep(100);
        Profile.endWait(className, methodName);
        Frame frame = findFrame(threadId, className, methodName);
        long waitingTime = frame.getWaitingTime();
        assertEquals(endTime - startTime, waitingTime);
    }

    @Test
    public void testUnwindWithException() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        try {
            throw new RuntimeException("Test Exception");
        } catch (Exception e) {
            Profile.start(className, methodName);
            Profile.end(className, methodName, e.getClass().getName());
            Profile.unwind(className, methodName, e.getClass().getName());
        }
        Frame frame = findFrame(threadId, className, methodName);
        assertTrue(frame.isExceptionFrame());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        String className = "TestClass";
        for (int i = 0; i < 1000; i++) {
            Profile.alloc(className);
        }
        ClassAllocation allocation = Profile._allocList.get(className);
        assertEquals(1000, allocation.getAllocCount());
    }

    @Test
    public void testRun() {
        Profile.init();
        Profile.start();
        underTest.run();
        assertTrue(underTest._controller.isClosed());
    }

    private Frame findFrame(long threadId, String className, String methodName) {
        for (Frame frame : frameList) {
            if (frame.getThreadId() == threadId && frame.getClassName().equals(className) && frame.getMethodName().equals(methodName)) {
                return frame;
            }
        }
        return null;
    }
}
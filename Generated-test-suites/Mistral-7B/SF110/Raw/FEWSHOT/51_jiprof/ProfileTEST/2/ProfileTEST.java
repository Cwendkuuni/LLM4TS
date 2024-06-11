package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.*;
import java.io.*;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
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
        assertNull(Profile._controllerThread);
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testStart() {
        Profile.initProfiler();
        Profile.start();
        assertTrue(Profile._controller != null && Profile._controllerThread != null && Profile._controllerThread.isAlive());
    }

    @Test
    public void testStop() {
        Profile.initProfiler();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.stop();
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        Profile.initProfiler();
        Profile.setFileName("output.txt");
        assertEquals("output.txt", Profile._controller.getOutputFileName());
    }

    @Test(expected = NullPointerException.class)
    public void testShutdownNullController() {
        Profile.initProfiler();
        Profile._controller = null;
        Profile.shutdown();
    }

    @Test
    public void testThreads() {
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(() -> {});
        thread.start();
        List<Long> threads = Profile.threads();
        assertNotNull(threads);
        assertTrue(threads.contains(Thread.currentThread().getId()));
        assertTrue(threads.contains(thread.getId()));
        assertEquals(2, threads.size());
    }

    @Test
    public void testInteractions() {
        Profile.initProfiler();
        Profile.start("TestClass", "testMethod");
        Frame frame1 = new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId());
        Frame frame2 = new Frame(frame1, new Method("TestClass2", "testMethod2"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame1);
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame2);
        List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
        assertNotNull(interactions);
        assertEquals(2, interactions.size());
        assertEquals(frame2, interactions.get(1));
    }

    @Test
    public void testFrameList() {
        Profile.initProfiler();
        Profile.start("TestClass", "testMethod");
        Frame frame1 = new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId());
        Frame frame2 = new Frame(frame1, new Method("TestClass2", "testMethod2"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame1);
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame2);
        List<Frame> frameList = Profile.frameList();
        assertNotNull(frameList);
        assertEquals(2, frameList.size());
        assertEquals(frame2, frameList.get(1));
    }

    @Test
    public void testAllocations() {
        Profile.initProfiler();
        Profile.start();
        ClassAllocation ca1 = new ClassAllocation("TestClass");
        ClassAllocation ca2 = new ClassAllocation("TestClass2");
        Profile._allocList.put("TestClass", ca1);
        Profile._allocList.put("TestClass2", ca2);
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertNotNull(allocations);
        assertEquals(2, allocations.size());
        assertEquals(ca2, allocations.get(1));
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.initProfiler();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId());
        Frame frame2 = new Frame(frame1, new Method("TestClass2", "testMethod2"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame1);
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame2);
        long threadId = Thread.currentThread().getId();
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(frame2.getEndTime() - frame1.getBeginTime(), threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.initProfiler();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass3", "testMethod3"), Thread.currentThread().getId());
        Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), Thread.currentThread().getId());
        Frame frame3 = new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame1);
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame2);
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame3);
        Comparator<Frame> comp = (f1, f2) -> f1.getBeginTime() - f2.getBeginTime();
        Profile.sortFrameList(comp);
        List<Frame> sortedFrameList = Profile.frameList();
        assertEquals(3, sortedFrameList.size());
        assertEquals(frame3, sortedFrameList.get(0));
        assertEquals(frame2, sortedFrameList.get(1));
        assertEquals(frame1, sortedFrameList.get(2));
    }

    @Test
    public void testStartEnd() {
        Profile.initProfiler();
        Profile.start("TestClass", "testMethod");
        Frame frame = new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame);
        Profile.end("TestClass", "testMethod");
        assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.initProfiler();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame1);
        Profile.beginWait("TestClass", "testMethodWait");
        Frame frame2 = new Frame(frame1, new Method("TestClass", "testMethodWait"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame2);
        Profile.endWait("TestClass", "testMethodWait");
        assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }

    @Test
    public void testUnwind() {
        Profile.initProfiler();
        Profile.start();
        Frame root = new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), root);
        Frame exceptionFrame = new Frame(root, new Method("TestClassException", "testMethodException"), Thread.currentThread().getId());
        Profile._threadActiveFrame.put(Thread.currentThread().getId(), exceptionFrame);
        Profile.unwind("TestClassException", "testMethodException", "TestException");
        Frame target = Profile._threadActiveFrame.get(Thread.currentThread().getId());
        assertEquals(root, target);
    }

    @Test
    public void testAlloc() {
        Profile.initProfiler();
        Profile.start();
        Profile.alloc("TestClass");
        ClassAllocation ca = Profile._allocList.get("TestClass");
        assertEquals(1, ca.getAllocCount());
    }

    @Test
    public void testRun() throws IOException {
        Profile.initProfiler();
        Profile.start();
        Profile.run();
        assertTrue(Profile._controller.isClosed());
    }
}
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;
import com.mentorgen.tools.profile.output.ThreadDictionary;

public class ProfileTest {

    private static Profile underTest;
    private static ThreadDictionary threadDictionary;
    private static List<Frame> frameList;
    private static Map<Long, Frame> threadActiveFrame;
    private static Map<String, Method> methodDictionary;
    private static Map<String, ClassAllocation> allocList;
    private static Controller controller;

    @BeforeClass
    public static void setupClass() {
        underTest = new Profile();
        threadDictionary = new ThreadDictionary();
        frameList = new ArrayList<>();
        threadActiveFrame = new HashMap<>();
        methodDictionary = new HashMap<>();
        allocList = new HashMap<>();
        controller = new Controller();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(underTest.isInitialized());
    }

    @Test
    public void testInit() {
        Profile.init();
        assertEquals(0, threadDictionary.size());
        assertEquals(0, frameList.size());
        assertEquals(0, threadActiveFrame.size());
        assertEquals(0, methodDictionary.size());
        assertEquals(0, allocList.size());
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertEquals(0, threadDictionary.size());
        assertEquals(0, frameList.size());
        assertEquals(0, threadActiveFrame.size());
        assertEquals(0, methodDictionary.size());
        assertEquals(0, allocList.size());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        assertTrue(controller.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAlreadyRunning() {
        Profile.init();
        Profile.start();
        Profile.start();
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        assertFalse(controller.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        Profile.init();
        Profile.stop();
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName("output.txt");
        assertEquals("output.txt", controller.getFileName());
    }

    @Test(expected = NullPointerException.class)
    public void testShutdownNullController() {
        Profile.init();
        Profile._controller = null;
        Profile.shutdown();
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        assertTrue(controller.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void testShutdownRunning() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        Profile.start();
        Profile.shutdown();
        Profile.start(); // Exception should be thrown here
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start("TestThread", "testThreadMethod");
        Profile.start("TestThread", "testThreadMethod");
        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
        assertTrue(threads.contains(Thread.currentThread().getId()));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
        assertEquals(2, interactions.size());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass2", "testMethod2");
        Profile.start("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        List<Frame> frameList = Profile.frameList();
        assertEquals(4, frameList.size());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.alloc("TestClass");
        Profile.alloc("TestClass");
        List<ClassAllocation> allocations = new ArrayList<>(allocList.values());
        assertEquals(1, allocations.size());
        assertEquals("TestClass", allocations.get(0).getClassName());
        assertEquals(2, allocations.get(0).getAllocationCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        long threadId = Thread.currentThread().getId();
        Thread.yield();
        Thread.sleep(1);
        Profile.end("TestClass", "testMethod");
        long totalTime = Profile.getThreadTotalTime(threadId);
        assertTrue(totalTime > 0);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass2", "testMethod2");
        Profile.start("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        Collections.sort(frameList);
    }

    @Test
    public void testStartWithNullClassNameOrMethodName() {
        Profile.init();
        Profile.start(null, "testMethod");
        Profile.start("TestClass", null);
        Profile.start(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartWithEmptyClassNameOrMethodName() {
        Profile.init();
        Profile.start("", "testMethod");
        Profile.start("TestClass", "");
        Profile.start("", "");
    }

    @Test
    public void testStartWithSameClassNameAndMethodName() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.start("TestClass", "testMethod");
        List<Frame> frames = Profile.frameList();
        assertEquals(1, frames.size());
    }

    @Test
    public void testStartWithDifferentClassNameAndMethodName() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        List<Frame> frames = Profile.frameList();
        assertEquals(2, frames.size());
    }

    @Test
    public void testEndWithNullClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end(null, "testMethod");
        Profile.end("TestClass", null);
        Profile.end(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndWithEmptyClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end("", "testMethod");
        Profile.end("TestClass", "");
        Profile.end("", "");
    }

    @Test
    public void testEndWithMismatchedClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass1", "testMethod2");
        List<Frame> frames = Profile.frameList();
        assertEquals(1, frames.size());
    }

    @Test
    public void testBeginWaitWithNullClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.beginWait(null, "testMethod");
        Profile.beginWait("TestClass", null);
        Profile.beginWait(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBeginWaitWithEmptyClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.beginWait("", "testMethod");
        Profile.beginWait("TestClass", "");
        Profile.beginWait("", "");
    }

    @Test
    public void testBeginWaitWithDifferentClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.beginWait("TestClass1", "testMethod2");
        List<Frame> frames = Profile.frameList();
        assertEquals(1, frames.size());
    }

    @Test
    public void testEndWaitWithNullClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.beginWait("TestClass", "testMethod");
        Profile.endWait(null, "testMethod");
        Profile.endWait("TestClass", null);
        Profile.endWait(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndWaitWithEmptyClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.beginWait("TestClass", "testMethod");
        Profile.endWait("", "testMethod");
        Profile.endWait("TestClass", "");
        Profile.endWait("", "");
    }

    @Test
    public void testEndWaitWithMismatchedClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.beginWait("TestClass1", "testMethod2");
        Profile.endWait("TestClass2", "testMethod1");
        List<Frame> frames = Profile.frameList();
        assertEquals(1, frames.size());
    }

    @Test
    public void testUnwindWithNullClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.unwind(null, "testMethod");
        Profile.unwind("TestClass", null);
        Profile.unwind(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnwindWithEmptyClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.unwind("", "testMethod");
        Profile.unwind("TestClass", "");
        Profile.unwind("", "");
    }

    @Test
    public void testUnwindWithDifferentClassNameOrMethodName() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.unwind("TestClass2", "testMethod1");
        List<Frame> frames = Profile.frameList();
        assertEquals(1, frames.size());
    }

    @Test
    public void testAllocWithNullClassName() {
        Profile.init();
        Profile.alloc(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllocWithEmptyClassName() {
        Profile.init();
        Profile.alloc("");
    }
}
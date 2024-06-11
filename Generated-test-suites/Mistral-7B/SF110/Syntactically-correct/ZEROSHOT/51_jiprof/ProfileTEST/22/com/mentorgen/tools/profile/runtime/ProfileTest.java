package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.runtime.Controller;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

    private static Profile underTest;
    private static long threadId;
    private static Frame frame;
    private static List<Frame> frameList;
    private static Map<Long, Frame> threadActiveFrame;
    private static Map<String, Method> methodDictionary;
    private static Map<String, ClassAllocation> allocList;
    private static Controller controller;
    private static ProfileDump profileDump;

    @Before
    public void setUp() {
        underTest = new Profile();
        threadId = 1;
        frame = new Frame(null, new Method("TestClass", "testMethod"), threadId);
        frameList = new ArrayList<>();
        threadActiveFrame = new HashMap<>();
        methodDictionary = new HashMap<>();
        allocList = new HashMap<>();
        controller = new Controller();
        profileDump = new ProfileDump();
    }

    @After
    public void tearDown() {
        underTest = null;
        threadId = 0;
        frame = null;
        frameList = null;
        threadActiveFrame = null;
        methodDictionary = null;
        allocList = null;
        controller = null;
        profileDump = null;
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
        if (Profile._controllerThread != null) {
            assertTrue(Profile._controllerThread.isAlive());
        }
    }

    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertTrue(Profile._frameList.isEmpty());
        assertTrue(Profile._threadActiveFrame.isEmpty());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        assertTrue(controller.isProfileActive());
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        controller.stop();
        assertFalse(controller.isProfileActive());
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName("test.out");
        assertEquals("test.out", controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, frameList().size());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        threadActiveFrame.put(threadId, frame);
        frameList.add(frame);
        Iterable<Long> threads = Profile.threads();
        assertEquals(1, threads.spliterator().getExactSizeIfKnown());
        assertTrue(threads.contains(threadId));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        threadActiveFrame.put(threadId, frame);
        frameList.add(frame);
        Iterable<Frame> interactions = Profile.interactions(threadId);
        assertEquals(1, interactions.spliterator().getExactSizeIfKnown());
        assertTrue(interactions.contains(frame));
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        threadActiveFrame.put(threadId, frame);
        frameList.add(frame);
        Iterable<Frame> frameList = Profile.frameList();
        assertEquals(1, frameList.spliterator().getExactSizeIfKnown());
        assertTrue(frameList.contains(frame));
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.alloc("TestClass");
        Map<String, ClassAllocation> allocations = Profile.allocations();
        assertTrue(allocations.containsKey("TestClass"));
        ClassAllocation ca = allocations.get("TestClass");
        assertEquals(1, ca.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        threadActiveFrame.put(threadId, frame);
        frameList.add(frame);
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(0, threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        frameList.add(new Frame(null, new Method("TestClass2", "testMethod2"), 2));
        frameList.add(new Frame(null, new Method("TestClass1", "testMethod1"), 1));
        Profile.sortFrameList((f1, f2) -> Long.compare(f1.getBeginTime(), f2.getBeginTime()));
        Iterable<Frame> sortedFrameList = Profile.frameList();
        Frame testMethod1 = sortedFrameList.iterator().next();
        Frame testMethod2 = sortedFrameList.iterator().next();
        assertTrue(testMethod1.getBeginTime() < testMethod2.getBeginTime());
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        Profile.start();
        threadActiveFrame.put(threadId, frame);
        frameList.add(frame);
        Profile.end("TestClass", "testMethod");
        Frame endFrame = findFrame(threadId, "TestClass", "testMethod");
        assertTrue(endFrame != null);
        assertTrue(endFrame.isEnded());
        assertEquals(frame, endFrame.getParent());
        long netTime = endFrame.getNetTime();
        assertTrue(netTime > 0);
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        Profile.start();
        threadActiveFrame.put(threadId, frame);
        frameList.add(frame);
        Profile.beginWait("TestClass", "testMethodWait");
        Frame beginWaitFrame = findFrame(threadId, "TestClass", "testMethodWait");
        assertTrue(beginWaitFrame != null);
        assertTrue(beginWaitFrame.isBeginWait());
        assertTrue(beginWaitFrame.getEndWaitTime() == 0);
        Profile.endWait("TestClass", "testMethodWait");
        Frame endWaitFrame = findFrame(threadId, "TestClass", "testMethodWait");
        assertTrue(endWaitFrame != null);
        assertTrue(endWaitFrame.isEndWait());
        long netTime = endWaitFrame.getNetTime();
        assertTrue(netTime > 0);
    }

    @Test
    public void testUnwind() {
        Profile.init();
        Profile.start();
        threadActiveFrame.put(threadId, frame);
        frameList.add(frame);
        Profile.unwind("TestClass", "testMethodException");
        Frame unwindFrame = findFrame(threadId, "TestClass", "testMethodException");
        assertTrue(unwindFrame != null);
        assertTrue(unwindFrame.isUnwinding());
    }

    @Test
    public void testAlloc() {
        Profile.init();
       Profile.start();
        ClassAllocation classAllocation = allocList.get("TestClass");
        if (classAllocation == null) {
            classAllocation = new ClassAllocation("TestClass");
            allocList.put("TestClass", classAllocation);
        }
        classAllocation.incAllocCount();
        assertEquals(1, classAllocation.getAllocCount());
    }

    private Frame findFrame(long threadId, String className, String methodName) {
        Iterable<Frame> interactions = Profile.interactions(threadId);
        for (Frame frame : interactions) {
            if (frame.getClassName().equals(className) && frame.getMethodName().equals(methodName)) {
                return frame;
            }
        }
        return null;
    }
}
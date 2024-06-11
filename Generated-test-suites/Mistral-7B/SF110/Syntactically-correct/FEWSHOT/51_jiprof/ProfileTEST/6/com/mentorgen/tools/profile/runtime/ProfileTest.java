package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
        if (Profile._controllerThread != null) {
            assertTrue(Profile._controllerThread.isAlive());
        }
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.initProfiler();
        assertEquals(0, Profile.threads().size());
        assertEquals(0, Profile.frameList().size());
        assertEquals(0, Profile.allocations().size());
        Profile.clear();
        Profile.initProfiler();
        assertEquals(0, Profile.threads().size());
        assertEquals(0, Profile.frameList().size());
        assertEquals(0, Profile.allocations().size());
    }

    @Test
    public void testStartAndEnd() {
        Profile.init();
        Profile.initProfiler();

        long threadId = Thread.currentThread().getId();
        String className = "TestClass";
        String methodName = "testMethod";

        Profile.start(className, methodName);
        // Some code to be tested here
        Profile.end(className, methodName);

        Iterator<Frame> frameIterator = Profile.frameList().iterator();
        Frame frame = frameIterator.next();
        assertEquals(className, frame.getClassName());
        assertEquals(methodName, frame.getMethodName());
        assertEquals(threadId, frame.getThreadId());
        assertNull(frame.getParent());
        assertNotNull(frame.getBeginTime());
        assertNotNull(frame.getEndTime());
        assertNotNull(frame.getNetTime());

        frameIterator = Profile.frameList().iterator();
        Frame parentFrame = null;
        while (frameIterator.hasNext()) {
            Frame frame = frameIterator.next();
            if (frame.getParent() != null) {
                parentFrame = frame.getParent();
                break;
            }
        }
        assertEquals(className, parentFrame.getClassName());
        assertEquals("<init>", parentFrame.getMethodName());
        assertEquals(threadId, parentFrame.getThreadId());
        assertNull(parentFrame.getParent());
        assertNotNull(parentFrame.getBeginTime());
        assertNotNull(parentFrame.getEndTime());
        assertNotNull(parentFrame.getNetTime());
    }

    @Test
    public void testStartAndEndWithParent() {
        Profile.init();
        Profile.initProfiler();

        long threadId = Thread.currentThread().getId();
        String parentClassName = "ParentClass";
        String parentMethodName = "parentMethod";
        String className = "TestClass";
        String methodName = "testMethod";

        Profile.start(parentClassName, parentMethodName);
        // Some code to be tested here
        Profile.start(className, methodName);
        // Some code to be tested here
        Profile.end(className, methodName);
        Profile.end(parentClassName, parentMethodName);

        Iterator<Frame> frameIterator = Profile.frameList().iterator();
        Frame testFrame = null;
        Frame parentFrame = null;
        while (frameIterator.hasNext()) {
            Frame frame = frameIterator.next();
            if (frame.getClassName().equals(className)) {
                testFrame = frame;
            }
            if (frame.getClassName().equals(parentClassName)) {
                parentFrame = frame;
                break;
            }
        }

        assertEquals(className, testFrame.getClassName());
        assertEquals(methodName, testFrame.getMethodName());
        assertEquals(threadId, testFrame.getThreadId());
        assertEquals(parentFrame.getThreadId(), testFrame.getParent().getThreadId());
        assertNotNull(testFrame.getBeginTime());
        assertNotNull(testFrame.getEndTime());
        assertNotNull(testFrame.getNetTime());
        assertNotNull(parentFrame.getBeginTime());
        assertNotNull(parentFrame.getEndTime());
        assertNotNull(parentFrame.getNetTime());
    }

    @Test
    public void testBeginWaitAndEndWait() {
        Profile.init();
        Profile.initProfiler();

        long threadId = Thread.currentThread().getId();
        String className = "TestClass";
        String methodName = "testMethod";

        Profile.start(className, methodName);
        // Some code to block here
        Profile.beginWait(className, methodName);
        // Some code to be tested here while blocked
        Profile.endWait(className, methodName);

        Iterator<Frame> frameIterator = Profile.frameList().iterator();
        Frame testFrame = null;
        while (frameIterator.hasNext()) {
            Frame frame = frameIterator.next();
            if (frame.getClassName().equals(className) && frame.isWaiting()) {
                testFrame = frame;
                break;
            }
        }

        assertEquals(className, testFrame.getClassName());
        assertEquals(methodName, testFrame.getMethodName());
        assertEquals(threadId, testFrame.getThreadId());
        assertNotNull(testFrame.getBeginTime());
        assertNotNull(testFrame.getBeginWaitTime());
        assertNotNull(testFrame.getEndTime());
        assertNotNull(testFrame.getEndWaitTime());
        assertNotNull(testFrame.getNetTime());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.initProfiler();

        long threadId = Thread.currentThread().getId();
        String className = "TestClass";

        Profile.start(className, "<init>");
        new TestClass();
        Profile.end(className, "<init>");

        Iterator<ClassAllocation> allocationIterator = Profile.allocations().iterator();
        ClassAllocation allocation = null;
        while (allocationIterator.hasNext()) {
            ClassAllocation ca = allocationIterator.next();
            if (ca.getClassName().equals(className)) {
                allocation = ca;
                break;
            }
        }

        assertEquals(className, allocation.getClassName());
        assertEquals(1, allocation.getAllocCount());
    }

    @Test
    public void testAllocMultiple() {
        Profile.init();
        Profile.initProfiler();

        long threadId = Thread.currentThread().getId();
        String className = "TestClass";

        Profile.start(className, "<init>");
        new TestClass();
        new TestClass();
        new TestClass();
        Profile.end(className, "<init>");

        Iterator<ClassAllocation> allocationIterator = Profile.allocations().iterator();
        ClassAllocation allocation = null;
        while (allocationIterator.hasNext()) {
            ClassAllocation ca = allocationIterator.next();
            if (ca.getClassName().equals(className)) {
                allocation = ca;
                break;
            }
        }

        assertEquals(className, allocation.getClassName());
        assertEquals(3, allocation.getAllocCount());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.initProfiler();

        Thread newThread = new Thread(() -> { // Some code to be run in a new thread here
        });
        newThread.start();

        List<Long> threads = new ArrayList<>(Profile.threads());
        assertEquals(2, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
        assertEquals(newThread.getId(), threads.get(1));
    }
}
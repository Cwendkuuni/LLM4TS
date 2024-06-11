package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

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
        Profile.start("ProfileTest", "testMethod");
        Profile.end("ProfileTest", "testMethod");
        Profile.clear();
        assertNull(Profile.threads().iterator().next());
        assertNull(Profile.interactions(1L).iterator().next());
        assertNull(Profile.frameList().iterator().next());
    }

    @Test
    public void testStartAndStop() {
        Profile.initProfiler();
        Profile.start("ProfileTest", "testMethod");
        Profile.end("ProfileTest", "testMethod");
        Profile.stop();
        assertFalse(Profile.isProfiling());
    }

    @Test
    public void testSetFileName() {
        Profile.initProfiler();
        Profile.setFileName("output.prof");
        assertEquals("output.prof", Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.initProfiler();
        Profile.start("ProfileTest", "testMethod");
        Profile.end("ProfileTest", "testMethod");
        Profile.shutdown();
        assertFalse(Profile.isProfiling());
    }

    @Test
    public void testThreads() {
        Profile.initProfiler();
        Profile.start("ProfileTest", "testMethod1");
        Profile.start("ProfileTest", "testMethod2");
        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
        assertTrue(threads.contains(Thread.currentThread().getId()));
    }

    @Test
    public void testInteractions() {
        Profile.initProfiler();
        Profile.start("ProfileTest", "testMethod1");
        Profile.end("ProfileTest", "testMethod1");
        List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
        assertEquals(1, interactions.size());
        Frame interaction = interactions.get(0);
        assertEquals("ProfileTest", interaction.getClassName());
        assertEquals("testMethod1", interaction.getMethodName());
    }

    @Test
    public void testFrameList() {
        Profile.initProfiler();
        Profile.start("ProfileTest", "testMethod1");
        Profile.end("ProfileTest", "testMethod1");
        List<Frame> frameList = Profile.frameList();
        assertEquals(1, frameList.size());
        Frame frame = frameList.get(0);
        assertEquals("ProfileTest", frame.getClassName());
        assertEquals("testMethod1", frame.getMethodName());
    }

    @Test
    public void testAllocations() {
        Profile.initProfiler();
        Profile.alloc("String");
        Profile.alloc("ArrayList");
        List<ClassAllocation> allocations = Profile.allocations();
        assertEquals(2, allocations.size());
        ClassAllocation stringAllocation = allocations.get(0);
        ClassAllocation arrayListAllocation = allocations.get(1);
        assertEquals("String", stringAllocation.getClassname());
        assertEquals(1, stringAllocation.getAllocCount());
        assertEquals("ArrayList", arrayListAllocation.getClassname());
        assertEquals(1, arrayListAllocation.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.initProfiler();
        Profile.start("ProfileTest", "testMethod1");
        Profile.end("ProfileTest", "testMethod1");
        long threadId = Thread.currentThread().getId();
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertTrue(threadTotalTime > 0);
    }

    @Test
    public void testSortFrameList() {
        Profile.initProfiler();
        Profile.start("ProfileTest", "testMethod1");
        Profile.end("ProfileTest", "testMethod1");
        Profile.start("ProfileTest", "testMethod2");
        Profile.end("ProfileTest", "testMethod2");
        Comparator<Frame> comparator = Collections.reverseOrder();
        Profile.sortFrameList(comparator);
        Frame firstFrame = Profile.frameList().get(0);
        Frame secondFrame = Profile.frameList().get(1);
        assertTrue(firstFrame.getBeginTime() > secondFrame.getBeginTime());
    }

    @Test
    public void testStartAndEndMethods() {
        Profile.initProfiler();
        String className = "ProfileTest";
        String methodName = "testMethod";
        long startTime = System.nanoTime();
        Profile.start(className, methodName);
        long endTime = System.nanoTime();
        Profile.end(className, methodName);
        Frame frame = findFrame(className, methodName);
        assertTrue(frame != null);
        assertTrue(frame.getBeginTime() <= startTime);
        assertTrue(frame.getEndTime() >= endTime);
    }

    @Test
    public void testBeginAndEndWait() {
        Profile.initProfiler();
        String className = "ProfileTest";
        String methodName = "testMethod";
        long startTime = System.nanoTime();
        Profile.beginWait(className, methodName);
        long waitTime = System.nanoTime();
        Profile.endWait(className, methodName);
        Frame frame = findFrame(className, methodName);
        assertTrue(frame != null);
        assertTrue(frame.getBeginTimeWait() <= startTime);
        assertTrue(frame.getEndTimeWait() >= waitTime);
    }

    @Test
    public void testUnwind() {
        Profile.initProfiler();
        String className = "ProfileTest";
        String methodName = "testMethod";
        String exception = "TestException";
        try {
            throw new TestException();
        } catch (TestException e) {
            Profile.unwind(className, methodName, exception);
        }
        Frame frame = findFrame(className, methodName);
        assertTrue(frame != null);
        assertTrue(frame.isException());
        assertTrue(frame.getException().equals(exception));
    }

    @Test
    public void testAlloc() {
        Profile.initProfiler();
        String className = "ProfileTest";
        Profile.alloc(className);
        ClassAllocation classAllocation = Profile._allocList.get(className);
        assertEquals(1, classAllocation.getAllocCount());
    }

    @Test
    public void testRun() {
        Profile.initProfiler();
        Profile.start("ProfileTest", "testMethod");
        Profile.end("ProfileTest", "testMethod");
        Profile.shutdown();
    }

    private Frame findFrame(String className, String methodName) {
        long threadId = Thread.currentThread().getId();
        Frame target = Profile._threadActiveFrame.get(threadId);
        while (target != null && !target.getClassName().equals(className) || !target.getMethodName().equals(methodName)) {
            target = target.getParent();
        }
        return target;
    }

    private class TestException extends Exception {

    }
}
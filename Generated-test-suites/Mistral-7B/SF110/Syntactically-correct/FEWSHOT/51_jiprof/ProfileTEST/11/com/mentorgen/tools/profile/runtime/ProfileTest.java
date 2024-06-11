package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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

    @Test
    public void testClear() {
        Profile.initProfiler();
        Profile.clear();
        assertTrue(Profile._threadDictionary.isEmpty());
        assertTrue(Profile._frameList.isEmpty());
    }

    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testThreads() {
        Profile.initProfiler();
        Profile.init();
        List<Long> threads = Profile.threads();
        assertTrue(threads.isEmpty());
    }

    @Test
    public void testInteractions() {
        Profile.initProfiler();
        Profile.init();
        long threadId = 1;
        List<Frame> interactions = Profile.interactions(threadId);
        assertTrue(interactions.isEmpty());
    }

    @Test
    public void testFrameList() {
        Profile.initProfiler();
        Profile.init();
        List<Frame> frameList = Profile.frameList();
        assertTrue(frameList.isEmpty());
    }

    @Test
    public void testAllocations() {
        Profile.initProfiler();
        Profile.init();
        List<ClassAllocation> allocations = new ArrayList<>(Profile.allocations());
        assertTrue(allocations.isEmpty());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.initProfiler();
        Profile.init();
        long threadId = 1;
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(0, threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.initProfiler();
        Profile.init();
        Comparator<Frame> comp = (f1, f2) -> Long.compare(f1.getBeginTime(), f2.getBeginTime());
        Profile.sortFrameList(comp);
        List<Frame> sortedFrameList = new ArrayList<>(Profile.frameList());
        Collections.sort(sortedFrameList, comp);
        assertTrue(Profile.frameList().equals(sortedFrameList));
    }

    @Test
    public void testStart() {
        Profile.initProfiler();
        Profile.init();
        String className = "test.ClassName";
        String methodName = "test.MethodName";
        Profile.start(className, methodName);
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertNotNull(frame);
    }

    @Test
    public void testEnd() {
        Profile.initProfiler();
        Profile.init();
        String className = "test.ClassName";
        String methodName = "test.MethodName";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertNotNull(frame);
        assertTrue(frame.isFinished());
    }

    @Test
    public void testBeginWait() {
        Profile.initProfiler();
        Profile.init();
        String className = "test.ClassName";
        String methodName = "test.MethodName";
        Profile.start(className, methodName);
        Profile.beginWait(className, methodName);
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertNotNull(frame);
        assertTrue(frame.isWaiting());
    }

    @Test
    public void testEndWait() {
        Profile.initProfiler();
        Profile.init();
        String className = "test.ClassName";
        String methodName = "test.MethodName";
        Profile.start(className, methodName);
        Profile.beginWait(className, methodName);
        Profile.endWait(className, methodName);
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertNotNull(frame);
        assertTrue(frame.isFinished());
    }

    @Test
    public void testUnwind() {
        Profile.initProfiler();
        Profile.init();
        String className = "test.ClassName";
        String methodName = "test.MethodName";
        String exception = "java.lang.Exception";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Profile.unwind(className, methodName, exception);
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertNotNull(frame);
        assertTrue(frame.isException());
    }

    @Test
    public void testAlloc() {
        Profile.initProfiler();
        Profile.init();
        String className = "test.ClassName";
        Profile.alloc(className);
        ClassAllocation classAllocation = Profile._allocList.get(className);
        assertNotNull(classAllocation);
        assertEquals(1, classAllocation.getAllocCount());
    }
}
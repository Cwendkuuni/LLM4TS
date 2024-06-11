package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Collections;
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
        Profile.init();
        Profile.clear();
        assertTrue(Profile._threadDictionary.isEmpty());
        assertTrue(Profile._frameList.isEmpty());
        assertTrue(Profile._methodDictionary.isEmpty());
        assertTrue(Profile._threadActiveFrame.isEmpty());
    }

    @Test
    public void testInitAndClear() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertTrue(Profile._threadDictionary.isEmpty());
        assertTrue(Profile._frameList.isEmpty());
        assertTrue(Profile._methodDictionary.isEmpty());
        assertTrue(Profile._threadActiveFrame.isEmpty());
    }

    @Test
    public void testInitAndStart() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.stop();
        List<Frame> frames = Profile.frameList();
        assertFalse(frames.isEmpty());
        Frame frame = frames.get(0);
        assertNotNull(frame);
        assertEquals("TestClass", frame.getClassName());
        assertEquals("testMethod", frame.getMethodName());
        assertNull(frame.getParent());
        assertNull(frame.getChild(new Method("TestClass", "testMethod2")));
    }

    @Test
    public void testEndWithNoStart() {
        Profile.init();
        Profile.end("TestClass", "testMethod");
        assertTrue(Profile._threadDictionary.isEmpty());
        assertTrue(Profile._frameList.isEmpty());
    }

    @Test
    public void testFindFrame() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass", "testMethod");
        Profile.end("TestClass2", "testMethod2");
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), "TestClass", "testMethod");
        assertNotNull(frame);
        assertEquals("TestClass", frame.getClassName());
        assertEquals("testMethod", frame.getMethodName());
        Frame frame2 = Profile.findFrame(Thread.currentThread().getId(), "TestClass2", "testMethod2");
        assertNotNull(frame2);
        assertEquals("TestClass2", frame2.getClassName());
        assertEquals("testMethod2", frame2.getMethodName());
        Frame frame3 = Profile.findFrame(Thread.currentThread().getId(), "TestClass3", "testMethod3");
        assertNull(frame3);
    }

    @Test
    public void testFindFrameWithException() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass", "testMethod");
        Profile.end("TestClass2", "testMethod2");
        Profile._debugException = true;
        try {
            Profile.end("TestClass3", "testMethod3");
        } catch (Exception e) {
            Frame frame = Profile.findFrame(Thread.currentThread().getId(), "TestClass3", "testMethod3");
            assertNotNull(frame);
            assertEquals("TestClass3", frame.getClassName());
            assertEquals("testMethod3", frame.getMethodName());
        }
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.alloc("java.lang.String");
        Profile.alloc("java.util.ArrayList");
        Profile.end("TestClass", "testMethod");
        Map<String, ClassAllocation> allocList = Profile._allocList;
        assertFalse(allocList.isEmpty());
        List<ClassAllocation> classAllocations = new ArrayList<>(allocList.values());
        ClassAllocation stringAllocation = classAllocations.get(0);
        ClassAllocation arrayListAllocation = classAllocations.get(1);
        assertEquals("java.lang.String", stringAllocation.getClassname());
        assertEquals(2, stringAllocation.getAllocCount());
        assertEquals("java.util.ArrayList", arrayListAllocation.getClassname());
        assertEquals(1, arrayListAllocation.getAllocCount());
    }

    @Test
    public void testFrameListSorting() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass2", "testMethod2");
        Profile.sortFrameList((f1, f2) -> Long.compare(f1.getBeginTime(), f2.getBeginTime()));
        List<Frame> frames = Profile.frameList();
        Frame frame1 = frames.get(0);
        Frame frame2 = frames.get(1);
        assertEquals("TestClass1", frame1.getClassName());
        assertEquals("testMethod1", frame1.getMethodName());
        assertEquals("TestClass2", frame2.getClassName());
        assertEquals("testMethod2", frame2.getMethodName());
        assertTrue(frame1.getBeginTime() < frame2.getBeginTime());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass2", "testMethod2");
        Iterable<Long> threads = Profile.threads();
        List<Long> threadIds = new ArrayList<>(Arrays.asList(Thread.currentThread().getId(), 1L, 2L));
        assertTrue(Arrays.asList(threads.toArray()).containsAll(threadIds.toArray()));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass2", "testMethod2");
        Iterable<Long> threads = Profile.threads();
        Iterable<Frame> interactions = Profile.interactions(1L);
        List<Frame> frames = new ArrayList<>(Arrays.asList(interactions.toArray()));
        List<Long> threadIds = new ArrayList<>(Arrays.asList(Thread.currentThread().getId(), 1L, 2L));
        assertEquals(2, frames.size());
        assertEquals(frames.get(0).getThreadId(), 1L);
        assertEquals(frames.get(1).getThreadId(), 1L);
        assertTrue(Arrays.asList(threads.toArray()).containsAll(threadIds.toArray()));
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass2", "testMethod2");
        long thread1TotalTime = Profile.getThreadTotalTime(1L);
        long thread2TotalTime = Profile.getThreadTotalTime(2L);
        assertEquals(thread1TotalTime, frames(1L).getTotalTime());
        assertEquals(thread2TotalTime, frames(2L).getTotalTime());
    }

    private Frame frames(long threadId) {
        Iterable<Frame> interactions = Profile.interactions(threadId);
        Frame frame = interactions.iterator().next();
        return frame;
    }
}
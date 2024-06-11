package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

    private static Profile profile;

    @Before
    public void setup() {
        Profile.init();
        profile = new Profile();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertEquals(0, Profile.threads().size());
        assertEquals(0, Profile.frameList().size());
    }

    @Test
    public void testStartAndEnd() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        List<Long> threads = Profile.threads();
        assertEquals(1, threads.size());
        Frame frame = Profile.findFrame(threads.get(0), "TestClass", "testMethod");
        assertNotNull(frame);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartNullClassName() {
        Profile.init();
        Profile.start(null, "testMethod");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartNullMethodName() {
        Profile.init();
        Profile.start("TestClass", null);
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass2", "testMethod2");
        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        List<Long> threads = Profile.threads();
        long threadId = threads.get(0);
        List<Frame> interactions = Profile.interactions(threadId);
        assertEquals(2, interactions.size());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        List<Frame> frameList = Profile.frameList();
        assertEquals(2, frameList.size());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        new TestClass().new TestInnerClass();
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertEquals(1, allocations.size());
        ClassAllocation ca = allocations.get(0);
        assertEquals("TestClass", ca.getClassName());
        ca.incAllocCount();
        assertEquals(2, ca.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        List<Long> threads = Profile.threads();
        long threadId = threads.get(0);
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(2 * 1000L * 1000L, threadTotalTime); // Assume each end() call takes 1ms
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass2", "testMethod2");
        Profile.end("TestClass1", "testMethod1");
        List<Frame> frameList = new ArrayList<>(Profile._frameList);
        Collections.sort(frameList, (f1, f2) -> f1.getBeginTime() - f2.getBeginTime());
        List<Frame> sortedFrameList = Profile.frameList();
        for (int i = 0; i < frameList.size(); i++) {
            Frame f1 = frameList.get(i);
            Frame f2 = sortedFrameList.get(i);
            assertEquals(f1.getClassName(), f2.getClassName());
            assertEquals(f1.getMethodName(), f2.getMethodName());
            assertEquals(f1.getBeginTime(), f2.getBeginTime());
            assertEquals(f1.getEndTime(), f2.getEndTime());
        }
    }

    @Test
    public void testBeginWaitAndEndWait() {
        Profile.init();
        Profile.start();
        Profile.beginWait("TestClass1", "testMethod1");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Profile.endWait("TestClass1", "testMethod1");
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), "TestClass1", "testMethod1");
        assertEquals(200L, frame.getWaitTime());
    }

    @Test
    public void testUnwind() {
        Profile.init();
        Profile.start();
        try {
            throw new Exception("TestException");
        } catch (Exception e) {
            Profile.unwind("TestClass1", "testMethod1", e.getClass().getName());
        }
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), "TestClass1", "testMethod1");
        assertNotNull(frame);
        assertTrue(frame.isException());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start();
        new TestClass();
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        ClassAllocation ca = allocations.get(0);
        assertEquals("TestClass", ca.getClassName());
        ca.incAllocCount();
        assertEquals(2, ca.getAllocCount());
    }

    @Test
    public void testRun() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        Profile.shutdown();
        ProfileDump.dump();
    }

    static class TestClass {
        public TestClass() {
            alloc("TestClass");
        }

        public class TestInnerClass {
            TestInnerClass() {
                alloc("TestInnerClass");
            }
        }
    }
}
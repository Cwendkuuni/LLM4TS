package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;
import com.mentorgen.tools.profile.output.ThreadDictionary;

public class ProfileTest {

    private static Profile profile;
    private static ThreadDictionary threadDictionary;
    private static List<Frame> frameList;

    @BeforeClass
    public static void init() {
        Profile.init();
        profile = new Profile();
        threadDictionary = Profile._threadDictionary;
        frameList = Profile._frameList;
    }

    // Test for initProfiler method
    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controllerThread != null);
    }

    // Test for init method
    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    // Test for clear method
    @Test
    public void testClear() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertTrue(Profile._threadDictionary.size() == 0);
        assertTrue(Profile._frameList.size() == 0);
    }

    // Test for threads method
    @Test
    public void testThreads() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
    }

    // Test for interactions method
    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass1", "testMethod1");
        Iterator<Frame> frameIterator = Profile.interactions(1L);
        Frame frame1 = frameIterator.next();
        Frame frame2 = frameIterator.next();
        assertEquals(frame1.getClassName(), "TestClass1");
        assertEquals(frame1.getMethodName(), "testMethod1");
        assertEquals(frame2.getClassName(), "TestClass1");
        assertEquals(frame2.getMethodName(), "testMethod1");
    }

    // Test for frameList method
    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        List<Frame> frames = new ArrayList<>(frameList);
        Frame frame1 = frames.get(0);
        Frame frame2 = frames.get(1);
        assertEquals(frame1.getClassName(), "TestClass1");
        assertEquals(frame1.getMethodName(), "testMethod1");
        assertEquals(frame2.getClassName(), "TestClass1");
        assertEquals(frame2.getMethodName(), "testMethod1");
    }

    // Test for getThreadTotalTime method
    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass2", "testMethod2");
        long thread1TotalTime = Profile.getThreadTotalTime(1L);
        long thread2TotalTime = Profile.getThreadTotalTime(2L);
        assertEquals(thread1TotalTime, frameList.get(0).getNetTime());
        assertEquals(thread2TotalTime, frameList.get(1).getNetTime());
    }

    // Test for sortFrameList method
    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start("TestClass1", "testMethod1");
        Profile.start("TestClass2", "testMethod2");
        Profile.end("TestClass1", "testMethod1");
        Profile.end("TestClass2", "testMethod2");
        Collections.sort(frameList);
        Frame firstFrame = frameList.get(0);
        Frame secondFrame = frameList.get(1);
        assertEquals(firstFrame.getNetTime(), lessThan(secondFrame.getNetTime()));
    }

    // Test for start method
    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.start("TestClass", "testMethod");
    }

    // Test for start method with null arguments
    @Test(expected = NullPointerException.class)
    public void testStartWithNullArguments() {
        Profile.init();
        Profile.start(null, null);
    }

    // Test for end method with null arguments
    @Test(expected = NullPointerException.class)
    public void testEndWithNullArguments() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end(null, null);
    }

    // Test for unwind method with null arguments
    @Test(expected = NullPointerException.class)
    public void testUnwindWithNullArguments() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.unwind(null, null, null);
    }

    // Test for alloc method
    @Test
    public void testAlloc() {
        Profile.init();
        Profile.alloc("TestClass");
        ClassAllocation ca = Profile._allocList.get("TestClass");
        assertEquals(ca.getAllocationCount(), 1);
    }

    // Test for alloc method with existing allocation
    @Test
    public void testAllocWithExistingAllocation() {
        Profile.init();
        Profile.alloc("TestClass");
        Profile.alloc("TestClass");
        ClassAllocation ca = Profile._allocList.get("TestClass");
        assertEquals(ca.getAllocationCount(), 2);
    }

    // Test for run method
    @Test
    public void testRun() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        profile.run();
        assertTrue(threadDictionary.size() == 0);
        assertTrue(frameList.isEmpty());
    }
}
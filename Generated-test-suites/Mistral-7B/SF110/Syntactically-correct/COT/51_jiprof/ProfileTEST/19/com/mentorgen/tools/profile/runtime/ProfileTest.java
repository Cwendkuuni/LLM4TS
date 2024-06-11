package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.output.ProfileFormatter;
import com.mentorgen.tools.profile.runtime.Controller;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;
import com.mentorgen.tools.profile.runtime.ThreadDictionary.ThreadInfo;

public class ProfileTest {

    private Map<String, Method> methodDictionary;
    private Map<String, Frame> threadActiveFrame;
    private Map<String, ClassAllocation> allocList;
    private Frame frame;
    private ThreadInfo threadInfo;

    @Before
    public void setUp() {
        Profile.init();
        methodDictionary = new HashMap<String, Method>(2003);
        threadActiveFrame = new HashMap<Long, Frame>(1001);
        allocList = new HashMap<String, ClassAllocation>();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        // Check if profiler is initialized
        assertNotNull(Profile._controller);
        assertNotNull(Profile._controllerThread);
    }

    @Test
    public void testInit() {
        Profile.init();
        // Check if data structures are initialized
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start();
        // Add some frames
        start("TestClass", "testMethod");
        end("TestClass", "testMethod");
        start("TestClass", "testMethod2");
        end("TestClass", "testMethod2");
        Profile.clear();
        // Check if data is cleared
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
    }

    @Test
    public void testStartAndStop() {
        Profile.init();
        // Start profiling
        Profile.start();
        // Check if profiling is started
        assertTrue(Profile._controller.isRunning());
        // Stop profiling
        Profile.stop();
        // Check if profiling is stopped
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName("test.prof");
        // Check if file name is set
        assertEquals("test.prof", Profile._controller.getFileName());
    }

    @Test(expected = IllegalStateException.class)
    public void testShutdownWhileProfiling() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
    }

    @Test
    public void testThreads() {
        Profile.init();
        start("Thread1", "threadMethod1");
        start("Thread1", "threadMethod2");
        start("Thread2", "threadMethod1");
        end("Thread1", "threadMethod2");
        end("Thread1", "threadMethod1");
        end("Thread2", "threadMethod1");
        // Check threads
        List<Long> threads = new ArrayList<Long>(Profile.threads());
        assertEquals(2, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
        assertEquals(1, threads.get(1));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        start("TestClass", "testMethod");
        start("TestClass", "testMethod2");
        end("TestClass", "testMethod2");
        end("TestClass", "testMethod");
        // Check interactions
        List<Frame> interactions = new ArrayList<Frame>(Profile.interactions(Thread.currentThread().getId()));
        assertEquals(2, interactions.size());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        start("TestClass1", "testMethod1");
        start("TestClass1", "testMethod2");
        end("TestClass1", "testMethod2");
        end("TestClass1", "testMethod1");
        start("TestClass2", "testMethod1");
        start("TestClass2", "testMethod2");
        end("TestClass2", "testMethod2");
        end("TestClass2", "testMethod1");
        // Check frame list
        List<Frame> frameList = new ArrayList<Frame>(Profile.frameList());
        assertEquals(4, frameList.size());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        alloc("TestClass1");
        alloc("TestClass2");
        alloc("TestClass1");
        // Check allocations
        List<ClassAllocation> allocations = new ArrayList<ClassAllocation>(Profile.allocations());
        assertEquals(2, allocations.size());
        assertEquals("TestClass1", allocations.get(0).getClassname());
        assertEquals(3, allocations.get(0).getAllocationCount());
        assertEquals("TestClass2", allocations.get(1).getClassname());
        assertEquals(1, allocations.get(1).getAllocationCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        start("TestClass", "testMethod");
        Thread.yield();
        Thread.yield();
        end("TestClass", "testMethod");
        long threadId = Thread.currentThread().getId();
        long totalTime = Profile.getThreadTotalTime(threadId);
        // Check thread total time
        assertEquals(-1, totalTime);
        // Profile for some time
        start("TestClass", "testMethod");
        Thread.sleep(1000);
        end("TestClass", "testMethod");
        totalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(1000, totalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        start("TestClass1", "testMethod1");
        start("TestClass1", "testMethod2");
        end("TestClass1", "testMethod2");
        end("TestClass1", "testMethod1");
        start("TestClass2", "testMethod1");
        start("TestClass2", "testMethod2");
        end("TestClass2", "testMethod2");
        end("TestClass2", "testMethod1");
        // Sort frame list
        Collections.sort(Profile._frameList, new FrameComparator());
        // Check sorted frame list
        Frame prevFrame = null;
        for (Frame frame : Profile._frameList) {
            if (prevFrame != null) {
                assertTrue(frame.getBeginTime() > prevFrame.getEndTime());
            }
            prevFrame = frame;
        }
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        // Start and end method
        start(className, methodName);
        end(className, methodName);
        // Check if method is profiled
        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        // Check method overhead
        assertEquals(0, frame.getOverheadNanos());
        // Start and end method again
        start(className, methodName);
        end(className, methodName);
        frame = findFrame(className, methodName);
        assertEquals(2, frame.getOverheadCount());
        // Check method net time
        frame.computeNetTime();
        assertEquals(0, frame.getNetTimeNanos());
        // Start and end method with exception
        start(className, methodName);
        Thread.currentThread().interrupt();
        end(className, methodName);
        frame = findFrame(className, methodName);
        assertEquals(1, frame.getExceptionCount());
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        // Begin and end wait
        start(className, methodName);
        beginWait(className, methodName);
        endWait(className, methodName);
        end(className, methodName);
        // Check if method is profiled
        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        // Check method overhead
        assertEquals(0, frame.getOverheadNanos());
        // Check wait time
        assertEquals(0, frame.getWaitTimeNanos());
        // Begin and end wait with exception
        start(className, methodName);
        beginWait(className, methodName);
        Thread.currentThread().interrupt();
        endWait(className, methodName);
        end(className, methodName);
        frame = findFrame(className, methodName);
        assertEquals(1, frame.getExceptionCount());
    }

    @Test
    public void testUnwind() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        // Unwind method
        start(className, methodName);
        unwind(className, methodName, "TestException");
        end(className, methodName);
        // Check if method is profiled
        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        // Check method overhead
        assertEquals(1, frame.getOverheadCount());
        // Check exception count
        assertEquals(1, frame.getExceptionCount());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        String className = "TestClass";
        // Allocate class
        alloc(className);
        // Check if class is allocated
        ClassAllocation allocation = allocList.get(className);
        assertNotNull(allocation);
        // Allocate class again
        alloc(className);
        allocation = allocList.get(className);
        assertEquals(2, allocation.getAllocationCount());
    }

    private Frame findFrame(String className, String methodName) {
        for (Frame frame : Profile._frameList) {
            if (frame.getClassName().equals(className) && frame.getMethodName().equals(methodName)) {
                return frame;
            }
        }
        return null;
    }

    private static class FrameComparator implements Comparator<Frame> {
        @Override
        public int compare(Frame f1, Frame f2) {
            return Long.compare(f1.getBeginTime(), f2.getBeginTime());
        }
    }
}
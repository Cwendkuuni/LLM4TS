package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.runtime.Controller;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;
import com.mentorgen.tools.profile.runtime.ThreadDictionary.FrameComparator;
import com.mentorgen.tools.profile.runtime.ThreadDictionary.ThreadInfo;

public class ProfileTest {

    private static final String TEST_CLASS_NAME = "TestProfile";
    private static final String TEST_METHOD_NAME_1 = "testMethod1";
    private static final String TEST_METHOD_NAME_2 = "testMethod2";
    private static final String TEST_METHOD_NAME_3 = "testMethod3";

    private Controller _controller;
    private Profile _profile;
    private Thread _controllerThread;

    @Before
    public void setUp() {
        Profile.init();
        _controller = new Controller();
        _profile = new Profile();
        _controllerThread = new Thread(_controller);
        Profile.initProfiler();
    }

    @After
    public void tearDown() {
        Profile.shutdown();
    }

    @Test
    public void testInit() {
        // Check initial state
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._allocList.size());
        assertNull(_controllerThread);
        assertNull(_controller);
    }

    @Test
    public void testInitProfiler() {
        // Check profiler initialization
        assertEquals("Java Interactive Profiler: starting", System.err.getErrorStream().toString().trim());
        assertTrue(Profile._controller != null);
        if (Profile._controllerThread != null) {
            assertTrue(Profile._controllerThread.isAlive());
        }
    }

    @Test
    public void testClear() {
        // Test clearing
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testInitProfiler_ShutdownHook() {
        // Test profiler initialization with shutdown hook
        System.exit(0);
        // Check profiler initialization
        assertEquals("Java Interactive Profiler: starting", System.err.getErrorStream().toString().trim());
        assertTrue(Profile._controller != null);
        if (Profile._controllerThread != null) {
            assertTrue(Profile._controllerThread.isAlive());
        }
    }

    @Test
    public void testThreads() {
        // Test threads method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        List<Long> threads = Profile.threads();
        assertEquals(1, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
    }

    @Test
    public void testThreads_MultipleThreads() {
        // Test threads method with multiple threads
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_2);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_2);
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
        assertTrue(Arrays.asList(threads.toArray()).contains(Thread.currentThread().getId()));
        assertTrue(Arrays.asList(threads.toArray()).containsAll(Arrays.asList(t1.getId(), t2.getId())));
    }

    @Test
    public void testInteractions() {
        // Test interactions method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        List<Frame> frames = Profile.interactions(Thread.currentThread().getId());
        assertEquals(1, frames.size());
        Frame frame = frames.get(0);
        assertEquals(TEST_CLASS_NAME, frame.getClassName());
        assertEquals(TEST_METHOD_NAME_1, frame.getMethodName());
        assertNull(frame.getParent());
        assertEquals(0, frame.getNetTime());
    }

    @Test
    public void testFrameList() {
        // Test frameList method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_2);
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_2);
        List<Frame> frames = Profile.frameList();
        assertEquals(2, frames.size());

        Frame frame1 = frames.get(0);
        Frame frame2 = frames.get(1);

        assertEquals(TEST_CLASS_NAME, frame1.getClassName());
        assertEquals(TEST_METHOD_NAME_1, frame1.getMethodName());
        assertNull(frame1.getParent());
        assertEquals(0, frame1.getNetTime());

        assertEquals(TEST_CLASS_NAME, frame2.getClassName());
        assertEquals(TEST_METHOD_NAME_2, frame2.getMethodName());
        assertNull(frame2.getParent());
        assertEquals(0, frame2.getNetTime());

        assertNotEquals(frame1, frame2);
    }

    @Test
    public void testAllocations() {
        // Test allocations method
        Class<?> testClass = getClass();
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        new TestProfileAllocation(TEST_CLASS_NAME);
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertEquals(1, allocations.size());
        ClassAllocation allocation = allocations.get(0);
        assertEquals(TEST_CLASS_NAME, allocation.getClassname());
        assertEquals(1, allocation.getAllocationCount());
    }

    @Test
    public void testAllocations_MultipleClasses() {
        // Test allocations method with multiple classes
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        new TestProfileAllocation("test.TestClass1");
        new TestProfileAllocation("test.TestClass2");
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertEquals(2, allocations.size());
        Iterator<ClassAllocation> iterator = allocations.iterator();
        ClassAllocation allocation1 = iterator.next();
        ClassAllocation allocation2 = iterator.next();
        assertNotEquals(allocation1, allocation2);
        assertEquals("test.TestClass1", allocation1.getClassname());
        assertEquals(1, allocation1.getAllocationCount());
        assertEquals("test.TestClass2", allocation2.getClassname());
        assertEquals(1, allocation2.getAllocationCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        // Test getThreadTotalTime method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Thread.yield();
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        long threadId = Thread.currentThread().getId();
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertTrue(threadTotalTime > 0);
    }

    @Test
    public void testSortFrameList() {
        // Test sortFrameList method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_2);
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_3);
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_2);
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_3);
        Collections.sort(Profile._frameList, new FrameComparator());
        Frame frame1 = Profile._frameList.get(0);
        Frame frame2 = Profile._frameList.get(1);
        Frame frame3 = Profile._frameList.get(2);

        assertEquals(TEST_CLASS_NAME, frame1.getClassName());
        assertEquals(TEST_METHOD_NAME_1, frame1.getMethodName());
        assertNull(frame1.getParent());
        assertEquals(0, frame1.getNetTime());

        assertEquals(TEST_CLASS_NAME, frame2.getClassName());
        assertEquals(TEST_METHOD_NAME_2, frame2.getMethodName());
        assertNull(frame2.getParent());
        assertEquals(0, frame2.getNetTime());

        assertEquals(TEST_CLASS_NAME, frame3.getClassName());
        assertEquals(TEST_METHOD_NAME_3, frame3.getMethodName());
        assertNull(frame3.getParent());
        assertEquals(0, frame3.getNetTime());

        assertNotEquals(frame1, frame2);
        assertNotEquals(frame1, frame3);
        assertNotEquals(frame2, frame3);
    }

    @Test
    public void testStart() {
        // Test start method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Thread.yield();
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);

        Frame frame = Profile.findFrame(Thread.currentThread().getId(), TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        assertNotNull(frame);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStart_NullClass() {
        // Test start method with null class
        Profile.start(null, TEST_METHOD_NAME_1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStart_NullMethod() {
        // Test start method with null method
        Profile.start(TEST_CLASS_NAME, null);
    }

    @Test
    public void testEnd() {
        // Test end method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Thread.yield();
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);

        Frame frame = Profile.findFrame(Thread.currentThread().getId(), TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        assertNotNull(frame);
        assertTrue(frame.isEnded());
    }

    @Test
    public void testBeginWait() {
        // Test beginWait method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.beginWait(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Thread.yield();
        Profile.endWait(TEST_CLASS_NAME, TEST_METHOD_NAME_1);

        Frame frame = Profile.findFrame(Thread.currentThread().getId(), TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        assertNotNull(frame);
        assertTrue(frame.isWaiting());
    }

    @Test
    public void testEndWait() {
        // Test endWait method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.beginWait(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Thread.yield();
        Profile.endWait(TEST_CLASS_NAME, TEST_METHOD_NAME_1);

        Frame frame = Profile.findFrame(Thread.currentThread().getId(), TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        assertNotNull(frame);
        assertFalse(frame.isWaiting());
    }

    @Test
    public void testUnwind() {
        // Test unwind method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Thread.yield();
        try {
            throw new RuntimeException("Test exception");
        } catch (RuntimeException e) {
            Profile.unwind(TEST_CLASS_NAME, TEST_METHOD_NAME_1, e.getClass().getName());
        }
        Thread.yield();
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        assertNotNull(frame);
        assertTrue(frame.isExceptionCaught());
    }

    @Test
    public void testAlloc() {
        // Test alloc method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        new TestProfileAllocation(TEST_CLASS_NAME);
        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME_1);

        Map<String, ClassAllocation> allocations = Profile._allocList;
        assertNotNull(allocations);
        ClassAllocation allocation = allocations.get(TEST_CLASS_NAME);
        assertNotNull(allocation);
        assertEquals(1, allocation.getAllocationCount());
    }

    @Test
    public void testShutdown() {
        // Test shutdown method
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.shutdown();
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        assertNull(frame);
    }

    @Test(expected = IllegalStateException.class)
    public void testShutdown_AlreadyShutdown() {
        // Test shutdown method when already shutdown
        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME_1);
        Profile.shutdown();
        Profile.shutdown();
    }

    @Test
    public void testController() {
        // Test Controller class
        Controller controller = _controller;
        assertNotNull(controller);
        assertEquals(false, controller.isProfileEnabled());

        Profile.initProfiler();
        controller = _controller;
        assertTrue(controller.isProfileEnabled());

        Profile.shutdown();
        controller = _controller;
        assertFalse(controller.isProfileEnabled());

        _controller = null;
        controller = _controller;
        assertNull(controller);
    }

    private static class TestProfileAllocation {
        TestProfileAllocation(String className) {
            new String(className);
        }
    }
}
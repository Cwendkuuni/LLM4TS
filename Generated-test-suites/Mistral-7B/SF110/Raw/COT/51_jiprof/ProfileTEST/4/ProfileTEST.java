package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.output.ProfileFormatter;
import com.mentorgen.tools.profile.runtime.Controller;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

    private static final String FILENAME = "test.prof";
    private static final boolean DEBUG_START = false;
    private static final boolean DEBUG_EXCEPTION = false;
    private Controller controller;
    private ThreadDictionary threadDictionary;
    private Profile profile;

    @Before
    public void setUp() {
        Profile.init();
        controller = new Controller();
        threadDictionary = new ThreadDictionary();
        profile = new Profile();
        Profile.setFileName(FILENAME);
    }

    @After
    public void tearDown() {
        Profile.clear();
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
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
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
        Profile.init();
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
        Profile.setFileName("test.prof");
        assertEquals("test.prof", Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        assertFalse(controller.isRunning());
        assertFalse(Profile._controllerThread.isAlive());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        List<Long> threads = new ArrayList<>(Profile.threads());
        assertEquals(1, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        List<Frame> interactions = new ArrayList<>(Profile.interactions(Thread.currentThread().getId()));
        assertEquals(1, interactions.size());
        Frame interaction = interactions.get(0);
        assertEquals("TestClass", interaction.getClassName());
        assertEquals("testMethod", interaction.getMethodName());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        List<Frame> frameList = new ArrayList<>(Profile.frameList());
        assertEquals(1, frameList.size());
        Frame frame = frameList.get(0);
        assertEquals("TestClass", frame.getClassName());
        assertEquals("testMethod", frame.getMethodName());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start("TestClass");
        Profile.alloc("TestClass");
        List<ClassAllocation> allocations = new ArrayList<>(Profile.allocations());
        assertEquals(1, allocations.size());
        ClassAllocation allocation = allocations.get(0);
        assertEquals("TestClass", allocation.getClassName());
        assertEquals(1, allocation.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        long threadTotalTime = Profile.getThreadTotalTime(Thread.currentThread().getId());
        assertTrue(threadTotalTime > 0);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), 1L);
        Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), 2L);
        Frame frame3 = new Frame(null, new Method("TestClass1", "testMethod1"), 1L);
        Profile._frameList.add(frame1);
        Profile._frameList.add(frame2);
        Profile._frameList.add(frame3);
        Collections.sort(Profile._frameList, new Comparator<Frame>() {
            @Override
            public int compare(Frame o1, Frame o2) {
                return Long.compare(o1.getBeginTime(), o2.getBeginTime());
            }
        });
        List<Frame> sortedFrameList = new ArrayList<>(Profile._frameList);
        assertEquals(3, sortedFrameList.size());
        Frame firstFrame = sortedFrameList.get(0);
        assertEquals(frame1, firstFrame);
        Frame secondFrame = sortedFrameList.get(1);
        assertEquals(frame3, secondFrame);
        Frame thirdFrame = sortedFrameList.get(2);
        assertEquals(frame2, thirdFrame);
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Thread.yield();
        Profile.end(className, methodName);

        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Thread.yield();
        Profile.beginWait(className, methodName);
        Thread.yield();
        Profile.endWait(className, methodName);

        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        assertTrue(frame.isWaiting());
    }

    @Test
    public void testUnwind() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        String exception = "TestException";
        Profile.start(className, methodName);
        Profile.unwind(className, methodName, exception);

        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        assertTrue(frame.isException());
        assertEquals(exception, frame.getException());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        String className = "TestClass";
        Profile.start(className, "<init>");
        Profile.alloc(className);

        ClassAllocation allocation = findClassAllocation(className);
        assertNotNull(allocation);
        assertEquals(1, allocation.getAllocCount());
    }

    @Test
    public void testRun() {
        Profile.init();
        Profile.start();
        Profile.shutdown();

        try {
            profile.run();
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            // expected
        }
    }

    private Frame findFrame(String className, String methodName) {
        Frame frame = null;
        for (Frame f : frameList()) {
            if (f.getClassName().equals(className) && f.getMethodName().equals(methodName)) {
                frame = f;
                break;
            }
        }
        return frame;
    }

    private ClassAllocation findClassAllocation(String className) {
        for (ClassAllocation ca : allocations()) {
            if (ca.getClassName().equals(className)) {
                return ca;
            }
        }
        return null;
    }

}
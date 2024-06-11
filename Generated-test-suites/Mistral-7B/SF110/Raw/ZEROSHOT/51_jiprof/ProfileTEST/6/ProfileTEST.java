package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

    private static final String CLASS_NAME = "com.test.TestClass";
    private static final String METHOD_NAME = "testMethod";
    private static final long THREAD_ID = 1;

    private Profile _profile;

    @Before
    public void setUp() {
        Profile.init();
        _profile = new Profile();
    }

    @Test
    public void testInit() {
        assertNull(_profile._threadDictionary);
        assertNull(_profile._threadActiveFrame);
        assertNull(_profile._methodDictionary);
        assertNull(_profile._frameList);
        assertNull(_profile._lock);
        assertNull(_profile._allocList);
        assertNull(_profile._controller);
        assertNull(_profile._controllerThread);
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
    public void testClear() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.end(CLASS_NAME, METHOD_NAME);
        Profile.clear();
        assertNull(_profile._threadDictionary);
        assertNull(_profile._threadActiveFrame);
        assertNull(_profile._methodDictionary);
        assertNull(_profile._frameList);
        assertNull(_profile._lock);
        assertNull(_profile._allocList);
        assertNull(_profile._controller);
        assertNull(_profile._controllerThread);
    }

    @Test
    public void testStart() {
        Profile.init();
        _profile.start();
        assertTrue(_profile._controller != null);
        if (_profile._controllerThread != null) {
            assertTrue(_profile._controllerThread.isAlive());
        }
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        _profile.stop();
        assertFalse(_profile._controller.isRunning());
        assertNull(_profile._controllerThread);
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        _profile.setFileName("output.txt");
        assertEquals("output.txt", Profile._controller.getFileName());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        List<Long> threads = Profile.threads();
        assertTrue(threads.size() > 0);
        assertTrue(threads.contains(THREAD_ID));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.end(CLASS_NAME, METHOD_NAME);
        List<Frame> interactions = Profile.interactions(THREAD_ID);
        assertTrue(interactions.size() > 0);
        Frame interaction = interactions.get(0);
        assertNotNull(interaction.getClassName());
        assertNotNull(interaction.getMethodName());
        assertNotNull(interaction.getBeginTime());
        assertNotNull(interaction.getEndTime());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.end(CLASS_NAME, METHOD_NAME);
        List<Frame> frameList = Profile.frameList();
        assertTrue(frameList.size() > 0);
        Frame frame = frameList.get(0);
        assertNotNull(frame.getClassName());
        assertNotNull(frame.getMethodName());
        assertNotNull(frame.getBeginTime());
        assertNotNull(frame.getEndTime());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.alloc(CLASS_NAME);
        Profile.alloc(CLASS_NAME);
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertEquals(2, allocations.size());
        ClassAllocation allocation = allocations.get(0);
        assertEquals(CLASS_NAME, allocation.getClassName());
        assertEquals(2, allocation.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.end(CLASS_NAME, METHOD_NAME);
        long threadTotalTime = Profile.getThreadTotalTime(THREAD_ID);
        assertTrue(threadTotalTime > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSortFrameListWithNullComparator() {
        Profile.init();
        Profile.sortFrameList(null);
    }

    @Test
    public void testStartEndMethods() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.end(CLASS_NAME, METHOD_NAME);
        Frame frame = Profile.findFrame(THREAD_ID, CLASS_NAME, METHOD_NAME);
        assertNotNull(frame);
        assertTrue(frame.isMethodCalled());
    }

    @Test
    public void testBeginWaitEndWaitMethods() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.beginWait(CLASS_NAME, "beginWaitMethod");
        Profile.endWait(CLASS_NAME, "endWaitMethod");
        Frame frame = Profile.findFrame(THREAD_ID, CLASS_NAME, METHOD_NAME);
        assertNotNull(frame);
        assertTrue(frame.isWaiting());
    }

    @Test
    public void testUnwindMethods() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.unwind(CLASS_NAME, "unwindMethod", "TestException");
        Frame frame = Profile.findFrame(THREAD_ID, CLASS_NAME, METHOD_NAME);
        assertNotNull(frame);
        assertTrue(frame.isExceptionThrown());
    }

    @Test
    public void testAllocMethod() {
        Profile.init();
        Profile.alloc(CLASS_NAME);
        ClassAllocation allocation = Profile._allocList.get(CLASS_NAME);
        assertEquals(1, allocation.getAllocCount());
    }
}
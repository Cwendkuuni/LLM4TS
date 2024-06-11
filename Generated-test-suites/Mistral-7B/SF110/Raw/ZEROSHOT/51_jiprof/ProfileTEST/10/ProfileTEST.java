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
    public void setUp() {
        Profile.init();
        profile = new Profile();
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
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "init");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "init");
        Profile.clear();
        assertTrue(Profile._frameList.isEmpty());
        assertTrue(Profile._threadActiveFrame.isEmpty());
    }

    @Test
    public void testInit() {
        assertNull(Profile._threadDictionary);
        assertNull(Profile._threadActiveFrame);
        assertNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNull(Profile._lock);
        assertNull(Profile._allocList);
        assertNull(Profile._controller);
        assertNull(Profile._controllerThread);
        Profile.init();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertNull(Profile._controller);
        assertNull(Profile._controllerThread);
    }

    @Test
    public void testStart() {
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void testStop() {
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "init");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "init");
        Profile.stop();
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        Profile.setFileName("test.txt");
        assertEquals("test.txt", Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.shutdown();
        assertTrue(Profile._controller.isClosed());
    }

    @Test
    public void testThreads() {
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "init");
        List<Long> threads = new ArrayList<>(Profile.threads());
        assertEquals(2, threads.size());
        assertFalse(threads.isEmpty());
    }

    @Test
    public void testInteractions() {
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "init");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "init");
        List<Frame> interactions = new ArrayList<>(Profile.interactions(Thread.currentThread().getId()));
        assertEquals(2, interactions.size());
        assertFalse(interactions.isEmpty());
    }

    @Test
    public void testFrameList() {
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "init");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "init");
        List<Frame> frameList = new ArrayList<>(Profile.frameList());
        assertEquals(2, frameList.size());
        assertFalse(frameList.isEmpty());
    }

    @Test
    public void testAllocations() {
        Profile.alloc("java.lang.String");
        Profile.alloc("java.util.ArrayList");
        List<ClassAllocation> allocations = new ArrayList<>(Profile.allocations());
        assertEquals(2, allocations.size());
        assertFalse(allocations.isEmpty());
        ClassAllocation stringAllocation = allocations.get(0);
        assertEquals("java.lang.String", stringAllocation.getClassName());
        ClassAllocation arrayListAllocation = allocations.get(1);
        assertEquals("java.util.ArrayList", arrayListAllocation.getClassName());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "init");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "init");
        long threadTotalTime = Profile.getThreadTotalTime(Thread.currentThread().getId());
        assertTrue(threadTotalTime > 0);
    }

    @Test
    public void testSortFrameList() {
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.start("com.mentorgen.tools.profile.runtime.Profile", "init");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "<init>");
        Profile.end("com.mentorgen.tools.profile.runtime.Profile", "init");
        Collections.sort(Profile.frameList());
        Frame firstFrame = Profile.frameList().get(0);
        Frame lastFrame = Profile.frameList().get(Profile.frameList().size() - 1);
        Frame initFrame = Profile.findFrame(Thread.currentThread().getId(), "com.mentorgen.tools.profile.runtime.Profile", "init");
        Frame initEndFrame = Profile.findFrame(Thread.currentThread().getId(), "com.mentorgen.tools.profile.runtime.Profile", "init");
        assertEquals(firstFrame, initFrame);
        assertEquals(lastFrame, initEndFrame);
    }

    @Test
    public void testStartEndMethod() {
        Profile.start("com.mentorgen.tools.profile.runtime.ProfileTest", "testStartEndMethod");
        Thread.yield();
        Thread.yield();
        Profile.end("com.mentorgen.tools.profile.runtime.ProfileTest", "testStartEndMethod");
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), "com.mentorgen.tools.profile.runtime.ProfileTest", "testStartEndMethod");
        assertNotNull(frame);
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.start("com.mentorgen.tools.profile.runtime.ProfileTest", "testBeginWaitEndWait");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Profile.endWait("com.mentorgen.tools.profile.runtime.ProfileTest", "testBeginWaitEndWait");
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), "com.mentorgen.tools.profile.runtime.ProfileTest", "testBeginWaitEndWait");
        assertNotNull(frame);
    }

    @Test
    public void testUnwind() {
        try {
            throw new Exception("Test exception");
        } catch (Exception e) {
            Profile.unwind("com.mentorgen.tools.profile.runtime.ProfileTest", "testUnwind", e.getMessage());
        }
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), "com.mentorgen.tools.profile.runtime.ProfileTest", "testUnwind");
        assertNotNull(frame);
    }

    @Test
    public void testAlloc() {
        String[] arr = new String[5];
        Profile.alloc("java.lang.String[]");
        ClassAllocation classAllocation = Profile.findClassAllocation("java.lang.String[]");
        assertEquals(1, classAllocation.getAllocCount());
    }

}
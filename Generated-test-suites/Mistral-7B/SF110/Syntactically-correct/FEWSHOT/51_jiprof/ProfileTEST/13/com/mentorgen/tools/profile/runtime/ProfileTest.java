package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
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
        if (Profile._controllerThread != null) {
            assertTrue(Profile._controllerThread.isAlive());
        }
    }

    @Test
    public void testClear() {
        Profile.initProfiler();
        Profile.start();
        Profile.end("AnyClass", "anyMethod");
        Profile.clear();
        assertTrue(Profile._frameList.isEmpty());
        assertTrue(Profile._threadActiveFrame.isEmpty());
    }

    @Test
    public void testThreads() {
        Profile.initProfiler();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        List<Long> threads = Profile.threads();
        assertFalse(threads.isEmpty());
        assertTrue(threads.contains(threadId));
    }

    @Test
    public void testInteractions() {
        Profile.initProfiler();
        Profile.start();
        Profile.start("AnyClass", "anyMethod");
        Profile.end("AnyClass", "anyMethod");
        List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
        assertEquals(1, interactions.size());
    }

    @Test
    public void testStart() {
        Profile.initProfiler();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        List<Frame> frames = Profile.frameList();
        Frame frame = findFrame(frames, threadId, "AnyClass", "anyMethod");
        assertNotNull(frame);
    }

    @Test
    public void testEnd() {
        Profile.initProfiler();
        Profile.start();
        Profile.start("AnyClass", "anyMethod");
        Profile.end("AnyClass", "anyMethod");
        List<Frame> frames = Profile.frameList();
        Frame frame = findFrame(frames, Thread.currentThread().getId(), "AnyClass", "anyMethod");
        assertTrue(frame.isEnded());
    }

    @Test
    public void testAlloc() {
        Profile.initProfiler();
        Profile.start();
        Profile.alloc("AnyClass");
        Map<String, ClassAllocation> allocList = Profile._allocList;
        ClassAllocation ca = allocList.get("AnyClass");
        assertEquals(1, ca.getAllocCount());
    }

    @Test
    public void testFindFrame() {
        Profile.initProfiler();
        Profile.start();
        Profile.start("AnyClass1", "anyMethod1");
        Profile.end("AnyClass1", "anyMethod1");
        Profile.start("AnyClass2", "anyMethod2");
        Profile.end("AnyClass2", "anyMethod2");
        List<Frame> frames = Profile.frameList();
        Frame frame1 = findFrame(frames, Thread.currentThread().getId(), "AnyClass1", "anyMethod1");
        Frame frame2 = findFrame(frames, Thread.currentThread().getId(), "AnyClass2", "anyMethod2");
        assertNotNull(frame1);
        assertNotNull(frame2);
        assertEquals(frame1.getBeginTime(), frame1.getStartTime());
        assertEquals(frame2.getBeginTime(), frame2.getStartTime());
    }

    @Test
    public void testBeginWait() {
        Profile.initProfiler();
        Profile.start();
        String className = "AnyClass";
        String methodName = "anyMethod";
        Profile.beginWait(className, methodName);
        Profile.endWait(className, methodName);
        Frame frame = findFrame(Profile.frameList(), Thread.currentThread().getId(), className, methodName);
        assertTrue(frame.isWaiting());
    }

    @Test
    public void testEndWait() {
        Profile.initProfiler();
        Profile.start();
        String className = "AnyClass";
        String methodName = "anyMethod";
        Profile.beginWait(className, methodName);
        Profile.endWait(className, methodName);
        Frame frame = findFrame(Profile.frameList(), Thread.currentThread().getId(), className, methodName);
        assertFalse(frame.isWaiting());
    }

    @Test
    public void testUnwind() {
        Profile.initProfiler();
        Profile.start();
        String className = "AnyClass";
        String methodName = "anyMethod";
        String exception = "AnyException";
        Profile.unwind(className, methodName, exception);
        Frame frame = findFrame(Profile.frameList(), Thread.currentThread().getId(), className, methodName);
        assertNotNull(frame);
        assertEquals(exception, frame.getException());
    }

    private Frame findFrame(List<Frame> frames, long threadId, String className, String methodName) {
        for (Frame frame : frames) {
            if (frame.getThreadId() == threadId && frame.getClassName().equals(className) && frame.getMethodName().equals(methodName)) {
                return frame;
            }
        }
        return null;
    }

    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName("output.txt");
        assertEquals("output.txt", Profile._controller.getFileName());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.initProfiler();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        Profile.end("AnyClass", "anyMethod");
        long totalTime = Profile.getThreadTotalTime(threadId);
        assertTrue(totalTime > 0);
    }

    @Test
    public void testSortFrameList() {
        Profile.initProfiler();
        Profile.start();
        Profile.start("AnyClass1", "anyMethod1");
        Profile.end("AnyClass1", "anyMethod1");
        Profile.start("AnyClass2", "anyMethod2");
        Profile.end("AnyClass2", "anyMethod2");
        Profile.sortFrameList(Comparator.comparingLong(Frame::getStartTime));
        List<Frame> frames = Profile.frameList();
        Frame frame1 = frames.get(0);
        Frame frame2 = frames.get(1);
        assertTrue(frame1.getStartTime() < frame2.getStartTime());
    }
}
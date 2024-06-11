package com.mentorgen.tools.profile.runtime; // Make sure to import necessary classes

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.*;

import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ProfileTest {

    private static final String CLASS_NAME = "com.example.TestClass";
    private static final String METHOD_NAME = "testMethod";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Profile.init();
    }

    

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controllerThread.isAlive());
    }

    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.end(CLASS_NAME, METHOD_NAME);
        Profile.clear();
        assertEquals(0, Profile.threads().size());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName("test.txt");
        assertEquals("test.txt", Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.end(CLASS_NAME, METHOD_NAME);
        List<Long> threads = new ArrayList<>(Profile.threads());
        assertEquals(1, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.end(CLASS_NAME, METHOD_NAME);
        List<Frame> interactions = new ArrayList<>(Profile.interactions(Thread.currentThread().getId()));
        assertEquals(1, interactions.size());
        Frame interaction = interactions.get(0);
        assertEquals(CLASS_NAME, interaction.getClassName());
        assertEquals(METHOD_NAME, interaction.getMethodName());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), Thread.currentThread().getId());
        Frame frame2 = new Frame(frame1, new Method(CLASS_NAME, "<init>"), Thread.currentThread().getId());
        Profile._frameList.add(frame1);
        Profile._frameList.add(frame2);
        List<Frame> frameList = new ArrayList<>(Profile.frameList());
        assertEquals(2, frameList.size());
        Frame listFrame1 = frameList.get(0);
        Frame listFrame2 = frameList.get(1);
        assertEquals(frame1, listFrame1);
        assertEquals(frame2, listFrame2);
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.alloc(CLASS_NAME);
        ClassAllocation allocation = Profile._allocList.get(CLASS_NAME);
        assertEquals(1, allocation.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), Thread.currentThread().getId());
        Frame frame2 = new Frame(frame1, new Method(CLASS_NAME, "<init>"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), frame1);
        long threadId = new Thread(this::testGetThreadTotalTime).start().getId();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Frame threadFrame = new Frame(frame1, new Method(getClass(), "testGetThreadTotalTime"), threadId);
        Profile._threadDictionary.add(threadId, threadFrame);
        Profile._threadDictionary.remove(Thread.currentThread().getId());
        long totalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(2 * System.nanoTime() / 2, totalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), Thread.currentThread().getId());
        Frame frame2 = new Frame(null, new Method(CLASS_NAME, "testSortFrameList"), Thread.currentThread().getId());
        Frame frame3 = new Frame(frame1, new Method(CLASS_NAME, "testSortFrameList"), Thread.currentThread().getId());
        Profile._frameList.add(frame3);
        Profile._frameList.add(frame2);
        Profile._frameList.add(frame1);
        Comparator<Frame> comp = (frame1, frame2) -> Long.compare(frame1.getBeginTime(), frame2.getBeginTime());
        Profile.sortFrameList(comp);
        List<Frame> sortedFrameList = new ArrayList<>(Profile.frameList());
        assertEquals(3, sortedFrameList.size());
        Frame sortedFrame1 = sortedFrameList.get(0);
        Frame sortedFrame2 = sortedFrameList.get(1);
        Frame sortedFrame3 = sortedFrameList.get(2);
        assertEquals(frame1, sortedFrame1);
        assertEquals(frame2, sortedFrame2);
        assertEquals(frame3, sortedFrame3);
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        long startTime = System.nanoTime();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.end(CLASS_NAME, METHOD_NAME);
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), Thread.currentThread().getId());
        frame.setBeginTime(startTime);
        frame.setEndTime(startTime + 1);
        Frame testFrame = new Frame(frame, new Method(getClass(), "testStartEnd"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), testFrame);
        Profile._threadDictionary.remove(Thread.currentThread().getId());
        Frame actualFrame = findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
        assertEquals(frame, actualFrame);
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        long startTime = System.nanoTime();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.beginWait(CLASS_NAME, METHOD_NAME);
        Profile.endWait(CLASS_NAME, METHOD_NAME);
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), Thread.currentThread().getId());
        frame.setBeginTime(startTime);
        frame.beginWait(startTime + 1);
        frame.setEndTime(startTime + 2);
        Frame testFrame = new Frame(frame, new Method(getClass(), "testBeginWaitEndWait"), Thread.currentThread().getId());
        Profile._threadDictionary.add(Thread.currentThread().getId(), testFrame);
        Profile._threadDictionary.remove(Thread.currentThread().getId());
        Frame actualFrame = findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
        assertEquals(frame, actualFrame);
    }

    @Test
    public void testUnwind() {
        Profile.init();
        try {
            throw new RuntimeException("Test exception");
        } catch (RuntimeException e) {
            Profile.unwind(CLASS_NAME, METHOD_NAME, e.getClass().getName());
        }
        Frame testFrame = new Frame(null, new Method(getClass(), "testUnwind"), Thread.currentThread().getId());
        Profile._threadActiveFrame.put(Thread.currentThread().getId(), testFrame);
        Frame actualFrame = findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
        assertEquals(testFrame, actualFrame);
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.alloc(CLASS_NAME);
        ClassAllocation allocation = Profile._allocList.get(CLASS_NAME);
        assertEquals(1, allocation.getAllocCount());
    }

    @Test
    public void testRun() {
        Profile.init();
        Profile _profile = mock(Profile.class);
        Profile p = new Profile();
        when(_profile.isRunning()).thenReturn(true);
        when(_profile.shutdown()).thenThrow(new IOException());
        when(_profile.getFileName()).thenReturn("test.txt");
        when(_profile.close()).thenReturn(true);
        when(_profile.threads()).thenReturn(Collections.singletonList(Thread.currentThread().getId()));
        when(Profile._controller.isRunning()).thenReturn(true);
        when(Profile._controller.close()).thenReturn(true);
        when(Profile._controllerThread.isAlive()).thenReturn(true);
        when(Profile._controllerThread.interrupt()).thenReturn(true);
        when(Profile._threadActiveFrame.get(Thread.currentThread().getId())).thenReturn(null);
        Profile.initProfiler();
        p.run();
        verify(_profile, times(1)).shutdown();
    }

    

    private Frame findFrame(long threadId, String className, String methodName) {
        Frame testFrame = Profile._threadActiveFrame.get(threadId);
        if (testFrame == null) {
            for (Frame frame : Profile._frameList) {
                if (frame.getClassName().equals(className) && frame.getMethodName().equals(methodName) && frame.getThreadId() == threadId) {
                    return frame;
                }
            }
        }
        return testFrame;
    }
}
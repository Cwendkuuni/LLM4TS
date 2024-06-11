package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

    private static final String CLASS_NAME_1 = "com.mentorgen.tools.profile.runtime.ProfileTest_Class1";
    private static final String CLASS_NAME_2 = "com.mentorgen.tools.profile.runtime.ProfileTest_Class2";
    private static final String METHOD_NAME_1 = "testMethod1";
    private static final String METHOD_NAME_2 = "testMethod2";
    private static final String FILENAME = "test.prof";

    private static Profile _profile;
    private static Controller _controller;

    @Before
    public void setUp() {
        Profile.init();
        _controller = new Controller();
        _profile = new Profile();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
    }

    @Test(expected = IllegalStateException.class)
    public void testInitProfilerTwice() {
        Profile.initProfiler();
        Profile.initProfiler();
    }

    @Test
    public void testInit() {
        Profile.init();
        assertFalse(Profile._controllerThread.isAlive());
        assertFalse(Controller._profile);
        assertNull(Profile._threadActiveFrame);
        assertNull(Profile._threadDictionary);
        assertNull(Profile._methodDictionary);
        assertNull(Profile._frameList);
        assertNull(Profile._lock);
        assertNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        assertFalse(Profile._threadActiveFrame.containsKey(Thread.currentThread().getId()));
        assertFalse(Profile._threadDictionary.size() > 0);
        assertFalse(Profile._frameList.size() > 0);
        assertFalse(Profile._allocList.size() > 0);
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME_1, METHOD_NAME_1);
        assertNotNull(frame);
        assertFalse(frame.isEnded());
    }

    @Test
    public void testEnd() {
        Profile.init();
        Profile.start();
        Frame frame = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        Profile.end(CLASS_NAME_1, METHOD_NAME_1);
        assertNotNull(frame);
        assertTrue(frame.isEnded());
    }

    @Test
    public void testStartEndWithSameMethodAndClassName() {
        Profile.init();
        Profile.start();
        Frame parent = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        Frame child = Profile.end(CLASS_NAME_1, METHOD_NAME_1);
        assertSame(parent, child);
    }

    @Test
    public void testStartEndWithDifferentMethodAndClassName() {
        Profile.init();
        Profile.start();
        Frame parent = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        Frame child = Profile.end(CLASS_NAME_2, METHOD_NAME_2);
        assertNotSame(parent, child);
    }

    @Test
    public void testFindFrame() {
        Profile.init();
        Frame parent = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        Frame child = Profile.end(CLASS_NAME_1, METHOD_NAME_1);
        Frame result = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME_1, METHOD_NAME_1);
        assertSame(child, result);
    }

    @Test(expected = NullPointerException.class)
    public void testFindFrameWithNullClassName() {
        Profile.init();
        Frame result = Profile.findFrame(Thread.currentThread().getId(), null, METHOD_NAME_1);
    }

    @Test(expected = NullPointerException.class)
    public void testFindFrameWithNullMethodName() {
        Profile.init();
        Frame result = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME_1, null);
    }

    @Test
    public void testStartWithDifferentThread() {
        Profile.init();
        Thread thread = new Thread() {
            @Override
            public void run() {
                Profile.start();
                Profile.end(CLASS_NAME_1, METHOD_NAME_1);
            }
        };
        thread.start();
        Frame result = Profile.findFrame(thread.getId(), CLASS_NAME_1, METHOD_NAME_1);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFrameWithInvalidThreadId() {
        Profile.init();
        Frame result = Profile.findFrame(-1, CLASS_NAME_1, METHOD_NAME_1);
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName(FILENAME);
        assertEquals(FILENAME, Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME_1, METHOD_NAME_1);
        assertNull(frame);
        assertFalse(Profile._threadActiveFrame.containsKey(Thread.currentThread().getId()));
        assertFalse(Profile._threadDictionary.size() > 0);
        assertFalse(Profile._frameList.size() > 0);
        assertFalse(Profile._allocList.size() > 0);
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        Frame frame1 = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        Frame frame2 = Profile.end(CLASS_NAME_1, METHOD_NAME_1);
        List<Frame> frames = new ArrayList<Frame>(Profile.frameList());
        assertEquals(2, frames.size());
        assertEquals(frame1, frames.get(0));
        assertEquals(frame2, frames.get(1));
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        Set<Long> threads = new HashSet<Long>(Profile.threads());
        assertEquals(2, threads.size());
        threads.remove(Thread.currentThread().getId());
        assertFalse(threads.contains(Thread.currentThread().getId()));
        assertFalse(threads.isEmpty());
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        Frame frame1 = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        Frame frame2 = Profile.end(CLASS_NAME_1, METHOD_NAME_1);
        List<Frame> interactions = new ArrayList<Frame>(Profile.interactions(Thread.currentThread().getId()));
        assertEquals(2, interactions.size());
        assertEquals(frame1, interactions.get(0));
        assertEquals(frame2, interactions.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInteractionsWithInvalidThreadId() {
        Profile.init();
        Profile.start();
        List<Frame> interactions = new ArrayList<Frame>(Profile.interactions(-1));
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        Profile.alloc(CLASS_NAME_1);
        Profile.alloc(CLASS_NAME_1);
        List<ClassAllocation> allocations = new ArrayList<ClassAllocation>(Profile._allocList.values());
        assertEquals(1, allocations.size());
        assertEquals(2, allocations.get(0).getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        Frame frame1 = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        Thread.yield();
        Frame frame2 = Profile.end(CLASS_NAME_1, METHOD_NAME_1);
        long threadId = Thread.currentThread().getId();
        long time1 = Profile.getThreadTotalTime(threadId);
        Frame frame3 = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        Thread.yield();
        Frame frame4 = Profile.end(CLASS_NAME_1, METHOD_NAME_1);
        long time2 = Profile.getThreadTotalTime(threadId);
        assertEquals(time1 + frame1.getNetTime() + frame2.getNetTime(), time2 + frame3.getNetTime() + frame4.getNetTime());
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        Frame frame1 = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        Frame frame2 = Profile.start(CLASS_NAME_1, METHOD_NAME_2);
        Frame frame3 = Profile.end(CLASS_NAME_1, METHOD_NAME_2);
        Frame frame4 = Profile.end(CLASS_NAME_1, METHOD_NAME_1);
        Profile.sortFrameList(new Comparator<Frame>() {

            @Override
            public int compare(Frame o1, Frame o2) {
                return (int) (o1.getBeginTime() - o2.getBeginTime());
            }
        });
        Iterator<Frame> iterator = Profile._frameList.iterator();
        Frame first = iterator.next();
        Frame second = iterator.next();
        Frame third = iterator.next();
        Frame fourth = iterator.next();
        assertEquals(frame1, first);
        assertEquals(frame2, second);
        assertEquals(frame3, third);
        assertEquals(frame4, fourth);
    }

    @Test
    public void testBeginWait() {
        Profile.init();
        Profile.start();
        Frame frame = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        long start = System.nanoTime();
        Profile.beginWait(CLASS_NAME_1, METHOD_NAME_1);
        long end = System.nanoTime();
        frame.overhead(end - start);
        assertTrue(frame.isWaiting());
    }

    @Test
    public void testEndWait() {
        Profile.init();
        Profile.start();
        Frame frame = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        long start = System.nanoTime();
        Profile.beginWait(CLASS_NAME_1, METHOD_NAME_1);
        long waitTime = System.nanoTime() - start;
        Profile.endWait(CLASS_NAME_1, METHOD_NAME_1);
        frame.overhead(waitTime);
        assertFalse(frame.isWaiting());
    }

    @Test
    public void testUnwind() {
        Profile.init();
        Profile.start();
        Frame frame = Profile.start(CLASS_NAME_1, METHOD_NAME_1);
        try {
            throw new Exception();
        } catch (Exception e) {
            Profile.unwind(CLASS_NAME_1, METHOD_NAME_1, e.getClass().getName());
        }
        assertFalse(Profile._threadActiveFrame.containsKey(Thread.currentThread().getId()));
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start();
        Profile.alloc(CLASS_NAME_1);
        Profile.alloc(CLASS_NAME_1);
        ClassAllocation ca = Profile._allocList.get(CLASS_NAME_1);
        assertEquals(2, ca.getAllocCount());
    }

    @Test
    public void testRun() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
    }
}
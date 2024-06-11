package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

    private static Profile _profile;
    private static Frame _frame1;
    private static Frame _frame2;
    private static Frame _frame3;
    private static Frame _frame4;
    private static ClassAllocation _classAllocation;

    @Before
    public void setUp() {
        Profile.init();
        _profile = new Profile();
        _frame1 = new Frame(null, "ClassName1", "MethodName1", 1L);
        _frame2 = new Frame(_frame1, "ClassName2", "MethodName2", 2L);
        _frame3 = new Frame(_frame2, "ClassName1", "MethodName1", 3L);
        _frame4 = new Frame(_frame1, "ClassName3", "MethodName3", 4L);
        _classAllocation = new ClassAllocation("ClassName");
    }

    @After
    public void tearDown() {
        Profile.clear();
    }

    @Test
    public void testThreads() {
        List<Long> threads = Profile.threads();
        assertEquals(0, threads.size());
        Profile.init();
        threads = Profile.threads();
        assertTrue(threads.isEmpty());
        Profile.start();
        threads = Profile.threads();
        assertEquals(1, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
    }

    @Test
    public void testInteractions() {
        Profile.start("ClassName1", "MethodName1");
        List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
        assertEquals(1, interactions.size());
        assertEquals(_frame1, interactions.get(0));
        Profile.end("ClassName1", "MethodName1");
    }

    @Test
    public void testFrameList() {
        Profile.start("ClassName1", "MethodName1");
        Profile.start("ClassName1", "MethodName1");
        Iterable<Frame> frameList = Profile.frameList();
        assertEquals(2, frameList.spliterator().getExactSizeIfKnown());
        Profile.end("ClassName1", "MethodName1");
        Profile.end("ClassName1", "MethodName1");
    }

    @Test
    public void testAllocations() {
        Profile.alloc("ClassName");
        ClassAllocation allocation = Profile.allocations().iterator().next();
        assertSame(_classAllocation, allocation);
        Profile.alloc("ClassName");
        assertSame(_classAllocation, Profile.allocations().iterator().next());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.start("ClassName1", "MethodName1");
        long time1 = System.nanoTime();
        Profile.end("ClassName1", "MethodName1");
        long time2 = System.nanoTime();
        long threadId = Thread.currentThread().getId();
        assertEquals(time2 - time1, Profile.getThreadTotalTime(threadId));
    }

    @Test
    public void testSortFrameList() {
        Profile.start("ClassName1", "MethodName1");
        Profile.start("ClassName1", "MethodName1");
        Profile.start("ClassName2", "MethodName2");
        Profile.start("ClassName1", "MethodName1");
        Profile.sortFrameList((frame1, frame2) -> Long.compare(frame1.getBeginTime(), frame2.getBeginTime()));
        Frame firstFrame = Profile.frameList().iterator().next();
        Frame secondFrame = Profile.frameList().iterator().next();
        assertEquals(_frame1, firstFrame);
        assertEquals(_frame2, secondFrame);
    }

    @Test
    public void testStartEnd() {
        Profile.start("ClassName1", "MethodName1");
        assertEquals(_frame1, Profile._threadActiveFrame.get(Thread.currentThread().getId()));
        Profile.end("ClassName1", "MethodName1");
        assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.start("ClassName1", "MethodName1");
        Profile.beginWait("ClassName1", "MethodName1");
        assertEquals(_frame1, Profile._threadActiveFrame.get(Thread.currentThread().getId()));
        Profile.endWait("ClassName1", "MethodName1");
        assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }

    @Test
    public void testUnwind() {
        Profile.start("ClassName1", "MethodName1");
        Profile.unwind("ClassName1", "MethodName1", "ExceptionClass");
        assertSame(_frame1, Profile._threadActiveFrame.get(Thread.currentThread().getId()));
    }

    @Test(expected = AssertionError.class)
    public void testUnwindException() {
        Profile.start("ClassName1", "MethodName1");
        Profile.unwind("ClassName2", "MethodName2", "ExceptionClass");
    }

    @Test
    public void testFindFrame() {
        Profile.start("ClassName1", "MethodName1");
        Profile.start("ClassName1", "MethodName1");
        Profile.start("ClassName2", "MethodName2");
        Profile.start("ClassName1", "MethodName1");
        Frame target = Profile.findFrame(Thread.currentThread().getId(), "ClassName1", "MethodName1");
        assertSame(_frame1, target);
        Frame target2 = Profile.findFrame(Thread.currentThread().getId(), "ClassName2", "MethodName2");
        assertNull(target2);
    }

    @Test
    public void testFindFrameNotFound() {
        Profile.start("ClassName1", "MethodName1");
        Frame target = Profile.findFrame(Thread.currentThread().getId(), "ClassName2", "MethodName2");
        assertNull(target);
    }
}
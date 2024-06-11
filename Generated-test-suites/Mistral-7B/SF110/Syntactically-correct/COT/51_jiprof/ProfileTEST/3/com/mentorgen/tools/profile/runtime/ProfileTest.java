package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.output.ProfileFormatter;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

    private Profile _profile;
    private ThreadDictionary _threadDictionary;
    private Frame _frame;

    @Before
    public void setUp() {
        Profile.init();
        _profile = new Profile();
        _threadDictionary = new ThreadDictionary();
    }

    // Test for initProfiler method
    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
        if (Profile._controllerThread != null) {
            assertTrue(Profile._controllerThread.isAlive());
        }
    }

    // Test for init method
    @Test
    public void testInit() {
        Profile.init();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertNull(Profile._controller);
        assertNull(Profile._controllerThread);
    }

    // Test for clear method
    @Test
    public void testClear() {
        Profile.init();
        Profile.start();
        _frame = new Frame(null, new Method("TestClass", "<init>"), Thread.currentThread().getId());
        Profile._threadActiveFrame.put(Thread.currentThread().getId(), _frame);
        Profile._frameList.add(_frame);
        Profile._threadDictionary.add(Thread.currentThread().getId(), _frame);
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertNull(Profile._controller);
        assertNull(Profile._controllerThread);
    }

    // Test for start method
    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        assertTrue(Profile._controller.isRunning());
    }

    // Test for stop method
    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        assertFalse(Profile._controller.isRunning());
    }

    // Test for setFileName method
    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName("profile.txt");
        assertEquals("profile.txt", Profile._controller.getFileName());
    }

    // Test for shutdown method
    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        assertFalse(Profile._controller.isRunning());
        assertFalse(Profile._controllerThread.isAlive());
    }

    // Test for threads method
    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        _threadDictionary.add(Thread.currentThread().getId(), new Frame(null, new Method("TestClass", "<init>"), Thread.currentThread().getId()));
        List<Long> threads = Profile.threads();
        assertEquals(1, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
    }

    // Test for interactions method
    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        _threadDictionary.add(Thread.currentThread().getId(), new Frame(null, new Method("TestClass", "<init>"), Thread.currentThread().getId()));
        List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
        assertEquals(1, interactions.size());
        assertEquals(_frame, interactions.get(0));
    }

    // Test for frameList method
    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass1", "<init>"), Thread.currentThread().getId());
        Frame frame2 = new Frame(null, new Method("TestClass2", "<init>"), Thread.currentThread().getId());
        _threadDictionary.add(Thread.currentThread().getId(), frame1);
        _threadDictionary.add(Thread.currentThread().getId(), frame2);
        List<Frame> frameList = Profile.frameList();
        assertEquals(2, frameList.size());
        assertTrue(frameList.contains(frame1));
        assertTrue(frameList.contains(frame2));
    }

    // Test for allocations method
    @Test
    public void testAllocations() {
        Profile.init();
        Profile.alloc("TestClass1");
        Map<String, ClassAllocation> allocList = Profile._allocList;
        assertEquals(1, allocList.size());
        ClassAllocation ca = allocList.get("TestClass1");
        assertEquals(1, ca.getAllocCount());
    }

    // Test for getThreadTotalTime method
    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass1", "<init>"), Thread.currentThread().getId());
        Frame frame2 = new Frame(null, new Method("TestClass2", "<init>"), Thread.currentThread().getId());
        long threadId = Thread.currentThread().getId();
        _threadDictionary.add(threadId, frame1);
        _threadDictionary.add(threadId, frame2);
        long time1 = System.nanoTime();
        Thread.sleep(10);
        long time2 = System.nanoTime();
        _frame = frame1;
        _frame.setEndTime(time2);
        _frame.setBeginTime(time1);
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(time2 - time1, threadTotalTime);
    }

    // Test for sortFrameList method
    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass1", "<init>"), Thread.currentThread().getId());
        Frame frame2 = new Frame(null, new Method("TestClass2", "<init>"), Thread.currentThread().getId());
        Frame frame3 = new Frame(null, new Method("TestClass3", "<init>"), Thread.currentThread().getId());
        _threadDictionary.add(Thread.currentThread().getId(), frame1);
        _threadDictionary.add(Thread.currentThread().getId(), frame2);
        _threadDictionary.add(Thread.currentThread().getId(), frame3);
        Collections.sort(Profile._frameList, new Comparator<Frame>() {
            @Override
            public int compare(Frame o1, Frame o2) {
                long start1 = o1.getBeginTime();
                long start2 = o2.getBeginTime();
                return Long.compare(start1, start2);
            }
        });
        Frame firstFrame = Profile._frameList.get(0);
        assertEquals(frame1, firstFrame);
    }

    // Test for start method with IllegalArgumentException when className or methodName is null
    @Test(expected = IllegalArgumentException.class)
    public void testStartNullArgument() {
        Profile.init();
        Profile.start(null, null);
    }

    // Test for start method with IllegalArgumentException when className is empty or methodName is empty
    @Test(expected = IllegalArgumentException.class)
    public void testStartEmptyArgument() {
        Profile.init();
        Profile.start("", "");
    }

    // Test for start method with IllegalStateException when profiling is not enabled
    @Test(expected = IllegalStateException.class)
    public void testStartProfilingDisabled() {
        Profile.init();
        Profile._controller.setProfile(false);
        Profile.start("TestClass", "");
    }

    // Test for end method with IllegalArgumentException when className or methodName is null
    @Test(expected = IllegalArgumentException.class)
    public void testEndNullArgument() {
        Profile.init();
        Profile.start("TestClass", "<init>");
        Profile.end(null, null);
    }

    // Test for end method with IllegalArgumentException when className is empty or methodName is empty
    @Test(expected = IllegalArgumentException.class)
    public void testEndEmptyArgument() {
        Profile.init();
        Profile.start("TestClass", "<init>");
        Profile.end("", "");
    }

    // Test for end method with IllegalStateException when profiling is not enabled
    @Test(expected = IllegalStateException.class)
    public void testEndProfilingDisabled() {
        Profile.init();
        Profile._controller.setProfile(false);
        Profile.start("TestClass", "<init>");
        Profile.end("TestClass", "");
    }

    // Test for end method with IllegalArgumentException when no matching frame is found
    @Test(expected = IllegalArgumentException.class)
    public void testEndNoMatchingFrame() {
        Profile.init();
        Profile.start("TestClass1", "<init>");
        Profile.start("TestClass2", "<init>");
        Profile.end("TestClass3", "");
    }

    // Test for beginWait method with IllegalArgumentException when className or methodName is null
    @Test(expected = IllegalArgumentException.class)
    public void testBeginWaitNullArgument() {
        Profile.init();
        Profile.start("TestClass", "<init>");
        Profile.beginWait(null, null);
    }

    // Test for beginWait method with IllegalArgumentException when className is empty or methodName is empty
    @Test(expected = IllegalArgumentException.class)
    public void testBeginWaitEmptyArgument() {
        Profile.init();
        Profile.start("TestClass", "<init>");
        Profile.beginWait("", "");
    }

    // Test for beginWait method with IllegalStateException when profiling is not enabled
    @Test(expected = IllegalStateException.class)
    public void testBeginWaitProfilingDisabled() {
        Profile.init();
        Profile._controller.setProfile(false);
        Profile.start("TestClass", "<init>");
        Profile.beginWait("TestClass", "");
    }

    // Test for endWait method with IllegalArgumentException when className or methodName is null
    @Test(expected = IllegalArgumentException.class)
    public void testEndWaitNullArgument() {
        Profile.init();
        Profile.start("TestClass", "<init>");
        Profile.beginWait("TestClass", "");
        Profile.endWait(null, null);
    }

    // Test for endWait method with IllegalArgumentException when className is empty or methodName is empty
    @Test(expected = IllegalArgumentException.class)
    public void testEndWaitEmptyArgument() {
        Profile.init();
        Profile.start("TestClass", "<init>");
        Profile.beginWait("TestClass", "");
        Profile.endWait("", "");
    }

    // Test for endWait method with IllegalStateException when profiling is not enabled
    @Test(expected = IllegalStateException.class)
    public void testEndWaitProfilingDisabled() {
        Profile.init();
        Profile._controller.setProfile(false);
        Profile.start("TestClass", "<init>");
        Profile.beginWait("TestClass", "");
        Profile.endWait("TestClass", "");
    }

    // Test for endWait method with IllegalStateException when no matching frame is found
    @Test(expected = IllegalStateException.class)
    public void testEndWaitNoMatchingFrame() {
        Profile.init();
        Profile.start("TestClass1", "<init>");
        Profile.start("TestClass2", "<init>");
        Profile.beginWait("TestClass3", "");
        Profile.endWait("TestClass4", "");
    }

    // Test for unwind method with IllegalArgumentException when className or methodName is null
    @Test(expected = IllegalArgumentException.class)
    public void testUnwindNullArgument() {
        Profile.init();
        Profile.start("TestClass", "<init>");
        Profile.unwind(null, null, "");
    }

    // Test for unwind method with IllegalArgumentException when className is empty or methodName is empty
    @Test(expected = IllegalArgumentException.class)
    public void testUnwindEmptyArgument() {
        Profile.init();
        Profile.start("TestClass", "<init>");
        Profile.unwind("", "", "");
    }

    // Test for unwind method with IllegalStateException when profiling is not enabled
    @Test(expected = IllegalStateException.class)
    public void testUnwindProfilingDisabled() {
        Profile.init();
        Profile._controller.setProfile(false);
        Profile.start("TestClass", "<init>");
        Profile.unwind("TestClass", "", "");
    }

    // Test for unwind method with IllegalArgumentException when no matching frame is found
    @Test(expected = IllegalStateException.class)
    public void testUnwindNoMatchingFrame() {
        Profile.init();
        Profile.start("TestClass1", "<init>");
        Profile.start("TestClass2", "<init>");
        Profile.unwind("TestClass3", "", "");
    }

    // Test for alloc method with IllegalArgumentException when className is null
    @Test(expected = IllegalArgumentException.class)
    public void testAllocNullClassName() {
        Profile.init();
        Profile.alloc(null);
    }

    // Test for alloc method with IllegalArgumentException when className is empty
    @Test(expected = IllegalArgumentException.class)
    public void testAllocEmptyClassName() {
        Profile.init();
        Profile.alloc("");
    }

    // Test for run method with IOException
    @Test(expected = IOException.class)
    public void testRunIOException() {
        Profile.init();
        _profile.run();
    }

}
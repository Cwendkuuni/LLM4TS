package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterable;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.output.ProfileFileName;
import com.mentorgen.tools.profile.runtime.Controller;
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
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.clear();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertTrue(Profile._frameList.isEmpty());
        assertTrue(Profile._threadDictionary.isEmpty());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        assertNotNull(Profile._controller);
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
        ProfileFileName fileName = new ProfileFileName();
        Profile.setFileName(fileName.getFileName());
        assertTrue(Profile._controller.getFileName().equals(fileName.getFileName()));
    }

    @Test
    public void testShutdown() {
        Profile.init();
        _frame = new Frame(null, new Method("className", "<init>"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        _profile.run();
        Profile.shutdown();
        assertFalse(Profile._threadDictionary.size() > 0);
        assertTrue(Profile._controllerThread == null || !Profile._controllerThread.isAlive());
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testThreads() {
        Profile.init();
        _frame = new Frame(null, new Method("className", "<init>"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        _frame = new Frame(null, new Method("className2", "<init>"), 2L);
        Profile._threadActiveFrame.put(2L, _frame);
        List<Long> threads = new ArrayList<>(Profile.threads());
        Collections.sort(threads);
        List<Long> expectedThreads = new ArrayList<>();
        expectedThreads.add(1L);
        expectedThreads.add(2L);
        assertTrue(threads.equals(expectedThreads));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        _frame = new Frame(null, new Method("className", "<init>"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        _frame = new Frame(_frame, new Method("className", "method"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        _frame = new Frame(_frame, new Method("className2", "method2"), 2L);
        Profile._threadActiveFrame.put(2L, _frame);
        Iterable<Frame> interactions = Profile.interactions(1L);
        List<Frame> interactionsList = new ArrayList<>();
        for (Frame frame : interactions) {
            interactionsList.add(frame);
        }
        List<Frame> expectedInteractions = new ArrayList<>();
        expectedInteractions.add(_frame);
        expectedInteractions.add(_frame);
        assertTrue(interactionsList.equals(expectedInteractions));
    }

    @Test
    public void testFrameList() {
        Profile.init();
        _frame = new Frame(null, new Method("className", "<init>"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        _frame = new Frame(_frame, new Method("className", "method"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        _frame = new Frame(_frame, new Method("className2", "method2"), 2L);
        Profile._threadActiveFrame.put(2L, _frame);
        Iterable<Frame> frameList = Profile.frameList();
        List<Frame> frameListList = new ArrayList<>();
        for (Frame frame : frameList) {
            frameListList.add(frame);
        }
        List<Frame> expectedFrameList = new ArrayList<>();
        expectedFrameList.add(_frame);
        expectedFrameList.add(_frame);
        expectedFrameList.add(_frame);
        assertTrue(frameListList.equals(expectedFrameList));
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.alloc("className");
        Profile.alloc("className");
        Iterable<ClassAllocation> allocations = Profile.allocations();
        ClassAllocation classNameAllocation = null;
        for (ClassAllocation ca : allocations) {
            if (ca.getClassname().equals("className")) {
                classNameAllocation = ca;
                break;
            }
        }
        assertTrue(classNameAllocation != null);
        assertTrue(classNameAllocation.getAllocCount() == 2);
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        _frame = new Frame(null, new Method("className", "<init>"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        Profile.end("<className>", "<init>");
        _frame = new Frame(_frame, new Method("className", "method"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        Profile.end("<className>", "method");
        long threadTotalTime = Profile.getThreadTotalTime(1L);
        assertTrue(threadTotalTime > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindFrameNullThreadId() {
        Profile.init();
        Profile.findFrame(0L, "className", "method");
    }

    @Test(expected = IllegalStateException.class)
    public void testFindFrameNullClassName() {
        Profile.init();
        _frame = new Frame(null, new Method("className", "<init>"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        Profile.findFrame(1L, null, "method");
    }

    @Test(expected = IllegalStateException.class)
    public void testFindFrameNullMethodName() {
        Profile.init();
        _frame = new Frame(null, new Method("className", "<init>"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        Profile.findFrame(1L, "className", null);
    }

    @Test
    public void testFindFrame() {
        Profile.init();
        _frame = new Frame(null, new Method("className", "<init>"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        _frame = new Frame(_frame, new Method("className", "method"), 1L);
        Profile._threadActiveFrame.put(1L, _frame);
        Frame target = Profile.findFrame(1L, "className", "method");
        assertTrue(target != null && target.equals(_frame));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartInvalidMethodName() {
        Profile.init();
        Profile.start("invalidClassName", "invalidMethodName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartInvalidClassName() {
        Profile.init();
        Profile.start("className", null);
    }

}
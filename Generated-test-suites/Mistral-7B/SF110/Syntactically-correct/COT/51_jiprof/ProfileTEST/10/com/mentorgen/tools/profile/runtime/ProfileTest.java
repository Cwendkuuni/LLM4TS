package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

    private static final String CLASS_NAME = "com.example.MyClass";
    private static final String METHOD_NAME1 = "method1";
    private static final String METHOD_NAME2 = "method2";
    private static final String METHOD_NAME3 = "method3";
    private static final int FRAME_COUNT = 10;
    private static final long THREAD_ID = 1L;
    private static final long START_TIME = 1L;
    private static final long END_TIME = 2L;
    private static final String FILENAME = "output.txt";
    private Profile _profile;
    private Frame[] _frameList;
    private Map<String, Method> _methodDictionary;
    private Map<Long, Frame> _threadActiveFrame;
    private Map<String, Frame> _threadDictionary;
    private Map<String, ClassAllocation> _allocList;
    private Controller _controller;

    @Before
    public void setUp() {
        Profile.init();
        _profile = new Profile();
        _frameList = new Frame[FRAME_COUNT];
        _methodDictionary = new HashMap<>();
        _threadActiveFrame = new HashMap<>();
        _threadDictionary = new ThreadDictionary();
        _allocList = new HashMap<>();
        _controller = new Controller();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
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
        Profile.start(CLASS_NAME, METHOD_NAME1);
        Profile.end(CLASS_NAME, METHOD_NAME1);
        Profile.clear();
        assertTrue(Profile._threadDictionary.isEmpty());
        assertTrue(Profile._frameList.isEmpty());
    }

    @Test
    public void testStart() {
        Profile.start();
        assertTrue(_controller.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAlreadyRunning() {
        Profile.init();
        Profile.start();
        Profile.start();
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        assertFalse(_controller.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        Profile.init();
        Profile.stop();
    }

    @Test
    public void testSetFileName() {
        Profile.setFileName(FILENAME);
        assertEquals(FILENAME, Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        _frameList[0] = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        _threadActiveFrame.put(THREAD_ID, _frameList[0]);
        _frameList[0].setEndTime(END_TIME);
        _frameList[0].computeNetTime();
        Profile.shutdown();
        assertTrue(Profile._controller.isClosed());
        assertTrue(ProfileDump.isFileWritable(FILENAME));
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        _frameList[0] = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        _threadActiveFrame.put(THREAD_ID, _frameList[0]);
        _threadDictionary.add(THREAD_ID, _frameList[0]);
        List<Long> threads = Profile.threads();
        assertEquals(1, threads.size());
        assertEquals(THREAD_ID, threads.get(0).longValue());
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        _frameList[0] = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        _frameList[1] = new Frame(null, new Method(CLASS_NAME, METHOD_NAME2), THREAD_ID);
        _threadActiveFrame.put(THREAD_ID, _frameList[0]);
        _threadDictionary.add(THREAD_ID, _frameList[0]);
        List<Frame> interactions = Profile.interactions(THREAD_ID);
        assertEquals(2, interactions.size());
        assertEquals(_frameList[0], interactions.get(0));
        assertEquals(_frameList[1], interactions.get(1));
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        _frameList[0] = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        _threadActiveFrame.put(THREAD_ID, _frameList[0]);
        _threadDictionary.add(THREAD_ID, _frameList[0]);
        _frameList[1] = new Frame(null, new Method(CLASS_NAME, METHOD_NAME2), THREAD_ID);
        _threadActiveFrame.put(THREAD_ID, _frameList[1]);
        _threadDictionary.add(THREAD_ID, _frameList[1]);
        List<Frame> frameList = Profile.frameList();
        assertEquals(2, frameList.size());
        assertEquals(_frameList[0], frameList.get(0));
        assertEquals(_frameList[1], frameList.get(1));
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        Profile.alloc(CLASS_NAME);
        Profile.alloc(CLASS_NAME);
        List<ClassAllocation> allocations = new ArrayList<>(_allocList.values());
        assertEquals(2, allocations.size());
        assertEquals(CLASS_NAME, allocations.get(0).getName());
        assertEquals(2, allocations.get(0).getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        _frameList[0] = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        _frameList[0].setBeginTime(START_TIME);
        _frameList[0].setEndTime(END_TIME);
        _threadActiveFrame.put(THREAD_ID, _frameList[0]);
        _threadDictionary.add(THREAD_ID, _frameList[0]);
        long threadTotalTime = Profile.getThreadTotalTime(THREAD_ID);
        assertEquals(END_TIME - START_TIME, threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        _frameList[0] = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        _frameList[1] = new Frame(null, new Method(CLASS_NAME, METHOD_NAME2), THREAD_ID);
        _threadActiveFrame.put(THREAD_ID, _frameList[0]);
        _threadDictionary.add(THREAD_ID, _frameList[0]);
        _threadActiveFrame.put(THREAD_ID, _frameList[1]);
        _threadDictionary.add(THREAD_ID, _frameList[1]);
        Comparator<Frame> comp = (frame1, frame2) -> Long.compare(frame1.getBeginTime(), frame2.getBeginTime());
        Profile.sortFrameList(comp);
        Frame firstFrame = _frameList[0];
        Frame secondFrame = _frameList[1];
        assertTrue(firstFrame.getBeginTime() < secondFrame.getBeginTime());
    }

    @Test
    public void testStartWithException() {
        Profile.init();
        Profile._debugException = true;
        try {
            Profile.start(CLASS_NAME, METHOD_NAME1);
            fail("Expected an exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testEndWithException() {
        Profile.init();
        Profile._debugException = true;
        Profile.start(CLASS_NAME, METHOD_NAME1);
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        _threadActiveFrame.put(THREAD_ID, frame);
        _threadDictionary.add(THREAD_ID, frame);
        try {
            Profile.end(CLASS_NAME, METHOD_NAME1);
            fail("Expected an exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testBeginWaitWithException() {
        Profile.init();
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        _threadActiveFrame.put(THREAD_ID, frame);
        try {
            Profile.beginWait(CLASS_NAME, METHOD_NAME1);
            fail("Expected an exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testEndWaitWithException() {
        Profile.init();
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        frame.beginWait(START_TIME);
        _threadActiveFrame.put(THREAD_ID, frame);
        try {
            Profile.endWait(CLASS_NAME, METHOD_NAME1);
            fail("Expected an exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testUnwindWithException() {
        Profile.init();
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        frame.beginWait(START_TIME);
        try {
            Profile.unwind(CLASS_NAME, METHOD_NAME1, "ExceptionClass");
            fail("Expected an exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testFindFrame() {
        Profile.init();
        Frame parentFrame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME1), THREAD_ID);
        Frame childFrame1 = new Frame(parentFrame, new Method(CLASS_NAME, METHOD_NAME2), THREAD_ID);
        Frame childFrame2 = new Frame(parentFrame, new Method(CLASS_NAME, METHOD_NAME3), THREAD_ID);
        _threadActiveFrame.put(THREAD_ID, parentFrame);
        _threadDictionary.add(THREAD_ID, parentFrame);
        List<Frame> frameList = new ArrayList<>();
        frameList.add(childFrame1);
        frameList.add(childFrame2);
        _threadDictionary.setFrames(THREAD_ID, frameList);
        Frame target = Profile.findFrame(THREAD_ID, CLASS_NAME, METHOD_NAME3);
        assertEquals(childFrame2, target);
    }
}
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.runtime.Controller;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

    private static final String FILENAME = "test.profile";
    private static final String CLASS_NAME = "TestClass";
    private static final String METHOD_NAME = "testMethod";
    private static final String THREAD_NAME = "TestThread";
    private static final long THREAD_ID = 1L;

    private Controller mockController;
    private Profile profile;
    private Frame testFrame;

    @Before
    public void setUp() {
        mockController = mock(Controller.class);
        when(mockController.isProfile()).thenReturn(true);
        when(mockController.setFileName(anyString())).thenReturn(mockController);
        when(mockController.close()).thenReturn(mockController);
        profile = new Profile();
        profile.init();
        testFrame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
    }

    @After
    public void tearDown() {
        Profile.clear();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        verify(mockController, times(1)).start();
        assertTrue(Profile._controller != null);
    }

    @Test
    public void testInit() {
        Profile.init();
        assertEquals(1001, Profile._threadDictionary.size());
        assertEquals(2003, Profile._methodDictionary.size());
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Profile.end(CLASS_NAME, METHOD_NAME);
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertTrue(Profile._frameList.isEmpty());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        verify(mockController, times(1)).start();
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        verify(mockController, times(1)).stop();
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
        when(mockController.getThreadTotalTime(THREAD_ID)).thenReturn(1L);
        Profile.shutdown();
        verify(mockController, times(1)).shutdown();
        assertEquals(1, Profile._threadDictionary.size());
        assertFalse(Profile._controllerThread.isAlive());
        assertTrue(ProfileDump.dumpFileExists());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        Profile.stop();
        List<Long> threads = Profile.threads();
        assertEquals(1, threads.size());
        assertEquals(THREAD_ID, threads.get(0).longValue());
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        Profile.end(CLASS_NAME, METHOD_NAME);
        List<Frame> interactions = Profile.interactions(THREAD_ID);
        assertEquals(1, interactions.size());
        assertEquals(testFrame, interactions.get(0));
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Frame parentFrame = new Frame(null, new Method(CLASS_NAME, "<init>"), THREAD_ID);
        Frame childFrame = new Frame(parentFrame, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Profile._threadActiveFrame.put(THREAD_ID, childFrame);
        Profile._frameList.add(parentFrame);
        Profile._frameList.add(childFrame);
        List<Frame> frameList = Profile.frameList();
        assertEquals(2, frameList.size());
        assertEquals(parentFrame, frameList.get(0));
        assertEquals(childFrame, frameList.get(1));
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        Profile.alloc(CLASS_NAME);
        Profile.alloc(CLASS_NAME);
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertEquals(1, allocations.size());
        ClassAllocation classAllocation = allocations.get(0);
        assertEquals(CLASS_NAME, classAllocation.getName());
        assertEquals(2, classAllocation.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        when(mockController.getThreadTotalTime(THREAD_ID)).thenReturn(1L);
        long threadTotalTime = Profile.getThreadTotalTime(THREAD_ID);
        assertEquals(1, threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Frame frame2 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Frame frame3 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Profile._frameList.add(frame1);
        Profile._frameList.add(frame3);
        Profile._frameList.add(frame2);
        Collections.sort(Profile._frameList, new Comparator<Frame>() {
            @Override
            public int compare(Frame o1, Frame o2) {
                return (int) (o1.getBeginTime() - o2.getBeginTime());
            }
        });
        Frame sortedFrame1 = Profile._frameList.get(0);
        Frame sortedFrame2 = Profile._frameList.get(1);
        Frame sortedFrame3 = Profile._frameList.get(2);
        assertEquals(frame1, sortedFrame1);
        assertEquals(frame2, sortedFrame2);
        assertEquals(frame3, sortedFrame3);
    }

    @Test
    public void testStartWithException() {
        Profile.init();
        Profile._debugException = true;
        try {
            Profile.start(CLASS_NAME, METHOD_NAME);
            fail("Expected exception");
        } catch (Exception e) {
            // Expected
        }
        verify(mockController, times(0)).start();
        assertNull(Profile._controllerThread);
    }

    @Test
    public void testStartWithMultipleThreads() {
        Profile.init();
        Profile._controller = mockController;
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start(CLASS_NAME, METHOD_NAME);
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start(CLASS_NAME, METHOD_NAME);
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            // Ignore
        }
        assertEquals(2, Profile._threadDictionary.size());
    }

    @Test
    public void testEndWithException() {
        Profile.init();
        Profile._debugException = true;
        try {
            Profile.end(CLASS_NAME, METHOD_NAME);
            fail("Expected exception");
        } catch (Exception e) {
            // Expected
        }
        verify(mockController, times(0)).end(anyString(), anyString());
    }

    @Test
    public void testEndWithNullClassName() {
        Profile.init();
        try {
            Profile.end(null, METHOD_NAME);
            fail("Expected exception");
        } catch (AssertionError e) {
            // Expected
        }
        verify(mockController, times(0)).end(anyString(), anyString());
    }

    @Test
    public void testEndWithNullMethodName() {
        Profile.init();
        try {
            Profile.end(CLASS_NAME, null);
            fail("Expected exception");
        } catch (AssertionError e) {
            // Expected
        }
        verify(mockController, times(0)).end(anyString(), anyString());
    }

    @Test
    public void testBeginWaitAndEndWait() {
        Profile.init();
        Profile.start();
        Frame parentFrame = new Frame(null, new Method(CLASS_NAME, "<init>"), THREAD_ID);
        Frame childFrame = new Frame(parentFrame, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Frame waitFrame = new Frame(childFrame, new Method(CLASS_NAME, "waitMethod"), THREAD_ID);
        Profile._threadActiveFrame.put(THREAD_ID, waitFrame);
        Profile._frameList.add(parentFrame);
        Profile._frameList.add(childFrame);
        Profile._frameList.add(waitFrame);
        Profile.beginWait(CLASS_NAME, METHOD_NAME);
        Profile.endWait(CLASS_NAME, METHOD_NAME);
        Frame waitFrameAfterEnd = Profile._threadActiveFrame.get(THREAD_ID);
        assertEquals(waitFrameAfterEnd, Profile._threadActiveFrame.get(THREAD_ID));
        assertEquals(waitFrame, waitFrameAfterEnd);
    }

    @Test
    public void testUnwind() {
        Profile.init();
        Profile._debugException = true;
        when(mockController.isProfile()).thenReturn(true);
        when(mockController.getThreadTotalTime(THREAD_ID)).thenReturn(1L);
        try {
            Profile.start(CLASS_NAME, METHOD_NAME);
            Profile.end(CLASS_NAME, METHOD_NAME);
            Profile.unwind(CLASS_NAME, "TestException.class.getName()", "TestException.class.getName()");
        } catch (Exception e) {
            // Ignore
        }
        verify(mockController, times(1)).shutdown();
        assertEquals(1, Profile._threadDictionary.size());
        assertFalse(Profile._controllerThread.isAlive());
        assertTrue(ProfileDump.dumpFileExists());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.alloc(CLASS_NAME);
        ClassAllocation ca = Profile._allocList.get(CLASS_NAME);
        assertEquals(1, ca.getAllocCount());
    }

    @Test
    public void testAllocMultipleTimes() {
        Profile.init();
        Profile.alloc(CLASS_NAME);
        Profile.alloc(CLASS_NAME);
        ClassAllocation ca = Profile._allocList.get(CLASS_NAME);
        assertEquals(2, ca.getAllocCount());
    }

    @Test
    public void testAllocWithDifferentClassName() {
        Profile.init();
        Profile.alloc(CLASS_NAME);
        Profile.alloc("OtherClass.class.getName()");
        Map<String, ClassAllocation> allocList = Profile._allocList;
        assertEquals(2, allocList.size());
        ClassAllocation classAllocation1 = allocList.get(CLASS_NAME);
        ClassAllocation classAllocation2 = allocList.get("OtherClass.class.getName()");
        assertNotSame(classAllocation1, classAllocation2);
    }
}
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.io.*;
import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

    private static final String CLASS_NAME = "com.example.TestClass";
    private static final String METHOD_NAME = "testMethod";
    private static final long THREAD_ID = 1L;
    private static final String FILENAME = "test.prof";
    private static final Controller MOCK_CONTROLLER = mock(Controller.class);

    @Before
    public void setUp() {
        Profile.init();
        Profile.initProfiler();
        Profile._controller = MOCK_CONTROLLER;
    }

    @Test
    public void testInit() {
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertFalse(Profile._controllerThread.isAlive());
        assertFalse(Controller._profile);
    }

    @Test
    public void testClear() {
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._methodDictionary.size());
    }

    @Test
    public void testInitProfiler() {
        assertTrue(Profile._controllerThread.isAlive());
        assertTrue(Controller._profile);
    }

    @Test
    public void testShutdown() throws IOException {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Profile.shutdown();
        ProfileDump.dump();
        verify(MOCK_CONTROLLER, times(1)).close();
        assertTrue(Controller._profile == false);
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            start(CLASS_NAME, METHOD_NAME);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end(CLASS_NAME, METHOD_NAME);
        });
        Thread thread2 = new Thread(() -> {
            start(CLASS_NAME, METHOD_NAME);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end(CLASS_NAME, METHOD_NAME);
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        List<Long> threads = new ArrayList<>(Profile.threads());
        assertEquals(2, threads.size());
        assertEquals(THREAD_ID, threads.get(0));
        assertEquals(Thread.currentThread().getId(), threads.get(1));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(() -> {
            start(CLASS_NAME, METHOD_NAME);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end(CLASS_NAME, METHOD_NAME);
            start(CLASS_NAME, METHOD_NAME);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end(CLASS_NAME, METHOD_NAME);
        });
        thread.start();
        thread.join();
        List<Frame> interactions = new ArrayList<>(Profile.interactions(THREAD_ID));
        assertEquals(2, interactions.size());
        Frame firstFrame = interactions.get(0);
        Frame secondFrame = interactions.get(1);
        assertEquals(CLASS_NAME, firstFrame.getClassName());
        assertEquals(METHOD_NAME, firstFrame.getMethodName());
        assertEquals(CLASS_NAME, secondFrame.getClassName());
        assertEquals(METHOD_NAME, secondFrame.getMethodName());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(() -> {
            start(CLASS_NAME, METHOD_NAME);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end(CLASS_NAME, METHOD_NAME);
            start(CLASS_NAME, METHOD_NAME);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end(CLASS_NAME, METHOD_NAME);
        });
        thread.start();
        thread.join();
        Collection<Frame> frameList = Profile.frameList();
        List<Frame> sortedFrameList = new ArrayList<>(frameList);
        Collections.sort(sortedFrameList, FrameComparator.INSTANCE);
        assertEquals(2, frameList.size());
        Frame firstFrame = sortedFrameList.get(0);
        Frame secondFrame = sortedFrameList.get(1);
        assertEquals(CLASS_NAME, firstFrame.getClassName());
        assertEquals(METHOD_NAME, firstFrame.getMethodName());
        assertEquals(CLASS_NAME, secondFrame.getClassName());
        assertEquals(METHOD_NAME, secondFrame.getMethodName());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(() -> {
            alloc(CLASS_NAME);
            alloc(CLASS_NAME);
            alloc(CLASS_NAME);
        });
        thread.start();
        thread.join();
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertEquals(1, allocations.size());
        ClassAllocation classAllocation = allocations.get(0);
        assertEquals(CLASS_NAME, classAllocation.getClassname());
        assertEquals(3, classAllocation.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        long thread1TotalTime = Profile.getThreadTotalTime(THREAD_ID);
        long thread2TotalTime = Profile.getThreadTotalTime(Thread.currentThread().getId());
        assertTrue(thread1TotalTime > 0);
        assertTrue(thread2TotalTime > 0);
        assertTrue(thread1TotalTime < thread2TotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(() -> {
            start(CLASS_NAME, METHOD_NAME);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end(CLASS_NAME, METHOD_NAME);
            start(CLASS_NAME, METHOD_NAME);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end(CLASS_NAME, METHOD_NAME);
        });
        thread.start();
        thread.join();
        Collections.sort(Profile.frameList(), FrameComparator.INSTANCE);
        Frame firstFrame = Profile.frameList().get(0);
        Frame secondFrame = Profile.frameList().get(1);
        assertEquals(CLASS_NAME, firstFrame.getClassName());
        assertEquals(METHOD_NAME, firstFrame.getMethodName());
        assertEquals(CLASS_NAME, secondFrame.getClassName());
        assertEquals(METHOD_NAME, secondFrame.getMethodName());
    }
}
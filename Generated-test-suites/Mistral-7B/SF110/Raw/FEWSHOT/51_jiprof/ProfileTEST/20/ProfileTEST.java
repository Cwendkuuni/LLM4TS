package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.mentorgen.tools.profile.*;
import java.util.*;
import java.io.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ProfileTest {

    private Controller controller;
    private Profile profile;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = mock(Controller.class);
        profile = new Profile();
        profile.setController(controller);
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        verify(controller).start();
    }

    @Test
    public void testClear() {
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testInit() {
        Profile.init();
        assertEquals(1001, Profile._threadDictionary.capacity());
        assertEquals(2003, Profile._methodDictionary.capacity());
        assertNull(Profile._controller);
        assertNull(Profile._controllerThread);
    }

    @Test
    public void testStart() {
        profile.start();
        verify(controller).start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        profile.start();
        profile.start();
    }

    @Test
    public void testStop() {
        profile.start();
        profile.stop();
        verify(controller).stop();
    }

    @Test
    public void testShutdown() throws IOException {
        profile.start();
        profile.shutdown();
        verify(controller).close();
    }

    @Test
    public void testThreads() {
        profile.start();
        Thread thread1 = new Thread(() -> {});
        Thread thread2 = new Thread(() -> {});
        thread1.start();
        thread2.start();
        List<Long> threads = Profile.threads();
        assertEquals(3, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
        assertEquals(thread1.getId(), threads.get(1));
        assertEquals(thread2.getId(), threads.get(2));
    }

    @Test
    public void testInteractions() {
        profile.start();
        Thread thread1 = new Thread(() -> {
            start("Class1", "method1");
            start("Class1", "method2");
            end("Class1", "method2");
        });
        Thread thread2 = new Thread(() -> {
            start("Class2", "method1");
            start("Class1", "method1");
            end("Class2", "method1");
            end("Class1", "method1");
        });
        thread1.start();
        thread2.start();

        List<Long> threads = Profile.threads();
        Frame thread1Frame1 = findFrame(threads.get(1), "Class1", "method1");
        Frame thread1Frame2 = findFrame(threads.get(1), "Class1", "method2");
        Frame thread2Frame1 = findFrame(threads.get(2), "Class2", "method1");

        assertEquals(2, thread1Frame1.getChildCount());
        assertEquals(thread1Frame2, thread1Frame1.getChild(0));
        assertEquals(thread1Frame1, thread1Frame2.getParent());

        assertEquals(1, thread2Frame1.getChildCount());
        assertEquals(thread2Frame1, thread1Frame1.getParent());
        assertEquals(thread2Frame1, thread2Frame1.getParent());
    }

    @Test
    public void testFrameList() {
        profile.start();
        Thread thread1 = new Thread(() -> {
            start("Class1", "method1");
            start("Class1", "method2");
            end("Class1", "method2");
        });
        Thread thread2 = new Thread(() -> {
            start("Class2", "method1");
            start("Class1", "method1");
            end("Class2", "method1");
            end("Class1", "method1");
        });
        thread1.start();
        thread2.start();

        List<Frame> frameList = Profile.frameList();
        assertEquals(4, frameList.size());
        Frame thread1Frame1 = findFrame(threads.get(1), "Class1", "method1");
        Frame thread1Frame2 = findFrame(threads.get(1), "Class1", "method2");
        Frame thread2Frame1 = findFrame(threads.get(2), "Class2", "method1");

        assertEquals(thread1Frame1, frameList.get(0));
        assertEquals(thread1Frame2, frameList.get(1));
        assertEquals(thread2Frame1, frameList.get(2));
        assertEquals(thread1Frame1, frameList.get(3));
    }

    @Test
    public void testAllocations() {
        profile.start();
        new Object();
        new Object();
        new Object();
        List<ClassAllocation> allocations = Profile.allocations();
        assertEquals(1, allocations.size());
        ClassAllocation allocation = allocations.get(0);
        assertEquals("java.lang.Object", allocation.getClassName());
        assertEquals(3, allocation.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        profile.start();
        Thread thread1 = new Thread(() -> {
            start("Class1", "method1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore
            }
            end("Class1", "method1");
        });
        Thread thread2 = new Thread(() -> {
            start("Class2", "method1");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // ignore
            }
            end("Class2", "method1");
        });
        thread1.start();
        thread2.start();

        long thread1Time = Profile.getThreadTotalTime(threads.get(1));
        long thread2Time = Profile.getThreadTotalTime(threads.get(2));

        assertEquals(100, thread1Time);
        assertEquals(200, thread2Time);
    }

    @Test
    public void testSortFrameList() {
        profile.start();
        Thread thread1 = new Thread(() -> {
            start("Class1", "method1");
            start("Class1", "method2");
            end("Class1", "method2");
            start("Class1", "method3");
            end("Class1", "method3");
        });
        Thread thread2 = new Thread(() -> {
            start("Class2", "method1");
            start("Class2", "method2");
            end("Class2", "method2");
            start("Class2", "method1");
            end("Class2", "method1");
        });
        thread1.start();
        thread2.start();

        Comparator<Frame> comparator = (f1, f2) -> f1.getBeginTime() - f2.getBeginTime();
        Profile.sortFrameList(comparator);

        Frame thread1Frame1 = findFrame(threads.get(1), "Class1", "method1");
        Frame thread1Frame2 = findFrame(threads.get(1), "Class1", "method2");
        Frame thread1Frame3 = findFrame(threads.get(1), "Class1", "method3");
        Frame thread2Frame1 = findFrame(threads.get(2), "Class2", "method1");
        Frame thread2Frame2 = findFrame(threads.get(2), "Class2", "method2");

        assertEquals(thread1Frame1, frameList.get(0));
        assertEquals(thread2Frame1, frameList.get(1));
        assertEquals(thread1Frame2, frameList.get(2));
        assertEquals(thread2Frame2, frameList.get(3));
        assertEquals(thread1Frame3, frameList.get(4));
    }

    private Frame findFrame(long threadId, String className, String methodName) {
        List<Long> threads = Profile.threads();
        for (Long thread : threads) {
            Frame frame = Profile._threadDictionary.getMostRecentFrame(thread);
            if (frame != null && frame.getThreadId() == threadId && frame.getClassName().equals(className) && frame.getMethodName().equals(methodName)) {
                return frame;
            }
        }
        return null;
    }
}
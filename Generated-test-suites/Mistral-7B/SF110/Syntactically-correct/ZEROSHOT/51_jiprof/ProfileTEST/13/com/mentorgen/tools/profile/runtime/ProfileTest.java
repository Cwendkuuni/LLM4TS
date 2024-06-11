package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileTest {

    private static Profile profile;

    @BeforeClass
    public static void setUp() {
        Profile.initProfiler();
    }

    @Test
    public void testInit() {
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertNull(Profile._controller);
        assertNull(Profile._controllerThread);
    }

    @Test
    public void testInitAndShutdown() {
        Profile.initProfiler();
        Profile.init();
        Profile.shutdown();
        Profile.initProfiler();

        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertNotNull(Profile._controller);
        assertNotNull(Profile._controllerThread);
    }

    @Test
    public void testStartAndStop() {
        Profile.init();
        Profile.start();
        Profile.stop();

        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testStartAndStopMultipleTimes() {
        Profile.init();
        Profile.start();
        Profile.stop();
        Profile.start();
        Profile.stop();

        assertTrue(Profile._controller.isRunning());
        assertFalse(Profile._controller.isRunning());
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName("test.txt");
        Profile.shutdown();

        assertEquals("test.txt", Profile._controller.getFileName());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Thread thread1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass1", "testMethod1");
        });
        Thread thread2 = new Thread(() -> {
            Profile.start("TestClass2", "testMethod2");
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass2", "testMethod2");
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        Profile.shutdown();

        List<Long> threads = new ArrayList<>(Profile.threads());

        assertEquals(2, threads.size());
        assertTrue(threads.contains(Thread.currentThread().getId()));
        assertTrue(threads.contains(thread1.getId()));
        assertTrue(threads.contains(thread2.getId()));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Thread thread1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass1", "testMethod1");
        });
        Thread thread2 = new Thread(() -> {
            Profile.start("TestClass2", "testMethod2");
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass2", "testMethod2");
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        Profile.shutdown();

        List<Long> thread1Interactions = new ArrayList<>(Profile.interactions(thread1.getId()));
        List<Long> thread2Interactions = new ArrayList<>(Profile.interactions(thread2.getId()));

        assertEquals(1, thread1Interactions.size());
        assertEquals(1, thread2Interactions.size());
        assertTrue(thread1Interactions.get(0) == thread2Interactions.get(0));
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Thread thread = new Thread(() -> {
            Profile.start("TestClass", "testMethod");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass", "testMethod");
        });
        thread.start();
        thread.join();
        Profile.shutdown();

        Collection<Frame> frameList = Profile.frameList();

        assertEquals(1, frameList.size());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                Profile.alloc("TestClass");
            }
        });
        thread.start();
        thread.join();
        Profile.shutdown();

        Map<String, ClassAllocation> allocations = new HashMap<>(Profile._allocList);

        assertEquals(1, allocations.size());
        assertEquals(1000, allocations.get("TestClass").getAllocationCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        Profile.shutdown();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndTwice() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        Profile.shutdown();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWithoutEnd() {
        Profile.init();
        Profile.start("TestClass", "testMethod");
        Profile.shutdown();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndWithoutStart() {
        Profile.init();
        Profile.end("TestClass", "testMethod");
        Profile.shutdown();
    }

    @Test(expected = NullPointerException.class)
    public void testFindFrameNullClassName() {
        Profile.init();
        synchronized (Profile._lock) {
            Profile._threadActiveFrame.put(Thread.currentThread().getId(), null);
            Profile._threadActiveFrame.put(Thread.currentThread().getId(), new Frame(null, null, Thread.currentThread().getId()));
            Profile.shutdown();
            Frame frame = Profile.findFrame(Thread.currentThread().getId(), null, "testMethod");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFindFrameNullMethodName() {
        Profile.init();
        synchronized (Profile._lock) {
            Profile._threadActiveFrame.put(Thread.currentThread().getId(), new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId()));
            Profile._threadActiveFrame.put(Thread.currentThread().getId(), null);
            Profile.shutdown();
            Frame frame = Profile.findFrame(Thread.currentThread().getId(), "TestClass", null);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFindFrameNullThreadId() {
        Profile.init();
        synchronized (Profile._lock) {
            Profile._threadActiveFrame.put(null, new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId()));
            Profile.shutdown();
            Frame frame = Profile.findFrame(null, "TestClass", "testMethod");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFindFrameNullMethod() {
        Profile.init();
        synchronized (Profile._lock) {
            Profile._threadActiveFrame.put(Thread.currentThread().getId(), new Frame(null, null, Thread.currentThread().getId()));
            Profile._threadActiveFrame.put(Thread.currentThread().getId(), new Frame(new Method("TestClass", "testMethod"), null, Thread.currentThread().getId()));
            Profile.shutdown();
            Frame frame = Profile.findFrame(Thread.currentThread().getId(), "TestClass", null);
        }
    }

    @Test
    public void testFindFrame() {
        Profile.init();
        Thread thread = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass1", "testMethod1");
        });
        thread.start();
        synchronized (Profile._lock) {
            Frame target1 = Profile._threadActiveFrame.get(thread.getId());
            Profile._threadActiveFrame.put(thread.getId(), null);
            Frame target2 = Profile.findFrame(thread.getId(), "TestClass1", "testMethod1");
            Profile._threadActiveFrame.put(thread.getId(), target1);
        }
        thread.join();
        Profile.shutdown();

        assertEquals(target1, target2);
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                Profile.alloc("TestClass");
            }
        });
        thread.start();
        thread.join();
        Profile.shutdown();

        Map<String, ClassAllocation> allocations = new HashMap<>(Profile._allocList);

        assertEquals(1, allocations.size());
        assertEquals(1000, allocations.get("TestClass").getAllocationCount());
    }

    @Test
    public void testRun() throws IOException {
        Profile.init();
        Profile.run();
        Profile.shutdown();
    }
}
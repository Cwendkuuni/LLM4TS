package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ProfileTest {

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
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertNull(Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId()));
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        assertTrue(Controller._profile);
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Thread.yield();
        Profile.stop();
        assertFalse(Controller._profile);
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName("output.txt");
        assertEquals("output.txt", Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.shutdown();
        assertNull(Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId()));
        assertTrue(Profile._controllerThread.isInterrupted());
        assertTrue(Profile._controller.isClosed());
        assertFalse(Controller._profile);
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod");
                Profile.end("TestClass", "testMethod");
            }
        });
        t.start();
        t.join();
        List<Long> threads = new ArrayList<>(Profile.threads());
        assertTrue(threads.size() > 0);
        assertTrue(threads.contains(t.getId()));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end("TestClass", "testMethod");
            }
        });
        t.start();
        t.join();
        Iterable<Frame> interactions = Profile.interactions(t.getId());
        assertTrue(interactions.iterator().hasNext());
        Frame frame = interactions.iterator().next();
        assertEquals("TestClass", frame.getClassName());
        assertEquals("testMethod", frame.getMethodName());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end("TestClass", "testMethod");
            }
        });
        t.start();
        t.join();
        Iterable<Frame> frameList = Profile.frameList();
        assertTrue(frameList.iterator().hasNext());
        Frame frame = frameList.iterator().next();
        assertEquals("TestClass", frame.getClassName());
        assertEquals("testMethod", frame.getMethodName());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        new TestAllocations();
        Iterable<ClassAllocation> allocations = Profile.allocations();
        assertTrue(allocations.iterator().hasNext());
        ClassAllocation ca = allocations.iterator().next();
        assertEquals("TestAllocations", ca.getClassName());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.nanoTime();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        t.join();
        long threadTotalTime = Profile.getThreadTotalTime(t.getId());
        assertTrue(threadTotalTime > 0);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass1", "testMethod1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end("TestClass1", "testMethod1");
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass2", "testMethod2");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end("TestClass2", "testMethod2");
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        Collections.sort(new ArrayList<>(Profile.frameList()), Frame.COMPARATOR);
        Frame firstFrame = Profile.frameList().iterator().next();
        Frame secondFrame = Profile.frameList().iterator().next();
        assertTrue(firstFrame.getBeginTime() < secondFrame.getBeginTime());
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        long startTime = System.nanoTime();
        Profile.start(className, methodName);
        long endTime = System.nanoTime();
        Profile.end(className, methodName);
        Frame frame = findFrame(className, methodName);
        assertEquals(className, frame.getClassName());
        assertEquals(methodName, frame.getMethodName());
        assertTrue(frame.getBeginTime() <= startTime);
        assertTrue(frame.getEndTime() >= endTime);
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        long startTime = System.nanoTime();
        Profile.beginWait(className, methodName);
        long waitStartTime = System.nanoTime();
        Thread.yield();
        Profile.endWait(className, methodName);
        long waitEndTime = System.nanoTime();
        Frame frame = findFrame(className, methodName);
        assertEquals(className, frame.getClassName());
        assertEquals(methodName, frame.getMethodName());
        assertTrue(frame.getBeginTime() <= startTime);
        assertTrue(frame.getWaitBeginTime() <= waitStartTime);
        assertTrue(frame.getWaitEndTime() >= waitEndTime);
    }

    @Test
    public void testUnwind() {
        Profile.init();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        String exception = "TestException";
        try {
            throw new TestException();
        } catch (TestException e) {
            Profile.unwind(className, methodName, exception);
        }
        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        assertEquals(exception, frame.getException());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start();
        new TestAllocations();
        ClassAllocation ca = findClassAllocation("TestAllocations");
        assertEquals("TestAllocations", ca.getClassName());
        assertEquals(1, ca.getAllocCount());
    }

    @Test
    public void testRun() {
        Profile.init();
        Profile.shutdown();
    }

    private static Frame findFrame(String className, String methodName) {
        Frame frame = null;
        Frame mostRecentFrame = null;
        for (Frame f : Profile.frameList()) {
            if (f.getClassName().equals(className) && f.getMethodName().equals(methodName)) {
                frame = f;
                break;
            }
            mostRecentFrame = f;
        }
        if (frame == null) {
            throw new AssertionError("Frame not found: " + className + "." + methodName);
        }
        return frame;
    }

    private static ClassAllocation findClassAllocation(String className) {
        for (ClassAllocation ca : Profile._allocList.values()) {
            if (ca.getClassName().equals(className)) {
                return ca;
            }
        }
        throw new AssertionError("ClassAllocation not found: " + className);
    }

    private static class TestAllocations {
        public TestAllocations() {
            new TestClass().allocate();
        }

        public void allocate() {
            new Object();
        }

        public static class TestClass {
            public TestClass() {
                alloc();
            }

            public void alloc() {
                new Object();
            }
        }
    }

    private static class TestException extends Exception {}
}
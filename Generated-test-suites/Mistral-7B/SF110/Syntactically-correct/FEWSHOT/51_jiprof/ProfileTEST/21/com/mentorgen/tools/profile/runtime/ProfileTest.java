package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import com.mentorgen.tools.profile.*;
import java.util.*;
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
    public void testClear() {
        Profile.initProfiler();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertFalse(Profile._controller.isRunning());
        assertEmpty(Profile.threads());
        assertEmpty(Profile.frameList());
    }

    @Test
    public void testStart() {
        Profile.initProfiler();
        Profile.start();
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void testStop() {
        Profile.initProfiler();
        Profile.start();
        Profile.stop();
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        Profile.initProfiler();
        Profile.setFileName("output.txt");
        assertEquals("output.txt", Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() throws IOException {
        Profile.initProfiler();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.shutdown();
        assertFalse(Profile._controller.isRunning());
        assertTrue(new File("output.txt").exists());
    }

    @Test
    public void testThreads() {
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Profile.start("TestClass", "testMethod");
                Profile.end("TestClass", "testMethod");
            }
        });
        thread.start();
        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
        assertTrue(threads.contains(Thread.currentThread().getId()));
        assertTrue(threads.contains(thread.getId()));
    }

    @Test
    public void testInteractions() {
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Profile.start("TestClass", "testMethod");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end("TestClass", "testMethod");
            }
        });
        thread.start();
        List<Frame> frames = Profile.interactions(thread.getId());
        assertEquals(21, frames.size()); // 10 start, 10 end, 1 initialize
    }

    @Test
    public void testFrameList() {
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Profile.start("TestClass", "testMethod");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end("TestClass", "testMethod");
            }
        });
        thread.start();
        List<Frame> frames = Profile.frameList();
        assertEquals(21, frames.size()); // 10 start, 10 end, 1 initialize
    }

    @Test
    public void testAllocations() {
        Profile.initProfiler();
        Profile.start();
        new TestClass();
        List<ClassAllocation> allocations = Profile.allocations();
        assertEquals(1, allocations.size());
        assertEquals("TestClass", allocations.get(0).getClassName());
        assertEquals(1, allocations.get(0).getAllocationCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Profile.start("TestClass", "testMethod");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end("TestClass", "testMethod");
            }
        });
        thread.start();
        long threadId = thread.getId();
        long totalTime = Profile.getThreadTotalTime(threadId);
        long expectedTotalTime = 1000; // 100 iterations * 10 ms each
        assertEquals(expectedTotalTime, totalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Profile.start("TestClass", "testMethod1");
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end("TestClass", "testMethod1");

                Profile.start("TestClass", "testMethod2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end("TestClass", "testMethod2");
            }
        });
        thread.start();
        Profile.sortFrameList((f1, f2) -> f1.getStartTime() - f2.getStartTime());
        List<Frame> sortedFrames = new ArrayList<>(Profile.frameList());
        Frame method1Start = null;
        Frame method1End = null;
        Frame method2Start = null;
        Frame method2End = null;
        for (Frame frame : sortedFrames) {
            if (frame.getClassName().equals("TestClass") && frame.getMethodName().equals("testMethod1")) {
                if (method1Start == null) {
                    method1Start = frame;
                }
                method1End = frame;
            } else if (frame.getClassName().equals("TestClass") && frame.getMethodName().equals("testMethod2")) {
                if (method2Start == null) {
                    method2Start = frame;
                }
                method2End = frame;
            }
        }
        assertNotNull(method1Start);
        assertNotNull(method1End);
        assertNotNull(method2Start);
        assertNotNull(method2End);
        long method1Duration = method1End.getEndTime() - method1Start.getStartTime();
        long method2Duration = method2End.getEndTime() - method2Start.getStartTime();
        assertTrue(method1Duration < method2Duration);
    }

    @Test
    public void testStartEnd() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        long startTime = System.nanoTime();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Profile.end(className, methodName);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertEquals(duration, frame.getNetTime());
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.beginWait(className, methodName);
        long startTime = System.nanoTime();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Profile.endWait(className, methodName);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertEquals(duration, frame.getWaitTime());
    }

    @Test
    public void testUnwind() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        try {
            throw new Exception("Test Exception");
        } catch (Exception e) {
            Profile.unwind(className, methodName, e.getClass().getName());
        }
        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertEquals(-1, frame.getEndTime());
    }

    @Test
    public void testAlloc() {
        Profile.initProfiler();
        Profile.start();
        new TestClass();
        ClassAllocation allocation = Profile.findClassAllocation("TestClass");
        assertEquals(1, allocation.getAllocationCount());
    }

    private static ClassAllocation findClassAllocation(String className) {
        for (ClassAllocation allocation : Profile.allocations()) {
            if (allocation.getClassName().equals(className)) {
                return allocation;
            }
        }
        return null;
    }

    static class TestClass {
        TestClass() {
            alloc(this.getClass().getName());
        }
    }
}
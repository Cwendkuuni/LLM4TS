package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import com.mentorgen.tools.profile.*;
import java.util.*;
import com.mentorgen.tools.profile.output.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
    }

    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._allocList);
        assertNotNull(Profile._lock);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.clear();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._allocList);
        assertTrue(Profile._frameList.isEmpty());
        assertTrue(Profile._threadActiveFrame.isEmpty());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
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
        Profile.init();
        Profile.setFileName("test.prof");
        assertEquals("test.prof", Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        assertFalse(Profile._controller.isRunning());
        assertNotNull(ProfileDump.dump());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(() -> {
            Profile.start("TestClass", "testMethod");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass", "testMethod");
        });
        t.start();
        List<Long> threads = Profile.threads();
        assertEquals(1, threads.size());
        assertEquals(t.getId(), threads.get(0));
        Profile.shutdown();
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(() -> {
            Profile.start("TestClass", "testMethod");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass", "testMethod");
            Profile.start("TestClass", "testMethod");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass", "testMethod");
        });
        t.start();
        List<Frame> interactions = Profile.interactions(t.getId());
        assertEquals(2, interactions.size());
        Profile.shutdown();
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(() -> {
            Profile.start("TestClass", "testMethod");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass", "testMethod");
        });
        t.start();
        List<Frame> frameList = Profile.frameList();
        assertEquals(1, frameList.size());
        Profile.shutdown();
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        new Object();
        List<ClassAllocation> allocations = Profile.allocations();
        assertEquals(1, allocations.size());
        assertEquals("java.lang.Object", allocations.get(0).getClassName());
        Profile.shutdown();
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        long threadId = t.getId();
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertTrue(threadTotalTime > 0);
        Profile.shutdown();
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass1", "testMethod1");
            Profile.start("TestClass2", "testMethod2");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass2", "testMethod2");
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass1", "testMethod1");
        });
        t.start();
        Comparator<Frame> comparator = (Frame f1, Frame f2) -> Long.compare(f1.getBeginTime(), f2.getBeginTime());
        Profile.sortFrameList(comparator);
        Frame frame1 = Profile.frameList().get(0);
        Frame frame2 = Profile.frameList().get(1);
        Frame frame3 = Profile.frameList().get(2);
        assertEquals("TestClass1", frame1.getClassName());
        assertEquals("testMethod1", frame1.getMethodName());
        assertEquals("TestClass2", frame2.getClassName());
        assertEquals("testMethod2", frame2.getMethodName());
        assertEquals("TestClass1", frame3.getClassName());
        assertEquals("testMethod1", frame3.getMethodName());
        assertTrue(frame1.getBeginTime() < frame2.getBeginTime());
        assertTrue(frame1.getBeginTime() < frame3.getBeginTime());
        Profile.shutdown();
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(() -> {
            Profile.start("TestClass", "testMethod");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass", "testMethod");
        });
        t.start();
        Profile.end("TestClass", "testMethod");
        Frame frame = Profile.findFrame(t.getId(), "TestClass", "testMethod");
        assertNotNull(frame);
        Profile.shutdown();
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(() -> {
            Profile.start("TestClass", "testMethod");
            try {
                Thread.sleep(500);
                Profile.beginWait("TestClass", "testMethodWait");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.endWait("TestClass", "testMethodWait");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end("TestClass", "testMethod");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Frame frame = Profile.findFrame(t.getId(), "TestClass", "testMethod");
        assertNotNull(frame);
        assertTrue(frame.isWaiting());
        Profile.shutdown();
    }

    @Test
    public void testUnwind() {
        Profile.init();
        Profile.start();
        Thread t = new Thread(() -> {
            try {
                throw new RuntimeException("TestException");
            } catch (RuntimeException e) {
                Profile.unwind("TestClass", "testMethod", "TestException.class");
                throw e;
            }
        });
        t.start();
        Frame frame = Profile.findFrame(t.getId(), "TestClass", "testMethod");
        assertNotNull(frame);
        assertEquals("TestException.class", frame.getExceptionClassName());
        Profile.shutdown();
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start();
        new ArrayList<>();
        ClassAllocation ca = Profile.findClassAllocation("java.util.ArrayList");
        assertNotNull(ca);
        assertEquals(1, ca.getAllocCount());
        Profile.shutdown();
    }
}
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.runtime.Controller;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

    private static Profile profile;

    @BeforeClass
    public static void init() {
        Profile.initProfiler();
        Profile.init();
        Profile.clear();
        profile = new Profile();
    }

    @Test
    public void testInitAndClear() {
        Profile.init();
        Profile.clear();
        assertFalse(Profile._threadDictionary.size() > 0);
        assertFalse(Profile._frameList.size() > 0);
    }

    @Test
    public void testStartAndStop() {
        Profile.start();
        assertTrue(Controller._profile);
        Profile.stop();
        assertFalse(Controller._profile);
    }

    @Test
    public void testSetFileName() {
        String fileName = "test.txt";
        Profile.setFileName(fileName);
        assertEquals(fileName, Controller._controller.getFileName());
    }

    @Test
    public void testThreads() {
        Profile.start();
        Thread t1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass1", "testMethod1");
        });
        Thread t2 = new Thread(() -> {
            Profile.start("TestClass2", "testMethod2");
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass2", "testMethod2");
        });
        t1.start();
        t2.start();
        List<Long> threads = Profile.threads();
        assertTrue(threads.size() == 2);
        assertTrue(threads.containsAll(Arrays.asList(t1.getId(), t2.getId())));
        Profile.shutdown();
    }

    @Test
    public void testInteractions() {
        Profile.start();
        Thread t1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass1", "testMethod1");
        });
        Thread t2 = new Thread(() -> {
            Profile.start("TestClass2", "testMethod2");
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass2", "testMethod2");
        });
        t1.start();
        t2.start();
        long thread1Id = t1.getId();
        long thread2Id = t2.getId();
        Iterable<Frame> interactions = Profile.interactions(thread1Id);
        assertTrue(interactions.size() > 0);
        Iterable<Frame> interactions2 = Profile.interactions(thread2Id);
        assertTrue(interactions2.size() > 0);
        Profile.shutdown();
    }

    @Test
    public void testFrameList() {
        Profile.start();
        Thread t1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass1", "testMethod1");
        });
        Thread t2 = new Thread(() -> {
            Profile.start("TestClass2", "testMethod2");
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass2", "testMethod2");
        });
        t1.start();
        t2.start();
        Iterable<Frame> frameList = Profile.frameList();
        assertTrue(frameList.size() > 0);
        Profile.shutdown();
    }

    @Test
    public void testAllocations() {
        Profile.start();
        new TestClass1();
        new TestClass1();
        new TestClass1();
        Profile.shutdown();
        Iterable<ClassAllocation> allocations = Profile.allocations();
        String className = TestClass1.class.getName();
        int count = 0;
        for (ClassAllocation ca : (List<ClassAllocation>) allocations) {
            if (ca.getClassname().equals(className)) {
                count++;
            }
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.start();
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        long thread1Id = t1.getId();
        long thread2Id = t2.getId();
        long thread1TotalTime = Profile.getThreadTotalTime(thread1Id);
        long thread2TotalTime = Profile.getThreadTotalTime(thread2Id);
        assertTrue(thread1TotalTime > 0);
        assertTrue(thread2TotalTime > 0);
        Profile.shutdown();
    }

    @Test
    public void testSortFrameList() {
        Profile.start();
        Thread t1 = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass1", "testMethod1");
        });
        Thread t2 = new Thread(() -> {
            Profile.start("TestClass2", "testMethod2");
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass2", "testMethod2");
        });
        t1.start();
        t2.start();
        Comparator<Frame> comparator = (Frame f1, Frame f2) -> f1.getBeginTime() - f2.getBeginTime();
        Profile.sortFrameList(comparator);
        Iterable<Frame> sortedFrameList = Profile.frameList();
        Frame f1 = null, f2 = null;
        for (Frame frame : sortedFrameList) {
            if (f1 == null && frame.getClassName().equals("TestClass1")) {
                f1 = frame;
            }
            if (f2 == null && frame.getClassName().equals("TestClass2")) {
                f2 = frame;
            }
            if (f1 != null && f2 != null) {
                break;
            }
        }
        assertTrue(f1 != null && f2 != null);
        assertTrue(f1.getBeginTime() < f2.getBeginTime());
        Profile.shutdown();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        Profile.start();
        Profile.start("TestClass1", "testMethod1");
        Profile.end("TestClass1", "testMethod1");
        Profile.end();
    }

    @Test(expected = RuntimeException.class)
    public void testStartNullClassName() {
        Profile.start();
        Profile.start(null, "testMethod1");
        Profile.end();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartNullMethodName() {
        Profile.start();
        Profile.start("TestClass1", null);
        Profile.end();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndTwice() {
        Profile.start();
        Profile.start("TestClass1", "testMethod1");
        Profile.end();
        Profile.end("TestClass1", "testMethod1");
        Profile.end();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndNullClassName() {
        Profile.start();
        Profile.start("TestClass1", "testMethod1");
        Profile.end();
        Profile.end(null, "testMethod1");
    }

    @Test(expected = IllegalStateException.class)
    public void testEndNullMethodName() {
        Profile.start();
        Profile.start("TestClass1", "testMethod1");
        Profile.end();
        Profile.end("TestClass1", null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginWaitNullClassName() {
        Profile.start();
        Profile.beginWait(null, "testMethod1");
        Profile.end();
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginWaitNullMethodName() {
        Profile.start();
        Profile.beginWait("TestClass1", null);
        Profile.end();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndWaitNullClassName() {
        Profile.start();
        Profile.beginWait("TestClass1", "testMethod1");
        Profile.endWait(null, "testMethod1");
        Profile.end();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndWaitNullMethodName() {
        Profile.start();
        Profile.beginWait("TestClass1", "testMethod1");
        Profile.endWait("TestClass1", null);
        Profile.end();
    }

    @Test
    public void testUnwind() {
        try {
            throw new Exception("testException");
        } catch (Exception e) {
            Profile.unwind(TestClass1.class.getName(), "testMethod1", e.getClass().getName());
        }
    }

    @Test
    public void testAlloc() {
        Profile.alloc(TestClass1.class.getName());
        Profile.alloc(TestClass1.class.getName());
        Profile.alloc(TestClass1.class.getName());
        Iterable<ClassAllocation> allocations = Profile.allocations();
        int count = 0;
        for (ClassAllocation ca : (List<ClassAllocation>) allocations) {
            if (ca.getClassname().equals(TestClass1.class.getName())) {
                count++;
            }
        }
        assertEquals(3, count);
    }

    public static class TestClass1 {
        public TestClass1() {
        }
    }

}
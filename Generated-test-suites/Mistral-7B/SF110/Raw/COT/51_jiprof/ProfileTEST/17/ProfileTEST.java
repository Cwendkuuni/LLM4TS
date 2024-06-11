package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mentorgen.tools.profile.output.Controller;
import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;
import com.mentorgen.tools.profile.output.ThreadDictionary;

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
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._frameList.size());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        assertTrue(Controller._profile);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        Profile.init();
        Profile.start();
        Profile.start();
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        assertFalse(Controller._profile);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        Profile.init();
        Profile.stop();
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName("test.out");
        assertEquals("test.out", Controller._fileName);
    }

    @Test(expected = NullPointerException.class)
    public void testSetFileNameNull() {
        Profile.init();
        Profile.setFileName(null);
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        assertFalse(Controller._profile);
        for (Frame frame : frameList()) {
            frame.computeNetTime();
        }
    }

    @Test
    public void testThreads() {
        Profile.init();
        Thread t = new Thread(() -> {
            Profile.start("TestClass", "testMethod");
            Profile.end("TestClass", "testMethod");
        });
        t.start();
        List<Long> threads = new ArrayList<>(Profile.threads());
        assertEquals(1, threads.size());
        assertEquals(t.getId(), threads.get(0));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Thread t = new Thread(() -> {
            Profile.start("TestClass", "testMethod1");
            // Do some work
            Profile.start("TestClass", "testMethod2");
            // Do some work
            Profile.end("TestClass", "testMethod1");
            Profile.end("TestClass", "testMethod2");
        });
        t.start();
        List<Frame> interactions1 = new ArrayList<>(Profile.interactions(t.getId()));
        List<Frame> interactions2 = new ArrayList<>(Profile.interactions(t.getId()));
        assertEquals(2, interactions1.size());
        assertEquals(2, interactions2.size());
        Frame firstFrame1 = interactions1.get(0);
        Frame firstFrame2 = interactions2.get(0);
        Frame secondFrame1 = interactions1.get(1);
        Frame secondFrame2 = interactions2.get(1);
        assertEquals(firstFrame1.getClassName(), firstFrame2.getClassName());
        assertEquals(firstFrame1.getMethodName(), firstFrame2.getMethodName());
        assertEquals(secondFrame1.getClassName(), secondFrame2.getClassName());
        assertEquals(secondFrame1.getMethodName(), secondFrame2.getMethodName());
        assertNotEquals(firstFrame1, secondFrame1);
        assertNotEquals(firstFrame2, secondFrame2);
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Thread t = new Thread(() -> {
            Profile.start("TestClass", "testMethod1");
            // Do some work
            Profile.start("TestClass", "testMethod2");
            // Do some work
            Profile.end("TestClass", "testMethod1");
            Profile.end("TestClass", "testMethod2");
        });
        t.start();
        List<Frame> frameList = new ArrayList<>(Profile.frameList());
        assertEquals(4, frameList.size());
        Frame firstFrame = frameList.get(0);
        Frame secondFrame = frameList.get(1);
        Frame thirdFrame = frameList.get(2);
        Frame fourthFrame = frameList.get(3);
        assertEquals(firstFrame.getClassName(), "TestClass");
        assertEquals(firstFrame.getMethodName(), "<init>");
        assertEquals(secondFrame.getClassName(), "TestClass");
        assertEquals(secondFrame.getMethodName(), "testMethod1");
        assertEquals(thirdFrame.getClassName(), "TestClass");
        assertEquals(thirdFrame.getMethodName(), "testMethod2");
        assertEquals(fourthFrame.getClassName(), "TestClass");
        assertEquals(fourthFrame.getMethodName(), "<init>");
        assertNotEquals(firstFrame, secondFrame);
        assertNotEquals(firstFrame, thirdFrame);
        assertNotEquals(firstFrame, fourthFrame);
        assertNotEquals(secondFrame, thirdFrame);
        assertNotEquals(secondFrame, fourthFrame);
        assertNotEquals(thirdFrame, fourthFrame);
    }

    @Test
    public void testAllocations() {
        Profile.init();
        new TestClass();
        new TestClass();
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertEquals(1, allocations.size());
        assertEquals("TestClass", allocations.get(0).getClassName());
        assertEquals(2, allocations.get(0).getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Thread t = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                Profile.start("TestClass", "testMethod");
                // Do some work
                Profile.end("TestClass", "testMethod");
            }
        });
        t.start();
        long totalTime = Profile.getThreadTotalTime(t.getId());
        assertEquals(100000, totalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Thread t = new Thread(() -> {
            Profile.start("TestClass1", "testMethod1");
            // Do some work
            Profile.start("TestClass2", "testMethod2");
            // Do some work
            Profile.end("TestClass1", "testMethod1");
            Profile.end("TestClass2", "testMethod2");
        });
        t.start();
        Profile.sortFrameList((frame1, frame2) -> Long.compare(frame1.getBeginTime(), frame2.getBeginTime()));
        Frame firstFrame = Profile.frameList().get(0);
        Frame secondFrame = Profile.frameList().get(1);
        assertEquals("TestClass1", firstFrame.getClassName());
        assertEquals("testMethod1", firstFrame.getMethodName());
        assertEquals("TestClass2", secondFrame.getClassName());
        assertEquals("testMethod2", secondFrame.getMethodName());
        assertFalse(firstFrame.getBeginTime() > secondFrame.getBeginTime());
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        Thread t = new Thread(() -> {
            Profile.start("TestClass", "testMethod");
            // Do some work
            Profile.end("TestClass", "testMethod");
        });
        t.start();
        Frame frame = findFrame(t.getId(), "TestClass", "testMethod");
        assertNotNull(frame);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartNullClassName() {
        Profile.init();
        Profile.start(null, "testMethod");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartEmptyClassName() {
        Profile.init();
        Profile.start("", "testMethod");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartNullMethodName() {
        Profile.init();
        Profile.start("TestClass", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartEmptyMethodName() {
        Profile.init();
        Profile.start("TestClass", "");
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        Thread t = new Thread(() -> {
            Profile.start("TestClass", "testMethod");
            Profile.beginWait("TestClass", "testMethod1");
            // Do some work
            Profile.endWait("TestClass", "testMethod1");
            Profile.end("TestClass", "testMethod");
        });
        t.start();
        Frame frame = findFrame(t.getId(), "TestClass", "testMethod");
        assertNotNull(frame);
        assertEquals(1, frame.getBeginWaitCallCount());
        assertTrue(frame.isBeginWaitCalled());
        assertTrue(frame.isEndWaitCalled());
    }

    @Test
    public void testUnwind() {
        Profile.init();
        Thread t = new Thread(() -> {
            try {
                throw new RuntimeException("Test exception");
            } catch (Exception e) {
                Profile.unwind("TestClass", "testMethod", e.getClass().getName());
            }
        });
        t.start();
        Frame frame = findFrame(t.getId(), "TestClass", "testMethod");
        assertNotNull(frame);
        assertTrue(frame.isUnwound());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        new TestClass();
        ClassAllocation ca = Profile._allocList.get("TestClass");
        assertNotNull(ca);
        assertEquals(1, ca.getAllocCount());
    }

    @Test
    public void testRun() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
    }

    private Frame findFrame(long threadId, String className, String methodName) {
        Frame target = null;
        Frame currentFrame = Profile._threadActiveFrame.get(threadId);
        while (currentFrame != null) {
            if (currentFrame.getClassName().equals(className) && currentFrame.getMethodName().equals(methodName)) {
                target = currentFrame;
                break;
            }
            currentFrame = currentFrame.getParent();
        }
        return target;
    }

    static class TestClass {
        TestClass() {
            alloc("TestClass");
        }
    }
}
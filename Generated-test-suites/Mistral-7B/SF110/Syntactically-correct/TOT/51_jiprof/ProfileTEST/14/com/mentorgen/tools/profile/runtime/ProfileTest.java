package com.mentorgen.tools.profile.runtime;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProfileTest {

    private static Profile _profile;

    @BeforeClass
    public static void initProfiler() {
        Profile.initProfiler();
    }

    @AfterClass
    public static void clearProfiler() {
        Profile.clear();
    }

    @Test
    public void testInitAndStartProfiler() {
        Profile.init();
        Profile.start();
        assertEquals("Java Interactive Profiler: starting", System.err.getErrorStream().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitProfilerWithProfileActive() {
        Profile.init();
        Profile.init();
    }

    @Test
    public void testClearProfiler() {
        Profile.init();
        Profile.start();
        Profile.clear();
        assertEquals(0, Profile.threads().size());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
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
        Profile.init();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            start("TestClass1", "testMethod1");
            // some code here
            end("TestClass1", "testMethod1");
        });
        Thread thread2 = new Thread(() -> {
            start("TestClass2", "testMethod2");
            // some code here
            end("TestClass2", "testMethod2");
        });
        thread1.start();
        thread2.start();
        List<Frame> interactions = Profile.interactions(thread1.getId());
        assertEquals(2, interactions.size());
        Frame firstInteraction = interactions.get(0);
        assertEquals("TestClass1", firstInteraction.getClassName());
        assertEquals("testMethod1", firstInteraction.getMethodName());
        Frame secondInteraction = interactions.get(1);
        assertEquals("TestClass1", secondInteraction.getClassName());
        assertEquals("<init>", secondInteraction.getMethodName());

        interactions = Profile.interactions(thread2.getId());
        assertEquals(2, interactions.size());
        Frame firstInteraction2 = interactions.get(0);
        assertEquals("TestClass2", firstInteraction2.getClassName());
        assertEquals("testMethod2", firstInteraction2.getMethodName());
        Frame secondInteraction2 = interactions.get(1);
        assertEquals("TestClass2", secondInteraction2.getClassName());
        assertEquals("<init>", secondInteraction2.getMethodName());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            start("TestClass1", "testMethod1");
            end("TestClass1", "testMethod1");
        });
        Thread thread2 = new Thread(() -> {
            start("TestClass2", "testMethod2");
            end("TestClass2", "testMethod2");
        });
        thread1.start();
        thread2.start();
        List<Frame> frameList = Profile.frameList();
        assertEquals(4, frameList.size());
        Frame firstFrame = frameList.get(0);
        assertEquals("<clinit>", firstFrame.getMethodName());
        Frame secondFrame = frameList.get(1);
        assertEquals("TestClass1", secondFrame.getClassName());
        assertEquals("testMethod1", secondFrame.getMethodName());
        Frame thirdFrame = frameList.get(2);
        assertEquals("TestClass2", thirdFrame.getClassName());
        assertEquals("testMethod2", thirdFrame.getMethodName());
        Frame fourthFrame = frameList.get(3);
        assertEquals("<clinit>", fourthFrame.getMethodName());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        new TestClass1();
        new TestClass1();
        new TestClass2();
        new TestClass2();
        List<ClassAllocation> allocations = Profile.allocations();
        assertEquals(4, allocations.size());
        ClassAllocation testClass1Allocation = null;
        for (ClassAllocation allocation : allocations) {
            if ("TestClass1".equals(allocation.getClassName())) {
                testClass1Allocation = allocation;
                break;
            }
        }
        assertEquals(2, testClass1Allocation.getAllocCount());

        ClassAllocation testClass2Allocation = null;
        for (ClassAllocation allocation : allocations) {
            if ("TestClass2".equals(allocation.getClassName())) {
                testClass2Allocation = allocation;
                break;
            }
        }
        assertEquals(2, testClass2Allocation.getAllocCount());
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        Thread thread1 = new Thread(() -> {
            start("TestClass1", "testMethod1");
            end("TestClass1", "testMethod1");
        });
        Thread thread2 = new Thread(() -> {
            start("TestClass2", "testMethod2");
            end("TestClass2", "testMethod2");
        });
        thread1.start();
        thread2.start();
        Collections.sort(Profile.frameList());
        Frame firstFrame = Profile.frameList().get(0);
        Frame secondFrame = Profile.frameList().get(1);
        Frame thirdFrame = Profile.frameList().get(2);
        Frame fourthFrame = Profile.frameList().get(3);
        assertEquals("<clinit>", firstFrame.getMethodName());
        assertEquals("<clinit>", fourthFrame.getMethodName());
        assertEquals("TestClass1", secondFrame.getClassName());
        assertEquals("TestClass1", secondFrame.getClassName());
        assertEquals("testMethod1", secondFrame.getMethodName());
        assertEquals("TestClass2", thirdFrame.getClassName());
        assertEquals("TestClass2", thirdFrame.getClassName());
        assertEquals("testMethod2", thirdFrame.getMethodName());
    }

    @Test
    public void testBeginWaitAndEndWait() {
        Profile.init();
        Profile.start();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        start("TestClass", "testMethod");
        beginWait("TestClass", "waitMethod");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        endWait("TestClass", "waitMethod");
        end("TestClass", "testMethod");
        thread.join();
        Frame threadFrame = findFrame("TestClass", "testMethod");
        assertEquals(3, threadFrame.getWaitCount());
    }

    @Test
    public void testUnwind() {
        Profile.init();
        Profile.start();
        try {
            throw new Exception("TestException");
        } catch (Exception e) {
            unwind("TestClass", "testMethod", "TestException");
        }
        Frame threadFrame = findFrame("TestClass", "testMethod");
        assertNotNull(threadFrame.getExceptionStacktrace());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start();
        new TestClass1();
        new TestClass1();
        new TestClass2();
        new TestClass2();
        alloc("TestClass1");
        alloc("TestClass2");
        List<ClassAllocation> allocations = Profile.allocations();
        ClassAllocation testClass1Allocation = null;
        for (ClassAllocation allocation : allocations) {
            if ("TestClass1".equals(allocation.getClassName())) {
                testClass1Allocation = allocation;
                break;
            }
        }
        assertEquals(3, testClass1Allocation.getAllocCount());

        ClassAllocation testClass2Allocation = null;
        for (ClassAllocation allocation : allocations) {
            if ("TestClass2".equals(allocation.getClassName())) {
                testClass2Allocation = allocation;
                break;
            }
        }
        assertEquals(3, testClass2Allocation.getAllocCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFrameNullClassNameOrMethodName() {
        Profile.init();
        Profile.start();
        findFrame(Thread.currentThread().getId(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFrameNullThreadId() {
        Profile.init();
        Profile.start();
        findFrame(null, "TestClass", "testMethod");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFrameNullClassNameOrMethodNameOrThreadId() {
        Profile.init();
        Profile.start();
        findFrame(null, null, null);
    }

    private Frame findFrame(long threadId, String className, String methodName) {
        Profile.init();
        Profile.start();
        Thread testThread = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            start(className, methodName);
            end(className, methodName);
        });
        testThread.start();
        testThread.join();
        return Profile.findFrame(threadId, className, methodName);
    }

    static class TestClass1 {
        TestClass1() {
        }
    }

    static class TestClass2 {
        TestClass2() {
        }
    }
}
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;
import com.mentorgen.tools.profile.output.ThreadDictionary;

public class ProfileTest {

    private static Map<String, Method> methodDictionary;
    private static Map<Long, Frame> threadActiveFrame;
    private static Map<String, ClassAllocation> allocList;
    private static ThreadDictionary threadDictionary;
    private static Iterable<Frame> frameList;

    @Before
    public void setUp() {
        Profile.init();
        methodDictionary = new HashMap<String, Method>(2003);
        threadActiveFrame = new HashMap<Long, Frame>(1001);
        allocList = new HashMap<String, ClassAllocation>();
        threadDictionary = new ThreadDictionary();
        frameList = Profile._frameList;
    }

    @After
    public void tearDown() {
        Profile.clear();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
    }

    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadDictionary);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertTrue(Profile._threadDictionary.size() == 0);
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        Frame frame = new Frame(null, new Method("TestClass", "testMethod"), 1);
        frame.setBeginTime(System.nanoTime());
        threadActiveFrame.put(1L, frame);
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Frame frame = new Frame(null, new Method("TestClass", "testMethod"), 1);
        frame.setBeginTime(System.nanoTime());
        threadActiveFrame.put(1L, frame);
        Profile.stop();
        assertTrue(Profile._controller.isStopped());
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName("test.txt");
        assertTrue(Profile._controller.getFileName().equals("test.txt"));
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), 1);
        frame1.setBeginTime(System.nanoTime());
        threadActiveFrame.put(1L, frame1);
        Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), 2);
        frame2.setBeginTime(System.nanoTime());
        threadActiveFrame.put(2L, frame2);
        Iterable<Long> threads = Profile.threads();
        List<Long> threadIds = new ArrayList<Long>(2);
        threadIds.add(1L);
        threadIds.add(2L);
        Collections.sort(threadIds);
        for (Long threadId : threadIds) {
            assertTrue(threads.contains(threadId));
        }
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), 1);
        frame1.setBeginTime(System.nanoTime());
        threadActiveFrame.put(1L, frame1);
        Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), 2);
        frame2.setBeginTime(System.nanoTime());
        threadActiveFrame.put(2L, frame2);
        Frame frame3 = new Frame(frame1, new Method("TestClass3", "testMethod3"), 3);
        frame3.setBeginTime(System.nanoTime());
        threadActiveFrame.put(3L, frame3);
        Iterable<Frame> interactions = Profile.interactions(1L);
        List<Long> interactionIds = new ArrayList<Long>(2);
        interactionIds.add(frame1.getId());
        interactionIds.add(frame3.getId());
        Collections.sort(interactionIds);
        for (Long interactionId : interactionIds) {
            assertTrue(interactions.contains(new Frame(null, null, interactionId)));
        }
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), 1);
        frame1.setBeginTime(System.nanoTime());
        threadActiveFrame.put(1L, frame1);
        Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), 2);
        frame2.setBeginTime(System.nanoTime());
        threadActiveFrame.put(2L, frame2);
        Frame frame3 = new Frame(frame1, new Method("TestClass3", "testMethod3"), 3);
        frame3.setBeginTime(System.nanoTime());
        threadActiveFrame.put(3L, frame3);
        Iterable<Frame> frameList = Profile.frameList();
        List<Long> frameIds = new ArrayList<Long>(3);
        frameIds.add(frame1.getId());
        frameIds.add(frame3.getId());
        frameIds.add(frame2.getId());
        Collections.sort(frameIds);
        for (Long frameId : frameIds) {
            assertTrue(frameList.contains(new Frame(null, null, frameId)));
        }
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        Profile.alloc("TestClass1");
        Profile.alloc("TestClass1");
        Iterable<ClassAllocation> allocations = Profile.allocations();
        Map<String, Integer> allocationCounts = new HashMap<String, Integer>() {{
            put("TestClass1", 2);
        }};
        for (ClassAllocation allocation : allocations) {
            assertTrue(allocationCounts.get(allocation.getName()).equals(allocation.getAllocCount()));
        }
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), 1);
        frame1.setBeginTime(System.nanoTime());
        threadActiveFrame.put(1L, frame1);
        Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), 2);
        frame2.setBeginTime(System.nanoTime());
        threadActiveFrame.put(2L, frame2);
        Frame frame3 = new Frame(frame1, new Method("TestClass3", "testMethod3"), 3);
        frame3.setBeginTime(System.nanoTime());
        threadActiveFrame.put(3L, frame3);
        Frame frame4 = new Frame(frame3, new Method("TestClass4", "testMethod4"), 4);
        frame4.setBeginTime(System.nanoTime());
        threadActiveFrame.put(4L, frame4);
        long thread1TotalTime = Profile.getThreadTotalTime(1L);
        long thread2TotalTime = Profile.getThreadTotalTime(2L);
        long thread3TotalTime = Profile.getThreadTotalTime(3L);
        long thread4TotalTime = Profile.getThreadTotalTime(4L);
        long expectedTotalTime = System.nanoTime() - frame1.getBeginTime();
        assertTrue(thread1TotalTime + thread2TotalTime + thread3TotalTime + thread4TotalTime == expectedTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), 1);
        frame1.setBeginTime(System.nanoTime());
        threadActiveFrame.put(1L, frame1);
        Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), 2);
        frame2.setBeginTime(System.nanoTime());
        threadActiveFrame.put(2L, frame2);
        Frame frame3 = new Frame(frame1, new Method("TestClass3", "testMethod3"), 3);
        frame3.setBeginTime(System.nanoTime());
        threadActiveFrame.put(3L, frame3);
        Collections.sort(frameList, new Comparator<Frame>() {
            @Override
            public int compare(Frame f1, Frame f2) {
                return (int) (f1.getBeginTime() - f2.getBeginTime());
            }
        });
        Frame sortedFrame1 = frameList.iterator().next();
        Frame sortedFrame3 = frameList.iterator().next();
        Frame sortedFrame2 = frameList.iterator().next();
        assertTrue(sortedFrame1.getBeginTime() < sortedFrame2.getBeginTime());
        assertTrue(sortedFrame2.getBeginTime() < sortedFrame3.getBeginTime());
    }
}
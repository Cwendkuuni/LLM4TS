package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Profile.init();
        Profile.shutdown();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testStartAndEnd() {
        Profile.init();
        long threadId = Thread.currentThread().getId();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");

        Frame threadFrame = Profile._threadDictionary.get(threadId);
        assertNotNull(threadFrame);
        assertEquals("TestClass", threadFrame.getClassName());
        assertEquals("testMethod", threadFrame.getMethodName());
        assertNull(threadFrame.getParent());
        assertEquals(0, threadFrame.getNetTime());
        assertEquals(0, threadFrame.getOverhead());
        assertEquals(System.nanoTime(), threadFrame.getBeginTime());
        assertEquals(System.nanoTime(), threadFrame.getEndTime());

        Profile.shutdown();
    }

    @Test
    public void testBeginWaitAndEndWait() {
        Profile.init();
        long threadId = Thread.currentThread().getId();
        Profile.start("TestClass", "testMethod");
        Profile.beginWait("TestClass", "testWaitMethod");
        long start = System.nanoTime();
        Profile.endWait("TestClass", "testWaitMethod");
        Frame threadFrame = Profile._threadDictionary.get(threadId);
        Frame waitFrame = threadFrame.getChild("TestClass", "testWaitMethod");
        assertNotNull(waitFrame);
        assertEquals("TestClass", waitFrame.getClassName());
        assertEquals("testWaitMethod", waitFrame.getMethodName());
        assertNull(waitFrame.getParent());
        assertEquals(0, waitFrame.getNetTime());
        assertEquals(System.nanoTime() - start, waitFrame.getOverhead());
        assertEquals(System.nanoTime(), waitFrame.getBeginTime());
        assertEquals(System.nanoTime(), waitFrame.getEndTime());
        Profile.end("TestClass", "testMethod");
        Profile.shutdown();
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start("TestClass", "<init>");
        Profile.alloc("TestClass");
        Profile.end("TestClass", "<init>");
        Profile.shutdown();

        Map<String, ClassAllocation> allocMap = Profile._allocList;
        ClassAllocation alloc = allocMap.get("TestClass");
        assertNotNull(alloc);
        assertEquals(1, alloc.getAllocCount());
    }

    @Test
    public void testMultipleThreads() throws InterruptedException {
        Profile.init();

        Thread thread1 = new Thread(() -> {
            long threadId = Thread.currentThread().getId();
            Profile.start("Thread1Class", "thread1Method");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            Profile.end("Thread1Class", "thread1Method");
        });

        Thread thread2 = new Thread(() -> {
            long threadId = Thread.currentThread().getId();
            Profile.start("Thread2Class", "thread2Method");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            Profile.end("Thread2Class", "thread2Method");
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        Profile.shutdown();

        List<Long> threadIds = new ArrayList<>(Profile._threadDictionary.keySet());
        Collections.sort(threadIds);

        int index1 = threadIds.indexOf(thread1.getId());
        int index2 = threadIds.indexOf(thread2.getId());

        Frame thread1Frame = Profile._threadDictionary.get(thread1.getId());
        Frame thread2Frame = Profile._threadDictionary.get(thread2.getId());

        // Check the order of frames in the list
        List<Frame> frameList = Profile.frameList();
        Frame frame1 = frameList.get(index1);
        Frame frame2 = frameList.get(index2);

        assertEquals(frame1, thread1Frame);
        assertEquals(frame2, thread2Frame);

        // Check the thread's total time
        long thread1TotalTime = Profile.getThreadTotalTime(thread1.getId());
        long thread2TotalTime = Profile.getThreadTotalTime(thread2.getId());

        assertTrue(thread1TotalTime > 0);
        assertTrue(thread2TotalTime > 0);
        assertTrue(thread1TotalTime < thread2TotalTime);
    }
}
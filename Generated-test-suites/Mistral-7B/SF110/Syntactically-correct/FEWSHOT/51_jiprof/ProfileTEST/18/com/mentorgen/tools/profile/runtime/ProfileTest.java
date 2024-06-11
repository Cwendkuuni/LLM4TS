package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.*;
import java.io.*;
import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;
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
        assertTrue(Profile._threadDictionary != null);
        assertTrue(Profile._methodDictionary != null);
        assertTrue(Profile._frameList != null);
        assertTrue(Profile._allocList != null);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start();
        Profile.end("TestClass", "testMethod");
        Profile.clear();
        assertTrue(Profile._threadDictionary.size() == 0);
        assertTrue(Profile._frameList.size() == 0);
    }

    @Test
    public void testStartAndEnd() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        Profile.start("TestClass", "testMethod");
        Profile.end("TestClass", "testMethod");
        Frame frame = findFrame(threadId, "TestClass", "testMethod");
        assertNotNull(frame);
        Profile.end("TestClass", "testMethod"); //should not throw an error
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartWithNullClassName() {
        Profile.init();
        Profile.start(null, "testMethod");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartWithNullMethodName() {
        Profile.init();
        Profile.start("TestClass", null);
    }

    @Test
    public void testStartMultipleThreads() {
        Profile.init();
        Profile.start();
        Thread thread1 = new Thread(()->{
            Profile.start("TestClass1", "testMethod1");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass1", "testMethod1");
        });
        Thread thread2 = new Thread(()->{
            Profile.start("TestClass2", "testMethod2");
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Profile.end("TestClass2", "testMethod2");
        });
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Iterable<Long> threads = Profile.threads();
        assertEquals(2, Collections.size(threads));
        Iterator<Long> threadIterator = threads.iterator();
        long threadId1 = threadIterator.next();
        long threadId2 = threadIterator.next();

        Iterable<Frame> frames1 = Profile.interactions(threadId1);
        Iterator<Frame> frameIterator1 = frames1.iterator();
        Frame frame1 = frameIterator1.next();
        assertEquals("TestClass1", frame1.getClassName());
        assertEquals("testMethod1", frame1.getMethodName());

        Iterable<Frame> frames2 = Profile.interactions(threadId2);
        Iterator<Frame> frameIterator2 = frames2.iterator();
        Frame frame2 = frameIterator2.next();
        assertEquals("TestClass2", frame2.getClassName());
        assertEquals("testMethod2", frame2.getMethodName());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start();
        String className = "TestClass";
        for (int i = 0; i < 10; i++) {
            Profile.alloc(className);
        }
        ClassAllocation ca = Profile._allocList.get(className);
        assertEquals(10, ca.getAllocCount());
    }

    @Test
    public void testFindFrame() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        Profile.start("TestClass", "testMethod");
        Frame target = findFrame(threadId, "TestClass", "testMethod");
        assertNotNull(target);
    }

    @Test
    public void testFindFrameNull() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        Frame target = findFrame(threadId, "TestClass1", "testMethod");
        assertNull(target);
    }

    private Frame findFrame(long threadId, String className, String methodName) {
        Frame target = null;
        Frame current = Profile._threadActiveFrame.get(threadId);
        while (current != null && (!current.getClassName().equals(className) || !current.getMethodName().equals(methodName))) {
            target = current;
            current = current.getParent();
        }
        return target;
    }
}
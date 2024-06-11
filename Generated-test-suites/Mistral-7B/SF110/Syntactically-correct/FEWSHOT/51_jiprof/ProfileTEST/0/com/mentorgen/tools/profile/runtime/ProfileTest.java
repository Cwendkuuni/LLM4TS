package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;
import java.io.*;

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
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._methodDictionary.size());
    }

    @Test
    public void testStartEnd() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Frame frame = findFrame(Profile._frameList, className, methodName);
        assertNotNull(frame);
        assertEquals(0, frame.getChildren().size());
    }

    @Test
    public void testBeginEndWait() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.beginWait(className, methodName);
        long waitTime = 50;
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Profile.endWait(className, methodName);
        Profile.end(className, methodName);
        Frame frame = findFrame(Profile._frameList, className, methodName);
        assertNotNull(frame);
        assertTrue(frame.isWaiting());
        assertEquals(waitTime, frame.getWaitTimeNanos());
    }

    @Test
    public void testUnwind() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        String exception = "TestException";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Profile.unwind(className, methodName, exception);
        Frame frame = findFrame(Profile._frameList, className, methodName);
        assertNotNull(frame);
        Frame unwoundFrame = findFrame(Profile._frameList, className, "unwind");
        assertNotNull(unwoundFrame);
        assertTrue(unwoundFrame.getException() != null);
        assertEquals(exception, unwoundFrame.getException().getClass().getName());
    }

    @Test
    public void testAlloc() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        Profile.alloc(className);
        Profile.alloc(className);
        ClassAllocation ca = Profile._allocList.get(className);
        assertNotNull(ca);
        assertEquals(2, ca.getAllocCount());
    }

    @Test(expected = AssertionError.class)
    public void testDoubleStart() {
        Profile.initProfiler();
        Profile.start();
        Profile.start();
    }

    @Test(expected = AssertionError.class)
    public void testDoubleEnd() {
        Profile.initProfiler();
        Profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Profile.end(className, methodName);
    }

    private Frame findFrame(Iterable<Frame> frameList, String className, String methodName) {
        for (Frame frame : frameList) {
            if (frame.getClassName().equals(className) && frame.getMethodName().equals(methodName)) {
                return frame;
            }
        }
        return null;
    }

    @AfterClass
    public static void tearDown() {
        Profile.clear();
        Profile.initProfiler();
    }
}
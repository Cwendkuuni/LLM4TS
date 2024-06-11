package com.mentorgen.tools.profile.runtime.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;
import com.mentorgen.tools.profile.runtime.*;

public class ProfileTest {

 private static Profile underTest;

 @Before
 public void setUp() {
 Profile.init();
 underTest = new Profile();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 }

 @Test(expected = IllegalStateException.class)
 public void testInitProfilerTwice() {
 Profile.initProfiler();
 Profile.initProfiler();
 fail("Should throw an exception as profiler can only be initialized once");
 }

 @Test
 public void testInit() {
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._allocList);
 assertNotNull(Profile._lock);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertTrue(Profile._threadDictionary.isEmpty());
 }

 @Test
 public void testStart() {
 Profile.start();
 assertTrue(Profile._controller.isRunning());
 }

 @Test
 public void testStop() {
 Profile.stop();
 assertFalse(Profile._controller.isRunning());
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("output.txt");
 assertEquals("output.txt", Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 Profile.shutdown();
 assertTrue(Profile._controller.isClosed());
 }

 @Test
 public void testThreads() {
 assertFalse(Profile.threads().isEmpty());
 }

 @Test
 public void testInteractions() {
 Long threadId = 1L;
 assertTrue(Profile.interactions(threadId).isEmpty());
 Profile.start("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(1, interactions.size());
 }

 @Test
 public void testFrameList() {
 assertTrue(Profile.frameList().isEmpty());
 Profile.start("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 List<Frame> frameList = Profile.frameList();
 assertEquals(1, frameList.size());
 }

 @Test
 public void testAllocations() {
 assertFalse(Profile.allocations().isEmpty());
 }

 @Test
 public void testGetThreadTotalTime() {
 Long threadId = 1L;
 Profile.start("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 long totalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(-1, totalTime); // total time should be set to -1 when the thread is not present in the dictionary
 Profile.start("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 totalTime = Profile.getThreadTotalTime(threadId);
 assertTrue(totalTime > 0);
 }

 @Test
 public void testSortFrameList() {
 List<Frame> frameList = new ArrayList<>();
 Frame frame1 = new Frame(null, new Method("TestClass", "testMethod"), 1L);
 Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), 2L);
 frameList.add(frame1);
 frameList.add(frame2);
 Collections.sort(frameList, Frame.COMPARATOR);
 List<Frame> sortedFrameList = Profile.frameList();
 assertEquals(frame1, sortedFrameList.get(0));
 assertEquals(frame2, sortedFrameList.get(1));
 }

 @Test
 public void testStartWithExistingClassNameAndMethodName() {
 Profile.init();
 String className = "TestClass";
 String methodName = "testMethod";
 Frame previousFrame = new Frame(null, new Method(className, methodName), 1L);
 Profile.start(className, methodName);
 Frame currentFrame = Profile._threadActiveFrame.get(1L);
 assertEquals(previousFrame, currentFrame);
 }

 @Test(expected = NullPointerException.class)
 public void testStartWithNullClassName() {
 Profile.start(null, "testMethod");
 fail("Should throw a NullPointerException as className should not be null");
 }

 @Test(expected = NullPointerException.class)
 public void testStartWithNullMethodName() {
 Profile.start("TestClass", null);
 fail("Should throw a NullPointerException as methodName should not be null");
 }

 @Test
 public void testEndWithNonExistentClassNameAndMethodName() {
 Profile.init();
 String className = "TestClass";
 String methodName = "testMethod";
 Profile.start(className, methodName);
 Profile.end("TestClass2", "testMethod2");
 assertNull(Profile._threadActiveFrame.get(1L));
 }

 @Test
 public void testStartWithExistingClassNameButDifferentMethodName() {
 Profile.init();
 String className = "TestClass";
 String previousMethodName = "testMethod1";
 String newMethodName = "testMethod2";
 Profile.start(className, previousMethodName);
 Frame previousFrame = Profile._threadActiveFrame.get(1L);
 Profile.start(className, newMethodName);
 Frame currentFrame = Profile._threadActiveFrame.get(1L);
 assertNotEquals(previousFrame, currentFrame);
 }

 @Test
 public void testEndWithNullClassName() {
 Profile.end(null, "testMethod");
 fail("Should throw a NullPointerException as className should not be null");
 }

 @Test
 public void testEndWithNullMethodName() {
 Profile.end("TestClass", null);
 fail("Should throw a NullPointerException as methodName should not be null");
 }

 @Test
 public void testBeginWaitAndEndWait() {
 Profile.init();
 String className = "TestClass";
 String methodName = "testMethod";
 Profile.start(className, methodName);
 long startTime = System.nanoTime();
 Profile.beginWait(className, methodName);
 long beginWaitTime = System.nanoTime();
 Profile.endWait(className, methodName);
 long endWaitTime = System.nanoTime();
 Frame frame = Profile._threadActiveFrame.get(1L);
 assertEquals(beginWaitTime, frame.getBeginWaitTime(), 0.001);
 assertEquals(endWaitTime, frame.getEndWaitTime(), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testBeginWaitWithNullClassName() {
 Profile.beginWait(null, "testMethod");
 fail("Should throw a NullPointerException as className should not be null");
 }

 @Test(expected = NullPointerException.class)
 public void testBeginWaitWithNullMethodName() {
 Profile.beginWait("TestClass", null);
 fail("Should throw a NullPointerException as methodName should not be null");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBeginWaitWithEmptyClassName() {
 Profile.beginWait("", "testMethod");
 fail("Should throw an IllegalArgumentException as className should not be empty");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBeginWaitWithEmptyMethodName() {
 Profile.beginWait("TestClass", "");
 fail("Should throw an IllegalArgumentException as methodName should not be empty");
 }

 @Test
 public void testUnwind() {
 Profile.init();
 String className = "TestClass";
 String methodName = "testMethod";
 String exception = "TestException";
 Profile.start(className, methodName);
 Profile.unwind(className, methodName, exception);
 Frame frame = Profile._threadActiveFrame.get(1L);
 assertEquals(exception, frame.getException());
 }

 @Test(expected = NullPointerException.class)
 public void testUnwindWithNullClassName() {
 Profile.unwind(null, "testMethod", "TestException");
 fail("Should throw a NullPointerException as className should not be null");
 }

 @Test(expected = NullPointerException.class)
 public void testUnwindWithNullMethodName() {
 Profile.unwind("TestClass", null, "TestException");
 fail("Should throw a NullPointerException as methodName should not be null");
 }

 @Test(expected = NullPointerException.class)
 public void testUnwindWithNullException() {
 Profile.unwind("TestClass", "testMethod", null);
 fail("Should throw a NullPointerException as exception should not be null");
 }

 @Test
 public void testAlloc() {
 Profile.init();
 String className = "TestClass";
 Profile.alloc(className);
 ClassAllocation classAllocation = Profile._allocList.get(className);
 assertEquals(1, classAllocation.getAllocCount());
 }

 @Test
 public void testAllocForExistingClass() {
 Profile.init();
 String className = "TestClass";
 Profile.alloc(className);
 Profile.alloc(className);
 ClassAllocation classAllocation = Profile._allocList.get(className);
 assertEquals(2, classAllocation.getAllocCount());
 }

 @Test(expected = NullPointerException.class)
 public void testAllocWithNullClassName() {
 Profile.alloc(null);
 fail("Should throw a NullPointerException as className should not be null");
 }
}
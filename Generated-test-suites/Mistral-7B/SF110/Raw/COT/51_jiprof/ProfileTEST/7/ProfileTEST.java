package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {

 private static Profile underTest;
 static {
 try {
 Profile.initProfiler();
 } catch (Exception e) {
 e.printStackTrace();
 }
 underTest = new Profile();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(underTest._controllerThread != null);
 }

 @Test
 public void testInit() {
 Profile.init();
 assertNotNull(underTest._threadDictionary);
 assertNotNull(underThread._frameList);
 assertNotNull(underTest._methodDictionary);
 assertNotNull(underTest._allocList);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertTrue(underTest._threadDictionary.isEmpty());
 assertTrue(underTest._frameList.isEmpty());
 assertTrue(underTest._methodDictionary.isEmpty());
 assertTrue(underTest._allocList.isEmpty());
 }

 @Test
 public void testStart() {
 Profile.start();
 assertTrue(underTest._controller.isRunning());
 }

 @Test
 public void testStop() {
 Profile.stop();
 assertTrue(underTest._controller.isStopped());
 }

 @Test
 public void testSetFileName() {
 String fileName = "test.txt";
 Profile.setFileName(fileName);
 assertEquals(fileName, underTest._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 Profile.shutdown();
 assertTrue(underTest._controller.isClosed());
 }

 @Test
 public void testThreads() {
 long threadId1 = 1;
 long threadId2 = 2;
 Profile.init();
 Profile.start(threadId1, "TestClass1", "testMethod1");
 Profile.start(threadId2, "TestClass2", "testMethod2");
 List<Long> threads = Profile.threads();
 assertTrue(threads.size() == 2);
 assertTrue(threads.contains(threadId1));
 assertTrue(threads.contains(threadId2));
 }

 @Test
 public void testInteractions() {
 long threadId = 1;
 Profile.init();
 Profile.start(threadId, "TestClass1", "testMethod1");
 Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), threadId);
 Frame frame2 = new Frame(frame1, new Method("TestClass1", "testMethod2"), threadId);
 Profile.end(threadId, "TestClass1", "testMethod1");
 List<Frame> interactions = Profile.interactions(threadId);
 assertTrue(interactions.size() == 2);
 assertTrue(interactions.get(0) == frame1);
 assertTrue(interactions.get(1) == frame2);
 }

 @Test
 public void testFrameList() {
 Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), 1);
 Frame frame2 = new Frame(frame1, new Method("TestClass1", "testMethod2"), 1);
 Profile.init();
 Profile.start(1, "TestClass1", "testMethod1");
 Profile.end(1, "TestClass1", "testMethod1");
 Profile.start(1, "TestClass1", "testMethod2");
 Profile.end(1, "TestClass1", "testMethod2");
 List<Frame> frameList = Profile.frameList();
 assertTrue(frameList.size() == 2);
 assertTrue(frameList.get(0) == frame1);
 assertTrue(frameList.get(1) == frame2);
 }

 @Test
 public void testAllocations() {
 String className = "TestClass";
 Profile.init();
 Profile.start(1, className, "<init>");
 Profile.alloc(className);
 Profile.alloc(className);
 Profile.alloc(className);
 List<ClassAllocation> allocations = Profile.allocations();
 assertTrue(allocations.size() == 1);
 ClassAllocation ca = allocations.get(0);
 assertEquals(className, ca.getClassName());
 assertEquals(3, ca.getAllocationCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 long threadId = 1;
 Profile.init();
 long startTime = System.nanoTime();
 Profile.start(threadId, "TestClass1", "testMethod1");
 Profile.end(threadId, "TestClass1", "testMethod1");
 long endTime = System.nanoTime();
 long totalTime = Profile.getThreadTotalTime(threadId);
 assertTrue(totalTime > startTime && totalTime < endTime);
 }

 @Test(expected = RuntimeException.class)
 public void testFindFrameNullThreadId() {
 String className = "TestClass";
 String methodName = "testMethod";
 Profile.findFrame(0, className, methodName);
 fail("Expected RuntimeException but no exception was thrown.");
 }

 @Test(expected = RuntimeException.class)
 public void testFindFrameNullClassName() {
 long threadId = 1;
 String methodName = "testMethod";
 Profile.findFrame(threadId, null, methodName);
 fail("Expected RuntimeException but no exception was thrown.");
 }

 @Test(expected = RuntimeException.class)
 public void testFindFrameNullMethodName() {
 long threadId = 1;
 String className = "TestClass";
 Profile.findFrame(threadId, className, null);
 fail("Expected RuntimeException but no exception was thrown.");
 }

 @Test(expected = RuntimeException.class)
 public void testFindFrameInvalidClassName() {
 long threadId = 1;
 String className = "InvalidClass";
 String methodName = "testMethod";
 Profile.findFrame(threadId, className, methodName);
 fail("Expected RuntimeException but no exception was thrown.");
 }

 @Test(expected = RuntimeException.class)
 public void testFindFrameInvalidMethodName() {
 long threadId = 1;
 String className = "TestClass";
 String methodName = "invalidMethod";
 Profile.findFrame(threadId, className, methodName);
 fail("Expected RuntimeException but no exception was thrown.");
 }

 @Test
 public void testAllocUninitialized() {
 String className = "TestClass";
 Profile.alloc(className);
 fail("Expected IllegalStateException but no exception was thrown.");
 }
}
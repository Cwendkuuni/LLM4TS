package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfileTest {

 private static Profile profile;

 @BeforeClass
 public static void setUpClass() {
 Profile.init();
 profile = new Profile();
 }

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
 Profile.clear();
 assertTrue(Profile._threadDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
 assertTrue(Profile._methodDictionary.isEmpty());
 }

 @Test
 public void testStart() {
 Profile.start();
 assertTrue(Profile._controller.isRunning());
 }

 @Test
 public void testStop() {
 Profile.stop();
 assertTrue(Profile._controller.isStopped());
 }

 @Test
 public void testSetFileName() {
 String fileName = "test.out";
 Profile.setFileName(fileName);
 assertEquals(fileName, Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 Profile.shutdown();
 assertTrue(Profile._controller.isClosed());
 }

 @Test
 public void testThreads() {
 long threadId1 = Thread.currentThread().getId();
 long threadId2 = new Thread(() -> {}).start().getId();
 List<Long> threads = Profile.threads();
 assertTrue(threads.contains(threadId1));
 assertTrue(threads.contains(threadId2));
 }

 @Test
 public void testInteractions() {
 long threadId = Thread.currentThread().getId();
 String className = "TestClass";
 String methodName = "testMethod";
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(1, interactions.size());
 Frame interaction = interactions.get(0);
 assertEquals(className, interaction.getClassName());
 assertEquals(methodName, interaction.getMethodName());
 }

 @Test
 public void testFrameList() {
 String className = "TestClass";
 String methodName1 = "testMethod1";
 String methodName2 = "testMethod2";
 Profile.start(className, methodName1);
 Profile.end(className, methodName1);
 Profile.start(className, methodName2);
 Profile.end(className, methodName2);
 Iterable<Frame> frameList = Profile.frameList();
 assertEquals(2, frameList.spliterator().getExactSizeIfKnown());
 Frame frame1 = (Frame) frameList.iterator().next();
 Frame frame2 = (Frame) frameList.iterator().next();
 assertEquals(methodName1, frame1.getMethodName());
 assertEquals(methodName2, frame2.getMethodName());
 }

 @Test
 public void testAllocations() {
 String className = "TestClass";
 Profile.alloc(className);
 Profile.alloc(className);
 Iterable<ClassAllocation> allocations = Profile.allocations();
 assertEquals(1, allocations.spliterator().getExactSizeIfKnown());
 ClassAllocation allocation = (ClassAllocation) allocations.iterator().next();
 assertEquals(className, allocation.getClassname());
 assertEquals(2, allocation.getAllocCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 long threadId1 = Thread.currentThread().getId();
 long threadId2 = new Thread(() -> {}).start().getId();
 Profile.start(className, methodName);
 Thread.yield();
 Thread.yield();
 long time1 = System.nanoTime();
 Profile.end(className, methodName);
 long time2 = System.nanoTime();
 long timeDifference = time2 - time1;
 long threadTotalTime1 = Profile.getThreadTotalTime(threadId1);
 long threadTotalTime2 = Profile.getThreadTotalTime(threadId2);
 assertTrue(threadTotalTime1 == 0);
 assertTrue(threadTotalTime2 == 0);
 Profile.start(className, methodName);
 Thread.yield();
 Thread.yield();
 time1 = System.nanoTime();
 Profile.end(className, methodName);
 time2 = System.nanoTime();
 timeDifference = time2 - time1;
 long expectedThreadTotalTime = timeDifference;
 Profile.sortFrameList((frame1, frame2) -> Long.compare(frame1.getBeginTime(), frame2.getBeginTime()));
 long threadTotalTime = Profile.getThreadTotalTime(threadId1);
 assertEquals(expectedThreadTotalTime, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 String className = "TestClass";
 String methodName1 = "testMethod1";
 String methodName2 = "testMethod2";
 Profile.start(className, methodName1);
 Profile.end(className, methodName1);
 Profile.start(className, methodName2);
 Profile.end(className, methodName2);
 Profile.sortFrameList(null);
 Iterator<Frame> frameIterator = Profile.frameList().iterator();
 Frame frame1 = frameIterator.next();
 Frame frame2 = frameIterator.next();
 assertTrue(frame1.getBeginTime() < frame2.getBeginTime());
 }

 @Test
 public void testStartEnd() {
 String className = "TestClass";
 String methodName = "testMethod";
 long startTime = System.nanoTime();
 Profile.start(className, methodName);
 long endTime = System.nanoTime();
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
 assertEquals(methodName, frame.getMethodName());
 long timeDifference = endTime - startTime;
 long expectedNetTime = timeDifference - frame.getOverhead();
 long actualNetTime = frame.getNetTime();
 assertEquals(expectedNetTime, actualNetTime);
 }

 @Test
 public void testBeginWaitEndWait() {
 String className = "TestClass";
 String methodName = "testMethod";
 long startTime = System.nanoTime();
 Profile.beginWait(className, methodName);
 Thread.yield();
 Thread.yield();
 long waitStartTime = System.nanoTime();
 Profile.endWait(className, methodName);
 long waitEndTime = System.nanoTime();
 long waitTimeDifference = waitEndTime - waitStartTime;
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
 assertEquals(methodName, frame.getMethodName());
 long netTime = frame.getNetTime();
 long expectedWaitTime = waitTimeDifference - netTime;
 long actualWaitTime = frame.getWaitTime();
 assertEquals(expectedWaitTime, actualWaitTime);
 }

 @Test
 public void testUnwind() {
 String className = "TestClass";
 String methodName = "testMethod";
 String exception = "TestException";
 try {
 throw new TestException();
 } catch (TestException e) {
 Profile.unwind(className, methodName, exception);
 }
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
 assertNotNull(frame);
 assertEquals(methodName, frame.getMethodName());
 }

 @Test
 public void testAlloc() {
 String className = "TestClass";
 for (int i = 0; i < 100; i++) {
 new TestClass();
 }
 Iterable<ClassAllocation> allocations = Profile.allocations();
 ClassAllocation allocation = (ClassAllocation) allocations.iterator().next();
 assertEquals(101, allocation.getAllocCount());
 }
}
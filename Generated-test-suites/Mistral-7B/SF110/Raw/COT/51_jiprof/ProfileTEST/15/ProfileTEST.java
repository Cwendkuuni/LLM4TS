package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import com.mentorgen.tools.profile.*;
import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfileTest {

 private static Profile underTest;
 private static List<Frame> frameList;

 @BeforeClass
 public static void setUpClass() {
 Profile.init();
 underTest = new Profile();
 frameList = Profile.frameList();
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
 Profile.clear();
 assertTrue(Profile._threadDictionary.isEmpty());
 assertTrue(Profile._methodDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
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
 Profile.setFileName("testOutput.txt");
 assertEquals("testOutput.txt", Profile._controller.getFileName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShutdownWhenProfilerIsNotRunning() {
 Profile.shutdown();
 fail("Should throw an IllegalArgumentException as the profiler is not running.");
 }

 @Test
 public void testThreads() {
 Long threadId = Thread.currentThread().getId();
 Iterable<Long> threads = Profile.threads();
 assertTrue(threads.contains(threadId));
 }

 @Test
 public void testInteractions() {
 Long threadId = Thread.currentThread().getId();
 Frame frame = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Profile._threadDictionary.add(threadId, frame);
 Iterable<Frame> interactions = Profile.interactions(threadId);
 assertTrue(interactions.contains(frame));
 }

 @Test
 public void testFrameList() {
 Frame frame = new Frame(null, new Method("TestClass", "<init>"), 1L);
 Profile._frameList.add(frame);
 Iterable<Frame> frameList = Profile.frameList();
 assertTrue(frameList.contains(frame));
 }

 @Test
 public void testAllocations() {
 assertFalse(Profile._allocList.isEmpty());
 }

 @Test
 public void testGetThreadTotalTime() {
 Long threadId = 1L;
 Frame frame = new Frame(null, new Method("TestClass", "<init>"), threadId);
 long startTime = System.nanoTime();
 Profile._threadDictionary.add(threadId, frame);
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(0, threadTotalTime);
 Profile.end(null, null);
 threadTotalTime = Profile.getThreadTotalTime(threadId);
 long endTime = System.nanoTime();
 long expectedThreadTotalTime = endTime - startTime;
 assertEquals(expectedThreadTotalTime, threadTotalTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSortFrameListWithNullComparator() {
 Profile.sortFrameList(null);
 fail("Should throw an IllegalArgumentException as the comparator is null.");
 }

 @Test
 public void testStartWithNullClassNameOrMethodName() {
 Profile.start(null, null);
 }

 @Test
 public void testEndWithNullClassNameOrMethodName() {
 Profile.end(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBeginWaitWithNullClassNameOrMethodName() {
 Profile.beginWait(null, null);
 fail("Should throw an IllegalArgumentException as the className or methodName are null.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndWaitWithNullClassNameOrMethodName() {
 Profile.endWait(null, null);
 fail("Should throw an IllegalArgumentException as the className or methodName are null.");
 }

 @Test
 public void testUnwindWithNullClassNameOrMethodName() {
 Profile.unwind(null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnwindWithNullException() {
 Profile.unwind(null, null, null);
 fail("Should throw an IllegalArgumentException as the exception is null.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAllocWithNullClassName() {
 Profile.alloc(null);
 fail("Should throw an IllegalArgumentException as the className is null.");
 }

 @Test
 public void testFindFrameWithValidParameters() {
 Long threadId = 1L;
 String className = "TestClass";
 String methodName = "<init>";
 Frame target = findFrame(threadId, className, methodName);
 assertNotNull(target);
 }

 @Test
 public void testFindFrameWithInvalidClassName() {
 Long threadId = 1L;
 String className = "TestClassX";
 String methodName = "<init>";
 Frame target = findFrame(threadId, className, methodName);
 assertNull(target);
 }

 @Test
 public void testFindFrameWithInvalidMethodName() {
 Long threadId = 1L;
 String className = "TestClass";
 String methodName = "testMethod";
 Frame target = findFrame(threadId, className, methodName);
 assertNull(target);
 }

 @Test
 public void testFindFrameWithInvalidThreadId() {
 Long threadId = 2L;
 String className = "TestClass";
 String methodName = "<init>";
 Frame target = findFrame(threadId, className, methodName);
 assertNull(target);
 }

 private Frame findFrame(Long threadId, String className, String methodName) {
 return underTest.findFrame(threadId, className, methodName);
 }
}
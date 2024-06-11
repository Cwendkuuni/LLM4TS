package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import com.mentorgen.tools.profile.*;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.*;
import java.util.concurrent.*;

public class ProfileTest {

 private static Profile underTest;
 private static long threadId1, threadId2;

 @BeforeClass
 public static void setUpClass() {
 Profile.initProfiler();
 underTest = new Profile();
 threadId1 = Thread.currentThread().getId();
 }

 @Test
 public void testInit() {
 // Check that init methods are called and initializes the necessary data structures
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._allocList);
 assertNull(Profile._controller);
 assertNull(Profile._controllerThread);
 }

 @Test
 public void testInitTwice() {
 // Check that init method can be called multiple times without throwing an exception
 Profile.initProfiler();
 Profile.init();
 }

 @Test(expected = RuntimeException.class)
 public void testInitWithoutShutdownHook() {
 // Check that initProfiler method throws a RuntimeException if no shutdown hook is registered
 Runtime.getRuntime().addShutdownHook(null);
 Profile.initProfiler();
 }

 @Test
 public void testClear() {
 // Check that clear method resets the data structures to their initial state
 Profile.clear();
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._allocList);
 assertNull(Profile._controller);
 assertNull(Profile._controllerThread);
 }

 @Test
 public void testStart() {
 // Check that start method starts the profiler
 underTest.start();
 assertTrue(Controller._profile);
 }

 @Test(expected = RuntimeException.class)
 public void testStartTwice() {
 // Check that start method throws a RuntimeException if already started
 underTest.start();
 underTest.start();
 }

 @Test
 public void testSetFileName() {
 // Check that setFileName method sets the profiler file name
 Profile.setFileName("test.prof");
 assertEquals("test.prof", Controller._fileName);
 }

 @Test
 public void testShutdown() {
 // Check that shutdown method shuts down the profiler and saves the data to a file
 underTest.start();
 Profile.shutdown();
 assertFalse(Controller._profile);
 }

 @Test
 public void testInteractions() {
 // Check that interactions method returns the correct frames for a given thread
 threadId2 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(" complicate.test.Package", "TestClass.testMethod");
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.end(" complicate.test.Package", "TestClass.testMethod");
 }
 }).start().getId();
 List<Frame> interactions = Profile.interactions(threadId2);
 Frame expectedFrame = new Frame(null, new Method(" complicate.test.Package", "TestClass.testMethod"), threadId2);
 assertTrue(interactions.contains(expectedFrame));
 }

 @Test
 public void testThreads() {
 // Check that threads method returns the correct threads
 List<Long> threads = Profile.threads();
 assertTrue(threads.contains(threadId1));
 assertTrue(threads.contains(threadId2));
 }

 @Test
 public void testFrameList() {
 // Check that frameList method returns the correct frames
 List<Frame> frameList = Profile.frameList();
 Frame frame1 = new Frame(null, new Method(" complicate.test.Package", "TestClass.testMethod"), threadId1);
 Frame frame2 = new Frame(null, new Method(" complicate.test.Package", "TestClass.testMethod"), threadId2);
 assertTrue(frameList.contains(frame1));
 assertTrue(frameList.contains(frame2));
 }

 @Test
 public void testAllocations() {
 // Check that allocations method returns the correct class allocations
 Profile.alloc("com.mentorgen.tools.profile.runtime.Profile");
 Profile.alloc("java.lang.String");
 List<ClassAllocation> allocations = Profile.allocations();
 ClassAllocation expectedAllocation1 = new ClassAllocation("com.mentorgen.tools.profile.runtime.Profile");
 ClassAllocation expectedAllocation2 = new ClassAllocation("java.lang.String");
 assertTrue(allocations.contains(expectedAllocation1));
 assertTrue(allocations.contains(expectedAllocation2));
 }

 @Test
 public void testGetThreadTotalTime() {
 // Check that getThreadTotalTime method returns the correct thread total time
 underTest.start();
 long startTime = System.nanoTime();
 Profile.start("complicate.test.Package", "TestClass.testMethod");
 Profile.end("complicate.test.Package", "TestClass.testMethod");
 long threadTotalTime = Profile.getThreadTotalTime(threadId1);
 long expectedTotalTime = System.nanoTime() - startTime;
 assertTrue(Math.abs(threadTotalTime - expectedTotalTime) < 1000L);
 }

 @Test
 public void testSortFrameList() {
 // Check that sortFrameList method sorts the frames in the correct order
 List<Frame> frameList = Profile.frameList();
 Collections.sort(frameList, new Comparator<Frame>() {
 @Override
 public int compare(Frame f1, Frame f2) {
 return (int) (f1.getBeginTime() - f2.getBeginTime());
 }
 });
 Frame expectedFrame1 = new Frame(null, new Method("complicate.test.Package", "TestClass.testMethod"), threadId1);
 Frame expectedFrame2 = new Frame(null, new Method("complicate.test.Package", "TestClass.testMethod"), threadId2);
 assertEquals(expectedFrame1, frameList.get(0));
 assertEquals(expectedFrame2, frameList.get(1));
 }

 @Test
 public void testStartMethodWithClassNameAndMethodName() {
 // Check that start method correctly records the beginning of a method execution
 underTest.start();
 Profile.start("complicate.test.Package", "TestClass.testMethod");
 Frame expectedFrame = new Frame(null, new Method("complicate.test.Package", "TestClass.testMethod"), threadId1);
 assertTrue(Profile._frameList.contains(expectedFrame));
 }

 @Test
 public void testEndMethodWithClassNameAndMethodName() {
 // Check that end method correctly records the ending of a method execution and its overhead
 underTest.start();
 Profile.start("complicate.test.Package", "TestClass.testMethod");
 long startTime = System.nanoTime();
 Profile.end("complicate.test.Package", "TestClass.testMethod");
 Frame expectedFrame = new Frame(null, new Method("complicate.test.Package", "TestClass.testMethod"), threadId1);
 assertEquals(expectedFrame.getOverhead(), Math.abs(System.nanoTime() - startTime));
 }

 @Test
 public void testBeginWaitAndEndWaitMethods() {
 // Check that beginWait and endWait methods correctly record the waiting time for a method
 underTest.start();
 Profile.beginWait("complicate.test.Package", "TestClass.testMethod");
 long startTime = System.nanoTime();
 Profile.endWait("complicate.test.Package", "TestClass.testMethod");
 Frame expectedFrame = new Frame(null, new Method("complicate.test.Package", "TestClass.testMethod"), threadId1);
 assertEquals(expectedFrame.getWaitTime(), Math.abs(System.nanoTime() - startTime));
 }

 @Test
 public void testUnwindMethods() {
 // Check that unwind methods correctly handle exceptions
 Profile.setDebugException(true);
 underTest.start();
 Profile.start("complicate.test.Package", "TestClass.testException");
 try {
 throw new RuntimeException("Test exception");
 } catch (RuntimeException e) {
 Profile.unwind("complicate.test.Package", "TestClass.testException", e.getClass().getName());
 }
 Frame expectedFrame = new Frame(null, new Method("complicate.test.Package", "TestClass.testException"), threadId1);
 assertTrue(Profile._frameList.contains(expectedFrame));
 }

 @Test
 public void testAllocMethod() {
 // Check that alloc method correctly records class allocations
 underTest.start();
 Profile.alloc("com.mentorgen.tools.profile.runtime.Profile");
 ClassAllocation expectedAllocation = new ClassAllocation("com.mentorgen.tools.profile.runtime.Profile");
 assertEquals(expectedAllocation.getAllocCount(), Profile._allocList.get("com.mentorgen.tools.profile.runtime.Profile").getAllocCount());
 }
}
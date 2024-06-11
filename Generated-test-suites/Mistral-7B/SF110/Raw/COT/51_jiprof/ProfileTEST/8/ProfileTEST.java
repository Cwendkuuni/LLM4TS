package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import com.mentorgen.tools.profile.*;
import java.util.*;
import org.junit.Test;

public class ProfileTest {

 private static Profile instance;

 @BeforeClass
 public static void setUpClass() {
 Profile.init();
 instance = new Profile();
 }

 @AfterClass
 public static void tearDownClass() {
 Profile.clear();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controllerThread != null);
 }

 @Test
 public void testInit() {
 Profile.init();
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertTrue(Profile._threadActiveFrame.isEmpty());
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
 Profile.start();
 Profile.stop();
 assertTrue(Profile._controller.isStopped());
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("test.out");
 assertEquals("test.out", Profile._controller.getFileName());
 }

 @Test(expected = IllegalStateException.class)
 public void testShutdownWhenProfilerNotRunning() {
 Profile.shutdown();
 fail("Shutdown should not be called when profiler is not running");
 }

 @Test
 public void testThreads() {
 Profile.initProfiler();
 List<Long> threads = Profile.threads();
 assertFalse(threads.isEmpty());
 }

 @Test
 public void testInteractions() {
 Profile.initProfiler();
 long threadId = Thread.currentThread().getId();
 Frame frame = new Frame(null, new Method("TestClass", "testMethod"), threadId);
 Profile._threadDictionary.add(threadId, frame);
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(1, interactions.size());
 assertEquals(frame, interactions.get(0));
 }

 @Test
 public void testFrameList() {
 Profile.initProfiler();
 Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), 1);
 Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), 2);
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 Iterable<Frame> frameList = Profile.frameList();
 assertEquals(2, frameList.spliterator().getExactSizeIfKnown());
 }

 @Test
 public void testAllocations() {
 Profile.initProfiler();
 Profile.alloc("TestClass");
 Iterable<ClassAllocation> allocations = Profile.allocations();
 ClassAllocation ca = allocations.iterator().next();
 assertEquals("TestClass", ca.getClassName());
 assertEquals(1, ca.getAllocCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 Profile.initProfiler();
 long threadId = Thread.currentThread().getId();
 Frame frame = new Frame(null, new Method("TestClass", "testMethod"), threadId);
 Profile._threadDictionary.add(threadId, frame);
 Profile.start(new Method("TestClass", "testMethod"), 1);
 Profile.end(new Method("TestClass", "testMethod"), 1);
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(0, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 Profile.initProfiler();
 Frame frame1 = new Frame(null, new Method("TestClass1", "testMethod1"), 1);
 Frame frame2 = new Frame(null, new Method("TestClass2", "testMethod2"), 2);
 Frame frame3 = new Frame(null, new Method("TestClass3", "testMethod3"), 3);
 Profile._frameList.add(frame3);
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 Comparator<Frame> comp = (f1, f2) -> f1.getBeginTime() - f2.getBeginTime();
 Profile.sortFrameList(comp);
 Frame sortedFrame1 = Profile.frameList().iterator().next();
 Frame sortedFrame2 = Profile.frameList().iterator().next();
 Frame sortedFrame3 = Profile.frameList().iterator().next();
 assertEquals(sortedFrame1.getBeginTime(), frame1.getBeginTime());
 assertEquals(sortedFrame2.getBeginTime(), frame2.getBeginTime());
 assertEquals(sortedFrame3.getBeginTime(), frame3.getBeginTime());
 }

 @Test
 public void testStartEnd() {
 Profile.initProfiler();
 String className = "TestClass";
 String methodName = "testMethod";
 long threadId = Thread.currentThread().getId();
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 Frame frame = findFrame(threadId, className, methodName);
 assertNotNull(frame);
 }

 @Test
 public void testBeginWaitEndWait() {
 Profile.initProfiler();
 String className = "TestClass";
 String methodName = "testMethod";
 long threadId = Thread.currentThread().getId();
 Profile.beginWait(className, methodName);
 Profile.endWait(className, methodName);
 Frame frame = findFrame(threadId, className, methodName);
 assertNotNull(frame);
 }

 @Test
 public void testUnwind() {
 Profile.initProfiler();
 String className = "TestClass";
 String methodName = "testMethod";
 String exception = "TestException";
 long threadId = Thread.currentThread().getId();
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 Profile.unwind(className, methodName, exception);
 Frame frame = findFrame(threadId, className, methodName);
 assertNotNull(frame);
 }

 @Test
 public void testAlloc() {
 Profile.initProfiler();
 String className = "TestClass";
 Profile.alloc(className);
 ClassAllocation ca = Profile._allocList.get(className);
 assertEquals(1, ca.getAllocCount());
 }

 @Test
 public void testRun() {
 Profile.initProfiler();
 Profile._controllerThread.start();
 try {
 Thread.sleep(1000);
 }
 catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile._controllerThread.interrupt();
 Profile._controller.close();
 }

 private Frame findFrame(long threadId, String className, String methodName) {
 Frame target = null;
 Frame current = Profile._threadActiveFrame.get(threadId);
 while (current != null && (!current.getClassName().equals(className) || !current.getMethodName().equals(methodName))) {
 current = current.getParent();
 }
 return current;
 }
}
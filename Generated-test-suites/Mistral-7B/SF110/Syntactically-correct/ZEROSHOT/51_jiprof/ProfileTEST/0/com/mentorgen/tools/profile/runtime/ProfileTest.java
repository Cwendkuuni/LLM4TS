package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfileTest {

 private static Profile profile;

 @BeforeClass
 public static void setupClass() {
 Profile.init();
 profile = new Profile();
 }

 @Test
 public void testInit() {
 assertNull(Profile._controller);
 assertNull(Profile._controllerThread);
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 }

 @Test
 public void testClear() {
 Profile.init();
 Profile.shutdown();
 Profile.init();
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 }

 @Test
 public void testInitProfiler() {
 assertFalse(Profile.Controller._profile);
 Profile.initProfiler();
 assertTrue(Profile.Controller._profile);
 }

 @Test
 public void testThreads() {
 Profile.init();
 Profile.start();
 List<Long> threads = Profile.threads();
 assertEquals(1, threads.size());
 }

 @Test
 public void testStart() {
 Profile.init();
 Profile.start();
 assertTrue(Profile.Controller._profile);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 Profile.init();
 Profile.start();
 Profile.start();
 }

 @Test
 public void testStop() {
 Profile.init();
 Profile.start();
 Profile.stop();
 assertFalse(Profile.Controller._profile);
 }

 @Test
 public void testSetFileName() {
 Profile.init();
 Profile.setFileName("test.log");
 }

 @Test
 public void testShutdown() {
 Profile.init();
 Profile.start();
 Profile.shutdown();
 assertFalse(Profile.Controller._profile);
 }

 @Test
 public void testInteractions() {
 Profile.init();
 Profile.start();
 long threadId = Thread.currentThread().getId();
 Frame frame1 = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Frame frame2 = new Frame(frame1, new Method("TestClass", "method1"), threadId);
 Frame frame3 = new Frame(frame2, new Method("TestClass", "method2"), threadId);
 Profile._threadDictionary.add(threadId, frame3);
 Profile._frameList.add(frame3);
 Profile._frameList.add(frame2);
 Profile._frameList.add(frame1);
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(3, interactions.size());
 }

 @Test
 public void testFrameList() {
 Profile.init();
 Profile.start();
 long threadId = Thread.currentThread().getId();
 Frame frame1 = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Frame frame2 = new Frame(frame1, new Method("TestClass", "method1"), threadId);
 Frame frame3 = new Frame(frame2, new Method("TestClass", "method2"), threadId);
 Profile._threadDictionary.add(threadId, frame3);
 Profile._frameList.add(frame3);
 Profile._frameList.add(frame2);
 Profile._frameList.add(frame1);
 Iterable<Frame> frameList = Profile.frameList();
 assertEquals(3, frameList.spliterator().getExactSizeIfKnown());
 }

 @Test
 public void testAllocations() {
 Profile.init();
 Profile.start();
 Profile.alloc("TestClass");
 Iterable<ClassAllocation> allocations = Profile.allocations();
 assertEquals(1, allocations.spliterator().getExactSizeIfKnown());
 }

 @Test
 public void testGetThreadTotalTime() {
 Profile.init();
 Profile.start();
 long threadId = Thread.currentThread().getId();
 Frame frame1 = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Frame frame2 = new Frame(frame1, new Method("TestClass", "method1"), threadId);
 Frame frame3 = new Frame(frame2, new Method("TestClass", "method2"), threadId);
 long time1 = System.nanoTime();
 Profile._threadDictionary.add(threadId, frame3);
 Profile._frameList.add(frame3);
 Profile._frameList.add(frame2);
 Profile._frameList.add(frame1);
 long time2 = System.nanoTime();
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertTrue(time2 > time1);
 assertTrue(threadTotalTime > time1);
 }

 @Test
 public void testSortFrameList() {
 Profile.init();
 Profile.start();
 long threadId = Thread.currentThread().getId();
 Frame frame1 = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Frame frame2 = new Frame(null, new Method("TestClass2", "<init>"), threadId);
 Frame frame3 = new Frame(frame1, new Method("TestClass", "method1"), threadId);
 Frame frame4 = new Frame(frame2, new Method("TestClass2", "method1"), threadId);
 Frame frame5 = new Frame(frame3, new Method("TestClass", "method2"), threadId);
 Frame frame6 = new Frame(frame4, new Method("TestClass2", "method2"), threadId);
 Profile._threadDictionary.add(threadId, frame6);
 Profile._frameList.add(frame6);
 Profile._frameList.add(frame5);
 Profile._frameList.add(frame4);
 Profile._frameList.add(frame3);
 Profile._frameList.add(frame2);
 Profile._frameList.add(frame1);
 Comparator<Frame> comp = new Comparator<Frame>() {
 @Override
 public int compare(Frame o1, Frame o2) {
 long o1Time = o1.getBeginTime();
 long o2Time = o2.getBeginTime();
 return (int) (o1Time - o2Time);
 }
 };
 Profile.sortFrameList(comp);
 Frame firstFrame = Profile.frameList().iterator().next();
 assertEquals("TestClass2", firstFrame.getClassName());
 }

 @Test
 public void testStartEnd() {
 Profile.init();
 Profile.start();
 String className = "TestClass";
 String methodName = "<init>";
 String className2 = "TestClass2";
 String methodName2 = "method1";
 long threadId = Thread.currentThread().getId();
 Profile.start(className, methodName);
 long startTime = System.nanoTime();
 Profile.end(className, methodName);
 Frame frame1 = new Frame(null, new Method(className, methodName), threadId);
 frame1.setBeginTime(startTime);
 Frame frame2 = new Frame(frame1, new Method(className2, methodName2), threadId);
 Profile._threadDictionary.add(threadId, frame2);
 Profile._frameList.add(frame2);
 Profile._frameList.add(frame1);
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(2, interactions.size());
 }

 @Test
 public void testBeginWaitEndWait() {
 Profile.init();
 Profile.start();
 String className = "TestClass";
 String methodName = "<init>";
 long threadId = Thread.currentThread().getId();
 Profile.beginWait(className, methodName);
 long startTime = System.nanoTime();
 Profile.endWait(className, methodName);
 Frame frame1 = new Frame(null, new Method(className, methodName), threadId);
 frame1.beginWait(startTime);
 frame1.endWait(startTime);
 Frame frame2 = new Frame(frame1, new Method("TestClass2", "method1"), threadId);
 Profile._threadDictionary.add(threadId, frame2);
 Profile._frameList.add(frame2);
 Profile._frameList.add(frame1);
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(2, interactions.size());
 }

 @Test
 public void testUnwind() {
 Profile.init();
 Profile.start();
 String className = "TestClass";
 String methodName = "<init>";
 String exception = "TestException";
 long threadId = Thread.currentThread().getId();
 try {
 throw new TestException();
 } catch (TestException e) {
 Profile.unwind(className, methodName, exception);
 }
 Frame frame1 = new Frame(null, new Method(className, methodName), threadId);
 Profile._threadDictionary.add(threadId, frame1);
 Profile._frameList.add(frame1);
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(1, interactions.size());
 }

 @Test
 public void testAlloc() {
 Profile.init();
 Profile.start();
 String className = "TestClass";
 Profile.alloc(className);
 ClassAllocation ca = Profile._allocList.get(className);
 assertEquals(1, ca.getAllocCount());
 }
}
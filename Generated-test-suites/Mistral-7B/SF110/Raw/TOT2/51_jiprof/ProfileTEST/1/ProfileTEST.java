package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.output.ProfilePrinter;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

 private static final String TEST_CLASS_NAME = "TestClass";
 private static final String TEST_METHOD_NAME = "testMethod";
 private static final String EXCEPTION_CLASS_NAME = "ExceptionClass";
 private static final String EXCEPTION_METHOD_NAME = "exceptionMethod";

 @SuppressWarnings("unchecked")
 @Test
 public void testInitProfiler() {
  Profile.initProfiler();
  assertEquals(1, Profile._threadDictionary.size());
  assertTrue(Profile._controller != null);
  if (Profile._controllerThread != null) {
   assertTrue(Profile._controllerThread.isAlive());
  }
 }

 @Test
 public void testInit() {
  Profile.init();
  assertEquals(1001, Profile._threadDictionary.size());
  assertEquals(2003, Profile._methodDictionary.size());
  assertEquals(1, Profile._frameList.size());
  assertEquals(1, Profile._allocList.size());
  assertEquals(0, Profile._controllerThread.size());
 }

 @Test
 public void testClear() {
  Profile.init();
  Profile.start();
  Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
  Profile.clear();
  assertEquals(0, Profile._threadDictionary.size());
  assertEquals(0, Profile._methodDictionary.size());
  assertEquals(0, Profile._frameList.size());
  assertEquals(0, Profile._allocList.size());
 }

 @Test
 public void testThreads() {
  Profile.init();
  Profile.start();
  Thread t = new Thread(() -> { Thread.yield(); });
  t.start();
  List<Long> threads = Profile.threads();
  assertEquals(2, threads.size());
  assertTrue(threads.contains(Thread.currentThread().getId()));
  assertTrue(threads.contains(t.getId()));
 }

 @Test
 public void testInteractions() {
  Profile.init();
  Profile.start();
  Thread t = new Thread(() -> {
   try {
    Thread.sleep(10);
   } catch (InterruptedException e) {}
  });
  t.start();
  List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
  Frame interaction = interactions.get(0);
  assertEquals(TEST_CLASS_NAME, interaction.getClassName());
  assertEquals(TEST_METHOD_NAME, interaction.getMethodName());
  assertTrue(interaction.getParent() == null);
  interactions = Profile.interactions(t.getId());
  Frame interaction2 = interactions.get(0);
  assertEquals(Thread.class.getName(), interaction2.getClassName());
  assertEquals("run", interaction2.getMethodName());
  assertNotNull(interaction2.getParent());
 }

 @Test
 public void testFrameList() {
  Profile.init();
  Profile.start();
  Thread t = new Thread(() -> {
   Thread.yield();
   Thread.yield();
  });
  t.start();
  List<Frame> frameList = Profile.frameList();
  Frame frame1 = frameList.get(0);
  assertEquals(TEST_CLASS_NAME, frame1.getClassName());
  assertEquals(TEST_METHOD_NAME, frame1.getMethodName());
  Frame frame2 = frameList.get(1);
  assertEquals(Thread.class.getName(), frame2.getClassName());
  assertEquals("run", frame2.getMethodName());
  Frame frame3 = frameList.get(2);
  assertEquals(Thread.class.getName(), frame3.getClassName());
  assertEquals("run", frame3.getMethodName());
 }

 @Test
 public void testAllocations() {
  Profile.init();
  Profile.start();
  new TestClass();
  List<ClassAllocation> allocations = Profile.allocations();
  ClassAllocation allocation = (ClassAllocation) allocations.get(0);
  assertEquals(TEST_CLASS_NAME, allocation.getClassName());
  assertEquals(1, allocation.getAllocCount());
 }

 @Test
 public void testGetThreadTotalTime() {
  Profile.init();
  Thread t = new Thread(() -> {
   try {
    Thread.sleep(50);
   } catch (InterruptedException e) {}
  });
  t.start();
  Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
  Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
  Profile.start();
  t.join();
  long thread1TotalTime = Profile.getThreadTotalTime(Thread.currentThread().getId());
  long thread2TotalTime = Profile.getThreadTotalTime(t.getId());
  assertTrue(thread1TotalTime > 0);
  assertTrue(thread2TotalTime > 0);
  assertTrue(thread1TotalTime > thread2TotalTime);
 }

 @Test
 public void testSortFrameList() {
  Profile.init();
  Profile.start();
  Thread t1 = new Thread(() -> {
   Thread.yield();
   Thread.yield();
  });
  t1.start();
  Thread t2 = new Thread(() -> {
   Thread.yield();
  });
  t2.start();
  Profile.sortFrameList((o1, o2) -> o1.getBeginTime() - o2.getBeginTime());
  List<Frame> frameList = Profile.frameList();
  assertTrue(frameList.get(0).getBeginTime() < frameList.get(1).getBeginTime());
 }

 @Test
 public void testStart() {
  Profile.init();
  Profile.start();
  Method method = new Method(TEST_CLASS_NAME, TEST_METHOD_NAME);
  Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
  Frame frame = Profile.findFrame(Thread.currentThread().getId(), TEST_CLASS_NAME, TEST_METHOD_NAME);
  assertEquals(method.toString(), frame.getMethodSignature());
 }

 @Test
 public void testStartException() {
  Profile.init();
  Profile.start();
  try {
   throw new RuntimeException("Test Exception");
  } catch (RuntimeException e) {
   Profile.unwind(EXCEPTION_CLASS_NAME, EXCEPTION_METHOD_NAME, e.getClass().getName());
  }
  Frame frame = Profile.findFrame(Thread.currentThread().getId(), EXCEPTION_CLASS_NAME, EXCEPTION_METHOD_NAME);
  assertEquals(EXCEPTION_CLASS_NAME, frame.getClassName());
  assertEquals(EXCEPTION_METHOD_NAME, frame.getMethodName());
 }

 @Test
 public void testBeginWait() {
  Profile.init();
  Profile.start();
  try {
   Thread.sleep(100);
  } catch (InterruptedException e) {}
  Profile.beginWait(TEST_CLASS_NAME, TEST_METHOD_NAME);
  Frame frame = Profile.findFrame(Thread.currentThread().getId(), TEST_CLASS_NAME, TEST_METHOD_NAME);
  assertTrue(frame.isWaiting());
  Profile.endWait(TEST_CLASS_NAME, TEST_METHOD_NAME);
  frame = Profile.findFrame(Thread.currentThread().getId(), TEST_CLASS_NAME, TEST_METHOD_NAME);
  assertFalse(frame.isWaiting());
 }

 @Test
 public void testAllocation() {
  Profile.init();
  Profile.start();
  new TestClass();
  ClassAllocation allocation = Profile.findAllocation(TEST_CLASS_NAME);
  assertEquals(TEST_CLASS_NAME, allocation.getClassName());
  assertEquals(2, allocation.getAllocCount());
 }

 @Test
 public void testShutdown() {
  Profile.init();
  Profile.start();
  Profile.shutdown();
  List<Frame> frameList = Profile.frameList();
  assertEquals(0, frameList.size());
  assertEquals(0, Profile._threadDictionary.size());
 }
}
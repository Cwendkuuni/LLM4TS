package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import com.mentorgen.tools.profile.*;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.*;
import java.io.*;

public class ProfileTest {

 private static Profile profile;
 private static Controller controller;

 @BeforeClass
 public static void setupClass() {
 Profile.init();
 profile = new Profile();
 controller = new Controller();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 if (Profile._controllerThread != null) {
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertEquals(0, Profile.threads().size());
 assertEquals(0, Profile.frameList().size());
 }

 @Test
 public void testStart() {
 Profile.start();
 assertTrue(controller.isRunning());
 }

 @Test
 public void testStop() {
 Profile.stop();
 assertFalse(controller.isRunning());
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("test.prof");
 assertEquals("test.prof", controller.getFileName());
 }

 @Test(expected = IllegalStateException.class)
 public void testShutdownWhenNotRunning() {
 Profile.shutdown();
 }

 @Test
 public void testIterableThreads() {
 List<Long> threads = new ArrayList<>(Profile.threads());
 assertEquals(0, threads.size());
 Profile.initProfiler();
 threads = new ArrayList<>(Profile.threads());
 assertEquals(1, threads.size());
 }

 @Test
 public void testIterableInteractions() {
 Long threadId = 0L;
 Frame frame = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Frame interaction = new Frame(frame, new Method("TestClass", "testMethod"), threadId);
 Profile.init();
 Profile.start();
 Profile.threadActiveFrame().put(threadId, frame);
 Profile.threadActiveFrame().put(threadId, interaction);
 List<Frame> interactions = new ArrayList<>(Profile.interactions(threadId));
 assertEquals(1, interactions.size());
 }

 @Test
 public void testFrameList() {
 Frame frame1 = new Frame(null, new Method("TestClass1", "method1"), 1L);
 Frame frame2 = new Frame(null, new Method("TestClass2", "method2"), 2L);
 Profile.init();
 Profile.start();
 Profile.threadActiveFrame().put(1L, frame1);
 Profile.threadActiveFrame().put(2L, frame2);
 List<Frame> frameList = new ArrayList<>(Profile.frameList());
 assertEquals(2, frameList.size());
 }

 @Test
 public void testAllocations() {
 Set<String> allocations = new HashSet<>();
 Profile.alloc("TestClass1");
 Profile.alloc("TestClass1");
 Profile.alloc("TestClass2");
 assertEquals(2, Profile.allocations().size());
 }

 @Test
 public void testGetThreadTotalTime() {
 Long threadId = 0L;
 Frame frame = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Frame interaction = new Frame(frame, new Method("TestClass", "testMethod"), threadId);
 Profile.init();
 Profile.start();
 Profile.threadActiveFrame().put(threadId, frame);
 Profile.threadActiveFrame().put(threadId, interaction);
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertTrue(threadTotalTime >= 0);
 }

 @Test
 public void testSortFrameList() {
 Frame frame1 = new Frame(null, new Method("TestClass1", "method1"), 1L);
 Frame frame2 = new Frame(null, new Method("TestClass2", "method2"), 2L);
 Frame frame3 = new Frame(null, new Method("TestClass1", "method1"), 1L);
 Profile.init();
 Profile.start();
 Profile.threadActiveFrame().put(1L, frame1);
 Profile.threadActiveFrame().put(2L, frame2);
 Profile.threadActiveFrame().put(1L, frame3);
 Collections.sort(new ArrayList<>(Profile.frameList()), new Comparator<Frame>() {
 @Override
 public int compare(Frame o1, Frame o2) {
 return Long.compare(o1.getEndTime(), o2.getEndTime());
 }
 });
 Frame sortedFrame1 = Profile.frameList().get(0);
 Frame sortedFrame2 = Profile.frameList().get(1);
 assertEquals(sortedFrame1.getEndTime(), frame3.getEndTime());
 assertEquals(sortedFrame2.getEndTime(), frame2.getEndTime());
 }

 @Test
 public void testStartEnd() {
 Long threadId = 0L;
 String className = "TestClass";
 String methodName = "testMethod";
 Frame parent = new Frame(null, new Method(className, "<init>"), threadId);
 Frame frame = new Frame(parent, new Method(className, methodName), threadId);
 Frame child = new Frame(frame, new Method(className, "testMethod2"), threadId);
 Profile.init();
 Profile.start();
 Profile.threadActiveFrame().put(threadId, parent);
 Profile.threadActiveFrame().put(threadId, frame);
 Profile.threadActiveFrame().put(threadId, child);
 Profile.end(className, methodName);
 Profile.end(className, "testMethod2");
 assertEquals(0, Profile.frameList().size());
 }

 @Test
 public void testBeginWaitEndWait() {
 Long threadId = 0L;
 String className = "TestClass";
 String methodName = "testMethod";
 Frame parent = new Frame(null, new Method(className, "<init>"), threadId);
 Frame frame = new Frame(parent, new Method(className, methodName), threadId);
 Frame child = new Frame(frame, new Method(className, "testMethod2"), threadId);
 Profile.init();
 Profile.start();
 Profile.threadActiveFrame().put(threadId, parent);
 Profile.threadActiveFrame().put(threadId, frame);
 Profile.threadActiveFrame().put(threadId, child);
 Profile.beginWait(className, methodName);
 Profile.endWait(className, methodName);
 assertEquals(0, Profile.frameList().size());
 }

 @Test
 public void testUnwind() {
 Long threadId = 0L;
 String className = "TestClass";
 String methodName = "testMethod";
 String exception = "TestException";
 Frame parent = new Frame(null, new Method(className, "<init>"), threadId);
 Frame frame = new Frame(parent, new Method(className, methodName), threadId);
 Frame child = new Frame(frame, new Method(className, "testMethod2"), threadId);
 Frame exceptionFrame = new Frame(child, new Method(className, "handleException"), threadId);
 Frame root = new Frame(null, new Method(className, "<init>"), threadId);
 Profile.init();
 Profile.start();
 Profile.threadActiveFrame().put(threadId, parent);
 Profile.threadActiveFrame().put(threadId, frame);
 Profile.threadActiveFrame().put(threadId, child);
 Profile.threadActiveFrame().put(threadId, exceptionFrame);
 Profile.unwind(className, methodName, exception);
 assertEquals(root, Profile.threadActiveFrame().get(threadId));
 }

 @Test
 public void testAlloc() {
 String className = "TestClass";
 int allocationCount = 0;
 for (int i = 0; i < 100; i++) {
 Profile.alloc(className);
 allocationCount++;
 }
 assertEquals(100, Profile.allocList().size());
 }

 @Test
 public void testRun() throws IOException {
 Profile.shutdown();
 profile.run();
 }
}
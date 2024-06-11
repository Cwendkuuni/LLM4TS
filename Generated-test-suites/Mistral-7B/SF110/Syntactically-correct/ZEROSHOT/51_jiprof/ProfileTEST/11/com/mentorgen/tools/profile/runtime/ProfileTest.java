package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfileTest {

 private static Profile profile;
 private static Controller controller;

 @BeforeClass
 public static void setup() {
 Profile.init();
 profile = new Profile();
 controller = new Controller();
 }

 @Test
 public void testInit() {
 assertTrue(Profile._threadDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
 assertTrue(Profile._methodDictionary.isEmpty());
 assertTrue(Profile._threadActiveFrame.isEmpty());
 assertTrue(Profile._allocList.isEmpty());
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertNotNull(Profile._controllerThread);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertTrue(Profile._threadDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
 assertTrue(Profile._methodDictionary.isEmpty());
 assertTrue(Profile._threadActiveFrame.isEmpty());
 assertTrue(Profile._allocList.isEmpty());
 }

 @Test
 public void testStart() {
 Profile.start();
 assertTrue(controller.isRunning());
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 Profile.start();
 Profile.start();
 fail("Expected an IllegalStateException");
 }

 @Test
 public void testStop() {
 Profile.stop();
 assertFalse(controller.isRunning());
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("output.txt");
 assertEquals("output.txt", controller.getFileName());
 }

 @Test
 public void testThreads() {
 Long threadId = Thread.currentThread().getId();
 Profile.start("ProfileTest", "<init>");
 List<Long> threads = Profile.threads();
 assertEquals(1, threads.size());
 assertEquals(threadId, threads.get(0));
 }

 @Test
 public void testInteractions() {
 Long threadId = Thread.currentThread().getId();
 String className = "ProfileTest";
 String methodName = "<init>";
 Profile.start(className, methodName);
 Frame frame = findFrame(threadId, className, methodName);
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(1, interactions.size());
 assertEquals(frame, interactions.get(0));
 }

 @Test
 public void testFrameList() {
 Frame frame1 = new Frame(null, new Method("ProfileTest", "<init>"), Thread.currentThread().getId());
 Frame frame2 = new Frame(frame1, new Method("ProfileTest", "testInteractions"), Thread.currentThread().getId());
 Profile.start("ProfileTest", "<init>");
 Profile.start("ProfileTest", "testInteractions");
 List<Frame> frameList = Profile.frameList();
 assertEquals(2, frameList.size());
 assertTrue(frameList.contains(frame1));
 assertTrue(frameList.contains(frame2));
 }

 @Test
 public void testAllocations() {
 String className = "ProfileTest";
 Profile.alloc(className);
 Profile.alloc(className);
 List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
 assertEquals(1, allocations.size());
 assertEquals(2, allocations.get(0).getAllocationCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 Long threadId = Thread.currentThread().getId();
 Profile.start("ProfileTest", "<init>");
 Frame frame = findFrame(threadId, "ProfileTest", "<init>");
 long startTime = frame.getBeginTime();
 long endTime = System.nanoTime();
 Profile.stop();
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(endTime - startTime, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 Frame frame1 = new Frame(null, new Method("ProfileTest", "<init>"), Thread.currentThread().getId());
 Frame frame2 = new Frame(null, new Method("ProfileTest", "testInteractions"), Thread.currentThread().getId());
 Frame frame3 = new Frame(frame1, new Method("ProfileTest", "testFrameList"), Thread.currentThread().getId());
 Frame frame4 = new Frame(frame2, new Method("ProfileTest", "testSortFrameList"), Thread.currentThread().getId());
 Profile.start("ProfileTest", "<init>");
 Profile.start("ProfileTest", "testInteractions");
 Profile.start("ProfileTest", "testFrameList");
 Profile.start("ProfileTest", "testSortFrameList");
 List<Frame> frameList = new ArrayList<>(Profile._frameList);
 Collections.sort(frameList, Frame.COMPARATOR);
 List<Frame> sortedFrameList = Profile.frameList();
 assertEquals(4, sortedFrameList.size());
 assertTrue(sortedFrameList.contains(frame1));
 assertTrue(sortedFrameList.contains(frame2));
 assertTrue(sortedFrameList.contains(frame3));
 assertTrue(sortedFrameList.contains(frame4));
 }

 @Test
 public void testStartEnd() {
 String className = "ProfileTest";
 String methodName = "<init>";
 long startTime = System.nanoTime();
 Profile.start(className, methodName);
 String className2 = "ProfileTest";
 String methodName2 = "testStartEnd";
 long endTime = System.nanoTime();
 Profile.end(className2, methodName2);
 Frame frame = findFrame(Thread.currentThread().getId(), className, methodName2);
 assertEquals(endTime - startTime, frame.getNetTime());
 }

 @Test
 public void testBeginWaitEndWait() {
 String className = "ProfileTest";
 String methodName = "<init>";
 long startTime = System.nanoTime();
 Profile.beginWait(className, methodName);
 String className2 = "ProfileTest";
 String methodName2 = "testBeginWaitEndWait";
 long endTime = System.nanoTime();
 Profile.endWait(className2, methodName2);
 Frame frame = findFrame(Thread.currentThread().getId(), className, methodName2);
 assertEquals(endTime - startTime, frame.getWaitTime());
 }

 @Test
 public void testUnwind() {
 String className = "ProfileTest";
 String methodName = "<init>";
 String exception = "TestException";
 try {
 throw new TestException();
 } catch (TestException e) {
 Profile.unwind(className, methodName, exception);
 }
 Frame frame = findFrame(Thread.currentThread().getId(), className, methodName);
 assertTrue(frame.isException());
 }

 private Frame findFrame(long threadId, String className, String methodName) {
 Frame frame = null;
 for (Frame f : Profile.frameList()) {
 if (f.getThreadId() == threadId && f.getClassName().equals(className) && f.getMethodName().equals(methodName)) {
 frame = f;
 break;
 }
 }
 return frame;
 }

}
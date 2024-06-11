package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfileTest {

 private static Profile profile;
 private static Map<String, Method> methodDictionary;

 @BeforeClass
 public static void setUp() {
 Profile.init();
 profile = new Profile();
 methodDictionary = new HashMap<String, Method>(2003);
 }

 @Test
 public void testInitProfiler() {
 assertEquals("Java Interactive Profiler: starting", System.err.getErrorStream().readLine());
 assertNotNull(Profile._controller);
 }

 @Test
 public void testInit() {
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 }

 @Test
 public void testClear() {
 // Clear test will initialize the Profile again, check if the init method gets called
 assertFalse(Profile._threadDictionary.isEmpty());
 Profile.clear();
 assertFalse(Profile._threadDictionary.isEmpty());
 Profile.init(); // This call should set the profile to false and clear the thread dictionary
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
 String fileName = "testProfile.txt";
 Profile.setFileName(fileName);
 assertEquals(fileName, Profile._controller.getFileName());
 }

 @Test(expected = IllegalStateException.class)
 public void testShutdownWhenNotRunning() {
 Profile.shutdown();
 }

 @Test
 public void testShutdown() {
 Profile.start();
 Profile.shutdown();
 assertFalse(Profile._controller.isRunning());
 }

 @Test
 public void testThreads() {
 long threadId = Thread.currentThread().getId();
 Profile.start("ProfileTest", "<init>");
 assertTrue(Profile.threads().contains(threadId));
 Profile.shutdown();
 }

 @Test
 public void testInteractions() {
 long threadId = Thread.currentThread().getId();
 Profile.start("ProfileTest", "<init>");
 Profile.start("ProfileTest", "testMethod");
 Profile.end("ProfileTest", "testMethod");
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(1, interactions.size());
 Frame interaction = interactions.get(0);
 assertEquals("ProfileTest", interaction.getClassName());
 assertEquals("testMethod", interaction.getMethodName());
 Profile.shutdown();
 }

 @Test
 public void testFrameList() {
 List<Frame> frameList = Profile.frameList();
 assertEquals(0, frameList.size());
 Profile.start("ProfileTest", "<init>");
 Profile.start("ProfileTest", "testMethod");
 Profile.end("ProfileTest", "testMethod");
 frameList = Profile.frameList();
 assertEquals(2, frameList.size());
 Profile.shutdown();
 }

 @Test
 public void testAllocations() {
 String className = "ProfileTest";
 Profile.alloc(className);
 Profile.alloc(className);
 List<ClassAllocation> allocations = new ArrayList<ClassAllocation>(Profile._allocList.values());
 assertEquals(1, allocations.size());
 ClassAllocation ca = allocations.get(0);
 assertEquals(2, ca.getAllocCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 long threadId = Thread.currentThread().getId();
 long time = 100L;
 Profile.start("ProfileTest", "<init>");
 Profile.start("ProfileTest", "testMethod");
 Profile.end("ProfileTest", "testMethod");
 Profile.end("ProfileTest", "<init>");
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(time * 2, threadTotalTime);
 Profile.shutdown();
 }

 @Test
 public void testSortFrameList() {
 Comparator<Frame> comp = new Comparator<Frame>() {
 @Override
 public int compare(Frame o1, Frame o2) {
 return Long.compare(o1.getBeginTime(), o2.getBeginTime());
 }
 };
 Profile.start("ProfileTest1", "<init>");
 Profile.start("ProfileTest2", "<init>");
 Profile.end("ProfileTest1", "<init>");
 Profile.end("ProfileTest2", "<init>");
 Profile.sortFrameList(comp);
 Frame firstFrame = Profile.frameList().get(0);
 Frame secondFrame = Profile.frameList().get(1);
 long firstBeginTime = firstFrame.getBeginTime();
 long secondBeginTime = secondFrame.getBeginTime();
 assertTrue(firstBeginTime < secondBeginTime);
 Profile.shutdown();
 }

 @Test
 public void testStartMethod() {
 String className = "ProfileTest";
 String methodName = "testMethod";
 long startTime = System.nanoTime();
 Profile.start(className, methodName);
 long threadId = Thread.currentThread().getId();
 Frame threadFrame = Profile.findFrame(threadId, className, methodName);
 assertTrue(threadFrame != null);
 long endTime = System.nanoTime();
 long time = endTime - startTime;
 assertEquals(methodName, threadFrame.getMethodName());
 assertEquals(className, threadFrame.getClassName());
 assertEquals(time, threadFrame.getNetTime());
 }

 @Test
 public void testEndMethod() {
 String className = "ProfileTest";
 String methodName = "testMethod";
 long startTime = System.nanoTime();
 Profile.start(className, "<init>");
 Profile.start(className, methodName);
 Frame threadFrame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
 long threadId = threadFrame.getThreadId();
 Profile.end(className, methodName);
 Frame endFrame = Profile.findFrame(threadId, className, methodName);
 long endTime = System.nanoTime();
 long time = endTime - startTime;
 assertEquals(methodName, endFrame.getMethodName());
 assertEquals(className, endFrame.getClassName());
 assertEquals(time, endFrame.getNetTime());
 assertEquals(Thread.currentThread().getId(), endFrame.getThreadId());
 }

 @Test
 public void testBeginWaitMethod() {
 String className = "ProfileTest";
 String methodName = "testWaitMethod";
 long startTime = System.nanoTime();
 Profile.beginWait(className, methodName);
 Frame threadFrame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
 assertTrue(threadFrame != null);
 long endTime = System.nanoTime();
 long time = endTime - startTime;
 assertEquals(methodName, threadFrame.getMethodName());
 assertEquals(className, threadFrame.getClassName());
 assertEquals(time, threadFrame.getWaitTime());
 }

 @Test
 public void testEndWaitMethod() {
 String className = "ProfileTest";
 String methodName = "testWaitMethod";
 long startTime = System.nanoTime();
 Profile.beginWait(className, methodName);
 Frame threadFrame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
 long threadId = threadFrame.getThreadId();
 Profile.endWait(className, methodName);
 Frame endFrame = Profile.findFrame(threadId, className, methodName);
 long endTime = System.nanoTime();
 long time = endTime - startTime;
 assertEquals(methodName, endFrame.getMethodName());
 assertEquals(className, endFrame.getClassName());
 assertEquals(time, endFrame.getNetTime() - threadFrame.getNetTime());
 }

 @Test
 public void testUnwindMethod() {
 String className = "ProfileTest";
 String methodName = "testMethod";
 String exception = "TestException";
 long startTime = System.nanoTime();
 Profile.start(className, methodName);
 try {
 throw new TestException();
 } catch (TestException e) {
 Profile.unwind(className, methodName, exception);
 }
 Frame threadFrame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
 assertTrue(threadFrame != null);
 long endTime = System.nanoTime();
 long time = endTime - startTime;
 assertEquals(methodName, threadFrame.getMethodName());
 assertEquals(className, threadFrame.getClassName());
 assertEquals(time, threadFrame.getNetTime());
 }

 @Test
 public void testAllocMethod() {
 String className = "ProfileTest";
 long allocationCount = 10;
 for (int i = 0; i < allocationCount; i++) {
 Profile.alloc(className);
 }
 List<ClassAllocation> allocations = new ArrayList<ClassAllocation>(Profile._allocList.values());
 int size = allocations.size();
 for (int i = 0; i < size; i++) {
 ClassAllocation ca = allocations.get(i);
 assertEquals(className, ca.getClassName());
 assertEquals(allocationCount, ca.getAllocCount());
 }
 }
}
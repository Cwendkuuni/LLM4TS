package com.mentorgen.tools.profile.runtime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileTest {

 private static Profile underTest;
 private static long initialThreadId;

 @Before
 public void setUp() {
 initialThreadId = Thread.currentThread().getId();
 underTest = new Profile();
 Profile.init();
 }

 @After
 public void tearDown() {
 underTest = null;
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
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._allocList);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertTrue(Profile._threadDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
 assertTrue(Profile._threadActiveFrame.isEmpty());
 assertTrue(Profile._methodDictionary.isEmpty());
 assertTrue(Profile._allocList.isEmpty());
 }

 @Test
 public void testStart() {
 underTest.start();
 assertTrue(Profile._controller.isRunning());
 }

 @Test
 public void testStop() {
 underTest.stop();
 assertFalse(Profile._controller.isRunning());
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("test.txt");
 assertEquals("test.txt", Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 Profile.shutdown();
 assertEquals(0, Profile._frameList.size());
 assertTrue(Profile._controller.isClosed());
 }

 @Test
 public void testThreads() {
 underTest.start();
 List<Long> threads = Profile.threads();
 assertEquals(1, threads.size());
 assertEquals(initialThreadId, threads.get(0));
 }

 @Test
 public void testInteractions() {
 underTest.start();
 String className = "TestClass";
 String methodName = "testMethod";
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 List<Frame> interactions = Profile.interactions(initialThreadId);
 assertEquals(1, interactions.size());
 Frame interaction = interactions.get(0);
 assertEquals(className, interaction.getClassName());
 assertEquals(methodName, interaction.getMethodName());
 }

 @Test
 public void testFrameList() {
 underTest.start();
 String className = "TestClass";
 String methodName = "testMethod";
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 List<Frame> frameList = Profile.frameList();
 assertEquals(1, frameList.size());
 Frame frame = frameList.get(0);
 assertEquals(className, frame.getClassName());
 assertEquals(methodName, frame.getMethodName());
 }

 @Test
 public void testAllocations() {
 underTest.start();
 String className = "TestClass";
 Profile.alloc(className);
 List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
 assertEquals(1, allocations.size());
 ClassAllocation allocation = allocations.get(0);
 assertEquals(className, allocation.getClassName());
 allocation.decAllocCount();
 Profile.clear();
 }

 @Test
 public void testGetThreadTotalTime() {
 underTest.start();
 String className = "TestClass";
 String methodName = "testMethod";
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 long threadId = initialThreadId;
 long totalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(0, totalTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSortFrameListNullComparator() {
 underTest.start();
 Comparator<Frame> comparator = null;
 Profile.sortFrameList(comparator);
 fail("IllegalStateException expected");
 }
}
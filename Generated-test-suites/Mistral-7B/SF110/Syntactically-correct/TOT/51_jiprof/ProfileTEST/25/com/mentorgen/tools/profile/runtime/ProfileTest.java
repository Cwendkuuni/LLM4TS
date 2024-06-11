package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mentorgen.tools.profile.output.*;
import com.mentorgen.tools.profile.runtime.*;

public class ProfileTest {

 private static Profile _profile;
 private static Controller _controller;
 private static Frame _frame;

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 _profile = Profile.getInstance();
 _controller = Profile.getController();
 }

 @AfterClass
 public static void shutdownProfile() {
 Profile.shutdown();
 }

 @Test
 public void testInitAndClear() {
 // Test initial state
 assertEquals(0, Profile.getThreadTotalTime(Thread.currentThread().getId()));
 assertNotNull(Profile.threads());
 assertFalse(Profile.controllerIsRunning());
 // Test clear state
 Profile.clear();
 assertEquals(0, Profile.getThreadTotalTime(Thread.currentThread().getId()));
 assertNotNull(Profile.threads());
 assertFalse(Profile.controllerIsRunning());
 }

 @Test
 public void testStartAndStop() {
 // Test start and stop
 Profile.start();
 assertTrue(Profile.controllerIsRunning());
 Profile.stop();
 assertFalse(Profile.controllerIsRunning());
 }

 @Test
 public void testThreadTotalTime() {
 // Test thread total time
 long threadId = Thread.currentThread().getId();
 Profile.start();
 Thread.yield();
 Profile.stop();
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(-1, threadTotalTime); // Initially -1
 Profile.start();
 Thread.yield();
 Thread.yield();
 Profile.stop();
 long threadTotalTime2 = Profile.getThreadTotalTime(threadId);
 assertGreaterThan(0, threadTotalTime2);
 }

 @Test
 public void testInteractions() {
 // Test interactions
 Profile.start("TestClass", "testMethod");
 Thread.yield();
 Frame frame = Profile.getMostRecentFrame("TestClass", "testMethod");
 List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
 // Interactions should contain only the last frame
 assertEquals(1, interactions.size());
 Frame expectedFrame = frame;
 Frame actualFrame = interactions.get(0);
 assertEquals(expectedFrame.getMethodName(), actualFrame.getMethodName());
 assertEquals(expectedFrame.getClassName(), actualFrame.getClassName());
 assertEquals(expectedFrame.getThreadId(), actualFrame.getThreadId());
 }

 @Test
 public void testFrameList() {
 // Test frame list
 Profile.start("TestClass", "testMethod");
 Thread.yield();
 Frame frame = Profile.getMostRecentFrame("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 Profile.sortFrameList(new FrameComparator());
 List<Frame> frameList = Profile.frameList();
 // Frame list should contain the sorted frame
 Frame expectedFrame = frame;
 Frame actualFrame = frameList.get(0);
 assertEquals(expectedFrame.getMethodName(), actualFrame.getMethodName());
 assertEquals(expectedFrame.getClassName(), actualFrame.getClassName());
 assertEquals(expectedFrame.getThreadId(), actualFrame.getThreadId());
 }

 @Test
 public void testAllocations() {
 // Test allocations
 Profile.start("TestClass", "testMethod");
 Thread.yield();
 Profile.alloc("TestClass");
 Profile.end("TestClass", "testMethod");
 List<ClassAllocation> allocations = Profile.allocations();
 ClassAllocation expectedAllocation = new ClassAllocation("TestClass");
 ClassAllocation actualAllocation = allocations.get(0);
 assertEquals(expectedAllocation.getClassName(), actualAllocation.getClassName());
 assertEquals(1, actualAllocation.getAllocCount());
 }

 @Test
 public void testStartMethod() {
 // Test start method
 Profile.start("TestClass", "testMethod");
 Frame frame = Profile.getMostRecentFrame("TestClass", "testMethod");
 assertNotNull(frame);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartNullMethod() {
 // Test start method with null method
 Profile.start(null, "testMethod");
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartEmptyClassName() {
 // Test start method with empty className
 Profile.start("", "testMethod");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testEndMethod() {
 // Test end method
 Profile.start("TestClass", "testMethod");
 Thread.yield();
 Frame frame = Profile.getMostRecentFrame("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 assertNull(Profile.getMostRecentFrame("TestClass", "testMethod"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndNullMethod() {
 // Test end method with null method
 Profile.end(null, "testMethod");
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndEmptyClassName() {
 // Test end method with empty className
 Profile.end("", "testMethod");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testBeginWaitEndWait() {
 // Test beginWait and endWait
 Profile.start("TestClass", "testMethod");
 Thread.yield();
 Profile.beginWait("TestClass", "testMethodWait");
 Thread.yield();
 Profile.endWait("TestClass", "testMethodWait");
 Frame frame = Profile.getMostRecentFrame("TestClass", "testMethod");
 assertEquals(1, frame.getWaitCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBeginWaitNullMethod() {
 // Test beginWait with null method
 Profile.beginWait(null, "testMethodWait");
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBeginWaitEmptyClassName() {
 // Test beginWait with empty className
 Profile.beginWait("", "testMethodWait");
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndWaitNullMethod() {
 // Test endWait with null method
 Profile.endWait(null, "testMethodWait");
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndWaitEmptyClassName() {
 // Test endWait with empty className
 Profile.endWait("", "testMethodWait");
 fail("Should throw IllegalArgumentException");
 }

}
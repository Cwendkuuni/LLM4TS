package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import com.mentorgen.tools.profile.*;
import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfileTest {

 private static Profile underTest;

 @BeforeClass
 public static void setUpClass() {
 Profile.initProfiler();
 underTest = new Profile();
 }

 @Test
 public void testInit() {
 Profile.init();
 assertTrue(Profile._threadDictionary != null);
 assertTrue(Profile._methodDictionary != null);
 assertTrue(Profile._frameList != null);
 assertTrue(Profile._lock != null);
 assertTrue(Profile._allocList != null);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertFalse(Profile._threadDictionary.size() > 0);
 assertFalse(Profile._methodDictionary.size() > 0);
 assertFalse(Profile._frameList.size() > 0);
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 if (Profile._controllerThread != null) {
 assertTrue(Profile._controllerThread.isAlive());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testInitProfilerTwice() {
 Profile.initProfiler();
 Profile.initProfiler();
 fail("Should throw an exception");
 }

 @Test
 public void testThreads() {
 final Long threadId = Thread.currentThread().getId();
 assertTrue(Profile.threads().contains(threadId));
 }

 @Test
 public void testInteractions() {
 final Long threadId = Thread.currentThread().getId();
 Profile.start("ProfileTest", "<init>");
 Profile.end("ProfileTest", "<init>");
 List<Frame> interactions = Profile.interactions(threadId);
 assertTrue(interactions.size() > 0);
 }

 @Test
 public void testFrameList() {
 List<Frame> frameList = Profile.frameList();
 assertFalse(frameList.isEmpty());
 }

 @Test
 public void testAllocations() {
 Profile.alloc("ProfileTest");
 Iterable<ClassAllocation> allocations = Profile.allocations();
 assertTrue(allocations.iterator().next().getAllocCount() > 0);
 }

 @Test
 public void testGetThreadTotalTime() {
 final Long threadId = Thread.currentThread().getId();
 Profile.start("ProfileTest", "<init>");
 Profile.end("ProfileTest", "<init>");
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertTrue(threadTotalTime > 0);
 }

 @Test
 public void testSortFrameList() {
 Comparator<Frame> comp = new Comparator<Frame>() {
 @Override
 public int compare(Frame o1, Frame o2) {
 return o1.getBeginTime() < o2.getBeginTime() ? -1 : 1;
 }
 };
 Profile.sortFrameList(comp);
 }

 @Test
 public void testStartEnd() {
 final String className = "ProfileTest";
 final String methodName = "testStartEnd";
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 }

 @Test
 public void testBeginWaitEndWait() {
 final String className = "ProfileTest";
 final String methodName = "testBeginWaitEndWait";
 Profile.beginWait(className, methodName);
 Profile.endWait(className, methodName);
 }

 @Test
 public void testUnwind() {
 final String className = "ProfileTest";
 final String methodName = "testUnwind";
 final String exception = "TestException";
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 Profile.unwind(className, methodName, exception);
 }

 @Test
 public void testAlloc() {
 final String className = "ProfileTest";
 Profile.alloc(className);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentsForStart() {
 Profile.start(null, null);
 fail("Should throw an exception");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentsForEnd() {
 Profile.end(null, null);
 fail("Should throw an exception");
 }

 @Test
 public void testFindFrameNull() {
 assertNull(Profile.findFrame(-1L, null, null));
 }

 @Test
 public void testFindFrameNotMatching() {
 Profile.start("ProfileTest", "<init>");
 Profile.end("ProfileTest", "testMethod");
 Frame target = Profile.findFrame(-1L, "ProfileTest", "testMethod");
 assertNull(target);
 }

 @Test
 public void testFindFrameMatching() {
 Profile.start("ProfileTest", "<init>");
 Profile.end("ProfileTest", "<init>");
 Frame target = Profile.findFrame(Thread.currentThread().getId(), "ProfileTest", "<init>");
 assertNotNull(target);
 }

 @Test
 public void testShutdown() {
 Profile.shutdown();
 assertFalse(Profile._frameList.size() > 0);
 }

}
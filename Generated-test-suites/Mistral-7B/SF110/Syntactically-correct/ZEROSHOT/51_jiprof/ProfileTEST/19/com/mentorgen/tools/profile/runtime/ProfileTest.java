package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

 private static Profile profile;
 private static long threadId1, threadId2;
 private static Frame frame1, frame2;

 @Before
 public void setUp() throws Exception {
 Profile.init();
 profile = new Profile();
 threadId1 = Thread.currentThread().getId();
 threadId2 = Thread.currentThread();
 new Thread(profile).start();
 }

 @Test
 public void testInitProfiler() {
 assertEquals("Java Interactive Profiler: starting", System.err.getErr(), "Unexpected initialization message");
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
 Profile.clear();
 assertNull(Profile._threadActiveFrame.get(threadId1));
 assertNull(Profile._threadActiveFrame.get(threadId2));
 }

 @Test
 public void testStartAndEnd() {
 Profile.start("TestClass1", "testMethod1");
 Profile.end("TestClass1", "testMethod1");
 List<Frame> frames = Profile.frameList();
 assertEquals(1, frames.size());
 frame1 = frames.get(0);
 assertEquals("TestClass1", frame1.getClassName());
 assertEquals("testMethod1", frame1.getMethodName());
 }

 @Test
 public void testStartAndEndWithFrameChain() {
 Profile.start("TestClass1", "testMethod1");
 Profile.start("TestClass2", "testMethod2");
 Profile.end("TestClass2", "testMethod2");
 Profile.end("TestClass1", "testMethod1");
 frames = Profile.frameList();
 assertEquals(2, frames.size());
 frame1 = frames.get(0);
 frame2 = frames.get(1);
 assertEquals("TestClass1", frame1.getClassName());
 assertEquals("testMethod1", frame1.getMethodName());
 assertEquals("TestClass2", frame2.getClassName());
 assertEquals("testMethod2", frame2.getMethodName());
 assertNotNull(frame1.getParent());
 assertNull(frame2.getParent());
 }

 @Test
 public void testBeginAndEndWait() {
 Profile.start("TestClass1", "testMethod1");
 Profile.beginWait("TestClass1", "testMethod1");
 Thread.sleep(100);
 Profile.endWait("TestClass1", "testMethod1");
 frames = Profile.frameList();
 assertEquals(1, frames.size());
 frame1 = frames.get(0);
 assertNotNull(frame1.getBeginWaitTime());
 }

 @Test
 public void testUnwind() {
 try {
 throw new Exception();
 }
 catch (Exception e) {
 Profile.start("TestClass1", "testMethod1");
 Profile.start("TestClass1", "testMethod1");
 Profile.start("TestClass1", "testMethod1");
 Profile.unwind("TestClass1", "testMethod1", e.getClass().getName());
 frames = Profile.frameList();
 assertEquals(3, frames.size());
 frame1 = frames.get(0);
 frame2 = frames.get(1);
 frame3 = frames.get(2);
 assertEquals("TestClass1", frame1.getClassName());
 assertEquals("testMethod1", frame1.getMethodName());
 assertEquals("TestClass1", frame2.getClassName());
 assertEquals("testMethod1", frame2.getMethodName());
 assertEquals("TestClass1", frame3.getClassName());
 assertEquals("testMethod1", frame3.getMethodName());
 assertNotNull(frame1.getParent());
 assertNotNull(frame2.getParent());
 assertNull(frame3.getParent());
 }
 }

 @Test
 public void testAlloc() {
 Profile.alloc("TestClass1");
 Map<String, ClassAllocation> allocList = Profile._allocList;
 assertEquals(1, allocList.size());
 ClassAllocation ca = allocList.get("TestClass1");
 assertEquals("TestClass1", ca.getClassName());
 assertEquals(1, ca.getAllocCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 Profile.start("TestClass1", "testMethod1");
 Thread.sleep(100);
 Profile.end("TestClass1", "testMethod1");
 long threadTotalTime = Profile.getThreadTotalTime(threadId1);
 assertEquals(150, threadTotalTime); // 100ms sleep + method overhead
 }

 @Test
 public void testThreads() {
 List<Long> threads = new ArrayList<>(Profile.threads());
 assertEquals(1, threads.size());
 assertEquals(threadId1, threads.get(0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testInteractionsThreadIdNotFound() {
 List<Frame> interactions = new ArrayList<>(Profile.interactions(Long.MAX_VALUE));
 assertEquals(0, interactions.size());
 }

 @Test
 public void testInteractions() {
 Profile.start("TestClass1", "testMethod1");
 frames = Profile.interactions(threadId1);
 assertEquals(1, frames.size());
 assertEquals(frame1, frames.get(0));
 }

 @Test
 public void testSortFrameList() {
 Collections.sort(Profile._frameList, new FrameComparator());
 FrameComparator comparator = new FrameComparator();
 List<Frame> sortedFrames = new ArrayList<>(Profile._frameList);
 assertEquals(sortedFrames, Profile._frameList);
 }
}

 class FrameComparator implements Comparator<Frame> {
 @Override
 public int compare(Frame f1, Frame f2) {
 long f1Time = f1.getBeginTime();
 long f2Time = f2.getBeginTime();
 return Long.compare(f1Time, f2Time);
 }
}
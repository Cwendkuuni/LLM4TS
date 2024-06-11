package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

 private static Profile profile;
 private static long threadId;
 private static Frame frame;
 private static Method method;
 private static ClassAllocation allocation;

 @Before
 public void setUp() {
 Profile.init();
 threadId = 1L;
 method = new Method("className", "methodName");
 allocation = new ClassAllocation("className");
 profile = new Profile();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 }

 @Test
 public void testInit() {
 Profile.init();
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._allocList);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertNotNull(Profile._threadDictionary);
 assertEquals(0, Profile._threadDictionary.size());
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
 Profile.setFileName("fileName.txt");
 assertEquals("fileName.txt", Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 Profile.shutdown();
 assertTrue(Profile._controller.isClosed());
 }

 @Test
 public void testThreads() {
 Frame thread1 = new Frame(null, method, threadId);
 Frame thread2 = new Frame(null, method, threadId + 1);
 Profile._threadDictionary.add(threadId, thread1);
 Profile._threadDictionary.add(threadId + 1, thread2);
 List<Long> threads = Profile.threads();
 assertEquals(2, threads.size());
 assertEquals(threadId, threads.get(0).longValue());
 assertEquals(threadId + 1, threads.get(1).longValue());
 }

 @Test
 public void testInteractions() {
 Frame threadInteraction = new Frame(null, method, threadId);
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(1, interactions.size());
 assertEquals(threadInteraction, interactions.get(0));
 }

 @Test
 public void testFrameList() {
 Frame frame1 = new Frame(null, method, threadId);
 Frame frame2 = new Frame(null, method, threadId + 1);
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 List<Frame> frameList = Profile.frameList();
 assertEquals(2, frameList.size());
 assertEquals(frame1, frameList.get(0));
 assertEquals(frame2, frameList.get(1));
 }

 @Test
 public void testAllocations() {
 ClassAllocation allocation1 = new ClassAllocation("className1");
 ClassAllocation allocation2 = new ClassAllocation("className2");
 Profile._allocList.put("className1", allocation1);
 Profile._allocList.put("className2", allocation2);
 List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
 assertEquals(2, allocations.size());
 assertTrue(Profile._allocList.containsValue(allocation1));
 assertTrue(Profile._allocList.containsValue(allocation2));
 }

 @Test
 public void testGetThreadTotalTime() {
 Frame threadFrame = new Frame(null, method, threadId);
 threadFrame.setTotalTime(1000L);
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(1000L, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 Frame frame1 = new Frame(null, method, threadId);
 Frame frame2 = new Frame(null, method, threadId + 1);
 Frame frame3 = new Frame(null, method, threadId);
 Profile._frameList.add(frame3);
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 Collections.sort(Profile._frameList, new Comparator<Frame>() {
 @Override
 public int compare(Frame f1, Frame f2) {
 return Long.compare(f1.getThreadId(), f2.getThreadId());
 }
 });
 List<Frame> sortedFrameList = new ArrayList<>(Profile._frameList);
 assertEquals(3, sortedFrameList.size());
 assertEquals(frame1, sortedFrameList.get(0));
 assertEquals(frame2, sortedFrameList.get(1));
 assertEquals(frame3, sortedFrameList.get(2));
 }

 @Test
 public void testStartEnd() {
 long startTime = System.nanoTime();
 Profile.start("className", "methodName");
 long endTime = System.nanoTime();
 Frame expectedFrame = new Frame(null, method, threadId);
 expectedFrame.setBeginTime(startTime);
 expectedFrame.setEndTime(endTime);
 Frame actualFrame = findFrame();
 assertEquals(expectedFrame, actualFrame);
 }

 @Test
 public void testBeginWaitEndWait() {
 long startTime = System.nanoTime();
 Profile.beginWait("className", "methodName");
 long endTime = System.nanoTime();
 Frame expectedFrame = new Frame(null, method, threadId);
 expectedFrame.beginWait(startTime);
 expectedFrame.endWait(endTime);
 Frame actualFrame = findFrame();
 assertEquals(expectedFrame, actualFrame);
 }

 @Test
 public void testAlloc() {
 Profile.alloc("className");
 ClassAllocation expectedAllocation = allocation;
 ClassAllocation actualAllocation = findAllocation();
 assertEquals(expectedAllocation, actualAllocation);
 }

 @Test
 public void testUnwind() {
 Frame parentFrame = new Frame(null, method, threadId - 1);
 Frame unwindFrame = new Frame("exceptionClassName", "exceptionMethodName", threadId);
 Profile._threadActiveFrame.put(threadId, unwindFrame);
 Profile._threadActiveFrame.put(threadId - 1, parentFrame);
 Profile.unwind("className", "methodName", "exceptionClassName");
 Frame expectedFrame = parentFrame;
 Frame actualFrame = findFrame();
 assertEquals(expectedFrame, actualFrame);
 }

 private Frame findFrame() {
 Frame frame = null;
 Iterator<Frame> frameIterator = Profile._frameList.iterator();
 while (frame == null && frameIterator.hasNext()) {
 frame = frameIterator.next();
 }
 return frame;
 }

 private ClassAllocation findAllocation() {
 ClassAllocation allocation = null;
 Iterator<ClassAllocation> allocationIterator = Profile._allocList.values().iterator();
 while (allocation == null && allocationIterator.hasNext()) {
 allocation = allocationIterator.next();
 }
 return allocation;
 }
}
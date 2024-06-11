package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;
import com.mentorgen.tools.profile.output.ThreadDictionary;

public class ProfileTest {

 private static Profile profile;
 private static ThreadDictionary threadDictionary;
 private static Frame frame;

 @BeforeClass
 public static void setUpClass() {
 Profile.init();
 profile = new Profile();
 threadDictionary = new ThreadDictionary();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 }

 @Test(expected = IllegalStateException.class)
 public void testInitProfilerAlreadyInitialized() {
 Profile.initProfiler();
 Profile.initProfiler();
 }

 @Test
 public void testInit() {
 Profile.init();
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._methodDictionary);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertTrue(Profile._threadDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
 }

 @Test
 public void testStart() {
 Profile.start();
 assertTrue(Profile._controller.isRunning());
 }

 @Test
 public void testStop() {
 Profile.start();
 Profile.stop();
 assertFalse(Profile._controller.isRunning());
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("test.txt");
 assertEquals("test.txt", Profile._controller.getFileName());
 }

 @Test(expected = NullPointerException.class)
 public void testShutdownNullLock() {
 Profile._lock = null;
 Profile.shutdown();
 }

 @Test
 public void testThreads() {
 Frame f = new Frame(null, new Method("TestClass", "<init>"), 1L);
 threadDictionary.add(f.getThreadId(), f);
 Frame f2 = new Frame(null, new Method("TestClass", "testMethod"), 2L);
 threadDictionary.add(f2.getThreadId(), f2);
 List<Long> threads = Profile.threads();
 assertEquals(2, threads.size());
 assertEquals(1L, threads.get(0).longValue());
 assertEquals(2L, threads.get(1).longValue());
 }

 @Test
 public void testInteractions() {
 Frame f = new Frame(null, new Method("TestClass", "<init>"), 1L);
 threadDictionary.add(f.getThreadId(), f);
 Frame f2 = new Frame(null, new Method("TestClass", "testMethod"), 2L);
 threadDictionary.add(f2.getThreadId(), f2);
 List<Frame> interactions = Profile.interactions(1L);
 assertEquals(1, interactions.size());
 assertEquals(f, interactions.get(0));
 }

 @Test
 public void testFrameList() {
 Frame f = new Frame(null, new Method("TestClass", "<init>"), 1L);
 Frame f2 = new Frame(null, new Method("TestClass", "testMethod"), 2L);
 threadDictionary.add(f.getThreadId(), f);
 threadDictionary.add(f2.getThreadId(), f2);
 List<Frame> frameList = Profile.frameList();
 assertEquals(2, frameList.size());
 }

 @Test
 public void testAllocations() {
 Frame f = new Frame(null, new Method("TestClass", "<init>"), 1L);
 threadDictionary.add(f.getThreadId(), f);
 List<ClassAllocation> allocations = Profile.allocations();
 assertEquals(0, allocations.size());
 f.allocate(10);
 List<ClassAllocation> allocations2 = Profile.allocations();
 assertEquals(1, allocations2.size());
 assertEquals("TestClass", allocations2.get(0).className);
 assertEquals(1, allocations2.get(0).allocCount);
 }

 @Test
 public void testGetThreadTotalTime() {
 Frame f = new Frame(null, new Method("TestClass", "<init>"), 1L);
 Frame f2 = new Frame(null, new Method("TestClass", "testMethod"), 2L);
 threadDictionary.add(f.getThreadId(), f);
 threadDictionary.add(f2.getThreadId(), f2);
 long threadId = 1L;
 long expectedTotalTime = 1000L; // for testing purpose
 f.setBeginTime(0);
 f.setEndTime(expectedTotalTime);
 assertEquals(expectedTotalTime, Profile.getThreadTotalTime(threadId));
 }

 @Test
 public void testSortFrameList() {
 Frame f1 = new Frame(null, new Method("TestClass1", "testMetho1"), 1L);
 Frame f2 = new Frame(null, new Method("TestClass2", "testMethod2"), 2L);
 Frame f3 = new Frame(null, new Method("TestClass1", "testMetho1"), 3L);
 List<Frame> frameList = new ArrayList<Frame>();
 frameList.add(f1);
 frameList.add(f2);
 frameList.add(f3);
 Comparator<Frame> comp = (f1, f2) -> Long.compare(f1.getBeginTime(), f2.getBeginTime());
 Profile.sortFrameList(comp);
 assertEquals(f1.getBeginTime(), frameList.get(0).getBeginTime());
 assertEquals(f2.getBeginTime(), frameList.get(1).getBeginTime());
 assertEquals(f3.getBeginTime(), frameList.get(2).getBeginTime());
 }

 @Test
 public void testStartEnd() {
 Method method = new Method("TestClass", "testMethod");
 String className = "TestClass";
 String methodName = "testMethod";
 Frame parent = new Frame(null, null, 1L);
 Frame frame = new Frame(parent, method, 2L);
 threadDictionary.add(2L, frame);
 Profile.start(className, methodName);
 long startTime = System.nanoTime();
 Profile.end(className, methodName);
 long endTime = System.nanoTime();
 long overhead = frame.getOverhead();
 long netTime = endTime - startTime;
 assertEquals(overhead, netTime);
 }

 @Test
 public void testBeginWaitEndWait() {
 Method method = new Method("TestClass", "testMethod");
 String className = "TestClass";
 String methodName = "testMethod";
 Frame frame = new Frame(null, method, 1L);
 threadDictionary.add(1L, frame);
 Profile.beginWait(className, methodName);
 long startTime = System.nanoTime();
 Profile.endWait(className, methodName);
 long overhead = frame.getOverhead();
 long netTime = endTime - startTime;
 assertEquals(overhead, netTime);
 }

 @Test
 public void testUnwind() {
 Method method = new Method("TestClass", "testMethod");
 String className = "TestClass";
 String methodName = "testMethod";
 Frame parent = new Frame(null, null, 1L);
 Frame frame = new Frame(parent, method, 2L);
 threadDictionary.add(2L, frame);
 Profile.unwind(className, methodName, "Exception");
 Frame unwindFrame = threadDictionary.getMostRecentFrame(2L);
 assertEquals(frame, unwindFrame);
 }

 @Test
 public void testFindFrame() {
 Method method = new Method("TestClass", "testMethod");
 String className = "TestClass";
 String methodName = "testMethod";
 Frame parent = new Frame(null, null, 1L);
 Frame parent2 = new Frame(null, null, 2L);
 Frame frame = new Frame(parent, method, 3L);
 Frame frame2 = new Frame(parent2, method, 4L);
 Frame frame3 = new Frame(null, null, 5L);
 threadDictionary.add(3L, frame);
 threadDictionary.add(4L, frame2);
 threadDictionary.add(5L, frame3);
 Frame foundFrame = Profile.findFrame(3L, className, methodName);
 assertEquals(frame, foundFrame);
 }

 @Test
 public void testAlloc() {
 String className = "TestClass";
 Profile.alloc(className);
 ClassAllocation ca = Profile._allocList.get(className);
 assertEquals(1, ca.getAllocCount());
 }

 @Test
 public void testRun() {
 profile.run();
 assertTrue(Profile._controller.isClosed());
 }

}
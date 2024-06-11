package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

 private static Profile underTest;

 @Before
 public void setUp() {
 underTest = new Profile();
 }

 @Test
 public void testInitProfiler() {
 // Test that the initProfiler method initializes the necessary fields and starts the profiler thread
 underTest.initProfiler();
 assertNotNull(underTest._controller);
 assertNotNull(underTest._controllerThread);
 assertTrue(underTest._controllerThread.isAlive());
 }

 @Test
 public void testClear() {
 // Test that the clear method initializes all necessary fields when called
 underTest.init();
 underTest.clear();
 assertSame(underTest._threadActiveFrame, underTest.getThreadActiveFrame());
 assertSame(underTest._threadDictionary, underTest.getThreadDictionary());
 assertSame(underTest._frameList, underTest.getFrameList());
 assertSame(underTest._methodDictionary, underTest.getMethodDictionary());
 assertSame(underTest._allocList, underTest.getAllocList());
 assertSame(underTest._controller, underTest.getController());
 }

 @Test
 public void testInitAndClearMultipleTimes() {
 // Test that the init and clear methods can be called multiple times without causing issues
 underTest.init();
 underTest.clear();
 underTest.init();
 underTest.clear();
 }

 @Test
 public void testThreads() {
 // Test that the threads method returns an iterable containing all initialized threads
 underTest.init();
 underTest.start();
 List<Long> threads = new ArrayList<>();
 threads.add(Thread.currentThread().getId());
 threads.add(new Thread(new Runnable() {
 @Override
 public void run() {
 // Empty thread body to ensure it is added to the thread dictionary
 }
 }).start().getId());
 Iterator<Long> threadsIterator = underTest.threads();
 for (Long threadId : threads) {
 assertTrue(threadsIterator.hasNext());
 assertEquals(threadId, threadsIterator.next());
 }
 }

 @Test
 public void testInteractions() {
 // Test that the interactions method returns an iterable containing all frames for a given thread
 underTest.init();
 underTest.start();
 underTest.start("TestClass", "testMethod");
 underTest.end("TestClass", "testMethod");
 List<Long> threads = new ArrayList<>();
 threads.add(Thread.currentThread().getId());
 Iterator<Long> threadsIterator = underTest.threads();
 Iterator<Frame> frames = underTest.interactions(threads.get(0)).iterator();
 Frame firstFrame = frames.next();
 assertEquals("TestClass", firstFrame.getClassName());
 assertEquals("testMethod", firstFrame.getMethodName());
 assertNull(firstFrame.getParent());
 }

 @Test
 public void testFrameList() {
 // Test that the frameList method returns an iterable containing all frames in the correct order
 underTest.init();
 underTest.start("TestClass1", "testMethod1");
 underTest.end("TestClass1", "testMethod1");
 underTest.start("TestClass2", "testMethod2");
 underTest.end("TestClass2", "testMethod2");
 underTest.start("TestClass1", "testMethod1");
 underTest.end("TestClass1", "testMethod1");
 List<Long> threads = new ArrayList<>();
 threads.add(Thread.currentThread().getId());
 Iterator<Frame> frames = underTest.frameList().iterator();
 Frame firstFrame = frames.next();
 assertEquals("TestClass1", firstFrame.getClassName());
 assertEquals("testMethod1", firstFrame.getMethodName());
 assertNull(firstFrame.getParent());
 Frame secondFrame = frames.next();
 assertEquals("TestClass2", secondFrame.getClassName());
 assertEquals("testMethod2", secondFrame.getMethodName());
 assertNull(secondFrame.getParent());
 Frame thirdFrame = frames.next();
 assertEquals("TestClass1", thirdFrame.getClassName());
 assertEquals("testMethod1", thirdFrame.getMethodName());
 assertSame(firstFrame, thirdFrame.getParent());
 }

 @Test
 public void testAllocations() {
 // Test that the allocations method returns an iterable containing all class allocations
 underTest.init();
 underTest.start("TestClass1", "testMethod1");
 underTest.alloc("TestClass1");
 underTest.end("TestClass1", "testMethod1");
 List<String> allocations = new ArrayList<>();
 allocations.add("TestClass1");
 Iterator<ClassAllocation> classAllocations = underTest.allocations().iterator();
 ClassAllocation firstClassAllocation = classAllocations.next();
 assertEquals("TestClass1", firstClassAllocation.getClassName());
 assertEquals(1, firstClassAllocation.getAllocCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 // Test that the getThreadTotalTime method correctly returns the total time for a given thread
 underTest.init();
 underTest.start();
 Long threadId = Thread.currentThread().getId();
 underTest.start("TestClass1", "testMethod1");
 underTest.end("TestClass1", "testMethod1");
 underTest.start("TestClass1", "testMethod1");
 underTest.end("TestClass1", "testMethod1");
 long threadTotalTime = underTest.getThreadTotalTime(threadId);
 long expectedThreadTotalTime = 2 * (System.nanoTime() - underTest.getFrameList().get(1).getBeginTime());
 assertEquals(expectedThreadTotalTime, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 // Test that the sortFrameList method correctly sorts the frame list
 underTest.init();
 underTest.start("TestClass1", "testMethod1");
 underTest.end("TestClass1", "testMethod1");
 underTest.start("TestClass2", "testMethod2");
 underTest.end("TestClass2", "testMethod2");
 underTest.start("TestClass1", "testMethod1");
 underTest.end("TestClass1", "testMethod1");
 List<Long> threads = new ArrayList<>();
 threads.add(Thread.currentThread().getId());
 underTest.sortFrameList((f1, f2) -> f1.getBeginTime() < f2.getBeginTime() ? -1 : (f1.getBeginTime() > f2.getBeginTime() ? 1 : 0));
 Iterator<Frame> frames = underTest.frameList().iterator();
 Frame firstFrame = frames.next();
 assertEquals("TestClass1", firstFrame.getClassName());
 assertEquals("testMethod1", firstFrame.getMethodName());
 assertNull(firstFrame.getParent());
 Frame secondFrame = frames.next();
 assertEquals("TestClass2", secondFrame.getClassName());
 assertEquals("testMethod2", secondFrame.getMethodName());
 assertNull(secondFrame.getParent());
 Frame thirdFrame = frames.next();
 assertEquals("TestClass1", thirdFrame.getClassName());
 assertEquals("testMethod1", thirdFrame.getMethodName());
 assertSame(firstFrame, thirdFrame.getParent());
 }

 @Test(expected = IllegalStateException.class)
 public void testStartMethodTwice() {
 // Test that the start method throws an IllegalStateException when called multiple times for the same thread and method
 underTest.init();
 underTest.start();
 underTest.start("TestClass1", "testMethod1");
 underTest.end("TestClass1", "testMethod1");
 underTest.start("TestClass1", "testMethod1");
 }

 @Test(expected = IllegalStateException.class)
 public void testEndMethodWithoutStart() {
 // Test that the end method throws an IllegalStateException when called without a corresponding start method call
 underTest.init();
 underTest.end("TestClass1", "testMethod1");
 }

 @Test
 public void testStartEndWithParentChildRelationship() {
 // Test that frames have the correct parent-child relationships when started and ended in the correct order
 underTest.init();
 underTest.start();
 underTest.start("ParentClass", "parentMethod");
 underTest.start("ChildClass", "childMethod");
 underTest.end("ChildClass", "childMethod");
 underTest.end("ParentClass", "parentMethod");
 List<Long> threads = new ArrayList<>();
 threads.add(Thread.currentThread().getId());
 Iterator<Frame> frames = underTest.frameList().iterator();
 Frame parentFrame = frames.next();
 assertEquals("ParentClass", parentFrame.getClassName());
 assertEquals("parentMethod", parentFrame.getMethodName());
 assertNull(parentFrame.getParent());
 Frame childFrame = frames.next();
 assertEquals("ChildClass", childFrame.getClassName());
 assertEquals("childMethod", childFrame.getMethodName());
 assertSame(parentFrame, childFrame.getParent());
 }
}
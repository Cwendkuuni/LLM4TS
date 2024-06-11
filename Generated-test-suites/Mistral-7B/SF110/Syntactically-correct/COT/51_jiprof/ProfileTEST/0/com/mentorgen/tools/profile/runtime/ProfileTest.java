package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import com.mentorgen.tools.profile.*;
import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfileTest {

 private static Profile underTest;
 static Map<Long, Frame> threadDictionary;
 static List<Frame> frameList;
 static Map<String, Method> methodDictionary;
 static Map<String, ClassAllocation> allocList;

 @BeforeClass
 public static void setUp() {
 Profile.init();
 underTest = new Profile();
 threadDictionary = Profile.threadDictionary();
 frameList = Profile.frameList();
 methodDictionary = Profile.methodDictionary();
 allocList = Profile.allocList();
 }

 @Test
 public void testInitProfiler() {
 assertTrue(Profile.isProfiling());
 }

 @Test
 public void testClear() {
 assertFalse(Profile.isProfiling());
 Profile.init();
 assertTrue(Profile.isProfiling());
 }

 @Test
 public void testInit() {
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._allocList);
 }

 @Test
 public void testThreads() {
 List<Long> threads = Profile.threads();
 assertFalse(threads.isEmpty());
 }

 @Test
 public void testInteractions() {
 Iterator<Frame> frameIterator = frameList.iterator();
 long threadId = frameIterator.next().getThreadId();
 Iterable<Frame> interactions = Profile.interactions(threadId);
 assertFalse(interactions.isEmpty());
 }

 @Test
 public void testFrameList() {
 assertFalse(frameList.isEmpty());
 }

 @Test
 public void testAllocations() {
 assertFalse(allocList.isEmpty());
 }

 @Test
 public void testGetThreadTotalTime() {
 long threadId = 1L;
 long expectedThreadTotalTime = 1000L;
 when(threadDictionary.get(threadId)).thenReturn(new Frame(null, null, threadId));
 when(threadDictionary.getMostRecentFrame(threadId)).thenReturn(new Frame(null, null, threadId));
 Profile.setThreadTotalTime(threadId, expectedThreadTotalTime);
 long actualThreadTotalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(expectedThreadTotalTime, actualThreadTotalTime);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetThreadTotalTimeNotFound() {
 long threadId = 2L;
 when(threadDictionary.get(threadId)).thenReturn(null);
 Profile.getThreadTotalTime(threadId);
 }

 @Test
 public void testSortFrameList() {
 Comparator<Frame> comparator = new Comparator<Frame>() {
 @Override
 public int compare(Frame f1, Frame f2) {
 return Long.compare(f1.getThreadId(), f2.getThreadId());
 }
 };
 Profile.sortFrameList(comparator);
 Collections.sort(frameList, comparator);
 assertTrue(frameList.equals(Profile.frameList()));
 }

 @Test
 public void testStart() {
 String className = "MyClass";
 String methodName = "myMethod";
 underTest.start(className, methodName);
 Frame expectedFrame = new Frame(null, new Method(className, methodName), Thread.currentThread().getId());
 Frame actualFrame = findFrame(Thread.currentThread().getId(), className, methodName);
 assertEquals(expectedFrame, actualFrame);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 String className = "MyClass";
 String methodName = "myMethod";
 underTest.start(className, methodName);
 underTest.start(className, methodName);
 }

 @Test
 public void testEnd() {
 String className = "MyClass";
 String methodName = "myMethod";
 underTest.start(className, methodName);
 underTest.end(className, methodName);
 Frame expectedFrame = findFrame(Thread.currentThread().getId(), className, methodName);
 assertNull(expectedFrame.getParent());
 }

 @Test(expected = IllegalStateException.class)
 public void testEndTwice() {
 String className = "MyClass";
 String methodName = "myMethod";
 underTest.start(className, methodName);
 underTest.start(className, methodName);
 underTest.end(className, methodName);
 underTest.end(className, methodName);
 }

 @Test
 public void testBeginWait() {
 String className = "MyClass";
 String methodName = "myMethod";
 underTest.beginWait(className, methodName);
 Frame expectedFrame = findFrame(Thread.currentThread().getId(), className, methodName);
 assertTrue(expectedFrame.isWaiting());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginWaitTwice() {
 String className = "MyClass";
 String methodName = "myMethod";
 underTest.beginWait(className, methodName);
 underTest.beginWait(className, methodName);
 }

 @Test
 public void testEndWait() {
 String className = "MyClass";
 String methodName = "myMethod";
 underTest.beginWait(className, methodName);
 underTest.endWait(className, methodName);
 Frame expectedFrame = findFrame(Thread.currentThread().getId(), className, methodName);
 assertFalse(expectedFrame.isWaiting());
 }

 @Test(expected = IllegalStateException.class)
 public void testEndWaitTwice() {
 String className = "MyClass";
 String methodName = "myMethod";
 underTest.beginWait(className, methodName);
 underTest.endWait(className, methodName);
 underTest.endWait(className, methodName);
 }

 @Test
 public void testUnwind() {
 String className = "MyClass";
 String methodName = "myMethod";
 String exception = "MyException";
 underTest.unwind(className, methodName, exception);
 Frame expectedFrame = findFrame(Thread.currentThread().getId(), className, methodName);
 assertEquals(exception, expectedFrame.getException());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnwindTwice() {
 String className = "MyClass";
 String methodName = "myMethod";
 String exception = "MyException";
 underTest.unwind(className, methodName, exception);
 underTest.unwind(className, methodName, exception);
 }

 @Test
 public void testAlloc() {
 String className = "MyClass";
 underTest.alloc(className);
 ClassAllocation expectedAllocation = allocList.get(className);
 assertEquals(1, expectedAllocation.getAllocCount());
 }

 @Test
 public void testAllocTwice() {
 String className = "MyClass";
 underTest.alloc(className);
 underTest.alloc(className);
 ClassAllocation expectedAllocation = allocList.get(className);
 assertEquals(2, expectedAllocation.getAllocCount());
 }

 @Test
 public void testRun() {
 underTest.run();
 assertTrue(Profile.isShutdown());
 }

 private Frame findFrame(long threadId, String className, String methodName) {
 Frame frame = null;
 for (Frame f : frameList) {
 if (f.getThreadId() == threadId && f.getClassName().equals(className) && f.getMethodName().equals(methodName)) {
 frame = f;
 break;
 }
 }
 return frame;
 }

}
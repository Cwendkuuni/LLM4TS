package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;
import com.mentorgen.tools.profile.output.ThreadDictionary;

public class ProfileTest {

 private static Profile underTest;

 @BeforeClass
 public static void setupClass() {
 Profile.init();
 underTest = new Profile();
 }

 @AfterClass
 public static void teardownClass() {
 Profile.clear();
 }

 @Before
 public void setUp() {
 Profile._threadActiveFrame.clear();
 Profile._frameList.clear();
 }

 @Test
 public void testInitProfiler() {
 assertFalse(Profile._controllerThread.isAlive());
 assertTrue(Profile._controllerThread.isAlive());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitProfilerTwice() {
 Profile.initProfiler();
 Profile.initProfiler();
 }

 @Test
 public void testInit() {
 assertFalse(Profile._controllerThread.isAlive());
 assertNull(Profile._controller);
 assertEquals(0, Profile._threadDictionary.size());
 assertEquals(0, Profile._methodDictionary.size());
 assertEquals(0, Profile._frameList.size());
 assertNull(Profile._lock);
 assertEquals(0, Profile._allocList.size());
 }

 @Test
 public void testClear() {
 assertFalse(Profile._controllerThread.isAlive());
 assertNull(Profile._controller);
 assertEquals(0, Profile._threadDictionary.size());
 assertEquals(0, Profile._methodDictionary.size());
 assertEquals(0, Profile._frameList.size());
 assertNull(Profile._lock);
 assertEquals(0, Profile._allocList.size());
 }

 @Test
 public void testStart() {
 Profile.start();
 assertTrue(underTest._controller.isRunning());
 }

 @Test
 public void testStop() {
 Profile.stop();
 assertFalse(underController.isRunning());
 }

 @Test
 public void testSetFileName() {
 String fileName = "test.txt";
 Profile.setFileName(fileName);
 assertEquals(fileName, Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 Profile.initProfiler();
 Profile.shutdown();
 assertFalse(Profile._controllerThread.isAlive());
 }

 @Test
 public void testThreads() {
 List<Long> threads = new ArrayList<>(Profile.threads());
 assertEquals(1, threads.size());
 assertEquals(Thread.currentThread().getId(), threads.get(0));
 }

 @Test
 public void testInteractions() {
 Long threadId = Thread.currentThread().getId();
 Frame frame = new Frame(null, new Method("Test", "<init>"), threadId);
 Profile._threadDictionary.add(threadId, frame);
 Iterator<Frame> iterator = Profile.interactions(threadId).iterator();
 assertEquals(1, iterator.next().getMethodName().length());
 }

 @Test
 public void testFrameList() {
 Frame frame = new Frame(null, new Method("Test", "<init>"), Thread.currentThread().getId());
 Profile._frameList.add(frame);
 List<Frame> frames = new ArrayList<>(Profile.frameList());
 assertEquals(1, frames.size());
 assertEquals(frame, frames.get(0));
 }

 @Test
 public void testAllocations() {
 Map<String, ClassAllocation> allocations = new HashMap<>();
 ClassAllocation allocation = new ClassAllocation("Test");
 allocations.put("Test", allocation);
 assertEquals(1, allocations.size());
 Iterable<ClassAllocation> iterableAllocations = Profile.allocations();
 assertEquals(1, iterableAllocations.spliterator().getExactSizeIfKnown());
 }

 @Test
 public void testGetThreadTotalTime() {
 Long threadId = Thread.currentThread().getId();
 Frame frame = new Frame(null, new Method("Test", "<init>"), threadId);
 Profile._threadDictionary.add(threadId, frame);
 long totalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(0, totalTime);
 }

 @Test
 public void testSortFrameList() {
 Frame frame1 = new Frame(null, new Method("Test1", "<init>"), Thread.currentThread().getId());
 Frame frame2 = new Frame(null, new Method("Test2", "<init>"), Thread.currentThread().getId());
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 Collections.sort(Profile._frameList, new Comparator<Frame>() {
 @Override public int compare(Frame f1, Frame f2) {return f1.getBeginTime() - f2.getBeginTime();} });
 Frame firstFrame = Profile._frameList.get(0);
 Frame secondFrame = Profile._frameList.get(1);
 assertTrue(firstFrame.getBeginTime() < secondFrame.getBeginTime());
 }

 @Test
 public void testStartEnd() {
 String className = "Test";
 String methodName = "<init>";
 Profile.start(className, methodName);
 long startTime = System.nanoTime();
 Profile.end(className, methodName);
 Frame frame = findFrame(Thread.currentThread().getId(), className, methodName);
 long netTime = frame.getNetTime();
 long overhead = frame.getOverhead();
 assertEquals(0, netTime);
 assertTrue(overhead > startTime);
 }

 @Test
 public void testBeginWaitEndWait() {
 String className = "Test";
 String methodName = "<init>";
 Profile.beginWait(className, methodName);
 long startTime = System.nanoTime();
 Profile.endWait(className, methodName);
 Frame frame = findFrame(Thread.currentThread().getId(), className, methodName);
 long netTime = frame.getNetTime();
 long overhead = frame.getOverhead();
 assertEquals(0, netTime);
 assertTrue(overhead > startTime);
 }

 @Test
 public void testUnwind() {
 String className = "Test";
 String methodName = "<init>";
 String exception = "TestException";
 Profile.unwind(className, methodName, exception);
 Frame frame = findFrame(Thread.currentThread().getId(), className, methodName);
 assertNotNull(frame.getException());
 }

 @Test
 public void testAlloc() {
 String className = "Test";
 Profile.alloc(className);
 ClassAllocation allocation = Profile._allocList.get(className);
 assertEquals(1, allocation.getAllocCount());
 }

 private Frame findFrame(long threadId, String className, String methodName) {
 Frame frame = null;
 for (Frame f : Profile._frameList) {
 if (f.getThreadId() == threadId && f.getClassName().equals(className) && f.getMethodName().equals(methodName)) {
 frame = f;
 break;
 }
 }
 return frame;
 }
}
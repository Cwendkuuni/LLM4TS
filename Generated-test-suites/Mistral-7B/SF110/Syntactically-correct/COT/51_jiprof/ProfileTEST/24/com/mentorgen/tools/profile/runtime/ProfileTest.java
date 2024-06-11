package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;
import com.mentorgen.tools.profile.output.ThreadDictionary;

public class ProfileTest {

 private Profile profile;
 private ThreadDictionary threadDictionary;
 private Frame frame;

 @Before
 public void setUp() {
 profile = new Profile();
 threadDictionary = new ThreadDictionary();
 }

 @Test
 public void testInitProfiler() {
 profile.initProfiler();
 assertTrue(profile._controllerThread.isAlive());
 }

 @Test(expected = IllegalStateException.class)
 public void testInitProfilerAlreadyInitialized() {
 profile.initProfiler();
 profile.initProfiler();
 }

 @Test
 public void testInit() {
 profile.init();
 assertNotNull(profile._threadActiveFrame);
 assertNotNull(profile._threadDictionary);
 assertNotNull(profile._methodDictionary);
 assertNotNull(profile._frameList);
 assertNotNull(profile._lock);
 assertNotNull(profile._allocList);
 }

 @Test
 public void testClear() {
 profile.clear();
 assertEquals(0, threadDictionary.size());
 assertEquals(0, profile._frameList.size());
 }

 @Test
 public void testStart() {
 profile.start();
 assertTrue(profile._controller.isRunning());
 }

 @Test
 public void testStop() {
 profile.stop();
 assertFalse(profile._controller.isRunning());
 }

 @Test
 public void testSetFileName() {
 profile.setFileName("test.out");
 assertEquals("test.out", profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 profile.shutdown();
 assertFalse(profile._controller.isRunning());
 }

 @Test
 public void testThreads() {
 profile.init();
 Thread thread = new Thread(() -> {});
 thread.start();
 profile.start("Main", "<init>");
 List<Long> threads = profile.threads();
 assertEquals(1, threads.size());
 assertEquals(Thread.currentThread().getId(), threads.get(0));
 }

 @Test
 public void testInteractions() {
 profile.init();
 Thread thread = new Thread(() -> {
 frame = new Frame(null, new Method("Main", "<init>"), Thread.currentThread().getId());
 profile.start("Main", "<init>");
 });
 thread.start();
 List<Frame> interactions = profile.interactions(Thread.currentThread().getId());
 assertEquals(1, interactions.size());
 assertEquals(frame, interactions.get(0));
 }

 @Test
 public void testFrameList() {
 profile.init();
 Thread thread = new Thread(() -> {
 frame = new Frame(null, new Method("Main", "<init>"), Thread.currentThread().getId());
 profile.start("Main", "<init>");
 });
 thread.start();
 frame.end("Main", "<init>");
 List<Frame> frameList = profile.frameList();
 assertEquals(1, frameList.size());
 assertEquals(frame, frameList.get(0));
 }

 @Test
 public void testAllocations() {
 profile.init();
 profile.alloc("Object");
 List<ClassAllocation> allocations = new ArrayList<>(profile._allocList.values());
 assertEquals(1, allocations.size());
 assertEquals("Object", allocations.get(0).getClassName());
 }

 @Test
 public void testGetThreadTotalTime() {
 profile.init();
 Thread thread = new Thread(() -> {
 long start = System.nanoTime();
 profile.start("Main", "<init>");
 });
 thread.start();
 frame = new Frame(null, new Method("Main", "<init>"), Thread.currentThread().getId());
 frame.end("Main", "<init>");
 long threadId = Thread.currentThread().getId();
 long threadTotalTime = profile.getThreadTotalTime(threadId);
 assertEquals(frame.getNetTime(), threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 profile.init();
 Thread thread1 = new Thread(() -> {
 frame = new Frame(null, new Method("Main", "<init>"), Thread.currentThread().getId());
 profile.start("Main", "<init>");
 frame.end("Main", "<init>");
 });
 thread1.start();
 Thread thread2 = new Thread(() -> {
 frame = new Frame(null, new Method("Main", "<init>"), Thread.currentThread().getId());
 profile.start("Main", "<init>");
 frame.end("Main", "<init>");
 });
 thread2.start();
 List<Frame> frameList = new ArrayList<>(profile._frameList);
 Collections.sort(frameList, (f1, f2) -> Long.compare(f1.getBeginTime(), f2.getBeginTime()));
 List<Frame> sortedFrameList = profile.frameList();
 assertEquals(frameList, sortedFrameList);
 }

 @Test
 public void testStartEnd() {
 profile.init();
 Thread thread = new Thread(() -> {
 frame = new Frame(null, new Method("Main", "<init>"), Thread.currentThread().getId());
 profile.start("Main", "<init>");
 frame.end("Main", "<init>");
 });
 thread.start();
 List<Frame> interactions = profile.interactions(Thread.currentThread().getId());
 assertEquals(1, interactions.size());
 assertEquals(frame, interactions.get(0));
 }

 @Test
 public void testBeginWaitEndWait() {
 profile.init();
 Thread thread = new Thread(() -> {
 frame = new Frame(null, new Method("Main", "<init>"), Thread.currentThread().getId());
 profile.start("Main", "<init>");
 profile.beginWait("Main", "<init>");
 try {
 Thread.sleep(10);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 frame.endWait("Main", "<init>");
 frame.end("Main", "<init>");
 });
 thread.start();
 List<Frame> interactions = profile.interactions(Thread.currentThread().getId());
 assertEquals(2, interactions.size());
 Frame beginWaitFrame = interactions.get(1);
 Frame endWaitFrame = interactions.get(1);
 assertEquals(beginWaitFrame.getBeginTime(), endWaitFrame.getBeginTime());
 assertEquals(beginWaitFrame.getClassname(), endWaitFrame.getClassname());
 assertEquals(beginWaitFrame.getMethodname(), endWaitFrame.getMethodname());
 }

 @Test
 public void testUnwind() {
 profile.init();
 profile.start("Main", "<init>");
 profile.unwind("Main", "<init>", "Exception");
 Frame frame = profile.findFrame(Thread.currentThread().getId(), "Main", "<init>");
 assertTrue(frame.isException());
 }

 @Test(expected = IllegalStateException.class)
 public void testFindFrameNull() {
 profile.init();
 profile.findFrame(Thread.currentThread().getId(), "Main", "<init>");
 }

 @Test
 public void testFindFrameEmpty() {
 profile.init();
 profile.shutdown();
 Frame frame = profile.findFrame(Thread.currentThread().getId(), "Main", "<init>");
 assertNull(frame);
 }

 @Test
 public void testFindFrameWrongClassName() {
 profile.init();
 profile.start("Main", "<init>");
 Frame frame = profile.findFrame(Thread.currentThread().getId(), "WrongClassName", "<init>");
 assertNull(frame);
 }

 @Test
 public void testFindFrameWrongMethodName() {
 profile.init();
 profile.start("Main", "<init>");
 Frame frame = profile.findFrame(Thread.currentThread().getId(), "Main", "WrongMethodName");
 assertNull(frame);
 }

 @Test
 public void testFindFrameCorrect() {
 profile.init();
 profile.start("Main", "<init>");
 Frame frame = profile.findFrame(Thread.currentThread().getId(), "Main", "<init>");
 assertEquals(frame, profile._threadActiveFrame.get(Thread.currentThread().getId()));
 }

 @Test
 public void testAlloc() {
 profile.init();
 profile.alloc("Object");
 ClassAllocation ca = profile._allocList.get("Object");
 assertEquals(1, ca.getAllocCount());
 }

 @Test
 public void testAllocMultiple() {
 profile.init();
 profile.alloc("Object");
 profile.alloc("Object");
 ClassAllocation ca1 = profile._allocList.get("Object");
 ClassAllocation ca2 = profile._allocList.get("Object");
 assertEquals(2, ca1.getAllocCount());
 assertEquals(2, ca2.getAllocCount());
 }

 @Test
 public void testRun() {
 profile.init();
 profile.start("Main", "<init>");
 profile.shutdown();
 }
}
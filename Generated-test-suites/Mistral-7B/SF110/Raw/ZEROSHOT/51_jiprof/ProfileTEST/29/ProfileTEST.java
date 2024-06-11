package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

 private static final String CLASS_NAME = "com.example.TestClass";
 private static final String METHOD_NAME = "testMethod";

 @Before
 public void setUp() {
 Profile.init();
 Profile.initProfiler();
 }

 @After
 public void tearDown() {
 Profile.shutdown();
 }

 @Test
 public void testInit() {
 assertEquals(0, Profile._threadDictionary.size());
 assertEquals(0, Profile._frameList.size());
 assertEquals(0, Profile._threadActiveFrame.size());
 assertEquals(0, Profile._methodDictionary.size());
 }

 @Test
 public void testInitProfiler() {
 assertTrue(Controller._profile);
 }

 @Test
 public void testThreads() {
 Profile.start();
 Thread thread1 = new Thread(new Runnable() {
 @Override
 public void run() {
 start(CLASS_NAME, METHOD_NAME);
 Thread.yield();
 end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread1.start();
 Profile.start();
 Thread thread2 = new Thread(new Runnable() {
 @Override
 public void run() {
 start(CLASS_NAME, METHOD_NAME);
 Thread.yield();
 end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread2.start();

 List<Long> threads = Profile.threads();
 assertEquals(2, threads.size());

 Iterator<Long> threadsIterator = threads.iterator();
 Long threadId1 = threadsIterator.next();
 Long threadId2 = threadsIterator.next();

 Frame thread1Frame = Profile.findFrame(threadId1, CLASS_NAME, METHOD_NAME);
 Frame thread2Frame = Profile.findFrame(threadId2, CLASS_NAME, METHOD_NAME);

 assertEquals(thread1Frame.getThreadId(), threadId1);
 assertEquals(thread2Frame.getThreadId(), threadId2);
 }

 @Test
 public void testInteractions() {
 Profile.start();
 Thread thread1 = new Thread(new Runnable() {
 @Override
 public void run() {
 start(CLASS_NAME, METHOD_NAME);
 Thread.yield();
 end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread1.start();
 Profile.start();
 Thread thread2 = new Thread(new Runnable() {
 @Override
 public void run() {
 start(CLASS_NAME, METHOD_NAME);
 Thread.yield();
 end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread2.start();

 List<Frame> interactions1 = Profile.interactions(threadIdOf(thread1));
 List<Frame> interactions2 = Profile.interactions(threadIdOf(thread2));

 assertEquals(interactions1.size(), 2);
 assertEquals(interactions2.size(), 2);

 Frame interaction1 = interactions1.get(0);
 Frame interaction2 = interactions1.get(1);
 Frame interaction3 = interactions2.get(0);
 Frame interaction4 = interactions2.get(1);

 assertEquals(interaction1.getThreadId(), threadIdOf(thread1));
 assertEquals(interaction2.getThreadId(), threadIdOf(thread1));
 assertEquals(interaction3.getThreadId(), threadIdOf(thread2));
 assertEquals(interaction4.getThreadId(), threadIdOf(thread2));
 }

 @Test
 public void testAllocations() {
 Profile.start();
 start(CLASS_NAME, "<init>");
 end(CLASS_NAME, "<init>");

 List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
 assertEquals(1, allocations.size());
 ClassAllocation allocation = allocations.get(0);
 assertEquals(CLASS_NAME, allocation.getClassname());
 assertEquals(1, allocation.getAllocationCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 Profile.start();
 start(CLASS_NAME, METHOD_NAME);
 Thread.yield();
 end(CLASS_NAME, METHOD_NAME);

 long threadId = threadIdOf(Thread.currentThread());
 long threadTotalTime = Profile.getThreadTotalTime(threadId);

 assertEquals(threadTotalTime, 0);
 }

 @Test
 public void testSortFrameList() {
 List<Frame> frames = new ArrayList<>(Profile._frameList);
 Collections.sort(frames, (f1, f2) -> f1.getBeginTime() - f2.getBeginTime());

 List<Frame> sortedFrames = Profile._frameList;
 assertEquals(frames, sortedFrames);
 }

 @Test
 public void testStart() {
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(frame);
 }

 @Test
 public void testEnd() {
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(frame);
 frame.end();
 }

 @Test
 public void testBeginWait() {
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(frame);
 frame.beginWait();
 frame.endWait();
 }

 @Test(expected = java.lang.IllegalStateException.class)
 public void testUnwindWithNoFrame() {
 Profile.unwind(CLASS_NAME, METHOD_NAME, "ExceptionClass");
 }

 @Test
 public void testUnwind() {
 Frame frame1 = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(frame1);
 Profile.unwind(CLASS_NAME, METHOD_NAME, "ExceptionClass");
 Frame frame2 = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNull(frame2);
 }

 @Test
 public void testAlloc() {
 Class<?> clazz = Class.forName(CLASS_NAME);
 ClassAllocation allocation = Profile.findClassAllocation(clazz.getName());
 int initialAllocationCount = allocation.getAllocationCount();

Profile.alloc(CLASS_NAME);
 ClassAllocation newAllocation = Profile.findClassAllocation(clazz.getName());

 assertEquals(initialAllocationCount + 1, newAllocation.getAllocationCount());
 }

 private long threadIdOf(Thread thread) {
 return thread.getId();
 }
}
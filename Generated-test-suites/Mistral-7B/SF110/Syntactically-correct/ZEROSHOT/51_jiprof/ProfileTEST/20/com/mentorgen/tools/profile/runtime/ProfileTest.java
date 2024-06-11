package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.*;

import org.junit.*;
import org.junit.runner.*;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

@RunWith(JUnit4.class)
public class ProfileTest {

 private static final String CLASS_NAME = "com.example.TestClass";
 private static final String METHOD_NAME = "testMethod";

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 public static void shutdownProfiler() {
 Profile.shutdown();
 }

 @Test
 public void testInitAndClear() {
 Profile.init();
 Profile.clear();
 assertNull(Profile._threadDictionary);
 assertNull(Profile._methodDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._allocList);
 assertFalse(Profile._controllerThread.isAlive());
 }

 @Test
 public void testThreads() {
 Profile.init();
 Thread thread1 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 // Do some work here
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 Thread thread2 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 // Do some work here
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread1.start();
 thread2.start();
 List<Long> threads = Profile.threads();
 assertEquals(2, threads.size());
 thread1.join();
 thread2.join();
 }

 @Test
 public void testInteractions() {
 Profile.init();
 Thread thread = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 // Do some work here
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, METHOD_NAME);
 // Do some work here
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread.start();
 List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
 assertEquals(2, interactions.size());
 thread.join();
 }

 @Test
 public void testFrameList() {
 Profile.init();
 Thread thread = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 // Do some work here
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, "testMethod2");
 // Do some work here
 Profile.end(CLASS_NAME, "testMethod2");
 }
 });
 thread.start();
 List<Frame> frameList = Profile.frameList();
 assertEquals(3, frameList.size());
 thread.join();
 }

 @Test
 public void testAllocations() {
 Profile.init();
 Class<?> clazz = Class.forName(CLASS_NAME);
 Profile.alloc(CLASS_NAME);
 Profile.alloc(CLASS_NAME);
 List<ClassAllocation> allocations = new ArrayList<>(Profile.allocList().size());
 for (ClassAllocation allocation : Profile.allocList().values()) {
 if (allocation.getClassname().equals(CLASS_NAME)) {
 allocations.add(allocation);
 }
 }
 assertEquals(2, allocations.size());
 }

 @Test
 public void testThreadTotalTime() {
 Profile.init();
 Thread thread1 = new Thread(new Runnable() {
 @Override
 public void run() {
 long start = System.nanoTime();
 Profile.start(CLASS_NAME, METHOD_NAME);
 // Do some work for 1 second
 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 long end = System.nanoTime();
 Profile.end(CLASS_NAME, METHOD_NAME);
 long threadTotalTime = Profile.getThreadTotalTime(Thread.currentThread().getId());
 assertEquals(end - start, threadTotalTime);
 }
 });
 thread1.start();
 Thread thread2 = new Thread(new Runnable() {
 @Override
 public void run() {
 long start = System.nanoTime();
 Profile.start(CLASS_NAME, METHOD_NAME);
 // Do some work for 2 seconds
 try {
 Thread.sleep(2000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 long end = System.nanoTime();
 Profile.end(CLASS_NAME, METHOD_NAME);
 long threadTotalTime1 = Profile.getThreadTotalTime(Thread.currentThread().getId());
 assertEquals(end - start, threadTotalTime1);
 Profile.init(); // Clear the profiler data
 long threadTotalTime2 = Profile.getThreadTotalTime(Thread.currentThread().getId());
 assertEquals(0, threadTotalTime2);
 }
 });
 thread2.start();
 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testSortFrameList() {
 Profile.init();
 Thread thread = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 // Do some work here
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, "testMethod2");
 // Do some work here
 Profile.end(CLASS_NAME, "testMethod2");
 }
 });
 thread.start();
 List<Frame> frameList = new ArrayList<>(Profile.frameList());
 Collections.sort(frameList, Frame.COMPARATOR);
 List<Frame> sortedFrameList = Profile.frameList();
 assertArrayEquals(frameList.toArray(), sortedFrameList.toArray());
 thread.join();
 }
}
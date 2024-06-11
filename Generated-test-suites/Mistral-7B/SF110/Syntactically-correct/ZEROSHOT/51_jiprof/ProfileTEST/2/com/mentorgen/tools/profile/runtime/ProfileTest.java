package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.*;

import org.junit.*;
import org.mockito.*;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {

 private static final String CLASS_NAME = "com.example.ExampleClass";
 private static final String METHOD_NAME = "exampleMethod";
 private static final String FILENAME = "test.prof";

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 public static void shutdownProfiler() {
 Profile.shutdown();
 }

 @Before
 public void resetMocks() {
 reset(Controller.INSTANCE);
 }

 @Test
 public void testInit() {
 // Test initialization
 Profile.init();

 // Check that all instance variables are initialized
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 assertNull(Profile._controller);
 assertNull(Profile._controllerThread);
 }

 @Test
 public void testInitWithControllerThread() {
 // Test initialization with controller thread
 Profile.initProfiler();

 // Check that controller is initialized and controller thread is running
 assertNotNull(Profile._controller);
 assertTrue(Profile._controllerThread.isAlive());
 }

 @Test
 public void testClear() {
 // Test clear method
 Profile.clear();

 // Check that all instance variables are reset
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 assertNull(Profile._controller);
 assertNull(Profile._controllerThread);
 }

 @Test
 public void testStartAndStop() {
 // Test start and stop methods
 Profile.start();
 Profile.stop();

 // Check that controller is stopped
 verify(Controller.INSTANCE, times(1)).stop();
 }

 @Test
 public void testSetFileName() {
 // Test setFileName method
 Profile.setFileName(FILENAME);

 // Check that filename is set correctly in controller
 verify(Controller.INSTANCE, times(1)).setFileName(FILENAME);
 }

 @Test
 public void testThreads() {
 // Test threads method
 Profile.initProfiler();
 Thread thread1 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 Thread.sleep(50);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 Thread thread2 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread1.start();
 thread2.start();
 List<Long> threads = Profile.threads();

 // Check that both threads are in the list
 assertEquals(2, threads.size());
 assertTrue(threads.contains(Thread.currentThread().getId()));
 assertTrue(threads.contains(thread1.getId()));
 assertTrue(threads.contains(thread2.getId()));
 }

 @Test
 public void testInteractions() {
 // Test interactions method
 Profile.initProfiler();
 Thread thread = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 Thread.sleep(50);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread.start();
 List<Frame> interactions = Profile.interactions(thread.getId());

 // Check that there is exactly one interaction for the thread
 assertEquals(1, interactions.size());
 Frame interaction = interactions.get(0);
 // Check that interaction contains correct className and methodName
 assertEquals(CLASS_NAME, interaction.getClassName());
 assertEquals(METHOD_NAME, interaction.getMethodName());
 }

 @Test
 public void testFrameList() {
 // Test frameList method
 Profile.initProfiler();
 Thread thread1 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 Thread.sleep(50);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 Thread thread2 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread1.start();
 thread2.start();
 List<Frame> frameList = Profile.frameList();

 // Check that there are exactly two frames in the list
 assertEquals(2, frameList.size());
 // Check that both frames contain correct className and methodName
 Frame frame1 = frameList.get(0);
 assertEquals(CLASS_NAME, frame1.getClassName());
 assertEquals(METHOD_NAME, frame1.getMethodName());
 Frame frame2 = frameList.get(1);
 assertEquals(CLASS_NAME, frame2.getClassName());
 assertEquals(METHOD_NAME, frame2.getMethodName());
 }

 @Test
 public void testAllocations() {
 // Test allocations method
 Profile.initProfiler();
 Profile.alloc(CLASS_NAME);
 Profile.alloc(CLASS_NAME);
 List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());

 // Check that there is exactly one allocation for the className
 assertEquals(1, allocations.size());
 ClassAllocation allocation = allocations.get(0);
 // Check that allocation contains correct className and allocation count
 assertEquals(CLASS_NAME, allocation.getClassname());
 assertEquals(2, allocation.getAllocationCount());
 }

 @Test
 public void testThreadTotalTime() {
 // Test threadTotalTime method
 Profile.initProfiler();
 Thread thread1 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 Thread.sleep(50);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 Thread thread2 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread1.start();
 thread2.start();
 long thread1TotalTime = Profile.getThreadTotalTime(thread1.getId());
 long thread2TotalTime = Profile.getThreadTotalTime(thread2.getId());

 // Check that thread1 total time is less than thread2 total time
 assertTrue(thread1TotalTime < thread2TotalTime);
 }

 @Test
 public void testSortFrameList() {
 // Test sortFrameList method
 Profile.initProfiler();
 Thread thread1 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 Thread thread2 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 Thread.sleep(50);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 });
 thread1.start();
 thread2.start();
 Comparator<Frame> comparator = new Comparator<Frame>() {
 @Override
 public int compare(Frame f1, Frame f2) {
 return Long.compare(f1.getBeginTime(), f2.getBeginTime());
 }
 };
 Profile.sortFrameList(comparator);
 List<Frame> sortedFrameList = Profile.frameList();

 // Check that the first frame in the list has a smaller begin time than the second frame
 Frame firstFrame = sortedFrameList.get(0);
 Frame secondFrame = sortedFrameList.get(1);
 assertTrue(firstFrame.getBeginTime() < secondFrame.getBeginTime());
 }
}
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.*;

import org.junit.*;
import org.junit.rules.*;
import org.mockito.*;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {

 private static final String TEST_CLASS = "TestClass";
 private static final String TEST_METHOD = "testMethod";
 private static final String TEST_EXCEPTION = "TestException";
 private static final String TEST_FILENAME = "test.log";
 private static Controller mockController;
 private static Profile underTest;

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 }

 @Before
 public void setUp() {
 mockController = mock(Controller.class);
 underTest = new Profile();
 }

 @After
 public void tearDown() {
 underTest.shutdown();
 }

 @Test
 public void testInit() {
 // Check if init method initializes all static variables
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 assertNull(Profile._controller);
 assertNull(Profile._controllerThread);
 }

 @Test
 public void testClear() {
 // Check if clear method resets all static variables
 Profile.init();
 underTest.start(TEST_CLASS, TEST_METHOD);
 underTest.end(TEST_CLASS, TEST_METHOD);
 Profile.clear();
 assertNull(Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId()));
 assertFalse(Profile._frameList.isEmpty());
 }

 @Test
 public void testStartAndEnd() {
 // Test normal scenario
 underTest.start(TEST_CLASS, TEST_METHOD);
 underTest.end(TEST_CLASS, TEST_METHOD);

 // Check if correct frame is added and removed
 Frame threadFrame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId());
 assertEquals(TEST_CLASS, threadFrame.getClassName());
 assertEquals(TEST_METHOD, threadFrame.getMethodName());
 assertTrue(Profile._frameList.contains(threadFrame));

 // Test multiple calls
 underTest.start(TEST_CLASS, TEST_METHOD);
 underTest.end(TEST_CLASS, TEST_METHOD);
 threadFrame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId());
 assertEquals(TEST_CLASS, threadFrame.getClassName());
 assertEquals(TEST_METHOD, threadFrame.getMethodName());
 assertTrue(Profile._frameList.contains(threadFrame));

 // Test multiple threads
 Thread thread1 = new Thread(() -> underTest.start(TEST_CLASS, TEST_METHOD));
 Thread thread2 = new Thread(() -> underTest.start(TEST_CLASS, TEST_METHOD));
 thread1.start();
 thread2.start();
 thread1.join();
 thread2.join();
 threadFrame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId());
 Thread thread3 = new Thread(() -> underTest.end(TEST_CLASS, TEST_METHOD));
 thread3.start();
 thread3.join();
 threadFrame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId());
 assertTrue(threadFrame.isParentOf(Profile._threadDictionary.getMostRecentFrame(thread1.getId())));
 assertTrue(threadFrame.isParentOf(Profile._threadDictionary.getMostRecentFrame(thread2.getId())));
 }

 @Test
 public void testStartEndWithException() {
 // Test scenario with exception
 underTest.start(TEST_CLASS, TEST_METHOD);
 when(mockController.printStackTrace(any(PrintWriter.class))).thenCallRealMethod();
 doThrow(new TestException("Test exception")).when(mockController).setFileName(eq(TEST_FILENAME));
 try {
 underTest.end(TEST_CLASS, TEST_METHOD);
 fail("Expected TestException to be thrown");
 } catch (TestException e) {
 // Expected
 }

 // Check if correct frame is added and removed
 Frame threadFrame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId());
 assertEquals(TEST_CLASS, threadFrame.getClassName());
 assertEquals(TEST_METHOD, threadFrame.getMethodName());
 assertTrue(Profile._frameList.contains(threadFrame));
 }

 @Test
 public void testSortFrameList() {
 // Test sorting frame list
 Frame[] frames = new Frame[3];
 frames[0] = new Frame(null, new Method("Class1", "method1"), 1);
 frames[1] = new Frame(null, new Method("Class2", "method2"), 2);
 frames[2] = new Frame(null, new Method("Class1", "method1"), 1);
 Profile._frameList.addAll(Arrays.asList(frames));
 Profile.sortFrameList((frame1, frame2) -> Long.compare(frame1.getThreadId(), frame2.getThreadId()));
 List<Frame> sortedList = Profile._frameList;
 assertEquals(frames[1].getThreadId(), sortedList.get(1).getThreadId());
 assertEquals(frames[0].getThreadId(), sortedList.get(0).getThreadId());
 }

 @Test
 public void testAlloc() {
 // Test allocating and deallocating objects
 underTest.start(TEST_CLASS, TEST_METHOD);
 underTest.alloc(TEST_CLASS);
 underTest.end(TEST_CLASS, TEST_METHOD);
 ClassAllocation classAllocation = Profile._allocList.get(TEST_CLASS);
 assertEquals(1, classAllocation.getAllocCount());
 underTest.start(TEST_CLASS, TEST_METHOD);
 underTest.alloc(TEST_CLASS);
 underTest.alloc(TEST_CLASS);
 underTest.end(TEST_CLASS, TEST_METHOD);
 classAllocation = Profile._allocList.get(TEST_CLASS);
 assertEquals(3, classAllocation.getAllocCount());
 }
}
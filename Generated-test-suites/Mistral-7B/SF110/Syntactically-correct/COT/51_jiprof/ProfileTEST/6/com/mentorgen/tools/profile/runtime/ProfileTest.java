package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import com.mentorgen.tools.profile.*;
import java.util.*;
import org.junit.Test;

public class ProfileTest {

 private static Profile underTest;

 @BeforeClass
 public static void setUpClass() {
 Profile.init();
 underTest = new Profile();
 }

 @AfterClass
 public static void tearDownClass() {
 Profile.clear();
 }

 @Before
 public void setUp() {
 Profile.init();
 }

 @After
 public void tearDown() {
 Profile.clear();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 if (Profile._controllerThread != null) {
 assertTrue(Profile._controllerThread.isAlive());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testInitProfiler_AlreadyInitialized() {
 Profile.initProfiler();
 Profile.initProfiler();
 fail("Expected IllegalStateException");
 }

 @Test
 public void testInit() {
 Profile.init();
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertTrue(Profile._threadDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
 assertTrue(Profile._allocList.isEmpty());
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
 String fileName = "testProfile.txt";
 Profile.setFileName(fileName);
 assertEquals(fileName, Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 Profile.shutdown();
 assertTrue(Profile._controller.isClosed());
 }

 @Test
 public void testThreads() {
 Profile.start();
 Thread thread1 = new Thread(() -> {
 Profile.start("TestClass1", "testMethod1");
 });
 Thread thread2 = new Thread(() -> {
 Profile.start("TestClass2", "testMethod2");
 });
 thread1.start();
 thread2.start();
 List<Long> threads = Profile.threads();
 assertEquals(2, threads.size());
 long thread1Id = threads.get(0);
 long thread2Id = threads.get(1);
 Frame thread1Frame = findFrame(thread1Id, "TestClass1", "testMethod1");
 Frame thread2Frame = findFrame(thread2Id, "TestClass2", "testMethod2");
 assertNotNull(thread1Frame);
 assertNotNull(thread2Frame);
 }

 @Test
 public void testInteractions() {
 Profile.start("TestClass1", "testMethod1");
 List<Frame> interactions = Profile.interactions(1L);
 assertEquals(1, interactions.size());
 }

 @Test
 public void testFrameList() {
 Profile.start("TestClass1", "testMethod1");
 Profile.end("TestClass1", "testMethod1");
 List<Frame> frameList = Profile.frameList();
 assertEquals(1, frameList.size());
 }

 @Test
 public void testAllocations() {
 Profile.alloc("TestClass1");
 List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
 assertEquals(1, allocations.size());
 assertEquals("TestClass1", allocations.get(0).getClassName());
 }

 @Test
 public void testGetThreadTotalTime() {
 Profile.start("TestClass1", "testMethod1");
 Profile.end("TestClass1", "testMethod1");
 Profile.start("TestClass1", "testMethod1");
 Profile.end("TestClass1", "testMethod1");
 long threadId = 1L;
 long totalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(2 * 1_000_000L, totalTime); // Assuming nanoseconds are converted to milliseconds
 }

 @Test(expected = NoSuchElementException.class)
 public void testFindFrame_NullThreadId() {
 findFrame(null, "TestClass1", "testMethod1");
 fail("Expected NoSuchElementException");
 }

 @Test(expected = NoSuchElementException.class)
 public void testFindFrame_NullClassName() {
 Profile.start(Thread.currentThread().getId(), null);
 findFrame(Thread.currentThread().getId(), null, "testMethod1");
 fail("Expected NoSuchElementException");
 }

 @Test(expected = NoSuchElementException.class)
 public void testFindFrame_NullMethodName() {
 Profile.start(Thread.currentThread().getId(), "TestClass1");
 findFrame(Thread.currentThread().Id, "TestClass1", null);
 fail("Expected NoSuchElementException");
 }

 @Test(expected = NoSuchElementException.class)
 public void testFindFrame_NotFound() {
 Profile.start(Thread.currentThread().getId(), "TestClass1", "testMethod1");
 findFrame(Thread.currentThread().getId(), "TestClass2", "testMethod2");
 fail("Expected NoSuchElementException");
 }

 @Test
 public void testAlloc() {
 Profile.alloc("TestClass1");
 ClassAllocation allocation = Profile._allocList.get("TestClass1");
 assertEquals(1, allocation.getAllocCount());
 }

 @Test
 public void testBeginWait() {
 Profile.start("TestClass1", "testMethod1");
 Profile.beginWait("TestClass1", "testMethod1");
 Frame frame = findFrame(Thread.currentThread().getId(), "TestClass1", "testMethod1");
 assertTrue(frame.isWaiting());
 }

 @Test
 public void testEndWait() {
 Profile.start("TestClass1", "testMethod1");
 Profile.beginWait("TestClass1", "testMethod1");
 Profile.endWait("TestClass1", "testMethod1");
 Frame frame = findFrame(Thread.currentThread().getId(), "TestClass1", "testMethod1");
 assertFalse(frame.isWaiting());
 }

 @Test
 public void testUnwind() {
 try {
 throw new Exception();
 } catch (Exception e) {
 Profile.unwind(Thread.class.getName(), "run", e.getClass().getName());
 Frame frame = findFrame(Thread.currentThread().getId(), Thread.class.getName(), "run");
 assertEquals(e.getClass().getName(), frame.getException());
 }
 }

 private Frame findFrame(long threadId, String className, String methodName) {
 return Profile._threadActiveFrame.get(threadId) != null ? Profile._threadActiveFrame.get(threadId).getChild(new Method(className, methodName)) : null;
 }

}
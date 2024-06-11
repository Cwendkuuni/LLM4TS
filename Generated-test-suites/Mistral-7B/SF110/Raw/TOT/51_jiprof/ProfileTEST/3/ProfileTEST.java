package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.*;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;
import org.mockito.MockitoAnnotations;

public class ProfileTest {

 private static final String CLASS_NAME = "com.example.TestClass";
 private static final String METHOD_NAME = "testMethod";
 private static final String METHOD_NAME_2 = "anotherMethod";
 private static final String FILENAME = "test.log";
 private static final long THREAD_ID = 1L;
 private static final String EXCEPTION_CLASS_NAME = "java.lang.NullPointerException";
 private static final String EXCEPTION_MESSAGE = "Null pointer exception";

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 public static void shutdownProfiler() {
 Profile.shutdown();
 }

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 Profile._debugStart = true;
 }

 @After
 public void tearDown() {
 Profile._debugStart = false;
 }

 @Test
 public void testInitAndShutdown() {
 // Test init and shutdown when no threads are running
 Profile.init();
 Profile.shutdown();
 Profile.init();
 // Test init and shutdown during thread execution
 Thread thread = new Thread(() -> {
 Profile.start();
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.shutdown();
 });
 thread.start();
 }

 @Test
 public void testStartAndStop() {
 // Test start and stop with no methods called
 Profile.start();
 Profile.stop();
 // Test start and stop with methods called
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.stop();
 Profile.start(CLASS_NAME, METHOD_NAME_2);
 Profile.stop();
 }

 @Test
 public void testSetFileName() {
 // Test setting and retrieving the filename
 Profile.setFileName(FILENAME);
 assertEquals(FILENAME, Profile._controller.getFileName());
 }

 @Test
 public void testThreads() {
 // Test getting threads
 List<Long> threads = Profile.threads();
 assertEquals(1, threads.size());
 // Test adding and getting a thread
 Profile.start(CLASS_NAME, METHOD_NAME);
 threads = Profile.threads();
 assertEquals(1, threads.size());
 assertEquals(THREAD_ID, threads.get(0));
 // Test removing a thread
 Profile.shutdown();
 threads = Profile.threads();
 assertEquals(0, threads.size());
 }

 @Test
 public void testInteractions() {
 // Test getting interactions for a thread
 List<Frame> interactions = Profile.interactions(THREAD_ID);
 assertEquals(1, interactions.size());
 Frame interaction = interactions.get(0);
 assertEquals(CLASS_NAME, interaction.getClassName());
 assertEquals(METHOD_NAME, interaction.getMethodName());
 // Test getting interactions for a non-existent thread
 interactions = Profile.interactions(2L);
 assertEquals(0, interactions.size());
 }

 @Test
 public void testFrameList() {
 // Test getting the frame list
 List<Frame> frameList = Profile.frameList();
 assertEquals(0, frameList.size());
 // Test adding and getting a frame
 Profile.start(CLASS_NAME, METHOD_NAME);
 frameList = Profile.frameList();
 assertEquals(1, frameList.size());
 Frame frame = frameList.get(0);
 assertEquals(CLASS_NAME, frame.getClassName());
 assertEquals(METHOD_NAME, frame.getMethodName());
 // Test removing all frames
 Profile.shutdown();
 frameList = Profile.frameList();
 assertEquals(0, frameList.size());
 }

 @Test
 public void testAllocations() {
 // Test getting allocations
 List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
 assertEquals(0, allocations.size());
 // Test adding and getting an allocation
 Profile.alloc(CLASS_NAME);
 allocations = new ArrayList<>(Profile._allocList.values());
 assertEquals(1, allocations.size());
 ClassAllocation allocation = allocations.get(0);
 assertEquals(CLASS_NAME, allocation.getClassName());
 assertEquals(1, allocation.getAllocationCount());
 // Test adding the same allocation multiple times
 Profile.alloc(CLASS_NAME);
 allocations = new ArrayList<>(Profile._allocList.values());
 assertEquals(1, allocations.size());
 allocation = allocations.get(0);
 assertEquals(2, allocation.getAllocationCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 // Test getting the total time for a thread
 long totalTime = Profile.getThreadTotalTime(THREAD_ID);
 assertEquals(0, totalTime);
 // Test adding and getting time for a thread
 Profile.start(CLASS_NAME, METHOD_NAME);
 Thread.yield();
 Thread.yield();
 long startTime = System.nanoTime();
 Profile.end(CLASS_NAME, METHOD_NAME);
 totalTime = Profile.getThreadTotalTime(THREAD_ID);
 assertTrue(totalTime > startTime);
 }

 @Test
 public void testSortFrameList() {
 // Test sorting the frame list
 List<Frame> frameList = new ArrayList<>(Arrays.asList(
 new Frame(null, new Method("TestClass", "testMethod"), THREAD_ID),
 new Frame(null, new Method("TestClass", "anotherMethod"), THREAD_ID),
 new Frame(null, new Method("TestClass", "testMethod"), THREAD_ID)
 ));
 Profile._frameList = frameList;
Comparator<Frame> comp = (f1, f2) -> f1.getBeginTime() - f2.getBeginTime();
Profile.sortFrameList(comp);
List<Frame> sortedFrameList = new ArrayList<>(Profile._frameList);
assertEquals(3, sortedFrameList.size());
Frame firstFrame = sortedFrameList.get(0);
assertEquals(CLASS_NAME, firstFrame.getClassName());
assertEquals(METHOD_NAME, firstFrame.getMethodName());
Frame secondFrame = sortedFrameList.get(1);
assertEquals(CLASS_NAME, secondFrame.getClassName());
assertEquals(METHOD_NAME_2, secondFrame.getMethodName());
Frame thirdFrame = sortedFrameList.get(2);
assertEquals(CLASS_NAME, thirdFrame.getClassName());
assertEquals(METHOD_NAME, thirdFrame.getMethodName());
}

 @Test
 public void testStartEndMethodsWithExceptions() {
 // Test starting and ending methods with exceptions
 Profile._debugException = true;
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 throw new NullPointerException(EXCEPTION_MESSAGE);
 } catch (NullPointerException e) {
 Profile.unwind(CLASS_NAME, METHOD_NAME, EXCEPTION_CLASS_NAME);
 }
 Profile.end(CLASS_NAME, METHOD_NAME);
 }

 @Test
 public void testBeginWaitEndWait() {
 // Test beginning and ending waits
 Profile.beginWait(CLASS_NAME, METHOD_NAME);
 Thread.yield();
 Thread.yield();
 long startTime = System.nanoTime();
 Profile.endWait(CLASS_NAME, METHOD_NAME);
 long waitTime = Profile.getThreadTotalTime(THREAD_ID) - startTime;
 assertTrue(waitTime > 0);
 }
}
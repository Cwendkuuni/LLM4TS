package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class ProfileTest {

 private static final String CLASS_NAME = "com.example.TestClass";
 private static final String METHOD_NAME = "testMethod";
 private static final String FILENAME = "test.prof";
 private static final long THREAD_ID = 1L;

 @BeforeClass
 static public void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 static public void shutdownProfiler() {
 Profile.shutdown();
 }

 @Before
 static public void setUp() {
 Profile._debugStart = true;
 Profile._debugException = true;
 }

 @After
 static public void tearDown() {
 Profile._debugStart = false;
 Profile._debugException = false;
 }

 @Test
 public void testInit() {
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
 public void testInitAndAddShutdownHook() {
 Runtime runtime = Mockito.mock(Runtime.class);
 assertFalse(runtime.getClass().getProtectionDomain().getCodeSource().isSealed());
 Profile.init();
 Profile.initProfiler();
 Mockito.verify(runtime).addShutdownHook(ArgumentMatchers.isA(Profile.class));
 }

 @Test
 public void testClear() {
 Profile.init();
 Profile.clear();
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 }

 @Test
 public void testStartAndStop() {
 Profile.start();
 Profile.stop();
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName(FILENAME);
 assertEquals(FILENAME, Profile._controller.getFileName());
 }

 @Test
 public void testThreads() {
 Profile._threadDictionary.add(THREAD_ID, new Frame(null, null, THREAD_ID));
 List<Long> threads = Profile.threads();
 assertEquals(1, threads.size());
 assertEquals(THREAD_ID, threads.get(0));
 }

 @Test
 public void testInteractions() {
 Frame frame = new Frame(null, null, THREAD_ID);
 Profile._threadDictionary.add(THREAD_ID, frame);
 List<Frame> interactions = Profile.interactions(THREAD_ID);
 assertEquals(1, interactions.size());
 assertEquals(frame, interactions.get(0));
 }

 @Test
 public void testFrameList() {
 Frame frame1 = new Frame(null, null, THREAD_ID);
 Frame frame2 = new Frame(frame1, null, THREAD_ID);
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 List<Frame> frameList = Profile.frameList();
 assertEquals(2, frameList.size());
 }

 @Test
 public void testAllocations() {
 Set<String> allocations = new HashSet<>();
 allocations.add("com.example.TestClass");
 Profile._allocList.put("com.example.TestClass", new ClassAllocation("com.example.TestClass"));
 Iterable<ClassAllocation> allocationsIterable = Profile.allocations();
 assertEquals(1, allocationsIterable.spliterator().getExactSizeIfKnown());
 ClassAllocation classAllocation = allocationsIterable.iterator().next();
 assertEquals("com.example.TestClass", classAllocation.getClassName());
 }

 @Test
 public void testGetThreadTotalTime() {
 Frame frame = new Frame(null, null, THREAD_ID);
 long threadTotalTime = 100;
 frame.setThreadTotalTime(threadTotalTime);
 Profile._threadDictionary.add(THREAD_ID, frame);
 long threadTotalTimeResult = Profile.getThreadTotalTime(THREAD_ID);
 assertEquals(threadTotalTime, threadTotalTimeResult);
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testFindFrameNull() {
 Frame result = Profile.findFrame(THREAD_ID, CLASS_NAME, METHOD_NAME);
 assertNull(result);
 }

 @Test
 public void testStartEnd() {
 Class clazz = Mockito.mock(Class.class);
 Method method = Mockito.mock(Method.class);
 when(clazz.getName()).thenReturn(CLASS_NAME);
 when(method.getName()).thenReturn(METHOD_NAME);
 when(method.toString()).thenReturn(METHOD_NAME + "(" + CLASS_NAME + ")");
 Profile.start(CLASS_NAME, METHOD_NAME);
 Frame parentFrame = new Frame(null, null, THREAD_ID);
 Frame childFrame = new Frame(parentFrame, method, THREAD_ID);
 Profile._threadDictionary.add(THREAD_ID, parentFrame);
 Profile._frameList.add(childFrame);
 Profile._threadActiveFrame.put(THREAD_ID, childFrame);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Frame resultFrame = Profile.findFrame(THREAD_ID, CLASS_NAME, METHOD_NAME);
 assertNotNull(resultFrame);
 verify(parentFrame).addChild(ArgumentMatchers.eq(method), ArgumentMatchers.eq(childFrame));
 }

 @Test
 public void testBeginWaitEndWait() {
 Frame parentFrame = new Frame(null, null, THREAD_ID);
 Frame childFrame = new Frame(parentFrame, null, THREAD_ID);
 Profile._threadDictionary.add(THREAD_ID, parentFrame);
 Profile._frameList.add(childFrame);
 Profile._threadActiveFrame.put(THREAD_ID, childFrame);
 Profile.beginWait(CLASS_NAME, METHOD_NAME);
 childFrame.beginWait(System.nanoTime());
 Profile.endWait(CLASS_NAME, METHOD_NAME);
 childFrame.endWait(System.nanoTime());
 Frame resultFrame = Profile.findFrame(THREAD_ID, CLASS_NAME, METHOD_NAME);
 assertNotNull(resultFrame);
 verify(childFrame).setEndWaitTime(ArgumentMatchers.eq(System.nanoTime()));
 }

 @Test
 public void testUnwind() {
 Frame parentFrame = new Frame(null, null, THREAD_ID);
 Frame childFrame = new Frame(parentFrame, null, THREAD_ID);
 Frame grandChildFrame = new Frame(childFrame, null, THREAD_ID);
 Profile._threadDictionary.add(THREAD_ID, parentFrame);
 Profile._frameList.add(childFrame);
 Profile._frameList.add(grandChildFrame);
 Profile._threadActiveFrame.put(THREAD_ID, grandChildFrame);
 Profile.unwind(CLASS_NAME, METHOD_NAME, "TestException");
 Frame resultFrame = Profile.findFrame(THREAD_ID, CLASS_NAME, METHOD_NAME);
 assertNotNull(resultFrame);
 verify(grandChildFrame).setEndTime(ArgumentMatchers.eq(System.nanoTime()));
 }

 @Test
 public void testAlloc() {
 Frame frame = new Frame(null, null, THREAD_ID);
 Profile._threadActiveFrame.put(THREAD_ID, frame);
 ClassAllocation classAllocation = new ClassAllocation("com.example.TestClass");
 Profile.alloc(CLASS_NAME);
 verify(classAllocation).incAllocCount();
 }

}
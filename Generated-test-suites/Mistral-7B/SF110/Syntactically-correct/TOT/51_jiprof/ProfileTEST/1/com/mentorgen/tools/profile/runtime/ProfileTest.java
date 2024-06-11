package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.*;
import org.junit.rules.TestName;
import org.mockito.MockitoAnnotations;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;
import com.mentorgen.tools.profile.runtime.*;

public class ProfileTest {

 private static final String TEST_CLASS_NAME = "TestClass";
 private static final String TEST_METHOD_NAME = "testMethod";
 private static final String EXCEPTION_CLASS_NAME = "TestException";
 private static final String EXCEPTION_METHOD_NAME = "testException";

 @Rule
 public TestName testName = new TestName();

 private Profile _profile;
 private Controller _controller;
 private Thread _controllerThread;
 private ThreadDictionary _threadDictionary;
 private Map<Long, Frame> _threadActiveFrame;
 private Map<String, Method> _methodDictionary;
 private Object _lock;
 private Map<String, ClassAllocation> _allocList;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 _profile = new Profile();
 _controller = mock(Controller.class);
 _controllerThread = mock(Thread.class);
 _threadDictionary = new ThreadDictionary();
 _threadActiveFrame = new HashMap<>();
 _methodDictionary = new HashMap<>();
 _lock = new Object();
 _allocList = new HashMap<>();
 Profile.init();
 }

 @After
 public void teardown() {
 _profile = null;
 _controller = null;
 _controllerThread = null;
 _threadDictionary = null;
 _threadActiveFrame = null;
 _methodDictionary = null;
 _lock = null;
 _allocList = null;
 }

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 public static void shutdownProfiler() {
 Profile.shutdown();
 }

 @Test
 public void testThreads() {
 Thread thread1 = new Thread();
 Thread thread2 = new Thread();
 Thread thread3 = new Thread();
 _threadDictionary.add(thread1.getId(), new Frame(null, null, thread1.getId()));
 _threadDictionary.add(thread2.getId(), new Frame(null, null, thread2.getId()));
 _threadDictionary.add(thread3.getId(), new Frame(null, null, thread3.getId()));

 List<Long> threads = Profile.threads();
 assertEquals(3, threads.size());
 assertTrue(threads.contains(thread1.getId()));
 assertTrue(threads.contains(thread2.getId()));
 assertTrue(threads.contains(thread3.getId()));
 }

 @Test
 public void testInteractions() {
 Frame frame1 = new Frame(null, null, Thread.currentThread().getId());
 Frame frame2 = new Frame(null, null, Thread.currentThread().getId());
 Frame frame3 = new Frame(null, null, Thread.currentThread().getId());
 _threadActiveFrame.put(Thread.currentThread().getId(), frame1);
 _threadActiveFrame.put(Thread.currentThread().getId(), frame2);
 _threadActiveFrame.put(Thread.currentThread().getId(), frame3);

 Iterable<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
 assertEquals(3, Iterators.size(interactions));
 assertTrue(interactions.iterator().next().equals(frame3));
 }

 @Test
 public void testFrameList() {
 Frame frame1 = new Frame(null, null, Thread.currentThread().getId());
 Frame frame2 = new Frame(null, null, Thread.currentThread().getId());
 Frame frame3 = new Frame(null, null, Thread.currentThread().getId());
 _threadActiveFrame.put(Thread.currentThread().getId(), frame1);
 _threadActiveFrame.put(Thread.currentThread().getId(), frame2);
 _threadActiveFrame.put(Thread.currentThread().getId(), frame3);

 Iterable<Frame> frameList = Profile.frameList();
 assertEquals(3, Iterators.size(frameList));
 }

 @Test
 public void testAllocations() {
 ClassAllocation classAllocation = new ClassAllocation("TestClass");
 _allocList.put("TestClass", classAllocation);

 Iterable<ClassAllocation> allocations = Profile.allocations();
 assertEquals(1, Iterators.size(allocations));
 assertEquals("TestClass", allocations.iterator().next().getName());
 }

 @Test
 public void testGetThreadTotalTime() {
 Frame frame = new Frame(null, null, Thread.currentThread().getId());
 _threadActiveFrame.put(Thread.currentThread().getId(), frame);
 long startTime = System.nanoTime();
 _threadActiveFrame.put(Thread.currentThread().getId(), frame);

 long threadId = Thread.currentThread().getId();
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(0, threadTotalTime);

 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 threadTotalTime = Profile.getThreadTotalTime(threadId);
 long expectedThreadTotalTime = System.nanoTime() - startTime;
 assertEquals(expectedThreadTotalTime, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 Frame frame1 = new Frame(null, null, Thread.currentThread().getId());
 Frame frame2 = new Frame(null, null, Thread.currentThread().getId());
 Frame frame3 = new Frame(null, null, Thread.currentThread().getId());
 Frame frame4 = new Frame(null, null, Thread.currentThread().getId());
 _threadActiveFrame.put(Thread.currentThread().getId(), frame1);
 _threadActiveFrame.put(Thread.currentThread().getId(), frame2);
 _threadActiveFrame.put(Thread.currentThread().getId(), frame3);
 _threadActiveFrame.put(Thread.currentThread().getId(), frame4);

 Comparator<Frame> comparator = (f1, f2) -> f1.getBeginTime() - f2.getBeginTime();
 Profile.sortFrameList(comparator);

 Iterator<Frame> frameIterator = Profile.frameList().iterator();
 Frame currentFrame = frameIterator.next();
 Frame previousFrame = null;
 while (frameIterator.hasNext()) {
 Frame nextFrame = frameIterator.next();
 assertTrue(currentFrame.getBeginTime() < nextFrame.getBeginTime());
 currentFrame = nextFrame;
 previousFrame = currentFrame;
 }
 assertEquals(previousFrame, frame1);
 }

 @Test
 public void testStartAndEnd() {
 Frame parentFrame = new Frame(null, null, Thread.currentThread().getId());
 Frame targetFrame = new Frame(parentFrame, new Method(TEST_CLASS_NAME, TEST_METHOD_NAME), Thread.currentThread().getId());
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 _threadActiveFrame.put(Thread.currentThread().getId(), targetFrame);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);

 Frame actualFrame = _threadActiveFrame.get(Thread.currentThread().getId());
 Frame expectedFrame = targetFrame;
 assertEquals(expectedFrame, actualFrame);
 }

 @Test
 public void testBeginWaitAndEndWait() {
 Frame parentFrame = new Frame(null, null, Thread.currentThread().getId());
 Frame targetFrame = new Frame(parentFrame, new Method(TEST_CLASS_NAME, TEST_METHOD_NAME), Thread.currentThread().getId());
 _threadActiveFrame.put(Thread.currentThread().getId(), targetFrame);

 Profile.beginWait(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.endWait(TEST_CLASS_NAME, TEST_METHOD_NAME);

 Frame actualFrame = _threadActiveFrame.get(Thread.currentThread().getId());
 Frame expectedFrame = targetFrame;
 assertEquals(expectedFrame, actualFrame);
 }

 @Test
 public void testUnwind() {
 Frame parentFrame = new Frame(null, null, Thread.currentThread().getId());
 Frame targetFrame = new Frame(parentFrame, new Method(TEST_CLASS_NAME, TEST_METHOD_NAME), Thread.currentThread().getId());
 Frame exceptionFrame = new Frame(targetFrame, new Method(EXCEPTION_CLASS_NAME, EXCEPTION_METHOD_NAME), Thread.currentThread().getId());
 _threadActiveFrame.put(Thread.currentThread().getId(), targetFrame);
 _threadActiveFrame.put(Thread.currentThread().getId(), exceptionFrame);

 Profile.unwind(TEST_CLASS_NAME, TEST_METHOD_NAME, EXCEPTION_CLASS_NAME);
 Frame actualFrame = _threadActiveFrame.get(Thread.currentThread().getId());
 Frame expectedFrame = targetFrame;
 assertEquals(expectedFrame, actualFrame);
 }

 @Test
 public void testAlloc() {
 Frame frame = new Frame(new Method(TEST_CLASS_NAME, "<init>"), Thread.currentThread().getId());
 _threadActiveFrame.put(Thread.currentThread().getId(), frame);
 Profile.alloc(TEST_CLASS_NAME);
 ClassAllocation classAllocation = _allocList.get("TestClass");
 assertEquals(1, classAllocation.getAllocCount());
 }
}
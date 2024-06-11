package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {

 private static final String CLASS_NAME = "TestClass";
 private static final String METHOD_NAME = "testMethod";
 private static final String EXCEPTION_CLASS_NAME = "TestException";
 private static final String FILENAME = "test.prof";
 private static final int NUM_THREADS = 5;
 private Profile underTest;
 private Controller mockController;
 private ThreadDictionary mockThreadDictionary;
 private Map<Long, Frame> mockThreadActiveFrame;
 private Map<String, Method> mockMethodDictionary;
 private Map<String, ClassAllocation> mockAllocList;
 private Object lock;

 @Before
 public void setUp() {
 initMocks();
 initProfile();
 underTest = new Profile();
 }

 @After
 public void tearDown() {
 reset(mockController, mockThreadDictionary, mockThreadActiveFrame, mockMethodDictionary, mockAllocList);
 }

 @Test
 public void testInitProfiler() {
 verify(mockController).start();
 verify(mockController).setFileName(eq(FILENAME));
 }

 @Test
 public void testInit() {
 assertNotNull(underTest._threadActiveFrame);
 assertNotNull(underTest._threadDictionary);
 assertNotNull(underTest._methodDictionary);
 assertNotNull(underTest._frameList);
 assertNotNull(underTest._lock);
 assertNotNull(underTest._allocList);
 }

 @Test
 public void testClear() {
 underTest.clear();
 assertNotNull(underTest._threadActiveFrame);
 assertNotNull(underTest._threadDictionary);
 assertNotNull(underTest._methodDictionary);
 assertNotNull(underTest._frameList);
 assertNotNull(underTest._lock);
 assertNotNull(underTest._allocList);
 assertEquals(0, underTest._threadDictionary.size());
 assertEquals(0, underTest._frameList.size());
 }

 @Test
 public void testStart() {
 underTest.start();
 verify(mockController).start();
 }

 @Test(expected = IllegalStateException.class)
 public void testStartAlreadyStarted() {
 underTest.start();
 underTest.start();
 }

 @Test
 public void testStop() {
 underTest.stop();
 verify(mockController).stop();
 }

 @Test
 public void testSetFileName() {
 underTest.setFileName(FILENAME);
 verify(mockController).setFileName(eq(FILENAME));
 }

 @Test(expected = IllegalStateException.class)
 public void testSetFileNameWhileRunning() {
 underTest.start();
 underTest.setFileName(FILENAME);
 }

 @Test
 public void testShutdown() throws IOException {
 underTest.shutdown();
 verify(mockController).close();
 }

 @Test
 public void testThreads() {
 assertEquals(NUM_THREADS, underTest.threads().spliterator().estimateSize());
 }

 @Test
 public void testInteractions() {
 List<Frame> interactions = underTest.interactions(1L);
 assertEquals(1, interactions.size());
 }

 @Test
 public void testFrameList() {
 assertEquals(0, underTest.frameList().size());
 }

 @Test
 public void testGetThreadTotalTime() {
 Thread localThread = Thread.currentThread();
 long threadId = localThread.getId();
 Frame testFrame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), threadId);
 underTest._threadDictionary.add(threadId, testFrame);
 underTest._threadActiveFrame.put(threadId, testFrame);
 underTest.start(CLASS_NAME, METHOD_NAME);
 underTest.end(CLASS_NAME, METHOD_NAME);
 long threadTotalTime = underTest.getThreadTotalTime(threadId);
 assertEquals(0, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), 1L);
 Frame frame2 = new Frame(null, new Method(CLASS_NAME, "testMethod2"), 2L);
 Frame frame3 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), 1L);
 List<Frame> frameList = new ArrayList<>();
 frameList.add(frame1);
 frameList.add(frame2);
 frameList.add(frame3);
 underTest._frameList = frameList;
 Profile.compare(Mockito.mock(Comparator.class), underTest._frameList);
 List<Frame> sortedFrameList = underTest.frameList();
 assertEquals(frame1.getBeginTime(), sortedFrameList.get(0).getBeginTime());
 assertEquals(frame2.getBeginTime(), sortedFrameList.get(1).getBeginTime());
 assertEquals(frame3.getBeginTime(), sortedFrameList.get(2).getBeginTime());
 }

 @Test
 public void testStartEnd() {
 underTest.start(CLASS_NAME, METHOD_NAME);
 underTest.end(CLASS_NAME, METHOD_NAME);
 Frame frame = underTest.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(frame);
 assertTrue(frame.isRunning());
 }

 @Test
 public void testBeginWaitEndWait() {
 underTest.start(CLASS_NAME, METHOD_NAME);
 underTest.beginWait(CLASS_NAME, METHOD_NAME);
 underTest.endWait(CLASS_NAME, METHOD_NAME);
 Frame frame = underTest.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(frame);
 assertTrue(frame.isWaiting());
 }

 @Test
 public void testUnwind() {
 underTest.start(CLASS_NAME, METHOD_NAME);
 underTest.unwind(CLASS_NAME, METHOD_NAME, EXCEPTION_CLASS_NAME);
 Frame frame = underTest.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(frame);
 assertFalse(frame.isRunning());
 assertFalse(frame.isWaiting());
 }

 @Test
 public void testAlloc() {
 underTest.alloc(CLASS_NAME);
 ClassAllocation ca = underTest._allocList.get(CLASS_NAME);
 assertEquals(1, ca.getAllocCount());
 }

 @Test
 public void testRun() throws IOException {
 underTest.run();
 verify(mockController).close();
 }

 private void initMocks() {
 mockController = Mockito.mock(Controller.class);
 mockThreadDictionary = Mockito.mock(ThreadDictionary.class);
 mockThreadActiveFrame = new HashMap<>();
 mockMethodDictionary = new HashMap<>();
 mockAllocList = new HashMap<>();
 lock = new Object();
 }

 private Frame findFrame(long threadId, String className, String methodName) {
 Frame frame = underTest._threadActiveFrame.get(threadId);
 while (frame != null) {
 if (frame.getClassName().equals(className) && frame.getMethodName().equals(methodName)) {
 return frame;
 }
 frame = frame.getParent();
 }
 return null;
 }

}
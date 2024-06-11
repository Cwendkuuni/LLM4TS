package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import com.mentorgen.tools.profile.*;
import java.util.*;
import org.junit.Test;

public class ProfileTest {

 private static final String TEST_CLASS_NAME = "com.example.TestClass";
 private static final String TEST_METHOD_NAME = "testMethod";

 @BeforeClass
 static public void setupClass() {
 Profile.init();
 }

 @AfterClass
 static public void tearDownClass() {
 Profile.clear();
 }

 @Before
 static public void setup() {
 Profile.initProfiler();
 }

 @After
 static public void tearDown() {
 Profile.shutdown();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertNull(Profile._controller);
 }

 @Test
 public void testInit() {
 Profile.init();
 assertNotNull(Profile._threadDictionary);
 }

 @Test
 public void testThreads() {
 List<Long> threads = Profile.threads();
 assertFalse(threads.isEmpty());
 }

 @Test
 public void testInteractions() {
 List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
 assertFalse(interactions.isEmpty());
 }

 @Test
 public void testFrameList() {
 List<Frame> frameList = Profile.frameList();
 assertFalse(frameList.isEmpty());
 }

 @Test
 public void testGetThreadTotalTime() {
 long threadId = Thread.currentThread().getId();
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assert(threadTotalTime > 0);
 }

 @Test
 public void testSortFrameList() {
 List<Frame> frameList = Profile.frameList();
 Collections.sort(frameList, new Comparator<Frame>() {
 @Override
 public int compare(Frame f1, Frame f2) {
 return Long.compare(f1.getBeginTime(), f2.getBeginTime());
 }
 });
 }

 @Test
 public void testStart() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Frame frame = findFrameByName(TEST_CLASS_NAME, TEST_METHOD_NAME);
 assertNotNull(frame);
 }

 @Test(expected = AssertionError.class)
 public void testStartTwice() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 fail("Expected an AssertionError");
 }

 @Test
 public void testEnd() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Frame frame = findFrameByName(TEST_CLASS_NAME, TEST_METHOD_NAME);
 assertNotNull(frame);
 }

 @Test(expected = AssertionError.class)
 public void testEndWithoutStart() {
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 fail("Expected an AssertionError");
 }

 @Test
 public void testBeginWait() {
 Profile.beginWait(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Frame frame = findFrameByName(TEST_CLASS_NAME, TEST_METHOD_NAME);
 assertNotNull(frame);
 }

 @Test(expected = AssertionError.class)
 public void testEndWaitWithoutBeginWait() {
 Profile.endWait(TEST_CLASS_NAME, TEST_METHOD_NAME);
 fail("Expected an AssertionError");
 }

 @Test
 public void testUnwind() {
 try {
 throw new Exception();
 } catch (Exception e) {
 Profile.unwind(TEST_CLASS_NAME, TEST_METHOD_NAME, e.getClass().getName());
 Frame frame = findFrameByName(TEST_CLASS_NAME, TEST_METHOD_NAME);
 assertNotNull(frame);
 }
 }

 @Test(expected = AssertionError.class)
 public void testUnwindWithoutException() {
 Profile.unwind(TEST_CLASS_NAME, TEST_METHOD_NAME, "ExceptionClass");
 fail("Expected an AssertionError");
 }

 @Test
 public void testAlloc() {
 Profile.alloc(TEST_CLASS_NAME);
 ClassAllocation classAllocation = Profile._allocList.get(TEST_CLASS_NAME);
 assertNotNull(classAllocation);
 }

 private Frame findFrameByName(String className, String methodName) {
 Frame frame = null;
 for (Frame f : Profile.frameList()) {
 if (f.getClassName().equals(className) && f.getMethodName().equals(methodName)) {
 frame = f;
 break;
 }
 }
 return frame;
}
}
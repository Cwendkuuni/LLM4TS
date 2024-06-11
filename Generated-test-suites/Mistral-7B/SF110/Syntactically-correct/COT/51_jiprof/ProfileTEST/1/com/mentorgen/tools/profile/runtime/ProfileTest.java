package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {

 private static Profile underTest;

 @BeforeClass
 public static void setupClass() {
 Profile.init();
 underTest = new Profile();
 }

 @AfterClass
 public static void tearDownClass() {
 Profile.clear();
 }

 @Before
 public void setUp() {
 underTest = new Profile();
 }

 @After
 public void tearDown() {
 underTest = null;
 }

 // Test init method
 @Test
 public void testInit() {
 assertTrue(Profile._threadDictionary != null);
 assertTrue(Profile._frameList != null);
 assertTrue(Profile._threadActiveFrame != null);
 assertTrue(Profile._methodDictionary != null);
 assertTrue(Profile._allocList != null);
 }

 // Test initProfiler method
 @Test
 public void testInitProfiler() {
 assertFalse(Profile._controllerThread.isAlive());
 assertFalse(Controller._profile);
 Profile.initProfiler();
 assertTrue(Controller._profile);
 assertTrue(Profile._controllerThread.isAlive());
 }

 // Test clear method
 @Test
 public void testClear() {
 assertFalse(Profile._threadDictionary.isEmpty());
 Profile.clear();
 assertTrue(Profile._threadDictionary.isEmpty());
 }

 // Test threads method
 @Test
 public void testThreads() {
 assertFalse(Profile._threadDictionary.threads().isEmpty());
 }

 // Test interactions method with valid thread ID
 @Test
 public void testInteractionsValidThreadId() {
 long threadId = 1L;
 Iterable<Frame> interactions = Profile.interactions(threadId);
 assertFalse(interactions.isEmpty());
 }

 // Test interactions method with invalid thread ID
 @Test(expected = UnsupportedOperationException.class)
 public void testInteractionsInvalidThreadId() {
 long threadId = Long.MIN_VALUE;
 Iterable<Frame> interactions = Profile.interactions(threadId);
 }

 // Test frameList method
 @Test
 public void testFrameList() {
 assertFalse(Profile._frameList.isEmpty());
 }

 // Test getThreadTotalTime method with valid thread ID
 @Test
 public void testGetThreadTotalTimeValidThreadId() {
 long threadId = 1L;
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertGreaterThan(threadTotalTime, 0L);
 }

 // Test getThreadTotalTime method with invalid thread ID
 @Test(expected = IllegalArgumentException.class)
 public void testGetThreadTotalTimeInvalidThreadId() {
 long threadId = Long.MIN_VALUE;
 Profile.getThreadTotalTime(threadId);
 }

 // Test sortFrameList method
 @Test
 public void testSortFrameList() {
 List<Frame> frameListCopy = new ArrayList<>(Profile._frameList);
 Collections.sort(frameListCopy);
 Profile.sortFrameList((Frame f1, Frame f2) -> f1.getBeginTime() - f2.getBeginTime());
 List<Frame> sortedFrameList = new ArrayList<>(Profile._frameList);
 for (int i = 0; i < frameListCopy.size(); i++) {
 Frame currentFrame = frameListCopy.get(i);
 Frame testFrame = sortedFrameList.get(i);
 assertEquals(currentFrame.getBeginTime(), testFrame.getBeginTime());
 }
 }

 // Test start method with valid className and methodName
 @Test
 public void testStartValidClassNameAndMethodName() {
 String className = "com.mentorgen.tools.profile.runtime.Profile";
 String methodName = "testStartValidClassNameAndMethodName";
 Profile.start(className, methodName);
 Frame frame = findFrameWithClassNameAndMethodName(className, methodName);
 assertNotNull(frame);
 }

 // Test start method with invalid className or methodName
 @Test(expected = IllegalArgumentException.class)
 public void testStartInvalidClassNameOrMethodName() {
 String className = "com.mentorgen.tools.profile.runtime.Profile";
 String methodName = "";
 Profile.start(className, methodName);
 }

 // Test start method with already running frame
 @Test
 public void testStartAlreadyRunningFrame() {
 String className = "com.mentorgen.tools.profile.runtime.Profile";
 String methodName = "testStartValidClassNameAndMethodName";
 Profile.start(className, methodName);
 Profile.start(className, methodName);
 Frame frame = findFrameWithClassNameAndMethodName(className, methodName);
 assertEquals(1, frame.getChildren().size());
 }

 // Test end method with valid className and methodName
 @Test
 public void testEndValidClassNameAndMethodName() {
 String className = "com.mentorgen.tools.profile.runtime.Profile";
 String methodName = "testEndValidClassNameAndMethodName";
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 Frame frame = findFrameWithClassNameAndMethodName(className, methodName);
 assertNotNull(frame.getBeginTime());
 assertNotNull(frame.getEndTime());
 }

 // Test end method with invalid className or methodName
 @Test(expected = IllegalArgumentException.class)
 public void testEndInvalidClassNameOrMethodName() {
 String className = "com.mentorgen.tools.profile.runtime.Profile";
 String methodName = "";
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 }

 // Test unwind method with valid className, methodName, and exception
 @Test
 public void testUnwindValidClassNameMethodNameException() {
 String className = "com.mentorgen.tools.profile.runtime.Profile";
 String methodName = "testUnwindValidClassNameMethodNameException";
 String exception = "java.lang.NullPointerException";
 Profile.start(className, methodName);
 Profile.unwind(className, methodName, exception);
 Frame frame = findFrameWithClassNameAndMethodName(className, methodName);
 assertEquals(exception, frame.getException());
 }

 // Test unwind method with invalid className, methodName, or exception
 @Test(expected = IllegalArgumentException.class)
 public void testUnwindInvalidClassNameMethodNameException() {
 String className = "com.mentorgen.tools.profile.runtime.Profile";
 String methodName = "";
 String exception = "java.lang.NullPointerException";
 Profile.start(className, methodName);
 Profile.unwind(className, methodName, exception);
 }

 // Test alloc method with valid className
 @Test
 public void testAllocValidClassName() {
 String className = "com.mentorgen.tools.profile.runtime.Profile";
 Profile.alloc(className);
 ClassAllocation ca = Profile._allocList.get(className);
 assertEquals(1, ca.getAllocationCount());
 }

 // Test alloc method with invalid className
 @Test(expected = IllegalArgumentException.class)
 public void testAllocInvalidClassName() {
 String className = "";
 Profile.alloc(className);
 }

 // Helper method to find a Frame object with the given className and methodName
 private Frame findFrameWithClassNameAndMethodName(String className, String methodName) {
 Frame frame = null;
 for (Frame currentFrame : Profile._frameList) {
 if (currentFrame.getClassName().equals(className) && currentFrame.getMethodName().equals(methodName)) {
 frame = currentFrame;
 break;
 }
 }
 return frame;
 }
}
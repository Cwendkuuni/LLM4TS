package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.output.ProfileFormatter;
import com.mentorgen.tools.profile.runtime.Controller;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

 private static final String FILENAME = "test.prof";
 private Controller controller;
 private Profile profile;
 private ThreadDictionary threadDictionary;

 @Before
 public void setUp() {
 controller = mock(Controller.class);
 profile = new Profile();
 threadDictionary = new ThreadDictionary();
 Profile.init();
 }

 @After
 public void tearDown() {
 Profile.clear();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 verify(controller, times(1)).start();
 }

 @Test
 public void testInit() {
 Profile.init();
 assertEquals(1001, Profile._threadDictionary.size());
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertEquals(0, Profile._threadDictionary.size());
 }

 @Test
 public void testStart() {
 Profile.start();
 verify(controller, times(1)).start();
 }

 @Test
 public void testStop() {
 Profile.stop();
 verify(controller, times(1)).stop();
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName(FILENAME);
 assertEquals(FILENAME, Profile._controller.getFileName());
 }

 @Test(expected = IllegalStateException.class)
 public void testShutdownWhenNotRunning() {
 Profile.shutdown();
 fail("Should have thrown an exception");
 }

 @Test
 public void testThreads() {
 Profile.start("TestClass", "testMethod");
 Profile.start("TestClass", "testMethod2");
 List<Long> threads = Profile.threads();
 assertEquals(2, threads.size());
 }

 @Test
 public void testInteractions() {
 Profile.start("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
 assertEquals(1, interactions.size());
 }

 @Test
 public void testFrameList() {
 Profile.start("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 List<Frame> frameList = Profile.frameList();
 assertEquals(1, frameList.size());
 }

 @Test
 public void testAllocations() {
 Profile.alloc("TestClass");
 Profile.alloc("TestClass");
 List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
 assertEquals(1, allocations.size());
 ClassAllocation ca = allocations.get(0);
 assertEquals("TestClass", ca.getClassName());
 assertEquals(2, ca.getAllocCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 Profile.start("TestClass", "testMethod");
 Thread.yield();
 Thread.yield();
 long time = System.nanoTime();
 Profile.end("TestClass", "testMethod");
 long threadTotalTime = Profile.getThreadTotalTime(Thread.currentThread().getId());
 long expectedThreadTotalTime = time - Profile.getThreadTotalTime(Thread.currentThread().getId()) + 2 * 1000000000L; //2 seconds
 assertEquals(expectedThreadTotalTime, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 List<Frame> frameList = Profile.frameList();
 Collections.sort(frameList);
 Frame firstFrame = frameList.get(0);
 Frame lastFrame = frameList.get(frameList.size() - 1);
 assertNull(firstFrame.getParent());
 assertNull(lastFrame.getParent());
 }

 @Test
 public void testStartEnd() {
 Profile.init();
 String className = "TestClass";
 String methodName = "testMethod";
 long threadId = Thread.currentThread().getId();
 long startTime = System.nanoTime();
 Profile.start(className, methodName);
 Frame parent = ThreadDictionary.getMostRecentFrame(threadId);
 Frame target = findFrame(parent, className, methodName);
 assertEquals(0, target.getChildCount());
 long endTime = System.nanoTime();
 Profile.end(className, methodName);
 assertEquals(1, target.getChildCount());
 assertEquals(endTime - startTime, target.getNetTime());
 }

 @Test
 public void testBeginEndWait() {
 Profile.init();
 String className = "TestClass";
 String methodName = "testMethod";
 long threadId = Thread.currentThread().getId();
 long startTime = System.nanoTime();
 Profile.beginWait(className, methodName);
 Frame parent = ThreadDictionary.getMostRecentFrame(threadId);
 Frame target = findFrame(parent, className, methodName);
 assertEquals(0, target.getChildCount());
 long endTime = System.nanoTime();
 Profile.endWait(className, methodName);
 assertEquals(1, target.getChildCount());
 assertEquals(endTime - startTime, target.getNetTime());
 }

 @Test
 public void testUnwind() {
 Profile.init();
 String className = "TestClass";
 String methodName = "testMethod";
 String exception = "TestException";
 long threadId = Thread.currentThread().getId();
 long startTime = System.nanoTime();
 Profile.start(className, methodName);
 Frame parent = ThreadDictionary.getMostRecentFrame(threadId);
 Frame target = findFrame(parent, className, methodName);
 assertNull(target.getException());
 Profile.unwind(className, methodName, exception);
 assertEquals(exception, target.getException());
 }

 @Test
 public void testAlloc() {
 Profile.init();
 String className = "TestClass";
 long threadId = Thread.currentThread().getId();
 long startTime = System.nanoTime();
 Profile.alloc(className);
 Frame parent = ThreadDictionary.getMostRecentFrame(threadId);
 Frame target = findFrame(parent, className, "<init>");
 assertEquals(1, target.getChildCount());
 assertEquals(1, target.getAllocCount());
 }

 private Frame findFrame(Frame parent, String className, String methodName) {
 Frame frame = parent;
 while (frame != null) {
 if (frame.getClassName().equals(className) && frame.getMethodName().equals(methodName)) {
 return frame;
 }
 frame = frame.getParent();
 }
 return null;
 }
}
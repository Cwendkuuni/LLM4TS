package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;

public class ProfileTest {

 private static Profile underTest;

 @Before
 public void setUp() {
 Profile.init();
 underTest = new Profile();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 if (Profile._controllerThread != null) {
 assertTrue(Profile._controllerThread.isAlive());
 }
 }

 @Test
 public void testInit() {
 Profile.init();
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
 assertTrue(Profile._methodDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
 }

 @Test
 public void testStart() {
 Profile.start();
 assertNotNull(Profile._controller);
 assertTrue(Profile._controller.isRunning());
 }

 @Test
 public void testStop() {
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
 assertFalse(Profile._controller.isRunning());
 }

 @Test
 public void testThreads() {
 long threadId = Thread.currentThread().getId();
 List<Long> threads = Profile.threads();
 assertTrue(threads.contains(threadId));
 }

 @Test
 public void testInteractions() {
 long threadId = Thread.currentThread().getId();
 String className = "TestClass";
 String methodName = "testMethod";
 Profile.start(className, methodName);
 Frame frame = new Frame(null, new Method(className, methodName), threadId);
 Profile.end(className, methodName);
 List<Frame> interactions = Profile.interactions(threadId);
 assertTrue(interactions.contains(frame));
 }

 @Test
 public void testFrameList() {
 Frame frame = new Frame(null, new Method("TestClass", "testMethod"), Thread.currentThread().getId());
 Profile.start("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 Iterable<Frame> frameList = Profile.frameList();
 assertTrue(frameList.contains(frame));
 }

 @Test
 public void testAllocations() {
 String className = "TestClass";
 Profile.alloc(className);
 ClassAllocation ca = Profile._allocList.get(className);
 assertEquals(1, ca.getAllocCount());
 }

 @Test(expected = NullPointerException.class)
 public void testFindFrameNullClassName() {
 long threadId = Thread.currentThread().getId();
 Profile.findFrame(threadId, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFindFrameNullMethodName() {
 long threadId = Thread.currentThread().getId();
 Frame frame = new Frame(null, new Method("TestClass", "testMethod"), threadId);
 Profile._threadActiveFrame.put(threadId, frame);
 Profile.findFrame(threadId, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindFrameDifferentClassName() {
 long threadId = Thread.currentThread().getId();
 Frame frame = new Frame(null, new Method("TestClass1", "testMethod"), threadId);
 Profile._threadActiveFrame.put(threadId, frame);
 Profile.findFrame(threadId, "TestClass2", "testMethod");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindFrameDifferentMethodName() {
 long threadId = Thread.currentThread().getId();
 Frame frame = new Frame(null, new Method("TestClass", "testMethod1"), threadId);
 Profile._threadActiveFrame.put(threadId, frame);
 Profile.findFrame(threadId, "TestClass", "testMethod2");
 }

 @Test
 public void testFindFrameSameThreadSameClassNameSameMethod() {
 long threadId = Thread.currentThread().getId();
 String className = "TestClass";
 String methodName = "testMethod";
 Frame frame1 = new Frame(null, new Method(className, methodName), threadId);
 Frame frame2 = new Frame(null, new Method(className, methodName), threadId);
 Profile._threadActiveFrame.put(threadId, frame1);
 Frame result = Profile.findFrame(threadId, className, methodName);
 assertEquals(frame1, result);
 }

 @Test
 public void testFindFrameSameThreadDifferentMethod() {
 long threadId = Thread.currentThread().getId();
 String className = "TestClass";
 String methodName1 = "testMethod1";
 String methodName2 = "testMethod2";
 Frame frame1 = new Frame(null, new Method(className, methodName1), threadId);
 Frame frame2 = new Frame(null, new Method(className, methodName2), threadId);
 Profile._threadActiveFrame.put(threadId, frame1);
 Frame result = Profile.findFrame(threadId, className, methodName2);
 assertNull(result);
 }

 @Test
 public void testStartEnd() {
 long threadId = Thread.currentThread().getId();
 String className = "TestClass";
 String methodName = "testMethod";
 Profile.start(className, methodName);
 Frame frame = new Frame(null, new Method(className, methodName), threadId);
 Profile.end(className, methodName);
 Frame result = Profile.findFrame(threadId, className, methodName);
 assertEquals(frame, result);
 }

 @Test
 public void testBeginWaitEndWait() {
 long threadId = Thread.currentThread().getId();
 String className = "TestClass";
 String methodName = "testMethod";
 Profile.beginWait(className, methodName);
 Frame frame = new Frame(null, new Method(className, methodName), threadId);
 frame.beginWait(System.nanoTime());
 Profile.endWait(className, methodName);
 Frame result = Profile.findFrame(threadId, className, methodName);
 assertEquals(frame, result);
 }

 @Test
 public void testUnwind() {
 long threadId = Thread.currentThread().getId();
 String className = "TestClass";
 String methodName = "testMethod";
 String exception = "TestException";
 Profile.start(className, methodName);
 Frame frame = new Frame(null, new Method(className, methodName), threadId);
 Profile.end(className, methodName);
 Profile.unwind(className, methodName, exception);
 Frame result = Profile.findFrame(threadId, className, methodName);
 assertNull(result);
 }

 @Test
 public void testAlloc() {
 String className = "TestClass";
 Profile.alloc(className);
 ClassAllocation ca = Profile._allocList.get(className);
 assertEquals(1, ca.getAllocCount());
 }

 @Test
 public void testRun() {
 Profile.shutdown();
 underTest.run();
 assertTrue(Profile._controller.isClosed());
 }
}
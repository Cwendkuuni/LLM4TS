package com.mentorgen.tools.profile.runtime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProfileTest {

 private static final String CLASS_NAME = "com.example.TestClass";
 private static final String METHOD_NAME_1 = "method1";
 private static final String METHOD_NAME_2 = "method2";
 private static final String METHOD_NAME_3 = "method3";
 private static final String EXCEPTION_CLASS_NAME = "java.lang.NullPointerException";

 private Profile _profile;

 @Before
 public void initProfiler() {
 Profile.initProfiler();
 }

 @After
 public void clearProfiler() {
 Profile.clear();
 }

 @Test
 public void testStartAndEnd() {
 _profile = new Profile();
 _profile.start(CLASS_NAME, METHOD_NAME_1);
 _profile.end(CLASS_NAME, METHOD_NAME_1);
 assertNotNull(_profile.findFrame(CLASS_NAME, METHOD_NAME_1));
 }

 @Test
 public void testNestedCalls() {
 _profile = new Profile();
 _profile.start(CLASS_NAME, METHOD_NAME_1);
 _profile.start(CLASS_NAME, METHOD_NAME_2);
 _profile.end(CLASS_NAME, METHOD_NAME_2);
 _profile.end(CLASS_NAME, METHOD_NAME_1);
 Frame parentFrame = _profile.findFrame(CLASS_NAME, METHOD_NAME_1);
 Frame childFrame = parentFrame.getChild(METHOD_NAME_2);
 assertNotNull(childFrame);
 }

 @Test
 public void testThreadInteractions() {
 Thread thread1 = new Thread(() -> {
 _profile = new Profile();
 _profile.start(CLASS_NAME, METHOD_NAME_1);
 _profile.end(CLASS_NAME, METHOD_NAME_1);
 });

 Thread thread2 = new Thread(() -> {
 _profile = new Profile();
 _profile.start(CLASS_NAME, METHOD_NAME_2);
 _profile.end(CLASS_NAME, METHOD_NAME_2);
 });

 thread1.start();
 thread2.start();
 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 List<Long> threads = Profile.threads();
 assertEquals(2, threads.size());
 List<Frame> thread1Frames = Profile.interactions(threads.get(0));
 List<Frame> thread2Frames = Profile.interactions(threads.get(1));
 assertNotNull(thread1Frames);
 assertNotNull(thread2Frames);
 }

 @Test
 public void testAllocations() {
 _profile = new Profile();
 _profile.alloc(CLASS_NAME);
 _profile.alloc(CLASS_NAME);
 List<ClassAllocation> allocations = Profile.allocations();
 assertEquals(1, allocations.size());
 ClassAllocation allocation = allocations.get(0);
 assertEquals(2, allocation.getAllocCount());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidClassName() {
 _profile = new Profile();
 _profile.start("InvalidClassName", METHOD_NAME_1);
 _profile.end("InvalidClassName", METHOD_NAME_1);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidMethodName() {
 _profile = new Profile();
 _profile.start(CLASS_NAME, "InvalidMethodName");
 _profile.end(CLASS_NAME, "InvalidMethodName");
 }

 @Test(expected = AssertionError.class)
 public void testStartTwice() {
 _profile = new Profile();
 _profile.start(CLASS_NAME, METHOD_NAME_1);
 _profile.start(CLASS_NAME, METHOD_NAME_1);
 }

 @Test
 public void testDebugMode() {
 Profile._debugStart = true;
 _profile = new Profile();
 _profile.start(CLASS_NAME, METHOD_NAME_1);
 _profile.end(CLASS_NAME, METHOD_NAME_1);
 String output = new String(new File("output.txt").getBytes());
 assertTrue(output.contains("(com.example.TestClass : method1)"));
 }

 @Test
 public void testExceptionHandling() {
 _profile = new Profile();
 try {
 throw new NullPointerException();
 } finally {
 _profile.unwind(CLASS_NAME, METHOD_NAME_1, EXCEPTION_CLASS_NAME);
 }
 Frame frame = _profile.findFrame(CLASS_NAME, METHOD_NAME_1);
 assertNotNull(frame);
 assertEquals(EXCEPTION_CLASS_NAME, frame.getException());
 }
}
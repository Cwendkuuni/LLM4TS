package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfileTest {

 private static Profile underTest;

 @BeforeClass
 public static void setUpClass() {
 Profile.init();
 underTest = new Profile();
 }

 @Test
 public void testInit() {
 // Assert that init method initializes static variables
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
 public void testInitProfiler() {
 // Assert that initProfiler method initializes controller and adds shutdown hook
 Profile.initProfiler();
 assertTrue(Runtime.getRuntime().getShutdownHooks().contains(underTest));
 }

 @Test
 public void testClear() {
 // Assert that clear method resets static variables
 Profile.clear();
 assertNull(Profile._threadDictionary.getThreadTotalTime(Thread.currentThread().getId()));
 assertEquals(0, Profile._frameList.size());
 assertEmpty(Profile._threadActiveFrame.values());
 assertEmpty(Profile._methodDictionary.values());
 }

 @Test
 public void testStartAndEnd() {
 // Test start and end methods
 String className = "com.example.TestClass";
 String methodName = "testMethod";
 Profile.start(className, methodName);
 Profile.end(className, methodName);
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
 assertNotNull(frame);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartNullClassName() {
 Profile.start(null, methodName);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartNullMethodName() {
 Profile.start(className, null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartSameClassNameSameMethodTwice() {
 Profile.start(className, methodName);
 Profile.start(className, methodName);
 Frame frame1 = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
 Frame frame2 = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
 assertNotNull(frame1);
 assertNotNull(frame2);
 assertNotSame(frame1.getFrameNumber(), frame2.getFrameNumber());
 }

 @Test
 public void testBeginWaitEndWait() {
 // Test beginWait and endWait methods
 String className1 = "com.example.TestClass1";
 String methodName1 = "testMethod1";
 String className2 = "com.example.TestClass2";
 String methodName2 = "testMethod2";
 Profile.start(className1, methodName1);
 Profile.beginWait(className1, methodName1);
 Profile.endWait(className1, methodName1);
 Profile.start(className2, methodName2);
 Profile.beginWait(className2, methodName2);
 Profile.endWait(className2, methodName2);
 Frame frame1 = Profile.findFrame(Thread.currentThread().getId(), className1, methodName1);
 Frame frame2 = Profile.findFrame(Thread.currentThread().getId(), className2, methodName2);
 assertNotNull(frame1);
 assertNotNull(frame2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBeginWaitNullClassName() {
 Profile.beginWait(null, methodName);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBeginWaitNullMethodName() {
 Profile.beginWait(className, null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndWaitNullClassName() {
 Profile.endWait(null, methodName);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndWaitNullMethodName() {
 Profile.endWait(className, null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testAlloc() {
 // Test alloc method
 String className = "com.example.TestClass";
 Profile.alloc(className);
 ClassAllocation ca = Profile._allocList.get(className);
 assertTrue(ca.getAllocCount() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAllocNullClassName() {
 Profile.alloc(null);
 fail("Expected IllegalArgumentException");
 }
}
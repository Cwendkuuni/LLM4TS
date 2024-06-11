package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfileTest {

 private static Profile profile;

 @BeforeClass
 public static void setupClass() {
 Profile.init();
 profile = new Profile();
 }

 @Test
 public void testInit() {
 assertEquals("size should be zero before init", 0, Profile._threadActiveFrame.size());
 assertEquals("size should be zero before init", 0, Profile._threadDictionary.size());
 assertEquals("size should be zero before init", 0, Profile._frameList.size());
 assertEquals("size should be zero before init", 0, Profile._methodDictionary.size());
 assertEquals("size should be zero before init", 0, Profile._allocList.size());
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue("Controller should not be null", Profile._controller != null);
 if (Profile._controllerThread != null) {
 assertTrue("Controller thread should not be null", Profile._controllerThread != null);
 }
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertEquals("size should be zero after clear", 0, Profile._threadActiveFrame.size());
 assertEquals("size should be zero after clear", 0, Profile._threadDictionary.size());
 assertEquals("size should be zero after clear", 0, Profile._frameList.size());
 assertEquals("size should be zero after clear", 0, Profile._methodDictionary.size());
 assertEquals("size should be zero after clear", 0, Profile._allocList.size());
 }

 @Test
 public void testStartAndEnd() {
 Profile.start("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), "TestClass", "testMethod");
 assertNotNull("Frame should not be null", frame);
 }

 @Test(expected = NullPointerException.class)
 public void testFindFrameWithNullClassName() {
 Profile.findFrame(Thread.currentThread().getId(), null, "testMethod");
 }

 @Test(expected = NullPointerException.class)
 public void testFindFrameWithNullMethodName() {
 Profile.findFrame(Thread.currentThread().getId(), "TestClass", null);
 }

 @Test
 public void testFindFrameWithInvalidClassNameAndMethodName() {
 Profile.findFrame(Thread.currentThread().getId(), "OtherClass", "otherMethod");
 }

 @Test
 public void testFindFrameWithValidClassNameAndMethodName() {
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), "TestClass", "testMethod");
 assertNotNull("Frame should not be null", frame);
 }

 @Test
 public void testAlloc() {
 Profile.alloc("TestClass");
 ClassAllocation ca = Profile._allocList.get("TestClass");
 assertEquals("Allocation count should be one", 1, ca.getAllocCount());
 }

 @Test
 public void testRun() {
 Profile.shutdown();
 profile.run();
 }

}
package com.mentorgen.tools.profile.runtime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProfileTest {

 private static final String TEST_CLASS_NAME = "com.example.TestClass";
 private static final String TEST_METHOD_NAME = "testMethod";

 @Before
 public void setUp() {
 Profile.initProfiler();
 Profile.init();
 }

 @After
 public void tearDown() {
 Profile.shutdown();
 }

 @Test
 public void testStartAndEnd() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 // Perform some actions inside the test method to be measured
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }

 @Test
 public void testStartAndEndWithException() {
 try {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 throw new RuntimeException("Test exception");
 } finally {
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }
 }

 @Test
 public void testStartAndWait() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 // Simulate some waiting time (e.g., Thread.sleep())
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.endWait(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }

 @Test
 public void testAllocations() {
 int iterations = 10000;
 for (int i = 0; i < iterations; i++) {
 Profile.alloc(TEST_CLASS_NAME);
 }
 List<ClassAllocation> allocList = Profile.allocations();
 int actualAllocCount = allocList.size();
 int expectedAllocCount = iterations;
 assertEquals(expectedAllocCount, actualAllocCount);
 }
}
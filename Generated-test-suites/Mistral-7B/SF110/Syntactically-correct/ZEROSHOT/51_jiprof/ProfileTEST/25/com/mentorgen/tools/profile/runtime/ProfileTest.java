package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;

public class ProfileTest {

 private static final String CLASS_NAME = "com.example.MyClass";
 private static final String METHOD_NAME = "myMethod";

 @BeforeClass
 static public void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 static public void shutdownProfiler() {
 Profile.shutdown();
 }

 @Before
 static public void init() {
 Profile.init();
 }

 @After
 static public void clear() {
 Profile.clear();
 }

 @Test
 public void testInitAndClear() {
 Profile.init();
 Profile.clear();
 assertNull(Profile._threadDictionary);
 assertNull(Profile._frameList);
 assertNull(Profile._threadActiveFrame);
 assertNull(Profile._methodDictionary);
 }

 @Test
 public void testInitProfiler() {
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._threadActiveFrame);
 assertNotNull(Profile._methodDictionary);
 }

 @Test(expected = AssertionError.class)
 public void testInitTwice() {
 Profile.init();
 Profile.init();
 fail("Init should only be called once");
 }

 @Test
 public void testGetThreadTotalTime() {
 long threadId = Thread.currentThread().getId();
 Profile.start(CLASS_NAME, METHOD_NAME);
 long startTime = System.nanoTime();
 Thread.yield();
 Profile.end(CLASS_NAME, METHOD_NAME);

 List<Long> threads = Profile.threads();
 Long thread = null;
 for (Long t : threads) {
 if (t.equals(threadId)) {
 thread = t;
 break;
 }
 }
 assertNotNull(thread);

 long threadTotalTime = Profile.getThreadTotalTime(thread.longValue());
 assertTrue(threadTotalTime > startTime);
 }

 @Test
 public void testInteractions() {
 long threadId = Thread.currentThread().getId();
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);

 List<Long> threads = Profile.threads();
 Long thread = null;
 for (Long t : threads) {
 if (t.equals(threadId)) {
 thread = t;
 break;
 }
 }
 assertNotNull(thread);

 List<Frame> interactions = Profile.interactions(thread.longValue());
 assertEquals(2, interactions.size());
 }

 @Test
 public void testFrameList() {
 long threadId = Thread.currentThread().getId();
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);

 List<Long> threads = Profile.threads();
 Long thread = null;
 for (Long t : threads) {
 if (t.equals(threadId)) {
 thread = t;
 break;
 }
 }
 assertNotNull(thread);

 List<Frame> frameList = Profile.frameList();
 assertEquals(2, frameList.size());
 }

 @Test
 public void testAllocations() {
 ClassAllocation alloc = null;
 Profile.alloc(CLASS_NAME);
 Profile.alloc(CLASS_NAME);
 List<ClassAllocation> allocations = new ArrayList<>(Profile.allocList().values());
 assertEquals(1, allocations.size());
 alloc = allocations.get(0);
 assertEquals(2, alloc.getAllocCount());
 }

 @Test
 public void testStartAndEnd() {
 long threadId = Thread.currentThread().getId();
 long startTime = System.nanoTime();
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);

 List<Long> threads = Profile.threads();
 Long thread = null;
 for (Long t : threads) {
 if (t.equals(threadId)) {
 thread = t;
 break;
 }
 }
 assertNotNull(thread);

 Frame frame = Profile.findFrame(thread.longValue(), CLASS_NAME, METHOD_NAME);
 assertTrue(frame.getBeginTime() > startTime);
 assertTrue(frame.getEndTime() > startTime);
 }

 @Test
 public void testBeginWaitAndEndWait() {
 long threadId = Thread.currentThread().getId();
 long startTime = System.nanoTime();
 Profile.beginWait(CLASS_NAME, METHOD_NAME);
 Thread.yield();
 Profile.endWait(CLASS_NAME, METHOD_NAME);

 List<Long> threads = Profile.threads();
 Long thread = null;
 for (Long t : threads) {
 if (t.equals(threadId)) {
 thread = t;
 break;
 }
 }
 assertNotNull(thread);

 Frame frame = Profile.findFrame(thread.longValue(), CLASS_NAME, METHOD_NAME);
 assertTrue(frame.getBeginTime() > startTime);
 assertTrue(frame.getEndTime() > startTime);
 }

 @Test
 public void testUnwind() {
 long threadId = Thread.currentThread().getId();
 long startTime = System.nanoTime();
 try {
 throw new Exception("Test exception");
 } catch (Exception e) {
 Profile.unwind(CLASS_NAME, METHOD_NAME, e.getClass().getName());
 }

 List<Long> threads = Profile.threads();
 Long thread = null;
 for (Long t : threads) {
 if (t.equals(threadId)) {
 thread = t;
 break;
 }
 }
 assertNotNull(thread);

 Frame frame = Profile.findFrame(thread.longValue(), CLASS_NAME, METHOD_NAME);
 assertTrue(frame.getBeginTime() > startTime);
 assertTrue(frame.getEndTime() > startTime);
 }
}
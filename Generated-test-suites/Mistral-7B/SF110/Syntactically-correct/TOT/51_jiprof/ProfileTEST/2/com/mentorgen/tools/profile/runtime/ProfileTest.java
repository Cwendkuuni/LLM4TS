package com.mentorgen.tools.profile.runtime;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProfileTest {

 private static final String CLASS_NAME = "com.example.MyClass";
 private static final String METHOD_NAME_1 = "method1";
 private static final String METHOD_NAME_2 = "method2";
 private static final String METHOD_NAME_3 = "method3";
 private static final long THREAD_ID_1 = 1L;
 private static final long THREAD_ID_2 = 2L;
 private static final long THREAD_ID_3 = 3L;
 private static final String FILENAME = "output.txt";
 private static final boolean REMOTE = false;

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 public static void clearProfiler() {
 Profile.clear();
 }

 @Test
 public void testInitAndClear() {
 // Test initialization and clear
 Profile.init();
 Profile.clear();
 }

 @Test
 public void testThreads() {
 // Test threads
 Profile.init();
 Profile.start();
 List<Long> threads = Profile.threads();
 assertEquals(0, threads.size());
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 threads = Profile.threads();
 assertEquals(1, threads.size());
 assertEquals(THREAD_ID_1, threads.get(0));
 Profile.end(CLASS_NAME, METHOD_NAME_1);
 threads = Profile.threads();
 assertEquals(0, threads.size());
 }

 @Test
 public void testInteractions() {
 // Test interactions for a single thread
 Profile.init();
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 Profile.start(CLASS_NAME, METHOD_NAME_2);
 Profile.end(CLASS_NAME, METHOD_NAME_1);
 List<Frame> interactions = Profile.interactions(THREAD_ID_1);
 assertEquals(1, interactions.size());
 Frame interaction = interactions.get(0);
 assertEquals(CLASS_NAME, interaction.getClassName());
 assertEquals(METHOD_NAME_1, interaction.getMethodName());
 assertEquals(THREAD_ID_1, interaction.getThreadId());
 // Test interactions for multiple threads
 Profile.init();
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 Profile.start(CLASS_NAME, METHOD_NAME_2);
 Profile.end(CLASS_NAME, METHOD_NAME_1);
 interactions = Profile.interactions(THREAD_ID_1);
 assertEquals(1, interactions.size());
 interactions = Profile.interactions(THREAD_ID_2);
 assertEquals(1, interactions.size());
 }

 @Test
 public void testFrameList() {
 // Test frame list
 Profile.init();
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 Profile.start(CLASS_NAME, METHOD_NAME_2);
 Profile.end(CLASS_NAME, METHOD_NAME_1);
 List<Frame> frameList = Profile.frameList();
 assertEquals(3, frameList.size());
 Frame frame1 = frameList.get(0);
 assertEquals(CLASS_NAME, frame1.getClassName());
 assertEquals(METHOD_NAME_1, frame1.getMethodName());
 assertEquals(THREAD_ID_1, frame1.getThreadId());
 Frame frame2 = frameList.get(1);
 assertEquals(CLASS_NAME, frame2.getClassName());
 assertEquals(METHOD_NAME_1, frame2.getMethodName());
 assertEquals(THREAD_ID_1, frame2.getThreadId());
 Frame frame3 = frameList.get(2);
 assertEquals(CLASS_NAME, frame3.getClassName());
 assertEquals(METHOD_NAME_2, frame3.getMethodName());
 assertEquals(THREAD_ID_2, frame3.getThreadId());
 }

 @Test
 public void testAllocations() {
 // Test allocations
 Profile.init();
 Profile.start(CLASS_NAME);
 Profile.alloc(CLASS_NAME);
 Profile.alloc(CLASS_NAME);
 List<ClassAllocation> allocations = Profile.allocations();
 assertEquals(1, allocations.size());
 ClassAllocation allocation = allocations.get(0);
 assertEquals(CLASS_NAME, allocation. getClassName());
 assertEquals(2, allocation.getAllocCount());
 }

 @Test
 public void testThreadTotalTime() {
 // Test thread total time
 Profile.init();
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 Profile.start(CLASS_NAME, METHOD_NAME_2);
 long threadTotalTime1 = Profile.getThreadTotalTime(THREAD_ID_1);
 assertEquals(0, threadTotalTime1);
 Profile.end(CLASS_NAME, METHOD_NAME_1);
 threadTotalTime1 = Profile.getThreadTotalTime(THREAD_ID_1);
 assertEquals(-1, threadTotalTime1); // Indicates that the thread has been terminated
 Profile.end(CLASS_NAME, METHOD_NAME_1);
 threadTotalTime1 = Profile.getThreadTotalTime(THREAD_ID_1);
 assertNotNull(threadTotalTime1);
 assertEquals(2 * System.nanoTime() / 3, threadTotalTime1); // Assuming uneven distribution of time
 }

 @Test
 public void testSortFrameList() {
 // Test sorting of frame list
 Profile.init();
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 Profile.start(CLASS_NAME, METHOD_NAME_2);
 Profile.end(CLASS_NAME, METHOD_NAME_1);
 Profile.end(CLASS_NAME, METHOD_NAME_1);
 Profile.end(CLASS_NAME, METHOD_NAME_2);
 Collections.sort(Profile.frameList());
 Frame firstFrame = Profile.frameList().get(0);
 assertEquals(CLASS_NAME, firstFrame.getClassName());
 assertEquals(METHOD_NAME_2, firstFrame.getMethodName());
 Frame lastFrame = Profile.frameList().get(Profile.frameList().size() - 1);
 assertEquals(CLASS_NAME, lastFrame.getClassName());
 assertEquals(METHOD_NAME_1, lastFrame.getMethodName());
 }

 @Test
 public void testStartAndEndMethodsWithException() {
 // Test methods with exceptions
 Profile.init();
 PrintStream originalErr = System.err;
 ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
 System.setErr(new PrintStream(errorStream));
 try {
 Profile.init();
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 throw new RuntimeException("Test exception");
 } finally {
 System.setErr(originalErr);
 }
 List<Frame> frameList = Profile.frameList();
 Frame exceptionFrame = findFrameWithException(frameList, "Test exception");
 assertNotNull(exceptionFrame);
 assertEquals(CLASS_NAME, exceptionFrame.getClassName());
 assertEquals(METHOD_NAME_1, exceptionFrame.getMethodName());
 assertEquals(-1, exceptionFrame.getEndTime()); // Indicates that the method has not finished yet
 }

 @Test
 public void testBeginAndEndWait() {
 // Test begin and end wait
 Profile.init();
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 long startNanoTime = System.nanoTime();
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 // Ignore
 }
 Profile.beginWait(CLASS_NAME, METHOD_NAME_1);
 try {
 Thread.sleep(50);
 } catch (InterruptedException e) {
 // Ignore
 }
 Profile.endWait(CLASS_NAME, METHOD_NAME_1);
 Frame waitFrame = findFrameWithWait(Profile.frameList(), "Thread.sleep");
 assertNotNull(waitFrame);
 assertEquals(CLASS_NAME, waitFrame.getClassName());
 assertEquals(METHOD_NAME_1, waitFrame.getMethodName());
 assertEquals(Thread.currentThread().getId(), waitFrame.getThreadId());
 assertEquals(startNanoTime + 150, waitFrame.getBeginWaitNanoTime());
 }

 private Frame findFrameWithException(List<Frame> frameList, String exceptionMessage) {
 for (Frame frame : frameList) {
 if (frame.getExceptionMessage().equals(exceptionMessage)) {
 return frame;
 }
 }
 return null;
 }

 private Frame findFrameWithWait(List<Frame> frameList, String methodName) {
 for (Frame frame : frameList) {
 if (frame.getClassName().equals("java.lang.Thread") && frame.getMethodName().equals("sleep")
 && frame.getClassName().equals(CLASS_NAME) && frame.getMethodName().equals(METHOD_NAME_1)) {
 return frame;
 }
 }
 return null;
 }
}
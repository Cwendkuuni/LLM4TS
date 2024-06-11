package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {

 private static final String TEST_CLASS_NAME = "TestCase";
 private static final String TEST_METHOD_NAME = "testMethod";
 private static final String TEST_OTHER_CLASS_NAME = "AnotherTestCase";
 private static final String TEST_OTHER_METHOD_NAME = "testOtherMethod";

 @Before
 public void setup() {
 Profile.init();
 }

 @After
 public void teardown() {
 Profile.clear();
 }

 @Test
 public void testInit() {
 assertFalse(Profile._controllerThread.isAlive());
 assertNull(Profile._controller);
 assertNull(Profile._threadActiveFrame);
 assertNull(Profile._threadDictionary);
 assertNull(Profile._methodDictionary);
 assertNull(Profile._frameList);
 assertNull(Profile._allocList);
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controllerThread.isAlive());
 assertNotNull(Profile._controller);
 }

 @Test
 public void testStart() {
 Profile.start();
 assertTrue(Profile._controller.isRunning());
 }

 @Test
 public void testStop() {
 Profile.start();
 Profile.stop();
 assertFalse(Profile._controller.isRunning());
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("output.txt");
 assertEquals("output.txt", Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 Profile.start();
 Profile.shutdown();
 assertFalse(Profile._controller.isRunning());
 }

 @Test
 public void testThreads() {
 Profile.start();
 Thread thread = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }
 });
 thread.start();
 List<Long> threads = Profile.threads();
 Thread mainThread = Thread.currentThread();
 assertTrue(threads.contains(mainThread.getId()));
 assertTrue(threads.contains(thread.getId()));
 }

 @Test
 public void testInteractions() {
 Profile.start();
 Thread thread = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }
 });
 thread.start();
 List<Frame> interactions = Profile.interactions(thread.getId());
 Frame interaction = interactions.iterator().next();
 assertNotNull(interaction);
 assertEquals(TEST_CLASS_NAME, interaction.getClassName());
 assertEquals(TEST_METHOD_NAME, interaction.getMethodName());
 }

 @Test
 public void testFrameList() {
 Profile.start();
 Thread thread = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }
 });
 thread.start();
 Profile.sortFrameList(new FrameComparator());
 List<Frame> frameList = Profile.frameList();
 Frame firstFrame = frameList.iterator().next();
 Frame lastFrame = frameList.get(frameList.size() - 1);
 assertNotNull(firstFrame);
 assertNotNull(lastFrame);
 assertTrue(firstFrame.getBeginTime() < lastFrame.getBeginTime());
 }

 @Test
 public void testAllocations() {
 Profile.start();
 new TestCase();
 List<ClassAllocation> allocations = new ArrayList<ClassAllocation>(Profile._allocList.values());
 ClassAllocation classNameAllocation = null;
 for (ClassAllocation allocation : allocations) {
 if (TEST_CLASS_NAME.equals(allocation.getClassName())) {
 classNameAllocation = allocation;
 break;
 }
 }
 assertNotNull(classNameAllocation);
 assertEquals(1, classNameAllocation.getAllocCount());
 }

 @Test(expected = NullPointerException.class)
 public void testFindFrameNullThread() {
 Profile.start();
 Frame frame = Profile.findFrame(0, TEST_CLASS_NAME, TEST_METHOD_NAME);
 assertNull(frame);
 }

 @Test
 public void testFindFrame() {
 Profile.start();
 Thread thread = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }
 });
 thread.start();
 Frame frame = Profile.findFrame(thread.getId(), TEST_CLASS_NAME, TEST_METHOD_NAME);
 assertNotNull(frame);
 assertEquals(TEST_CLASS_NAME, frame.getClassName());
 assertEquals(TEST_METHOD_NAME, frame.getMethodName());
 }

 @Test(expected = NullPointerException.class)
 public void testFindFrameNullClassName() {
 Profile.start();
 Thread thread = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }
 });
 thread.start();
 Frame frame = Profile.findFrame(thread.getId(), null, TEST_METHOD_NAME);
 assertNull(frame);
 }

 @Test(expected = NullPointerException.class)
 public void testFindFrameNullMethodName() {
 Profile.start();
 Thread thread = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }
 });
 thread.start();
 Frame frame = Profile.findFrame(thread.getId(), TEST_CLASS_NAME, null);
 assertNull(frame);
 }

 @Test
 public void testFindFrameNotFound() {
 Profile.start();
 Thread thread1 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }
 });
 thread1.start();
 Thread thread2 = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start(TEST_OTHER_CLASS_NAME, TEST_OTHER_METHOD_NAME);
 Profile.end(TEST_OTHER_CLASS_NAME, TEST_OTHER_METHOD_NAME);
 }
 });
 thread2.start();
 Frame frame = Profile.findFrame(thread1.getId(), TEST_OTHER_CLASS_NAME, TEST_OTHER_METHOD_NAME);
 assertNull(frame);
 }
}

class FrameComparator implements Comparator<Frame> {
 @Override
 public int compare(Frame o1, Frame o2) {
 long beginTime1 = o1.getBeginTime();
 long beginTime2 = o2.getBeginTime();
 return Long.compare(beginTime1, beginTime2);
 }
}

class TestCase {
 // Empty test case to trigger class allocation
}
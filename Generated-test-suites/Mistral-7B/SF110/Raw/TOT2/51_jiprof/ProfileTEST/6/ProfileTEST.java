package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.output.ProfileOutput;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

 private static final String TEST_FILE_NAME = "testProfile.txt";
 private static final boolean DEBUG_START = false;
 private static final boolean DEBUG_EXCEPTION = false;
 private Profile _profile;

 @Before
 public void setUp() {
 Profile.init();
 Profile.initProfiler();
 }

 @After
 public void tearDown() {
 Profile.shutdown();
 }

 @Test
 public void testInit() {
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._allocList);
 assertNull(Profile._controller);
 assertNull(Profile._controllerThread);
 }

 @Test
 public void testInitProfiler() {
 assertNotNull(Profile._controller);
 assertNotNull(Profile._controllerThread);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertNull(Profile._threadDictionary.getMostRecentFrame(1L));
 }

 @Test
 public void testStart() {
 Profile.start();
 assertTrue(Profile._controller.isRunning());
 }

 @Test
 public void testStop() {
 Profile.stop();
 assertFalse(Profile._controller.isRunning());
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName(TEST_FILE_NAME);
 assertEquals(TEST_FILE_NAME, Profile._controller.getFileName());
 }

 @Test(expected = NullPointerException.class)
 public void testStartNullClassName() {
 Profile.start(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testStartNullMethodName() {
 Profile.start("", null);
 }

 @Test(expected = NullPointerException.class)
 public void testStartNullClassNameAndMethodName() {
 Profile.start(null, null);
 }

 @Test
 public void testInitThread() {
 Thread t = new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 }
 });
 t.start();
 ThreadDictionary threads = Profile.threads();
 Frame threadFrame = threads.iterator().next().get(0);
 assertNotNull(threadFrame);
 }

 @Test
 public void testMultipleThreads() {
 List<Thread> threads = new ArrayList<>();
 for (int i = 0; i < 10; i++) {
 final int threadId = i;
 threads.add(new Thread(new Runnable() {
 @Override
 public void run() {
 Profile.start("TestClass", "testMethod" + threadId);
 Profile.end("TestClass", "testMethod" + threadId);
 }
 }));
 }
 for (Thread thread : threads) {
 thread.start();
 }
 List<Long> threadIds = new ArrayList<>(threads.size());
 for (Thread thread : threads) {
 threadIds.add(thread.getId());
 }
 List<Frame> frames = Profile.frameList();
 for (int i = 0; i < frames.size(); i++) {
 Frame frame = frames.get(i);
 long threadId = threadIds.get(i / 2);
 assertEquals(threadId, frame.getThreadId());
 }
 }

 @Test
 public void testAllocations() {
 Class clazz = TestClass.class;
 Profile.start(clazz.getName(), "<init>");
 Profile.alloc(clazz.getName());
 Frame frame = Profile.frameList().iterator().next();
 ClassAllocation ca = new ClassAllocation(clazz.getName());
 ca.incAllocCount();
 assertEquals(ca.getAllocCount(), frame.getAllocCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFileNameNull() {
 Profile.setFileName(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartAfterStop() {
 Profile.stop();
 Profile.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testEndAfterStop() {
 Profile.stop();
 Profile.end("TestClass", "testMethod");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndNullClassName() {
 Profile.end(null, "testMethod");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndNullMethodName() {
 Profile.end("TestClass", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndNullClassNameAndMethodName() {
 Profile.end(null, null);
 }

 @Test
 public void testDump() throws IOException {
 Frame firstFrame = Profile.frameList().iterator().next();
 ProfileDump.dump();
 assertNotNull(new File(TEST_FILE_NAME));
 }

}

class TestClass {

 }
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.output.ProfileWriter;
import com.mentorgen.tools.profile.Profile;
import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;

public class ProfileTest {

	private static Profile profile;
	private static Map<String, Method> methodDictionary;
	private static Frame rootFrame;
	private static Frame methodFrame;
	private static List<Frame> frameList;
	private static long threadId;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Profile.init();
		profile = new Profile();
		methodDictionary = new HashMap<String, Method>();
		rootFrame = new Frame(null, null, 0);
		methodFrame = new Frame(rootFrame, new Method("TestClass", "testMethod"), 0);
		frameList = new ArrayList<Frame>();
		frameList.add(rootFrame);
		frameList.add(methodFrame);
		Profile._frameList = frameList;
		Thread.currentThread().setName("TestThread");
		threadId = Thread.currentThread().getId();
		Profile.start("TestClass", "testMethod");
	}

	@Test
	public void testInit() {
		assertEquals("Java Interactive Profiler: starting", System.err.getErr(), "Unexpected initialization message");
		assertTrue(Profile._controllerThread == null);
		assertFalse(Profile._controller.isRunning());
		assertFalse(Profile._frameList.isEmpty());
		assertFalse(Profile._methodDictionary.isEmpty());
		assertEquals(2, Profile._frameList.size());
		assertEquals(1, Profile._methodDictionary.size());
		assertFalse(Profile._threadDictionary.isEmpty());
		assertEquals(1, Profile._threadDictionary.size());
		assertFalse(Profile._allocList.isEmpty());
		assertEquals(1, Profile._allocList.size());
	}

	@Test
	public void testInitProfiler() {
		Profile.initProfiler();
		assertTrue(Profile._controllerThread != null);
		assertTrue(Profile._controller.isRunning());
	}

	@Test
	public void testClear() {
		Profile.clear();
		assertEquals("Java Interactive Profiler: starting", System.err.getErr(), "Unexpected initialization message");
		assertTrue(Profile._controllerThread == null);
		assertFalse(Profile._controller.isRunning());
		assertFalse(Profile._frameList.isEmpty());
		assertFalse(Profile._methodDictionary.isEmpty());
		assertEquals(0, Profile._frameList.size());
		assertEquals(0, Profile._methodDictionary.size());
		assertTrue(Profile._threadDictionary.isEmpty());
		assertTrue(Profile._allocList.isEmpty());
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
		Profile.setFileName("test.prof");
		assertTrue(Profile._controller.getFileName().equals("test.prof"));
	}

	@Test(expected = IllegalStateException.class)
	public void testShutdownWhileRunning() {
		Profile.shutdown();
		fail("Unexpected shutdown while running");
	}

	@Test
	public void testShutdown() {
		Profile.start("TestClass", "testMethod");
		Profile.end("TestClass", "testMethod");
		Profile.shutdown();
		assertFalse(Profile._controller.isRunning());
		assertTrue(Profile._frameList.isEmpty());
		assertTrue(Profile._methodDictionary.isEmpty());
		assertTrue(Profile._threadDictionary.isEmpty());
		assertTrue(Profile._allocList.isEmpty());
	}

	@Test
	public void testThreads() {
		Profile.start("TestClass", "testMethod");
		Profile.end("TestClass", "testMethod");
		List<Long> threads = Profile.threads();
		assertEquals(1, threads.size());
		assertEquals(threadId, threads.get(0));
	}

	@Test
	public void testInteractions() {
		Profile.start("TestClass", "testMethod");
		Profile.end("TestClass", "testMethod");
		List<Frame> interactions = Profile.interactions(threadId);
		assertEquals(1, interactions.size());
		assertEquals(methodFrame, interactions.get(0));
	}

	@Test
	public void testFrameList() {
		Profile.start("TestClass", "testMethod");
		Profile.end("TestClass", "testMethod");
		List<Frame> frameList = Profile.frameList();
		assertEquals(1, frameList.size());
		assertEquals(rootFrame, frameList.get(0));
	}

	@Test
	public void testAllocations() {
		Profile.alloc("TestClass");
		Profile.alloc("TestClass");
		Map<String, ClassAllocation> allocList = Profile._allocList;
		assertEquals(1, allocList.size());
		assertEquals(2, allocList.get("TestClass").getAllocationCount());
	}

	@Test
	public void testThreadTotalTime() {
		Profile.start("TestClass", "testMethod");
		Thread.sleep(100);
		Profile.end("TestClass", "testMethod");
		long threadTotalTime = Profile.getThreadTotalTime(threadId);
		assertEquals(100, threadTotalTime);
	}

	@Test
	public void testSortFrameList() {
		Profile.start("TestClass1", "testMethod1");
		Profile.end("TestClass1", "testMethod1");
		Profile.start("TestClass2", "testMethod2");
		Profile.end("TestClass2", "testMethod2");
		Profile.sortFrameList((frame1, frame2) -> frame1.getBeginTime() - frame2.getBeginTime());
		Frame firstFrame = Profile._frameList.get(0);
		assertEquals("TestClass1", firstFrame.getClassName());
		assertEquals("testMethod1", firstFrame.getMethodName());
	}

	@Test
	public void testStartEnd() {
		Profile.start("TestClass", "testMethod");
		Profile.end("TestClass", "testMethod");
		assertTrue(methodFrame.isMeasured());
		assertTrue(methodFrame.getBeginTime() > 0);
		assertTrue(methodFrame.getEndTime() > 0);
		assertTrue(methodFrame.getNetTime() > 0);
	}

	@Test
	public void testBeginWaitEndWait() {
		Profile.start("TestClass", "testMethod");
		Thread.sleep(100);
		Profile.beginWait("TestClass", "testMethodWait");
		Thread.sleep(200);
		Profile.endWait("TestClass", "testMethodWait");
		assertTrue(methodFrame.getBeginWaitTime() > 0);
		assertTrue(methodFrame.getEndWaitTime() > 0);
		assertTrue(methodFrame.getWaitTime() > 0);
	}

	@Test
	public void testUnwind() {
		Profile.start("TestClass", "testMethod");
		Profile.end("TestClass", "testMethod");
		Profile.unwind("TestClass", "testMethodException");
		assertTrue(methodFrame.isExceptionHandled());
	}

	@Test
	public void testAlloc() {
		Profile.start("TestClass", "testMethod");
		Profile.alloc("TestClass");
		Profile.alloc("TestClass");
		Profile.end("TestClass", "testMethod");
		ClassAllocation ca = Profile._allocList.get("TestClass");
		assertEquals(3, ca.getAllocationCount());
	}
}
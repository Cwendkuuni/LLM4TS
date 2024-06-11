package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;
import com.mentorgen.tools.profile.output.ThreadDictionary;

public class ProfileTest {

	@Test
	public void testInitProfiler() {
		Profile.initProfiler();
		assertTrue(Profile._controller != null);
		assertFalse(Profile._controllerThread == null);
	}

	@Test
	public void testInit() {
		Profile.init();
		assertNotNull(Profile._threadDictionary);
		assertNotNull(Profile._frameList);
		assertNotNull(Profile._methodDictionary);
		assertNotNull(Profile._allocList);
		assertNotNull(Profile._lock);
	}

	@Test
	public void testClear() {
		Profile.init();
		Profile.clear();
		assertEquals(0, Profile._threadDictionary.size());
		assertEquals(0, Profile._frameList.size());
		assertEquals(0, Profile._methodDictionary.size());
		assertEquals(0, Profile._allocList.size());
	}

	@Test
	public void testGetThreadTotalTime() {
		Profile.init();
		long threadId = 1L;
		Profile.start();
		Thread.sleep(100);
		Profile.end("TestClassName", "testMethod");
		Profile.shutdown();
		assertEquals(100, Profile.getThreadTotalTime(threadId));
	}

	@Test
	public void testThreads() {
		Profile.init();
		Profile.start();
		Thread t1 = new Thread(() -> {});
		Thread t2 = new Thread(() -> {});
		Thread t3 = new Thread(() -> {});
		List<Long> threads = Profile.threads();
		assertEquals(3, threads.size());
		assertTrue(threads.contains(t1.getId()));
		assertTrue(threads.contains(t2.getId()));
		assertTrue(threads.contains(t3.getId()));
		Profile.shutdown();
	}

	@Test
	public void testInteractions() {
		Profile.init();
		Profile.start();
		Thread t1 = new Thread(() -> {
			start("TestClassName1", "testMethod1");
			start("TestClassName2", "testMethod2");
			end("TestClassName1", "testMethod1");
			end("TestClassName2", "testMethod2");
		});
		List<Frame> interactions = Profile.interactions(t1.getId());
		assertEquals(2, interactions.size());
		Frame interaction1 = interactions.get(0);
		Frame interaction2 = interactions.get(1);
		assertEquals("TestClassName1", interaction1.getClassName());
		assertEquals("testMethod1", interaction1.getMethodName());
		assertEquals("TestClassName2", interaction2.getClassName());
		assertEquals("testMethod2", interaction2.getMethodName());
		Profile.shutdown();
	}

	@Test
	public void testFrameList() {
		Profile.init();
		Profile.start();
		Thread t1 = new Thread(() -> {
			start("TestClassName1", "testMethod1");
			start("TestClassName2", "testMethod2");
			end("TestClassName1", "testMethod1");
			end("TestClassName2", "testMethod2");
		});
		Profile.shutdown();
		List<Frame> frameList = Profile.frameList();
		assertEquals(4, frameList.size());
		Frame frame1 = frameList.get(0);
		Frame frame2 = frameList.get(1);
		Frame frame3 = frameList.get(2);
		Frame frame4 = frameList.get(3);
		assertEquals("TestClassName1", frame1.getClassName());
		assertEquals("testMethod1", frame1.getMethodName());
		assertEquals("TestClassName2", frame2.getClassName());
		assertEquals("testMethod2", frame2.getMethodName());
		assertEquals(null, frame3.getParent());
		assertEquals(null, frame4.getParent());
	}

	@Test
	public void testAllocations() {
		Profile.init();
		Profile.start();
		new TestAllocationsClass();
		Profile.shutdown();
		List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
		assertEquals(1, allocations.size());
		assertEquals("TestAllocationsClass", allocations.get(0).getName());
		assertEquals(1, allocations.get(0).getAllocCount());
	}

	@Test(expected = AssertionError.class)
	public void testStartWithNullClassName() {
		Profile.init();
		Profile.start(null, null);
	}

	@Test(expected = AssertionError.class)
	public void testStartWithEmptyClassName() {
		Profile.init();
		Profile.start("", null);
	}

	@Test(expected = AssertionError.class)
	public void testStartWithNullMethodName() {
		Profile.init();
		Profile.start("TestClassName", null);
	}

	@Test(expected = AssertionError.class)
	public void testEndWithNullClassName() {
		Profile.init();
		Profile.end(null, null);
	}

	@Test(expected = AssertionError.class)
	public void testEndWithEmptyClassName() {
		Profile.init();
		Profile.end("", null);
	}

	@Test(expected = AssertionError.class)
	public void testEndWithNullMethodName() {
		Profile.init();
		Profile.end("TestClassName", null);
	}

	@Test
	public void testFindFrame() {
		Profile.init();
		Thread t1 = new Thread(() -> {
			start("TestClassName1", "testMethod1");
			start("TestClassName2", "testMethod2");
			start("TestClassName1", "testMethod1");
			end("TestClassName1", "testMethod1");
			end("TestClassName2", "testMethod2");
			end("TestClassName1", "testMethod1");
		});
		Frame target = Profile.findFrame(t1.getId(), "TestClassName1", "testMethod1");
		assertNotNull(target);
		assertEquals("TestClassName1", target.getClassName());
		assertEquals("testMethod1", target.getMethodName());
		assertEquals(3, target.getCallCount());
		Profile.shutdown();
	}

	@Test
	public void testFindFrameNull() {
		Profile.init();
		Thread t1 = new Thread(() -> {
			start("TestClassName1", "testMethod1");
			start("TestClassName2", "testMethod2");
			end("TestClassName2", "testMethod2");
		});
		Frame target = Profile.findFrame(t1.getId(), "TestClassName3", "testMethod3");
		assertNull(target);
		Profile.shutdown();
	}

	@Test
	public void testFindFrameEmptyClassName() {
		Profile.init();
		Thread t1 = new Thread(() -> {
			start("TestClassName1", "testMethod1");
			start("TestClassName2", "testMethod2");
			end("TestClassName2", "testMethod2");
		});
		Frame target = Profile.findFrame(t1.getId(), "", "testMethod3");
		assertNull(target);
		Profile.shutdown();
	}

	@Test
	public void testFindFrameEmptyMethodName() {
		Profile.init();
		Thread t1 = new Thread(() -> {
			start("TestClassName1", "testMethod1");
			start("TestClassName2", "testMethod2");
			end("TestClassName2", "testMethod2");
		});
		Frame target = Profile.findFrame(t1.getId(), "TestClassName1", "");
		assertNull(target);
		Profile.shutdown();
	}

	@Test(expected = AssertionError.class)
	public void testFindFrameWrongClassName() {
		Profile.init();
		Thread t1 = new Thread(() -> {
			start("TestClassName1", "testMethod1");
			start("TestClassName2", "testMethod2");
			end("TestClassName2", "testMethod2");
		});
		Frame target = Profile.findFrame(t1.getId(), "TestClassName3", "testMethod3");
		assertNull(target);
		Profile.shutdown();
	}

	@Test(expected = AssertionError.class)
	public void testFindFrameWrongMethodName() {
		Profile.init();
		Thread t1 = new Thread(() -> {
			start("TestClassName1", "testMethod1");
			start("TestClassName2", "testMethod2");
			end("TestClassName2", "testMethod2");
		});
		Frame target = Profile.findFrame(t1.getId(), "TestClassName1", "testMethod4");
		assertNull(target);
		Profile.shutdown();
	}

	private static class TestAllocationsClass {
		public TestAllocationsClass() {
			new TestAllocationsInnerClass();
		}

		private static class TestAllocationsInnerClass {
		}
	}
}
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;

public class ProfileTest {

	private static Profile profile;
	private static Frame frame;
	private static final String CLASS_NAME = "TestClass";
	private static final String METHOD_NAME = "testMethod";
	private static final String EXCEPTION = "TestException";
	private static final String FILENAME = "test.out";

	@BeforeClass
	public static void setUp() {
		Profile.initProfiler();
		Profile.init();
		profile = new Profile();
	}

	// Test init methods
	@Test
	public void testInitProfiler() {
		Profile.initProfiler();
		assertTrue(Profile._controller != null);
	}

	@Test
	public void testInit() {
		Profile.init();
		assertEquals(1001, Profile._threadActiveFrame.size());
		assertEquals(1001, Profile._threadDictionary.size());
		assertEquals(2003, Profile._methodDictionary.size());
		assertEquals(1001, Profile._frameList.size());
		assertEquals(0, Profile._allocList.size());
	}

	// Test clear method
	@Test
	public void testClear() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.clear();
		assertEquals(0, Profile._threadDictionary.size());
		assertEquals(0, Profile._frameList.size());
	}

	// Test threads method
	@Test
	public void testThreads() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		List<Long> threads = Profile.threads();
		assertEquals(2, threads.size());
	}

	// Test interactions method for a single thread
	@Test
	public void testInteractionsSingleThread() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
		assertEquals(1, interactions.size());
		assertEquals(CLASS_NAME, interactions.get(0).getClassName());
		assertEquals(METHOD_NAME, interactions.get(0).getMethodName());
	}

	// Test interactions method for multiple threads
	@Test
	public void testInteractionsMultipleThreads() {
		Profile.init();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Profile.start(CLASS_NAME, METHOD_NAME);
				Profile.end(CLASS_NAME, METHOD_NAME);
			}
		}).start();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		List<Frame> interactions1 = Profile.interactions(Thread.currentThread().getId());
		assertEquals(1, interactions1.size());

		List<Frame> interactions2 = Profile.interactions(Thread.currentThread().getId() + 1);
		assertEquals(1, interactions2.size());
	}

	// Test frameList method
	@Test
	public void testFrameList() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		List<Frame> frameList = Profile.frameList();
		assertEquals(2, frameList.size());
	}

	// Test allocations method
	@Test
	public void testAllocations() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.alloc(CLASS_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		List<ClassAllocation> allocations = new ArrayList<ClassAllocation>(Profile._allocList.values());
		assertEquals(1, allocations.size());
		assertEquals(CLASS_NAME, allocations.get(0).getClassName());
		assertEquals(1, allocations.get(0).getAllocCount());
	}

	// Test getThreadTotalTime method
	@Test
	public void testGetThreadTotalTime() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Thread.yield();
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.start(CLASS_NAME, METHOD_NAME);
		Thread.yield();
		Profile.end(CLASS_NAME, METHOD_NAME);
		long threadId = Thread.currentThread().getId();
		long totalTime = Profile.getThreadTotalTime(threadId);
		assertTrue(totalTime > 0);
	}

	// Test sortFrameList method
	@Test
	public void testSortFrameList() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Comparator<Frame> comparator = (f1, f2) -> f1.getBeginTime() - f2.getBeginTime();
		Profile.sortFrameList(comparator);
		List<Frame> sortedFrameList = Profile.frameList();
		Frame firstFrame = sortedFrameList.get(0);
		Frame secondFrame = sortedFrameList.get(1);
		assertTrue(firstFrame.getBeginTime() < secondFrame.getBeginTime());
	}

	// Test start method
	@Test(expected = AssertionError.class)
	public void testStartWithAlreadyStartedMethod() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.start(CLASS_NAME, METHOD_NAME);
	}

	// Test start method with null className or methodName
	@Test(expected = IllegalArgumentException.class)
	public void testStartWithNullClassNameOrMethodName() {
		Profile.init();
		Profile.start(null, METHOD_NAME);
		Profile.start(CLASS_NAME, null);
	}

	// Test end method with null className or methodName
	@Test(expected = IllegalArgumentException.class)
	public void testEndWithNullClassNameOrMethodName() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(null, METHOD_NAME);
		Profile.end(CLASS_NAME, null);
	}

	// Test findFrame method with no matching frame
	@Test
	public void testFindFrameNoMatchingFrame() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.start(CLASS_NAME, METHOD_NAME + "1");
		Profile.end(CLASS_NAME, METHOD_NAME + "1");
		Frame target = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME + "1");
		assertNull(target);
	}

	// Test findFrame method with matching parent frame
	@Test
	public void testFindFrameWithMatchingParentFrame() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME + "1");
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME + "1");
		Profile.end(CLASS_NAME, METHOD_NAME);
		Frame parentFrame = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME + "1");
		Frame target = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
		assertSame(parentFrame, target.getParent());
	}

	// Test unwind method with no matching frame
	@Test
	public void testUnwindNoMatchingFrame() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.unwind(CLASS_NAME, METHOD_NAME, EXCEPTION);
		assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
	}

	// Test unwind method with matching frame
	@Test
	public void testUnwindWithMatchingFrame() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Frame target = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
		Profile.unwind(CLASS_NAME, METHOD_NAME, EXCEPTION);
		assertSame(target, Profile._threadActiveFrame.get(Thread.currentThread().getId()));
	}

	// Test unwind method with null className or methodName
	@Test(expected = IllegalArgumentException.class)
	public void testUnwindWithNullClassNameOrMethodName() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.unwind(null, METHOD_NAME, EXCEPTION);
		Profile.unwind(CLASS_NAME, null, EXCEPTION);
	}

	// Test unwind method with null exception
	@Test(expected = NullPointerException.class)
	public void testUnwindWithNullException() {
		Profile.init();
		Profile.start(CLASS_NAME, METHOD_NAME);
		Profile.end(CLASS_NAME, METHOD_NAME);
		Profile.unwind(CLASS_NAME, METHOD_NAME, null);
	}

	// Test alloc method with null className
	@Test(expected = IllegalArgumentException.class)
	public void testAllocWithNullClassName() {
		Profile.init();
		Profile.alloc(null);
	}
}
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

 private static final String CLASS_NAME = "SomeClass";
 private static final String METHOD_NAME = "someMethod";
 private static final String FILENAME = "testProfile.out";
 private Profile underTest;

 @Before
 public void setUp() {
 Profile.init();
 underTest = new Profile();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 if (Profile._controllerThread != null) {
 assertTrue(Profile._controllerThread.isAlive());
 }
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertEquals(0, Profile.frameList().size());
 assertEquals(0, Profile.allocations().size());
 }

 @Test
 public void testStartAndEnd() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 List<Frame> frames = Profile.frameList();
 ClassAllocation allocation = Profile.findAllocation(CLASS_NAME);
 assertTrue(frames.size() > 0);
 assertTrue(allocation.getAllocationCount() > 0);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, METHOD_NAME);
 }

 @Test(expected = IllegalStateException.class)
 public void testEndWithoutStart() {
 Profile.end(CLASS_NAME, METHOD_NAME);
 }

 @Test
 public void testInteractions() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 List<Frame> frameInteractions = Profile.interactions(Thread.currentThread().getId());
 assertEquals(2, frameInteractions.size());
 }

 @Test
 public void testThreads() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, METHOD_NAME);
 List<Long> threadIds = Profile.threads();
 assertEquals(2, threadIds.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThreadsEmptyProfile() {
 Profile.clear();
 List<Long> threadIds = Profile.threads();
 }

 @Test
 public void testSortFrameList() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Collections.sort(Profile.frameList());
 Frame firstFrame = Profile.frameList().get(0);
 Frame secondFrame = Profile.frameList().get(1);
 assertTrue(firstFrame.getBeginTime() < secondFrame.getBeginTime());
 }

 @Test
 public void testGetThreadTotalTime() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 long threadId = Thread.currentThread().getId();
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertTrue(threadTotalTime > 0);
 }

 @Test
 public void testAllocations() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.alloc(CLASS_NAME);
 ClassAllocation allocation = Profile.findAllocation(CLASS_NAME);
 assertTrue(allocation.getAllocationCount() > 0);
 }

 @Test
 public void testFindAllocationNull() {
 ClassAllocation allocation = Profile.findAllocation(null);
 assertNull(allocation);
 }

 @Test
 public void testFindAllocationEmptyProfile() {
 ClassAllocation allocation = Profile.findAllocation(CLASS_NAME);
 assertNull(allocation);
 }

 @Test
 public void testFindAllocationNonExistentClass() {
 ClassAllocation allocation = Profile.findAllocation("NonExistentClass");
 assertNull(allocation);
 }

 @Test
 public void testFindAllocationMultipleAllocations() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.alloc(CLASS_NAME);
 Profile.alloc(CLASS_NAME);
 ClassAllocation allocation = Profile.findAllocation(CLASS_NAME);
 assertTrue(allocation.getAllocationCount() > 1);
 }

 @Test
 public void testBeginWaitEndWait() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.beginWait(CLASS_NAME, METHOD_NAME);
 Profile.endWait(CLASS_NAME, METHOD_NAME);
 List<Frame> interactionList = Profile.interactions(Thread.currentThread().getId());
 Frame waitingFrame = null;
 for (Frame frame : interactionList) {
 if (frame.getClassName().equals(CLASS_NAME) && frame.getMethodName().equals("beginWait")) {
 waitingFrame = frame;
 break;
 }
 }
 assertNotNull(waitingFrame);
 Frame endedWaitingFrame = null;
 for (Frame frame : interactionList) {
 if (frame.getClassName().equals(CLASS_NAME) && frame.getMethodName().equals("endWait")) {
 endedWaitingFrame = frame;
 break;
 }
 }
 assertNotNull(endedWaitingFrame);
 assertTrue(waitingFrame.getBeginTime() < endedWaitingFrame.getBeginTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginWaitWithoutEndWait() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.beginWait(CLASS_NAME, METHOD_NAME);
 }

 @Test
 public void testUnwind() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 try {
 throw new RuntimeException("Test Exception");
 } catch (RuntimeException e) {
 Profile.unwind(CLASS_NAME, METHOD_NAME, e.getClass().getName());
 }
 Frame unwindFrame = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(unwindFrame);
 }

 @Test
 public void testFindFrameNull() {
 Frame frame = Profile.findFrame(null, CLASS_NAME, METHOD_NAME);
 assertNull(frame);
 }

 @Test
 public void testFindFrameEmptyProfile() {
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNull(frame);
 }

 @Test
 public void testFindFrameNonExistentClass() {
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), "NonExistentClass", METHOD_NAME);
 assertNull(frame);
 }

 @Test
 public void testFindFrameNonExistentMethod() {
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, "NonExistentMethod");
 assertNull(frame);
 }

 @Test
 public void testFindFrameMultipleFrames() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.start(CLASS_NAME, "someOtherMethod");
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, "someOtherMethod");
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(frame);
 }

 @Test
 public void testShutdown() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.shutdown();
 assertFalse(Profile._controller.isRunning());
 }

 @Test
 public void testShutdownWithProfileData() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.shutdown();
 ProfileDump dump = new ProfileDump(FILENAME);
 assertTrue(dump.loadProfileData());
 }
}
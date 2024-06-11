package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.mentorgen.tools.profile.Controller;
import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

 private Profile profile;
 private Controller controllerMock;
 private ProfileDump profileDumpMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 profile = new Profile();
 controllerMock = mock(Controller.class);
 profileDumpMock = mock(ProfileDump.class);
 profile.setController(controllerMock);
 profile.setProfileDump(profileDumpMock);
 }

 @After
 public void teardown() {
 profile = null;
 controllerMock = null;
 profileDumpMock = null;
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 verify(controllerMock, times(1)).start();
 }

 @Test
 public void testInit() {
 Profile.init();
 assertEquals(1001, Profile._threadDictionary.size());
 assertEquals(0, Profile._frameList.size());
 assertEquals(2003, Profile._methodDictionary.size());
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertEquals(0, Profile._threadDictionary.size());
 assertEquals(0, Profile._frameList.size());
 assertEquals(0, Profile._methodDictionary.size());
 }

 @Test
 public void testStart() {
 Profile.start();
 verify(controllerMock, times(1)).start();
 }

 @Test
 public void testStop() {
 Profile.stop();
 verify(controllerMock, times(1)).stop();
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("test.txt");
 verify(profileDumpMock, times(1)).setFileName("test.txt");
 }

 @Test
 public void testShutdown() {
 Profile.shutdown();
 verify(controllerMock, times(1)).stop();
 }

 @Test
 public void testThreads() {
 Profile.init();
 Profile.start();
 Thread thread = new Thread(() -> {});
 thread.start();
 List<Long> threads = Profile.threads();
 assertEquals(1, threads.size());
 assertEquals(thread.getId(), threads.get(0));
 }

 @Test
 public void testInteractions() {
 Profile.init();
 Profile.start();
 Thread thread = new Thread(() -> {
 Frame frame1 = new Frame(null, new Method("TestClass", "method1"), 1L);
 Frame frame2 = new Frame(frame1, new Method("TestClass", "method2"), 2L);
 frame1.setParent(frame2);
 });
 thread.start();
 List<Frame> interactions = Profile.interactions(1L);
 assertEquals(2, interactions.size());
 assertSame(frame2, interactions.get(0));
 assertSame(frame1, interactions.get(1));
 }

 @Test
 public void testFrameList() {
 Profile.init();
 Profile.start();
 Thread thread = new Thread(() -> {
 Frame frame1 = new Frame(null, new Method("TestClass", "method1"), 1L);
 Frame frame2 = new Frame(frame1, new Method("TestClass", "method2"), 2L);
 frame1.setParent(frame2);
 });
 thread.start();
 List<Frame> frameList = Profile.frameList();
 assertEquals(1, frameList.size());
 assertSame(frame2, frameList.get(0));
 }

 @Test
 public void testAllocations() {
 Profile.init();
 Profile.start();
 Thread thread = new Thread(() -> {
 new TestClass();
 });
 thread.start();
 List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
 assertEquals(1, allocations.size());
 assertEquals("TestClass", allocations.get(0).getClassName());
 assertEquals(1, allocations.get(0).getAllocCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 Profile.init();
 Profile.start();
 Thread thread = new Thread(() -> {
 Frame frame = new Frame(null, new Method("TestClass", "method1"), 1L);
 frame.setBeginTime(System.nanoTime());
 frame.setEndTime(System.nanoTime());
 });
 thread.start();
 long totalTime = Profile.getThreadTotalTime(1L);
 assertEquals(0, totalTime);
 Frame frame = new Frame(null, new Method("TestClass", "method1"), 1L);
 frame.setBeginTime(System.nanoTime() - 1000);
 frame.setEndTime(System.nanoTime());
 Profile._threadDictionary.add(1L, frame);
 totalTime = Profile.getThreadTotalTime(1L);
 assertEquals(1000, totalTime);
 }

 @Test
 public void testSortFrameList() {
 Profile.init();
 Profile.start();
 Frame frame1 = new Frame(null, new Method("TestClass1", "method1"), 1L);
 Frame frame2 = new Frame(null, new Method("TestClass2", "method2"), 2L);
 Frame frame3 = new Frame(null, new Method("TestClass1", "method2"), 3L);
 Frame frame4 = new Frame(null, new Method("TestClass1", "method1"), 4L);
 Frame frame5 = new Frame(null, new Method("TestClass2", "method1"), 5L);
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 Profile._frameList.add(frame3);
 Profile._frameList.add(frame4);
 Profile._frameList.add(frame5);
 Collections.sort(Profile._frameList, new FrameComparator());
 assertEquals(5, Profile._frameList.size());
 assertEquals(frame5, Profile._frameList.get(0));
 assertEquals(frame4, Profile._frameList.get(1));
 assertEquals(frame3, Profile._frameList.get(2));
 assertEquals(frame2, Profile._frameList.get(3));
 assertEquals(frame1, Profile._frameList.get(4));
 }

 @Test
 public void testStartMethod() {
 Frame parentFrame = new Frame(null, new Method("TestClass", "method1"), 1L);
 Frame childFrame = new Frame(parentFrame, new Method("TestClass", "method2"), 2L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), parentFrame);
 Profile.start("TestClass", "method2");
 assertSame(childFrame, Profile._threadActiveFrame.get(Thread.currentThread().getId()));
 }

 @Test(expected = IllegalStateException.class)
 public void testStartMethodNoParent() {
 Frame childFrame = new Frame(null, new Method("TestClass", "method2"), 2L);
 Profile.start("TestClass", "method2");
 verify(controllerMock, times(1)).startMethod(anyString(), anyString());
 assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
 }

 @Test(expected = IllegalStateException.class)
 public void testStartMethodWrongParent() {
 Frame parentFrame = new Frame(null, new Method("TestClass1", "method1"), 1L);
 Frame childFrame = new Frame(parentFrame, new Method("TestClass2", "method2"), 2L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), parentFrame);
 Profile.start("TestClass1", "method3");
 }

 @Test
 public void testEndMethod() {
 Frame parentFrame = new Frame(null, new Method("TestClass", "method1"), 1L);
 Frame childFrame = new Frame(parentFrame, new Method("TestClass", "method2"), 2L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), parentFrame);
 Profile.start("TestClass", "method2");
 Profile.end("TestClass", "method2");
 assertNull(Profile._threadActiveFrame.get(Thread.currentThread().getId()));
 }

 @Test(expected = IllegalStateException.class)
 public void testEndMethodNoChild() {
 Frame parentFrame = new Frame(null, new Method("TestClass", "method1"), 1L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), parentFrame);
 Profile.end("TestClass", "method2");
 }

 @Test(expected = IllegalStateException.class)
 public void testEndMethodWrongChild() {
 Frame parentFrame = new Frame(null, new Method("TestClass1", "method1"), 1L);
 Frame childFrame1 = new Frame(parentFrame, new Method("TestClass", "method2"), 2L);
 Frame childFrame2 = new Frame(parentFrame, new Method("TestClass1", "method3"), 3L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), parentFrame);
 Profile.end("TestClass1", "method3");
 }

 @Test
 public void testBeginWait() {
 Frame frame = new Frame(null, new Method("TestClass", "method1"), 1L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), frame);
 Profile.beginWait("TestClass", "method1");
 assertEquals(true, frame.isWaiting());
 }

 @Test
 public void testEndWait() {
 Frame frame = new Frame(null, new Method("TestClass", "method1"), 1L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), frame);
 Profile.beginWait("TestClass", "method1");
 Profile.endWait("TestClass", "method1");
 assertEquals(false, frame.isWaiting());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginWaitNoWaiting() {
 Frame frame = new Frame(null, new Method("TestClass", "method1"), 1L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), frame);
 Profile.beginWait("TestClass", "method2");
 }

 @Test(expected = IllegalStateException.class)
 public void testEndWaitNoWaiting() {
 Frame frame = new Frame(null, new Method("TestClass", "method1"), 1L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), frame);
 Profile.endWait("TestClass", "method2");
 }

 @Test
 public void testUnwind() {
 Frame parentFrame = new Frame(null, new Method("TestClass1", "method1"), 1L);
 Frame childFrame1 = new Frame(parentFrame, new Method("TestClass2", "method1"), 2L);
 Frame childFrame2 = new Frame(childFrame1, new Method("TestClass3", "method1"), 3L);
 Frame childFrame3 = new Frame(childFrame1, new Method("TestClass3", "method2"), 4L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), childFrame3);
 Profile.unwind("TestClass3", "method2", "Exception");
 assertEquals(childFrame2, Profile._threadActiveFrame.get(Thread.currentThread().getId()));
 }

 @Test(expected = IllegalStateException.class)
 public void testUnwindNoChild() {
 Frame frame = new Frame(null, new Method("TestClass", "method1"), 1L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), frame);
 Profile.unwind("TestClass", "method2", "Exception");
 }

 @Test(expected = IllegalStateException.class)
 public void testUnwindWrongChild() {
 Frame parentFrame = new Frame(null, new Method("TestClass1", "method1"), 1L);
 Frame childFrame1 = new Frame(parentFrame, new Method("TestClass2", "method1"), 2L);
 Frame childFrame2 = new Frame(parentFrame, new Method("TestClass3", "method1"), 3L);
 Frame childFrame3 = new Frame(childFrame1, new Method("TestClass3", "method2"), 4L);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), childFrame3);
 Profile.unwind("TestClass1", "method1", "Exception");
 }

 @Test
 public void testAlloc() {
 Frame frame = new Frame(null, new Method("TestClass", "method1"), 1L);
 Profile.alloc("TestClass");
 assertEquals(1, Profile._allocList.get("TestClass").getAllocCount());
 }

 @Test
 public void testRun() {
 profile.run();
 verifyNoMoreInteractions(controllerMock);
 verifyNoMoreInteractions(profileDumpMock);
 }
}
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.Controller;
import com.mentorgen.tools.profile.Frame;
import com.mentorgen.tools.profile.Method;
import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;
import com.mentorgen.tools.profile.runtime.ThreadDictionary.FrameComparator;

public class ProfileTest {

 private Profile underTest;
 private Controller controllerMock;
 private Frame frameMock;
 private ClassAllocation classAllocationMock;
 private ThreadDictionary threadDictionaryMock;

 @Before
 public void setUp() {
 controllerMock = mock(Controller.class);
 frameMock = mock(Frame.class);
 classAllocationMock = mock(ClassAllocation.class);
 threadDictionaryMock = mock(ThreadDictionary.class);
 underTest = new Profile();
 underTest.setController(controllerMock);
 underTest._threadDictionary = threadDictionaryMock;
 }

 @Test
 public void testInitProfiler() {
    when(controllerMock.isRemote()).thenReturn(false);
    underTest.initProfiler();
    verify(controllerMock, times(1)).start();
    verify(controllerMock, times(1)).setFileName("default.prof");
  }

 @Test
 public void testInitProfilerWithRemoteController() {
    when(controllerMock.isRemote()).thenReturn(true);
    underTest.initProfiler();
    verify(controllerMock, times(1)).start();
    verify(controllerThreadMock, times(1)).start();
  }

 @Test
 public void testInit() {
    underTest.init();
    assertEquals(1001, underTest._threadActiveFrame.size());
    assertEquals(1001, underTest._threadDictionary.size());
    assertEquals(2003, underTest._methodDictionary.size());
    assertEquals(1, underTest._frameList.size());
    assertEquals(0, underTest._allocList.size());
  }

 @Test
 public void testClear() {
    underTest.init();
    underTest.clear();
    assertEquals(0, underTest._threadActiveFrame.size());
    assertEquals(0, underTest._threadDictionary.size());
    assertEquals(0, underTest._methodDictionary.size());
    assertEquals(0, underTest._frameList.size());
    assertEquals(0, underTest._allocList.size());
  }

 @Test
 public void testStart() {
    underTest.init();
    underTest.start();
    verify(controllerMock, times(1)).start();
  }

 @Test
 public void testStop() {
    underTest.init();
    underTest.start();
    underTest.stop();
    verify(controllerMock, times(1)).stop();
  }

 @Test
 public void testSetFileName() {
    underTest.init();
    underTest.setFileName("testFileName.prof");
    verify(controllerMock, times(1)).setFileName("testFileName.prof");
  }

 @Test
 public void testShutdown() {
    underTest.init();
    underTest.start();
    underTest.shutdown();
    Thread dictionaryThread = underTest._controllerThread;
    verify(controllerMock, times(1)).stop();
    verify(dictionaryThread, times(1)).interrupt();
  }

 @Test
 public void testThreads() {
    underTest.init();
    underTest.start();
    List<Long> threads = underTest.threads();
    assertTrue(threads.size() > 0);
  }

 @Test
 public void testInteractions() {
    underTest.init();
    underTest.start();
    Frame frame = mock(Frame.class);
    when(threadDictionaryMock.getMostRecentFrame(anyLong())).thenReturn(frame);
    List<Frame> interactions = underTest.interactions(1L);
    assertEquals(1, interactions.size());
    assertEquals(frame, interactions.get(0));
  }

 @Test
 public void testFrameList() {
    underTest.init();
    underTest.start();
    Frame frame = mock(Frame.class);
    when(threadDictionaryMock.getMostRecentFrame(anyLong())).thenReturn(frame);
    List<Frame> frameList = underTest.frameList();
    assertEquals(1, frameList.size());
    assertEquals(frame, frameList.get(0));
  }

 @Test
 public void testAllocations() {
    underTest.init();
    underTest.start();
    ClassAllocation allocationMock = mock(ClassAllocation.class);
    when(allocationMock.getName()).thenReturn("TestClass");
    when(allocationMock.getAllocCount()).thenReturn(10);
    when(allocList.get("TestClass")).thenReturn(allocationMock);
    underTest._allocList = new HashMap<String, ClassAllocation>();
    List<ClassAllocation> allocations = underTest.allocations();
    assertEquals(1, allocations.size());
    assertEquals(allocationMock, allocations.get(0));
  }

 @Test
 public void testGetThreadTotalTime() {
    underTest.init();
    underTest.start();
    long threadId = 1L;
    Frame frame = mock(Frame.class);
    when(threadDictionaryMock.getMostRecentFrame(threadId)).thenReturn(frame);
    when(frame.getEndTime()).thenReturn(System.nanoTime());
    long threadTotalTime = underTest.getThreadTotalTime(threadId);
    assertEquals(-1, threadTotalTime);
  }

 @Test
 public void testSortFrameList() {
    underTest.init();
    underTest.start();
    Frame frame1 = mock(Frame.class);
    Frame frame2 = mock(Frame.class);
    when(frame1.getBeginTime()).thenReturn(10);
    when(frame2.getBeginTime()).thenReturn(5);
    List<Frame> frameList = new ArrayList<Frame>();
    frameList.add(frame1);
    frameList.add(frame2);
    underTest.sortFrameList(new FrameComparator());
    Frame sortedFrame1 = frameList.get(0);
    Frame sortedFrame2 = frameList.get(1);
    assertEquals(sortedFrame2, frame1);
    assertEquals(sortedFrame1, frame2);
  }

 @Test
 public void testStartMethod() {
    underTest.init();
    underTest.start();
    String className = "TestClass";
    String methodName = "testMethod";
    underTest.start(className, methodName);
    Frame frame = mock(Frame.class);
    when(threadDictionaryMock.getMostRecentFrame(anyLong())).thenReturn(frame);
    verify(threadDictionaryMock, times(1)).add(anyLong(), eq(frame));
    verify(frame, times(1)).overhead(anyLong());
    verify(frame, times(1)).setBeginTime(anyLong());
  }

 @Test
 public void testEndMethod() {
    underTest.init();
    underTest.start();
    String className = "TestClass";
    String methodName = "testMethod";
    underTest.start(className, methodName);
    Frame parentFrameMock = mock(Frame.class);
    Frame childFrameMock = mock(Frame.class);
    when(parentFrameMock.getChild(anyString(), anyString())).thenReturn(childFrameMock);
    when(threadDictionaryMock.getMostRecentFrame(anyLong())).thenReturn(childFrameMock);
    Frame frame = mock(Frame.class);
    when(findFrame(anyLong(), eq(className), eq(methodName))).thenReturn(frame);
    underTest.end(className, methodName);
    verify(threadDictionaryMock, times(1)).put(anyLong(), eq(frame));
    verify(threadActiveFrame, times(1)).put(anyLong(), eq(parentFrameMock));
  }

 @Test
 public void testBeginWaitMethod() {
    underTest.init();
    underTest.start();
    String className = "TestClass";
    String methodName = "testMethod";
    underTest.beginWait(className, methodName);
    Frame frame = mock(Frame.class);
    when(threadDictionaryMock.getMostRecentFrame(anyLong())).thenReturn(frame);
    verify(frame, times(1)).beginWait(anyLong());
  }

 @Test
 public void testEndWaitMethod() {
    underTest.init();
    underTest.start();
    String className = "TestClass";
    String methodName = "testMethod";
    underTest.beginWait(className, methodName);
    Frame frame = mock(Frame.class);
    when(threadDictionaryMock.getMostRecentFrame(anyLong())).thenReturn(frame);
    underTest.endWait(className, methodName);
    verify(frame, times(1)).endWait(anyLong());
  }

 @Test
 public void testUnwindMethod() {
    underTest.init();
    underTest.start();
    String className = "TestClass";
    String methodName = "testMethod";
    String exception = "TestException";
    underTest.unwind(className, methodName, exception);
    Frame frame = mock(Frame.class);
    when(threadDictionaryMock.getMostRecentFrame(anyLong())).thenReturn(frame);
    verify(threadActiveFrame, times(1)).put(anyLong(), eq(frame));
  }

 @Test
 public void testAllocMethod() {
    underTest.init();
    underTest.start();
    String className = "TestClass";
    underTest.alloc(className);
    ClassAllocation allocationMock = mock(ClassAllocation.class);
    when(allocationMock.getName()).thenReturn(className);
    when(allocationMock.getAllocCount()).thenReturn(1);
    when(allocList.get(className)).thenReturn(allocationMock);
    underTest._allocList = new HashMap<String, ClassAllocation>();
    underTest.alloc(className);
    verify(allocList, times(1)).put(anyString(), eq(allocationMock));
  }
}
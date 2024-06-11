package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.runtime.Controller;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

 private Controller mockController;
 private ThreadDictionary mockThreadDictionary;
 private Frame mockFrame;
 private Iterable<Frame> mockFrameList;
 private Iterable<ClassAllocation> mockAllocList;
 private Profile systemUnderTest;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockController = mock(Controller.class);
 mockThreadDictionary = mock(ThreadDictionary.class);
 mockFrame = mock(Frame.class);
 mockFrameList = mock(Iterable.class);
 mockAllocList = mock(Iterable.class);
 systemUnderTest = new Profile();
 System.setOut(mock(PrintStream.class)); // Silence the console output
 }

 @After
 public void teardown() {
 System.setOut(System.out);
 }

 @Test
 public void testInitProfiler() {
 // This method doesn't return anything, we'll just check that it is called
 verify(mockController).start();
 systemUnderTest.initProfiler();
 }

 @Test
 public void testInit() {
 // This method doesn't return anything, we'll just check that it is called
 verify(mockController).close();
 systemUnderTest.init();
 }

 @Test
 public void testClear() {
 // This method doesn't return anything, we'll just check that it is called
 verify(mockController).close();
 systemUnderTest.clear();
 }

 @Test
 public void testStart() {
 // This method doesn't return anything, we'll just check that it is called
 verify(mockController).start();
 systemUnderTest.start();
 }

 @Test
 public void testStop() {
 // This method doesn't return anything, we'll just check that it is called
 verify(mockController).stop();
 systemUnderTest.stop();
 }

 @Test
 public void testSetFileName() {
 String fileName = "output.txt";
 systemUnderTest.setFileName(fileName);
 verify(mockController).setFileName(fileName);
 }

 @Test
 public void testShutdown() {
 // This method doesn't return anything, we'll just check that it is called
 verify(mockController).close();
 systemUnderTest.shutdown();
 }

 @Test
 public void testThreads() {
 List<Long> threads = new ArrayList<>();
 threads.add(1L);
 threads.add(2L);
 when(mockThreadDictionary.threads()).thenReturn(threads);
 assertEquals(threads, systemUnderTest.threads());
 }

 @Test
 public void testInteractions() {
 long threadId = 1L;
 Frame interaction1 = mock(Frame.class);
 Frame interaction2 = mock(Frame.class);
 when(mockThreadDictionary.interactions(threadId)).thenReturn(Arrays.asList(interaction1, interaction2));
 List<Frame> interactions = new ArrayList<>();
 interactions.add(interaction1);
 interactions.add(interaction2);
 assertEquals(interactions, systemUnderTest.interactions(threadId));
 }

 @Test
 public void testFrameList() {
 Frame frame1 = mock(Frame.class);
 Frame frame2 = mock(Frame.class);
 when(mockFrameList.iterator().next()).thenReturn(frame1);
 when(mockFrameList.size()).thenReturn(2);
 when(systemUnderTest.frameList()).thenReturn(mockFrameList);
 List<Frame> frameList = new ArrayList<>();
 frameList.add(frame1);
 assertEquals(frameList, systemUnderTest.frameList());
 }

 @Test
 public void testAllocations() {
 String className = "TestClass";
 ClassAllocation alloc = new ClassAllocation(className);
 when(mockAllocList.iterator().next()).thenReturn(alloc);
 when(systemUnderTest.allocations()).thenReturn(mockAllocList);
 List<ClassAllocation> allocations = new ArrayList<>();
 allocations.add(alloc);
 assertEquals(allocations, systemUnderTest.allocations());
 }

 @Test
 public void testGetThreadTotalTime() {
 long threadId = 1L;
 long threadTotalTime = 100;
 when(mockThreadDictionary.getThreadTotalTime(threadId)).thenReturn(threadTotalTime);
 assertEquals(threadTotalTime, systemUnderTest.getThreadTotalTime(threadId));
 }

 @Test
 public void testSortFrameList() {
 Comparator<Frame> comparator = (f1, f2) -> Long.compare(f1.getThreadId(), f2.getThreadId());
 systemUnderTest.sortFrameList(comparator);
 verify(mockFrameList).sort(comparator);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSortFrameListWithNullComparator() {
 systemUnderTest.sortFrameList(null);
 }

 @Test
 public void testStartWithValidMethodNameAndClassName() {
 String className = "TestClass";
 String methodName = "testMethod";
 long threadId = 1L;
 Method method = new Method(className, methodName);
 Frame parent = mock(Frame.class);
 Frame target = mock(Frame.class);
 when(mockThreadDictionary.getMostRecentFrame(threadId)).thenReturn(parent);
 when(parent.getChild(method)).thenReturn(target);
 when(mockThreadDictionary.add(threadId, target)).thenReturn(true);
 when(mockFrameList.add(target)).thenReturn(true);
 when(systemUnderTest.findFrame(threadId, className, methodName)).thenReturn(target);
 systemUnderTest.start(className, methodName);
 verify(mockController).start();
 verify(mockThreadDictionary).add(threadId, target);
 verify(mockFrameList).add(target);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartWithNullMethodNameOrClassName() {
 systemUnderTest.start(null, "testMethod");
 systemUnderTest.start("TestClass", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartWithEmptyMethodNameOrClassName() {
 systemUnderTest.start("", "testMethod");
 systemUnderTest.start("TestClass", "");
 }

 @Test
 public void testEndWithValidMethodNameAndClassName() {
 String className = "TestClass";
 String methodName = "testMethod";
 long threadId = 1L;
 Frame target = mock(Frame.class);
 when(mockThreadDictionary.getMostRecentFrame(threadId)).thenReturn(target);
 when(systemUnderTest.findFrame(threadId, className, methodName)).thenReturn(target);
 systemUnderTest.end(className, methodName);
 verify(target).setEndTime(anyLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndWithNullMethodNameOrClassName() {
 systemUnderTest.end(null, "testMethod");
 systemUnderTest.end("TestClass", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndWithEmptyMethodNameOrClassName() {
 systemUnderTest.end("", "testMethod");
 systemUnderTest.end("TestClass", "");
 }

 @Test
 public void testBeginWaitWithValidMethodNameAndClassName() {
 String className = "TestClass";
 String methodName = "testMethod";
 long threadId = 1L;
 Frame target = mock(Frame.class);
 when(mockThreadDictionary.getMostRecentFrame(threadId)).thenReturn(target);
 when(systemUnderTest.findFrame(threadId, className, methodName)).thenReturn(target);
 systemUnderTest.beginWait(className, methodName);
 verify(target).beginWait(anyLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBeginWaitWithNullMethodNameOrClassName() {
 systemUnderTest.beginWait(null, "testMethod");
 systemUnderTest.beginWait("TestClass", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBeginWaitWithEmptyMethodNameOrClassName() {
 systemUnderTest.beginWait("", "testMethod");
 systemUnderTest.beginWait("TestClass", "");
 }

 @Test
 public void testEndWaitWithValidMethodNameAndClassName() {
 String className = "TestClass";
 String methodName = "testMethod";
 long threadId = 1L;
 Frame target = mock(Frame.class);
 when(mockThreadDictionary.getMostRecentFrame(threadId)).thenReturn(target);
 when(systemUnderTest.findFrame(threadId, className, methodName)).thenReturn(target);
 systemUnderTest.endWait(className, methodName);
 verify(target).endWait(anyLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndWaitWithNullMethodNameOrClassName() {
 systemUnderTest.endWait(null, "testMethod");
 systemUnderTest.endWait("TestClass", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndWaitWithEmptyMethodNameOrClassName() {
 systemUnderTest.endWait("", "testMethod");
 systemUnderTest.endWait("TestClass", "");
 }

 @Test
 public void testUnwindWithValidExceptionAndMethodNameAndClassName() {
 String className = "TestClass";
 String methodName = "testMethod";
 String exception = "TestException";
 long threadId = 1L;
 Frame target = mock(Frame.class);
 when(mockThreadDictionary.getMostRecentFrame(threadId)).thenReturn(target);
 when(systemUnderTest.findFrame(threadId, className, methodName)).thenReturn(target);
 when(target.getClass()).thenReturn(className.getClass());
 when(target.getMethodName()).thenReturn(methodName);
 when(target.getException()).thenReturn(exception);
 systemUnderTest.unwind(className, methodName, exception);
 verify(target, times(2)).setEndTime(anyLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnwindWithNullMethodNameOrClassName() {
 systemUnderTest.unwind(null, "testMethod", "TestException");
 systemUnderTest.unwind("TestClass", null, "TestException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnwindWithEmptyMethodNameOrClassName() {
 systemUnderTest.unwind("", "testMethod", "TestException");
 systemUnderTest.unwind("TestClass", "", "TestException");
 }

 @Test
 public void testUnwindWithNullException() {
 systemUnderTest.unwind("TestClass", "testMethod", null);
 }

 @Test
 public void testAllocWithValidClassName() {
 String className = "TestClass";
 systemUnderTest.alloc(className);
 verify(mockAllocList).iterator().next();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAllocWithNullClassName() {
 systemUnderTest.alloc(null);
 }
}
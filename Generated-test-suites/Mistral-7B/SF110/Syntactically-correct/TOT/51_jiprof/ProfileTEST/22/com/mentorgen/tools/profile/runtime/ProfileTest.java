package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

 private static final String CLASS_NAME = "com.example.MyClass";
 private static final String METHOD_NAME_1 = "method1";
 private static final String METHOD_NAME_2 = "method2";
 private static final String THREAD_ID = "thread1";
 private static final long START_TIME = 1234567890L;
 private static final long END_TIME = 2345678901L;
 private static final long WAIT_START_TIME = 3456789012L;
 private static final long WAIT_END_TIME = 4567890123L;
 private static final String EXCEPTION_CLASS_NAME = "java.lang.Exception";

 private Profile underTest;
 private Map<String, Method> methodDictionary;
 private Map<Long, Frame> threadDictionary;
 private Frame frame1;
 private Frame frame2;
 private ClassAllocation allocList;

 @Before
 public void setUp() {
 underTest = new Profile();
 methodDictionary = mock(Map.class);
 threadDictionary = new HashMap<>();
 frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME_1), Thread.currentThread().getId());
 frame2 = new Frame(frame1, new Method(CLASS_NAME, METHOD_NAME_2), Thread.currentThread().getId());
 allocList = new ClassAllocation(CLASS_NAME);
 when(methodDictionary.get(anyString())).thenReturn(new Method(CLASS_NAME, ""));
 when(threadDictionary.getMostRecentFrame(anyLong())).thenReturn(frame1);
 when(threadDictionary.threads()).thenReturn(Collections.singletonList(THREAD_ID));
 when(threadDictionary.interactions(eq(THREAD_ID))).thenReturn(Arrays.asList(frame1, frame2));
 when(threadDictionary.getThreadTotalTime(eq(THREAD_ID))).thenReturn(END_TIME - START_TIME);
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 verify(underTest).init();
 }

 @Test
 public void testClear() {
 Profile.clear();
 verify(underTest).init();
 }

 @Test
 public void testStart() {
 Profile.start();
 verify(underTest._controller).start();
 }

 @Test
 public void testStop() {
 Profile.stop();
 verify(underTest._controller).stop();
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("output.txt");
 verify(underTest._controller).setFileName("output.txt");
 }

 @Test
 public void testShutdown() {
 Profile.shutdown();
 verify(methodDictionary, times(2)).put(anyString(), any(Method.class));
 verify(frame1).close();
 verify(frame2).close();
 }

 @Test
 public void testThreads() {
 List<Long> threads = Profile.threads();
 assertEquals(1, threads.size());
 assertEquals(THREAD_ID, threads.get(0));
 }

 @Test
 public void testInteractions() {
 List<Frame> interactions = Profile.interactions(THREAD_ID);
 assertEquals(2, interactions.size());
 assertEquals(frame1, interactions.get(0));
 assertEquals(frame2, interactions.get(1));
 }

 @Test
 public void testFrameList() {
 List<Frame> frameList = Profile.frameList();
 assertEquals(2, frameList.size());
 assertEquals(frame1, frameList.get(0));
 assertEquals(frame2, frameList.get(1));
 }

 @Test
 public void testAllocations() {
 List<ClassAllocation> allocations = new ArrayList<>(Profile.allocations());
 assertEquals(1, allocations.size());
 assertEquals(CLASS_NAME, allocations.get(0).getName());
 }

 @Test
 public void testGetThreadTotalTime() {
 long threadTotalTime = Profile.getThreadTotalTime(THREAD_ID);
 assertEquals(END_TIME - START_TIME, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 Profile.sortFrameList(null);
 verify(frame1).setBeginTime(START_TIME);
 verify(frame2).setBeginTime(START_TIME);
 verify(frame1).setEndTime(END_TIME);
 verify(frame2).setEndTime(END_TIME);
 }

 @Test
 public void testStartMethod() {
 Profile.start(CLASS_NAME, METHOD_NAME_1);
 verify(methodDictionary).put(METHOD_NAME_1, new Method(CLASS_NAME, METHOD_NAME_1));
 verify(threadDictionary).add(THREAD_ID, frame1);
 verify(frame1).overhead(anyLong());
 }

 @Test
 public void testEndMethod() {
 Profile.end(CLASS_NAME, METHOD_NAME_1);
 verify(frame1).setEndTime(END_TIME);
 }

 @Test
 public void testBeginWait() {
 Profile.beginWait(CLASS_NAME, METHOD_NAME_1);
 verify(frame1).beginWait(WAIT_START_TIME);
 }

 @Test
 public void testEndWait() {
 Profile.endWait(CLASS_NAME, METHOD_NAME_1);
 verify(frame1).endWait(WAIT_END_TIME);
 }

 @Test
 public void testUnwind() {
 Profile.unwind(CLASS_NAME, METHOD_NAME_1, EXCEPTION_CLASS_NAME);
 verify(frame1).setEndTime(END_TIME);
 }

 @Test
 public void testAlloc() {
 Profile.alloc(CLASS_NAME);
 verify(allocList).incAllocCount();
 }
}
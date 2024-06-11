package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.mentorgen.tools.profile.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.io.*;

public class ProfileTest {

 private Profile underTest;
 private Map<String, Method> methodDictionary;
 private Frame frame;
 private Thread thread;
 private Controller controller;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 underTest = new Profile();
 methodDictionary = mock(Map.class);
 when(underTest._methodDictionary).thenReturn(methodDictionary);
 frame = mock(Frame.class);
 thread = mock(Thread.class);
 when(thread.getId()).thenReturn(1L);
 controller = mock(Controller.class);
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 verify(controller).start();
 }

 @Test
 public void testClear() {
 underTest.clear();
 verify(methodDictionary).clear();
 }

 @Test
 public void testStart() {
 underTest.start();
 verify(controller).start();
 }

 @Test
 public void testStop() {
 underTest.stop();
 verify(controller).stop();
 }

 @Test
 public void testSetFileName() {
 underTest.setFileName("test.txt");
 verify(controller).setFileName("test.txt");
 }

 @Test
 public void testShutdown() throws IOException {
 underTest.shutdown();
 verify(methodDictionary).values().forEach(f -> f.close());
 verify(controller).close();
 }

 @Test
 public void testThreads() {
 List<Long> threads = underTest.threads();
 assertEquals(1, threads.size());
 assertEquals(1L, threads.get(0));
 }

 @Test
 public void testInteractions() {
 List<Frame> interactions = underTest.interactions(1L);
 assertEquals(1, interactions.size());
 assertEquals(frame, interactions.get(0));
 }

 @Test
 public void testFrameList() {
 List<Frame> frameList = underTest.frameList();
 assertEquals(0, frameList.size());
 }

 @Test
 public void testGetThreadTotalTime() {
 when(methodDictionary.get("TestClass.testMethod")).thenReturn(frame);
 long threadTotalTime = underTest.getThreadTotalTime(1L);
 assertEquals(0, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 List<Frame> frameList = Arrays.asList(frame, frame, frame);
 underTest.sortFrameList(null);
 verify(frameList).toString();
 }

 @Test
 public void testStartMethod() {
 underTest.start("TestClass", "testMethod");
 verify(methodDictionary).put("TestClass.testMethod", any(Method.class));
 }

 @Test
 public void testEndMethod() {
 underTest.start("TestClass", "testMethod");
 underTest.end("TestClass", "testMethod");
 verify(frame).setEndTime(anyLong());
 }

 @Test
 public void testBeginWait() {
 underTest.start("TestClass", "testMethodWaiting");
 underTest.beginWait("TestClass", "testMethodWaiting");
 verify(frame).beginWait(anyLong());
 }

 @Test
 public void testEndWait() {
 underTest.start("TestClass", "testMethodWaiting");
 underTest.beginWait("TestClass", "testMethodWaiting");
 underTest.endWait("TestClass", "testMethodWaiting");
 verify(frame).endWait(anyLong());
 }

 @Test
 public void testUnwind() {
 underTest.start("TestClass", "testMethod");
 underTest.unwind("TestClass", "testMethodException");
 verify(frame).setEndTime(anyLong());
 }

 @Test
 public void testAlloc() {
 underTest.alloc("TestClass");
 verify(methodDictionary).put("TestClass.<init>", any(Method.class));
 }
}
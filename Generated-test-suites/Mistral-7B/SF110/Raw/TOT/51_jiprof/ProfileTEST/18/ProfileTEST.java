package com.mentorgen.tools.profile.runtime;
 import static org.junit.Assert.*;
 import static org.mockito.Mockito.*;

 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;

 import org.junit.Before;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.mockito.MockitoAnnotations;
 import org.powermock.api.mockito.PowerMockito;
 import org.powermock.core.classloader.annotations.PrepareForTest;
 import org.powermock.modules.junit4.PowerMockRunner;

 @RunWith(PowerMockRunner.class)
 @PrepareForTest({Profile.class})
 public class ProfileTest {

 private static final String CLASS_NAME = "com.example.MyTestClass";
 private static final String METHOD_NAME = "someMethod";
 private static final String FILENAME = "output.txt";

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 PowerMockito.mockStatic(Profile.class);
 }

 @Test
 public void testInitProfiler() {
 // Given
 Profile.init();
 Profile.initProfiler();

 // Then
 verify(Profile.class, times(1)).init();
 verify(Profile.class, times(1)).initProfiler();
 }

 @Test
 public void testClear() {
 // Given
 Profile.init();
 Profile.clear();

 // Then
 assertEquals(0, Profile._threadDictionary.size());
 assertEquals(0, Profile._frameList.size());
 assertEquals(0, Profile._methodDictionary.size());
 assertEquals(0, Profile._allocList.size());
 }

 @Test
 public void testInitTwice() {
 // Given
 Profile.init();
 Profile.init();

 // Then
 assertEquals(1, Profile._threadDictionary.size());
 }

 @Test
 public void testStartAndStop() {
 // Given
 Profile.init();
 Profile.start();
 Profile.stop();

 // Then
 verify(Profile.class, times(1)).start();
 verify(Profile.class, times(1)).stop();
 }

 @Test
 public void testSetFileName() {
 // Given
 Profile.init();
 Profile.setFileName(FILENAME);

 // Then
 assertEquals(FILENAME, Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 // Given
 Profile.init();
 Profile.shutdown();

 // Then
 verify(Profile.class, times(1)).shutdown();
 }

 @Test
 public void testThreads() {
 // Given
 Profile.init();
 Profile.start();
 Thread thread1 = new Thread(() -> {
 start(CLASS_NAME, METHOD_NAME);
 });
 Thread thread2 = new Thread(() -> {
 start(CLASS_NAME, METHOD_NAME);
 });
 thread1.start();
 thread2.start();

 // When
 List<Long> threads = Profile.threads();

 // Then
 assertEquals(2, threads.size());
 assertTrue(threads.containsAll(Arrays.asList(thread1.getId(), thread2.getId())));
 }

 @Test
 public void testInteractions() {
 // Given
 Profile.init();
 Profile.start();
 Thread thread1 = new Thread(() -> {
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 });
 Thread thread2 = new Thread(() -> {
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 });
 thread1.start();
 thread2.start();

 // When
 List<Frame> interactions = Profile.interactions(thread1.getId());

 // Then
 assertEquals(4, interactions.size());
 }

 @Test
 public void testFrameList() {
 // Given
 Profile.init();
 Profile.start();
 Thread thread1 = new Thread(() -> {
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 });
 Thread thread2 = new Thread(() -> {
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 });
 thread1.start();
 thread2.start();

 // When
 List<Frame> frameList = Profile.frameList();

 // Then
 assertEquals(6, frameList.size());
 }

 @Test
 public void testAllocations() {
 // Given
 Profile.init();
 Profile.start();
 alloc(CLASS_NAME);
 alloc(CLASS_NAME);
 alloc(CLASS_NAME);
 alloc(CLASS_NAME);
 alloc(CLASS_NAME);
 alloc(CLASS_NAME);

 // When
 Set<String> allocations = new HashSet<>(Profile._allocList.keySet());

 // Then
 assertEquals(1, allocations.size());
 assertTrue(allocations.contains(CLASS_NAME));
 }

 @Test
 public void testGetThreadTotalTime() {
 // Given
 Profile.init();
 Profile.start();
 Thread thread1 = new Thread(() -> {
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 });
 Thread thread2 = new Thread(() -> {
 start(CLASS_NAME, METHOD_NAME);
 start(CLASS_NAME, METHOD_NAME);
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 });
 thread1.start();
 thread2.start();

 // When
 long thread1TotalTime = Profile.getThreadTotalTime(thread1.getId());
 long thread2TotalTime = Profile.getThreadTotalTime(thread2.getId());

 // Then
 assertTrue(thread1TotalTime > 0);
 assertTrue(thread2TotalTime > 0);
 assertFalse(thread1TotalTime == thread2TotalTime);
 }

 @Test
 public void testSortFrameList() {
 // Given
 Profile.init();
 Profile.start();
 Thread thread1 = new Thread(() -> {
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 });
 Thread thread2 = new Thread(() -> {
 start(CLASS_NAME, METHOD_NAME);
 start(CLASS_NAME, METHOD_NAME);
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 });
 thread1.start();
 thread2.start();

 // When
 Collections.sort(Profile._frameList, (f1, f2) -> Long.compare(f1.getBeginTime(), f2.getBeginTime()));

 // Then
 Frame firstFrame = Profile._frameList.get(0);
 Frame lastFrame = Profile._frameList.get(Profile._frameList.size() - 1);
 assertTrue(firstFrame.getBeginTime() < lastFrame.getBeginTime());
 }

 @Test
 public void testStartEnd() {
 // Given
 Profile.init();
 Profile.start();

 // When
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);

 // Then
 assertTrue(Profile._threadActiveFrame.containsKey(Thread.currentThread().getId()));
 }

 @Test(expected = RuntimeException.class)
 public void testStartEndTwice() {
 // Given
 Profile.init();
 Profile.start();

 // When
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);

 // Then
 // Exception is thrown as expected
 }

 @Test
 public void testBeginWaitEndWait() {
 // Given
 Profile.init();
 Profile.start();

 // When
 beginWait(CLASS_NAME, METHOD_NAME);
 endWait(CLASS_NAME, METHOD_NAME);

 // Then
 assertTrue(Profile._threadActiveFrame.containsKey(Thread.currentThread().getId()));
 }

 @Test
 public void testUnwind() {
 // Given
 Profile.init();
 Profile.start();

 // When
 try {
 throw new RuntimeException("Test exception");
 } catch (RuntimeException e) {
 unwind(CLASS_NAME, METHOD_NAME, e.getClass().getName());
 }

 // Then
 assertTrue(Profile._threadActiveFrame.containsKey(Thread.currentThread().getId()));
 }
 }
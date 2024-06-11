package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.*;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {

 private static final String TEST_CLASS_NAME = "TestClass";
 private static final String TEST_METHOD_NAME = "testMethod";

 @BeforeClass
 public static void setupClass() {
 Profile.initProfiler();
 }

 @AfterClass
 public static void teardownClass() {
 Profile.shutdown();
 }

 @Before
 public void setup() {
 Profile.clear();
 }

 @Test
 public void testInitProfiler() {
 // Assert that initProfiler prints the correct message
 verify(System.err, times(1)).println("Java Interactive Profiler: starting");
 // Assert that initProfiler initializes the necessary data structures
 assertEquals(0, Profile._frameList.size());
 assertEquals(0, Profile._threadDictionary.size());
 assertEquals(0, Profile._methodDictionary.size());
 assertEquals(0, Profile._allocList.size());
 }

 @Test
 public void testInit() {
 // Assert that init initializes the necessary data structures
 assertEquals(0, Profile._frameList.size());
 assertEquals(0, Profile._threadDictionary.size());
 assertEquals(0, Profile._methodDictionary.size());
 assertEquals(0, Profile._allocList.size());
 }

 @Test
 public void testClear() {
 // Assert that clear initializes the necessary data structures
 assertEquals(0, Profile._frameList.size());
 assertEquals(0, Profile._threadDictionary.size());
 assertEquals(0, Profile._methodDictionary.size());
 assertEquals(0, Profile._allocList.size());
 }

 @Test
 public void testStart() {
 // Create a mock controller
 Controller mockController = mock(Controller.class);
 when(mockController.start()).thenReturn(true);
 Profile._controller = mockController;
 // Assert that start returns true
 assertTrue(Profile.start());
 // Assert that the controller's start method was called
 verify(mockController).start();
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 // Create a mock controller
 Controller mockController = mock(Controller.class);
 when(mockController.start()).thenReturn(true);
 Profile._controller = mockController;
 // Assert that the first call to start returns true
 assertTrue(Profile.start());
 // Assert that the second call to start throws an IllegalStateException
 Profile.start();
 verify(mockController, times(1)).start();
 verify(mockController, never()).stop();
 }

 @Test(expected = IllegalStateException.class)
 public void testStartWithoutInit() {
 // Assert that the call to start throws an IllegalStateException when initProfiler was not called
 assertThrows(IllegalStateException.class, () -> Profile.start());
 }

 @Test
 public void testStop() {
 // Create a mock controller
 Controller mockController = mock(Controller.class);
 when(mockController.stop()).thenReturn(true);
 Profile._controller = mockController;
 // Assert that stop returns true
 assertTrue(Profile.stop());
 // Assert that the controller's stop method was called
 verify(mockController).stop();
 }

 @Test(expected = IllegalStateException.class)
 public void testStopTwice() {
 // Create a mock controller
 Controller mockController = mock(Controller.class);
 when(mockController.stop()).thenReturn(true);
 Profile._controller = mockController;
 // Assert that the first call to stop returns true
 assertTrue(Profile.stop());
 // Assert that the second call to stop throws an IllegalStateException
 Profile.stop();
 verify(mockController, times(1)).stop();
 verify(mockController, times(1)).close();
 }

 @Test(expected = IllegalStateException.class)
 public void testStopWithoutStart() {
 // Assert that the call to stop throws an IllegalStateException when start was not called
 assertThrows(IllegalStateException.class, () -> Profile.stop());
 }

 @Test
 public void testSetFileName() {
 // Assert that setFileName sets the filename correctly
 Profile.setFileName("test.out");
 assertEquals("test.out", Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 // Create a mock output writer
 OutputWriter mockOutputWriter = mock(OutputWriter.class);
 when(mockOutputWriter.writeToFile(anyString())).thenReturn(true);
 Profile._controller.setOutputWriter(mockOutputWriter);
 // Assert that shutdown closes all frames and writes the data to a file
 Profile.shutdown();
 verify(mockOutputWriter, times(1)).writeToFile(anyString());
 }

 @Test
 public void testThreads() {
 // Assert that threads returns an empty iterable when no threads are running
 assertEmpty(Profile.threads());
 // Create a new thread
 Thread testThread = new Thread(()->{});
 testThread.start();
 // Assert that threads returns the newly created thread
 List<Long> threads = new ArrayList<>(Profile.threads());
 assertEquals(1, threads.size());
 assertEquals(testThread.getId(), threads.get(0));
 }

 @Test
 public void testInteractions() {
 // Create a new thread and start a method
 Thread testThread = new Thread(()->{
 start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 });
 testThread.start();
 try {
 Thread.sleep(10);
 } catch (InterruptedException e) {
 // Assert that interactions returns an empty iterable when no interactions have been recorded
 assertEmpty(Profile.interactions(testThread.getId()));
 // End the method
 end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 // Assert that interactions returns the expected frame
 Frame interaction = Profile.interactions(testThread.getId()).iterator().next();
 assertEquals(TEST_CLASS_NAME, interaction.getClassName());
 assertEquals(TEST_METHOD_NAME, interaction.getMethodName());
 }
 }

 @Test
 public void testFrameList() {
 // Assert that frameList returns an empty iterable when no frames have been recorded
 assertEmpty(Profile.frameList());
 // Start and end a method
 start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 // Assert that frameList returns the expected frame
 Frame frame = Profile.frameList().iterator().next();
 assertEquals(TEST_CLASS_NAME, frame.getClassName());
 assertEquals(TEST_METHOD_NAME, frame.getMethodName());
 }

 @Test
 public void testAllocations() {
 // Assert that allocations returns an empty iterable when no allocations have been made
 assertEmpty(Profile.allocations());
 // Allocate an object
 alloc(TEST_CLASS_NAME);
 // Assert that allocations returns the expected allocation
 ClassAllocation allocation = Profile.allocations().iterator().next();
 assertEquals(TEST_CLASS_NAME, allocation.getClassName());
 }

 @Test
 public void testGetThreadTotalTime() {
 // Create a new thread and start a method
 Thread testThread = new Thread(()->{
 start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 });
 testThread.start();
 try {
 Thread.sleep(10);
 end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 } catch (InterruptedException e) {
 // Assert that getThreadTotalTime returns 0 when no time has been recorded
 long threadTotalTime = Profile.getThreadTotalTime(testThread.getId());
 assertEquals(0, threadTotalTime);
 }
 // Allocate an object and start a method again
 alloc(TEST_CLASS_NAME);
 start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 try {
 Thread.sleep(10);
 end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 } catch (InterruptedException e) {
 // Assert that getThreadTotalTime returns the expected time
 long threadTotalTime = Profile.getThreadTotalTime(testThread.getId());
 assertTrue(threadTotalTime > 0);
 }
 }

 @Test
 public void testSortFrameList() {
 // Start and end a method multiple times
 start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 // Sort the frame list
 Collections.sort(Profile.frameList(), new FrameComparator());
 // Assert that the sorted frame list contains the expected frames in the correct order
 List<Frame> sortedFrames = new ArrayList<>(Profile.frameList());
 assertEquals(TEST_CLASS_NAME, sortedFrames.get(0).getClassName());
 assertEquals(TEST_METHOD_NAME, sortedFrames.get(0).getMethodName());
 assertEquals(TEST_CLASS_NAME, sortedFrames.get(1).getClassName());
 assertEquals(TEST_METHOD_NAME, sortedFrames.get(1).getMethodName());
 assertEquals(TEST_CLASS_NAME, sortedFrames.get(2).getClassName());
 assertEquals(TEST_METHOD_NAME, sortedFrames.get(2).getMethodName());
 }

 @Test
 public void testStartWithException() {
 // Create a mock controller that throws an exception
 Controller mockController = mock(Controller.class);
 when(mockController.start()).thenThrow(new RuntimeException("Test exception"));
 Profile._controller = mockController;
 // Assert that starting a method with an exception throws an UnwindingException
 try {
 start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 fail("Expected UnwindingException");
 } catch (UnwindingException e) {
 // Assert that the controller's start method was called once
 verify(mockController, times(1)).start();
 }
 }

 @Test
 public void testEndWithException() {
 // Create a mock controller that throws an exception
 Controller mockController = mock(Controller.class);
 when(mockController.end(anyString(), anyString())).thenThrow(new RuntimeException("Test exception"));
 Profile._controller = mockController;
 // Assert that ending a method with an exception throws an UnwindingException
 try {
 end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 fail("Expected UnwindingException");
 } catch (UnwindingException e) {
 // Assert that the controller's end method was called once
 verify(mockController, times(1)).end(eq(TEST_CLASS_NAME), eq(TEST_METHOD_NAME));
 }
 }
}
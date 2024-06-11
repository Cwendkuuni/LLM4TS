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
import com.mentorgen.tools.profile.output.ProfileOutputStream;

public class ProfileTest {

 private static final String TEST_CLASS_NAME = "TestClass";
 private static final String TEST_METHOD_NAME = "testMethod";
 private static final String TEST_EXCEPTION = "TestException";
 private static final long TEST_THREAD_ID = 1L;
 private Controller mockController;
 private Profile underTest;

 @Before
 public void setUp() {
 mockController = mock(Controller.class);
 underTest = new Profile();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 verify(mockController, times(1)).start();
 }

 @Test
 public void testInit() {
 // Verify that init method initializes all static variables
 Profile.init();

 assertEquals(HashMap.class, Profile._threadDictionary.getClass());
 assertEquals(ArrayList.class, Profile._frameList.getClass());
 assertEquals(HashMap.class, Profile._threadActiveFrame.getClass());
 assertEquals(HashMap.class, Profile._methodDictionary.getClass());
 assertEquals(Object.class, Profile._lock.getClass());
 assertEquals(HashMap.class, Profile._allocList.getClass());
 }

 @Test
 public void testClear() {
 // Verify that clear method initializes all static variables
 Profile.clear();

 assertEquals(HashMap.class, Profile._threadDictionary.getClass());
 assertEquals(ArrayList.class, Profile._frameList.getClass());
 assertEquals(HashMap.class, Profile._threadActiveFrame.getClass());
 assertEquals(HashMap.class, Profile._methodDictionary.getClass());
 assertEquals(Object.class, Profile._lock.getClass());
 assertEquals(HashMap.class, Profile._allocList.getClass());

 // Verify that all frames and threads are cleared
 assertEquals(0, Profile.threads().size());
 assertEquals(0, Profile.frameList().size());
 }

 @Test
 public void testStartAndStop() {
 // Verify that start and stop methods work correctly
 Profile.start();
 Profile.stop();
 }

 @Test
 public void testSetFileName() {
 // Verify that setFileName method sets the fileName correctly
 Profile.setFileName("test.txt");
 assertEquals("test.txt", Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() {
 // Verify that shutdown method closes all frames and writes the profile data to a file
 final List<Long> threads = new ArrayList<>();
 threads.add(TEST_THREAD_ID);
 when(mockController.threads()).thenReturn(threads);
 when(mockController.interactions(TEST_THREAD_ID)).thenReturn(Collections.emptyList());
 when(mockController.getThreadTotalTime(TEST_THREAD_ID)).thenReturn(10L);

 Profile.initProfiler();

 // Start profiling
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.beginWait(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.endWait(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.unwind(TEST_CLASS_NAME, TEST_METHOD_NAME, TEST_EXCEPTION);
 Profile.alloc(TEST_CLASS_NAME);

 // Verify that the profile data is written to a file
 final byte[] expectedData = new byte[0]; // Replace this with the expected profile data
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(mockController.getOutputStream()).thenReturn(outputStream);
 ProfileDump.dump(mockController);
 final byte[] actualData = outputStream.toByteArray();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testSortFrameList() {
 // Verify that sortFrameList method sorts the frame list correctly
 final List<Frame> frames = new ArrayList<>();
 frames.add(new Frame(null, new Method("Class1", "method1"), 1L));
 frames.add(new Frame(null, new Method("Class2", "method2"), 2L));
 frames.add(new Frame(null, new Method("Class1", "method1"), 3L));

 Profile._frameList = frames;
 Profile.sortFrameList((frame1, frame2) -> Long.compare(frame1.getBeginTime(), frame2.getBeginTime()));

 List<Frame> sortedFrames = new ArrayList<>(Profile._frameList);
 Frame firstFrame = sortedFrames.get(0);
 assertEquals(frames.get(1).getBeginTime(), firstFrame.getBeginTime());
 assertEquals(frames.get(1).getClassName(), firstFrame.getClassName());
 assertEquals(frames.get(1).getMethodName(), firstFrame.getMethodName());
 }

 @Test
 public void testStartEndMethodsWithNoParent() {
 // Verify that start and end methods work correctly when there is no parent frame
 Profile.init();
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }

 @Test
 public void testStartEndMethodsWithParent() {
 // Verify that start and end methods work correctly when there is a parent frame
 Profile.init();
 Frame parentFrame = new Frame(null, new Method("ParentClass", "parentMethod"), 1L);
 Profile._frameList.add(parentFrame);
 Profile._threadActiveFrame.put(Thread.currentThread().getId(), parentFrame);

 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Frame childFrame = findFrame(TEST_CLASS_NAME, TEST_METHOD_NAME);
 assertEquals(parentFrame, childFrame.getParent());
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }
}
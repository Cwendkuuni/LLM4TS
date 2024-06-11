package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProfileTest {

 private static final String CLASS_NAME = "TestClass";
 private static final String METHOD_NAME = "testMethod";
 private static final String EXCEPTION = "TestException";
 private static final long THREAD_ID = 1L;
 private static final String FILENAME = "test.prof";
 private static final Controller MOCK_CONTROLLER = mock(Controller.class);
 private static final Profile MOCK_PROFILE = mock(Profile.class);
 private static final ThreadDictionary MOCK_THREAD_DICTIONARY = mock(ThreadDictionary.class);
 private static final Frame MOCK_FRAME = mock(Frame.class);
 private static final ClassAllocation MOCK_CLASS_ALLOCATION = mock(ClassAllocation.class);

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(MOCK_PROFILE.getThreadDictionary()).thenReturn(MOCK_THREAD_DICTIONARY);
 when(MOCK_THREAD_DICTIONARY.getMostRecentFrame(THREAD_ID)).thenReturn(MOCK_FRAME);
 when(MOCK_FRAME.getClassName()).thenReturn(CLASS_NAME);
 when(MOCK_FRAME.getMethodName()).thenReturn(METHOD_NAME);
 }

 @After
 public void teardown() {
 reset(MOCK_CONTROLLER, MOCK_PROFILE, MOCK_THREAD_DICTIONARY, MOCK_FRAME, MOCK_CLASS_ALLOCATION);
 }

 @Test
 public void testInitProfiler() {
 assertEquals("Java Interactive Profiler: starting", System.err.getErr());
 verify(MOCK_PROFILE, times(1)).init();
 verify(MOCK_PROFILE, times(1)).start();
 }

 @Test
 public void testInit() {
 assertEquals(1001, Profile._threadDictionary.size());
 assertEquals(2003, Profile._methodDictionary.size());
 assertEquals(0, Profile._frameList.size());
 assertEquals(0, Profile._threadActiveFrame.size());
 }

 @Test
 public void testClear() {
 assertEquals(0, Profile._threadDictionary.size());
 assertEquals(0, Profile._methodDictionary.size());
 assertEquals(0, Profile._frameList.size());
 assertEquals(0, Profile._threadActiveFrame.size());
 }

 @Test
 public void testStart() {
 assertTrue(MOCK_CONTROLLER.isRunning());
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 Profile.start();
 Profile.start();
 fail("Expected IllegalStateException");
 }

 @Test
 public void testStop() {
 assertFalse(MOCK_CONTROLLER.isRunning());
 }

 @Test
 public void testSetFileName() {
 assertEquals(FILENAME, Profile._controller.getFileName());
 }

 @Test
 public void testShutdown() throws IOException {
 assertEquals(1, Profile._threadDictionary.size());
 verify(MOCK_FRAME, times(1)).close();
 verify(ProfileDump.class, times(1)).dump();
 }

 @Test
 public void testThreads() {
 assertEquals(1, Profile.threads().spliterator().getExactSizeIfPresent());
 }

 @Test
 public void testInteractions() {
 List<Frame> interactions = Profile.interactions(THREAD_ID);
 assertEquals(1, interactions.size());
 assertEquals(CLASS_NAME, interactions.get(0).getClassName());
 assertEquals(METHOD_NAME, interactions.get(0).getMethodName());
 }

 @Test
 public void testFrameList() {
 List<Frame> frameList = Profile.frameList();
 assertEquals(0, frameList.size());
 }

 @Test
 public void testAllocations() {
 List<ClassAllocation> allocations = new ArrayList<>(Profile.allocations());
 assertEquals(0, allocations.size());
 }

 @Test
 public void testGetThreadTotalTime() {
 long threadTotalTime = Profile.getThreadTotalTime(THREAD_ID);
 assertEquals(0, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 List<Frame> frameList = new ArrayList<>(Profile.frameList());
 Collections.sort(frameList, any(Comparator.class));
 }

 @Test
 public void testStartEnd() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 verify(MOCK_FRAME, times(1)).setEndTime(anyLong());
 }

 @Test
 public void testBeginEndWait() {
 Profile.beginWait(CLASS_NAME, METHOD_NAME);
 Profile.endWait(CLASS_NAME, METHOD_NAME);
 verify(MOCK_FRAME, times(1)).endWait(anyLong());
 }

 @Test
 public void testUnwind() {
 Profile.unwind(CLASS_NAME, METHOD_NAME, EXCEPTION);
 verify(MOCK_FRAME, times(1)).setEndTime(anyLong());
 }

 @Test
 public void testAlloc() {
 Profile.alloc(CLASS_NAME);
 verify(MOCK_CLASS_ALLOCATION, times(1)).incAllocCount();
 }

 @Test
 public void testRun() throws IOException {
 Profile.initProfiler();
 Profile.run();
 }
}
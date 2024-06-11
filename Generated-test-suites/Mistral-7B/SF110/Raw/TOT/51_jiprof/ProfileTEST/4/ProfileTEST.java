package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.*;
import org.mockito.*;

public class ProfileTest {

 private static final String CLASS_NAME = "com.example.TestClass";
 private static final String METHOD_NAME = "testMethod";
 private static final String FILENAME = "test.txt";

 @BeforeClass
 public static void setUpClass() {
 Profile.initProfiler();
 Profile.init();
 }

 @AfterClass
 public static void tearDownClass() {
 Profile.shutdown();
 }

 @Before
 public void setUp() {
 Profile.clear();
 }

 @After
 public void tearDown() {
 Profile.clear();
 }

 @Test
 public void testInitProfilerAndThreadCreation() {
 Profile.initProfiler();
 assertEquals(1, Profile.threads().size());
 }

 @Test
 public void testStartAndStop() {
 Profile.start();
 Profile.stop();
 assertEquals(0, Profile.threads().size());
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName(FILENAME);
 assertTrue(Profile._controller.getFileName().equals(FILENAME));
 }

 @Test
 public void testClear() {
 Profile.init();
 Profile.start();
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.clear();
 assertEquals(0, Profile.threads().size());
 }

 @Test
 public void testIterableFrames() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 List<Frame> frames = new ArrayList<>(Profile.frameList());
 assertEquals(1, frames.size());
 }

 @Test
 public void testIterableAllocation() {
 Profile.alloc(CLASS_NAME);
 List<ClassAllocation> allocations = new ArrayList<>(Profile.allocations());
 assertEquals(1, allocations.size());
 }

 @Test
 public void testGetThreadTotalTime() {
 Profile.start(CLASS_NAME, METHOD_NAME);
 Thread thread = mock(Thread.class);
 when(thread.getId()).thenReturn(1L);
 Profile._threadDictionary.add(1L, new Frame(null, new Method(CLASS_NAME, METHOD_NAME), 1L));
 long threadTotalTime = Profile.getThreadTotalTime(1L);
 assertEquals(0, threadTotalTime);
 }

 @Test(expected = NullPointerException.class)
 public void testFindFrameWithNullClassName() {
 Profile.findFrame(Thread.currentThread().getId(), null, METHOD_NAME);
 }

 @Test(expected = NullPointerException.class)
 public void testFindFrameWithNullMethodName() {
 Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, null);
 }

 @Test
 public void testFindFrame() {
 Profile.init();
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Frame target = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(target);
 }

 @Test
 public void testUnwind() {
 Profile.init();
 Profile.start(CLASS_NAME, METHOD_NAME);
 Profile.end(CLASS_NAME, METHOD_NAME);
 Profile.unwind(CLASS_NAME, METHOD_NAME, "ExceptionClass");
 Frame target = Profile.findFrame(Thread.currentThread().getId(), CLASS_NAME, METHOD_NAME);
 assertNotNull(target);
 }

 @Test
 public void testAlloc() {
 Profile.init();
 Profile.alloc(CLASS_NAME);
 ClassAllocation ca = Profile._allocList.get(CLASS_NAME);
 assertEquals(1, ca.getAllocCount());
 }
}
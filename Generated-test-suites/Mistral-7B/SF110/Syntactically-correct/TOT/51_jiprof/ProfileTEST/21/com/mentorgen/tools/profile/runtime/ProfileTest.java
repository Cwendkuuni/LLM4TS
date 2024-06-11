package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

public class ProfileTest {

 private static final String CLASS_NAME = "com.example.TestClass";
 private static final String METHOD_NAME = "testMethod";
 private static final String FILENAME = "test.txt";
 private static final long THREAD_ID = 1L;
 private static final String EXCEPTION_CLASS_NAME = "java.lang.NullPointerException";

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 public static void shutdown() {
 Profile.shutdown();
 }

 @Before
 public void resetMockController() {
 Controller controllerMock = mock(Controller.class);
 Profile._controller = controllerMock;
 }

 @Test
 public void testThreads() {
 // Given
 Profile.init();
 // When
 Profile.initProfiler();
 // Then
 assertEquals(0, Profile.threads().size());
 // When
 Profile.start();
 // Then
 assertEquals(1, Profile.threads().size());
 // When
 Profile.stop();
 // Then
 assertEquals(0, Profile.threads().size());
 }

 @Test
 public void testInteractions() {
 // Given
 Profile.init();
 Profile.initProfiler();
 Profile.start();
 // When
 Profile.start(CLASS_NAME, METHOD_NAME);
 // Then
 List<Frame> interactions = Profile.interactions(THREAD_ID);
 assertEquals(1, interactions.size());
 Frame interaction = interactions.get(0);
 assertEquals(CLASS_NAME, interaction.getClassName());
 assertEquals(METHOD_NAME, interaction.getMethodName());
 // When
 Profile.end(CLASS_NAME, METHOD_NAME);
 // Then
 interactions = Profile.interactions(THREAD_ID);
 assertEquals(0, interactions.size());
 }

 @Test
 public void testAllocations() {
 // Given
 Profile.init();
 Profile.initProfiler();
 // When
 Profile.alloc(CLASS_NAME);
 // Then
 int allocCount = Profile.allocList().stream()
 .filter(ca -> ca.getClassname().equals(CLASS_NAME))
 .map(ca -> ca.getAllocCount())
 .findFirst()
 .orElse(0);
 assertEquals(1, allocCount);
 // When
 Profile.alloc(CLASS_NAME);
 // Then
 allocCount = Profile.allocList().stream()
 .filter(ca -> ca.getClassname().equals(CLASS_NAME))
 .map(ca -> ca.getAllocCount())
 .findFirst()
 .orElse(0);
 assertEquals(2, allocCount);
 }

 @Test
 public void testFileName() {
 // Given
 Profile.init();
 Profile.initProfiler();
 // When
 Profile.setFileName(FILENAME);
 // Then
 assertEquals(FILENAME, Profile._controller.getFileName());
 }

 @Test
 public void testStartAndStop() {
 // Given
 Profile.init();
 Profile.initProfiler();
 // When
 Profile.start();
 // Then
 assertTrue(Profile._controller.isRunning());
 // When
 Profile.stop();
 // Then
 assertFalse(Profile._controller.isRunning());
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 // Given
 Profile.init();
 Profile.initProfiler();
 Profile.start();
 // When
 Profile.start();
 // Then
 fail("Expected IllegalStateException but got none");
 }

 @Test
 public void testDebugStart() {
 // Given
 Profile._debugStart = true;
 Profile.init();
 Profile.initProfiler();
 // When
 Profile.start(CLASS_NAME, METHOD_NAME);
 // Then
 String debugOutput = System.out.getHistogram().get(0).toString();
 assertTrue(debugOutput.contains(CLASS_NAME + " : " + METHOD_NAME));
 }

 @Test
 public void testDebugException() {
 // Given
 Profile._debugException = true;
 Profile.init();
 Profile.initProfiler();
 // When
 try {
 throw new NullPointerException();
 } catch (NullPointerException e) {
 Profile.unwind(CLASS_NAME, METHOD_NAME, e.getClass().getName());
 }
 // Then
 String debugOutput = System.err.getHistogram().get(0).toString();
 assertTrue(debugOutput.contains("Detected an exception at " + CLASS_NAME + "." + METHOD_NAME));
 }

 @Test
 public void testEndWait() {
 // Given
 Profile.init();
 Profile.initProfiler();
 // When
 Profile.start();
 // Then
 assertFalse(Profile._controller.isWaiting());
 // When
 Profile.beginWait(CLASS_NAME, METHOD_NAME);
 // Then
 assertTrue(Profile._controller.isWaiting());
 // When
 Profile.endWait(CLASS_NAME, METHOD_NAME);
 // Then
 assertFalse(Profile._controller.isWaiting());
 }

 @Test
 public void testStartEndMethodWithException() {
 // Given
 Profile._debugException = true;
 Profile.init();
 Profile.initProfiler();
 // When
 try {
 Profile.start(CLASS_NAME, METHOD_NAME);
 throw new NullPointerException();
 } catch (NullPointerException e) {
 Profile.end(CLASS_NAME, METHOD_NAME);
 }
 // Then
 ArgumentCaptor<String> exceptionClassNameCapture = ArgumentCaptor.forClass(String.class);
 verify(Profile._controller).printExceptionMessage(exceptionClassNameCapture.capture());
 assertEquals(EXCEPTION_CLASS_NAME, exceptionClassNameCapture.getValue());
 }

 @Rule
 public ExpectedException expectedException = ExpectedException.none();

 @Test
 public void testStartMethodWithException() {
 // Given
 expectedException.expect(IllegalStateException.class);
 // When
 Profile.start(CLASS_NAME, METHOD_NAME);
 throw new NullPointerException();
 // Then
 // Not reachable
 }
}
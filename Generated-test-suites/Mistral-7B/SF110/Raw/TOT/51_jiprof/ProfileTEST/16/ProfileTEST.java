package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.*;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.mockito.MockitoAnnotations;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

@Category(IntegrationTest.class)
public class ProfileTest {

 private static final String CLASS_NAME = "com.example.TestClass";
 private static final String METHOD_NAME = "testMethod";
 private static final String FILENAME = "output.txt";

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 public static void shutdown() {
 Profile.shutdown();
 }

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @After
 public void tearDown() {
 clearMocks();
 }

 @Test
 public void testInitAndShutdown() {
 initProfiler();
 assertTrue(Profile._controllerThread.isAlive());
 shutdown();
 assertFalse(Profile._controllerThread.isAlive());
 }

 @Test
 public void testInitTwice() {
 initProfiler();
 initProfiler();
 assertEquals(1, Profile._threadDictionary.size());
 }

 @Test
 public void testClear() {
 initProfiler();
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 clear();
 assertEquals(0, Profile._threadDictionary.size());
 }

 @Test
 public void testStartAndStop() {
 initProfiler();
 start();
 assertTrue(Profile._controller.isRunning());
 stop();
 assertFalse(Profile._controller.isRunning());
 }

 @Test
 public void testSetFileName() {
 initProfiler();
 Profile.setFileName(FILENAME);
 assertTrue(new File(FILENAME).exists());
 }

 @Test
 public void testThreads() {
 initProfiler();
 runTestClass();
 assertEquals(1, Profile.threads().size());
 }

 @Test
 public void testInteractions() {
 initProfiler();
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
 assertEquals(1, interactions.size());
 }

 @Test
 public void testFrameList() {
 initProfiler();
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 List<Frame> frameList = Profile.frameList();
 assertEquals(1, frameList.size());
 }

 @Test
 public void testAllocations() {
 initProfiler();
 runTestClass();
 assertEquals(1, Profile.allocations().size());
 }

 @Test
 public void testGetThreadTotalTime() {
 initProfiler();
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 long threadId = Thread.currentThread().getId();
 long totalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(0, totalTime);
 }

 @Test
 public void testSortFrameList() {
 initProfiler();
 runTestClass();
 Profile.sortFrameList((f1, f2) -> f1.getBeginTime() - f2.getBeginTime());
 Frame firstFrame = Profile.frameList().iterator().next();
 Frame lastFrame = Profile.frameList().iterator().next();
 long firstBeginTime = firstFrame.getBeginTime();
 long lastBeginTime = lastFrame.getBeginTime();
 assertTrue(firstBeginTime < lastBeginTime);
 }

 @Test
 public void testStartAndEnd() {
 initProfiler();
 start(CLASS_NAME, METHOD_NAME);
 end(CLASS_NAME, METHOD_NAME);
 Frame frame = findFrame(CLASS_NAME, METHOD_NAME);
 assertNotNull(frame);
 }

 @Test
 public void testBeginWaitAndEndWait() {
 initProfiler();
 start(CLASS_NAME, METHOD_NAME);
 beginWait(CLASS_NAME, METHOD_NAME);
 endWait(CLASS_NAME, METHOD_NAME);
 Frame frame = findFrame(CLASS_NAME, METHOD_NAME);
 assertNotNull(frame);
 assertTrue(frame.isWaiting());
 }

 @Test
 public void testUnwind() {
 initProfiler();
 start(CLASS_NAME, METHOD_NAME);
 try {
 throw new Exception();
 } catch (Exception e) {
 unwind(CLASS_NAME, METHOD_NAME, e.getClass().getName());
 Frame frame = findFrame(CLASS_NAME, METHOD_NAME);
 assertNotNull(frame);
 }
 }

 @Test
 public void testAlloc() {
 initProfiler();
 runTestClass();
 ClassAllocation allocation = findClassAllocation(CLASS_NAME);
 assertEquals(1, allocation.getAllocCount());
 }

 @Test
 public void testMultipleThreads() throws InterruptedException {
 initProfiler();
 ExecutorService executor = Executors.newFixedThreadPool(2);
 Callable<Integer> task1 = () -> {
 start(CLASS_NAME, METHOD_NAME);
Thread.sleep(100);
 end(CLASS_NAME, METHOD_NAME);
 return 1;
 };
 Callable<Integer> task2 = () -> {
 start(CLASS_NAME, METHOD_NAME);
Thread.sleep(200);
 end(CLASS_NAME, METHOD_NAME);
 return 2;
 };
 Future<Integer> future1 = executor.submit(task1);
 Future<Integer> future2 = executor.submit(task2);
 executor.shutdownNow();
 int result1 = future1.get();
 int result2 = future2.get();
 assertEquals(1, result1);
 assertEquals(2, result2);
 }

 @Test
 public void testDebugStartAndDebugException() {
 Profile._debugStart = true;
 Profile._debugException = true;
 Profile.initProfiler();
 try {
 throw new Exception();
 } catch (Exception e) {
 System.out.println("Caught exception: " + e.getMessage());
 } finally {
 Profile._debugStart = false;
 Profile._debugException = false;
 }
 }

 private void runTestClass() {
 Class<?> clazz = Class.forName(CLASS_NAME);
 Constructor<?> constructor = clazz.getDeclaredConstructor();
 constructor.setAccessible(true);
 Object instance = constructor.newInstance();
 try {
 method(instance);
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 private void method(Object instance) throws Exception {
 Method method = clazz.getDeclaredMethod(METHOD_NAME);
 method.setAccessible(true);
 method.invoke(instance);
 }

 private Frame findFrame(String className, String methodName) {
 Frame frame = null;
 for (Frame f : Profile.frameList()) {
 if (f.getClassName().equals(className) && f.getMethodName().equals(methodName)) {
 frame = f;
 break;
 }
 }
 return frame;
 }

 private ClassAllocation findClassAllocation(String className) {
 ClassAllocation allocation = null;
 for (ClassAllocation ca : Profile.allocations()) {
 if (ca.getClassName().equals(className)) {
 allocation = ca;
 break;
 }
 }
 return allocation;
 }
}
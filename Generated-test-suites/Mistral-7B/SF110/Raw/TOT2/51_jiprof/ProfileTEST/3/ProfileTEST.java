import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;
import static com.mentorgen.tools.profile.output.Dumper.*;

public class ProfileTest {

 private static Profile _profile;
 private static Controller _controller;
 private static Thread _controllerThread;
 private static Map<Long, Frame> _threadActiveFrame;
 private static Frame testFrame;

 @BeforeClass
 public static void setUpClass() {
 Profile.initProfiler();
 _controller = new Controller();
 _threadActiveFrame = new HashMap<Long, Frame>();
 }

 @AfterClass
 public static void tearDownClass() {
 Profile.shutdown();
 }

 @Test
 public void testInit() {
 Profile.init();
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 if (Profile._controllerThread != null) {
 assertTrue(Profile._controllerThread.isAlive());
 }
 }

 @Test
 public void testClear() {
 Profile.init();
 Profile.clear();
 assertNotNull(Profile._threadDictionary);
 assertNotNull(Profile._methodDictionary);
 assertNotNull(Profile._frameList);
 assertNotNull(Profile._lock);
 assertNotNull(Profile._allocList);
 assertTrue(Profile._threadActiveFrame.isEmpty());
 }

 @Test
 public void testStart() {
 Profile.start();
 assertTrue(_controller.isRunning());
 }

 @Test
 public void testStop() {
 Profile.start();
 Thread.yield(); // to let controller start
 Profile.stop();
 assertFalse(_controller.isRunning());
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("output.txt");
 assertEquals("output.txt", _controller.getFileName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFileNameNull() {
 Profile.setFileName(null);
 fail("expected IllegalArgumentException");
 }

 @Test
 public void testShutdown() {
 Profile.init();
 Profile.shutdown();
 assertNull(_controller);
 assertNull(_controllerThread);
 }

 @Test
 public void testThreads() {
 Profile.init();
 long thread1Id = Thread.currentThread().getId();
 long thread2Id = Thread.currentThread().getId();
 List<Long> threads = Profile.threads();
 assertEquals(2, threads.size());
 assertTrue(threads.contains(thread1Id));
 assertTrue(threads.contains(thread2Id));
 }

 @Test
 public void testInteractions() {
 Profile.init();
 long threadId = Thread.currentThread().getId();
 Frame frame1 = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Frame frame2 = new Frame(null, new Method("TestClass", "method1"), threadId);
 Frame frame3 = new Frame(null, new Method("TestClass", "method2"), threadId);
 Profile._threadActiveFrame.put(threadId, frame1);
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 Profile._threadActiveFrame.put(threadId, frame2);
 Profile._frameList.add(frame3);
 Profile._threadActiveFrame.put(threadId, frame3);
 List<Frame> interactions = Profile.interactions(threadId);
 assertEquals(4, interactions.size());
 assertTrue(interactions.contains(frame1));
 assertTrue(interactions.contains(frame2));
 assertTrue(interactions.contains(frame3));
 }

 @Test
 public void testFrameList() {
 Profile.init();
 long threadId = Thread.currentThread().getId();
 Frame frame1 = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Frame frame2 = new Frame(null, new Method("TestClass", "method1"), threadId);
 Frame frame3 = new Frame(null, new Method("TestClass", "method2"), threadId);
 Profile._threadActiveFrame.put(threadId, frame1);
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 Profile._threadActiveFrame.put(threadId, frame2);
 Profile._frameList.add(frame3);
 Profile._threadActiveFrame.put(threadId, frame3);
 List<Frame> frameList = Profile.frameList();
 assertEquals(4, frameList.size());
 assertTrue(frameList.contains(frame1));
 assertTrue(frameList.contains(frame2));
 assertTrue(frameList.contains(frame3));
 }

 @Test
 public void testAllocations() {
 Profile.init();
 Profile.alloc("TestClass");
 Profile.alloc("TestClass");
 Profile.alloc("TestClass");
 List<ClassAllocation> allocations = Profile.allocations();
 assertEquals(1, allocations.size());
 ClassAllocation allocation = allocations.iterator().next();
 assertEquals("TestClass", allocation.getName());
 assertEquals(3, allocation.getAllocCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 Profile.init();
 long threadId = Thread.currentThread().getId();
 Frame frame1 = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Frame frame2 = new Frame(null, new Method("TestClass", "method1"), threadId);
 Frame frame3 = new Frame(null, new Method("TestClass", "method2"), threadId);
 Profile._threadActiveFrame.put(threadId, frame1);
 Profile._frameList.add(frame1);
 Profile._threadList.add(threadId);
 Profile._frameList.add(frame2);
 Profile._threadActiveFrame.put(threadId, frame2);
 Profile._frameList.add(frame3);
 Profile._threadActiveFrame.put(threadId, frame3);
 long time1 = System.nanoTime();
 Profile.start("TestClass", "<init>");
 Profile.end("TestClass", "<init>");
 long time2 = System.nanoTime();
 long threadTotalTime = Profile.getThreadTotalTime(threadId);
 assertEquals(time2 - time1, threadTotalTime);
 }

 @Test
 public void testSortFrameList() {
 Profile.init();
 long threadId = Thread.currentThread().getId();
 Frame frame1 = new Frame(null, new Method("TestClass", "<init>"), threadId);
 Frame frame2 = new Frame(null, new Method("TestClass", "method1"), threadId);
 Frame frame3 = new Frame(null, new Method("TestClass", "method2"), threadId);
 Profile._threadActiveFrame.put(threadId, frame1);
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 Profile._threadActiveFrame.put(threadId, frame2);
 Profile._frameList.add(frame3);
 Profile._threadActiveFrame.put(threadId, frame3);
 Comparator<Frame> comp = Collections.reverseOrder();
 Profile.sortFrameList(comp);
 Frame frame4 = Profile.frameList().get(0);
 assertEquals(frame3.getEndTime(), frame4.getBeginTime());
 }

 @Test
 public void testStartEnd() {
 Profile.init();
 String className = "TestClass";
 String methodName = "<init>";
 long threadId = Thread.currentThread().getId();
 Profile.start(className, methodName);
 long start = System.nanoTime();
 Profile.end(className, methodName);
 long end = System.nanoTime();
 Frame testFrame = findFrame(threadId, className, methodName);
 assertEquals(start, testFrame.getBeginTime());
 assertEquals(end, testFrame.getEndTime());
 }

 @Test
 public void testBeginWaitEndWait() {
 Profile.init();
 String className = "TestClass";
 String methodName = "method1";
 long threadId = Thread.currentThread().getId();
 Profile.beginWait(className, methodName);
 long start = System.nanoTime();
 Profile.endWait(className, methodName);
 long end = System.nanoTime();
 Frame testFrame = findFrame(threadId, className, methodName);
 assertEquals(start, testFrame.getBeginWaitTime());
 assertEquals(end, testFrame.getEndWaitTime());
 }

 @Test
 public void testUnwind() {
 Profile.init();
 String className = "TestClass";
 String methodName = "<init>";
 String exception = "TestException";
 long threadId = Thread.currentThread().getId();
 Profile.start(className, methodName);
 try {
 throw new TestException();
 } catch (TestException e) {
 Profile.unwind(className, methodName, exception);
 }
 Frame testFrame = findFrame(threadId, className, methodName);
 assertNotNull(testFrame.getExceptionName());
 assertEquals(exception, testFrame.getExceptionName());
 }

 @Test
 public void testAlloc() {
 Profile.init();
 String className = "TestClass";
 Profile.alloc(className);
 ClassAllocation testAllocation = Profile._allocList.get(className);
 assertEquals(1, testAllocation.getAllocCount());
 }

 @Test
 public void testRun() {
 Profile.init();
 Profile p = new Profile();
 p.run();
 assertTrue(Profile._threadDictionary.isEmpty());
 }
}
import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;
import junit.framework.TestCase;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ThreadLocalRandom;

public class ProfileTest extends TestCase {

 private static final boolean DEBUG_START = false;
 private static final boolean DEBUG_EXCEPTION = false;

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 public static void shutdown() throws IOException {
 Profile.shutdown();
 ProfileDump.dump();
 }

 @Test
 public void testInit() {
 Profile.init();
 assertTrue(Profile._threadDictionary.isEmpty());
 assertTrue(Profile._methodDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
 }

 @Test
 public void testStartAndEnd() {
 Profile.start("com.mentorgen.tools.profile.test.TestClass", "testMethod");
 Profile.end("com.mentorgen.tools.profile.test.TestClass", "testMethod");
 List<Frame> frames = Profile.frameList();
 assertEquals(1, frames.size());
 Frame frame = frames.get(0);
 assertEquals("com.mentorgen.tools.profile.test.TestClass", frame.getClassName());
 assertEquals("testMethod", frame.getMethodName());
 }

 @Test
 public void testBeginWaitAndEndWait() {
 Profile.start("com.mentorgen.tools.profile.test.TestClass", "testMethod");
 Profile.beginWait("com.mentorgen.tools.profile.test.TestClass", "testMethodWait");
 Profile.endWait("com.mentorgen.tools.profile.test.TestClass", "testMethodWait");
 List<Frame> frames = Profile.frameList();
 assertEquals(2, frames.size());
 Frame frame1 = frames.get(0);
 Frame frame2 = frames.get(1);
 assertTrue(frame1.getBeginTime() < frame2.getBeginTime());
 }

 @Test
 public void testClear() {
 Profile.init();
 Profile.start("com.mentorgen.tools.profile.test.TestClass", "testMethod");
 Profile.end("com.mentorgen.tools.profile.test.TestClass", "testMethod");
 Profile.clear();
 List<Frame> frames = Profile.frameList();
 assertTrue(frames.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testFindFrameNull() {
 Profile.init();
 Profile.findFrame(-1L, "com.mentorgen.tools.profile.test.TestClass", "testMethod");
 }

 @Test
 public void testFindFrame() {
 Profile.init();
 Profile.start("com.mentorgen.tools.profile.test.TestClass", "testMethod");
 Frame frame = Profile.findFrame(Thread.currentThread().getId(), "com.mentorgen.tools.profile.test.TestClass", "testMethod");
 assertNotNull(frame);
 assertEquals("com.mentorgen.tools.profile.test.TestClass", frame.getClassName());
 assertEquals("testMethod", frame.getMethodName());
 }

 @Test
 public void testUnwind() {
 try {
 throw new Exception();
 } catch (Exception e) {
 Profile.unwind("com.mentorgen.tools.profile.test.TestClass", "testUnwindMethod", e.getClass().getName());
 List<Frame> frames = Profile.frameList();
 assertEquals(2, frames.size());
 Frame frame1 = frames.get(0);
 Frame frame2 = frames.get(1);
 assertTrue(frame1.getClassName().equals("com.mentorgen.tools.profile.test.TestClass") && frame1.getMethodName().equals("testUnwindMethod"));
 assertTrue(frame2.getClassName().equals("java.lang.Exception"));
 assertTrue(frame2.getMethodName().equals("printStackTrace"));
 }
 }

 @Test
 public void testAlloc() {
 Profile.start("com.mentorgen.tools.profile.test.TestClass", "testAllocMethod");
 Profile.alloc("java.util.ArrayList");
 Profile.end("com.mentorgen.tools.profile.test.TestClass", "testAllocMethod");
 List<ClassAllocation> allocations = Profile.allocations();
 assertEquals(1, allocations.size());
 ClassAllocation allocation = allocations.get(0);
 assertEquals("java.util.ArrayList", allocation.getClassName());
 assertEquals(1, allocation.getAllocationCount());
 }

 @Test
 public void testMultipleAllocs() {
 Profile.init();
 Profile.start("com.mentorgen.tools.profile.test.TestClass", "testMultipleAllocs");
 for (int i = 0; i < 10; i++) {
 Profile.alloc("java.util.ArrayList");
 }
 Profile.end("com.mentorgen.tools.profile.test.TestClass", "testMultipleAllocs");
 List<ClassAllocation> allocations = Profile.allocations();
 assertEquals(1, allocations.size());
 ClassAllocation allocation = allocations.get(0);
 assertEquals("java.util.ArrayList", allocation.getClassName());
 assertEquals(10, allocation.getAllocationCount());
 }

 @Test
 public void testThreads() {
 Profile.init();
 Thread thread1 = new Thread(() -> {
 try {
 Profile.start("com.mentorgen.tools.profile.test.TestClass", "testThread1");
 Thread.sleep(100);
 Profile.end("com.mentorgen.tools.profile.test.TestClass", "testThread1");
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 });
 Thread thread2 = new Thread(() -> {
 try {
 Profile.start("com.mentorgen.tools.profile.test.TestClass", "testThread2");
 Thread.sleep(50);
 Profile.end("com.mentorgen.tools.profile.test.TestClass", "testThread2");
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 });
 thread1.start();
 thread2.start();
 List<Long> threads = new ArrayList<>(Arrays.asList(thread1.getId(), thread2.getId()));
 List<Frame> framesThread1 = Profile.interactions(threads.get(0));
 List<Frame> framesThread2 = Profile.interactions(threads.get(1));
 assertEquals(2, framesThread1.size());
 assertEquals(2, framesThread2.size());
 }

 @Test
 public void testThreadsEmpty() {
 List<Long> threads = new ArrayList<>();
 List<Frame> frames = Profile.interactions(threads.get(0));
 assertTrue(frames.isEmpty());
 }

 @Test
 public void testSortFrameList() {
 Profile.init();
 Profile.start("com.mentorgen.tools.profile.test.TestClass", "testSortFrameList");
 Profile.end("com.mentorgen.tools.profile.test.TestClass", "testSortFrameList");
 Profile.sortFrameList((frame1, frame2) -> frame1.getBeginTime() < frame2.getBeginTime());
 List<Frame> frames = Profile.frameList();
 assertFalse(frames.get(0).getBeginTime() > frames.get(1).getBeginTime());
 }

 @After
 public void afterEachTest() {
 Profile.clear();
 }
}
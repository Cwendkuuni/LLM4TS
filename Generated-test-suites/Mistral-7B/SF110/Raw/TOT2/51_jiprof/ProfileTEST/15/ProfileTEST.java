import org.junit.Test;
import static org.junit.Assert.*;
import com.mentorgen.tools.profile.runtime.*;
import com.mentorgen.tools.profile.*;
import java.util.*;
import java.io.*;

public class ProfileTest {

 private static final String TEST_CLASS_NAME = "com.example.TestClass";
 private static final String TEST_METHOD_NAME = "testMethod";

 @BeforeClass
 public static void initProfiler() {
 Profile.initProfiler();
 }

 @AfterClass
 public static void shutdownProfiler() {
 Profile.shutdown();
 }

 @Test
 public void testInitAndClear() {
 Profile.init();
 Profile.clear();
 assertNull(Profile.threads().iterator().next());
 }

 @Test
 public void testStartAndStopThread() throws InterruptedException {
 final long threadId = Thread.currentThread().getId();
 Profile.start();
 Thread newThread = new Thread(() -> {});
 newThread.start();
 Thread.sleep(100);
 Profile.stop();
 List<Frame> interactions = Profile.interactions(threadId);
 assertTrue(interactions.size() > 0);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() throws InterruptedException {
 final long threadId = Thread.currentThread().getId();
 Profile.start();
 Thread.sleep(100);
 Profile.start();
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("test.txt");
 }

 @Test
 public void testThreadTotalTime() {
 final long threadId1 = Thread.currentThread().getId();
 final long threadId2 = new Thread(() -> {}).getId();
 Profile.start();
 Thread.sleep(100);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Thread.sleep(200);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 Thread.sleep(50);
 Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
 long threadTotalTime1 = Profile.getThreadTotalTime(threadId1);
 long threadTotalTime2 = Profile.getThreadTotalTime(threadId2);
 assertTrue(threadTotalTime1 > threadTotalTime2);
 }

 @Test
 public void testSortFrameList() {
 final long threadId = Thread.currentThread().getId();
 Frame firstFrame = new Frame(null, new Method(TEST_CLASS_NAME, "method1"), threadId);
 Frame secondFrame = new Frame(null, new Method(TEST_CLASS_NAME, "method2"), threadId);
 Frame thirdFrame = new Frame(null, new Method(TEST_CLASS_NAME, "method1"), threadId);
 Profile._frameList.add(firstFrame);
 Profile._frameList.add(secondFrame);
 Profile._frameList.add(thirdFrame);
 Collections.sort(Profile._frameList, new Comparator<Frame>() {
 @Override
 public int compare(Frame f1, Frame f2) {
 return Long.compare(f1.getBeginTime(), f2.getBeginTime());
 }
 });
 Frame sortedFirstFrame = Profile._frameList.get(0);
 assertEquals(sortedFirstFrame.getMethodName(), "method1");
 }

 @Test
 public void testAllocations() {
 final String className = "com.example.TestClass";
 Profile.alloc(className);
 Profile.alloc(className);
 Iterable<ClassAllocation> allocations = Profile.allocations();
 int allocCount = 0;
 for (ClassAllocation ca : allocations) {
 if (ca.getClassName().equals(className)) {
 allocCount += ca.getAllocCount();
 }
 }
 assertEquals(allocCount, 2);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartWithoutInitialization() {
 Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
 }

 @Test
 public void testFindFrame() {
 final long threadId = Thread.currentThread().getId();
 Frame frame1 = new Frame(null, new Method(TEST_CLASS_NAME, TEST_METHOD_NAME), threadId);
 Frame frame2 = new Frame(frame1, new Method(TEST_CLASS_NAME, "method1"), threadId);
 Frame frame3 = new Frame(null, new Method(TEST_CLASS_NAME, "method2"), threadId);
 Profile._frameList.add(frame1);
 Profile._frameList.add(frame2);
 Profile._frameList.add(frame3);
 Frame foundFrame = Profile.findFrame(threadId, TEST_CLASS_NAME, TEST_METHOD_NAME);
 assertEquals(foundFrame, frame1);
 }

 @Test
 public void testFindFrameWithNullParent() {
 final long threadId = Thread.currentThread().getId();
 Frame rootFrame = new Frame(null, new Method(TEST_CLASS_NAME, TEST_METHOD_NAME), threadId);
 Frame childFrame = new Frame(rootFrame, new Method(TEST_CLASS_NAME, "method1"), threadId);
 Profile._frameList.add(rootFrame);
 Profile._frameList.add(childFrame);
 Frame foundFrame = Profile.findFrame(threadId, TEST_CLASS_NAME, TEST_METHOD_NAME);
 assertEquals(foundFrame, rootFrame);
 }

 @Test
 public void testFindFrameWithException() {
 final long threadId = Thread.currentThread().getId();
 Frame rootFrame = new Frame(null, new Method(TEST_CLASS_NAME, TEST_METHOD_NAME), threadId);
 Frame childFrame = new Frame(rootFrame, new Method(TEST_CLASS_NAME, "method1"), threadId);
 Frame exceptionFrame = new Frame(childFrame, new Method("java.lang.Exception", "getMessage"), threadId);
 Profile._frameList.add(rootFrame);
 Profile._frameList.add(childFrame);
 Profile._frameList.add(exceptionFrame);
 Frame foundFrame = Profile.findFrame(threadId, TEST_CLASS_NAME, "getMessage");
 assertEquals(foundFrame, exceptionFrame);
 }

 @Test
 public void testUnwindWithException() {
 final long threadId = Thread.currentThread().getId();
 Frame rootFrame = new Frame(null, new Method(TEST_CLASS_NAME, TEST_METHOD_NAME), threadId);
 Frame exceptionFrame = new Frame(rootFrame, new Method("java.lang.Exception", "getMessage"), threadId);
 Frame childFrame = new Frame(exceptionFrame, new Method(TEST_CLASS_NAME, "method1"), threadId);
 Profile._threadActiveFrame.put(threadId, rootFrame);
 Profile._frameList.add(rootFrame);
 Profile._frameList.add(exceptionFrame);
 Profile._frameList.add(childFrame);
 Profile.unwind(TEST_CLASS_NAME, "getMessage", "Exception");
 Frame activeFrame = Profile._threadActiveFrame.get(threadId);
 assertEquals(activeFrame, childFrame);
 }
}
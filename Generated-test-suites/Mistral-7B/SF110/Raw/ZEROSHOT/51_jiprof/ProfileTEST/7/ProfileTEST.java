import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.mentorgen.tools.profile.Output.ProfileDump;
import com.mentorgen.tools.profile.runtime.Controller;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileTest {

 private static Profile profile;
 private static Controller controller;
 private static Frame frame;
 private static ThreadDictionary threadDictionary;
 private static List<Frame> frameList;
 private static Map<String, Method> methodDictionary;
 private static Map<String, ClassAllocation> allocList;

 @BeforeClass
 public static void setUpClass() {
 Profile.init();
 profile = new Profile();
 controller = Profile.getController();
 threadDictionary = Profile.getThreadDictionary();
 frameList = Profile.frameList();
 methodDictionary = Profile.getMethodDictionary();
 allocList = Profile.getAllocList();
 }

 @AfterClass
 public static void tearDownClass() {
 Profile.clear();
 }

 @Test
 public void testInit() {
 assertTrue(Profile._threadActiveFrame.isEmpty());
 assertTrue(Profile._threadDictionary.isEmpty());
 assertTrue(Profile._methodDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
 assertEquals(0L, Profile.getThreadTotalTime(Thread.currentThread().getId()));
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 if (controller.isRemote()) {
 assertTrue(Profile._controllerThread != null);
 }
 }

 @Test
 public void testClear() {
 Profile.init();
 Profile.start();
 Profile.end("TestClass", "testMethod");
 Profile.clear();
 assertTrue(Profile._threadActiveFrame.isEmpty());
 assertTrue(Profile._threadDictionary.isEmpty());
 assertTrue(Profile._frameList.isEmpty());
 }

 @Test
 public void testThreads() {
 Profile.init();
 Profile.start();
 Profile.end("TestClass", "testMethod");
 List<Long> threads = Profile.threads();
 assertEquals(1, threads.size());
 assertTrue(threads.contains(Thread.currentThread().getId()));
 }

 @Test
 public void testInteractions() {
 Profile.init();
 Profile.start();
 Frame firstFrame = startFrame("TestClass", "testMethod");
 Frame secondFrame = startFrame("TestClass", "testMethod2");
 Profile.end("TestClass", "testMethod");
 List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
 assertEquals(2, interactions.size());
 assertTrue(interactions.contains(firstFrame));
 assertTrue(interactions.contains(secondFrame));
 }

 @Test
 public void testFrameList() {
 Profile.init();
 startFrame("TestClass", "testMethod");
 startFrame("TestClass2", "testMethod2");
 Profile.end("TestClass", "testMethod");
 Profile.end("TestClass2", "testMethod2");
 List<Frame> frameList = Profile.frameList();
 assertEquals(2, frameList.size());
 }

 @Test
 public void testAllocations() {
 Profile.init();
 alloc("TestClass");
 alloc("TestClass2");
 List<ClassAllocation> allocations = new ArrayList<>(allocList.values());
 assertEquals(2, allocations.size());
 Map<String, Integer> classCount = new HashMap<>();
 for (ClassAllocation ca : allocations) {
 classCount.merge(ca.getClassName(), ca.getAllocCount(), Integer::sum);
 }
 assertEquals(2, classCount.size());
 assertEquals(2, classCount.get("TestClass"));
 assertEquals(0, classCount.get("TestClass2"));
 }

 @Test
 public void testThreadTotalTime() {
 Profile.init();
 Profile.start();
 Thread.yield();
 Thread.yield();
 Thread.yield();
 Profile.end("TestClass", "testMethod");
 long threadTotalTime = Profile.getThreadTotalTime(Thread.currentThread().getId());
 assertTrue(threadTotalTime > 0L);
 }

 @Test
 public void testSortFrameList() {
 Profile.init();
 startFrame("TestClass3", "testMethod3");
 startFrame("TestClass2", "testMethod2");
 startFrame("TestClass", "testMethod");
 Profile.end("TestClass3", "testMethod3");
 Profile.end("TestClass2", "testMethod2");
 Profile.end("TestClass", "testMethod");
 Collections.sort(frameList, Frame.COMPARATOR);
 Frame firstFrame = frameList.get(0);
 Frame secondFrame = frameList.get(1);
 Frame thirdFrame = frameList.get(2);
 assertEquals("TestClass", firstFrame.getClassName());
 assertEquals("testMethod", firstFrame.getMethodName());
 assertEquals("TestClass2", secondFrame.getClassName());
 assertEquals("testMethod2", secondFrame.getMethodName());
 assertEquals("TestClass", thirdFrame.getClassName());
 assertEquals("testMethod", thirdFrame.getMethodName());
 }

 @Test
 public void testStartEnd() {
 Profile.init();
 Frame firstFrame = startFrame("TestClass", "testMethod");
 Frame secondFrame = startFrame("TestClass", "testMethod");
 Profile.end("TestClass", "testMethod");
 List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
 assertEquals(1, interactions.size());
 assertTrue(interactions.contains(firstFrame));
 assertEquals(firstFrame.getEndTime(), secondFrame.getBeginTime());
 }

 @Test
 public void testBeginWaitEndWait() {
 Profile.init();
 startFrame("TestClass", "testMethod");
 beginWait("TestClass", "testMethod");
 Thread.yield();
 Thread.yield();
 endWait("TestClass", "testMethod");
 List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
 Frame interaction = interactions.get(0);
 assertTrue(interaction.isWaiting());
 }

 @Test
 public void testUnwind() {
 Profile.init();
 startFrame("TestClass", "testMethod1");
 startFrame("TestClass", "testMethod2");
 startFrame("TestClass", "testMethod3");
 unwind("TestClass", "testMethod3", "ExceptionTest");
 List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
 Frame unwindPoint = null;
 for (Frame frame : interactions) {
 if (frame.getClassName().equals("TestClass") && frame.getMethodName().equals("testMethod3")) {
 unwindPoint = frame;
 break;
 }
 }
 assertNotNull(unwindPoint);
 assertTrue(unwindPoint.isUnwinding());
 }

 private Frame startFrame(String className, String methodName) {
 long start = System.nanoTime();
 long threadId = Thread.currentThread().getId();
 Frame target = null;
 synchronized (Profile._lock) {
 if (!controller.isProfile()) {
 return null;
 }
 Method method = new Method(className, methodName);
 if (methodDictionary.get(method.toString()) == null) {
 methodDictionary.put(method.toString(), method);
 }
 if (threadActiveFrame(threadId) != null) {
 target = threadActiveFrame(threadId).getChild(method);
 if (target == null) {
 target = new Frame(threadActiveFrame(threadId), method, threadId);
 frameList.add(target);
 }
 }
 else {
 target = new Frame(null, method, threadId);
 frameList.add(target);
 threadDictionary.add(threadId, target);
 }
 threadActiveFrame(threadId) = target;
 target.setBeginTime(start);
 target.overhead(System.nanoTime() - start);
 }
 return target;
 }

 private Frame threadActiveFrame(long threadId) {
 return threadDictionary.get(threadId);
 }
}
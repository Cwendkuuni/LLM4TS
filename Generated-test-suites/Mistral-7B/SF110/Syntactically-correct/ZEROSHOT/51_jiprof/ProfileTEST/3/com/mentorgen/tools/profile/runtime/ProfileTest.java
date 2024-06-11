package com.mentorgen.tools.profile.runtime;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfileTest {

 @BeforeClass
 public static void setup() {
 Profile.init();
 }

 @Test
 public void testInitProfiler() {
 Profile.initProfiler();
 assertTrue(Profile._controller != null);
 }

 @Test
 public void testClear() {
 Profile.clear();
 assertEquals(0, Profile.threads().size());
 }

 @Test
 public void testStartAndStop() {
 Profile.start();
 Profile.stop();
 }

 @Test
 public void testSetFileName() {
 Profile.setFileName("output.txt");
 assertTrue(Profile._controller.getFileName().equals("output.txt"));
 }

 @Test
 public void testShutdown() {
 Profile.shutdown();
 assertEquals(0, Profile.threads().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 Profile.start();
 Profile.start();
 fail("Expected an IllegalStateException but didn't get one.");
 }

 @Test
 public void testInteractions() {
 long threadId = Thread.currentThread().getId();
 Profile.start("com.mentorgen.tools.profile.ProfileTest", "testInteractions");
 Profile.end("com.mentorgen.tools.profile.ProfileTest", "testInteractions");
 List<Frame> interactions = Profile.interactions(threadId);
 assertFalse(interactions.isEmpty());
 Frame interaction = interactions.get(0);
 assertEquals("com.mentorgen.tools.profile.ProfileTest:testInteractions", interaction.toString());
 }

 @Test
 public void testFrameList() {
 Profile.start("com.mentorgen.tools.profile.ProfileTest", "testFrameList");
 Frame frame1 = new Frame(null, new Method("com.mentorgen.tools.profile.ProfileTest", "testFrameList"), Thread.currentThread().getId());
 Frame frame2 = new Frame(frame1, new Method("java.util.ArrayList", "add"), Thread.currentThread().getId());
 Profile.end("com.mentorgen.tools.profile.ProfileTest", "testFrameList");
 Iterable<Frame> frameList = Profile.frameList();
 Frame testFrameListFrame = null;
 for (Frame frame : frameList) {
 if (frame.getClassName().equals("com.mentorgen.tools.profile.ProfileTest") && frame.getMethodName().equals("testFrameList")) {
 testFrameListFrame = frame;
 break;
 }
 }
 assertTrue(testFrameListFrame != null);
 Frame testFrameListParent = testFrameListFrame.getParent();
 assertTrue(testFrameListParent != null);
 assertEquals("java.util.ArrayList:add", testFrameListParent.getMethodName());
 }

 @Test
 public void testAllocations() {
 Profile.alloc("java.util.ArrayList");
 List<ClassAllocation> allocations = new ArrayList<>(Profile.allocations());
 ClassAllocation allocation = null;
 for (ClassAllocation ca : allocations) {
 if (ca.getClassname().equals("java.util.ArrayList")) {
 allocation = ca;
 break;
 }
 }
 assertTrue(allocation != null);
 assertEquals(1, allocation.getAllocationCount());
 }

 @Test
 public void testGetThreadTotalTime() {
 long threadId1 = Thread.currentThread().getId();
 long threadId2 = Thread.currentThread().getId();
 Profile.start("com.mentorgen.tools.profile.ProfileTest", "testGetThreadTotalTime");
 Profile.start("com.mentorgen.tools.profile.ProfileTest", "testGetThreadTotalTime");
 long time1 = Profile.getThreadTotalTime(threadId1);
 long time2 = Profile.getThreadTotalTime(threadId2);
 Profile.end("com.mentorgen.tools.profile.ProfileTest", "testGetThreadTotalTime");
 Profile.end("com.mentorgen.tools.profile.ProfileTest", "testGetThreadTotalTime");
 assertEquals(0, time1 - time2);
 }

 @Test
 public void testSortFrameList() {
 Collection<Frame> frames = new ArrayList<>();
 frames.add(new Frame(null, new Method("com.mentorgen.tools.profile.ProfileTest", "testSortFrameList"), Thread.currentThread().getId()));
 frames.add(new Frame(null, new Method("com.mentorgen.tools.profile.ProfileTest", "testFrameList"), Thread.currentThread().getId()));
 frames.add(new Frame(null, new Method("com.mentorgen.tools.profile.ProfileTest", "testGetThreadTotalTime"), Thread.currentThread().getId()));
 frames.add(new Frame(null, new Method("java.util.ArrayList", "add"), Thread.currentThread().getId()));
 Profile.sortFrameList((f1, f2) -> f1.getNetTime() > f2.getNetTime() ? -1 : 1);
 Iterator<Frame> iterator = frames.iterator();
 Frame lastFrame = iterator.next();
 while (iterator.hasNext()) {
 Frame currentFrame = iterator.next();
 assertTrue(lastFrame.getNetTime() > currentFrame.getNetTime());
 lastFrame = currentFrame;
 }
 }

 @Test
 public void testStartEnd() {
 Profile.start("com.mentorgen.tools.profile.ProfileTest", "testStartEnd");
 Profile.end("com.mentorgen.tools.profile.ProfileTest", "testStartEnd");
 }

 @Test
 public void testBeginWaitEndWait() {
 Profile.start("com.mentorgen.tools.profile.ProfileTest", "testBeginWaitEndWait");
 long start = System.nanoTime();
 try {
 Thread.sleep(10);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.beginWait("com.mentorgen.tools.profile.ProfileTest", "testBeginWaitEndWait");
 long waitStart = System.nanoTime();
 try {
 Thread.sleep(5);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Profile.endWait("com.mentorgen.tools.profile.ProfileTest", "testBeginWaitEndWait");
 long waitEnd = System.nanoTime();
 Profile.end("com.mentorgen.tools.profile.ProfileTest", "testBeginWaitEndWait");
 Frame frame = findFrame("com.mentorgen.tools.profile.ProfileTest", "testBeginWaitEndWait");
 assertEquals(waitEnd - waitStart, frame.getWaitTime());
 }

 @Test
 public void testUnwind() {
 try {
 throw new RuntimeException();
 } catch (RuntimeException e) {
 Profile.unwind("com.mentorgen.tools.profile.ProfileTest", "testUnwind", e.getClass().getName());
 }
 }

}
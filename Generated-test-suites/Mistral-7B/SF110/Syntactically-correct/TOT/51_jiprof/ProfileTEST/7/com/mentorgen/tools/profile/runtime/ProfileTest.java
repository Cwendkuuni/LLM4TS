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
import org.mockito.MockitoAnnotations;

import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.runtime.Controller;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;

public class ProfileTest {

 private Profile profile;
 private Controller controllerMock;
 private ProfileDump profileDumpMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 profile = new Profile();
 controllerMock = mock(Controller.class);
 profileDumpMock = mock(ProfileDump.class);
 profile._controller = controllerMock;
 profile._controllerThread = new Thread(controllerMock);
 }

 @Test
 public void testInitProfiler() {
 profile.initProfiler();
 verify(controllerMock).start();
 }

 @Test
 public void testInit() {
 profile.init();
 assertNotNull(profile._threadDictionary);
 assertNotNull(profile._methodDictionary);
 assertNotNull(profile._frameList);
 assertNotNull(profile._threadActiveFrame);
 assertNotNull(profile._allocList);
 }

 @Test
 public void testClear() {
 profile.clear();
 assertEquals(0, profile.threads().size());
 assertEquals(0, profile.frameList().size());
 }

 @Test
 public void testStartAndStop() {
 profile.start();
 profile.stop();
 verify(controllerMock, times(2)).start();
 verify(controllerMock, times(2)).stop();
 }

 @Test
 public void testSetFileName() {
 profile.setFileName("test.txt");
 verify(profileDumpMock).setFileName("test.txt");
 }

 @Test(expected = IllegalStateException.class)
 public void testShutdownWhileProfiling() {
 profile.shutdown();
 profile.start();
 }

 @Test
 public void testThreads() {
 profile.init();
 profile.start();
 Thread thread1 = new Thread(() -> {
 for (int i = 0; i < 10000; i++) {
 i++;
 }
 });
 thread1.start();
 Thread thread2 = new Thread(() -> {
 for (int i = 0; i < 10000; i++) {
 i++;
 }
 });
 thread2.start();
 Thread thread3 = new Thread(() -> {
 for (int i = 0; i < 10000; i++) {
 i++;
 }
 });
 thread3.start();
 List<Long> threads = profile.threads();
 assertEquals(3, threads.size());
 assertTrue(threads.contains(thread1.getId()));
 assertTrue(threads.contains(thread2.getId()));
 assertTrue(threads.contains(thread3.getId()));
 }

 @Test
 public void testInteractions() {
 profile.init();
 profile.start();
 Thread thread1 = new Thread(() -> {
 for (int i = 0; i < 100; i++) {
 new MyClass1().someMethod();
 }
 });
 thread1.start();
 Thread thread2 = new Thread(() -> {
 for (int i = 0; i < 100; i++) {
 new MyClass2().someMethod();
 }
 });
 thread2.start();
 thread1.join();
 thread2.join();
 List<Frame> interactions = profile.interactions(thread1.getId());
 List<Frame> interactions2 = profile.interactions(thread2.getId());
 assertEquals(101, interactions.size());
 assertEquals(101, interactions2.size());
 for (int i = 0; i < 100; i++) {
 Frame frame1 = interactions.get(i);
 Frame frame2 = interactions2.get(i);
 assertEquals(MyClass1.class.getName(), frame1.getClassName());
 assertEquals(MyClass1.class.getName() + ":someMethod", frame1.getMethodName());
 assertEquals(MyClass2.class.getName(), frame2.getClassName());
 assertEquals(MyClass2.class.getName() + ":someMethod", frame2.getMethodName());
 }
 }

 @Test
 public void testFrameList() {
 profile.init();
 profile.start();
 Thread thread1 = new Thread(() -> {
 for (int i = 0; i < 1000; i++) {
 new MyClass1().someMethod();
 }
 });
 thread1.start();
 Thread thread2 = new Thread(() -> {
 for (int i = 0; i < 1000; i++) {
 new MyClass2().someMethod();
 }
 });
 thread2.start();
 thread1.join();
 thread2.join();
 List<Frame> frameList = profile.frameList();
 assertEquals(2001, frameList.size());
 List<Frame> interactions1 = profile.interactions(thread1.getId());
 List<Frame> interactions2 = profile.interactions(thread2.getId());
 for (int i = 0; i < 1000; i++) {
 Frame frame1 = interactions1.get(i);
 Frame frame2 = interactions2.get(i);
 assertEquals(MyClass1.class.getName(), frame1.getClassName());
 assertEquals(MyClass1.class.getName() + ":someMethod", frame1.getMethodName());
 assertEquals(MyClass2.class.getName(), frame2.getClassName());
 assertEquals(MyClass2.class.getName() + ":someMethod", frame2.getMethodName());
 }
 }

 @Test
 public void testAllocations() {
 profile.init();
 profile.start();
 new MyClass1();
 new MyClass1();
 new MyClass2();
 new MyClass2();
 List<ClassAllocation> allocations = new ArrayList<>(profile.allocations());
 assertEquals(2, allocations.size());
 ClassAllocation ca1 = allocations.get(0);
 assertEquals("com.mentorgen.tools.profile.runtime.MyClass1", ca1.getClassName());
 assertTrue(ca1.getAllocCount() > 0);
 ClassAllocation ca2 = allocations.get(1);
 assertEquals("com.mentorgen.tools.profile.runtime.MyClass2", ca2.getClassName());
 assertTrue(ca2.getAllocCount() > 0);
 }

 @Test
 public void testGetThreadTotalTime() {
 profile.init();
 profile.start();
 Thread thread1 = new Thread(() -> {
 for (int i = 0; i < 1000; i++) {
 Thread.yield();
 }
 });
 thread1.start();
 thread1.join();
 long thread1TotalTime = profile.getThreadTotalTime(thread1.getId());
 assertEquals(-1, thread1TotalTime);
 profile.shutdown();
 profile.init();
 profile.start();
 Thread thread2 = new Thread(() -> {
 for (int i = 0; i < 1000; i++) {
 Thread.yield();
 }
 });
 thread2.start();
 thread2.join();
 long thread2TotalTime = profile.getThreadTotalTime(thread2.getId());
 assertTrue(thread2TotalTime > 0);
 }
}
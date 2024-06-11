package weka.core.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import weka.core.Memory;

@RunWith(Parameterized.class)
@MethodSorts(MethodSorters.NAME_ASCENDING)
public class MemoryTest {

 private Memory memory;

 @Parameterized.Parameters(name = "{0}")
 public static Object[] data() {
 return new Object[][] { { false }, { true } };
 }

 public MemoryTest(boolean useGUI) {
 this.memory = new Memory(useGUI);
 }

 @Test
 public void testMemoryConstruction() {
 Memory memory = new Memory();
 Memory memoryWithGUI = new Memory(true);
 assertEquals(false, memory.getUseGUI());
 assertEquals(true, memoryWithGUI.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 assertEquals(this.memory.getUseGUI(), this.memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory initialMemory = new Memory();
 Memory memory = new Memory();
 long initial = memory.getInitial();
 long initial2 = initialMemory.getInitial();
 assertEquals(initial, initial2);
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long current = memory.getCurrent();
 Runtime runtime = Runtime.getRuntime();
 long currentRuntime = runtime.totalMemory();
 assertEquals(current, currentRuntime);
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long max = memory.getMax();
 Runtime runtime = Runtime.getRuntime();
 long maxRuntime = runtime.maxMemory();
 assertEquals(max, maxRuntime);
 }

 @Test(expected = IllegalStateException.class)
 public void testIsOutOfMemoryWithEnabledFalse() {
 Memory memory = new Memory();
 memory.setEnabled(false);
 memory.isOutOfMemory();
 }

 @Test
 public void testIsOutOfMemoryWithEnabledTrue() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 long initial = memory.getInitial();
 long max = memory.getMax();
 long current = memory.getCurrent();
 long outOfMemoryThreshold = initial + 200000L;
 memory.setInitial(initial + 500000L);
 assertFalse(memory.isOutOfMemory());
 memory.setInitial(initial + 1000000L);
 assertTrue(memory.isOutOfMemory());
 }

 @Test
 public void testToMegaByte() {
 long bytes = 123456789;
 double megaBytes = Memory.toMegaByte(bytes);
 assertEquals(11.5239, megaBytes, 0.01);
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryWithUseGUIFalse() {
 Memory memory = new Memory();
 memory.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemoryWithUseGUITrue() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setInitial(50);
 memory.setTotal(100);
 memory.setMax(200);
 memory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() throws SecurityException, NoSuchMethodException,
 InvocationTargetException, IllegalAccessException {
 Memory memory = new Memory();
 Method stopThreadsMethod = Memory.class.getDeclaredMethod("stopThreads");
 stopThreadsMethod.setAccessible(true);
 stopThreadsMethod.invoke(memory);
 Thread[] threads = new Thread[Thread.activeCount()];
 Thread.enumerate(threads);
 for (Thread thread : threads) {
 if (thread != null) {
 thread.stop();
 }
 }
 assertTrue(memory.isEnabled());
 }

 @Test(expected = IllegalThreadStateException.class)
 public void testThreadStop() throws NoSuchMethodException, SecurityException,
 InvocationTargetException, InstantiationException, IllegalAccessException {
 Memory memory = new Memory();
 Method stopThreadMethod = Thread.class.getDeclaredMethod("stop");
 stopThreadMethod.setAccessible(true);
 Thread thread = new Thread(() -> {});
 thread.start();
 stopThreadMethod.invoke(thread);
 fail("Thread should have been terminated but was not");
 }

 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String revision = memory.getRevision();
 assertFalse(revision.equals(""));
 }

 @Test
 public void testMain() throws Exception {
 Memory memory = new Memory();
 memory.main(new String[0]);
 }
}
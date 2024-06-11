package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemoryInitialization() {
 Memory memory = new Memory();
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getTotal());
 assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
 }

 @Test
 public void testMemoryUseGUIInitialization() {
 Memory memory = new Memory(true);
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getTotal());
 assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
 }

 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());
 Memory memoryWithGUI = new Memory(true);
 assertTrue(memoryWithGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
 Memory memoryWithInitialSet = new Memory();
 memoryWithInitialSet.setInitial(Runtime.getRuntime().totalMemory() * 2);
 assertEquals(memoryWithInitialSet.getInitial(), Runtime.getRuntime().totalMemory() * 2);
 }

 @Test
 public void testgetCurrent() {
 Memory memory = new Memory();
 long initialTotalMemory = memory.getTotal();
 Runtime.getRuntime().gc();
 long newTotalMemory = memory.getTotal();
 assertEquals(initialTotalMemory, newTotalMemory);
 }

 @Test
 public void testgetMax() {
 Memory memory = new Memory();
 assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
 }

 @Test
 public void testisOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 long currentMemory = Runtime.getRuntime().totalMemory();
 long maxMemory = Runtime.getRuntime().maxMemory();
 long initialMemory = memory.getInitial();
 memory.setInitial(currentMemory + 1000000);
 assertFalse(memory.isOutOfMemory());
 memory.setInitial(initialMemory);
 assertTrue(memory.isOutOfMemory());
 }

 @Test
 public void testtoMegaByte() {
 long bytes = 1024 * 1024;
 double result = Memory.toMegaByte(bytes);
 assertEquals(1.0, result, 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShowOutOfMemoryDuringInitialization() {
 Memory memory = new Memory() {
 @Override
 public boolean isEnabled() {
 return true;
 }
 };
 memory.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setInitial(Runtime.getRuntime().totalMemory() - 10 * 1024 * 1024);
 memory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 Thread[] threads = new Thread[10];
 for (int i = 0; i < threads.length; i++) {
 threads[i] = new Thread(() -> {});
 threads[i].start();
 }
 memory.stopThreads();
 for (Thread thread : threads) {
 assertTrue(thread.isAlive());
 }
 memory.stopThreads();
 for (Thread thread : threads) {
 assertFalse(thread.isAlive());
 }
 }

 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String revision = memory.getRevision();
 String expectedRevision = "8034";
 assertTrue(revision.startsWith(expectedRevision));
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsWithNullThreadGroup() {
 Memory memory = new Memory();
 memory.stopThreads(null);
 }
}
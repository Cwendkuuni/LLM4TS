package weka.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory();
 }

 @Test
 public void testMemoryConstructor() {
 memory = new Memory(true);
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getTotal(), 0);
 }

 @Test
 public void testMemoryIsEnabled() {
 Memory.setEnabled(true);
 assertTrue(Memory.isEnabled());
 Memory.setEnabled(false);
 assertFalse(Memory.isEnabled());
 }

 @Test
 public void testMemoryGetUseGUI() {
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 memory = new Memory(false);
 assertFalse(memory.getUseGUI());
 }

 @Test
 public void testMemoryGetInitial() {
 assertEquals(Memory.m_Initial, memory.getInitial());
 }

 @Test
 public void testMemoryGetCurrent() {
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
 memory.setEnabled(false);
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
 }

 @Test
 public void testMemoryGetMax() {
 assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
 }

 @Test
 public void testMemoryIsOutOfMemory() {
 // Test when not out of memory
 memory.setEnabled(false);
 assertFalse(memory.isOutOfMemory());
 // Test when out of memory
 Memory.setEnabled(true);
 long initial = Memory.m_Initial;
 long max = Memory.getMax();
 long total = initial + (long) (Math.random() * (max - initial));
 Memory.m_Initial = total;
 assertTrue(memory.isOutOfMemory());
 Memory.m_Initial = initial;
 }

 @Test
 public void testMemoryToMegaByte() {
 assertEquals(Memory.toMegaByte(1024), 1.0, 0.0);
 assertEquals(Memory.toMegaByte(1048576), 1.0, 0.0);
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryNull() {
 memory = null;
 memory.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemory() {
 memory.setEnabled(true);
 memory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 Thread[] threads = new Thread[5];
 for (int i = 0; i < threads.length; i++) {
 threads[i] = new Thread(() -> {});
 threads[i].start();
 }
 memory.stopThreads();
 for (Thread thread : threads) {
 assertNull(thread.getState());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStopThreadsWithNullThreads() {
 Thread[] threads = null;
 memory.stopThreads(threads);
 }

 @Test
 public void testGetRevision() {
 assertNotNull(memory.getRevision());
 }

 @Test
 public void testMemoryMain() {
 Memory mem = new Memory();
 assertEquals(Memory.m_Initial, mem.getInitial(), 0);
 assertEquals(Memory.getMax(), mem.getMax(), 0);
 }

 @Test(expected = SecurityException.class)
 public void testThreadStop() {
 Thread thread = new Thread(() -> {});
 thread.start();
 memory.stopThreads();
 }

}
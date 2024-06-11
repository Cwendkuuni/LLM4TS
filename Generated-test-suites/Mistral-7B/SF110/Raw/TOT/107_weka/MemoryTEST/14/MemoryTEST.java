package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.BeforeClass;
import org.junit.Test;

public class MemoryTest {

 private static Memory memory;

 @BeforeClass
 public static void setup() {
 memory = new Memory(true);
 }

 @Test
 public void testIsEnabled() {
 Memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 Memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testSetEnabled() {
 Memory.setEnabled(true);
 memory.setEnabled(false);
 assertFalse(Memory.isEnabled());
 Memory.setEnabled(true);
 assertTrue(Memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 memory = new Memory(false);
 assertFalse(memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 long initial = memory.getInitial();
 memory = new Memory();
 assertEquals(initial, memory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 long current = memory.getCurrent();
 memory.getRuntime().gc();
 long currentAfterGC = memory.getCurrent();
 assertNotEquals(current, currentAfterGC);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMaxWithMemoryFull() {
 memory.setEnabled(true);
 memory.showOutOfMemory();
 memory.setMax(memory.getCurrent() - 1000L);
 memory.getCurrent();
 }

 @Test
 public void testIsOutOfMemory() {
 memory.setEnabled(true);
 memory.setMax(memory.getCurrent() - 1L);
 assertTrue(memory.isOutOfMemory());
 memory.setMax(memory.getCurrent() + 1L);
 assertFalse(memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsWithNullThreadGroup() {
 memory.stopThreads();
 Thread[] threads = null;
 memory.stopThreads();
 }

 @Test
 public void testStopThreads() {
 Thread[] threads = new Thread[10];
 for (int i = 0; i < threads.length; i++) {
 threads[i] = new Thread(() -> {});
 }
 memory.stopThreads();
 for (Thread thread : threads) {
 assertFalse(thread.isAlive());
 }
 }

 @Test
 public void testGetRevision() {
 String revision = memory.getRevision();
 assertNotNull(revision);
 }

 @Test
 public void testMain() {
 Memory.main(new String[0]);
 }
}
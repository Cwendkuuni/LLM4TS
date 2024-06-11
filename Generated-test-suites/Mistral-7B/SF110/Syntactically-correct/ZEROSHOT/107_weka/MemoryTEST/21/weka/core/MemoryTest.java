package weka.core.MemoryTest;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 Memory testMemory;
 long initialMemory;
 long maxMemory;

 @Test
 public void testMemory() {
 testMemory = new Memory();
 initialMemory = testMemory.getInitial();
 maxMemory = testMemory.getMax();

 // Test isEnabled()
 Memory.setEnabled(true);
 assertTrue(Memory.isEnabled());
 Memory.setEnabled(false);
 assertFalse(Memory.isEnabled());

 // Test getUseGUI()
 Memory newMemory = new Memory(true);
 assertTrue(newMemory.getUseGUI());

 // Test getInitial()
 assertEquals(initialMemory, testMemory.getInitial());

 // Test getCurrent()
 testMemory.getCurrent();
 assertEquals(testMemory.getTotal(), testMemory.getCurrent());

 // Test getMax()
 assertEquals(maxMemory, testMemory.getMax());

 // Test isOutOfMemory()
 testMemory.setEnabled(true);
 long heapSize = Runtime.getRuntime().availableProcessors() * 1024 * 1024 * 5; // 5 GB
 testMemory.setInitial(initialMemory + heapSize);
 assertFalse(testMemory.isOutOfMemory());

 testMemory.setEnabled(true);
 testMemory.setInitial(initialMemory + maxMemory);
 assertFalse(testMemory.isOutOfMemory());

 testMemory.setEnabled(true);
 testMemory.setInitial(initialMemory + (maxMemory - 1000000000L)); // Out of memory
 assertTrue(testMemory.isOutOfMemory());

 // Test showOutOfMemory()
 testMemory.showOutOfMemory();

 // Test toMegaByte()
 assertEquals(testMemory.toMegaByte(1024 * 1024 * 5), 5.0, 0.1);
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsNull() {
 testMemory = new Memory();
 testMemory.stopThreads();
}

 @Test
 public void testStopThreads() {
 testMemory = new Memory();
 Thread[] threads = new Thread[5];
 for (int i = 0; i < threads.length; i++) {
 threads[i] = new Thread(() -> {
 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 });
 threads[i].start();
 }
 testMemory.stopThreads();
 for (Thread thread : threads) {
 assertFalse(thread.isAlive());
 }
}

 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 assertEquals("$Revision: 8034 $", memory.getRevision());
}

 @Test
 public void testMain() {
 Memory mem = new Memory();
 assertEquals("Initial memory: " + Memory.toMegaByte(mem.getInitial()) + "MB (" + mem.getInitial() + ")", "", System.out::println);
 assertEquals("Max memory: " + Memory.toMegaByte(mem.getMax()) + "MB (" + mem.getMax() + ")", "", System.out::println);
}

 static {
 Memory.setEnabled(true);
 }
}
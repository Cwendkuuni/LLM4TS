package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.Test;

public class MemoryTest {

 // Test constructor with default parameters
 @Test
 public void testMemoryDefaultConstructor() {
 Memory memory = new Memory();
 assertEquals(false, memory.getUseGUI());
 }

 // Test constructor with useGUI parameter
 @Test
 public void testMemoryConstructorWithUseGUI() {
 Memory memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 // Test getters and setters for enabled flag
 @Test
 public void testEnabledFlags() {
 Memory memory = new Memory();
 assertFalse(memory.isEnabled());
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 // Test getters and setters for useGUI flag
 @Test
 public void testUseGUIFlags() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());
 memory.setUseGUI(true);
 assertTrue(memory.getUseGUI());
 memory.setUseGUI(false);
 assertFalse(memory.getUseGUI());
 }

 // Test getInitial method
 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 long initialMemory = memory.getInitial();
 Runtime runtime = Runtime.getRuntime();
 long expectedInitial = runtime.totalMemory() - runtime.freeMemory();
 assertEquals(expectedInitial, initialMemory);
 }

 // Test getCurrent method
 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long currentMemory = memory.getCurrent();
 Runtime runtime = Runtime.getRuntime();
 long expectedCurrent = runtime.totalMemory() - runtime.freeMemory();
 assertEquals(expectedCurrent, currentMemory);
 }

 // Test getMax method
 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long maxMemory = memory.getMax();
 Runtime runtime = Runtime.getRuntime();
 long expectedMax = runtime.maxMemory();
 assertEquals(expectedMax, maxMemory);
 }

 // Test isOutOfMemory method
 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 long maxMemory = memory.getMax();
 long currentMemory = maxMemory - (long) (Math.random() * 50.0);
 memory.setCurrent(currentMemory);
 long initialMemory = memory.getInitial();
 long expectedFreeMemory = initialMemory + 200000L;
 assertFalse(memory.isOutOfMemory()); // Not out of memory, current < max + buffer
 memory.setCurrent(maxMemory - 1L);
 assertTrue(memory.isOutOfMemory()); // Out of memory, current > max - initial - buffer
 }

 // Test toMegaByte method
 @Test
 public void testToMegaByte() {
 Memory memory = new Memory();
 long bytes = 1024 * 1024 * 256L;
 double expectedMB = 0.25;
 assertEquals(expectedMB, memory.toMegaByte(bytes), 0.01);
 }

 // Test showOutOfMemory method
 @Test(expected = Error.class)
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setInitial(memory.getCurrent());
 memory.setMax(memory.getCurrent() - 1000L);
 memory.showOutOfMemory();
 }

 // Test stopThreads method
 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 Thread[] threads = new Thread[5];
 for (int i = 0; i < threads.length; i++) {
 threads[i] = new Thread(() -> {});
 threads[i].start();
 }
 memory.stopThreads();
 for (int i = 0; i < threads.length; i++) {
 assertFalse(threads[i].isAlive());
 }
 }

 // Test getRevision method
 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String expectedRevision = "$Revision: 8034 $";
 String actualRevision = memory.getRevision();
 assertEquals(expectedRevision, actualRevision);
 }
}
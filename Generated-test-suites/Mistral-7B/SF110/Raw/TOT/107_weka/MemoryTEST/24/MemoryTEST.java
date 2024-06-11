package weka.core.memory;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 // Test constructor with default arguments
 @Test
 public void testDefaultConstructor() {
 Memory memory = new Memory();
 assertEquals(true, memory.isEnabled());
 assertFalse(memory.getUseGUI());
 assertNull(memory.getRuntime());
 assertEquals(0, memory.getInitial());
 assertEquals(0, memory.getCurrent());
 assertEquals(0, memory.getMax());
 assertFalse(memory.isOutOfMemory());
 }

 // Test constructor with useGUI argument
 @Test
 public void testUseGUIConstructor() {
 Memory memory = new Memory(true);
 assertEquals(true, memory.isEnabled());
 assertTrue(memory.getUseGUI());
 assertNull(memory.getRuntime());
 assertEquals(0, memory.getInitial());
 assertEquals(0, memory.getCurrent());
 assertEquals(0, memory.getMax());
 assertFalse(memory.isOutOfMemory());
 }

 // Test getters and setters for enabled flag
 @Test
 public void testEnableFlag() {
 Memory memory = new Memory();
 assertTrue(memory.isEnabled());
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 }

 // Test getUseGUI method
 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 }

 // Test getInitial, getCurrent, and getMax methods with default constructor
 @Test
 public void testGetInitialCurrentMaxDefaultConstructor() {
 Memory memory = new Memory();
 long initial = Runtime.getRuntime().totalMemory();
 long current = memory.getCurrent();
 long max = Runtime.getRuntime().maxMemory();
 assertEquals(initial, memory.getInitial());
 assertEquals(current, memory.getCurrent());
 assertEquals(max, memory.getMax());
 }

 // Test isOutOfMemory method with default constructor
 @Test
 public void testIsOutOfMemoryDefaultConstructor() {
 Memory memory = new Memory();
 assertFalse(memory.isOutOfMemory());
 }

 // Test isOutOfMemory method with limited memory
 @Test
 public void testIsOutOfMemoryLimitedMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 Runtime runtime = Runtime.getRuntime();
 long max = memory.getMax();
 long initial = memory.getInitial();
 long total = runtime.totalMemory();
 long free = runtime.freeMemory();
 memory.setInitial(initial + 200001L);
 assertFalse(memory.isOutOfMemory());
 memory.setTotal(total - 200002L);
 assertTrue(memory.isOutOfMemory());
 }

 // Test toMegaByte method
 @Test
 public void testToMegaByte() {
 Memory memory = new Memory();
 long bytes = 1024 * 1024 * 1024; // 1GB
 assertEquals(1.0, memory.toMegaByte(bytes), 0.0);
 }

 // Test showOutOfMemory method (non-GUI)
 @Test(expected = Error.class)
 public void testShowOutOfMemoryNoGUI() {
 Memory memory = new Memory();
 memory.showOutOfMemory();
 }

 // Test showOutOfMemory method (GUI)
 @Test(expected = HeadlessException.class)
 public void testShowOutOfMemoryGUINoHeadless() {
 Memory memory = new Memory(true);
 memory.showOutOfMemory();
 }

 // Test stopThreads method
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
 assertFalse(thread.isAlive());
 }
 }

 // Test getRevision method
 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String revision = memory.getRevision();
 assertNotNull(revision);
 }

 // Test main method
 @Test
 public void testMain() {
 Memory memory = new Memory();
 memory.main(new String[0]);
 }

}
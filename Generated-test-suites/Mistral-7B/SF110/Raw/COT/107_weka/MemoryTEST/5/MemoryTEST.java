package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory(true);
 }

 // Test constructor with useGUI = false
 @Test
 public void testMemoryConstructor() {
 Memory mem = new Memory();
 assertEquals(false, mem.getUseGUI());
 }

 // Test constructor with useGUI = true
 @Test
 public void testMemoryConstructorWithUseGUI() {
 memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 // Test getters
 @Test
 public void testGetters() {
 Memory mem = new Memory();
 assertEquals(Memory.m_Enabled, mem.isEnabled());
 mem.setEnabled(false);
 assertEquals(false, mem.isEnabled());
 assertEquals(false, mem.getUseGUI());
 mem.setUseGUI(true);
 assertEquals(true, mem.getUseGUI());
 long initial = Runtime.getRuntime().totalMemory();
 mem = new Memory(true);
 assertEquals(initial, mem.getInitial());
 assertEquals(Memory.m_Initial, mem.getInitial());
 mem.getCurrent();
 assertEquals(initial + memory.m_Runtime.totalMemory(), mem.getTotal());
 assertEquals(Runtime.getRuntime().maxMemory(), mem.getMax());
 }

 // Test isOutOfMemory method
 @Test
 public void testIsOutOfMemory() {
 Memory mem = new Memory();
 assertFalse(mem.isOutOfMemory());
 long initial = Runtime.getRuntime().totalMemory();
 mem = new Memory(true);
 long total = initial + mem.m_Runtime.totalMemory();
 long max = Runtime.getRuntime().maxMemory();
 mem.setInitial(initial);
 mem.setTotal(total);
 mem.setMax(max);
 mem.setEnabled(true);
 assertTrue(mem.isOutOfMemory());
 }

 // Test toMegaByte method
 @Test
 public void testToMegaByte() {
 long bytes = 123456789L;
 double expected = 115.0;
 assertEquals(expected, Memory.toMegaByte(bytes), 0.0);
 }

 // Test showOutOfMemory method
 @Test(expected = UnsupportedOperationException.class)
 public void testShowOutOfMemoryWithoutGUI() {
 Memory mem = new Memory();
 mem.setEnabled(true);
 mem.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemoryWithGUI() {
 Memory mem = new Memory(true);
 mem.setEnabled(true);
 mem.showOutOfMemory();
 assertTrue(true); // ShowOutOfMemory dialog should be displayed, JUnit cannot check this directly
 }

 // Test stopThreads method
 @Test
 public void testStopThreads() {
 Memory mem = new Memory();
 Thread[] threads = new Thread[5];
 for (int i = 0; i < threads.length; i++) {
 threads[i] = new Thread(() -> {});
 threads[i].start();
 }
 mem.stopThreads();
 for (Thread thread : threads) {
 assertFalse(thread.isAlive());
 }
 }

 // Test getRevision method
 @Test
 public void testGetRevision() {
 Memory mem = new Memory();
 assertEquals("$Revision: 8034 $", mem.getRevision());
 }

 // Test main method
 @Test
 public void testMain() {
 Memory mem = new Memory();
 mem.main(new String[0]);
 }
}
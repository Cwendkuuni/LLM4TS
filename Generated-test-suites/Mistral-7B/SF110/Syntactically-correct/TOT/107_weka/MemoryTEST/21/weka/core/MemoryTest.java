package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.Test;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testConstructor() {
 memory = new Memory();
 assertFalse(memory.isUseGUI());
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
 }

 @Test
 public void testConstructorWithUseGUI() {
 memory = new Memory(true);
 assertTrue(memory.isUseGUI());
 }

 @Test
 public void testIsEnabled() {
 memory = new Memory();
 assertFalse(memory.isEnabled());
 Memory.setEnabled(true);
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 Memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 Memory memoryWithoutGUI = new Memory();
 assertFalse(memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 memory = new Memory();
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 memory = new Memory();
 Runtime runtime = Runtime.getRuntime();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 assertEquals(runtime.totalMemory() + 200000L, memory.getCurrent());
 }

 @Test
 public void testGetMax() {
 memory = new Memory();
 assertEquals(runtime.maxMemory(), memory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 memory = new Memory();
 Memory.setEnabled(true);
 memory.setInitial(Runtime.getRuntime().totalMemory() - 1000000);
 assertFalse(memory.isOutOfMemory());
 memory.setTotal(memory.getInitial() - 500000);
 assertTrue(memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryWithNullGUI() {
 memory = new Memory();
 memory.setEnabled(true);
 memory.setUseGUI(null);
 memory.showOutOfMemory();
 }

 @Test(expected = IllegalThreadStateException.class)
 public void testStopThreads() {
 Thread thread = new Thread(() -> { });
 memory = new Memory();
 memory.stopThreads();
 thread.start();
 memory.stopThreads();
 }

 @Test
 public void testToMegaByte() {
 long bytes = 123456789;
 assertEquals(11.85232276335417, Memory.toMegaByte(bytes), 0.0001);
 }
}
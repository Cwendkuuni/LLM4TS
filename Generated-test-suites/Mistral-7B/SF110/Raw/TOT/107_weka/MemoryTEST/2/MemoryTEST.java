package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.Test;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemoryConstructor() {
 memory = new Memory();
 assertEquals(false, memory.getUseGUI());
 assertEquals(0, memory.getInitial());
 assertEquals(false, memory.isEnabled());
 }

 @Test
 public void testMemoryConstructorWithUseGUI() {
 memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 memory = new Memory();
 memory.setEnabled(true);
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 memory = new Memory(true);
 memory = new Memory(false);
 assertFalse(memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 memory = new Memory();
 memory = new Memory(false);
 long initial = Runtime.getRuntime().totalMemory();
 assertEquals(initial, memory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 memory = new Memory();
 memory = new Memory(false);
 long current = Runtime.getRuntime().totalMemory();
 assertEquals(current, memory.getCurrent());
 }

 @Test
 public void testGetMax() {
 memory = new Memory();
 memory = new Memory(false);
 long max = Runtime.getRuntime().maxMemory();
 assertEquals(max, memory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 memory = new Memory();
 memory.setEnabled(true);
 long max = Runtime.getRuntime().maxMemory();
 long initial = Runtime.getRuntime().totalMemory();
 long current = Runtime.getRuntime().totalMemory();
 long expected = initial + 200000L;
 long limit = max - current - expected;
 memory.setInitial(initial);
 assertFalse(memory.isOutOfMemory());
 current += 1000000L;
 assertTrue(memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testToMegaByteNull() {
 Memory.toMegaByte(null);
 }

 @Test
 public void testToMegaByteValid() {
 long bytes = 1024L * 1024L * 1024L;
 double expected = 1.0;
 double result = Memory.toMegaByte(bytes);
 assertEquals(expected, result, 0.0);
 }

 @Test
 public void testShowOutOfMemory() {
 memory = new Memory();
 memory.setEnabled(true);
 Memory.showOutOfMemory();
 }

 @Test(expected = SecurityException.class)
 public void testStopThreads() {
 memory = new Memory();
 Thread thread = new Thread(() -> {});
 memory.stopThreads();
 thread.start();
 }
}
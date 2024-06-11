package weka.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemoryConstructor() {
 memory = new Memory();
 assertEquals(false, memory.isUseGUI());
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent(), 0);
 }

 @Test
 public void testMemoryConstructorUseGUI() {
 memory = new Memory(true);
 assertEquals(true, memory.isUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory.setEnabled(false);
 memory = new Memory();
 assertEquals(false, memory.isEnabled());
 Memory.setEnabled(true);
 memory = new Memory();
 assertEquals(true, memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 memory = new Memory();
 assertEquals(false, memory.getUseGUI());
 memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 memory = new Memory();
 long initial = Runtime.getRuntime().totalMemory();
 assertEquals(initial, memory.getInitial(), 0);
 }

 @Test
 public void testGetCurrent() {
 memory = new Memory();
 long current = Runtime.getRuntime().totalMemory();
 assertEquals(current, memory.getCurrent(), 0);
 }

 @Test
 public void testGetMax() {
 memory = new Memory();
 long max = Runtime.getRuntime().maxMemory();
 assertEquals(max, memory.getMax(), 0);
 }

 @Test
 public void testIsOutOfMemory() {
 memory = new Memory();
 Memory.setEnabled(true);
 long maxMemory = Runtime.getRuntime().maxMemory();
 long totalMemory = Runtime.getRuntime().totalMemory();
 long heapSize = maxMemory - totalMemory;
 long threshold = getThreshold();
 assertTrue(memory.isOutOfMemory());
 assertEquals(true, Memory.isEnabled());
 assertEquals(heapSize + Memory.getInitial(), totalMemory, 0);
 }

 @Test
 public void testIsOutOfMemoryFalse() {
 memory = new Memory();
 Memory.setEnabled(true);
 long maxMemory = Runtime.getRuntime().maxMemory();
 long totalMemory = Runtime.getRuntime().totalMemory();
 long heapSize = maxMemory - totalMemory;
 long threshold = getThreshold();
 assertFalse(memory.isOutOfMemory());
 }

 @Test
 public void testToMegaByte() {
 long bytes = 123456789L;
 double expected = 117.87601844210527;
 assertEquals(expected, Memory.toMegaByte(bytes), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryNullGUI() {
 memory = new Memory();
 memory.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemory() {
 memory = new Memory();
 Memory.setEnabled(true);
 Memory.m_Initial = 1024 * 1024;
 Memory.m_Total = 2048 * 1024;
 Memory.m_Max = 4096 * 1024;
 memory.showOutOfMemory();
 }

 @Test
 private long getThreshold() {
 return Memory.getInitial() + 200000L;
 }

}
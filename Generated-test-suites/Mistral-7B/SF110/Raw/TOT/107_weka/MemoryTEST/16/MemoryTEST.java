package weka.core.test;

import static org.junit.Assert.*;
import weka.core.Memory;

import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory(true);
 Memory.m_Enabled = true;
 Memory.m_Initial = Runtime.getRuntime().totalMemory();
 }

 @Test
 public void testIsEnabled() {
 Memory.m_Enabled = true;
 assertTrue(memory.isEnabled());
 Memory.m_Enabled = false;
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memory1 = new Memory();
 assertFalse(memory1.getUseGUI());
 Memory memory2 = new Memory(true);
 assertTrue(memory2.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 assertEquals(Memory.m_Initial, memory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long initial = memory.getCurrent();
 memory = new Memory();
 assertEquals(initial + (Runtime.getRuntime().totalMemory() - initial), memory.getCurrent());
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 long heapSize = Runtime.getRuntime().totalMemory() / 2;
 Runtime.getRuntime().exec("java -Xms" + heapSize + "m -Xmx" + heapSize + "m -classpath ./ Weka.class"); // Reduce available memory
 assertFalse(memory.isOutOfMemory());
 memory = new Memory();
 assertTrue(memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryWithGUIDisabled() {
 Memory memory = new Memory(false);
 memory.showOutOfMemory();
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreads() {
 Memory memory = new Memory();
 memory.stopThreads();
 }

 @Test
 public void testToMegaByte() {
 long size = 1024 * 1024 * 5;
 assertEquals(5.0, Memory.toMegaByte(size), 0.01);
 }
}
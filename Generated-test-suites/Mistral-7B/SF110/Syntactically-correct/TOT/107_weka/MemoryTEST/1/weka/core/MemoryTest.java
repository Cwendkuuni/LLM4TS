package weka.core.memory;

import static org.junit.Assert.*;

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
 public void testIsEnabled() {
 Memory.m_Enabled = true;
 assertTrue(memory.isEnabled());
 Memory.m_Enabled = false;
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testSetEnabled() {
 Memory.m_Enabled = false;
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 Memory.m_Enabled = false;
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memoryWithGUI = new Memory(true);
 assertTrue(memoryWithGUI.getUseGUI());
 Memory memoryWithoutGUI = new Memory(false);
 assertFalse(memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 assertEquals(Memory.m_Initial, memory.getInitial());
 memory = new Memory(false);
 assertEquals(Memory.m_Initial, memory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long initialTotalMemory = memory.getTotal();
 memory.getCurrent();
 long totalMemoryAfterCall = memory.getTotal();
 assertEquals("Current method should not change total memory", initialTotalMemory, totalMemoryAfterCall);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetCurrentWithoutInit() {
 Memory memory = new Memory();
 memory.getCurrent(); // Init total memory
 memory.setEnabled(false);
 memory.getCurrent(); // Should throw an exception
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 assertEquals(Memory.m_Max, memory.getMax());
 memory = new Memory(false);
 assertEquals(Memory.m_Max, memory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 long initialTotalMemory = memory.getTotal();
 long maxMemory = memory.getMax();
 memory.setTotal(initialTotalMemory + 1000000);
 assertFalse(memory.isOutOfMemory());
 memory.setTotal(initialTotalMemory + maxMemory + 1000000);
 assertTrue(memory.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.showOutOfMemory();
 Memory memoryWithGUI = new Memory(true);
 memoryWithGUI.showOutOfMemory();
 }

 @Test
 public void testToMegaByte() {
 double toMB = Memory.toMegaByte(1000000);
 assertEquals(1.0, toMB, 0.0);
 }
}
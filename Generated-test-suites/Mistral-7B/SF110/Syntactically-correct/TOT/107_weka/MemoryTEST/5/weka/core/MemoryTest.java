package weka.core.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
 Memory.m_Enabled = true;
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
 assertEquals(memory.getInitial(), Runtime.getRuntime().totalMemory());
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 memory.getCurrent();
 memory.getCurrent(); // call it again to consume some memory and change the total
 assertEquals(memory.getCurrent(), Runtime.getRuntime().totalMemory());
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 assertEquals(memory.getMax(), Runtime.getRuntime().maxMemory());
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 assertFalse(memory.isOutOfMemory());
 Runtime.getRuntime().gc();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 assertFalse(memory.isOutOfMemory()); // reset Memory.m_Enabled after testing showOutOfMemory
 Runtime.getRuntime().runFinalization();
 memory.setEnabled(true);
 long initialMemory = memory.getInitial();
 memory.stopThreads();
 long memoryConsumed = Runtime.getRuntime().totalMemory() - initialMemory;
 long maxMemory = Runtime.getRuntime().maxMemory();
 long threshold = initialMemory + 200000;
 Memory memoryWithOutOfMemoryCondition = new Memory();
 memoryWithOutOfMemoryCondition.setEnabled(true);
 memoryWithOutOfMemoryCondition.maxMemory(maxMemory - memoryConsumed);
 assertTrue(memoryWithOutOfMemoryCondition.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryWithNullUseGUI() {
 Memory memory = new Memory();
 memory.showOutOfMemory();
 memory.showOutOfMemory(null);
 }
}
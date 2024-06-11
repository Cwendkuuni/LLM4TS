package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {

 Memory memory;

 @Test
 public void testMemoryInitialization() {
 memory = new Memory();
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
 assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
 }

 @Test
 public void testMemoryInitializationWithUseGUI() {
 memory = new Memory(true);
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
 assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
 }

 @Test
 public void testIsEnabled() {
 Memory.m_Enabled = true;
 memory = new Memory();
 assertTrue(memory.isEnabled());
 Memory.m_Enabled = false;
 memory = new Memory();
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memoryWithGUI = new Memory(true);
 assertTrue(memoryWithGUI.getUseGUI());
 Memory memoryWithoutGUI = new Memory();
 assertFalse(memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 memory = new Memory();
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
 Runtime.getRuntime().gc();
 assertEquals(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), memory.getCurrent());
 }

 @Test
 public void testGetMax() {
 memory = new Memory();
 assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 memory = new Memory();
 assertFalse(memory.isOutOfMemory());
 Runtime.getRuntime().gc();
 Runtime.getRuntime().runFinalization();
 Runtime.getRuntime().gc();
 memory.setEnabled(true);
 Runtime.getRuntime().gc();
 Runtime.getRuntime().runFinalization();
 Runtime.getRuntime().gc();
 long maxMemory = Runtime.getRuntime().maxMemory();
 long totalMemory = Runtime.getRuntime().totalMemory();
 memory.setInitial(totalMemory);
 long currentMemory = totalMemory / 2;
 assertEquals(currentMemory, memory.getCurrent());
 assertEquals(maxMemory, memory.getMax());
 // Allocate memory to simulate out of memory condition
 long allocatedMemory = maxMemory - currentMemory;
 try {
 byte[] largeArray = new byte[allocatedMemory];
 } catch (OutOfMemoryError e) {
 // Expected
 assertTrue(memory.isOutOfMemory());
 }
 }

 @Test
 public void testShowOutOfMemory() {
 memory = new Memory();
 memory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 memory = new Memory();
 assertThrows(UnsupportedOperationException.class, () -> memory.stopThreads());
 }

 @Test
 public void testRevisionHandler() {
 assertNotNull(memory.getRevision());
 }

 @Test
 public void testMain() {
 memory = new Memory();
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial(), 0.1);
 assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax(), 0.1);
 }
}
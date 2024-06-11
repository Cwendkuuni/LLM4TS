package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemory() {
 Memory memory = new Memory();
 // Test constructor without GUI
 assertFalse(memory.getUseGUI());
 Memory memoryWithGUI = new Memory(true);
 assertTrue(memoryWithGUI.getUseGUI());

 // Test getters
 assertEquals(memory.isEnabled(), Memory.m_Enabled);
 Memory.setEnabled(true);
 assertEquals(memory.isEnabled(), true);
 assertEquals(memoryWithGUI.getUseGUI(), true);

 // Test methods without GUI
 assertEquals(memory.getInitial(), Memory.m_Initial);
 assertEquals(memory.getCurrent(), Memory.m_Initial);
 assertEquals(memory.getMax(), Memory.m_Max);
 assertEquals(memory.isOutOfMemory(), false);

 // Test methods with GUI
 memoryWithGUI = new Memory(true);
 assertEquals(memoryWithGUI.getInitial(), Memory.m_Initial);
 assertEquals(memoryWithGUI.getCurrent(), Memory.m_Initial);
 assertEquals(memoryWithGUI.getMax(), Memory.m_Max);
 assertFalse(memoryWithGUI.isOutOfMemory());

 // Test showOutOfMemory method
 memoryWithGUI.setEnabled(true);
 Memory.m_Initial += 50 * 1024 * 1024; // set initial memory to 50MB
 memoryWithGUI.showOutOfMemory();

 // Test stopThreads method
 memoryWithGUI.stopThreads();
 // We cannot verify this method directly as it stops all threads and we cannot check if all threads are stopped
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsWithNullArgument() {
 Memory memory = new Memory();
 memory.stopThreads(null);
 }

 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String revision = memory.getRevision();
 assertNotNull(revision);
}

 @Test
 public void testMain() {
 Memory memory = new Memory();
 memory.main(new String[0]);
}

}
package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemory() {
 memory = new Memory();
 // Test constructor with default useGUI
 assertEquals(false, memory.getUseGUI());
 // Test constructor with useGUI enabled
 memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 // Test getters and setters
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 // Test isOutOfMemory method
 memory.setEnabled(true);
 memory.setTotal(Memory.m_Initial + 1024 * 1024 * 5); // Set total memory to be less than max
 assertFalse(memory.isOutOfMemory());
 memory.setTotal(Memory.m_Initial + 1024 * 1024 * 6); // Set total memory to be greater than max
 assertTrue(memory.isOutOfMemory());
 // Test toMegaByte method
 assertEquals(1.0, Memory.toMegaByte(1024 * 1024), 0.0);
 // Test showOutOfMemory method
 memory.showOutOfMemory();
 // Test stopThreads method
 memory.stopThreads();
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsWithNullThreadGroup() {
 Memory memory = new Memory();
 Thread[] thGroup = null;
 memory.stopThreads();
 }

 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 assertEquals("$Revision: 8034 $", memory.getRevision());
 }

 @Test
 public void testMain() {
 Memory memory = new Memory();
 memory.main(new String[0]);
 }

 @org.junit.BeforeClass
 public static void staticInitializer() {
 Memory memory = new Memory();
 assertEquals(true, Memory.m_Enabled);
 assertEquals(memory.getInitial(), Runtime.getRuntime().totalMemory());
 }
}
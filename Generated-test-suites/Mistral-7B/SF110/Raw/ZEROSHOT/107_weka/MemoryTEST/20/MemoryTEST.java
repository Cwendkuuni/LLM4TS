package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemory() {
 memory = new Memory();
 // Test constructor without GUI
 assertEquals(false, memory.getUseGUI());
 // Test constructor with GUI
 memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 // Test getters
 assertEquals(Memory.m_Enabled, memory.isEnabled());
 assertEquals(Memory.m_Initial, memory.getInitial());
 assertEquals(Memory.m_Total, memory.getCurrent());
 assertEquals(Memory.m_Max, memory.getMax());
 // Test isOutOfMemory method
 Memory.m_Enabled = true;
 Memory.m_Initial = 10 * (1024 * 1024); // Set initial memory to 10MB
 Memory.m_Max = 20 * (1024 * 1024); // Set max memory to 20MB
 memory = new Memory();
 assertTrue(memory.isOutOfMemory());
 // Test showOutOfMemory method
 memory.showOutOfMemory();
 // Test stopThreads method
 memory.stopThreads();
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsWithNullThreads() {
 Memory.m_Enabled = true;
 Memory memory = new Memory();
 memory.stopThreads();
 Thread[] thGroup = null;
 memory.stopThreads(thGroup);
 }

 @Test
 public void testToMegaByte() {
 assertEquals(4.2150837609375D, Memory.toMegaByte(4338813688L), 0.00001D); // 4GB
 }

 @Test
 public void testMain() {
 Memory memory = new Memory();
 memory.main(new String[0]);
 }
}
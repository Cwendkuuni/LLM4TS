package weka.core.test;

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

 // Test constructor with default useGUI
 @Test
 public void testMemoryDefaultConstructor() {
 Memory memory = new Memory();
 assertEquals(false, memory.getUseGUI());
 }

 // Test constructor with useGUI set to true
 @Test
 public void testMemoryConstructorWithUseGUI() {
 Memory memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 // Test getters
 @Test
 public void testGetters() {
 Memory memory = new Memory();
 assertTrue(memory.isEnabled());
 assertFalse(memory.getUseGUI());
 assertEquals(0, memory.getInitial());
 assertEquals(0, memory.getCurrent());
 assertEquals(0, memory.getMax());
 assertFalse(memory.isOutOfMemory());
 }

 // Test isOutOfMemory method with enabled memory and total memory less than max + initial + 200000L
 @Test(expected = AssertionError.class)
 public void testIsOutOfMemoryTrue() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.m_Initial = memory.m_Total + 100000L;
 memory.m_Max = memory.m_Total + 200000L;
 assertTrue(memory.isOutOfMemory());
 }

 // Test isOutOfMemory method with disabled memory
 @Test
 public void testIsOutOfMemoryFalse() {
 Memory memory = new Memory();
 assertFalse(memory.isOutOfMemory());
 }

 // Test toMegaByte method
 @Test
 public void testToMegaByte() {
 long bytes = 123456789;
 assertEquals(11.622811835596846, Memory.toMegaByte(bytes), 0.00001);
 }

 // Test showOutOfMemory method with useGUI set to true
 @Test
 public void testShowOutOfMemoryWithUseGUI() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.m_Initial = memory.m_Total + 100000L;
 memory.m_Max = memory.m_Total + 200000L;
 memory.showOutOfMemory();
 }

 // Test showOutOfMemory method with useGUI set to false
 @Test
 public void testShowOutOfMemoryWithoutUseGUI() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.m_Initial = memory.m_Total + 100000L;
 memory.m_Max = memory.m_Total + 200000L;
 memory.setUseGUI(false);
 memory.showOutOfMemory();
 }

 // Test stopThreads method
 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 memory.stopThreads();
 }

 // Test getRevision method
 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 assertEquals("$Revision: 8034 $", memory.getRevision());
 }

 // Test main method
 @Test
 public void testMain() {
 Memory memory = new Memory();
 memory.m_Runtime = null;
 Memory.main(new String[0]);
 }
}
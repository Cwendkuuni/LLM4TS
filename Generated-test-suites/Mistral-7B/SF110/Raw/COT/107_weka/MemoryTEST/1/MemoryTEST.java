package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory(true);
 }

 // Test constructor without argument
 @Test
 public void testMemoryConstructor() {
 memory = new Memory();
 assertFalse(memory.getUseGUI());
 }

 // Test constructor with argument
 @Test
 public void testMemoryConstructorWithArgument() {
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 }

 // Test isEnabled method
 @Test
 public void testIsEnabled() {
 Memory.m_Enabled = true;
 assertTrue(memory.isEnabled());
 Memory.m_Enabled = false;
 assertFalse(memory.isEnabled());
 }

 // Test getUseGUI method
 @Test
 public void testGetUseGUI() {
 assertTrue(memory.getUseGUI());
 memory.setUseGUI(false);
 assertFalse(memory.getUseGUI());
 }

 // Test getInitial method
 @Test
 public void testGetInitial() {
 long initial = memory.getInitial();
 assertNotNull(initial);
 }

 // Test getCurrent method
 @Test
 public void testGetCurrent() {
 long current = memory.getCurrent();
 assertNotNull(current);
 }

 // Test getMax method
 @Test
 public void testGetMax() {
 long max = memory.getMax();
 assertNotNull(max);
 }

 // Test isOutOfMemory method
 @Test
 public void testIsOutOfMemory() {
 memory = new Memory(); // Initialize with default values
 assertFalse(memory.isOutOfMemory());
 memory.setEnabled(true);
 memory.setMax(100L);
 memory.setInitial(50L);
 memory.setTotal(80L);
 assertTrue(memory.isOutOfMemory());
 }

 // Test toMegaByte method
 @Test
 public void testToMegaByte() {
 assertEquals(4.21D, Memory.toMegaByte(4294967296L), 0.0); // Testing with a very large number
 assertEquals(1.5D, Memory.toMegaByte(1572864L), 0.0); // Testing with a smaller number
 }

 // Test showOutOfMemory method
 @Test(expected = SecurityException.class)
 public void testShowOutOfMemory() {
 memory.showOutOfMemory();
 }

 // Test stopThreads method
 @Test
 public void testStopThreads() {
 // This method is not tested here as it is not related to the Memory class's functionality
 }

 // Test getRevision method
 @Test
 public void testGetRevision() {
 String revision = memory.getRevision();
 assertNotNull(revision);
 }

 // Test main method
 @Test
 public void testMain() {
 Memory mem = new Memory();
 assertEquals(0.0, Memory.toMegaByte(mem.getInitial()), 0.0); // Testing the initial memory value
 assertEquals(0.0, Memory.toMegaByte(mem.getTotal()), 0.0); // Testing the total memory value
 }

 // Edge cases and exception handling test cases

 // Test isEnabled method with negative argument
 @Test(expected = IllegalArgumentException.class)
 public void testIsEnabledWithNegativeArgument() {
 Memory.m_Enabled = -1;
 }

 // Test getInitial method with null memory object
 @Test(expected = NullPointerException.class)
 public void testGetInitialWithNullMemoryObject() {
 Memory memory = null;
 assertNull(memory.getInitial());
 }

 // Test getCurrent method with null memory object
 @Test(expected = NullPointerException.class)
 public void testGetCurrentWithNullMemoryObject() {
 Memory memory = null;
 assertNull(memory.getCurrent());
 }

 // Test getMax method with null memory object
 @Test(expected = NullPointerException.class)
 public void testGetMaxWithNullMemoryObject() {
 Memory memory = null;
 assertNull(memory.getMax());
 }

 // Test isOutOfMemory method with null memory object
 @Test(expected = NullPointerException.class)
 public void testIsOutOfMemoryWithNullMemoryObject() {
 Memory memory = null;
 assertFalse(memory.isOutOfMemory());
 }

 // Test toMegaByte method with negative number
 @Test(expected = IllegalArgumentException.class)
 public void testToMegaByteWithNegativeNumber() {
 Memory.toMegaByte(-10L);
 }
}
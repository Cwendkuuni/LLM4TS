package weka.core. MemoryTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory(true);
 }

 @Test
 public void testMemoryConstructorDefault() {
 Memory memoryDefault = new Memory();
 assertNotNull(memoryDefault);
 assertEquals(Runtime.getRuntime().totalMemory(), memoryDefault.getTotal());
 }

 @Test
 public void testMemoryConstructorWithUseGUI() {
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 Memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 memory = new Memory(false);
 assertFalse(memory.getUseGUI());
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 memory = new Memory();
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 memory = new Memory();
 long initialTotalMemory = Runtime.getRuntime().totalMemory();
 memory.getCurrent();
 long currentTotalMemory = Runtime.getRuntime().totalMemory();
 assertEquals(initialTotalMemory, currentTotalMemory);
 }

 @Test
 public void testGetMax() {
 memory = new Memory();
 long initialMaxMemory = memory.getMax();
 long expectedMaxMemory = Runtime.getRuntime().maxMemory();
 assertEquals(expectedMaxMemory, initialMaxMemory);
 }

 @Test
 public void testIsOutOfMemory() {
 long initialTotalMemory = Runtime.getRuntime().totalMemory();
 long expectedInitial = initialTotalMemory;
 memory = new Memory();
 assertTrue(memory.isOutOfMemory());
 long currentTotalMemory = Runtime.getRuntime().totalMemory();
 long expectedCurrent = currentTotalMemory;
 assertNotSame(expectedInitial, expectedCurrent);
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memoryWithOutOfMemory = new Memory();
 memoryWithOutOfMemory.showOutOfMemory();
 }

 @Test(expected = RuntimeException.class)
 public void testStopThreads() {
 Memory memoryWithStopThreads = new Memory();
 memoryWithStopThreads.stopThreads();
 }

 @Test
 public void testGetRevision() {
 Memory memoryRevision = new Memory();
 assertEquals("$Revision: 8034 $", memoryRevision.getRevision());
 }

 @Test
 public void testMain() {
 Memory memoryMain = new Memory();
 String initialMemory = "Initial memory: " + Memory.toMegaByte(memoryMain.getInitial()) + "MB (" + memoryMain.getInitial() + ")";
 String maxMemory = "Max memory: " + Memory.toMegaByte(memoryMain.getMax()) + "MB (" + memoryMain.getMax() + ")";
 assertEquals(initialMemory, memoryMain.toString());
 assertEquals(maxMemory, memoryMain.toString());
 }
}
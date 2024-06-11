package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemoryConstructors() {
 Memory memoryWithGUI = new Memory(true);
 Memory memoryWithoutGUI = new Memory();

 assertEquals(true, memoryWithGUI.getUseGUI());
 assertEquals(false, memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 assertTrue(Memory.m_Enabled);
 memory.setEnabled(false);
 assertFalse(Memory.m_Enabled);
 }

 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 Memory memoryWithTotal = new Memory(true);
 long initial = Runtime.getRuntime().totalMemory();

 assertEquals(initial, memory.getInitial());
 assertEquals(initial, memoryWithTotal.getInitial());
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long totalMemory = Runtime.getRuntime().totalMemory();
 long currentMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

 assertEquals(totalMemory, memory.getCurrent());
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long maxMemory = Runtime.getRuntime().maxMemory();

 assertEquals(maxMemory, memory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 long currentMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() + 1000000;

 assertFalse(memory.isOutOfMemory());
 memory.setEnabled(true);
 memory.setInitial(Runtime.getRuntime().totalMemory() - currentMemory);
 assertTrue(memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryNullGUI() {
 Memory memory = new Memory();
 memory.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setInitial(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() + 1000000);
 memory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 memory.stopThreads();
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
 System.out.println("Initial memory: " + Memory.toMegaByte(memory.getInitial()) + "MB (" + memory.getInitial() + ")");
 System.out.println("Max memory: " + Memory.toMegaByte(memory.getMax()) + "MB (" + memory.getMax() + ")");
 }

 static {
 Memory memory = new Memory();
 }
}
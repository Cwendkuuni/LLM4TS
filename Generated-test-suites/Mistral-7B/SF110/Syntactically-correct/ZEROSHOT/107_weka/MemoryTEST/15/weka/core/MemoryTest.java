package weka.core.MemoryTest;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 Memory testMemory;

 @Test
 public void testMemory() {
 testMemory = new Memory();
 assertFalse(testMemory.isOutOfMemory());
 }

 @Test
 public void testMemoryWithInitialSet() {
 testMemory = new Memory(false);
 assertFalse(testMemory.isOutOfMemory());
 }

 @Test
 public void testIsEnabled() {
 testMemory = new Memory();
 testMemory.setEnabled(true);
 testMemory = new Memory(true);
 assertTrue(testMemory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 testMemory = new Memory();
 assertFalse(testMemory.getUseGUI());
 testMemory = new Memory(true);
 assertTrue(testMemory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 testMemory = new Memory();
 long initial = testMemory.getInitial();
 testMemory = new Memory();
 assertEquals(initial, testMemory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 testMemory = new Memory();
 long current = testMemory.getCurrent();
 testMemory = new Memory();
 assertEquals(current, testMemory.getCurrent());
 }

 @Test
 public void testGetMax() {
 testMemory = new Memory();
 long max = testMemory.getMax();
 testMemory = new Memory();
 assertEquals(max, testMemory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 testMemory = new Memory();
 assertFalse(testMemory.isOutOfMemory());
 Memory testMemory2 = new Memory();
 testMemory2.setEnabled(true);
 testMemory2.setMax(testMemory2.getCurrent() - 200001L);
 assertTrue(testMemory2.isOutOfMemory());
 }

 @Test
 public void testToMegaByte() {
 Memory memory = new Memory();
 assertEquals(0.0, memory.toMegaByte(0L), 0.0);
 assertEquals(1.0, memory.toMegaByte(1048576L), 1.0);
 assertEquals(1.5, memory.toMegaByte(16777216L), 1.5);
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryNullGui() {
 testMemory = new Memory();
 testMemory.setEnabled(true);
 testMemory.m_UseGUI = null;
 testMemory.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemory() {
 testMemory = new Memory();
 testMemory.setEnabled(true);
 testMemory.m_UseGUI = true;
 testMemory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 testMemory = new Memory();
 testMemory.stopThreads();
 }
}
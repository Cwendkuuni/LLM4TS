package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.Test;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemoryConstructor() {
 memory = new Memory();
 assertEquals(false, memory.getUseGUI());
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
 }

 @Test
 public void testMemoryConstructorWithUseGUI() {
 memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 memory = new Memory();
 assertFalse(memory.isEnabled());
 memory.setEnabled(true);
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
 long initial = memory.getInitial();
 long max = memory.getMax();
 memory.setEnabled(true);
 memory.setInitial(initial - 1000000);
 memory.setMax(max - 500000);
 assertTrue(memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryNullGui() {
 memory = new Memory();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 memory.setUseGUI(null);
 memory.showOutOfMemory();
 }
}
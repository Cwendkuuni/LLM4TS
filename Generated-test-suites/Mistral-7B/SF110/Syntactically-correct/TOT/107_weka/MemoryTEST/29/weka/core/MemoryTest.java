package weka.core.test;

import static org.junit.Assert.*;

import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;

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
 public void testIsEnabled() {
 Memory.m_Enabled = true;
 assertTrue(memory.isEnabled());
 Memory.m_Enabled = false;
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testSetEnabled() {
 memory.setEnabled(true);
 assertTrue(Memory.m_Enabled);
 memory.setEnabled(false);
 assertFalse(Memory.m_Enabled);
 }

 @Test
 public void testGetUseGUI() {
 assertTrue(memory.getUseGUI());
 Memory memoryWithoutGUI = new Memory();
 assertFalse(memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 assertEquals(memory.getInitial(), Runtime.getRuntime().totalMemory());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetInitialAfterSetEnabledFalse() {
 Memory.m_Enabled = false;
 memory = new Memory();
 memory.getInitial();
 }

 @Test(expected = IllegalStateException.class)
 public void testSetEnabledAfterInstanceCreated() {
 memory = new Memory(true);
 memory.setEnabled(false);
 }

 @Test(expected = HeadlessException.class)
 public void testShowOutOfMemoryGUI() throws InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException {
 Memory memory = new Memory();
 memory.getClass().getDeclaredMethod("showOutOfMemory").invoke(memory);
 }

 @Test
 public void testGetCurrent() {
 Runtime runtime = Runtime.getRuntime();
 Memory memory = new Memory();
 assertEquals(memory.getCurrent(), runtime.totalMemory());
 }

 @Test
 public void testGetMax() {
 Runtime runtime = Runtime.getRuntime();
 Memory memory = new Memory();
 assertEquals(memory.getMax(), runtime.maxMemory());
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 long initial = memory.getInitial();
 memory.setEnabled(false);
 memory.setEnabled(true);
 assertFalse(memory.isOutOfMemory());
 memory.setTotal(initial - 1000000L);
 assertTrue(memory.isOutOfMemory());
 }

 @Test
 public void testToMegaByte() {
 assertEquals(Memory.toMegaByte(1024 * 1024 * 1024), 1024.0, 0.0);
 }
}
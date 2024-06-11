package weka.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
 public void testMemoryInitialization() {
 Memory.m_Enabled = false;
 Memory mem = new Memory();
 assertFalse(Memory.m_Enabled);
 assertEquals(0, mem.getInitial(), 0.0);
 Memory.m_Enabled = true;
 mem = new Memory();
 assertTrue(Memory.m_Enabled);
 memory = new Memory(true);
 }

 @Test
 public void testGetUseGUI() {
 Memory mem = new Memory();
 assertFalse(mem.getUseGUI());
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory.m_Enabled = false;
 Memory mem = new Memory();
 assertFalse(mem.isEnabled());
 Memory.m_Enabled = true;
 mem = new Memory();
 assertTrue(mem.isEnabled());
 }

 @Test
 public void testGetCurrent() {
 Memory mem = new Memory();
 long initial = mem.getCurrent();
 mem.setEnabled(true);
 mem.getCurrent();
 long total = mem.getCurrent();
 assertEquals(initial, total);
 }

 @Test
 public void testGetMax() {
 Memory mem = new Memory();
 long max = mem.getMax();
 mem.setEnabled(true);
 long maxNew = mem.getMax();
 assertEquals(max, maxNew);
 }

 @Test(expected = RuntimeException.class)
 public void testIsOutOfMemoryNegative() {
 Memory memory = new Memory();
 memory.isOutOfMemory(-1000L);
 }

 @Test
 public void testIsOutOfMemoryPositive() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setMax(1L);
 memory.setInitial(0L);
 memory.setTotal(1L);
 assertTrue(memory.isOutOfMemory());
 memory.setTotal(memory.getMax() + 1L);
 assertFalse(memory.isOutOfMemory());
 }

 @Test
 public void testToMegaByte() {
 Memory memory = new Memory();
 long bytes = 123456789;
 double expected = 119.463223621526;
 assertEquals(expected, memory.toMegaByte(bytes), 0.000001);
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setMax(1024L);
 memory.setInitial(512L);
 memory.setTotal(1536L);
 memory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.stopThreads();
 }

 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String expected = "8034";
 assertEquals(expected, memory.getRevision().split(" ")[1]);
 }
}
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

 @Test
 public void testMemoryConstructors() {
 Memory memory1 = new Memory();
 Memory memory2 = new Memory(true);
 assertEquals(false, memory1.getUseGUI());
 assertEquals(true, memory2.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 assertFalse(memory.isEnabled());
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 memory = new Memory(false);
 assertFalse(memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 memory = new Memory(true);
 long newInitial = memory.getInitial();
 assertEquals(initial, newInitial);
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long current = memory.getCurrent();
 memory.setEnabled(true);
 memory.getCurrent();
 long newCurrent = memory.getCurrent();
 assertTrue(newCurrent > current);
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long max = memory.getMax();
 memory = new Memory(true);
 long newMax = memory.getMax();
 assertEquals(max, newMax);
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 assertFalse(memory.isOutOfMemory());
 memory.setEnabled(true);
 long initial = memory.getInitial();
 long max = memory.getMax();
 long total = max - initial - 200001L;
 memory.setTotal(total);
 assertTrue(memory.isOutOfMemory());
 }

 @Test(expected = IllegalStateException.class)
 public void testToMegaByteWithNegativeBytes() {
 Memory.toMegaByte(-1L);
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
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
 String revision = memory.getRevision();
 assertNotNull(revision);
 }

 @Test
 public void testMain() {
 Memory memory = new Memory();
 memory.main(new String[0]);
 }
}
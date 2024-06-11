package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory mem;

 @Test
 public void testMemory() {
 mem = new Memory();
 assertEquals(true, Memory.m_Enabled);
 assertNotNull(mem.getRuntime());
 }

 @Test
 public void testMemoryEnabled() {
 Memory.m_Enabled = false;
 mem = new Memory();
 assertFalse(Memory.m_Enabled);
 }

 @Test
 public void testMemoryUseGUI() {
 Memory memWithGUI = new Memory(true);
 assertTrue(memWithGUI.getUseGUI());
 }

 @Test
 public void testMemoryInitial() {
 mem = new Memory();
 long initial = mem.getInitial();
 long max = mem.getMax();
 long total = mem.getCurrent();
 long initialMB = Memory.toMegaByte(initial);
 long maxMB = Memory.toMegaByte(max);
 long totalMB = Memory.toMegaByte(total);

 assertEquals(initialMB, mem.getInitial());
 assertEquals(maxMB, mem.getMax());
 assertEquals(totalMB, mem.getCurrent());
 }

 @Test
 public void testMemoryIsOutOfMemory() {
 mem = new Memory();
 long max = mem.getMax();
 long total = max * 2;
 mem.setTotal(total);
 assertFalse(mem.isOutOfMemory());

 total += 5000000;
 assertTrue(mem.isOutOfMemory());
 }

 @Test
 public void testMemoryShowOutOfMemory() {
 mem = new Memory();
 mem.setEnabled(true);
 mem.showOutOfMemory();
 }

 @Test
 public void testMemoryStopThreads() {
 mem = new Memory();
 mem.setEnabled(true);
 mem.stopThreads();
 }

 @Test
 public void testMemoryGetRevision() {
 Memory memWithRevision = new Memory();
 assertEquals("$Revision: 8034 $", memWithRevision.getRevision());
 }

 @Test
 public void testMemoryMain() {
 Memory mem = new Memory();
 mem.main(new String[0]);
 }
}
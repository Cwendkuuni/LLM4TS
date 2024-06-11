package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemory() {
 memory = new Memory();
 assertFalse(memory.isOutOfMemory());
 }

 @Test
 public void testMemoryWithInitialization() {
 memory = new Memory(true);
 memory.setEnabled(true);
 long initial = memory.getInitial();
 long max = memory.getMax();
 long total = memory.getCurrent();
 assertEquals(initial, total);
 assertTrue(memory.isOutOfMemory());
 memory.showOutOfMemory();
 }

 @Test
 public void testToMegaByte() {
 long bytes = 123456789;
 double megaBytes = Memory.toMegaByte(bytes);
 assertEquals(11.6, megaBytes, 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMemoryNullArgument() {
 Memory.toMegaByte(null);
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
 memory.setEnabled(true);
 long initial = memory.getInitial();
 long max = memory.getMax();
 long total = memory.getCurrent();
 assertEquals(initial, total);
 assertEquals(Memory.class.getSimpleName(), Memory.getRevision().split(": ")[1].trim());
 }

}
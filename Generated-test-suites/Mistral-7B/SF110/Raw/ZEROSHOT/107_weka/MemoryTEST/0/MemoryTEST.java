package weka.core.test;

import static org.junit.Assert.assertEquals;
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
 public void testMemoryConstruction() {
 Memory memoryWithGUI = new Memory(true);
 Memory memoryWithoutGUI = new Memory(false);

 assertEquals(true, memoryWithGUI.getUseGUI());
 assertEquals(false, memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testMemoryProperties() {
 long initial = memory.getInitial();
 long total = memory.getCurrent();
 long max = memory.getMax();

 assertEquals(initial, memory.getInitial());
 assertEquals(total, memory.getCurrent());
 assertEquals(max, memory.getMax());
 }

 @Test
 public void testMemoryOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 long max = memory.getMax();
 long initial = memory.getInitial();

 memory.setEnabled(false);
 long total = memory.getCurrent();

 assertEquals(true, memory.isEnabled());
 assertEquals(max, memory.getMax());
 assertEquals(initial, memory.getInitial());
 assertEquals(false, memory.isOutOfMemory());

 memory.setEnabled(true);
 total += 10000000; // consume more memory

 assertEquals(max, memory.getMax());
 assertEquals(initial, memory.getInitial());
 assertEquals(true, memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testThreadInterruption() {
 Memory memory = new Memory();
 memory.stopThreads();
 }
}
package weka.core.memory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;
import weka.core.RevisionUtils;
import weka.core.Utils;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory(true);
 }

 @Test
 public void testIsEnabled() {
 Memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 Memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 memory = new Memory(false);
 assertFalse(memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 long initial = memory.getInitial();
 Memory.setEnabled(true);
 long newInitial = memory.getInitial();
 assertEquals(initial, newInitial);
 }

 @Test
 public void testGetCurrent() {
 long initial = memory.getCurrent();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 long newCurrent = memory.getCurrent();
 assertEquals(initial + 200000L, newCurrent);
 }

 @Test
 public void testGetMax() {
 long max = memory.getMax();
 assertEquals(Runtime.getRuntime().maxMemory(), max);
 }

 @Test
 public void testIsOutOfMemory() {
 Memory.setEnabled(true);
 long max = memory.getMax();
 long initial = memory.getInitial();
 long current = memory.getCurrent();
 long expected = initial + 200000L;
 long actual = current + Memory.toMegaByte(200000L).longValue();
 assertFalse(memory.isOutOfMemory());
 memory.showOutOfMemory();
 assertFalse(memory.isOutOfMemory());
 Memory.setEnabled(false);
 memory = new Memory(true);
 Memory.setEnabled(true);
 memory.showOutOfMemory();
 assertTrue(memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryWhenDisabled() {
 Memory.setEnabled(false);
 memory.showOutOfMemory();
 }

 @Test
 public void testToMegaByte() {
 assertEquals(1.2, Memory.toMegaByte(1234567), 0.0);
 }

 @Test
 public void testStopThreads() {
 memory.stopThreads();
 fail("Expected NullPointerException, but it did not occur.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testThreadsEnumWhenNoThreadsRunning() {
 Thread[] threads = new Thread[0];
 Thread.enumerate(threads);
 }

 @Test
 public void testRevision() {
 String revision = memory.getRevision();
 String expected = RevisionUtils.extract("$Revision: 8034 $");
 assertEquals(expected, revision);
 }

 @Test
 public void testMain() {
 Memory mem = new Memory();
 assertEquals(Utils.doubleToString(Memory.toMegaByte(mem.getInitial()), 1), "Initial memory: ", Memory.class.getDeclaredMethod("getInitial").invoke(mem));
 assertEquals(Utils.doubleToString(Memory.toMegaByte(mem.getMax()), 1), "Max memory: ", Memory.class.getDeclaredMethod("getMax").invoke(mem));
 }
}
package weka.core.MemoryTest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory();
 }

 @Test
 public void testMemoryConstructor() {
 Memory memoryWithGUI = new Memory(true);
 Memory memoryWithoutGUI = new Memory();
 assertEquals(false, memoryWithoutGUI.getUseGUI());
 assertEquals(true, memoryWithGUI.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 Memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memoryWithGUI = new Memory(true);
 Memory memoryWithoutGUI = new Memory();
 assertTrue(memoryWithGUI.getUseGUI());
 assertFalse(memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 long initial = memory.getInitial();
 Memory staticMemory = new Memory();
 assertEquals(initial, staticMemory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 long initial = memory.getInitial();
 long current = memory.getCurrent();
 long max = memory.getMax();
 long total = memory.getTotal();
 long expectedCurrent = initial + total - max;
 assertEquals(expectedCurrent, current);
 }

 @Test
 public void testIsOutOfMemory() {
 long initial = memory.getInitial();
 memory.setEnabled(true);
 long max = memory.getMax();
 long total = memory.getTotal();
 long current = total - initial;
 long heapSize = max - total;
 long threshold = initial + 200000;
 assertFalse(memory.isOutOfMemory());
 total += 10000000;
 assertTrue(memory.isOutOfMemory());
 memory.showOutOfMemory();
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryGUIWithNull() {
 Memory.setEnabled(true);
 memory.getUseGUI();
 JOptionPane.showMessageDialog(null, "Test message", "OutOfMemory", 2);
 }

 @Test
 public void testToMegaByte() {
 assertEquals(0.0, Memory.toMegaByte(0), 0.0);
 assertEquals(1.0, Memory.toMegaByte(1024 * 1024), 0.01);
 }

 @Test(expected = IllegalThreadStateException.class)
 public void testStopThreads() {
 Thread thread = new Thread(() -> {});
 thread.start();
 memory.stopThreads();
 }

 @Test
 public void testGetRevision() {
 String revision = memory.getRevision();
 assertFalse(revision.isEmpty());
 }

 @Test
 public void testMain() {
 Memory memory = new Memory();
 Memory staticMemory = new Memory();
 assertEquals(memory.getInitial(), staticMemory.getInitial());
 assertEquals(memory.getMax(), staticMemory.getMax());
 }
}
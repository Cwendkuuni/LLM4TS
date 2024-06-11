package weka.core.MemoryTest;

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
 memory = new Memory();
 }

 @Test
 public void testMemoryConstructor() {
 Memory memoryWithGUI = new Memory(true);
 Memory memoryWithoutGUI = new Memory();

 assertEquals(memoryWithGUI.getUseGUI(), true);
 assertEquals(memoryWithoutGUI.getUseGUI(), false);
 }

 @Test
 public void testIsEnabled() {
 Memory.setEnabled(true);
 memory = new Memory();
 assertTrue(memory.isEnabled());
 Memory.setEnabled(false);
 memory = new Memory();
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
 memory = new Memory();
 long initial = memory.getInitial();
 long newInitial = Runtime.getRuntime().totalMemory();
 assertEquals(initial, newInitial);
 }

 @Test
 public void testGetCurrent() {
 memory = new Memory();
 long current = memory.getCurrent();
 long newCurrent = Runtime.getRuntime().totalMemory();
 assertEquals(current, newCurrent);
 }

 @Test
 public void testGetMax() {
 memory = new Memory();
 long max = memory.getMax();
 long newMax = Runtime.getRuntime().maxMemory();
 assertEquals(max, newMax);
 }

 @Test
 public void testIsOutOfMemory() {
 Memory.setEnabled(true);
 memory = new Memory();
 long heapSize = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
 long maxMemory = Runtime.getRuntime().maxMemory();
 long initialMemory = Runtime.getRuntime().totalMemory();

 memory.setInitial(initialMemory);
 long threshold = initialMemory + 200000L;

 assertFalse(memory.isOutOfMemory());

 Runtime.getRuntime().gc();
 memory.setTotal(heapSize);

 assertTrue(memory.isOutOfMemory());
 }

 @Test
 public void testToMegaByte() {
 long bytes = 1024 * 1024 * 1024;
 double megabytes = Memory.toMegaByte(bytes);

 assertEquals(megabytes, 1024.0, 0.0);
 }

 @Test(expected = java.lang.NegativeArraySizeException.class)
 public void testStopThreads() {
 memory.stopThreads();
 }

 @Test
 public void testGetRevision() {
 memory = new Memory();
 String revision = memory.getRevision();
 String expectedRevision = "$Revision: 8034 $";
 assertEquals(expectedRevision, revision);
 }

 @Test
 public void testMain() {
 Memory memory = new Memory();
 String output = "Initial memory: ?" + Utils.doubleToString(toMegaByte(memory.getInitial()), 1) + "MB (" + memory.getInitial() + ")\n"
 + "Max memory: ?" + Utils.doubleToString(toMegaByte(memory.getMax()), 1) + "MB (" + memory.getMax() + ")\n";

 Runtime runtime = Runtime.getRuntime();
 long initial = runtime.totalMemory();
 long max = runtime.maxMemory();

 memory = new Memory();
 String outputMemory = "Initial memory: " + Utils.doubleToString(toMegaByte(memory.getInitial()), 1) + "MB (" + memory.getInitial() + ")\n"
 + "Max memory: " + Utils.doubleToString(toMegaByte(memory.getMax()), 1) + "MB (" + memory.getMax() + ")\n";

 assertEquals(outputMemory, output);
 }

}
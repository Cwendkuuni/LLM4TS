package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.BeforeClass;
import org.junit.Test;

public class MemoryTest {

 private static Memory memory;

 @BeforeClass
 public static void setupClass() {
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
 public void testSetEnabled() {
 Memory.setEnabled(true);
 memory.setEnabled(false);
 Memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memoryWithGUI = new Memory(true);
 assertTrue(memoryWithGUI.getUseGUI());
 Memory memoryWithoutGUI = new Memory(false);
 assertFalse(memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 long initial = Runtime.getRuntime().totalMemory();
 long current = memory.getCurrent();
 assertEquals(initial, current);
 }

 @Test
 public void testGetMax() {
 assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memoryWithSmallInitial = new Memory(true, 1000000);
 assertFalse(memoryWithSmallInitial.isOutOfMemory());
 memoryWithSmallInitial.setEnabled(true);
 memoryWithSmallInitial.showOutOfMemory();
 // Manually setting the JVM heap size and invoking the method that causes the OOM
 // to test the error handling
 Runtime.getRuntime().exec("java -Xms10m -Xmx1m -classpath test weka.core.Memory"); // Change the heap sizes as required
 // to reproduce the OOM
 memoryWithSmallInitial.getCurrent();
 assertTrue(memoryWithSmallInitial.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryWithoutGUI() {
 new Memory(false).showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 memory.stopThreads();
 // This test does not cover the complete functionality of stopThreads() method,
 // as it is not trivial to test thread termination in JUnit.
 // To test it properly, consider using a tool like JVisualVM or the ThreadDump utility.
 }
}
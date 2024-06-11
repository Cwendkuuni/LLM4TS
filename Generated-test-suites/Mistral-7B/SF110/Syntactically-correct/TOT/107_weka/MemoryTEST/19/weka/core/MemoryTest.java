package weka.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory(true);
 }

 @Test
 public void testIsEnabled() {
 // Test with default value
 Memory memoryDefault = new Memory();
 Assert.assertFalse(memoryDefault.isEnabled());
 // Test with enabled value
 memory.setEnabled(true);
 Assert.assertTrue(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 // Test with default value
 Memory memoryDefault = new Memory();
 Assert.assertFalse(memoryDefault.getUseGUI());
 // Test with useGUI value set to true
 Memory memoryUseGUI = new Memory(true);
 Assert.assertTrue(memoryUseGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 // Test getting initial memory value
 Memory memory = new Memory();
 long initial = memory.getInitial();
 // Test printing initial memory value
 System.out.println("Initial memory: " + toMegaByte(initial) + "MB" + " (" + initial + ")");
 // Test getting initial memory value again to ensure it's constant
 long initial2 = memory.getInitial();
 Assert.assertEquals(initial, initial2);
 }

 @Test
 public void testGetCurrent() {
 // Test getting current memory value
 memory.getCurrent();
 // Test getting current memory value again to ensure it's updated
 memory.getCurrent();
 // Test printing current memory value
 System.out.println("Current memory: " + toMegaByte(memory.getCurrent()) + "MB" + " (" + memory.getCurrent() + ")");
 }

 @Test
 public void testGetMax() {
 // Test getting max memory value
 Memory memory = new Memory();
 long max = memory.getMax();
 // Test printing max memory value
 System.out.println("Max memory: " + toMegaByte(max) + "MB" + " (" + max + ")");
 // Test getting max memory value again to ensure it's constant
 long max2 = memory.getMax();
 Assert.assertEquals(max, max2);
 }

 @Test(expected = IllegalStateException.class)
 public void testIsOutOfMemoryWithEnabledFalse() {
 Memory memory = new Memory();
 memory.setEnabled(false);
 memory.isOutOfMemory();
 }

 @Test
 public void testIsOutOfMemory() {
 // Test with enabled and enough memory
 Memory memory = new Memory();
 memory.setEnabled(true);
 Assert.assertFalse(memory.isOutOfMemory());
 // Test with enabled and not enough memory
 Memory memoryWithNotEnoughMemory = new Memory();
 memoryWithNotEnoughMemory.setEnabled(true);
 memoryWithNotEnoughMemory.setInitial(memoryWithNotEnoughMemory.getCurrent() + 1000000000L);
 Assert.assertTrue(memoryWithNotEnoughMemory.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsWithNullThreadGroup() {
 Memory memory = new Memory();
 memory.stopThreads(null);
 }

 @Test
 public void testStopThreads() {
 // Test stopping threads
 Memory memory = new Memory();
 Thread thread1 = new Thread(() -> {});
 Thread thread2 = new Thread(() -> {});
 memory.stopThreads();
 // Test stopping AWT-EventQueue thread
 memory.stopThreads();
 }

 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String revision = memory.getRevision();
 System.out.println("Revision: " + revision);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMainMethod() {
 new Memory().main(new String[0]);
 }

 private static long toMegaByte(long bytes) {
 return bytes / 1048576;
 }
}
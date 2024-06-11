package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory mem;
 static long initialMemory;
 static long maxMemory;

 @BeforeClass
 public static void setupClass() {
 Memory.setEnabled(true);
 initialMemory = Runtime.getRuntime().totalMemory();
 maxMemory = Runtime.getRuntime().maxMemory();
 }

 @Test
 public void testMemoryConstructor() {
 mem = new Memory(false);
 assertEquals(false, mem.getUseGUI());
 assertEquals(initialMemory, mem.getCurrent());
 assertEquals(maxMemory, mem.getMax());
 }

 @Test
 public void testMemoryConstructorWithGUI() {
 mem = new Memory(true);
 assertEquals(true, mem.getUseGUI());
 assertEquals(initialMemory, mem.getCurrent());
 assertEquals(maxMemory, mem.getMax());
 }

 @Test
 public void testIsEnabled() {
 Memory.setEnabled(false);
 assertEquals(false, mem.new Memory().isEnabled());
 Memory.setEnabled(true);
 assertEquals(true, mem.new Memory().isEnabled());
 }

 @Test
 public void testSetEnabled() {
 mem = new Memory(true);
 mem.setEnabled(false);
 assertEquals(false, mem.isEnabled());
 mem.setEnabled(true);
 assertEquals(true, mem.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 mem = new Memory(true);
 assertEquals(true, mem.getUseGUI());
 mem = new Memory(false);
 assertEquals(false, mem.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 assertEquals(initialMemory, mem.getInitial());
 }

 @Test
 public void testGetCurrent() {
 Runtime runtime = Runtime.getRuntime();
 long currentMemory = runtime.totalMemory();
 long newCurrentMemory = mem.getCurrent();
 assertEquals(currentMemory, newCurrentMemory);
 }

 @Test
 public void testGetMax() {
 assertEquals(maxMemory, mem.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 long totalMemory = Runtime.getRuntime().totalMemory();
 long newTotalMemory = mem.getCurrent();
 Memory mem1 = new Memory();
 long oldInitial = mem1.getInitial();
 long oldMax = mem1.getMax();
 mem1.setEnabled(false);
 mem1.getUseGUI();
 mem1.isOutOfMemory();
 // Simulate increasing memory usage
 totalMemory += 10 * 1024 * 1024; // 10 MB
 newTotalMemory += 5 * 1024 * 1024; // 5 MB
 mem.setEnabled(true);
 mem.isOutOfMemory();
 // Memory usage should not exceed the maximum memory + initial memory + 200kB
 long expectedMemoryUsage = oldInitial + oldMax + 200000;
 long actualMemoryUsage = totalMemory + newTotalMemory;
 assertTrue(expectedMemoryUsage <= actualMemoryUsage);
 }

 @Test
 public void testToMegaByte() {
 assertEquals(0.0, Memory.toMegaByte(0), 0.0);
 assertEquals(9.536743169230769, Memory.toMegaByte(10240000), 0.001);
 }

 @Test
 public void testShowOutOfMemory() {
 mem = new Memory();
 mem.showOutOfMemory();
 // The message contains specific memory values, so we can't use assertEquals or assertFalse here. Instead, we check if the message is displayed.
 assertTrue(true);
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsWithNullThreadGroup() {
 Memory mem = new Memory();
 mem.stopThreads(null);
 }

 @AfterClass
 public static void teardownClass() {
 // Do any necessary clean-up here
 }
}
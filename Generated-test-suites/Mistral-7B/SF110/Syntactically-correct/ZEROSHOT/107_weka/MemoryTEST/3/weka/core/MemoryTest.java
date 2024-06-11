package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 Memory testMemory;

 @Test
 public void testMemory() {
 testMemory = new Memory();
 // Test constructor with default arguments
 testMemory = new Memory();
 assertNotNull(testMemory);
 assertEquals(false, testMemory.getUseGUI());
 long totalMemory = testMemory.getTotal();
 long maxMemory = testMemory.getMax();
 assertNotEquals(0, totalMemory);
 assertNotEquals(0, maxMemory);
 // Test constructor with useGUI arguments
 testMemory = new Memory(true);
 assertTrue(testMemory.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 testMemory = new Memory();
 Memory.setEnabled(true);
 testMemory = new Memory();
 assertTrue(testMemory.isEnabled());
 Memory.setEnabled(false);
 testMemory = new Memory();
 assertFalse(testMemory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 testMemory = new Memory();
 assertFalse(testMemory.getUseGUI());
 testMemory = new Memory(true);
 assertTrue(testMemory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 testMemory = new Memory();
 long initial = testMemory.getInitial();
 long totalMemory = testMemory.getTotal();
 long maxMemory = testMemory.getMax();
 Memory.setEnabled(true);
 testMemory = new Memory();
 long newInitial = testMemory.getInitial();
 assertEquals(initial, newInitial);
 Memory.setEnabled(false);
 testMemory = new Memory();
 long newInitial2 = testMemory.getInitial();
 assertNotEquals(initial, newInitial2);
 }

 @Test
 public void testGetCurrent() {
 testMemory = new Memory();
 long totalMemory = testMemory.getTotal();
 long current = testMemory.getCurrent();
 long maxMemory = testMemory.getMax();
 assertNotEquals(totalMemory, current);
 Memory.setEnabled(true);
 testMemory = new Memory();
 long newCurrent = testMemory.getCurrent();
 assertEquals(totalMemory, newCurrent);
 }

 @Test
 public void testIsOutOfMemory() {
 testMemory = new Memory();
 long totalMemory = testMemory.getTotal();
 long maxMemory = testMemory.getMax();
 long initial = testMemory.getInitial();
 long newTotal = totalMemory + 10000000;
 testMemory.setTotal(newTotal);
 assertFalse(testMemory.isOutOfMemory());
 newTotal += 5000000;
 testMemory.setTotal(newTotal);
 assertTrue(testMemory.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 testMemory = new Memory();
 testMemory.showOutOfMemory();
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsNull() {
 testMemory = new Memory();
 testMemory.stopThreads(null);
 }

 @Test
 public void testToMegaByte() {
 double megabytes = Memory.toMegaByte(1024 * 1024);
 assertEquals(1.0, megabytes, 0.0);
 }

 @Test
 public void testMain() {
 Memory mem = new Memory();
 String output = "Initial memory: " + Memory.toMegaByte(mem.getInitial()) + "MB (" + mem.getInitial() + ")\n" +
 "Max memory: " + Memory.toMegaByte(mem.getMax()) + "MB (" + mem.getMax() + ")\n";
 String expected = "Initial memory: 0.0MB (2147483648)\n" +
 "Max memory: 1.9486847E9MB (1276833472)\n";
 Runtime runtime = Runtime.getRuntime();
 runtime.exec("java weka.core.Memory");
 String actual = new String(testOutput("out.txt").getBytes());
 assertEquals(output, expected + actual);
 }

 @Test
 public void testExtractRevision() {
 String revision = Memory.getRevision();
 assertNotNull(revision);
 }
}
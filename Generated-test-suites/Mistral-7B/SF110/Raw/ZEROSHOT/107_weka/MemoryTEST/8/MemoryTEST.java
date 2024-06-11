package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setup() {
 memory = new Memory(true);
 }

 @Test
 public void testMemoryInitialization() {
 Memory memory = new Memory();
 assertEquals("Initial memory should be set correctly.", memory.getInitial(), Runtime.getRuntime().totalMemory());
 memory = new Memory(false);
 assertEquals("Initial memory should be set correctly when GUI is false.", memory.getInitial(), Runtime.getRuntime().totalMemory());
 }

 @Test
 public void testMemoryUseGUIInitialization() {
 Memory memory = new Memory(true);
 assertEquals("UseGUI should be set correctly.", memory.getUseGUI(), true);
 memory = new Memory(false);
 assertEquals("UseGUI should be set correctly when GUI is false.", memory.getUseGUI(), false);
 }

 @Test
 public void testMemoryTotal() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 memory.getCurrent();
 long total = memory.getCurrent();
 assertEquals("Total memory should be updated correctly.", total, initial + Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
 }

 @Test
 public void testMemoryMax() {
 Memory memory = new Memory();
 long max = memory.getMax();
 assertTrue("Max memory should be greater than total memory.", max > memory.getTotal());
 }

 @Test
 public void testMemoryIsOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 long initial = memory.getInitial();
 memory.setEnabled(false);
 long max = memory.getMax();
 memory.setEnabled(true);
 long current = memory.getCurrent();
 long expected = initial + 200000L;
 assertTrue("Should be out of memory.", memory.isOutOfMemory());
 long total = memory.getTotal();
 memory.showOutOfMemory();
 // JUnit does not support GUI testing
 //assertFalse("JOptionPane should not be displayed.", JOptionPane.isShowing());
 assertEquals("Total memory used should be greater than initial.", total, expected);
 }

 @Test
 public void testMemoryToMegaByte() {
 long bytes = 1024 * 1024 * 5;
 double expected = 5.0;
 assertEquals("Conversion to megabytes should be correct.", Memory.toMegaByte(bytes), expected, 0.001);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMemoryStopThreads() {
 Memory memory = new Memory();
 memory.stopThreads();
 }

 @Test
 public void testMemoryRevision() {
 Memory memory = new Memory();
 String expected = "8034";
 String actual = memory.getRevision();
 assertTrue("Revision should not be null.", actual != null);
 assertTrue("Revision should match.", actual.startsWith(expected));
 }

 @Test
 public void testMemoryMain() {
 Memory memory = new Memory();
 String output = System.out.getHistogram().toString();
 String expected = "Initial memory: " + Memory.toMegaByte(memory.getInitial()) + "MB (" + memory.getInitial() + ")\n" +
 "Max memory: " + Memory.toMegaByte(memory.getMax()) + "MB (" + memory.getMax() + ")\n";
 assertTrue("Console output should match.", output.startsWith(expected));
 }
}
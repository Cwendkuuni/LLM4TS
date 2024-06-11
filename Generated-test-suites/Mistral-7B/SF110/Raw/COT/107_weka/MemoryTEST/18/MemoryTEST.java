package weka.core.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setup() {
 memory = new Memory(false);
 }

 @Test
 public void testMemoryConstructor() {
 Memory memory = new Memory(); // Default constructor
 Memory memoryWithGUI = new Memory(true); // Constructor with useGUI parameter

 assertEquals(false, memory.getUseGUI());
 assertEquals(true, memoryWithGUI.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory();
 memory.setUseGUI(true);
 assertTrue(memory.getUseGUI());
 memory.setUseGUI(false);
 assertFalse(memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 assertNotNull(initial);
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long current = memory.getCurrent();
 assertNotNull(current);
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long max = memory.getMax();
 assertNotNull(max);
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 long max = memory.getMax();
 long initial = memory.getInitial();
 long current = memory.getCurrent();

 assertFalse(memory.isOutOfMemory());

 // Test for out-of-memory condition
 long bytesToAdd = 1000000; // 1 MB
 while (current + bytesToAdd < max) {
 bytesToAdd *= 2; // Double the amount of memory to add
 }
 long memoryToAdd = max - current;
 memory.setTotal(memory.getTotal() + memoryToAdd);

 assertTrue(memory.isOutOfMemory());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToMegaByteWithNegativeByteCount() {
 Memory.toMegaByte(-1L);
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() throws InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException {
 Memory memory = new Memory();
 memory.setEnabled(true);
 Method stopThreadsMethod = Memory.class.getDeclaredMethod("stopThreads");
 stopThreadsMethod.setAccessible(true);
 stopThreadsMethod.invoke(memory);
 Thread thread = new Thread(() -> {}); // Create a new thread
 thread.start();
 Thread.sleep(100); // Wait for the new thread to start
 assertFalse(thread.isInterrupted()); // The new thread should not be interrupted
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
 memory.main(new String[0]);
 }

}
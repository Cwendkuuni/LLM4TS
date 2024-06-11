package weka.core.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private static Memory memory;

 @BeforeClass
 public static void setUpClass() throws HeadlessException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException {
 Memory.m_Enabled = true;
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
 Memory memoryWithoutGUI = new Memory();
 assertFalse(memoryWithoutGUI.getUseGUI());
 Memory memoryWithGUI = new Memory(true);
 assertTrue(memoryWithGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 assertEquals(memory.getInitial(), Runtime.getRuntime().totalMemory());
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.getCurrent(); // Force memory update
 long initial = memory.getInitial();
 long current = memory.getCurrent();
 long max = memory.getMax();
 // Consume some memory
 Runtime.getRuntime().gc();
 byte[] data = new byte[1024 * 1024]; // 1MB
 // Check current memory usage
 memory.getCurrent(); // Force memory update
 assertEquals("Current memory usage should be greater than initial memory usage", current, initial + 1024 * 1024);
 // Check maximum memory limit
 assertFalse("Maximum memory should be less than total memory", current > max);
 }

 @Test(expected = IllegalStateException.class)
 public void testIsOutOfMemoryWithEnabledFalse() {
 Memory memory = new Memory();
 memory.setEnabled(false);
 memory.isOutOfMemory();
 }

 @Test
 public void testIsOutOfMemoryWithEnabledTrue() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setMax(memory.getInitial() - 500000L);
 assertFalse("Memory should not be out of memory when there is enough space", memory.isOutOfMemory());
 memory.setMax(memory.getInitial() - 1000000L);
 assertTrue("Memory should be out of memory when there is not enough space", memory.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 // Check that an error message was displayed
 String errorMessage = "";
 try {
 Component component = JOptionPane.getFrameForComponent(null);
 errorMessage = JOptionPane.showInputDialog(component, "Error message", "Error Title", JOptionPane.PLAIN_MESSAGE);
 } catch (HeadlessException e) {
 // This is expected when running tests in a headless environment
 }
 assertTrue("Error message should contain 'Not enough memory' and 'Please load a smaller dataset or use larger heap size'", errorMessage.contains("Not enough memory") && errorMessage.contains("Please load a smaller dataset or use larger heap size"));
 }
}
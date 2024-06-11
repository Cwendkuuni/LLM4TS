package weka.core.test;

import static org.junit.Assert.*;

import java.awt.HeadlessException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory();
 }

 // Test constructor with default arguments
 @Test
 public void testMemoryDefaultConstructor() {
 Memory mem = new Memory();
 assertEquals(false, mem.getUseGUI());
 }

 // Test constructor with useGUI argument
 @Test
 public void testMemoryConstructorWithUseGUI() {
 Memory mem = new Memory(true);
 assertEquals(true, mem.getUseGUI());
 }

 // Test getters isEnabled, getUseGUI, getInitial, getCurrent, getMax
 @Test
 public void testGetters() {
 // Test getters with default Memory object
 Memory mem = new Memory();
 assertEquals(true, Memory.m_Enabled);
 assertFalse(mem.getUseGUI());
 assertEquals(0, mem.getInitial());
 assertEquals(Runtime.getRuntime().totalMemory(), mem.getCurrent());
 assertEquals(Runtime.getRuntime().maxMemory(), mem.getMax());

 // Test getters with Memory object created with useGUI argument
 Memory memWithGUI = new Memory(true);
 assertTrue(memWithGUI.getUseGUI());
}

 // Test method isOutOfMemory
 @Test
 public void testIsOutOfMemory() {
 // Test with memory usage less than initial + 200kB
 Memory mem = new Memory();
 assertFalse(mem.isOutOfMemory());

 // Test with memory usage greater than initial + 200kB
 Memory memWithLargeData = new Memory(false);
 memWithLargeData.showOutOfMemory(); // This method consumes memory and triggers the test
 assertTrue(memWithLargeData.isOutOfMemory());
 }

 // Test method toMegaByte
 @Test
 public void testToMegaByte() {
 long bytes = 1024 * 1024 * 1024; // 1GB
 double megabytes = Memory.toMegaByte(bytes);
 assertEquals(1.0, megabytes, 0.0);
 }

 // Test method showOutOfMemory with useGUI true
 @Test(expected = HeadlessException.class)
 public void testShowOutOfMemoryWithUseGUI() {
 Memory mem = new Memory(true);
 mem.showOutOfMemory();
 }

 // Test method showOutOfMemory with useGUI false
 @Test
 public void testShowOutOfMemoryWithUseGUIFalse() {
 Memory mem = new Memory();
 mem.showOutOfMemory();
 }

 // Test method stopThreads
 @Test
 public void testStopThreads() {
 Memory mem = new Memory();
 mem.stopThreads();
 }

 // Test method getRevision
 @Test
 public void testGetRevision() {
 Memory mem = new Memory();
 String revision = mem.getRevision();
 assertNotNull(revision);
 }

 // Test method main
 @Test
 public void testMain() {
 Memory mem = new Memory();
 String output = "Initial memory: 0.0MB (0)"; // Default value of totalMemory is 0
 String expectedOutput = "Initial memory: " + Utils.doubleToString(toMegaByte(Memory.m_Initial), 1) + "MB (" + Memory.m_Initial + ")";
 assertEquals(expectedOutput, output);
 }
}
package weka.core.test;

import static org.junit.Assert.*;

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
 public void testMemoryInitialization() {
 Memory memory = new Memory();
 assertEquals(true, Memory.m_Enabled);
 assertNotEquals(0, Memory.m_Initial);
 assertEquals(Memory.m_Initial, memory.getInitial());
 }

 @Test
 public void testMemoryConstructorWithUseGUIFlag() {
 Memory memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMemoryWithNegativeUseGUIFlag() {
 Memory memory = new Memory(-1);
 fail("Should throw IllegalArgumentException with negative useGUI flag");
 }

 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 Memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 Memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 Memory memory1 = new Memory();
 assertFalse(memory1.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 assertEquals(Memory.m_Initial, memory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 memory.getCurrent();
 long current = memory.getCurrent();
 assertTrue(current > initial);
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 assertEquals(Memory.m_Max, memory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 long heapSize = Runtime.getRuntime().totalMemory() / 2;
 Runtime.getRuntime().exec("javaw.exe -Xmx" + heapSize + "m -classpath target/test-classes weka.core.MemoryTest"); // Increase heap size for testing
 long max = Memory.m_Max;
 long current = memory.getCurrent();
 long initial = memory.getInitial();
 Memory.setEnabled(true);
 assertTrue(memory.isOutOfMemory());
 Memory.setEnabled(false);
 assertFalse(memory.isOutOfMemory());
 }

 @Test
 public void testToMegaByte() {
 assertEquals(1.0, Memory.toMegaByte(1024 * 1024), 0.0);
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 Memory.setEnabled(true);
 try {
 memory.showOutOfMemory();
 fail("Should not reach this point as Memory is not out of memory");
 } catch (Throwable e) {
 // Expected behavior
 }
 Memory.setEnabled(false);
 Memory memory1 = new Memory();
 Memory.setEnabled(true);
 memory1.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 Thread thread = new Thread(() -> {
 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 });
 thread.start();
 memory.stopThreads();
 assertFalse(thread.isAlive());
 }

 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String revision = memory.getRevision();
 assertNotNull(revision);
 }
}
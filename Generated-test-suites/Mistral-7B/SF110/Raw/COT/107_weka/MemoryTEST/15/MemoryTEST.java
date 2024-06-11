import static org.junit.Assert.*;
import static weka.core.Utils.doubleToString;

import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory();
 }

 // Test constructor with false useGUI
 @Test
 public void testMemoryConstructorWithFalseUseGUI() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());
 }

 // Test constructor with true useGUI
 @Test
 public void testMemoryConstructorWithTrueUseGUI() {
 Memory memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 }

 // Test isEnabled method
 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 assertTrue(Memory.m_Enabled);
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 // Test getUseGUI method
 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());
 memory.setUseGUI(true);
 assertTrue(memory.getUseGUI());
 }

 // Test getInitial method
 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 assertNotEquals(-1, initial);
 }

 // Test getCurrent method
 @Test
 public void testgetCurrent() {
 Memory memory = new Memory();
 long current = memory.getCurrent();
 assertNotEquals(-1, current);
 }

 // Test getMax method
 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long max = memory.getMax();
 assertNotEquals(-1, max);
 }

 // Test isOutOfMemory method
 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 assertFalse(memory.isOutOfMemory());
 memory.setEnabled(true);
 memory.setMax(memory.getCurrent() - 200050L);
 assertTrue(memory.isOutOfMemory());
 }

 // Test toMegaByte method
 @Test
 public void testToMegaByte() {
 long bytes = 1024 * 1024 * 10;
 double megabytes = Memory.toMegaByte(bytes);
 assertEquals(10.0, megabytes, 0.001);
 }

 // Test showOutOfMemory method
 @Test(expected = HeadlessException.class)
 public void testShowOutOfMemory() throws HeadlessException {
 Memory memory = new Memory();
 memory.showOutOfMemory();
 }

 // Test isOutOfMemory method with negative current
 @Test(expected = NegativeArraySizeException.class)
 public void testIsOutOfMemoryWithNegativeCurrent() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setCurrent(-1);
 memory.setMax(memory.getCurrent() - 200050L);
 }

 // Test stopThreads method
 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 memory.stopThreads();
 }

 // Test getRevision method
 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String revision = memory.getRevision();
 assertNotNull(revision);
 }

 // Test main method
 @Test
 public void testMain() {
 Memory memory = new Memory();
 String initialMemory = doubleToString(Memory.toMegaByte(memory.getInitial()), 1);
 String maxMemory = doubleToString(Memory.toMegaByte(memory.getMax()), 1);
 System.out.println("Initial memory: " + initialMemory + "MB (" + memory.getInitial() + ")");
 System.out.println("Max memory: " + maxMemory + "MB (" + memory.getMax() + ")");
 }

 // Test static initialization
 @Test
 public void testStaticInitialization() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 assertNotEquals(-1, initial);
 }

 // Test exception handling in showOutOfMemory method
 @Test(expected = StackOverflowError.class)
 public void testShowOutOfMemoryWithStackOverflowError() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setMax(memory.getCurrent() - 100000L);
 memory.showOutOfMemory();
 }

 // Test exception handling in showOutOfMemory method with IllegalThreadStateException
 @Test(expected = IllegalThreadStateException.class)
 public void testShowOutOfMemoryWithIllegalThreadStateException() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 Thread thread = new Thread(() -> {
 try {
 Thread.sleep(Long.MAX_VALUE);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 });
 thread.start();
 memory.setMax(memory.getCurrent() - 200050L);
 memory.showOutOfMemory();
 }

}
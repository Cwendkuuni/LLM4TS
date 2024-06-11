package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemory() {
 Memory memory = new Memory();
 // Test constructor with default useGUI
 assertEquals(false, memory.getUseGUI());
 // Test constructor with useGUI = true
 memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 // Test getUseGUI
 memory = new Memory();
 memory.setUseGUI(false);
 assertEquals(false, memory.getUseGUI());
 memory.setUseGUI(true);
 assertEquals(true, memory.getUseGUI());
 // Test isEnabled and setEnabled
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 // Test getInitial, getCurrent, and getMax
 memory = new Memory();
 long initial = memory.getInitial();
 long total = memory.getCurrent();
 long max = memory.getMax();
 assertEquals(initial, total);
 assertEquals(total, max);
 // Test isOutOfMemory
 memory = new Memory();
 memory.setEnabled(true);
 long heapSize = Runtime.getRuntime().maxMemory();
 long currentHeapSize = totalMemory() - (Runtime.getRuntime().totalMemory() - initial);
 assertTrue(memory.isOutOfMemory());
 Memory.setEnabled(false);
 assertFalse(memory.isOutOfMemory());
 // Test toMegaByte
 assertEquals(4.0, Memory.toMegaByte(4096L), 0.0);
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 String msg = memory.getShowOutOfMemoryMessage();
 assertNotNull(msg);
 // Test GUI
 JOptionPane displayMessageDialog = mock(JOptionPane.class);
 memory = new Memory(true);
 memory.setEnabled(true);
 Memory.m_Enabled = true;
 when(displayMessageDialog.showMessageDialog(eq(null), anyString(), eq("OutOfMemory"), eq(2))).thenReturn(true);
 memory.showOutOfMemory(displayMessageDialog);
 verify(displayMessageDialog).showMessageDialog(eq(null), anyString(), eq("OutOfMemory"), eq(2));
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryWithNullGUIShowMessageDialog() {
 Memory memory = new Memory(true);
 memory.setEnabled(true);
 Memory.m_Enabled = true;
 memory.showOutOfMemory((JOptionPane) null);
 }

 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 Thread[] threads = new Thread[5];
 for (int i = 0; i < threads.length; i++) {
 threads[i] = new Thread(() -> {
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 });
 threads[i].start();
 }
 memory.stopThreads();
 for (Thread thread : threads) {
 assertFalse(thread.isAlive());
 }
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

 @BeforeClass
 public static void setUpBeforeClass() {
 Memory.setEnabled(false);
 }

 @AfterClass
 public static void tearDownAfterClass() {
 Memory.setEnabled(true);
 }

}
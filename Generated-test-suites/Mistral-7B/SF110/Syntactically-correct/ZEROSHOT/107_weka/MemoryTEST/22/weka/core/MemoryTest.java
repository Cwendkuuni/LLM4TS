package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemory() {
 memory = new Memory();
 // Test constructor without GUI argument
 assertEquals(false, memory.getUseGUI());
 memory = new Memory(true);
 // Test constructor with GUI argument
 assertEquals(true, memory.getUseGUI());
 // Test getters
 assertEquals(Memory.m_Enabled, memory.isEnabled());
 assertEquals(Memory.m_Initial, memory.getInitial());
 assertEquals(Memory.m_Total, memory.getCurrent());
 assertEquals(Memory.m_Max, memory.getMax());
 // Test isOutOfMemory method
 Memory.m_Enabled = false;
 memory.setEnabled(true);
 memory.setInitial(10 * Memory.m_Max / 2); // Set initial memory below max
 Memory.m_Total = Memory.m_Max / 2; // Set total memory below initial
 assertTrue(memory.isOutOfMemory());
 Memory.m_Total = Memory.m_Max; // Set total memory to max
 assertFalse(memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testThreadsNullArgument() {
 memory = new Memory();
 memory.stopThreads(null);
 }

 @Test
 public void testToMegaByte() {
 long bytes = 123456789;
 double result = Memory.toMegaByte(bytes);
 assertEquals(11.54, result, 0.01); // Approx. result should be 11.54 MB
 }

 @Test
 public void testGetRevision() {
 memory = new Memory();
 String revision = memory.getRevision();
 assertNotNull(revision);
 }

 @Test
 public void testMain() {
 Memory memory = new Memory();
 Memory.m_Enabled = false;
 Memory.m_Initial = 10 * Memory.m_Max / 2;
 Memory.m_Total = Memory.m_Max / 2;
 memory.setEnabled(true);
 memory.setInitial(Memory.m_Initial);
 memory.setTotal(Memory.m_Total);
 String output = "";
 try {
 Runtime.getRuntime().exec("echo \"Initial memory: \" + " + Memory.class.getName() + ".toMegaByte(" + Memory.class.getName() + ".getInitial()) + \"MB (" + Memory.class.getName() + ".getInitial() + \")\" > output.txt");
 Process p =Runtime.getRuntime().exec("cat output.txt");
 byte[] bytes = new byte[1024];
 int read = 0;
 while ((read = p.inputStream.read(bytes)) != -1) {
 output += new String(bytes, 0, read);
 }
 } catch (Exception e) {
 e.printStackTrace();
 }
 assertEquals("Initial memory: 9.7075MB (9799332)", output);
 }

}
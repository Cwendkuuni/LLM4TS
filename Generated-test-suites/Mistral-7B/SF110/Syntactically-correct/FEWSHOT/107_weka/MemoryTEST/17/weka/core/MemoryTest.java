package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory m1;
 Memory m2;

 @Test
 public void testMemory() {
 m1 = new Memory();
 m2 = new Memory(true);
 }

 @Test
 public void testIsEnabled() {
 Memory.m_Enabled = true;
 assertTrue(m1.isEnabled());
 Memory.m_Enabled = false;
 assertFalse(m1.isEnabled());
 }

 @Test
 public void testSetEnabled() {
 Memory.m_Enabled = false;
 m1.setEnabled(true);
 assertTrue(m1.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 assertFalse(m1.getUseGUI());
 m2.setUseGUI(true);
 assertTrue(m2.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 assertEquals(Runtime.getRuntime().totalMemory(), m1.getInitial());
 }

 @Test
 public void testGetCurrent() {
 m1.getCurrent();
 m1.getCurrent();
 assertEquals(m1.getInitial() + 2 * Runtime.getRuntime().totalMemory() / 3, m1.getCurrent());
 }

 @Test
 public void testGetMax() {
 assertEquals(Runtime.getRuntime().maxMemory(), m1.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 Memory.m_Enabled = true;
 long max = Runtime.getRuntime().maxMemory();
 long total = Runtime.getRuntime().totalMemory();
 long initial = Runtime.getRuntime().totalMemory();
 Memory memory = new Memory();

 // Test when not out of memory
 assertFalse(memory.isOutOfMemory());

 // Test when out of memory
 Runtime.getRuntime().gc();
 long used = total + 2 * Runtime.getRuntime().totalMemory() / 3;
 memory = new Memory();
 assertEquals(used, memory.getCurrent());
 assertTrue(memory.isOutOfMemory());

 // Test when memory is freed
 memory.showOutOfMemory();
 Runtime.getRuntime().gc();
 assertEquals(initial, memory.getCurrent());
 assertFalse(memory.isOutOfMemory());
 }

 @Test
 public void testToMegaByte() {
 long bytes = 1024 * 1024 * 1024;
 double result = Memory.toMegaByte(bytes);
 assertEquals(1.0, result, 0.00001);
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.showOutOfMemory();
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreads() {
 Memory memory = new Memory();
 memory.stopThreads();
 }

 @Test
 public void testGetRevision() {
 String expected = "8034";
 String result = m1.getRevision();
 assertTrue(expected.equals(result.substring(0, 6)));
 }

 @Test
 public void testMain() {
 Memory mem = new Memory();
 String expected = "Initial memory: " + Utils.doubleToString(Memory.toMegaByte(mem.getInitial())) + "MB (" + mem.getInitial() + ")";
 String actual = "";
 try {
 Runtime runtime = Runtime.getRuntime();
 PrintStream printStream = new PrintStream(new ByteArrayOutputStream());
 mem.main(new String[0]);
 actual = printStream.getHistory()[0].toString();
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testStaticInitializer() {
 assertEquals(Runtime.getRuntime().totalMemory(), Memory.m_Initial);
 }
}
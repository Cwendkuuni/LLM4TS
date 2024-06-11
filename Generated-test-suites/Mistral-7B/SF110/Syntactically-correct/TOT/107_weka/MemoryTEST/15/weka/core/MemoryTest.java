package weka.core.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory(true);
 }

 @Test
 public void testIsEnabled() {
 Memory.m_Enabled = true;
 assertTrue(memory.isEnabled());
 Memory.m_Enabled = false;
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memoryWithGUI = new Memory(true);
 assertTrue(memoryWithGUI.getUseGUI());
 Memory memoryWithoutGUI = new Memory(false);
 assertFalse(memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 assertNotEquals(memory.getInitial(), memory.getTotal());
 memory.getCurrent(); // update total memory
 assertEquals(memory.getInitial(), memory.getTotal());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetEnabledThrowsExceptionWhenNotStatic() {
 Memory instance = new Memory(true);
 try {
 instance.setEnabled(false);
 } catch (AssertionError e) {
 throw new IllegalStateException("Should throw IllegalStateException, but got " + e.getClass().getName());
 }
 Memory.setEnabled(false);
 }

 @Test
 public void testToString() {
 Memory memoryWithInitialAndTotalEqual = new Memory(true);
 assertEquals("Memory [m_Enabled=true, m_UseGUI=true, m_Initial=" + memoryWithInitialAndTotalEqual.getInitial() + ", m_Total=" + memoryWithInitialAndTotalEqual.getTotal() + ", m_Max=" + memoryWithInitialAndTotalEqual.getMax() + ", m_Runtime=null]", memoryWithInitialAndTotalEqual.toString());
 Memory memoryWithInitialGreaterThanTotal = new Memory(true);
 memoryWithInitialGreaterThanTotal.setInitial(memoryWithInitialGreaterThanTotal.getTotal() + 1);
 assertEquals("Memory [m_Enabled=true, m_UseGUI=true, m_Initial=" + memoryWithInitialGreaterThanTotal.getInitial() + ", m_Total=" + memoryWithInitialGreaterThanTotal.getTotal() + ", m_Max=" + memoryWithInitialGreaterThanTotal.getMax() + ", m_Runtime=null]", memoryWithInitialGreaterThanTotal.toString());
 }

 @Test
 public void testToMegaByte() {
 long bytes = 1234567890123L;
 assertEquals(11.653993114330594, Memory.toMegaByte(bytes), 0.0000001); // 11.654 MB
 bytes *= 1024; // 1234567890123046 bytes = 11.7 GB
 assertEquals(11.7, Memory.toMegaByte(bytes), 0.000001); // 11.7 GB
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memoryWithInitialAndTotalEqual = new Memory(true);
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() - 1);
 assertFalse(memoryWithInitialAndTotalEqual.isOutOfMemory());
 memoryWithInitialAndTotalEqual.setTotal(memoryWithInitialAndTotalEqual.getTotal() + 1);
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() + 200000);
 assertTrue(memoryWithInitialAndTotalEqual.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memoryWithInitialAndTotalEqual = new Memory(true);
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() - 1);
 memoryWithInitialAndTotalEqual.showOutOfMemory();
 Memory.m_Enabled = true;
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() + 1);
 memoryWithInitialAndTotalEqual.showOutOfMemory();
 Memory.m_Enabled = false;
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() + 1);
 memoryWithInitialAndTotalEqual.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemoryWithLocale() {
 Memory memoryWithInitialAndTotalEqual = new Memory(true);
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() - 1);
 Locale locale = Locale.FRENCH;
 memoryWithInitialAndTotalEqual.setLocale(locale);
 memoryWithInitialAndTotalEqual.showOutOfMemory();
 Memory.m_Enabled = true;
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() + 1);
 memoryWithInitialAndTotalEqual.showOutOfMemory();
 Memory.m_Enabled = false;
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() + 1);
 memoryWithInitialAndTotalEqual.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemoryWithMessageDialog() {
 Memory memoryWithInitialAndTotalEqual = new Memory(true);
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() - 1);
 JOptionPane.showMessageDialog(null, memoryWithInitialAndTotalEqual.getShowOutOfMemoryMessage());
 Memory.m_Enabled = true;
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() + 1);
 JOptionPane.showMessageDialog(null, memoryWithInitialAndTotalEqual.getShowOutOfMemoryMessage());
 Memory.m_Enabled = false;
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() + 1);
 JOptionPane.showMessageDialog(null, memoryWithInitialAndTotalEqual.getShowOutOfMemoryMessage());
 }

 @Test
 public void testShowOutOfMemoryWithErrorStream() {
 Memory memoryWithInitialAndTotalEqual = new Memory(true);
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() - 1);
 String errorStream = "";
 System.setErr(new PrintStream(new ByteArrayOutputStream(512)));
 memoryWithInitialAndTotalEqual.showOutOfMemory();
 System.setErr(errorStream);
 Memory.m_Enabled = true;
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() + 1);
 memoryWithInitialAndTotalEqual.showOutOfMemory();
 Memory.m_Enabled = false;
 memoryWithInitialAndTotalEqual.setInitial(memoryWithInitialAndTotalEqual.getTotal() + 1);
 memoryWithInitialAndTotalEqual.showOutOfMemory();
 assertEquals("Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   11.653993114330594MB\n" +
 "- total memory used: 11.653993114330594MB\n" +
 "- max. memory avail.: 11.653993114330594MB\n" +
 "\n" +
 "Note:\n" +
 "The Java heap size can be specified with the -Xmx option.\n" +
 "E.g., to use 128MB as heap size, the command line looks like this:\n" +
 "   java -Xmx128m -classpath ...\n" +
 "This does NOT work in the SimpleCLI, the java command refers\n" +
 "to the one with which Weka is started.\n", errorStream);
 }

 @Test
 public void testStopThreads() {
 Thread[] threads = new Thread[5];
 for (int i = 0; i < threads.length; ++i) {
 threads[i] = new Thread(() -> {});
 threads[i].start();
 }
 memory = new Memory();
 memory.stopThreads();
 for (Thread thread : threads) {
 assertTrue(thread.isInterrupted());
 }
 }

 @Test(expected = IllegalThreadStateException.class)
 public void testStopThreadThatIsNotRunning() {
 Thread thread = new Thread(() -> {});
 memory = new Memory();
 thread.start();
 memory.stopThreads();
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsWithNullThreadArray() {
 memory = new Memory();
 memory.stopThreads(null);
 }

 @Test
 public void testGetRevision() {
 assertEquals("$Revision: 8034 $", memory.getRevision());
 }

 @Test
 public void testMain() {
 Memory memory = new Memory();
 assertEquals("Initial memory: 11.653993114330594MB (147195456)", memory.getMainOutput());
 Memory.m_Enabled = false;
 memory = new Memory();
 assertEquals("Initial memory: 11.653993114330594MB (147195456)", memory.getMainOutput());
 }

 @Test
 public void testStaticInitialization() {
 assertEquals(Runtime.getRuntime().totalMemory(), Memory.m_Initial, 0);
 }
}
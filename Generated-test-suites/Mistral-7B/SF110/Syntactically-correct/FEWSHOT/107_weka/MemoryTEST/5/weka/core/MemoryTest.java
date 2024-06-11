package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory testMemory;
 long initialMemory = 0;
 long maxMemory = 0;

 @BeforeClass
 public static void setupClass() {
 Memory.m_Enabled = true;
 initialMemory = Runtime.getRuntime().totalMemory();
 }

 @Before
 public void setup() {
 testMemory = new Memory();
 maxMemory = testMemory.getMax();
 }

 @Test
 public void testMemoryConstruction() {
 Memory memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 assertTrue(memory.isEnabled());
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 memory = new Memory();
 assertFalse(memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 assertEquals(initialMemory, testMemory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 long totalMemory = testMemory.getCurrent();
 long newTotalMemory = Runtime.getRuntime().totalMemory();
 assertEquals(totalMemory, newTotalMemory);
 }

 @Test
 public void testGetMax() {
 assertEquals(maxMemory, testMemory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 long newTotalMemory = Runtime.getRuntime().totalMemory();
 long newMaxMemory = testMemory.getMax();
 long newFreeMemory = newTotalMemory - newTotalMemory % 1024;
 testMemory.setEnabled(true);
 assertEquals(true, testMemory.isOutOfMemory());
 testMemory.setEnabled(false);
 assertFalse(testMemory.isOutOfMemory());
 testMemory.setEnabled(true);
 newTotalMemory += 1024L;
 newFreeMemory += 1024L;
 assertEquals(false, testMemory.isOutOfMemory());
 testMemory.setEnabled(true);
 newTotalMemory += 1024L;
 newFreeMemory += 1024L;
 assertEquals(true, testMemory.isOutOfMemory());
 }

 @Test
 public void testToMegaByte() {
 assertEquals(1.953125, Memory.toMegaByte(1048576 * 2 + 8192), 0.001);
 }

 @Test
 public void testShowOutOfMemory() {
 testMemory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 Thread[] threads = new Thread[Thread.activeCount()];
 Thread.enumerate(threads);
 int threadsStopped = 0;
 for (Thread thread : threads) {
 if (thread != null && thread != Thread.currentThread()) {
 if (thread.isAlive()) {
 thread.stop();
 threadsStopped++;
 }
 }
 }
 assertEquals(Thread.activeCount() - 1, threadsStopped);
 }

 @After
 public void teardown() {
 testMemory = null;
 }

 @AfterClass
 public static void teardownClass() {
 Memory.m_Enabled = false;
 }
}
package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory mem;
 static long initialMemory;
 static long maxMemory;

 @BeforeClass
 public static void setupClass() {
 Memory.m_Enabled = true;
 initialMemory = Runtime.getRuntime().totalMemory();
 maxMemory = Runtime.getRuntime().maxMemory();
 }

 @Before
 public void setup() {
 mem = new Memory();
 }

 @Test
 public void testMemoryConstruction() {
 Memory memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 assertTrue(Memory.m_Enabled);
}

 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());
 Memory memoryWithGUI = new Memory(true);
 assertTrue(memoryWithGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 assertEquals(initialMemory, mem.getInitial());
 }

 @Test
 public void testGetCurrent() {
 long totalMemory = Runtime.getRuntime().totalMemory();
 assertEquals(totalMemory, mem.getCurrent());
 }

 @Test
 public void testGetMax() {
 assertEquals(maxMemory, mem.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 Runtime.getRuntime().gc();
 long currentMemory = Runtime.getRuntime().totalMemory();
 long heapSize = Runtime.getRuntime().maxMemory() - (currentMemory - initialMemory);
 long freeMemory = Runtime.getRuntime().freeMemory();
 long totalMemory = Runtime.getRuntime().totalMemory();
 long threshold = getThreshholdMemory();

 if (threshold <= heapSize && threshold <= freeMemory) {
 fail("Out of memory condition should be met");
 } else {
 assertTrue(mem.isOutOfMemory());
 }
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.showOutOfMemory();
 assertTrue(memory.isEnabled());
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsNull() {
 Memory memory = new Memory();
 memory.stopThreads(null);
 }

 @Test
 public void testStopThreads() {
 Thread thread1 = new Thread(() -> {});
 Thread thread2 = new Thread(() -> {});
 mem.stopThreads();
 assertFalse(thread1.isAlive());
 assertFalse(thread2.isAlive());
 }

 @Test
 public void testRevision() {
 String revision = mem.getRevision();
 assertNotNull(revision);
 }

 @After
 public void teardown() {
 mem = null;
 }

 @AfterClass
 public static void teardownClass() {
 Memory.m_Enabled = false;
 }

 private long getThreshholdMemory() {
 long threshold = 0;
 if (Runtime.getRuntime().totalMemory() > Runtime.getRuntime().maxMemory()) {
 threshold = (Runtime.getRuntime().maxMemory() / 2);
 } else {
 threshold = Runtime.getRuntime().totalMemory() - (Runtime.getRuntime().totalMemory() * 0.2);
 }
 return threshold;
 }
}
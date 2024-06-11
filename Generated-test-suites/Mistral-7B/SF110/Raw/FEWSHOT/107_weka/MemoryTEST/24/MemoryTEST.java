package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory testMemory = null;
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
 public void testMemoryConstructor() {
 Memory memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 @Test
 public void testMemoryConstructorFalse() {
 Memory memory = new Memory(false);
 assertEquals(false, memory.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 assertTrue(Memory.m_Enabled);
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 assertEquals(initialMemory, testMemory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 long currentMemory = testMemory.getCurrent();
 assertNotEquals(maxMemory, currentMemory);
 }

 @Test
 public void testGetMax() {
 assertEquals(maxMemory, testMemory.getMax());
 }

 @Test
 public void testIsOutOfMemory() {
 long availableMemory = testMemory.getMax() - testMemory.getCurrent();
 long totalInitialMemory = initialMemory + 200000L;
 assertFalse(testMemory.isOutOfMemory());
 testMemory.setEnabled(false);
 assertFalse(testMemory.isOutOfMemory());
 testMemory.setEnabled(true);
 if (availableMemory < totalInitialMemory) {
 assertTrue(testMemory.isOutOfMemory());
 }
 }

 @Test
 public void testToMegaByte() {
 assertEquals(4.5, Memory.toMegaByte(4611686016L), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryNull() {
 testMemory.showOutOfMemory(null);
 }

 @Test
 public void testShowOutOfMemory() {
 testMemory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 testMemory.stopThreads();
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
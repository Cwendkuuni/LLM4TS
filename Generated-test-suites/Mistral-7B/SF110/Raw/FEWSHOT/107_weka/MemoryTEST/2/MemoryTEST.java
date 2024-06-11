package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory testMemory;
 long initialMemory;
 long maxMemory;

 @Test
 public void testMemory() {
 testMemory = new Memory();
 initialMemory = testMemory.getInitial();
 maxMemory = testMemory.getMax();
 // Test isEnabled()
 Memory.setEnabled(true);
 Memory memory = new Memory();
 assertTrue(memory.isEnabled());
 Memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 // Test getUseGUI()
 Memory useGUIMemory = new Memory(true);
 assertTrue(useGUIMemory.getUseGUI());
 Memory noGUIMemory = new Memory();
 assertFalse(noGUIMemory.getUseGUI());
 // Test getInitial()
 assertEquals(initialMemory, testMemory.getInitial());
 // Test getCurrent()
 testMemory.getCurrent();
 // Test getMax()
 assertEquals(maxMemory, testMemory.getMax());
 // Test isOutOfMemory()
 testMemory.setEnabled(true);
 Memory outOfMemoryMemory = new Memory();
 assertFalse(outOfMemoryMemory.isOutOfMemory());
 long currentMemory = testMemory.m_Total + 200000L;
 Memory outOfMemoryMemory2 = new Memory();
 assertTrue(outOfMemoryMemory2.isOutOfMemory());
 // Test toMegaByte()
 assertEquals(1.953125, Memory.toMegaByte(1048576), 0.00001);
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.showOutOfMemory();
}

 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.stopThreads();
}

}
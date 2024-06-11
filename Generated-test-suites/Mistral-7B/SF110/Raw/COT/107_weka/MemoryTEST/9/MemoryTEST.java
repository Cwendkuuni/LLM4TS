package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        memory.setUseGUI(false);
        assertFalse(memory.getUseGUI());
        assertEquals(memory.getInitial(), Memory.m_Initial);
        memory.getCurrent(); // method call to update current memory
        assertNotEquals(memory.getCurrent(), Memory.m_Initial);
        assertFalse(memory.isOutOfMemory());
        long bytesToAllocate = 200000000L; // 200 MB
        Memory memoryWithLargeDataset = new Memory();
        for (int i = 0; i < 100; i++) {
            double[] data = new double[bytesToAllocate];
        }
        assertTrue(memoryWithLargeDataset.isOutOfMemory());
        memoryWithLargeDataset.showOutOfMemory(); // test for showOutOfMemory method
    }

    @Test(expected = NullPointerException.class)
    public void testThreadsInterruption() {
        Memory memory = new Memory();
        memory.stopThreads(); // test for method that uses deprecated Thread.stop()
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals(memory.getRevision(), RevisionUtils.extract("$Revision: 8034 $"));
    }

    @Test
    public void testMain() {
        Memory.main(new String[0]); // test for static main method
    }
}
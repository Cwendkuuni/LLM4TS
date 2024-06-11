package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        Memory memoryWithGUI = new Memory(true);
        assertTrue(memoryWithGUI.getUseGUI());
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getTotal());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(Memory.m_Enabled);
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEnabledWithInvalidArgument() {
        Memory memory = new Memory();
        memory.setEnabled(-1);
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
        Memory memory = new Memory();
        long initial = memory.getInitial();
        assertEquals(Runtime.getRuntime().totalMemory(), initial);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        assertEquals(Runtime.getRuntime().totalMemory(), current);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        assertEquals(Runtime.getRuntime().maxMemory(), max);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        long heapSize = Runtime.getRuntime().totalMemory() + 500000L;
        Runtime.getRuntime().exec("java -Xmx" + heapSize + "m -classpath ... WekaClass"); // Assuming WekaClass is the name of the Weka class to be tested
        Memory memoryWithSmallDataset = new Memory();
        assertTrue(memoryWithSmallDataset.isOutOfMemory());
        Memory memoryWithLargeHeapSize = new Memory();
        assertFalse(memoryWithLargeHeapSize.isOutOfMemory());
    }

    @Test(expected = IllegalStateException.class)
    public void testShowOutOfMemoryWithoutGUI() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
    }

    @Test
    public void testShowOutOfMemoryWithGUI() {
        Memory memory = new Memory(true);
        memory.showOutOfMemory();
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1024 * 1024 * 2;
        double megabytes = Memory.toMegaByte(bytes);
        assertEquals(2.0, megabytes, 0.0);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {});
            threads[i].start();
        }
        memory.stopThreads();
        for (Thread thread : threads) {
            assertFalse(thread.isAlive());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRevision() {
        Memory memory = new Memory();
        memory.getRevision();
    }

    @Test
    public void testMain() {
        Memory.main(new String[]{});
    }
}
package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertFalse(memory.isUseGUI());
        Memory memoryWithGUI = new Memory(true);
        assertTrue(memoryWithGUI.isUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        Memory.setEnabled(true);
        memory = new Memory();
        assertTrue(memory.isEnabled());
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
        Memory memoryWithInitial = new Memory();
        long initial2 = memoryWithInitial.getInitial();
        assertEquals(initial, initial2);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        Memory memory2 = new Memory();
        long current2 = memory2.getCurrent();
        assertEquals(current, current2);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        Memory memory2 = new Memory();
        long max2 = memory2.getMax();
        assertEquals(max, max2);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        long totalMemory = Runtime.getRuntime().totalMemory();
        totalMemory += 10000000; // add some memory usage
        Memory memoryWithMemoryUsage = new Memory();
        assertTrue(memoryWithMemoryUsage.isOutOfMemory());
    }

    @Test(expected = NullPointerException.class)
    public void testToMegaByteWithNull() {
        Memory memory = null;
        Memory.toMegaByte(memory);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();
        assertNotNull(revision);
    }

    @Test
    public void testMain() {
        Memory.main(new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoryStaticInitializationWithFalse() {
        Memory.m_Enabled = false;
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testThreadEnumarate() {
        Memory memory = new Memory();
        memory.stopThreads();
        Thread[] threadGroup = new Thread[Thread.activeCount()];
        Thread.enumerate(threadGroup);
        Thread.enumerate(null);
    }
}
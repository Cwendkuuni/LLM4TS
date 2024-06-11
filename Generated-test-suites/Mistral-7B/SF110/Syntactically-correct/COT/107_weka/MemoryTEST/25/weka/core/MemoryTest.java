package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertNotNull(memory);
        Memory memoryWithGUI = new Memory(true);
        assertNotNull(memoryWithGUI);
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        Memory memoryWithEnabled = new Memory();
        memoryWithEnabled.setEnabled(true);
        assertTrue(memoryWithEnabled.isEnabled());
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
        assertNotEquals(0, memory.getInitial());
        Memory memoryWithInitial = new Memory();
        long initial = memoryWithInitial.getInitial();
        Memory memoryWithNewInitial = new Memory();
        memoryWithNewInitial.setInitial(initial + 100);
        assertEquals(initial + 100, memoryWithNewInitial.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        memory = new Memory();
        long newCurrent = memory.getCurrent();
        assertEquals(current, newCurrent);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        memory = new Memory();
        long newMax = memory.getMax();
        assertEquals(max, newMax);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        Memory memoryWithLimitedMemory = new Memory();
        memoryWithLimitedMemory.setInitial(Memory.toMegaByte(100) * 1024L);
        memoryWithLimitedMemory.setMax(Memory.toMegaByte(50) * 1024L);
        memoryWithLimitedMemory.setTotal(Memory.toMegaByte(30) * 1024L);
        assertTrue(memoryWithLimitedMemory.isOutOfMemory());
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryNullGUI() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.setUseGUI(true);
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
    }

    @Test(expected = NullPointerException.class)
    public void testStopThreadsNullThreads() {
        Memory memory = new Memory();
        memory.stopThreads(null);
    }

    @Test
    public void testToString() {
        Memory memory = new Memory();
        String toString = memory.toString();
        assertNotNull(toString);
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
}
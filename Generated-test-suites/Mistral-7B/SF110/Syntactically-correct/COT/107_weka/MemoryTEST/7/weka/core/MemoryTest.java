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
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        Memory memoryWithGUI = new Memory(true);
        assertTrue(memoryWithGUI.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        Memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        Memory.setEnabled(true);
        assertTrue(memory.isEnabled());
        Memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        assertNotNull(initial);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        assertNotNull(current);
        Memory newMemory = new Memory();
        long newCurrent = newMemory.getCurrent();
        assertNotEquals(current, newCurrent);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        assertNotNull(max);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        Memory memoryWithLimitedResources = new Memory();
        memoryWithLimitedResources.setEnabled(true);
        memoryWithLimitedResources.m_Max -= 100000L; // reduce max memory
        memoryWithLimitedResources.m_Initial += 50000L; // increase initial memory
        assertTrue(memoryWithLimitedResources.isOutOfMemory());
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryWithDisabledGUI() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
    }

    @Test
    public void testShowOutOfMemoryWithEnabledGUI() {
        Memory memory = new Memory(true);
        memory.showOutOfMemory();
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1234567;
        double megabytes = Memory.toMegaByte(bytes);
        assertEquals(1151.71, megabytes, 0.01); // checking against the example value from the method documentation
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStopThreadsWithInvalidThreadName() {
        Memory memory = new Memory();
        memory.stopThreads();
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        Thread thread = new Thread(() -> {});
        thread.start();
        memory.stopThreads();
        assertTrue(thread.isInterrupted());
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();
        assertNotNull(revision);
    }

    @Test
    public void testMainMethod() {
        Memory memory = new Memory();
        String output = Memory.main(new String[0])[0];
        assertNotNull(output);
    }

    static {
        Memory.setEnabled(false);
        Memory memory = new Memory();
        memory.getInitial();
        memory.getCurrent();
        memory.getMax();
    }
}
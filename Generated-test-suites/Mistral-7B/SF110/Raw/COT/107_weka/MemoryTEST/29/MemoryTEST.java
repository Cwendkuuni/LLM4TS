package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        Memory memoryWithGUI = new Memory(true);

        assertEquals(false, memory.getUseGUI());
        assertEquals(false, memoryWithGUI.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        Memory memoryWithEnabled = new Memory();
        memoryWithEnabled.setEnabled(true);

        assertFalse(memory.isEnabled());
        assertTrue(memoryWithEnabled.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);

        assertTrue(memory.isEnabled());
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
        memory.getCurrent();

        assertNotNull(memory.getTotal());
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
        memory.setEnabled(true);

        assertFalse(memory.isOutOfMemory());

        long initial = memory.getInitial();
        long max = memory.getMax();

        // Simulate running out of memory
        memory.setTotal(initial + 1000000000L);

        assertTrue(memory.isOutOfMemory());
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryWithNullGUI() {
        Memory memory = new Memory();
        memory.setEnabled(true);

        memory.showOutOfMemory();

        // showOutOfMemory should not be called with null GUI
        memory.getUseGUI();
        memory.showOutOfMemory();
    }

    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        long bytes = 123456789L;

        assertEquals(11.71385553203125, memory.toMegaByte(bytes), 0.00000001);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();

        Thread thread = new Thread(() -> {});
        thread.start();
        memory.stopThreads();

        assertFalse(thread.isAlive());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStopThreadsWithNullThreadGroup() {
        Memory memory = new Memory();
        Thread[] threads = null;

        memory.stopThreads(threads);
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();

        String revision = memory.getRevision();

        assertNotNull(revision);
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();

        memory.main(new String[0]);
    }

    @Test(expected = LinkageError.class)
    public void testShowOutOfMemoryWithNoMessageDialogSupport() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        MemoryTestUtils.disableMessageDialogs();

        memory.showOutOfMemory();
    }
}
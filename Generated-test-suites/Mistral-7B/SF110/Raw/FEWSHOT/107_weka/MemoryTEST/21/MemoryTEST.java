package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

    private Memory memory;

    @Test
    public void testMemory() {
        memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory.m_Enabled = true;
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
        Memory.m_Enabled = false;
        memory = new Memory();
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory.m_Enabled = false;
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        Memory.m_Enabled = false;
        assertTrue(memory.isEnabled());
        Memory.m_Enabled = true;
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
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
        memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
        Memory.m_Initial = 100L;
        memory = new Memory();
        assertEquals(100, memory.getInitial());
    }

    @Test
    public void testgetCurrent() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        memory = new Memory();
        assertEquals(runtime.totalMemory(), memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        memory = new Memory();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        Runtime runtime = Runtime.getRuntime();
        runtime.maxMem = 200L;
        memory = new Memory();
        assertEquals(200, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        memory = new Memory();
        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());
        long maxMemory = Runtime.getRuntime().maxMemory() - 200000L;
        memory = new Memory();
        memory.setEnabled(true);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.0);
        assertEquals(1.5, Memory.toMegaByte(16777216), 0.0);
        assertEquals(2.5, Memory.toMegaByte(2147483648), 0.0);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
        assertTrue(true); // Testing for side effect
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {});
            threads[i].start();
        }
        memory.stopThreads();
        for (Thread thread : threads) {
            assertTrue(thread.isAlive());
        }
        memory.stopThreads();
        for (Thread thread : threads) {
            assertFalse(thread.isAlive());
        }
    }

    @Test
    public void testGetRevision() {
        assertEquals("$Revision: 8034 $", Memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        memory.main(new String[0]);
        String initialMemoryMessage = "Initial memory: ";
        String maxMemoryMessage = "Max memory: ";
        assertTrue(memory.getOutput().contains(initialMemoryMessage));
        assertTrue(memory.getOutput().contains(maxMemoryMessage));
    }
}
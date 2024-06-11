package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {

    @Test
    public void testMemory() {
        Memory memory = new Memory();
        assertEquals(false, memory.isEnabled());
        memory.setEnabled(true);
        assertEquals(true, memory.isEnabled());

        memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());

        memory = new Memory(false);
        assertEquals(false, memory.getUseGUI());

        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
        assertFalse(memory.isOutOfMemory());

        long newMemory = Runtime.getRuntime().totalMemory() + 100000000;
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().setTotalMemory(newMemory);
        Runtime.getRuntime().setMaxMemory(newMemory);
        Memory memoryWithLimit = new Memory();
        memoryWithLimit.setEnabled(true);
        assertTrue(memoryWithLimit.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(4.19430469168621E9D, Memory.toMegaByte(430727552000L), 0.0000001D);
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryWithNull() {
        Memory memory = new Memory();
        memory.showOutOfMemory(null);
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

        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        int count = 0;
        for (Thread thread : threads) {
            if (thread != null) {
                memory.stopThreads();
                if (thread.isAlive()) {
                    ++count;
                }
            }
        }
        assertEquals(0, count);
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        memory.main(new String[0]);

        assertEquals("Initial memory: 11.551MB (12383472)", memory.getRevision());
        assertEquals("Max memory: 13.36MB (14336000)", memory.getRevision());
    }

    @Test
    public void testStaticInitialization() {
        assertEquals(true, Memory.m_Enabled);
        assertEquals(Runtime.getRuntime().totalMemory(), Memory.m_Initial);
    }
}
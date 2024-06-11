package weka.core;

import static org.junit.Assert.*;
import static weka.core.Utils.doubleToString;

import org.junit.Test;

public class MemoryTest {

    @Test
    public void testMemory() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent(), 0);
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax(), 0);
        memory = new Memory(true);
        assertFalse(memory.isOutOfMemory());
        memory.setEnabled(true);
        long initial = memory.getInitial();
        memory.showOutOfMemory();
        assertTrue(memory.isOutOfMemory());
        assertEquals(initial, memory.getInitial(), 0);
        memory.stopThreads();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(1.5, Memory.toMegaByte(1572864), 0.0);
        assertEquals(2.5, Memory.toMegaByte(2672450), 0.0);
        assertEquals(3.5, Memory.toMegaByte(38982448), 0.0);
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();
        assertFalse(revision.isEmpty());
    }

    @Test
    public void testMain() {
        Memory.main(new String[0]);
        Memory memory = new Memory();
        assertEquals(Utils.doubleToString(Memory.toMegaByte(memory.getInitial()), 1), "Initial memory: 0.0MB (0)", true);
        assertEquals(Utils.doubleToString(Memory.toMegaByte(memory.getMax()), 1), "Max memory: 12.0MB (14336064)", true);
    }

    @Test(expected = NullPointerException.class)
    public void testStopThreadsWithNullThreadGroup() {
        Memory memory = new Memory();
        Thread[] thGroup = null;
        memory.stopThreads();
    }
}
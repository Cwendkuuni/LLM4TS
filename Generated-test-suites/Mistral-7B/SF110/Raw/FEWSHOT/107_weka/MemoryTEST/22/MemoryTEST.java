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
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
    }

    @Test
    public void testIsEnabled() {
        Memory.setEnabled(true);
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
        Memory.setEnabled(false);
        memory = new Memory();
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory.setEnabled(true);
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
        Memory.setEnabled(true);
        memory = new Memory();
        assertTrue(memory.isEnabled());
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
        memory = new Memory(true);
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        memory = new Memory();
        assertEquals(current, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        memory = new Memory(true);
        assertEquals(max, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        memory = new Memory(true);
        Memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());
        memory = new Memory(true);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(2.5, Memory.toMegaByte(2621440), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryNull() {
        Memory memory = new Memory();
        memory.showOutOfMemory(null);
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
        assertEquals(RevisionUtils.extract("$Revision: 8034 $"), memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory.main(new String[0]);
    }

    @ org.junit.BeforeClass
    public static void setupStaticVariables() {
        Memory.setEnabled(true);
        Memory.m_Initial = Runtime.getRuntime().totalMemory();
    }
}
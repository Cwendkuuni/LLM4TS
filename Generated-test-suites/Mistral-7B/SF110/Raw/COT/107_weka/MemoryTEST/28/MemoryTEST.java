package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
    }

    @Test
    public void testMemoryConstructorDefault() {
        Memory memory = new Memory();
        assertEquals(false, memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        Memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        Memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        assertNotEquals(-1, initial);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        assertNotEquals(-1, current);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        assertNotEquals(-1, max);
    }

    @Test(expected = IllegalStateException.class)
    public void testIsOutOfMemoryWithEnabledFalse() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemoryWithEnabledTrue() {
        Memory memory = new Memory(true);
        Memory.setEnabled(true);
        Memory.m_Initial = 5000000;
        Memory.m_Total = 10000000;
        Memory.m_Max = 15000000;
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(3.14, Memory.toMegaByte(3141592655L), 0.01);
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryWithGUIFalse() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
    }

    @Test
    public void testShowOutOfMemoryWithGUITrue() {
        Memory memory = new Memory(true);
        Memory.setEnabled(true);
        Memory.m_Initial = 5000000;
        Memory.m_Total = 10000000;
        Memory.m_Max = 15000000;
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
    }

    @Test(expected = SecurityException.class)
    public void testStopThreadsWithoutPermission() {
        Memory memory = new Memory();
        memory.stopThreads();
    }

    @Test
    public void testRevisionHandlerMethod() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMainMethod() {
        Memory memory = new Memory();
        Memory.m_Enabled = true;
        Memory.m_Initial = 1000000;
        Memory.m_Total = 2000000;
        Memory.m_Max = 3000000;
        try {
            Memory.main(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Memory memoryInstance = new Memory();
        assertEquals(1.0, Memory.toMegaByte(Memory.m_Initial), 0.0);
        assertEquals(2.0, Memory.toMegaByte(Memory.m_Total), 0.0);
        assertEquals(3.0, Memory.toMegaByte(Memory.m_Max), 0.0);
    }
}
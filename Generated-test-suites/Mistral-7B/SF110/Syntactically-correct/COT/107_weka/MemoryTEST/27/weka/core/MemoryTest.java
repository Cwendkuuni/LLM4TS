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
        assertNotEquals(-1, initial);
        memory = new Memory(); // reset
        Memory.setStaticField("m_Initial", initial + 1);
        memory = new Memory();
        assertEquals(initial + 1, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        assertNotEquals(-1, current);
        memory = new Memory(); // reset
        Runtime runtime = Mockito.mock(Runtime.class);
        when(runtime.totalMemory()).thenReturn(current + 1L);
        Memory memoryWithMockRuntime = new Memory(null, runtime);
        assertEquals(current + 1, memoryWithMockRuntime.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        assertNotEquals(-1, max);
        memory = new Memory(); // reset
        Runtime runtime = Mockito.mock(Runtime.class);
        when(runtime.maxMemory()).thenReturn(max + 1L);
        Memory memoryWithMockRuntime = new Memory(null, runtime);
        assertEquals(max + 1, memoryWithMockRuntime.getMax());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsOutOfMemoryWithEnabledFalse() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        memory.isOutOfMemory();
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        Memory.setEnabled(true);
        memory.setInitial(10L);
        memory.setMax(5L);
        memory.setTotal(3L);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.setMax(1L);
        memory.setTotal(0L);
        memory.showOutOfMemory();
    }

    @Test(expected = NullPointerException.class)
    public void testStopThreadsWithNullThreadGroup() {
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

    @Test
    public void testStaticInitialization() {
        Memory memory = new Memory();
        assertEquals(Memory.m_Initial, memory.getInitial());
    }
}
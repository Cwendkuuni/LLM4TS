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
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getTotal());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());

        Memory memoryDisabled = new Memory();
        assertFalse(memoryDisabled.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        memory.setUseGUI(true);
        assertTrue(memory.getUseGUI());

        Memory memoryFalse = new Memory();
        assertFalse(memoryFalse.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        Memory memoryWithInitial = new Memory();
        memoryWithInitial.setInitial(100L);

        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
        assertEquals(100L, memoryWithInitial.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        Runtime runtime = Runtime.getRuntime();
        long initial = runtime.totalMemory();
        memory.getCurrent();

        assertTrue(memory.getTotal() > initial);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        long maxMemory = Runtime.getRuntime().maxMemory() - 50000L;
        memory.setInitial(Runtime.getRuntime().totalMemory() + 50000L);

        assertFalse(memory.isOutOfMemory());
        memory.setTotal(maxMemory);
        assertTrue(memory.isOutOfMemory());
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryWithNullUseGUI() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.setTotal(memory.getMax() - 100000L);
        memory.showOutOfMemory();
        memory.getUseGUI();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShowOutOfMemoryWithNegativeInitial() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.setTotal(memory.getMax() - 100000L);
        memory.setInitial(-100000L);
        memory.showOutOfMemory();
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.setTotal(memory.getMax() - 100000L);
        memory.showOutOfMemory();
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1234;
        double result = Memory.toMegaByte(bytes);

        assertEquals(1.2, result, 0.0);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // This is hard to test as we cannot see the threads inside the test class
        // We can only test that the method does not throw an exception
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRevision() {
        Memory memory = new Memory();
        memory.getRevision();
    }

    @Test
    public void testMain() {
        Memory.main(new String[0]);
    }

    static {
        Memory memory = new Memory();
        Memory memoryDisabled = new Memory();
        memoryDisabled.setEnabled(false);

        assertEquals(true, Memory.m_Enabled);
        assertEquals(Runtime.getRuntime().totalMemory(), Memory.m_Initial);
        assertEquals(true, memory.isEnabled());
        assertEquals(false, memoryDisabled.isEnabled());
    }
}
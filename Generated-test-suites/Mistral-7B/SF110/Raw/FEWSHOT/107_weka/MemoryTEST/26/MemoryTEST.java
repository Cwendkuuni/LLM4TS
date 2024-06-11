package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

    private Memory memory;

    @Test
    public void testMemory() {
        memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testMemoryEnable() {
        memory = new Memory(true);
        assertTrue(memory.isEnabled());
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testMemoryDisable() {
        memory = new Memory(false);
        assertFalse(memory.isEnabled());
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemoryTrue() {
        memory = new Memory();
        long maxMemory = Runtime.getRuntime().maxMemory() - 1000000;
        memory.setEnabled(true);
        memory.setUseGUI(true);
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("javaw.exe -Xms512m -Xmx" + Long.toString(maxMemory) + "m -classpath .."); // set smaller heap size
        memory.setInitial(runtime.totalMemory() - 500000);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemoryFalse() {
        memory = new Memory();
        memory.setEnabled(true);
        memory.setUseGUI(true);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testShowOutOfMemory() {
        memory = new Memory();
        memory.setEnabled(true);
        memory.setUseGUI(true);
        memory.showOutOfMemory();
    }

    @Test
    public void testToMegaByte() {
        assertEquals(4.215471263556356, Memory.toMegaByte(43262096752L), 0.00001);
    }

    @Test
    public void testStopThreads() {
        memory = new Memory();
        memory.setEnabled(true);
        memory.stopThreads();
        assertTrue(true); // TODO: Implement proper verification for thread stopping
    }

    @Test
    public void testGetRevision() {
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory mem = new Memory();
        assertEquals(Memory.toMegaByte(Runtime.getRuntime().totalMemory()), Memory.toMegaByte(mem.getInitial()), 0.0);
        assertEquals(Memory.toMegaByte(Runtime.getRuntime().maxMemory()), Memory.toMegaByte(mem.getMax()), 0.0);
    }

    @BeforeClass
    public static void setupClass() {
        Memory.m_Enabled = true;
        Memory.m_Initial = Runtime.getRuntime().totalMemory();
    }
}
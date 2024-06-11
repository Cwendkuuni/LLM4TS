package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {

    private Memory memory;

    @Test
    public void testMemory() {
        memory = new Memory();
        assertEquals(false, memory.isEnabled());
        assertEquals(0, memory.getUseGUI());
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        assertEquals(false, memory.isOutOfMemory());
    }

    @Test
    public void testMemoryBoolean() {
        memory = new Memory(true);
        assertEquals(true, memory.isEnabled());
        assertEquals(true, memory.getUseGUI());
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        assertEquals(false, memory.isOutOfMemory());
    }

    @Test
    public void testIsEnabled() {
        Memory.setEnabled(false);
        Memory memory = new Memory();
        assertEquals(false, memory.isEnabled());

        Memory.setEnabled(true);
        memory = new Memory();
        assertEquals(true, memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());

        memory.setEnabled(true);
        assertEquals(true, memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertEquals(false, memory.getUseGUI());

        memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        Runtime runtime = Runtime.getRuntime();
        memory.setRuntime(new Runtime() {
            @Override
            public long totalMemory() {
                return runtime.totalMemory() * 2;
            }

            @Override
            public long maxMemory() {
                return runtime.maxMemory() * 2;
            }
        });
        assertEquals(runtime.totalMemory() * 2, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertEquals(false, memory.isOutOfMemory());

        long totalMemory = Runtime.getRuntime().totalMemory() - 1000000;
        memory.setTotal(totalMemory);
        Memory.setInitial(Runtime.getRuntime().totalMemory() - 2000000);
        assertEquals(true, memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.0001);
        assertEquals(2.5, Memory.toMegaByte(2621440), 0.0001);
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
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        assertEquals("Initial memory: ..." + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB (" + Runtime.getRuntime().totalMemory() + ")", memory.getRevision());
        assertEquals("Max memory: " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB (" + Runtime.getRuntime().maxMemory() + ")", memory.getRevision());
    }

    @Test
    public void testStaticInitialization() {
        Memory memory = new Memory();
        assertEquals(true, Memory.m_Enabled);
        assertEquals(Runtime.getRuntime().totalMemory(), Memory.m_Initial);
    }
}
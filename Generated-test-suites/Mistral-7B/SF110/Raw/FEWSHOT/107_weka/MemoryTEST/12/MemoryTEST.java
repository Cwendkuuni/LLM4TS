package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        long total = memory.getTotal();
        long max = memory.getMax();
        long initial = memory.getInitial();

        Memory memoryWithGUI = new Memory(true);
        long totalWithGUI = memoryWithGUI.getTotal();
        long maxWithGUI = memoryWithGUI.getMax();
        long initialWithGUI = memoryWithGUI.getInitial();

        assertEquals("Memory constructor without GUI should return default value", false, memory.getUseGUI());
        assertEquals("Memory constructor with GUI should return true", true, memoryWithGUI.getUseGUI());
        assertEquals("Initial memory should be the same for both constructors", initial, initialWithGUI);
        assertEquals("Total memory should be the same for both constructors", total, totalWithGUI);
        assertEquals("Max memory should be the same for both constructors", max, maxWithGUI);
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue("Memory should be enabled by default", memory.isEnabled());

        Memory memoryDisabled = new Memory();
        memoryDisabled.setEnabled(false);
        assertFalse("Memory should be disabled after setting enabled to false", memoryDisabled.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse("Memory should not use GUI by default", memory.getUseGUI());

        Memory memoryWithGUI = new Memory(true);
        assertTrue("Memory should use GUI after creating with useGUI = true", memoryWithGUI.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();

        Memory memoryWithInitialSet = new Memory();
        memoryWithInitialSet.setInitial(initial + 1000000L);
        assertNotEquals("Initial memory should not be the same after setting it", initial, memoryWithInitialSet.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();

        memory.setEnabled(false);
        long currentDisabled = memory.getCurrent();

        assertEquals("Current memory should be the same before and after disabling Memory instance", current, currentDisabled);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        long max = memory.getMax();
        long initial = memory.getInitial();

        long current = initial + 2 * max;
        memory.setTotal(current);

        assertTrue("Memory should be out of memory", memory.isOutOfMemory());

        memory.setEnabled(false);
        assertFalse("Memory should not be out of memory after disabling it", memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals("1 byte should be 0.000001 MB", 0.000001, Memory.toMegaByte(1L), 0.000001);
        assertEquals("1024 bytes should be 1 MB", 1, Memory.toMegaByte(1024L), 0.001);
        assertEquals("1048576 bytes should be 1 MB", 1, Memory.toMegaByte(1048576L), 0.01);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();

        memory.setEnabled(true);
        memory.showOutOfMemory();
    }

    @Test(expected = NullPointerException.class)
    public void testStopThreadsNull() {
        Memory memory = new Memory();
        memory.stopThreads(null);
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("Get revision should return a valid string", "8034", memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        long max = memory.getMax();

        Memory.main(new String[0]);

        assertEquals("Initial memory should be printed correctly", Utils.doubleToString(Memory.toMegaByte(initial), 1) + "MB", Memory.m_Initial);
        assertEquals("Max memory should be printed correctly", Utils.doubleToString(Memory.toMegaByte(max), 1) + "MB", Memory.m_Max);
    }

    @Test
    public void testStaticInitialization() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        long max = memory.getMax();

        assertEquals("Memory should be initialized with true enabled and the current total and max memory", true, Memory.m_Enabled);
        assertEquals("Initial memory should be the same as the one from Memory constructor", initial, Memory.m_Initial);
        assertEquals("Max memory should be the same as the one from Memory constructor", max, Memory.m_Max);
    }
}
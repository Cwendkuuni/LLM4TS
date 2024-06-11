package weka.core.MemoryTest;

import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        boolean initialEnabledState = memory.isEnabled();

        memory.setEnabled(false);
        boolean disabledState = memory.isEnabled();

        assertEquals(initialEnabledState, memory.isEnabled());
        assertEquals(false, disabledState);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long initialTotalMemory = memory.getCurrent();
        memory = new Memory(true);
        long guiMemoryTotal = memory.getCurrent();

        assertEquals(initialTotalMemory, memory.getCurrent());
        assertEquals(guiMemoryTotal, memory.getCurrent());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        long totalMemory = memory.getCurrent();
        long maxMemory = memory.getMax();

        assertFalse(memory.isOutOfMemory());

        totalMemory += 1000000L; // Allocating additional memory
        assertTrue(memory.isOutOfMemory());

        totalMemory -= 1000000L; // Freeing some memory
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemory_largeMemoryUsage() {
        Memory memory = new Memory();
        long initialTotalMemory = memory.getCurrent();
        long maxMemory = memory.getMax();

        // Allocating a large amount of memory
        byte[] largeData = new byte[maxMemory * 2];

        totalMemory += largeData.length;
        assertTrue(memory.isOutOfMemory());

        // Freeing the memory
        memory = new Memory();
        totalMemory = memory.getCurrent();

        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemory_maxMemorySizeChanged() {
        Memory memory = new Memory();
        long initialTotalMemory = memory.getCurrent();
        long initialMaxMemory = memory.getMax();
        long newMaxMemory = initialMaxMemory * 2;

        memory.setMax(newMaxMemory);

        assertFalse(memory.isOutOfMemory());

        totalMemory += 1000000L; // Allocating additional memory
        assertTrue(memory.isOutOfMemory());
    }

    @Test(expected = OutOfMemoryError.class)
    public void testIsOutOfMemory_outOfMemoryError() {
        Memory memory = new Memory();
        long totalMemory = memory.getCurrent();
        long maxMemory = memory.getMax();

        // Allocating more memory than the maximum available
        byte[] largeData = new byte[maxMemory + 1000000L];
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
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        System.out.println("Initial memory: " + Utils.doubleToString(toMegaByte(memory.getInitial()), 1) + "MB" + " (" + memory.getInitial() + ")");
        System.out.println("Max memory: " + Utils.doubleToString(toMegaByte(memory.getMax()), 1) + "MB" + " (" + memory.getMax() + ")");
    }
}
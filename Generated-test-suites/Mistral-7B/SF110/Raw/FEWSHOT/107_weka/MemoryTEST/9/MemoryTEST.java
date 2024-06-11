package weka.core;

import static org.junit.Assert.*;
import static weka.core.Utils.doubleToString;

import org.junit.Test;

public class MemoryTest {

    Memory memory;

    @Test
    public void testMemory() {
        memory = new Memory();
        assertEquals(Memory.m_Enabled, true);
        memory = new Memory(false);
        assertEquals(memory.getUseGUI(), false);
        memory = new Memory(true);
        assertEquals(memory.getUseGUI(), true);
    }

    @Test
    public void testIsEnabled() {
        memory = new Memory();
        assertTrue(memory.isEnabled());
        memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        memory = new Memory();
        assertFalse(memory.getUseGUI());
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        memory = new Memory();
        long initial = memory.getInitial();
        long total = memory.getCurrent();
        assertEquals(initial, total);
    }

    @Test
    public void testGetCurrent() {
        memory = new Memory();
        long initial = memory.getInitial();
        long total = memory.getCurrent();
        memory.setEnabled(false);
        long current = memory.getCurrent();
        assertEquals(initial, current);
        memory.setEnabled(true);
        total = memory.getCurrent();
        assertNotEquals(initial, total);
    }

    @Test
    public void testGetMax() {
        memory = new Memory();
        long max = memory.getMax();
        Runtime runtime = Runtime.getRuntime();
        long expectedMax = runtime.maxMemory();
        assertEquals(max, expectedMax);
    }

    @Test
    public void testIsOutOfMemory() {
        memory = new Memory();
        long max = memory.getMax();
        long total = memory.getCurrent();
        assertFalse(memory.isOutOfMemory());
        total += 10000000L;
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1024 * 1024 * 1024;
        double expected = 1.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.0);
    }

    @Test
    public void testShowOutOfMemory() {
        memory = new Memory();
        Memory.m_Enabled = false;
        memory.showOutOfMemory();
        Memory.m_Enabled = true;
        Runtime runtime = Runtime.getRuntime();
        long max = runtime.maxMemory();
        long total = runtime.totalMemory();
        memory = new Memory();
        memory.setEnabled(true);
        memory.showOutOfMemory();
        String expectedMsg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   ?";
        String actualMsg = "";
        try {
            memory.showOutOfMemory();
        } catch (Exception e) {
            actualMsg = e.getMessage();
        }
        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    public void testStopThreads() {
        memory = new Memory();
        Thread[] threads = new Thread[2];
        for (int i = 0; i < 2; ++i) {
            threads[i] = new Thread(() -> {});
            threads[i].start();
        }
        memory.stopThreads();
        for (int i = 0; i < 2; ++i) {
            try {
                threads[i].join(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetRevision() {
        memory = new Memory();
        String expectedRevision = "8034";
        String actualRevision = memory.getRevision();
        assertEquals(expectedRevision, actualRevision);
    }

    @Test
    public void testMain() {
        memory = new Memory();
        String expectedOutput = "Initial memory: ?.?MB (?)\nMax memory: ?.?MB (?)";
        Runtime runtime = Runtime.getRuntime();
        long initial = runtime.totalMemory();
        long max = runtime.maxMemory();
        String output = "";
        try {
            memory.main(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expectedOutput, output);
    }
}
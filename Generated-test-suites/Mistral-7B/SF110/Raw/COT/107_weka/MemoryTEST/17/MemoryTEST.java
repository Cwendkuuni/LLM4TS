package weka.core.test;

import static org.junit.Assert.*;
import static weka.core.Memory.toMegaByte;

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
    public void testMemoryIsEnabled() {
        Memory memory = new Memory();

        assertFalse(memory.isEnabled());

        Memory.setEnabled(true);
        memory = new Memory();
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testMemorySetEnabled() {
        Memory memory = new Memory();

        assertFalse(memory.isEnabled());

        memory.setEnabled(true);
        assertTrue(memory.isEnabled());

        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testMemoryIsOutOfMemory() {
        Memory memory = new Memory();
        Runtime runtime = Runtime.getRuntime();

        assertFalse(memory.isOutOfMemory());

        long initial = memory.getInitial();
        long max = memory.getMax();

        while (memory.getCurrent() < max - initial - 1) {
            memory.getCurrent(); // consume more memory
        }

        assertTrue(memory.isOutOfMemory());
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryWithNullUseGUI() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.showOutOfMemory();
        memory.setUseGUI(null);
        memory.showOutOfMemory();
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);

        long initial = memory.getInitial();
        long max = memory.getMax();
        long total = 0;

        while (memory.getCurrent() < max - initial - 1) {
            total += 1024 * 1024; // consume more memory
            memory.getCurrent();
        }

        memory.showOutOfMemory();
    }

    @Test
    public void testToMegaByte() {
        long bytes = 123456789;
        double expected = 115.11511511511511;

        assertEquals(expected, toMegaByte(bytes), 0.0000000000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToMegaByteWithNegativeBytes() {
        long bytes = -123456789;
        toMegaByte(bytes);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        memory.stopThreads();
        assertFalse(thread.isAlive());
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String expected = "8034";

        assertEquals(expected, memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();

        Memory.setEnabled(false);

        memory.main(new String[0]);

        String initialMessage = "Initial memory: 0.0MB (0)";
        String maxMessage = "Max memory: 1148.8315546875MB (14492230336)";

        String output = "Initial memory: " + Utils.doubleToString(0.0, 1) + "MB (" + 0 + ")\n" +
                "Max memory: " + Utils.doubleToString(1148.8315546875, 1) + "MB (" + 14492230336 + ")\n";

        assertEquals(output, memory.getOutput());
    }

}
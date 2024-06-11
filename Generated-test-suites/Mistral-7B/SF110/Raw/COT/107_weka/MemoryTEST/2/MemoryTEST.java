package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
        long initial = memory.getInitial();
        long total = memory.getCurrent();
        long max = memory.getMax();
        assertTrue(initial < total);
        assertTrue(total < max);
    }

    @Test
    public void testMemoryIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testMemoryIsOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        long max = memory.getMax();
        long initial = max - 200001L;
        memory.setInitial(initial);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testMemoryShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.showOutOfMemory();
        // This test is difficult to do in JUnit as it prints to System.err,
        // which we cannot assert on directly. You may consider using a
        // testing framework with functionality to capture System.err output.
    }

    @Test(expected = NullPointerException.class)
    public void testMemoryStopThreadsNullThreads() {
        Memory memory = new Memory();
        memory.stopThreads(null);
    }

    @Test
    public void testMemoryToMegaByte() {
        long bytes = 123456L;
        double expected = 117.45;
        double result = Memory.toMegaByte(bytes);
        assertEquals(expected, result, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoryToMegaByteNegativeBytes() {
        long bytes = -123456L;
        Memory.toMegaByte(bytes);
    }

    @Test
    public void testMemoryMain() {
        Memory memory = new Memory();
        memory.main(new String[0]);
        // This test is difficult to do in JUnit as it prints to System.out,
        // which we cannot assert on directly.
    }

    @Test(expected = SecurityException.class)
    public void testMemoryStopThreads() {
        Memory memory = new Memory();
        Thread thread = new Thread() {
            @Override
            public void run() {
                // Do nothing
            }
        };
        memory.stopThreads();
        thread.start();
        thread.join();
        memory.stopThreads();
    }

    @Test
    public void testMemoryRevision() {
        Memory memory = new Memory();
        String expected = "$Revision: 8034 $";
        String result = memory.getRevision();
        assertEquals(expected, result);
    }
}
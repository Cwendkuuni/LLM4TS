package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemory() {
        Memory memory = new Memory();
        assertEquals(false, memory.isEnabled());
        memory.setEnabled(true);
        assertEquals(true, memory.isEnabled());
    }

    @Test
    public void testMemoryWithGUI() {
        Memory memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        long initial = memory.getInitial();
        long max = memory.getMax();
        long total = memory.getCurrent();
        long expected = initial + 200000L;

        memory.setTotal(total + 1000000L); // set total memory to be more than max + initial
        assertTrue(memory.isOutOfMemory());
        memory.setTotal(total); // set total memory back to original value
        assertFalse(memory.isOutOfMemory());

        memory.setInitial(expected); // set initial memory to be less than current + max
        assertTrue(memory.isOutOfMemory());
        memory.setInitial(initial); // set initial memory back to original value
        assertFalse(memory.isOutOfMemory());
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryNull() {
        Memory memory = new Memory();
        memory.showOutOfMemory(null);
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1234567890L;
        double expected = 1156.6302736328125;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.001);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {});
            threads[i].start();
        }
        memory.stopThreads();
        for (Thread thread : threads) {
            assertFalse(thread.isAlive());
        }
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String expected = "$Revision: 8034 $";
        assertEquals(expected, memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        long max = memory.getMax();

        Memory.main(new String[0]);
        assertEquals("Initial memory: " + initial + " (" + initial + ")", System.out.toString());
        assertEquals("Max memory: " + max + " (" + max + ")", System.out.toString().substring(System.out.toString().indexOf("Max memory: ")));
    }
}
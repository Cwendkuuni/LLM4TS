package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.JOptionPane;
import java.awt.ThreadedEventQueueRenderer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

    private Memory memory;
    private long initialMemory;
    private long maxMemory;

    @Before
    public void setUp() {
        memory = new Memory(false);
        initialMemory = memory.getInitial();
        maxMemory = memory.getMax();
    }

    @Test
    public void testMemory() {
        assertEquals(false, Memory.m_Enabled);
        assertEquals(false, memory.getUseGUI());
        assertEquals(initialMemory, memory.getInitial());
        assertEquals(maxMemory, memory.getMax());
        assertEquals(maxMemory, memory.getCurrent());
        assertEquals(false, memory.isOutOfMemory());
    }

    @Test
    public void testMemoryEnabled() {
        Memory.m_Enabled = true;
        memory = new Memory(false);
        assertEquals(true, Memory.m_Enabled);
        assertEquals(false, memory.getUseGUI());
        assertEquals(initialMemory, memory.getInitial());
        assertEquals(maxMemory, memory.getMax());
        assertEquals(maxMemory, memory.getCurrent());
        assertEquals(false, memory.isOutOfMemory());
    }

    @Test
    public void testMemoryUseGUI() {
        memory = new Memory(true);
        assertEquals(false, Memory.m_Enabled);
        assertEquals(true, memory.getUseGUI());
        assertEquals(initialMemory, memory.getInitial());
        assertEquals(maxMemory, memory.getMax());
        assertEquals(maxMemory, memory.getCurrent());
        assertEquals(false, memory.isOutOfMemory());
    }

    @Test
    public void testGetCurrentMemory() {
        memory = new Memory(false);
        memory.getCurrent();
        memory.getCurrent();
        assertEquals(maxMemory, memory.getCurrent());
    }

    @Test
    public void testIsOutOfMemory() {
        memory = new Memory(false);
        // Initial memory is less than max memory + initial memory + 200000L
        long newInitial = maxMemory + memory.getCurrent() + 199999L;
        Memory.m_Initial = newInitial;
        Memory.m_Enabled = true;
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemoryFalse() {
        memory = new Memory(false);
        // Initial memory is greater than max memory + current memory + 200000L
        long newInitial = maxMemory + memory.getCurrent() + 200010L;
        Memory.m_Initial = newInitial;
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testShowOutOfMemory() {
        Memory.m_Enabled = true;
        memory = new Memory(false);
        // Simulate showing out of memory error message using reflection
        try {
            Class<?> clazz = Memory.class;
            Method method = clazz.getDeclaredMethod("showOutOfMemory");
            method.setAccessible(true);
            method.invoke(memory);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail("Failed to invoke 'showOutOfMemory' method");
        }
        assertTrue(JOptionPane.showMessageDialog(null, "OutOfMemory", "OutOfMemory", JOptionPane.ERROR_MESSAGE) != null);
    }

    @Test
    public void testStopThreads() {
        memory = new Memory(false);
        Thread[] threads = new Thread[6];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(() -> {});
            threads[i].start();
        }
        memory.stopThreads();
        for (int i = 0; i < threads.length; ++i) {
            assertEquals(Thread.State.TERMINATED, threads[i].getState());
        }
    }

    @Test
    public void testGetRevision() {
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @After
    public void tearDown() {
        Memory.m_Enabled = false;
        memory = null;
    }

    @Test
    public void testMain() {
        Memory mem = new Memory();
        assertEquals(Utils.doubleToString(toMegaByte(mem.getInitial()), 1) + "MB" + " (" + mem.getInitial() + ")", "Initial memory: 0.0MB (0)");
        assertEquals(Utils.doubleToString(toMegaByte(mem.getMax()), 1) + "MB" + " (" + mem.getMax() + ")", "Max memory: 1.7GB (1421274240)");
    }
}
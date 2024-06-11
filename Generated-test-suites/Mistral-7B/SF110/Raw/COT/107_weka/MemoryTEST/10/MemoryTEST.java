package weka.core.test;

import static org.junit.Assert.*;

import java.awt.Dialog;
import java.lang.reflect.Field;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemoryConstructorWithUseGUIFalse() {
        Memory memory = new Memory();
        assertEquals(false, memory.getUseGUI());
    }

    @Test
    public void testMemoryConstructorWithUseGUITrue() {
        Memory memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        Memory.m_Enabled = true;
        assertTrue(memory.isEnabled());
        Memory.m_Enabled = false;
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        Memory memoryWithGUI = new Memory(true);
        assertTrue(memoryWithGUI.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        Field m_Initial = null;
        try {
            m_Initial = Memory.class.getDeclaredField("m_Initial");
            m_Initial.setAccessible(true);
            long initialField = (long) m_Initial.get(memory);
            assertEquals(initial, initialField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        Field m_Total = null;
        try {
            m_Total = Memory.class.getDeclaredField("m_Total");
            m_Total.setAccessible(true);
            long totalField = (long) m_Total.get(memory);
            assertEquals(current, totalField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        Field m_Max = null;
        try {
            m_Max = Memory.class.getDeclaredField("m_Max");
            m_Max.setAccessible(true);
            long maxField = (long) m_Max.get(memory);
            assertEquals(max, maxField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testIsOutOfMemoryWithEnabledFalse() {
        Memory memory = new Memory();
        Memory.m_Enabled = false;
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemoryWithEnabledTrue() {
        Memory memory = new Memory();
        Memory.m_Enabled = true;
        long current = memory.getCurrent();
        long max = memory.getMax();
        long initial = memory.getInitial();
        long threshold = initial + 200000L;
        Memory.m_Initial = initial;
        Memory.m_Total = current;
        Memory.m_Max = max;
        assertTrue(memory.isOutOfMemory());
        Memory.m_Total = current + 100000L; // decreasing the memory usage should make it false
        assertFalse(memory.isOutOfMemory());
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryWithUseGUIFalse() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
    }

    @Test
    public void testShowOutOfMemoryWithUseGUITrue() {
        Memory memory = new Memory(true);
        memory.showOutOfMemory();
        // The JOptionPane.showMessageDialog() should be called and not throw any exception
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRevisionWithNullArgs() {
        Memory memory = new Memory();
        memory.getRevision(null);
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        Dialog dialog = new Dialog(new Frame(), "Frame", Dialog.ModalType.MESSAGE);
        memory.showOutOfMemory();
        dialog.dispose();
        // The System.out.println() should be called and print the expected messages
    }
}
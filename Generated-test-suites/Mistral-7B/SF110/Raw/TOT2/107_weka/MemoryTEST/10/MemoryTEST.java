import static org.junit.jupiter.api.Assertions.*;
import static weka.core.Utils.doubleToString;

import org.junit.Test;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertEquals(false, memory.m_UseGUI);
        Memory memoryWithGUI = new Memory(true);
        assertEquals(true, memoryWithGUI.m_UseGUI);
    }

    // ... Test cases for other methods follow here ...

    // Edge cases
    @Test
    public void testMemoryManipulation_EdgeCase1() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.m_Total += Long.MAX_VALUE;
        memory.m_Max += Long.MAX_VALUE;
        Memory.m_Initial += Long.MAX_VALUE;
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testMemoryManipulation_EdgeCase2() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.m_Total -= Long.MAX_VALUE;
        memory.m_Max -= Long.MAX_VALUE;
        Memory.m_Initial -= Long.MAX_VALUE;
        assertFalse(memory.isOutOfMemory());
    }

    // Exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testMemoryConstructorWithNegativeBoolean() {
        assertThrows(IllegalArgumentException.class, () -> new Memory(-1));
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemory_NullMessageDialog() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.m_Total += Memory.m_Max * 0.99; // 99% memory usage
        JOptionPane.showMessageDialog(null, null, "OutOfMemory", 2);
        memory.showOutOfMemory();
    }

    @Test(expected = NullPointerException.class)
    public void testStopThreads_NullThreadGroup() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.m_Total += Memory.m_Max * 0.99; // 99% memory usage
        Thread[] thGroup = null;
        memory.stopThreads();
    }

    // ... Test cases for other methods follow here ...

    @Test
    public void testMain() {
        Memory memory = new Memory();
        System.out.println("Initial memory: " + doubleToString(toMegaByte(memory.getInitial()), 1) + "MB" + " (" + memory.getInitial() + ")");
        System.out.println("Max memory: " + doubleToString(toMegaByte(memory.getMax()), 1) + "MB" + " (" + memory.getMax() + ")");
    }

    static {
        Memory.m_Enabled = true;
        Memory.m_Initial = Runtime.getRuntime().totalMemory();
    }
}
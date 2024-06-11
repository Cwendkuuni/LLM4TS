package weka.core;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

    Memory memory;
    long initialMemory;

    @Before
    public void setUp() {
        memory = new Memory();
        initialMemory = memory.getInitial();
    }

    @Test
    public void testMemoryConstructor() {
        Memory memoryWithGUI = new Memory(true);
        assertEquals(true, memoryWithGUI.getUseGUI());
    }

    @Test
    public void testMemoryConstructorDefault() {
        Memory memory = new Memory();
        assertEquals(false, memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
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
        assertEquals(initialMemory, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        memory.getCurrent(); // trigger a new memory measurement
        assertEquals(totalMemory, memory.getTotal());
    }

    @Test
    public void testGetMax() {
        assertEquals(memory.m_Max, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());

        long maxMemory = memory.m_Max;
        long currentMemory = memory.getTotal();
        long initialMemory = memory.getInitial();
        memory.setEnabled(true);

        // set the memory usage to a level where the method returns true
        long threshold = maxMemory - initialMemory - 200000L;
        long remainingMemory = currentMemory + threshold;
        Field totalMemoryField = null;
        try {
            totalMemoryField = Runtime.class.getDeclaredField("totalMemory");
            totalMemoryField.setAccessible(true);
            totalMemoryField.setLong(Runtime.getRuntime(), remainingMemory);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(2.5, Memory.toMegaByte(2560000), 0.001);
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryNull() {
        Memory memory = null;
        memory.showOutOfMemory();
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();

        // set up a fake GUI for testing
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(WindowEvent.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(300, 200));
        frame.setVisible(true);

        memory.setEnabled(true);
        memory.showOutOfMemory();

        // close the frame to allow the JOptionPane to close
        frame.dispose();
    }

    @Test
    public void testStopThreads() {
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        String revision = memory.getRevision();
        assertTrue(revision.startsWith("$Revision: "));
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        String output = "";
        String[] args = {"arg1", "arg2"};
        try {
            Method mainMethod = Memory.class.getDeclaredMethod("main", String[].class);
            mainMethod.setAccessible(true);
            mainMethod.invoke(null, (Object) args);
            OutputStream outputStream = System.out;
            byte[] bytes = new byte[1024];
            int len = outputStream.read(bytes, 0, bytes.length);
            while (len > 0) {
                output += new String(bytes, 0, len);
                len = outputStream.read(bytes, 0, bytes.length);
            }
            System.out.println("Output: " + output);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        Memory memory = new Memory();
        memory.setEnabled(false);
    }
}
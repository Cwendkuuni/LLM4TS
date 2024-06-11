package weka.core.test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import weka.core.Memory;

@RunWith(JUnit4.class)
public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertNotNull(memory);
        Memory memoryWithGUI = new Memory(true);
        assertNotNull(memoryWithGUI);
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        Memory memoryWithEnabled = new Memory();
        memoryWithEnabled.setEnabled(true);
        assertTrue(memoryWithEnabled.isEnabled());
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
        assertNotNull(initial);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        assertNotNull(current);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        assertNotNull(max);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        long max = memory.getMax();
        long current = max / 2;
        assertFalse(memory.isOutOfMemory());

        memory.setEnabled(true);
        memory.setCurrent(current);
        assertTrue(memory.isOutOfMemory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToMegaByteWithNegativeBytes() {
        Memory.toMegaByte(-1L);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.showOutOfMemory();
    }

    @Test(expected = SecurityException.class)
    public void testStopThreads() throws SecurityException, IllegalThreadStateException {
        Memory memory = new Memory();
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();
        assertNotNull(revision);
    }

    @Test
    public void testMain() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Memory memory = new Memory();
        memory.getClass().getMethod("main", new Class[0]).invoke(memory, new Object[0]);
        String initialMemoryMsg = "Initial memory: ";
        String maxMemoryMsg = "Max memory: ";
        String output = getOutputFromSystemOut();
        assertTrue(output.contains(initialMemoryMsg));
        assertTrue(output.contains(maxMemoryMsg));
    }

    private String getOutputFromSystemOut() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream origOut = System.out;
        System.setOut(new PrintStream(outContent));
        new Memory().main(new String[0]);
        System.setOut(origOut);
        return outContent.toString();
    }
}
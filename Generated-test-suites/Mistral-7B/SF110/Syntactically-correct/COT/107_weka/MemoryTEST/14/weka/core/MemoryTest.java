package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        Memory memoryWithGUI = new Memory(true);

        assertEquals(false, memory.getUseGUI());
        assertEquals(true, memoryWithGUI.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        Memory memoryWithEnabled = new Memory();
        memoryWithEnabled.setEnabled(true);

        assertFalse(memory.isEnabled());
        assertTrue(memoryWithEnabled.isEnabled());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();

        assertNotEquals(-1, initial);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();

        assertNotEquals(-1, current);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();

        assertNotEquals(-1, max);
    }

    @Test(expected = RuntimeException.class)
    public void testIsOutOfMemoryWithEnabledFalse() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        memory.isOutOfMemory();
    }

    @Test
    public void testIsOutOfMemoryWithEnabledTrue() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.setMax(10L);
        memory.setCurrent(5L);
        memory.setInitial(3L);

        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        long bytes = 1024L * 1024L * 2; // 2 MB
        double result = memory.toMegaByte(bytes);

        assertEquals(2.0, result, 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryNullGUI() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.showOutOfMemory(null);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);

        String msg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   1.94MB (1073741888)\n- total memory used: 1.94MB (1073741888)\n- max. memory avail.: 1.94MB (1073741888)\n\nNote:\nThe Java heap size can be specified with the -Xmx option.\nE.g., to use 128MB as heap size, the command line looks like this:\n   java -Xmx128m -classpath ...\nThis does NOT work in the SimpleCLI, the java command refers\nto the one with which Weka is started.";
        memory.showOutOfMemory();
        assertEquals(msg, System.err.getError().getMessage());
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();

        Thread thread1 = new Thread(() -> {});
        Thread thread2 = new Thread(() -> {});
        Thread thread3 = new Thread(() -> {});

        memory.stopThreads();

        assertFalse(thread1.isAlive());
        assertFalse(thread2.isAlive());
        assertFalse(thread3.isAlive());
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();

        assertNotNull(revision);
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        memory.main(new String[0]);

        String initialMessage = "Initial memory: ";
        String maxMessage = "Max memory: ";

        assertTrue(memory.getClass().getName().contains("Memory"));
        assertTrue(memory.getClass().getSuperclass().getName().contains("RevisionHandler"));

        String output = System.out.getError().toString();
        assertTrue(output.contains(initialMessage));
        assertTrue(output.contains(maxMessage));
    }

    @Test(expected = LinkageError.class)
    public void testStaticInitializerWithNoGUI() {
        Memory memory = new Memory();
        memory.setEnabled(false);
    }

    @Test
    public void testStaticInitializerWithGUI() {
        Memory memory = new Memory();
    }
}
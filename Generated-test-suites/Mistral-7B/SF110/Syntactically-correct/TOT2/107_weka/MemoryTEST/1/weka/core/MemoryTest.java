package weka.core.test;

import org.junit.*;
import static org.junit.Assert.*;
import weka.core.Memory;

public class MemoryTest {

    @BeforeClass
    public static void setupClass() {
        Memory.m_Enabled = true;
        Memory.m_Initial = Runtime.getRuntime().totalMemory();
    }

    @AfterClass
    public static void teardownClass() {
        Memory.m_Enabled = false;
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
        Memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        Memory memory2 = new Memory(false);
        assertFalse(memory2.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        long totalInitial = Runtime.getRuntime().totalMemory();
        assertEquals(initial, totalInitial);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        Runtime runtime = Runtime.getRuntime();
        assertEquals(current, runtime.totalMemory());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        long totalMemory = Runtime.getRuntime().totalMemory();
        Memory memory2 = new Memory();
        memory2.setMax(totalMemory);
        assertEquals(max, totalMemory);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        Memory.setEnabled(false);
        memory = new Memory();
        long maxMemory = memory.getMax();
        long totalMemory = memory.getCurrent() + memory.getInitial();
        long exceededMemory = totalMemory + 200001L;
        Memory memory2 = new Memory();
        memory2.setInitial(exceededMemory);
        memory2.setEnabled(true);
        assertTrue(memory2.isOutOfMemory());
    }

    @Test(expected = NullPointerException.class)
    public void testShowOutOfMemoryWithNull() {
        Memory memory = new Memory();
        memory.showOutOfMemory(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShowOutOfMemoryWithNegative() {
        Memory memory = new Memory();
        memory.showOutOfMemory(-1L);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        long totalMemory = memory.getCurrent() + initial;
        Memory.setEnabled(true);
        memory = new Memory();
        String expectedMessage = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   1.0MB (totalInitial)\n- total memory used:  " + Utils.doubleToString(toMegaByte(totalMemory), 1) + "MB\n- max. memory avail.:  " + Utils.doubleToString(toMegaByte(memory.getMax()), 1) + "MB\n\nNote:\nThe Java heap size can be specified with the -Xmx option.\nE.g., to use 128MB as heap size, the command line looks like this:\n   java -Xmx128m -classpath ...\nThis does NOT work in the SimpleCLI, the java command refers\nto the one with which Weka is started.";
        memory.showOutOfMemory();
        String actualMessage = Utils.getConsoleOutput();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testStopThreads() {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {});
            threads[i].start();
        }
        Memory memory = new Memory();
        memory.stopThreads();
        for (Thread thread : threads) {
            assertTrue(thread.isInterrupted());
        }
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String expectedRevision = "8034";
        String actualRevision = memory.getRevision();
        assertEquals(expectedRevision, actualRevision);
    }
}
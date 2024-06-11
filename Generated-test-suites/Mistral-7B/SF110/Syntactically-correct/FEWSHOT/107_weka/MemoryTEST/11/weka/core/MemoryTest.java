package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.JOptionPane;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import weka.core.Memory;

@RunWith(Parameterized.class)
public class MemoryTest {

    private boolean useGUI;
    private Memory memory;

    public MemoryTest(boolean useGUI) {
        this.useGUI = useGUI;
    }

    @Parameters
    public static Object[] data() {
        return new Object[][] {
                {false},
                {true}
        };
    }

    @Test
    public void testMemoryInstance() {
        memory = new Memory(useGUI);
        assertNotNull(memory);
        assertEquals(useGUI, memory.getUseGUI());
    }

    @Test
    public void testMemoryIsEnabled() {
        Memory.setEnabled(true);
        memory = new Memory();
        assertTrue(memory.isEnabled());
        Memory.setEnabled(false);
        memory = new Memory();
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testMemoryInitial() {
        memory = new Memory();
        long initial = memory.getInitial();
        long total = memory.getCurrent();
        assertEquals(initial, total);
    }

    @Test
    public void testMemoryMax() {
        memory = new Memory();
        long max = memory.getMax();
        assertTrue(max > 0);
    }

    @Test
    public void testMemoryCurrent() {
        memory = new Memory();
        long current = memory.getCurrent();
        long max = memory.getMax();
        assertTrue(current <= max);
    }

    @Test
    public void testMemoryOutOfMemory() {
        Memory.setEnabled(true);
        Runtime runtime = Runtime.getRuntime();
        long max = memory.getMax();
        long total = max - 1000000;
        memory = new Memory();
        assertTrue(memory.isOutOfMemory());
        memory.setEnabled(false);
        memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        memory = new Memory(useGUI);
        Runtime newRuntime = Runtime.getRuntime();
        total += newRuntime.totalMemory() - runtime.totalMemory();
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testMemoryShowOutOfMemory() {
        Memory.setEnabled(true);
        memory = new Memory(useGUI);
        memory.showOutOfMemory();
        if (useGUI) {
            assertTrue(JOptionPane.showOptionDialog(null, null, "OutOfMemory", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION);
        }
    }

    @Test
    public void testMemoryStopThreads() {
        memory = new Memory();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    Thread.sleep(new Random().nextInt(1000) + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        memory.stopThreads();
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testMemoryGetRevision() {
        memory = new Memory();
        String revision = memory.getRevision();
        assertNotNull(revision);
    }

    @Test
    public void testMemoryMain() {
        Memory.main(new String[0]);
    }
}
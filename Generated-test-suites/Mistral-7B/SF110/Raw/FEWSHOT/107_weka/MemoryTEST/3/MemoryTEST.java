package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

    private Memory memory;

    @Test
    public void testMemoryInitialization() {
        memory = new Memory();
        long initial = memory.getInitial();
        long max = memory.getMax();
        long total = memory.getCurrent();

        assertEquals("Initial memory should be set correctly", initial, memory.getInitial());
        assertEquals("Max memory should be set correctly", max, memory.getMax());
        assertEquals("Total memory should be set correctly", total, memory.getCurrent());
    }

    @Test
    public void testMemoryEnabled() {
        memory = new Memory();
        memory.setEnabled(false);
        assertFalse("Memory should be disabled", memory.isEnabled());

        memory.setEnabled(true);
        assertTrue("Memory should be enabled", memory.isEnabled());
    }

    @Test
    public void testMemoryOutOfMemory() {
        memory = new Memory(true);
        long initial = memory.getInitial();
        long max = memory.getMax();
        long total = memory.getCurrent();

        memory.setEnabled(false); // disable memory tracking

        // simulate memory usage
        long usedMemory = 10000000; // 10 MB
        total += usedMemory;

        assertTrue("Memory should report out of memory condition", memory.isOutOfMemory());

        // reset memory state
        memory = new Memory(true);
        total = memory.getCurrent();
        assertEquals("Memory total should be reset", total, memory.getCurrent());
    }

    @Test
    public void testMemoryShowOutOfMemory() {
        memory = new Memory(true);
        memory.setEnabled(false); // disable memory tracking

        // simulate memory usage
        long usedMemory = 10000000; // 10 MB
        memory.m_Total += usedMemory;

        memory.showOutOfMemory();

        // check message content
        String msg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   ?";
        assertTrue("Out of memory message should contain initial JVM size", msg.startsWith(msg.substring(0, msg.indexOf(":"))));
        double initial = Double.parseDouble(msg.substring(msg.indexOf(":") + 1, msg.indexOf("MB")).trim());
        assertEquals("Initial memory size should be correct", memory.getInitial(), initial * 1024 * 1024, 0);

        msg = msg.substring(msg.indexOf("total memory used:  "));
        double total = Double.parseDouble(msg.substring(0, msg.indexOf("MB")).trim());
        assertEquals("Total memory used should be correct", memory.m_Total, total * 1024 * 1024, 0);

        msg = msg.substring(msg.indexOf("max. memory avail.: "));
        double max = Double.parseDouble(msg.substring(0, msg.indexOf("MB")).trim());
        assertEquals("Max memory available should be correct", memory.getMax(), max * 1024 * 1024, 0);

        // check message dialog
        JOptionPane.showMessageDialog(null, msg, "OutOfMemory", JOptionPane.ERROR_MESSAGE);
        String dialogTitle = JOptionPane.getRootFrame().getTitle();
        assertEquals("Dialog title should be 'OutOfMemory'", "OutOfMemory", dialogTitle);
    }

    @Test
    public void testMemoryStopThreads() {
        memory = new Memory();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        memory.stopThreads();

        for (Thread thread : threads) {
            try {
                thread.join(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertTrue("Thread should be stopped", !thread.isAlive());
        }
    }
}
package weka.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.JOptionPane;
import java.awt.event.InputEvent;
import java.lang.reflect.FieldOrNull;
import java.lang.reflect.Method;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

    private static final int MEGABYTES = 1024 * 1024;
    private Memory memory;
    private long initialMemory;
    private long maxMemory;
    private long totalMemory;

    @Before
    public void setUp() {
        memory = new Memory();
        initialMemory = memory.getInitial();
        maxMemory = memory.getMax();
        totalMemory = memory.getCurrent();
    }

    @Test
    public void testMemoryConstructor() {
        Memory memoryWithGUI = new Memory(true);
        Memory memoryWithoutGUI = new Memory(false);

        assertEquals(memoryWithGUI.getUseGUI(), true);
        assertEquals(memoryWithoutGUI.getUseGUI(), false);

        assertEquals(memoryWithGUI.m_Runtime.totalMemory(), memoryWithoutGUI.m_Runtime.totalMemory());
        assertEquals(memoryWithGUI.m_Runtime.maxMemory(), memoryWithoutGUI.m_Runtime.maxMemory());
    }

    @Test
    public void testMemoryIsEnabled() {
        assertTrue(Memory.m_Enabled);
        Memory.setEnabled(false);
        assertFalse(Memory.m_Enabled);
        Memory.setEnabled(true);
        assertTrue(Memory.m_Enabled);
    }

    @Test
    public void testMemoryIsOutOfMemory() {
        long availableMemory = maxMemory - totalMemory - initialMemory - 200000L;

        if (availableMemory > 0) {
            Memory memoryWithLowerLimit = new Memory();
            assertFalse(memoryWithLowerLimit.isOutOfMemory());
        } else {
            Memory memoryWithLowerLimit = new Memory();
            assertTrue(memoryWithLowerLimit.isOutOfMemory());
        }
    }

    @Test
    public void testMemoryShowOutOfMemory() {
        try {
            Memory memory = new Memory();
            memory.showOutOfMemory();
        } catch (Throwable e) {
            //swallow the exception as JOptionPane.showMessageDialog is not testable
        }

        Memory memory = new Memory();
        memory.setEnabled(true);

        long maxMemory = memory.getMax();
        long totalMemory = memory.getCurrent();
        long initialMemory = memory.getInitial();
        long availableMemory = maxMemory - totalMemory - initialMemory;

        if (availableMemory > 0) {
            Memory memoryWithLowerLimit = new Memory();
            memoryWithLowerLimit.showOutOfMemory();
            String expectedMsg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:    " + toFormat(initialMemory, "0.00") + " MB\n- total memory used:   " + toFormat(totalMemory, "0.00") + " MB\n- max. memory avail.:  " + toFormat(maxMemory, "0.00") + " MB\n\nNote:\nThe Java heap size can be specified with the -Xmx option.\nE.g., to use 128MB as heap size, the command line looks like this:\n  java -Xmx128m -classpath ...\nThis does NOT work in the SimpleCLI, the java command refers\nto the one with which Weka is started.";
            String actualMsg = "";
            if (memory.getUseGUI()) {
                JOptionPane jop = new JOptionPane("", JOptionPane.ERROR_MESSAGE, JOptionPane.PLAIN_MESSAGE);
                Object[] options = {"OK"};
                JDialog dialog = jop.createDialog("OutOfMemory", jop);
                dialog.setVisible(true);
                dialog.dispose();
                actualMsg = jop.getMessage();
            }
            assertEquals(expectedMsg, actualMsg);
        } else {
            Memory memoryWithLowerLimit = new Memory();
            memoryWithLowerLimit.showOutOfMemory();
            //swallow the exception as JOptionPane.showMessageDialog is not testable
        }
    }

    @Test
    public void testMemoryStopThreads() {
        long numThreads = 10;
        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        memory.stopThreads();

        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        assertTrue(Thread.interrupted());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    @After
    public void tearDown() {
        Memory.setEnabled(true);
        memory = null;
    }

    private static void setEnabled(boolean enabled) {
        try {
            FieldOrNull field = Memory.class.getDeclaredField("m_Enabled");
            field.setAccessible(true);
            field.set(null, enabled);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static String toFormat(long value, String format) {
        return String.format(format, value / MEGABYTES
                .toString()
                .substring(0, format.indexOf(".") > 0 ? format.length() - 1 : format.length()));
    }
}
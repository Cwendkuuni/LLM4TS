package weka.core.test;

import static org.junit.Assert.*;

import java.awt.HeadlessException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

    private static Memory memory;

    @BeforeClass
    public static void setup() {
        memory = new Memory();
    }

    // Test constructor with default useGUI
    @Test
    public void testMemoryDefaultConstructor() {
        Memory instance = new Memory();
        assertEquals(false, instance.getUseGUI());
    }

    // Test constructor with useGUI
    @Test
    public void testMemoryConstructorWithUseGUI() {
        Memory instance = new Memory(true);
        assertEquals(true, instance.getUseGUI());
    }

    // Test getters

    @Test
    public void testIsEnabled() {
        Memory instance = memory;
        assertTrue(instance.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory instance = memory;
        assertEquals(false, instance.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory instance = memory;
        long initial = instance.getInitial();
        assertNotNull(initial);
    }

    @Test
    public void testGetCurrent() {
        Memory instance = memory;
        long current = instance.getCurrent();
        assertNotNull(current);
    }

    @Test
    public void testGetMax() {
        Memory instance = memory;
        long max = instance.getMax();
        assertNotNull(max);
    }

    @Test(expected = IllegalStateException.class)
    public void testIsOutOfMemoryNegativeInitial() {
        Memory instance = memory;
        instance.setInitial(-1);
        instance.isOutOfMemory();
    }

    @Test
    public void testIsOutOfMemory() {
        Memory instance = memory;
        instance.setEnabled(true);
        long max = instance.getMax();
        long current = instance.getCurrent();
        long initial = instance.getInitial();
        long memoryFree = max - current;

        if (memoryFree < initial + 200000L) {
            instance.isOutOfMemory();
        } else {
            assertFalse(instance.isOutOfMemory());
        }
    }

    // Test toMegaByte method
    @Test
    public void testToMegaByte() {
        long bytes = 123456789;
        double expected = 115.10110110110111;
        double result = Memory.toMegaByte(bytes);
        assertEquals(expected, result, 0.0);
    }

    // Test showOutOfMemory method
    @Test
    public void testShowOutOfMemory() {
        final boolean isEnabled = true;
        Memory instance = new Memory();
        instance.setEnabled(isEnabled);

        try {
            instance.showOutOfMemory();
        } catch (HeadlessException e) {
            fail("Expected showOutOfMemory method to not throw HeadlessException");
        }

        if (isEnabled) {
            assertTrue(instance.isEnabled());
            assertEquals(Memory.m_Enabled, true);
        }

        instance.setEnabled(false);
        instance.showOutOfMemory();
    }

    // Test stopThreads method
    @Test
    public void testStopThreads() {
        Memory instance = memory;
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {});
            threads[i].start();
        }

        instance.stopThreads();

        for (Thread thread : threads) {
            try {
                thread.join(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                fail("Threads should not be interrupted");
            }

            assertNotSame(Thread.State.TERMINATED, thread.getState());
        }
    }

    // Test getRevision method
    @Test
    public void testGetRevision() {
        Memory instance = memory;
        String expected = "8034";
        String result = instance.getRevision();
        assertEquals(expected, result);
    }

    // Test main method
    @Test
    public void testMain() {
        Memory instance = new Memory();
        try {
            Memory.main(new String[0]);
        } catch (SecurityException e) {
            fail("Expected main method to not throw SecurityException");
        } catch (UnsupportedOperationException e) {
            fail("Expected main method to not throw UnsupportedOperationException");
        }

        String expected = "Initial memory: ?";
        String result = System.out.toString();
        assertTrue(result.contains(expected));

        expected = "Max memory: ?";
        result = System.out.toString();
        assertTrue(result.contains(expected));
    }

    // Test static initialization
    @Test
    public void testStaticInitialization() {
        Memory instance = new Memory();
        long initial = instance.getInitial();
        long m_Initial = Memory.m_Initial;
        assertEquals(initial, m_Initial);
    }

    // Test exception handling in isOutOfMemory
    @Test(expected = UndeclaredThrowableException.class)
    public void testIsOutOfMemoryWithIllegalArgumentException() {
        Memory instance = memory;
        instance.setInitial(-1);
        instance.isOutOfMemory();
    }
}
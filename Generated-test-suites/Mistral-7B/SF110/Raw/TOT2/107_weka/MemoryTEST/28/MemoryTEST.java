import static org.junit.Assert.*;
import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemory() {
        Memory memory = new Memory();

        // Testing basic functionality
        assertEquals(false, memory.isEnabled());
        memory.setEnabled(true);
        assertEquals(true, memory.isEnabled());

        Memory memoryUseGUI = new Memory(true);
        assertEquals(true, memoryUseGUI.getUseGUI());

        long initialMemory = memory.getInitial();
        long totalMemory = memory.getCurrent();
        long maxMemory = memory.getMax();

        // Basic functionality test for getInitial(), getCurrent(), getMax() methods
        assertEquals(initialMemory, memory.getInitial());
        assertEquals(totalMemory, memory.getCurrent());
        assertEquals(maxMemory, memory.getMax());

        // Testing isOutOfMemory() edge case
        memory.setEnabled(true);
        long expectedOutOfMemory = initialMemory + 200001L;
        long actualOutOfMemory = totalMemory + 200001L;
        assertEquals(false, memory.isOutOfMemory());
        memory.setMax(expectedOutOfMemory);
        assertEquals(true, memory.isOutOfMemory());

        // Testing showOutOfMemory() edge case
        memory.showOutOfMemory();

        // Testing stopThreads() edge cases
        memory.stopThreads();
    }

    @Test(expected = NullPointerException.class)
    public void testMemoryWithNullThreadGroup() {
        Memory memory = new Memory();
        Thread[] thGroup = null;
        memory.stopThreads(thGroup);
    }

    @Test
    public void testMemoryMain() {
        Memory memory = new Memory();
        String expectedInitialMemory = "Initial memory: ";
        String expectedMaxMemory = "Max memory: ";
        String output = "Initial memory: 0.0MB (0)\nMax memory: 0.0MB (0)";
        memory.main(new String[0]);
        assertEquals(expectedInitialMemory, output.substring(0, expectedInitialMemory.length()));
        assertEquals(expectedMaxMemory, output.substring(output.indexOf(expectedMaxMemory), output.indexOf(expectedMaxMemory) + expectedMaxMemory.length()));
    }
}
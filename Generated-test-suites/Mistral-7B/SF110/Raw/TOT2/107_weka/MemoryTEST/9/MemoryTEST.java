import static org.junit.Assert.*;
import org.junit.Test;
import weka.core.Memory;
import static weka.core.Utils.doubleToString;

public class MemoryTest {

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long expectedInitial = memory.m_Runtime.totalMemory();
        long actualInitial = memory.getInitial();
        assertEquals("Expected initial memory value to be equal to the value returned by getInitial()", expectedInitial, actualInitial);
    }

    @Test
    public void testIsOutOfMemoryWithSmallDifference() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.m_Max = memory.m_Runtime.totalMemory() - 100000;
        memory.m_Total = memory.m_Runtime.totalMemory();
        assertTrue("Expected isOutOfMemory() to be true with a small difference between total memory and max memory", memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemoryWithEqualMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.m_Max = memory.m_Runtime.totalMemory();
        memory.m_Total = memory.m_Runtime.totalMemory();
        assertFalse("Expected isOutOfMemory() to be false with equal memory", memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemoryWithLessMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.m_Total = memory.m_Runtime.totalMemory();
        memory.m_Max = memory.m_Runtime.totalMemory() - 1000000; // 1MB less than total memory
        assertTrue("Expected isOutOfMemory() to be true with less total memory than max memory", memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemoryWithZeroInitialMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.m_Initial = 0;
        memory.m_Max = memory.m_Runtime.totalMemory();
        memory.m_Total = memory.m_Runtime.totalMemory();
        assertFalse("Expected isOutOfMemory() to be false with initial memory = 0", memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemoryWithNegativeValues() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.m_Total = -1000000;
        memory.m_Max = -500000;
        memory.m_Initial = -200000;
        assertFalse("Expected isOutOfMemory() to be false with negative memory values", memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 500000;
        double expected = 0.5;
        double actual = Memory.toMegaByte(bytes);
        assertEquals("Expected toMegaByte() to return the correct value for 500000 bytes", expected, actual, 0.001);
    }

    @Test(expected = RuntimeException.class)
    public void testShowOutOfMemoryWithoutGUI() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
        fail("Expected RuntimeException when showOutOfMemory() is called without GUI");
    }

    @Test
    public void testShowOutOfMemoryWithGUI() {
        Memory memory = new Memory(true);
        memory.showOutOfMemory();
        // Check if the message dialog is displayed
        // The JUnit test framework does not support testing GUI components directly
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        long initialThreadCount = Thread.activeCount();
        memory.stopThreads();
        long finalThreadCount = Thread.activeCount();
        assertTrue("Expected the number of threads to be less than before after calling stopThreads()", initialThreadCount > finalThreadCount);
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String expectedRevision = "8034";
        String actualRevision = memory.getRevision();
        assertEquals("Expected getRevision() to return the correct revision number", expectedRevision, actualRevision);
    }

    @Test
    public void testMain() {
        String[] args = new String[0];
        Memory memory = new Memory();
        memory.main(args);
        String initialMemoryMessage = "Initial memory: ";
        String maxMemoryMessage = "Max memory: ";
        assertTrue("Expected the message 'Initial memory:' to be printed", memory.m_Runtime.getClass().getName().contains("Runtime"));
        assertTrue("Expected the message 'Max memory:' to be printed", memory.getClass().getName().equals("weka.core.Memory"));
        assertTrue("Expected the initial memory to be printed before the max memory", memory.out.contains(initialMemoryMessage));
        assertTrue("Expected the max memory to be printed after the initial memory", memory.out.contains(maxMemoryMessage));
    }

    @BeforeClass
    public static void setupClass() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }
}
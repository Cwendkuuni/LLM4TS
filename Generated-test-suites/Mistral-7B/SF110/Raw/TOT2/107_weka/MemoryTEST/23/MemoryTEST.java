import static org.junit.Assert.*;
import weka.core.Memory;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        Memory memoryWithGUI = new Memory(true);
        // Test constructors here
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        Memory memoryWithGUI = new Memory(true);
        assertEquals(false, memory.getUseGUI());
        assertEquals(true, memoryWithGUI.getUseGUI());
    }

    // Test other methods with similar test cases
}
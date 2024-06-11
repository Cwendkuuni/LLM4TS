import static org.junit.Assert.*;
import weka.core.Memory;

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

        Memory memoryEnabled = new Memory(true);
        assertTrue(memoryEnabled.isEnabled());
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

    // Other test cases for getCurrent(), getMax(), isOutOfMemory(), toMegaByte(), and getRevision() methods can be added here.
}
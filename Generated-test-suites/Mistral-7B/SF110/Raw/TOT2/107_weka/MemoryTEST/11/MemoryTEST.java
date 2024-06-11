import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        Memory.setEnabled(true);
        memory = new Memory();
        assertTrue(memory.isEnabled());
    }
}
import static org.junit.Assert.*;
import weka.core.Memory;

public class MemoryTest {
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
    }
}
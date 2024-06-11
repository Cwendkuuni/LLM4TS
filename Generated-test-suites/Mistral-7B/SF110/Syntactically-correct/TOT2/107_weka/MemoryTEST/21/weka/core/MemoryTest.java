import static org.junit.Assert.*;

import weka.core.Memory;

public class MemoryTest {
    @Test
    public void testMemoryConstructorWithoutUseGUI() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        long max = memory.getMax();
        assertEquals(initial, Runtime.getRuntime().totalMemory());
        assertEquals(max, Runtime.getRuntime().maxMemory());
    }
}
package weka.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MemoryTest {
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        boolean expected = Memory.m_Enabled;
        boolean actual = memory.isEnabled();
        assertEquals(expected, actual);
    }
}
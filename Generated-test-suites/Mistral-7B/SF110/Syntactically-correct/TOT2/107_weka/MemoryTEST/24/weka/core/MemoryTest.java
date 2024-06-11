import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        boolean expected = true; // default value
        boolean actual = memory.isEnabled();
        assertEquals(expected, actual);

        memory.setEnabled(false);
        expected = false;
        actual = memory.isEnabled();
        assertEquals(expected, actual);
    }
}
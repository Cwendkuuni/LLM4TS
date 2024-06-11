package weka.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        Memory memory2 = new Memory();
        long initial2 = memory2.getInitial();
        assertEquals(initial, initial2);
    }
}
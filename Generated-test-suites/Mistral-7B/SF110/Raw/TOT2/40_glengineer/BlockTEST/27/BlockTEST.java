import static org.junit.Assert.*;
import glengineer.blocks.Block;
import glengineer.positions.CharPosition;

public class BlockTest {
    @Test
    public void testCharAt() {
        Block block = new Block(new Scheme(10, 10), 1, 1, 3, 3);
        assertEquals('A', block.charAt(1, 1));
        assertEquals(' ', block.charAt(0, 0));
        assertEquals('Z', block.charAt(3, 3));
    }
}
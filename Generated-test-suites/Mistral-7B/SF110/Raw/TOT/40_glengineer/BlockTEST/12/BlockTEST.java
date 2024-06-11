import static org.junit.Assert.*;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;

public class BlockTest {
    // 
    @Test
    public void testBlockConstructionWithCharPosition() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(3, 4);
        CharPosition p2 = new CharPosition(6, 5);
        Block block = new Block(scheme, p1, p2);
        assertEquals(scheme, block.getScheme());
        assertEquals(p1.x, block.getX1());
        assertEquals(p1.y, block.getY1());
        assertEquals(p2.x, block.getX2());
        assertEquals(p2.y, block.getY2());
    }
    // 
}
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import glengineer.blocks.Block;
import glengineer.positions.CharPosition;
import glengineer.positions.Scheme;

public class BlockTest {

    @Test
    public void testBlockConstruction() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 0, 0, 5, 5);
        CharPosition expectedMin = new CharPosition(0, 0);
        CharPosition expectedMax = new CharPosition(5, 5);
        assertEquals(expectedMin, block.getMinPosition());
        assertEquals(expectedMax, block.getMaxPosition());
    }

    // Edge cases and exception handling scenarios:
    // - x1, y1, x2, y2 out of bounds of scheme
    // - x1, y1 equal to x2, y2
    // - scheme is null
}
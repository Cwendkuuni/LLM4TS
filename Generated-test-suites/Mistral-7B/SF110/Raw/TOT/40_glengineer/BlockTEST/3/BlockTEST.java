import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    // ...

    @Test
    public void testBlockConstruction() {
        Scheme scheme = new Scheme(new CharTable(5, 5));
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(scheme, block.getScheme());
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
    }
}
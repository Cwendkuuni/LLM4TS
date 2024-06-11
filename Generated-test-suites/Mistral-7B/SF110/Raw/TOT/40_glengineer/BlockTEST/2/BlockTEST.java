import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    @Test
    public void testBlockConstructorWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(2, 3);
        CharPosition p2 = new CharPosition(5, 6);

        Block block = new Block(scheme, p1, p2);
        int expectedX1 = p1.x;
        int expectedY1 = p1.y;
        int expectedX2 = p2.x;
        int expectedY2 = p2.y;

        assertEquals(expectedX1, block.x1);
        assertEquals(expectedY1, block.y1);
        assertEquals(expectedX2, block.x2);
        assertEquals(expectedY2, block.y2);
    }

    @Test
    public void testBlockConstructorWithScheme() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        int expectedX1 = 0;
        int expectedY1 = 0;
        int expectedX2 = scheme.x2;
        int expectedY2 = scheme.y2;

        assertEquals(expectedX1, block.x1);
        assertEquals(expectedY1, block.y1);
        assertEquals(expectedX2, block.x2);
        assertEquals(expectedY2, block.y2);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        CharTable text = new CharTable("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        Block block = new Block(scheme, text);

        assertTrue(block.containsOnlyOneWord());
    }

    // Add more test cases for other methods here
}
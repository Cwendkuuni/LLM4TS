import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    Scheme scheme;
    Block block;

    @BeforeEach
    void setUp() {
        scheme = new Scheme(10, 10);
    }

    @Test
    public void testBlockConstructionWithCharPosition() {
        // Test case for constructor with Scheme and CharPosition arguments
        CharPosition p1 = new CharPosition(5, 3);
        CharPosition p2 = new CharPosition(8, 3);
        block = new Block(scheme, p1, p2);

        // ... (Expert 1's test case)
    }

    @Test
    public void testBlockConstructionWithScheme() {
        // Test case for constructor with Scheme argument
        block = new Block(scheme);

        // ... (Expert 2's test case)
    }

    @Test
    public void testContainsOnlyOneWord() {
        // Test case for containsOnlyOneWord() method
        block = new Block(scheme, new CharPosition(1, 1), new CharPosition(4, 4));

        // ... (Expert 3's test case)
    }

    @Test
    public void testGetUpperLine() {
        String[] lines = {"Hello World", "Java is awesome", "Block Testing"};
        scheme.setLines(lines);
        block = new Block(scheme, new CharPosition(1, 1), new CharPosition(4, 4));
        assertEquals("Hello World", block.getUpperLine());
    }
}
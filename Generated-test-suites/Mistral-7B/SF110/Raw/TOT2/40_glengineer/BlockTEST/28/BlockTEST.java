import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;

class BlockTest {

    @Test
    public void testBlockConstructorWithValidArguments() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
        assertEquals(5, block.x2 - block.x1);
        assertEquals(5, block.y2 - block.y1);
        // Add other assertions here
    }

    @Test
    public void testBlockConstructorWithInvalidArguments() {
        Scheme scheme = new Scheme(10, 10);
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, -1, 0, 5, 5));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 10, 10, -1, 5));
        assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 10, 10, 10, 10)); // empty block
        // Add other assertions for improper arguments
    }

    @Test
    public void testBlockCharacterAccess() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(5, 0));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(0, 5));
        // Add other assertions for valid and invalid character access
    }

    @Test
    public void testBlockTextAccess() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
        assertThrows(IllegalArgumentException.class, () -> block.textAt(-1, 5, 0));
        assertThrows(IllegalArgumentException.class, () -> block.textAt(5, 5, 0));
        assertThrows(IllegalArgumentException.class, () -> block.textAt(0, 5, 5));
        // Add other assertions for valid and invalid text access
    }

    // Add test cases for other methods

    @Test
    public void testBlockToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
        String result = block.toString();
        // Add your assertions here
    }

    @Test
    public void testBlockContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
        // Add your assertions here
    }

    // Add test cases for other methods
}
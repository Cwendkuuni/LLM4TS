import org.junit.Test;
import org.junit.Assert.*;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

    @Test
    public void testBlockConstructorWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        try {
            new Block(scheme, -1, 0, 5, 5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,0,5,5.", e.getMessage());
        }

        try {
            new Block(scheme, 10, -1, 5, 5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 10,-1,5,5.", e.getMessage());
        }

        try {
            new Block(scheme, 5, 5, 10, 10);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 5,5,10,10.", e.getMessage());
        }

        try {
            new Block(scheme, 5, 5, 11, 5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 5,5,11,5.", e.getMessage());
        }

        try {
            new Block(scheme, 5, 5, 5, 11);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 5,5,5,11.", e.getMessage());
        }

        try {
            new Block(scheme, 11, 5, 5, 5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 11,5,5,5.", e.getMessage());
        }
    }

    @Test
    public void testBlockConstructorWithValidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, 1, 1, 5, 5);
    }

    @Test
    public void testBlockConstructorWithValidCharPosition() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
    }

    @Test
    public void testBlockConstructorWithValidScheme() {
        Scheme scheme = new Scheme(10, 10);
        new Block(scheme);
    }

    @Test
    public void testSubblockWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);

        try {
            block.subblock(-1, -1, 2, 2);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper subblock definition: -1,-1,2,2.", e.getMessage());
        }

        try {
            block.subblock(5, 5, -1, -1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper subblock definition: 5,5,-1,-1.", e.getMessage());
        }

        try {
            block.subblock(5, 5, 6, 6);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper subblock definition: 5,5,6,6.", e.getMessage());
        }

        try {
            block.subblock(5, 5, 5, 6);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper subblock definition: 5,5,5,6.", e.getMessage());
        }

        try {
            block.subblock(5, 5, 5, 5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper subblock definition: 5,5,5,5.", e.getMessage());
        }

        try {
            block.subblock(5, 5, 11, 5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper subblock definition: 5,5,11,5.", e.getMessage());
        }

        try {
            block.subblock(5, 5, 5, 11);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper subblock definition: 5,5,5,11.", e.getMessage());
        }
    }

    @Test
    public void testSubblockWithValidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(2, 2, 2, 2);
        assertNotNull(subblock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidPosition() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        block.charAt(6, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 5, 5);
        block.textAt(6, 6, 0);
    }

    @Test
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        try {
            block.containsOnlyOneWord();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot search in a trivial block", e.getMessage());
        }
    }

    @Test
    public void testContainsOnlyOneWordWithSingleWordBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        try {
            block.containsOnlyOneWord();
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertNotNull(subblocks);
        assertEquals(4, subblocks.size());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        List<Block> subblocks = block.extractParallelSubblocks();
        assertNotNull(subblocks);
        assertEquals(10, subblocks.size());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        String expected = "Block(1,1,5,5):\n#################\n # A B C D E #\n # F G H I J #\n # K L M N O #\n # P Q R S T #\n ###################\n";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        String expected = "A B C D E";
        assertEquals(expected, block.getUpperLine());
    }
}
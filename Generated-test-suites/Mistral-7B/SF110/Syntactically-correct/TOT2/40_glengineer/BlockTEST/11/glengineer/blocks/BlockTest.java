import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import glengineer.blocks.Block;
import glengineer.blocks.BlockFactory;
import glengineer.blocks.CharPosition;
import glengineer.blocks.HWordPosition;
import glengineer.blocks.Scheme;
import glengineer.blocks.WordPosition;
import glengineer.positions.PositionFactory;

public class BlockTest {

    @Test
    public void testConstructorWithPositionAndSize() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 1, 1, 3, 4);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(4, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    // Test other methods' JUnit 4 tests here...

}
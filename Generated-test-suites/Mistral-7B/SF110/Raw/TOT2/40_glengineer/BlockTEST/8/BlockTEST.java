package glengineer.blocks;

import org.junit.Test;
import static org.junit.Assert.*;

import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import glengineer.positions.Scheme;
import glengineer.positions.WordPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {
    

    @Test
    public void testBlockConstructionWithValidArguments() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        assertEquals(3, block.getX2() - block.getX1() + 1);
        assertEquals(3, block.getY2() - block.getY1() + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructionWithInvalidArguments() {
        Scheme scheme = new Scheme(5, 5);
        new Block(scheme, -1, 1, 3, 3);
    }

    @Test
    public void testSubblockConstruction() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        Block subBlock = block.subblock(1, 1, 1, 1);
        assertEquals(1, subBlock.getX2() - subBlock.getX1() + 1);
        assertEquals(1, subBlock.getY2() - subBlock.getY1() + 1);
    }

    @Test(expected = RuntimeException.class)
    public void testCharAtOutOfBounds() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.charAt(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 3, 3);
        block.textAt(4, 4, 4);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 4, 4);
        assertEquals(true, block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 4, 4);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(3, subblocks.size());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 4, 4);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(2, subblocks.size());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 4, 4);
        String expected = "Block(1,1,4,4):\n" +
                "# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # \n" +
                "# A B C D E F G H I J K L M N O P Q R S T U V W X Y Z AA AB AC AD AE AF AG AH AI AJ AK AL AM AN AO AP AQ AR AS AT AU AV AW AX AY AZ AA AB AC \n" +
                "# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # \n";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 4, 4);
        String expected = "A B C D E";
        assertEquals(expected, block.getUpperLine());
    }

    
}
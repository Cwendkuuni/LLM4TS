package glengineer.blocks;

import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import java.util.Arrays;
import java.util.List;

public class BlockTest {
@Test
public void testBlockConstructionWithCharPosition() {
Scheme scheme = new Scheme(10, 10);
CharPosition p1 = new CharPosition(2, 3);
CharPosition p2 = new CharPosition(5, 5);

Block block = new Block(scheme, p1, p2);

assertEquals(p1.x, block.x1);
assertEquals(p1.y, block.y1);
assertEquals(p2.x, block.x2);
assertEquals(p2.y, block.y2);
assertEquals(scheme, block.scheme);
assertTrue(block.x1 >= 0 && block.y1 >= 0 && block.x2 <= scheme.x2 && block.y2 <= scheme.y2);
}

@Test
public void testContainsOnlyOneWord() {
Scheme scheme = new Scheme(10, 5);
Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(9, 4));

String expectedText = "ABCDEFGHI";
String actualText = block.toString();

// Check that the block contains only one word
assertTrue(block.containsOnlyOneWord());

// Test that the word matches the expected text
assertEquals(expectedText, actualText);
}

@Test
public void testSubblockAndExtractSequentialSubblocks() {
Scheme scheme = new Scheme(20, 10);
Block parentBlock = new Block(scheme, new CharPosition(0, 0), new CharPosition(19, 9));

Block subblock1 = parentBlock.subblock(0, 0, 5, 5);
Block subblock2 = parentBlock.subblock(5, 5, 10, 10);

List<Block> expectedSubblocks = Arrays.asList(subblock1, subblock2);
List<Block> actualSubblocks = parentBlock.extractSequentialSubblocks();

// Check that the extracted subblocks match the expected ones
assertEquals(expectedSubblocks, actualSubblocks);
}
}
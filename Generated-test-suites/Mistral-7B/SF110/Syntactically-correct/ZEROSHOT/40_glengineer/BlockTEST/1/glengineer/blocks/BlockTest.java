import org.junit.Test;
import org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setUp() {
 // Initialize schema and block for testing
 scheme = new Scheme(5, 5);
 block = new Block(scheme, 0, 0, scheme.x2, scheme.y2);
 }

 @Test
 public void testBlockConstructionWithCoords() {
 Block testBlock = new Block(scheme, 2, 2, 4, 4);
 assertEquals("Block(2, 2, 4, 4)", testBlock.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructionWithImproperCoords() {
 Block testBlock = new Block(scheme, -1, -1, 5, 5);
 fail("IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testBlockConstructionWithPosition() {
 CharPosition p1 = new CharPosition(2, 2);
 CharPosition p2 = new CharPosition(4, 4);
 Block testBlock = new Block(scheme, p1, p2);
 assertEquals("Block(2, 2, 4, 4)", testBlock.toString());
 }

 @Test
 public void testBlockConstructionWithSchemeOnly() {
 Block testBlock = new Block(scheme);
 assertEquals("Block(0, 0, " + scheme.x2 + ", " + scheme.y2 + ")", testBlock.toString());
 }

 @Test
 public void testSubblockConstructionWithImproperCoords() {
 try {
 block.subblock(0, 0, 5, 5);
 fail("IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubblockConstructionWithValidCoords() {
 final int subBlockX1 = 1;
 final int subBlockX2 = 3;
 final int subBlockY1 = 1;
 final int subBlockY2 = 3;
 Block subBlock = block.subblock(subBlockX1, subBlockX2, subBlockY1, subBlockY2);
 assertEquals("Block(" + subBlockX1 + ", " + subBlockY1 + ", " + (subBlockX2 - subBlockX1) + ", " + (subBlockY2 - subBlockY1) + ")", subBlock.toString());
 }

 @Test
 public void testContainsOnlyOneWord() {
 String singleWord = "the quick brown fox";
 block.setTextAt(0, scheme.x2, 0, singleWord);
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordWithMultipleWords() {
 String firstWord = "the quick";
 String secondWord = "brown fox";
 block.setTextAt(0, scheme.x2, 0, firstWord);
 block.setTextAt(0, scheme.x2, this.scheme.y2, secondWord);
 assertFalse(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> sequentialSubblocks = block.extractSequentialSubblocks();
 assertEquals(1, sequentialSubblocks.size());
 Block firstSubblock = sequentialSubblocks.get(0);
 assertEquals("Block(0, 0, " + scheme.x2 + ", " + scheme.y2 + ")", firstSubblock.toString());
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> parallelSubblocks = block.extractParallelSubblocks();
 assertEquals(scheme.y2 - scheme.y1 + 1, parallelSubblocks.size());
 }
}
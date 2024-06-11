import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import org.junit.*;
import java.util.*;

public class BlockTest {

 @BeforeClass
 public static void setUp() {
 // Initialize the Scheme and create a block
 Scheme scheme = new Scheme(10, 5);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 3));
 }

 @Test
 public void testBlockConstructionWithInvalidPosition() {
 assertThrows(IllegalArgumentException.class, () -> {
 Block block = new Block(new Scheme(10, 5), -1, -1, 5, 3);
 });
 assertThrows(IllegalArgumentException.class, () -> {
 Block block = new Block(new Scheme(10, 5), 6, 6, 10, 11);
 });
 }

 @Test
 public void testBlockConstructionWithValidPosition() {
 assertEquals(1, block.getX1());
 assertEquals(1, block.getY1());
 assertEquals(5, block.getX2());
 assertEquals(3, block.getY2());
 }

 @Test
 public void testBlockConstructionWithCharPosition() {
 Block block = new Block(new Scheme(10, 5), new CharPosition(1, 1), new CharPosition(5, 3));
 assertEquals(1, block.getX1());
 assertEquals(1, block.getY1());
 assertEquals(5, block.getX2());
 assertEquals(3, block.getY2());
 }

 @Test
 public void testBlockConstructionWithScheme() {
 Block block = new Block(new Scheme(10, 5));
 assertEquals(0, block.getX1());
 assertEquals(0, block.getY1());
 assertEquals(10, block.getX2());
 assertEquals(5, block.getY2());
 }

 @Test
 public void testSubblock() {
 // Subblock construction with invalid position
 assertThrows(IllegalArgumentException.class, () -> {
 Block subblock = block.subblock(-1, -1, 2, 2);
 });
 // Subblock construction with position outside of block
 assertThrows(IllegalArgumentException.class, () -> {
 Block subblock = block.subblock(12, 12, 2, 2);
 });
 // Subblock construction with valid position
 Block subblock = block.subblock(2, 3, 2, 2);
 assertEquals(2, subblock.getX1());
 assertEquals(3, subblock.getY1());
 assertEquals(4, subblock.getX2());
 assertEquals(4, subblock.getY2());
 }

 @Test
 public void testCharAt() {
 CharPosition charPosition = new CharPosition(2, 2);
 assertEquals(' ', block.charAt(charPosition.x, charPosition.y));
 CharPosition outOfBlockCharPosition = new CharPosition(6, 6);
 assertThrows(IllegalArgumentException.class, () -> {
 block.charAt(outOfBlockCharPosition.x, outOfBlockCharPosition.y);
 });
 }

 @Test
 public void testTextAt() {
 String expectedText = "abc";
 String text = block.textAt(1, 4, 1);
 assertEquals(expectedText, text);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtWithInvalidArguments() {
 block.textAt(-1, 4, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtWithInvalidYPosition() {
 block.textAt(1, -1, 1);
 }

 @Test
 public void testContainsOnlyOneWord() {
 String expectedText = "the quick brown fox jumps over the lazy dog";
 List<Block> blocks = Arrays.asList(
 new Block(new Scheme(10, 5), new CharPosition(1, 1), new CharPosition(5, 3)),
 new Block(new Scheme(10, 5), new CharPosition(6, 1), new CharPosition(10, 3))
 );
 for (Block block : blocks) {
 assertTrue(block.containsOnlyOneWord());
 }

 List<Block> blocksWithMultipleWords = Arrays.asList(
 new Block(new Scheme(10, 5), new CharPosition(1, 1), new CharPosition(5, 4)),
 new Block(new Scheme(10, 5), new CharPosition(6, 1), new CharPosition(10, 5))
 );
 for (Block block : blocksWithMultipleWords) {
 assertFalse(block.containsOnlyOneWord());
 }
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> expectedSequentialSubblocks = Arrays.asList(
 new Block(new Scheme(10, 5), new CharPosition(1, 1), new CharPosition(4, 3)),
 new Block(new Scheme(10, 5), new CharPosition(5, 1), new CharPosition(10, 3))
 );
 List<Block> actualSequentialSubblocks = block.extractSequentialSubblocks();
 assertEquals(expectedSequentialSubblocks, actualSequentialSubblocks);
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> expectedParallelSubblocks = Arrays.asList(
 new Block(new Scheme(10, 5), new CharPosition(1, 1), new CharPosition(5, 3)),
 new Block(new Scheme(10, 5), new CharPosition(1, 4), new CharPosition(5, 4)),
 new Block(new Scheme(10, 5), new CharPosition(1, 1), new CharPosition(5, 1)),
 new Block(new Scheme(10, 5), new CharPosition(1, 2), new CharPosition(5, 2)),
 new Block(new Scheme(10, 5), new CharPosition(1, 3), new CharPosition(5, 3))
 );
 List<Block> actualParallelSubblocks = block.extractParallelSubblocks();
 assertEquals(expectedParallelSubblocks, actualParallelSubblocks);
 }

 @Test
 public void testToString() {
 String expectedString = "Block(0,0:,10,5):\n###################\n # the quick brown fox\n # jumps over the\n # lazy dog\n ####################";
 String actualString = block.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testGetUpperLine() {
 String expectedUpperLine = "the quick brown fox";
 String actualUpperLine = block.getUpperLine();
 assertEquals(expectedUpperLine, actualUpperLine);
 }
}
import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import glengineer.positions.WordPosition;

public class BlockTest {

 @Test
 public void testBlockConstructorWithCoordinates() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 1, 5, 5);
 assertEquals(1, block.getX1());
 assertEquals(1, block.getY1());
 assertEquals(5, block.getX2());
 assertEquals(5, block.getY2());
 }

 @Test
 public void testBlockConstructorWithPositions() {
 Scheme scheme = new Scheme(10, 10);
 CharPosition p1 = new CharPosition(1, 1);
 CharPosition p2 = new CharPosition(5, 5);
 Block block = new Block(scheme, p1, p2);
 assertEquals(p1.x, block.getX1());
 assertEquals(p1.y, block.getY1());
 assertEquals(p2.x, block.getX2());
 assertEquals(p2.y, block.getY2());
 }

 @Test
 public void testBlockConstructorWithSchemeOnly() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme);
 assertEquals(0, block.getX1());
 assertEquals(0, block.getY1());
 assertEquals(scheme.getX2(), block.getX2());
 assertEquals(scheme.getY2(), block.getY2());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockImproperDefinition() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, -1, 1, 5, 5);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockOutOfScheme() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 10, 5, 5);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testSubblock() {
 Scheme scheme = new Scheme(10, 10);
 Block superBlock = new Block(scheme, 0, 0, 10, 10);
 Block subBlock = superBlock.subblock(1, 1, 2, 2);
 Scheme subScheme = new Scheme(3, 3);
 Block expectedBlock = new Block(subScheme, 1, 1, 2, 2);
 assertEquals(expectedBlock, subBlock);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAbstractSubblock() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme);
 try {
 block.subblock(1, 1, 2, 2);
 fail("UnsupportedOperationException expected");
 } catch (UnsupportedOperationException e) {
 }
 }

 @Test
 public void testCharAt() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 1, 5, 5);
 char expectedChar = 'A';
 block.scheme.lines[1][0] = expectedChar;
 char resultChar = block.charAt(0, 0);
 assertEquals(expectedChar, resultChar);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharPositionOutOfBlock() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 1, 5, 5);
 block.charAt(6, 6);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testTextAt() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 1, 5, 5);
 String expectedText = "ABCDE";
 Arrays.fill(scheme.lines[1], 1, 6, expectedText.toCharArray());
 String resultText = block.textAt(1, 5, 1);
 assertEquals(expectedText, resultText);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testImproperSublineCoordinates() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 1, 5, 5);
 block.textAt(6, 6, 1);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testContainsOnlyOneWord() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 1, 5, 5);
 String singleWord = "ABCDE";
 Arrays.fill(scheme.lines[1], 1, 6, singleWord.toCharArray());
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsMultipleWords() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 1, 5, 5);
 String firstWord = "ABC";
 String secondWord = "DEFG";
 Arrays.fill(scheme.lines[1], 0, 3, firstWord.toCharArray());
 Arrays.fill(scheme.lines[1], 4, 10, secondWord.toCharArray());
 assertFalse(block.containsOnlyOneWord());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTrivialBlockSearchWord() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme);
 block.containsOnlyOneWord();
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testExtractSequentialSubblocks() {
 Scheme scheme = new Scheme(10, 10);
 Block superBlock = new Block(scheme, 1, 1, 10, 10);
 List<Block> expectedBlocks = Arrays.asList(
 new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3)),
 new Block(scheme, new CharPosition(4, 1), new CharPosition(6, 3)),
 new Block(scheme, new CharPosition(7, 1), new CharPosition(10, 3))
 );
 List<Block> resultBlocks = superBlock.extractSequentialSubblocks();
 assertEquals(expectedBlocks, resultBlocks);
 }

 @Test
 public void testExtractParallelSubblocks() {
 Scheme scheme = new Scheme(10, 10);
 Block superBlock = new Block(scheme, 1, 1, 10, 10);
 List<Block> expectedBlocks = Arrays.asList(
 new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 10)),
 new Block(scheme, new CharPosition(1, 4), new CharPosition(3, 7)),
 new Block(scheme, new CharPosition(1, 8), new CharPosition(3, 10))
 );
 List<Block> resultBlocks = superBlock.extractParallelSubblocks();
 assertEquals(expectedBlocks, resultBlocks);
 }

 @Test
 public void testToString() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 1, 5, 5);
 String expectedString = "Block(0, 0:)\n" +
 "# ABCDE #\n" +
 "# FGHIJK #\n" +
 "# LMNOPQ #\n" +
 "# STUVWX #\n" +
 "# YZABCD #\n" +
 "# ------ #\n";
 String resultString = block.toString();
 assertEquals(expectedString, resultString);
 }

 @Test
 public void testGetUpperLine() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 1, 5, 5);
 String expectedUpperLine = "ABCDE";
 String resultUpperLine = block.getUpperLine();
 assertEquals(expectedUpperLine, resultUpperLine);
 }
}
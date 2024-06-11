import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setUp() {
 // Assuming we have a Scheme object setup
 this.scheme = new Scheme(5, 5);
 this.block = new Block(scheme, 1, 1, 3, 3);
 }

 @Test
 public void testBlockConstructorWithInvalidCoordinates() {
 assertThrows(IllegalArgumentException.class, () -> new Block(scheme, -1, 1, 3, 3));
 assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, -1, 3, 3));
 assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 5, 5, 1, 1));
 assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 5, 5, 6, 6));
 }

 @Test
 public void testBlockConstructorWithValidCoordinates() {
 assertEquals(1, block.getX1());
 assertEquals(1, block.getY1());
 assertEquals(3, block.getX2());
 assertEquals(3, block.getY2());
 }

 @Test
 public void testBlockConstructorWithSinglePosition() {
 Block block1 = new Block(scheme, new CharPosition(1, 1));
 assertEquals(1, block1.getX1());
 assertEquals(1, block1.getY1());
 assertEquals(scheme.x2, block1.getX2());
 assertEquals(scheme.y2, block1.getY2());
 }

 @Test
 public void testBlockConstructorWithSchemeOnly() {
 Block block2 = new Block(scheme);
 assertEquals(0, block2.getX1());
 assertEquals(0, block2.getY1());
 assertEquals(scheme.x2, block2.getX2());
 assertEquals(scheme.y2, block2.getY2());
 }

 @Test
 public void testSubblockWithValidCoordinates() {
 // Assuming Block has a subblock method implementation
 Block subblock = block.subblock(1, 1, 1, 1);
 assertEquals(1, subblock.getX1());
 assertEquals(1, subblock.getY1());
 assertEquals(1, subblock.getX2());
 assertEquals(1, subblock.getY2());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubblockWithInvalidCoordinates() {
 // Assuming Block has a subblock method implementation
 Block subblock = block.subblock(5, 5, 1, 1);
 }

 @Test
 public void testCharAtWithValidCoordinates() {
 char expectedChar = 'A';
 char actualChar = block.charAt(1, 1);
 assertEquals(expectedChar, actualChar);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharAtWithInvalidCoordinates() {
 // Assuming proper IllegalArgumentException is thrown
 block.charAt(4, 4);
 }

 @Test
 public void testTextAtWithValidCoordinates() {
 String expectedText = "Hello";
 String actualText = block.textAt(1, 5, 1);
 assertEquals(expectedText, actualText);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtWithInvalidCoordinates() {
 // Assuming proper IllegalArgumentException is thrown
 block.textAt(4, 4, 1);
 }

 @Test
 public void testContainsOnlyOneWordWithSingleWord() {
 String word = "Block";
 block.setTextAt(1, 5, 1, word);
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordWithMultipleWords() {
 String word1 = "Block";
 String word2 = "Test";
 block.setTextAt(1, 3, 1, word1);
 block.setTextAt(1, 6, 1, word2);
 assertFalse(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 // Assuming Block has extractSequentialSubblocks method implementation
 List<Block> subblocks = block.extractSequentialSubblocks();
 assertEquals(2, subblocks.size());
 }

 @Test
 public void testExtractParallelSubblocks() {
 // Assuming Block has extractParallelSubblocks method implementation
 List<Block> subblocks = block.extractParallelSubblocks();
 assertEquals(2, subblocks.size());
 }

 @Test
 public void testToString() {
 String expectedToString = "Block(1,1,3,3):\n-------------------\n # Hello #\n # Test #\n-------------------";
 String actualToString = block.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testGetUpperLineWithSingleLineBlock() {
 String expectedUpperLine = "Hello";
 String actualUpperLine = block.getUpperLine();
 assertEquals(expectedUpperLine, actualUpperLine);
 }

 @Test
 public void testGetUpperLineWithMultiLineBlock() {
 String expectedUpperLine1 = "Hello";
 String expectedUpperLine2 = "Test";
 String actualUpperLine = block.getUpperLine();
 assertTrue(actualUpperLine.equals(expectedUpperLine1) || actualUpperLine.equals(expectedUpperLine2));
 }
}
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setUp() {
 // Assuming Scheme is initialized properly
 scheme = new Scheme(10, 10);
 block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 }

 @Test
 public void testConstructorWithCoordinates() {
 assertEquals(1, block.x1);
 assertEquals(1, block.y1);
 assertEquals(5, block.x2);
 assertEquals(5, block.y2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithImproperCoordinates() {
 Block blockWithImproperCoordinates = new Block(scheme, -1, 1, 10, 10);
 // IllegalArgumentException should be thrown
 }

 @Test
 public void testConstructorWithScheme() {
 assertEquals(0, block.x1);
 assertEquals(0, block.y1);
 assertEquals(10, block.x2);
 assertEquals(10, block.y2);
 }

 @Test
 public void testSubblockWithValidCoordinates() {
 Block subblock = block.subblock(1, 2, 3, 4);
 assertNotNull(subblock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubblockWithImproperCoordinates() {
 Block subblock = block.subblock(1, 2, scheme.x2 + 1, scheme.y2 + 1);
 // IllegalArgumentException should be thrown
 }

 @Test
 public void testCharAt() {
 char expectedChar = 'A';
 CharPosition charPosition = new CharPosition(2, 3);
 char actualChar = block.charAt(charPosition.x, charPosition.y);
 assertEquals(expectedChar, actualChar);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharAtOutOfBlock() {
 CharPosition charPositionOutOfBlock = new CharPosition(-1, 3);
 block.charAt(charPositionOutOfBlock.x, charPositionOutOfBlock.y);
 // IllegalArgumentException should be thrown
 }

 @Test
 public void testTextAt() {
 String expectedText = "Hello";
 String actualText = block.textAt(1, 5, 2);
 assertEquals(expectedText, actualText);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtImproperCoordinates() {
 String actualText = block.textAt(scheme.x2 + 1, scheme.x2 + 1, 2);
 // IllegalArgumentException should be thrown
 }

 @Test
 public void testContainsOnlyOneWord() {
 String expectedText = "The quick brown fox jumps over the lazy dog.";
 block = new Block(scheme, new CharPosition(1, 1), new CharPosition(30, 10), new SchemeText("The quick brown fox jumps over the lazy dog."));
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> sequentialSubblocks = block.extractSequentialSubblocks();
 assertFalse(sequentialSubblocks.isEmpty());
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> parallelSubblocks = block.extractParallelSubblocks();
 assertFalse(parallelSubblocks.isEmpty());
 }

 @Test
 public void testToString() {
 String expectedBlockString = "Block(0,0,10,10):\n----------------------------------\n # The quick brown fox jumps over the lazy dog. #\n # The quick brown fox jumps over the lazy dog. #\n # The quick brown fox jumps over the lazy dog. #\n # The quick brown fox jumps over the lazy dog. #\n # The quick brown fox jumps over the lazy dog. #\n # The quick brown fox jumps over the lazy dog. #\n # The quick brown fox jumps over the lazy dog. #\n # The quick brown fox jumps over the lazy dog. #\n # The quick brown fox jumps over the lazy dog. #\n # The quick brown fox jumps over the lazy dog. #\n ----------------------------------\n";
 String actualBlockString = block.toString();
 assertEquals(expectedBlockString, actualBlockString);
 }

 @Test
 public void testGetUpperLine() {
 String expectedUpperLine = "The quick brown fox jumps over the lazy dog.";
 String actualUpperLine = block.getUpperLine();
 assertEquals(expectedUpperLine, actualUpperLine);
 }
}
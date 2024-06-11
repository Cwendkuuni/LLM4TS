import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import org.junit.Test;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setUp() {
 scheme = new Scheme(10, 5);
 block = new Block(scheme, new CharPosition(2, 3), new CharPosition(5, 4));
 }

 @Test
 public void testBlockConstructionWithInvalidCoordinates() {
 assertThrows(IllegalArgumentException.class, () -> new Block(scheme, -1, 3, 5, 4));
 assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 5, 4, 10, 11));
 }

 @Test
 public void testBlockConstructionWithValidCoordinates() {
 assertEquals(2, block.getX1());
 assertEquals(3, block.getY1());
 assertEquals(5, block.getX2());
 assertEquals(4, block.getY2());
 }

 @Test
 public void testBlockConstructionWithValidCoordinatesAndScheme() {
 Block block1 = new Block(scheme);
 assertEquals(0, block1.getX1());
 assertEquals(0, block1.getY1());
 assertEquals(scheme.x2, block1.getX2());
 assertEquals(scheme.y2, block1.getY2());
 }

 @Test
 public void testBlockConstructionWithValidPosition1AndPosition2() {
 Block block2 = new Block(scheme, new CharPosition(3, 5), new CharPosition(6, 7));
 assertEquals(3, block2.getX1());
 assertEquals(5, block2.getY1());
 assertEquals(6, block2.getX2());
 assertEquals(7, block2.getY2());
 }

 @Test
 public void testSubblockThrowsException() {
 assertThrows(UnsupportedOperationException.class, () -> block.subblock(1, 2, 3, 4));
 }

 @Test
 public void testCharAt() {
 CharPosition charPosition = new CharPosition(3, 5);
 char expected = 'A';
 scheme.lines[5].setCharAt(3, expected);
 assertEquals(expected, block.charAt(charPosition.x, charPosition.y));
 }

 @Test
 public void testTextAt() {
 String expected = "test text";
 scheme.lines[5].setSubSequence(3, 6, expected);
 assertEquals(expected, block.textAt(3, 6, 5));
 }

 @Test
 public void testContainsOnlyOneWord() {
 String expected = "test word";
 scheme.lines[5].setSubSequence(3, 6, expected);
 scheme.lines[5].setCharAt(6, ' ');
 scheme.lines[5].setCharAt(7, ' ');
 scheme.lines[5].setCharAt(8, '.');
 assertFalse(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordTrue() {
 String expected = "test word.";
 scheme.lines[5].setSubSequence(3, 8, expected);
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(2, 3), new CharPosition(4, 3)),
 new Block(scheme, new CharPosition(4, 3), new CharPosition(5, 4))
 );
 List<Block> actualSubblocks = block.extractSequentialSubblocks();
 assertEquals(expectedSubblocks, actualSubblocks);
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(2, 3), new CharPosition(5, 3)),
 new Block(scheme, new CharPosition(2, 4), new CharPosition(5, 4))
 );
 List<Block> actualSubblocks = block.extractParallelSubblocks();
 assertEquals(expectedSubblocks, actualSubblocks);
 }

 @Test
 public void testToString() {
 String expected = "Block(2, 3, 5, 4):\n-------------------\n # test text #\n # #\n # #\n # #\n # #\n # test word. #\n-------------------\n";
 String actual = block.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetUpperLine() {
 String expected = "test text";
 String actual = block.getUpperLine();
 assertEquals(expected, actual);
 }
}
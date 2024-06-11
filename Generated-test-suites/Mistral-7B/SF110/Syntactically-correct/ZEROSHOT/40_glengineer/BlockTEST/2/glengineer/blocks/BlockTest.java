import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

 private Scheme scheme;
 private Block block1;
 private Block block2;
 private Block block3;

 @org.junit.Before
 public void setUp() {
 // Assuming Scheme class and CharPosition class are properly mocked or initialized
 scheme = new Scheme(10, 10);
 block1 = new Block(scheme, 1, 1, 3, 5);
 block2 = new Block(scheme, 1, 1, 5, 5);
 block3 = new Block(scheme, 1, 1, 7, 7);
 }

 @Test
 public void testBlockConstruction() {
 Block b = new Block(scheme);
 assertEquals(scheme, b.getScheme());
 assertEquals(0, b.getX1());
 assertEquals(0, b.getY1());
 assertEquals(scheme.x2, b.getX2());
 assertEquals(scheme.y2, b.getY2());
 }

 @Test
 public void testBlockConstructionWithCoordinates() {
 assertEquals(scheme, block1.getScheme());
 assertEquals(1, block1.getX1());
 assertEquals(1, block1.getY1());
 assertEquals(3, block1.getX2());
 assertEquals(5, block1.getY2());
 }

 @Test
 public void testBlockConstructionWithPositions() {
 CharPosition p1 = new CharPosition(1, 1);
 CharPosition p2 = new CharPosition(3, 5);
 assertEquals(scheme, block1.getScheme());
 assertEquals(p1, block1.getCharPosition1());
 assertEquals(p2, block1.getCharPosition2());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testImproperBlockDefinition() {
 Block b = new Block(scheme, 1, 1, 10, 10);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testSubblockConstruction() {
 Block subblock = block1.subblock(2, 2, 3, 3);
 assertEquals(scheme, subblock.getScheme());
 assertEquals(2, subblock.getX1());
 assertEquals(2, subblock.getY1());
 assertEquals(3, subblock.getX2());
 assertEquals(3, subblock.getY2());
 }

 @Test
 public void testCharAt() {
 char expected = 'A';
 CharPosition pos = new CharPosition(1, 1);
 assertEquals(expected, block1.charAt(pos.x, pos.y));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharPositionOutOfBlock() {
 CharPosition pos = new CharPosition(4, 5);
 block1.charAt(pos.x, pos.y);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testTextAt() {
 String expected = "abc";
 int x1 = 1, x2 = 3, y = 1;
 assertEquals(expected, block1.textAt(x1, x2, y));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testImproperSublineCoordinates() {
 int x1 = 1, x2 = 3, y = 4;
 block1.textAt(x1, x2, y);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testContainsOnlyOneWord() {
 String expected = "hello";
 assertEquals(expected, block2.textAt(1, 5, 1));
 assertTrue(block2.containsOnlyOneWord());
 }

 @Test
 public void testDoesNotContainOnlyOneWord() {
 String expected1 = "hello";
 String expected2 = "world";
 assertEquals(expected1, block3.textAt(1, 4, 1));
 assertEquals(expected2, block3.textAt(5, 7, 1));
 assertFalse(block3.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> expected = Arrays.asList(new Block(scheme, 2, 2, 3, 3), new Block(scheme, 4, 2, 5, 3));
 List<Block> result = block2.extractSequentialSubblocks();
 assertEquals(expected, result);
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> expected1 = Arrays.asList(new Block(scheme, 1, 2, 2, 3), new Block(scheme, 1, 4, 2, 3));
 List<Block> expected2 = Arrays.asList(new Block(scheme, 1, 2, 2, 3), new Block(scheme, 4, 2, 6, 3));
 List<Block> result1 = block3.extractParallelSubblocks();
 List<Block> result2 = block3.extractParallelSubblocks();
 assertEquals(expected1, result1);
 assertEquals(expected2, result2);
 }

 @Test
 public void testToString() {
 String expected = "Block(1, 1, 3, 5):\n# # # # # # # # # # # # # # # \n # a b c # # # # # # # # # # # # # # \n # # # # # # # # # # # # # # # # # # # # # \n# # # # # # # # # # # # # # # # # # # # # ";
 String result = block1.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testGetUpperLine() {
 String expected = "abc";
 String result = block1.getUpperLine();
 assertEquals(expected, result);
 }

}
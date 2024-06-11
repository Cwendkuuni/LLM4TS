import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import glengineer.positions.WordPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setup() {
 scheme = new Scheme(10, 10);
 block = new Block(scheme);
 }

 @Test
 public void testBlockConstructionWithCoordinates() {
 final int expectedX1 = 1;
 final int expectedY1 = 2;
 final int expectedX2 = 5;
 final int expectedY2 = 7;

 Block block = new Block(scheme, expectedX1, expectedY1, expectedX2, expectedY2);

 Assert.assertEquals(expectedX1, block.getX1());
 Assert.assertEquals(expectedY1, block.getY1());
 Assert.assertEquals(expectedX2, block.getX2());
 Assert.assertEquals(expectedY2, block.getY2());
 }

 @Test
 public void testBlockConstructionWithPositions() {
 final CharPosition p1 = new CharPosition(1, 2);
 final CharPosition p2 = new CharPosition(5, 7);

 Block block = new Block(scheme, p1, p2);

 Assert.assertEquals(p1.x, block.getX1());
 Assert.assertEquals(p1.y, block.getY1());
 Assert.assertEquals(p2.x, block.getX2());
 Assert.assertEquals(p2.y, block.getY2());
 }

 @Test
 public void testBlockConstructionWithSchemeOnly() {
 Block block = new Block(scheme);

 Assert.assertEquals(0, block.getX1());
 Assert.assertEquals(0, block.getY1());
 Assert.assertEquals(scheme.x2, block.getX2());
 Assert.assertEquals(scheme.y2, block.getY2());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructionWithInvalidCoordinates() {
 final int x1 = -1;
 final int y1 = -1;
 final int x2 = 11;
 final int y2 = 11;

 Assert.throwsException(IllegalArgumentException.class, () -> new Block(scheme, x1, y1, x2, y2));
 }

 @Test
 public void testSubblockConstruction() {
 final int p0 = 1;
 final int p1 = 2;
 final int p2 = 3;
 final int p3 = 4;
 final Block expectedBlock = new Block(scheme, p0, p1, p2, p3);

 Block block = new Block(scheme, 1, 2, 4, 4);
 Block subblock = block.subblock(p0, p1, p2, p3);

 Assert.assertEquals(expectedBlock, subblock);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSubblockConstructionWithUnsupportedBlock() {
 Block block = new Block(scheme);

 Assert.throwsException(UnsupportedOperationException.class, () -> block.subblock(1, 2, 3, 4));
 }

 @Test
 public void testCharAt() {
 final char expectedChar = 'A';
 scheme.setCharAt(1, 1, expectedChar);

 Assert.assertEquals(expectedChar, block.charAt(0, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharAtWithInvalidPosition() {
 Assert.throwsException(IllegalArgumentException.class, () -> block.charAt(-1, -1));
 }

 @Test
 public void testTextAt() {
 final String expectedText = "hello";
 scheme.setTextAt(1, 0, 2, 0, expectedText);

 Assert.assertEquals(expectedText, block.textAt(0, 5, 2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtWithInvalidPosition() {
 Assert.throwsException(IllegalArgumentException.class, () -> block.textAt(-1, -1, 2));
 }

 @Test
 public void testContainsOnlyOneWord() {
 final String word = "hello";
 scheme.setTextAt(1, 0, 2, 0, word);
 scheme.setTextAt(1, 0, 2, 5, "world");

 Assert.assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 final int subblockWidth = 2;
 final int subblockHeight = 3;
 final Block expectedSubblock = new Block(scheme, 1, 2, subblockWidth + 1, subblockHeight + 1);

 List<Block> subblocks = block.extractSequentialSubblocks();

 Assert.assertEquals(1, subblocks.size());
 Assert.assertEquals(expectedSubblock, subblocks.get(0));
 }

 @Test
 public void testExtractParallelSubblocks() {
 final int subblockWidth = 2;
 final int subblockHeight = 3;
 final int numSubblocks = 2;

 List<Block> expectedSubblocks = new ArrayList<>();
 for (int i = 0; i < numSubblocks; i++) {
 expectedSubblocks.add(new Block(scheme, 1 + i * (subblockWidth + 1), 2, subblockWidth + 1, subblockHeight + 1));
 }

 List<Block> subblocks = block.extractParallelSubblocks();

 Assert.assertEquals(numSubblocks, subblocks.size());
 for (int i = 0; i < numSubblocks; i++) {
 Assert.assertEquals(expectedSubblocks.get(i), subblocks.get(i));
 }
 }

 @Test
 public void testToString() {
 final String expectedString = "Block(0,0:10,10):\n------------------\n # hello #\n # world #\n # world #\n # !!! #\n------------------";

 scheme.setTextAt(0, 0, 0, 0, "hello");
 scheme.setTextAt(0, 0, 0, 5, "world");
 scheme.setTextAt(0, 0, 0, 10, "!!!");

 Assert.assertEquals(expectedString, block.toString());
 }

 @Test
 public void testGetUpperLine() {
 final String expectedUpperLine = "hello";
 scheme.setTextAt(0, 0, 0, 0, expectedUpperLine);

 Assert.assertEquals(expectedUpperLine, block.getUpperLine());
 }
}
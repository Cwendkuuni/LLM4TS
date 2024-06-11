import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setup() {
 scheme = new Scheme(10, 5);
 block = new Block(scheme, new CharPosition(2, 3), new CharPosition(5, 4));
 }

 @Test
 public void testConstructorWithInvalidCoords() {
 Assert.assertThrows(IllegalArgumentException.class, () -> {
 Block invalidBlock = new Block(scheme, -1, 0, 10, 10);
 });
 }

 @Test
 public void testConstructorWithValidCoords() {
 Assert.assertEquals(2, block.getX1());
 Assert.assertEquals(3, block.getY1());
 Assert.assertEquals(5, block.getX2());
 Assert.assertEquals(4, block.getY2());
 }

 @Test
 public void testConstructorWithCharPositions() {
 CharPosition p1 = new CharPosition(2, 3);
 CharPosition p2 = new CharPosition(5, 4);
 block = new Block(scheme, p1, p2);
 Assert.assertEquals(p1.x, block.getX1());
 Assert.assertEquals(p1.y, block.getY1());
 Assert.assertEquals(p2.x, block.getX2());
 Assert.assertEquals(p2.y, block.getY2());
 }

 @Test
 public void testConstructorWithEmptyBlock() {
 block = new Block(scheme);
 Assert.assertEquals(0, block.getX1());
 Assert.assertEquals(0, block.getY1());
 Assert.assertEquals(scheme.getX2(), block.getX2());
 Assert.assertEquals(scheme.getY2(), block.getY2());
 }

 @Test
 public void testSubblockThrowsException() {
 Assert.assertThrows(UnsupportedOperationException.class, () -> block.subblock(0, 0, 0, 0));
 }

 @Test
 public void testContainsOnlyOneWord() {
 String text = "one word";
 for (int i = 0; i < 5; ++i) {
 charAt(i, i, block);
 scheme.setCharAt(i + 2, i + 2, block.getX1(), block.getY1(), text.charAt(i), scheme);
 }
 List<Block> subblocks = block.extractSequentialSubblocks();
 Block subblock = subblocks.get(0);
 Assert.assertTrue(subblock.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordThrowsException() {
 Assert.assertThrows(IllegalStateException.class, () -> block.containsOnlyOneWord());
 }

 @Test
 public void testToString() {
 String expected = "Block(2, 3, 5, 4):\n---------------------\n # one word #\n # word #\n # word #\n # word #\n # word #\n---------------------\n";
 String actual = block.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetUpperLine() {
 String expected = "one word";
 String actual = block.getUpperLine();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> expectedSubblocks = new ArrayList<>();
 expectedSubblocks.add(new Block(scheme, new CharPosition(2, 3), new CharPosition(3, 4)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(4, 4), new CharPosition(5, 4)));
 List<Block> actualSubblocks = block.extractSequentialSubblocks();
 Assert.assertEquals(expectedSubblocks, actualSubblocks);
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> expectedSubblocks = new ArrayList<>();
 expectedSubblocks.add(new Block(scheme, new CharPosition(2, 3), new CharPosition(5, 3)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(2, 4), new CharPosition(5, 4)));
 List<Block> actualSubblocks = block.extractParallelSubblocks();
 Assert.assertEquals(expectedSubblocks, actualSubblocks);
 }

}
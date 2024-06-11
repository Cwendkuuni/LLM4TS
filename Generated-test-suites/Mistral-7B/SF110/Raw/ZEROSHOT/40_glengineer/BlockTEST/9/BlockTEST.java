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

public class BlockTest {

 private Scheme scheme;
 private Block block1;
 private Block block2;
 private Block block3;

 @Before
 public void setUp() {
 scheme = new Scheme(5, 5);
 block1 = new Block(scheme, 1, 1, 3, 3);
 block2 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
 block3 = new Block(scheme, 4, 4, 5, 5);
 }

 @Test
 public void testProperBlockDefinition() {
 Block block = new Block(scheme, 0, 0, 6, 6);
 Assert.assertThrows(IllegalArgumentException.class, () -> block);
 }

 @Test
 public void testBlockConstructionWithCharPosition() {
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
 Assert.assertEquals(block.x1, 1);
 Assert.assertEquals(block.y1, 1);
 Assert.assertEquals(block.x2, 3);
 Assert.assertEquals(block.y2, 3);
 }

 @Test
 public void testBlockConstructionWithScheme() {
 Block block = new Block(scheme);
 Assert.assertEquals(block.x1, 0);
 Assert.assertEquals(block.y1, 0);
 Assert.assertEquals(block.x2, scheme.x2);
 Assert.assertEquals(block.y2, scheme.y2);
 }

 @Test
 public void testSubblockWithInvalidParameters() {
 Assert.assertThrows(UnsupportedOperationException.class, () -> block1.subblock(-1, -1, 1, 1));
 Assert.assertThrows(UnsupportedOperationException.class, () -> block1.subblock(1, 1, -1, -1));
 Assert.assertThrows(UnsupportedOperationException.class, () -> block1.subblock(1, 1, scheme.x2 + 1, scheme.y2 + 1));
 }

 @Test
 public void testCharAt() {
 CharPosition charPosition = new CharPosition(1, 1);
 Assert.assertEquals(block1.charAt(charPosition.x, charPosition.y), scheme.lines[charPosition.y].charAt(charPosition.x));
 Assert.assertThrows(IllegalArgumentException.class, () -> block1.charAt(5, 5));
 }

 @Test
 public void testTextAt() {
 int x1 = 1;
 int x2 = 3;
 int y = 1;
 Assert.assertEquals(block1.textAt(x1, x2, y), scheme.lines[y].substring(x1, x2));
 Assert.assertThrows(IllegalArgumentException.class, () -> block1.textAt(5, 5, 1));
 Assert.assertThrows(IllegalArgumentException.class, () -> block1.textAt(1, 3, 5));
 }

 @Test
 public void testContainsOnlyOneWord() {
 String singleWord = "singleWord";
 scheme.lines[1] = singleWord;
 Assert.assertTrue(block1.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordFailure() {
 scheme.lines[1] = "singleWord singleWord";
 Assert.assertFalse(block1.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> expectedBlocks = new ArrayList<>();
 expectedBlocks.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(2, 1)));
 expectedBlocks.add(new Block(scheme, new CharPosition(2, 1), new CharPosition(3, 1)));
 List<Block> actualBlocks = block1.extractSequentialSubblocks();
 Assert.assertEquals(actualBlocks.size(), expectedBlocks.size());
 for (int i = 0; i < expectedBlocks.size(); i++) {
 Assert.assertEquals(expectedBlocks.get(i).toString(), actualBlocks.get(i).toString());
 }
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> expectedBlocks = new ArrayList<>();
 expectedBlocks.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(1, 2)));
 expectedBlocks.add(new Block(scheme, new CharPosition(2, 1), new CharPosition(2, 2)));
 expectedBlocks.add(new Block(scheme, new CharPosition(3, 1), new CharPosition(3, 2)));
 List<Block> actualBlocks = block3.extractParallelSubblocks();
 Assert.assertEquals(actualBlocks.size(), expectedBlocks.size());
 for (int i = 0; i < expectedBlocks.size(); i++) {
 Assert.assertEquals(expectedBlocks.get(i).toString(), actualBlocks.get(i).toString());
 }
 }

 @Test
 public void testToString() {
 StringBuilder expectedString = new StringBuilder();
 expectedString.append("Block(1, 1, 3, 3):\n# # #\n # singleWord #\n# # #\n # # #\n");
 String actualString = block1.toString();
 Assert.assertEquals(expectedString.toString(), actualString);
 }

 @Test
 public void testGetUpperLine() {
 Assert.assertEquals(block1.getUpperLine(), "singleWord");
 }
}
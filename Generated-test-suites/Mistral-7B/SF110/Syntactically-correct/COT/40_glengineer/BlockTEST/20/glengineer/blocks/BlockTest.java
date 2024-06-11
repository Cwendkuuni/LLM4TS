import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import glengineer.positions.WordPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

 @Test
 public void testBlockConstructorWithInvalidParameters() {
 try {
 new Block(new Scheme(10, 10), -1, 1, 10, 10);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Test message is correct
 assertEquals("improper block definition: -1,1,10,10.", e.getMessage());
 }
 }

 @Test
 public void testBlockConstructorWithValidParameters() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, 1, 1, 5, 5);
 assertEquals(1, block.x1);
 assertEquals(1, block.y1);
 assertEquals(5, block.x2);
 assertEquals(5, block.y2);
 assertEquals(scheme, block.scheme);
 }

 @Test
 public void testBlockConstructorWithCharPositionParameters() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 assertEquals(1, block.x1);
 assertEquals(1, block.y1);
 assertEquals(5, block.x2);
 assertEquals(5, block.y2);
 assertEquals(scheme, block.scheme);
 }

 @Test
 public void testBlockConstructorWithSingleParameter() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme);
 assertEquals(0, block.x1);
 assertEquals(0, block.y1);
 assertEquals(10, block.x2);
 assertEquals(10, block.y2);
 assertEquals(scheme, block.scheme);
 }

 @Test
 public void testSubblockMethodWithInvalidParameters() {
 try {
 Block block = new Block(new Scheme(10, 10), 1, 1, 5, 5);
 List<Block> subblocks = block.extractSequentialSubblocks();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Test message is correct
 assertEquals("This block does not have any subblocks.", e.getMessage());
 }
 }

 @Test
 public void testSubblockMethodWithValidParameters() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(1, 1), new CharPosition(1, 4)),
 new Block(scheme, new CharPosition(2, 1), new CharPosition(5, 4)),
 new Block(scheme, new CharPosition(1, 5), new CharPosition(5, 5))
 );
 List<Block> subblocks = block.extractSequentialSubblocks();
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test
 public void testSubblockMethodWithInvalidSubblockParameters() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 try {
 block.subblock(1, 1, 1, 1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Test message is correct
 assertEquals("Block position out of bounds.", e.getMessage());
 }
 }

 @Test
 public void testSubblockMethodWithValidSubblockParameters() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 Block subblock = block.subblock(2, 2, 2, 2);
 CharPosition expectedSubblockTopLeft = new CharPosition(3, 3);
 CharPosition expectedSubblockBottomRight = new CharPosition(4, 4);
 assertEquals(expectedSubblockTopLeft, subblock.getTopLeftPosition());
 assertEquals(expectedSubblockBottomRight, subblock.getBottomRightPosition());
 }

 @Test
 public void testContainsOnlyOneWordMethodWithTrivialBlock() {
 Block trivialBlock = new Block(new Scheme(1, 1));
 try {
 trivialBlock.containsOnlyOneWord();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Test message is correct
 assertEquals("cannot search in a trivial block", e.getMessage());
 }
 }

 @Test
 public void testContainsOnlyOneWordMethodWithSingleWordBlock() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 block.scheme.lines[block.y1][block.x1] = 'A';
 block.scheme.lines[block.y1][block.x1 + 1] = 'B';
 block.scheme.lines[block.y1][block.x1 + 2] = 'A';
 List<HWordPosition> words = block.getWords_Horizontally();
 assertTrue(words.size() > 1);
 HWordPosition firstWord = words.get(0);
 HWordPosition lastWord = words.get(words.size() - 1);
 Block expectedBlock = new Block(scheme, new CharPosition(1, 1), new CharPosition(4, 4));
 try {
 expectedBlock.containsOnlyOneWord();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Test message is correct
 assertEquals("cannot search in a block with more than one word.", e.getMessage());
 }
 Block testedBlock = new Block(scheme, new CharPosition(5, 5), new CharPosition(9, 9));
 assertTrue(testedBlock.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocksMethodWithEmptyBlock() {
 Scheme scheme = new Scheme(1, 1);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(1, 1));
 List<Block> expectedSubblocks = new ArrayList<>();
 List<Block> subblocks = block.extractSequentialSubblocks();
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test
 public void testExtractSequentialSubblocksMethodWithSingleWordBlock() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 block.scheme.lines[block.y1][block.x1] = 'A';
 block.scheme.lines[block.y1][block.x1 + 1] = 'B';
 block.scheme.lines[block.y1][block.x1 + 2] = 'A';
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(1, 1), new CharPosition(1, 4)),
 new Block(scheme, new CharPosition(2, 1), new CharPosition(5, 4))
 );
 List<Block> subblocks = block.extractSequentialSubblocks();
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test
 public void testExtractParallelSubblocksMethodWithEmptyBlock() {
 Scheme scheme = new Scheme(1, 1);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(1, 1));
 List<Block> expectedSubblocks = new ArrayList<>();
 List<Block> subblocks = block.extractParallelSubblocks();
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test
 public void testExtractParallelSubblocksMethodWithSingleLineBlock() {
 Scheme scheme = new Scheme(10, 1);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(10, 1));
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(1, 1), new CharPosition(10, 1))
 );
 List<Block> subblocks = block.extractParallelSubblocks();
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test
 public void testExtractParallelSubblocksMethodWithTwoLinesBlock() {
 Scheme scheme = new Scheme(10, 2);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 1)),
 new Block(scheme, new CharPosition(1, 2), new CharPosition(5, 2))
 );
 List<Block> subblocks = block.extractParallelSubblocks();
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test
 public void testToStringMethod() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 String expected = "Block(0,0:)\n" +
 "# A B A #\n" +
 "# # # # #\n" +
 "# # # # #\n" +
 "# # # # #\n" +
 "# # # # #\n" +
 "Block(5,5):\n" +
 "# # # # #\n" +
 "# # # # #\n" +
 "# # # # #\n" +
 "# # # # #\n" +
 "# # # # #\n" +
 "# # # # #\n";
 assertEquals(expected, block.toString());
 }

 @Test
 public void testGetUpperLineMethodWithEmptyBlock() {
 Scheme scheme = new Scheme(1, 1);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(1, 1));
 assertEquals("", block.getUpperLine());
 }

 @Test
 public void testGetUpperLineMethodWithSingleLineBlock() {
 Scheme scheme = new Scheme(10, 1);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(10, 1));
 block.scheme.lines[block.y1][block.x1] = 'A';
 assertEquals("A", block.getUpperLine());
 }

}
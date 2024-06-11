import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setup() {
 scheme = new Scheme(10, 10);
 block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 }

 @Test
 public void testBlockConstructorWithCoordinates() {
 Block expectedBlock = new Block(scheme, 1, 1, 5, 5);
 assertEquals("Block(1, 1, 5, 5):\n################\n # a b c d e #\n # f g h i j #\n # k l m n o #\n # p q r s t #\n ###################", block.toString());
 assertEquals(expectedBlock, block);
 }

 @Test
 public void testBlockConstructorWithScheme() {
 Block expectedBlock = new Block(scheme);
 assertEquals("Block(0, 0, 10, 10):\n################\n #                 #\n #                 #\n #                 #\n #                 #\n #                 #\n #                 #\n #                 #\n #                 #\n #                 #\n #                 #\n ###################", block.toString());
 assertEquals(expectedBlock, block);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructorWithInvalidCoordinates() {
 Block block = null;
 try {
 block = new Block(scheme, -1, 1, 5, 5);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertNull(block);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructorWithInvalidCoordinates2() {
 Block block = null;
 try {
 block = new Block(scheme, 5, 5, 10, 10);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertNull(block);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructorWithInvalidCoordinates3() {
 Block block = null;
 try {
 block = new Block(scheme, 5, 5, 11, 11);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertNull(block);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructorWithInvalidCoordinates4() {
 Block block = null;
 try {
 block = new Block(scheme, 1, 1, 10, 11);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertNull(block);
 }

 @Test
 public void testGetUpperLine() {
 String expected = "abcd e";
 assertEquals(expected, block.getUpperLine());
 }

 @Test
 public void testContainsOnlyOneWord() {
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWord_False() {
 Scheme scheme2 = new Scheme(10, 10);
 block = new Block(scheme2, new CharPosition(1, 1), new CharPosition(5, 5));
 block.scheme.lines[2] = "abc def";
 assertFalse(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> expectedBlocks = new ArrayList<>();
 expectedBlocks.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(2, 5)));
 expectedBlocks.add(new Block(scheme, new CharPosition(3, 1), new CharPosition(5, 5)));
 List<Block> actualBlocks = block.extractSequentialSubblocks();
 assertEquals(expectedBlocks, actualBlocks);
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> expectedBlocks = new ArrayList<>();
 expectedBlocks.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 1)));
 expectedBlocks.add(new Block(scheme, new CharPosition(1, 2), new CharPosition(5, 5)));
 List<Block> actualBlocks = block.extractParallelSubblocks();
 assertEquals(expectedBlocks, actualBlocks);
 }
}
import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setUp() {
 scheme = new Scheme(10, 5);
 block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 3));
 }

 @Test
 public void testBlockConstructionWithInvalidArguments() {
 try {
 Block invalidBlock = new Block(scheme, -1, 0, 5, 3);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 Block invalidBlock = new Block(scheme, 5, 5, 10, 10);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 Block invalidBlock = new Block(scheme, new CharPosition(5, 5), new CharPosition(-1, -1));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 Block invalidBlock = new Block(scheme, new CharPosition(11, 11));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBlockConstructionWithValidArguments() {
 Block validBlock = new Block(scheme, new CharPosition(1, 2), new CharPosition(5, 3));
 assertEquals(5, validBlock.x2 - validBlock.x1);
 assertEquals(3, validBlock.y2 - validBlock.y1);
 }

 @Test
 public void testBlockConstructionWithSchemeOnly() {
 Block validBlock = new Block(scheme);
 assertEquals(scheme.x2, validBlock.x2);
 assertEquals(scheme.y2, validBlock.y2);
 }

 @Test
 public void testSubblockWithValidArguments() {
 List<Block> subblocks = block.extractSequentialSubblocks();
 Block subblock = subblocks.get(0);
 Block expectedSubblock = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 1));
 assertTrue(expectedSubblock.equals(subblock));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSubblockWithInvalidArguments() {
 Block invalidBlock = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 3));
 List<Block> subblocks = invalidBlock.extractParallelSubblocks();
 subblocks.get(0);
 }

 @Test
 public void testContainsOnlyOneWord() {
 String singleWord = "apple";
 String blockText = singleWord + " " + singleWord + " apple";
 Scheme schemeWithText = new Scheme(10, 5);
 schemeWithText.setLines(Arrays.asList( Arrays.asList(singleWord.split("")).toArray(new Char[singleWord.length()])).toArray(new Char[][]{}));
 Block singleWordBlock = new Block(schemeWithText, new CharPosition(0, 0), new CharPosition(singleWord.length(), singleWord.length()));
 assertTrue(singleWordBlock.containsOnlyOneWord());
}

 @Test
 public void testToString() {
 String expectedBlockString = "Block(0,0:,5,3):\n" +
 "# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # \n" +
 "# a p p l e #\n" +
 "# a p p l e #\n" +
 "# a p p l e #\n" +
 "# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # \n";
 assertEquals(expectedBlockString, block.toString());
}

 @Test
 public void testGetUpperLine() {
 assertEquals("apple", block.getUpperLine());
}

}
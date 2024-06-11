import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setUp() {
 scheme = new Scheme(10, 10);
 block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 }

 @Test
 public void testBlockConstructorWithCoordinates() {
 Block expectedBlock = new Block(scheme, 1, 1, 5, 5);
 assertEquals(expectedBlock.x1, block.x1);
 assertEquals(expectedBlock.y1, block.y1);
 assertEquals(expectedBlock.x2, block.x2);
 assertEquals(expectedBlock.y2, block.y2);
 }

 @Test
 public void testBlockConstructorWithPosition() {
 CharPosition p1 = new CharPosition(1, 1);
 CharPosition p2 = new CharPosition(5, 5);
 Block expectedBlock = new Block(scheme, p1, p2);
 assertEquals(expectedBlock.x1, block.x1);
 assertEquals(expectedBlock.y1, block.y1);
 assertEquals(expectedBlock.x2, block.x2);
 assertEquals(expectedBlock.y2, block.y2);
 }

 @Test
 public void testBlockConstructorWithScheme() {
 Block expectedBlock = new Block(scheme);
 assertEquals(expectedBlock.x1, block.x1);
 assertEquals(expectedBlock.y1, block.y1);
 assertEquals(expectedBlock.x2, block.x2);
 assertEquals(expectedBlock.y2, block.y2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructorWithNegativeCoordinates() {
 CharPosition p1 = new CharPosition(-1, -1);
 CharPosition p2 = new CharPosition(5, 5);
 try {
 Block block = new Block(scheme, p1, p2);
 } catch (IllegalArgumentException iae) {
 fail("Expected IllegalArgumentException, but got: " + iae.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructorWithOutOfRangeCoordinates() {
 CharPosition p1 = new CharPosition(11, 11);
 CharPosition p2 = new CharPosition(5, 5);
 try {
 Block block = new Block(scheme, p1, p2);
 } catch (IllegalArgumentException iae) {
 fail("Expected IllegalArgumentException, but got: " + iae.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructorWithInvalidSchemeSize() {
 Scheme invalidScheme = new Scheme(-5, 10);
 try {
 Block block = new Block(invalidScheme);
 } catch (IllegalArgumentException iae) {
 fail("Expected IllegalArgumentException, but got: " + iae.getMessage());
 }
 }

 @Test
 public void testSubblockMethod() {
 Block subblock = block.subblock(1, 2, 3, 4);
 assertNotNull(subblock);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSubblockMethodWithInvalidArguments() {
 try {
 block.subblock(-1, 2, 3, 4);
 } catch (UnsupportedOperationException uoe) {
 fail("Expected UnsupportedOperationException, but got: " + uoe.getMessage());
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSubblockMethodWithOutOfRangeArguments() {
 try {
 block.subblock(1, 10, 3, 4);
 } catch (UnsupportedOperationException uoe) {
 fail("Expected UnsupportedOperationException, but got: " + uoe.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharAtMethodWithInvalidPosition() {
 try {
 block.charAt(-1, 0);
 } catch (IllegalArgumentException iae) {
 fail("Expected IllegalArgumentException, but got: " + iae.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharAtMethodWithPositionOutOfBlock() {
 try {
 block.charAt(10, 0);
 } catch (IllegalArgumentException iae) {
 fail("Expected IllegalArgumentException, but got: " + iae.getMessage());
 }
 }

 @Test
 public void testTextAtMethodWithValidPosition() {
 String expectedText = "sample text";
 String text = block.textAt(1, 6, 2);
 assertEquals(expectedText, text);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtMethodWithInvalidPosition() {
 try {
 block.textAt(0, 0, 0);
 } catch (IllegalArgumentException iae) {
 fail("Expected IllegalArgumentException, but got: " + iae.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtMethodWithPositionOutOfBlock() {
 try {
 block.textAt(11, 0, 0);
 } catch (IllegalArgumentException iae) {
 fail("Expected IllegalArgumentException, but got: " + iae.getMessage());
 }
 }

 @Test
 public void testContainsOnlyOneWordMethodWithSingleWord() {
 String text = "sample text";
 block.scheme.setTextAt(block.y1, block.x1, text);
 block.scheme.setTextAt(block.y1, block.x2, text.substring(text.length() - 2));
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordMethodWithMultipleWords() {
 String text1 = "sample text 1";
 String text2 = "sample text 2";
 block.scheme.setTextAt(block.y1, block.x1, text1);
 block.scheme.setTextAt(block.y1, block.x2, text2);
 assertFalse(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocksMethod() {
 List<Block> expectedBlocks = new ArrayList<>();
 for (int i = 0; i < 5; ++i) {
 expectedBlocks.add(new Block(scheme, new CharPosition(i+1, block.y1), new CharPosition(i+1 + 1, block.y2)));
 }
 List<Block> actualBlocks = block.extractSequentialSubblocks();
 assertEquals(expectedBlocks.size(), actualBlocks.size());
 for (int i = 0; i < expectedBlocks.size(); ++i) {
 assertEquals(expectedBlocks.get(i).toString(), actualBlocks.get(i).toString());
 }
 }

 @Test
 public void testExtractParallelSubblocksMethod() {
 List<Block> expectedBlocks = new ArrayList<>();
 for (int i = 0; i < 5; ++i) {
 expectedBlocks.add(new Block(scheme, new CharPosition(block.x1, i+1), new CharPosition(block.x2, i+1 + 1)));
 }
 List<Block> actualBlocks = block.extractParallelSubblocks();
 assertEquals(expectedBlocks.size(), actualBlocks.size());
 for (int i = 0; i < expectedBlocks.size(); ++i) {
 assertEquals(expectedBlocks.get(i).toString(), actualBlocks.get(i).toString());
 }
 }

 @Test
 public void testToStringMethod() {
 String expectedString = "Block(0,0:,10,10):\n" +
         "# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # " +
         "# text1 #\n" +
         "# text2 #\n" +
         "# text3 #\n" +
         "# text4 #\n" +
         "# text5 #\n" +
         "# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # ";
 String actualString = block.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testGetUpperLineMethod() {
 String expectedText = "sample text";
 String upperLine = block.getUpperLine();
 assertEquals(expectedText, upperLine);
 }

}
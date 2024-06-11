import org.junit.Test;
import org.junit.Assert.*;
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
 public void testBlockConstructorWithInvalidPosition() {
 Block block = null;
 try {
 block = new Block(new Scheme(10, 10), -1, 0, 5, 5);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException iae) {
 // expected
 }
 }

 @Test
 public void testBlockConstructorWithInvalidSize() {
 Block block = null;
 try {
 block = new Block(new Scheme(10, 10), 0, 0, 11, 11);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException iae) {
 // expected
 }
 }

 @Test
 public void testBlockConstructorWithValidPosition() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(3, 4), new CharPosition(6, 7));
 assertEquals(3, block.getX1());
 assertEquals(4, block.getY1());
 assertEquals(6, block.getX2());
 assertEquals(7, block.getY2());
 }

 @Test
 public void testBlockConstructorWithValidSize() {
 Block block = new Block(new Scheme(10, 10));
 assertEquals(0, block.getX1());
 assertEquals(0, block.getY1());
 assertEquals(10, block.getX2());
 assertEquals(10, block.getY2());
 }

 @Test
 public void testSubblockMethodThrowsIllegalArgumentException() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(3, 4), new CharPosition(6, 7));
 try {
 block.subblock(-1, -1, 2, 2);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException iae) {
 // expected
 }
 }

 @Test
 public void testCharAtMethodThrowsIllegalArgumentException() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(3, 4), new CharPosition(6, 7));
 try {
 block.charAt(11, 11);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException iae) {
 // expected
 }
 }

 @Test
 public void testTextAtMethodThrowsIllegalArgumentException() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(3, 4), new CharPosition(6, 7));
 try {
 block.textAt(11, 12, 5);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException iae) {
 // expected
 }
 }

 @Test
 public void testContainsOnlyOneWordMethodWithEmptyBlock() {
 Block block = new Block(new Scheme(10, 10));
 Assert.assertFalse(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordMethodWithSingleWord() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(0, 0), new CharPosition(9, 0));
 block.scheme.lines[0] = "singleWord".toCharArray();
 Assert.assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordMethodWithMultipleWords() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(0, 0), new CharPosition(9, 0));
 block.scheme.lines[0] = "firstWord firstWord".toCharArray();
 block.scheme.lines[1] = "secondWord secondWord".toCharArray();
 Assert.assertFalse(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocksMethod() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(0, 0), new CharPosition(9, 9));
 String text = "line1 line2 line3 line4 line5 line6 line7 line8 line9";
 for (int i = 0; i < 10; ++i) {
 block.scheme.lines[i] = text.substring(i * 10, i * 10 + 10).toCharArray();
 }
 List<Block> subblocks = block.extractSequentialSubblocks();
 List<String> expected = Arrays.asList("line1", "line2", "line3", "line4", "line5", "line6", "line7", "line8", "line9");
 for (int i = 0; i < subblocks.size(); ++i) {
 List<String> subblockText = new ArrayList<String>();
 for (int j = subblocks.get(i).getY1(); j < subblocks.get(i).getY2(); ++j) {
 subblockText.add(new String(subblocks.get(i).scheme.lines[j]));
 }
 assertEquals(expected.get(i), subblockText.toString());
 }
 }

 @Test
 public void testExtractParallelSubblocksMethod() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(0, 0), new CharPosition(9, 9));
 String text = "line1 line2 line3 line4 line5 line6 line7 line8 line9";
 for (int i = 0; i < 10; ++i) {
 block.scheme.lines[i] = text.substring(0, 10).toCharArray();
 }
 List<Block> subblocks = block.extractParallelSubblocks();
 List<String> expected = Arrays.asList("line1line2", "line3line4", "line5line6", "line7line8", "line9");
 for (int i = 0; i < subblocks.size(); ++i) {
 List<String> subblockText = new ArrayList<String>();
 for (int j = subblocks.get(i).getX1(); j < subblocks.get(i).getX2(); ++j) {
 for (int k = subblocks.get(i).getY1(); k < subblocks.get(i).getY2(); ++k) {
 subblockText.add(Character.toString(subblocks.get(i).charAt(j, k)));
 }
 }
 assertEquals(expected.get(i), new String(new StringBuilder().append(String.join("", subblockText)).reverse().toString()));
 }
 }

 @Test
 public void testToStringMethod() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(0, 0), new CharPosition(9, 9));
 String text = "line1 line2 line3 line4 line5 line6 line7 line8 line9";
 for (int i = 0; i < 10; ++i) {
 block.scheme.lines[i] = text.substring(i * 10, i * 10 + 10).toCharArray();
 }
 String expected = "Block(0,0,10,10):\n" +
 "# line1 line2 line3 line4 line5 line6 line7 line8 line9 #\n" +
 "# line1 line2 # # line4 line5 line6 line7 line8 line9 #\n" +
 "# line1 line2 line3 # # line5 line6 line7 line8 line9 #\n" +
 "# line1 line2 line3 line4 # # line7 line8 line9 #\n" +
 "# line1 line2 line3 line4 line5 # # line9 #\n" +
 "# line1 line2 line3 line4 line5 line6 # #\n" +
 "# line1 line2 line3 line4 line5 line6 line7 #\n" +
 "# line1 line2 line3 line4 line5 line6 line7 line8 #\n" +
 "# line1 line2 line3 line4 line5 line6 line7 line8 line9 #\n";
 assertEquals(expected, block.toString());
 }

 @Test
 public void testGetUpperLineMethod() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(0, 0), new CharPosition(9, 9));
 block.scheme.lines[0] = "upperLine".toCharArray();
 assertEquals("upperLine", block.getUpperLine());
 }
}
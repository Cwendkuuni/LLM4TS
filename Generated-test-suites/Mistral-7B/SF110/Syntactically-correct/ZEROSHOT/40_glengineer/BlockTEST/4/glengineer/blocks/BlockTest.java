import org.junit.Test;
import org.junit.Assert.*;
import java.util.List;
import java.util.Arrays;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import glengineer.positions.WordPosition;

public class BlockTest {

 @Test
 public void testConstructorWithInvalidArguments() {
 Block block = null;
 try {
 block = new Block(new Scheme(10, 10), -1, 1, 5, 5);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected exception.
 }

 try {
 block = new Block(new Scheme(10, 10), 0, 1, 5, 5);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected exception.
 }

 try {
 block = new Block(new Scheme(10, 10), 5, 5, 10, 10);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected exception.
 }

 try {
 block = new Block(new Scheme(10, 10), 5, 5, 11, 10);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected exception.
 }

 try {
 block = new Block(new Scheme(10, 10), 5, 5, 5, 11);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected exception.
 }

 try {
 block = new Block(new Scheme(10, 10), 5, 5, 5, 5);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected exception.
 }
 }

 @Test
 public void testConstructorWithValidArguments() {
 Block block = new Block(new Scheme(10, 10), 1, 1, 3, 3);
 assertEquals(1, block.getX1());
 assertEquals(1, block.getY1());
 assertEquals(3, block.getX2());
 assertEquals(3, block.getY2());
 }

 @Test
 public void testConstructorWithCharPosition() {
 CharPosition p1 = new CharPosition(1, 1);
 CharPosition p2 = new CharPosition(3, 3);
 Block block = new Block(new Scheme(10, 10), p1, p2);
 assertEquals(1, block.getX1());
 assertEquals(1, block.getY1());
 assertEquals(3, block.getX2());
 assertEquals(3, block.getY2());
 }

 @Test
 public void testConstructorWithScheme() {
 Block block = new Block(new Scheme(10, 10));
 assertEquals(0, block.getX1());
 assertEquals(0, block.getY1());
 assertEquals(10, block.getX2());
 assertEquals(10, block.getY2());
 }

 @Test
 public void testSubblockMethod() {
 Block mainBlock = new Block(new Scheme(10, 10), 1, 1, 3, 3);
 Block subBlock = mainBlock.subblock(1, 1, 1, 1);
 Block expectedSubBlock = new Block(new Scheme(10, 10), 1, 1, 1, 1);
 assertEquals(expectedSubBlock, subBlock);
 }

 @Test
 public void testCharAtMethod() {
 Block block = new Block(new Scheme(10, 10), 1, 1, 3, 3);
 char expectedCharAt11 = 'A';
 char expectedCharAt22 = 'B';
 char expectedCharAt33 = 'C';
 char expectedCharAt13 = 'D';
 char expectedCharAt21 = 'E';
 char expectedCharAt31 = 'F';
 char expectedCharAt11 = 'G';

 assertEquals(expectedCharAt11, block.charAt(1, 1));
 assertEquals(expectedCharAt22, block.charAt(2, 2));
 assertEquals(expectedCharAt33, block.charAt(3, 3));
 assertEquals(expectedCharAt13, block.charAt(1, 3));
 assertEquals(expectedCharAt21, block.charAt(2, 1));
 assertEquals(expectedCharAt31, block.charAt(3, 1));
 assertEquals(expectedCharAt11, block.charAt(1, 1));
 }

 @Test
 public void testTextAtMethod() {
 Block block = new Block(new Scheme(10, 10), 1, 1, 3, 3);
 String expectedTextAt112 = "AB";
 String expectedTextAt113 = "CD";
 String expectedTextAt213 = "EF";

 assertEquals(expectedTextAt112, block.textAt(1, 2, 1));
 assertEquals(expectedTextAt113, block.textAt(1, 3, 1));
 assertEquals(expectedTextAt213, block.textAt(2, 1, 3));
 }

 @Test
 public void testContainsOnlyOneWordMethod() {
 Block block = new Block(new Scheme(10, 10), 1, 1, 3, 3);
 block.setCharAt(1, 1, 'A');
 block.setCharAt(1, 2, 'B');
 block.setCharAt(2, 1, 'C');
 block.setCharAt(2, 2, 'B');
 block.setCharAt(3, 1, 'D');
 block.setCharAt(3, 2, 'D');
 block.setCharAt(3, 3, 'C');

 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordMethodFalse() {
 Block block = new Block(new Scheme(10, 10), 1, 1, 3, 3);
 block.setCharAt(1, 1, 'A');
 block.setCharAt(1, 2, 'B');
 block.setCharAt(2, 1, 'C');
 block.setCharAt(2, 2, 'B');
 block.setCharAt(3, 1, 'D');
 block.setCharAt(3, 2, 'E');
 block.setCharAt(3, 3, 'C');

 assertFalse(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocksMethod() {
 Block block = new Block(new Scheme(10, 10), 1, 1, 5, 5);
 List<Block> expectedExtractSequentialSubblocks = Arrays.asList(
 new Block(new Scheme(10, 10), 1, 1, 2, 2),
 new Block(new Scheme(10, 10), 2, 1, 3, 3)
 );
 List<Block> actualExtractSequentialSubblocks = block.extractSequentialSubblocks();
 assertEquals(expectedExtractSequentialSubblocks, actualExtractSequentialSubblocks);
 }

 @Test
 public void testExtractParallelSubblocksMethod() {
 Block block = new Block(new Scheme(10, 10), 1, 1, 5, 5);
 List<Block> expectedExtractParallelSubblocks = Arrays.asList(
 new Block(new Scheme(10, 10), 1, 1, 1, 5),
 new Block(new Scheme(10, 10), 1, 5, 1, 5),
 new Block(new Scheme(10, 10), 5, 1, 1, 5),
 new Block(new Scheme(10, 10), 5, 1, 5, 1),
 new Block(new Scheme(10, 10), 5, 5, 1, 1)
 );
 List<Block> actualExtractParallelSubblocks = block.extractParallelSubblocks();
 assertEquals(expectedExtractParallelSubblocks, actualExtractParallelSubblocks);
 }

 @Test
 public void testToStringMethod() {
 Block block = new Block(new Scheme(10, 10), 1, 1, 5, 5);
 String expectedToString = "Block(1,1,5,5):\n" +
 "# A B C D E F G #\n" +
 " # AB CD EF #\n" +
 " # CDE FG #\n" +
 " # DE #\n" +
 "#-------#\n";
 String actualToString = block.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testGetUpperLineMethod() {
 Block block = new Block(new Scheme(10, 10), 1, 1, 5, 5);
 String expectedGetUpperLine = "AB";
 String actualGetUpperLine = block.getUpperLine();
 assertEquals(expectedGetUpperLine, actualGetUpperLine);
 }
}
import org.junit.Test;
import org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setUp() {
 scheme = new Scheme(10, 10);
 block = new Block(scheme);
 }

 @Test
 public void testBlockConstructionWithValidParams() {
 block = new Block(scheme, 1, 1, 5, 5);
 assertTrue(block.x1 == 1 && block.y1 == 1 && block.x2 == 5 && block.y2 == 5);
 }

 @Test
 public void testBlockConstructionWithInvalidParams() {
 try {
 Block invalidBlock = new Block(scheme, -1, -1, 5, 5);
 fail();
 } catch (IllegalArgumentException e) {
 String expectedMessage = "improper block definition: -1, -1, 5, 5.";
 String actualMessage = e.getMessage();
 assertTrue(expectedMessage.equals(actualMessage));
 }
 }

 @Test
 public void testBlockConstructionWithPositions() {
 CharPosition p1 = new CharPosition(1, 1);
 CharPosition p2 = new CharPosition(5, 5);
 block = new Block(scheme, p1, p2);
 assertTrue(block.x1 == p1.x && block.y1 == p1.y && block.x2 == p2.x && block.y2 == p2.y);
 }

 @Test
 public void testBlockConstructionWithDefaultParams() {
 block = new Block(scheme);
 assertTrue(block.x1 == 0 && block.y1 == 0 && block.x2 == scheme.x2 && block.y2 == scheme.y2);
 }

 @Test
 public void testSubblockWithValidParams() {
 Block subblock = block.subblock(1, 1, 3, 3);
 Block expectedSubblock = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
 assertEquals(expectedSubblock, subblock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubblockWithInvalidParams() {
 block.subblock(-1, -1, 3, 3);
 fail();
 }

 @Test
 public void testCharAtWithValidParams() {
 CharPosition charPosition = new CharPosition(3, 2);
 char expectedChar = 'A';
 block.scheme.lines[2].charAt(3) = 'A';
 char actualChar = block.charAt(charPosition.x, charPosition.y);
 assertEquals(expectedChar, actualChar);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharAtWithInvalidParams() {
 CharPosition invalidCharPosition = new CharPosition(-1, 2);
 block.charAt(invalidCharPosition.x, invalidCharPosition.y);
 fail();
 }

 @Test
 public void testTextAtWithValidParams() {
 String expectedText = "HelloWorld";
 block.scheme.lines[2] = new StringBuilder("HelloWorld").toString().toCharArray();
 String actualText = block.textAt(3, 11, 2);
 assertEquals(expectedText, actualText);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtWithInvalidParams() {
 block.textAt(-1, 2, 2);
 fail();
 }

 @Test
 public void testContainsOnlyOneWord() {
 String singleWord = "HelloWorld";
 String multiWord = "HelloWorldThisIsATest";
 block.scheme.lines[0] = singleWord.toCharArray();
 block.scheme.lines[1] = multiWord.toCharArray();
 block.scheme.lines[2] = new char[10];
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> expectedSubblocks = new ArrayList<>();
 expectedSubblocks.add(new Block(scheme, new CharPosition(0, 0), new CharPosition(2, 5)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(3, 5), new CharPosition(5, 5)));
 List<Block> actualSubblocks = block.extractSequentialSubblocks();
 assertEquals(expectedSubblocks, actualSubblocks);
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> expectedSubblocks = new ArrayList<>();
 expectedSubblocks.add(new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 2)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(5, 2), new CharPosition(5, 5)));
 List<Block> actualSubblocks = block.extractParallelSubblocks();
 assertEquals(expectedSubblocks, actualSubblocks);
 }

 @Test
 public void testToString() {
 String expectedToString = "Block(0, 0: \n# # # # # # # # # # # \n# A B C D E F G H I J \n# K L M N O P Q R \n# S T U V W X Y Z \n# # # # # # # # # # # \n)";
 String actualToString = block.toString();
 assertEquals(expectedToString, actualToString);
 }
}
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
 public void testBlockConstructorWithPosition1() {
 Block expectedBlock = new Block(scheme, new CharPosition(1, 1));
 assertEquals(expectedBlock.x1, block.x1);
 assertEquals(expectedBlock.y1, block.y1);
 assertEquals(expectedBlock.x2, scheme.x2);
 assertEquals(expectedBlock.y2, scheme.y2);
 }

 @Test
 public void testBlockConstructorWithPosition2() {
 Block expectedBlock = new Block(scheme);
 assertEquals(expectedBlock.x1, 0);
 assertEquals(expectedBlock.y1, 0);
 assertEquals(expectedBlock.x2, scheme.x2);
 assertEquals(expectedBlock.y2, scheme.y2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructorWithInvalidCoordinates() {
 Block block = new Block(scheme, -1, 1, 5, 5);
 fail("IllegalArgumentException expected but not thrown.");
 }

 @Test
 public void testSubblockAbstractMethod() {
 try {
 block.subblock(1, 1, 1, 1);
 fail("Method should throw an AbstractMethodError.");
 } catch (AbstractMethodError ame) {
 // Expected
 }
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testCharAt() {
 CharPosition charPosition = new CharPosition(3, 3);
 char expectedChar = 'A';
 char actualChar = block.charAt(charPosition.x, charPosition.y);
 assertEquals(expectedChar, actualChar);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharAtOutOfBlock() {
 CharPosition charPosition = new CharPosition(3, 3);
 block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 try {
 block.charAt(charPosition.x, charPosition.y);
 fail("IllegalArgumentException expected but not thrown.");
 } catch (IllegalArgumentException iae) {
 // Expected
 }
 }

 @Test
 public void testTextAt() {
 String expectedString = "hello world";
 String actualString = block.textAt(1, 6, 3);
 assertEquals(expectedString, actualString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtInvalidCoordinates() {
 try {
 block.textAt(1, 6, 3);
 fail("IllegalArgumentException expected but not thrown.");
 } catch (IllegalArgumentException iae) {
 // Expected
 }
 }

 @Test
 public void testContainsOnlyOneWord() {
 String expectedString = "hello world";
 block = new Block(scheme, new CharPosition(1, 1), new CharPosition(6, 7), new CharPosition(12, 12), new CharPosition(18, 18));
 block.setTextAt(1, 6, 3, expectedString);
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordFalse() {
 String expectedString1 = "hello world";
 String expectedString2 = "hello java";
 block = new Block(scheme, new CharPosition(1, 1), new CharPosition(6, 7), new CharPosition(12, 12), new CharPosition(18, 18));
 block.setTextAt(1, 6, 3, expectedString1);
 block.setTextAt(1, 6, 5, expectedString2);
 assertFalse(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> expectedList = Arrays.asList(new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5)), new Block(scheme, new CharPosition(6, 1), new CharPosition(10, 5)), new Block(scheme, new CharPosition(11, 1), new CharPosition(15, 5)), new Block(scheme, new CharPosition(16, 1), new CharPosition(18, 5)));
 List<Block> actualList = block.extractSequentialSubblocks();
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> expectedList = Arrays.asList(new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 1)), new Block(scheme, new CharPosition(1, 2), new CharPosition(5, 2)), new Block(scheme, new CharPosition(1, 3), new CharPosition(5, 3)), new Block(scheme, new CharPosition(1, 4), new CharPosition(5, 4)), new Block(scheme, new CharPosition(1, 5), new CharPosition(5, 5)));
 List<Block> actualList = block.extractParallelSubblocks();
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testToString() {
 String expectedString = "Block(0,0,10,10):\n----------\n # hello world #\n----------";
 assertEquals(expectedString, block.toString());
 }

 @Test
 public void testGetUpperLine() {
 String expectedString = "hello world";
 String actualString = block.getUpperLine();
 assertEquals(expectedString, actualString);
 }
}
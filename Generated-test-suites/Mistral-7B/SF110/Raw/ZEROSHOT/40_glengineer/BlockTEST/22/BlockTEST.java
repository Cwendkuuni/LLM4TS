import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import org.junit.Test;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setup() {
 scheme = new Scheme(10, 10);
 block = new Block(scheme);
 }

 @Test
 public void testBlockCreationWithInvalidCoordinates() {
 try {
 new Block(scheme, -1, 0, 10, 10);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new Block(scheme, 10, 10, -1, 10);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new Block(scheme, 10, 10, 10, 11);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new Block(scheme, 11, 10, 10, 10);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBlockCreationWithValidCoordinates() {
 Block block = new Block(scheme, 1, 2, 3, 4);
 assertEquals(1, block.getX1());
 assertEquals(2, block.getY1());
 assertEquals(3, block.getX2());
 assertEquals(4, block.getY2());
 }

 @Test
 public void testBlockCreationWithValidCharPosition() {
 Block block = new Block(scheme, new CharPosition(1, 2), new CharPosition(3, 4));
 assertEquals(1, block.getX1());
 assertEquals(2, block.getY1());
 assertEquals(3, block.getX2());
 assertEquals(4, block.getY2());
 }

 @Test
 public void testBlockCreationWithValidScheme() {
 Block block = new Block(scheme);
 assertEquals(0, block.getX1());
 assertEquals(0, block.getY1());
 assertEquals(scheme.x2, block.getX2());
 assertEquals(scheme.y2, block.getY2());
 }

 @Test
 public void testSubblockCreationWithInvalidCoordinates() {
 try {
 block.subblock(-1, 0, 1, 1);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 block.subblock(0, -1, 1, 1);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 block.subblock(1, 1, -1, 1);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 block.subblock(1, 1, 1, -1);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubblockCreationWithValidCoordinates() {
 Block subblock = block.subblock(1, 1, 2, 2);
 assertEquals(1, subblock.getX1());
 assertEquals(1, subblock.getY1());
 assertEquals(2, subblock.getX2());
 assertEquals(2, subblock.getY2());
 }

 @Test
 public void testCharAt() {
 CharPosition charPosition = new CharPosition(1, 1);
 assertEquals(' ', block.charAt(charPosition.x, charPosition.y));
 assertEquals('#', block.charAt(0, 0));
 assertEquals('#', block.charAt(scheme.x2, scheme.y2));
 }

 @Test
 public void testTextAt() {
 String expectedText = "abc";
 block.scheme.lines[1] = "abc";
 assertEquals(expectedText, block.textAt(0, 3, 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtWithInvalidHorizontalPosition() {
 block.textAt(-1, 3, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtWithInvalidVerticalPosition() {
 block.textAt(3, -1, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtWithInvalidSublineCoordinates() {
 block.textAt(0, 3, scheme.y2 + 1);
 }

 @Test
 public void testContainsOnlyOneWord() {
 String expectedText = "the quick brown fox jumps over the lazy dog";
 block.scheme.lines[1] = expectedText;
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> subblocks = block.extractSequentialSubblocks();
 assertEquals(1, subblocks.size());
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> subblocks = block.extractParallelSubblocks();
 assertEquals(1, subblocks.size());
 }

 @Test
 public void testToString() {
 String expectedResult = "Block(0,0:10,10):\n########################\n # the quick brown fox jumps over the lazy dog #\n ###########################";
 assertEquals(expectedResult, block.toString());
 }

 @Test
 public void testGetUpperLine() {
 String expectedText = "the quick brown fox jumps over the lazy dog";
 assertEquals(expectedText, block.getUpperLine());
 }
}
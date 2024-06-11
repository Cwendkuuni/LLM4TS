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

 private Scheme scheme;
 private Block block;

 @Before
 public void setUp() {
 scheme = new Scheme(3, 3);
 block = new Block(scheme);
 }

 @Test(expected = IllegalArgumentException.class)
 public void constructorTest1() {
 block = new Block(scheme, -1, -1, 3, 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void constructorTest2() {
 block = new Block(scheme, 3, 3, -1, 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void constructorTest3() {
 block = new Block(scheme, 3, 3, 3, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void constructorTest4() {
 block = new Block(scheme, 3, 3, 4, 4);
 }

 @Test(expected = IllegalArgumentException.class)
 public void constructorTest5() {
 block = new Block(scheme, new CharPosition(-1, -1), new CharPosition(3, 3));
 }

 @Test
 public void constructorTest6() {
 block = new Block(scheme, new CharPosition(0, 0), new CharPosition(2, 2));
 assertEquals(3, block.x2 - block.x1);
 assertEquals(3, block.y2 - block.y1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void constructorTest7() {
 block = new Block(scheme);
 block.x1 = -1;
 block.y1 = -1;
 }

 @Test(expected = IllegalArgumentException.class)
 public void constructorTest8() {
 block = new Block(scheme);
 block.x2 = scheme.x2 + 1;
 block.y2 = scheme.y2 + 1;
 }

 @Test
 public void subblockTest1() {
 Block subblock = block.subblock(0, 0, 1, 1);
 assertNotNull(subblock);
 assertEquals(1, subblock.x2 - subblock.x1);
 assertEquals(1, subblock.y2 - subblock.y1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void subblockTest2() {
 block.subblock(-1, -1, 1, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void subblockTest3() {
 block.subblock(block.x2 + 1, block.y1, 1, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void subblockTest4() {
 block.subblock(block.x1, block.y2 + 1, 1, 1);
 }

 @Test
 public void subblockTest5() {
 Block subblock = block.subblock(block.x1, block.y1, 2, 2);
 assertEquals(2, subblock.x2 - subblock.x1);
 assertEquals(2, subblock.y2 - subblock.y1);
 }

 @Test
 public void charAtTest1() {
 char expected = 'A';
 block.scheme.lines[0] = "ABC".toCharArray();
 assertEquals(expected, block.charAt(0, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void charAtTest2() {
 int x = block.x1 + 2;
 int y = block.y1;
 try {
 block.charAt(x, y);
 } catch (IllegalArgumentException iae) {
 fail("Expected IllegalArgumentException but got: " + iae.getMessage());
 }
 assertEquals("char position " + new CharPosition(x, y) + " out of block.\n" + block, iae.getMessage());
 }

 @Test
 public void textAtTest1() {
 String expected = "AB";
 assertEquals(expected, block.textAt(0, 2, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void textAtTest2() {
 int x1 = block.x1;
 int x2 = block.x2;
 int y = block.y2 + 1;
 try {
 block.textAt(x1, x2, y);
 } catch (IllegalArgumentException iae) {
 fail("Expected IllegalArgumentException but got: " + iae.getMessage());
 } assertEquals("Improper subline coordinates.", iae.getMessage());
 }

 @Test
 public void containsOnlyOneWordTest1() {
 String text = "Hello World";
 block.scheme.lines[0] = text.toCharArray();
 block.scheme.lines[1] = Character.toString(' ').toCharArray();
 block.scheme.lines[2] = Character.toString('\n').toCharArray();
 block.scheme.lines[3] = text.toCharArray();
 List<Block> subblocks = block.extractSequentialSubblocks();
 Block subblock = subblocks.get(0);
 assertTrue(subblock.containsOnlyOneWord());
 }

 @Test
 public void containsOnlyOneWordTest2() {
 String text = "Hello World";
 block.scheme.lines[0] = text.toCharArray();
 block.scheme.lines[1] = Character.toString(' ').toCharArray();
 block.scheme.lines[2] = text.toCharArray();
 block.scheme.lines[3] = Character.toString('\n').toCharArray();
 block.scheme.lines[4] = Character.toString('\n').toCharArray();
 List<Block> subblocks = block.extractSequentialSubblocks();
 Block subblock = subblocks.get(0);
 assertFalse(subblock.containsOnlyOneWord());
 }

 @Test
 public void extractSequentialSubblocksTest1() {
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(0, 0), new CharPosition(1, 1)),
 new Block(scheme, new CharPosition(1, 0), new CharPosition(2, 1))
 );
 List<Block> subblocks = block.extractSequentialSubblocks();
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test
 public void extractSequentialSubblocksTest2() {
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(0, 0), new CharPosition(2, 2))
 );
 List<Block> subblocks = block.extractSequentialSubblocks();
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test
 public void extractParallelSubblocksTest1() {
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(0, 0), new CharPosition(1, 1)),
 new Block(scheme, new CharPosition(1, 1), new CharPosition(2, 2)),
 new Block(scheme, new CharPosition(0, 1), new CharPosition(1, 2))
 );
 List<Block> subblocks = block.extractParallelSubblocks();
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test
 public void extractParallelSubblocksTest2() {
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(0, 0), new CharPosition(2, 2))
 );
 List<Block> subblocks = block.extractParallelSubblocks();
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test
 public void toStringTest() {
 String expected = "Block(0,0:,2,2):\n" +
 "# # H e l l o # #\n" +
 "# # W o r l d # #\n" +
 "# # # # # # # #\n" +
 "# # # # # # # #";
 assertEquals(expected, block.toString());
 }

 @Test
 public void getUpperLineTest() {
 String expected = "Hello";
 assertEquals(expected, block.getUpperLine());
 }

}
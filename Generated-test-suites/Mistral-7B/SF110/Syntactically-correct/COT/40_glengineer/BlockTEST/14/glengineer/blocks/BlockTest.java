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
 public void testBlockConstructorWithCoordinates() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(5, 5));
 assertEquals(1, block.getX1());
 assertEquals(1, block.getY1());
 assertEquals(5, block.getX2());
 assertEquals(5, block.getY2());
 }

 @Test
 public void testBlockConstructorWithScheme() {
 Block block = new Block(new Scheme(10, 10));
 assertEquals(0, block.getX1());
 assertEquals(0, block.getY1());
 assertEquals(10, block.getX2());
 assertEquals(10, block.getY2());
 }

 @Test
 public void testBlockConstructorWithInvalidCoordinates() {
 assertThrows(IllegalArgumentException.class, () -> new Block(new Scheme(10, 10), -1, 1, 11, 10));
 }

 @Test
 public void testSubblockWithValidCoordinates() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme);
 Block subblock = block.subblock(0, 0, 3, 3);
 assertEquals(3, subblock.getX1());
 assertEquals(3, subblock.getY1());
 assertEquals(6, subblock.getX2());
 assertEquals(6, subblock.getY2());
 assertEquals(scheme, subblock.getScheme());
 }

 @Test
 public void testSubblockWithOutOfBlockCoordinates() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 assertThrows(IllegalArgumentException.class, () -> block.subblock(6, 6, 3, 3));
 }

 @Test
 public void testCharAtWithValidPosition() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 assertEquals(' ', block.charAt(1, 1));
 }

 @Test
 public void testCharAtWithOutOfBlockPosition() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 assertThrows(IllegalArgumentException.class, () -> block.charAt(6, 6));
 }

 @Test
 public void testTextAtWithValidPosition() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 List<String> expected = Arrays.asList("abc", "def", "ghi");
 String text = block.textAt(1, 4, 2);
 assertEquals(expected.get(1), text);
 }

 @Test
 public void testTextAtWithOutOfBlockPosition() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 assertThrows(IllegalArgumentException.class, () -> block.textAt(6, 6, 2));
 }

 @Test
 public void testContainsOnlyOneWordWithSingleWordBlock() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 block.setCharAt(1, 1, 'a');
 block.setCharAt(2, 1, 'b');
 block.setCharAt(3, 1, 'c');
 block.setCharAt(4, 1, ' ');
 block.setCharAt(5, 1, 'e');
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordWithMultipleWordsBlock() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 block.setCharAt(1, 1, 'a');
 block.setCharAt(2, 1, 'b');
 block.setCharAt(3, 1, 'c');
 block.setCharAt(4, 1, ' ');
 block.setCharAt(5, 1, 'e');
 block.setCharAt(1, 2, 'd');
 block.setCharAt(2, 2, ' ');
 block.setCharAt(3, 2, 'f');
 block.setCharAt(4, 2, ' ');
 block.setCharAt(5, 2, 'g');
 assertFalse(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 List<Block> subblocks = block.extractSequentialSubblocks();
 assertEquals(3, subblocks.size());
 Block firstSubblock = subblocks.get(0);
 assertEquals(1, firstSubblock.getX1());
 assertEquals(1, firstSubblock.getY1());
 assertEquals(3, firstSubblock.getX2());
 assertEquals(3, firstSubblock.getY2());
 Block secondSubblock = subblocks.get(1);
 assertEquals(3, secondSubblock.getX1());
 assertEquals(3, secondSubblock.getY1());
 assertEquals(3, secondSubblock.getX2());
 assertEquals(6, secondSubblock.getY2());
 Block thirdSubblock = subblocks.get(2);
 assertEquals(3, thirdSubblock.getX1());
 assertEquals(6, thirdSubblock.getY1());
 assertEquals(3, thirdSubblock.getX2());
 assertEquals(6, thirdSubblock.getY2());
 }

 @Test
 public void testExtractParallelSubblocks() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 List<Block> subblocks = block.extractParallelSubblocks();
 assertEquals(3, subblocks.size());
 Block firstSubblock = subblocks.get(0);
 assertEquals(1, firstSubblock.getX1());
 assertEquals(1, firstSubblock.getY1());
 assertEquals(3, firstSubblock.getX2());
 assertEquals(3, firstSubblock.getY2());
 Block secondSubblock = subblocks.get(1);
 assertEquals(3, secondSubblock.getX1());
 assertEquals(3, secondSubblock.getY1());
 assertEquals(3, secondSubblock.getX2());
 assertEquals(6, secondSubblock.getY2());
 Block thirdSubblock = subblocks.get(2);
 assertEquals(3, thirdSubblock.getX1());
 assertEquals(6, thirdSubblock.getY1());
 assertEquals(3, thirdSubblock.getX2());
 assertEquals(6, thirdSubblock.getY2());
 }

 @Test
 public void testToString() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 block.setCharAt(1, 1, 'a');
 block.setCharAt(2, 1, 'b');
 block.setCharAt(3, 1, 'c');
 block.setCharAt(4, 1, ' ');
 block.setCharAt(5, 1, 'e');
 block.setCharAt(1, 2, 'd');
 block.setCharAt(2, 2, ' ');
 block.setCharAt(3, 2, 'f');
 block.setCharAt(4, 2, ' ');
 block.setCharAt(5, 2, 'g');
 String expected = "Block(0,0:)\n" +
 "# abcc #\n" +
 "# d    #\n" +
 "# fg#\n" +
 "#-----#";
 assertEquals(expected, block.toString());
 }

 @Test
 public void testGetUpperLine() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
 block.setCharAt(1, 1, 'a');
 block.setCharAt(2, 1, 'b');
 block.setCharAt(3, 1, 'c');
 block.setCharAt(4, 1, ' ');
 block.setCharAt(5, 1, 'e');
 assertEquals("abcc", block.getUpperLine());
 }
}
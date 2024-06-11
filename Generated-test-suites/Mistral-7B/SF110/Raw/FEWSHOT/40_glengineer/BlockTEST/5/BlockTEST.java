package glengineer.blocks;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

public class BlockTest {

 Scheme mockScheme;
 Block mockSubblock;
 List<Block> mockExtractedSubblocks;

 @Before
 public void setup() {
 mockScheme = Mockito.mock(Scheme.class);
 mockSubblock = Mockito.mock(Block.class);
 mockExtractedSubblocks = Arrays.asList(mockSubblock);
 }

 @Test
 public void testBlockWithValidCoordinates() {
 Block block = new Block(mockScheme, 1, 1, 3, 3);
 assertEquals(1, block.getX1());
 assertEquals(1, block.getY1());
 assertEquals(3, block.getX2());
 assertEquals(3, block.getY2());
 assertSame(mockScheme, block.getScheme());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockWithInvalidCoordinates() {
 Block block = new Block(mockScheme, -1, 1, 5, 3);
 fail("Should have thrown an IllegalArgumentException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockWithInvalidX2() {
 Block block = new Block(mockScheme, 1, 1, 3, 5);
 fail("Should have thrown an IllegalArgumentException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockWithInvalidY2() {
 Block block = new Block(mockScheme, 1, 1, 3, 4);
 Block schemeBlock = Mockito.spy(new Block(mockScheme));
 when(schemeBlock.getY2()).thenReturn(mockScheme.getY2());
 when(mockScheme.lines[0].length()).thenReturn(5);
 when(mockScheme.lines[0].substring(0, 1)).thenReturn("A");
 when(mockScheme.lines[1].length()).thenReturn(5);
 when(mockScheme.lines[1].substring(0, 1)).thenReturn("B");
 when(mockScheme.lines[1].substring(3, 5)).thenReturn("C");
 when(mockScheme.lines[2].length()).thenReturn(5);
 when(mockScheme.lines[2].substring(0, 1)).thenReturn("D");
 when(mockScheme.lines[2].substring(3, 5)).thenReturn("E");

 Block blockUnderTest = new Block(mockScheme, 1, 1, 3, 5);
 fail("Should have thrown an IllegalArgumentException.");
 }

 @Test
 public void testBlockWithDefaultCoordinates() {
 Block block = new Block(mockScheme);
 assertEquals(0, block.getX1());
 assertEquals(0, block.getY1());
 assertEquals(mockScheme.x2, block.getX2());
 assertEquals(mockScheme.y2, block.getY2());
 assertSame(mockScheme, block.getScheme());
 }

 @Test
 public void testSubblockWithValidCoordinates() {
 when(mockSubblock.getX1()).thenReturn(5);
 when(mockSubblock.getX2()).thenReturn(6);
 when(mockSubblock.getY1()).thenReturn(2);
 when(mockSubblock.getY2()).thenReturn(3);

 Block block = new Block(mockScheme, 1, 1, 3, 3);
 Block subblock = block.subblock(1, 2, 2, 3);
 assertEquals(5, subblock.getX1());
 assertEquals(2, subblock.getY1());
 assertEquals(6, subblock.getX2());
 assertEquals(3, subblock.getY2());
 assertSame(mockScheme, subblock.getScheme());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubblockWithInvalidCoordinates() {
 Block block = new Block(mockScheme, 1, 1, 3, 3);
 block.subblock(1, 0, 2, 3);
 fail("Should have thrown an IllegalArgumentException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubblockWithNegativeCoordinates() {
 Block block = new Block(mockScheme, 1, 1, 3, 3);
 block.subblock(-1, 0, 2, 3);
 fail("Should have thrown an IllegalArgumentException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubblockWithInvalidX() {
 Block block = new Block(mockScheme, 1, 1, 3, 3);
 block.subblock(1, 1, 0, 3);
 fail("Should have thrown an IllegalArgumentException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubblockWithInvalidY() {
 Block block = new Block(mockScheme, 1, 1, 3, 3);
 block.subblock(1, 1, 1, 0);
 fail("Should have thrown an IllegalArgumentException.");
 }

 @Test
 public void testExtractSequentialSubblocks() {
 when(mockScheme.getLines()).thenReturn(mockExtractedSubblocks.toArray(new Block[0]));

 Block block = new Block(mockScheme, 1, 1, 3, 3);
 List<Block> extractedSubblocks = block.extractSequentialSubblocks();
 assertEquals(1, extractedSubblocks.size());
 assertSame(mockSubblock, extractedSubblocks.get(0));
 }

 @Test
 public void testExtractParallelSubblocks() {
 when(mockScheme.getWidth()).thenReturn(5);
 when(mockScheme.getHeight()).thenReturn(5);
 when(mockScheme.getLines()).thenReturn(new Block[] { new Block(mockScheme), new Block(mockScheme), new Block(mockScheme), new Block(mockScheme), new Block(mockScheme) });

 Block block = new Block(mockScheme, 1, 1, 5, 5);
 List<Block> extractedSubblocks = block.extractParallelSubblocks();
 assertEquals(5, extractedSubblocks.size());
 for (Block subblock : extractedSubblocks) {
 assertEquals(1, subblock.getX1());
 assertEquals(1, subblock.getY1());
 assertEquals(5, subblock.getX2());
 assertEquals(1, subblock.getY2());
 }
 }

 @Test
 public void testToString() {
 Block block = new Block(mockScheme, 1, 1, 3, 3);
 String result = block.toString();
 assertTrue(result.contains("#"));
 assertTrue(result.contains("Block"));
 assertTrue(result.contains("Block("));
 assertTrue(result.contains("Block(" + 1 + "," + 1 + ", " + 3 + "," + 3 + "):"));
 }

 @Test
 public void testGetUpperLine() {
 Block block = new Block(mockScheme, 1, 1, 3, 3);
 String upperLine = block.getUpperLine();
 assertEquals("", upperLine);
 }

 @Test
 public void testContainsOnlyOneWord() {
 String singleWord = "HelloWorld";
 when(mockScheme.getLines()[0].length()).thenReturn(singleWord.length());
 when(mockScheme.getLines()[0].substring(0, singleWord.length())).thenReturn(singleWord);
 when(mockScheme.getLines()[1].length()).thenReturn(singleWord.length());
 when(mockScheme.getLines()[1].substring(0, singleWord.length())).thenReturn(singleWord);
 when(mockScheme.getLines()[1].substring(singleWord.length())).thenReturn("");

 Block block = new Block(mockScheme, 1, 1, 3, 3);
 assertTrue(block.containsOnlyOneWord());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsOnlyOneWordWithMultipleWords() {
 String multipleWords = "HelloWorld HelloThere";
 when(mockScheme.getLines()[0].length()).thenReturn(multipleWords.length());
 when(mockScheme.getLines()[0].substring(0, multipleWords.length())).thenReturn(multipleWords);
 when(mockScheme.getLines()[1].length()).thenReturn(multipleWords.length());
 when(mockScheme.getLines()[1].substring(0, multipleWords.length())).thenReturn(multipleWords);
 when(mockScheme.getLines()[1].substring(multipleWords.length())).thenReturn(" ");

 Block block = new Block(mockScheme, 1, 1, 3, 3);
 fail("Should have thrown an IllegalArgumentException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsOnlyOneWordWithEmptyBlock() {
 Block block = new Block(mockScheme, 1, 1, 3, 3);
 fail("Should have thrown an IllegalArgumentException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsOnlyOneWordWithTrivialBlock() {
 Block block = new Block(mockScheme);
 fail("Should have thrown an IllegalArgumentException.");
 }
}
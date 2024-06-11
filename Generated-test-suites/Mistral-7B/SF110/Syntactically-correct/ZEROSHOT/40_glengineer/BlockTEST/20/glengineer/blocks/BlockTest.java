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
 // Initialize Scheme and Block for testing
 scheme = new Scheme(10, 10);
 block = new Block(scheme, 1, 1, 5, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockInitializationWithImproperDefinition() {
 Block block = new Block(scheme, -1, 1, 5, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockInitializationWithImproperDefinition2() {
 Block block = new Block(scheme, 1, 1, 10, 10);
 }

 @Test
 public void testBlockInitialization() {
 Block block = new Block(scheme);
 assertEquals("Block(0, 0, 10, 10):\n-------------------\n #                 #\n #                 #\n #                 #\n #                 #\n #                 #\n #                 #\n -------------------\n", block.toString());
 }

 @Test
 public void testBlockInitializationWithCharPosition() {
 CharPosition p1 = new CharPosition(1, 1);
 CharPosition p2 = new CharPosition(5, 5);
 Block block = new Block(scheme, p1, p2);
 assertEquals("Block(1, 1, 5, 5):\n-------------------\n #                 #\n #                 #\n #                 #\n #                 #\n #                 #\n -------------------\n", block.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockSubblockWithImproperDefinition() {
 block.subblock(-1, 1, 5, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockSubblockWithImproperDefinition2() {
 block.subblock(1, 10, 5, 5);
 }

 @Test
 public void testBlockSubblock() {
 Block subblock = block.subblock(1, 1, 3, 3);
 assertEquals("Block(2, 2, 4, 4):\n-------------------\n #   #   #\n #   #   #\n #   #   #\n #   #   #\n #   #   #\n -------------------\n", subblock.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsOnlyOneWordWhenIsTrivial() {
 block = new Block(scheme);
 block.containsOnlyOneWord();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsOnlyOneWordWhenEmptyBlock() {
 block = new Block(scheme, 1, 1, 1, 1);
 block.containsOnlyOneWord();
 }

 @Test
 public void testContainsOnlyOneWord() {
 String expected = "testWord";
 for (int i = 0; i < block.x2 - block.x1; ++i) {
 for (int j = block.y1; j < block.y2; ++j) {
 char c = block.charAt(i, j);
 if (Character.isAlphabetic(c)) {
 StringBuilder sb = new StringBuilder(expected);
 sb.append(c);
 expected = sb.toString();
 }
 }
 }
 block.containsOnlyOneWord();
 assertEquals(expected, block.textAt(block.x1, block.x2, block.y2));
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> subblocks = block.extractSequentialSubblocks();
 // Add your assertions here
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> subblocks = block.extractParallelSubblocks();
 // Add your assertions here
 }
}
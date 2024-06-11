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
 block = new Block(scheme, 1, 1, 3, 3);
 }

 @Test
 public void testBlockConstructionWithInvalidArguments() {
 try {
 Block invalidBlock = new Block(scheme, -1, 1, 3, 3);
 fail("IllegalArgumentException should be thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 Block invalidBlock = new Block(scheme, 1, 1, 10, 10);
 fail("IllegalArgumentException should be thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBlockConstructionWithValidArguments() {
 assertEquals(1, block.getX1());
 assertEquals(1, block.getY1());
 assertEquals(3, block.getX2());
 assertEquals(3, block.getY2());
 assertEquals(scheme, block.getScheme());
 }

 @Test
 public void testBlockConstructionWithCharPositionArguments() {
 CharPosition p1 = new CharPosition(1, 1);
 CharPosition p2 = new CharPosition(3, 3);
 block = new Block(scheme, p1, p2);
 assertEquals(p1.x, block.getX1());
 assertEquals(p1.y, block.getY1());
 assertEquals(p2.x, block.getX2());
 assertEquals(p2.y, block.getY2());
 }

 @Test
 public void testBlockConstructionWithSchemeOnly() {
 block = new Block(scheme);
 assertEquals(0, block.getX1());
 assertEquals(0, block.getY1());
 assertEquals(scheme.x2, block.getX2());
 assertEquals(scheme.y2, block.getY2());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockSubblockWithInvalidArguments() {
 block.subblock(-1, 0, 0, 0);
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testExtractSequentialSubblocks() {
 List<Block> expectedSubblocks = new ArrayList<>();
 expectedSubblocks.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 2)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(3, 3), new CharPosition(3, 3)));
 List<Block> actualSubblocks = block.extractSequentialSubblocks();
 assertEquals(expectedSubblocks.size(), actualSubblocks.size());
 for (int i = 0; i < expectedSubblocks.size(); ++i) {
 Block expectedSubblock = expectedSubblocks.get(i);
 Block actualSubblock = actualSubblocks.get(i);
 assertEquals(expectedSubblock.getX1(), actualSubblock.getX1());
 assertEquals(expectedSubblock.getY1(), actualSubblock.getY1());
 assertEquals(expectedSubblock.getX2(), actualSubblock.getX2());
 assertEquals(expectedSubblock.getY2(), actualSubblock.getY2());
 assertEquals(expectedSubblock.getScheme(), actualSubblock.getScheme());
 }
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testExtractParallelSubblocks() {
 List<Block> expectedSubblocks = new ArrayList<>();
 expectedSubblocks.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(1, 3)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(2, 1), new CharPosition(3, 1)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(3, 1), new CharPosition(3, 3)));
 List<Block> actualSubblocks = block.extractParallelSubblocks();
 assertEquals(expectedSubblocks.size(), actualSubblocks.size());
 for (int i = 0; i < expectedSubblocks.size(); ++i) {
 Block expectedSubblock = expectedSubblocks.get(i);
 Block actualSubblock = actualSubblocks.get(i);
 assertEquals(expectedSubblock.getX1(), actualSubblock.getX1());
 assertEquals(expectedSubblock.getY1(), actualSubblock.getY1());
 assertEquals(expectedSubblock.getX2(), actualSubblock.getX2());
 assertEquals(expectedSubblock.getY2(), actualSubblock.getY2());
 assertEquals(expectedSubblock.getScheme(), actualSubblock.getScheme());
 }
 }

 @Test
 public void testContainsOnlyOneWord() {
 String singleWordBlockText = "The quick brown fox jumps over the lazy dog.";
 block = new Block(scheme, singleWordBlockText);
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWord_failure() {
 String multipleWordsBlockText = "The quick brown fox jumps over the lazy dog. The cat is sleeping.";
 block = new Block(scheme, multipleWordsBlockText);
 assertFalse(block.containsOnlyOneWord());
 }
}
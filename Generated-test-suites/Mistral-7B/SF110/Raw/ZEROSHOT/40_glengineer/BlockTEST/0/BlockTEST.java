import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import glengineer.blocks.Block;
import glengineer.blocks.Scheme;
import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import glengineer.positions.WordPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class BlockTest {

 private Scheme scheme;
 private Block block1;
 private Block block2;
 private Block block3;

 @Before
 public void setUp() {
 scheme = new Scheme(10, 10);
 block1 = new Block(scheme, 1, 1, 5, 5);
 block2 = new Block(scheme, 1, 1, 5, 3);
 block3 = new Block(scheme, 1, 1, 10, 10);
 }

 @Test
 public void constructorWithInvalidX1Y1X2Y2Test() {
 assertThrows(IllegalArgumentException.class, () -> new Block(scheme, -1, -1, 5, 5));
 assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 1, 1, 10, 11));
 assertThrows(IllegalArgumentException.class, () -> new Block(scheme, 5, 5, 1, 1));
 }

 @Test
 public void constructorWithValidX1Y1X2Y2Test() {
 assertEquals(1, block1.getX1());
 assertEquals(1, block1.getY1());
 assertEquals(5, block1.getX2());
 assertEquals(5, block1.getY2());
 }

 @Test
 public void constructorWithCharPositionTest() {
 CharPosition p1 = new CharPosition(1, 1);
 CharPosition p2 = new CharPosition(5, 5);
 Block block4 = new Block(scheme, p1, p2);
 assertEquals(p1.getX(), block4.getX1());
 assertEquals(p1.getY(), block4.getY1());
 assertEquals(p2.getX(), block4.getX2());
 assertEquals(p2.getY(), block4.getY2());
 }

 @Test
 public void constructorWithValidSchemeTest() {
 assertEquals(scheme, block1.getScheme());
 }

 @Test
 public void constructorWithTrivialSchemeTest() {
 Block block5 = new Block(scheme);
 assertEquals(0, block5.getX1());
 assertEquals(0, block5.getY1());
 assertEquals(scheme.getX2(), block5.getX2());
 assertEquals(scheme.getY2(), block5.getY2());
 }

 @Test
 public void subblockWithInvalidParametersTest() {
 assertThrows(IllegalArgumentException.class, () -> block1.subblock(1, 2, 3, 4));
 }

 @Test
 public void subblockWithValidParametersTest() {
 Block subblock = block1.subblock(1, 1, 2, 2);
 assertEquals(2, subblock.getX2() - subblock.getX1());
 assertEquals(2, subblock.getY2() - subblock.getY1());
 }

 @Test
 public void charAtTest() {
 assertEquals(' ', block1.charAt(0, 0));
 assertEquals('A', block1.charAt(2, 2));
 assertThrows(IllegalArgumentException.class, () -> block1.charAt(-1, 0));
 assertThrows(IllegalArgumentException.class, () -> block1.charAt(6, 6));
 }

 @Test
 public void textAtTest() {
 assertEquals("", block1.textAt(0, 0, block1.getY1()));
 assertEquals("ABCDE", block1.textAt(1, 5, block1.getY2()));
 }

 @Test
 public void containsOnlyOneWordTest() {
 assertTrue(block2.containsOnlyOneWord());
 assertFalse(block3.containsOnlyOneWord());
 }

 @Test
 public void extractSequentialSubblocksTest() {
 List<Block> expectedSubblocks = new ArrayList<>(Arrays.asList(block2, block1));
 List<Block> actualSubblocks = block3.extractSequentialSubblocks();
 assertEquals(expectedSubblocks, actualSubblocks);
 }

 @Test
 public void extractParallelSubblocksTest() {
 List<Block> expectedSubblocks = new ArrayList<>(Arrays.asList(block1, block2));
 List<Block> actualSubblocks = block3.extractParallelSubblocks();
 assertEquals(expectedSubblocks, actualSubblocks);
 }

 @Test
 public void toStringTest() {
 assertEquals("Block(1, 1, 5, 5):\n------------------------\n # A B C D E #\n # F G H I J #\n # K L M N O #\n # P Q R S T #\n # U V W X Y #\n ------------------------\n", block1.toString());
 }

 @Test
 public void getUpperLineTest() {
 assertEquals("ABCDE", block1.getUpperLine());
 }
}
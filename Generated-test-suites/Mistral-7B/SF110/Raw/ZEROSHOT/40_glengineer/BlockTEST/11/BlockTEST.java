import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import glengineer.blocks.Block;
import glengineer.blocks.BlockFactory;
import glengineer.positions.CharPosition;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BlockTest {

 private Block block;
 private Scheme scheme;
 private CharPosition p1, p2;

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 // Constructor with x1, y1, x2, y2
 new Object[] { new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(3, 3), new CharPosition(5, 5) },
 // Constructor with CharPosition p1, p2
 new Object[] { new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(3, 3) },
 // Constructor with Scheme scheme
 new Object[] { new Scheme(10, 10) }
 });
 }

 @Parameters(name = "{index}: Block({0})")
 public Block getBlock(Scheme scheme, CharPosition... positions) {
 this.scheme = scheme;
 if (positions.length == 2) {
 p1 = positions[0];
 p2 = positions[1];
 }
 block = new Block(scheme, p1, p2);
 return block;
 }

 @Test
 public void constructorTest() {
 assertEquals(scheme.x2, block.x2);
 assertEquals(scheme.y2, block.y2);
 assertEquals(scheme, block.scheme);
 }

 @Test(expected = IllegalArgumentException.class)
 public void constructorWithInvalidBlockDefinitionTest() {
 scheme = new Scheme(5, 5);
 CharPosition p = new CharPosition(-1, 0);
 Block testBlock = new Block(scheme, p, new CharPosition(scheme.x2, scheme.y2));
 fail(); // This line will not be reached
 }

 @Test
 public void charAtTest() {
 CharPosition p = new CharPosition(2, 2);
 assertEquals(' ', block.charAt(p.x, p.y));
 }

 @Test(expected = IllegalArgumentException.class)
 public void charAtOutsideBlockTest() {
 CharPosition p = new CharPosition(-1, 0);
 try {
 block.charAt(p.x, p.y);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void textAtTest() {
 String expected = "Hello World";
 CharPosition charPosition1 = new CharPosition(0, 0);
 CharPosition charPosition2 = new CharPosition(expected.length(), 0);
 String actual = block.textAt(charPosition1.x, charPosition2.x, charPosition1.y);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void textAtInvalidSublineCoordinatesTest() {
 fail(); // This line will not be reached
 }

 @Test
 public void containsOnlyOneWordTest() {
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void isTrivialTest() {
 if (block instanceof TrivialBlock) {
 assertTrue(block.containsOnlyOneWord());
 } else {
 assertFalse(block.containsOnlyOneWord());
 }
 }

 @Test
 public void firstWord_HorizontallyTest() {
 HWordPosition expected = new HWordPosition(new CharPosition(2, 2), new CharPosition(4, 2), new CharPosition(6, 2), new CharPosition(8, 2), new CharPosition(10, 2));
 HWordPosition actual = block.firstWord_Horizontally();
 assertEquals(expected.x1, actual.x1);
 assertEquals(expected.x2, actual.x2);
 assertEquals(expected.y, actual.y);
 }

 @Test
 public void lastWord_HorizontallyTest() {
 WordPosition expected = new WordPosition(new CharPosition(2, 3), new CharPosition(4, 3), new CharPosition(6, 3), new CharPosition(8, 3), new CharPosition(10, 3));
 WordPosition actual = block.lastWord_Horizontally();
 assertEquals(expected.x1, actual.x1);
 assertEquals(expected.x2, actual.x2);
 assertEquals(expected.y, actual.y);
 }

 @Test
 public void extractSequentialSubblocksTest() {
 List<Block> expected = new ArrayList<>();
 Block subBlock1 = new BlockFactory().createSubBlock(BlockType.TEXT, new CharPosition(0, 0), new CharPosition(5, 5));
 Block subBlock2 = new BlockFactory().createSubBlock(BlockType.TEXT, new CharPosition(6, 0), new CharPosition(10, 5));
 expected.add(subBlock1);
 expected.add(subBlock2);
 List<Block> actual = block.extractSequentialSubblocks();
 assertEquals(expected, actual);
 }

 @Test
 public void extractParallelSubblocksTest() {
 List<Block> expected = new ArrayList<>();
 Block subBlock1 = new BlockFactory().createSubBlock(BlockType.TEXT, new CharPosition(0, 0), new CharPosition(5, 5));
 Block subBlock2 = new BlockFactory().createSubBlock(BlockType.TEXT, new CharPosition(0, 5), new CharPosition(5, 10));
 expected.add(subBlock1);
 expected.add(subBlock2);
 List<Block> actual = block.extractParallelSubblocks();
 assertEquals(expected, actual);
 }

 @Test
 public void toStringTest() {
 String expected = "Block(0,0):\n-------------------\n # Hello World #\n # Hello World #\n-------------------";
 String actual = block.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void getUpperLineTest() {
 String expected = "Hello World";
 String actual = block.getUpperLine();
 assertEquals(expected, actual);
 }
}
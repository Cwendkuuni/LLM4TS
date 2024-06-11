import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setUp() {
 scheme = new Scheme(10, 5);
 block = new Block(scheme);
 }

 @Test
 public void testBlockConstructionWithX1Y1X2Y2() {
 Block testBlock = new Block(scheme, 1, 2, 3, 4);
 assertEquals(1, testBlock.getX1());
 assertEquals(2, testBlock.getY1());
 assertEquals(3, testBlock.getX2());
 assertEquals(4, testBlock.getY2());
 }

 @Test
 public void testBlockConstructionWithP1P2() {
 CharPosition p1 = new CharPosition(1, 2);
 CharPosition p2 = new CharPosition(3, 4);
 Block testBlock = new Block(scheme, p1, p2);
 assertEquals(p1.getX(), testBlock.getX1());
 assertEquals(p1.getY(), testBlock.getY1());
 assertEquals(p2.getX(), testBlock.getX2());
 assertEquals(p2.getY(), testBlock.getY2());
 }

 @Test
 public void testBlockConstructionWithSchemeOnly() {
 Block testBlock = new Block(scheme);
 assertEquals(0, testBlock.getX1());
 assertEquals(0, testBlock.getY1());
 assertEquals(scheme.getX2(), testBlock.getX2());
 assertEquals(scheme.getY2(), testBlock.getY2());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructionWithImproperCoordinates() {
 Block testBlock = new Block(scheme, -1, 2, 10, 10);
 fail("Block creation should not be successful with improper coordinates.");
 }

 @Test
 public void testSubblockCreation() {
 fail("Abstract class cannot be instantiated or test methods for subclasses must be implemented.");
 }

 @Test
 public void testCharAt() {
 CharPosition charPosition = new CharPosition(1, 2);
 char expectedCharacter = 'A';
 scheme.lines[charPosition.getY()] = new StringBuilder(scheme.lines[charPosition.getY()]).replace(charPosition.getX(), charPosition.getX() + 1, "A").toString().toCharArray();
 char actualCharacter = block.charAt(charPosition.getX(), charPosition.getY());
 assertEquals(expectedCharacter, actualCharacter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharAtOutOfBlock() {
 CharPosition charPosition = new CharPosition(5, 5);
 block.charAt(charPosition.getX(), charPosition.getY());
 fail("Method should throw exception when given char position is out of block.");
 }

 @Test
 public void testTextAt() {
 String expectedText = "Hello World";
 scheme.lines[1] = new StringBuilder(scheme.lines[1]).append(expectedText).toString();
 String actualText = block.textAt(0, 5, 1);
 assertEquals(expectedText, actualText);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextAtImproperCoordinates() {
 block.textAt(0, 5, 5);
 fail("Method should throw exception when given improper subline coordinates.");
 }

 @Test
 public void testContainsOnlyOneWord() {
 String singleWord = "Hello";
 scheme.lines[1] = new StringBuilder(singleWord).toString();
 scheme.lines[2] = new StringBuilder().toString();
 scheme.lines[3] = new StringBuilder(singleWord).toString();
 List<Block> blocks = new ArrayList<>();
 blocks.add(new Block(scheme, 0, 1, 5, 3));
 boolean result = blocks.get(0).containsOnlyOneWord();
 assertTrue(result);
 }

 @Test
 public void testExtractSequentialSubblocks() {
 fail("Abstract class cannot instantiate or test methods for subclasses must be implemented.");
 }

 @Test
 public void testExtractParallelSubblocks() {
 fail("Abstract class cannot instantiate or test methods for subclasses must be implemented.");
 }

 @Test
 public void testToString() {
 String expectedString = "Block(0, 0, 10, 5):\n#################\n # H e l l o   #\n # W o r l d   #\n ####################\n";
 String actualString = block.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testGetUpperLine() {
 String expectedUpperLine = "H e l l o";
 String actualUpperLine = block.getUpperLine();
 assertEquals(expectedUpperLine, actualUpperLine);
 }
}
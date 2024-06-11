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
 public void testBlockConstructionWithCoordinates() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
 assertEquals(5, block.getX2());
 assertEquals(5, block.getY2());
 assertEquals(scheme, block.getScheme());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlockConstructionWithInvalidCoordinates() {
 Scheme scheme = new Scheme(10, 10);
 try {
 Block block = new Block(scheme, -1, 0, 5, 5);
 fail();
 } catch (IllegalArgumentException iae) {
 // expected
 }
 }

 @Test
 public void testBlockConstructionWithScheme() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme);
 assertEquals(10, block.getX2());
 assertEquals(10, block.getY2());
 assertEquals(scheme, block.getScheme());
 }

 @Test
 public void testSubblockConstruction() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
 Block subblock = block.subblock(1, 1, 2, 2);
 Block expectedSubblock = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
 assertEquals(expectedSubblock.getX2(), subblock.getX2());
 assertEquals(expectedSubblock.getY2(), subblock.getY2());
 assertEquals(scheme, subblock.getScheme());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAbstractSubblockMethod() {
 Block block = new Block(new Scheme(10, 10));
 try {
 List<Block> subblocks = block.extractSequentialSubblocks();
 fail();
 } catch (UnsupportedOperationException uoe) {
 // expected
 }
 }

 @Test
 public void testExtractSequentialSubblocks() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
 List<Block> subblocks = block.extractSequentialSubblocks();
 List<Block> expectedSubblocks = new ArrayList<>();
 expectedSubblocks.add(new Block(scheme, new CharPosition(0, 0), new CharPosition(1, 1)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(1, 1), new CharPosition(2, 2)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(2, 2), new CharPosition(3, 3)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(3, 3), new CharPosition(4, 4)));
 expectedSubblocks.add(new Block(scheme, new CharPosition(4, 4), new CharPosition(5, 5)));
 assertEquals(expectedSubblocks, subblocks);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testExtractParallelSubblocks() {
 Block block = new Block(new Scheme(10, 10));
 try {
 List<Block> subblocks = block.extractParallelSubblocks();
 fail();
 } catch (UnsupportedOperationException uoe) {
 // expected
 }
 }

 @Test
 public void testExtractParallelSubblocksMethod() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
 List<Block> subblocks = new ArrayList<>();
 subblocks.add(new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 0)));
 subblocks.add(new Block(scheme, new CharPosition(0, 1), new CharPosition(5, 1)));
 subblocks.add(new Block(scheme, new CharPosition(0, 2), new CharPosition(5, 2)));
 subblocks.add(new Block(scheme, new CharPosition(0, 3), new CharPosition(5, 3)));
 subblocks.add(new Block(scheme, new CharPosition(0, 4), new CharPosition(5, 4)));
 List<Block> expectedSubblocks = Arrays.asList(subblocks.toArray(new Block[0]));
 assertEquals(expectedSubblocks, block.extractParallelSubblocks());
 }

 @Test
 public void testGetUpperLine() {
 Scheme scheme = new Scheme(10, 10);
 Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
 String upperLine = block.getUpperLine();
 String expectedUpperLine = "Block(0,0,5,5):\n#########\n # ABCDE #\n # FGHIJK #\n # LMNOPM #\n ########";
 assertEquals(expectedUpperLine, upperLine);
 }
}
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTest {

 @Test
 public void testBlockConstructionWithCoordinates() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(5, 3));
 assertEquals(1, block.getX1());
 assertEquals(1, block.getY1());
 assertEquals(5, block.getX2());
 assertEquals(3, block.getY2());
 }

 @Test
 public void testBlockConstructionWithScheme() {
 Block block = new Block(new Scheme(10, 10));
 assertEquals(0, block.getX1());
 assertEquals(0, block.getY1());
 assertEquals(10, block.getX2());
 assertEquals(10, block.getY2());
 }

 @Test
 public void testBlockConstructionWithInvalidCoordinates() {
 Scheme scheme = new Scheme(10, 10);
 try {
 new Block(scheme, -1, 0, 10, 10);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("improper block definition: -1, 0, 10, 10.", e.getMessage());
 }

 try {
 new Block(scheme, 11, 0, 10, 10);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("improper block definition: 11, 0, 10, 10.", e.getMessage());
 }

 try {
 new Block(scheme, 0, 11, 10, 10);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("improper block definition: 0, 11, 10, 10.", e.getMessage());
 }

 try {
 new Block(scheme, 1, 1, 11, 11);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("improper block definition: 1, 1, 11, 11.", e.getMessage());
 }
}

 @Test
 public void testBlockSubblock() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(5, 3));
 Block subblock = block.subblock(0, 2, 3, 3);
 Block expectedSubblock = new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(3, 3));
 assertEquals(expectedSubblock, subblock);
}

 @Test(expected = UnsupportedOperationException.class)
 public void testAbstractSubblockMethod() {
 Block block = new Block(new Scheme(10, 10));
 List<Block> subblocks = block.extractSequentialSubblocks();
 subblocks.get(0).subblock(0, 0, 0, 0);
}

 @Test
 public void testBlockTextAt() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(5, 3));
 assertEquals("Block(1, 1, 5, 3):\n-------------------\n# ABCDE #\n# FGHIJKL #\n# MNOPQRL #\n-------------------\n", block.toString());
 assertEquals("ABCDE", block.textAt(1, 5, 0));
 assertEquals("FGHIJKL", block.textAt(1, 5, 1));
 assertEquals("MNOPQRL", block.textAt(1, 5, 2));
}

 @Test
 public void testBlockContainsOnlyOneWord() {
 Block block1 = new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(5, 3));
 assertFalse(block1.containsOnlyOneWord());

 Block block2 = new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(5, 1));
 assertTrue(block2.containsOnlyOneWord());
}

 @Test
 public void testBlockExtractSequentialSubblocks() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(5, 3));
 List<Block> subblocks = block.extractSequentialSubblocks();
 List<Block> expectedSubblocks = new ArrayList<>();
 expectedSubblocks.add(new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(3, 3)));
 expectedSubblocks.add(new Block(new Scheme(10, 10), new CharPosition(3, 3), new CharPosition(5, 3)));
 assertEquals(expectedSubblocks, subblocks);
}

 @Test
 public void testBlockExtractParallelSubblocks() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(5, 3));
 List<Block> subblocks = block.extractParallelSubblocks();
 List<Block> expectedSubblocks = new ArrayList<>();
 expectedSubblocks.add(new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(1, 3)));
 expectedSubblocks.add(new Block(new Scheme(10, 10), new CharPosition(2, 1), new CharPosition(2, 3)));
 expectedSubblocks.add(new Block(new Scheme(10, 10), new CharPosition(3, 1), new CharPosition(3, 3)));
 expectedSubblocks.add(new Block(new Scheme(10, 10), new CharPosition(4, 1), new CharPosition(4, 3)));
 expectedSubblocks.add(new Block(new Scheme(10, 10), new CharPosition(5, 1), new CharPosition(5, 3)));
 assertEquals(expectedSubblocks, subblocks);
}

 @Test
 public void testBlockToString() {
 Block block = new Block(new Scheme(10, 10), new CharPosition(1, 1), new CharPosition(5, 3));
 assertEquals("Block(1, 1, 5, 3)\n-------------------\n# ABCDE \n# FGHIJKL \n# MNOPQRL \n-------------------\n", block.toString());
}
}
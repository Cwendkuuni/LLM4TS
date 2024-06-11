import org.junit.Test;
import org.junit.Before;
import java.util.*;
import static org.junit.Assert.*;
import glengineer.blocks.*;
import glengineer.positions.*;

public class BlockTest {

 private Scheme scheme;
 private Block block1;
 private Block block2;
 private Block block3;

 @Before
 public void setUp() {
 scheme = new Scheme(5, 5);
 block1 = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3));
 block2 = new Block(scheme, 1, 1, 3, 3);
 block3 = new Block(scheme);
 }

 @Test
 public void testBlockConstructionWithInvalidArguments() {
 try {
 new Block(scheme, -1, -1, 5, 5);
 fail("Exception should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new Block(scheme, 5, 5, 0, 0);
 fail("Exception should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new Block(scheme, 5, 5, 5, 5 + 1);
 fail("Exception should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new Block(scheme, 5, 5, scheme.x2 + 1, 5);
 fail("Exception should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBlockConstructionWithValidArguments() {
 assertEquals("Block(1,1,3,3):", block1.toString());
 assertEquals("Block(1,1,3,3):", block2.toString());
 }

 @Test
 public void testBlockConstructionWithNullArguments() {
 try {
 new Block(null, new CharPosition(1, 1), new CharPosition(3, 3));
 fail("Exception should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new Block(scheme, null, new CharPosition(3, 3));
 fail("Exception should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new Block(scheme, new CharPosition(1, 1), null);
 fail("Exception should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBlockSubblock() {
 Block subblock = block1.subblock(1, 1, 2, 2);
 assertEquals("Block(2,2,2,2):", subblock.toString());
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testAbstractMethod() {
 block1.extractSequentialSubblocks();
 }

 @Test
 public void testContainsOnlyOneWord() {
 String text = "the quick brown fox jumps over the lazy dog";
 block3.setText(text);
 assertTrue(block3.containsOnlyOneWord());
 }

 @Test
 public void testContainsOnlyOneWordFailure() {
 String text = "the quick brown fox jumps over the quick brown fox";
 block3.setText(text);
 assertFalse(block3.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocks() {
 List<Block> subblocks = block1.extractSequentialSubblocks();
 assertEquals(2, subblocks.size());
 }

 @Test
 public void testExtractParallelSubblocks() {
 List<Block> subblocks = block1.extractParallelSubblocks();
 assertEquals(1, subblocks.size());
 }

}
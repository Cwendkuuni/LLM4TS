package glengineer.blocks;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import glengineer.positions.*;
import org.mockito.Mockito;

public class BlockTest {

 private Scheme scheme;
 private Block block;

 @Before
 public void setup() {
 scheme = Mockito.mock(Scheme.class);
 block = new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3)) {
 @Override
 public Block subblock(final int p0, final int p1, final int p2, final int p3) {
 throw new UnsupportedOperationException();
 }
 };
 }

 @Test
 public void testConstructorWithX1Y1X2Y2ThrowsIllegalArgumentException() {
 IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
 new Block(scheme, -1, 1, 3, 3);
 });
 assertEquals("improper block definition: -1,1,3,3.", exception.getMessage());
 }

 @Test
 public void testConstructorWithX1Y1X2Y2Valid() {
 assertEquals(scheme, block.scheme);
 assertEquals(1, block.x1);
 assertEquals(1, block.y1);
 assertEquals(3, block.x2);
 assertEquals(3, block.y2);
 }

 @Test
 public void testConstructorWithP1P2ThrowsIllegalArgumentException() {
 CharPosition p1 = new CharPosition(-1, 1);
 CharPosition p2 = new CharPosition(3, -1);
 IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
 new Block(scheme, p1, p2);
 });
 assertEquals("improper block definition:" + p1 + "," + p2 + ".", exception.getMessage());
 }

 @Test
 public void testConstructorWithP1P2Valid() {
 assertEquals(scheme, block.scheme);
 assertEquals(1, block.x1);
 assertEquals(1, block.y1);
 assertEquals(3, block.x2);
 assertEquals(3, block.y2);
 }

 @Test
 public void testConstructorWithSchemeOnly() {
 assertEquals(scheme, block.scheme);
 assertEquals(0, block.x1);
 assertEquals(0, block.y1);
 assertEquals(scheme.x2, block.x2);
 assertEquals(scheme.y2, block.y2);
 }

 @Test
 public void testSubblockThrowsUnsupportedOperationException() {
 assertThrows(UnsupportedOperationException.class, block::subblock);
 }

 @Test
 public void testCharAt() {
 char expected = 'A';
 when(scheme.lines[0].charAt(0)).thenReturn(expected);
 assertEquals(expected, block.charAt(0, 0));
 }

 @Test
 public void testTextAt() {
 String expected = "hello";
 when(scheme.lines[0].substring(0, 6)).thenReturn(expected);
 assertEquals(expected, block.textAt(0, 6, 0));
 }

 @Test
 public void testContainsOnlyOneWordThrowsIllegalArgumentExceptionWhenIsTrivial() {
 assertThrows(IllegalArgumentException.class, block::containsOnlyOneWord);
 }

 @Test
 public void testContainsOnlyOneWordReturnsTrue() {
 String expected = "hello world";
 when(scheme.lines[0].substring(0, 11)).thenReturn(expected);
 assertTrue(block.containsOnlyOneWord());
 }

 @Test
 public void testExtractSequentialSubblocksReturnsListWithTwoBlocks() {
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(1, 1), new CharPosition(3, 3)) {
 @Override
 public Block subblock(final int p0, final int p1, final int p2, final int p3) {
 throw new UnsupportedOperationException();
 }
 },
 new Block(scheme, new CharPosition(1, 4), new CharPosition(3, 4)) {
 @Override
 public Block subblock(final int p0, final int p1, final int p2, final int p3) {
 throw new UnsupportedOperationException();
 }
 }
 );
 when(scheme.lines[0].substring(0, 4)).thenReturn("foo");
 when(scheme.lines[0].substring(5, 9)).thenReturn("bar");
 List<Block> actualSubblocks = block.extractSequentialSubblocks();
 assertEquals(expectedSubblocks, actualSubblocks);
 }

 @Test
 public void testExtractParallelSubblocksReturnsListWithTwoBlocks() {
 List<Block> expectedSubblocks = Arrays.asList(
 new Block(scheme, new CharPosition(1, 1), new CharPosition(1, 3)) {
 @Override
 public Block subblock(final int p0, final int p1, final int p2, final int p3) {
 throw new UnsupportedOperationException();
 }
 },
 new Block(scheme, new CharPosition(2, 1), new CharPosition(3, 3)) {
 @Override
 public Block subblock(final int p0, final int p1, final int p2, final int p3) {
 throw new UnsupportedOperationException();
 }
 }
 );
 when(scheme.lines[0].substring(0, 3)).thenReturn("foo");
 when(scheme.lines[1].substring(1, 4)).thenReturn("bar");
 List<Block> actualSubblocks = block.extractParallelSubblocks();
 assertEquals(expectedSubblocks, actualSubblocks);
 }

 @Test
 public void testToString() {
 String expected = "Block(1,1,3,3):\n------------------\n # foo #\n # bar #\n------------------";
 assertEquals(expected, block.toString());
 }

 @Test
 public void testGetUpperLine() {
 String expected = "foo";
 assertEquals(expected, block.getUpperLine());
 }
}
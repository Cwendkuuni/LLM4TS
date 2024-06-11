import org.junit.Before;
import org.junit.Test;
import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 private CharMatcher charMatcher;
 private BitSet expectedTable;

 @Before
 public void setUp() {
 charMatcher = CharMatcher.from(BitSet.create().add('a').add('b').add('c'), "matches 'a', 'b', or 'c'.");
 expectedTable = BitSet.create();
 expectedTable.add('a');
 expectedTable.add('b');
 expectedTable.add('c');
 }

 @Test
 public void testMatches() {
 assertFalse(charMatcher.matches('\0'));
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertTrue(charMatcher.matches('c'));
 assertFalse(charMatcher.matches('d'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromThrowsIAE_EmptyBitSet() {
 CharMatcher.from(BitSet.create(), "empty bitset test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromThrowsIAE_NegativeBitSet() {
 BitSet negativeBitSet = BitSet.create();
 negativeBitSet.add(-1);
 CharMatcher.from(negativeBitSet, "negative bitset test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromThrowsIAE_NullDescription() {
 CharMatcher.from(expectedTable, null);
 }

 @Test
 public void testSetBits() {
 BitSet actualTable = BitSet.create();
 charMatcher.setBits(actualTable);
 assertTrue(actualTable.equals(expectedTable));
 }

 @Test
 public void testMatchesMultipleCharacters() {
 CharMatcher multipleCharsMatcher = CharMatcher.from(BitSet.create().add('a').add('b').add('c').add('d'), "matches 'a', 'b', 'c' or 'd'.");
 char multipleChar = 'c';
 assertTrue(multipleCharsMatcher.matches(multipleChar));
 }

 @Test
 public void testMatchesEmptyTable() {
 CharMatcher emptyTableMatcher = CharMatcher.from(BitSet.create(), "empty table test");
 assertFalse(emptyTableMatcher.matches('\0'));
 }
}
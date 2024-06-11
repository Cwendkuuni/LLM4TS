import org.junit.Test;
import org.junit.Assert.*;
import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;

public class SmallCharMatcherTest {

 @Test
 public void testMatchesCharacterInTable() {
 char[] table = new char[]{'a', 'b', 'c', 'd'};
 CharMatcher charMatcher = CharMatcher.from(new BitSet(Arrays.asList(0, 2, 3)), "Matches character 'b'");
 assertTrue(charMatcher.matches('b'));
 }

 @Test
 public void testMatchesCharacterNotInTable() {
 char[] table = new char[]{'a', 'b', 'c', 'd'};
 CharMatcher charMatcher = CharMatcher.from(new BitSet(Arrays.asList(0, 2, 3)), "Matches character 'e'");
 assertFalse(charMatcher.matches('e'));
 }

 @Test
 public void testMatchesCharacterWithFilter() {
 char[] table = new char[]{'0', 'a', 'b'};
 CharMatcher charMatcher = new SmallCharMatcher(table, 0b1111_0101_0110_1101_1111_1111_1111_0111, true, "Matches character '1' with filter");
 assertTrue(charMatcher.matches('1'));
 }

 @Test
 public void testMatchesCharacterWithoutFilter() {
 char[] table = new char[]{'0', 'a', 'b'};
 CharMatcher charMatcher = new SmallCharMatcher(table, 0, false, "Matches character 'a' without filter");
 assertTrue(charMatcher.matches('a'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTable() {
 CharMatcher charMatcher = CharMatcher.from(new BitSet(), "Empty table");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeIndexTable() {
 char[] table = new char[]{'a', 'b', 'c', 'd'};
 CharMatcher charMatcher = CharMatcher.from(new BitSet(Arrays.asList(-1)), "Negative index in table");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testSetBits() {
 char[] table = new char[]{'a', 'b', 'c', 'd'};
 BitSet bitSet = new BitSet();
 CharMatcher charMatcher = CharMatcher.from(new BitSet(Arrays.asList(0, 2, 3)), "Sets bits in table");
 charMatcher.setBits(bitSet);
 assertTrue(bitSet.get(0));
 assertTrue(bitSet.get(2));
 assertTrue(bitSet.get(3));
 }

 @Test
 public void testSetBitsWithZero() {
 char[] table = new char[]{'a', 'b', 'c', 'd'};
 BitSet bitSet = new BitSet();
 CharMatcher charMatcher = CharMatcher.from(new SmallCharMatcher(table, 0, true, "Sets bit 0 in table"), "Sets bit 0 with containsZero = true");
 charMatcher.setBits(bitSet);
 assertTrue(bitSet.get(0));
 assertFalse(bitSet.get(2));
 assertFalse(bitSet.get(3));
 }
}
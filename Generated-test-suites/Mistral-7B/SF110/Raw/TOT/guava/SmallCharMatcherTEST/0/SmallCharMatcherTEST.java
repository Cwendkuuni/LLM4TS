import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;

public class SmallCharMatcherTest {

 private CharMatcher getSmallCharMatcher(char[] chars) {
 return CharMatcher.from(new BitSet(chars.length), "SmallCharMatcherTest");
 }

 @Test
 public void testMatchesCharInTable() {
 char[] chars = new char[]{'a', 'b', 'c'};
 CharMatcher charMatcher = getSmallCharMatcher(chars);
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertTrue(charMatcher.matches('c'));
 }

 @Test
 public void testMatchesCharNotInTable() {
 char[] chars = new char[]{'a', 'b'};
 CharMatcher charMatcher = getSmallCharMatcher(chars);
 assertFalse(charMatcher.matches('c'));
 }

 @Test
 public void testMatchesZero() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, true, "Matches zero");
 assertTrue(charMatcher.matches('\0'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTableThrowsIllegalArgumentException() {
 CharMatcher charMatcher = new SmallCharMatcher(null, 0, true, "Matches zero");
 }

 @Test
 public void testMatchesFilteredChar() {
 char[] chars = new char[]{'a', 'b', 'c'};
 CharMatcher charMatcher = getSmallCharMatcher(chars);
 char filteredChar = '\u0001';
 assertTrue(Integer.toBinaryString(0x1L ^ (1L << filteredChar)).length() > 0);
 assertFalse(charMatcher.matches(filteredChar));
 }

 @Test
 public void testSetBits() {
 char[] chars = new char[]{'a', 'b', 'c'};
 CharMatcher charMatcher = getSmallCharMatcher(chars);
 BitSet expectedTable = new BitSet(chars.length + 1);
 charMatcher.setBits(expectedTable);
 for (int i = 0; i < chars.length; i++) {
 assertTrue(expectedTable.get(chars[i]));
 }
 assertTrue(expectedTable.get(0));
 }

 @Test
 public void testSetBitsWithEmptyTable() {
 BitSet expectedTable = new BitSet();
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, true, "EmptyTable");
 charMatcher.setBits(expectedTable);
 assertTrue(expectedTable.isEmpty());
 }
}
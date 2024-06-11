import org.junit.Test;
import org.junit.Assert.*;
import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import java.util.Arrays;

public class SmallCharMatcherTest {

 private CharMatcher getCharMatcher(char[] chars) {
 return CharMatcher.from(new BitSet(Arrays.asList(chars)), "Test CharMatcher");
 }

 @Test
 public void testMatches() {
 char[] table1 = new char[] {'a', 'b', 'c'};
 CharMatcher charMatcher1 = getCharMatcher(table1);
 assertTrue(charMatcher1.matches('a'));
 assertTrue(charMatcher1.matches('b'));
 assertFalse(charMatcher1.matches('d'));

 char[] table2 = new char[] {'0', 'a', 'b', 'c'};
 CharMatcher charMatcher2 = getCharMatcher(table2);
 assertTrue(charMatcher2.matches('0'));
 assertTrue(charMatcher2.matches('a'));
 assertTrue(charMatcher2.matches('b'));
 assertTrue(charMatcher2.matches('c'));

 char[] table3 = new char[] {};
 CharMatcher charMatcher3 = getCharMatcher(table3);
 assertFalse(charMatcher3.matches('\0'));
 }

 @Test
 public void testMatchesZero() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, true, "Test CharMatcher with zero");
 assertTrue(charMatcher.matches('\0'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyCharArray() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Test CharMatcher with empty char array");
 }

 @Test
 public void testContainsZero() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[] {'a', 'b', '0'}, 0, true, "Test CharMatcher with zero in table");
 assertTrue(charMatcher.matches('\0'));
 }

 @Test
 public void testFilter() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 3, false, "Test CharMatcher with filter");
 assertFalse(charMatcher.matches('d'));
 assertFalse(charMatcher.matches('0'));
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertTrue(charMatcher.matches('c'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeFilter() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, -1, false, "Test CharMatcher with negative filter");
 }

 @Test
 public void testSetBits() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0, false, "Test CharMatcher setBits");
 BitSet expectedTable = new BitSet();
 expectedTable.set('a');
 expectedTable.set('b');
 expectedTable.set('c');
 charMatcher.setBits(expectedTable);
 BitSet actualTable = new BitSet();
 charMatcher.setBits(actualTable);
 Arrays.equals(expectedTable.toArray(), actualTable.toArray());
 }
}
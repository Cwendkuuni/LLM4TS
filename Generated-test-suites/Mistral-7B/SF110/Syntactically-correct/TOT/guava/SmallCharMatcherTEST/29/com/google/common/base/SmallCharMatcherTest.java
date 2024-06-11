import org.junit.Before;
import org.junit.Test;
import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmallCharMatcherTest {

 private CharMatcher charMatcher;
 private BitSet charSet;

 @Before
 public void setUp() {
 charSet = new BitSet();
 charSet.set('a').set('b').set('c');
 charMatcher = CharMatcher.from(charSet, "matches 'a', 'b', or 'c'");
 }

 @Test
 public void testMatchesCharInSet() {
 char expected = 'a';
 char actual = charMatcher.matches((char) expected) ? expected : '\u0000';
 assertTrue("Expected: " + expected + ", but was: " + actual, expected == actual);
 }

 @Test
 public void testDoesNotMatchCharNotInSet() {
 char expected = 'd';
 char actual = charMatcher.matches((char) expected) ? expected : '\u0000';
 assertFalse("Expected: false, but was: " + actual, actual != '\u0000');
 }

 @Test
 public void testMatchesZeroWhenContainsZeroIsTrue() {
 charMatcher = new SmallCharMatcher(new char[0], 0, true, "matches any character, including zero");
 char expected = '\u0000';
 char actual = charMatcher.matches((char) expected) ? expected : '\u0000';
 assertTrue("Expected: " + expected + ", but was: " + actual, expected == actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowsExceptionWhenTableSizeIsZero() {
 charMatcher = new SmallCharMatcher(new char[0], 0, false, "should throw IllegalArgumentException");
 }

 @Test
 public void testSetsBitsCorrectly() {
 BitSet result = new BitSet();
 charMatcher.setBits(result);
 assertTrue(result.get('a'));
 assertTrue(result.get('b'));
 assertTrue(result.get('c'));
 }

 @Test
 public void testDoesNotSetBitsForZeroWhenContainsZeroIsFalse() {
 charMatcher = new SmallCharMatcher(new char[0], 0, false, "should not set bit for zero");
 BitSet result = new BitSet();
 charMatcher.setBits(result);
 assertFalse(result.get(0));
 }

 @Test
 public void testMatchesZeroWhenContainsZeroIsFalse() {
 charMatcher = new SmallCharMatcher(new char[0], 0, false, "matches any character except zero");
 char expected = '\u0001';
 char actual = charMatcher.matches((char) expected) ? expected : '\u0000';
 assertFalse("Expected: false, but was: " + actual, actual != '\u0000');
 }

 @Test
 public void testMatchesCharacterAtStartOfTable() {
 charMatcher = new SmallCharMatcher(new char[]{'a'}, 0, false, "matches 'a'");
 char expected = 'a';
 char actual = charMatcher.matches((char) expected) ? expected : '\u0000';
 assertTrue("Expected: " + expected + ", but was: " + actual, expected == actual);
 }

 @Test
 public void testMatchesCharacterAtEndOfTable() {
 charMatcher = new SmallCharMatcher(new char[]{'z'}, 0, false, "matches 'z'");
 char expected = 'z';
 char actual = charMatcher.matches((char) expected) ? expected : '\u0000';
 assertTrue("Expected: " + expected + ", but was: " + actual, expected == actual);
 }

 @Test
 public void testMatchesCharacterInMiddleOfTable() {
 charMatcher = new SmallCharMatcher(new char[]{'a', 'b', 'c', 'd'}, 0, false, "matches 'b'");
 char expected = 'b';
 char actual = charMatcher.matches((char) expected) ? expected : '\u0000';
 assertTrue("Expected: " + expected + ", but was: " + actual, expected == actual);
 }

 @Test
 public void testMatchesCharacterNotInTable() {
 charMatcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0, false, "matches 'd'");
 char expected = 'd';
 char actual = charMatcher.matches((char) expected) ? expected : '\u0000';
 assertFalse("Expected: false, but was: " + actual, actual != '\u0000');
 }
}
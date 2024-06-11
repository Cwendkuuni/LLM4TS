import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 private BitSet charSet;
 private CharMatcher charMatcher;

 @Before
 public void setUp() {
 charSet = new BitSet();
 charSet.set('a');
 charSet.set('b');
 charSet.set('c');
 charSet.set('d');
 charMatcher = SmallCharMatcher.from(charSet, "Character Matcher for set {a, b, c, d}");
 }

 @Test
 public void testMatchesWithCharInTable() {
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertTrue(charMatcher.matches('c'));
 assertTrue(charMatcher.matches('d'));
 }

 @Test
 public void testMatchesWithCharNotInTable() {
 assertFalse(charMatcher.matches('e'));
 }

 @Test
 public void testMatchesWithZero() {
 if (charMatcher.containsZero()) {
 assertTrue(charMatcher.matches('\0'));
 } else {
 assertFalse(charMatcher.matches('\0'));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromEmptyCharSet() {
 try {
 SmallCharMatcher.from(new BitSet(), "Empty Character Set");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromNullCharSet() {
 try {
 SmallCharMatcher.from(null, "Null Character Set");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromCharSetWithDuplicates() {
 charSet.set('a');
 charSet.set('a');
 try {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(charSet, "Character Matcher for set {a, a}");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }
}
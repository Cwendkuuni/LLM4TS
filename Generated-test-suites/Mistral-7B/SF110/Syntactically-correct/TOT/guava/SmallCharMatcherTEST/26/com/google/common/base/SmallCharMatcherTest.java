import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SmallCharMatcherTest {

 private SmallCharMatcher charMatcher;
 private BitSet inputBitSet;

 @Before
 public void setup() {
 charCodes charCodes = new charCodes('a', 'b', 'c', 'd', 'e', 'f');
 inputBitSet = SmallCharMatcher.from(charCodes.getBitSet(), "CharMatcher for given characters");
 charMatcher = SmallCharMatcher.from(inputBitSet, "CharMatcher for given characters");
 }

 @Test
 public void testMatchesCharInTable() {
 char testChar = 'a';
 assertTrue(charMatcher.matches(testChar));
 }

 @Test
 public void testMatchesCharNotInTable() {
 char testChar = 'g';
 assertFalse(charMatcher.matches(testChar));
 }

 @Test
 public void testMatchesZero() {
 char testChar = '\0';
 assertTrue(charMatcher.matches(testChar));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 BitSet nullInput = null;
 try {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(nullInput, "Null Input");
 } catch (IllegalArgumentException e) {
 assertEquals("Null BitSet", e.getMessage());
 }
 }

 @Test
 public void testEmptyBitSet() {
 BitSet emptyBitSet = new BitSet();
 SmallCharMatcher charMatcher = SmallCharMatcher.from(emptyBitSet, "Empty BitSet");
 assertTrue(charMatcher.isEmpty());
 }

 @Test
 public void testMatchesMultipleChars() {
 Set<Character> testChars = new HashSet<Character>(Collections.nCopies(5, (char) 0));
 for (char c : testChars) {
 charMatcher.matches(c);
 testChars.remove(c);
 }
 assertFalse(charMatcher.matches((char) 0));
 }

 @Test
 public void testSetBits() {
 BitSet outputBitSet = new BitSet();
 charMatcher.setBits(outputBitSet);
 for (char c : inputBitSet) {
 assertTrue(outputBitSet.get(c));
 }
 }
}
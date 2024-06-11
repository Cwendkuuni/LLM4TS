import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcherWithSingleCharacter() {
 CharMatcher charMatcher = CharMatcher.from(new BitSet().set(65), "Matches 'A'");
 assertFalse(charMatcher.matches((char) 64)); // Test for character that is not in the set
 assertTrue(charMatcher.matches((char) 65)); // Test for character that is in the set
 }

 @Test
 public void testSmallCharMatcherWithMultipleCharacters() {
 CharMatcher charMatcher = CharMatcher.from(new BitSet().set(65).set(66).set(67), "Matches 'A', 'B', and 'C'");
 assertFalse(charMatcher.matches((char) 64)); // Test for character that is not in the set
 assertTrue(charMatcher.matches((char) 65)); // Test for character that is in the set
 assertTrue(charMatcher.matches((char) 66)); // Test for another character that is in the set
 assertTrue(charMatcher.matches((char) 67)); // Test for another character that is in the set
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcherWithEmptySet() {
 try {
 CharMatcher.from(new BitSet(), "Empty set");
 } catch (IllegalArgumentException e) {
 assertEquals("Empty set", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcherWithSetContainingZero() {
 try {
 CharMatcher.from(new BitSet().set(0), "Set containing zero");
 } catch (IllegalArgumentException e) {
 assertEquals("Set containing zero", e.getMessage());
 }
 }

 @Test
 public void testSmallCharMatcherWithSetContainingRepeatedCharacters() {
 CharMatcher charMatcher = CharMatcher.from(new BitSet().set(65).set(65).set(67), "Matches 'A' twice and 'C'");
 assertFalse(charMatcher.matches((char) 64)); // Test for character that is not in the set
 assertTrue(charMatcher.matches((char) 65)); // Test for character that is repeated in the set and is in the set
 assertTrue(charMatcher.matches((char) 67)); // Test for character that is in the set
 }

 @Test
 public void testSmallCharMatcherMatchesDescription() {
 CharMatcher charMatcher = CharMatcher.from(new BitSet().set(65).set(66).set(67), "Matches 'A', 'B', and 'C'");
 assertEquals("Matches 'A', 'B', and 'C'", charMatcher.description());
 }

 @Test
 public void testSmallCharMatcherSetBits() {
 CharMatcher charMatcher = CharMatcher.from(new BitSet().set(65).set(66).set(67), "Matches 'A', 'B', and 'C'");
 BitSet expected = new BitSet();
 charMatcher.setBits(expected);
 assertTrue(expected.get(65)); // 'A' is in the set, so it should be set in the BitSet
 assertTrue(expected.get(66)); // 'B' is in the set, so it should be set in the BitSet
 assertTrue(expected.get(67)); // 'C' is in the set, so it should be set in the BitSet
 }
}
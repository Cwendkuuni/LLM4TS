import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import com.google.common.base.CharMatcher;
import com.google.common.base.BitSets;

@RunWith(JUnit4.class)
public class SmallCharMatcherTest {

 @Test
 public void testEmptyMatcher() {
 CharMatcher emptyMatcher = SmallCharMatcher.from(BitSets.emptySet(), "Empty CharMatcher");
 assertFalse(emptyMatcher.matches('\0'));
 assertFalse(emptyMatcher.matches('A'));
 }

 @Test
 public void testSingleCharacterMatcher() {
 BitSet singleCharacterSet = BitSets.newSet();
 singleCharacterSet.add('A');
 CharMatcher singleCharacterMatcher = SmallCharMatcher.from(singleCharacterSet, "Single Character CharMatcher");
 assertFalse(singleCharacterMatcher.matches('\0'));
 assertFalse(singleCharacterMatcher.matches('B'));
 assertTrue(singleCharacterMatcher.matches('A'));
 }

 @Test
 public void testCharacterWithMultipleOccurrencesMatcher() {
 BitSet multipleCharacterSet = BitSets.newSet();
 multipleCharacterSet.add('A');
 multipleCharacterSet.add('A');
 CharMatcher multipleCharacterMatcher = SmallCharMatcher.from(multipleCharacterSet, "Character with Multiple Occurrences CharMatcher");
 assertFalse(multipleCharacterMatcher.matches('\0'));
 assertFalse(multipleCharacterMatcher.matches('B'));
 assertTrue(multipleCharacterMatcher.matches('A'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDescription() {
 BitSet chars = BitSets.newSet();
 CharMatcher charMatcher = SmallCharMatcher.from(chars, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyBitSet() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.emptySet(), "Empty BitSet CharMatcher");
 }

 @Test(expected = IllegalArgumentArgument.class)
 public void testNegativeBitSetIndex() {
 BitSet bitSet = BitSets.newSet();
 bitSet.set(-1, true);
 assertThrows(IllegalArgumentException.class, () -> SmallCharMatcher.from(bitSet, "Negative BitSet Index CharMatcher"));
 }

 @Test(expected = IllegalArgumentArgument.class)
 public void testBitSetWithSizeGreaterThanMaxTableSize() {
 BitSet bitSet = BitSets.newSet();
 int size = SmallCharMatcher.MAX_SIZE + 1;
 for (int i = 0; i < size; i++) {
 bitSet.set(i, true);
 }
 assertThrows(IllegalArgumentException.class, () -> SmallCharMatcher.from(bitSet, "BitSet with Size Greater Than Max Table Size CharMatcher"));
 }

 @Test
 public void testMatchingNullInput() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newSet('A', 'B'), "CharMatcher with Characters A and B");
 assertFalse(charMatcher.matches(null));
 }

 @Test
 public void testMatchingEmptyString() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newSet('A', 'B'), "CharMatcher with Characters A and B");
 assertFalse(charMatcher.matches(""));
 }

 @Test
 public void testMatchingStringContainingMatchedCharacter() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newSet('A', 'B'), "CharMatcher with Characters A and B");
 assertTrue(charMatcher.matches("AB"));
 }

 @Test
 public void testMatchingStringContainingUnmatchedCharacter() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newSet('A', 'B'), "CharMatcher with Characters A and B");
 assertFalse(charMatcher.matches("ABC"));
 }

 @Test
 public void testMatchingStringContainingMatchedCharacterAtEnd() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newSet('A', 'B'), "CharMatcher with Characters A and B");
 assertTrue(charMatcher.matches("BC"));
 }

 @Test
 public void testMatchingStringContainingMatchedCharacterAtBeginning() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newSet('A', 'B'), "CharMatcher with Characters A and B");
 assertTrue(charMatcher.matches("AB"));
 }

 @Test
 public void testMatchingEmptyStringAfterSettingBits() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newSet(), "Empty CharMatcher");
 charMatcher.setBits(BitSets.newSet());
 assertFalse(charMatcher.matches(""));
 }

 @Test
 public void testMatchingStringWithMatchedCharacterAfterSettingBits() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newSet('A'), "CharMatcher with Character A");
 charMatcher.setBits(BitSets.newSet('B'));
 assertFalse(charMatcher.matches("AB"));
 }

 @Test
 public void testMatchingStringWithMatchedCharacterAtBeginningAfterSettingBits() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newSet('A'), "CharMatcher with Character A");
 charMatcher.setBits(BitSets.newSet('B'));
 assertFalse(charMatcher.matches("BA"));
 }

 @Test
 public void testMatchingStringWithMatchedCharacterAtEndAfterSettingBits() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newSet('A'), "CharMatcher with Character A");
 charMatcher.setBits(BitSets.newSet('B'));
 assertTrue(charMatcher.matches("AB"));
 }
}
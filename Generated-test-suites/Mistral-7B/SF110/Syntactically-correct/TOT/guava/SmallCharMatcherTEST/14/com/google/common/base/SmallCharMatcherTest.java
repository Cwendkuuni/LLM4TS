import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {

 @Test
 public void testEmptyMatcher() {
 CharMatcher emptyMatcher = SmallCharMatcher.from(new BitSet(), "Empty CharMatcher");
 assertTrue(emptyMatcher.matches('\0'));
 }

 @Test
 public void testSingleCharacterMatcher() {
 CharMatcher singleCharacterMatcher = SmallCharMatcher.from(BitSet.create(0x33), "Single CharacterMatcher for '3'");
 assertTrue(singleCharacterMatcher.matches('3'));
 assertFalse(singleCharacterMatcher.matches('4'));
 }

 @Test
 public void testMultipleCharacterMatcher() {
 CharMatcher multipleCharacterMatcher = SmallCharMatcher.from(BitSet.create(0x33, 0x61, 0x64), "Multiple CharacterMatcher for '3', 'a', 'd'");
 assertTrue(multipleCharacterMatcher.matches('3'));
 assertTrue(multipleCharacterMatcher.matches('a'));
 assertTrue(multipleCharacterMatcher.matches('d'));
 assertFalse(multipleCharacterMatcher.matches('4'));
 }

 @Test
 public void testContainsZeroMatcher() {
 CharMatcher containsZeroMatcher = SmallCharMatcher.from(BitSet.create(0), "ContainsZeroMatcher");
 assertTrue(containsZeroMatcher.matches('\0'));
 assertFalse(containsZeroMatcher.matches('a'));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetBitsOnUnmodifiableBitSet() {
 BitSet table = BitSet.create(true, false); // Unmodifiable bitset
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[1023], 0, false, "Test CharMatcher");
 charMatcher.setBits(table);
 }

 @Test
 public void testNullDescription() {
 try {
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[1023], 0, false, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}
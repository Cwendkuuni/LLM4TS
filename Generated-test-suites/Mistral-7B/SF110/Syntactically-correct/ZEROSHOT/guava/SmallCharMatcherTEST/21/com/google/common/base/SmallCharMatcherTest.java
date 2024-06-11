import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class SmallCharMatcherTest {

 private SmallCharMatcher smallCharMatcher;
 private BitSet bitSet;

 @Before
 public void setUp() {
 char[] chars = {'a', 'b', 'c'};
 bitSet = new BitSet(chars.length);
 for (int i = 0; i < chars.length; i++) {
 bitSet.set(chars[i]);
 }
 smallCharMatcher = CharMatcher.from(bitSet, "SmallCharMatcherTest");
 }

 @Test
 public void testMatchesCharacterInTable() {
 char character = 'a';
 assertTrue(smallCharMatcher.matches(character));
 }

 @Test
 public void testMatchesCharacterNotInTable() {
 char character = 'd';
 assertFalse(smallCharMatcher.matches(character));
 }

 @Test
 public void testMatchesCharacterWithFilter() {
 char character = 'b';
 assertTrue(smallCharMatcher.matches(character));
 }

 @Test
 public void testMatchesCharacterWithFilterFalse() {
 char character = 'e';
 assertFalse(smallCharMatcher.matches(character));
 }

 @Test
 public void testMatchesZero() {
 boolean containsZero = smallCharMatcher.containsZero();
 assertTrue(containsZero);
 char zero = '\0';
 assertTrue(smallCharMatcher.matches(zero));
 }

 @Test
 public void testSetBits() {
 BitSet resultedBitSet = new BitSet();
 smallCharMatcher.setBits(resultedBitSet);
 for (int i = 0; i < smallCharMatcher.table.length; i++) {
 if (smallCharMatcher.table[i] != '\0') {
 resultedBitSet.set(smallCharMatcher.table[i]);
 }
 }
 assertTrue(resultedBitSet.containsAll(bitSet));
 }
}
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher_EmptySet() {
 CharMatcher emptyMatcher = CharMatcher.from(new BitSet(), "Empty CharMatcher");
 assertFalse("Empty CharMatcher should not match any character", emptyMatcher.matches('\u0001'));
 }

 @Test
 public void testSmallCharMatcher_SingleCharacterSet() {
 BitSet singleCharacterSet = new BitSet();
 singleCharacterSet.set(0x30); // digit 0
 CharMatcher singleCharacterMatcher = CharMatcher.from(singleCharacterSet, "Single Character CharMatcher for digit 0");
 assertTrue("Single Character CharMatcher for digit 0 should match digit 0", singleCharacterMatcher.matches((char)0x30));
 assertFalse("Single Character CharMatcher for digit 0 should not match digit 1", singleCharacterMatcher.matches((char)0x31));
 }

 @Test
 public void testSmallCharMatcher_MultipleCharactersSet() {
 BitSet multipleCharactersSet = new BitSet();
 multipleCharactersSet.set(0x30); // digit 0
 multipleCharactersSet.set(0x31); // digit 1
 CharMatcher multipleCharactersMatcher = CharMatcher.from(multipleCharactersSet, "Multiple Characters CharMatcher for digits 0 and 1");
 assertTrue("Multiple Characters CharMatcher for digits 0 and 1 should match digit 0", multipleCharactersMatcher.matches((char)0x30));
 assertTrue("Multiple Characters CharMatcher for digits 0 and 1 should match digit 1", multipleCharactersMatcher.matches((char)0x31));
 assertFalse("Multiple Characters CharMatcher for digits 0 and 1 should not match digit 2", multipleCharactersMatcher.matches((char)0x32));
 }

 @Test
 public void testSmallCharMatcher_ContainsZero() {
 BitSet containsZeroSet = new BitSet();
 containsZeroSet.set(0x30); // digit 0
 containsZeroSet.set(0); // null character
 CharMatcher containsZeroMatcher = CharMatcher.from(containsZeroSet, "Contains Zero CharMatcher for digit 0 and null character");
 assertTrue("Contains Zero CharMatcher for digit 0 and null character should match digit 0", containsZeroMatcher.matches((char)0x30));
 assertTrue("Contains Zero CharMatcher for digit 0 and null character should match null character", containsZeroMatcher.matches((char)0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_NegativeFilterBit() {
 BitSet negativeFilterBitSet = new BitSet();
 negativeFilterBitSet.set(-1); // negative number
 try {
 CharMatcher negativeFilterMatcher = CharMatcher.from(negativeFilterBitSet, "Negative Filter Bit CharMatcher");
 fail("Expected IllegalArgumentException as filter bit set contains negative number");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSmallCharMatcher_LargeSet() {
 BitSet largeSet = new BitSet();
 for (char c = 0; c < SmallCharMatcher.MAX_SIZE; c++) {
 largeSet.set(c);
 }
 CharMatcher largeSetMatcher = CharMatcher.from(largeSet, "Large Set CharMatcher for all characters within MAX_SIZE limit");
 for (char c = 0; c < SmallCharMatcher.MAX_SIZE; c++) {
 assertTrue("Large Set CharMatcher for all characters within MAX_SIZE limit should match character: " + c, largeSetMatcher.matches(c));
 }
 }

 @Test
 public void testSmallCharMatcher_Filter() {
 BitSet filterSet = new BitSet();
 filterSet.set(0x1); // bit 0
 filterSet.set(0x2); // bit 1
 CharMatcher filterMatcher = CharMatcher.from(filterSet, "Filter CharMatcher for bits 0 and 1");
 final int filter = SmallCharMatcher.smear(0x3); // smear hash code for character 'C'
 assertTrue("Filter CharMatcher for bits 0 and 1 should match character 'C' if filter bit is set", filterMatcher.matches((char)0x3) && (filter & 0x1L) != 0 && (filter & 0x2L) != 0);
 assertFalse("Filter CharMatcher for bits 0 and 1 should not match character 'D' if filter bit is not set", filterMatcher.matches((char)0x4) && ((filter & 0x1L) == 0 || (filter & 0x2L) == 0));
 }

 @Test
 public void testSmallCharMatcher_TableSize() {
 BitSet tableSizeSet = new BitSet();
 tableSizeSet.set(0x100); // 256
 CharMatcher tableSizeMatcher = CharMatcher.from(tableSizeSet, "Table Size CharMatcher for size 256");
 char[] table = new char[SmallCharMatcher.chooseTableSize(tableSizeMatcher.cardinality())];
 for (char c = 0; c < tableSizeMatcher.cardinality(); c++) {
 table[smear(c) & (table.length - 1)] = c;
 }
 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, "SmallCharMatcher");
 assertEquals("SmallCharMatcher table size should match the expected value", table.length, smallCharMatcher.table.length);
 }
}
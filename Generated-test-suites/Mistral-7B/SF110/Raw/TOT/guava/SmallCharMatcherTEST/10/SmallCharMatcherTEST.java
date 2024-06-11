import org.junit.Test;
import org.junit.Assert.*;
import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {

 @Test
 public void testEmptyMatcher() {
 CharMatcher emptyMatcher = SmallCharMatcher.from(new BitSet(), "empty");
 BitSet expectedTable = new BitSet();
 SmallCharMatcher expectedSmallCharMatcher = new SmallCharMatcher(new char[0], 0L, false, "empty");

 assertEquals("Unexpected table size", expectedTable.size(), emptyMatcher.size());
 assertEquals("Unexpected description", "empty", emptyMatcher.description());
 assertEquals("Unexpected matches result", expectedSmallCharMatcher, emptyMatcher);
 }

 @Test
 public void testSingleCharacterMatcher() {
 CharMatcher singleCharacterMatcher = SmallCharMatcher.from(new BitSet().set(65), "singleA");
 BitSet expectedTable = new BitSet().set(65);
 SmallCharMatcher expectedSmallCharMatcher = new SmallCharMatcher(new char[]{'A'}, 1L << 65, false, "singleA");

 assertEquals("Unexpected table size", expectedTable.size(), singleCharacterMatcher.size());
 assertEquals("Unexpected description", "singleA", singleCharacterMatcher.description());
 assertEquals("Unexpected matches result", expectedSmallCharMatcher, singleCharacterMatcher);

 assertTrue("Unexpected match result", singleCharacterMatcher.matches('A'));
 assertFalse("Unexpected non-match result", singleCharacterMatcher.matches('B'));
 }

 @Test
 public void testMultipleCharactersMatcher() {
 CharMatcher multipleCharactersMatcher = SmallCharMatcher.from(new BitSet().set(65, 66, 67), "multipleABC");
 BitSet expectedTable = new BitSet().set(65, 66, 67);
 SmallCharMatcher expectedSmallCharMatcher = new SmallCharMatcher(new char[]{'A', 'B', 'C'}, 0b1111111111111111011111111101111111L, false, "multipleABC");

 assertEquals("Unexpected table size", expectedTable.size(), multipleCharactersMatcher.size());
 assertEquals("Unexpected description", "multipleABC", multipleCharactersMatcher.description());
 assertEquals("Unexpected matches result", expectedSmallCharMatcher, multipleCharactersMatcher);

 assertTrue("Unexpected match result for A", multipleCharactersMatcher.matches('A'));
 assertTrue("Unexpected match result for B", multipleCharactersMatcher.matches('B'));
 assertTrue("Unexpected match result for C", multipleCharactersMatcher.matches('C'));
 assertFalse("Unexpected non-match result for D", multipleCharactersMatcher.matches('D'));
 }

 @Test
 public void testZeroInTable() {
 CharMatcher zeroInTableMatcher = SmallCharMatcher.from(new BitSet().set(0).set(65), "zeroInTableA");
 BitSet expectedTable = new BitSet().set(0, 65);
 SmallCharMatcher expectedSmallCharMatcher = new SmallCharMatcher(new char[]{'A'}, 0b100000001111111111111111011111111101111111L, true, "zeroInTableA");

 assertEquals("Unexpected table size", expectedTable.size(), zeroInTableMatcher.size());
 assertEquals("Unexpected description", "zeroInTableA", zeroInTableMatcher.description());
 assertEquals("Unexpected matches result", expectedSmallCharMatcher, zeroInTableMatcher);

 assertTrue("Unexpected match result for A", zeroInTableMatcher.matches('A'));
 assertTrue("Unexpected match result for zero", zeroInTableMatcher.matches((char)0));
 assertFalse("Unexpected non-match result for B", zeroInTableMatcher.matches('B'));
 }

 @Test(expected = IllegalStateException.class)
 public void testNegativeSizeTable() {
 CharMatcher negativeSizeTableMatcher = SmallCharMatcher.from(new BitSet(), "negativeSize");
 // The test should fail with an IllegalStateException as the table size cannot be negative
 }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import com.google.common.base.FastMatcher;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {

 private CharMatcher charMatcher;
 private BitSet expectedTable;

 @Before
 public void setUp() {
 BitSet chars = new BitSet();
 chars.set(111); // 'g'
 chars.set(101); // 'e'
 chars.set(118); // 'f'
 charMatcher = SmallCharMatcher.from(chars, "matches 'g', 'e', and 'f'");
 expectedTable = BitSet.create();
 expectedTable.set(111); // 'g'
 expectedTable.set(101); // 'e'
 expectedTable.set(118); // 'f'
 }

 @Test
 public void testMatches() {
 CharMatcher charMatcher = this.charMatcher;
 // Matching character in the table
 assertTrue(charMatcher.matches('g'));
 // Matching character not in the table but in the filter
 assertTrue(charMatcher.matches('e'));
 // Character not in the table or the filter
 assertFalse(charMatcher.matches('h'));
 // Testing empty CharMatcher
 SmallCharMatcher emptyCharMatcher = new SmallCharMatcher(new char[0], 0, false, "Empty CharMatcher");
 assertFalse(emptyCharMatcher.matches('\0'));
 assertFalse(emptyCharMatcher.matches('a'));
 }

 @Test
 public void testSetBits() {
 BitSet table = BitSet.create();
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Empty CharMatcher");
 charMatcher.setBits(table);
 assertTrue(table.isEmpty());
 SmallCharMatcher expectedCharMatcher = this.charMatcher;
 expectedCharMatcher.setBits(this.expectedTable);
 BitSet actualTable = BitSet.create();
 expectedCharMatcher.setBits(actualTable);
 assertArrayEquals(this.expectedTable.toArray(), actualTable.toArray());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetBitsWithReadOnlyBitSet() {
 BitSet table = BitSet.create().readOnly();
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "ReadOnly BitSet CharMatcher");
 charMatcher.setBits(table);
 }

 @Test
 public void testChooseTableSize() {
 int setSize = 16;
 int expectedTableSize = 32;
 int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals(expectedTableSize, actualTableSize);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChooseTableSizeWithNegativeSize() {
 int setSize = -1;
 SmallCharMatcher.chooseTableSize(setSize);
 }

}
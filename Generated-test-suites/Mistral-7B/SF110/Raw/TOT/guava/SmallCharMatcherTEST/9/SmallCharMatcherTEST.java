import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {

 private CharMatcher charMatcher;
 private BitSet expectedTable;

 @Before
 public void setUp() {
 BitSet chars = new BitSet();
 chars.set('a');
 chars.set('b');
 chars.set('c');
 this.charMatcher = SmallCharMatcher.from(chars, "matches characters a, b, and c");
 this.expectedTable = BitSet.create(2);
 this.expectedTable.set('a');
 this.expectedTable.set('b');
 }

 @Test
 public void testMatchesCharInTable() {
 char expectedChar = 'a';
 char actualChar = expectedChar;
 assertTrue(this.charMatcher.matches(expectedChar));
 assertEquals(this.expectedTable, this.charMatcher.getTable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesCharNotInTableThrowsException() {
 char expectedChar = 'd';
 char actualChar = expectedChar;
 assertFalse(this.charMatcher.matches(expectedChar));
 }

 @Test
 public void testMatchesCharZeroWhenContainsZeroIsTrue() {
 char expectedChar = '\0';
 boolean containsZero = true;
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, containsZero, "matches zero");
 assertTrue(charMatcher.matches(expectedChar));
 }

 @Test
 public void testMatchesCharZeroWhenContainsZeroIsFalse() {
 char expectedChar = '\0';
 boolean containsZero = false;
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, containsZero, "does not match zero");
 assertFalse(charMatcher.matches(expectedChar));
 }

 @Test
 public void testSetBits() {
 BitSet actualTable = BitSet.create(this.charMatcher.getTable().length);
 this.charMatcher.setBits(actualTable);
 assertEquals(this.expectedTable, actualTable);
 }

 @Test
 public void testSetBitsWithZeroWhenContainsZeroIsTrue() {
 BitSet actualTable = BitSet.create(this.charMatcher.getTable().length);
 this.charMatcher.setBits(actualTable);
 actualTable.set(0);
 assertTrue(actualTable.get(0));
 }

 @Test
 public void testSetBitsWithZeroWhenContainsZeroIsFalse() {
 BitSet actualTable = BitSet.create(this.charMatcher.getTable().length);
 this.charMatcher.setBits(actualTable);
 assertFalse(actualTable.get(0));
 }

 @Test
 public void testChooseTableSize() {
 int setSize = 11;
 int expectedTableSize = 22;
 int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals(expectedTableSize, actualTableSize);
 }
}
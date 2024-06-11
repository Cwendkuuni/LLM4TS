import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.base.*;
import com.google.common.collect.BitSets;

public class SmallCharMatcherTest {

 private CharMatcher charMatcher;
 private BitSet expectedTable;

 @Before
 public void setUp() {
 char chars = BitSets.newBitSet();
 chars.set('a');
 chars.set('b');
 chars.set('c');
 this.charMatcher = CharMatcher.from(chars, "matches characters 'a', 'b', and 'c'");
 this.expectedTable = BitSets.newBitSet(2);
 this.expectedTable.set('a');
 this.expectedTable.set('b');
 this.expectedTable.set('c');
 }

 @Test
 public void testMatches() {
 char[] inputTable = new char[SmallCharMatcher.chooseTableSize(this.charMatcher.size())];
 for (int i = 0; i < inputTable.length; i++) {
 inputTable[i] = this.charMatcher.table[i];
 }
 BitSet actualTable = BitSets.newBitSet();
 this.charMatcher.setBits(actualTable);
 assertTrue("Tables should be equal", actualTable.equals(this.expectedTable));

 for (char c : this.charMatcher.getAcceptedChars()) {
 assertTrue("Matches expected character " + c, this.charMatcher.matches(c));
 }

 for (char c : BitSets.newBitSet(0).asCharacterArray()) {
 assertFalse("Does not match unexpected character " + c, this.charMatcher.matches(c));
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetBitsThrowsException() {
 BitSet table = BitSets.immutableBitSet();
 this.charMatcher.setBits(table);
 fail("Should not be able to modify immutable BitSet");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyCharMatcher() {
 BitSet emptySet = BitSets.newBitSet();
 this.charMatcher = CharMatcher.from(emptySet, "emptyCharMatcher");
 fail("Empty CharMatcher should not be created");
 }
}
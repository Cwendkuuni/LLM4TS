import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;

public class SmallCharMatcherTest {

 private CharMatcher charMatcher;
 private BitSet expectedTable;

 @Before
 public void setUp() {
 char c1 = 'a';
 char c2 = 'b';
 char c3 = '0';
 BitSet chars = BitSet.create();
 chars.set(c1);
 chars.set(c2);
 if (this.containsZero()) {
 chars.set(c3);
 }
 this.charMatcher = CharMatcher.from(chars, "Matcher for char 'a' and 'b' with zero as an optional character");
 this.expectedTable = BitSet.create();
 this.expectedTable.set(smear(c1));
 this.expectedTable.set(smear(c2));
 if (this.containsZero()) {
 this.expectedTable.set(smear(c3));
 }
 }

 @Test
 public void testMatches() {
 // Test matching characters in the table
 char c = 'a';
 assertTrue(this.charMatcher.matches(c));
 assertEquals(this.expectedTable.get(smear(c)), 1);

 c = 'b';
 assertTrue(this.charMatcher.matches(c));
 assertEquals(this.expectedTable.get(smear(c)), 1);

 c = 'c';
 assertFalse(this.charMatcher.matches(c));
 assertEquals(this.expectedTable.get(smear(c)), 0);

 // Test matching character when table is empty
 this.charMatcher = new SmallCharMatcher(new char[0], 0, false, "Empty CharMatcher");
 assertFalse(this.charMatcher.matches('a'));
 assertTrue(this.charMatcher.containsZero());

 // Test matching character when table contains only zero
 this.charMatcher = new SmallCharMatcher(new char[]{'0'}, 0, true, "CharMatcher for character '0'");
 assertTrue(this.charMatcher.matches('0'));

 // Test matching character when filter bit is not set
 this.charMatcher = new SmallCharMatcher(new char[]{'a'}, (1L << 'b'), false, "CharMatcher for character 'a' without bit 'b'");
 assertFalse(this.charMatcher.matches('b'));

 // Test matching character when filter bit is set
 this.charMatcher = new SmallCharMatcher(new char[]{'a'}, (1L << 'a' | 1L << 'b'), false, "CharMatcher for characters 'a' and 'b'");
 assertTrue(this.charMatcher.matches('a'));
 assertTrue(this.charMatcher.matches('b'));

 // Test setting bits in BitSet
 BitSet actualTable = BitSet.create();
 this.charMatcher.setBits(actualTable);
 assertEquals(this.expectedTable, actualTable);
 }

 // Helper method to check if the test environment is GWT compatible
 private boolean containsZero() {
 return this.charMatcher.containsZero;
 }

 private static int smear(char c) {
 return SmallCharMatcher.smear((int)c);
 }
}
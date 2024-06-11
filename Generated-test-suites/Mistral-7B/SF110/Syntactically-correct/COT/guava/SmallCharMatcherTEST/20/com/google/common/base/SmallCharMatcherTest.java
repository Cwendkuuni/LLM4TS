import static org.junit.Assert.*;
import static com.google.common.base.SmallCharMatcher.*;

import org.junit.Test;

public class SmallCharMatcherTest {

 private char[] createTable(char[] table, char[] input) {
 char[] newTable = new char[table.length];
 System.arraycopy(table, 0, newTable, 0, table.length);
 CharMatcher charMatcher = from(asList(input), "Test CharMatcher");
 charMatcher.setBits(newTable);
 return newTable;
 }

 @Test
 public void testSmallCharMatcher_NullInput() {
 assertNull(from(null, "Null Input"));
 }

 @Test
 public void testSmallCharMatcher_EmptyInput() {
 CharMatcher charMatcher = from(Collections.emptyList().toArray(new char[0]), "Empty Input");
 assertFalse(charMatcher.matches('\0'));
 assertFalse(charMatcher.matches('a'));
 }

 @Test
 public void testSmallCharMatcher_SingleCharacterInput() {
 CharMatcher charMatcher = from(new char[]{'a'}, "Single Character Input");
 assertTrue(charMatcher.matches('a'));
 assertFalse(charMatcher.matches('b'));
 }

 @Test
 public void testSmallCharMatcher_MultipleCharacterInput() {
 CharMatcher charMatcher = from(new char[]{'a', 'b', 'c'}, "Multiple Character Input");
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertTrue(charMatcher.matches('c'));
 assertFalse(charMatcher.matches('d'));
 }

 @Test
 public void testSmallCharMatcher_FilterBitSet() {
 char[] table = new char[10];
 CharMatcher charMatcher = from(asList('1', '2', '3').toArray(new char[0]), "Filter BitSet Test");
 table = createTable(table, charMatcher.getInput());
 for (int i = 0; i < table.length; i++) {
 if ((i + 1) == '1' || (i + 1) == '2' || (i + 1) == '3') {
 assertEquals((char)(i + 1), table[i]);
 } else {
 assertNull(table[i]);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_NegativeInputSize() {
 from(new char[0], "Negative Input Size Test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_DuplicateCharacterInput() {
 CharMatcher charMatcher = from(new char[]{'a', 'a'}, "Duplicate Character Input Test");
 assertFalse(charMatcher.matches('a'));
 }

 @Test
 public void testSmallCharMatcher_ContainsZero() {
 CharMatcher charMatcher = from(new char[]{'a', '\0'}, "Contains Zero Test");
 assertTrue(charMatcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcher_NoMatch() {
 CharMatcher charMatcher = from(new char[]{'a', 'b'}, "No Match Test");
 assertFalse(charMatcher.matches('c'));
 }

 @Test
 public void testSmallCharMatcher_CycleDetection() {
 CharMatcher charMatcher = from(new char[]{'a', 'b'}, "Cycle Detection Test");
 assertFalse(charMatcher.matches('a'));
 assertFalse(charMatcher.matches('b'));
 }

}
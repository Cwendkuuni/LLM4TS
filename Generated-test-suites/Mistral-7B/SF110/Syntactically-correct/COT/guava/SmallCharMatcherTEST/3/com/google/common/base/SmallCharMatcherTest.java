package com.google.common.base.test;

import com.google.common.base.CharMatcher;
import com.google.common.base.SmallCharMatcher;
import com.google.common.collect.BitSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 private char[] getTableForTest() {
 char[] table = new char[SmallCharMatcher.chooseTableSize(10)];
 BitSet charSet = new BitSet();
 charSet.add('a').add('b').add('c').add('d').add('e').add('f').add('g').add('h').add('i').add('j');
 CharMatcher charMatcher = SmallCharMatcher.from(charSet, "Test CharMatcher");
 return charMatcher.table;
 }

 @Test
 public void testSmallCharMatcher_matches() {
 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(getTableForTest(), 0x1L, true, "Test CharMatcher");

 // Basic functionality test
 assertTrue(smallCharMatcher.matches('a'));
 assertFalse(smallCharMatcher.matches('z'));

 // Edge cases and exception handling tests

 // Test for empty table
 SmallCharMatcher emptyTableSmallCharMatcher = new SmallCharMatcher(new char[0], 0x1L, false, "Empty Table CharMatcher");
 assertFalse(emptyTableSmallCharMatcher.matches((char)0));
 assertFalse(emptyTableSmallCharMatcher.matches('a'));

 // Test for table size equals to 1
 SmallCharMatcher tableSizeOneSmallCharMatcher = new SmallCharMatcher(new char[]{'a'}, 0x1L, false, "Table Size One CharMatcher");
 assertTrue(tableSizeOneSmallCharMatcher.matches('a'));
 assertFalse(tableSizeOneSmallCharMatcher.matches('b'));

 // Test for table size greater than MAX_SIZE
 SmallCharMatcher tableSizeGreaterThanMaxSmallCharMatcher = new SmallCharMatcher(new char[SmallCharMatcher.MAX_SIZE + 1], 0x1L, false, "Table Size Greater Than Max CharMatcher");
 assertFalse(tableSizeGreaterThanMaxSmallCharMatcher.matches((char)0));
 assertFalse(tableSizeGreaterThanMaxSmallCharMatcher.matches('a'));

 // Test for null description
 SmallCharMatcher nullDescriptionSmallCharMatcher = new SmallCharMatcher(getTableForTest(), 0x1L, true, null);
 assertNotNull(nullDescriptionSmallCharMatcher.description);

 // Test for invalid filter
 SmallCharMatcher invalidFilterSmallCharMatcher = new SmallCharMatcher(getTableForTest(), -1L, true, "Invalid Filter CharMatcher");
 assertFalse(invalidFilterSmallCharMatcher.matches((char)0));
 assertFalse(invalidFilterSmallCharMatcher.matches('a'));

 // Test for invalid table
 SmallCharMatcher invalidTableSmallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0x1L, true, "Invalid Table CharMatcher");
 assertFalse(invalidTableSmallCharMatcher.matches((char)0));
 assertFalse(invalidTableSmallCharMatcher.matches('a'));

 // Test for invalid containsZero
 SmallCharMatcher invalidContainsZeroSmallCharMatcher = new SmallCharMatcher(getTableForTest(), 0x1L, false, "Invalid ContainsZero CharMatcher");
 BitSet invalidContainsZeroTable = new BitSet();
 invalidContainsZeroTable.set(0);
 assertFalse(invalidContainsZeroSmallCharMatcher.matches((char)0));
 assertFalse(invalidContainsZeroSmallCharMatcher.matches('a'));
 assertFalse(invalidContainsZeroSmallCharMatcher.containsZero());
 }

}
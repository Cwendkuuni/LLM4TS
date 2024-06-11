package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {

 private CharMatcher smallCharMatcher;
 private BitSet charSet;

 @Before
 public void setUp() {
 charSet = new BitSet();
 charSet.set('a').set('b').set('c');
 smallCharMatcher = SmallCharMatcher.from(charSet, "Test CharMatcher");
 }

 @Test
 public void testMatches() {
 // Test basic functionality
 assertEquals(true, smallCharMatcher.matches('a'));
 assertEquals(true, smallCharMatcher.matches('b'));
 assertEquals(true, smallCharMatcher.matches('c'));
 // Test filter functionality
 assertEquals(false, smallCharMatcher.matches('d')); // a, b and c are set, d is not
 // Test containsZero functionality
 assertEquals(true, smallCharMatcher.matches('\0')); // containsZero flag is set to true
 // Test edge cases and exceptions
 // Test empty char set
 CharMatcher emptyMatcher = SmallCharMatcher.from(new BitSet(), "Empty CharMatcher");
 assertFalse(emptyMatcher.matches('\0')); // should not match since empty set
 // Test null description
 CharMatcher nullDescriptionMatcher = new SmallCharMatcher(new char[0], 0, false, null);
 assertNotNull(nullDescriptionMatcher); // should not null out
 // Test null table
 CharMatcher nullTableMatcher = new SmallCharMatcher(null, 0, false, "Null Table CharMatcher");
 assertNull(nullTableMatcher); // should null out
 // Test negative table size
 CharMatcher negativeTableSizeMatcher = new SmallCharMatcher(new char[1], 0, false, "Negative Table CharMatcher");
 assertFalse(negativeTableSizeMatcher.matches('a')); // table size should be positive
 // Test overflow in chooseTableSize
 int largeSetSize = Integer.MAX_VALUE;
 int overflowTableSize = SmallCharMatcher.chooseTableSize(largeSetSize);
 assertEquals(Integer.MAX_VALUE, overflowTableSize); // table size should be equal to set size
 }

 @After
 public void tearDown() {
 smallCharMatcher = null;
 charSet = null;
 }
}
package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcherTest");
 char[] expectedTable = new char[]{'a', 'b', 'c'};
 long expectedFilter = 0b11111111111011111110111111101111L; // 0x6666666666F6F6F6F6F6F6F6F6F6F6F6L
 boolean expectedContainsZero = true;

 assertEquals("Unexpected table size.", expectedTable.length, charMatcher.table.length);
 assertArrayEquals("Unexpected table content.", expectedTable, charMatcher.table);
 assertEquals("Unexpected filter value.", expectedFilter, charMatcher.filter);
 assertTrue("Unexpected containsZero value.", charMatcher.containsZero);
 }

 @Test
 public void testSmallCharMatcherMatches() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcherTest");
 char testChar = 'a';
 assertTrue("Unexpected match result for char '" + testChar + "'", charMatcher.matches(testChar));
 }

 @Test
 public void testSmallCharMatcherDoesNotMatch() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcherTest");
 char testChar = 'd';
 assertFalse("Unexpected match result for char '" + testChar + "'", charMatcher.matches(testChar));
 }

 @Test
 public void testSmallCharMatcherSetBits() {
 BitSet table = new BitSet();
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcherTest");
 charMatcher.setBits(table);

 char expectedChar = 'a';
 assertTrue("Unexpected bit set for char '" + expectedChar + "'", table.get(expectedChar));

 char unexpectedChar = 'd';
 assertFalse("Unexpected bit set for char '" + unexpectedChar + "'", table.get(unexpectedChar));
 }

 @Test
 public void testSmallCharMatcherChooseTableSize() {
 int setSize = 10;
 int expectedTableSize = 20;
 int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);

 assertEquals("Unexpected table size for set size " + setSize, expectedTableSize, actualTableSize);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcherNegativeSetSize() {
 SmallCharMatcher.chooseTableSize(-1);
 }

 @Test
 public void testSmallCharMatcherFromEmptyBitSet() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcherTest");
 assertTrue("Unexpected containsZero value.", charMatcher.containsZero);
 assertEmpty("Unexpected non-empty table.", charMatcher.table);
 }
}
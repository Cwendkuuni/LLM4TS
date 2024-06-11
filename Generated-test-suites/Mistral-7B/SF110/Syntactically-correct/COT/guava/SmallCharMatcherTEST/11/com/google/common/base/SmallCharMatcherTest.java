package com.google.common.base.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.SmallCharMatcher;
import com.google.common.collect.BitSet;
import org.junit.Test;

public class SmallCharMatcherTest {

 private char[] createCharArray(char[] table, int length) {
 char[] arr = new char[length];
 System.arraycopy(table, 0, arr, 0, length);
 return arr;
 }

 @Test
 public void testMatches() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create(), "Test CharMatcher");
 for (char c = '\u0000'; c <= '\u00FF'; c++) {
 boolean expected = charMatcher.matches(c);
 char[] table = charMatcher.table;
 boolean actual = new SmallCharMatcher(createCharArray(table, table.length), charMatcher.filter, charMatcher.containsZero, "Test CharMatcher").matches(c);
 assertEquals("CharMatcher does not match expected character", expected, actual);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCharMatcherWithNullTable() {
 CharMatcher charMatcher = SmallCharMatcher.from(null, "Test CharMatcher");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCharMatcherWithEmptyBitSet() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create(), "Test CharMatcher");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCharMatcherWithNegativeFilterBit() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create(), "Test CharMatcher", true, Long.toBinaryString(Long.reverseBytes(-1L)));
 }

 @Test
 public void testChooseTableSize() {
 int setSize = 22;
 int expectedTableSize = 32;
 int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals("Unexpected table size", expectedTableSize, actualTableSize);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCharMatcherWithTooLargeSetSize() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create(Integer.MAX_VALUE), "Test CharMatcher");
 }

 @Test
 public void testCheckFilter() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a', 'b', 'c'), "Test CharMatcher");
 assertTrue("Filter should pass character 'a'", charMatcher.checkFilter('a'));
 assertTrue("Filter should pass character 'b'", charMatcher.checkFilter('b'));
 assertTrue("Filter should pass character 'c'", charMatcher.checkFilter('c'));
 assertFalse("Filter should not pass character 'd'", charMatcher.checkFilter('d'));
 }

 @Test
 public void testMatchesWithEmptyTable() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "Test CharMatcher");
 assertFalse("Empty CharMatcher should not match any character", charMatcher.matches('\u0000'));
 }

 @Test
 public void testMatchesWithNullCharacter() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a', 'b', 'c'), "Test CharMatcher");
 assertFalse("CharMatcher should not match null character", charMatcher.matches((char)0));
 }

 @Test
 public void testMatchesWithCharacterAtBeginningOfTable() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a'), "Test CharMatcher");
 assertTrue("CharMatcher should match character at beginning of table", charMatcher.matches('a'));
 }

 @Test
 public void testMatchesWithCharacterAtEndOfTable() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a'), "Test CharMatcher");
 char[] table = charMatcher.table;
 int mask = table.length - 1;
 int index = (smear('z' & 0xFF) & mask);
 char expectedChar = table[index];
 CharMatcher expectedCharMatcher = new SmallCharMatcher(createCharArray(table, table.length), charMatcher.filter, charMatcher.containsZero, "Test CharMatcher");
 assertTrue("CharMatcher should match character at end of table", expectedCharMatcher.matches('z'));
 }

 @Test
 public void testMatchesWithCharacterNotInTable() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a'), "Test CharMatcher");
 assertFalse("CharMatcher should not match character not in table", charMatcher.matches('b'));
 }

 @Test
 public void testMatchesWithCharacterInTableTwice() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a', 'a'), "Test CharMatcher");
 assertTrue("CharMatcher should match character in table twice", charMatcher.matches('a'));
 }

 @Test
 public void testMatchesWithCharacterAtIndexZero() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a'), "Test CharMatcher");
 assertTrue("CharMatcher should match character at index zero", charMatcher.matches('\u0000'));
 }

 @Test
 public void testSetBits() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create('a', 'b'), "Test CharMatcher");
 BitSet expectedTable = BitSet.create('a', 'b');
 charMatcher.setBits(expectedTable);
 assertTrue("Table should match expected BitSet", expectedTable.equals(charMatcher.table));
 }
}
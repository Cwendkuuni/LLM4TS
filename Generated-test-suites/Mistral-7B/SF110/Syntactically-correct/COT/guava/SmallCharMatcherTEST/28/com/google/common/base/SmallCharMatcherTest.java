package com.google.common.base;

import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.Arrays;

import org.junit.Test;

public class SmallCharMatcherTest {

 private char[] createTestTable(int size, char[] chars) {
 char[] table = new char[SmallCharMatcher.chooseTableSize(size)];
 SmallCharMatcher.from(new BitSet(chars.length), "Test Matcher").setBits(new BitSet());
 for (int i = 0; i < table.length; i++) {
 if (table[i] != '\0') {
 assertSame(table[i], chars[i & (table.length - 1)]);
 }
 }
 return table;
 }

 @Test
 public void testMatchesWithEmptyTable() {
 char[] emptyTable = new char[0];
 SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Empty Table Matcher");
 assertFalse(matcher.matches('\u0000'));
 assertFalse(matcher.matches('\u0001'));
 }

 @Test
 public void testMatchesWithSingleCharacterTable() {
 char[] singleCharacterTable = {'a'};
 SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet('a'), "Single Character Table Matcher");
 assertTrue(matcher.matches('a'));
 assertFalse(matcher.matches('b'));
 }

 @Test
 public void testMatchesWithMultipleCharactersTable() {
 char[] multipleCharactersTable = {'a', 'b', 'c'};
 SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(Arrays.asList('a', 'b', 'c').toArray()), "Multiple Characters Table Matcher");
 assertTrue(matcher.matches('a'));
 assertTrue(matcher.matches('b'));
 assertTrue(matcher.matches('c'));
 assertFalse(matcher.matches('d'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesWithNegativeCharacterTable() {
 char[] negativeCharacterTable = {'-'};
 SmallCharMatcher matcher = null;
 try {
 matcher = SmallCharMatcher.from(new BitSet(Arrays.asList('-').toArray()), "Negative Character Table Matcher");
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertNull(matcher);
 }

 @Test
 public void testMatchesWithZeroCharacterTable() {
 char[] zeroCharacterTable = {'0'};
 SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(Arrays.asList('0').toArray()), "Zero Character Table Matcher");
 assertTrue(matcher.matches('0'));
 }

 @Test
 public void testMatchesWithLargeCharacterTable() {
 char[] largeCharacterTable = new char[SmallCharMatcher.MAX_SIZE];
 Arrays.fill(largeCharacterTable, 'a');
 SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(Arrays.asList('a').toArray()), "Large Character Table Matcher");
 Arrays.fill(largeCharacterTable, (char) (SmallCharMatcher.MAX_SIZE + 1), (char) (SmallCharMatcher.MAX_SIZE * 2), 'b');
 assertTrue(matcher.matches('a'));
 assertFalse(matcher.matches('b'));
 }

 @Test
 public void testMatchesWithCharactersOutsideTable() {
 char[] table = createTestTable(10, new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
 SmallCharMatcher matcher = new SmallCharMatcher(table, 0x1L, false, "Test Matcher");
 assertFalse(matcher.matches('k'));
 }

 @Test
 public void testMatchesWithCharactersInsideTable() {
 char[] table = createTestTable(10, new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
 SmallCharMatcher matcher = new SmallCharMatcher(table, 0x1L, false, "Test Matcher");
 assertTrue(matcher.matches('a'));
 assertTrue(matcher.matches('b'));
 assertTrue(matcher.matches('c'));
 assertFalse(matcher.matches('k'));
 }

 @Test
 public void testMatchesWithZeroInTable() {
 char[] table = createTestTable(10, new char[] {'a', 'b', 'c', '\u0000', 'e', 'f', 'g', 'h', 'i', 'j'});
 SmallCharMatcher matcher = new SmallCharMatcher(table, 0x1L, true, "Test Matcher");
 assertTrue(matcher.matches('\u0000'));
 }

 @Test
 public void testMatchesWithFilter() {
 char[] table = createTestTable(10, new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
 SmallCharMatcher matcher = new SmallCharMatcher(table, 0x1L | (1L << 5), false, "Test Matcher");
 assertFalse(matcher.matches('a'));
 assertFalse(matcher.matches('b'));
 assertTrue(matcher.matches('e'));
 assertFalse(matcher.matches('f'));
 }
}
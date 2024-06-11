package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

 private char[] getTableForTest() {
 char[] table = new char[10];
 table[0] = 'a';
 table[1] = 'e';
 table[2] = 'i';
 table[3] = 'o';
 table[4] = 'u';
 return table;
 }

 @Test
 public void testMatchesWithValidChar() {
 char[] table = getTableForTest();
 CharMatcher charMatcher = CharMatcher.from(new BitSet(), "Test CharMatcher");
 assertEquals(true, charMatcher.matches('e'));
 }

 @Test
 public void testMatchesWithInvalidChar() {
 char[] table = getTableForTest();
 CharMatcher charMatcher = CharMatcher.from(new BitSet(), "Test CharMatcher");
 assertEquals(false, charMatcher.matches('f'));
 }

 @Test
 public void testContainsZero() {
 char[] table = new char[1];
 CharMatcher charMatcher = new SmallCharMatcher(table, 0, true, "Test CharMatcher with containsZero = true");
 assertTrue(charMatcher.matches('\0'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTable() {
 CharMatcher charMatcher = new SmallCharMatcher(null, 0, false, "Test CharMatcher with null table");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeFilter() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[1], -1, false, "Test CharMatcher with negative filter");
 }

 @Test
 public void testChooseTableSize() {
 int size = 16;
 int expectedTableSize = 32;
 int actualTableSize = SmallCharMatcher.chooseTableSize(size);
 assertEquals(expectedTableSize, actualTableSize);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidIndexInMatchesMethod() {
 char[] table = new char[1];
 CharMatcher charMatcher = new SmallCharMatcher(table, 0, false, "Test CharMatcher with invalid index");
 char invalidChar = '\u0001';
 int startingIndex = (Integer.sum(smear(invalidChar.codePointAt(0)), 1) & (table.length - 1));
 int index = startingIndex;
 while (charMatcher.table[index] != '\0') {
 if (charMatcher.table[index] == invalidChar) {
fail("Invalid index should not match the character");
}
 index = (index + 1 & (table.length - 1));
 }
 assertTrue(charMatcher.table[index] == '\0');
 }

 @Test
 public void testMatchesWithEmptyTable() {
 char[] table = new char[0];
 CharMatcher charMatcher = new SmallCharMatcher(table, 0, false, "Test CharMatcher with empty table");
 assertFalse(charMatcher.matches('\u0001'));
 }

 @Test
 public void testMatchesWithFilteredOutChar() {
 char[] table = new char[1];
 table[0] = 'c';
 CharMatcher charMatcher = new SmallCharMatcher(table, 1L << 'a', false, "Test CharMatcher with filtered out character");
 assertFalse(charMatcher.matches('a'));
 }

}
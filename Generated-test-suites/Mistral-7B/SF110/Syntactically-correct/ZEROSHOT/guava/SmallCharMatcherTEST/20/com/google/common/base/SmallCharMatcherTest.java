package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher_whenValidInput_shouldCreateObject() {
 char[] table = new char[5];
 long filter = 1L;
 boolean containsZero = false;
 String description = "SmallCharMatcher description";
 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, description);
 assertNotNull(smallCharMatcher);
 }

 @Test
 public void testChooseTableSize_whenValidInput_shouldReturnCorrectTableSize() {
 int setSize = 10;
 int expectedTableSize = 16;
 int tableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals(expectedTableSize, tableSize);
 }

 @Test
 public void testCheckFilter_whenCharacterIsInFilter_shouldReturnTrue() {
 SmallCharMatcher smallCharMatcher = createSmallCharMatcherWithFilter('a', 'b', 'c', true);
 assertTrue(smallCharMatcher.checkFilter('a'));
 }

 @Test
 public void testCheckFilter_whenCharacterIsNotInFilter_shouldReturnFalse() {
 SmallCharMatcher smallCharMatcher = createSmallCharMatcherWithFilter('a', 'b', 'c', true);
 assertFalse(smallCharMatcher.checkFilter('d'));
 }

 @Test
 public void testMatches_whenCharacterIsInTable_shouldReturnTrue() {
 SmallCharMatcher smallCharMatcher = createSmallCharMatcherWithTableAndCharacter('a', 'b', 'a', true);
 assertTrue(smallCharMatcher.matches('a'));
 }

 @Test
 public void testMatches_whenCharacterIsNotInTable_shouldReturnFalse() {
 SmallCharMatcher smallCharMatcher = createSmallCharMatcherWithTableAndCharacter('a', 'b', 'c', true);
 assertFalse(smallCharMatcher.matches('d'));
 }

 @Test
 public void testMatches_whenCharacterIsZero_shouldReturnTrueIfContainsZero() {
 SmallCharMatcher smallCharMatcher = createSmallCharMatcherWithZero(true);
 assertTrue(smallCharMatcher.matches('\0'));
 }

 @Test
 public void testMatches_whenCharacterIsZero_shouldReturnFalseIfDoesNotContainZero() {
 SmallCharMatcher smallCharMatcher = createSmallCharMatcherWithZero(false);
 assertFalse(smallCharMatcher.matches('\0'));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetBits_shouldThrowUnsupportedOperationException() {
 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[5], 1L, false, "");
 try {
 smallCharMatcher.setBits(new BitSet());
 } catch (UnsupportedOperationException e) {
 assertEquals("This matcher does not support setting bits.", e.getMessage());
 throw e;
 }
 }

 private SmallCharMatcher createSmallCharMatcherWithTableAndCharacter(char c, char... chars) {
 long filter = 0L;
 int size = (chars.length > 0) ? chars.length : 1;
 BitSet bitSet = new BitSet();
 for (char character : chars) {
 bitSet.set(character);
 filter |= 1L << character;
 }
 char[] table = new char[SmallCharMatcher.chooseTableSize(size)];
 int mask = table.length - 1;
 for (int index = (smear(c) & mask), i = 0; i < table.length; i++) {
 if (i == index) {
 table[i] = c;
 break;
 }
 if (i > index && table[i] != '\0') {
 throw new IllegalStateException("Unexpected character in table: " + table[i]);
 }
 }
 return new SmallCharMatcher(table, filter, false, "");
 }

 private SmallCharMatcher createSmallCharMatcherWithFilter(char... chars) {
 long filter = 0L;
 int size = chars.length;
 BitSet bitSet = new BitSet();
 for (char character : chars) {
 bitSet.set(character);
 filter |= 1L << character;
 }
 return SmallCharMatcher.from(bitSet, "");
 }

 private SmallCharMatcher createSmallCharMatcherWithZero(boolean containsZero) {
 char[] table = new char[2];
 table[0] = '\0';
 if (containsZero) {
 table[1] = '\0';
 }
 return new SmallCharMatcher(table, 0L, containsZero, "");
 }
}
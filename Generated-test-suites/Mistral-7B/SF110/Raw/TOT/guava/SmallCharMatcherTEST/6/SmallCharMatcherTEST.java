package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

 private CharMatcher getMatcherForCharacters(char[] characters) {
 return SmallCharMatcher.from(BitSets.newBitSet(characteristicsToIntArray(characters)), "Matcher for given characters");
 }

 @Test
 public void testEmptyMatcher() {
 CharMatcher matcher = SmallCharMatcher.from(BitSets.create(), "Empty Matcher");
 assertTrue(matcher.matches('\0'));
}

 @Test
 public void testSingleCharacterMatcher() {
 char[] singleCharacter = {'a'};
 CharMatcher matcher = getMatcherForCharacters(singleCharacter);
 assertTrue(matcher.matches('a'));
 assertFalse(matcher.matches('b'));
}

 @Test
 public void testMultipleCharacterMatcher() {
 char[] multipleCharacters = {'a', 'b', 'c'};
 CharMatcher matcher = getMatcherForCharacters(multipleCharacters);
 assertTrue(matcher.matches('a'));
 assertTrue(matcher.matches('b'));
 assertTrue(matcher.matches('c'));
 assertFalse(matcher.matches('d'));
}

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCharacter() {
 char[] negativeCharacter = {(char) -1};
 try {
 getMatcherForCharacters(negativeCharacter);
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid character: -1", e.getMessage());
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testNullCharacter() {
 char[] nullCharacter = {'\0'};
 try {
 getMatcherForCharacters(nullCharacter);
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid character: null", e.getMessage());
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testLargeCharacterSet() {
 char[] largeCharacterSet = new char[SmallCharMatcher.MAX_SIZE + 1];
 try {
 getMatcherForCharacters(largeCharacterSet);
 } catch (IllegalArgumentException e) {
 assertEquals("The size of the character set is too large.", e.getMessage());
 }
}

 @Test
 public void testMatchesMultipleTimes() {
 char[] multipleCharacters = {'a', 'b', 'c'};
 CharMatcher matcher = getMatcherForCharacters(multipleCharacters);
 for (char character : multipleCharacters) {
 assertTrue(matcher.matches(character));
 }
}

 @Test
 public void testSetBits() {
 BitSet table = BitSets.create();
 CharMatcher matcher = SmallCharMatcher.from(BitSets.newBitSet(new int[] { 0, 1, 2 }), "Matcher for given bits");
 matcher.setBits(table);
 assertEquals(3, table.cardinality());
 assertTrue(table.get(0));
 assertTrue(table.get(1));
 assertTrue(table.get(2));
}

 @Test
 public void testSetBitsWithZero() {
 BitSet table = BitSets.create();
 CharMatcher matcher = SmallCharMatcher.from(BitSets.newBitSet(new int[] { 0 }), "Matcher for given bits with zero");
 matcher.setBits(table);
 assertEquals(1, table.cardinality());
 assertTrue(table.get(0));
}

 @Test
 public void testSetBitsWithDuplicates() {
 BitSet table = BitSets.create();
 CharMatcher matcher = SmallCharMatcher.from(BitSets.newBitSet(new int[] { 0, 1, 2, 1, 2 }), "Matcher for given bits with duplicates");
 matcher.setBits(table);
 assertEquals(3, table.cardinality());
 assertTrue(table.get(0));
 assertTrue(table.get(1));
 assertTrue(table.get(2));
}

 @Test
 public void testSetBitsWithInvalidCharacter() {
 BitSet table = BitSets.create();
 CharMatcher matcher = SmallCharMatcher.from(BitSets.newBitSet(new int[] { -1 }), "Matcher for given bits with invalid character");
 matcher.setBits(table);
 assertEquals(0, table.cardinality());
}

 private int[] characteristicsToIntArray(char[] characters) {
 int[] intValues = new int[characters.length];
 for (int i = 0; i < characters.length; i++) {
 intValues[i] = characters[i];
 }
 return intValues;
}
}
package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

 private CharMatcher createCharMatcher(char[] chars) {
 return SmallCharMatcher.from(new BitSet(chars.length), "SmallCharMatcherTest");
 }

 @Test
 public void testSmallCharMatcher_Empty() {
 char[] emptyTable = new char[0];
 SmallCharMatcher emptyMatcher = new SmallCharMatcher(emptyTable, 0, false, "");
 BitSet expected = new BitSet();
 assertArrayEquals("Sets the correct bits for an empty character set.", new char[0], emptyMatcher.getDescription().getAsString().toCharArray());
 emptyMatcher.setBits(expected);
 assertTrue("An empty SmallCharMatcher should match no characters.", !emptyMatcher.matches('\0'));
 assertTrue("An empty SmallCharMatcher should match no characters.", !emptyMatcher.matches('a'));
 assertTrue("An empty SmallCharMatcher should match no characters.", !emptyMatcher.matches('z'));
 assertTrue("An empty SmallCharMatcher should match no characters.", !emptyMatcher.matches('{'));
 assertTrue("An empty SmallCharMatcher should match no characters.", !emptyMatcher.matches('}'));
 }

 @Test
 public void testSmallCharMatcher_SingleCharacter() {
 char[] singleCharTable = new char[]{'a'};
 SmallCharMatcher singleCharMatcher = createCharMatcher(singleCharTable);
 BitSet expected = new BitSet();
 expected.set('a');
 assertArrayEquals("Sets the correct bit for a single character.", new char[]{'a'}, singleCharMatcher.getDescription().getAsString().toCharArray());
 singleCharMatcher.setBits(expected);
 assertTrue("A SmallCharMatcher for a single character should match that character.", singleCharMatcher.matches('a'));
 assertFalse("A SmallCharMatcher for a single character should not match any other character.", singleCharMatcher.matches('b'));
 }

 @Test
 public void testSmallCharMatcher_MultipleCharacters() {
 char[] multipleCharsTable = new char[]{'a', 'b', 'c'};
 SmallCharMatcher multipleCharsMatcher = createCharMatcher(multipleCharsTable);
 BitSet expected = new BitSet();
 expected.set('a');
 expected.set('b');
 expected.set('c');
 assertArrayEquals("Sets the correct bits for multiple characters.", new char[]{'a', 'b', 'c'}, multipleCharsMatcher.getDescription().getAsString().toCharArray());
 multipleCharsMatcher.setBits(expected);
 assertTrue("A SmallCharMatcher for multiple characters should match those characters.", multipleCharsMatcher.matches('a'));
 assertTrue("A SmallCharMatcher for multiple characters should match those characters.", multipleCharsMatcher.matches('b'));
 assertTrue("A SmallCharMatcher for multiple characters should match those characters.", multipleCharsMatcher.matches('c'));
 assertFalse("A SmallCharMatcher for multiple characters should not match any other character.", multipleCharsMatcher.matches('d'));
 }

 @Test
 public void testSmallCharMatcher_ContainsZero() {
 char[] containsZeroTable = new char[]{'0'};
 SmallCharMatcher containsZeroMatcher = new SmallCharMatcher(containsZeroTable, 0, true, "");
 BitSet expected = new BitSet();
 expected.set(0);
 assertArrayEquals("Sets the correct bit for zero in a SmallCharMatcher that contains zero.", new char[]{'0'}, containsZeroMatcher.getDescription().getAsString().toCharArray());
 containsZeroMatcher.setBits(expected);
 assertTrue("A SmallCharMatcher that contains zero should match zero.", containsZeroMatcher.matches('0'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_NullTable() {
 try {
 new SmallCharMatcher(null, 0, false, "");
 fail("Should throw IllegalArgumentException for null table.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_EmptyFilterBitSet() {
 try {
 SmallCharMatcher emptyFilterMatcher = new SmallCharMatcher(new char[1], 0, false, "");
 emptyFilterMatcher.setFilterBitSet(new BitSet());
 fail("Should throw IllegalArgumentException for an empty filter bitset.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSmallCharMatcher_FilterBitSetSize() {
 char[] chars = new char[1024];
 SmallCharMatcher smallCharMatcher = createCharMatcher(chars);
 BitSet filterBitSet = smallCharMatcher.getFilterBitSet();
 int filterBitSetSize = filterBitSet.cardinality();
 assertEquals("The filter bitset size should match the table size.", chars.length, filterBitSetSize);
 }

 @Test
 public void testSmallCharMatcher_FilterBitSetContainsExpectedChars() {
 char[] chars = new char[10];
 SmallCharMatcher smallCharMatcher = createCharMatcher(chars);
 BitSet filterBitSet = smallCharMatcher.getFilterBitSet();
 for (int i = 0; i < chars.length; i++) {
 char c = chars[i];
 if (c != '\0') {
 assertTrue("The filter bitset should contain the expected characters.", filterBitSet.get(c));
 }
 }
 }
}
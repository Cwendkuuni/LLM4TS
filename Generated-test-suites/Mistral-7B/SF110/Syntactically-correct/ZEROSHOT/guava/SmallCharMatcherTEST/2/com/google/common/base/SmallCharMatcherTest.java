package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher_valid() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(new int[] { 'a', 'b', 'c' }), "SmallCharMatcherTest");
 char expected[] = {'a', 'b', 'c'};
 char actual[] = new char[charMatcher.table.length];
 charMatcher.setBits(BitSets.newMutableBitSet());
 for (int i = 0; i < charMatcher.table.length; i++) {
 actual[i] = charMatcher.table[i];
 }
 assertArrayEquals(expected, actual);

 for (char c : new char[]{'a', 'b', 'c'}) {
 assertTrue(charMatcher.matches(c));
 }
 }

 @Test
 public void testSmallCharMatcher_invalid() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(new int[] { 'a', 'b', 'c' }), "SmallCharMatcherTest");
 assertFalse(charMatcher.matches('\0'));
 for (char c : new char[]{'d', 'e', 'f'}) {
 assertFalse(charMatcher.matches(c));
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSmallCharMatcher_immutable() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(new int[] { 'a', 'b', 'c' }), "SmallCharMatcherTest");
 try {
 charMatcher.setBits(BitSets.newMutableBitSet());
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testSmallCharMatcher_containsZero() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(new int[] { 0 }), "SmallCharMatcherTest");
 assertTrue(charMatcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcher_chooseTableSize() {
 int size = 12;
 int expectedTableSize = (int)Math.pow(2, 5);
 SmallCharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(new int[size]), "SmallCharMatcherTest");
 int tableSize = SmallCharMatcher.chooseTableSize(size);
 assertEquals(expectedTableSize, tableSize);
 }

}
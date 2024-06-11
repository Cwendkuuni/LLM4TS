package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher_empty() {
 char[] table = new char[0];
 SmallCharMatcher charMatcher = SmallCharMatcher.from(BitSets.bitSet(), "Empty CharMatcher");

 assertArrayEquals(new char[0], charMatcher.table);
 assertFalse(charMatcher.containsZero);
 assertFalse(charMatcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcher_singleCharacter() {
 BitSet charSet = BitSets.newBitSet();
 charSet.set(10);
 SmallCharMatcher charMatcher = SmallCharMatcher.from(charSet, "Single Character CharMatcher");

 int mask = charMatcher.table.length - 1;
 int index;
 char expectedChar = (char) 10;

 charMatcher.setBits(BitSets.newMutableBitSet());

 assertTrue(charMatcher.matches(expectedChar));
 assertTrue(charMatcher.containsZero);
 BitSet table = BitSets.newMutableBitSet();
 charMatcher.setBits(table);
 assertTrue(table.get(10));
 }

 @Test
 public void testSmallCharMatcher_multipleCharacters() {
 BitSet charSet = BitSets.newBitSet();
 charSet.set(10);
 charSet.set(20);
 SmallCharMatcher charMatcher = SmallCharMatcher.from(charSet, "Multiple Characters CharMatcher");

 int mask = charMatcher.table.length - 1;
 int index;
 char expectedChar1 = (char) 10;
 char expectedChar2 = (char) 20;

 charMatcher.setBits(BitSets.newMutableBitSet());

 assertTrue(charMatcher.matches(expectedChar1));
 assertTrue(charMatcher.matches(expectedChar2));
 assertTrue(charMatcher.containsZero);
 BitSet table = BitSets.newMutableBitSet();
 charMatcher.setBits(table);
 assertTrue(table.get(10));
 assertTrue(table.get(20));
 }

 @Test
 public void testSmallCharMatcher_filter() {
 BitSet charSet = BitSets.newBitSet();
 charSet.set(10);
 charSet.set(20);
 charSet.set(50);
 SmallCharMatcher charMatcher1 = SmallCharMatcher.from(charSet, "CharMatcher1");
 charSet.clear();
 charSet.set(20);
 charSet.set(65);
 SmallCharMatcher charMatcher2 = SmallCharMatcher.from(charSet, "CharMatcher2");
 SmallCharMatcher orCharMatcher = CharMatchers.or(charMatcher1, charMatcher2);

 int mask = orCharMatcher.table.length - 1;
 int index;
 char expectedChar1 = (char) 10;
 char expectedChar2 = (char) 20;
 char expectedChar3 = (char) 65;

 charMatcher1.setBits(BitSets.newMutableBitSet());
 charMatcher2.setBits(BitSets.newMutableBitSet());
 orCharMatcher.setBits(BitSets.newMutableBitSet());

 assertTrue(orCharMatcher.matches(expectedChar1));
 assertTrue(orCharMatcher.matches(expectedChar2));
 assertTrue(orCharMatcher.matches(expectedChar3));
 BitSet table = BitSets.newMutableBitSet();
 orCharMatcher.setBits(table);
 assertTrue(table.get(10));
 assertTrue(table.get(20));
 assertTrue(table.get(65));
 }

 @Test
 public void testSmallCharMatcher_size() {
 BitSet charSet = BitSets.newBitSet();
 for (int i = 0; i < 1024; i++) {
 charSet.set(i);
 }
 SmallCharMatcher charMatcher = SmallCharMatcher.from(charSet, "Full Size CharMatcher");
 int tableSize = charMatcher.table.length;
 assertTrue(tableSize > 0);
 assertTrue(tableSize <= SmallCharMatcher.MAX_SIZE);
 }
}
package com.google.common.base;

import static org.junit.Assert.*;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.collect.BitSets;
import java.util.Arrays;

public class SmallCharMatcherTest {

 @Test
 public void testMatchesChar() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "matches 'a'");
 assertTrue(charMatcher.matches('a'));
 CharMatcher charMatcher1 = SmallCharMatcher.from(BitSets.newBitSet(), "matches 'z'");
 assertFalse(charMatcher1.matches('a'));
 }

 @Test
 public void testMatchesMultipleChars() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create(65, 66, 67), "matches 'A' or 'B' or 'C'");
 assertTrue(charMatcher.matches('A'));
 assertTrue(charMatcher.matches('B'));
 assertTrue(charMatcher.matches('C'));
 }

 @Test
 public void testMatchesZero() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "matches the null character");
 assertTrue(charMatcher.matches((char) 0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMatchesNullTable() {
 CharMatcher charMatcher = new SmallCharMatcher(null, 0L, false, "null table");
 }

 @Test
 public void testMatchesEmptyTable() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "empty table");
 assertFalse(charMatcher.matches('a'));
 }

 @Test
 public void testMatchesFilter() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "matches 'a' and 'e' and not 'i'");
 assertFalse(charMatcher.matches('i'));
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('e'));
 }

 @Test
 public void testMatchesTableSize() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15), "matches all characters from 'a' to 'n'");
 for (int i = 0; i < 26; i++) {
 char c = (char) (i + 97);
 assertTrue(charMatcher.matches(c));
 }
 }

 @Test
 public void testSetBits() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(1, 2, 3), "matches 'a', 'b', 'c'");
 CharMatcher charMatcher1 = CharMatcher.matches('a');
 CharMatcher charMatcher2 = CharMatcher.or(charMatcher1, charMatcher);
 CharMatcher charMatcher3 = SmallCharMatcher.from(BitSets.newBitSet(), "initialized with empty set");
 char[] table = new char[SmallCharMatcher.chooseTableSize(3)];
 charMatcher3.setBits(BitSets.newMutableBitSet());
 charMatcher3.setBits(table);
 CharMatcher charMatcher4 = SmallCharMatcher.from(table, 0L, false, "table populated by setBits()");
 assertEquals(charMatcher2, charMatcher4);
 }

 @Test
 public void testSetBitsZeroFlag() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "matches any character except the null character");
 CharMatcher charMatcher1 = CharMatcher.not(SmallCharMatcher.matches((char) 0));
 CharMatcher charMatcher2 = CharMatcher.or(charMatcher, charMatcher1);
 CharMatcher charMatcher3 = SmallCharMatcher.from(BitSets.newBitSet(), "initialized with empty set");
 char[] table = new char[SmallCharMatcher.chooseTableSize(Character.MAX_VALUE)];
 charMatcher3.setBits(BitSets.newMutableBitSet());
 charMatcher3.setBits(table);
 CharMatcher charMatcher4 = SmallCharMatcher.from(table, 0L, false, "table populated by setBits()");
 assertEquals(charMatcher2, charMatcher4);
 }

 @Test
 @VisibleForTesting
 public void testChooseTableSize() {
 assertEquals(2, SmallCharMatcher.chooseTableSize(1));
 assertEquals(16, SmallCharMatcher.chooseTableSize(32));
 assertEquals(128, SmallCharMatcher.chooseTableSize(256));
 assertEquals(1024, SmallCharMatcher.chooseTableSize(1024));
 assertEquals(1023, SmallCharMatcher.chooseTableSize(1025));
 }

 @Test
 public void testSmeaar() {
 int hashCode = 123;
 int smearedHashCode = SmallCharMatcher.smear(hashCode);
 int expected = 1092232382;
 assertEquals(expected, smearedHashCode);
 }
}
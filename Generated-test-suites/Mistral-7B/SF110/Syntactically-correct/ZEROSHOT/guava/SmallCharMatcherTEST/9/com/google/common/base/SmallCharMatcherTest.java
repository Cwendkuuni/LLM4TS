package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcher test");
 char c;

 // Test empty CharMatcher
 assertTrue(charMatcher.matches('\0'));
 assertFalse(charMatcher.matches('a'));
 BitSet table = new BitSet();
 charMatcher.setBits(table);
 assertTrue(table.get(0));
 assertFalse(table.get('a'.charValue()));

 // Test CharMatcher with specific characters
 charMatcher = SmallCharMatcher.from(BitSets.newBitSet(Character.toIntArray(new char[]{'a', 'b'})), "SmallCharMatcher test with 'a' and 'b'");
 c = 'a';
 assertTrue(charMatcher.matches(c));
 assertFalse(charMatcher.matches('b'));
 table.clear();
 charMatcher.setBits(table);
 assertTrue(table.get(Character.codePointAt('a')));
 assertFalse(table.get(Character.codePointAt('b')));

 // Test CharMatcher with large set of characters
 charMatcher = SmallCharMatcher.from(BitSets.newBitSet(Character.toIntArray(new char[SmallCharMatcher.MAX_SIZE - 1])), "SmallCharMatcher test with all characters except '\0'");
 for (c = '\u0001'; c < 0x10FFFF; c++) {
 if (c != '\0') {
 table.clear();
 charMatcher.setBits(table);
 assertTrue(table.get(c));
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTableSize() {
 try {
 SmallCharMatcher.from(new BitSet(), "SmallCharMatcher test with invalid table size");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeSetSize() {
 try {
 SmallCharMatcher.from(BitSets.newBitSet(-1), "SmallCharMatcher test with negative set size");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSmearedHashes() {
 for (int i = 0; i < SmallCharMatcher.MAX_SIZE; i++) {
 int hashCode = i;
 int smearedHashCode = SmallCharMatcher.smear(hashCode);
 int tableSize = SmallCharMatcher.chooseTableSize(Integer.bitCount(Long.toLong(Long.toUnsignedLong(1L << smearedHashCode))));
 assertEquals(tableSize, SmallCharMatcher.chooseTableSize(Integer.bitCount(Long.toLong(1L << i))));
 }
 }

 @Test
 public void testCheckFilter() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcher test checkFilter");
 char c = '\u00FF';
 for (int i = 0; i < 256; i++) {
 assertTrue(charMatcher.checkFilter((char)i));
 charMatcher.filter |= 1L << i;
 }
 assertFalse(charMatcher.checkFilter((char)256));
 }

 @Test
 public void testContainsZero() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(true), "SmallCharMatcher test containsZero true");
 assertTrue(charMatcher.containsZero);
 SmallCharMatcher charMatcher1 = SmallCharMatcher.from(new BitSet(false), "SmallCharMatcher test containsZero false");
 assertFalse(charMatcher1.containsZero);
 }
}
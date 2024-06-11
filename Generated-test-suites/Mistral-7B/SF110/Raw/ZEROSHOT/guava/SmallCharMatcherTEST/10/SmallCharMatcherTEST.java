package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcherCreation() {
 char[] table = new char[10];
 BitSet chars = new BitSet(10);
 chars.set(5, true);
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "CharMatcher for character 5");
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;

 assertEquals("CharMatcher for character 5", smallCharMatcher.description());
 assertArrayEquals(new char[]{'5'}, smallCharMatcher.table);
 assertTrue(smallCharMatcher.containsZero());
 assertFalse(smallCharMatcher.checkFilter('2'));
 assertTrue(smallCharMatcher.matches('5'));
 }

 @Test
 public void testSmallCharMatcherEmpty() {
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Empty CharMatcher");
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;

 assertEquals("Empty CharMatcher", smallCharMatcher.description());
 assertArrayEquals(new char[0], smallCharMatcher.table);
 assertFalse(smallCharMatcher.containsZero());
 }

 @Test
 public void testSmallCharMatcherSingleCharacter() {
 char[] table = new char[2];
 table[0] = 'a';
 BitSet chars = new BitSet(1);
 chars.set(0, true);
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "CharMatcher for character a");
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;

 assertEquals("CharMatcher for character a", smallCharMatcher.description());
 assertArrayEquals(new char[]{'a'}, smallCharMatcher.table);
 assertFalse(smallCharMatcher.containsZero());
 assertTrue(smallCharMatcher.matches('a'));
 }

 @Test
 public void testSmallCharMatcherMultipleCharacters() {
 char[] table = new char[16];
 BitSet chars = new BitSet(7);
 chars.set(1, true);
 chars.set(3, true);
 chars.set(5, true);
 chars.set(7, true);
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "CharMatcher for characters 1, 3, 5, and 7");
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;

 assertEquals("CharMatcher for characters 1, 3, 5, and 7", smallCharMatcher.description());
 assertArrayEquals(new char[]{'1', '3', '5', '7'}, smallCharMatcher.table);
 assertFalse(smallCharMatcher.containsZero());
 assertTrue(smallCharMatcher.matches('1'));
 assertTrue(smallCharMatcher.matches('3'));
 assertTrue(smallCharMatcher.matches('5'));
 assertTrue(smallCharMatcher.matches('7'));
 }

 @Test
 public void testSmallCharMatcherNoMatch() {
 char[] table = new char[16];
 BitSet chars = new BitSet(7);
 chars.set(1, true);
 chars.set(3, true);
 chars.set(5, true);
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "CharMatcher for characters 1, 3, and 5");
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;

 assertFalse(smallCharMatcher.matches('0'));
 assertFalse(smallCharMatcher.matches('2'));
 assertFalse(smallCharMatcher.matches('4'));
 assertFalse(smallCharMatcher.matches('6'));
 assertFalse(smallCharMatcher.matches('8'));
 }

 @Test
 public void testSmallCharMatcherSetBits() {
 char[] table = new char[16];
 BitSet tableBits = new BitSet(16);
 BitSet chars = new BitSet(7);
 chars.set(1, true);
 chars.set(3, true);
 chars.set(5, true);
 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, 0x1L, false, "TestCharMatcher");
 smallCharMatcher.setBits(tableBits);

 assertTrue(tableBits.get(5));
 assertTrue(tableBits.get(3));
 assertTrue(tableBits.get(1));
 assertFalse(tableBits.get(0));
 }
}
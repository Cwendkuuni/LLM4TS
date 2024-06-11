package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcherCreation() {
 char[] table = new char[3];
 table[0] = 'a';
 table[1] = 'b';
 table[2] = '\0';
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "SmallCharMatcherTest");
 assertEquals("SmallCharMatcherTest", charMatcher.description());
 assertArrayEquals(table, charMatcher.getTable());
 assertFalse(charMatcher.containsZero());
 }

 @Test
 public void testSmallCharMatcherMatches() {
 char[] table = new char[3];
 table[0] = 'a';
 table[1] = 'b';
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "SmallCharMatcherTest");
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertFalse(charMatcher.matches('c'));
 }

 @Test
 public void testSmallCharMatcherContainsZero() {
 char[] table = new char[3];
 table[0] = 'a';
 table[1] = 'b';
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, true, "SmallCharMatcherTest");
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertTrue(charMatcher.matches('0'));
 }

 @Test
 public void testSmallCharMatcherFilter() {
 char[] table = new char[3];
 table[0] = 'a';
 table[1] = 'b';
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 1L, false, "SmallCharMatcherTest");
 assertFalse(charMatcher.matches('a'));
 assertFalse(charMatcher.matches('b'));
 assertFalse(charMatcher.matches('0'));
 table[2] = 'a';
 charMatcher = new SmallCharMatcher(table, 1L, false, "SmallCharMatcherTest");
 assertTrue(charMatcher.matches('a'));
 assertFalse(charMatcher.matches('b'));
 assertFalse(charMatcher.matches('0'));
 table[2] = '\0';
 charMatcher = new SmallCharMatcher(table, 1L, false, "SmallCharMatcherTest");
 assertFalse(charMatcher.matches('a'));
 assertFalse(charMatcher.matches('b'));
 assertFalse(charMatcher.matches('0'));
 }

 @Test
 public void testChooseTableSize() {
 int setSize = 10;
 int expectedTableSize = 16;
 int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals(expectedTableSize, actualTableSize);
 }

 @Test
 public void testFrom() {
 BitSet chars = new BitSet();
 chars.set('a');
 chars.set('b');
 chars.set('c');
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "SmallCharMatcherTest");
 char[] table = charMatcher.getTable();
 assertEquals(3, table.length);
 assertTrue(table[0] == 'a');
 assertTrue(table[1] == 'b');
 assertFalse(table[2] == '\0');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromEmptyBitSet() {
 BitSet emptyBitSet = new BitSet();
 CharMatcher charMatcher = SmallCharMatcher.from(emptyBitSet, "SmallCharMatcherTest");
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromNullDescription() {
 BitSet chars = new BitSet();
 chars.set('a');
 chars.set('b');
 CharMatcher charMatcher = SmallCharMatcher.from(chars, null);
 fail("Should throw IllegalArgumentException");
 }
}
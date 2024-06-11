package com.google.common.base;

import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcherCreation() {
 char[] table = new char[10];
 BitSet charSet = BitSet.create();
 charSet.add('a');
 charSet.add('b');
 CharMatcher charMatcher = SmallCharMatcher.from(charSet, "SmallCharMatcher for 'a' and 'b'");

 assertEquals("SmallCharMatcher for 'a' and 'b'", charMatcher.description());
 assertArrayEquals(new char[]{'a', 'b'}, charMatcher.getTable());
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertFalse(charMatcher.matches('c'));
 }

 @Test
 public void testSmallCharMatcherWithZero() {
 char[] table = new char[5];
 BitSet charSet = BitSet.create();
 charSet.add('a');
 charSet.add('b');
 BitSet zeroSet = BitSet.create();
 zeroSet.set(0);
 CharMatcher charMatcher = SmallCharMatcher.from(charSet, "SmallCharMatcher for 'a' and 'b'");
 CharMatcher zeroMatcher = SmallCharMatcher.from(zeroSet, "SmallCharMatcher for zero");

 assertArrayEquals(new char[0], charMatcher.getTable());
 assertFalse(charMatcher.matches('\0'));
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertFalse(charMatcher.matches('c'));

 Table<Character, Character, Boolean> tableOfMatches = HashBasedTable.create();
 charMatcher.setBits(tableOfMatches.columnKeySet());
 zeroMatcher.setBits(tableOfMatches.rowKeySet());

 for (Character c1 : tableOfMatches.rowKeySet()) {
 for (Character c2 : tableOfMatches.columnKeySet()) {
 tableOfMatches.put(c1, c2, charMatcher.matches(c2) || zeroMatcher.matches(c2));
 }
 }

 assertTrue(tableOfMatches.get('a', 'a'));
 assertTrue(tableOfMatches.get('a', 'b'));
 assertTrue(tableOfMatches.get('a', '\0'));
 assertTrue(tableOfMatches.get('b', 'a'));
 assertTrue(tableOfMatches.get('b', 'b'));
 assertTrue(tableOfMatches.get('b', '\0'));
 assertFalse(tableOfMatches.get('c', 'a'));
 assertFalse(tableOfMatches.get('c', 'b'));
 assertFalse(tableOfMatches.get('c', '\0'));
 }

 @Test
 public void testSmallCharMatcherEdgeCases() {
 char[] table = new char[1];
 BitSet charSet = BitSet.create();
 charSet.add('a');
 CharMatcher charMatcher = SmallCharMatcher.from(charSet, "SmallCharMatcher for 'a'");

 assertEquals("SmallCharMatcher for 'a'", charMatcher.description());
 assertArrayEquals(new char[]{'\0'}, charMatcher.getTable());
 assertFalse(charMatcher.matches('\0'));
 assertTrue(charMatcher.matches('a'));
 assertFalse(charMatcher.matches('b'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcherNullTable() {
 char[] table = null;
 BitSet charSet = BitSet.create();
 charSet.add('a');
 CharMatcher charMatcher = SmallCharMatcher.from(charSet, "SmallCharMatcher for 'a'");
 charMatcher.setTable(table);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcherNegativeTableSize() {
 char[] table = new char[1];
 BitSet charSet = BitSet.create();
 charSet.add('a');
 CharMatcher charMatcher = SmallCharMatcher.from(charSet, "SmallCharMatcher for 'a'");
 charMatcher.setTable(new char[-1]);
 }

 @Test
 public void testSmallCharMatcherFilterBit() {
 char[] table = new char[5];
 BitSet charSet = BitSet.create();
 charSet.add('a');
 charSet.add('b');
 charSet.add('c');
 BitSet zeroSet = BitSet.create();
 zeroSet.set(0);
 CharMatcher charMatcher = SmallCharMatcher.from(charSet, "SmallCharMatcher for 'a', 'b', and 'c'");
 CharMatcher zeroMatcher = SmallCharMatcher.from(zeroSet, "SmallCharMatcher for zero");
 charMatcher.setFilter(0xFFFFFFFFL ^ 0x1L); // invert all bits

 assertFalse(charMatcher.matches('a'));
 assertFalse(charMatcher.matches('b'));
 assertFalse(charMatcher.matches('c'));
 assertFalse(charMatcher.matches('\0'));
 }
}
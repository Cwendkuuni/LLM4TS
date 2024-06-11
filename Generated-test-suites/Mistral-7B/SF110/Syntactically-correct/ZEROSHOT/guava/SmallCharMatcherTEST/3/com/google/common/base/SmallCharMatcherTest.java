package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

 private char[] getTable(char[] table, int size) {
 char[] tableToTest = new char[size];
 System.arraycopy(table, 0, tableToTest, 0, size);
 return tableToTest;
 }

 @Test
 public void testSmallCharMatcher_valid() {
 char[] chars = new char[10];
 Arrays.fill(chars, 'a');
 CharMatcher charMatcher = CharMatcher.from(new BitSet(chars), "Matcher for char 'a'");
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;

 assertTrue(smallCharMatcher.matches('a'));
 assertTrue(smallCharMatcher.matches('a'));

 char[] table = getTable(smallCharMatcher.table, smallCharMatcher.table.length);
 assertEquals('a', table[smear('a') & (table.length - 1)]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_emptySet() {
 char[] chars = new char[0];
 BitSet bitSet = new BitSet();
 CharMatcher charMatcher = CharMatcher.from(bitSet, "Matcher for empty set");

 try {
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_setWithZeroAndNonZero() {
 char[] chars = new char[3];
 chars[0] = '\0';
 chars[1] = 'a';
 chars[2] = 'b';
 BitSet bitSet = new BitSet();
 bitSet.set(chars);
 CharMatcher charMatcher = CharMatcher.from(bitSet, "Matcher for set with zero and non-zero");

 try {
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSmallCharMatcher_matchesZeroWithZeroMatcher() {
 SmallCharMatcher zeroMatcher = SmallCharMatcher.forRegex(".");
 assertTrue(zeroMatcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcher_matchesZeroWithNonZeroMatcher() {
 SmallCharMatcher nonZeroMatcher = new SmallCharMatcher(new char[0], 0, false, "Non-zero matcher");
 assertFalse(nonZeroMatcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcher_matchesNonZero() {
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, 0, false, "Matcher for char 'a'");
 assertTrue(charMatcher.matches('a'));
 }

 @Test
 public void testSmallCharMatcher_matchesNonZero_differentTable() {
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, 0, false, "Matcher for char 'a'");
 char[] table = getTable(charMatcher.table, charMatcher.table.length);
 assertEquals('a', table[smear('a') & (table.length - 1)]);
 }

 @Test
 public void testSmallCharMatcher_matchesNonZero_differentFilter() {
 SmallCharMatcher charMatcher1 = new SmallCharMatcher(new char[]{'a'}, 0x1L, false, "Matcher for char 'a' with filter 0x1L");
 SmallCharMatcher charMatcher2 = new SmallCharMatcher(new char[]{'a'}, 0x2L, false, "Matcher for char 'a' with filter 0x2L");

 assertFalse(charMatcher1.matches('a'));
 assertTrue(charMatcher2.matches('a'));
 }

 @Test
 public void testSmallCharMatcher_matchesNonZero_differentContainsZero() {
 SmallCharMatcher charMatcher1 = new SmallCharMatcher(new char[]{'a'}, 0, true, "Matcher for char 'a' with containsZero=true");
 SmallCharMatcher charMatcher2 = new SmallCharMatcher(new char[]{'a'}, 0, false, "Matcher for char 'a' with containsZero=false");

 assertTrue(charMatcher1.matches('\0'));
 assertFalse(charMatcher2.matches('\0'));
 }

 @Test
 public void testSmallCharMatcher_matchesNonZero_sameTableButDifferentMask() {
 SmallCharMatcher charMatcher1 = new SmallCharMatcher(new char[]{'a'}, 0, false, "Matcher for char 'a'");
 SmallCharMatcher charMatcher2 = new SmallCharMatcher(new char[]{'a'}, 0, false, "Matcher for char 'a' with different mask");

 char[] table1 = getTable(charMatcher1.table, charMatcher1.table.length);
 char[] table2 = getTable(charMatcher2.table, charMatcher2.table.length);

 assertEquals('a', table1[smear('a') & (table1.length - 1)]);
 assertEquals('a', table2[smear('a') & (table2.length - 1)]);
 }

 @Test
 public void testSmallCharMatcher_setBits_valid() {
 BitSet table = new BitSet();
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Matcher for empty set");
 charMatcher.setBits(table);

 assertTrue(table.get('a'));
 }

 @Test
 public void testSmallCharMatcher_setBits_setZero() {
 BitSet table = new BitSet();
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Matcher for empty set");
 charMatcher.setBits(table);

 assertTrue(table.get(0));
 }

 @Test
 public void testSmallCharMatcher_setBits_setNonZero() {
 BitSet table = new BitSet();
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, 0, false, "Matcher for char 'a'");
 charMatcher.setBits(table);

 assertTrue(table.get('a'));
 }
}
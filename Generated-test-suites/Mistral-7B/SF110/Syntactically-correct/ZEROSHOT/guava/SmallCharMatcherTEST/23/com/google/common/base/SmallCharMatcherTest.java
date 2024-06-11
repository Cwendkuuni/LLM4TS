package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.BitSet;

public class SmallCharMatcherTest {

 private char[] createTableForTest(char[] testChars) {
 char[] table = new char[SmallCharMatcher.chooseTableSize(testChars.length)];
 BitSet chars = new BitSet(table.length);
 for (char c : testChars) {
 chars.set(c);
 int index;
 for (index = (SmallCharMatcher.smear(c) & (table.length - 1)); table[index] != '\0'; index = (index + 1 & (table.length - 1))) {
 table[index] = c;
 }
 }
 return SmallCharMatcher.from(chars, "SmallCharMatcherTest").table;
 }

 @Test
 public void testSmallCharMatcher_Empty() {
 char[] table = new SmallCharMatcher(new char[0], 0L, false, "SmallCharMatcher_Empty").table;
 assertTrue(new SmallCharMatcher(table, 0L, false, "SmallCharMatcher_Empty").matches('\0'));
 assertFalse(new SmallCharMatcher(table, 0L, false, "SmallCharMatcher_Empty").matches('a'));
 }

 @Test
 public void testSmallCharMatcher_SingleCharacter() {
 char[] table = createTableForTest(new char[]{'a'});
 assertTrue(new SmallCharMatcher(table, 1L << 'a', false, "SmallCharMatcher_SingleCharacter").matches('a'));
 assertFalse(new SmallCharMatcher(table, 1L << 'a', false, "SmallCharMatcher_SingleCharacter").matches('b'));
 }

 @Test
 public void testSmallCharMatcher_MultipleCharacters() {
 char[] table = createTableForTest(new char[]{'a', 'b', 'c'});
 assertTrue(new SmallCharMatcher(table, (1L << 'a') | (1L << 'b') | (1L << 'c'), false, "SmallCharMatcher_MultipleCharacters").matches('a'));
 assertTrue(new SmallCharMatcher(table, (1L << 'a') | (1L << 'b') | (1L << 'c'), false, "SmallCharMatcher_MultipleCharacters").matches('b'));
 assertTrue(new SmallCharMatcher(table, (1L << 'a') | (1L << 'b') | (1L << 'c'), false, "SmallCharMatcher_MultipleCharacters").matches('c'));
 assertFalse(new SmallCharMatcher(table, (1L << 'a') | (1L << 'b') | (1L << 'c'), false, "SmallCharMatcher_MultipleCharacters").matches('d'));
 }

 @Test
 public void testSmallCharMatcher_ContainsZero() {
 char[] table = new SmallCharMatcher(new char[0], 0L, true, "SmallCharMatcher_ContainsZero").table;
 assertTrue(new SmallCharMatcher(table, 0L, true, "SmallCharMatcher_ContainsZero").matches('\0'));
 assertFalse(new SmallCharMatcher(table, 0L, true, "SmallCharMatcher_ContainsZero").matches('a'));
 }

 @Test(expected = AssertionError.class)
 public void testSmallCharMatcher_TableSizeOverflow() {
 char[] testChars = new char[SmallCharMatcher.MAX_SIZE + 1];
 char[] table = SmallCharMatcher.from(new BitSet(testChars), "SmallCharMatcher_TableSizeOverflow").table;
 fail("Expected AssertionError, but got table with size " + table.length);
 }
}
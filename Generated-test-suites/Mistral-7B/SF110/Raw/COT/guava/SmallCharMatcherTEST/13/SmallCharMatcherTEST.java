package com.example.junit.tests;

import static org.junit.Assert.*;

import com.google.common.base.*;
import org.junit.Test;

public class SmallCharMatcherTest {

 private char[] createTable(int size, char[] chars) {
 char[] table = new char[chooseTableSize(size)];
 SmallCharMatcher.from(new BitSet(chars), "Test CharMatcher");
 return table;
 }

 @Test
 public void testSmallCharMatcher_matches_charInTable() {
 char[] table = createTable(5, new char[]{'a', 'e', 'i', 'o', 'u'});
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('e'));
 assertTrue(charMatcher.matches('i'));
 assertTrue(charMatcher.matches('o'));
 assertTrue(charMatcher.matches('u'));
 }

 @Test
 public void testSmallCharMatcher_matches_charNotInTable() {
 char[] table = createTable(5, new char[]{'a', 'e', 'i', 'o', 'u'});
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
 assertFalse(charMatcher.matches('b'));
 }

 @Test
 public void testSmallCharMatcher_matches_charInTableWithFilter() {
 char[] table = createTable(5, new char[]{'a', 'e', 'i', 'o', 'u'});
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 1L, false, "Test CharMatcher");
 assertFalse(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('e'));
 assertTrue(charMatcher.matches('i'));
 assertFalse(charMatcher.matches('o'));
 assertTrue(charMatcher.matches('u'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_throwsExceptionWhenSizeIsZero() {
 char[] table = createTable(0, null);
 try {
 new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
 } catch (IllegalArgumentException e) {
 assertEquals("The set of characters cannot be empty.", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_throwsExceptionWhenSizeIsNegative() {
 char[] table = createTable(-1, null);
 try {
 new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
 } catch (IllegalArgumentException e) {
 assertEquals("The set of characters cannot contain negative numbers.", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_throwsExceptionWhenTableSizeIsNegative() {
 char[] table = createTable(10, new char[]{-1});
 try {
 new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
 } catch (IllegalArgumentException e) {
 assertEquals("The table size must be a power of 2.", e.getMessage());
 throw e;
 }
 }

}
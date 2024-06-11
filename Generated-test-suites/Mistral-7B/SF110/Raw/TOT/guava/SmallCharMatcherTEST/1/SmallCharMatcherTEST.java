package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SmallCharMatcherTest {

 private static final char[] TEST_CHAR_ARRAY = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
 private static final BitSet TEST_BIT_SET = new BitSet();

 @Test
 public void testSmallCharMatcherCreation() {
 charMatcher = SmallCharMatcher.from(TEST_BIT_SET, "Test CharMatcher");
 assertTrue(charMatcher instanceof SmallCharMatcher);
 assertEquals("Test CharMatcher", charMatcher.description());
 }

 @Test
 public void testMatchesWithValidChar() {
 charMatcher = SmallCharMatcher.from(TEST_BIT_SET, "Test CharMatcher");
 char c = 'a';
 assertTrue(charMatcher.matches(c));
 }

 @Test
 public void testMatchesWithInvalidChar() {
 charMatcher = SmallCharMatcher.from(TEST_BIT_SET, "Test CharMatcher");
 char c = 'h';
 assertFalse(charMatcher.matches(c));
 }

 @Test
 public void testMatchesWithZero() {
 charMatcher = SmallCharMatcher.from(TEST_BIT_SET, "Test CharMatcher");
 boolean containsZero = charMatcher.containsZero();
 assertTrue(containsZero);
 char c = '\0';
 assertTrue(charMatcher.matches(c));
 }

 @Test
 public void testMatchesWithZeroAndNonZero() {
 charMatcher = SmallCharMatcher.from(TEST_BIT_SET, "Test CharMatcher");
 TEST_BIT_SET.set(5); // Set 'e' as a test character
 char[] testTable = charMatcher.table;
 char c = '\0';
 assertTrue(charMatcher.matches(c));
 assertTrue(charMatcher.matches('e'));
 assertFalse(charMatcher.matches('f'));
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidBitsetSize() {
 TEST_BIT_SET.clear();
 TEST_BIT_SET.set(0);
 TEST_BIT_SET.set(-1); // Set an invalid index
 try {
 charMatcher = SmallCharMatcher.from(TEST_BIT_SET, "Test CharMatcher");
 } catch (IllegalArgumentException e) {
 assertEquals("Size of bitset must be > 0.", e.getMessage());
 fail("Expected IllegalStateException, but got IllegalArgumentException.");
 }
 TEST_BIT_SET.clear();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDescription() {
 TEST_BIT_SET.clear();
 TEST_BIT_SET.set(0);
 charMatcher = SmallCharMatcher.from(TEST_BIT_SET, null);
 fail("Expected IllegalArgumentException, but got null.");
 }

 @Test
 public void testSetBits() {
 charMatcher = SmallCharMatcher.from(TEST_BIT_SET, "Test CharMatcher");
 BitSet table = new BitSet();
 charMatcher.setBits(table);
 for (int i = 0; i < TEST_CHAR_ARRAY.length; i++) {
 char c = TEST_CHAR_ARRAY[i];
 assertTrue(table.get(c));
 }
 assertTrue(table.get(0));
 }
}
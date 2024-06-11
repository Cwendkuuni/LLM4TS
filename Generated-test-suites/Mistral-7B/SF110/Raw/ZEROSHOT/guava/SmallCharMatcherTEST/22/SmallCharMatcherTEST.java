package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SmallCharMatcherTest {

 private SmallCharMatcher charMatcher;
 private BitSet expectedBitSet;

 @Before
 public void setUp() {
 char c1 = 'a';
 char c2 = 'e';
 char c3 = 'i';
 char c4 = 'o';
 char c5 = '\0';

 expectedBitSet = new BitSet();
 expectedBitSet.set(c1);
 expectedBitSet.set(c2);
 expectedBitSet.set(c3);
 expectedBitSet.set(c4);
 expectedBitSet.set(c5);

 charMatcher = SmallCharMatcher.from(expectedBitSet, "SmallCharMatcherTest");
 }

 @Test
 public void testFrom() {
 CharMatcher charMatcher = SmallCharMatcher.from(expectedBitSet, "SmallCharMatcherTest");
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('e'));
 assertTrue(charMatcher.matches('i'));
 assertTrue(charMatcher.matches('o'));
 assertTrue(charMatcher.matches('\0'));
 }

 @Test
 public void testCheckFilter() {
 char c1 = 'a';
 char c2 = 'b';
 char c3 = 'e';
 char c4 = '\0';

 assertTrue(SmallCharMatcher.checkFilter(c1));
 assertTrue(SmallCharMatcher.checkFilter(c3));
 assertFalse(SmallCharMatcher.checkFilter(c2));
 assertFalse(SmallCharMatcher.checkFilter(c4));
 }

 @Test
 public void testMatches() {
 char c1 = 'a';
 char c2 = 'b';
 char c3 = 'e';
 char c4 = 'i';
 char c5 = '\0';

 assertTrue(charMatcher.matches(c1));
 assertTrue(charMatcher.matches(c3));
 assertFalse(charMatcher.matches(c2));
 assertFalse(charMatcher.matches(c4));
 assertFalse(charMatcher.matches(c5));
 }

 @Test
 public void testSetBits() {
 BitSet actualBitSet = new BitSet();
 charMatcher.setBits(actualBitSet);
 byte[] expectedBytes = new byte[5];
 expectedBytes[0] = 1; // Bit 0 for '\0'
 expectedBytes[1] = 128 | (1 << 1) | (1 << 5) | (1 << 2); // Bit pattern for 'abc'
 byte[] actualBytes = actualBitSet.toByteArray();
 assertArrayEquals(expectedBytes, actualBytes);
 }
}
package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SmallCharMatcherTest {

 private SmallCharMatcher charMatcher;
 private BitSet bitSet;

 @Before
 public void setUp() {
 charValues = new char[]{'a', 'b', 'c', 'd', 'e', 'f'};
 bitSet = new BitSet();
 for (char c : charValues) {
 bitSet.set(c);
 }
 charMatcher = SmallCharMatcher.from(bitSet, "SmallCharMatcherTest");
 }

 @Test
 public void testMatches() {
 for (char c : charValues) {
 if (c == '\0') {
 assertTrue(charMatcher.matches(c));
 } else {
 assertTrue(charMatcher.matches(c));
 }
 }

 char notInCharMatcher = '\u0001';
 assertFalse(charMatcher.matches(notInCharMatcher));
 }

 @Test
 public void testSetBits() {
 BitSet expected = new BitSet();
 expected.addAll(bitSet);
 charMatcher.setBits(expected);
 for (char c : charValues) {
 if (c == '\0') {
 assertTrue(expected.get(0));
 } else {
 assertTrue(expected.get(c));
 }
 }
 }
}
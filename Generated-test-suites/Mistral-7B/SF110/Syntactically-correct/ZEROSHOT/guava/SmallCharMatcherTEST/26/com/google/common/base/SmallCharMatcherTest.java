package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcher test");
 char c;

 // Test matches method with valid characters
 for (int i = 0; i < SmallCharMatcher.MAX_SIZE; i++) {
 c = (char) i;
 assertTrue(charMatcher.matches(c));
 charMatcher.setBits(new BitSet());
 charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcher test");
 }

 // Test matches method with invalid character (character not present in the BitSet)
 assertFalse(charMatcher.matches((char) (SmallCharMatcher.MAX_SIZE + 1)));

 // Test matches method with zero
 assertTrue(charMatcher.matches((char) 0));

 // Test chooseTableSize method
 int setSize = 21;
 int tableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals(32, tableSize);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTableSize() {
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "");
 }

 @Test
 public void testSmearedHashCode() {
 int hashCode = 15;
 int smearedHashCode = SmallCharMatcher.smear(hashCode);
 assertEquals(461845907 * Integer.rotateLeft(hashCode * -862048943, 15), smearedHashCode);
 }

 @Test
 public void testCheckFilter() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcher test");
 char c = 'a';
 assertTrue(charMatcher.checkFilter((int) c));
 charMatcher.setBits(new BitSet());
 charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcher test");
 assertFalse(charMatcher.checkFilter((int) 'z'));
 }
}
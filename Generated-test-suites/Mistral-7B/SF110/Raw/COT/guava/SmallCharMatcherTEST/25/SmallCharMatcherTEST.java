package com.google.common.base.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.SmallCharMatcher;
import com.google.common.collect.BitSets;
import org.junit.Test;

public class SmallCharMatcherTest {

 private CharMatcher getCharMatcher(char[] chars) {
 return SmallCharMatcher.from(BitSets.newBitSet(chars), "Test CharMatcher");
 }

 @Test
 public void testMatchesCharacter() {
 char[] table = new char[]{'a', 'e', 'i', 'o', 'u'};
 CharMatcher charMatcher = getCharMatcher(table);
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('e'));
 assertTrue(charMatcher.matches('i'));
 assertTrue(charMatcher.matches('o'));
 assertTrue(charMatcher.matches('u'));
 assertFalse(charMatcher.matches('b'));
 }

 @Test
 public void testMatchesZero() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, true, "Test CharMatcher");
 assertTrue(charMatcher.matches((char)0));
 }

 @Test
 public void testMatchesNonExistentCharacter() {
 char[] table = new char[]{'a', 'e', 'i', 'o', 'u'};
 CharMatcher charMatcher = getCharMatcher(table);
 assertFalse(charMatcher.matches('x'));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetBitsUnsupportedOperationException() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Test CharMatcher");
 try {
 charMatcher.setBits(BitSets.newBitSet());
 } catch (UnsupportedOperationException e) {
 fail("Expected UnsupportedOperationException, but got " + e.getMessage());
 }
 }

 @Test
 public void testChooseTableSize() {
 int setSize = 16;
 int expectedTableSize = 32;
 int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals(expectedTableSize, actualTableSize);
 }

 @Test
 public void testChooseTableSizeOne() {
 int setSize = 1;
 int expectedTableSize = 2;
 int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals(expectedTableSize, actualTableSize);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChooseTableSizeNegativeSize() {
 int setSize = -1;
 try {
 SmallCharMatcher.chooseTableSize(setSize);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got " + e.getMessage());
 }
 }

 @Test
 public void testCheckFilter() {
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Test CharMatcher");
 assertFalse(charMatcher.checkFilter('a'));
 CharMatcher charMatcherWithFilter = getCharMatcher(new char[]{'a'});
 assertTrue(charMatcherWithFilter.checkFilter('a'));
 }

 @Test
 public void testSmear() {
 int hashCode = 10;
 int expected = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
 int actual = SmallCharMatcher.smear(hashCode);
 assertEquals(expected, actual);
 }

}
package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcher Test");
 char[] expectedTable = new char[]{'a', 'b', 'c'}; // Replace with expected table values
 long expectedFilter = 0b1111111111101111L; // Replace with expected filter value
 boolean containsZero = true; // Replace with expected containsZero value

 assertEquals(expectedTable.length, charMatcher.table.length);
 assertArrayEquals(expectedTable, charMatcher.table);
 assertEquals(expectedFilter, charMatcher.filter);
 assertEquals(containsZero, charMatcher.containsZero);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyCharSet() {
 try {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "EmptyCharSetTest");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testSmallCharMatcherContainsZero() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(0), "SmallCharMatcherContainsZeroTest");
 assertTrue(charMatcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcherDoesNotContainZero() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "SmallCharMatcherDoesNotContainZeroTest");
 assertFalse(charMatcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcherMatchesCharacter() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(65), "SmallCharMatcherMatchesCharacterTest");
 assertTrue(charMatcher.matches('A'));
 }

 @Test
 public void testSmallCharMatcherDoesNotMatchCharacter() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(65), "SmallCharMatcherDoesNotMatchCharacterTest");
 assertFalse(charMatcher.matches('B'));
 }

 @Test
 public void testSmallCharMatcherMatchesMultipleCharacters() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(65, 66, 67), "SmallCharMatcherMatchesMultipleCharactersTest");
 assertTrue(charMatcher.matches('A'));
 assertTrue(charMatcher.matches('B'));
 assertTrue(charMatcher.matches('C'));
 }

 @Test
 public void testSmallCharMatcherDoesNotMatchMultipleCharacters() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(65, 66), "SmallCharMatcherDoesNotMatchMultipleCharactersTest");
 assertFalse(charMatcher.matches('C'));
 }

 @Test
 public void testSmallCharMatcherMatchesCharacterWithFilter() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(0), "SmallCharMatcherMatchesCharacterWithFilterTest");
 charMatcher.filter = 0b1111111111101111L; // Set filter to include expected character
 assertTrue(charMatcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcherDoesNotMatchCharacterWithFilter() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set(65), "SmallCharMatcherDoesNotMatchCharacterWithFilterTest");
 charMatcher.filter = 0b1111111111101110L; // Set filter to exclude expected character
 assertFalse(charMatcher.matches('A'));
 }
}
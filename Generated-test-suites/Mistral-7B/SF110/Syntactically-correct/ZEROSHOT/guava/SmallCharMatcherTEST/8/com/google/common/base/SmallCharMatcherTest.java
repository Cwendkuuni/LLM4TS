package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher_validCharacters() {
 CharMatcher charMatcher = CharMatcher.from(new BitSet(), "ValidCharactersMatcher");
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;

 char[] validChars = {'a', 'b', 'c'};
 for (char c : validChars) {
 assertTrue(smallCharMatcher.matches(c));
 }
 }

 @Test
 public void testSmallCharMatcher_invalidCharacter() {
 CharMatcher charMatcher = CharMatcher.from(new BitSet(), "InvalidCharactersMatcher");
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;

 char invalidChar = 'd';
 assertFalse(smallCharMatcher.matches(invalidChar));
 }

 @Test
 public void testSmallCharMatcher_containsZero() {
 CharMatcher charMatcher = CharMatcher.from(new BitSet(true).setCardinality(1), "ContainsZeroMatcher");
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;

 assertTrue(smallCharMatcher.matches((char)0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSmallCharMatcher_setBits_throwsException() {
 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[10], 0, false, "TestMatcher");
 smallCharMatcher.setBits(new BitSet());
 }

 @Test
 public void testSmallCharMatcher_chooseTableSize() {
 int setSize = 10;
 int expectedTableSize = 20;
 int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals(expectedTableSize, actualTableSize);
 }

 @Test
 public void testSmallCharMatcher_smear() {
 int hashCode = 123;
 int expectedHashCode = 106126723;
 int actualHashCode = SmallCharMatcher.smear(hashCode);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testSmallCharMatcher_checkFilter() {
 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[10], 0, false, "TestMatcher");
 char c = 'a';
 assertTrue(smallCharMatcher.checkFilter(c));
 }

 @Test
 public void testSmallCharMatcher_from() {
 BitSet chars = new BitSet();
 chars.setCardinality(3);
 chars.set(0);
 chars.set(1);
 chars.set(2);

 CharMatcher charMatcher = SmallCharMatcher.from(chars, "ValidCharsMatcher");
 SmallCharMatcher smallCharMatcher = (SmallCharMatcher) charMatcher;

 char[] table = smallCharMatcher.table;
 long filter = smallCharMatcher.filter;
 boolean containsZero = smallCharMatcher.containsZero;

 assertEquals(3, table.length);
 assertTrue(containsZero == false);
 assertEquals((int)'a', table[smear((int)'a') & (table.length - 1)]);
 assertEquals((int)'b', table[smear((int)'b') & (table.length - 1)]);
 assertEquals((int)'c', table[smear((int)'c') & (table.length - 1)]);
 }
}
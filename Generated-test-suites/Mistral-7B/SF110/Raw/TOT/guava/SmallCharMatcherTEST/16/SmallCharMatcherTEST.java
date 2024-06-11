package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 // Test case for SmallCharMatcher constructor
 @Test
 public void testSmallCharMatcherConstruction() {
 char[] table = new char[]{'a', 'b', 'c'};
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Test CharMatcher");
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertTrue(charMatcher.matches('c'));
 assertFalse(charMatcher.matches('d'));
 assertFalse(charMatcher.matches('\0'));
 }

 // Test case for matches method with valid character
 @Test
 public void testMatchesValidCharacter() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a'), "Test CharMatcher with 'a'");
 assertTrue(charMatcher.matches('a'));
 }

 // Test case for matches method with invalid character
 @Test
 public void testMatchesInvalidCharacter() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a'), "Test CharMatcher with 'a'");
 assertFalse(charMatcher.matches('b'));
 }

 // Test case for matches method with zero character
 @Test
 public void testMatchesZeroCharacter() {
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Test CharMatcher with zero");
 assertTrue(charMatcher.matches('\0'));
 }

 // Test case for matches method with null character
 @Test(expected = NullPointerException.class)
 public void testMatchesNullCharacter() {
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "Test CharMatcher with null");
 charMatcher.matches((char)0);
 }

 // Test case for setBits method
 @Test
 public void testSetBitsMethod() {
 BitSet table = new BitSet();
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a'), "Test CharMatcher with 'a'");
 charMatcher.setBits(table);
 assertTrue(table.get('a'));
 assertFalse(table.get('b'));
 }
}
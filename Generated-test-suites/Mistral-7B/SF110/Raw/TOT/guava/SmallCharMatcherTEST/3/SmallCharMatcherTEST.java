package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.BitSet;
import java.util.Arrays;

public class SmallCharMatcherTest {

 // Test case for "matches" method with valid character
 @Test
 public void testMatchValidCharacter() {
 char[] chars = new char[]{'a', 'b', 'c'};
 CharMatcher charMatcher = CharMatcher.from(new BitSet(Arrays.asList(chars)), "CharMatcher for 'a', 'b', 'c'");
 char testChar = 'a';
 assertTrue(charMatcher.matches(testChar));
 }

 // Test case for "matches" method with invalid character
 @Test
 public void testMatchInvalidCharacter() {
 char[] chars = new char[]{'a', 'b'};
 CharMatcher charMatcher = CharMatcher.from(new BitSet(Arrays.asList(chars)), "CharMatcher for 'a', 'b'");
 char testChar = 'c';
 assertFalse(charMatcher.matches(testChar));
 }

 // Test case for "matches" method with zero character
 @Test
 public void testMatchZeroCharacter() {
 char[] chars = new char[]{'\0'};
 CharMatcher charMatcher = CharMatcher.from(new BitSet(Arrays.asList(chars)), "CharMatcher for zero character");
 char testChar = '\0';
 assertTrue(charMatcher.matches(testChar));
 }

 // Test case for "matches" method with empty character array
 @Test
 public void testMatchEmptyArray() {
 CharMatcher charMatcher = CharMatcher.from(new BitSet(), "Empty CharMatcher");
 char testChar = 'a';
 assertFalse(charMatcher.matches(testChar));
 }

 // Test case for "setBits" method with valid characters
 @Test
 public void testSetBitsValidCharacters() {
 BitSet expectedTable = new BitSet();
 expectedTable.set(new char[]{'a', 'b'});
 CharMatcher charMatcher = new SmallCharMatcher(new char[2], 0, false, "SmallCharMatcher with valid characters");
 charMatcher.setBits(expectedTable);
 Character[] actualTable = charMatcher.extractBits();
 Assert.assertArrayEquals(expectedTable.toArray(), actualTable);
 }

 // Test case for "setBits" method with zero character
 @Test
 public void testSetBitsZeroCharacter() {
 BitSet expectedTable = new BitSet();
 expectedTable.set(new char[]{'0'});
 CharMatcher charMatcher = new SmallCharMatcher(new char[2], 0, true, "SmallCharMatcher with zero character");
 charMatcher.setBits(expectedTable);
 Character[] actualTable = charMatcher.extractBits();
 Assert.assertArrayEquals(expectedTable.toArray(), actualTable);
 }

 // Test case for "setBits" method with empty character array
 @Test
 public void testSetBitsEmptyArray() {
 BitSet expectedTable = new BitSet();
 CharMatcher charMatcher = new SmallCharMatcher(new char[2], 0, false, "SmallCharMatcher with empty array");
 charMatcher.setBits(expectedTable);
 Character[] actualTable = charMatcher.extractBits();
 Assert.assertArrayEquals(expectedTable.toArray(), actualTable);
 }

 // Test case for "chooseTableSize" method with valid input
 @Test
 public void testChooseTableSizeValidInput() {
 int setSize = 5;
 int expectedTableSize = 12;
 int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals(expectedTableSize, actualTableSize);
 }

 // Test case for "chooseTableSize" method with empty set
 @Test (expected = UnsupportedOperationException.class)
 public void testChooseTableSizeEmptySet() {
 int setSize = 0;
 SmallCharMatcher.chooseTableSize(setSize);
 }

 // Test case for "smear" method
 @Test
 public void testSmearMethod() {
 int hashCode = 123;
 int expectedHashCode = 303203562;
 int actualHashCode = SmallCharMatcher.smear(hashCode);
 assertEquals(expectedHashCode, actualHashCode);
 }

 // Test case for "checkFilter" method with valid character
 @Test
 public void testCheckFilterValidCharacter() {
 char c = 'a';
 boolean expectedResult = true;
 boolean actualResult = SmallCharMatcher.checkFilter(c);
 assertEquals(expectedResult, actualResult);
 }

 // Test case for "checkFilter" method with invalid character
 @Test
 public void testCheckFilterInvalidCharacter() {
 char c = 'z';
 boolean expectedResult = false;
 boolean actualResult = SmallCharMatcher.checkFilter(c);
 assertEquals(expectedResult, actualResult);
 }
}
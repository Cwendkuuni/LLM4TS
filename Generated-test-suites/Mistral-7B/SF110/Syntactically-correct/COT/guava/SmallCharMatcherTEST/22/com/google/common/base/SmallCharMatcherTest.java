package com.example.junit.test.smallcharmatcher;

import static org.junit.Assert.*;

import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import org.junit.Test;

public class SmallCharMatcherTest {

 private char[] createTableForTest() {
 char[] table = new char[10];
 table[0] = 'a';
 table[1] = 'b';
 table[2] = '\0'; // null character
 table[3] = 'c';
 table[4] = 'd';
 table[5] = 'e';
 table[6] = 'f';
 table[7] = 'g';
 table[8] = 'h';
 table[9] = 'i';
 return table;
 }

 @Test
 public void testMatchesWithPositiveCharacter() {
 CharMatcher charMatcher = CharMatcher.from(BitSet.create(), "Test CharMatcher");
 char[] table = this.createTableForTest();
 charMatcher = new SmallCharMatcher(table, 0x1L, false, charMatcher.description());
 char expectedChar = 'c';
 char actualChar = 'c';
 assertTrue(charMatcher.matches(expectedChar));
 assertEquals(actualChar, expectedChar);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesWithNullCharacter() {
 CharMatcher charMatcher = CharMatcher.from(BitSet.create(), "Test CharMatcher");
 char[] table = this.createTableForTest();
 charMatcher = new SmallCharMatcher(table, 0x1L, false, charMatcher.description());
 char expectedChar = '\0'; // null character
 char actualChar = '\0'; // null character
 assertFalse(charMatcher.matches(expectedChar));
 assertThrows(IllegalArgumentException.class, () -> charMatcher.matches(expectedChar));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromWithEmptyBitSet() {
 CharMatcher charMatcher = null;
 try {
 charMatcher = CharMatcher.from(BitSet.create(), "Test CharMatcher");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFromWithSingleCharacterBitSet() {
 CharMatcher charMatcher = CharMatcher.from(BitSet.create((byte) 'a'), "Test CharMatcher");
 char[] table = this.createTableForTest();
 charMatcher = new SmallCharMatcher(table, 0x1L | (1L << 'a'), false, charMatcher.description());
 char expectedChar = 'a';
 char actualChar = 'a';
 assertTrue(charMatcher.matches(expectedChar));
 assertEquals(actualChar, expectedChar);
 }

 @Test
 public void testFromWithMultipleCharacterBitSet() {
 CharMatcher charMatcher = CharMatcher.from(BitSet.create((byte) 'a', (byte) 'b', (byte) 'c'), "Test CharMatcher");
 char[] table = new char[15];
 charMatcher = new SmallCharMatcher(table, 0x7L, false, charMatcher.description());
 char expectedCharA = 'a';
 char expectedCharB = 'b';
 char expectedCharC = 'c';
 char actualCharA = 'a';
 char actualCharB = 'b';
 char actualCharC = 'c';
 assertTrue(charMatcher.matches(expectedCharA));
 assertTrue(charMatcher.matches(expectedCharB));
 assertTrue(charMatcher.matches(expectedCharC));
 assertEquals(actualCharA, expectedCharA);
 assertEquals(actualCharB, expectedCharB);
 assertEquals(actualCharC, expectedCharC);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromWithBitSetContainsNullCharacter() {
 CharMatcher charMatcher = null;
 try {
 charMatcher = CharMatcher.from(BitSet.create((byte) 'a', (byte) '\0'), "Test CharMatcher");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = RuntimeException.class)
 public void testFromWithBitSetSizeGreaterThanExpected() {
 CharMatcher charMatcher = null;
 try {
 charMatcher = CharMatcher.from(BitSet.create((byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e'), "Test CharMatcher");
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // expected
 }
 }

 @Test
 public void testSetBits() {
 BitSet expectedTable = BitSet.create((byte) 'a', (byte) 'b', (byte) 'c');
 CharMatcher charMatcher = CharMatcher.from(BitSet.create(), "Test CharMatcher");
 char[] table = new char[15];
 charMatcher = new SmallCharMatcher(table, 0x7L, false, charMatcher.description());
 charMatcher.setBits(expectedTable);
 BitSet actualTable = BitSet.create();
 charMatcher.setBits(actualTable);
 assertTrue(expectedTable.equals(actualTable));
 }
}
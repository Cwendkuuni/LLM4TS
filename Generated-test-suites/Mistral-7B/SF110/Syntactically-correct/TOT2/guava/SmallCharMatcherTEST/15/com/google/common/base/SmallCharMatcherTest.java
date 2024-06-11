package com.example.junit;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.base.*;
import com.google.common.collect.*;

public class SmallCharMatcherTest {

@Test
public void testMatches() {
SmallCharMatcher matcher = SmallCharMatcher.from(BitSets.newBitSet(), "Test matcher for vowels.");
char[] table = {"a", "e", "i"};
matcher.setBits(BitSets.newBitSet(table));
assertTrue(matcher.matches('a'));
assertTrue(matcher.matches('e'));
assertTrue(matcher.matches('i'));
assertFalse(matcher.matches('b'));
}

// Test for empty table
@Test
public void testEmptyTable() {
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Empty Matcher");
assertFalse(matcher.matches('\0'));
assertFalse(matcher.matches('a'));
}

// Test for table with one character
@Test
public void testTableWithOneCharacter() {
char[] table = new char[]{'a'};
long filter = 1L;
boolean containsZero = false;
String description = "Test matcher for single character.";
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), description);
matcher.setBits(new BitSet(table));
assertTrue(matcher.matches('a'));
assertFalse(matcher.matches('b'));
}

// Test for table with repeated characters
@Test
public void testTableWithRepeatedCharacters() {
char[] table = new char[]{'a', 'a', 'a'};
long filter = 1L;
boolean containsZero = false;
String description = "Test matcher for repeated characters.";
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), description);
matcher.setBits(new BitSet(table));
assertTrue(matcher.matches('a'));
assertFalse(matcher.matches('b'));
}

// Test for table with all characters
@Test
public void testTableWithAllCharacters() {
char[] table = new char[]{(char) 0, (char) 1, (char) '\u0041', (char) '\u007F'};
long filter = 0xFFFFFFFFL;
boolean containsZero = false;
String description = "Test matcher for all characters.";
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), description);
matcher.setBits(new BitSet(table));
for (char c = (char) 0; c <= (char) 0x7F; c++) {
assertTrue(matcher.matches(c));
}
}

// Test for table with non-printable characters
@Test
public void testTableWithNonPrintableCharacters() {
char[] table = new char[]{(char) 0x00, (char) 0x01, (char) 0x7F, (char) 0x80};
long filter = 0xFFFFFFFFL;
boolean containsZero = false;
String description = "Test matcher for non-printable characters.";
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), description);
matcher.setBits(new BitSet(table));
for (char c = (char) 0x00; c <= (char) 0x7F || c == 0x80; c++) {
assertTrue(matcher.matches(c));
}
}

// Test for negative filter
@Test(expected = IllegalArgumentException.class)
public void testNegativeFilter() {
char[] table = new char[]{'a'};
long filter = -1L;
boolean containsZero = false;
String description = "Test matcher with negative filter.";
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), description);
}

// Test for filter with all bits set
@Test
public void testFilterWithAllBitsSet() {
char[] table = new char[]{'a', 'b', 'c'};
long filter = 0xFFFFFFFFL;
boolean containsZero = false;
String description = "Test matcher for filter with all bits set.";
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), description);
matcher.setBits(new BitSet(table));
for (char c = (char) 0; c <= (char) 0xFF; c++) {
assertTrue(matcher.matches(c));
}
}

// Test for filter with no set bits
@Test
public void testFilterWithNoSetBits() {
char[] table = new char[]{'a', 'b', 'c'};
long filter = 0L;
boolean containsZero = false;
String description = "Test matcher for filter with no set bits.";
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), description);
matcher.setBits(new BitSet(table));
assertFalse(matcher.matches('a'));
assertFalse(matcher.matches('b'));
assertFalse(matcher.matches('c'));
}

// Test for containsZero is true and table contains no zero
@Test
public void testContainsZeroIsTrueAndTableContainsNoZero() {
char[] table = new char[]{'a', 'b', 'c'};
long filter = 0L;
boolean containsZero = true;
String description = "Test matcher for containsZero is true and table contains no zero.";
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), description);
BitSet expectedTable = new BitSet();
matcher.setBits(expectedTable);
BitSet actualTable = new BitSet();
matcher.setBits(actualTable);
assertArrayEquals(expectedTable.toArray(), actualTable.toArray());
assertFalse(matcher.matches('\0'));
}

// Test for containsZero is false and table contains zero
@Test
public void testContainsZeroIsFalseAndTableContainsZero() {
char[] table = new char[]{'a', '\0', 'c'};
long filter = 0L;
boolean containsZero = false;
String description = "Test matcher for containsZero is false and table contains zero.";
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), description);
matcher.setBits(new BitSet(table));
assertFalse(matcher.matches('\0'));
}

// Test for description is null
@Test(expected = NullPointerException.class)
public void testDescriptionIsNull() {
char[] table = new char[]{'a'};
long filter = 1L;
boolean containsZero = false;
String description = null;
CharMatcher matcher = SmallCharMatcher.from(new BitSet(), description);
}

}
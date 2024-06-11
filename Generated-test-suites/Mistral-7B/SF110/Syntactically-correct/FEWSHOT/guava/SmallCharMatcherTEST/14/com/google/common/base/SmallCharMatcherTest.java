package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;
import org.junit.Test;

public class SmallCharMatcherTest {

 @Test
 public void testFromCharArray() {
 char[] charArray = new char[]{'a', 'b', 'c'};
 CharMatcher charMatcher = CharMatcher.from(Arrays.asList(charArray));
 CharMatcher expectedMatcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, true, "matches 'a', 'b' or 'c'");
 assertEquals(expectedMatcher, charMatcher);
 }

 @Test
 public void testMatchesCharacter() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a', '\0', 'c'}, 0L, true, "matches 'a' or '\0'");
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('\0'));
 assertFalse(charMatcher.matches('b'));

 CharMatcher charMatcher1 = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L << 'a'.codePointAt(0), false, "matches 'a'");
 assertTrue(charMatcher1.matches('a'));
 assertFalse(charMatcher1.matches('b'));
 assertFalse(charMatcher1.matches('c'));
 }

 @Test
 public void testMatchesBitSet() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create(), "matches any character");
 assertEquals(charMatcher.description(), "matches any character");

 BitSet table = BitSet.create();
 table.set('a');
 table.set('b');
 CharMatcher charMatcher1 = SmallCharMatcher.from(table, "matches 'a' and 'b'");
 assertEquals(charMatcher1.description(), "matches 'a' and 'b'");

 assertArrayEquals(new char[]{'a', 'b'}, charMatcher1.charArray());
 }

 @Test
 @VisibleForTesting
 public void testChooseTableSize() {
 int setSize = 16;
 int expectedTableSize = 32;
 int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
 assertEquals(expectedTableSize, actualTableSize);
 }

 @Test
 @VisibleForTesting
 public void testSmearedHashCode() {
 byte[] input = new byte[]{(byte)'a'};
 int expectedHashCode = 461845907 * Integer.rotateLeft(Arrays.hashCode(input) * -862048943, 15);
 int actualHashCode = SmallCharMatcher.smear(Arrays.hashCode(input));
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testSetBits() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a', '\0', 'c'}, 0L, true, "matches 'a' or '\0'");
 BitSet expectedTable = BitSet.create();
 if (charMatcher.containsZero()) {
 expectedTable.set(0);
 }
 for (char c : charMatcher.charArray()) {
 expectedTable.set(c);
 }
 charMatcher.setBits(expectedTable);
 assertEquals(charMatcher.description(), expectedTable.toString());
 }

 @Test
 public void testFrom() {
 BitSet charSet = BitSet.create();
 charSet.set('a');
 charSet.set('b');
 CharMatcher charMatcher = SmallCharMatcher.from(charSet, "matches 'a' and 'b'");
 CharMatcher expectedMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0L, false, "matches 'a' and 'b'");
 assertEquals(expectedMatcher, charMatcher);
 }

 @Test
 public void testMatchesString() {
 String input = "abc";
 CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, false, "matches 'a', 'b' or 'c'");
 boolean expectedResult = true;
 for (char c : input.toCharArray()) {
 expectedResult &= charMatcher.matches(c);
 }
 assertTrue(expectedResult);
 }

 @Test
 public void testMatchesEmptyString() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "matches any character");
 assertTrue(charMatcher.matches(''));
 }

 @Test
 public void testMatchesNull() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "matches any character");
 assertFalse(charMatcher.matches(null));
 }

 @Test
 public void testMatchesEmptyBitSet() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "matches any character");
 CharMatcher emptyBitSetMatcher = CharMatcher.from(BitSet.create(), "matches any character");
 assertEquals(charMatcher, emptyBitSetMatcher);
 }

 @Test
 public void testMatchesAll() {
 CharMatcher charMatcher = CharMatcher.forAll();
 for (char c = '\u0000'; c <= '\u007F'; c++) {
 assertTrue(charMatcher.matches(c));
 }
 }

 @Test
 public void testMatchesNone() {
 CharMatcher charMatcher = CharMatcher.none();
 for (char c = '\u0000'; c <= '\u007F'; c++) {
 assertFalse(charMatcher.matches(c));
 }
 }

 @Test
 public void testMatchesRange() {
 CharMatcher charMatcher = CharMatcher.inRange('a', 'z');
 for (char c = '\u0041'; c <= '\u005A'; c++) {
 assertTrue(charMatcher.matches(c));
 }
 for (char c = '\u0000'; c < '\u0040' || c > '\u005A'; c++) {
 assertFalse(charMatcher.matches(c));
 }
 }

 @Test
 public void testMatchesNot() {
 CharMatcher charMatcher = CharMatcher.not(CharMatcher.inRange('a', 'z'));
 for (char c = '\u0041'; c <= '\u005A'; c++) {
 assertFalse(charMatcher.matches(c));
 }
 for (char c = '\u0000'; c < '\u0040' || c > '\u005A'; c++) {
 assertTrue(charMatcher.matches(c));
 }
 }

 @Test
 public void testMatchesAnyOf() {
 CharMatcher charMatcher1 = CharMatcher.anyOf('a', 'b');
 CharMatcher charMatcher2 = CharMatcher.anyOf('a', 'c');
 CharMatcher charMatcher3 = CharMatcher.or(charMatcher1, charMatcher2);
 CharMatcher expectedMatcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, false, "matches 'a', 'b' or 'c'");
 assertEquals(expectedMatcher, charMatcher3);
 }

 @Test
 public void testMatchesNoneOf() {
 CharMatcher charMatcher1 = CharMatcher.noneOf('a', 'b');
 CharMatcher charMatcher2 = CharMatcher.noneOf('a', 'c');
 CharMatcher charMatcher3 = CharMatcher.and(charMatcher1, charMatcher2);
 CharMatcher expectedMatcher = CharMatcher.not(new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, false, "matches 'a', 'b' or 'c'"));
 assertEquals(expectedMatcher, charMatcher3);
 }

 @Test
 public void testMatchesCaseInsensitive() {
 CharMatcher charMatcher = CharMatcher.caseInsensitive(CharMatcher.inRange('a', 'z'));
 for (char c = '\u0041'; c <= '\u005A'; c++) {
 assertTrue(charMatcher.matches(c));
 }
 }

 @Test
 public void testMatchesPredefined() {
 CharMatcher charMatcher = CharMatcher.WHITESPACE;
 String input = "   Hello World!   ";
 boolean expectedResult = true;
 for (int i = 0; i < input.length(); i++) {
 expectedResult &= charMatcher.matches(input.charAt(i));
 }
 assertTrue(expectedResult);
 }

 @Test
 public void testMatchesRegexp() {
 String regex = "[a-zA-Z]";
 CharMatcher charMatcher = CharMatcher.forRegexp(regex);
 String input = "Hello World!";
 boolean expectedResult = true;
 for (int i = 0; i < input.length(); i++) {
 char c = input.charAt(i);
 expectedResult &= charMatcher.matches(c);
 }
 assertTrue(expectedResult);
 }

 @Test
 public void testMatchesRegexpWithCaptureGroup() {
 String regex = "(ab)+";
 CharMatcher charMatcher = CharMatcher.forRegexp(regex);
 String input = "aabbaabbaabbaabbaabba";
 Map<String, Integer[]> expectedCaptureGroups = Maps.newLinkedHashMap();
 expectedCaptureGroups.put("ab", new Integer[]{3, 5, 7, 9, 11, 13, 15, 17});
 Table<String, Integer, Integer> actualCaptureGroups = HashBasedTable.create();
 charMatcher.matches(input, actualCaptureGroups);
 assertEquals(expectedCaptureGroups, actualCaptureGroups);
 }
}
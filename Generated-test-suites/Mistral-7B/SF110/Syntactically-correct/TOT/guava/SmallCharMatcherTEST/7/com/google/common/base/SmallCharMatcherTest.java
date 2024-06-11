package com.google.common.base;

import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmallCharMatcherTest {

 private CharMatcher charMatcher;
 private BitSet expectedSet;

 @Before
 public void setUp() {
 char[] chars = {'a', 'b', 'c', 'd'};
 BitSet actualSet = new BitSet();
 Table<Integer, Integer, Boolean> table = HashBasedTable.create();
 for (int i = 0; i < chars.length; i++) {
 actualSet.set(chars[i]);
 expectedSet = BitSet.create(new int[]{i});
 charMatcher = CharMatcher.from(actualSet, "CharMatcher for the given characters");
 }
 }

 @Test
 public void testMatchesCharInTable() {
 char testChar = 'a';
 assertTrue(charMatcher.matches(testChar));
 }

 @Test
 public void testMatchesCharNotInTable() {
 char testChar = 'e';
 assertFalse(charMatcher.matches(testChar));
 }

 @Test
 public void testMatchesZero() {
 char testChar = '\0';
 boolean expectedResult = charMatcher.containsZero();
 assertTrue(expectedResult);
 if (charMatcher.matches(testChar)) {
 throw new AssertionError("Zero character should not match");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDescription() {
 char[] nullChars = null;
 try {
 CharMatcher.from(new BitSet(nullChars), null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptySet() {
 char[] emptyChars = new char[0];
 try {
 CharMatcher.from(new BitSet(emptyChars), "Empty CharMatcher");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateChar() {
 char[] duplicateChars = {'a', 'a', 'c', 'd'};
 try {
 CharMatcher.from(new BitSet(duplicateChars), "Duplicate CharMatcher");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetBits() {
 BitSet actualTable = new BitSet();
 charMatcher.setBits(actualTable);
 Table<Integer, Integer, Boolean> expectedTable = HashBasedTable.create();
 for (int i = 0; i < chars.length; i++) {
 expectedTable.put(i, i, true);
 }
 assertTrue(actualTable.isBitSet(chars[0]));
 for (int i = 1; i < chars.length; i++) {
 assertTrue(actualTable.get(chars[i]));
 }
 for (int i = 0; i < chars.length; i++) {
 if (chars[i] != '\0') {
 assertTrue(expectedTable.get(i));
 } else {
 assertTrue(expectedTable.get(i) == false);
 }
 }
 }
}
package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 private CharMatcher getCharMatcher(char[] characters) {
 return SmallCharMatcher.from(BitSets.bitSetOf(characteristics), "CharMatcher for given characters");
 }

 @Test
 public void testMatchesCharInTable() {
 char[] characteristics = new char[]{'a', 'b'};
 CharMatcher charMatcher = getCharMatcher(characteristics);
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 }

 @Test
 public void testMatchesCharNotInTable() {
 char[] characteristics = new char[]{'a', 'b'};
 CharMatcher charMatcher = getCharMatcher(characteristics);
 assertFalse(charMatcher.matches('c'));
 }

 @Test
 public void testMatchesZero() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, true, "Matches zero");
 assertTrue(charMatcher.matches('\0'));
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchesNegativeChar() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, false, "Matches negative char");
 charMatcher.matches((char)Integer.MIN_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromEmptyBitSet() {
 BitSet bitSet = BitSets.bitSet();
 try {
 SmallCharMatcher.from(bitSet, "Empty BitSet");
 } catch (IllegalArgumentException e) {
 assertEquals("BitSet must not be empty.", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromNullDescription() {
 BitSet bitSet = BitSets.bitSetOf('a');
 try {
 SmallCharMatcher.from(bitSet, null);
 } catch (IllegalArgumentException e) {
 assertEquals("Description must not be null.", e.getMessage());
 }
 }

 @Test
 public void testSetBits() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[0], 0L, true, "Test setBits method");
 BitSet expectedTable = BitSets.bitSetOf('a', 'b', 'c');
 charMatcher.setBits(expectedTable);
 CharMatcher actualCharMatcher = new SmallCharMatcher(charMatcher.getTable(), charMatcher.getFilter(), charMatcher.containsZero(), "Test setBits method");
 assertTrue(Arrays.equals(expectedTable.toArray(), actualCharMatcher.getTable()));
 }

 @Test
 public void testMatchesCharInTableAfterSetBits() {
 char[] characteristics = new char[]{'a', 'b'};
 CharMatcher charMatcher = getCharMatcher(characteristics);
 BitSet expectedTable = BitSets.bitSetOf('x', 'a');
 charMatcher.setBits(expectedTable);
 assertTrue(charMatcher.matches('a'));
 }

 @Test
 public void testMatchesCharNotInTableAfterSetBits() {
 char[] characteristics = new char[]{'a', 'b'};
 CharMatcher charMatcher = getCharMatcher(characteristics);
 BitSet expectedTable = BitSets.bitSetOf('x', 'a');
 charMatcher.setBits(expectedTable);
 assertFalse(charMatcher.matches('c'));
 }
}
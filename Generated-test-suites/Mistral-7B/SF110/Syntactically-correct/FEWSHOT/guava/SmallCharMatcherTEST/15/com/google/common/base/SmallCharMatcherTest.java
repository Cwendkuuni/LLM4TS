package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import java.util.BitSet;
import org.junit.Test;

public class SmallCharMatcherTest {

 @Test
 public void testFromBitSet() {
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "charMatcher");

 assertNotNull(charMatcher);
 BitSet table = new BitSet();
 charMatcher.setBits(table);

 int size = table.cardinality();
 for (int i = 0; i < size; i++) {
 char c = (char) table.nextSetBit(0);
 assertTrue(charMatcher.matches(c));
 }
 }

 @Test
 public void testMismatch() {
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "charMatcher");
 assertFalse(charMatcher.matches('a'));
 }

 @Test
 public void testMatchZero() {
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(1), "charMatcherWithZero");
 assertTrue(charMatcher.matches('\0'));
 }

 @Test
 public void testEmptyMatcher() {
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "emptyMatcher");
 assertTrue(charMatcher.matches('\0'));
 assertFalse(charMatcher.matches('a'));
 }

 @Test
 public void testMatcherWithSingleCharacter() {
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(1, 'a'), "singleCharacterMatcher");
 assertTrue(charMatcher.matches('a'));
 assertFalse(charMatcher.matches('b'));
 }

 @Test
 public void testMatcherWithMultipleCharacters() {
 BitSet chars = new BitSet();
 chars.set(1, 'a');
 chars.set(2, 'b');
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "multipleCharactersMatcher");

 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertFalse(charMatcher.matches('c'));
 }

 @Test
 public void testMatcherWithLargeSetOfCharacters() {
 BitSet chars = new BitSet();
 for (char i = 'a'; i <= 'z'; i++) {
 chars.set(i);
 }
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "allLowerCaseMatcher");

 for (char c = 'a'; c <= 'z'; c++) {
 assertTrue(charMatcher.matches(c));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeSizeCharSet() {
 BitSet chars = new BitSet();
 chars.set(-1);
 assertThrows(IllegalArgumentException.class, () -> SmallCharMatcher.from(chars, "negativeSizeCharSetMatcher"));
 }

 @Test
 public void testSmearedHashCode() {
 char c = 'a';
 int hashCode = Character.hashCode(c);
 int smearedHashCode = SmallCharMatcher.smear(hashCode);
 assertNotEquals(hashCode, smearedHashCode);
 }
}
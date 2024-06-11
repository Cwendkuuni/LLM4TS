package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.BitSet;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher_EmptyMatcher() {
 SmallCharMatcher emptyMatcher = SmallCharMatcher.from(new BitSet(), "Empty CharMatcher");
 assertTrue(emptyMatcher.matches('\0'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_NullDescription() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), null);
 }

 @Test
 public void testSmallCharMatcher_SingleCharacterMatcher() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set('a'), "Single Character Matcher");
 assertTrue(charMatcher.matches('a'));
 assertFalse(charMatcher.matches('b'));
 }

 @Test
 public void testSmallCharMatcher_MultipleCharacterMatcher() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set('a', 'b', 'c'), "Multiple Character Matcher");
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertTrue(charMatcher.matches('c'));
 assertFalse(charMatcher.matches('d'));
 }

 @Test
 public void testSmallCharMatcher_CharMatcherWithZero() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set('a', '0'), "CharMatcherWithZero");
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('0'));
 }

 @Test
 public void testSmallCharMatcher_Matches() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set('a', 'b', 'c'), "CharMatcher");
 char[] input = {'a', 'b', 'c', 'd', 'e', 'a', 'b', 'c'};
 for (char element : input) {
 if (charMatcher.matches(element)) {
 System.out.println("Matched: " + element);
 } else {
 System.out.println("Not Matched: " + element);
 }
 }
 }

 @Test
 public void testSmallCharMatcher_SetBits() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set('a', 'b', 'c'), "CharMatcher");
 BitSet expectedTable = new BitSet();
 charMatcher.setBits(expectedTable);
 BitSet actualTable = new BitSet();
 charMatcher.setBits(actualTable);
 assertEquals(expectedTable, actualTable);
 }

 @Test
 public void testSmallCharMatcher_chooseTableSize() {
 int size = 16;
 int tableSize = SmallCharMatcher.chooseTableSize(size);
 assertEquals(Math.pow(2, 5), tableSize);
 }

 @Test
 public void testSmallCharMatcher_smear() {
 int hashCode = 123;
 int smearedHashCode = SmallCharMatcher.smear(hashCode);
 int expectedSmearedHashCode = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
 assertEquals(expectedSmearedHashCode, smearedHashCode);
 }

 @Test
 public void testSmallCharMatcher_checkFilter() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet().set('a', 'b', 'c'), "CharMatcher");
 assertTrue(charMatcher.checkFilter('a'));
 assertTrue(charMatcher.checkFilter('b'));
 assertTrue(charMatcher.checkFilter('c'));
 assertFalse(charMatcher.checkFilter('d'));
 }
}
package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.BitSets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SmallCharMatcherTest {

 private SmallCharMatcher charMatcher;
 private BitSet charSet;

 @Before
 public void setUp() {
 charSet = BitSets.newBitSet();
 charSet.set('a').set('b').set('c');
 charMatcher = SmallCharMatcher.from(charSet, "SmallCharMatcher for Set {a, b, c}");
 }

 @Test
 public void testMatches() {
 // Test matching valid characters
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertTrue(charMatcher.matches('c'));

 // Test matching invalid characters
 assertFalse(charMatcher.matches('d'));

 // Test matching zero character
 if (charMatcher.containsZero()) {
 assertTrue(charMatcher.matches((char)0));
 } else {
 assertFalse(charMatcher.matches((char)0));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDescription() {
 SmallCharMatcher.from(charSet, null);
 }

 @Test
 public void testSetBits() {
 BitSet result = BitSets.newBitSet();
 charMatcher.setBits(result);
 assertTrue(result.contains('a'));
 assertTrue(result.contains('b'));
 assertTrue(result.contains('c'));
 if (charMatcher.containsZero()) {
 result.set(0);
 }
 }

 @Test
 public void testChooseTableSize() {
 int size = 10;
 int expectedTableSize = Math.pow(2, 1 + (int)Math.floor(Math.log(size) / Math.log(2)));
 int actualTableSize = SmallCharMatcher.chooseTableSize(size);
 assertEquals(expectedTableSize, actualTableSize);
 }

 @Test
 @VisibleForTesting
 public void testCheckFilter() {
 char c = 'a';
 boolean expectedResult = true;
 boolean actualResult = charMatcher.checkFilter(c);
 assertEquals(expectedResult, actualResult);

 c = 'd';
 expectedResult = false;
 actualResult = charMatcher.checkFilter(c);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 @VisibleForTesting
 public void testSmearedHashCode() {
 int hashCode = 'a'.hashCode();
 int expectedIndex = smear(hashCode) & (charMatcher.table.length - 1);
 int actualIndex = 0;
 for (int i = 0; i < charMatcher.table.length; i++) {
 if (charMatcher.table[i] == 'a') {
 actualIndex = i;
 break;
 }
 }
 assertEquals(expectedIndex, actualIndex);
 }
}
package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SmallCharMatcherTest {

 private CharMatcher createCharMatcher(char[] chars) {
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(chars.length), "SmallCharMatcher");
 return charMatcher;
 }

 @Test
 public void testEmptyCharMatcher() {
 char[] emptyCharArray = new char[0];
 CharMatcher emptyCharMatcher = createCharMatcher(emptyCharArray);
 BitSet bitSet = new BitSet(1);
 assertFalse(emptyCharMatcher.matches('a'));
 emptyCharMatcher.setBits(bitSet);
 assertFalse(bitSet.get(0));
 }

 @Test
 public void testCharMatcherWithOneCharacter() {
 char[] oneCharArray = {'a'};
 CharMatcher charMatcher = createCharMatcher(oneCharArray);
 assertTrue(charMatcher.matches('a'));
 BitSet bitSet = new BitSet(1);
 charMatcher.setBits(bitSet);
 assertTrue(bitSet.get(0));
 }

 @Test
 public void testCharMatcherWithMultipleCharacters() {
 char[] multipleCharsArray = {'a', 'b', 'c'};
 CharMatcher charMatcher = createCharMatcher(multipleCharsArray);
 assertFalse(charMatcher.matches('d'));
 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('b'));
 assertTrue(charMatcher.matches('c'));
 BitSet bitSet = new BitSet(3);
 charMatcher.setBits(bitSet);
 assertTrue(bitSet.get(0));
 assertTrue(bitSet.get(1));
 assertTrue(bitSet.get(2));
 }

 @Test
 public void testCharMatcherWithZero() {
 char[] zeroCharMatcherArray = {'0'};
 CharMatcher charMatcher = createCharMatcher(zeroCharArray);
 assertTrue(charMatcher.matches('\0'));
 BitSet bitSet = new BitSet(1);
 charMatcher.setBits(bitSet);
 assertTrue(bitSet.get(0));
 }

 @Test
 public void testCharMatcherWithLargeCharArray() {
 char[] largeCharArray = new char[SmallCharMatcher.MAX_SIZE];
 for (int i = 0; i < SmallCharMatcher.MAX_SIZE - 1; i++) {
 largeCharArray[i] = (char)i;
 }
 CharMatcher charMatcher = createCharMatcher(largeCharArray);
 for (int i = 0; i < SmallCharMatcher.MAX_SIZE; i++) {
 if (i == 0) {
 continue;
 }
 assertTrue(charMatcher.matches((char)i));
 }
 }
}
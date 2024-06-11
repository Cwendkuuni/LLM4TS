package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.BitSet;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SmallCharMatcherTest {

 private char[] createTable(int setSize) {
 char[] table = new char[SmallCharMatcher.chooseTableSize(setSize)];
 BitSet chars = new BitSet(setSize + 1);
 chars.set(0, true);
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "Test CharMatcher");
 return charMatcher.table;
 }

 @Test
 public void testEmptyMatcher() {
 SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0, false, "Empty Matcher");
 BitSet expected = new BitSet(1);
 expected.set(0, true);
 BitSet actual = new BitSet();
 matcher.setBits(actual);
 assertEquals(expected, actual);
 assertFalse(matcher.matches('\u0001'));
 }

 @Test
 public void testSingleCharacterMatcher() {
 SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 0, false, "Single Character Matcher");
 CharSequence expected = Character.toString('a');
 String actual = matcher.description();
 assertEquals(expected.toString(), actual);
 assertTrue(matcher.matches('a'));
 assertFalse(matcher.matches('b'));
 }

 @Test
 public void testMatcherWithMultipleCharacters() {
 SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0, false, "Matcher with Multiple Characters");
 CharSequence expected = "Matcher with Multiple Characters[a, b]";
 String actual = matcher.description();
 assertEquals(expected.toString(), actual);
 assertTrue(matcher.matches('a'));
 assertTrue(matcher.matches('b'));
 assertFalse(matcher.matches('c'));
 }

 @Test
 public void testMatcherWithZero() {
 SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 0, true, "Matcher with Zero");
 CharSequence expected = "Matcher with Zero[\\0]";
 String actual = matcher.description();
 assertEquals(expected.toString(), actual);
 assertTrue(matcher.matches('\u0000'));
 assertFalse(matcher.matches('a'));
 }

 @Test
 public void testMatcherWithFilteredChar() {
 SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 1L << 2, false, "Matcher with Filtered Char");
 CharSequence expected = "Matcher with Filtered Char[a, b]";
 String actual = matcher.description();
 assertEquals(expected.toString(), actual);
 assertFalse(matcher.matches('c'));
 assertFalse(matcher.matches('a'));
 assertTrue(matcher.matches('b'));
 }

 @Test
 public void testMatcherWithSameCharMultipleTimes() {
 SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 0, false, "Matcher with Same Char Multiple Times");
 CharSequence expected = "Matcher with Same Char Multiple Times[a]";
 String actual = matcher.description();
 assertEquals(expected.toString(), actual);
 assertTrue(matcher.matches('a'));
 assertTrue(matcher.matches('a'));
 }

 @Test
 public void testMatcherWithDifferentCharsInTable() {
 SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0, false, "Matcher with Different Chars in Table");
 CharSequence expected = "Matcher with Different Chars in Table[a, b, c]";
 String actual = matcher.description();
 assertEquals(expected.toString(), actual);
 assertTrue(matcher.matches('a'));
 assertTrue(matcher.matches('b'));
 assertTrue(matcher.matches('c'));
 }
}
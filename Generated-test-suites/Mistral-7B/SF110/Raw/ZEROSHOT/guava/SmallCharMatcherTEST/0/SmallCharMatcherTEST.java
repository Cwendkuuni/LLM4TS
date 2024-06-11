package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher_empty() {
 CharMatcher emptyCharMatcher = CharMatcher.from(BitSet.create(), "Empty CharMatcher");
 SmallCharMatcher emptyMatcher = (SmallCharMatcher) emptyCharMatcher;
 assertTrue(emptyMatcher.matches('\0'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcher_nullDescription() {
 CharMatcher charMatcher = CharMatcher.from(BitSet.create(), (String) null);
 assertNull(charMatcher);
 }

 @Test
 public void testSmallCharMatcher_singleCharacter() {
 BitSet charSet = BitSet.create();
 charSet.set('a');
 CharMatcher charMatcher = CharMatcher.from(charSet, "SingleCharacterCharMatcher");
 SmallCharMatcher matcher = (SmallCharMatcher) charMatcher;
 assertArrayEquals(new char[]{'a'}, matcher.table);
 assertTrue(matcher.matches('a'));
 assertFalse(matcher.matches('b'));
 }

 @Test
 public void testSmallCharMatcher_multipleCharacters() {
 BitSet charSet = BitSet.create();
 charSet.set('a').set('b').set('c');
 CharMatcher charMatcher = CharMatcher.from(charSet, "MultipleCharactersCharMatcher");
 SmallCharMatcher matcher = (SmallCharMatcher) charMatcher;
 int expectedTableSize = Math.max(3, (int) Math.ceil(Math.log(charSet.cardinality()) / Math.log(2.0d)) << 1);
 int actualTableSize = matcher.table.length;
 assertEquals(expectedTableSize, actualTableSize);
 assertArrayEquals(new char[]{'a', 'b', 'c'}, matcher.table);
 assertTrue(matcher.matches('a'));
 assertTrue(matcher.matches('b'));
 assertTrue(matcher.matches('c'));
 assertFalse(matcher.matches('d'));
 }

 @Test
 public void testSmallCharMatcher_containsZero() {
 BitSet charSet = BitSet.create();
 charSet.set(0);
 CharMatcher charMatcher = CharMatcher.from(charSet, "ContainsZeroCharMatcher");
 SmallCharMatcher matcher = (SmallCharMatcher) charMatcher;
 assertTrue(matcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcher_noZero() {
 BitSet charSet = BitSet.create();
 CharMatcher charMatcher = CharMatcher.from(charSet, "NoZeroCharMatcher");
 SmallCharMatcher matcher = (SmallCharMatcher) charMatcher;
 assertFalse(matcher.matches('\0'));
 }
}
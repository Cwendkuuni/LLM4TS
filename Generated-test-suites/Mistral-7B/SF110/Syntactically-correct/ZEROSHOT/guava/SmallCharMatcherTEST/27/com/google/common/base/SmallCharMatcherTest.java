package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher_empty() {
 char[] table = new char[0];
 long filter = 0L;
 boolean containsZero = false;
 CharMatcher charMatcher = SmallCharMatcher.from(Collections.emptyBitSet(), "empty CharMatcher");

 SmallCharMatcher expectedSmallCharMatcher = new SmallCharMatcher(table, filter, containsZero, "empty CharMatcher");

 assertEquals(expectedSmallCharMatcher, charMatcher);
 }

 @Test
 public void testSmallCharMatcher_singleCharacter() {
 BitSet chars = new BitSet();
 chars.set(58); // ':'
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "CharMatcher for ':'");

 SmallCharMatcher expectedSmallCharMatcher = new SmallCharMatcher(new char[]{':'}, 0x1L << 58, true, "CharMatcher for ':'");

 assertEquals(expectedSmallCharMatcher, charMatcher);
 }

 @Test
 public void testSmallCharMatcher_multipleCharacters() {
 BitSet chars = new BitSet();
 chars.set(58); // ':'
 chars.set(101); // 'e'
 chars.set(110); // 'n'
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "CharMatcher for ':', 'e', 'n'");

 SmallCharMatcher expectedSmallCharMatcher = new SmallCharMatcher(new char[]{' ', ':', 'e', 'n'}, 0xB5B5B5B5L, false, "CharMatcher for ':', 'e', 'n'");

 assertEquals(expectedSmallCharMatcher, charMatcher);
 }

 @Test
 public void testSmallCharMatcher_matchesCharacter() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, 0x1L, false, "CharMatcher for 'a'");

 assertTrue(charMatcher.matches('a'));
 }

 @Test
 public void testSmallCharMatcher_doesNotMatchCharacter() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a'}, 0x1L, false, "CharMatcher for 'a'");

 assertFalse(charMatcher.matches('b'));
 }

 @Test
 public void testSmallCharMatcher_containsZero() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a', '\0'}, 0x1L << 97, true, "CharMatcher for 'a', '\0'");

 assertTrue(charMatcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcher_matchesMultipleCharacters() {
 CharMatcher charMatcher = new SmallCharMatcher(new char[]{'a', 'e', 'n'}, 0x1L << 97 | 0x1L << 101 | 0x1L << 110, false, "CharMatcher for 'a', 'e', 'n'");

 assertTrue(charMatcher.matches('a'));
 assertTrue(charMatcher.matches('e'));
 assertTrue(charMatcher.matches('n'));
 }

 @Test
 public void testSmallCharMatcher_matchesSet() {
 Set<Character> expectedSet = new HashSet<>(Collections.singleton(':'));
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(expectedSet.stream().mapToInt(Character::hashCode).toArray()), "CharMatcher for ':'");

 Set<Character> actualSet = new HashSet<>();
 charMatcher.setBits(new BitSet());
 for (char c : charMatcher.getDescription().split(" for ")[1].split(" ")) {
 actualSet.add(c);
 }

 assertEquals(expectedSet, actualSet);
 }
}
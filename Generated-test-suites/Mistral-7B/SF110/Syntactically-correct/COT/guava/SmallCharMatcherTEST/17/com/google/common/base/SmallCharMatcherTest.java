package com.example.junit.tests;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.collect.BitSet;

public class SmallCharMatcherTest {

 private char[] createTable(int size) {
 char[] table = new char[SmallCharMatcher.chooseTableSize(size)];
 return table;
 }

 @Test
 public void testSmallCharMatcher_Empty() {
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Empty CharMatcher");
 assertTrue(charMatcher.matches(' '));
 assertFalse(charMatcher.matches('\u0000'));
 assertFalse(charMatcher.matches('\u0001'));
 }

 @Test
 public void testSmallCharMatcher_SingleCharacter() {
 char c = 'a';
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add(c), "Single Character CharMatcher");
 assertTrue(charMatcher.matches(c));
 assertFalse(charMatcher.matches('b'));
 }

 @Test
 public void testSmallCharMatcher_MultipleCharacters() {
 char[] chars = {'a', 'b', 'c'};
 BitSet bitSet = new BitSet();
 for (char c : chars) {
 bitSet.set(c);
 }
 CharMatcher charMatcher = SmallCharMatcher.from(bitSet, "Multiple Characters CharMatcher");
 for (char c : chars) {
 assertTrue(charMatcher.matches(c));
 }
 assertFalse(charMatcher.matches('d'));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSmallCharMatcher_FromNullBitSet() {
 CharMatcher charMatcher = assertThrows(UnsupportedOperationException.class, () -> SmallCharMatcher.from(null, "Null BitSet CharMatcher"));
 assertEquals("CharMatcher cannot be created from null BitSet", charMatcher.getMessage());
 }

 @Test
 public void testSmallCharMatcher_ContainsZero() {
 CharMatcher charMatcher = SmallCharMatcher.from(new BitSet().add('a').add('\u0000'), "ContainsZero CharMatcher");
 assertTrue(charMatcher.matches('\u0000'));
 }

 @Test
 public void testSmallCharMatcher_NoFilter() {
 char[] table = createTable(5);
 CharMatcher charMatcher = new SmallCharMatcher(table, 0, false, "No Filter CharMatcher");
 for (char c = '\u0000'; c <= '\u0004'; c++) {
 assertTrue(charMatcher.matches(c));
 }
 }

 @Test
 public void testSmallCharMatcher_Filter() {
 char[] table = createTable(5);
 CharMatcher charMatcher = new SmallCharMatcher(table, 0b1111_0101_0110_1101_1111_1111_1111_1111_1111_0111, false, "Filter CharMatcher");
 for (char c = '\u0000'; c <= '\u0009'; c++) {
 if ((c & 0x0F) == 0x04) {
 assertTrue(charMatcher.matches(c));
 } else {
 assertFalse(charMatcher.matches(c));
 }
 }
 }

 @Test
 public void testSmallCharMatcher_Smear() {
 int hashCode = 123;
 int expected = SmallCharMatcher.smear(hashCode);
 int actual = SmallCharMatcher.smear(hashCode);
 assertEquals(expected, actual);
 }

 @Test
 public void testSmallCharMatcher_CheckFilter() {
 char c = 'a';
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[1], 0, false, "CheckFilter CharMatcher");
 assertFalse(charMatcher.checkFilter(c));
 }

 @Test
 public void testSmallCharMatcher_ChooseTableSize() {
 int sizes[] = {0, 1, 10, 12, 1024};
 int expectedSizes[] = {2, 2, 25, 51, 1024};
 for (int i = 0; i < sizes.length; i++) {
 int actualSize = SmallCharMatcher.chooseTableSize(sizes[i]);
 assertEquals(expectedSizes[i], actualSize);
 }
 }

}
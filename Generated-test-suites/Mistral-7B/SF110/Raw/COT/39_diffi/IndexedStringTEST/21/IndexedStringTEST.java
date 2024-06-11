package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private char[] data1 = {'h', 'e', 'l', 'l', 'o'};
 private char[] data2 = {'h', 'e', 'l', 'l', 'o', 'w'};
 private char[] data3 = {'h', 'e', 'l', 'l', 'o', 'x'};
 private char[] data4 = {'h', 'e', 'l', 'l', 'o', 'w', 'x'};
 private char[] data5 = {};
 private char[] data6 = {'\u00E4', '\u00E4', '\u00E4'};
 private char[] data7 = {'\u0131', '\u0131', '\u0131'};
 private IndexedString is1;
 private IndexedString is2;

 @Test
 public void testIndexedStringConstructor() {
 is1 = new IndexedString("hello");
 Assert.assertArrayEquals(is1.values, new char[]{'h', 'e', 'l', 'l', 'o'});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexedStringConstructorNullString() {
 try {
 new IndexedString(null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("String cannot be null", e.getMessage());
 }
 }

 @Test
 public void testCountCharacter() {
 char c = 'l';
 int expected = 3;
 int result = new IndexedString("hello").count(c);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSingleCharacter() {
 char c = 'l';
 int expected = 2;
 int result = new IndexedString("hello").indexOf(new char[] {c});
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfEmptyArray() {
 int expected = -1;
 int result = new IndexedString("hello").indexOf(new char[] {});
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSingleCharacterNotPresent() {
 char c = 'x';
 int expected = -1;
 int result = new IndexedString("hello").indexOf(new char[] {c});
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfMultipleCharacters() {
 char[] target = {'l', 'l'};
 int expected = 2;
 int result = new IndexedString("hello").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfMultipleCharactersNotPresentFirstCharacter() {
 char[] target = {'w', 'l'};
 int expected = -1;
 int result = new IndexedString("hello").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfMultipleCharactersNotPresentSecondCharacter() {
 char[] target = {'l', 'w'};
 int expected = -1;
 int result = new IndexedString("hello").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstring() {
 char[] target = {'l', 'l', 'o'};
 int expected = 2;
 int result = new IndexedString("hello").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstringNotPresent() {
 char[] target = {'w', 'o'};
 int expected = -1;
 int result = new IndexedString("hello").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstringWithDifferentCasing() {
 char[] target = {'L', 'o', 'l'};
 int expected = 2;
 int result = new IndexedString("hello").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstringWithDifferentLength() {
 char[] target = {'h', 'e', 'l'};
 int expected = 0;
 int result = new IndexedString("hello").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstringWithLeadingAndTrailingSpaces() {
 char[] target = {' ', 'h', 'e', 'l', 'l', 'o', ' '};
 int expected = 2;
 int result = new IndexedString(" hello ").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstringWithLeadingSpaces() {
 char[] target = {' ', 'h', 'e', 'l', 'l', 'o'};
 int expected = 1;
 int result = new IndexedString(" hello ").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstringWithTrailingSpaces() {
 char[] target = {'h', 'e', 'l', 'l', 'o', ' '};
 int expected = 5;
 int result = new IndexedString("hello ").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstringWithMiddleSpaces() {
 char[] target = {'h', ' ', 'e', 'l', 'l', 'o', ' '};
 int expected = 3;
 int result = new IndexedString("hello ").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstringWithUmlauts() {
 char[] target = {'\u00E4', '\u00E4', '\u00E4'};
 int expected = 2;
 int result = new IndexedString("M\u00F6ller").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstringWithNonASCIICharacters() {
 char[] target = {'\u0131', '\u0131', '\u0131'};
 int expected = 1;
 int result = new IndexedString("Şöleyman").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIndexOfSubstringWithMixedCasing() {
 char[] target = {'l', 'o', 'L', 'o', 'l'};
 int expected = 2;
 int result = new IndexedString("HeLLo").indexOf(target);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfNullTarget() {
 try {
 new IndexedString("hello").indexOf(null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("Target array cannot be null", e.getMessage());
 }
 }
}
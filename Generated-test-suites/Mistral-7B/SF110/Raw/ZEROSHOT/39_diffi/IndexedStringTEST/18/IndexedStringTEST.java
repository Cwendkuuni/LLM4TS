package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 @Test
 public void testIndexedStringConstruction() {
 char[] values = new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
 IndexedString indexedString = new IndexedString(new String(values));
 Assert.assertArrayEquals(values, indexedString.values);

 int[][] expectedIndex = new int[256][];
 for (char c = '\u0000'; c <= '\u0060'; c++) {
 if (c == ' ') {
 expectedIndex[c] = new int[]{-1};
 } else {
 expectedIndex[c] = new int[]{0, 0, 1, 2, 2, 3, 4, 5, 5, 6, 7, 8, 10, 11};
 }
 }

 Assert.assertArrayEquals(expectedIndex, indexedString.index);
 }

 @Test
 public void testEmptyString() {
 IndexedString indexedString = new IndexedString("");
 Assert.assertArrayEquals(new char[0], indexedString.values);
 Assert.assertArrayEquals(new int[256][0], indexedString.index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullString() {
 try {
 new IndexedString(null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testInvalidCharInIndex() {
 IndexedString indexedString = new IndexedString("hello world!");
 try {
 indexedString.indexOf(new char[]{'z'});
 fail("Should throw ArrayIndexOutOfBoundsException");
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIndexOfSingleCharacter() {
 IndexedString indexedString = new IndexedString("hello world!");
 Assert.assertEquals(0, indexedString.indexOf('h'));
 Assert.assertEquals(6, indexedString.indexOf(' '));
 Assert.assertEquals(11, indexedString.indexOf('!'));
 Assert.assertEquals(-1, indexedString.indexOf('z'));
 }

 @Test
 public void testIndexOfSubstring() {
 IndexedString indexedString = new IndexedString("hello world!");
 Assert.assertEquals(0, indexedString.indexOf(new char[]{'h', 'e'}));
 Assert.assertEquals(5, indexedString.indexOf(new char[]{' ', 'w', 'o', 'r', 'l', 'd'}));
 Assert.assertEquals(-1, indexedString.indexOf(new char[]{'z', 'z'}));
 }

 @Test
 public void testIndexOfSubstringWithLeadingSpace() {
 IndexedString indexedString = new IndexedString(" hello world!");
 Assert.assertEquals(1, indexedString.indexOf(new char[]{' ', 'h', 'e'}));
 }

 @Test
 public void testIndexOfSubstringWithTrailingSpace() {
 IndexedString indexedString = new IndexedString("hello world!");
 Assert.assertEquals(6, indexedString.indexOf(new char[]{'w', 'o', 'r', 'l', 'd', ' ', '!'}));
 }

 @Test
 public void testIndexOfSubstringWithMiddleSpace() {
 IndexedString indexedString = new IndexedString("hello world!");
 Assert.assertEquals(5, indexedString.indexOf(new char[]{' ', 'w', 'o', 'r', 'l', 'd', ' ', '!'}));
 }

 @Test
 public void testIndexOfSubstringWithLengthGreaterThanString() {
 IndexedString indexedString = new IndexedString("hello world!");
 try {
 indexedString.indexOf(new char[10]);
 fail("Should throw ArrayIndexOutOfBoundsException");
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }
}
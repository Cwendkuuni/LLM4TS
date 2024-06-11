package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import org.junit.Assert.*;

public class IndexedStringTest {

 private char[] getTestStringCharArray(String testString) {
 char[] result = new char[testString.length()];
 testString.getChars(0, testString.length(), result, 0);
 return result;
 }

 @Test
 public void testIndexedStringCreation() {
 char[] testValues = getTestStringCharArray("HelloWorld");
 IndexedString indexedString = new IndexedString("HelloWorld");
 assertArrayEquals(testValues, indexedString.values);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullStringArgument() {
 try {
 new IndexedString(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCharCount() {
 IndexedString indexedString = new IndexedString("HelloWorld");
 int hCount = indexedString.count('H');
 int oCount = indexedString.count('o');
 int lCount = indexedString.count('l');
 int dCount = indexedString.count('d');
 int wCount = indexedString.count('W');
 int eCount = indexedString.count('e');

 assertEquals(1, hCount);
 assertEquals(1, oCount);
 assertEquals(3, lCount);
 assertEquals(1, dCount);
 assertEquals(1, wCount);
 assertEquals(1, eCount);
 }

 @Test
 public void testIndexCreation() {
 IndexedString indexedString = new IndexedString("HelloWorld");
 for (char c = '\u0000'; c < '\u0100'; c++) {
 if (Character.isAlphabetic(c) || Character.isDigit(c)) {
 int[] indices = indexedString.index[c];
 for (int i = 0; i < indices.length; i++) {
 int index = indices[i];
 int expectedIndex = Character.isAlphabetic(c) ? c - 'a' + i : i;
 assertEquals(expectedIndex, index);
 }
 }
 }
 }

 @Test
 public void testIndexOf() {
 IndexedString indexedString = new IndexedString("HelloWorld");
 char[] target1 = {'H', 'e', 'l', 'l', 'o'};
 int result1 = indexedString.indexOf(target1);
 assertEquals(0, result1);

 char[] target2 = {'l', 'o'};
 int result2 = indexedString.indexOf(target2);
 assertEquals(4, result2);

 char[] target3 = {'W'};
 int result3 = indexedString.indexOf(target3);
 assertEquals(-1, result3);
 }

 @Test
 public void testIndexOfWithSameCharacter() {
 IndexedString indexedString = new IndexedString("HelloWorld");
 char target = 'l';
 int result = indexedString.indexOf(new char[] { target });
 int expected = (int) ('l' - 'a') + 1;
 assertEquals(expected, result);
 }

 @Test
 public void testIndexOfWithSubstring() {
 IndexedString indexedString = new IndexedString("HelloWorld");
 char[] target = {'l', 'o'};
 int result = indexedString.indexOf(target);
 int expected = 4;
 assertEquals(expected, result);
 }

 @Test
 public void testIndexOfWithEmptyString() {
 IndexedString indexedString = new IndexedString("HelloWorld");
 int result = indexedString.indexOf(new char[0][]);
 assertEquals(-1, result);
 }
}
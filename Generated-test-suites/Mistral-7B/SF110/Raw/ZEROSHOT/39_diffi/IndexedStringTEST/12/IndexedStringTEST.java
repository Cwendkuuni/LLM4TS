package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 @Test
 public void testIndexedStringConstruction() {
 char[] values = new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
 IndexedString indexedString = new IndexedString(new String(values));
 Assert.assertArrayEquals(values, indexedString.values);
 int expectedLength = 12;
 Assert.assertEquals(expectedLength, indexedString.index.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexedStringNullConstruction() {
 IndexedString indexedString = new IndexedString(null);
 // the assertion is not necessary as an exception is expected
 }

 @Test
 public void testIndexedStringEmptyConstruction() {
 IndexedString indexedString = new IndexedString("");
 char[] expectedValues = new char[0];
 Assert.assertArrayEquals(expectedValues, indexedString.values);
 int expectedIndexLength = 0;
 Assert.assertEquals(expectedIndexLength, indexedString.index.length);
 }

 @Test
 public void testGetCharAt() {
 IndexedString indexedString = new IndexedString("hello world!");
 for (int i = 0; i < indexedString.values.length; i++) {
 Assert.assertEquals(indexedString.values[i], Character.forDigit(i, 10));
 }
 }

 @Test
 public void testCount() {
 IndexedString indexedString = new IndexedString("hello world!");
 char testChar = 'l';
 int expectedCount = 3;
 int actualCount = indexedString.count(testChar);
 Assert.assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testIndexOfChar() {
 IndexedString indexedString = new IndexedString("hello world!");
 char testChar = 'l';
 int expectedIndex = 5;
 int actualIndex = indexedString.indexOf(new char[]{testChar});
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfCharArrayWithEmptyString() {
 IndexedString indexedString = new IndexedString("hello world!");
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(new char[0]);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfCharArrayWithWrongChar() {
 IndexedString indexedString = new IndexedString("hello world!");
 char[] testArray = new char[]{'a', 'b', 'c'};
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(testArray);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfCharArrayWithCorrectString() {
 IndexedString indexedString = new IndexedString("hello world!");
 char[] testArray = new char[]{'l', 'o', 'l'};
 int expectedIndex = 5;
 int actualIndex = indexedString.indexOf(testArray);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfCharArrayWithSubString() {
 IndexedString indexedString = new IndexedString("hello world!");
 char[] testArray = new char[]{'h', 'e', 'l'};
 int expectedIndex = 0;
 int actualIndex = indexedString.indexOf(testArray);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfCharArrayWithSubStringAtEnd() {
 IndexedString indexedString = new IndexedString("hello world!");
 char[] testArray = new char[]{'d', '!'};
 int expectedIndex = 10;
 int actualIndex = indexedString.indexOf(testArray);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfCharArrayWithSubStringInMiddle() {
 IndexedString indexedString = new IndexedString("hello world!");
 char[] testArray = new char[]{'o', 'r', 'l'};
 int expectedIndex = 6;
 int actualIndex = indexedString.indexOf(testArray);
 Assert.assertEquals(expectedIndex, actualIndex);
 }
}
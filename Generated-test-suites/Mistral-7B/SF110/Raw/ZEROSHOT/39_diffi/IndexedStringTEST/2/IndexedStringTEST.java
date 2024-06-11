package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private char[] expectedValues;
 private int[][] expectedIndex;

 @Test
 public void testIndexedStringConstructor() {
 char[] values = new char[]{"a", "b", "a", "c", "a"}.clone();
 IndexedString indexedString = new IndexedString(new String(values));

 this.expectedValues = values.clone();
 this.expectedIndex = createExpectedIndex(values);

 Assert.assertArrayEquals(this.expectedValues, indexedString.values);
 Assert.assertArrayEquals(this.expectedIndex, indexedString.index);
 }

 @Test
 public void testIndexOfChar() {
 IndexedString indexedString = new IndexedString("aabacaa");
 char target = 'a';
 int actualResult = indexedString.indexOf(new char[]{target});
 int expectedResult = 0;

 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIndexOfCharNotPresent() {
 IndexedString indexedString = new IndexedString("aabacaa");
 char target = 'd';
 int actualResult = indexedString.indexOf(new char[]{target});
 int expectedResult = -1;

 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIndexOfSubstring() {
 IndexedString indexedString = new IndexedString("aabaabacaa");
 char[] target = new char[]{'a', 'a'};
 int actualResult = indexedString.indexOf(target);
 int expectedResult = 0;

 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIndexOfSubstringNotPresent() {
 IndexedString indexedString = new IndexedString("aabaabacaa");
 char[] target = new char[]{'b', 'b'};
 int actualResult = indexedString.indexOf(target);
 int expectedResult = -1;

 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIndexOfSubstringPartialMatch() {
 IndexedString indexedString = new IndexedString("aabaabacaa");
 char[] target = new char[]{'a', 'b'};
 int actualResult = indexedString.indexOf(target);
 int expectedResult = 1;

 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIndexOfSubstringPartialMatchEnd() {
 IndexedString indexedString = new IndexedString("aabaabacaa");
 char[] target = new char[]{'c', 'a'};
 int actualResult = indexedString.indexOf(target);
 int expectedResult = 5;

 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testIndexOfSubstringEmpty() {
 IndexedString indexedString = new IndexedString("aabaabacaa");
 char[] target = new char[0][];
 int actualResult = indexedString.indexOf(target);
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testIndexOfSubstringNull() {
 IndexedString indexedString = new IndexedString("aabaabacaa");
 char[] target = null;
 int actualResult = indexedString.indexOf(target);
 }

 private int[][] createExpectedIndex(char[] values) {
 int[][] expectedIndex = new int[256][];
 for (char c = '\0'; c < '\u0100'; ++c) {
 expectedIndex[c] = new int[this.count(values, c)];
 int idx = 0;
 for (int i = 0; i < values.length; ++i) {
 if (values[i] == c) {
 expectedIndex[c][idx++] = i;
 }
 }
 }
 return expectedIndex;
 }

 @Test
 public void testCountChar() {
 IndexedString indexedString = new IndexedString("aabaabacaa");
 char target = 'a';
 int actualResult = indexedString.count(target);
 int expectedResult = 6;

 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCountCharNotPresent() {
 IndexedString indexedString = new IndexedString("aabaabacaa");
 char target = 'b';
 int actualResult = indexedString.count(target);
 int expectedResult = 2;

 Assert.assertEquals(expectedResult, actualResult);
 }

 private int count(char[] values, char target) {
 int result = 0;
 for (int i = 0; i < values.length; ++i) {
 if (values[i] == target) {
 ++result;
 }
 }
 return result;
 }
}
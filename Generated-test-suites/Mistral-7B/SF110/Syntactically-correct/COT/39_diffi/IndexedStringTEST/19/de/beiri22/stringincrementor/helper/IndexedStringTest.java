package de.beiri22.stringincrementor.helper.test;

import org.junit.Assert;
import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 private char[] testValues = new char[]{'a', 'b', 'a', 'b', 'a'};
 private IndexedString testIndexedString;

 @Test
 public void testIndexedStringConstructor() {
 testIndexedString = new IndexedString(new String(testValues));
 }

 @Test
 public void testCountChar() {
 char testChar = 'a';
 int actualCount = testIndexedString.count(testChar);
 int expectedCount = 3;
 Assert.assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testIndexOfChar() {
 char testChar = 'a';
 int actualIndex = testIndexedString.indexOf(new char[]{testChar});
 int expectedIndex = 0;
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfCharArrayWithMatchingSequence() {
 char[] testSequence = new char[]{'a', 'b', 'a'};
 int actualIndex = testIndexedString.indexOf(testSequence);
 int expectedIndex = 0;
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfCharArrayWithEmptySequence() {
 int actualIndex = testIndexedString.indexOf(new char[0]);
 int expectedIndex = -1;
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfCharArrayWithMismatchingSequence() {
 char[] testSequence = new char[]{'a', 'c'};
 int actualIndex = testIndexedString.indexOf(testSequence);
 int expectedIndex = -1;
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfCharArrayWithNullSequence() {
 testIndexedString.indexOf(null);
 }

 @Test
 public void testIndexOfCharArrayWithSequenceLongerThanString() {
 char[] testSequence = new char[testValues.length + 1];
 int actualIndex = testIndexedString.indexOf(testSequence);
 int expectedIndex = -1;
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfCharArrayWithSequenceLongerThanStringButNotAllMatches() {
 char[] testSequence = new char[testValues.length + 1];
 for (int i = 0; i < testValues.length; i++) {
 testSequence[i] = testValues[i];
 }
 testSequence[testValues.length] = 'x';
 int actualIndex = testIndexedString.indexOf(testSequence);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfCharArrayWithSequenceShorterThanMatchingSequence() {
 char[] testSequence = new char[2];
 testSequence[0] = testValues[0];
 testSequence[1] = testValues[1];
 int actualIndex = testIndexedString.indexOf(testSequence);
 int expectedIndex = 0;
 Assert.assertEquals(expectedIndex, actualIndex);
 // test for array bounds exception when last character index is used
 testSequence[2] = testValues[testValues.length];
 }
}
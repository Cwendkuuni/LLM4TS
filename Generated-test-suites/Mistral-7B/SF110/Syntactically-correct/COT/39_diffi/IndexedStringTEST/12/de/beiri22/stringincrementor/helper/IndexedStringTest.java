package de.beiri22.stringincrementor.helper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

 private IndexedString indexedString;

 @Before
 public void setUp() {
 char[] values = new char[]{"a", "b", "a", "c", "a", "b", "a"};
 indexedString = new IndexedString(new String(values));
 }

 @After
 public void tearDown() {
 indexedString = null;
 }

 @Test
 public void testIndexedStringConstructor() {
 char[] values = indexedString.values;
 int expectedSize = 7;
 Assert.assertArrayEquals(values, new char[expectedSize]);
 }

 @Test
 public void testCountCharacter() {
 char testChar = 'a';
 int actualCount = indexedString.count(testChar);
 int expectedCount = 4;
 Assert.assertEquals(expectedCount, actualCount);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCountCharacterWithInvalidCharacter() {
 char testChar = (char) 0;
 indexedString.count(testChar);
 }

 @Test
 public void testIndexOf() {
 char[] target = {'a', 'b'};
 int expectedIndex = 2;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfWithEmptyTargetArray() {
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(new char[0]);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfWithNullTargetArray() {
 char[] target = null;
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfWithTargetNotPresentInIndexedString() {
 char[] target = {'c', 'd'};
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfWithTargetPartiallyPresent() {
 char[] target = {'a', 'b', 'a'};
 int expectedIndex = 2;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfWithTargetPartiallyPresentAtEnd() {
 char[] target = {'a', 'b', 'a', 'b', 'a'};
 int expectedIndex = 6;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfWithTargetPartiallyPresentAtBeginning() {
 char[] target = {'b', 'a'};
 int expectedIndex = 1;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }
}
package de.beiri22.stringincrementor.helper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IndexedStringTest {

 private IndexedString indexedString;

 @Before
 public void setUp() {
 char[] values = {'h', 'e', 'l', 'l', 'o', 'w', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
 indexedString = new IndexedString(new String(values));
 }

 @Test
 public void testIndexedStringConstructor() {
 char[] values = indexedString.values;
 int expectedLength = 13;
 int actualLength = values.length;
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testCountMethod() {
 char c = 'o';
 int expectedCount = 3;
 int actualCount = indexedString.count(c);
 assertEquals(expectedCount, actualCount);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCountMethodWithInvalidCharacter() {
 char c = (char) 0;
 indexedString.count(c);
 }

 @Test
 public void testIndexMethod() {
 char[] target = {'h', 'e'};
 int expectedIndex = 0;
 int actualIndex = indexedString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexMethodWithEmptyTargetArray() {
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(new char[0]);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexMethodWithSingleCharacter() {
 char target = 'l';
 int expectedIndex = 5;
 int actualIndex = indexedString.indexOf(new char[]{target});
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexMethodWithInvalidCharacter() {
 char target = (char) 0;
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(new char[]{target});
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexMethodWithSubstring() {
 char[] target = {'h', 'e', 'l'};
 int expectedIndex = 0;
 int actualIndex = indexedString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexMethodWithSubstringStartingAfterEnd() {
 char[] target = {'o', 'l', 'd'};
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexMethodWithSubstringNotPresent() {
 char[] target = {'a', 'b', 'c'};
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }
}
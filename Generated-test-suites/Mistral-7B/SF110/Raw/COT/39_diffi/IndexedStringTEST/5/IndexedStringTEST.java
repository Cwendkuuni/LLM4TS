package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

 private IndexedString s;

 @Test
 public void testIndexedStringConstructor() {
 char[] expectedValues = new char[]{'h', 'e', 'l', 'l', 'o'};
 s = new IndexedString("hello");
 assertArrayEquals(expectedValues, s.values);
 }

 @Test
 public void testCountMethod() {
 s = new IndexedString("hello");
 char testChar = 'l';
 int expectedCount = 2;
 int actualCount = s.count(testChar);
 assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testInvalidCharInCountMethod() {
 s = new IndexedString("hello");
 char testChar = 'x';
 int expectedCount = 0;
 int actualCount = s.count(testChar);
 assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testIndexOfMethod() {
 s = new IndexedString("hello");
 char[] target = new char[]{'h', 'e', 'l'};
 int expectedIndex = 0;
 int actualIndex = s.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testEmptyTargetArrayInIndexOfMethod() {
 s = new IndexedString("hello");
 char[] target = new char[0][];
 int expectedIndex = -1;
 int actualIndex = s.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testTargetArrayLongerThanSourceStringInIndexOfMethod() {
 s = new IndexedString("hello");
 char[] target = new char[6];
 int expectedIndex = -1;
 int actualIndex = s.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testTargetArrayContainsCharOutsideSourceStringInIndexOfMethod() {
 s = new IndexedString("hello");
 char[] target = new char[]{'i', 'n', 'v', 'a', 'l', 'i', 'd'};
 s.indexOf(target);
 }

 @Test
 public void testTargetArrayContainsRepeatedCharsInIndexOfMethod() {
 s = new IndexedString("hello");
 char[] target = new char[]{'h', 'e', 'l', 'l'};
 int expectedIndex = 2;
 int actualIndex = s.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testTargetArrayContainsDifferentCharsInIndexOfMethod() {
 s = new IndexedString("hello");
 char[] target = new char[]{'h', 'e', 'l', 'o', 'k'};
 int expectedIndex = -1;
 int actualIndex = s.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTargetArrayInIndexOfMethod() {
 s = new IndexedString("hello");
 char[] target = null;
 s.indexOf(target);
 }
}
package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexedStringTest {

 private char[] values;
 private int[][] index;

 @Test
 public void testIndexedStringConstructor() {
 char[] testValues = new char[] {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
 IndexedString indexedString = new IndexedString(new String(testValues));
 assertEquals(testValues.length, indexedString.values.length);
 }

 @Test
 public void testCountMethod() {
 IndexedString indexedString = new IndexedString("hello world");
 assertEquals(1, indexedString.count('h'));
 assertEquals(5, indexedString.count('l'));
 assertEquals(3, indexedString.count('o'));
 assertEquals(12, indexedString.count(' '));
 assertEquals(11, indexedString.count('w'));
 assertEquals(1, indexedString.count('r'));
 assertEquals(1, indexedString.count('d'));
 }

 @Test
 public void testIndexOfMethod() {
 char[] target1 = new char[] {'h', 'e', 'l'};
 char[] target2 = new char[] {'l', 'o', 'l'};
 IndexedString indexedString = new IndexedString("hello world hello");
 int result1 = indexedString.indexOf(target1);
 int result2 = indexedString.indexOf(target2);
 assertEquals(0, result1);
 assertEquals(7, result2);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfMethodWithEmptyTargetArray() {
 IndexedString indexedString = new IndexedString("hello world");
 indexedString.indexOf(new char[0]);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfMethodWithTargetBeyondStringBounds() {
 IndexedString indexedString = new IndexedString("hello world");
 char[] target = new char[5];
 System.arraycopy(indexedString.values, 10, target, 0, 5);
 int result = indexedString.indexOf(target);
 assertEquals(-1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfMethodWithTargetOutsideCharacterSet() {
 IndexedString indexedString = new IndexedString("hello world");
 char[] target = new char[] {'@', 'e', 'l', 'l', 'o'};
 indexedString.indexOf(target);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfMethodWithTargetNull() {
 IndexedString indexedString = new IndexedString("hello world");
 indexedString.indexOf(null);
 }
}
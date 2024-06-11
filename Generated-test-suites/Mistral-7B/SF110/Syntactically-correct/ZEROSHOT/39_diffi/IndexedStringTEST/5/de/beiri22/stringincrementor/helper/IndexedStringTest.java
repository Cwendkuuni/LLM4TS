package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

 @Test
 public void testCountChar() {
 char[] values = new char[]{'a', 'b', 'a', 'c', 'a', 'a'};
 IndexedString indexedString = new IndexedString(new String(values));
 assertEquals(3, indexedString.count('a'));
 assertEquals(2, indexedString.count('b'));
 assertEquals(1, indexedString.count('c'));
 }

 @Test
 public void testConstructor() {
 char[] values = new char[]{'a', 'b', 'a', 'c', 'a', 'a'};
 IndexedString indexedString = new IndexedString(new String(values));
 char[] expectedValues = new char[]{'a', 'b', 'a', 'c', 'a', 'a'};
 assertArrayEquals(expectedValues, indexedString.values);

 int[][] expectedIndex = new int[][] {
 {'a', new int[] {0, 2, 3, 4, 5}},
 {'b', new int[] {1}},
 {'c', new int[] {}}
 };
 assertArrayEquals(expectedIndex, indexedString.index);
 }

 @Test
 public void testIndexOfEmptyArray() {
 IndexedString indexedString = new IndexedString("abc");
 char[] emptyArray = new char[0][];
 int result = indexedString.indexOf(emptyArray);
 assertEquals(-1, result);
 }

 @Test
 public void testIndexOfSingleCharacter() {
 IndexedString indexedString = new IndexedString("abc");
 char[] singleCharacter = new char[]{'a'};
 int result = indexedString.indexOf(singleCharacter);
 int expectedIndex = 0;
 assertEquals(expectedIndex, result);
 }

 @Test
 public void testIndexOfCharNotPresent() {
 IndexedString indexedString = new IndexedString("abc");
 char[] charToFind = new char[]{'d'};
 int result = indexedString.indexOf(charToFind);
 assertEquals(-1, result);
 }

 @Test
 public void testIndexOfSubArrayPresent() {
 IndexedString indexedString = new IndexedString("ababab");
 char[] subArrayToFind = new char[]{'a', 'b'};
 int result = indexedString.indexOf(subArrayToFind);
 int expectedIndex = 0;
 assertEquals(expectedIndex, result);
 }

 @Test
 public void testIndexOfSubArrayNotPresent() {
 IndexedString indexedString = new IndexedString("ababab");
 char[] subArrayToFind = new char[]{'c', 'd'};
 int result = indexedString.indexOf(subArrayToFind);
 assertEquals(-1, result);
 }

 @Test
 public void testIndexOfSubArrayPresentButNotContiguous() {
 IndexedString indexedString = new IndexedString("abacaba");
 char[] subArrayToFind = new char[]{'a', 'c'};
 int result = indexedString.indexOf(subArrayToFind);
 int expectedIndex = 3;
 assertEquals(expectedIndex, result);
 }
}
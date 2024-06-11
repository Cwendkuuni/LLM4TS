package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 @Test
 public void testIndexedStringConstructor() {
 char[] expectedValues = new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
 int[][] expectedIndex = new int[][]{
 // ' ' character index
 {-1},
 // 'h' character index
 {0, 1, 5, 9},
 // 'e' character index
 {1, 2, 3, 5, 6},
 // 'l' character index
 {3, 4, 5, 9, 10},
 // 'o' character index
 {5, 6, 10},
 // 'w' character index
 {8},
 // 'r' character index
 {9},
 // 'd' character index
 {10}
 };

 IndexedString indexedString = new IndexedString("hello world");
 Assert.assertArrayEquals(expectedValues, indexedString.values);
 for (int i = 0; i < expectedIndex.length; i++) {
 for (int j = 0; j < expectedIndex[i].length; j++) {
 Assert.assertEquals(expectedIndex[i][j], indexedString.index[i][j]);
 }
 }
 }

 @Test
 public void testCountMethod() {
 IndexedString indexedString = new IndexedString("hello world");
 Assert.assertEquals(1, indexedString.count('h'));
 Assert.assertEquals(5, indexedString.count('l'));
 Assert.assertEquals(3, indexedString.count('o'));
 Assert.assertEquals(1, indexedString.count('w'));
 Assert.assertEquals(1, indexedString.count('r'));
 Assert.assertEquals(1, indexedString.count('d'));
 Assert.assertEquals(11, indexedString.count(' '));
 }

 @Test
 public void testIndexOfMethodWithSingleChar() {
 IndexedString indexedString = new IndexedString("hello world");
 Assert.assertEquals(0, indexedString.indexOf('h'));
 Assert.assertEquals(5, indexedString.indexOf('l'));
 Assert.assertEquals(6, indexedString.indexOf('o'));
 Assert.assertEquals(-1, indexedString.indexOf('x'));
 }

 @Test
 public void testIndexOfMethodWithMultiChar() {
 char[] target = new char[]{'l', 'o'};
 IndexedString indexedString = new IndexedString("hello world");
 Assert.assertEquals(3, indexedString.indexOf(target));
 Assert.assertEquals(4, indexedString.indexOf(target) + 1);
 }
}
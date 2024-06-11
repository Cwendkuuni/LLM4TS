package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 @Test
 public void testIndexedStringConstructor() {
 char[] values = new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
 int[][] index = new int[][]{{-1, -1, 0, 2, 6, 8, 9, 10, 11}, {-1, -1, -1, -1, -1, -1, 4, 5, 7}};
 IndexedString is = new IndexedString("hello world");

 Assert.assertArrayEquals(values, is.values);
 Assert.assertArrayEquals(index, is.index);
 }

 @Test
 public void testCount() {
 IndexedString is = new IndexedString("hello world");

 Assert.assertEquals(5, is.count('h'));
 Assert.assertEquals(1, is.count(' '));
 Assert.assertEquals(1, is.count('w'));
 Assert.assertEquals(3, is.count('o'));
 Assert.assertEquals(1, is.count('r'));
 Assert.assertEquals(1, is.count('l'));
 Assert.assertEquals(12, is.count('d'));
 }

 @Test
 public void testIndexOfInvalidCharArray() {
 IndexedString is = new IndexedString("hello world");
 char[] invalidTarget = new char[0];

 Assert.assertEquals(-1, is.indexOf(invalidTarget));
 }

 @Test
 public void testIndexOfValidCharArray() {
 IndexedString is = new IndexedString("hello world");
 char[] validTarget1 = new char[]{'h', 'e', 'l', 'l', 'o'};
 char[] validTarget2 = new char[]{'w', 'o', 'r', 'l', 'd'};

 Assert.assertEquals(0, is.indexOf(validTarget1));
 Assert.assertEquals(6, is.indexOf(validTarget2));
 }

 @Test
 public void testIndexOfSubstring() {
 IndexedString is = new IndexedString("hello world");

 Assert.assertEquals(6, is.indexOf("world".toCharArray()));
 Assert.assertEquals(-1, is.indexOf("foo".toCharArray()));
 }
}
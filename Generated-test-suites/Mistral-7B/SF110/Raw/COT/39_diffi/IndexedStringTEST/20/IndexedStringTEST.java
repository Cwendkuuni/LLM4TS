package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private char[] testValues;
 private IndexedString indexedString;

 @Test
 public void testIndexedStringConstructor() {
 char[] values = new char[]{"a", "b", "a", "c", "a"}.clone();
 indexedString = new IndexedString(new String(values));
 Assert.assertArrayEquals(values, indexedString.values);
 }

 @Test
 public void testCountCharacter() {
 indexedString = new IndexedString("aaabbbccc");
 Assert.assertEquals(3, indexedString.count('a'));
 Assert.assertEquals(3, indexedString.count('b'));
 Assert.assertEquals(3, indexedString.count('c'));
 Assert.assertEquals(0, indexedString.count('d'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexedStringConstructorWithNullString() {
 Assert.assertThrows(IllegalArgumentException.class, () -> new IndexedString(null));
 }

 @Test
 public void testIndexOfCharacter() {
 indexedString = new IndexedString("aaabbbccc");
 Assert.assertEquals(0, indexedString.indexOf('a'));
 Assert.assertEquals(3, indexedString.indexOf('b'));
 Assert.assertEquals(6, indexedString.indexOf('c'));
 Assert.assertEquals(-1, indexedString.indexOf('d'));
 }

 @Test
 public void testIndexOfArray() {
 char[] target = new char[]{'a', 'b'};
 indexedString = new IndexedString("aaabbbccc");
 Assert.assertEquals(0, indexedString.indexOf(target));
 Assert.assertEquals(3, indexedString.indexOf(new char[]{'b', 'b'}));
 Assert.assertEquals(-1, indexedString.indexOf(new char[]{'d', 'e'}));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfArrayWithNullArray() {
 char[] target = null;
 Assert.assertThrows(IllegalArgumentException.class, () -> indexedString.indexOf(target));
 }

 @Test
 public void testIndexOfArrayWithEmptyArray() {
 indexedString = new IndexedString("aaabbbccc");
 Assert.assertEquals(0, indexedString.indexOf(new char[]{}));
 }

 @Test
 public void testIndexOfArrayWithOneElementArray() {
 indexedString = new IndexedString("aaabbbccc");
 Assert.assertEquals(0, indexedString.indexOf(new char[]{'a'}));
 }

 @Test
 public void testIndexOfArrayWithLongerArrayThanString() {
 indexedString = new IndexedString("aaabbbccc");
 char[] target = new char[10];
 Assert.assertEquals(-1, indexedString.indexOf(target));
 }

 @Test
 public void testIndexOfArrayWithArrayContainingDifferentCharacters() {
 indexedString = new IndexedString("aaabbbccc");
 char[] target = new char[]{'a', 'b', 'c', 'd'};
 Assert.assertEquals(-1, indexedString.indexOf(target));
 }
}
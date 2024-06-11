package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private char[] createTestData() {
 return new char[]{"abababaaabaaaaaaababbaabababaaa".toCharArray()};
 }

 @Test
 public void testIndexedStringConstruction() {
 char[] values = createTestData();
 IndexedString indexedString = new IndexedString(new String(values));
 Assert.assertArrayEquals(values, indexedString.values);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testIndexedStringConstructionWithInvalidEncoding() {
 char[] values = createTestData();
 IndexedString indexedString = new IndexedString(new String(values, "invalid_encoding"));
 }

 @Test
 public void testCountCharacter() {
 IndexedString indexedString = new IndexedString(new String(createTestData()));
 Assert.assertEquals(10, indexedString.count('a'));
 Assert.assertEquals(12, indexedString.count('b'));
 Assert.assertEquals(0, indexedString.count('z'));
 }

 @Test
 public void testIndexOfSingleCharacter() {
 IndexedString indexedString = new IndexedString(new String(createTestData()));
 Assert.assertEquals(0, indexedString.indexOf('a'));
 Assert.assertEquals(11, indexedString.indexOf('b'));
 Assert.assertEquals(-1, indexedString.indexOf('z'));
 }

 @Test
 public void testIndexOfMultipleCharacters() {
 char[] target = {'a', 'b', 'a'};
 IndexedString indexedString = new IndexedString(new String(createTestData()));
 Assert.assertEquals(1, indexedString.indexOf(target));
 Assert.assertEquals(0, indexedString.indexOf(target, 1));
 Assert.assertEquals(2, indexedString.indexOf(target, 2));
 }

 @Test
 public void testIndexOfEmptyCharacterArray() {
 IndexedString indexedString = new IndexedString(new String(createTestData()));
 Assert.assertEquals(-1, indexedString.indexOf(new char[0]));
 }

 @Test
 public void testIndexOfNonExistentCharacterArray() {
 IndexedString indexedString = new IndexedString(new String(createTestData()));
 char[] target = {'z', 'z'};
 Assert.assertEquals(-1, indexedString.indexOf(target));
 }
}
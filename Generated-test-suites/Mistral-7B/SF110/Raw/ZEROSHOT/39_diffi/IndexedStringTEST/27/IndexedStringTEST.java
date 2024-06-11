package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 @Test
 public void testIndexedStringCreation() {
 char[] values = new char[]{"a", "b", "a", "b", "a", "c", "a"}.clone();
 IndexedString indexedString = new IndexedString(new String(values));
 Assert.assertArrayEquals(values, indexedString.values);
 int expectedIndexLength = 2;
 for (char c = 'a'; c <= 'c'; c++) {
 int[] index = indexedString.index[c];
 Assert.assertNotNull(index);
 Assert.assertEquals(expectedIndexLength++, index.length);
 }
 }

 @Test
 public void testIndexOfNullTarget() {
 IndexedString indexedString = new IndexedString("ababac");
 Assert.assertNull(indexedString.indexOf(null));
 }

 @Test
 public void testIndexOfEmptyTarget() {
 IndexedString indexedString = new IndexedString("ababac");
 Assert.assertEquals(-1, indexedString.indexOf(new char[0]));
 }

 @Test
 public void testIndexOfSingleCharTarget() {
 IndexedString indexedString = new IndexedString("ababac");
 Assert.assertEquals(1, indexedString.indexOf(new char[]{'a'}));
 Assert.assertEquals(4, indexedString.indexOf(new char[]{'c'}));
 }

 @Test
 public void testIndexOfMultipleCharTarget() {
 IndexedString indexedString = new IndexedString("ababac");
 Assert.assertEquals(3, indexedString.indexOf(new char[]{'a', 'b'}));
 Assert.assertEquals(6, indexedString.indexOf(new char[]{'a', 'c'}));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfTargetBeyondStringBounds() {
 IndexedString indexedString = new IndexedString("ababac");
 indexedString.indexOf(new char[]{'e', 'd'});
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfTargetBeyondFirstCharIndex() {
 IndexedString indexedString = new IndexedString("ababac");
 indexedString.indexOf(new char[]{'a', 'e'});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfTargetBeyondCharacterTableBounds() {
 IndexedString indexedString = new IndexedString("ababac");
 indexedString.indexOf(new char[]{'z', 'z'});
 }
}
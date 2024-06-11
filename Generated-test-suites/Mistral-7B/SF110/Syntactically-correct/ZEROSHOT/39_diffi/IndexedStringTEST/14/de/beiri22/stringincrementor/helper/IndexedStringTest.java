package de.beiri22.stringincrementor.helper.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 @Test
 public void testIndexedStringCreation() {
 char[] values = new char[]{'a', 'b', 'a', 'c', 'b', 'a'};
 IndexedString indexedString = new IndexedString(new String(values));
 char[] expectedValues = values;
 int[][] expectedIndex = new int[256][];
 for (char c = '\0'; c <= 'z'; c++) {
 if (c == 'a') {
 expectedIndex[c] = new int[]{ 0, 2, 4 };
 } else if (c == 'b') {
 expectedIndex[c] = new int[]{ 1, 3, 5 };
 } else {
 expectedIndex[c] = new int[]{};
 }
 }
 assertEquals(expectedValues, indexedString.values);
 assertEquals(expectedIndex, indexedString.index);
 }

 @Test
 public void testIndexedStringEmpty() {
 IndexedString indexedString = new IndexedString(new String());
 assertEquals(new char[0], indexedString.values);
 assertEquals(new int[256][0], indexedString.index);
 }

 @Test
 public void testIndexedStringNull() {
 assertNull(new IndexedString(null));
 }

 @Test
 public void testIndexOfEmptyString() {
 IndexedString indexedString = new IndexedString("abc");
 assertFalse(indexedString.indexOf(new char[0][0]) >= 0);
 }

 @Test
 public void testIndexOfSingleCharacter() {
 IndexedString indexedString = new IndexedString("abcabc");
 char target = 'a';
 int result = indexedString.indexOf(new char[]{target});
 assertEquals(0, result);
 }

 @Test
 public void testIndexOfMultipleCharacters() {
 IndexedString indexedString = new IndexedString("abcabc");
 char[] target = new char[]{'a', 'b'};
 int result = indexedString.indexOf(target);
 assertEquals(2, result);
 }

 @Test
 public void testIndexOfNonExistentCharacters() {
 IndexedString indexedString = new IndexedString("abcabc");
 char[] target = new char[]{'d', 'e'};
 int result = indexedString.indexOf(target);
 assertEquals(-1, result);
 }

 @Test
 public void testIndexOfSubString() {
 IndexedString indexedString = new IndexedString("abcabc");
 char[] target = new char[]{'a', 'b', 'c'};
 int result = indexedString.indexOf(target);
 assertEquals(0, result);
 }

 @Test
 public void testIndexOfSubStringWithShift() {
 IndexedString indexedString = new IndexedString("abcabc");
 char[] target = new char[]{'b', 'c', 'a'};
 int result = indexedString.indexOf(target);
 assertEquals(2, result);
 }

 @Test
 public void testIndexOfSubStringWithPartialMatch() {
 IndexedString indexedString = new IndexedString("abcabc");
 char[] target = new char[]{'a', 'b'};
 int result = indexedString.indexOf(target);
 assertEquals(1, result);
 }

 @Test
 public void testIndexOfSubStringWithPrefix() {
 IndexedString indexedString = new IndexedString("abcabc");
 char[] target = new char[]{'a', 'b', 'd'};
 int result = indexedString.indexOf(target);
 assertEquals(-1, result);
 }

 @Test
 public void testIndexOfSubStringWithSuffix() {
 IndexedString indexedString = new IndexedString("abcabc");
 char[] target = new char[]{'c', 'a', 'b'};
 int result = indexedString.indexOf(target);
 assertEquals(-1, result);
 }

 @Test
 public void testIndexOfSubStringWithMiddleCharacterDifference() {
 IndexedString indexedString = new IndexedString("abcabc");
 char[] target = new char[]{'b', 'd', 'a'};
 int result = indexedString.indexOf(target);
 assertEquals(-1, result);
 }

 @Test
 public void testIndexOfSubStringWithDifferentLength() {
 IndexedString indexedString = new IndexedString("abcabc");
 char[] target = new char[]{'a', 'b'};
 int result = indexedString.indexOf(new char[]{'a'});
 assertEquals(0, result);
 int result2 = indexedString.indexOf(new char[]{'a', 'b'});
 assertEquals(1, result2);
 }
}
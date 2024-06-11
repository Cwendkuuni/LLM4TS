package de.beiri22.stringincrementor.helper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class IndexedStringTest {

 private IndexedString indexedString;
 private char[] target1, target2, target3;

 @Before
 public void setUp() {
 char[] values = "abccabbbccbaabccc".toCharArray();
 indexedString = new IndexedString(new String(values));

 target1 = new char[]{'a', 'b'};
 target2 = new char[]{'c', 'c'};
 target3 = new char[]{'b', 'a', 'b', 'c'};
 }

 @Test
 public void testIndexedStringConstructor() {
 char[] expectedValues = "abccabbbccbaabccc".toCharArray();
 assertEquals(expectedValues.length, indexedString.values.length);
 }

 @Test
 public void testCountCharacter() {
 assertEquals(5, indexedString.count('a'));
 assertEquals(6, indexedString.count('b'));
 assertEquals(3, indexedString.count('c'));
 }

 @Test
 public void testIndexOfEmptyArray() {
 assertNull(indexedString.indexOf(new char[0]));
 }

 @Test
 public void testIndexOfSingleCharacter() {
 assertEquals(0, indexedString.indexOf(new char[]{'a'}));
 assertEquals(1, indexedString.indexOf(new char[]{'c'}));
 }

 @Test
 public void testIndexOfCharacterNotPresent() {
 assertNull(indexedString.indexOf(new char[]{'d'}));
 }

 @Test
 public void testIndexOfMultipleCharacters() {
 int expected = 5;
 int index = indexedString.indexOf(target1);
 assertEquals(expected, index);
 }

 @Test
 public void testIndexOfMultipleOccurrences() {
 int expected = 11;
 int index = indexedString.indexOf(target2);
 assertEquals(expected, index);
 }

 @Test
 public void testIndexOfSubsequence() {
 int expected = 14;
 int index = indexedString.indexOf(target3);
 assertEquals(expected, index);
 }

 @Test
 public void testIndexOfSubsequenceNotPresent() {
 assertNull(indexedString.indexOf(new char[]{'e', 'f'}));
 }
}
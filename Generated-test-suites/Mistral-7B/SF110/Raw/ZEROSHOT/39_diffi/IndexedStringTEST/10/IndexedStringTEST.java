package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IndexedStringTest {

 @Test
 public void testIndexedStringCreation() {
 char[] values = new char[]{'h', 'e', 'l', 'l', 'o'};
 IndexedString indexedString = new IndexedString(new String(values));
 assertEquals(values.length, indexedString.values.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexedStringCreationWithNullString() {
 new IndexedString(null);
 }

 @Test
 public void testEmptyStringCreation() {
 IndexedString indexedString = new IndexedString("");
 assertEquals(0, indexedString.values.length);
 }

 @Test
 public void testCountCharacter() {
 IndexedString indexedString = new IndexedString("hello");
 assertEquals(1, indexedString.count('h'));
 assertEquals(1, indexedString.count('e'));
 assertEquals(1, indexedString.count('l'));
 assertEquals(1, indexedString.count('o'));
 }

 @Test
 public void testIndexCreation() {
 IndexedString indexedString = new IndexedString("hello");
 char[] hIndices = indexedString.index['h'];
 assertEquals(1, hIndices.length);
 assertEquals(0, hIndices[0]);
 char[] lIndices = indexedString.index['l'];
 assertEquals(1, lIndices.length);
 assertEquals(2, lIndices[0]);
 }

 @Test
 public void testIndexOfSingleCharacter() {
 IndexedString indexedString = new IndexedString("hello");
 assertEquals(0, indexedString.indexOf('h'));
 assertEquals(2, indexedString.indexOf('l'));
 }

 @Test
 public void testIndexOfEmptyArray() {
 IndexedString indexedString = new IndexedString("hello");
 assertNull(indexedString.indexOf(new char[0]));
 }

 @Test
 public void testIndexOfSingleCharacterNotPresent() {
 IndexedString indexedString = new IndexedString("hello");
 assertNull(indexedString.indexOf('i'));
 }

 @Test
 public void testIndexOfMultipleCharacters() {
 IndexedString indexedString = new IndexedString("hello");
 char[] hello = new char[]{'h', 'e', 'l', 'l', 'o'};
 assertEquals(0, indexedString.indexOf(hello));
 }

 @Test
 public void testIndexOfSubstringPresent() {
 IndexedString indexedString = new IndexedString("hello world");
 assertEquals(6, indexedString.indexOf("ll".toCharArray()));
 }

 @Test
 public void testIndexOfSubstringNotPresent() {
 IndexedString indexedString = new IndexedString("hello world");
 assertNull(indexedString.indexOf("mm".toCharArray()));
 }
}
package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

 private char[] testValues = {'a', 'b', 'a', 'b', 'a'};
 private IndexedString indexedString;

 @Test
 public void testIndexedStringConstructor() {
 indexedString = new IndexedString(new String(testValues));
 assertEquals(testValues.length, indexedString.values.length);
 }

 @Test
 public void testCountCharacter() {
 indexedString = new IndexedString(new String(testValues));
 assertEquals(3, indexedString.count('a'));
 }

 @Test
 public void testIndexOfCharacter() {
 indexedString = new IndexedString(new String(testValues));
 assertEquals(0, indexedString.indexOf('a'));
 }

 @Test
 public void testIndexOfCharacterNotFound() {
 indexedString = new IndexedString(new String(testValues));
 assertEquals(-1, indexedString.indexOf('c'));
 }

 @Test
 public void testIndexOfCharArray() {
 char[] target = {'a', 'b'};
 indexedString = new IndexedString(new String(testValues));
 assertEquals(0, indexedString.indexOf(target));
 }

 @Test
 public void testIndexOfCharArrayNotFound() {
 char[] target = {'c', 'd'};
 indexedString = new IndexedString(new String(testValues));
 assertEquals(-1, indexedString.indexOf(target));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexedStringConstructorNullString() {
 indexedString = new IndexedString(null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfCharArrayOutOfBounds() {
 indexedString = new IndexedString(new String(testValues));
 char[] target = new char[testValues.length + 1];
 target[testValues.length] = 'a';
 assertEquals(-1, indexedString.indexOf(target));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfCharArrayOutOfBounds2() {
 indexedString = new IndexedString(new String(testValues));
 char[] target = new char[testValues.length - 1];
 assertEquals(-1, indexedString.indexOf(target));
 }

 @Test(expected = NullPointerException.class)
 public void testIndexOfCharArrayNullArray() {
 indexedString = new IndexedString(new String(testValues));
 char[] target = null;
 assertEquals(-1, indexedString.indexOf(target));
 }

 @Test(expected = NullPointerException.class)
 public void testIndexOfCharArrayNullCharArrayElement() {
 indexedString = new IndexedString(new String(testValues));
 char[] target = {null};
 assertEquals(-1, indexedString.indexOf(target));
 }

}
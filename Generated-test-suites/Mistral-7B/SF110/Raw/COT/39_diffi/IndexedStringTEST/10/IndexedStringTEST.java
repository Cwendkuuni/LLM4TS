package de.beiri22.stringincrementor.helper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IndexedStringTest {

 private IndexedString indexedString;

 @Before
 public void init() {
 indexedString = new IndexedString("teststring12345");
 }

 @Test
 public void testIndexedStringConstructor() {
 char[] expectedValues = new char[]{'t', 'e', 'st', 'r', 'i', 'n', 'g', '1', '2', '3', '4', '5'};
 assertArrayEquals(expectedValues, indexedString.values);
 }

 @Test
 public void testIndexOfChar() {
 char[] target = new char[]{'t', 'e'};
 int expectedIndex = 0;
 int result = indexedString.indexOf(target);
 assertEquals(expectedIndex, result);
 }

 @Test
 public void testIndexOfCharArrayWithMatchingSequence() {
 char[] target = new char[]{'t', 'e', 's', 't', 'r', 'i', 'n', 'g'};
 int expectedIndex = 5;
 int result = indexedString.indexOf(target);
 assertEquals(expectedIndex, result);
 }

 @Test
 public void testIndexOfCharArrayWithMismatchingSequence() {
 char[] target = new char[]{'t', 'e', 's', 't', 'r', 'i', 'n', 'g', '6'};
 int expectedIndex = -1;
 int result = indexedString.indexOf(target);
 assertEquals(expectedIndex, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfCharArrayWithNullArray() {
 indexedString.indexOf(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfCharArrayWithTargetLongerThanString() {
 char[] target = new char[indexedString.values.length + 1];
 indexedString.indexOf(target);
 }

 @Test
 public void testIndexOfCharArrayWithEmptyTarget() {
 char[] target = new char[0];
 int expectedIndex = -1;
 int result = indexedString.indexOf(target);
 assertEquals(expectedIndex, result);
 }

 @Test
 public void testIndexOfCharArrayWithSingleCharacterTarget() {
 char[] target = new char[]{'t'};
 int expectedIndex = 0;
 int result = indexedString.indexOf(target);
 assertEquals(expectedIndex, result);
 }
}
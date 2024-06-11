package de.beiri22.stringincrementor.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IndexedStringTest {

 private IndexedString indexedString;

 @Before
 public void setUp() {
 char[] values = new char[]{'a', 'b', 'a', 'b', 'a'};
 indexedString = new IndexedString(new String(values));
 }

 @After
 public void tearDown() {
 indexedString = null;
 values = null;
 }

 @Test
 public void testIndexedStringConstructor() {
 char[] values = indexedString.values;
 int[][] index = indexedString.index;

 assertEquals("Values should match input string.", new String(values), "ababa");
 assertNull("Index for null character should be null.", index[0]);

 int countA = indexedString.count('a');
 assertEquals("Count of 'a' should be 3.", countA, 3);
 }

 @Test
 public void testIndexOfSingleCharacter() {
 char[] target = new char[]{'a'};
 int result = indexedString.indexOf(target);

 assertEquals("Index of single character 'a' should be 0.", result, 0);
 }

 @Test
 public void testIndexOfSingleCharacterNotPresent() {
 char[] target = new char[]{'c'};
 int result = indexedString.indexOf(target);

 assertNull("Index of non-present character 'c' should be null.", result);
 }

 @Test
 public void testIndexOfMultipleCharacters() {
 char[] target = new char[]{'a', 'a'};
 int result = indexedString.indexOf(target);

 assertEquals("Index of 'aa' should be 2.", result, 2);
 }

 @Test
 public void testIndexOfNonOverlappingMultipleCharacters() {
 char[] target = new char[]{'a', 'b', 'a'};
 int result = indexedString.indexOf(target);

 assertEquals("Index of 'aba' should be 1.", result, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfOverlappingMultipleCharacters() {
 char[] target = new char[]{'a', 'a', 'a'};

 indexedString.indexOf(target); // Should throw an IllegalArgumentException
 }
}
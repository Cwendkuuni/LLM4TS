package de.beiri22.stringincrementor.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.arrayContaining;
import static org.hamcrest.CoreMatchers.hasItem;

import org.junit.Test;

public class IndexedStringTest {

 private char[] testValues = new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
 private IndexedString indexedString;

 @Test
 public void testIndexedStringConstructor() {
 indexedString = new IndexedString(new String(testValues));
 }

 @Test
 public void testCountMethod() {
 for (char c = '\u0000'; c <= '\u00FF'; c++) {
 int count = indexedString.count(c);
 if (count > 0) {
 assertEquals(count, indexedString.index[c].length);
 } else {
 assertNull(indexedString.index[c]);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexedStringConstructorWithNullString() {
 try {
 new IndexedString(null);
 } catch (IllegalArgumentException e) {
 assertEquals("String cannot be null", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOfMethodWithEmptyString() {
 char[] emptyTarget = new char[0];
 assertNull(indexedString.indexOf(emptyTarget));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOfMethodWithCharNotInString() {
 char notInString = '\u0100';
 assertNull(indexedString.indexOf(new char[]{notInString}));
 }

 @Test
 public void testIndexOfMethodWithSubString() {
 char[] subString = new char[]{'h', 'e', 'l'};
 int expectedIndex = 0;
 int actualIndex = indexedString.indexOf(subString);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMethodWithSubStringDifferentStartIndex() {
 char[] subStringWithDifferentStartIndex = new char[]{'l', 'o', 'l'};
 int expectedIndex = 4;
 int actualIndex = indexedString.indexOf(subStringWithDifferentStartIndex);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMethodWithSubStringDifferentEndIndex() {
 char[] subStringWithDifferentEndIndex = new char[]{'h', 'e', 'l', 'o'};
 int expectedIndex = 0;
 int actualIndex = indexedString.indexOf(subStringWithDifferentEndIndex);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMethodWithSubStringDifferentChar() {
 char[] subStringWithDifferentChar = new char[]{'w', 'o', 'r', 'l', 'd'};
 int expectedIndex = 10;
 int actualIndex = indexedString.indexOf(subStringWithDifferentChar);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMethodWithSubStringDifferentSequence() {
 char[] subStringWithDifferentSequence = new char[]{'h', 'e', 'l', 'o', 'w'};
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(subStringWithDifferentSequence);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMethodWithSubStringPartiallyMatched() {
 char[] subStringPartiallyMatched = new char[]{'h', 'e'};
 int expectedIndex = 0;
 int actualIndex = indexedString.indexOf(subStringPartiallyMatched);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMethodWithSubStringPartiallyMatchedReverse() {
 char[] subStringPartiallyMatchedReversed = new char[]{'e', 'h'};
 int expectedIndex = 0;
 int actualIndex = indexedString.indexOf(subStringPartiallyMatchedReversed);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMethodWithSubStringPartiallyMatchedEnd() {
 char[] subStringPartiallyMatchedEnd = new char[]{'l', 'o', 'l', 'd'};
 int expectedIndex = 11;
 int actualIndex = indexedString.indexOf(subStringPartiallyMatchedEnd);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMethodWithSubStringPartiallyMatchedStart() {
 char[] subStringPartiallyMatchedStart = new char[]{'h', 'e', 'l', '!'};
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(subStringPartiallyMatchedStart);
 assertEquals(expectedIndex, actualIndex);
 }
}
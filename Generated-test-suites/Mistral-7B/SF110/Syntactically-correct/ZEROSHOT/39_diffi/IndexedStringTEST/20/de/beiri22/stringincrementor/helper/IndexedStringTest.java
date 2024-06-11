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
 char[] values = new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
 indexedString = new IndexedString(new String(values));
 }

 @After
 public void tearDown() {
 indexedString = null;
 }

 @Test
 public void testIndexedStringConstructor() {
 char[] values = indexedString.values;
 int expectedLength = values.length;
 int[][] index = indexedString.index;
 assertEquals(expectedLength, values.length);
 assertEquals(256, index.length);
 for (char c = '\u0000'; c < 0x100; c++) {
 if (c == '\u0020') {
 continue;
 }
 int expectedCount = 0;
 for (int i = 0; i < values.length; i++) {
 if (values[i] == c) {
 expectedCount++;
 }
 }
 int actualCount = index[c].length;
 assertEquals("Incorrect index count for character " + c, expectedCount, actualCount);
 }
 }

 @Test
 public void testEmptyString() {
 IndexedString emptyString = new IndexedString("");
 int[][] index = emptyString.index;
 assertEquals(0, index.length);
 }

 @Test
 public void testIndexOf() {
 char[] target1 = new char[]{'h', 'e', 'l', 'l', 'o'};
 int expectedIndex1 = 0;
 int actualIndex1 = indexedString.indexOf(target1);
 assertEquals("Unexpected index for target 'hello': ", expectedIndex1, actualIndex1);

 char[] target2 = new char[]{'w', 'o', 'r', 'l', 'd'};
 int expectedIndex2 = 10;
 int actualIndex2 = indexedString.indexOf(target2);
 assertEquals("Unexpected index for target 'world': ", expectedIndex2, actualIndex2);

 char[] target3 = new char[]{'x', 'y', 'z'};
 int actualIndex3 = indexedString.indexOf(target3);
 assertNull("Unexpected index for non-existent target 'xyz'");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullString() {
 IndexedString indexedStringNull = new IndexedString(null);
 }
}
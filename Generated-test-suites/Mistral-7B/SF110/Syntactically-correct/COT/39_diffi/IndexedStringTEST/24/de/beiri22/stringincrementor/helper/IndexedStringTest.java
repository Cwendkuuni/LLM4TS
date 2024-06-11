package de.beieri22.stringincrementor.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import de.beieri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 private char[] testString1 = {'h', 'e', 'l', 'l', 'o'};
 private char[] testString2 = {'h', 'e', 'l', 'l', 'o', 'w'};
 private char[] testString3 = {'h', 'e', 'l', 'l', 'o', 'w', 'o'};
 private char[] testString4 = {'h', 'e', 'l', 'l', 'o', 'w', 'o', 'o'};
 private char[] testString5 = {};
 private char[] testString6 = {'\u0001'};
 private char[] testString7 = {'a', 'b', 'c', 'd', 'e', 'f'};

 @Test
 public void testConstructor() {
 char[] actual = new char[5];
 String testString = new String(actual);
 IndexedString indexedString = new IndexedString(testString);
 assertEquals(5, indexedString.values.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullString() {
 IndexedString indexedString = new IndexedString(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorEmptyString() {
 IndexedString indexedString = new IndexedString(new char[0]);
 }

 @Test
 public void testCount() {
 IndexedString indexedString = new IndexedString(new String(testString1));
 assertEquals(2, indexedString.count('l'));
 assertEquals(1, indexedString.count('h'));
 assertEquals(0, indexedString.count('z'));
 }

 @Test
 public void testIndexOf() {
 IndexedString indexedString = new IndexedString(new String(testString1));
 assertEquals(0, indexedString.indexOf(testString1));
 indexedString = new IndexedString(new String(testString2));
 assertEquals(0, indexedString.indexOf(testString1));
 indexedString = new IndexedString(new String(testString3));
 assertEquals(0, indexedString.indexOf(testString1));
 indexedString = new IndexedString(new String(testString4));
 assertEquals(0, indexedString.indexOf(testString1));
 indexedString = new IndexedString(new String(testString5));
 assertEquals(-1, indexedString.indexOf(testString1));
 indexedString = new IndexedString(new String(testString6));
 assertEquals(-1, indexedString.indexOf(testString1));
 indexedString = new IndexedString(new String(testString7));
 assertEquals(-1, indexedString.indexOf(testString1));
 indexedString = new IndexedString(new String(testString2));
 assertEquals(-1, indexedString.indexOf(testString3));
 }

 @Test
 public void testIndexOfEdgeCases() {
 IndexedString indexedString = new IndexedString(new String(testString1));
 assertArrayEquals(new int[]{0, 1, 2, 3, 4}, indexedString.index['h']);
 assertArrayEquals(new int[]{1, 2, 3, 4}, indexedString.index['l']);
 indexedString = new IndexedString(new String(testString2));
 assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5}, indexedString.index['h']);
 assertArrayEquals(new int[]{1, 2, 3, 4, 5}, indexedString.index['l']);
 indexedString = new IndexedString(new String(testString3));
 assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6}, indexedString.index['h']);
 assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, indexedString.index['l']);
 indexedString = new IndexedString(new String(testString4));
 assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 6}, indexedString.index['h']);
 assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 6}, indexedString.index['l']);
 }

 @Test
 public void testIndexOfNullArray() {
 IndexedString indexedString = new IndexedString(new String(testString1));
 assertEquals(-1, indexedString.indexOf(null));
 }
}
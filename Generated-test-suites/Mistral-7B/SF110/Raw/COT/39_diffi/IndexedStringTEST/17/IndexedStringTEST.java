package de.beiri22.stringincrementor.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 private char[] charArray1 = {'h', 'e', 'l', 'l', 'o'};
 private char[] charArray2 = {'e', 'l', 'l', 'o'};
 private char[] charArray3 = {'h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd'};
 private char[] charArray4 = {};
 private char[] charArray5 = {'\u0001'};
 private char[] charArray6 = {'h', 'e', '\u0100'};

 @Test
 public void testIndexedStringConstructor() {
 char[] values = new char[5];
 String s = new String(values);
 IndexedString is = new IndexedString(s);
 assertEquals(5, is.values.length);
 assertArrayEquals(values, is.values);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexedStringConstructorNullString() {
 IndexedString is = new IndexedString(null);
 }

 @Test
 public void testCountMethod() {
 char c = 'l';
 IndexedString is = new IndexedString("hello");
 assertEquals(2, is.count(c));
 }

 @Test
 public void testIndexOfMethodNullTarget() {
 IndexedString is = new IndexedString("hello");
 assertNull(is.indexOf(null));
 }

 @Test
 public void testIndexOfMethodEmptyTarget() {
 IndexedString is = new IndexedString("hello");
 assertEquals(-1, is.indexOf(new char[0][]));
 }

 @Test
 public void testIndexOfMethodSingleCharacter() {
 IndexedString is = new IndexedString("hello");
 char c = 'l';
 assertEquals(2, is.indexOf(new char[]{c}));
 }

 @Test
 public void testIndexOfMethodMultipleCharactersSameString() {
 IndexedString is = new IndexedString("hello");
 char[] target = {'l', 'l'};
 assertEquals(2, is.indexOf(target));
 }

 @Test
 public void testIndexOfMethodMultipleCharactersDifferentString() {
 IndexedString is1 = new IndexedString("hello");
 IndexedString is2 = new IndexedString("world");
 char[] target = {'l', 'w'};
 assertEquals(-1, is1.indexOf(target));
 }

 @Test
 public void testIndexOfMethodLastCharacter() {
 IndexedString is = new IndexedString("hello");
 char c = 'o';
 assertEquals(5, is.indexOf(new char[]{c}));
 }

 @Test
 public void testIndexOfMethodFirstCharacter() {
 IndexedString is = new IndexedString("hello");
 char c = 'h';
 assertEquals(0, is.indexOf(new char[]{c}));
 }

 @Test
 public void testIndexOfMethodSubString() {
 IndexedString is = new IndexedString("hello");
 char[] target = {'e', 'l', 'l'};
 assertEquals(2, is.indexOf(target));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfMethodOutOfBounds() {
 IndexedString is = new IndexedString("hello");
 char[] target = {'l', 'l', 'l'};
 assertEquals(-1, is.indexOf(target));
 int index = is.indexOf(target);
 assertTrue(index == -1);
 int index2 = is.values[index];
 }

 @Test
 public void testIndexOfMethodOutOfRange() {
 IndexedString is = new IndexedString("hello");
 char[] target = {'h', 'e', 'l', 'l', 'o'};
 assertArrayEquals(target, is.values);
 int index = is.indexOf(target);
 assertEquals(0, index);
 char[] subArray = new char[6];
 System.arraycopy(is.values, index, subArray, 0, 6);
 assertArrayEquals(target, subArray);
 }

 @Test
 public void testIndexOfMethodMultipleMatchesDifferentPositions() {
 IndexedString is = new IndexedString("hello world");
 char[] target = {'l', 'w'};
 int index1 = is.indexOf(target);
 int index2 = is.indexOf(target);
 assertFalse(index1 == index2);
 }

 @Test
 public void testIndexOfMethodEmptyString() {
 IndexedString is = new IndexedString("hello");
 char[] target = {};
 assertEquals(-1, is.indexOf(target));
 }

 @Test
 public void testIndexOfMethodSingleCharacterOutOfRange() {
 IndexedString is = new IndexedString("hello");
 char c = '\u0101';
 assertEquals(-1, is.indexOf(new char[]{c}));
 }

 @Test
 public void testIndexOfMethodMultipleCharactersOutOfRange() {
 IndexedString is = new IndexedString("hello");
 char[] target = {'h', 'e', '\u0101'};
 assertEquals(-1, is.indexOf(target));
 }

 @Test
 public void testIndexOfMethodNullCharacter() {
 IndexedString is = new IndexedString("hello");
 char c = '\u0000';
 assertEquals(-1, is.indexOf(new char[]{c}));
 }
}
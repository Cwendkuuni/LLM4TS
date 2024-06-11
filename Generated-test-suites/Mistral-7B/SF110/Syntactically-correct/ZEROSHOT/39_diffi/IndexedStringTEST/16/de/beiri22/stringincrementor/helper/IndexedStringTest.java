package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private char[] getTestStringCharArray(String s) {
 char[] result = new char[s.length()];
 s.getChars(0, s.length(), result, 0);
 return result;
 }

 @Test
 public void testIndexedStringCreation() {
 CharSequence testString = "testString";
 IndexedString is = new IndexedString(testString.toString());
 Assert.assertArrayEquals(testString.toString().toCharArray(), is.values);
 }

 @Test
 public void testEmptyStringCreation() {
 IndexedString is = new IndexedString("");
 Assert.assertArrayEquals(new char[0], is.values);
 }

 @Test
 public void testCountCharacter() {
 IndexedString is = new IndexedString("testString");
 Assert.assertEquals(4, is.count('t'));
 Assert.assertEquals(3, is.count('s'));
 Assert.assertEquals(2, is.count('r'));
 Assert.assertEquals(1, is.count('ng'));
 }

 @Test
 public void testIndexOfSingleCharacter() {
 IndexedString is = new IndexedString("testString");
 Assert.assertEquals(0, is.indexOf(new char[]{'t'}));
 Assert.assertEquals(3, is.indexOf(new char[]{'s'}));
 Assert.assertEquals(5, is.indexOf(new char[]{'r'}));
 Assert.assertEquals(7, is.indexOf(new char[]{'n'}));
 }

 @Test
 public void testIndexOfMultipleCharacters() {
 IndexedString is = new IndexedString("testString");
 Assert.assertEquals(0, is.indexOf(new char[]{'t', 'e'}));
 Assert.assertEquals(1, is.indexOf(new char[]{'e', 's'}));
 Assert.assertEquals(6, is.indexOf(new char[]{'t', 'r'}));
 Assert.assertEquals(-1, is.indexOf(new char[]{'x', 'y'}));
 }

 @Test
 public void testIndexOfLongerSubString() {
 IndexedString is = new IndexedString("testString");
 CharSequence longerSubString = "estString";
 Assert.assertEquals(0, is.indexOf(longerSubString.subSequence(1, longerSubString.length()).charAtArray()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfNull() {
 IndexedString is = new IndexedString("testString");
 Assert.assertArrayIsNull(is.indexOf(null));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOfBeyondEndOfString() {
 IndexedString is = new IndexedString("testString");
 Assert.assertEquals(-1, is.indexOf(new char[]{'x'}));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOfBeyondEndOfSubString() {
 IndexedString is = new IndexedString("testString");
 CharSequence subString = "testest";
 Assert.assertEquals(-1, is.indexOf(subString.subSequence(2, subString.length()).charAtArray()));
 }

 private void assertArrayIsNull(int[] result) {
 if (result != null) {
 throw new AssertionError("Expected null, but got: " + result);
 }
 }

}
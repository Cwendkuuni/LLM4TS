package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private char[] getTestStringCharArray(String testString) {
 char[] charArray = new char[testString.length()];
 testString.getChars(0, testString.length(), charArray, 0);
 return charArray;
 }

 @Test
 public void testIndexedStringCreation() {
 final String testString = "The quick brown fox jumps over the lazy dog";
 IndexedString indexedString = new IndexedString(testString);
 Assert.assertEquals(testString.length(), indexedString.values.length);
 }

 @Test
 public void testIndexedStringCreateWithEmptyString() {
 IndexedString indexedString = new IndexedString("");
 Assert.assertEquals(0, indexedString.values.length);
 }

 @Test
 public void testIndexedStringCreateWithNullString() {
 try {
 new IndexedString(null);
 fail("Should not be able to create IndexedString with null string");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testCountCharacter() {
 final String testString = "The quick brown fox jumps over the lazy dog";
 IndexedString indexedString = new IndexedString(testString);
 Assert.assertEquals(11, indexedString.count(' '));
 Assert.assertEquals(1, indexedString.count('T'));
 }

 @Test
 public void testIndexedStringIndexOfCharacter() {
 final String testString = "The quick brown fox jumps over the lazy dog";
 IndexedString indexedString = new IndexedString(testString);

 Assert.assertEquals(0, indexedString.indexOf(new char[]{'T'}));
 Assert.assertEquals(4, indexedString.indexOf(new char[]{'q'}));
 Assert.assertEquals(6, indexedString.indexOf(new char[]{'o'}));
 Assert.assertEquals(8, indexedString.indexOf(new char[]{'w'}));
 Assert.assertEquals(-1, indexedString.indexOf(new char[]{'z'}));
 }

 @Test
 public void testIndexedStringIndexOfSubstring() {
 final String testString = "The quick brown fox jumps over the lazy dog";
 IndexedString indexedString = new IndexedString(testString);

 char[] substringToFind = new char[5];
 substringToFind[0] = 'q';
 substringToFind[1] = 'u';
 substringToFind[2] = 'i';
 substringToFind[3] = 'c';
 substringToFind[4] = 'k';

 int index = indexedString.indexOf(substringToFind);
 Assert.assertEquals(4, index);
 }

 @Test
 public void testIndexedStringIndexOfSubstringWithCaseSensitivity() {
 final String testString = "The quick brown fox jumps over the lazy dog";
 IndexedString indexedString = new IndexedString(testString);

 char[] substringToFind = new char[5];
 substringToFind[0] = 'Q';
 substringToFind[1] = 'u';
 substringToFind[2] = 'i';
 substringToFind[3] = 'c';
 substringToFind[4] = 'k';

 int index = indexedString.indexOf(substringToFind);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexedStringIndexOfSubstringWithPartialMatch() {
 final String testString = "The quick brown fox jumps over the lazy dog";
 IndexedString indexedString = new IndexedString(testString);

 char[] substringToFind = new char[3];
 substringToFind[0] = 'q';
 substringToFind[1] = 'u';
 substringToFind[2] = 'i';

 int index = indexedString.indexOf(substringToFind);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexedStringIndexOfSubstringWithSubstringLongerThanString() {
 final String testString = "The quick brown fox jumps over the lazy dog";
 IndexedString indexedString = new IndexedString(testString);

 char[] substringToFind = new char[testString.length() + 1];

 int index = indexedString.indexOf(substringToFind);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexedStringIndexOfSubstringWithNullArray() {
 try {
 IndexedString indexedString = new IndexedString("The quick brown fox jumps over the lazy dog");
 indexedString.indexOf(null);
 fail("Should not be able to call indexOf with null array");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testIndexedStringIndexOfSubstringWithEmptyArray() {
 final String testString = "The quick brown fox jumps over the lazy dog";
 IndexedString indexedString = new IndexedString(testString);

 int index = indexedString.indexOf(new char[0][]);
 Assert.assertEquals(-1, index);
 }
}
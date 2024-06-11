package de.beiri22.stringincrementor.helper.test;

import org.junit.Assert;
import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 private char[] testCharArray1 = {'a', 'a', 'b', 'b', 'c'};
 private char[] testCharArray2 = {'a', 'a', 'a', 'b', 'b'};
 private char[] testCharArray3 = {'a'};
 private char[] testCharArray4 = {};

 private IndexedString testIndexedString1;
 private IndexedString testIndexedString2;
 private IndexedString testIndexedString3;
 private IndexedString testIndexedString4;

 @Test
 public void testIndexedStringConstructor() {
 testIndexedString1 = new IndexedString("aaabbc");
 testIndexedString2 = new IndexedString("aaaaabbb");
 testIndexedString3 = new IndexedString("a");
 testIndexedString4 = new IndexedString("");
 }

 @Test
 public void testCountMethod() {
 // Test with valid character
 Assert.assertEquals(2, testIndexedString1.count('a'));
 // Test with invalid character
 Assert.assertEquals(0, testIndexedString1.count('z'));
 }

 @Test
 public void testIndexOfMethodWithValidInput() {
 // Test with single character input
 Assert.assertEquals(0, testIndexedString1.indexOf(new char[]{'a'}));
 // Test with multiple character input that is present in the IndexedString
 Assert.assertEquals(1, testIndexedString1.indexOf(new char[]{'b', 'b'}));
 // Test with multiple character input that is partially present in the IndexedString
 Assert.assertEquals(2, testIndexedString2.indexOf(new char[]{'a', 'a'}));
 }

 @Test(expected = java.lang.NullPointerException.class)
 public void testIndexOfMethodWithNullInput() {
 testIndexedString1.indexOf(null);
 }

 @Test(expected = java.lang.ArrayIndexOutOfBoundsException.class)
 public void testIndexOfMethodWithInputNotPresentInIndexedString() {
 testIndexedString1.indexOf(new char[]{'z'});
 testIndexedString2.indexOf(new char[]{'c'});
 }

 @Test(expected = java.lang.ArrayIndexOutOfBoundsException.class)
 public void testIndexOfMethodWithInputLongerThanIndexedString() {
 testIndexedString1.indexOf(new char[]{'a', 'a', 'a'});
 }
}
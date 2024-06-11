package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private char[] target;
 private IndexedString indexedString;

 @Test
 public void testIndexedStringConstructor() {
 char[] values = new char[]{"a", "b", "a", "a", "b"};
 indexedString = new IndexedString(new String(values));
 Assert.assertArrayEquals(values, indexedString.values);
 }

 @Test
 public void testIndexOfChar() {
 target = new char[]{'a', 'a'};
 indexedString = new IndexedString("aaabbbba");
 int index = indexedString.indexOf(target);
 Assert.assertEquals(2, index);
 }

 @Test
 public void testIndexOfEmptyCharArray() {
 indexedString = new IndexedString("aaabbbba");
 int index = indexedString.indexOf(new char[0][]);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexOfCharNotInString() {
 indexedString = new IndexedString("aaabbbba");
 int index = indexedString.indexOf(new char[]{'c'});
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexOfSubstring() {
 target = new char[]{'a', 'a'};
 indexedString = new IndexedString("aaabbbbaaa");
 int index = indexedString.indexOf(target);
 Assert.assertEquals(6, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexedStringConstructorNullString() {
 indexedString = new IndexedString(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfSubstringBeyondEndOfString() {
 indexedString = new IndexedString("aaabbbba");
 indexedString.indexOf(new char[]{'a', 'a', 'a'});
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfSubstringBeyondBeginningOfString() {
 indexedString = new IndexedString("aaabbbba");
 indexedString.indexOf(new char[]{'b', 'a'});
 }

 @Test
 public void testIndexOfSubstringWithLeadingSpace() {
 target = new char[]{' ', 'a'};
 indexedString = new IndexedString(" aaabbbba");
 int index = indexedString.indexOf(target);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexOfSubstringWithTrailingSpace() {
 target = new char[]{'a', ' '};
 indexedString = new IndexedString("aaabbbba ");
 int index = indexedString.indexOf(target);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexOfSubstringWithLeadingAndTrailingSpace() {
 target = new char[]{' ', 'a', ' '};
 indexedString = new IndexedString(" aaabbbba ");
 int index = indexedString.indexOf(target);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexOfSubstringWithDifferentCase() {
 target = new char[]{'A', 'a'};
 indexedString = new IndexedString("AAAaaaBbbba");
 int index = indexedString.indexOf(target);
 Assert.assertEquals(-1, index);
 }
}
package de.beiri22.stringincrementor.helper.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNegative;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 private char[] charArray1 = {'a', 'a', 'b', 'b', 'c', 'c'};
 private char[] charArray2 = {'a', 'b', 'a', 'b', 'a', 'b'};
 private char[] charArray3 = {'a', 'b', 'c'};
 private char[] charArray4 = {};
 private char[] charArray5 = {'a'};

 @Test
 public void testIndexedStringConstructor() {
 IndexedString is1 = new IndexedString("aaabbbccc");
 IndexedString is2 = new IndexedString("ababab");
 IndexedString is3 = new IndexedString("abc");
 IndexedString is4 = new IndexedString("");
 IndexedString is5 = new IndexedString("a");

 assertEquals(6, is1.values.length);
 assertEquals(26, is1.index.length);
 assertEquals(6, is2.values.length);
 assertEquals(26, is2.index.length);
 assertEquals(3, is3.values.length);
 assertEquals(26, is3.index.length);
 assertEquals(0, is4.values.length);
 assertEquals(26, is4.index.length);
 assertEquals(1, is5.values.length);
 assertEquals(26, is5.index.length);
 }

 @Test
 public void testCountMethod() {
 char c = 'a';
 IndexedString is = new IndexedString("aaabbbccc");

 assertEquals(6, is.count(c));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCountMethodWithNullCharacter() {
 char c = null;
 IndexedString is = new IndexedString("aaabbbccc");

 is.count(c);
 }

 @Test
 public void testIndexOfMethod() {
 int result;

 result = is1.indexOf(charArray1);
 assertEquals(0, result);

 result = is1.indexOf(charArray2);
 assertNegative(result);

 result = is1.indexOf(charArray3);
 assertNegative(result);

 result = is1.indexOf(charArray4);
 assertNegative(result);

 result = is1.indexOf(charArray5);
 assertNegative(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfMethodWithNullCharacterArray() {
 char[] target = null;
 IndexedString is = new IndexedString("aaabbbccc");

 is.indexOf(target);
 }
}
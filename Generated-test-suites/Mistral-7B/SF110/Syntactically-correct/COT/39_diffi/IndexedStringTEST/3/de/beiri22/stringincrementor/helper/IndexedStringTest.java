package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private char[] values;
 private int[][] index;

 @Test
 public void testIndexedStringConstructor() {
 char[] testValues = new char[]{'a', 'b', 'a', 'c', 'a', 'a'};
 IndexedString is = new IndexedString(new String(testValues));
 Assert.assertArrayEquals(testValues, is.values);
 // Check the index array is initialized correctly
 for (char c = '\0'; c < '\u0100'; ++c) {
 if (is.index[c] != null) {
 int cnt = is.count(c);
 Assert.assertEquals(cnt, is.index[c].length);
 for (int i = 0; i < cnt; ++i) {
 Assert.assertTrue(is.index[c][i] >= 0 && is.index[c][i] < is.values.length);
 }
 }
 }
 }

 @Test
 public void testCountMethod() {
 IndexedString is = new IndexedString("testtesttest");
 Assert.assertEquals(3, is.count('t'));
 Assert.assertEquals(3, is.count('e'));
 Assert.assertEquals(1, is.count('s'));
 Assert.assertEquals(0, is.count('z'));
 }

 @Test
 public void testIndexOfMethod() {
 IndexedString is = new IndexedString("testtesttest");
 char[] target1 = new char[]{'t', 'e', 's'};
 int result1 = is.indexOf(target1);
 Assert.assertEquals(1, result1);
 char[] target2 = new char[]{'s', 't', 'e'};
 int result2 = is.indexOf(target2);
 Assert.assertEquals(-1, result2);
 char[] target3 = new char[]{'a', 'a', 'a'};
 int result3 = is.indexOf(target3);
 Assert.assertEquals(3, result3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfMethodWithNullTarget() {
 IndexedString is = new IndexedString("testtesttest");
 char[] nullTarget = null;
 is.indexOf(nullTarget);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfMethodWithTargetBeyondStringLength() {
 IndexedString is = new IndexedString("testtesttest");
 char[] target1 = new char[this.values.length + 1];
 int result1 = is.indexOf(target1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfMethodWithEmptyTarget() {
 IndexedString is = new IndexedString("testtesttest");
 char[] emptyTarget = new char[0];
 is.indexOf(emptyTarget);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfMethodWithTargetLongerThanString() {
 IndexedString is = new IndexedString("testtesttest");
 char[] target1 = new char[this.values.length + 1];
 is.indexOf(target1);
 }

 @Test
 public void testIndexOfMethodWithTargetOnlyOneCharacter() {
 IndexedString is = new IndexedString("testtesttest");
 char[] singleTarget = new char[]{'t'};
 int result1 = is.indexOf(singleTarget);
 Assert.assertEquals(0, result1);
 char[] singleTarget2 = new char[]{'e'};
 int result2 = is.indexOf(singleTarget2);
 Assert.assertEquals(2, result2);
 char[] singleTarget3 = new char[]{'s'};
 int result3 = is.indexOf(singleTarget3);
 Assert.assertEquals(4, result3);
 }

 @Test
 public void testIndexOfMethodWithTargetContainingRepeatedCharacters() {
 IndexedString is = new IndexedString("testtesttest");
 char[] target1 = new char[]{'t', 'e'};
 int result1 = is.indexOf(target1);
 Assert.assertEquals(1, result1);
 char[] target2 = new char[]{'e', 't'};
 int result2 = is.indexOf(target2);
 Assert.assertEquals(2, result2);
 }

}
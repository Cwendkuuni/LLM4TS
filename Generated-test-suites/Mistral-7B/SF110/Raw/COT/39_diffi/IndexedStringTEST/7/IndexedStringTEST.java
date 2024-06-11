package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNegative;
import static org.junit.Assert.assertNull;

public class IndexedStringTest {

 private char[] testValues = new char[]{'a', 'b', 'a', 'b', 'a'};
 private IndexedString testIndexedString;

 @Test
 public void testIndexedStringConstructor() {
 testIndexedString = new IndexedString(new String(testValues));
 }

 @Test
 public void testCountChar() {
 assertEquals(3, testIndexedString.count('a'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCountCharInvalidChar() {
 testIndexedString.count('z');
 }

 @Test
 public void testIndexOfEmptyTarget() {
 assertNull(testIndexedString.indexOf(new char[0]));
 }

 @Test
 public void testIndexOfSingleChar() {
 assertEquals(0, testIndexedString.indexOf(new char[]{'a'}));
 }

 @Test
 public void testIndexOfMultipleCharsSuccess() {
 char[] target = new char[]{'a', 'b'};
 assertEquals(2, testIndexedString.indexOf(target));
 }

 @Test
 public void testIndexOfMultipleCharsFailure() {
 char[] target = new char[]{'c', 'b'};
 assertNegative(testIndexedString.indexOf(target));
 }

 @Test
 public void testIndexOfLongerTarget() {
 char[] target = new char[]{'a', 'b', 'a'};
 assertEquals(1, testIndexedString.indexOf(target));
 }

 @Test
 public void testIndexOfLongerTargetFailure() {
 char[] target = new char[]{'a', 'c'};
 assertNegative(testIndexedString.indexOf(target));
 }

 @Test
 public void testIndexOfSubArray() {
 char[] targetSubArray = new char[2][];
 targetSubArray[0] = new char[]{'a'};
 targetSubArray[1] = new char[]{'b'};
 assertNegative(testIndexedString.indexOf(targetSubArray));
 }

 @Test(expected = NullPointerException.class)
 public void testIndexOfNullTarget() {
 testIndexedString.indexOf(null);
 }
}
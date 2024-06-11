package org.mockito.internal.matchers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import static org.mockito.internal.matchers.Equality.areArrayElementsEqual;
import static org.mockito.internal.matchers.Equality.areArrayLengthsEqual;
import static org.mockito.internal.matchers.Equality.areArraysEqual;
import static org.mockito.internal.matchers.Equality.isArray;
import static org.mockito.internal.matchers.Equality.areEqual;

public class EqualityTest {

 @Test
 public void testAreEqualNullObjects() {
 Assert.assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqualSameObjects() {
 Object obj = new Object();
 Assert.assertTrue(Equality.areEqual(obj, obj));
 }

 @Test
 public void testAreEqualDifferentObjectsDifferentClasses() {
 Object obj1 = new Object();
 Integer obj2 = 1;
 Assert.assertFalse(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualDifferentObjectsSameClass() {
 Integer obj1 = 1;
 Integer obj2 = 2;
 Assert.assertFalse(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualArraysSameLengthAndSameElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 Assert.assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysSameLengthDifferentElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 4};
 Assert.assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysDifferentLengthSameElementsFirst() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2};
 Assert.assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysDifferentLengthSameElementsLast() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2};
 Object[] array3 = new Object[]{1, 2, null};
 Assert.assertFalse(Equality.areEqual(array1, array3));
 }

 @Test
 public void testAreEqualArraysSameLengthAndSameElementsOrderDoesNotMatter() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{3, 2, 1};
 Assert.assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysSameLengthAndDifferentElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 4};
 Assert.assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysDifferentLengthFirstShorter() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2};
 Assert.assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysDifferentLengthFirstLonger() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3, 4};
 Assert.assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArrayOfArrays() {
 Object[][] array1 = new Object[][]{{1, 2}, {3, 4}};
 Object[][] array2 = new Object[][]{{1, 2}, {3, 4}};
 Assert.assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysWithDifferentLengths() {
 Object[][] array1 = new Object[][]{{1, 2}, {3, 4}};
 Object[][] array2 = new Object[][]{{1, 2}, {3, 4, 5}};
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testIsArray() {
 Assert.assertTrue(Equality.isArray(new int[0][0]));
 Assert.assertTrue(Equality.isArray(new int[]{}));
 Assert.assertTrue(Equality.isArray(new int[]{1}));
 Assert.assertTrue(Equality.isArray(new int[0][][0]));
 Assert.assertFalse(Equality.isArray(1));
 Assert.assertFalse(Equality.isArray(""));
 Assert.assertFalse(Equality.isArray(new String[]{"1"}));
 Assert.assertFalse(Equality.isArray(new Object(){ }));
 }

 @Test
 public void testAreArraysEqualArrayElementsAreEqualArrays() {
 Object[] array1 = new Object[][]{new Object[]{1, 2}, new Object[]{3, 4}};
 Object[] array2 = new Object[][]{new Object[]{1, 2}, new Object[]{3, 4}};
 Assert.assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArrayElementsAreDifferentArrays() {
 Object[] array1 = new Object[][]{new Object[]{1, 2}, new Object[]{3, 4}};
 Object[] array2 = new Object[][]{new Object[]{1, 2}, new Object[]{3, 5}};
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArrayLengthsAreEqualArraysAreDifferent() {
 Object[] array1 = new Object[][]{new Object[]{1, 2}, new Object[]{3, 4}, new Object[]{5, 6}};
 Object[] array2 = new Object[]{new Object[]{1, 2}, new Object[]{3, 4}};
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArrayLengthsAreEqualArraysAreSame() {
 Object[] array1 = new Object[][]{new Object[]{1, 2}, new Object[]{3, 4}, new Object[]{5, 6}};
 Object[] array2 = new Object[][]{new Object[]{1, 2}, new Object[]{3, 4}, new Object[]{5, 6}};
 Assert.assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualMixedArrayAndPrimitive() {
 Object[] array1 = new Object[]{1, 2, (byte) 1};
 Object[] array2 = new Object[]{1, 2, (short) 1};
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualPrimitiveArrays() {
 short[] shortArray1 = {1, 2};
 short[] shortArray2 = {1, 2};
 Assert.assertTrue(Equality.areArraysEqual(shortArray1, shortArray2));
 }

 @Test
 public void testAreArraysEqualArraysWithNullElements() {
 Object[] array1 = new Object[]{1, 2, null};
 Object[] array2 = new Object[]{1, 2, null};
 Assert.assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysWithNullElementsDifferentPositions() {
 Object[] array1 = new Object[]{1, null, 2};
 Object[] array2 = new Object[]{1, 2, null};
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysWithNullElementsDifferentNulls() {
 Object[] array1 = new Object[]{1, new Object(), null};
 Object[] array2 = new Object[]{1, null, new Object()};
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysWithNullElementsDifferentLengths() {
 Object[] array1 = new Object[]{1, 2, null};
 Object[] array2 = new Object[]{1, 2};
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysWithNullElementsAndSameLengths() {
 Object[] array1 = new Object[]{1, 2, null};
 Object[] array2 = new Object[]{1, 2, null};
 Assert.assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysWithArraysAsElements() {
 Object[] array1 = new Object[]{new Object[]{1, 2}, new Object[]{3, 4}};
 Object[] array2 = new Object[]{new Object[]{1, 2}, new Object[]{3, 4}};
 Assert.assertTrue(Equality.areArraysEqual(array1, array2));
 }
}
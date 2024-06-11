package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

 private int[] testSize = {2, 3, 4};

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeSizes() {
 MultidimensionalCounter multidimensionalCounter = null;
 try {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{-1, 3, 4});
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 assertNull(multidimensionalCounter);
 }

 @Test
 public void testConstructorValidSizes() throws NotStrictlyPositiveException {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 assertEquals(3, multidimensionalCounter.getDimension());
 assertArrayEquals(testSize, multidimensionalCounter.getSizes());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsNegativeIndex() throws OutOfRangeException {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 int[] indices = multidimensionalCounter.getCounts(-1);
 fail("Should have thrown an exception");
 }

 @Test
 public void testGetCountsValidIndex() throws OutOfRangeException {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 int[] indices = multidimensionalCounter.getCounts(0);
 assertArrayEquals(new int[]{0, 0, 0}, indices);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() throws OutOfRangeException {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 int count = multidimensionalCounter.getCount(new int[]{2, 3, 3});
 int expectedCount = multidimensionalCounter.uniCounterOffset[0] * 2 + multidimensionalCounter.uniCounterOffset[1] * 3 + multidimensionalCounter.uniCounterOffset[2] * 3;
 int actualCount = multidimensionalCounter.getCount(new int[]{2, 3, 3});
 assertEquals(expectedCount, actualCount);
 int testIndex = multidimensionalCounter.totalSize + 1;
 int[] indicesOutOfRange = multidimensionalCounter.getCounts(testIndex);
 fail("Should have thrown an exception");
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountInvalidDimensions() throws DimensionMismatchException {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 int[] invalidDimensions = new int[]{3, 3, 2};
 multidimensionalCounter.getCount(invalidDimensions);
 fail("Should have thrown an exception");
 }

 @Test
 public void testGetCountValidDimensions() throws OutOfRangeException, DimensionMismatchException {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 int[] validDimensions = new int[]{1, 1, 1};
 int count = multidimensionalCounter.getCount(validDimensions);
 int expectedCount = multidimensionalCounter.uniCounterOffset[0] * 1 + multidimensionalCounter.uniCounterOffset[1] * 1 + multidimensionalCounter.uniCounterOffset[2] * 1;
 assertEquals(expectedCount, count);
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 int next = iterator.next();
 assertEquals(0, next);
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 int count = iterator.getCount();
 assertEquals(0, count);
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 int[] counts = iterator.getCounts();
 assertArrayEquals(new int[]{0, 0, 0}, counts);
 }

 @Test
 public void testConstructorWithZeroSizes() throws NotStrictlyPositiveException {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{0, 0, 0});
 assertEquals(3, multidimensionalCounter.getDimension());
 assertArrayEquals(new int[]{0, 0, 0}, multidimensionalCounter.getSizes());
 }

 @Test
 public void testGetTotalSize() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 assertEquals(144, multidimensionalCounter.getTotalSize());
 }

 @Test
 public void testToString() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 String toString = multidimensionalCounter.toString();
 String expectedToString = "[0][0][0]";
 assertEquals(expectedToString, toString);
 }
}
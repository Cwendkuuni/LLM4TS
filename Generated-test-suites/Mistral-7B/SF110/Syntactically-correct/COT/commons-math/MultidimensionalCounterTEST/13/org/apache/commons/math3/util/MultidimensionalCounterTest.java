package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import java.util.Arrays;
import java.util.Iterator;

public class MultidimensionalCounterTest {

 private int[] testSize = {2, 3, 4};

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterNegativeSize() {
 MultidimensionalCounter multidimensionalCounter = null;
 try {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{-1, 2, 3});
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 assertEquals(-1, e.getValue());
 } finally {
 if (multidimensionalCounter != null) {
 multidimensionalCounter.close();
 }
 }
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 assertEquals(testSize.length, multidimensionalCounter.getDimension());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsNegativeIndex() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 multidimensionalCounter.getCounts(-1);
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 int index = 10;
 int[] actual = multidimensionalCounter.getCounts(index);
 int[] expected = new int[multidimensionalCounter.getDimension()];
 Arrays.fill(expected, index);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetCountsIterator() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 Iterator<Integer> iterator = multidimensionalCounter.iterator();
 int[] expected = new int[multidimensionalCounter.getDimension()];
 int count = 0;
 while (iterator.hasNext()) {
 expected[count++] = iterator.next();
 }
 assertArrayEquals(expected, multidimensionalCounter.getCounts());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountNegativeIndex() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 multidimensionalCounter.getCount(-1, 0);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountDimensionMismatch() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 multidimensionalCounter.getCount(new int[]{3, 2});
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 int[] actual = multidimensionalCounter.getCount(new int[]{2, 1, 0});
 int expected = 2 * testSize[1] * testSize[0];
 assertEquals(expected, actual);
 }

 @Test
 public void testGetCountIterator() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 Iterator<Integer> iterator = multidimensionalCounter.iterator();
 int expected = 0;
 int count = 0;
 while (iterator.hasNext()) {
 int actual = iterator.next();
 assertEquals(expected++, actual);
 }
 assertEquals(MathArrays.prod(testSize), expected);
 }

 @Test
 public void testGetSize() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 assertEquals(MathArrays.prod(testSize), multidimensionalCounter.getSize());
 }

 @Test
 public void testGetSizes() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 int[] actual = multidimensionalCounter.getSizes();
 assertArrayEquals(testSize, actual);
 }

 @Test
 public void testToString() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 String actual = multidimensionalCounter.toString();
 StringBuilder expected = new StringBuilder();
 for (int i = 0; i < testSize.length; ++i) {
 expected.append("[").append(testSize[i]).append("]");
 }
 assertEquals(expected.toString(), actual);
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 Iterator<Integer> iterator = multidimensionalCounter.iterator();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 Iterator<Integer> iterator = multidimensionalCounter.iterator();
 int expected = 0;
 int actual = iterator.next();
 assertEquals(expected++, actual);
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 Iterator<Integer> iterator = multidimensionalCounter.iterator();
 int expected = 0;
 int count = iterator.getCount();
 assertEquals(expected, count);
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 Iterator<Integer> iterator = multidimensionalCounter.iterator();
 int[] expected = new int[multidimensionalCounter.getDimension()];
 int[] actual = iterator.getCounts();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testIteratorRemoveUnsupportedOperationException() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(testSize);
 Iterator<Integer> iterator = multidimensionalCounter.iterator();
 assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
 }
}
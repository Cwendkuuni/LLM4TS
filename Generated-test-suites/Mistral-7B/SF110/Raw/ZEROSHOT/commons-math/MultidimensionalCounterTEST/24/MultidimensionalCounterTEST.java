package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter multidimensionalCounter;

 @Test
 public void testMultidimensionalCounterConstruction() {
 int[] size = {2, 3, 4};
 try {
 multidimensionalCounter = new MultidimensionalCounter(size);
 } catch (NotStrictlyPositiveException e) {
 fail("Unexpected NotStrictlyPositiveException");
 }
 assertEquals(3, multidimensionalCounter.getDimension());
 assertEquals(Arrays.asList(2, 3, 4), Arrays.asList(multidimensionalCounter.getSizes()));
 assertEquals(2304, multidimensionalCounter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterNegativeSizes() {
 int[] size = {-1, 3, 4};
 try {
 multidimensionalCounter = new MultidimensionalCounter(size);
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }

 @Test
 public void testGetCounts() {
 int count = multidimensionalCounter.getCounts(0);
 assertEquals(Arrays.asList(0, 0, 0), Arrays.asList(count));
 count = multidimensionalCounter.getCounts(multidimensionalCounter.getSize() - 1);
 int[] expected = {2, 3, 4};
 assertArrayEquals(expected, count);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 int index = multidimensionalCounter.getSize() + 1;
 try {
 multidimensionalCounter.getCounts(index);
 } catch (OutOfRangeException e) {
 // Expected
 }
 }

 @Test
 public void testGetCount() {
 int[] coordinates = {1, 1, 1};
 int expected = 1 + multidimensionalCounter.uniCounterOffset[0] + multidimensionalCounter.uniCounterOffset[1] + multidimensionalCounter.uniCounterOffset[2];
 int actual = multidimensionalCounter.getCount(coordinates);
 assertEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 int[] coordinates = {-1, 1, 1};
 try {
 multidimensionalCounter.getCount(coordinates);
 } catch (OutOfRangeException e) {
 // Expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountDimensionMismatch() {
 int[] coordinates = {1, 1, 2};
 try {
 multidimensionalCounter.getCount(coordinates);
 } catch (DimensionMismatchException e) {
 // Expected
 }
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 int expectedCount = iterator.getCount();
 int next = iterator.next();
 int newCount = iterator.getCount();
 assertEquals(expectedCount + 1, newCount);
 assertEquals(next, expectedCount + 1);
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 int[] expected = {0, 0, 0};
 int[] actual = iterator.getCounts();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 int expected = 0;
 int actual = iterator.getCount(0);
 assertEquals(expected, actual);
 }
}
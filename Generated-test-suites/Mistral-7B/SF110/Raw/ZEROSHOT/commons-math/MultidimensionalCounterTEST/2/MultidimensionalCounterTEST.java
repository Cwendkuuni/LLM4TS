package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import java.util.Arrays;

public class MultidimensionalCounterTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorThrowsExceptionWithNegativeSize() {
 MultidimensionalCounter multidimensionalCounter = null;
 try {
 multidimensionalCounter = new MultidimensionalCounter(-1, 1);
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 assertNull(multidimensionalCounter);
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 assertEquals(3, multidimensionalCounter.getDimension());
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 int[] indices = multidimensionalCounter.getCounts(0);
 assertEquals(3, indices.length);
 assertArrayEquals(new int[]{0, 0, 0}, indices);

 indices = multidimensionalCounter.getCounts(multidimensionalCounter.getTotalSize() - 1);
 assertEquals(3, indices.length);
 assertArrayEquals(new int[]{2 - 1, 3 - 1, 3 - 1}, indices);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 multidimensionalCounter.getCounts(-1);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfTotalSize() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 multidimensionalCounter.getCounts(multidimensionalCounter.getTotalSize());
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 int count = multidimensionalCounter.getCount(0, 0);
 assertEquals(0, count);

 count = multidimensionalCounter.getCount(multidimensionalCounter.getDimension() - 1, multidimensionalCounter.getSize()[multidimensionalCounter.getDimension() - 1] - 1);
 assertEquals(multidimensionalCounter.getSize()[multidimensionalCounter.getDimension() - 1] * multidimensionalCounter.getSize()[multidimensionalCounter.getDimension() - 2], count);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfSize() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 multidimensionalCounter.getCount(-1, 0);
 multidimensionalCounter.getCount(multidimensionalCounter.getDimension(), 0);
 multidimensionalCounter.getCount(0, multidimensionalCounter.getSize()[0] - 1);
 multidimensionalCounter.getCount(multidimensionalCounter.getDimension() - 1, multidimensionalCounter.getSize()[multidimensionalCounter.getDimension() - 1]);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountDimensionMismatch() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 multidimensionalCounter.getCount(4, 0);
 }

 @Test
 public void testGetTotalSize() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 assertEquals(2 * 3, multidimensionalCounter.getTotalSize());
 }

 @Test
 public void testGetSizes() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 int[] sizes = multidimensionalCounter.getSizes();
 assertEquals(3, sizes.length);
 assertArrayEquals(new int[]{1, 2, 3}, sizes);
 }

 @Test
 public void testToString() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 assertEquals("[0][0][0]", multidimensionalCounter.toString());
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 assertEquals(0, iterator.next());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 iterator.next();
 assertEquals(1, iterator.getCount());
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(1, 2, 3);
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 iterator.next();
 int[] counts = iterator.getCounts();
 assertEquals(3, counts.length);
 assertArrayEquals(new int[]{0, 1, 0}, counts);
 }
}
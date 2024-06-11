package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class MultidimensionalCounterTest {

 @Test
 public void testConstructor() {
 MultidimensionalCounter mdCounter;
 int[] sizes = {2, 3, 4};
 try {
 mdCounter = new MultidimensionalCounter(sizes);
 assertEquals(3, mdCounter.getDimension());
 assertArrayEquals(sizes, mdCounter.getSizes());
 } catch (NotStrictlyPositiveException e) {
 fail("Size should be positive.");
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorNegativeSizes() {
 int[] sizes = {-2, 3, 4};
 new MultidimensionalCounter(sizes);
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 assertEquals(3, mdCounter.getDimension());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 mdCounter.getCounts(-1);
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 int[] counts = mdCounter.getCounts(1);
 assertEquals(3, counts.length);
 assertEquals(0, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(1, counts[2]);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfTotalSize() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 mdCounter.getCounts(10);
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 int count = mdCounter.getCount(new int[]{1, 1, 1});
 assertEquals(1, count);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfSize() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 mdCounter.getCount(new int[]{3, 3, 4});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountDimensionMismatch() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 mdCounter.getCount(new int[]{2, 3});
 }

 @Test
 public void testGetCountCorrect() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 int count = mdCounter.getCount(new int[]{1, 1, 2});
 assertEquals(3 * 2 + 1 * 2 + 1 * 1, count);
 }

 @Test
 public void testGetSize() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 assertEquals(8 * 2 * 3, mdCounter.getSize());
 }

 @Test
 public void testGetTotalSize() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 assertEquals(8 * 2 * 3, mdCounter.getTotalSize());
 }

 @Test
 public void testToString() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 assertEquals("[0][0][0]", mdCounter.toString());
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 MultidimensionalCounter.Iterator it = mdCounter.iterator();
 assertTrue(it.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 MultidimensionalCounter.Iterator it = mdCounter.iterator();
 assertEquals(0, it.next());
 it.next();
 it.next();
 it.next();
 assertEquals(4, it.next());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 MultidimensionalCounter.Iterator it = mdCounter.iterator();
 assertEquals(0, it.getCount());
 it.next();
 it.next();
 it.next();
 it.next();
 assertEquals(4, it.getCount());
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 MultidimensionalCounter.Iterator it = mdCounter.iterator();
 assertEquals(new int[0], it.getCounts());
 it.next();
 it.next();
 it.next();
 it.next();
 int[] counts = it.getCounts();
 assertArrayEquals(new int[]{1, 1, 1}, counts);
 }

 @Test
 public void testIteratorGetCountDim() {
 MultidimensionalCounter mdCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 MultidimensionalCounter.Iterator it = mdCounter.iterator();
 assertEquals(0, it.getCount(0));
 it.next();
 it.next();
 it.next();
 it.next();
 assertEquals(1, it.getCount(0));
 }
}
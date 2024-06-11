package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterNegativeSize() {
 try {
 new MultidimensionalCounter(new int[] { -1, 2, 3 });
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 }

 @Test
 public void testMultidimensionalCounterGetDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 assertEquals(3, mdc.getDimension());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 try {
 mdc.getCounts(-1);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfTotalSize() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 try {
 mdc.getCounts(mdc.getTotalSize() + 1);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetCountsCorrectIndex() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 int[] indices = mdc.getCounts(0);
 assertArrayEquals(new int[] { 0, 0, 0 }, indices);
 }

 @Test
 public void testGetCountsDifferentIndices() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 int[] indices1 = mdc.getCounts(1);
 int[] indices2 = mdc.getCounts(2);
 assertArrayEquals(new int[] { 0, 0, 1 }, indices1);
 assertArrayEquals(new int[] { 0, 1, 0 }, indices2);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithInvalidLength() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 try {
 mdc.getCounts(new int[] { 2, 4 });
 fail("Expected DimensionMismatchException");
 } catch (DimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 int count = mdc.getCount(new int[] { 0, 0, 0 });
 assertEquals(0, count);
 count = mdc.getCount(new int[] { 0, 0, 1 });
 assertEquals(1, count);
 count = mdc.getCount(new int[] { 1, 0, 0 });
 assertEquals(0, count);
 count = mdc.getCount(new int[] { 1, 0, 1 });
 assertEquals(1, count);
 count = mdc.getCount(new int[] { 1, 1, 0 });
 assertEquals(1, count);
 count = mdc.getCount(new int[] { 2, 0, 0 });
 assertEquals(0, count);
 count = mdc.getCount(new int[] { 2, 0, 1 });
 assertEquals(1, count);
 count = mdc.getCount(new int[] { 2, 1, 0 });
 assertEquals(1, count);
 count = mdc.getCount(new int[] { 2, 1, 1 });
 assertEquals(1, count);
 }

 @Test
 public void testGetSize() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 assertEquals(6, mdc.getSize());
 }

 @Test
 public void testGetSizes() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 int[] sizes = mdc.getSizes();
 assertArrayEquals(new int[] { 2, 3, 1 }, sizes);
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 Iterator it = mdc.iterator();
 // First call is false since the iterator is empty
 assertFalse(it.hasNext());
 // Iterator has values after the first call
 assertTrue(it.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 Iterator it = mdc.iterator();
 // First call returns null
 assertNull(it.next());
 // Iterator has values after the first call
 assertEquals(0, it.next().intValue());
 assertEquals(1, it.next().intValue());
 assertEquals(2, it.next().intValue());
 assertEquals(3, it.next().intValue());
 assertEquals(4, it.next().intValue());
 assertEquals(5, it.next().intValue());
 assertEquals(6, it.next().intValue());
 // Iterator has no more values after the last call
 assertFalse(it.hasNext());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 Iterator it = mdc.iterator();
 it.next(); // consume the first value
 it.next(); // consume the second value
 assertEquals(2, it.getCount());
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 Iterator it = mdc.iterator();
 it.next(); // consume the first value
 it.next(); // consume the second value
 int[] counts = it.getCounts();
 assertArrayEquals(new int[] { 0, 1, 0 }, counts);
 }

 @Test
 public void testToString() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] { 2, 3, 1 });
 assertEquals("[0][0][0]", mdc.toString());
 }
}
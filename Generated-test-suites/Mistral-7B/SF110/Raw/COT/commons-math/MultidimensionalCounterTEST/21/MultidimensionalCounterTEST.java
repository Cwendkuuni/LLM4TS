package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

 private static final int[] DIM_SIZE = {2, 3, 4};

 @Test
 public void testMultidimensionalCounterConstruction() throws NotStrictlyPositiveException {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 assertEquals(3, mdc.getDimension());
 assertArrayEquals(DIM_SIZE, mdc.getSizes());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterConstructionWithNegativeSize() throws NotStrictlyPositiveException {
 new MultidimensionalCounter(new int[]{-1, 3, 4});
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 assertEquals(3, mdc.getDimension());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() throws OutOfRangeException {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 mdc.getCounts(1000);
 }

 @Test
 public void testGetCounts() throws OutOfRangeException {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 int[] indices = mdc.getCounts(1);
 assertEquals(3, indices.length);
 assertEquals(0, indices[0]);
 assertEquals(0, indices[1]);
 assertEquals(1, indices[2]);
 }

 @Test
 public void testGetCount() throws OutOfRangeException {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 int count = mdc.getCount(new int[]{1, 1, 1});
 assertEquals(1, count);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountWithWrongDimension() throws DimensionMismatchException, OutOfRangeException {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 mdc.getCount(new int[]{1, 1, 2});
 }

 @Test
 public void testGetCount() throws OutOfRangeException {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 int count = mdc.getCount(new int[]{1, 1, 1});
 assertEquals(1, count);
 }

 @Test
 public void testGetTotalSize() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 assertEquals(32, mdc.getTotalSize());
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 Iterator iterator = mdc.iterator();
 assertTrue(iterator.hasNext());
 iterateAndAssertNextEquals(iterator, 0);
 }

 @Test
 public void testIteratorHasNext_lastElement() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 Iterator iterator = mdc.iterator();
 for (int i = 0; i < mdc.getTotalSize() - 1; ++i) {
 iterateAndAssertNextEquals(iterator, i + 1);
 }
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 Iterator iterator = mdc.iterator();
 int next = iterator.next();
 iterateAndAssertNextEquals(iterator, next);
 }

 @Test
 public void testIteratorNext_lastElement() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 Iterator iterator = mdc.iterator();
 for (int i = 0; i < mdc.getTotalSize() - 1; ++i) {
 iterateAndAssertNextEquals(iterator, i + 1);
 }
 assertSame(mdc.getTotalSize() - 1, iterator.next());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 Iterator iterator = mdc.iterator();
 int count = iterator.getCount();
 assertEquals(0, count);
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(DIM_SIZE);
 Iterator iterator = mdc.iterator();
 int[] counts = iterator.getCounts();
 assertEquals(3, counts.length);
 assertArrayEquals(new int[0], counts);
 }

 @Test
 public void testToString() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
 assertEquals("[0][0][0]", mdc.toString());
 }

 private void iterateAndAssertNextEquals(Iterator iterator, int expectedNext) {
 int next = iterator.next();
 assertEquals(expectedNext, next);
 }

}
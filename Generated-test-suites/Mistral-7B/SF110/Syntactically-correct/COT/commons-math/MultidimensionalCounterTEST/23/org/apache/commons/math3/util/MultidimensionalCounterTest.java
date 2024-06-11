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
 public void testMultidimensionalCounterConstructionWithPositiveSizes() throws NotStrictlyPositiveException {
 multidimensionalCounter = new MultidimensionalCounter(3, 2, 1);
 assertEquals(3, multidimensionalCounter.getDimension());
 assertArrayEquals(new int[]{3, 2, 1}, multidimensionalCounter.getSizes());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterConstructionWithNegativeSizes() {
 try {
 new MultidimensionalCounter(-3, 2, 1);
 } catch (NotStrictlyPositiveException e) {
 fail("Expected NotStrictlyPositiveException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(2, 3);
 assertEquals(2, multidimensionalCounter.getDimension());
 }

 @Test
 public void testGetSizes() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(2, 3);
 assertArrayEquals(new int[]{2, 3}, multidimensionalCounter.getSizes());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsNegativeIndex() {
 multidimensionalCounter = new MultidimensionalCounter(2, 3);
 multidimensionalCounter.getCounts(-1);
 }

 @Test
 public void testGetCountsValidIndex() throws OutOfRangeException {
 multidimensionalCounter = new MultidimensionalCounter(2, 3);
 int[] counts = multidimensionalCounter.getCounts(12);
 assertArrayEquals(new int[]{1, 2, 1}, counts);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithInvalidDimension() {
 multidimensionalCounter = new MultidimensionalCounter(2, 3);
 try {
 multidimensionalCounter.getCounts(new int[]{1, 4});
 } catch (DimensionMismatchException e) {
 fail("Expected DimensionMismatchException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testGetCountWithValidCoords() throws OutOfRangeException, DimensionMismatchException {
 multidimensionalCounter = new MultidimensionalCounter(2, 3);
 int count = multidimensionalCounter.getCount(1, 2, 0);
 assertEquals(4, count);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountWithOutOfRangeCoord1() throws OutOfRangeException, DimensionMismatchException {
 multidimensionalCounter = new MultidimensionalCounter(2, 3);
 try {
 multidimensionalCounter.getCount(1, 2, 4);
 } catch (OutOfRangeException e) {
 fail("Expected OutOfRangeException, but got: " + e.getMessage());
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountWithOutOfRangeCoord2() throws OutOfRangeException, DimensionMismatchException {
 multidimensionalCounter = new MultidimensionalCounter(2, 3);
 try {
 multidimensionalCounter.getCount(1, 4, 0);
 } catch (OutOfRangeException e) {
 fail("Expected OutOfRangeException, but got: " + e.getMessage());
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithInvalidDimensionCount() throws OutOfRangeException, DimensionMismatchException {
 multidimensionalCounter = new MultidimensionalCounter(2, 3);
 try {
 multidimensionalCounter.getCount(new int[]{1, 2, 4});
 } catch (DimensionMismatchException e) {
 fail("Expected DimensionMismatchException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testToString() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(2, 3);
 String result = multidimensionalCounter.toString();
 assertEquals("[0][0]", result);
 multidimensionalCounter.next();
 result = multidimensionalCounter.toString();
 assertEquals("[0][1]", result);
 }

 @Test
 public void testIteratorHasNext() throws OutOfRangeException {
 multidimensionalCounter = new MultidimensionalCounter(2, 3);
 Iterator iterator = multidimensionalCounter.iterator();
 assertFalse(iterator.hasNext());
 multidimensionalCounter.next();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() throws OutOfRangeException {
 multidimensionalCounter = new MultidimensionalCounter(2, 3);
 Iterator iterator = multidimensionalCounter.iterator();
 assertEquals(-1, iterator.next());
 iterator.next();
 assertEquals(0, iterator.next());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(2, 3);
 Iterator iterator = multidimensionalCounter.iterator();
 assertEquals(-1, iterator.getCount());
 multidimensionalCounter.next();
 iterator.next();
 assertEquals(0, iterator.getCount());
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(2, 3);
 Iterator iterator = multidimensionalCounter.iterator();
 assertArrayEquals(new int[]{-1, -1, -1}, iterator.getCounts());
 multidimensionalCounter.next();
 iterator.next();
 assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
 }

 @Test
 public void testIteratorGetCountWithValidDim() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(2, 3);
 Iterator iterator = multidimensionalCounter.iterator();
 assertEquals(-1, iterator.getCount(0));
 multidimensionalCounter.next();
 iterator.next();
 assertEquals(0, iterator.getCount(0));
 }

 @Test
 public void testIteratorRemove() throws UnsupportedOperationException {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(2, 3);
 Iterator iterator = multidimensionalCounter.iterator();
 try {
 iterator.remove();
 } catch (UnsupportedOperationException e) {
 fail("Expected UnsupportedOperationException, but got: " + e.getMessage());
 }
 }
}
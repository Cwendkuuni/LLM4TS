package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;

public class MultidimensionalCounterTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorThrowsExceptionWhenSizeIsNotPositive() {
 try {
 new MultidimensionalCounter(new int[] {-1, 2, 3});
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 assertEquals(3, counter.getDimension());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 try {
 counter.getCounts(-1);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected exception
 }

 try {
 counter.getCounts(100);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetCountsValidIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 int[] counts = counter.getCounts(5);
 assertArrayEquals(new int[] {1, 2, 3}, counts);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithInvalidDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 try {
 counter.getCount(new int[] {5, 5, 5});
 fail("Expected DimensionMismatchException");
 } catch (DimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetCountValidIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 int count = counter.getCount(new int[] {1, 2, 3});
 assertEquals(6, count);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 try {
 counter.getCount(new int[] {-1, 0, 2});
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected exception
 }

 try {
 counter.getCount(new int[] {2, 3, 5});
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetSize() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 assertEquals(2 * 3 * 4, counter.getSize());
 }

 @Test
 public void testGetSizes() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 int[] sizes = counter.getSizes();
 assertArrayEquals(new int[] {2, 3, 4}, sizes);
 }

 @Test
 public void testToString() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 assertEquals("[0][0][0]", counter.toString());

 counter.next();
 counter.next();
 assertEquals("[0][0][1]", counter.toString());
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
Iterator iterator = counter.iterator();
 assertTrue(iterator.hasNext());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 Iterator iterator = counter.iterator();
 assertEquals(0, iterator.next().intValue());
 assertEquals(1, iterator.next().intValue());
 assertEquals(2, iterator.next().intValue());
 assertEquals(3, iterator.next().intValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 Iterator iterator = counter.iterator();
 assertEquals(0, iterator.getCount());
 assertEquals(1, iterator.next().intValue());
 assertEquals(1, iterator.getCount());
 assertEquals(2, iterator.next().intValue());
 assertEquals(2, iterator.getCount());
 assertEquals(3, iterator.next().intValue());
 assertEquals(3, iterator.getCount());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 Iterator iterator = counter.iterator();
 int[] counts = iterator.getCounts();
 assertArrayEquals(new int[] {0, 0, 0}, counts);
 assertEquals(1, iterator.next().intValue());
 counts = iterator.getCounts();
 assertArrayEquals(new int[] {0, 0, 1}, counts);
 assertEquals(2, iterator.next().intValue());
 counts = iterator.getCounts();
 assertArrayEquals(new int[] {0, 1, 1}, counts);
 assertEquals(3, iterator.next().intValue());
 counts = iterator.getCounts();
 assertArrayEquals(new int[] {0, 1, 2}, counts);
 assertEquals(4, iterator.next().intValue());
 counts = iterator.getCounts();
 assertArrayEquals(new int[] {1, 1, 2}, counts);
 assertFalse(iterator.hasNext());
 }
}
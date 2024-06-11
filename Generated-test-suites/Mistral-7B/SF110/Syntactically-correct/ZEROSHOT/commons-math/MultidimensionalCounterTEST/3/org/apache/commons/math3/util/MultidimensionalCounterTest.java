package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class MultidimensionalCounterTest {

 @Test
 public void testMultidimensionalCounterConstruction() {
 MultidimensionalCounter mdc = null;
 try {
 mdc = new MultidimensionalCounter(new int[]{-1, 3, 3});
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 mdc = new MultidimensionalCounter(new int[]{0, 3, 3});
 fail("Expected NotStrictlyPositiveException");

 int[] sizes = new int[]{2, 3, 3};
 MultidimensionalCounter mdc1 = new MultidimensionalCounter(sizes);
 assertEquals(3, mdc1.getDimension());
 assertArrayEquals(sizes, mdc1.getSizes());
 assertEquals(216, mdc1.getSize());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 mdc.getCounts(-1);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange2() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 mdc.getCounts(217);
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 int[] index = new int[]{0, 1, 2};
 int[] result = mdc.getCounts(0);
 assertArrayEquals(new int[]{0, 1, 2}, result);

 result = mdc.getCounts(125);
 assertArrayEquals(new int[]{2, 1, 2}, result);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 mdc.getCounts(new int[]{4, 3, 3});
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 int[] c = new int[]{0, 1, 2};
 int result = mdc.getCount(c);
 assertEquals(5, result);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 int[] c = new int[]{-1, 1, 2};
 mdc.getCount(c);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongDimension2() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 int[] c = new int[]{0, 4, 2};
 mdc.getCount(c);
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 MultidimensionalCounter.Iterator iterator = mdc.iterator();
 assertTrue(iterator.hasNext());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.hasNext());
 // ... and so on
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 MultidimensionalCounter.Iterator iterator = mdc.iterator();
 assertEquals(-1, iterator.next());
 assertEquals(0, iterator.next());
 assertEquals(1, iterator.next());
 // ... and so on
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 MultidimensionalCounter.Iterator iterator = mdc.iterator();
 assertEquals(-1, iterator.getCount());
 assertEquals(0, iterator.getCount());
 assertEquals(1, iterator.getCount());
 // ... and so on
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 MultidimensionalCounter.Iterator iterator = mdc.iterator();
 int[] counts = iterator.getCounts();
 assertArrayEquals(new int[]{0, 0, 0}, counts);
 counts = iterator.getCounts();
 assertArrayEquals(new int[]{0, 1, 2}, counts);
 // ... and so on
 }

 @Test
 public void testIteratorGetCountWithWrongDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 3});
 MultidimensionalCounter.Iterator iterator = mdc.iterator();
 int[] counts = iterator.getCounts();
 iterator.getCount(4);
 fail("Expected UnsupportedOperationException");
 }

}
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;

 @Before
 public void setUp() {
 try {
 counter = new MultidimensionalCounter(new int[] { 2, 3, 3 });
 } catch (NotStrictlyPositiveException e) {
 e.printStackTrace();
 fail("Unexpected NotStrictlyPositiveException");
 }
 }

 @Test
 public void testGetDimension() {
 assertEquals(3, counter.getDimension());
 }

 @Test
 public void testGetTotalSize() {
 assertEquals(18, counter.getSize());
 }

 @Test
 public void testGetSizes() {
 int[] expectedSizes = new int[] { 2, 3, 3 };
 assertArrayEquals(expectedSizes, counter.getSizes());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 counter.getCounts(18);
 }

 @Test
 public void testGetCountsZeroDim() {
 int[] actual = counter.getCounts(0);
 int[] expected = new int[] { 0, 0, 0 };
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetCountsLastDim() {
 int[] actual = counter.getCounts(17);
 int[] expected = new int[] { 1, 1, 2 };
 assertArrayEquals(expected, actual);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongDimension() {
 counter = new MultidimensionalCounter(new int[] { 2, 3, 3, 1 });
 counter.getCounts(0);
 }

 @Test
 public void testGetCount() {
 int[] actual = counter.getCounts(3);
 int expected = 3 * 3 + 2;
 assertEquals(expected, counter.getCount(actual));
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 counter = new MultidimensionalCounter(new int[] { 2, 3, 3 });
 counter.getCount(new int[] { 5, 4, 0 });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongDimension2() {
 counter = new MultidimensionalCounter(new int[] { 2, 3, 3 });
 counter.getCount(new int[] { 3, 4 });
 }

 @Test
 public void testIteratorHasNext() {
 Iterator iterator = counter.iterator();
 for (int i = 0; i < 18; ++i) {
 assertTrue(iterator.hasNext());
 }
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 Iterator iterator = counter.iterator();
 for (int i = 0; i < 18; ++i) {
 int actual = iterator.next();
 int expected = i;
 assertEquals(expected, actual);
 }
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorGetCount() {
 Iterator iterator = counter.iterator();
 assertEquals(-1, iterator.getCount());
 for (int i = 0; i < 18; ++i) {
 int actual = iterator.next();
 int expected = i;
 assertEquals(expected, actual);
 int actualCount = iterator.getCount();
 assertEquals(i + 1, actualCount);
 }
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorGetCounts() {
 Iterator iterator = counter.iterator();
 assertEquals(new int[] { -1, -1, -1 }, iterator.getCounts());
 for (int i = 0; i < 18; ++i) {
 int actual = iterator.next();
 int[] actualCounts = iterator.getCounts();
 int[] expected = new int[] { i / 3, i % 3, i % 3 };
 assertArrayEquals(expected, actualCounts);
 }
 assertFalse(iterator.hasNext());
 }
}
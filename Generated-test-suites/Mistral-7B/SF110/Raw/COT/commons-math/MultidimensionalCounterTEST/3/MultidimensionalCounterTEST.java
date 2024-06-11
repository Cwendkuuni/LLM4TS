package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter multidimensionalCounter;

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeSize() {
 multidimensionalCounter = new MultidimensionalCounter(-1);
 }

 @Test
 public void testGetDimension() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 assertEquals(2, multidimensionalCounter.getDimension());
 }

 @Test
 public void testGetCountsWithValidIndex() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 int[] expected = new int[] { 0, 1 };
 int[] actual = multidimensionalCounter.getCounts(1);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsWithOutOfRangeIndex() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 multidimensionalCounter.getCounts(3);
 }

 @Test
 public void testGetCountWithValidCount() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 int expected = 2 * 3;
 int actual = multidimensionalCounter.getCount(new int[] { 0, 2 });
 assertEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountWithNegativeCount() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 multidimensionalCounter.getCount(new int[] { -1, 2 });
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountWithSizeOutOfRange() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 multidimensionalCounter.getCount(new int[] { 2, 4 });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountWithInvalidDimension() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 multidimensionalCounter.getCount(new int[] { 2, 2 });
 }

 @Test
 public void testGetSize() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 int expected = 2 * 3 * 3;
 int actual = multidimensionalCounter.getSize();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSizes() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 int[] expected = new int[] { 2, 3 };
 int[] actual = multidimensionalCounter.getSizes();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testToString() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 String expected = "[0][1][2]";
 String actual = multidimensionalCounter.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testIteratorHasNext() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 Iterator iterator = multidimensionalCounter.iterator();
 assertTrue(iterator.hasNext());
 iterator.next();
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 Iterator iterator = multidimensionalCounter.iterator();
 int expected = 0;
 int actual = iterator.next();
 assertEquals(expected, actual);
 expected = 1;
 actual = iterator.next();
 assertEquals(expected, actual);
 expected = 2;
 actual = iterator.next();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorGetCount() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 Iterator iterator = multidimensionalCounter.iterator();
 int expected = 0;
 int actual = iterator.getCount();
 assertEquals(expected, actual);
 iterator.next();
 iterator.next();
 expected = 2;
 actual = iterator.getCount();
 assertEquals(expected, actual);
 }

 @Test
 public void testIteratorGetCounts() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 Iterator iterator = multidimensionalCounter.iterator();
 int[] expected = new int[] { 0, 1 };
 int[] actual = iterator.getCounts();
 assertArrayEquals(expected, actual);
 iterator.next();
 iterator.next();
 expected = new int[] { 0, 2 };
 actual = iterator.getCounts();
 assertArrayEquals(expected, actual);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorGetCountWithDimesion() {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3 });
 Iterator iterator = multidimensionalCounter.iterator();
 int expected = 0;
 int actual = iterator.getCount(0);
 assertEquals(expected, actual);
 iterator.next();
 iterator.next();
 expected = 1;
 actual = iterator.getCount(0);
 assertEquals(expected, actual);
 }
}
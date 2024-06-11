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
 public void testConstructorNegativeSizes() {
 MultidimensionalCounter multidimensionalCounter = null;
 try {
 multidimensionalCounter = new MultidimensionalCounter(-1, -1);
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 assertNull(multidimensionalCounter);
 }

 @Test
 public void testConstructorValidSizes() {
 int[] size = new int[] { 2, 3, 4 };
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
 assertEquals(3, multidimensionalCounter.getDimension());
 assertArrayEquals(size, multidimensionalCounter.getSizes());
 assertEquals(2 * 3 * 4, multidimensionalCounter.getSize());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsNegativeIndex() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int index = -1;
 try {
 multidimensionalCounter.getCounts(index);
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetCountsValidIndex() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int index = 12;
 int[] expected = new int[] { 2, 3, 3 };
 int[] actual = multidimensionalCounter.getCounts(index);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountNegativeIndex() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int[] c = new int[] { -1, 1, 2 };
 try {
 multidimensionalCounter.getCount(c);
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountInvalidDimension() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int[] c = new int[] { 2, 5, 4 };
 try {
 multidimensionalCounter.getCount(c);
 } catch (DimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetCountValidIndex() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int[] c = new int[] { 1, 1, 2 };
 int expected = 1 * 1 * (3 * 2 + 1) + 1 * 2;
 int actual = multidimensionalCounter.getCount(c);
 assertEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfBoundsIndex() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int index = multidimensionalCounter.getSize();
 try {
 multidimensionalCounter.getCounts(index);
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 assertTrue(iterator.hasNext());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 int expected = 0;
 int actual = iterator.next();
 assertEquals(expected, actual);
 expected = 1;
 actual = iterator.next();
 assertEquals(expected, actual);
 expected = 2;
 actual = iterator.next();
 assertEquals(expected, actual);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testToString() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 String expected = "[0][0][0]";
 String actual = multidimensionalCounter.toString();
 assertEquals(expected, actual);
 }
}
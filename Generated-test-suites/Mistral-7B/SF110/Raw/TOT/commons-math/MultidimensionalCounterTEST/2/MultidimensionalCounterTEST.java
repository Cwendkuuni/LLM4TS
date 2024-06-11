package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorNegativeSize() {
 try {
 new MultidimensionalCounter(new int[] {-1, 2, 3});
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }

 @Test
 public void testConstructorValidSize() {
 int[] sizes = new int[] {2, 3, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 assertEquals(2, counter.getDimension());
 assertArrayEquals(sizes, counter.getSizes());
 assertEquals(2 * 3 * 3, counter.getSize());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 3});
 try {
 counter.getCounts(-1);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfTotalSize() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 3});
 try {
 counter.getCounts(counter.getSize() + 1);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 3});
 try {
 counter.getCounts(new int[] {2, 4});
 fail("Expected DimensionMismatchException");
 } catch (DimensionMismatchException e) {
 // Expected
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 3});
 try {
 counter.getCount(new int[] {2, 2, 10});
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongDimension2() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 3});
 try {
 counter.getCount(new int[] {3, 2});
 fail("Expected DimensionMismatchException");
 } catch (DimensionMismatchException e) {
 // Expected
 }
 }

 @Test
 public void testGetCountValidIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 3});
 int[] index = new int[] {1, 1, 1};
 int result = counter.getCount(index);
 assertEquals(2 * 3 * 3 + 1 + 1 + 1, result);
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 3});
 MultidimensionalCounter.Iterator it = counter.iterator();
 assertTrue(it.hasNext());
 assertTrue(it.hasNext());
 assertTrue(it.hasNext());
 assertFalse(it.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 3});
 MultidimensionalCounter.Iterator it = counter.iterator();
 int expected = 0;
 while (it.hasNext()) {
 Integer next = it.next();
 assertEquals(expected++, next.intValue());
 }
 assertFalse(it.hasNext());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 3});
 MultidimensionalCounter.Iterator it = counter.iterator();
 it.next();
 it.next();
 assertEquals(2, it.getCount());
 it.next();
 assertEquals(3, it.getCount());
 it.next();
 assertEquals(3, it.getCount());
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 3});
 MultidimensionalCounter.Iterator it = counter.iterator();
 it.next();
 it.next();
 int[] counts = it.getCounts();
 assertArrayEquals(new int[] {1, 1, 2}, counts);
 }
}
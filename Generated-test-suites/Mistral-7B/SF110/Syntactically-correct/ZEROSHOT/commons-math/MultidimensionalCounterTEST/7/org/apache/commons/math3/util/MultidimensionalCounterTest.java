package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Arrays.stream;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;

 @Test
 public void testConstructor() {
 int[] size = new int[] {2, 3, 4};
 try {
 counter = new MultidimensionalCounter(size);
 } catch (NotStrictlyPositiveException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 assertEquals(3, counter.getDimension());
 assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
 assertEquals(24, counter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSize() {
 int[] size = new int[] {-1, 3, 4};
 try {
 new MultidimensionalCounter(size);
 } catch (NotStrictlyPositiveException e) {
 assertEquals(-1, e.getValue());
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testZeroSize() {
 int[] size = new int[] {2, 0, 4};
 try {
 new MultidimensionalCounter(size);
 } catch (NotStrictlyPositiveException e) {
 assertEquals(0, e.getValue());
 }
 }

 @Test
 public void testGetDimension() {
 int[] size = new int[] {2, 3, 4};
 counter = new MultidimensionalCounter(size);
 assertEquals(3, counter.getDimension());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 int[] size = new int[] {2, 3, 4};
 counter = new MultidimensionalCounter(size);
 try {
 counter.getCounts(24);
 } catch (OutOfRangeException e) {
 assertEquals(24, e.getIndex());
 }
 }

 @Test
 public void testGetCounts() {
 int[] size = new int[] {2, 3, 4};
 int index = 0;
 counter = new MultidimensionalCounter(size);
 int[] counts = counter.getCounts(index);
 assertEquals(3, counts.length);
 assertArrayEquals(new int[] {0, 0, index}, counts);
 }

 @Test
 public void testGetCount() {
 int[] size = new int[] {2, 3, 4};
 int[] c = new int[] {1, 2, 3};
 counter = new MultidimensionalCounter(size);
 int count = counter.getCount(c);
 assertEquals(6, count);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountDimensionMismatch() {
 int[] size = new int[] {2, 3, 4};
 int[] c = new int[] {1, 2, 5};
 counter = new MultidimensionalCounter(size);
 try {
 counter.getCount(c);
 } catch (DimensionMismatchException e) {
 assertEquals(3, e.getExpectedDimension());
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 int[] size = new int[] {2, 3, 4};
 int[] c = new int[] {2, 2, 4};
 counter = new MultidimensionalCounter(size);
 try {
 counter.getCount(c);
 } catch (OutOfRangeException e) {
 assertEquals(3, e.getIndex());
 }
 }

 @Test
 public void testIteratorHasNext() {
 int[] size = new int[] {2, 3, 4};
 counter = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = counter.iterator();
 assertFalse(it.hasNext());
 it.next();
 assertTrue(it.hasNext());
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 assertFalse(it.hasNext());
 }

 @Test
 public void testIteratorNext() {
 int[] size = new int[] {2, 3, 4};
 counter = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = counter.iterator();
 assertEquals(-1, it.next());
 assertEquals(0, it.getCount());
 assertEquals(new int[] {0, 0, 0}, it.getCounts());
 it.next();
 assertEquals(0, it.getCount());
 assertEquals(new int[] {1, 0, 0}, it.getCounts());
 it.next();
 assertEquals(1, it.getCount());
 assertEquals(new int[] {1, 1, 0}, it.getCounts());
 it.next();
 assertEquals(2, it.getCount());
 assertEquals(new int[] {1, 1, 1}, it.getCounts());
 it.next();
 assertEquals(3, it.getCount());
 assertEquals(new int[] {1, 1, 2}, it.getCounts());
 it.next();
 assertEquals(4, it.getCount());
 assertEquals(new int[] {1, 1, 3}, it.getCounts());
 it.next();
 assertEquals(5, it.getCount());
 assertEquals(new int[] {1, 1, 4}, it.getCounts());
 it.next();
 assertEquals(6, it.getCount());
 assertEquals(new int[] {1, 2, 0}, it.getCounts());
 it.next();
 assertEquals(7, it.getCount());
 assertEquals(new int[] {1, 2, 1}, it.getCounts());
 it.next();
 assertEquals(8, it.getCount());
 assertEquals(new int[] {1, 2, 2}, it.getCounts());
 it.next();
 assertEquals(9, it.getCount());
 assertEquals(new int[] {1, 2, 3}, it.getCounts());
 it.next();
 assertEquals(10, it.getCount());
 assertEquals(new int[] {1, 3, 0}, it.getCounts());
 it.next();
 assertEquals(11, it.getCount());
 assertEquals(new int[] {1, 3, 1}, it.getCounts());
 it.next();
 assertEquals(12, it.getCount());
 assertEquals(new int[] {1, 3, 2}, it.getCounts());
 it.next();
 assertEquals(13, it.getCount());
 assertEquals(new int[] {1, 3, 3}, it.getCounts());
 it.next();
 assertEquals(14, it.getCount());
 assertEquals(new int[] {1, 2, 3}, it.getCounts());
 it.next();
 assertEquals(15, it.getCount());
 assertEquals(new int[] {2, 0, 0}, it.getCounts());
 it.next();
 assertEquals(16, it.getCount());
 assertEquals(new int[] {2, 0, 1}, it.getCounts());
 it.next();
 assertEquals(17, it.getCount());
 assertEquals(new int[] {2, 0, 2}, it.getCounts());
 it.next();
 assertEquals(18, it.getCount());
 assertEquals(new int[] {2, 0, 3}, it.getCounts());
 it.next();
 assertEquals(19, it.getCount());
 assertEquals(new int[] {2, 1, 0}, it.getCounts());
 it.next();
 assertEquals(20, it.getCount());
 assertEquals(new int[] {2, 1, 1}, it.getCounts());
 it.next();
 assertEquals(21, it.getCount());
 assertEquals(new int[] {2, 1, 2}, it.getCounts());
 it.next();
 assertEquals(22, it.getCount());
 assertEquals(new int[] {2, 1, 3}, it.getCounts());
 it.next();
 assertEquals(23, it.getCount());
 assertEquals(new int[] {2, 2, 0}, it.getCounts());
 it.next();
 assertEquals(24, it.getCount());
 assertEquals(new int[] {2, 2, 1}, it.getCounts());
 it.next();
 assertFalse(it.hasNext());
 }

 @Test
 public void testToString() {
 int[] size = new int[] {2, 3, 4};
 counter = new MultidimensionalCounter(size);
 assertEquals("[0][0][0]", counter.toString());
 MultidimensionalCounter.Iterator it = counter.iterator();
 it.next();
 assertEquals("[0][0][1]", counter.toString());
 it.next();
 assertEquals("[0][1][0]", counter.toString());
 it.next();
 assertEquals("[0][1][1]", counter.toString());
 it.next();
 assertEquals("[1][0][0]", counter.toString());
 it.next();
 assertEquals("[1][0][1]", counter.toString());
 it.next();
 assertEquals("[1][1][0]", counter.toString());
 it.next();
 assertEquals("[1][1][1]", counter.toString());
 it.next();
 assertEquals("[2][0][0]", counter.toString());
 it.next();
 assertEquals("[2][0][1]", counter.toString());
 it.next();
 assertEquals("[2][0][2]", counter.toString());
 it.next();
 assertEquals("[2][0][3]", counter.toString());
 it.next();
 assertEquals("[2][1][0]", counter.toString());
 it.next();
 assertEquals("[2][1][1]", counter.toString());
 it.next();
 assertEquals("[2][1][2]", counter.toString());
 it.next();
 assertEquals("[2][1][3]", counter.toString());
 it.next();
 assertEquals("[2][2][0]", counter.toString());
 it.next();
 assertEquals("[2][2][1]", counter.toString());
 it.next();
 assertEquals("[2][2][2]", counter.toString());
 it.next();
 assertEquals("[2][2][3]", counter.toString());
 it.next();
 assertEquals("[3][0][0]", counter.toString());
 it.next();
 assertEquals("[3][0][1]", counter.toString());
 it.next();
 assertEquals("[3][0][2]", counter.toString());
 it.next();
 assertEquals("[3][0][3]", counter.toString());
 it.next();
 assertEquals("[3][1][0]", counter.toString());
 it.next();
 assertEquals("[3][1][1]", counter.toString());
 it.next();
 assertEquals("[3][1][2]", counter.toString());
 it.next();
 assertEquals("[3][1][3]", counter.toString());
 it.next();
 assertEquals("[3][2][0]", counter.toString());
 it.next();
 assertEquals("[3][2][1]", counter.toString());
 it.next();
 assertEquals("[3][2][2]", counter.toString());
 it.next();
 assertEquals("[3][2][3]", counter.toString());
 it.next();
 assertEquals("[3][3][0]", counter.toString());
 it.next();
 assertEquals("[3][3][1]", counter.toString());
 it.next();
 assertEquals("[3][3][2]", counter.toString());
 it.next();
 assertEquals("[3][3][3]", counter.toString());
 }
}
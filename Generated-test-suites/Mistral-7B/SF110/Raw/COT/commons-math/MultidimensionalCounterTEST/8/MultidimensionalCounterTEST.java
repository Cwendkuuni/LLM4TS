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
 public void testConstructorWithNegativeSizeThrowsException() {
    int[] size = {-1, 2};
    try {
        multidimensionalCounter = new MultidimensionalCounter(size);
        fail("Expected NotStrictlyPositiveException");
    } catch (NotStrictlyPositiveException e) {
        // Expected
    }
 }

 @Test
 public void testGetDimension() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    int dimension = multidimensionalCounter.getDimension();
    assertEquals(3, dimension);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsWithNegativeIndexThrowsException() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    int index = -1;
    try {
        multidimensionalCounter.getCounts(index);
        fail("Expected OutOfRangeException");
    } catch (OutOfRangeException e) {
        // Expected
    }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsWithIndexOutOfRangeThrowsException() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    int index = multidimensionalCounter.getTotalSize();
    try {
        multidimensionalCounter.getCounts(index);
        fail("Expected OutOfRangeException");
    } catch (OutOfRangeException e) {
        // Expected
    }
 }

 @Test
 public void testGetCounts() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    int index = 5;
    int[] expected = {0, 1, 2};
    int[] actual = multidimensionalCounter.getCounts(index);
    assertArrayEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountWithNegativeIndexThrowsException() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    int[] coordinates = {-1, 1, 2};
    try {
        multidimensionalCounter.getCount(coordinates);
        fail("Expected OutOfRangeException");
    } catch (OutOfRangeException e) {
        // Expected
    }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountWithCoordinateOutOfRangeThrowsException() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    int[] coordinates = {1, 3, 2};
    try {
        multidimensionalCounter.getCount(coordinates);
        fail("Expected OutOfRangeException");
    } catch (OutOfRangeException e) {
        // Expected
    }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountWithDifferentDimensionThrowsException() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    int[] coordinates = {1, 2, 4};
    try {
        multidimensionalCounter.getCount(coordinates);
        fail("Expected DimensionMismatchException");
    } catch (DimensionMismatchException e) {
        // Expected
    }
 }

 @Test
 public void testGetCount() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    int[] coordinates = {1, 1, 1};
    int expected = 1;
    int actual = multidimensionalCounter.getCount(coordinates);
    assertEquals(expected, actual);
 }

 @Test
 public void testGetSize() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    int expected = 1 * 2 * 3;
    int actual = multidimensionalCounter.getSize();
    assertEquals(expected, actual);
 }

 @Test
 public void testGetSizes() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    int[] expected = {1, 2, 3};
    int[] actual = multidimensionalCounter.getSizes();
    assertArrayEquals(expected, actual);
 }

 @Test
 public void testToString() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    String expected = "[1][1][1]";
    String actual = multidimensionalCounter.toString();
    assertEquals(expected, actual);
 }

 @Test
 public void testIteratorHasNext() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    Iterator iterator = multidimensionalCounter.iterator();
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    Iterator iterator = multidimensionalCounter.iterator();
    assertEquals(-1, iterator.next());
    assertEquals(0, iterator.next());
    assertEquals(1, iterator.next());
    assertEquals(2, iterator.next());
    assertEquals(3, iterator.next());
    assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorGetCount() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    Iterator iterator = multidimensionalCounter.iterator();
    assertEquals(-1, iterator.getCount());
    assertEquals(0, iterator.getCount());
    assertEquals(1, iterator.getCount());
    assertEquals(2, iterator.getCount());
    assertEquals(3, iterator.getCount());
    assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorGetCounts() {
    int[] size = {1, 2, 3};
    multidimensionalCounter = new MultidimensionalCounter(size);
    Iterator iterator = multidimensionalCounter.iterator();
    int[] expected = {-1, 0, 1};
    int[] actual = iterator.getCounts();
    assertArrayEquals(expected, actual);
 }

}
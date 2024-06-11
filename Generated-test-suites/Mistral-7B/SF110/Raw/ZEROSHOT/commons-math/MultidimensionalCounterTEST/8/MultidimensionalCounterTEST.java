package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MultidimensionalCounterTest {

  @Test
  public void testMultidimensionalCounterConstruction() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    assertEquals(3, counter.getDimension());
    assertArrayEquals(size, counter.getSizes());
    assertEquals(2 * 3 * 4, counter.getSize());
    assertArrayEquals(new int[0], counter.getCounts(Integer.MIN_VALUE));
    assertArrayEquals(new int[0], counter.getCounts(counter.getSize() - 1));
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testMultidimensionalCounterNegativeSize() {
    int[] size = {-1, 2, 3};
    new MultidimensionalCounter(size);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testMultidimensionalCounterInvalidTotalSize() {
    int[] size = {2, 3, 4};
    new MultidimensionalCounter(size, 5 * 6 * 7 - (2 * 3 * 4));
  }

  @Test
  public void testMultidimensionalCounterGetCount() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    int[] testCount = {1, 2, 3};
    int count = counter.getCount(testCount);
    assertEquals(testCount[0] * size[0] + testCount[1] * size[1] + testCount[2] * size[2], count);
  }

  @Test(expected = OutOfRangeException.class)
  public void testMultidimensionalCounterGetCountOutOfRange() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    int[] outOfRangeCount = {4, 5, 6};
    counter.getCount(outOfRangeCount);
  }

  @Test(expected = DimensionMismatchException.class)
  public void testMultidimensionalCounterGetCountDimensionMismatch() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    int[] dimensionMismatchCount = {1, 4, 5};
    counter.getCount(dimensionMismatchCount);
  }

  @Test
  public void testMultidimensionalCounterIterator() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    Iterator<Integer> iterator = counter.iterator();
    List<Integer> expectedList = Arrays.asList(0, 3, 3 * 2, 3 * 2 * 2, 3 * 2 * 2 * 2);
    List<Integer> actualList = new ArrayList<>();
    while (iterator.hasNext()) {
      actualList.add(iterator.next());
    }
    assertEquals(expectedList, actualList);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testMultidimensionalCounterIteratorRemove() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    Iterator<Integer> iterator = counter.iterator();
    iterator.next();
    iterator.remove();
  }

  @Test
  public void testMultidimensionalCounterGetCounts() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    int index = 3 * 2 * 2 + 1;
    int[] expectedCounts = {1, 1, 2};
    int[] actualCounts = counter.getCounts(index);
    assertArrayEquals(expectedCounts, actualCounts);
  }

  @Test(expected = OutOfRangeException.class)
  public void testMultidimensionalCounterGetCountsOutOfRange() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    int outOfRangeIndex = size[0] * size[1] * size[2];
    counter.getCounts(outOfRangeIndex);
  }

}
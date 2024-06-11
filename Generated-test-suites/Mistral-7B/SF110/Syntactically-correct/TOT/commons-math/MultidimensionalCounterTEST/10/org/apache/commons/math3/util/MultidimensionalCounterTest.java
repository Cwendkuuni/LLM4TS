package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

 private final int[] size;

 public MultidimensionalCounterTest(int... size) {
 this.size = size;
 }

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 new Object[]{new int[]{3, 2, 1}},
 new Object[]{new int[]{2, 2, 2}},
 new Object[]{new int[]{1, 1, 1, 1}}
 });
 }

 @Test
 public void testConstructor() {
 try {
 MultidimensionalCounter counter = new MultidimensionalCounter(this.size);
 assertEquals(this.size.length, counter.getDimension());
 } catch (NotStrictlyPositiveException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInvalidConstructor() {
 int[] invalidSize = new int[]{-1, 2, 3};
 try {
 MultidimensionalCounter counter = new MultidimensionalCounter(invalidSize);
 } catch (NotStrictlyPositiveException e) {
 assertEquals(-1, invalidSize[0]);
 }
 }

 @Test
 public void testGetSizes() {
 MultidimensionalCounter counter = new MultidimensionalCounter(this.size);
 assertArrayEquals(this.size, counter.getSizes());
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(this.size);
 assertEquals(this.size.length, counter.getDimension());
 }

 @Test
 public void testGetTotalSize() {
 MultidimensionalCounter counter = new MultidimensionalCounter(this.size);
 assertEquals(Arrays.stream(this.size).parallel().reduce(1, (a, b) -> a * b), counter.getSize());
 }

 @Test
 public void testGetIterator() {
 MultidimensionalCounter counter = new MultidimensionalCounter(this.size);
 Iterator<Integer> it = counter.iterator();
 List<Integer> list = new ArrayList<>();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertFalse(it.hasNext());
 assertEquals(Arrays.asList(Arrays.stream(this.size).map(i -> Integer.valueOf(i) - 1).parallel().boxed().toList()), list);
 }

 @Test
 public void testGetCountsIndexOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 try {
 counter.getCounts(-1);
 fail();
 } catch (OutOfRangeException e) {
 // expected
 }
 }

 @Test
 public void testGetCountsValidIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int[] expected = new int[]{0, 0, 0};
 int[] actual = counter.getCounts(0);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetCountsMultipleDimensions() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int[] expected = new int[]{0, 0, 2};
 int[] actual = counter.getCounts(counter.getTotalSize() - 1);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithInvalidDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 counter.getCounts(3);
 }

 @Test
 public void testGetCountValidIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int count = counter.getCount(new int[]{2, 1, 0});
 assertEquals(2 * counter.uniCounterOffset[0] + 1 * counter.uniCounterOffset[1] + 0 * counter.uniCounterOffset[2], count);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountInvalidIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 counter.getCount(new int[]{-1, 2, 1});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithInvalidDimensionCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 counter.getCount(new int[]{2, 3, 1});
 }
}
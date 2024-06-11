package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class MultidimensionalCounterTest {

 @Test
 public void testConstructorWithValidSizes() throws NotStrictlyPositiveException {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 assertEquals(3, counter.getDimension());
 int[] sizes = counter.getSizes();
 assertEquals(3, sizes.length);
 assertEquals(1, sizes[0]);
 assertEquals(2, sizes[1]);
 assertEquals(3, sizes[2]);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeSizes() throws NotStrictlyPositiveException {
 MultidimensionalCounter counter = null;
 try {
 counter = new MultidimensionalCounter(-1, 2, 3);
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 assertNull(counter);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroSizes() throws NotStrictlyPositiveException {
 MultidimensionalCounter counter = null;
 try {
 counter = new MultidimensionalCounter(0, 2, 3);
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 assertNull(counter);
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 assertEquals(3, counter.getDimension());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsNegativeIndex() throws OutOfRangeException {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 counter.getCounts(-1);
 fail("OutOfRangeException expected but not thrown");
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRangeIndex() throws OutOfRangeException {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] index = new int[counter.getDimension()];
 index[counter.getDimension() - 1] = 10;
 counter.getCounts(Arrays.toString(index));
 fail("OutOfRangeException expected but not thrown");
 }

 @Test
 public void testGetCountsValidIndex() throws OutOfRangeException {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] index = new int[counter.getDimension()];
 index[0] = 1;
 index[1] = 2;
 index[2] = 3;
 int[] expected = new int[]{1, 2, 3};
 assertArrayEquals(expected, counter.getCounts(Arrays.toString(index)));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithInvalidDimension() throws DimensionMismatchException {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] invalidDimension = new int[]{2, 2};
 counter.getCounts(Arrays.toString(invalidDimension));
 fail("DimensionMismatchException expected but not thrown");
 }

 @Test
 public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] index = new int[]{1, 2, 3};
 int expected = 3 * 2 * 1 + 2 * 1 + 1;
 assertEquals(expected, counter.getCount(index));
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountNegativeIndex() throws OutOfRangeException {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] index = new int[]{-1, 2, 3};
 counter.getCount(index);
 fail("OutOfRangeException expected but not thrown");
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRangeIndex() throws OutOfRangeException {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] index = new int[]{1, 3, 3};
 counter.getCount(index);
 fail("OutOfRangeException expected but not thrown");
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithInvalidDimension() throws DimensionMismatchException {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] invalidDimension = new int[]{2, 2, 1};
 counter.getCount(invalidDimension);
 fail("DimensionMismatchException expected but not thrown");
 }

 @Test
 public void testGetSize() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 assertEquals(3 * 2 * 1, counter.getSize());
 }

 @Test
 public void testGetSizes() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] expected = new int[]{1, 2, 3};
 assertArrayEquals(expected, counter.getSizes());
 }

 @Test
 public void testToString() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 String expected = "[3][2][1]";
 assertEquals(expected, counter.toString());
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 boolean expected = true;
 assertTrue(iter.hasNext());
 }

 @Test
 public void testIteratorHasNextAfterNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 iter.next();
 boolean expected = true;
 assertTrue(iter.hasNext());
 }

 @Test
 public void testIteratorHasNoNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 int count = 0;
 while (iter.hasNext()) {
 iter.next();
 ++count;
 }
 boolean expected = false;
 assertTrue(iter.hasNoNext());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 int expected = 0;
 assertEquals(expected, iter.getCount());
 iter.next();
 expected = 1;
 assertEquals(expected, iter.getCount());
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 int[] expected = new int[]{0, 0, 0};
 assertArrayEquals(expected, iter.getCounts());
 iter.next();
 expected = new int[]{1, 0, 0};
 assertArrayEquals(expected, iter.getCounts());
 }

 @Test
 public void testIteratorGetCountsWithInvalidDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 int[] invalidDimension = new int[]{3, 2, 1};
 iter.getCounts(Arrays.toString(invalidDimension));
 fail("Expected IllegalArgumentException but got no exception");
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 int expected = 0;
 assertEquals(expected, iter.next().intValue());
 expected = 1;
 iter.next();
 assertEquals(expected, iter.next().intValue());
 }

 @Test
 public void testIteratorNextAfterLast() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 int expected = 3 * 2 * 1;
 assertEquals(expected, iter.next().intValue());
 iter.next();
 iter.next();
 iter.next();
 assertEquals(-1, iter.next().intValue());
 }

 @Test
 public void testIteratorNextThrowsOutOfRangeException() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 iter.next();
 iter.next();
 iter.next();
 try {
 iter.next();
 } catch (OutOfRangeException e) {
 // Expected
 }
 }

 @Test
 public void testIteratorRemove() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 try {
 iter.remove();
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testIteratorRemoveAfterNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator iter = counter.iterator();
 iter.next();
 iter.remove();
 try {
 iter.next();
 } catch (OutOfRangeException e) {
 // Expected
 }
 }

 @Test
 public void testIterateAllValues() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
 List<Integer> actual = new ArrayList<>();
 Iterator iter = counter.iterator();
 while (iter.hasNext()) {
 actual.add(iter.next().intValue());
 }
 assertEquals(expected, actual);
 }
}
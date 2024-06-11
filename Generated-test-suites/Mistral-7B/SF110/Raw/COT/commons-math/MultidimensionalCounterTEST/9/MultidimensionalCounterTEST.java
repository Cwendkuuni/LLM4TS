package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

 private int[] size = {2, 3, 4};

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeSizes() {
 MultidimensionalCounter mdc = null;
 try {
 mdc = new MultidimensionalCounter(new int[]{-1, 3, 4});
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 assertNull(mdc);
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 assertEquals(3, mdc.getDimension());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsInvalidIndex() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 mdc.getCounts(-1);
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 int[] indices = mdc.getCounts(10);
 assertArrayEquals(new int[]{1, 2, 3}, indices);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithInvalidDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 int[] invalidDimension = new int[4];
 try {
 mdc.getCounts(invalidDimension);
 } catch (DimensionMismatchException e) {
 // expected exception
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountInvalidIndex() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 try {
 mdc.getCount(-1, 0);
 } catch (OutOfRangeException e) {
 // expected exception
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithInvalidDimension2() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 int[] invalidDimension2 = new int[5];
 try {
 mdc.getCount(invalidDimension2);
 } catch (DimensionMismatchException e) {
 // expected exception
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountWithInvalidIndex1() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 try {
 mdc.getCount(-1, 0);
 } catch (OutOfRangeException e) {
 // expected exception
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountWithInvalidIndex2() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 try {
 mdc.getCount(1, -1);
 } catch (OutOfRangeException e) {
 // expected exception
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountWithInvalidIndex3() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 try {
 mdc.getCount(1, size[1] + 1);
 } catch (OutOfRangeException e) {
 // expected exception
 }
 }

 @Test
 public void testGetSize() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 assertEquals(8 * 3 * 4, mdc.getSize());
 }

 @Test
 public void testGetSizes() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 int[] sizes = mdc.getSizes();
 assertArrayEquals(size, sizes);
 }

 @Test
 public void testToString() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 assertEquals("[0][0][0]", mdc.toString());
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 assertTrue(it.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 assertEquals(0, it.next());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 assertEquals(0, it.getCount());
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 int[] counts = it.getCounts();
 assertArrayEquals(new int[]{0, 0, 0}, counts);
 }

 @Test
 public void testIteratorNextMultipleDimensions() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 assertEquals(0, it.next());
 assertEquals(1, it.next());
 assertEquals(1, it.next());
 assertEquals(2, it.next());
 assertEquals(2, it.next());
 assertEquals(3, it.next());
 assertEquals(3, it.next());
 assertEquals(3, it.next());
 assertEquals(4, it.next());
 }

 @Test
 public void testIteratorGetCountMultipleDimensions() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 assertEquals(0, it.getCount());
 assertEquals(1, it.getCount(0));
 assertEquals(1, it.getCount(1));
 assertEquals(1, it.getCount(2));
 }

 @Test
 public void testIteratorGetCountsMultipleDimensions() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 int[] counts = it.getCounts();
 assertArrayEquals(new int[]{0, 0, 0}, counts);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratorRemove() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 it.remove();
 }
}
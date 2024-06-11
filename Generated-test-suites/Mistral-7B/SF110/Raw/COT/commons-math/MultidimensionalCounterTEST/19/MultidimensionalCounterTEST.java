package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

 private int[] testSize = new int[]{2, 3, 4};

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorNegativeSizes() {
 MultidimensionalCounter mdc = null;
 try {
 mdc = new MultidimensionalCounter(new int[]{2, -3, 4});
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 assertNull(mdc);
 }

 @Test
 public void testConstructorPositiveSizes() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 assertEquals(3, mdc.getDimension());
 assertEquals(2, mdc.getSizes()[0]);
 assertEquals(3, mdc.getSizes()[1]);
 assertEquals(4, mdc.getSizes()[2]);
 assertEquals(24, mdc.getSize());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 mdc.getCounts(-1);
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 int[] counts = mdc.getCounts(15);
 assertArrayEquals(new int[]{1, 2, 3}, counts); // the value 15 is just an example
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsDimensionMismatch() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 try {
 mdc.getCounts(new int[]{3, 2});
 } catch (DimensionMismatchException e) {
 // expected exception
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 try {
 mdc.getCount(-1, 0);
 } catch (OutOfRangeException e) {
 // expected exception
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange2() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 try {
 mdc.getCount(new int[]{2, 3, 5});
 } catch (OutOfRangeException e) {
 // expected exception
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountDimensionMismatch() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 try {
 mdc.getCount(new int[]{2, 3});
 } catch (DimensionMismatchException e) {
 // expected exception
 }
 }

 @Test
 public void testToString() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 String str = mdc.toString();
 String expected = "[0][0][0]";
 for (int i = 0; i < mdc.getDimension(); i++) {
 int size = mdc.getSizes()[i];
 for (int j = 0; j < size; j++) {
 expected += "[";
 expected += j;
 expected += "]";
 }
 }
 assertEquals(expected, str);
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 assertTrue(it.hasNext());
 for (int i = 0; i < mdc.getTotalSize(); i++) {
 it.next();
 assertTrue(it.hasNext());
 }
 assertFalse(it.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 assertEquals(-1, it.next());
 for (int i = 0; i < mdc.getTotalSize(); i++) {
 int expected = i;
 int actual = it.next();
 assertEquals(expected, actual);
 }
 assertFalse(it.hasNext());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 it.next();
 int count = it.getCount();
 assertEquals(0, count);
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 it.next();
 int[] counts = it.getCounts();
 assertArrayEquals(new int[]{0, 0, 0}, counts);
 }

 @Test
 public void testIteratorGetCountLastDim() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 it.next();
 int count = it.getCount(2);
 assertEquals(0, count);
 }

 @Test
 public void testIteratorGetCountMiddleDim() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 it.next();
 int count = it.getCount(1);
 assertEquals(0, count);
 }

 @Test
 public void testIteratorGetCountFirstDim() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 it.next();
 int count = it.getCount(0);
 assertEquals(0, count);
 }

 @Test
 public void testIteratorGetCountAllDimEqual() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 int[] counts = new int[mdc.getDimension()];
 for (int i = 0; i < mdc.getDimension(); i++) {
 it.next();
 counts[i] = it.getCount(i);
 }
 assertArrayEquals(new int[]{testSize[0], testSize[1], testSize[2]}, counts);
 }

 @Test
 public void testIteratorGetCountAllDimDifferent() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(testSize);
 MultidimensionalCounter.Iterator it = mdc.iterator();
 int[] counts = new int[mdc.getDimension()];
 for (int i = 0; i < mdc.getDimension(); i++) {
 it.next();
 counts[i] = it.getCount(i) + 1;
 }
 assertArrayEquals(new int[]{testSize[0] + 1, testSize[1] + 1, testSize[2] + 1}, counts);
 }
}
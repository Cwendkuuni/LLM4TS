package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import java.util.Arrays;

public class MultidimensionalCounterTest {

 @Test
 public void testMultidimensionalCounterConstruction() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 assertEquals(3, mdc.getDimension());
 int[] expectedSizes = {1, 2, 3};
 assertArrayEquals(expectedSizes, mdc.getSizes());
 int totalSize = 1 * 2 * 3;
 assertEquals(totalSize, mdc.getSize());
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 assertEquals(3, mdc.getDimension());
 }

 @Test
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 assertThrows(OutOfRangeException.class, () -> mdc.getCounts(-1));
 assertThrows(OutOfRangeException.class, () -> mdc.getCounts(10));
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 int[] arr1 = mdc.getCounts(0);
 assertEquals(0, arr1[0]);
 assertEquals(0, arr1[1]);
 assertEquals(0, arr1[2]);
 int[] arr2 = mdc.getCounts(1);
 assertEquals(0, arr2[0]);
 assertEquals(0, arr2[1]);
 assertEquals(1, arr2[2]);
 int[] arr3 = mdc.getCounts(2);
 assertEquals(0, arr3[0]);
 assertEquals(0, arr3[1]);
 assertEquals(2, arr3[2]);
 int[] arr4 = mdc.getCounts(3);
 assertEquals(0, arr4[0]);
 assertEquals(1, arr4[1]);
 assertEquals(2, arr4[2]);
 }

 @Test
 public void testGetCountOutOfRange() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 assertThrows(OutOfRangeException.class, () -> mdc.getCount(-1, 0));
 assertThrows(OutOfRangeException.class, () -> mdc.getCount(-1, 1));
 assertThrows(OutOfRangeException.class, () -> mdc.getCount(-1, 2));
 assertThrows(OutOfRangeException.class, () -> mdc.getCount(3, 3));
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 int count = mdc.getCount(0, 0);
 assertEquals(0, count);
 int count1 = mdc.getCount(0, 1);
 assertEquals(0, count1);
 int count2 = mdc.getCount(0, 2);
 assertEquals(0, count2);
 int count3 = mdc.getCount(1, 0);
 assertEquals(0, count3);
 int count4 = mdc.getCount(1, 1);
 assertEquals(0, count4);
 int count5 = mdc.getCount(1, 2);
 assertEquals(0, count5);
 int count6 = mdc.getCount(2, 0);
 assertEquals(0, count6);
 int count7 = mdc.getCount(2, 1);
 assertEquals(0, count7);
 int count8 = mdc.getCount(2, 2);
 assertEquals(0, count8);
 int count9 = mdc.getCount(0, 1, 1);
 assertEquals(0, count9);
 int count10 = mdc.getCount(1, 0, 1);
 assertEquals(0, count10);
 int count11 = mdc.getCount(1, 1, 2);
 assertEquals(0, count11);
 int count12 = mdc.getCount(2, 0, 2);
 assertEquals(0, count12);
 int count13 = mdc.getCount(0, 1, 2);
 assertEquals(0, count13);
 int count14 = mdc.getCount(1, 0, 2);
 assertEquals(0, count14);
 int count15 = mdc.getCount(1, 1, 1);
 assertEquals(0, count15);
 int count16 = mdc.getCount(0, 2, 1);
 assertEquals(0, count16);
 int count17 = mdc.getCount(1, 2, 1);
 assertEquals(0, count17);
 int count18 = mdc.getCount(2, 1, 2);
 assertEquals(0, count18);
 int count19 = mdc.getCount(0, 2, 2);
 assertEquals(0, count19);
 int count20 = mdc.getCount(1, 2, 2);
 assertEquals(0, count20);
 int count21 = mdc.getCount(0, 1, 2, 1);
 assertEquals(0, count21);
 int count22 = mdc.getCount(1, 0, 2, 1);
 assertEquals(0, count22);
 int count23 = mdc.getCount(1, 1, 2, 2);
 assertEquals(0, count23);
 int count24 = mdc.getCount(1, 2, 2, 1);
 assertEquals(0, count24);
 int count25 = mdc.getCount(2, 1, 2, 2);
 assertEquals(0, count25);
 int count26 = mdc.getCount(0, 2, 2, 1);
 assertEquals(0, count26);
 int count27 = mdc.getCount(0, 2, 2, 2);
 assertEquals(0, count27);
 int count28 = mdc.getCount(1, 2, 2, 2);
 assertEquals(0, count28);
 int count29 = mdc.getCount(2, 2, 2, 1);
 assertEquals(0, count29);
 int count30 = mdc.getCount(2, 2, 2, 2);
 assertEquals(0, count30);
 int count31 = mdc.getCount(0, 2, 2, 2, 1);
 assertEquals(0, count31);
 int count32 = mdc.getCount(1, 2, 2, 2, 1);
 assertEquals(0, count32);
 int count33 = mdc.getCount(1, 2, 2, 2, 2);
 assertEquals(0, count33);
 int count34 = mdc.getCount(2, 2, 2, 2, 2);
 assertEquals(0, count34);
 }

 @Test
 public void testGetCountsAndCountDimensionsMismatched() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 assertThrows(DimensionMismatchException.class, () -> mdc.getCountsAndCount(new int[] {1, 2, 4}));
 }

 @Test
 public void testGetCountsAndCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 int[] counts = mdc.getCounts(3);
 int count = mdc.getCount(counts);
 int expectedCount = 0 + mdc.uniCounterOffset[0] * 0 + mdc.uniCounterOffset[1] * 0 + mdc.uniCounterOffset[2] * 3;
 assertEquals(expectedCount, count);
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 MultidimensionalCounter.Iterator itr = mdc.iterator();
 // First call should return false, since we haven't called next() yet
 assertFalse(itr.hasNext());
 // Test all possible combinations for the first dimension
 for (int i = 0; i < mdc.getSizes()[0]; ++i) {
 itr = mdc.iterator();
 for (int j = 0; j < mdc.getSizes()[1]; ++j) {
 for (int k = 0; k < mdc.getSizes()[2]; ++k) {
 assertTrue(itr.hasNext());
 assertEquals(i, itr.next());
 }
 }
 }
 // Test the last combination
 itr = mdc.iterator();
 for (int j = 0; j < mdc.getSizes()[1]; ++j) {
 for (int k = 0; k < mdc.getSizes()[2]; ++k) {
 itr.next();
 }
 }
 assertTrue(itr.hasNext());
 assertEquals(mdc.getSize(), itr.next());
 // Test the empty case
 itr = mdc.iterator();
 assertFalse(itr.hasNext());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 MultidimensionalCounter.Iterator itr = mdc.iterator();
 itr.next();
 itr.next();
 assertEquals(2, itr.getCount(1));
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 MultidimensionalCounter.Iterator itr = mdc.iterator();
 itr.next();
 itr.next();
 int[] counts = itr.getCounts();
 assertEquals(2, counts[1]);
 }

 @Test
 public void testToString() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 assertEquals("[0][0][0]", mdc.toString());
 }

 @Test
 public void testToStringMultiDimensional() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 assertEquals("[0][0][0]", mdc.toString());
 MultidimensionalCounter mdc1 = new MultidimensionalCounter(1, 1, 1, 1, 1, 1);
 assertEquals("[0][0][0][0][0][0]", mdc1.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratorRemove() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 MultidimensionalCounter.Iterator itr = mdc.iterator();
 itr.next();
 itr.remove();
 }
}
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Arrays;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter multidimensionalCounter;

 @Test
 public void testMultidimensionalCounterConstruction() {
 try {
 multidimensionalCounter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 } catch (NotStrictlyPositiveException e) {
 fail("Should not throw NotStrictlyPositiveException");
 }
 assertTrue(multidimensionalCounter instanceof MultidimensionalCounter);
 assertEquals(3, multidimensionalCounter.getDimension());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterNegativeSizeException() {
 try {
 new MultidimensionalCounter(new int[] { -1, 3, 4 });
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 }

 @Test
 public void testMultidimensionalCounterGetDimension() {
 assertEquals(3, multidimensionalCounter.getDimension());
 }

 @Test
 public void testMultidimensionalCounterGetTotalSize() {
 int expectedTotalSize = 1 * MathArrays.product(multidimensionalCounter.getSizes());
 assertEquals(expectedTotalSize, multidimensionalCounter.getSize());
 }

 @Test
 public void testMultidimensionalCounterGetSizes() {
 int[] expectedSizes = new int[] { 1, 3, 4 };
 assertArrayEquals(expectedSizes, multidimensionalCounter.getSizes());
 }

 @Test
 public void testMultidimensionalCounterGetCountsZeroIndex() {
 int[] expectedCounts = new int[] { 0, 0, 0 };
 try {
 int[] actualCounts = multidimensionalCounter.getCounts(0);
 assertArrayEquals(expectedCounts, actualCounts);
 } catch (OutOfRangeException e) {
 fail("Should not throw OutOfRangeException for index 0");
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testMultidimensionalCounterGetCountsOutOfRangeIndex() {
 try {
 multidimensionalCounter.getCounts(multidimensionalCounter.getTotalSize());
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test
 public void testMultidimensionalCounterGetCountsValidIndex() {
 int[] expectedCounts = new int[] { 2, 1, 1 };
 try {
 int[] actualCounts = multidimensionalCounter.getCounts(2);
 assertArrayEquals(expectedCounts, actualCounts);
 } catch (OutOfRangeException e) {
 fail("Should not throw OutOfRangeException for valid index");
 }
 }

 @Test
 public void testMultidimensionalCounterGetCountValidIndex() {
 int expectedCount = 2 * MathArrays.product(new int[] { 1, 1 }) + 1;
 assertEquals(expectedCount, multidimensionalCounter.getCount(new int[] { 0, 0 }));
 }

 @Test(expected = OutOfRangeException.class)
 public void testMultidimensionalCounterGetCountOutOfRangeIndex() {
 try {
 multidimensionalCounter.getCount(new int[] { 3, 3, 3 });
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultidimensionalCounterGetCountInvalidDimension() {
 try {
 multidimensionalCounter.getCount(new int[] { 3 });
 } catch (DimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testMultidimensionalCounterIteratorHasNext() {
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 for (int i = 0; i < multidimensionalCounter.getTotalSize(); ++i) {
 assertTrue(iterator.hasNext());
 }
 }

 @Test
 public void testMultidimensionalCounterIteratorNext() {
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 int count = -1;
 for (int i = 0; i < multidimensionalCounter.getTotalSize(); ++i) {
 Integer next = iterator.next();
 assertEquals(i, next.intValue());
 count++;
 }
 assertEquals(multidimensionalCounter.getTotalSize(), count);
 }

 @Test
 public void testMultidimensionalCounterIteratorGetCount() {
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 assertEquals(-1, iterator.getCount());
 for (int i = 0; i < multidimensionalCounter.getTotalSize(); ++i) {
 int next = iterator.next().intValue();
 assertEquals(next, iterator.getCount());
 }
 assertEquals(multidimensionalCounter.getTotalSize(), iterator.getCount());
 }

 @Test
 public void testMultidimensionalCounterIteratorGetCounts() {
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 for (int i = 0; i < multidimensionalCounter.getTotalSize(); ++i) {
 int[] counts = iterator.getCounts();
 assertArrayEquals(new int[] { i / multidimensionalCounter.getSizes()[0], i % multidimensionalCounter.getSizes()[0], i % multidimensionalCounter.getSizes()[1] % multidimensionalCounter.getSizes()[1] }, counts);
 }
 }
}
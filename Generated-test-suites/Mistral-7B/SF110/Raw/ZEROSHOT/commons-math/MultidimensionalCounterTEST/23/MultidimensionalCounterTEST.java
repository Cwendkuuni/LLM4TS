package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;

 @Test
 public void testMultidimensionalCounterConstruction() {
 int[] size = new int[] { 2, 3, 4 };
 try {
 counter = new MultidimensionalCounter(size);
 assertEquals(3, counter.getDimension());
 assertEquals(Arrays.asList(2, 3, 4), Arrays.asList(counter.getSizes()));
 } catch (NotStrictlyPositiveException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterNegativeSize() {
 int[] size = new int[] { -1, 3, 4 };
 try {
 new MultidimensionalCounter(size);
 } catch (NotStrictlyPositiveException e) {
 return;
 }
 fail("Negative size should have thrown exception");
 }

 @Test
 public void testMultidimensionalCounterBasicGetters() {
 int[] size = new int[] { 2, 3, 4 };
 counter = new MultidimensionalCounter(size);
 assertEquals(3, counter.getDimension());
 assertEquals(2, counter.getSize());
 assertArrayEquals(new int[] { 2, 3, 4 }, counter.getSizes());
 }

 @Test
 public void testMultidimensionalCounterGetCount() {
 int[] size = new int[] { 2, 3, 4 };
 counter = new MultidimensionalCounter(size);
 assertEquals(0, counter.getCount(new int[] { 0, 0, 0 }));
 try {
 counter.getCount(new int[] { 5, 5, 5 });
 fail("Out of range exception expected");
 } catch (OutOfRangeException e) {
 // expected
 }
 int[] expectedIndices = new int[] { 0, 1, 1 };
 int expectedValue = 1;
 try {
 int actualValue = counter.getCount(expectedIndices);
 assertEquals(expectedValue, actualValue);
 } catch (OutOfRangeException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test
 public void testMultidimensionalCounterGetCountInvalidDimension() {
 int[] size = new int[] { 2, 3, 4 };
 counter = new MultidimensionalCounter(size);
 try {
 counter.getCount(new int[] { 2, 3 });
 fail("Dimension mismatch exception expected");
 } catch (DimensionMismatchException e) {
 // expected
 }
 }

 @Test
 public void testMultidimensionalCounterIteratorBasics() {
 int[] size = new int[] { 2, 3, 4 };
 counter = new MultidimensionalCounter(size);
 Iterator<Integer> iterator = counter.iterator();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMultidimensionalCounterIteratorHasNext() {
 int[] size = new int[] { 2, 3, 4 };
 counter = new MultidimensionalCounter(size);
 Iterator<Integer> iterator = counter.iterator();
 for (int i = 0; i < counter.getTotalSize(); ++i) {
 assertTrue(iterator.hasNext());
 }
 }

 @Test
 public void testMultidimensionalCounterIteratorNext() {
 int[] size = new int[] { 2, 3, 4 };
 counter = new MultidimensionalCounter(size);
 Iterator<Integer> iterator = counter.iterator();
 for (int i = 0; i < counter.getTotalSize(); ++i) {
 Integer next = iterator.next();
 assertEquals(Integer.valueOf(i + 1), next);
 }
 }

 @Test
 public void testMultidimensionalCounterIteratorCount() {
 int[] size = new int[] { 2, 3, 4 };
 counter = new MultidimensionalCounter(size);
 Iterator<Integer> iterator = counter.iterator();
 for (int i = 0; i < counter.getTotalSize(); ++i) {
 iterator.next();
 }
 assertEquals(counter.getTotalSize(), iterator.getCount());
 }

 @Test
 public void testMultidimensionalCounterIteratorGetCounts() {
 int[] size = new int[] { 2, 3, 4 };
 counter = new MultidimensionalCounter(size);
 Iterator<Integer> iterator = counter.iterator();
 for (int i = 0; i < counter.getTotalSize(); ++i) {
 iterator.next();
 }
 int[] expected = new int[] { 2, 3, 3 };
 int[] actual = iterator.getCounts();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testMultidimensionalCounterIteratorGetCountsMismatchDimension() {
 int[] size = new int[] { 2, 3, 4 };
 counter = new MultidimensionalCounter(size);
 Iterator<Integer> iterator = counter.iterator();
 for (int i = 0; i < counter.getTotalSize(); ++i) {
 iterator.next();
 }
 try {
 iterator.getCounts(new int[] { 2, 3, 5 });
 fail("Dimension mismatch exception expected");
 } catch (DimensionMismatchException e) {
 // expected
 }
 }

 @Test
 public void testMultidimensionalCounterToString() {
 int[] size = new int[] { 2, 3, 4 };
 counter = new MultidimensionalCounter(size);
 String expected = "[0][0][0]", actual = counter.toString();
 assertEquals(expected, actual);
 for (int i = 0; i < counter.getTotalSize(); ++i) {
 iterator.next();
 expected = String.format("[%d]", i);
 actual = counter.toString();
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testMultidimensionalCounterIterationOrder() {
 int[] size = new int[] { 3, 2, 3 };
 counter = new MultidimensionalCounter(size);
 Iterator<Integer> iterator = counter.iterator();
 int[][] expected = new int[][] { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 1, 0 }, { 0, 1, 1 }, { 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 }, { 1, 1, 1 } };
 for (int i = 0; i < counter.getTotalSize(); ++i) {
 Integer next = iterator.next();
 int[] actual = iterator.getCounts();
 int[] expectedIndex = new int[counter.getDimension()];
 for (int j = 0; j < counter.getDimension(); ++j) {
 expectedIndex[j] = actual[j];
 }
 assertEquals(Arrays.asList(expectedIndex), Arrays.asList(expected[i]));
 }
 }

 @Test
 public void testMultidimensionalCounterIterationOrderBackwards() {
 int[] size = new int[] { 3, 2, 3 };
 counter = new MultidimensionalCounter(size);
 Iterator<Integer> iterator = Collections.reverseOrder()::new;
 int[][] expected = new int[][] { { 2, 2, 2 }, { 2, 2, 1 }, { 2, 1, 2 }, { 2, 1, 1 }, { 1, 2, 2 }, { 1, 2, 1 }, { 1, 1, 2 }, { 1, 1, 1 } };
 for (int i = counter.getTotalSize() - 1; i >= 0; --i) {
 Integer next = iterator.next();
 int[] actual = iterator.getCounts();
 int[] expectedIndex = new int[counter.getDimension()];
 for (int j = 0; j < counter.getDimension(); ++j) {
 expectedIndex[j] = actual[j];
 }
 assertEquals(Arrays.asList(expectedIndex), Arrays.asList(expected[i]));
 }
 }

}
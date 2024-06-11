package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;

 @Before
 public void setUp() {
 try {
 this.counter = new MultidimensionalCounter(new int[] { 3, 2, 1 });
 } catch (NotStrictlyPositiveException e) {
 e.printStackTrace();
 fail("NotStrictlyPositiveException was thrown unexpectedly");
 }
 }

 @Test
 public void testGetDimension() {
 assertEquals(3, this.counter.getDimension());
 }

 @Test
 public void testGetSize() {
 assertEquals(6, this.counter.getSize());
 }

 @Test
 public void testGetSizes() {
 int[] sizes = this.counter.getSizes();
 assertEquals(3, sizes.length);
 assertArrayEquals(new int[] { 3, 2, 1 }, sizes);
 }

 @Test
 public void testGetCounts_validIndex() {
 int[] indices = this.counter.getCounts(0);
 assertArrayEquals(new int[] { 0, 0, 0 }, indices);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCounts_outOfRange() {
 this.counter.getCounts(7);
 }

 @Test
 public void testGetCount() {
 assertEquals(0, this.counter.getCount(0, 0, 0));
 assertEquals(1, this.counter.getCount(1, 0, 0));
 assertEquals(2, this.counter.getCount(1, 1, 0));
 assertEquals(3, this.counter.getCount(2, 1, 1));
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCount_outOfRange() {
 this.counter.getCount(-1, 0, 0);
 this.counter.getCount(3, 2, 1);
 }

 @Test
 public void testGetCount_mismatchingDimension() {
 try {
 this.counter.getCount(new int[] { 4, 3 });
 } catch (DimensionMismatchException e) {
 assertEquals("Dimension mismatch, expected: 3, actual: 2", e.getMessage());
 }
 }

 @Test
 public void testIterator() {
 Iterator<Integer> iterator = this.counter.iterator();
 List<Integer> list = new ArrayList<>();
 while (iterator.hasNext()) {
 list.add(iterator.next());
 }
 assertArrayEquals(new int[] { 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3 }, list.stream().mapToInt(i -> i).toArray());
 }

 @Test
 public void testToString() {
 assertEquals("[0][1][2]", this.counter.toString());
 }
}
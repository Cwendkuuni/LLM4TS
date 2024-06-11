package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;
 private int[] sizes;

 @Parameters
 public static List<Object[]> data() {
 return Arrays.asList(new Object[][] { { 2, 3, 4 }, { 1, 1, 1 }, { 5, 5, 5 } });
 }

 @Parameters(name = "{index}: [sizes={0}]")
 public int[] sizes(int index) {
 return new int[] { (int) (Math.random() * 10), (int) (Math.random() * 10), (int) (Math.random() * 10) };
 }

 @Before
 public void setUp() throws NotStrictlyPositiveException {
 sizes = sizes();
 counter = new MultidimensionalCounter(sizes);
 }

 @Test
 public void testGetDimension() {
 Assert.assertEquals(sizes.length, counter.getDimension());
 }

 @Test
 public void testGetTotalSize() {
 int totalSize = 1;
 for (int i = 0; i < sizes.length; ++i) {
 totalSize *= sizes[i];
 }
 Assert.assertEquals(totalSize, counter.getSize());
 }

 @Test
 public void testGetCountsForIndexWithinRange() {
 int index = (int) (Math.random() * totalSize);
 int[] expected = new int[sizes.length];
 for (int i = sizes.length - 1; i >= 0; --i) {
 int counter = 0;
 for (int j = 0; j < i; ++j) {
 counter += sizes[j] * sizes[j + 1];
 }
 expected[i] = index - counter;
 }
 int[] actual = counter.getCounts(index);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsForIndexOutOfRange() {
 int index = totalSize + 1;
 counter.getCounts(index);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithDifferentSizes() {
 int[] newSizes = new int[] { sizes[0], sizes[1] + 1 };
 MultidimensionalCounter newCounter;
 try {
 newCounter = new MultidimensionalCounter(newSizes);
 } catch (NotStrictlyPositiveException e) {
 return;
 }
 int[] newCounts = newCounter.getCounts(Math.abs(Math.random()) % newCounter.getTotalSize());
 Assert.assertArrayNotEquals(counter.getCounts(Math.abs(Math.random()) % counter.getTotalSize()), newCounts);
 }

 @Test
 public void testGetCount() {
 int[] index = new int[sizes.length];
 int expected = 0;
 for (int i = 0; i < sizes.length; ++i) {
 index[i] = (int) (Math.random() * sizes[i]);
 expected += sizes[i] * uniCounterOffset(i, index);
 }
 int actual = counter.getCount(index);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountForIndexOutOfRange() {
 int[] index = new int[sizes.length];
 for (int i = 0; i < sizes.length; ++i) {
 index[i] = (int) (Math.random() * sizes[i]);
 }
 index[Math.abs(Math.random()) % sizes.length] = sizes[Math.abs(Math.random()) % sizes.length] + 1;
 counter.getCount(index);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountWithDifferentSizes() {
 int[] newIndex = new int[sizes.length + 1];
 Arrays.fill(newIndex, Collections.max(Arrays.asList(sizes)));
 counter.getCount(newIndex);
 }

 @Test
 public void testToString() {
 StringBuilder expected = new StringBuilder();
 for (int i = 0; i < sizes.length; ++i) {
 expected.append("[").append(counter.getCount(i)).append("]");
 }
 StringBuilder actual = new StringBuilder();
 actual.append(counter.toString());
 Assert.assertEquals(expected.toString(), actual.toString());
 }

}
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;
import static org.apache.commons.math3.util.MathArrays.copyOf;

public class MultidimensionalCounterTest {

 @Test
 public void testMultidimensionalCounterCreation() {
 MultidimensionalCounter counter = null;
 try {
 counter = new MultidimensionalCounter(new int[] { 1, 2, 3 });
 } catch (NotStrictlyPositiveException e) {
 fail("Should not throw NotStrictlyPositiveException while creating MultidimensionalCounter object.");
 }
 assertEquals(3, counter.getDimension());
 assertEquals(1, counter.getSizes()[0]);
 assertEquals(2, counter.getSizes()[1]);
 assertEquals(3, counter.getSizes()[2]);
 assertEquals(3 * 2 * 1, counter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSize() {
 try {
 new MultidimensionalCounter(new int[] { -1, 2, 3 });
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testZeroSize() {
 try {
 new MultidimensionalCounter(new int[] { 1, 0, 3 });
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 1, 2, 3 });
 try {
 counter.getCounts(-1);
 } catch (OutOfRangeException e) {
 // Expected
 }
 try {
 counter.getCounts(10);
 } catch (OutOfRangeException e) {
 // Expected
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 1, 2, 3 });
 try {
 counter.getCount(-1, new int[3]);
 } catch (OutOfRangeException e) {
 // Expected
 }
 try {
 counter.getCount(new int[] { 1, 2, 4 }, new int[3]);
 } catch (DimensionMismatchException e) {
 // Expected
 }
 try {
 counter.getCount(new int[] { 1, 2, 3 }, new int[] { 4 });
 } catch (DimensionMismatchException e) {
 // Expected
 }
 try {
 counter.getCount(new int[] { 1, 2, 3 }, new int[2][]);
 } catch (DimensionMismatchException e) {
 // Expected
 }
 try {
 counter.getCount(new int[] { 1, 2, 3 }, new int[] { 1, 2, 4 });
 } catch (OutOfRangeException e) {
 // Expected
 }
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 1, 2, 3 });
 int[] expected = new int[] { 1, 1, 1 };
 int count = counter.getCount(expected);
 assertArrayEquals(expected, counter.getCounts(count));
 assertEquals(3, count);
 }

 @Test
 public void testMultidimensionalCounterIterator() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 1, 2, 3 });
 Iterator<Integer> iterator = counter.iterator();
 assertEquals(-1, iterator.next());
 int count = 0;
 while (iterator.hasNext()) {
 int next = iterator.next();
 count++;
 assertEquals(next, iterator.getCount());
 assertArrayEquals(iterator.getCounts(), new int[] { count % 2, count % 3, count % 3 });
 }
 assertEquals(-1, iterator.next());
 }

}
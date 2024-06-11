package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

 private int[] size;
 private MultidimensionalCounter multidimensionalCounter;

 @Parameters(name = "{index}: size={0}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 {new int[]{1, 1, 1}},
 {new int[]{2, 2, 2}},
 {new int[]{3, 3, 3}},
 {new int[]{1, 2, 3}},
 {new int[]{2, 1, 3}}
 });
 }

 public MultidimensionalCounterTest(int[] size) {
 this.size = size;
 }

 @Test
 public void testConstructor() {
 try {
 multidimensionalCounter = new MultidimensionalCounter(size);
 } catch (NotStrictlyPositiveException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 assertEquals("Incorrect dimension", size.length, multidimensionalCounter.getDimension());
 assertEquals("Incorrect total size", Arrays.stream(size).parallel().sum(), multidimensionalCounter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInvalidSizeException() {
 int[] invalidSize = {-1, 1};
 try {
 new MultidimensionalCounter(invalidSize);
 } catch (NotStrictlyPositiveException e) {
 assertEquals("Expected exception: " + e.getMessage(), "-1", String.valueOf(invalidSize[0]));
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 int index = multidimensionalCounter.getTotalSize() + 1;
 try {
 multidimensionalCounter.getCounts(index);
 } catch (OutOfRangeException e) {
 assertEquals("Expected exception: " + e.getMessage(), index, e.getActual());
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongDimension() {
 int[] wrongDimension = {1, 2, 3, 4};
 try {
 new MultidimensionalCounter(wrongDimension);
 } catch (NotStrictlyPositiveException e) {
 // swallow
 }
 try {
 multidimensionalCounter.getCounts(new int[]{1, 2, 3, 5});
 } catch (DimensionMismatchException e) {
 assertEquals("Expected exception: " + e.getMessage(), 4, e.getExpectedDimension());
 assertEquals("Expected exception: " + e.getMessage(), 3, e.getActualDimension());
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsWithOutOfRangeDimension() {
 int[] outOfRangeDimension = {1, 2, 3, 5};
 try {
 new MultidimensionalCounter(outOfRangeDimension);
 } catch (NotStrictlyPositiveException e) {
 // swallow
 }
 try {
 multidimensionalCounter.getCount(outOfRangeDimension);
 } catch (DimensionMismatchException e) {
 // swallow
 } catch (OutOfRangeException e) {
 assertEquals("Expected exception: " + e.getMessage(), outOfRangeDimension[3], e.getIndex());
 }
 }

 @Test
 public void testIterator() {
 List<Integer> expected = new ArrayList<>();
 for (int i = 0; i < multidimensionalCounter.getSize(); ++i) {
 expected.add(i);
 }
 Iterator<Integer> iterator = multidimensionalCounter.iterator();
 List<Integer> actual = new ArrayList<>();
 while (iterator.hasNext()) {
 actual.add(iterator.next());
 }
 assertEquals("Expected and actual lists should be equal", expected, actual);
 }

 @Test
 public void testToString() {
 String expected = "[0][0][0]";
 String actual = multidimensionalCounter.toString();
 assertEquals("Expected and actual strings should be equal", expected, actual);
 }
}
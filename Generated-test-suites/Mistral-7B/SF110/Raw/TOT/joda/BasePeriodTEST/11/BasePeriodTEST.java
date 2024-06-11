package org.joda.time.base;

import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriodConstructionWithArguments() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 PeriodType expectedType = PeriodType.standard();
 int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};
 Period actualPeriod = period;

 assertEquals(expectedType, actualPeriod.getPeriodType());
 assertArrayEquals(expectedValues, actualPeriod.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithLongArgument() {
 BasePeriod period = new BasePeriod(1234567890L);
 PeriodType expectedType = PeriodType.standard();
 int[] expectedValues = {1, 0, 0, 0, 0, 0, 0, 0, 0};
 Period actualPeriod = period;

 assertEquals(expectedType, actualPeriod.getPeriodType());
 assertArrayEquals(expectedValues, actualPeriod.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithInvalidArguments() {
 try {
 BasePeriod period = new BasePeriod(null, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BasePeriod period = new BasePeriod(1, null, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BasePeriod period = new BasePeriod(1, 2, null, 4, 5, 6, 7, 8, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BasePeriod period = new BasePeriod(1, 2, 3, null, 5, 6, 7, 8, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, null, 6, 7, 8, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, null, 7, 8, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, null, 8, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, null, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testBasePeriodAddition() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
 BasePeriod sum = period1.add(period2);
 PeriodType expectedType = PeriodType.standard();
 int[] expectedValues = {3, 5, 7, 9, 6, 9, 10, 10, 0};
 Period actualSum = sum;

 assertEquals(expectedType, actualSum.getPeriodType());
 assertArrayEquals(expectedValues, actualSum.getValues());
 }

 @Test
 public void testBasePeriodSubtraction() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
 BasePeriod difference = period1.subtract(period2);
 PeriodType expectedType = PeriodType.standard();
 int[] expectedValues = {-1, -1, -1, -1, 1, -1, -2, -1, 0};
 Period actualDifference = difference;

 assertEquals(expectedType, actualDifference.getPeriodType());
 assertArrayEquals(expectedValues, actualDifference.getValues());
 }

 @Test
 public void testBasePeriodComparison() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 8, PeriodType.standard());
 BasePeriod period3 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 9, PeriodType.standard());

 assertTrue(period1.isEqual(period1));
 assertFalse(period1.isEqual(period2));
 assertFalse(period1.isEqual(period3));

 assertFalse(period2.isEqual(period1));
 assertFalse(period2.isEqual(period3));

 assertFalse(period3.isEqual(period1));
 assertFalse(period3.isEqual(period2));

 assertTrue(period1.isGreaterThan(new BasePeriod(0, 0, 0, 0, 0, 0, 0, 0, PeriodType.standard())));
 assertTrue(period1.isGreaterThan(period2));
 assertTrue(period2.isGreaterThan(period3));

 assertTrue(period1.isLessThan(period2));
 assertTrue(period3.isLessThan(period1));
 assertTrue(period3.isLessThan(period2));
}

 @Test
 public void testBasePeriodDurationConversion() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Duration durationTo = period.toDurationTo(new DateTime());
 Duration durationFrom = period.toDurationFrom(new DateTime());

 assertEquals(durationTo, durationFrom.negate());
 }

 @Test
 public void testBasePeriodMergePeriod() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
 BasePeriod mergedPeriod = period1.merge(period2);
 PeriodType expectedType = PeriodType.standard();
 int[] expectedValues = {3, 5, 7, 11, 6, 13, 10, 12, 0};
 Period actualMergedPeriod = mergedPeriod;

 assertEquals(expectedType, actualMergedPeriod.getPeriodType());
 assertArrayEquals(expectedValues, actualMergedPeriod.getValues());
 }
}
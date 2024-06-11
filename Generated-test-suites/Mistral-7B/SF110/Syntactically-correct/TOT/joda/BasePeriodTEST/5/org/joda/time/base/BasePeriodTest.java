package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testEmptyPeriod() {
 BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, period.iValues);
 }

 @Test
 public void testPeriodWithValidValues() {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{3, 2, 1, 4, 5, 6, 7, 8}, period.iValues);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPeriodWithInvalidValues() {
 BasePeriod period = new BasePeriod(3, 2, -1, 4, 5, 6, 7, 8, PeriodType.standard());
 }

 @Test
 public void testGetValue() {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 assertEquals(3, period.getValue(DurationFieldType.years().getCode()));
 assertEquals(2, period.getValue(DurationFieldType.months().getCode()));
 // ... and so on for other fields
 }

 @Test
 public void testToDurationFrom() {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0).toInstant();
 Duration duration = period.toDurationFrom(instant);
 assertEquals(3, duration.getStandardDays());
 }

 @Test
 public void testToDurationTo() {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0).toInstant();
 Duration duration = period.toDurationTo(instant);
 assertEquals(-3, duration.getStandardDays());
 }

 @Test
 public void testMergePeriods() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
 BasePeriod mergedPeriod = new BasePeriod();
 mergedPeriod.mergePeriod(period1);
 mergedPeriod.mergePeriod(period2);
 BasePeriod expectedPeriod = new BasePeriod(4, 6, 8, 10, 11, 12, 13, 14, PeriodType.standard());
 assertEquals(expectedPeriod.getPeriodType(), mergedPeriod.getPeriodType());
 assertArrayEquals(expectedPeriod.iValues, mergedPeriod.iValues);
 }

 @Test
 public void testAddPeriods() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
 BasePeriod sumPeriod = new BasePeriod();
 sumPeriod.addPeriod(period1);
 sumPeriod.addPeriod(period2);
 BasePeriod expectedPeriod = new BasePeriod(4, 6, 8, 10, 11, 12, 13, 14, PeriodType.standard());
 assertEquals(expectedPeriod.getPeriodType(), sumPeriod.getPeriodType());
 assertArrayEquals(expectedPeriod.iValues, sumPeriod.iValues);
 }
}
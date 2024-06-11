package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.utils.*;
import org.junit.*;

import static org.junit.Assert.*;

public class BasePeriodTest {

 @BeforeClass
 public static void setUp() {
 DateTimeUtils.initCachedChronologyMap();
 }

 @Test
 public void testConstructorWithValues() {
 int[] values = {2, 3, 4, 5, 6, 7, 8, 9};
 BasePeriod period = new BasePeriod(PeriodType.standard(), values);
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(values, period.getValues());
 }

 @Test
 public void testConstructorWithDurations() {
 ReadableDuration duration1 = Duration.standardDays(1);
 ReadableDuration duration2 = Duration.standardDays(2);
 BasePeriod period = new BasePeriod(duration1, duration2, PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 1}, period.getValues());
 }

 @Test
 public void testConstructorWithInstants() {
 ReadableInstant instant1 = DateTime.now();
 ReadableInstant instant2 = DateTime.now().plus(Duration.standardDays(1));
 BasePeriod period = new BasePeriod(instant1, instant2, PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 1}, period.getValues());
 }

 @Test
 public void testConstructorWithReadablePartials() {
 ReadablePartial readablePartial1 = new GeneralDatetimeField(DateTimeFieldType.year(), 2022, CalendarSystem.UTC);
 ReadablePartial readablePartial2 = new GeneralDatetimeField(DateTimeFieldType.monthOfYear(), 1, CalendarSystem.UTC);
 BasePeriod period = new BasePeriod(readablePartial1, readablePartial2, PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{2, 0, 0, 0, 0, 0, 0, 0}, period.getValues());
 }

 @Test
 public void testConstructorWithInvalidArguments() {
 try {
 new BasePeriod(null, PeriodType.standard(), DateTimeUtils.UTC);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new BasePeriod(new BaseLocalDate(2022, 1, 1), null, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testGetters() {
 BasePeriod period = new BasePeriod(PeriodType.standard(), new int[]{2, 3, 4, 5, 6, 7, 8, 9});
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{2, 3, 4, 5, 6, 7, 8, 9}, period.getValues());

 period = new BasePeriod(Duration.standardDays(1), Duration.standardDays(2), PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 1}, period.getValues());

 period = new BasePeriod(DateTime.now(), DateTime.now().plus(Duration.standardDays(1)), PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 1}, period.getValues());

 period = new BasePeriod(
 new GeneralDatetimeField(DateTimeFieldType.year(), 2022, CalendarSystem.UTC),
 new GeneralDatetimeField(DateTimeFieldType.monthOfYear(), 1, CalendarSystem.UTC),
 PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{2, 0, 0, 0, 0, 0, 0, 0}, period.getValues());
}

 @Test
 public void testToDurationFrom() {
 BasePeriod period = new BasePeriod(Duration.standardDays(1), Duration.standardDays(2), PeriodType.standard());
 ReadableInstant instant = DateTime.now();
 Duration durationFrom = period.toDurationFrom(instant);
 assertEquals(Duration.standardDays(3), durationFrom);
 }

 @Test
 public void testToDurationTo() {
 BasePeriod period = new BasePeriod(Duration.standardDays(1), Duration.standardDays(2), PeriodType.standard());
 ReadableInstant instant = DateTime.now();
 Duration durationTo = period.toDurationTo(instant);
 assertEquals(Duration.standardDays(-1), durationTo);
 }

 @Test
 public void testAddPeriod() {
 BasePeriod period1 = new BasePeriod(Duration.standardDays(1), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(Duration.standardDays(2), PeriodType.standard());
 BasePeriod periodSum = period1.add(period2);
 assertEquals(PeriodType.standard(), periodSum.getPeriodType());
 assertArrayEquals(new int[]{2, 0, 0, 0, 0, 0, 0, 2}, periodSum.getValues());
 }

 @Test
 public void testMergePeriods() {
 BasePeriod period1 = new BasePeriod(Duration.standardDays(1), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(DurationFieldType.weeks(), 3, PeriodType.standard());
 BasePeriod periodMerged = period1.merge(period2);
 assertEquals(PeriodType.standard(), periodMerged.getPeriodType());
 assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 3}, periodMerged.getValues());
 }

 @Test
 public void testSetPeriod() {
 BasePeriod period = new BasePeriod(Duration.standardDays(1), PeriodType.standard());
 BasePeriod periodToSet = new BasePeriod(DurationFieldType.weeks(), 3, PeriodType.standard());
 period.setPeriod(periodToSet);
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 3}, period.getValues());
 }

 @Test
 public void testSetField() {
 BasePeriod period = new BasePeriod(Duration.standardDays(1), PeriodType.standard());
 period.setField(DurationFieldType.weeks(), 3);
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 3}, period.getValues());
 }

 @Test
 public void testAddField() {
 BasePeriod period = new BasePeriod(Duration.standardDays(1), PeriodType.standard());
 period.addField(DurationFieldType.weeks(), 3);
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 4}, period.getValues());
 }
}
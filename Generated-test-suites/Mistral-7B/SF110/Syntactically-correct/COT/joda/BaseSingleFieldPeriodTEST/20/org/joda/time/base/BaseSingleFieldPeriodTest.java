package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.field.DateTimeFields;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ISOWeekFields;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private static final int TEST_PERIOD = 42;

 @Test
 public void testBetween() {
 ReadableInstant readableInstant = new BaseDateTime(1651, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
 ReadableInstant readableInstant2 = new BaseDateTime(1651, 1, 1, 1, 1, 1, 1, ISOChronology.getInstanceUTC()).toInstant();
 DurationFieldType durationFieldType = DateTimeFields.HOURS;
 int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
 assertEquals(0, result);

 readableInstant2 = new BaseDateTime(1651, 1, 1, 2, 2, 2, 2, ISOChronology.getInstanceUTC()).toInstant();
 result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
 assertEquals(1, result);
 }

 @Test
 public void testBetweenPartial() {
 ReadablePartial readablePartial = new LocalTime().withHourOfDay(1).plus(Duration.standardHours(2)).toLocalDateTime().toLocalDate().toLocalDateTime().toLocalDateTimePartial();
 ReadablePartial readablePartial2 = new LocalTime().withHourOfDay(3).toLocalDateTime().toLocalDate().toLocalDateTimePartial();
 ReadablePeriod readablePeriod = Period.ONE.withFields(DateTimeFields.HOURS, 1);
 int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 assertEquals(2, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadableInstant() {
 ReadableInstant readableInstant = null;
 ReadableInstant readableInstant2 = new BaseDateTime(1651, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
 DurationFieldType durationFieldType = DateTimeFields.HOURS;
 BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadablePartial() {
 ReadablePartial readablePartial = null;
 ReadablePartial readablePartial2 = new LocalTime().withHourOfDay(1).toLocalDateTime().toLocalDate().toLocalDateTimePartial();
 ReadablePeriod readablePeriod = Period.ONE.withFields(DateTimeFields.HOURS, 1);
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenDifferentSizeReadablePartials() {
 ReadablePartial readablePartial = new LocalTime().withHourOfDay(1).toLocalDateTime().toLocalDate().toLocalDateTimePartial();
 ReadablePartial readablePartial2 = new LocalDateTime().withYear(1651).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(2).toLocalDateTime().toLocalDateTimePartial();
 ReadablePeriod readablePeriod = Period.ONE.withFields(DateTimeFields.HOURS, 1);
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenDifferentFieldTypes() {
 ReadablePartial readablePartial = new LocalTime().withHourOfDay(1).toLocalDateTime().toLocalDate().toLocalDateTimePartial();
 ReadablePartial readablePartial2 = new LocalDateTime().withYear(1651).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(2).toLocalDateTime().toLocalDateTimePartial();
 ReadablePeriod readablePeriod = Period.ONE.withFields(DateTimeFields.MINUTES, 1);
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNonContiguousReadablePartial() {
 ReadablePartial readablePartial = new LocalDateTime().withYear(1651).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(300).toLocalDateTime().toLocalDateTimePartial();
 ReadablePartial readablePartial2 = new LocalDateTime().withYear(1651).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(1).withSecondOfMinute(0).toLocalDateTime().toLocalDateTimePartial();
 ReadablePeriod readablePeriod = Period.ONE.withFields(DateTimeFields.MINUTES, 1);
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test
 public void testStandardPeriodIn() {
 ReadablePeriod readablePeriod = Period.ONE.withFields(DateTimeFields.HOURS, 1, DateTimeFields.MINUTES, 30, DateTimeFields.SECONDS, 15);
 long result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 60 * 60 * 24);
 assertEquals(2, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInNullReadablePeriod() {
 ReadablePeriod readablePeriod = null;
 long n = 60 * 60 * 24;
 BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 }

 @Test
 public void testBaseSingleFieldPeriodConstruction() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(TEST_PERIOD);
 assertEquals(TEST_PERIOD, instance.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBaseSingleFieldPeriodNegativeValue() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(-TEST_PERIOD);
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(TEST_PERIOD);
 DurationFieldType result = instance.getFieldType();
 assertEquals(DateTimeFields.MINUTES, result);
 }

 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(TEST_PERIOD);
 PeriodType result = instance.getPeriodType();
 assertEquals(PeriodType.minute(), result);
 }

 @Test
 public void testGetFieldTypeClassCast() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(TEST_PERIOD);
 assertFalse(instance instanceof DurationFieldType);
 assertTrue(instance.getClass().isAssignableFrom(DurationFieldType.class));
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(TEST_PERIOD);
 BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(TEST_PERIOD);
 assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testNotEquals() {
 BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(TEST_PERIOD);
 BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(TEST_PERIOD + 1);
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(TEST_PERIOD);
 BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(TEST_PERIOD + 1);
 int result1 = instance1.hashCode();
 int result2 = instance2.hashCode();
 assertNotEquals(result1, result2);
 }

 @Test
 public void testCompareToZero() {
 BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(0);
 BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(TEST_PERIOD);
 int result = instance1.compareTo(instance2);
 assertTrue(result < 0);
 }

 @Test
 public void testCompareToSameInstance() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(TEST_PERIOD);
 int result = instance.compareTo(instance);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToDifferentInstances() {
 BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(TEST_PERIOD);
 BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(TEST_PERIOD + 1);
 int result = instance1.compareTo(instance2);
 assertTrue(result < 0);
 }

 @Test
 public void testCompareToSameClassDifferentFieldTypes() {
 BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(TEST_PERIOD, DateTimeFields.MINUTES);
 BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(TEST_PERIOD, DateTimeFields.HOURS);
 int result = instance1.compareTo(instance2);
 assertTrue(result < 0);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToDifferentClasses() {
 BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(TEST_PERIOD);
 Period instance2 = Period.ONE.withFields(DateTimeFields.MINUTES, 1);
 int result = instance1.compareTo(instance2);
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(TEST_PERIOD);
 Period result = instance.toPeriod();
 assertEquals(Period.ONE.withFields(DateTimeFields.MINUTES, TEST_PERIOD), result);
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(TEST_PERIOD);
 MutablePeriod mutablePeriod = instance.toMutablePeriod();
 assertEquals(TEST_PERIOD, mutablePeriod.get(DateTimeFields.MINUTES));
 }
}
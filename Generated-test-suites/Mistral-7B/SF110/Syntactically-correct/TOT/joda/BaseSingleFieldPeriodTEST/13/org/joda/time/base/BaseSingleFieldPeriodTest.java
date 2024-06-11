package org.joda.time.base;

import org.junit.Test;
import org.joda.time.BasePeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Period;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BaseSingleFieldPeriodTest {

 @Test
 public void testBetween() {
 ReadableInstant start = Instant.now();
 ReadableInstant end = Instant.now().plus(1, ChronoUnits.DAYS);
 DurationFieldType durationFieldType = ChronoUnits.DAYS;
 int result = BaseSingleFieldPeriod.between(start, end, durationFieldType);
 assertEquals(1, result);
 }

 @Test
 public void testBetweenWithInvalidArguments() {
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, Instant.now(), DurationFieldType.DAYS_30));
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(Instant.now(), null, DurationFieldType.DAYS_30));
 }

 @Test
 public void testBetweenWithDifferentChronologies() {
 ReadableInstant startUTC = Instant.now();
 Chronology utc = DateTimeUtils.getChronology(startUTC.getChronology()).withUTC();
 ReadableInstant startLocal = Instant.now();
 Chronology local = DateTimeUtils.getDefault();
 int result = BaseSingleFieldPeriod.between(startUTC, startLocal, DurationFieldType.HOURS_24);
 assertEquals(-DateTimeUtils.getLocalGmtDiff(DateTimeZone.getDefault(), local), result);
 }

 @Test
 public void testBetweenWithInvalidDurationFieldType() {
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(Instant.now(), Instant.now(), null));
 }

 @Test
 public void testStandardPeriodIn() {
 Period period = new Period(2, Months.FEBRUARY, 1, 1, 1, 1, 1);
 long n = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
 assertEquals(12 * 28 * 24 * 60 * 60 * 1000L / n, result * 1000);
 }

 @Test
 public void testStandardPeriodInWithNullPeriod() {
 assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(null, 1));
 }

 @Test
 public void testNewInstance() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 DurationFieldType fieldType = ChronoUnits.DAYS;
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(BaseSingleFieldPeriod.standardPeriodIn(period1.toPeriod(), 365 * 24 * 60 * 60L));
 assertEquals(period1.getClass(), period2.getClass());
 assertEquals(fieldType, period2.getFieldType());
 assertEquals(period1.getValue(), period2.getValue());
 }

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 period.setValue(2);
 assertEquals(2, period.getValue());
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 DurationFieldType fieldType = ChronoUnits.DAYS;
 assertEquals(fieldType, period.getFieldType());
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 assertEquals(1, period.size());
 }

 @Test
 public void testGetFieldTypeInvalidIndex() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 assertThrows(IndexOutOfBoundsException.class, () -> period.getFieldType(2));
 }

 @Test
 public void testGetValueInvalidIndex() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 assertThrows(IndexOutOfBoundsException.class, () -> period.getValue(2));
 }

 @Test
 public void testIsSupported() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 DurationFieldType fieldType = ChronoUnits.DAYS;
 assertEquals(true, period.isSupported(fieldType));
 }

 @Test
 public void testIsSupportedInvalidFieldType() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 DurationFieldType fieldType = new DurationFieldType() {
 @Override
 public String getName() {
 return "testFieldType";
 }
 };
 assertEquals(false, period.isSupported(fieldType));
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 Period result = period.toPeriod();
 assertEquals(1, result.getValues()[0]);
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
 MutablePeriod result = period.toMutablePeriod();
 result.add(1);
 assertEquals(2, result.getValue(0));
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
 assertEquals(period1, period2);
 }

 @Test
 public void testEqualsDifferentClass() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 Object period2 = new Object();
 assertFalse(period1.equals(period2));
 }

 @Test
 public void testCompareToSamePeriod() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
 assertEquals(0, period1.compareTo(period2));
 }

 @Test
 public void testCompareToDifferentPeriods() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2);
 assertEquals(-1, period1.compareTo(period2));
 }

 @Test
 public void testCompareToDifferentClasses() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
 Object period2 = new Object();
 throw new ClassCastException("Cannot compare " + period1.getClass() + " to " + period2.getClass());
 }
}
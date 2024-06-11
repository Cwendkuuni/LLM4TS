package org.joda.time.base;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldPosition;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.ReadablePartial;
import org.joda.time.SerializableSupplier;
import org.joda.time.field.DecimalField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.LongField;
import org.joda.time.field.MinutesField;
import org.joda.time.field.SecondsField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod period;
 private DurationFieldType daysField;
 private DurationFieldType hoursField;
 private DurationFieldType minutesField;
 private DurationFieldType secondsField;

 @Before
 public void setup() {
 daysField = new DaysField(0, PeriodType.standard());
 hoursField = new HoursField(0, PeriodType.standard());
 minutesField = new MinutesField(0, PeriodType.standard());
 secondsField = new SecondsField(0, PeriodType.standard());
 period = new BaseSingleFieldPeriod(1) {
 @Override
 public DurationFieldType getFieldType() {
 return daysField;
 }
 };
 }

 @Test
 public void testGetFieldType() {
 assertEquals(daysField, period.getFieldType());
 }

 @Test
 public void testGetPeriodType() {
 assertEquals(PeriodType.standard(), period.getPeriodType());
 }

 @Test
 public void testSize() {
 assertEquals(1, period.size());
 }

 @Test
 public void testGetFieldTypeAt() {
 assertEquals(daysField, period.getFieldType(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeAtNegativeIndex() {
 period.getFieldType(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeAtPositiveIndexGreaterThanSize() {
 period.getFieldType(2);
 }

 @Test
 public void testGetValue() {
 assertEquals(1, period.getValue());
 }

 @Test
 public void testSetValue() {
 period.setValue(2);
 assertEquals(2, period.getValue());
 }

 @Test
 public void testToString() {
 assertEquals("P1D", period.toString());
 }

 @Test
 public void testToPeriod() {
 Period expected = new Period(1, PeriodType.standard());
 Period actual = period.toPeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriod() {
 MutablePeriod expected = new MutablePeriod().setFields(new FieldPosition(0, daysField), 1);
 MutablePeriod actual = period.toMutablePeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(1) {
 @Override
 public DurationFieldType getFieldType() {
 return daysField;
 }
 };
 assertTrue(period.equals(other));
 }

 @Test
 public void testEqualsDifferentClass() {
 assertFalse(period.equals(new Object()));
 }

 @Test
 public void testEqualsNull() {
 assertFalse(period.equals(null));
 }

 @Test
 public void testEqualsSameInstance() {
 assertTrue(period.equals(period));
 }

 @Test
 public void testEqualsSameValueDifferentInstance() {
 BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(1) {
 @Override
 public DurationFieldType getFieldType() {
 return daysField;
 }
 };
 assertTrue(period.equals(other));
 }

 @Test
 public void testEqualsDifferentValue() {
 BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(2) {
 @Override
 public DurationFieldType getFieldType() {
 return daysField;
 }
 };
 assertFalse(period.equals(other));
 }

 @Test
 public void testHashCode() {
 int expected = 27 * (27 * 17 + 1) + daysField.hashCode();
 int actual = period.hashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testCompareTo() {
 BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(2) {
 @Override
 public DurationFieldType getFieldType() {
 return daysField;
 }
 };
 assertEquals(1, period.compareTo(other));
 }

 @Test
 public void testCompareToSameInstance() {
 assertEquals(0, period.compareTo(period));
 }

 @Test
 public void testCompareToZero() {
 assertEquals(1, period.compareTo(Period.ZERO));
 }

 @Test
 public void testCompareToNegative() {
 BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(-1) {
 @Override
 public DurationFieldType getFieldType() {
 return daysField;
 }
 };
 assertEquals(-1, period.compareTo(other));
 }

 @Test
 public void testCompareToSameValue() {
 BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(1) {
 @Override
 public DurationFieldType getFieldType() {
 return daysField;
 }
 };
 assertEquals(0, period.compareTo(other));
 }

 @Test
 public void testIsSupported() {
 assertTrue(period.isSupported(daysField));
 }

 @Test
 public void testIsSupportedDifferentFieldType() {
 assertFalse(period.isSupported(minutesField));
 }

 @Test
 public void testGetValueFromPeriod() {
 Period periodToTest = new Period(2, PeriodType.standard());
 int expected = 2;
 int actual = periodToTest.getValue(daysField);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetValueFromPeriodWithOffset() {
 Period periodToTest = new Period(2, PeriodType.standard()).withFieldsAdded(new FieldPosition(0, hoursField), 3);
 int expected = 72;
 int actual = periodToTest.getValue(daysField);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetValueFromMutablePeriod() {
 MutablePeriod mutablePeriodToTest = new MutablePeriod().setFields(new FieldPosition(0, daysField), 2);
 int expected = 2;
 int actual = mutablePeriodToTest.getValue(daysField);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetValueFromMutablePeriodWithOffset() {
 MutablePeriod mutablePeriodToTest = new MutablePeriod().setFields(new FieldPosition(0, hoursField), 3).add(period);
 int expected = 3 + 1;
 int actual = mutablePeriodToTest.getValue(daysField);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetValueFromJodaLocalDate() {
 LocalDate date1 = new LocalDate(2022, 10, 1);
 LocalDate date2 = date1.plus(period);
 int expected = Days.daysBetween(date1, date2).getDays();
 int actual = period.getValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetValueFromJodaLocalDateTime() {
 LocalDateTime dateTime1 = new LocalDateTime(2022, 10, 1, 12, 30, 0);
 LocalDateTime dateTime2 = dateTime1.plus(period);
 int expected = ChronoUnit.DAYS.between(dateTime1.toInstant(), dateTime2.toInstant());
 int actual = period.getValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetValueFromJodaInstant() {
 Instant instant1 = new Instant(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
 Instant instant2 = instant1.plus(Period.fromStandardDays(1));
 long expectedMillis = instant2.getMillis() - instant1.getMillis();
 long actualMillis = period.get(Chronology.getInstanceUTC(), new Instant(instant1.getMillis() + 63072000000L), new Instant(instant2.getMillis()));
 assertEquals(expectedMillis, actualMillis * 1000);
 }

 @Test
 public void testGetValueFromJodaReadableInstant() {
 ReadableInstant readableInstant1 = Instant.now();
 ReadableInstant readableInstant2 = readableInstant1.plus(Period.fromStandardDays(1));
 int expected = (int)(readableInstant2.getMillis() - readableInstant1.getMillis()) / (1000 * 60 * 60 * 24);
 int actual = period.get(DateTimeUtils.getInstantChronology(readableInstant1), readableInstant1, readableInstant2)[0];
 assertEquals(expected, actual);
 }

 @Test
 public void testGetValueFromJodaReadablePartial() {
 ReadablePartial readablePartial1 = new BaseDateTime(2022, 10, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toReadablePartial();
 ReadablePartial readablePartial2 = readablePartial1.plus(period);
 int expected = Period.standard().getDays();
 int actual = period.get(readablePartial1, readablePartial2, Period.standard())[0];
 assertEquals(expected, actual);
 }

 @Test
 public void testBetweenTwoReadableInstant() {
 ReadableInstant readableInstant1 = Instant.now();
 ReadableInstant readableInstant2 = Instant.now().plus(Duration.ofMinutes(2));
 int expected = daysField.getField(ISOChronology.getInstanceUTC()).getDifference(readableInstant2.getMillis(), readableInstant1.getMillis());
 int actual = BaseSingleFieldPeriod.between(readableInstant1, readableInstant2, daysField);
 assertEquals(expected, actual);
 }

 @Test
 public void testBetweenTwoReadablePartial() {
 ReadablePartial readablePartial1 = new BaseDateTime(2022, 10, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toReadablePartial();
 ReadablePartial readablePartial2 = readablePartial1.plus(Period.ofMinutes(2).withFieldsRemoved(MinutesField.instance()));
 int expected = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, daysField);
 int actual = period.get(DateTimeUtils.getChronology(readablePartial1.getChronology()).withUTC(), readablePartial1, readablePartial2)[0];
 assertEquals(expected, actual);
 }

 @Test
 public void testStandardPeriodIn() {
 Period periodToTest = new Period(2, PeriodType.standard());
 long expected = 2 * 24 * 60 * 60 * 1000;
 long actual = BaseSingleFieldPeriod.standardPeriodIn(periodToTest, 24 * 60 * 60 * 1000);
 assertEquals(expected, actual);
 }
}
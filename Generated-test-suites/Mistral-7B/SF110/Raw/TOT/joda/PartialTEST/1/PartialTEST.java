package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

 private static final Chronology UTC = DateTimeUtils.UTC;

 @Test
 public void testEmptyPartial() {
 Partial emptyPartial = new Partial();
 assertEquals(0, emptyPartial.size());
 assertNull(emptyPartial.getFormatter());
 }

 @Test
 public void testPartialWithOneField() {
 DateTimeFieldType dayOfYear = DateTimeFieldType.dayOfYear();
 Partial partial1 = new Partial(dayOfYear, 31);
 assertEquals(1, partial1.size());
 assertEquals(dayOfYear, partial1.getFieldType(0));
 assertEquals(31, partial1.getValue(0));
 assertNull(partial1.getFormatter());

 DateTimeFormatter formatter = DateTimeFormat.forFields(partial1.getFieldTypes());
 Partial partial1Formatted = new Partial(formatter.parseLocalDate("2022-01-31").toLocalDateTime(UTC).toDateTime(UTC));
 assertEquals(partial1, partial1Formatted);
 }

 @Test
 public void testPartialWithTwoFields() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 Partial partial2 = new Partial(dayOfMonth, 28, hourOfDay, 14);
 assertEquals(2, partial2.size());
 assertEquals(dayOfMonth, partial2.getFieldType(0));
 assertEquals(28, partial2.getValue(0));
 assertEquals(hourOfDay, partial2.getFieldType(1));
 assertEquals(14, partial2.getValue(1));
 assertNull(partial2.getFormatter());

 DateTimeFormatter formatter = DateTimeFormat.forFields(partial2.getFieldTypes());
 Partial partial2Formatted = new Partial(formatter.parseLocalDateTime("2022-02-28T14:00:00.000Z").toLocalDateTime(UTC));
 assertEquals(partial2, partial2Formatted);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFieldTypeArrayLength() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 Partial partial3 = new Partial(dayOfMonth, 31, hourOfDay, 14, new DateTimeFieldType[]{null});
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValuesArrayLength() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 Partial partial4 = new Partial(dayOfMonth, new int[]{31, 29}, hourOfDay, 14);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFieldTypesOrder() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial5 = new Partial(new DateTimeFieldType[]{minuteOfHour, dayOfMonth, hourOfDay});
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRangeDurationType() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial6 = new Partial(new DateTimeFieldType[]{dayOfMonth, hourOfDay, minuteOfHour}, new int[]{31, 14, 59}, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testWithChronologyRetainFields() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 Partial partial7 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 Partial partial8 = partial7.withChronologyRetainFields(DateTimeUtils.GREGORIAN);
 assertEquals(partial7, partial8);
 }

 @Test
 public void testWith() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial9 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 Partial partial10 = partial9.with(minuteOfHour, 15);
 assertEquals(2, partial10.size());
 assertEquals(dayOfMonth, partial10.getFieldType(0));
 assertEquals(31, partial10.getValue(0));
 assertEquals(minuteOfHour, partial10.getFieldType(1));
 assertEquals(15, partial10.getValue(1));
 }

 @Test
 public void testWithout() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial11 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 Partial partial12 = partial11.without(minuteOfHour);
 assertEquals(1, partial12.size());
 assertEquals(dayOfMonth, partial12.getFieldType(0));
 assertEquals(31, partial12.getValue(0));
 }

 @Test
 public void testWithField() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial13 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 Partial partial14 = partial13.withField(minuteOfHour, 59);
 assertEquals(2, partial14.size());
 assertEquals(dayOfMonth, partial14.getFieldType(0));
 assertEquals(31, partial14.getValue(0));
 assertEquals(minuteOfHour, partial14.getFieldType(1));
 assertEquals(59, partial14.getValue(1));
 }

 @Test
 public void testWithFieldAdded() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 DateTimeFieldType secondOfMinute = DateTimeFieldType.secondOfMinute();
 Partial partial15 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 Partial partial16 = partial15.withFieldAdded(secondOfMinute, 30);
 assertEquals(3, partial16.size());
 assertEquals(dayOfMonth, partial16.getFieldType(0));
 assertEquals(31, partial16.getValue(0));
 assertEquals(hourOfDay, partial16.getFieldType(1));
 assertEquals(14, partial16.getValue(1));
 assertEquals(secondOfMinute, partial16.getFieldType(2));
 assertEquals(30, partial16.getValue(2));
 }

 @Test
 public void testWithFieldAddWrapped() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 DateTimeFieldType secondOfMinute = DateTimeFieldType.secondOfMinute();
 DateTimeFieldType millisecondOfSecond = DateTimeFieldType.millisecondOfSecond();
 Partial partial17 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 Partial partial18 = partial17.withFieldAddWrapped(millisecondOfSecond, 500);
 assertEquals(3, partial18.size());
 assertEquals(dayOfMonth, partial18.getFieldType(0));
 assertEquals(31, partial18.getValue(0));
 assertEquals(hourOfDay, partial18.getFieldType(1));
 assertEquals(14, partial18.getValue(1));
 assertEquals(millisecondOfSecond, partial18.getFieldType(2));
 assertEquals(500, partial18.getValue(2));
 }

 @Test
 public void testWithPeriodAdded() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial19 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 Period period = new Period(2, 3, 4);
 Partial partial20 = partial19.withPeriodAdded(period, 1);
 assertEquals(3, partial20.size());
 assertEquals(dayOfMonth, partial20.getFieldType(0));
 assertEquals(33, partial20.getValue(0));
 assertEquals(hourOfDay, partial20.getFieldType(1));
 assertEquals(15, partial20.getValue(1));
 assertEquals(minuteOfHour, partial20.getFieldType(2));
 assertEquals(4, partial20.getValue(2));
 }

 @Test
 public void testPlus() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial21 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 Partial partial22 = Partial.parse("P2D3H4M");
 Partial partial23 = partial21.plus(partial22);
 assertEquals(3, partial23.size());
 assertEquals(dayOfMonth, partial23.getFieldType(0));
 assertEquals(33, partial23.getValue(0));
 assertEquals(hourOfDay, partial23.getFieldType(1));
 assertEquals(16, partial23.getValue(1));
 assertEquals(minuteOfHour, partial23.getFieldType(2));
 assertEquals(4, partial23.getValue(2));
 }

 @Test
 public void testMinus() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial24 = new Partial(dayOfMonth, 33, hourOfDay, 16, UTC);
 Partial partial25 = partial24.minus(Partial.parse("P1D2H1M"));
 assertEquals(3, partial25.size());
 assertEquals(dayOfMonth, partial25.getFieldType(0));
 assertEquals(31, partial25.getValue(0));
 assertEquals(hourOfDay, partial25.getFieldType(1));
 assertEquals(13, partial25.getValue(1));
 assertEquals(minuteOfHour, partial25.getFieldType(2));
 assertEquals(54, partial25.getValue(2));
 }

 @Test
 public void testIsMatch() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 LocalDate date = new LocalDate(2022, 2, 28);
 LocalDateTime localDateTime = new LocalDateTime(date.toDate().getTime(), new LocalTime(14, 15, 30));
 Partial partial26 = new Partial(dayOfMonth, 28, hourOfDay, 14, UTC);
 Partial partial27 = new Partial(dayOfMonth, 28, hourOfDay, 14, UTC);
 assertTrue(partial26.isMatch(localDateTime));
 assertTrue(partial27.isMatch(localDateTime));

 Partial partial28 = new Partial(dayOfMonth, 29, hourOfDay, 14, UTC);
 Partial partial29 = new Partial(dayOfMonth, 30, hourOfDay, 14, UTC);
 Partial partial30 = new Partial(minuteOfHour, 20, UTC);
 assertFalse(partial26.isMatch(partial28));
 assertFalse(partial26.isMatch(partial29));
 assertFalse(partial26.isMatch(partial30));
 }

 @Test
 public void testIsMatchWithReadablePartial() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 LocalDate date = new LocalDate(2022, 2, 28);
 LocalDateTime localDateTime = new LocalDateTime(date.toDate().getTime(), new LocalTime(14, 15, 30));
 Partial partial31 = new Partial(dayOfMonth, 28, hourOfDay, 14, UTC);
 ReadablePartial readablePartial = new BaseDateTime(date.getTime(), UTC).toLocalDateTime().toLocalDate().toPartial();
 assertEquals(partial31, readablePartial);
 assertTrue(partial31.isMatch(localDateTime));
 assertTrue(partial31.isMatch(readablePartial));
 }

 @Test
 public void testProperty() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial32 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 Property property1 = partial32.property(dayOfMonth);
 assertEquals(dayOfMonth, property1.getField());
 assertEquals(31, property1.get());
 assertTrue(property1 instanceof Partial.Property);

 Property property2 = partial32.property(hourOfDay);
 assertEquals(hourOfDay, property2.getField());
 assertEquals(14, property2.get());
 assertTrue(property2 instanceof Partial.Property);
 }

 @Test
 public void testToString() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial33 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 assertEquals("[dayOfMonth=31, hourOfDay=14]", partial33.toString());
 }

 @Test
 public void testToStringList() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial34 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 assertEquals("[dayOfMonth=31, hourOfDay=14]", partial34.toStringList());
 }

 @Test
 public void testToStringFormat() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial35 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 assertEquals("dayOfMonth=31, hourOfDay=14", partial35.toString("dd,HH"));
 }

 @Test
 public void testToStringFormatLocale() {
 DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
 DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
 Partial partial36 = new Partial(dayOfMonth, 31, hourOfDay, 14, UTC);
 assertEquals("31. January 2022, 14:00:00", partial36.toString("dd.MMM.yyyy, HH:mm:ss", Locale.ENGLISH));
 }
}
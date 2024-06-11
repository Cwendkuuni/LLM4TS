package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypeFactory;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldType;
import org.joda.time.LocalDateTimeFieldTypeFactory;
import org.joda.time.MutableDateTime;
import org.joda.time.Partial;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

 @Test
 public void testEmptyPartial() {
 Partial emptyPartial = new Partial();
 assertEquals(0, emptyPartial.size());
 assertTrue(emptyPartial.isMatch(new LocalDateTime()));
 }

 @Test
 public void testPartialWithOneField() {
 Partial oneFieldPartial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
 assertEquals(1, oneFieldPartial.size());
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, oneFieldPartial.getFieldTypes()[0]);
 assertEquals(12, oneFieldPartial.getValue(0));
 assertTrue(oneFieldPartial.isMatch(new LocalDateTime(2022, 1, 1, 12, 0)));
 }

 @Test
 public void testPartialWithMultipleFields() {
 Partial multipleFieldsPartial = new Partial(
 DateTimeFieldType.HOUR_OF_DAY, 12,
 DateTimeFieldType.MINUTE_OF_HOUR, 30,
 DateTimeFieldType.SECOND_OF_MINUTE, 15
 );
 assertEquals(3, multipleFieldsPartial.size());
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, multipleFieldsPartial.getFieldTypes()[0]);
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, multipleFieldsPartial.getFieldTypes()[1]);
 assertEquals(DateTimeFieldType.SECOND_OF_MINUTE, multipleFieldsPartial.getFieldTypes()[2]);
 assertEquals(12, multipleFieldsPartial.getValue(0));
 assertEquals(30, multipleFieldsPartial.getValue(1));
 assertEquals(15, multipleFieldsPartial.getValue(2));
 assertTrue(multipleFieldsPartial.isMatch(new LocalDateTime(2022, 1, 1, 12, 30, 15)));
 }

 @Test
 public void testPartialWithChronology() {
 Chronology isoChronology = DateTimeUtils.getISOChronology();
 Partial isoPartial = new Partial(isoChronology, new DateTimeFieldType[] {
 DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE_OF_HOUR, DateTimeFieldType.SECOND_OF_MINUTE
 }, new int[] { 12, 30, 15 });
 assertEquals(3, isoPartial.size());
 assertTrue(isoPartial.isMatch(new LocalDateTime(isoChronology, 2022, 1, 1, 12, 30, 15)));
 }

 @Test
 public void testPartialWithNullFieldType() {
 try {
 new Partial(null, 12);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithNullValues() {
 try {
 new Partial(DateTimeFieldType.HOUR_OF_DAY, null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithDifferentFieldsLength() {
 try {
 new Partial(
 new DateTimeFieldType[] { DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE_OF_HOUR, DateTimeFieldType.SECOND_OF_MINUTE },
 new int[] { 12, 30 }
 );
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithInvalidFields() {
 try {
 new Partial(
 new DateTimeFieldType[] {
 DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.HOUR_OF_DAY
 },
 new int[] { 12, 31 }
 );
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithInvalidFieldOrder() {
 try {
 new Partial(
 new DateTimeFieldType[] { DateTimeFieldType.MINUTE_OF_HOUR, DateTimeFieldType.HOUR_OF_DAY },
 new int[] { 12, 30 }
 );
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithInvalidRangeDurationType() {
 try {
 new Partial(
 new DateTimeFieldType[] { LocalDateTimeFieldType.HOUR_OF_DAY, LocalDateTimeFieldType.MINUTE_OF_HOUR },
 new int[] { 23, 60 }
 );
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithInvalidRangeDurationTypeValues() {
 try {
 new Partial(
 new DateTimeFieldType[] {
 LocalDateTimeFieldType.HOUR_OF_DAY, LocalDateTimeFieldType.MINUTE_OF_HOUR
 },
 new int[] { 23, 61 }
 );
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithInvalidRangeDurationTypeOrder() {
 try {
 new Partial(
 new DateTimeFieldType[] { LocalDateTimeFieldType.MINUTE_OF_HOUR, LocalDateTimeFieldType.HOUR_OF_DAY },
 new int[] { 60, 23 }
 );
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithSameRangeDurationType() {
 try {
 new Partial(
 new DateTimeFieldType[] {
 LocalDateTimeFieldType.HOUR_OF_DAY, LocalDateTimeFieldType.HOUR_OF_DAY
 },
 new int[] { 12, 13 }
 );
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithReadablePartial() {
 Partial readablePartial = new LocalDateTime(2022, 1, 1, 12, 30, 15);
 Partial partialFromReadablePartial = new Partial(readablePartial);
 assertEquals(3, partialFromReadablePartial.size());
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialFromReadablePartial.getFieldTypes()[0]);
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, partialFromReadablePartial.getFieldTypes()[1]);
 assertEquals(DateTimeFieldType.SECOND_OF_MINUTE, partialFromReadablePartial.getFieldTypes()[2]);
 assertEquals(12, partialFromReadablePartial.getValue(0));
 assertEquals(30, partialFromReadablePartial.getValue(1));
 assertEquals(15, partialFromReadablePartial.getValue(2));
 assertTrue(partialFromReadablePartial.isMatch(readablePartial));
 }

 @Test
 public void testPartialWithCustomFormatter() {
 Partial customFormattedPartial = new Partial(
 DateTimeFieldType.HOUR_OF_DAY, 12,
 DateTimeFieldType.MINUTE_OF_HOUR, 30,
 DateTimeFieldType.SECOND_OF_MINUTE, 15
 );
 DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
 String expectedString = "12:30:15";
 assertEquals(expectedString, customFormattedPartial.toString(formatter));
 }

 @Test
 public void testPartialWithCustomLocale() {
 Partial customLocalePartial = new Partial(
 DateTimeFieldType.HOUR_OF_DAY, 12,
 DateTimeFieldType.MINUTE_OF_HOUR, 30,
 DateTimeFieldType.SECOND_OF_MINUTE, 15
 );
 DateTimeFormat format = DateTimeFormat.forPattern("HH:mm:ss");
 Locale locale = new Locale("es", "ES");
 String expectedString = "12:30:15";
 assertEquals(expectedString, customLocalePartial.toString(format, locale));
 }

 @Test
 public void testPartialWithCustomLocaleAndPattern() {
 Partial customLocaleAndPatternPartial = new Partial(
 DateTimeFieldType.HOUR_OF_DAY, 12,
 DateTimeFieldType.MINUTE_OF_HOUR, 30,
 DateTimeFieldType.SECOND_OF_MINUTE, 15
 );
 DateTimeFormat format = DateTimeFormat.forPattern("hh:mm:ss a");
 Locale locale = new Locale("es", "ES");
 String expectedString = "12:30:15 pm";
 assertEquals(expectedString, customLocaleAndPatternPartial.toString(format, locale));
 }

 @Test
 public void testPartialWithAddingPeriod() {
 Partial basePartial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
 ReadablePeriod period = BaseDateTime.parse("P1D", null).withFieldAdded(DateTimeFieldType.DAY_OF_MONTH, 1);
 Partial partialWithAddedPeriod = basePartial.withPeriodAdded(period, 1);
 assertEquals(2, partialWithAddedPeriod.size());
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithAddedPeriod.getFieldTypes()[0]);
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partialWithAddedPeriod.getFieldTypes()[1]);
 assertEquals(13, partialWithAddedPeriod.getValue(0));
 assertEquals(1, partialWithAddedPeriod.getValue(1));
 assertTrue(partialWithAddedPeriod.isMatch(new LocalDateTime(2022, 1, 2, 13, 0)));
 }

 @Test
 public void testPartialWithAddingWrappedPeriod() {
 Partial basePartial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
 ReadablePeriod period = BaseDateTime.parse("P1D", null).withFieldAdded(DateTimeFieldType.DAY_OF_MONTH, 1);
 Partial partialWithAddedWrappedPeriod = basePartial.withFieldAddWrapped(DurationFieldTypeFactory.instance().getFieldType(DateTimeFieldType.DAY_OF_MONTH), 1);
 Partial partialWithAddedPeriod = new Partial(partialWithAddedWrappedPeriod);
 assertEquals(2, partialWithAddedPeriod.size());
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithAddedPeriod.getFieldTypes()[0]);
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partialWithAddedPeriod.getFieldTypes()[1]);
 assertEquals(13, partialWithAddedPeriod.getValue(0));
 assertEquals(1, partialWithAddedPeriod.getValue(1));
 assertTrue(partialWithAddedPeriod.isMatch(new LocalDateTime(2022, 1, 2, 13, 0)));
 }
}
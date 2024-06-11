package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.Partial;
import org.joda.time.format.ISODateTimeFormat;
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
 public void testSingleFieldPartial() {
 Partial singleFieldPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 5);
 assertEquals(1, singleFieldPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, singleFieldPartial.getFieldTypes()[0]);
 assertEquals(5, singleFieldPartial.getValue(0));

 LocalDateTime baseDateTime = new LocalDateTime();
 baseDateTime = baseDateTime.withDayOfMonth(5);
 assertTrue(singleFieldPartial.isMatch(baseDateTime));
 }

 @Test
 public void testMultipleFieldsPartial() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 13, DateTimeFieldType.MINUTE_OF_HOUR, 30);
 assertEquals(2, multipleFieldsPartial.size());
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, multipleFieldsPartial.getFieldTypes()[0]);
 assertEquals(13, multipleFieldsPartial.getValue(0));
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, multipleFieldsPartial.getFieldTypes()[1]);
 assertEquals(30, multipleFieldsPartial.getValue(1));

 LocalDateTime baseDateTime = new LocalDateTime();
 baseDateTime = baseDateTime.withHourOfDay(13).withMinuteOfHour(30);
 assertTrue(multipleFieldsPartial.isMatch(baseDateTime));
 }

 @Test
 public void testInvalidFieldsPartial() {
 try {
 Partial invalidFieldsPartial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 25);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 Partial invalidFieldsPartial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 13, DateTimeFieldType.MINUTE_OF_HOUR, 61);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testChronologyChange() {
 Partial partial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 1, DateTimeUtils.UTC);
 assertEquals(1, partial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partial.getFieldTypes()[0]);
 assertEquals(1, partial.getValue(0));

 Partial partialWithChronology = partial.withChronologyRetainFields(DateTimeUtils.GREGORIAN);
 assertEquals(1, partialWithChronology.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partialWithChronology.getFieldTypes()[0]);
 assertEquals(1, partialWithChronology.getValue(0));

 assertTrue(partial.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC)));
 assertTrue(partialWithChronology.isMatch(new BaseDateTime(1L, DateTimeUtils.GREGORIAN)));
 }

 @Test
 public void testFieldChange() {
 Partial partial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 1, DateTimeFieldType.HOUR_OF_DAY, 12);
 assertEquals(2, partial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partial.getFieldTypes()[0]);
 assertEquals(1, partial.getValue(0));
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partial.getFieldTypes()[1]);
 assertEquals(12, partial.getValue(1));

 Partial partialWithFieldChange = partial.with(DateTimeFieldType.HOUR_OF_DAY, 13);
 assertEquals(2, partialWithFieldChange.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partialWithFieldChange.getFieldTypes()[0]);
 assertEquals(1, partialWithFieldChange.getValue(0));
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithFieldChange.getFieldTypes()[1]);
 assertEquals(13, partialWithFieldChange.getValue(1));

 assertTrue(partial.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC).withDayOfMonth(1).withHourOfDay(12)));
 assertTrue(partialWithFieldChange.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC).withDayOfMonth(1).withHourOfDay(13)));
 }

 @Test
 public void testFieldAddition() {
 Partial partial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 1, DateTimeFieldType.HOUR_OF_DAY, 12);
 assertEquals(2, partial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partial.getFieldTypes()[0]);
 assertEquals(1, partial.getValue(0));
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partial.getFieldTypes()[1]);
 assertEquals(12, partial.getValue(1));

 Partial partialWithFieldAdded = partial.withFieldAdded(DateTimeFieldType.MINUTE_OF_HOUR, 30);
 assertEquals(3, partialWithFieldAdded.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partialWithFieldAdded.getFieldTypes()[0]);
 assertEquals(1, partialWithFieldAdded.getValue(0));
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithFieldAdded.getFieldTypes()[1]);
 assertEquals(12, partialWithFieldAdded.getValue(1));
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, partialWithFieldAdded.getFieldTypes()[2]);
 assertEquals(30, partialWithFieldAdded.getValue(2));

 assertTrue(partial.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC).withDayOfMonth(1).withHourOfDay(12)));
 assertTrue(partialWithFieldAdded.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC).withDayOfMonth(1).withHourOfDay(12).withMinuteOfHour(30)));
 }

 @Test
 public void testFieldAdditionWrapPartial() {
 Partial partial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 1, DateTimeFieldType.HOUR_OF_DAY, 23);
 assertEquals(2, partial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partial.getFieldTypes()[0]);
 assertEquals(1, partial.getValue(0));
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partial.getFieldTypes()[1]);
 assertEquals(23, partial.getValue(1));

 Partial partialWithFieldAddWrapped = partial.withFieldAddWrapped(DateTimeFieldType.MINUTE_OF_HOUR, 45);
 assertEquals(3, partialWithFieldAddWrapped.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partialWithFieldAddWrapped.getFieldTypes()[0]);
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithFieldAddWrapped.getFieldTypes()[1]);
 assertEquals(1, partialWithFieldAddWrapped.getValue(0));
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, partialWithFieldAddWrapped.getFieldTypes()[2]);
 assertEquals(45, partialWithFieldAddWrapped.getValue(2));

 assertTrue(partial.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC).withDayOfMonth(1).withHourOfDay(23)));
 assertTrue(partialWithFieldAddWrapped.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC).withDayOfMonth(2).withHourOfDay(1).withMinuteOfHour(45)));
 }

 @Test
 public void testPeriodAddition() {
 LocalDateTime baseDateTime = new LocalDateTime();
 Partial partial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 1, DateTimeFieldType.HOUR_OF_DAY, 12);
 ReadablePeriod readablePeriod = ISODateTimeFormat.parsePeriod("PT1H30M");

 Partial partialWithPeriodAdded = partial.withPeriodAdded(readablePeriod, 1);
 assertEquals(2, partialWithPeriodAdded.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partialWithPeriodAdded.getFieldTypes()[0]);
 assertEquals(1, partialWithPeriodAdded.getValue(0));
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithPeriodAdded.getFieldTypes()[1]);
 assertEquals(13, partialWithPeriodAdded.getValue(1));

 assertTrue(partial.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC).withDayOfMonth(1).withHourOfDay(12)));
 assertTrue(partialWithPeriodAdded.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC).withDayOfMonth(1).withHourOfDay(13).withMinuteOfHour(30)));
 }

 @Test
 public void testPeriodSubtraction() {
 LocalDateTime baseDateTime = new LocalDateTime();
 Partial partial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 1, DateTimeFieldType.HOUR_OF_DAY, 13, DateTimeFieldType.MINUTE_OF_HOUR, 30);
 ReadablePeriod readablePeriod = ISODateTimeFormat.parsePeriod("PT1H30M");

 Partial partialWithPeriodSubtracted = partial.minus(readablePeriod);
 assertEquals(2, partialWithPeriodSubtracted.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partialWithPeriodSubtracted.getFieldTypes()[0]);
 assertEquals(1, partialWithPeriodSubtracted.getValue(0));
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithPeriodSubtracted.getFieldTypes()[1]);
 assertEquals(12, partialWithPeriodSubtracted.getValue(1));

 assertTrue(partial.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC).withDayOfMonth(1).withHourOfDay(13).withMinuteOfHour(30)));
 assertTrue(partialWithPeriodSubtracted.isMatch(new BaseDateTime(1L, DateTimeUtils.UTC).withDayOfMonth(1).withHourOfDay(12)));
 }
}
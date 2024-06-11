package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.Period;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

 @Test
 public void testEmptyPartial() {
 Partial emptyPartial = new Partial();
 assertTrue(emptyPartial.size() == 0);
 assertNull(emptyPartial.getFormatter());
 assertTrue(emptyPartial.isMatch(new BaseDateTime()));
 }

 @Test
 public void testSingleFieldPartial() {
 Partial singleFieldPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30);
 assertEquals(1, singleFieldPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, singleFieldPartial.getFieldType(0));
 assertEquals(30, singleFieldPartial.getValue(0));
 LocalDateTimeFieldVerifier.assertIsValid(singleFieldPartial, new LocalDateTime(2022, 10, 30, 0, 0, 0, 0));
 }

 @Test
 public void testMultipleFieldsPartial() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY);
 assertEquals(2, multipleFieldsPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, multipleFieldsPartial.getFieldType(0));
 assertEquals(30, multipleFieldsPartial.getValue(0));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, multipleFieldsPartial.getFieldType(1));
 assertEquals(5, multipleFieldsPartial.getValue(1));
 LocalDateTimeFieldVerifier.assertIsValid(multipleFieldsPartial, new LocalDateTime(2022, 5, 30, 0, 0, 0, 0));
 }

 @Test
 public void testInvalidFieldsOrder() {
 try {
 Partial invalidFieldsOrderPartial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12, DateTimeFieldType.MINUTE_OF_HOUR, DateTimeFieldType.SECOND_OF_MINUTE, DateTimeFieldType.MILLISECOND_OF_SECOND);
 fail("Should not reach this point as the fields order is not valid");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFormatting() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY);
 DateTimeFormatter formatter = multipleFieldsPartial.getFormatter();
 assertEquals("30-05", formatter.print(multipleFieldsPartial));
 }

 @Test
 public void testInvalidValue() {
 try {
 Partial invalidValuePartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 33);
 fail("Should not reach this point as the value is not valid for the field type");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetChronology() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY);
 assertEquals(DateTimeUtils.UTC, multipleFieldsPartial.getChronology());
 }

 @Test
 public void testWithChronologyRetainFields() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY, DateTimeUtils.UTC);
 Partial withChronologyRetainFieldsPartial = multipleFieldsPartial.withChronologyRetainFields(DateTimeUtils.GREGORIAN);
 assertEquals(DateTimeUtils.GREGORIAN, withChronologyRetainFieldsPartial.getChronology());
 assertEquals(3, withChronologyRetainFieldsPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, withChronologyRetainFieldsPartial.getFieldType(0));
 assertEquals(30, withChronologyRetainFieldsPartial.getValue(0));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, withChronologyRetainFieldsPartial.getFieldType(1));
 assertEquals(5, withChronologyRetainFieldsPartial.getValue(1));
 }

 @Test
 public void testWith() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY, DateTimeUtils.UTC);
 Partial withPartial = multipleFieldsPartial.with(DateTimeFieldType.DAY_OF_MONTH, 31);
 assertEquals(2, withPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, withPartial.getFieldType(0));
 assertEquals(31, withPartial.getValue(0));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, withPartial.getFieldType(1));
 assertEquals(5, withPartial.getValue(1));
 LocalDateTimeFieldVerifier.assertIsValid(withPartial, new LocalDateTime(2022, 5, 31, 0, 0, 0, 0));
 }

 @Test
 public void testWithout() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY, DateTimeUtils.UTC);
 Partial withoutPartial = multipleFieldsPartial.without(DateTimeFieldType.MONTH_OF_YEAR);
 assertEquals(1, withoutPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, withoutPartial.getFieldType(0));
 assertEquals(30, withoutPartial.getValue(0));
 }

 @Test
 public void testWithField() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY, DateTimeUtils.UTC);
 Partial withFieldPartial = multipleFieldsPartial.withField(DateTimeFieldType.MONTH_OF_YEAR, 6);
 assertEquals(2, withFieldPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, withFieldPartial.getFieldType(0));
 assertEquals(30, withFieldPartial.getValue(0));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, withFieldPartial.getFieldType(1));
 assertEquals(6, withFieldPartial.getValue(1));
 LocalDateTimeFieldVerifier.assertIsValid(withFieldPartial, new LocalDateTime(2022, 6, 30, 0, 0, 0, 0));
 }

 @Test
 public void testWithFieldAdded() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY, DateTimeUtils.UTC);
 Partial withFieldAddedPartial = multipleFieldsPartial.withFieldAdded(DurationFieldType.MILLIS_PER_DAY, 3600 * 1000 * 24);
 assertEquals(3, withFieldAddedPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, withFieldAddedPartial.getFieldType(0));
 assertEquals(30, withFieldAddedPartial.getValue(0));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, withFieldAddedPartial.getFieldType(1));
 assertEquals(5, withFieldAddedPartial.getValue(1));
 assertEquals(DateTimeFieldType.MILLISECOND_OF_SECOND, withFieldAddedPartial.getFieldType(2));
 assertEquals(86400 * 2, withFieldAddedPartial.getValue(2));
 LocalDateTimeFieldVerifier.assertIsValid(withFieldAddedPartial, new LocalDateTime(2022, 5, 30, 12, 0, 2, 0));
 }

 @Test
 public void testWithFieldAddWrapped() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY, DateTimeUtils.UTC);
 Partial withFieldAddWrappedPartial = multipleFieldsPartial.withFieldAddWrapped(DurationFieldType.MILLISECOND_OF_SECOND, 3600 * 1000 * 24 * 30);
 assertEquals(3, withFieldAddWrappedPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, withFieldAddWrappedPartial.getFieldType(0));
 assertEquals(30, withFieldAddWrappedPartial.getValue(0));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, withFieldAddWrappedPartial.getFieldType(1));
 assertEquals(5, withFieldAddWrappedPartial.getValue(1));
 assertEquals(DateTimeFieldType.SECOND_OF_MINUTE, withFieldAddWrappedPartial.getFieldType(2));
 assertEquals(30 * 24 * 30, withFieldAddWrappedPartial.getValue(2));
 LocalDateTimeFieldVerifier.assertIsValid(withFieldAddWrappedPartial, new LocalDateTime(2022, 5, 30, 0, 30 * 24 * 30, 0, 0));
 }

 @Test
 public void testWithPeriodAdded() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY, DateTimeUtils.UTC);
 Period period = new Period(3, 2, 1, 1);
 Partial withPeriodAddedPartial = multipleFieldsPartial.withPeriodAdded(period, 1);
 assertEquals(3, withPeriodAddedPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, withPeriodAddedPartial.getFieldType(0));
 assertEquals(30 + 3, withPeriodAddedPartial.getValue(0));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, withPeriodAddedPartial.getFieldType(1));
 assertEquals(5, withPeriodAddedPartial.getValue(1));
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, withPeriodAddedPartial.getFieldType(2));
 assertEquals(2, withPeriodAddedPartial.getValue(2));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, withPeriodAddedPartial.getFieldType(3));
 assertEquals(1, withPeriodAddedPartial.getValue(3));
 LocalDateTimeFieldVerifier.assertIsValid(withPeriodAddedPartial, new LocalDateTime(2023, 6, 32, 1, 2, 1, 1));
 }

 @Test
 public void testPlus() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 30, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.MAY, DateTimeUtils.UTC);
 Partial plusPartial = multipleFieldsPartial.plus(new Period(3, 2, 1, 1));
 assertEquals(3, plusPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, plusPartial.getFieldType(0));
 assertEquals(30 + 3, plusPartial.getValue(0));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, plusPartial.getFieldType(1));
 assertEquals(5, plusPartial.getValue(1));
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, plusPartial.getFieldType(2));
 assertEquals(2, plusPartial.getValue(2));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, plusPartial.getFieldType(3));
 assertEquals(1, plusPartial.getValue(3));
 LocalDateTimeFieldVerifier.assertIsValid(plusPartial, new LocalDateTime(2023, 6, 32, 1, 2, 1, 1));
 }

 @Test
 public void testMinus() {
 Partial multipleFieldsPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 31, DateTimeFieldType.MONTH_OF_YEAR, DateTimeConstants.JUNE, DateTimeUtils.UTC);
 Partial minusPartial = multipleFieldsPartial.minus(new Period(3, 2, 1, 1));
 assertEquals(3, minusPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, minusPartial.getFieldType(0));
 assertEquals(30, minusPartial.getValue(0));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, minusPartial.getFieldType(1));
 assertEquals(5, minusPartial.getValue(1));
 LocalDateTimeFieldVerifier.assertIsValid(minusPartial, new LocalDateTime(2022, 5, 30, 0, 0, 0, 0));
 }

 @Test
 public void testParseFromString() {
 String dateString = "30-05";
 Partial parsedPartial = ISODateTimeFormat.forFields(Arrays.asList(DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR)).parsePartial(dateString);
 assertEquals(2, parsedPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, parsedPartial.getFieldType(0));
 assertEquals(30, parsedPartial.getValue(0));
 assertEquals(DateTimeFieldType.MONTH_OF_YEAR, parsedPartial.getFieldType(1));
 assertEquals(5, parsedPartial.getValue(1));
 }

 @Test
 public void testParseFromInvalidString() {
 try {
 ISODateTimeFormat.forFields(Arrays.asList(DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR)).parsePartial("InvalidDateString");
 fail("Should not reach this point as the string is not valid");
 } catch (ParseException e) {
 // Expected
 }
 }
}
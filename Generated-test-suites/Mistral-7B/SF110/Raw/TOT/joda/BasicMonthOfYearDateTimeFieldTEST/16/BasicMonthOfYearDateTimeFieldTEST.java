package org.joda.time.tests.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypeImpl;
import org.joda.time.ImmutableDate;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicGregorianChronology;
import org.joda.time.chrono.BasicLocaleChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static Chronology basicGregorianChronology = new BasicGregorianChronology();
 private static Chronology basicLocaleChronology = new BasicLocaleChronology("en-US", new BasicGregorianChronology());

 @Test
 public void testBasicMonthOfYearDateTimeFieldGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 LocalDateTime dateTime = new LocalDateTime(2012, 2, 28, 12, 30, 0).toDateTimeAtStartOfDay();
 long n = dateTime.getMillis();
 int expected = 2;
 int actual = field.get(n);
 assertEquals(expected, actual);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 LocalDateTime dateTime = new LocalDateTime(2012, 2, 28, 12, 30, 0).toDateTimeAtStartOfDay();
 long n = dateTime.getMillis();

 long n1 = field.add(n, 1);
 LocalDateTime dateTime1 = new LocalDateTime(2012, 3, 28, 12, 30, 0).toDateTimeAtStartOfDay();
 long expected1 = dateTime1.getMillis();
 assertEquals(expected1, n1);

 long n2 = field.add(n, -1);
 LocalDateTime dateTime2 = new LocalDateTime(2012, 1, 28, 12, 30, 0).toDateTimeAtStartOfDay();
 long expected2 = dateTime2.getMillis();
 assertEquals(expected2, n2);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldAddLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 LocalDateTime dateTime = new LocalDateTime(2012, 2, 28, 12, 30, 0).toDateTimeAtStartOfDay();
 long n = dateTime.getMillis();

 long n1 = field.add(n, 12);
 LocalDateTime dateTime1 = new LocalDateTime(2012, 4, 28, 12, 30, 0).toDateTimeAtStartOfDay();
 long expected1 = dateTime1.getMillis();
 assertEquals(expected1, n1);

 long n2 = field.add(n, -12);
 LocalDateTime dateTime2 = new LocalDateTime(2012, 1, 28, 12, 30, 0).toDateTimeAtStartOfDay();
 long expected2 = dateTime2.getMillis();
 assertEquals(expected2, n2);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 long n = 1234567890123L;
 int expected = 13;
 long actual = field.addWrapField(n, 11);
 assertEquals(expected, (int)actual);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 LocalDateTime dateTime = new LocalDateTime(2012, 2, 28, 12, 30, 0).toDateTimeAtStartOfDay();
 long n = dateTime.getMillis();
 int expected = 3;
 long actual = field.set(n, expected);
 LocalDateTime dateTime1 = new LocalDateTime(2012, 3, 28, 12, 30, 0).toDateTimeAtStartOfDay();
 long expected1 = dateTime1.getMillis();
 assertEquals(expected1, actual);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 LocalDateTime dateTime = new LocalDateTime(2012, 2, 29, 12, 30, 0).toDateTimeAtStartOfDay();
 long n = dateTime.getMillis();
 assertTrue(field.isLeap(n));
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 LocalDateTime dateTime = new LocalDateTime(2012, 2, 29, 12, 30, 0).toDateTimeAtStartOfDay();
 long n = dateTime.getMillis();
 int expected = 1;
 int actual = field.getLeapAmount(n);
 assertEquals(expected, actual);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 DurationFieldTypeImpl days = new DurationFieldTypeImpl("dayOfMonth", org.joda.time.field.DL.DAYS, 1, org.joda.time.DurationFieldType.INSTANCE, null);
 DurationField leapDurationField = field.getLeapDurationField();
 assertEquals(days, leapDurationField);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasicMonthOfYearDateTimeFieldAddInvalidAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 long n = 1234567890123L;
 int n2 = Integer.MAX_VALUE + 1;
 field.add(n, n2);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasicMonthOfYearDateTimeFieldAddInvalidLongAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 long n = 1234567890123L;
 long n2 = Long.MAX_VALUE + 1;
 field.add(n, n2);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasicMonthOfYearDateTimeFieldSetInvalidValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 long n = 1234567890123L;
 int n2 = 13 * 12 + 1;
 field.set(n, n2);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasicMonthOfYearDateTimeFieldSetInvalidLongValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 long n = 1234567890123L;
 long n2 = 13 * 12L + 1;
 field.set(n, n2);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldAddWithReadablePartial() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 LocalDateTime dateTime = new LocalDateTime(2012, 2, 28, 12, 30, 0).toDateTimeAtStartOfDay();
 long n = dateTime.getMillis();
 LocalDate localDate = new LocalDate(2012, 2, 28);
 int[] expected = {3, 1};
 int[] actual = field.add(localDate.toDateTimeAtStartOfDay(), 1, new int[] {2, 1}, 1).getValues();
 assertEquals(expected[0], actual[0]);
 assertEquals(expected[1], actual[1]);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicGregorianChronology, 2);
 DurationField years = field.getRangeDurationField();
 assertEquals(DurationFieldType.YEARS, years.getFieldType());
 }
}
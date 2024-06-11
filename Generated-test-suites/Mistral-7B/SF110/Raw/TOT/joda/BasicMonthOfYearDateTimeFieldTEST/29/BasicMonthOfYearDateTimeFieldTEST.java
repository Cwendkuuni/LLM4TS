package org.joda.time.chrono.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDateTime;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology ISO_CHRONOLOGY = BaseDateTime.UTC.getChronology();

 @Test
 public void testGetMinimumValueAndMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertEquals(1, field.getMinimumValue());
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertFalse(field.isLeap(new LocalDateTime(2000, 2, 28, 12, 30, 0, 0).getMillis()));
 assertFalse(field.isLeap(new LocalDateTime(2000, 2, 29, 12, 30, 0, 0).getMillis()));

 field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertTrue(field.isLeap(new LocalDateTime(2000, 2, 29, 12, 30, 0, 0).getMillis()));
 assertFalse(field.isLeap(new LocalDateTime(2000, 3, 1, 12, 30, 0, 0).getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertEquals(0, field.getLeapAmount(new LocalDateTime(2000, 2, 28, 12, 30, 0, 0).getMillis()));
 assertEquals(1, field.getLeapAmount(new LocalDateTime(2000, 2, 29, 12, 30, 0, 0).getMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertEquals(DurationFieldType.days(), field.getLeapDurationField());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 LocalDateTime date1 = new LocalDateTime(2000, 2, 28, 12, 30, 0, 0);
 LocalDateTime date2 = new LocalDateTime(2000, 3, 2, 12, 30, 0, 0);
 assertEquals(2, field.get(date1.getMillis()));
 assertEquals(3, field.get(date2.getMillis()));
 }

 @Test
 public void testAddMonth() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 LocalDateTime date = new LocalDateTime(2000, 2, 28, 12, 30, 0, 0);
 long expected = new LocalDateTime(2000, 3, 30, 12, 30, 0, 0).getMillis();
 long actual = field.add(date.getMillis(), 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddMonthWrongSign() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 LocalDateTime date = new LocalDateTime(2000, 2, 28, 12, 30, 0, 0);
 long expected = new LocalDateTime(1999, 11, 30, 12, 30, 0, 0).getMillis();
 long actual = field.add(date.getMillis(), -1);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddMonthWithLimit() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 LocalDateTime date = new LocalDateTime(2000, 12, 31, 12, 30, 0, 0);
 long expected = new LocalDateTime(2001, 1, 31, 12, 30, 0, 0).getMillis();
 long actual = field.add(date.getMillis(), 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddMonthWithLimitAndLeapYear() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 LocalDateTime date = new LocalDateTime(2000, 12, 31, 12, 30, 0, 0);
 long expected = new LocalDateTime(2001, 3, 1, 12, 30, 0, 0).getMillis();
 long actual = field.add(date.getMillis(), 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long date = new LocalDateTime(2000, 2, 28, 12, 30, 0, 0).getMillis();
 long expected = new LocalDateTime(2000, 3, 2, 12, 30, 0, 0).getMillis();
 long actual = field.set(date, 3);
 assertEquals(expected, actual);
 }

 @Test
 public void testSetInvalidValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long date = new LocalDateTime(2000, 2, 28, 12, 30, 0, 0).getMillis();
 try {
 field.set(date, 13);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long date = new LocalDateTime(2000, 12, 31, 12, 30, 0, 0).getMillis();
 long expected = new LocalDateTime(2001, 1, 1, 12, 30, 0, 0).getMillis();
 long actual = field.addWrapField(date, 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long date1 = new LocalDateTime(2000, 2, 28, 12, 30, 0, 0).getMillis();
 long date2 = new LocalDateTime(2000, 3, 31, 12, 30, 0, 0).getMillis();
 long expected = 30;
 long actual = field.getDifferenceAsLong(date1, date2);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWrapFieldWithNegativeSign() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long date1 = new LocalDateTime(2000, 12, 31, 12, 30, 0, 0).getMillis();
 long date2 = new LocalDateTime(2000, 11, 30, 12, 30, 0, 0).getMillis();
 long expected = -30;
 long actual = field.getDifferenceAsLong(date2, date1);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWrapFieldWithLimit() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long date1 = new LocalDateTime(2001, 1, 1, 12, 30, 0, 0).getMillis();
 long date2 = new LocalDateTime(2000, 12, 31, 12, 30, 0, 0).getMillis();
 long expected = -11;
 long actual = field.getDifferenceAsLong(date1, date2);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWrapFieldWithLeapYear() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long date1 = new LocalDateTime(2001, 3, 1, 12, 30, 0, 0).getMillis();
 long date2 = new LocalDateTime(2000, 12, 31, 12, 30, 0, 0).getMillis();
 long expected = -11;
 long actual = field.getDifferenceAsLong(date1, date2);
 assertEquals(expected, actual);
 }
}
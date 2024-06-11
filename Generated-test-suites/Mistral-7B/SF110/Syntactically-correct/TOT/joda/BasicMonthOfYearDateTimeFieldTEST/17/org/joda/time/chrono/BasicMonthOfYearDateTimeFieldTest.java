package org.joda.time.chrono.test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicChronology ISO_CHRONOLOGY = GregorianChronology.getInstance();

 @org.junit.Test
 public void testGetAndSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);

 // Get
 int initialMonth = field.get(DateTimeUtils.currentTimeMillis());
 assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
 assertEquals(ISO_CHRONOLOGY, field.getChronology());
 assertEquals(initialMonth, field.getValue());

 // Set
 int newMonth = 5;
 long newMillis = field.set(DateTimeUtils.currentTimeMillis(), newMonth);
 assertEquals(newMonth, field.getValue());
 assertEquals(newMillis, ISO_CHRONOLOGY.getYearMonthDayMillis(DateTimeUtils.getYear(newMillis), newMonth, 1));
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testSetOutOfBounds() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long n = ISO_CHRONOLOGY.getYearMonthDayMillis(2000, 13, 1);
 try {
 field.set(n, 14);
 }
 catch (IllegalArgumentException e) {
 assertEquals("Value out of valid range: 14", e.getMessage());
 }
 }

 @org.junit.Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);

 // Add one month
 long result = field.add(DateTimeUtils.currentTimeMillis(), 1);
 assertEquals(ISO_CHRONOLOGY, field.getChronology());
 assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
 assertEquals(1 + field.getValue(), field.getValue());
 assertEquals(1, FieldUtils.getWrappedValue(field.getValue(), 1, 1, ISO_CHRONOLOGY.getMaxMonth()));
 assertEquals(result, ISO_CHRONOLOGY.getYearMonthDayMillis(DateTimeUtils.getYear(result), 1 + DateTimeUtils.getMonthOfYear(result), 1));

 // Add 12 months
 long n = DateTimeUtils.currentTimeMillis();
 for (int i = 0; i < 12; i++) {
 result = field.add(n, 1);
 assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
 assertEquals(ISO_CHRONOLOGY, field.getChronology());
 assertEquals(13 + i, FieldUtils.getWrappedValue(field.getValue(), 1, 1, ISO_CHRONOLOGY.getMaxMonth()));
 assertEquals(result, ISO_CHRONOLOGY.getYearMonthDayMillis(DateTimeUtils.getYear(result), 13 + i, 1));
 }
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testAddOutOfBounds() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long n = ISO_CHRONOLOGY.getYearMonthDayMillis(2000, 13, 1);
 try {
 field.add(n, 14);
 }
 catch (IllegalArgumentException e) {
 assertEquals("Magnitude of add amount is too large: 14", e.getMessage());
 }
 }

 @org.junit.Test
 public void testAddWrap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long n = ISO_CHRONOLOGY.getYearMonthDayMillis(2000, 12, 31);
 long result = field.addWrapField(n, 13);
 assertEquals(ISO_CHRONOLOGY, field.getChronology());
 assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
 assertEquals(1, FieldUtils.getWrappedValue(field.getValue(), 1, 1, ISO_CHRONOLOGY.getMaxMonth()));
 assertEquals(DateTimeUtils.getYear(result), DateTimeUtils.getYear(n) + 1);
 assertEquals(DateTimeUtils.getMonthOfYear(result), 1);
 }

 @org.junit.Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertTrue(field.isLeap(ISO_CHRONOLOGY.getYearMonthDayMillis(2000, 2, 29)));
 assertFalse(field.isLeap(ISO_CHRONOLOGY.getYearMonthDayMillis(2000, 2, 28)));
 }

 @org.junit.Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertEquals(0, field.getLeapAmount(ISO_CHRONOLOGY.getYearMonthDayMillis(2000, 2, 28)));
 assertEquals(1, field.getLeapAmount(ISO_CHRONOLOGY.getYearMonthDayMillis(2000, 2, 29)));
 }

 @org.junit.Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertEquals(ISO_CHRONOLOGY.days(), field.getLeapDurationField());
 }

 @org.junit.Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertEquals(ISO_CHRONOLOGY.years(), field.getRangeDurationField());
 }

 @org.junit.Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertEquals(1, field.getMinimumValue());
 }

 @org.junit.Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 assertEquals(12, field.getMaximumValue());
 }

 @org.junit.Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long n = ISO_CHRONOLOGY.getYearMonthDayMillis(2001, 2, 28);
 long result = field.roundFloor(n);
 assertEquals(ISO_CHRONOLOGY.getYearMonthDayMillis(2001, 2, 28), result);

 n = ISO_CHRONOLOGY.getYearMonthDayMillis(2001, 2, 29);
 result = field.roundFloor(n);
 assertEquals(ISO_CHRONOLOGY.getYearMonthDayMillis(2001, 2, 28), result);
 }

 @org.junit.Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, 2);
 long n = ISO_CHRONOLOGY.getYearMonthDayMillis(2001, 2, 29);
 long result = field.remainder(n);
 assertEquals(0, result);

 n = ISO_CHRONOLOGY.getYearMonthDayMillis(2001, 3, 1);
 result = field.remainder(n);
 assertEquals(ISO_CHRONOLOGY.getMillisOfDay(n), result);
 }
}
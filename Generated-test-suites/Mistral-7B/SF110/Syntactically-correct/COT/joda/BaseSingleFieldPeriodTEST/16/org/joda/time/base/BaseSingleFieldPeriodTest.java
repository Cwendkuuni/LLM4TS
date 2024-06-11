package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod getInstanceWithValue(int periodValue) {
  return new BaseSingleFieldPeriod(periodValue) {
   @Override
   public DurationFieldType getFieldType() {
    return new DateTimeFieldType(0, "TestField", new BaseDurationFieldType(1, "TestUnit"));
   }
  };
 }

 @Test
 public void testConstructorWithValue() {
  BaseSingleFieldPeriod period = getInstanceWithValue(123);
  assertEquals(123, period.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeValue() {
  new BaseSingleFieldPeriod(-123);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueWithNegativeValue() {
  BaseSingleFieldPeriod period = getInstanceWithValue(123);
  period.setValue(-123);
 }

 @Test
 public void testGetValue() {
  BaseSingleFieldPeriod period = getInstanceWithValue(123);
  assertEquals(123, period.getValue());
 }

 @Test
 public void testSetValue() {
  BaseSingleFieldPeriod period = getInstanceWithValue(123);
  period.setValue(456);
  assertEquals(456, period.getValue());
 }

 @Test
 public void testSize() {
  BaseSingleFieldPeriod period = getInstanceWithValue(123);
  assertEquals(1, period.size());
 }

 @Test
 public void testGetFieldType() {
  BaseSingleFieldPeriod period = getInstanceWithValue(123);
  assertEquals(new DateTimeFieldType(0, "TestField", new BaseDurationFieldType(1, "TestUnit")), period.getFieldType());
 }

 @Test
 public void testGetPeriodType() {
  BaseSingleFieldPeriod period = getInstanceWithValue(123);
  assertEquals(PeriodType.instance(), period.getPeriodType());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithInvalidIndex() {
  BaseSingleFieldPeriod period = getInstanceWithValue(123);
  period.getValue(-1);
 }

 @Test
 public void testEquals() {
  BaseSingleFieldPeriod period1 = getInstanceWithValue(123);
  BaseSingleFieldPeriod period2 = getInstanceWithValue(123);
  BaseSingleFieldPeriod period3 = getInstanceWithValue(456);
  assertTrue(period1.equals(period1));
  assertTrue(period1.equals(period2));
  assertFalse(period1.equals(period3));
  assertFalse(period1.equals(null));
 }

 @Test
 public void testHashCode() {
  BaseSingleFieldPeriod period1 = getInstanceWithValue(123);
  BaseSingleFieldPeriod period2 = getInstanceWithValue(123);
  BaseSingleFieldPeriod period3 = getInstanceWithValue(456);
  assertEquals(period1.hashCode(), period1.hashCode());
  assertEquals(period1.hashCode(), period2.hashCode());
  assertNotEquals(period1.hashCode(), period3.hashCode());
 }

 @Test
 public void testCompareTo() {
  BaseSingleFieldPeriod period1 = getInstanceWithValue(123);
  BaseSingleFieldPeriod period2 = getInstanceWithValue(123);
  BaseSingleFieldPeriod period3 = getInstanceWithValue(456);
  BaseSingleFieldPeriod period4 = getInstanceWithValue(123);
  assertEquals(0, period1.compareTo(period2));
  assertEquals(1, period1.compareTo(period3));
  assertEquals(-1, period3.compareTo(period1));
  assertEquals(0, period1.compareTo(period4));
 }

 @Test
 public void testBetween() {
   long startInstant = 1462307200000L; // Thu Jan 01 00:00:00 UTC 2016
   long endInstant = 1462373600000L; // Thu Jan 01 23:59:59 UTC 2016
   
   Chronology chrono = DateTimeUtils.getChronology(DateTimeUtils.UTC);
   
   ReadableInstant start = new DateTime(startInstant, chrono);
   ReadableInstant end = new DateTime(endInstant, chrono);
   
   Period period = BaseSingleFieldPeriod.between(start, end, new Days());
   
   BaseSingleFieldPeriod testInstance = getInstanceWithValue(period.getValues()[0]);
   
   assertEquals(period.getValues()[0], testInstance.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullReadableInstant() {
  DateTime start = null;
  DateTime end = new DateTime();
  DateTimeUtils.getChronology(DateTimeUtils.UTC);
  Period period = BaseSingleFieldPeriod.between(start, end, new Days());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullReadableInstant2() {
  DateTime start = new DateTime();
  DateTime end = null;
  DateTimeUtils.getChronology(DateTimeUtils.UTC);
  Period period = BaseSingleFieldPeriod.between(start, end, new Days());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullDurationFieldType() {
  DateTime start = new DateTime();
  DateTime end = new DateTime();
  DateTimeUtils.getChronology(DateTimeUtils.UTC);
  Period period = BaseSingleFieldPeriod.between(start, end, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInWithNullReadablePeriod() {
  Period period = null;
  long n = 1;
  Period standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(period, n);
 }
}
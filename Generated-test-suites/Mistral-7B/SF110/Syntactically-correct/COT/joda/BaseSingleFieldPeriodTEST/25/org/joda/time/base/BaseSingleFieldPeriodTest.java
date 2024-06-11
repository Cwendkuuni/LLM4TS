package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod baseSingleFieldPeriod1;
 private BaseSingleFieldPeriod baseSingleFieldPeriod2;

 @Test
 public void testBetweenMethod() {
  ReadableInstant readableInstant = new DateTime(1972, 1, 1, 0, 0, 0, 0);
  ReadableInstant readableInstant2 = new DateTime(1972, 1, 2, 0, 0, 0, 0);
  DurationFieldType durationFieldType = Days.INSTANCE;

  int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
  int expected = 0;
  assertEquals(expected, result);

  readableInstant2 = new DateTime(1972, 1, 3, 0, 0, 0, 0);
  result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
  expected = 1;
  assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodNullReadableInstant() {
  ReadableInstant readableInstant = null;
  ReadableInstant readableInstant2 = new DateTime(1972, 1, 1, 0, 0, 0, 0);
  DurationFieldType durationFieldType = Days.INSTANCE;

  BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodNullReadableInstant2() {
  ReadableInstant readableInstant = new DateTime(1972, 1, 1, 0, 0, 0, 0);
  ReadableInstant readableInstant2 = null;
  DurationFieldType durationFieldType = Days.INSTANCE;

  BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
 }

 @Test
 public void testBetweenMethodDifferentChronologies() {
  ISOChronology chronology1 = ISOChronology.getInstance();
  CalendarSystem calendarSystem2 = GregorianCalendarSystem.INSTANCE;

  ReadableInstant readableInstant = new DateTime(1972, 1, 1, 0, 0, 0, 0, chronology1);
  ReadableInstant readableInstant2 = new DateTime(1972, 1, 1, 0, 0, 0, 0, calendarSystem2);
  DurationFieldType durationFieldType = Days.INSTANCE;

  int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
  int expected = 0;
  assertEquals(expected, result);
 }

 @Test
 public void testBetweenMethodSameInstant() {
  ReadableInstant readableInstant = new DateTime(1972, 1, 1, 0, 0, 0, 0);

  int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant, Days.INSTANCE);
  int expected = 0;
  assertEquals(expected, result);
 }

 @Test
 public void testBetweenMethodSameChronology() {
  ReadableInstant readableInstant = new DateTime(1972, 1, 1, 0, 0, 0, 0, ISOChronology.getInstance());
  ReadableInstant readableInstant2 = new DateTime(1972, 1, 2, 0, 0, 0, 0, ISOChronology.getInstance());
  DurationFieldType durationFieldType = Days.INSTANCE;

  int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
  int expected = 1;
  assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodDifferentNumberOfFields() {
  ReadablePartial readablePartial = new GeneralDatetimeField(1, ISOChronology.getInstance(), Minutes.INSTANCE);
  ReadablePartial readablePartial2 = new GeneralDatetimeField(1, ISOChronology.getInstance(), Days.INSTANCE);
  ReadablePeriod readablePeriod = Period.ZERO.withFields(readablePartial);

  int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test
 public void testStandardPeriodInMethod() {
  Period period = Period.DAYS.withValue(2);
  int n = 2;

  int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
  int expected = 1;
  assertEquals(expected, result);
 }

 @Test
 public void testStandardPeriodInMethodZeroPeriod() {
  Period period = Period.ZERO;
  int n = 2;

  int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
  int expected = 0;
  assertEquals(expected, result);
 }

 @Test
 public void testStandardPeriodInMethodNegativeN() {
  Period period = Period.DAYS.withValue(2);
  int n = -2;

  int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
  int expected = 1;
  assertEquals(expected, result);
 }

 @Test
 public void testStandardPeriodInMethodNullPeriod() {
  Period period = null;
  int n = 2;

  int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
  int expected = 0;
  assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInMethodNullN() {
  Period period = Period.DAYS.withValue(2);
  int n = 0;

  BaseSingleFieldPeriod.standardPeriodIn(period, n);
 }

 @Test
 public void testConstructor() {
  int iPeriod = 3;
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(iPeriod);

  int value = baseSingleFieldPeriod.getValue();
  int expected = iPeriod;
  assertEquals(expected, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNegativeIPeriod() {
  int iPeriod = -3;
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(iPeriod);
 }

 @Test
 public void testGetFieldTypeMethod() {
  DurationFieldType fieldType = Days.INSTANCE;
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
    @Override
    public DurationFieldType getFieldType() {
      return fieldType;
    }
  };

  DurationFieldType result = baseSingleFieldPeriod.getFieldType();
  DurationFieldType expected = fieldType;
  assertEquals(expected, result);
 }

 @Test
 public void testGetPeriodTypeMethod() {
  PeriodType periodType = PeriodType.instance(1);
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
    @Override
    public PeriodType getPeriodType() {
      return periodType;
    }
  };

  PeriodType result = baseSingleFieldPeriod.getPeriodType();
  PeriodType expected = periodType;
  assertEquals(expected, result);
 }

 @Test
 public void testSizeMethod() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
  int expected = 1;
  int result = baseSingleFieldPeriod.size();
  assertEquals(expected, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueMethodInvalidIndex() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
  baseSingleFieldPeriod.getValue(2);
 }

 @Test
 public void testGetMethod() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
  DurationFieldType durationFieldType = Days.INSTANCE;

  int result = baseSingleFieldPeriod.get(durationFieldType);
  int expected = 1;
  assertEquals(expected, result);
 }

 @Test
 public void testIsSupportedMethod() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
  DurationFieldType durationFieldType = Days.INSTANCE;

  boolean result = baseSingleFieldPeriod.isSupported(durationFieldType);
  boolean expected = true;
  assertTrue(expected);
 }

 @Test
 public void testToPeriodMethod() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
  Period expected = Period.DAYS.withValue(1);

  Period result = baseSingleFieldPeriod.toPeriod();
  assertEquals(expected, result);
 }

 @Test
 public void testToMutablePeriodMethod() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
  MutablePeriod mutablePeriod = new MutablePeriod();

  mutablePeriod.add(baseSingleFieldPeriod);
  Period expected = Period.DAYS.withValue(1);

  Period result = mutablePeriod.toPeriod();
  assertEquals(expected, result);
 }

 @Test
 public void testEqualsMethod() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
  BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1);
  Object obj = new Object();

  assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
  assertFalse(baseSingleFieldPeriod1.equals(obj));
 }

 @Test
 public void testHashCodeMethod() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
  BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1);

  int hashCode1 = baseSingleFieldPeriod1.hashCode();
  int hashCode2 = baseSingleFieldPeriod2.hashCode();

  assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testCompareToMethodSameBaseSingleFieldPeriod() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
  BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1);

  int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
  int expected = 0;
  assertEquals(expected, result);
 }

 @Test
 public void testCompareToMethodDifferentBaseSingleFieldPeriods() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
  BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2);

  int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
  int expected = -1;
  assertEquals(expected, result);
 }

 @Test
 public void testCompareToMethodSameClassDifferentValues() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
  BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2);

  int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
  int expected = -1;
  assertEquals(expected, result);
 }

 @Test
 public void testCompareToMethodDifferentClasses() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
  Period period2 = Period.DAYS.withValue(2);

  int result = baseSingleFieldPeriod1.compareTo(period2);
  int expected = -1;
  assertEquals(expected, result);
 }
}
package org.joda.time.base;

import org.junit.Test;
import org.joda.time.*;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod instance;

 @Test
 public void testConstructor() {
 int period = 7;
 instance = new BaseSingleFieldPeriod(period);
 assertEquals(period, instance.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeValue() {
 instance = new BaseSingleFieldPeriod(-7);
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType fieldType = new Days();
 assertEquals(fieldType, instance.getFieldType());
 }

 @Test
 public void testGetPeriodType() {
 PeriodType periodType = PeriodType.INSTANCE;
 assertEquals(periodType, instance.getPeriodType());
 }

 @Test
 public void testSize() {
 assertEquals(1, instance.size());
 }

 @Test
 public void testGetValue() {
 assertEquals(7, instance.getValue());
 }

 @Test
 public void testSetValue() {
 int newValue = 10;
 instance.setValue(newValue);
 assertEquals(newValue, instance.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithIndex() {
 instance.getValue(1);
 }

 @Test
 public void testGet() {
 DurationFieldType fieldType = new Days();
 assertEquals(7, instance.get(fieldType));
 }

 @Test
 public void testIsSupported() {
 DurationFieldType fieldType = new Days();
 assertTrue(instance.isSupported(fieldType));
 }

 @Test
 public void testToPeriod() {
 Period expectedPeriod = new Period(new Days(7));
 Period actualPeriod = instance.toPeriod();
 assertEquals(expectedPeriod, actualPeriod);
 }

 @Test
 public void testToMutablePeriod() {
 MutablePeriod expectedPeriod = new MutablePeriod();
 expectedPeriod.add(new Days(7));
 MutablePeriod actualPeriod = instance.toMutablePeriod();
 assertEquals(expectedPeriod, actualPeriod);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod anotherInstance = new BaseSingleFieldPeriod(7);
 assertTrue(instance.equals(anotherInstance));
 }

 @Test
 public void testNotEqualsWithDifferentType() {
 BaseSingleFieldPeriod anotherInstance = new BaseSingleFieldPeriod(5);
 assertFalse(instance.equals(anotherInstance));
 }

 @Test
 public void testNotEqualsWithSameTypeButDifferentValue() {
 BaseSingleFieldPeriod anotherInstance = new BaseSingleFieldPeriod(11);
 assertFalse(instance.equals(anotherInstance));
 }

 @Test
 public void testHashCode() {
 int expectedHashCode = 27 * (27 * 17 + 7) + fieldType.hashCode();
 int actualHashCode = instance.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testCompareToSameInstance() {
 assertEquals(0, instance.compareTo(instance));
 }

 @Test
 public void testCompareToGreaterInstance() {
 BaseSingleFieldPeriod greaterInstance = new BaseSingleFieldPeriod(10);
 assertEquals(1, instance.compareTo(greaterInstance));
 }

 @Test
 public void testCompareToSmallerInstance() {
 BaseSingleFieldPeriod smallerInstance = new BaseSingleFieldPeriod(5);
 assertEquals(-1, instance.compareTo(smallerInstance));
 }

 @Test
 public void testBetweenWithReadableInstant() {
 ReadableInstant instant1 = new DateTime(2012, 1, 1, 0, 0, 0).toInstant();
 ReadableInstant instant2 = new DateTime(2012, 1, 2, 0, 0, 0).toInstant();
 int expectedDaysBetween = 1;
 int actualDaysBetween = BaseSingleFieldPeriod.between(instant1, instant2, new Days());
 assertEquals(expectedDaysBetween, actualDaysBetween);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullReadableInstant() {
 ReadableInstant nullReadableInstant = null;
 ReadableInstant readableInstant2 = new DateTime(2012, 1, 2, 0, 0, 0).toInstant();
 BaseSingleFieldPeriod.between(nullReadableInstant, readableInstant2, new Days());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullReadableInstant2() {
 ReadableInstant readableInstant1 = new DateTime(2012, 1, 1, 0, 0, 0).toInstant();
 ReadableInstant nullReadableInstant2 = null;
 BaseSingleFieldPeriod.between(readableInstant1, nullReadableInstant2, new Days());
 }

 @Test
 public void testBetweenWithReadablePartial() {
 ReadablePartial readablePartial1 = new DateTime(2012, 1, 1, 0, 0, 0).toLocalDate().toLocalDateTime(0, 0, 0, 0).toDateTimeAtStartOfDay();
 ReadablePartial readablePartial2 = new DateTime(2012, 1, 2, 0, 0, 0).toLocalDate().toLocalDateTime(0, 0, 0, 0).toDateTimeAtStartOfDay();
 ReadablePeriod readablePeriod = new Period(new Days(1));
 int expectedDaysBetween = 1;
 int actualDaysBetween = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
 assertEquals(expectedDaysBetween, actualDaysBetween);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullReadablePartial() {
 ReadablePartial nullReadablePartial1 = null;
 ReadablePartial readablePartial2 = new DateTime(2012, 1, 2, 0, 0, 0).toLocalDate().toLocalDateTime(0, 0, 0, 0).toDateTimeAtStartOfDay();
 ReadablePeriod readablePeriod = new Period(new Days(1));
 BaseSingleFieldPeriod.between(nullReadablePartial1, readablePartial2, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNonContiguousReadablePartial() {
 ReadablePartial readablePartial1 = new DateTime(2012, 1, 1, 0, 0, 0).toLocalDate().toLocalDateTime(0, 0, 0, 15).toDateTimeAtStartOfMinute();
 ReadablePartial readablePartial2 = new DateTime(2012, 1, 1, 0, 0, 10).toLocalDate().toLocalDateTime(0, 0, 0, 0).toDateTimeAtStartOfDay();
 ReadablePeriod readablePeriod = new Period(new Minutes(1));
 BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithDifferentSetOfFieldsReadablePartial() {
 ReadablePartial readablePartial1 = new DateTime(2012, 1, 1, 0, 0, 0).toLocalDate().toLocalDateTime(0, 0, 0, 0).toDateTimeAtStartOfDay();
 ReadablePartial readablePartial2 = new DateTime(2012, 1, 1, 0, 0, 0).toLocalDate().toLocalDateTime(0, 0, 1, 0).toDateTimeAtStartOfDay();
 ReadablePeriod readablePeriod = new Period(new Days(1));
 BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
 }

 @Test
 public void testStandardPeriodIn() {
 ReadablePeriod period = new Period(new Days(7));
 long n = 2;
 int expectedDays = 3;
 int actualDays = BaseSingleFieldPeriod.standardPeriodIn(period, n);
 assertEquals(expectedDays, actualDays);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInWithNullReadablePeriod() {
 ReadablePeriod nullReadablePeriod = null;
 long n = 2;
 BaseSingleFieldPeriod.standardPeriodIn(nullReadablePeriod, n);
 }
}
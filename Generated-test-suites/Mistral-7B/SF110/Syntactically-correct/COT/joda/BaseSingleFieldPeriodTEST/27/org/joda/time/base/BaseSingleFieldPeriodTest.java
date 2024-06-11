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
 public void testConstructors() {
 baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(5);
 baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(10);
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType fieldType = baseSingleFieldPeriod1.getFieldType();
 assertEquals(DurationFieldType.days(), fieldType);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeWithInvalidIndex() {
 baseSingleFieldPeriod1.getFieldType(2);
 }

 @Test
 public void testGetPeriodType() {
 PeriodType periodType = baseSingleFieldPeriod1.getPeriodType();
 assertEquals(PeriodType.instance(), periodType);
 }

 @Test
 public void testSize() {
 int size = baseSingleFieldPeriod1.size();
 assertEquals(1, size);
 }

 @Test
 public void testGetValue() {
 int value = baseSingleFieldPeriod1.getValue();
 assertEquals(5, value);
 }

 @Test
 public void testSetValue() {
 baseSingleFieldPeriod1.setValue(10);
 int newValue = baseSingleFieldPeriod1.getValue();
 assertEquals(10, newValue);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithInvalidIndex() {
 baseSingleFieldPeriod1.getValue(2);
 }

 @Test
 public void testGet() {
 int result = baseSingleFieldPeriod1.get(DurationFieldType.days());
 assertEquals(5, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidDurationFieldType() {
 baseSingleFieldPeriod1.get(DurationFieldType.milliseconds());
 }

 @Test
 public void testIsSupported() {
 assertTrue(baseSingleFieldPeriod1.isSupported(DurationFieldType.days()));
 assertFalse(baseSingleFieldPeriod1.isSupported(DurationFieldType.milliseconds()));
 }

 @Test
 public void testToPeriod() {
 Period period = baseSingleFieldPeriod1.toPeriod();
 assertEquals("P5D", period.toString());
 }

 @Test
 public void testToMutablePeriod() {
 MutablePeriod mutablePeriod = baseSingleFieldPeriod1.toMutablePeriod();
 mutablePeriod.add(new Period(1, DurationFieldType.days()));
 Period result = mutablePeriod.toPeriod();
 assertEquals("P6D", result.toString());
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
 assertTrue(period1.equals(period2));
 }

 @Test
 public void testEqualsWithDifferentClasses() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
 Period period2 = new Period(5, DurationFieldType.days());
 assertFalse(period1.equals(period2));
 }

 @Test
 public void testEqualsWithNull() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
 assertFalse(period1.equals(null));
 }

 @Test
 public void testCompareTo() {
 int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
 assertEquals(-5, result);
 }

 @Test
 public void testCompareToWithSameInstance() {
 int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod1);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToWithSameValueButDifferentClasses() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
 Period period2 = new Period(5, DurationFieldType.days());
 int result = period1.compareTo(period2);
 assertEquals(0, result);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToWithDifferentClasses() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
 Period period2 = new Period(1, DurationFieldType.years());
 period1.compareTo(period2);
 }

 @Test
 public void testBetween() {
 ReadableInstant readableInstant1 = new DateTime(1972, 1, 1, 0, 0, 0, 0, ISO.UTC);
 ReadableInstant readableInstant2 = new DateTime(1972, 1, 6, 0, 0, 0, 0, ISO.UTC);
 int result = BaseSingleFieldPeriod.between(readableInstant1, readableInstant2, DurationFieldType.days());
 assertEquals(5, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullReadableInstant1() {
 BaseSingleFieldPeriod.between(null, readableInstant2, DurationFieldType.days());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullReadableInstant2() {readableInstant1 = new DateTime(1972, 1, 1, 0, 0, 0, 0, ISO.UTC);
 BaseSingleFieldPeriod.between(readableInstant1, null, DurationFieldType.days());
 }

 @Test
 public void testStandardPeriodIn() {
 ReadablePeriod readablePeriod = new Period(5, DurationFieldType.days());
 long result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 7);
 assertEquals(714285714647, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInWithNullReadablePeriod() {
 BaseSingleFieldPeriod.standardPeriodIn(null, 7);
 }
}
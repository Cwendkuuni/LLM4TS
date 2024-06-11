package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 @Test
 public void testBetween() {
 ReadableInstant instant1 = new DateTime(1972, 1, 1, 0, 0, 0, 0, ISO.UTC);
 ReadableInstant instant2 = new DateTime(1972, 1, 2, 0, 0, 0, 0, ISO.UTC);
 DurationFieldType durationFieldType = FieldType.DayOfYear();
 int between = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 assertEquals(1, between);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullInstants() {
 ReadableInstant instant1 = null;
 ReadableInstant instant2 = new DateTime(1972, 1, 1, 0, 0, 0, 0, ISO.UTC);
 DurationFieldType durationFieldType = FieldType.DayOfYear();
 BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullDurationFieldType() {
 ReadableInstant instant1 = new DateTime(1972, 1, 1, 0, 0, 0, 0, ISO.UTC);
 ReadableInstant instant2 = new DateTime(1972, 1, 2, 0, 0, 0, 0, ISO.UTC);
 DurationFieldType durationFieldType = null;
 BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 }

 @Test
 public void testBetweenDifferentChronologies() {
 ReadableInstant instant1 = new DateTime(1972, 1, 1, 0, 0, 0, 0, DateTimeUtils.USE_PROVIDED_CHRONOLOGY);
 ReadableInstant instant2 = new DateTime(1972, 1, 2, 0, 0, 0, 0, ISO.UTC);
 DurationFieldType durationFieldType = FieldType.DayOfYear();
 int between = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 assertEquals(1, between);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenDifferentFieldTypes() {
 ReadableInstant instant1 = new DateTime(1972, 1, 1, 0, 0, 0, 0, ISO.UTC);
 ReadableInstant instant2 = new DateTime(1972, 1, 2, 0, 0, 0, 0, ISO.UTC);
 DurationFieldType durationFieldType1 = FieldType.DayOfYear();
 DurationFieldType durationFieldType2 = FieldType.HourOfDay();
 BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType1);
 BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNonContiguousPartial() {
 ReadablePartial partial1 = new GeneralDate(1972, 1, 1, 0, 0);
 ReadablePartial partial2 = new GeneralDate(1972, 1, 2, 0, 0).withFieldAdded(DateTimeFieldType.HourOfDay(), 1);
 ReadablePeriod period = Period.days(1);
 int between = BaseSingleFieldPeriod.between(partial1, partial2, period);
 }

 @Test
 public void testStandardPeriodIn() {
 Period period = Period.days(5);
 long n = 2;
 int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(period, n);
 assertEquals(25 * 24 * 60 * 60 * 1000L / n, (long)standardPeriodIn * n);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInNullPeriod() {
 Period period = null;
 long n = 2;
 BaseSingleFieldPeriod.standardPeriodIn(period, n);
 }

 @Test
 public void testConstructor() {
 int iPeriod = 3;
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(iPeriod);
 assertEquals(iPeriod, baseSingleFieldPeriod.getValue());
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType fieldType = FieldType.DayOfMonth();
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
 @Override
 public DurationFieldType getFieldType() {
 return fieldType;
 }
 };
 assertEquals(fieldType, baseSingleFieldPeriod.getFieldType());
 }

 @Test
 public void testGetPeriodType() {
 PeriodType periodType = PeriodType.standard();
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
 @Override
 public PeriodType getPeriodType() {
 return periodType;
 }
 };
 assertEquals(periodType, baseSingleFieldPeriod.getPeriodType());
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
 assertEquals(1, baseSingleFieldPeriod.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeNegativeIndex() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
 baseSingleFieldPeriod.getFieldType(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)public void testGetValueNegativeIndex() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
 baseSingleFieldPeriod.getValue(-1);
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
 assertEquals(1, baseSingleFieldPeriod.getValue());
 }

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1);
 baseSingleFieldPeriod.setValue(2);
 assertEquals(2, baseSingleFieldPeriod.getValue());
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1);
 assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
 }

 @Test
 public void testNotEquals() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2);
 assertFalse(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1);
 assertEquals(baseSingleFieldPeriod1.hashCode(), baseSingleFieldPeriod2.hashCode());
 }

 @Test
 public void testHashCodeDifferentValues() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2);
 assertNotSame(baseSingleFieldPeriod1.hashCode(), baseSingleFieldPeriod2.hashCode());
 }

 @Test
 public void testCompareToSameClass() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1);
 assertEquals(0, baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2));
 }

 @Test
 public void testCompareToDifferentClasses() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
 Period period = Period.days(1);
 assertLessThan(0, baseSingleFieldPeriod1.compareTo(period));
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToDifferentClassesThrowsException() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
 Period period = Period.days(1);
 baseSingleFieldPeriod1.compareTo(period);
 }

 @Test
 public void testCompareToDifferentValuesSameClass() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2);
 assertTrue(baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2) < 0);
 }

 @Test
 public void testCompareToDifferentValuesDifferentClasses() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(1);
 Period period = Period.days(2);
 assertTrue(baseSingleFieldPeriod1.compareTo(period) < 0);
 }
}
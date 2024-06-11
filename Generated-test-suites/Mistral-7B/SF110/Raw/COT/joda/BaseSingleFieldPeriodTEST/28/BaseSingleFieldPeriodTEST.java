package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod instance;

 @Test
 public void testConstructor() {
 instance = new BaseSingleFieldPeriod(5);
 assertEquals(5, instance.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNegativeNumber() {
 instance = new BaseSingleFieldPeriod(5);
 instance.setValue(-2);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType expected = new DateTimeFields.Milliseconds();
 assertTrue(instance.getClass() instanceof BaseSingleFieldPeriod);
 assertEquals(expected, instance.getFieldType());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeInvalidIndex() {
 instance = new BaseSingleFieldPeriod(5);
 instance.getFieldType(2);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testSize() {
 assertEquals(1, instance.size());
 }

 @Test
 public void testGetValue() {
 assertEquals(5, instance.getValue());
 }

 @Test
 public void testGetValueInvalidIndex() {
 instance = new BaseSingleFieldPeriod(5);
 try {
 instance.getValue(2);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testGet() {
 DurationFieldType durationFieldType = new DateTimeFields.Milliseconds();
 assertEquals(5, instance.get(durationFieldType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsSupportedNullDurationFieldType() {
 instance = new BaseSingleFieldPeriod(5);
 assertFalse(instance.isSupported(null));
 }

 @Test
 public void testIsSupportedSameDurationFieldType() {
 DurationFieldType durationFieldType = new DateTimeFields.Milliseconds();
 assertTrue(instance.isSupported(durationFieldType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsSupportedDifferentDurationFieldType() {
 DurationFieldType durationFieldType = new DateTimeFields.Seconds();
 assertFalse(instance.isSupported(durationFieldType));
 }

 @Test
 public void testToPeriod() {
 Period expected = new Period(5, DateTimeUnit.MILLISECONDS);
 Period actual = instance.toPeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriod() {
 MutablePeriod expected = new MutablePeriod(DurationFieldType.MILLISECONDS, 5);
 MutablePeriod actual = instance.toMutablePeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(5);
 assertTrue(instance.equals(other));
 }

 @Test
 public void testEqualsDifferentClass() {
 Object other = new Object();
 assertFalse(instance.equals(other));
 }

 @Test
 public void testEqualsDifferentValue() {
 BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(3);
 assertFalse(instance.equals(other));
 }

 @Test
 public void testCompareToSameInstance() {
 assertEquals(0, instance.compareTo(instance));
 }

 @Test
 public void testCompareToZero() {
 assertEquals(1, instance.compareTo(BaseSingleFieldPeriod.ZERO));
 }

 @Test
 public void testCompareToNegativeInstance() {
 BaseSingleFieldPeriod negativeInstance = new BaseSingleFieldPeriod(-5);
 assertEquals(-1, instance.compareTo(negativeInstance));
 }

 @Test
 public void testCompareToDifferentClass() {
 Object other = new Object();
 assertEquals(Integer.MIN_VALUE, instance.compareTo(other));
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToDifferentClassInstance() {
 Object other = new Object();
 instance = (BaseSingleFieldPeriod) other;
 fail("Should throw ClassCastException");
 }

 @Test
 public void testCompareToZeroInstance() {
 assertEquals(0, instance.compareTo(BaseSingleFieldPeriod.ZERO));
 }

 @Test
 public void testCompareToNegativeValue() {
 BaseSingleFieldPeriod negativeInstance = new BaseSingleFieldPeriod(-5);
 assertEquals(-1, instance.compareTo(negativeInstance));
 }

 @Test
 public void testCompareToPositiveValue() {
 BaseSingleFieldPeriod positiveInstance = new BaseSingleFieldPeriod(10);
 assertEquals(1, instance.compareTo(positiveInstance));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadableInstant1() {
 int result = BaseSingleFieldPeriod.between(null, new DateTime(2022, 1, 1, 0, 0, 0), new DateTimeFields.Milliseconds());
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadableInstant2() {
 int result = BaseSingleFieldPeriod.between(new DateTime(2022, 1, 1, 0, 0, 0), null, new DateTimeFields.Milliseconds());
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadablePeriod() {
 int result = BaseSingleFieldPeriod.between(new DateTime(2022, 1, 1, 0, 0, 0), new DateTime(2022, 1, 1, 0, 0, 0), null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testBetween() {
 DateTime start = new DateTime(2022, 1, 1, 0, 0, 0);
 DateTime end = new DateTime(2022, 1, 2, 0, 0, 0);
 int expected = 86400000;
 int result = BaseSingleFieldPeriod.between(start, end, new DateTimeFields.Milliseconds());
 assertEquals(expected, result);
 }

 @Test
 public void testStandardPeriodInNullReadablePeriod() {
 int result = BaseSingleFieldPeriod.standardPeriodIn(null, 10);
 assertEquals(0, result);
 }

 @Test
 public void testStandardPeriodInZero() {
 int result = BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO, 10);
 assertEquals(0, result);
 }

 @Test
 public void testStandardPeriodIn() {
 Period period = new Period(5, DateTimeUnit.DAYS);
 int expected = 24 * 60 * 60 * 1000 * 5;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, 10);
 assertEquals(expected, result);
 }

}
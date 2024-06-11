package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private static final long START_1972 = 63072000000L;

 @Test
 public void testBetween_ReadableInstant() {
 ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
 ReadableInstant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();

 int result = BaseSingleFieldPeriod.between(instant, instant2, DurationFieldType.DAYS);

 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadableInstantNull() {
 BaseSingleFieldPeriod.between(null, new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()), DurationFieldType.DAYS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadableInstant2Null() {
 BaseSingleFieldPeriod.between(new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()), null, DurationFieldType.DAYS);
 }

 @Test
 public void testBetween_ReadablePartial() {
 ReadablePartial readablePartial = new DateTime(2022, 1, 1, 0, 0, 1, 0, ISOChronology.getInstanceUTC()).toLocalDate().toLocalDateParts();
 ReadablePartial readablePartial2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toLocalDate().toLocalDateParts();

 int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, new DurationFieldType[] { DurationFieldType.DAYS });

 assertEquals(1, result.length);
 assertEquals(1, result[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartialNull() {
 BaseSingleFieldPeriod.between(null, new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toLocalDate().toLocalDateParts(), new DurationFieldType[] { DurationFieldType.DAYS });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartial2Null() {
 BaseSingleFieldPeriod.between(new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toLocalDate().toLocalDateParts(), null, new DurationFieldType[] { DurationFieldType.DAYS });
 }

 @Test
 public void testBetween_ReadablePeriod() {
 Period period = new Period(1, DurationFieldType.DAYS);

 int result = BaseSingleFieldPeriod.between(period, 1);

 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePeriodNull() {
 BaseSingleFieldPeriod.between(null, 1);
 }

 @Test
 public void testStandardPeriodIn() {
 Period period = new Period(1, DurationFieldType.DAYS);

 int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1);

 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodIn_NullPeriod() {
 BaseSingleFieldPeriod.standardPeriodIn(null, 1);
 }

 @Test
 public void testConstructor() {
 int iPeriod = 5;
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(iPeriod);

 assertEquals(iPeriod, instance.getValue());
 }

 @Test
 public void testGetFieldType() {
 // Abstract class, cannot instantiate to test
 fail("Cannot instantiate abstract class: " + BaseSingleFieldPeriod.class.getName());
 }

 @Test
 public void testGetPeriodType() {
 // Abstract class, cannot instantiate to test
 fail("Cannot instantiate abstract class: " + BaseSingleFieldPeriod.class.getName());
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(5);

 assertEquals(1, instance.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValue_InvalidIndex() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(5);

 instance.getValue(2);
 }

 @Test
 public void testGet_DurationFieldType() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(5);

 assertEquals(instance.getFieldType(), instance.getFieldType(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGet_InvalidIndex() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(5);

 instance.getFieldType(2);
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(5);

 assertEquals(5, instance.getValue());
 }

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(5);
 int newValue = 10;

 instance.setValue(newValue);

 assertEquals(newValue, instance.getValue());
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(5);

 Period result = instance.toPeriod();

 assertEquals(5, result.getYears());
 assertEquals(0, result.getMonths());
 assertEquals(0, result.getWeeks());
 assertEquals(0, result.getDays());
 assertEquals(0, result.getHours());
 assertEquals(0, result.getMinutes());
 assertEquals(0, result.getSeconds());
 assertEquals(0, result.getMillis());
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(5);
 MutablePeriod mutablePeriod = instance.toMutablePeriod();

 mutablePeriod.add(new Period(2, DurationFieldType.DAYS));

 assertEquals(7, mutablePeriod.getYears());
 assertEquals(0, mutablePeriod.getMonths());
 assertEquals(0, mutablePeriod.getWeeks());
 assertEquals(7, mutablePeriod.getDays());
 assertEquals(0, mutablePeriod.getHours());
 assertEquals(0, mutablePeriod.getMinutes());
 assertEquals(0, mutablePeriod.getSeconds());
 assertEquals(0, mutablePeriod.getMillis());
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod instance3 = new BaseSingleFieldPeriod(10);

 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance2));
 assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod instance3 = new BaseSingleFieldPeriod(10);

 int hashCode1 = instance1.hashCode();
 int hashCode2 = instance1.hashCode();
 int hashCode3 = instance2.hashCode();
 int hashCode4 = instance3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 assertNotSame(hashCode1, hashCode4);
 }

 @Test
 public void testCompareTo() {
 BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(5);
 BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(10);
 BaseSingleFieldPeriod instance3 = new BaseSingleFieldPeriod(5);

 assertEquals(0, instance1.compareTo(instance1));
 assertEquals(5, instance1.compareTo(instance3));
 assertEquals(-5, instance3.compareTo(instance1));
 assertEquals(5, instance1.compareTo(instance2));
 assertEquals(-5, instance2.compareTo(instance1));
 }
}
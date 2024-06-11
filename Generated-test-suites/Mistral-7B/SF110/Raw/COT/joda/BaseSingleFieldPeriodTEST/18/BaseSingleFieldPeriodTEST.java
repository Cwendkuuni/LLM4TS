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

 @Test
 public void testGetFieldType() {
     instance = new BaseSingleFieldPeriod(5);
     DurationFieldType fieldType = instance.getFieldType();
     assertNotNull(fieldType);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeWrongIndex() {
     instance = new BaseSingleFieldPeriod(5);
     instance.getFieldType(1);
 }

 @Test
 public void testGetPeriodType() {
     instance = new BaseSingleFieldPeriod(5);
     PeriodType periodType = instance.getPeriodType();
     assertNotNull(periodType);
 }

 @Test
 public void testSize() {
     instance = new BaseSingleFieldPeriod(5);
     assertEquals(1, instance.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWrongIndex() {
     instance = new BaseSingleFieldPeriod(5);
     instance.getValue(1);
 }

 @Test
 public void testGetValue() {
     instance = new BaseSingleFieldPeriod(5);
     assertEquals(5, instance.getValue());
 }

 @Test
 public void testSetValue() {
     instance = new BaseSingleFieldPeriod(5);
     instance.setValue(10);
     assertEquals(10, instance.getValue());
 }

 @Test
 public void testGet() {
     instance = new BaseSingleFieldPeriod(5);
     DurationFieldType durationFieldType = new GenericDurationField("test", 1);
     assertEquals(5, instance.get(durationFieldType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWrongFieldType() {
     instance = new BaseSingleFieldPeriod(5);
     DurationFieldType durationFieldType = new GenericDurationField("test", 2);
     instance.get(durationFieldType);
 }

 @Test
 public void testIsSupported() {
     instance = new BaseSingleFieldPeriod(5);
     DurationFieldType durationFieldType = new GenericDurationField("test", 1);
     assertTrue(instance.isSupported(durationFieldType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsSupportedWrongFieldType() {
     instance = new BaseSingleFieldPeriod(5);
     DurationFieldType durationFieldType = new GenericDurationField("test", 2);
     instance.isSupported(durationFieldType);
 }

 @Test
 public void testToPeriod() {
     instance = new BaseSingleFieldPeriod(5);
     Period period = instance.toPeriod();
     assertEquals(5, period.getValues()[0]);
 }

 @Test
 public void testToMutablePeriod() {
     instance = new BaseSingleFieldPeriod(5);
     MutablePeriod mutablePeriod = instance.toMutablePeriod();
     assertEquals(5, mutablePeriod.getValues()[0]);
 }

 @Test
 public void testEquals() {
     BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
     BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
     BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
     assertTrue(period1.equals(period1));
     assertTrue(period1.equals(period2));
     assertFalse(period1.equals(period3));
 }

 @Test
 public void testHashCode() {
     BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
     BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
     assertEquals(period1.hashCode(), period2.hashCode());
 }

 @Test
 public void testCompareToZero() {
     instance = new BaseSingleFieldPeriod(5);
     BaseSingleFieldPeriod periodZero = new BaseSingleFieldPeriod(0);
     int compareResult = instance.compareTo(periodZero);
     assertEquals(1, compareResult);
 }

 @Test
 public void testCompareToNegative() {
     instance = new BaseSingleFieldPeriod(5);
     BaseSingleFieldPeriod periodNegative = new BaseSingleFieldPeriod(-5);
     int compareResult = instance.compareTo(periodNegative);
     assertEquals(-1, compareResult);
 }

 @Test
 public void testCompareToEqual() {
     instance = new BaseSingleFieldPeriod(5);
     BaseSingleFieldPeriod periodEqual = new BaseSingleFieldPeriod(5);
     int compareResult = instance.compareTo(periodEqual);
     assertEquals(0, compareResult);
 }

 @Test
 public void testCompareToDifferentClass() {
     instance = new BaseSingleFieldPeriod(5);
     BaseSingleFieldPeriod periodDifferentClass = new BaseMultiFieldPeriod(5, 5);
     int compareResult = instance.compareTo(periodDifferentClass);
     assertEquals(0, compareResult);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToDifferentClassWrong() {
     instance = new BaseSingleFieldPeriod(5);
     Object periodDifferentClass = new Object();
     int compareResult = instance.compareTo(periodDifferentClass);
 }

 @Test
 public void testBetween() throws Exception {
     ReadableInstant instant1 = new DateTime(0L);
     ReadableInstant instant2 = new DateTime(1000L);
     ReadablePeriod period = Duration.standardHours(1);
     int result = BaseSingleFieldPeriod.between(instant1, instant2, new Days());
     assertEquals(24 * 60 * 60 * 1000 + 1000, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullInstants() {
     ReadableInstant instant1 = null;
     ReadableInstant instant2 = new DateTime(1000L);
     ReadablePeriod period = Duration.standardHours(1);
     BaseSingleFieldPeriod.between(instant1, instant2, new Days());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullPeriod() {
     ReadableInstant instant1 = new DateTime(0L);
     ReadableInstant instant2 = new DateTime(1000L);
     BaseSingleFieldPeriod.between(instant1, instant2, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenDifferentChronologies() throws Exception {
     ReadableInstant instant1 = new DateTime(0L, ISOChronology.getInstance());
     ReadableInstant instant2 = new DateTime(1000L, UTCChronology.getInstance());
     ReadablePeriod period = Duration.standardHours(1);
     BaseSingleFieldPeriod.between(instant1, instant2, new Days());
 }

 @Test
 public void testStandardPeriodIn() {
     ReadablePeriod period = Duration.standardHours(2);
     long n = 3;
     int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
     assertEquals(7200000 * 2 / n, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInNullPeriod() {
     long n = 3;
     BaseSingleFieldPeriod.standardPeriodIn(null, n);
 }
}
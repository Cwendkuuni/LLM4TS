package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod baseSingleFieldPeriod;
 private BaseSingleFieldPeriod baseSingleFieldPeriod2;
 private ReadableInstant readableInstant;
 private ReadableInstant readableInstant2;
 private ReadablePeriod readablePeriod;
 private ReadablePartial readablePartial;
 private ReadablePartial readablePartial2;

 @Test
 public void testBetween_ReadableInstant() {
 readableInstant = new Instant(1516041623000L);
 readableInstant2 = new Instant(1516041683000L);
 int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.MILLISECONDS);
 assertEquals(50L, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadableInstant_Null() {
 BaseSingleFieldPeriod.between(null, readableInstant2, DurationFieldType.MILLISECONDS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadableInstant2_Null() {
 BaseSingleFieldPeriod.between(readableInstant, null, DurationFieldType.MILLISECONDS);
 }

 @Test
 public void testBetween_ReadablePartial() {
 readablePartial = new GeneralMillisecondPeriod(123456);
 readablePartial2 = new GeneralMillisecondPeriod(567890);
 readablePeriod = new Milliseconds(1000);
 int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 assertEquals(6441, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartial_Null() {
 BaseSingleFieldPeriod.between(readablePartial, null, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartial2_Null() {
 BaseSingleFieldPeriod.between(null, readablePartial2, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartial_DifferentSize() {
 readablePartial = new GeneralMillisecondPeriod(123456);
 readablePartial2 = new GeneralMillisecondPeriod(12345, 567890);
 readablePeriod = new Milliseconds(1000);
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartial_DifferentFields() {
 readablePartial = new GeneralMillisecondPeriod(123456);
 readablePartial2 = new GeneralSecondPeriod(567890);
 readablePeriod = new Milliseconds(1000);
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartial_NonContiguous() {
 readablePartial = new GeneralMillisecondPeriod(123456);
 readablePartial2 = new GeneralMillisecondPeriod(567890, 34560);
 readablePeriod = new Milliseconds(1000);
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 }

 @Test
 public void testStandardPeriodIn() {
 readablePeriod = new Milliseconds(1000);
 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 2);
 assertEquals(2, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodIn_Null() {
 BaseSingleFieldPeriod.standardPeriodIn(null, 2);
 }

 @Test
 public void testConstructors() {
 baseSingleFieldPeriod = new Days(3);
 assertEquals(3, baseSingleFieldPeriod.getValue());
 baseSingleFieldPeriod2 = new Days(5);
 assertEquals(5, baseSingleFieldPeriod2.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructors_NegativeValue() {
 BaseSingleFieldPeriod negativeBaseSingleFieldPeriod = new Days(-3);
 fail("Should have thrown IllegalArgumentException for negative value");
 }

 @Test
 public void testGetFieldType() {
 assertEquals(DurationFieldType.DAYS, baseSingleFieldPeriod.getFieldType());
 assertEquals(DurationFieldType.DAYS, baseSingleFieldPeriod2.getFieldType());
 }

 @Test
 public void testGetPeriodType() {
 assertEquals(PeriodType.fixedPeriod, baseSingleFieldPeriod.getPeriodType());
 assertEquals(PeriodType.fixedPeriod, baseSingleFieldPeriod2.getPeriodType());
 }

 @Test
 public void testSize() {
 assertEquals(1, baseSingleFieldPeriod.size());
 assertEquals(1, baseSingleFieldPeriod2.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldType_WrongIndex() {
 baseSingleFieldPeriod.getFieldType(1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValue_WrongIndex() {
 baseSingleFieldPeriod.getValue(1);
 }

 @Test
 public void testGet_SameFieldType() {
 assertEquals(3, baseSingleFieldPeriod.get(DurationFieldType.DAYS));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGet_DifferentFieldType() {
 baseSingleFieldPeriod.get(DurationFieldType.MILLISECONDS);
 }

 @Test
 public void testIsSupported() {
 assertTrue(baseSingleFieldPeriod.isSupported(DurationFieldType.DAYS));
 assertFalse(baseSingleFieldPeriod.isSupported(DurationFieldType.MILLISECONDS));
 }

 @Test
 public void testToPeriod() {
 Period period = baseSingleFieldPeriod.toPeriod();
 assertEquals(DurationFieldType.DAYS, period.getFieldType());
 assertEquals(3, period.getValues());
 }

 @Test
 public void testToMutablePeriod() {
 MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
 mutablePeriod.add(new Days(1));
 assertEquals(4, mutablePeriod.getValues());
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod baseSingleFieldPeriod3 = new Days(5);
 assertTrue(baseSingleFieldPeriod.equals(baseSingleFieldPeriod));
 assertTrue(baseSingleFieldPeriod.equals(baseSingleFieldPeriod2.toPeriod()));
 assertFalse(baseSingleFieldPeriod.equals(baseSingleFieldPeriod3));
 }

 @Test
 public void testHashCode() {
 int hashCode1 = baseSingleFieldPeriod.hashCode();
 int hashCode2 = baseSingleFieldPeriod2.hashCode();
 assertNotSame(hashCode1, hashCode2);
 }

 @Test
 public void testCompareTo_SameInstance() {
 assertEquals(0, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod));
 }

 @Test
 public void testCompareTo_SameValue() {
 BaseSingleFieldPeriod baseSingleFieldPeriodSameValue = new Days(3);
 assertEquals(0, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriodSameValue));
 }

 @Test
 public void testCompareTo_DifferentValue() {
 assertEquals(-2, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod2));
 }

 @Test
 public void testCompareTo_DifferentClass() {
 BaseSingleFieldPeriod baseSingleFieldPeriodDifferentClass = new Seconds(2);
 assertEquals(-1, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriodDifferentClass));
 }
}
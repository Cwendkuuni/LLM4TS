package org.joda.time.base;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createBaseSingleFieldPeriod(int period) {
 return new BaseSingleFieldPeriod(period) {
 @Override
 public DurationFieldType getFieldType() {
 return new IntDurationField(60, ChronoUnit.SECONDS);
 }
 };
 }

 @Test
 public void testBetween() {
 ReadableInstant instant1 = new DateTime(0L);
 ReadableInstant instant2 = new DateTime(3600000L);
 DurationFieldType durationFieldType = new IntDurationField(60, ChronoUnit.SECONDS);
 int result = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullInstant() {
 BaseSingleFieldPeriod.between(null, new DateTime(0L), new IntDurationField(60, ChronoUnit.SECONDS));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullInstant2() {
 BaseSingleFieldPeriod.between(new DateTime(0L), null, new IntDurationField(60, ChronoUnit.SECONDS));
 }

 @Test
 public void testBetweenSameInstant() {
 ReadableInstant instant = new DateTime(0L);
 DurationFieldType durationFieldType = new IntDurationField(60, ChronoUnit.SECONDS);
 int result = BaseSingleFieldPeriod.between(instant, instant, durationFieldType);
 assertEquals(0, result);
 }

 @Test
 public void testBetweenDifferentInstant() {
 ReadableInstant instant1 = new DateTime(0L);
 ReadableInstant instant2 = new DateTime(3600000L);
 DurationFieldType durationFieldType = new IntDurationField(60, ChronoUnit.SECONDS);
 int result = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenDifferentFieldTypes() {
 ReadableInstant instant1 = new DateTime(0L);
 ReadableInstant instant2 = new DateTime(3600000L);
 DurationFieldType durationFieldType1 = new IntDurationField(60, ChronoUnit.SECONDS);
 DurationFieldType durationFieldType2 = new IntDurationField(30, ChronoUnit.MINUTES);
 int result = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType1);
 }

 @Test
 public void testStandardPeriodIn() {
 Period period = new Period(3600000, ChronoUnit.MILLIS);
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
 assertEquals(3, result);
 }

 @Test(expected = NullPointerException.class)
 public void testStandardPeriodInNullPeriod() {
 BaseSingleFieldPeriod.standardPeriodIn(null, 1000);
 }

 @Test
 public void testStandardPeriodInZeroPeriod() {
 Period period = Period.ZERO;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
 assertEquals(0, result);
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod period = createBaseSingleFieldPeriod(60);
 int result = period.getValue();
 assertEquals(60, result);
 }

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod period = createBaseSingleFieldPeriod(60);
 period.setValue(120);
 int result = period.getValue();
 assertEquals(120, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueOutOfBounds() {
 BaseSingleFieldPeriod period = createBaseSingleFieldPeriod(60);
 period.getValue(1);
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod period = createBaseSingleFieldPeriod(60);
 int result = period.size();
 assertEquals(1, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)public void testGetFieldTypeOutOfBounds() { BaseSingleFieldPeriod period = createBaseSingleFieldPeriod(60); period.getFieldType(1); }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod period = createBaseSingleFieldPeriod(60);
 DurationFieldType result = period.getFieldType();
 assertEquals(IntDurationField.class, result.getClass());
 }

 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod period = createBaseSingleFieldPeriod(60);
 PeriodType result = period.getPeriodType();
 assertEquals(PeriodType.INSTANCE, result);
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod period = createBaseSingleFieldPeriod(60);
 Period result = period.toPeriod();
 assertEquals(new Period(60, ChronoUnit.SECONDS), result);
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod period = createBaseSingleFieldPeriod(60);
 MutablePeriod result = period.toMutablePeriod();
 assertEquals(new MutablePeriodImpl(new Period(60, ChronoUnit.SECONDS), 1), result);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod period1 = createBaseSingleFieldPeriod(60);
 BaseSingleFieldPeriod period2 = createBaseSingleFieldPeriod(60);
 assertTrue(period1.equals(period2));
 }

 @Test
 public void testNotEquals() {
 BaseSingleFieldPeriod period1 = createBaseSingleFieldPeriod(60);
 BaseSingleFieldPeriod period2 = createBaseSingleFieldPeriod(30);
 assertFalse(period1.equals(period2));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod period1 = createBaseSingleFieldPeriod(60);
 BaseSingleFieldPeriod period2 = createBaseSingleFieldPeriod(60);
 int result1 = period1.hashCode();
 int result2 = period2.hashCode();
 assertEquals(result1, result2);
 }

 @Test
 public void testCompareToZero() {
 BaseSingleFieldPeriod period1 = createBaseSingleFieldPeriod(60);
 BaseSingleFieldPeriod period2 = createBaseSingleFieldPeriod(0);
 int result = period1.compareTo(period2);
 assertEquals(1, result);
 }

 @Test
 public void testCompareToNegative() {
 BaseSingleFieldPeriod period1 = createBaseSingleFieldPeriod(30);
 BaseSingleFieldPeriod period2 = createBaseSingleFieldPeriod(60);
 int result = period1.compareTo(period2);
 assertEquals(-1, result);
 }

 @Test
 public void testCompareToEqual() {
 BaseSingleFieldPeriod period1 = createBaseSingleFieldPeriod(60);
 BaseSingleFieldPeriod period2 = createBaseSingleFieldPeriod(60);
 int result = period1.compareTo(period2);
 assertEquals(0, result);
 }
}
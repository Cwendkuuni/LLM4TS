package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.BasePeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.SerializableDuration;
import org.joda.time.field.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

 @Test
 public void testBetween() throws Exception {
  Instant start = Instant.now();
  Instant end = Instant.now();

  // Test with null arguments
  assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, null, DateTimeFieldType.millis()));

  // Test with valid arguments
  ReadableInstant readableInstant = new Instant(start.getMillis() - 1 * 60 * 60 * 1000);
  ReadableInstant readableInstant2 = new Instant(start.getMillis());
  int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DateTimeFieldType.millis());
  assertEquals(0, result);

  result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DateTimeFieldType.minutes());
  assertEquals(-1, result);

  end = new Instant(start.getMillis() + 1 * 60 * 60 * 1000);
  result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DateTimeFieldType.minutes());
  assertEquals(60, result);
 }

 @Test
 public void testBetweenPartial() throws Exception {
  LocalDateTime dt1 = new LocalDateTime(2022, 1, 1, 0, 0);
  LocalDateTime dt2 = new LocalDateTime(2022, 1, 1, 0, 30);
  ReadablePeriod readablePeriod = new Period(1, 30, 0);

  // Test with null arguments
  assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, null, readablePeriod));

  // Test with non-contiguous ReadablePartial objects
  LocalDateTime dt3 = new LocalDateTime(2022, 1, 1, 0, 1, 0);
  ReadablePartial readablePartial1 = new LocalDate(2022, 1, 1).toLocalDateTime();
  ReadablePartial readablePartial2 = dt3;
  assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod));

  // Test with invalid size
  ReadablePartial readablePartial3 = new LocalDateTime(2022, 1, 1, 0, 0).plus(readablePeriod);
  assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod));

  // Test with different sets of fields
  ReadablePartial readablePartial4 = new LocalDateTime(2022, 1, 1, 0, 0).plus(new Period(1, 0, 0));
  assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod));

  // Test with valid arguments
  result = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
  assertEquals(30, result);
 }

 @Test
 public void testStandardPeriodIn() throws Exception {
  Period period = new Period(1, 30, 0);

  // Test with null argument
  assertNull(BaseSingleFieldPeriod.standardPeriodIn(null, 1));

  // Test with valid argument
  assertEquals(3600000, BaseSingleFieldPeriod.standardPeriodIn(period, 1));
  assertEquals(86400000, BaseSingleFieldPeriod.standardPeriodIn(period, 24));
 }

 @Test
 public void testConstructor() {
  int period = 123;
  BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(period);
  assertEquals(123, singleFieldPeriod.getValue());
 }

 @Test
 public void testGetValue() {
  BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(456);
  assertEquals(456, singleFieldPeriod.getValue());
 }

 @Test
 public void testSetValue() {
  BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(123);
  singleFieldPeriod.setValue(456);
  assertEquals(456, singleFieldPeriod.getValue());
 }

 @Test
 public void testGetFieldType() {
  throw new UnsupportedOperationException();
 }

 @Test
 public void testGetPeriodType() {
  throw new UnsupportedOperationException();
 }

 @Test
 public void testSize() {
  assertEquals(1, new BaseSingleFieldPeriod(1).size());
 }

 @Test
 public void testGetFieldType_indexOutOfBounds() {
  throw new UnsupportedOperationException();
 }

 @Test
 public void testGetValue_indexOutOfBounds() {
  throw new UnsupportedOperationException();
 }

 @Test
 public void testGet_durationFieldType() {
  throw new UnsupportedOperationException();
 }

 @Test
 public void testIsSupported_durationFieldType() {
  throw new UnsupportedOperationException();
 }

 @Test
 public void testToPeriod() {
  BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(123);
  Period period = singleFieldPeriod.toPeriod();
  assertEquals(new Period(1, 1, 0).withValue(123), period);
 }

 @Test
 public void testToMutablePeriod() {
  BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(123);
  MutablePeriod mutablePeriod = singleFieldPeriod.toMutablePeriod();
  assertEquals(new MutablePeriod().withValue(123), mutablePeriod);
 }

 @Test
 public void testEquals() {
  BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriod(123);
  BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriod(123);
  assertTrue(singleFieldPeriod1.equals(singleFieldPeriod2));

  BaseSingleFieldPeriod singleFieldPeriod3 = new BaseSingleFieldPeriod(456);
  assertFalse(singleFieldPeriod1.equals(singleFieldPeriod3));
 }

 @Test
 public void testHashCode() {
  BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriod(123);
  BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriod(123);
  assertEquals(singleFieldPeriod1.hashCode(), singleFieldPeriod2.hashCode());

  BaseSingleFieldPeriod singleFieldPeriod3 = new BaseSingleFieldPeriod(456);
  assertNotSame(singleFieldPeriod1.hashCode(), singleFieldPeriod3.hashCode());
 }

 @Test
 public void testCompareTo() {
  BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriod(123);
  BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriod(456);
  BaseSingleFieldPeriod singleFieldPeriod3 = new BaseSingleFieldPeriod(123);
  int result1 = singleFieldPeriod1.compareTo(singleFieldPeriod2);
  int result2 = singleFieldPeriod2.compareTo(singleFieldPeriod1);
  int result3 = singleFieldPeriod1.compareTo(singleFieldPeriod3);

  assertEquals(-1, result1);
  assertEquals(1, result2);
  assertEquals(0, result3);
 }
}
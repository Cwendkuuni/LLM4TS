package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod testObject;

 @Test
 public void testBetween_NullReadableInstant() {
  try {
   BaseSingleFieldPeriod.between(null, new Instant(0), DurationFieldType.MILLISECONDS);
  } catch (IllegalArgumentException e) {
   assertEquals("ReadableInstant objects must not be null", e.getMessage());
  }
 }

 @Test
 public void testBetween_NullReadableInstant2() {
  try {
   BaseSingleFieldPeriod.between(new Instant(0), null, DurationFieldType.MILLISECONDS);
  } catch (IllegalArgumentException e) {
   assertEquals("ReadableInstant objects must not be null", e.getMessage());
  }
 }

 @Test
 public void testBetween_NullDurationFieldType() {
  try {
   BaseSingleFieldPeriod.between(new Instant(0), new Instant(0), null);
  } catch (IllegalArgumentException e) {
   assertEquals("DurationFieldType object must not be null", e.getMessage());
  }
 }

 @Test
 public void testBetween_DifferentChronology() {
  Instant instant1 = new Instant(1516099200000L);
  Instant instant2 = new Instant(1516102800000L);
  Chronology chronology1 = GregorianChronology.getInstance();
  Chronology chronology2 = ISOChronology.getInstance();

  try {
   int result = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.MILLISECONDS);
   fail("Expected exception");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }

  testObject = new BaseSingleFieldPeriod(1);
  Instant instant3 = new Instant(1516099200000L, chronology1);
  Instant instant4 = new Instant(1516102800000L, chronology2);

  try {
   int result = BaseSingleFieldPeriod.between(instant3, instant4, DurationFieldType.MILLISECONDS);
   fail("Expected exception");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }

 @Test
 public void testBetween_SameInstant() {
  Instant instant = new Instant(1516099200000L);

  int result = BaseSingleFieldPeriod.between(instant, instant, DurationFieldType.MILLISECONDS);
  assertEquals(0, result);
 }

 @Test
 public void testBetween_SameValueButDifferentInstants() {
  Instant instant1 = new Instant(1516099200000L);
  Instant instant2 = new Instant(1516099201000L);

  int result = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.MILLISECONDS);
  assertEquals(1000, result);
 }

 @Test
 public void testBetween_DifferentValuesButSameInstant() {
  Instant instant = new Instant(1516099200000L);

  int result1 = BaseSingleFieldPeriod.between(instant, instant, DurationFieldType.MILLISECONDS);
  assertEquals(0, result1);

  int result2 = BaseSingleFieldPeriod.between(instant, instant, DurationFieldType.SECONDS);
  assertEquals(60 * 1000, result2);
 }

 @Test
 public void testBetween_DifferentValuesDifferentInstants() {
  Instant instant1 = new Instant(1516099200000L);
  Instant instant2 = new Instant(1516099300000L);

  int result1 = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.MILLISECONDS);
  assertEquals(3600 * 1000, result1);

  int result2 = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.SECONDS);
  assertEquals(3600, result2);
 }

 @Test
 public void testStandardPeriodIn_NullReadablePeriod() {
  try {
   BaseSingleFieldPeriod.standardPeriodIn(null, 1);
  } catch (IllegalArgumentException e) {
   assertEquals("ReadablePeriod object must not be null", e.getMessage());
  }
 }

 @Test
 public void testStandardPeriodIn_ZeroDuration() {
  int result = BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO, 1);
  assertEquals(0, result);
 }

 @Test
 public void testStandardPeriodIn_ZeroValue() {
  testObject = new BaseSingleFieldPeriod(0);

  int result1 = BaseSingleFieldPeriod.standardPeriodIn(testObject, 1);
  assertEquals(0, result1);

  int result2 = BaseSingleFieldPeriod.standardPeriodIn(new Period(0, DurationFieldType.DAYS, 1), 1);
  assertEquals(0, result2);
 }

 @Test
 public void testStandardPeriodIn_SingleValue() {
  testObject = new BaseSingleFieldPeriod(1);

  int result1 = BaseSingleFieldPeriod.standardPeriodIn(testObject, 1);
  assertEquals(1, result1);

  int result2 = BaseSingleFieldPeriod.standardPeriodIn(new Period(1, DurationFieldType.DAYS, 1), 1);
  assertEquals(1, result2);
 }

 @Test
 public void testStandardPeriodIn_MultipleValues() {
  testObject = new BaseSingleFieldPeriod(12345);

  int result1 = BaseSingleFieldPeriod.standardPeriodIn(testObject, 1);
  assertEquals(12345, result1);

  int result2 = BaseSingleFieldPeriod.standardPeriodIn(new Period(1, DurationFieldType.DAYS, 1), 12345);
  assertEquals(12345, result2);

  int result3 = BaseSingleFieldPeriod.standardPeriodIn(new Period(3, DurationFieldType.DAYS, 4), 12345);
  assertEquals(4 * 3 * 24 * 60 * 60 * 1000, result3);
 }

}
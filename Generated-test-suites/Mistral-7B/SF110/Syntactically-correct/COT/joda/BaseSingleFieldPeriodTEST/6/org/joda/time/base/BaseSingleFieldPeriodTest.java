package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createBaseSingleFieldPeriod(int iPeriod) {
  return new BaseSingleFieldPeriod(iPeriod) {
   @Override
   public DurationFieldType getFieldType() {
    return new GenericFieldType("TestFieldType", new TestPeriodType(), null);
   }
  };
 }

 @Test
 public void testConstructor() {
  int iPeriod = 5;
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(iPeriod);
  assertEquals(iPeriod, baseSingleFieldPeriod.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullArgument() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(null);
 }

 @Test
 public void testGetFieldType() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  DurationFieldType durationFieldType = baseSingleFieldPeriod.getFieldType();
  assertNotNull(durationFieldType);
 }

 @Test
 public void testGetPeriodType() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  PeriodType periodType = baseSingleFieldPeriod.getPeriodType();
  assertNotNull(periodType);
 }

 @Test
 public void testSize() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  int size = baseSingleFieldPeriod.size();
  assertEquals(1, size);
 }

 @Test
 public void testGetValue() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  int value = baseSingleFieldPeriod.getValue();
  assertEquals(5, value);
 }

 @Test
 public void testGetValueWithNegativeIndex() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  try {
   int value = baseSingleFieldPeriod.getValue(-1);
   fail("Expected IndexOutOfBoundsException");
  } catch (IndexOutOfBoundsException e) {
   // Expected
  }
 }

 @Test
 public void testSetValue() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  int newValue = 10;
  baseSingleFieldPeriod.setValue(newValue);
  assertEquals(newValue, baseSingleFieldPeriod.getValue());
 }

 @Test
 public void testSetValueWithNegativeIndex() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  try {
   baseSingleFieldPeriod.setValue(-1);
   fail("Expected IndexOutOfBoundsException");
  } catch (IndexOutOfBoundsException e) {
   // Expected
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueWithNullArgument() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  baseSingleFieldPeriod.setValue(null);
 }

 @Test
 public void testGet() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  DurationFieldType durationFieldType = baseSingleFieldPeriod.getFieldType();
  assertEquals(5, baseSingleFieldPeriod.get(durationFieldType));
 }

 @Test
 public void testIsSupported() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  DurationFieldType durationFieldType = baseSingleFieldPeriod.getFieldType();
  assertTrue(baseSingleFieldPeriod.isSupported(durationFieldType));
 }

 @Test
 public void testIsSupportedWithDifferentFieldType() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  DurationFieldType differentFieldType = new GenericFieldType("TestDifferentFieldType", new TestPeriodType(), null);
  assertFalse(baseSingleFieldPeriod.isSupported(differentFieldType));
 }

 @Test
 public void testToPeriod() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  Period period = baseSingleFieldPeriod.toPeriod();
  assertEquals(5, period.getValues()[0]);
 }

 @Test
 public void testToMutablePeriod() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
  assertEquals(5, mutablePeriod.getBaseValue(), 0.001d);
 }

 @Test
 public void testEquals() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(5);
  BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(5);
  assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
 }

 @Test
 public void testEqualsWithDifferentInstance() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(5);
  BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(6);
  assertFalse(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
 }

 @Test
 public void testEqualsWithDifferentClass() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  Object differentObject = new Object();
  assertFalse(baseSingleFieldPeriod.equals(differentObject));
 }

 @Test
 public void testCompareToWithSameInstance() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(5);
  BaseSingleFieldPeriod baseSingleFieldPeriod2 = baseSingleFieldPeriod1;
  int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
  assertEquals(0, result);
 }

 @Test
 public void testCompareToWithDifferentInstanceAndSameValue() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(5);
  BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(5);
  int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
  assertEquals(0, result);
 }

 @Test
 public void testCompareToWithDifferentInstanceAndDifferentValue() {
  BaseSingleFieldPeriod baseSingleFieldPeriod1 = createBaseSingleFieldPeriod(5);
  BaseSingleFieldPeriod baseSingleFieldPeriod2 = createBaseSingleFieldPeriod(6);
  int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
  assertEquals(-1, result);
 }

 @Test
 public void testCompareToWithNullArgument() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(5);
  try {
   int result = baseSingleFieldPeriod.compareTo(null);
   fail("Expected ClassCastException");
  } catch (ClassCastException e) {
   // Expected
  }
 }

 @Test
 public void testBetweenWithNullReadableInstant() {
  ReadableInstant readableInstant1 = null;
  ReadableInstant readableInstant2 = new DateTime();
  DurationFieldType durationFieldType = new GenericFieldType("TestFieldType", new TestPeriodType(), null);
  try {
   int result = BaseSingleFieldPeriod.between(readableInstant1, readableInstant2, durationFieldType);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testBetweenWithNullReadablePartial() {
  ReadablePartial readablePartial1 = null;
  ReadablePartial readablePartial2 = new DateTime();
  ReadablePeriod readablePeriod = Period.getInstance(1, TimeUnit.DAYS);
  try {
   int result = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testBetweenWithDifferentSizeReadablePartial() {
  ReadablePartial readablePartial1 = new DateTime();
  ReadablePartial readablePartial2 = new DateTime().withFieldAdded(ChronoField.HOUR_OF_DAY, 1);
  ReadablePeriod readablePeriod = Period.getInstance(1, TimeUnit.DAYS);
  try {
   int result = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testBetweenWithDifferentFieldTypesInReadablePartial() {
  ReadablePartial readablePartial1 = new DateTime().withFieldAdded(ChronoField.HOUR_OF_DAY, 1);
  ReadablePartial readablePartial2 = new DateTime().withFieldAdded(ChronoField.MINUTE_OF_HOUR, 1);
  ReadablePeriod readablePeriod = Period.getInstance(1, TimeUnit.DAYS);
  try {
   int result = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testBetweenWithNonContiguousReadablePartial() {
  ReadablePartial readablePartial1 = new DateTime().withFieldAdded(ChronoField.HOUR_OF_DAY, 1);
  ReadablePartial readablePartial2 = new DateTime().withFieldAdded(ChronoField.MINUTE_OF_HOUR, 1).withFieldAdded(ChronoField.HOUR_OF_DAY, 2);
  ReadablePeriod readablePeriod = Period.getInstance(1, TimeUnit.DAYS);
  try {
   int result = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testStandardPeriodInWithNullReadablePeriod() {
  ReadablePeriod readablePeriod = null;
  long n = 1;
  int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
  assertEquals(0, result);
 }

 @Test
 public void testStandardPeriodInWithZeroValueReadablePeriod() {
  ReadablePeriod readablePeriod = Period.ZERO;
  long n = 1;
  int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
  assertEquals(0, result);
 }

 @Test
 public void testStandardPeriodInWithPeriodWithNonPreciseField() {
  ReadablePeriod readablePeriod = Period.parse("PT0S1H");
  long n = 1;
  try {
   int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

}
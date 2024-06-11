package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.period.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createTestPeriod(int periodValue) {
  return new BaseSingleFieldPeriod(periodValue) {
    @Override
    public DurationFieldType getFieldType() {
      return new GenericFieldType("TestFieldType", PeriodType.instant()) {
        @Override
        public int getPrecision() {
          return 1;
        }
      };
    }
  };
 }

 @Test
 public void testGetValue() {
  BaseSingleFieldPeriod testPeriod = createTestPeriod(123);
  assertEquals(123, testPeriod.getValue());
 }

 @Test
 public void testSetValue() {
  BaseSingleFieldPeriod testPeriod = createTestPeriod(123);
  testPeriod.setValue(456);
  assertEquals(456, testPeriod.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueNegativeIndex() {
  BaseSingleFieldPeriod testPeriod = createTestPeriod(123);
  testPeriod.getValue(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeNegativeIndex() {
  BaseSingleFieldPeriod testPeriod = createTestPeriod(123);
  testPeriod.getFieldType(-1);
 }

 @Test
 public void testSize() {
  BaseSingleFieldPeriod testPeriod = createTestPeriod(123);
  assertEquals(1, testPeriod.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadableInstant() {
  BaseSingleFieldPeriod testPeriod = createTestPeriod(123);
  Chronology chrono = GregorianChronology.getInstance();
  try {
   Between.between(null, chrono.getInstants().get(0), testPeriod.getFieldType());
  } catch (IllegalArgumentException e) {
   assertEquals("ReadableInstant objects must not be null", e.getMessage());
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadablePartial() {
  BaseSingleFieldPeriod testPeriod = createTestPeriod(123);
  try {
   Between.between(new GeneralDatetime().toLocalDateTime(), null, testPeriod.getFieldType());
  } catch (IllegalArgumentException e) {
   assertEquals("ReadablePartial objects must not be null", e.getMessage());
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenDifferentSizeReadablePartial() {
  BaseSingleFieldPeriod testPeriod1 = createTestPeriod(123);
  BaseSingleFieldPeriod testPeriod2 = createTestPeriod(456);
  try {
   Between.between(new GeneralDatetime().toLocalDateTime(), new GeneralDatetime().toLocalDateTime().withFieldAdded(ChronoField.HOUR_OF_DAY, 1), testPeriod1, testPeriod2);
  } catch (IllegalArgumentException e) {
   assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNonContiguousReadablePartial() {
  BaseSingleFieldPeriod testPeriod1 = createTestPeriod(123);
  BaseSingleFieldPeriod testPeriod2 = createTestPeriod(456);
  try {
   Between.between(new GeneralDatetime().toLocalDateTime().withFieldAdded(ChronoField.DAY_OF_MONTH, -1), new GeneralDatetime().toLocalDateTime().withFieldAdded(ChronoField.DAY_OF_MONTH, 1), testPeriod1, testPeriod2);
  } catch (IllegalArgumentException e) {
   assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInNullReadablePeriod() {
  BaseSingleFieldPeriod testPeriod = createTestPeriod(123);
  try {
   Between.standardPeriodIn(null, 1);
  } catch (IllegalArgumentException e) {
   assertEquals("ReadablePeriod objects must not be null", e.getMessage());
  }
 }
}
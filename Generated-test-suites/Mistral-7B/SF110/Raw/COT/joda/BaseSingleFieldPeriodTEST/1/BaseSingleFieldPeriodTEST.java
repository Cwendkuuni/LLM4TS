package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createBaseSingleFieldPeriod(int periodValue, DurationFieldType fieldType) {
  return new BaseSingleFieldPeriod(periodValue) {
   @Override
   public DurationFieldType getFieldType() {
    return fieldType;
   }
  };
 }

 @Test
 public void testBetween_NullReadableInstant() {
  try {
   BaseSingleFieldPeriod.between(null, new Instant(), DurationFieldType.DAYS);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }

 @Test
 public void testBetween_NullReadableInstant2() {
  try {
   BaseSingleFieldPeriod.between(new Instant(), null, DurationFieldType.DAYS);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }

 @Test
 public void testBetween_NullDurationFieldType() {
  try {
   BaseSingleFieldPeriod.between(new Instant(), new Instant(), null);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }

 @Test
 public void testBetween_SameInstants() {
  int result = BaseSingleFieldPeriod.between(new Instant(), new Instant(), DurationFieldType.DAYS);
  assertEquals(0, result);
 }

 @Test
 public void testBetween_DifferentInstants() {
  Instant instant1 = new Instant(1451001600000L); // 2015-10-15 12:00:00.000Z
  Instant instant2 = new Instant(1451005200000L); // 2015-10-15 18:00:00.000Z
  int result = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.HOURS);
  assertEquals(6, result);
 }

 @Test
 public void testBetween_DifferentReadablePeriodicals() {
  DateTime dt1 = new DateTime(1451001600000L); // 2015-10-15 12:00:00.000Z
  DateTime dt2 = new DateTime(1451005200000L); // 2015-10-15 18:00:00.000Z
  ReadablePartial readablePartial1 = new GeneralDatetimeField(DateTimeFieldType.HOUR_OF_DAY, dt1, DateTimeFieldType.forOffset(1, 30));
  ReadablePartial readablePartial2 = new GeneralDatetimeField(DateTimeFieldType.HOUR_OF_DAY, dt2, DateTimeFieldType.forOffset(1, 30));
  int result = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, new DurationFieldType() {
   @Override
   public String getName() {
    return "Unknown DurationFieldType";
   }

   @Override
   public int getPrecision() {
    return Precision.SECONDS;
   }

   @Override
   public int getFieldValue(ReadablePartial readablePartial, Chronology chronology) {
    return 0;
   }

   @Override
   public DurationField getField(Chronology chronology) {
    throw new UnsupportedOperationException();
   }

   @Override
   public int getDifference(long value, long baseValue, Chronology chronology) {
    throw new UnsupportedOperationException();
   }
  });
  fail("Expected IllegalArgumentException");
 }

 @Test
 public void testBetween_InvalidReadablePartial() {
  DateTime dt = new DateTime(1451001600000L); // 2015-10-15 12:00:00.000Z
  ReadablePartial readablePartial = new GeneralDatetimeField(DateTimeFieldType.HOUR_OF_DAY, dt, DateTimeFieldType.forOffset(1, 30));
  try {
   BaseSingleFieldPeriod.between(readablePartial, null, new DurationFieldType() {
    // Anonymous inner class for test
   });
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }

  try {
   BaseSingleFieldPeriod.between(null, readablePartial, new DurationFieldType() {
    // Anonymous inner class for test
   });
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }

 @Test
 public void testBetween_InvalidReadablePeriodicals() {
  DateTime dt1 = new DateTime(1451001600000L); // 2015-10-15 12:00:00.000Z
  DateTime dt2 = new DateTime(1451005200000L); // 2015-10-15 18:00:00.000Z
  ReadablePartial readablePartial1 = new GeneralDatetimeField(DateTimeFieldType.HOUR_OF_DAY, dt1, DateTimeFieldType.forOffset(1, 30));
  ReadablePartial readablePartial2 = new GeneralDatetimeField(DateTimeFieldType.MINUTE_OF_HOUR, dt2, DateTimeFieldType.forOffset(1, 30));
  try {
   BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, new DurationFieldType() {
    // Anonymous inner class for test
   });
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }

 @Test
 public void testBetween_NonContiguousReadablePeriodicals() {
  DateTime dt1 = new DateTime(1451001600000L); // 2015-10-15 12:00:00.000Z
  DateTime dt2 = new DateTime(1451005200000L); // 2015-10-15 18:00:00.000Z
  ReadablePartial readablePartial1 = new GeneralDatetimeField(DateTimeFieldType.HOUR_OF_DAY, dt1, DateTimeFieldType.forOffset(1, 30));
  ReadablePartial readablePartial2 = new GeneralDatetimeField(DateTimeFieldType.MINUTE_OF_HOUR, dt2, DateTimeFieldType.forOffset(1, 30));
  try {
   BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, new DurationFieldType() {
    // Anonymous inner class for test
   });
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }

 @Test
 public void testBetween_DifferentChronologies() {
  DateTime dt1UTC = new DateTime(1451001600000L); // 2015-10-15 12:00:00.000Z
  DateTime dt2UTC = new DateTime(1451005200000L); // 2015-10-15 18:00:00.000Z
  ReadablePartial readablePartial1 = new GeneralDatetimeField(DateTimeFieldType.HOUR_OF_DAY, dt1UTC,DateTimeFieldType.forOffset(1, 30));
  ReadablePartial readablePartial2 = new GeneralDatetimeField(DateTimeFieldType.HOUR_OF_DAY, dt2UTC,DateTimeFieldType.forOffset(1, 30));
  ISOChronology utcChronology = ISOChronology.getInstanceUTC();
  GregorianChronology gregorianChronology = GregorianChronology.getInstance();
  int result = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, new DurationFieldType() {
   @Override
   public String getName() {
    return "Unknown DurationFieldType";
   }

   @Override
   public int getPrecision() {
    return Precision.SECONDS;
   }

   @Override
   public int getFieldValue(ReadablePartial readablePartial, Chronology chronology) {
    return 0;
   }

   @Override
   public DurationField getField(Chronology chronology) {
    throw new UnsupportedOperationException();
   }

   @Override
   public int getDifference(long value, long baseValue, Chronology chronology) {
    return (int)(chronology.get(this, value, baseValue) / 1000L);
   }
  });
  assertEquals(6 * 60, result); // Expected result: 6 hours * 60 minutes

  // Test with different chronologies having the same date but different time
  DateTime dt1Local = new DateTime(1451001600000L - 3600 * 1000); // 2015-10-15 11:00:00.000Z
  DateTime dt2Local = new DateTime(1451005200000L); // 2015-10-15 18:00:00.000Z
  int resultLocal = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, new DurationFieldType() {
   // Anonymous inner class for test
  });
  assertEquals(7 * 60, resultLocal); // Expected result: 7 hours * 60 minutes
 }

 @Test
 public void testStandardPeriodIn_NullReadablePeriod() {
  try {
   BaseSingleFieldPeriod.standardPeriodIn(null, 1000L);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }

 @Test
 public void testStandardPeriodIn_ZeroDivision() {
  try {
   BaseSingleFieldPeriod baseSingleFieldPeriod = createBaseSingleFieldPeriod(1, DurationFieldType.DAYS);
   BaseSingleFieldPeriod.standardPeriodIn(baseSingleFieldPeriod, 0L);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }

 @Test
 public void testStandardPeriodIn_ZeroPeriod() {
  int result = BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO, 1000L);
  assertEquals(0, result);
 }

 @Test
 public void testStandardPeriodIn_OnePeriod() {
  int result = BaseSingleFieldPeriod.standardPeriodIn(new Period(1, DurationFieldType.DAYS), 1000L);
  assertEquals(1, result);
 }

 @Test
 public void testStandardPeriodIn_MultiPeriod() {
  int result = BaseSingleFieldPeriod.standardPeriodIn(new Period(1, DurationFieldType.DAYS).withFields(new BasicFieldSet(new int[] { 2, 12 })), 1000L);
  assertEquals(2 * 24 * 60 * 60, result); // Expected result: 2 days * 24 hours * 60 minutes * 60 seconds
 }

 @Test
 public void testStandardPeriodIn_PeriodWithNonPreciseField() {
  try {
   BaseSingleFieldPeriod.standardPeriodIn(Period.DAYS_30, 1000L);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }
}
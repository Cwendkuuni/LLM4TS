package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createBaseSingleFieldPeriod(int iPeriod) {
  return new MySingleFieldPeriod(iPeriod);
 }

 @Test
 public void testBetween_ReadableInstant_ReadableInstant_DurationFieldType() {
  ReadableInstant readableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
  ReadableInstant readableInstant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
  DurationFieldType durationFieldType = Fields.millisOfSeconds();

  int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
  assertEquals(0, result);

  readableInstant2 = new DateTime(2022, 1, 2, 0, 0, 1, 0, ISOChronology.getInstanceUTC());
  result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
  assertEquals(1000, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadableInstant_Null_DurationFieldType() {
  ReadableInstant readableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
  DurationFieldType durationFieldType = Fields.millisOfSeconds();

  BaseSingleFieldPeriod.between(readableInstant, null, durationFieldType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_Null_ReadableInstant_DurationFieldType() {
  ReadableInstant readableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
  DurationFieldType durationFieldType = Fields.millisOfSeconds();

  BaseSingleFieldPeriod.between(null, readableInstant, durationFieldType);
 }

 @Test
 public void testBetween_ReadablePartial_ReadablePartial_ReadablePeriod() {
  ReadablePartial readablePartial = new GeneralBaseDate(2022, 1, 1, null);
  ReadablePartial readablePartial2 = new GeneralBaseDate(2022, 1, 2, null);
  ReadablePeriod readablePeriod = Duration.standardDays(1);

  int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
  assertEquals(0, result);

  result = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
  assertEquals(Duration.standardDays(1).getMillis(), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadablePartial_Null_ReadablePeriod() {
  ReadablePartial readablePartial = new GeneralBaseDate(2022, 1, 1, null);
  ReadablePeriod readablePeriod = null;

  BaseSingleFieldPeriod.between(readablePartial, null, readablePeriod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_Null_ReadablePartial_ReadablePeriod() {
  ReadablePartial readablePartial = new GeneralBaseDate(2022, 1, 1, null);
  ReadablePeriod readablePeriod = Duration.standardDays(1);

  BaseSingleFieldPeriod.between(null, readablePartial, readablePeriod);
 }

 @Test
 public void testBetween_ReadablePartial_ReadablePartial_WhenObjectsHaveDifferentNumberOfFields() {
  ReadablePartial readablePartial = new GeneralBaseDate(2022, 1, 1, null);
  ReadablePartial readablePartial2 = new GeneralBaseDate(2022, 1, 1, 2);
  ReadablePeriod readablePeriod = Duration.standardDays(1);

  try {
   BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
   fail("IllegalArgumentException expected");
  } catch (IllegalArgumentException e) {
   // expected
  }
 }

 @Test
 public void testBetween_ReadablePartial_ReadablePartial_WhenObjectsHaveDifferentFieldTypes() {
  ReadablePartial readablePartial = new GeneralBaseDate(2022, 1, 1, null);
  ReadablePartial readablePartial2 = new GeneralBaseDate(2022, 2, 1, null);
  ReadablePeriod readablePeriod = Duration.standardDays(1);

  try {
   BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
   fail("IllegalArgumentException expected");
  } catch (IllegalArgumentException e) {
   // expected
  }
 }

 @Test
 public void testBetween_ReadablePartial_ReadablePartial_WhenObjectsAreNotContiguous() {
  ReadablePartial readablePartial = new GeneralBaseDate(2022, 1, 1, null);
  ReadablePartial readablePartial2 = new GeneralBaseDate(2022, 1, 1, 2).withFieldAdded(Months.FIVE);
  ReadablePeriod readablePeriod = Duration.standardDays(1);

  try {
   BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
   fail("IllegalArgumentException expected");
  } catch (IllegalArgumentException e) {
   // expected
  }
 }

 @Test
 public void testStandardPeriodIn_ReadablePeriod_Long() {
  ReadablePeriod readablePeriod = Duration.standardDays(1);
  long n = 2;

  int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
  assertEquals(Duration.standardDays(2).getMillis() / n, result);
 }

 @Test
 public void testStandardPeriodIn_Null_ReadablePeriod() {
  ReadablePeriod readablePeriod = null;
  long n = 2;

  int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
  assertEquals(0, result);
 }

 @Test
 public void testStandardPeriodIn_ReadablePeriod_Zero() {
  ReadablePeriod readablePeriod = Duration.standardDays(1);
  long n = 0;

  int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
  assertEquals(0, result);
 }

 @Test
 public void testStandardPeriodIn_ReadablePeriod_NegativeNumber() {
  ReadablePeriod readablePeriod = Duration.standardDays(1);
  long n = -1;

  try {
   BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
   fail("IllegalArgumentException expected");
  } catch (IllegalArgumentException e) {
   // expected
  }
 }

 @Test
 public void testGetFieldType_MySingleFieldPeriod() {
  MySingleFieldPeriod mySingleFieldPeriod = new MySingleFieldPeriod(1);
  DurationFieldType result = mySingleFieldPeriod.getFieldType();
  assertEquals("MyFieldType", result.getClass().getSimpleName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetFieldType_WhenClassHasNoFieldType() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(0);
  try {
   baseSingleFieldPeriod.getFieldType();
   fail("UnsupportedOperationException expected");
  } catch (UnsupportedOperationException e) {
   // expected
  }
 }

 @Test
 public void testGetPeriodType_MySingleFieldPeriod() {
  MySingleFieldPeriod mySingleFieldPeriod = new MySingleFieldPeriod(1);
  PeriodType result = mySingleFieldPeriod.getPeriodType();
  assertEquals("MyPeriodType", result.getClass().getSimpleName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPeriodType_WhenClassHasNoPeriodType() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(0);
  try {
   baseSingleFieldPeriod.getPeriodType();
   fail("UnsupportedOperationException expected");
  } catch (UnsupportedOperationException e) {
   // expected
  }
 }

 @Test
 public void testSize_MySingleFieldPeriod() {
  MySingleFieldPeriod mySingleFieldPeriod = new MySingleFieldPeriod(1);
  int result = mySingleFieldPeriod.size();
  assertEquals(1, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSize_WhenClassHasNoSize() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(0);
  try {
   baseSingleFieldPeriod.size();
   fail("UnsupportedOperationException expected");
  } catch (UnsupportedOperationException e) {
   // expected
  }
 }

 @Test
 public void testGetValue_MySingleFieldPeriod() {
  MySingleFieldPeriod mySingleFieldPeriod = new MySingleFieldPeriod(1);
  int result = mySingleFieldPeriod.getValue();
  assertEquals(1, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue_WhenClassHasNoValue() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(0);
  try {
   baseSingleFieldPeriod.getValue();
   fail("UnsupportedOperationException expected");
  } catch (UnsupportedOperationException e) {
   // expected
  }
 }

 @Test
 public void testSetValue_MySingleFieldPeriod() {
  MySingleFieldPeriod mySingleFieldPeriod = new MySingleFieldPeriod(1);
  mySingleFieldPeriod.setValue(2);
  int result = mySingleFieldPeriod.getValue();
  assertEquals(2, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetValue_WhenClassHasNoSetValue() {
  BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(0);
  try {
   baseSingleFieldPeriod.setValue(1);
   fail("UnsupportedOperationException expected");
  } catch (UnsupportedOperationException e) {
   // expected
  }
 }

 @Test
 public void testEquals_ObjectsAreEqual() {
  MySingleFieldPeriod mySingleFieldPeriod1 = new MySingleFieldPeriod(1);
  MySingleFieldPeriod mySingleFieldPeriod2 = new MySingleFieldPeriod(1);

  boolean result = mySingleFieldPeriod1.equals(mySingleFieldPeriod2);
  assertTrue(result);
 }

 @Test
 public void testEquals_ObjectsAreNotEqual() {
  MySingleFieldPeriod mySingleFieldPeriod1 = new MySingleFieldPeriod(1);
  Object obj = new Object();

  boolean result = mySingleFieldPeriod1.equals(obj);
  assertFalse(result);
 }

 @Test
 public void testCompareTo_SameObjects() {
  MySingleFieldPeriod mySingleFieldPeriod1 = new MySingleFieldPeriod(1);
  MySingleFieldPeriod mySingleFieldPeriod2 = new MySingleFieldPeriod(1);

  int result = mySingleFieldPeriod1.compareTo(mySingleFieldPeriod2);
  assertEquals(0, result);
 }

 @Test
 public void testCompareTo_FirstObjectIsGreater() {
  MySingleFieldPeriod mySingleFieldPeriod1 = new MySingleFieldPeriod(2);
  MySingleFieldPeriod mySingleFieldPeriod2 = new MySingleFieldPeriod(1);

  int result = mySingleFieldPeriod1.compareTo(mySingleFieldPeriod2);
  assertEquals(1, result);
 }

 @Test
 public void testCompareTo_FirstObjectIsLess() {
  MySingleFieldPeriod mySingleFieldPeriod1 = new MySingleFieldPeriod(1);
  MySingleFieldPeriod mySingleFieldPeriod2 = new MySingleFieldPeriod(2);

  int result = mySingleFieldPeriod1.compareTo(mySingleFieldPeriod2);
  assertEquals(-1, result);
 }

 @Test
 public void testCompareTo_DifferentClasses() {
  MySingleFieldPeriod mySingleFieldPeriod1 = new MySingleFieldPeriod(1);
  AnotherSingleFieldPeriod anotherSingleFieldPeriod = new AnotherSingleFieldPeriod(1);

  int result = mySingleFieldPeriod1.compareTo(anotherSingleFieldPeriod);
  assertNotEquals(0, result);
 }

 private static class MySingleFieldPeriod extends BaseSingleFieldPeriod {
  public MySingleFieldPeriod(int iPeriod) {
   super(iPeriod);
  }

  public static class MyFieldType extends BaseSingleFieldPeriod.BaseSingleFieldPeriodType<MyFieldType, MySingleFieldPeriod> {
   public static final MyFieldType INSTANCE = new MyFieldType();

   @Override
   public String getName() {
    return "MyFieldType";
   }
  }

  @Override
  public MyFieldType getFieldType() {
   return MyFieldType.INSTANCE;
  }

  @Override
  public Class<MyPeriodType> getPeriodTypeClass() {
   return MyPeriodType.class;
  }

  public static class MyPeriodType extends BaseSingleFieldPeriod.BasePeriodType<MyPeriodType, MySingleFieldPeriod> {
   @Override
   public String getName() {
    return "MyPeriodType";
   }
  }
 }

}
package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createInstance(int periodValue) {
  return new BaseSingleFieldPeriod(periodValue) {
   @Override
   public DurationFieldType getFieldType() {
    return new GenericFieldType("TEST", PeriodType.instance(PeriodType.FIELD_COUNT));
   }
  };
 }

 @Test
 public void testConstructor() {
  int periodValue = 123;
  BaseSingleFieldPeriod instance = createInstance(periodValue);
  assertEquals(periodValue, instance.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullPeriodValue() {
  BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(null);
 }

 @Test
 public void testGetValue() {
  int periodValue = 123;
  BaseSingleFieldPeriod instance = createInstance(periodValue);
  assertEquals(periodValue, instance.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithWrongIndex() {
  BaseSingleFieldPeriod instance = createInstance(123);
  instance.getValue(1);
 }

 @Test
 public void testSetValue() {
  int oldPeriodValue = 123;
  int newPeriodValue = 456;
  BaseSingleFieldPeriod instance = createInstance(oldPeriodValue);
  instance.setValue(newPeriodValue);
  assertEquals(newPeriodValue, instance.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetValueWithWrongIndex() {
  int periodValue = 123;
  BaseSingleFieldPeriod instance = createInstance(periodValue);
  instance.setValue(1, 456);
 }

 @Test
 public void testGetFieldType() {
  BaseSingleFieldPeriod instance = createInstance(123);
  DurationFieldType fieldType = instance.getFieldType();
  assertNotNull(fieldType);
  assertEquals("TEST", fieldType.getName());
 }

 @Test
 public void testGetPeriodType() {
  BaseSingleFieldPeriod instance = createInstance(123);
  PeriodType periodType = instance.getPeriodType();
  assertNotNull(periodType);
  assertEquals("TEST", periodType.getName());
 }

 @Test
 public void testSize() {
  BaseSingleFieldPeriod instance = createInstance(123);
  assertEquals(1, instance.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeWithWrongIndex() {
  BaseSingleFieldPeriod instance = createInstance(123);
  instance.getFieldType(1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithWrongIndex() {
  BaseSingleFieldPeriod instance = createInstance(123);
  instance.getValue(1);
 }

 @Test
 public void testGet() {
  BaseSingleFieldPeriod instance = createInstance(123);
  DurationFieldType fieldType = instance.getFieldType();
  assertEquals(instance.getValue(), instance.get(fieldType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithWrongFieldType() {
  BaseSingleFieldPeriod instance = createInstance(123);
  assertNotSame(instance.getFieldType(), new DurationFieldType("WRONG_TYPE"));
  assertFalse(instance.isSupported(new DurationFieldType("WRONG_TYPE")));
  try {
   instance.get(new DurationFieldType("WRONG_TYPE"));
  } catch (IllegalArgumentException e) {
   //expected
  }
 }

 @Test
 public void testToPeriod() {
  BaseSingleFieldPeriod instance = createInstance(123);
  Period period = instance.toPeriod();
  assertEquals(instance.getValue(), period.getValues()[0]);
 }

 @Test
 public void testToMutablePeriod() {
  BaseSingleFieldPeriod instance = createInstance(123);
  MutablePeriod mutablePeriod = instance.toMutablePeriod();
  assertEquals(instance.getValue(), mutablePeriod.getValue(0));
  mutablePeriod.add(new BaseSingleFieldPeriod(456));
  assertEquals(instance.getValue() + 456, mutablePeriod.getValue(0));
 }

 @Test
 public void testEquals() {
  BaseSingleFieldPeriod instance1 = createInstance(123);
  BaseSingleFieldPeriod instance2 = createInstance(123);
  assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testEqualsWithWrongClass() {
  BaseSingleFieldPeriod instance = createInstance(123);
  assertFalse(instance.equals(new Object()));
 }

 @Test
 public void testEqualsWithWrongValue() {
  BaseSingleFieldPeriod instance1 = createInstance(123);
  BaseSingleFieldPeriod instance2 = createInstance(456);
  assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testCompareTo() {
  BaseSingleFieldPeriod instance1 = createInstance(123);
  BaseSingleFieldPeriod instance2 = createInstance(456);
  int comparisonResult = instance1.compareTo(instance2);
  assertTrue(comparisonResult < 0);
 }

 @Test
 public void testCompareToWithSameInstance() {
  BaseSingleFieldPeriod instance = createInstance(123);
  int comparisonResult = instance.compareTo(instance);
  assertEquals(0, comparisonResult);
 }

 @Test
 public void testCompareToWithSameValue() {
  BaseSingleFieldPeriod instance1 = createInstance(123);
  BaseSingleFieldPeriod instance2 = createInstance(123);
  int comparisonResult = instance1.compareTo(instance2);
  assertEquals(0, comparisonResult);
 }

 @Test
 public void testCompareToWithNegativeValue() {
  BaseSingleFieldPeriod instance1 = createInstance(456);
  BaseSingleFieldPeriod instance2 = createInstance(123);
  int comparisonResult = instance1.compareTo(instance2);
  assertTrue(comparisonResult > 0);
 }

 @Test
 public void testStandardPeriodIn() {
  BaseSingleFieldPeriod instance = createInstance(123);
  assertEquals(123, BaseSingleFieldPeriod.standardPeriodIn(instance, 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInWithNullPeriod() {
  BaseSingleFieldPeriod.standardPeriodIn(null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInWithNegativeStep() {
  BaseSingleFieldPeriod instance = createInstance(123);
  try {
   BaseSingleFieldPeriod.standardPeriodIn(instance, -1);
  } catch (IllegalArgumentException e) {
   //expected
  }
 }

 @Test
 public void testBetween() {
  Instant instant1 = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
  Instant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
  int expectedPeriodValue = 86400000;
  assertEquals(expectedPeriodValue, BaseSingleFieldPeriod.between(instant1, instant2, new Days()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullFirstInstant() {
  Instant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
  try {
   BaseSingleFieldPeriod.between(null, instant2, new Days());
  } catch (IllegalArgumentException e) {
   //expected
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithNullSecondInstant() {
  Instant instant1 = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
  try {
   BaseSingleFieldPeriod.between(instant1, null, new Days());
  } catch (IllegalArgumentException e) {
   //expected
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWithDifferentFieldTypes() {
  Instant instant1 = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
  Instant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
  try {
   BaseSingleFieldPeriod.between(instant1, instant2, new Days());
   BaseSingleFieldPeriod.between(instant1, instant2, new Hours());
  } catch (IllegalArgumentException e) {
   //expected
  }
 }

 @Test
 public void testBetweenWithSameInstant() {
  Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
  int expectedPeriodValue = 0;
  assertEquals(expectedPeriodValue, BaseSingleFieldPeriod.between(instant, instant, new Days()));
 }
}
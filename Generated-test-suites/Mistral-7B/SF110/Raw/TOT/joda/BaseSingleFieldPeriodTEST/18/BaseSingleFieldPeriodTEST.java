package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.field.DecimalField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.IntField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 @Test
 public void testBetweenMethod() {
 ReadableInstant instant1 = new DateTime(1972, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 ReadableInstant instant2 = new DateTime(1972, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 BaseSingleFieldPeriod period = new MyPeriod(5);

 int result = BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType());
 assertEquals(5 * 24 * 60 * 60 * 1000, result); // 5 days in milliseconds
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodWithNullReadableInstant() {
 BaseSingleFieldPeriod period = new MyPeriod(5);
 ReadableInstant instant1 = null;
 ReadableInstant instant2 = new DateTime(1972, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 int result = BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodWithNullReadableInstant2() {
 BaseSingleFieldPeriod period = new MyPeriod(5);
 ReadableInstant instant1 = new DateTime(1972, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 ReadableInstant instant2 = null;
 int result = BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodWithDifferentFieldTypes() {
 ReadableInstant instant1 = new DateTime(1972, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 ReadableInstant instant2 = new DateTime(1972, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 BaseSingleFieldPeriod period1 = new MyPeriod(5, IntField.DAYS);
 BaseSingleFieldPeriod period2 = new MyPeriod(2, DecimalField.HOURS.asType(DecimalFieldType.HALF_HOUR));
 int result = BaseSingleFieldPeriod.between(instant1, instant2, period1.getFieldType());
 int result2 = BaseSingleFieldPeriod.between(instant1, instant2, period2.getFieldType());
 assertNotEquals(result, result2);
 }

 @Test
 public void testStandardPeriodInMethod() {
 ReadablePeriod period = new Period(5, IntField.DAYS);
 long n = 1000 * 60 * 60 * 24; // 1 day in milliseconds
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
 assertEquals(5, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInMethodWithNullReadablePeriod() {
 int result = BaseSingleFieldPeriod.standardPeriodIn(null, 1000 * 60 * 60 * 24);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testConstructor() {
 BaseSingleFieldPeriod period = new MyPeriod(5);
 assertEquals(5, period.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeValue() {
 BaseSingleFieldPeriod period = new MyPeriod(-5);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetValueMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5);
 assertEquals(5, period.getValue());
 }

 @Test
 public void testSetValueMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5);
 period.setValue(10);
 assertEquals(10, period.getValue());
 }

 @Test
 public void testGetFieldTypeMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5, IntField.DAYS);
 assertEquals(IntField.DAYS, period.getFieldType());
 }

 @Test
 public void testGetPeriodTypeMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5, IntField.DAYS);
 assertEquals(PeriodType.fixedPeriod, period.getPeriodType());
 }

 @Test
 public void testSizeMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5, IntField.DAYS);
 assertEquals(1, period.size());
 }

 @Test
 public void testGetFieldTypeAtMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5, IntField.DAYS);
 assertEquals(IntField.DAYS, period.getFieldType(0));
 }

 @Test
 public void testGetValueAtMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5, IntField.DAYS);
 assertEquals(5, period.getValue(0));
 }

 @Test
 public void testGetMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5, IntField.DAYS);
 assertEquals(5, period.get(IntField.DAYS));
 }

 @Test
 public void testIsSupportedMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5, IntField.DAYS);
 assertTrue(period.isSupported(IntField.DAYS));
 }

 @Test
 public void testToPeriodMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5, IntField.DAYS);
 Period result = period.toPeriod();
 assertEquals("P5D", result.toString());
 }

 @Test
 public void testToMutablePeriodMethod() {
 BaseSingleFieldPeriod period = new MyPeriod(5, IntField.DAYS);
 MutablePeriod result = period.toMutablePeriod();
 result.add(new Period(1, IntField.DAYS));
 assertEquals("P6D", result.toString());
 }

 @Test
 public void testEqualsMethod() {
 BaseSingleFieldPeriod period1 = new MyPeriod(5, IntField.DAYS);
 BaseSingleFieldPeriod period2 = new MyPeriod(5, IntField.DAYS);
 assertTrue(period1.equals(period2));
 }

 @Test
 public void testNotEqualsMethod() {
 BaseSingleFieldPeriod period1 = new MyPeriod(5, IntField.DAYS);
 BaseSingleFieldPeriod period2 = new MyPeriod(10, IntField.DAYS);
 assertFalse(period1.equals(period2));
 }

 @Test
 public void testHashCodeMethod() {
 BaseSingleFieldPeriod period1 = new MyPeriod(5, IntField.DAYS);
 BaseSingleFieldPeriod period2 = new MyPeriod(5, IntField.DAYS);
 assertEquals(period1.hashCode(), period2.hashCode());
 }

 @Test
 public void testCompareToMethod() {
 BaseSingleFieldPeriod period1 = new MyPeriod(5, IntField.DAYS);
 BaseSingleFieldPeriod period2 = new MyPeriod(10, IntField.DAYS);
 BaseSingleFieldPeriod period3 = new MyPeriod(5, IntField.DAYS);
 assertEquals(0, period1.compareTo(period3));
 assertEquals(1, period2.compareTo(period1));
 }
}

class MyPeriod extends BaseSingleFieldPeriod {

 public MyPeriod(int iPeriod) {
 super(iPeriod);
 }

 public MyPeriod(int iPeriod, DurationFieldType fieldType) {
 super(iPeriod);
 }

 @Override
 public DurationFieldType getFieldType() {
 return IntField.DAYS;
 }
}
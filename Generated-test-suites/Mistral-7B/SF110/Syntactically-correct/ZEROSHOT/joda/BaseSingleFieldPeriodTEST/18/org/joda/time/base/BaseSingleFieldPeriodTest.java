package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod testPeriod;
 private int testValue;
 private DurationFieldType testFieldType;

 @Test
 public void testConstructor() {
 testValue = 5;
 testFieldType = DurationFieldType.DAYS;
 testPeriod = new BaseSingleFieldPeriod(testValue);
 assertEquals(testValue, testPeriod.getValue());
 }

 @Test
 public void testGetFieldType() {
 testFieldType = DurationFieldType.DAYS;
 testPeriod = new BaseSingleFieldPeriod(testValue) {
 @Override
 public DurationFieldType getFieldType() {
 return testFieldType;
 }
 };
 assertEquals(testFieldType, testPeriod.getFieldType());
 }

 @Test
 public void testGetPeriodType() {
 testPeriod = new BaseSingleFieldPeriod(testValue);
 assertEquals(PeriodType.instance(), testPeriod.getPeriodType());
 }

 @Test
 public void testSize() {
 testPeriod = new BaseSingleFieldPeriod(testValue);
 assertEquals(1, testPeriod.size());
 }

 @Test
 public void testGetValue() {
 testPeriod = new BaseSingleFieldPeriod(testValue);
 assertEquals(testValue, testPeriod.getValue());
 }

 @Test
 public void testSetValue() {
 testPeriod = new BaseSingleFieldPeriod(testValue);
 testValue = 10;
 testPeriod.setValue(testValue);
 assertEquals(testValue, testPeriod.getValue());
 }

 @Test
 public void testGetValueWithIndexOutOfBoundsException() {
 testPeriod = new BaseSingleFieldPeriod(testValue);
 try {
 testPeriod.getValue(1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testGetFieldTypeWithIndexOutOfBoundsException() {
 testPeriod = new BaseSingleFieldPeriod(testValue);
 try {
 testPeriod.getFieldType(1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testBetween() {
 ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 ReadableInstant instant2 = new DateTime(2022, 2, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 DurationFieldType fieldType = DurationFieldType.DAYS;
 int expectedDays = 30;
 int actualDays = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
 assertEquals(expectedDays, actualDays);
 }

 @Test
 public void testBetweenWithNullReadableInstant() {
 assertNull(BaseSingleFieldPeriod.between(null, instant1, fieldType));
 }

 @Test
 public void testBetweenWithNullReadableInstant2() {
 assertNull(BaseSingleFieldPeriod.between(instant1, null, fieldType));
 }

 @Test
 public void testStandardPeriodIn() {
 Period testPeriod1 = new Period(1, DurationFieldType.DAYS, PeriodType.instance());
 Period testPeriod2 = new Period(2, DurationFieldType.DAYS, PeriodType.instance());
 long divisor = 1000L;
 int expectedValue = 15;
 int actualValue = BaseSingleFieldPeriod.standardPeriodIn(testPeriod1, divisor * 10);
 assertEquals(expectedValue, actualValue);
 expectedValue = 3;
 actualValue = BaseSingleFieldPeriod.standardPeriodIn(testPeriod2, divisor);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testStandardPeriodInWithNullReadablePeriod() {
 assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, divisor * 10));
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod testPeriod1 = new BaseSingleFieldPeriod(testValue);
 BaseSingleFieldPeriod testPeriod2 = new BaseSingleFieldPeriod(testValue);
 assertTrue(testPeriod1.equals(testPeriod2));
 BaseSingleFieldPeriod testPeriod3 = new BaseSingleFieldPeriod(testValue + 1);
 assertFalse(testPeriod1.equals(testPeriod3));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod testPeriod1 = new BaseSingleFieldPeriod(testValue);
 BaseSingleFieldPeriod testPeriod2 = new BaseSingleFieldPeriod(testValue);
 assertEquals(testPeriod1.hashCode(), testPeriod2.hashCode());
 BaseSingleFieldPeriod testPeriod3 = new BaseSingleFieldPeriod(testValue + 1);
 assertNotEquals(testPeriod1.hashCode(), testPeriod3.hashCode());
 }

 @Test
 public void testCompareTo() {
 BaseSingleFieldPeriod testPeriod1 = new BaseSingleFieldPeriod(testValue);
 BaseSingleFieldPeriod testPeriod2 = new BaseSingleFieldPeriod(testValue);
 assertEquals(0, testPeriod1.compareTo(testPeriod2));
 BaseSingleFieldPeriod testPeriod3 = new BaseSingleFieldPeriod(testValue + 1);
 assertTrue(testPeriod1.compareTo(testPeriod3) < 0);
 }

 @Test
 public void testCompareToWithNullObject() {
 BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(testValue);
 try {
 testPeriod.compareTo(null);
 fail("Expected ClassCastException");
 } catch (ClassCastException e) {
 // expected
 }
 }
}
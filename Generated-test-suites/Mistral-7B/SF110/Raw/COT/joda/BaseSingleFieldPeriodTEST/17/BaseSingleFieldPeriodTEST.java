package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod getTestInstance(int iPeriod) {
 return new TestSingleFieldPeriod(iPeriod);
 }

 @Test
 public void testBetween_ReadableInstant_ReadableInstant_DurationFieldType() {
 // Test with valid arguments
 int result = BaseSingleFieldPeriod.between(DateTime.now(), DateTime.now().plus(Duration.standardHours(1)), DurationFieldType.standardHours());
 assertEquals(1, result);

 // Test with null arguments
 try {
 BaseSingleFieldPeriod.between(null, DateTime.now(), DurationFieldType.standardHours());
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(DateTime.now(), null, DurationFieldType.standardHours());
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(null, null, DurationFieldType.standardHours());
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBetween_ReadablePartial_ReadablePartial_ReadablePeriod() {
 // Test with valid arguments
 DateTimeZone datetimeZone = DateTimeZone.forID("America/New_York");
 BaseDateTime baseDateTime1 = new BaseDateTime(1468800000000L, datetimeZone);
 BaseDateTime baseDateTime2 = new BaseDateTime(1468820000000L, datetimeZone);
 ReadablePartial readablePartial1 = baseDateTime1.toReadablePartial();
 ReadablePartial readablePartial2 = baseDateTime2.toReadablePartial();
 ReadablePeriod readablePeriod = Duration.standardHours(1);
 int result = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
 assertEquals(1, result);

 // Test with null arguments
 try {
 BaseSingleFieldPeriod.between(null, readablePartial1, readablePeriod);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(readablePartial1, null, readablePeriod);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(null, null, readablePeriod);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testStandardPeriodIn_ReadablePeriod_Long() {
 // Test with valid arguments
 ReadablePeriod readablePeriod = Duration.standardDays(3).plus(Duration.standardHours(2));
 long n = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 assertEquals(2, result);

 // Test with null argument
 ReadablePeriod nullReadablePeriod = null;
 try {
 BaseSingleFieldPeriod.standardPeriodIn(nullReadablePeriod, n);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodIn_ReadablePeriod_Long_InvalidPreciseField() {
 // Test with a non-precise field
 ReadablePeriod readablePeriod = Duration.standardDays(1).plus(Duration.standardMinutes(30).withMillisAdded(1));
 long n = 60;
 BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testConstructor_Int() {
 // Test with valid arguments
 int iPeriod = 12;
 BaseSingleFieldPeriod testInstance = getTestInstance(iPeriod);
 assertEquals(iPeriod, testInstance.getValue());

 // Test with negative arguments
 try {
 getTestInstance(-1);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testGetterAndSetter_Value() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 int oldValue = testInstance.getValue();
 testInstance.setValue(20);
 int newValue = testInstance.getValue();
 assertEquals(20, newValue);
 assertEquals(10, oldValue);
}

 @Test
 public void testGetFieldType_EmptyMethod() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 assertNotNull(testInstance.getFieldType());
 // This method does not return anything, just get the field type instance
}

 @Test
 public void testGetPeriodType_EmptyMethod() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 assertNotNull(testInstance.getPeriodType());
 // This method does not return anything, just get the period type instance
}

 @Test
 public void testSize_EmptyMethod() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 assertEquals(1, testInstance.size());
}

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldType_InvalidIndex() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 testInstance.getFieldType(1);
 fail("IndexOutOfBoundsException expected");
}

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValue_InvalidIndex() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 testInstance.getValue(1);
 fail("IndexOutOfBoundsException expected");
}

 @Test
 public void testGet_DurationFieldType() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 DurationFieldType expected = testInstance.getFieldType();
 DurationFieldType actual = testInstance.getFieldType();
 assertEquals(expected, actual);
}

 @Test
 public void testIsSupported_DurationFieldType() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 assertTrue(testInstance.isSupported(testInstance.getFieldType()));
 assertFalse(testInstance.isSupported(DurationFieldType.standardDays()));
}

 @Test
 public void testToPeriod_EmptyMethod() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 Period expected = Period.ZERO;
 Period actual = testInstance.toPeriod();
 assertEquals(expected, actual);
}

 @Test
 public void testToMutablePeriod_EmptyMethod() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 MutablePeriod expected = new MutablePeriod();
 MutablePeriod actual = testInstance.toMutablePeriod();
 assertEquals(expected, actual);
}

 @Test
 public void testEquals_Object() {
 BaseSingleFieldPeriod testInstance1 = getTestInstance(10);
 BaseSingleFieldPeriod testInstance2 = getTestInstance(10);
 assertTrue(testInstance1.equals(testInstance2));

 BaseSingleFieldPeriod testInstance3 = getTestInstance(5);
 assertFalse(testInstance1.equals(testInstance3));

 BaseSingleFieldPeriod testInstance4 = new TestSingleFieldPeriod(10.5); // Different class
 assertFalse(testInstance1.equals(testInstance4));
}

 @Test
 public void testHashCode_EmptyMethod() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 int expectedHashCode = 27 * (27 * 17 + 10) + testInstance.getFieldType().hashCode();
 int actualHashCode = testInstance.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
}

 @Test
 public void testCompareTo_BaseSingleFieldPeriod() {
 BaseSingleFieldPeriod testInstance1 = getTestInstance(10);
 BaseSingleFieldPeriod testInstance2 = getTestInstance(5);
 int expected = 5;
 int actual = testInstance1.compareTo(testInstance2);
 assertEquals(expected, actual);
}

 @Test(expected = ClassCastException.class)
 public void testCompareTo_DifferentClass() {
 BaseSingleFieldPeriod testInstance = getTestInstance(10);
 Object testInstance2 = new Object();
 int expected = -1;
 int actual = testInstance.compareTo(testInstance2);
 fail("ClassCastException expected");
}

}

 class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
 TestSingleFieldPeriod(int iPeriod) {
 super(iPeriod);
 }

 @Override
 DurationFieldType getFieldType() {
 return DurationFieldTypes.standardHours();
 }
 }
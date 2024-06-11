package org.joda.time.base;

import org.junit.Test;
import org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod testInstance;

 @Test
 public void testConstructor() {
 testInstance = new BaseSingleFieldPeriod(123);
 assertEquals(123, testInstance.getValue());
 }

 @Test
 public void testGetFieldType() {
 try {
 testInstance = new BaseSingleFieldPeriod(0);
 DurationFieldType fieldType = testInstance.getFieldType();
 Fail.fail("Expected exception: IllegALArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }

 testInstance = new BaseSingleFieldPeriod(123);
 DurationFieldType fieldType = testInstance.getFieldType();
 assertEquals(DurationFieldType.DAYS, fieldType);
 }

 @Test
 public void testGetPeriodType() {
 testInstance = new BaseSingleFieldPeriod(123);
 PeriodType periodType = testInstance.getPeriodType();
 assertEquals(PeriodType.createInstance("P" + DurationFieldType.DAYS), periodType);
 }

 @Test
 public void testSize() {
 testInstance = new BaseSingleFieldPeriod(123);
 assertEquals(1, testInstance.size());
 }

 @Test
 public void testGetValue() {
 testInstance = new BaseSingleFieldPeriod(123);
 assertEquals(123, testInstance.getValue());
 }

 @Test
 public void testSetValue() {
 testInstance = new BaseSingleFieldPeriod(123);
 testInstance.setValue(456);
 assertEquals(456, testInstance.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeWithWrongIndex() {
 testInstance = new BaseSingleFieldPeriod(123);
 testInstance.getFieldType(1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithWrongIndex() {
 testInstance = new BaseSingleFieldPeriod(123);
 testInstance.getValue(1);
 }

 @Test
 public void testGetValueFromAnotherInstance() {
 testInstance = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod anotherInstance = new BaseSingleFieldPeriod(123);
 assertEquals(123, anotherInstance.getValue());
 }

 @Test
 public void testEquals() {
 testInstance = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod anotherInstance = new BaseSingleFieldPeriod(123);
 assertTrue(testInstance.equals(anotherInstance));
 }

 @Test
 public void testNotEquals() {
 testInstance = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod anotherInstance = new BaseSingleFieldPeriod(456);
 assertFalse(testInstance.equals(anotherInstance));
 }

 @Test
 public void testHashCode() {
 testInstance = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod anotherInstance = new BaseSingleFieldPeriod(123);
 assertEquals(testInstance.hashCode(), anotherInstance.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentValues() {
 testInstance = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod anotherInstance = new BaseSingleFieldPeriod(456);
 assertNotSame(testInstance.hashCode(), anotherInstance.hashCode());
 }

 @Test
 public void testCompareTo() {
 testInstance = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod anotherInstance1 = new BaseSingleFieldPeriod(111);
 BaseSingleFieldPeriod anotherInstance2 = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod anotherInstance3 = new BaseSingleFieldPeriod(124);

 assertEquals(0, testInstance.compareTo(testInstance));
 assertEquals(1, testInstance.compareTo(anotherInstance1));
 assertEquals(0, testInstance.compareTo(anotherInstance2));
 assertEquals(-1, testInstance.compareTo(anotherInstance3));
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToWithWrongClass() {
 testInstance = new BaseSingleFieldPeriod(123);
 Object anotherInstance = new Object();
 assertEquals(-1, testInstance.compareTo(anotherInstance));
 }

 @Test
 public void testToString() {
 testInstance = new BaseSingleFieldPeriod(123);
 String stringRepresentation = testInstance.toString();
 String expected = "P123D";
 assertEquals(expected, stringRepresentation);
 }

 @Test
 public void testBetweenMethodWithNullReadableInstants() {
 try {
 BaseSingleFieldPeriod.between(null, null, DurationFieldType.DAYS);
 Fail.fail("Expected exception: IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testBetweenMethodWithValidReadableInstantsAndFieldType() {
 DateTimeZone dtZone = DateTimeZone.forID("America/New_York");
 DateTime start = new DateTime(2022, 5, 1, 12, 30, 0, 0, dtZone);
 DateTime end = new DateTime(2022, 5, 2, 12, 30, 0, 0, dtZone);
 testBetweenMethod(start, end, DurationFieldType.DAYS);
 }

 @Test
 public void testBetweenMethodWithInvalidReadableInstants() {
 try {
 BaseSingleFieldPeriod.between(null, new DateTime(), DurationFieldType.DAYS);
 Fail.fail("Expected exception: IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }

 try {
 BaseSingleFieldPeriod.between(new DateTime(), null, DurationFieldType.DAYS);
 Fail.fail("Expected exception: IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testBetweenMethodWithDifferentChronologies() {
 DateTimeZone dtZone1 = DateTimeZone.forID("America/New_York");
 DateTime start1 = new DateTime(2022, 5, 1, 12, 30, 0, 0, dtZone1);
 DateTime end1 = new DateTime(2022, 5, 2, 12, 30, 0, 0, dtZone1);
 DateTimeZone dtZone2 = DateTimeZone.forID("Europe/London");
 DateTime start2 = new DateTime(2022, 5, 1, 12, 30, 0, 0, dtZone2);
 DateTime end2 = new DateTime(2022, 5, 2, 12, 30, 0, 0, dtZone2);

 int result = BaseSingleFieldPeriod.between(start1, end1, DurationFieldType.DAYS);
 int expected = 1;
 assertEquals(expected, result);

 int result2 = BaseSingleFieldPeriod.between(start2, end2, DurationFieldType.DAYS);
 int expected2 = 0;
 assertEquals(expected2, result2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenMethodWithInvalidFieldType() {
 DateTime start = new DateTime(2022, 5, 1, 12, 30, 0, 0, DateTimeZone.forID("America/New_York"));
 DateTime end = new DateTime(2022, 5, 2, 12, 30, 0, 0, DateTimeZone.forID("America/New_York"));
 try {
 BaseSingleFieldPeriod.between(start, end, null);
 Fail.fail("Expected exception: IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testBetweenMethodWithSameInstant() {
 DateTime start = new DateTime(2022, 5, 1, 12, 30, 0, 0, DateTimeZone.forID("America/New_York"));
 DateTime end = start;
 int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
 int expected = 0;
 assertEquals(expected, result);
 }

 @Test
 public void testBetweenMethodWithDifferentChronologiesAndSameInstant() {
 DateTimeZone dtZone1 = DateTimeZone.forID("America/New_York");
 DateTime start1 = new DateTime(2022, 5, 1, 12, 30, 0, 0, dtZone1);
 DateTime end1 = new DateTime(2022, 5, 1, 12, 30, 0, 0, dtZone1);
 DateTimeZone dtZone2 = DateTimeZone.forID("Europe/London");
 DateTime start2 = new DateTime(2022, 5, 1, 12, 30, 0, 0, dtZone2);
 DateTime end2 = new DateTime(2022, 5, 1, 12, 30, 0, 0, dtZone2);

 int result = BaseSingleFieldPeriod.between(start1, end1, DurationFieldType.DAYS);
 int expected = 0;
 assertEquals(expected, result);

 int result2 = BaseSingleFieldPeriod.between(start2, end2, DurationFieldType.DAYS);
 int expected2 = 0;
 assertEquals(expected2, result2);
 }

 @Test
 public void testBetweenMethodWithDifferentChronologiesAndDifferentInstant() {
 DateTimeZone dtZone1 = DateTimeZone.forID("America/New_York");
 DateTime start1 = new DateTime(2022, 5, 1, 12, 30, 0, 0, dtZone1);
 DateTime end1 = new DateTime(2022, 5, 2, 12, 30, 0, 0, dtZone1);
 DateTimeZone dtZone2 = DateTimeZone.forID("Europe/London");
 DateTime start2 = new DateTime(2022, 5, 1, 11, 30, 0, 0, dtZone2);
 DateTime end2 = new DateTime(2022, 5, 2, 13, 30, 0, 0, dtZone2);

 int result = BaseSingleFieldPeriod.between(start1, end1, DurationFieldType.DAYS);
 int expected = 1;
 assertEquals(expected, result);

 int result2 = BaseSingleFieldPeriod.between(start2, end2, DurationFieldType.DAYS);
 int expected2 = 2;
 assertEquals(expected2, result2);
 }

 private void testBetweenMethod(DateTime start, DateTime end, DurationFieldType fieldType) {
 int result = BaseSingleFieldPeriod.between(start, end, fieldType);
 int expected = Period.between(start, end).getValues(fieldType)[0];
 assertEquals(expected, result);
 }

 @Test
 public void testStandardPeriodInMethodWithNullReadablePeriod() {
 try {
 BaseSingleFieldPeriod.standardPeriodIn(null, 5);
 Fail.fail("Expected exception: IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInMethodWithInvalidReadablePeriod() {
 ISOChronology chronology = ISOChronology.getInstanceUTC();
 Period period = Period.parse("P1Y2M3D4S");
 try {
 BaseSingleFieldPeriod.standardPeriodIn(period, 0);
 Fail.fail("Expected exception: IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testStandardPeriodInMethodWithValidReadablePeriodAndFactor() {
 ISOChronology chronology = ISOChronology.getInstanceUTC();
 Period period = Period.parse("P1D");
 int factor = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, factor);
 int expected = 2;
 assertEquals(expected, result);
 }

 @Test
 public void testStandardPeriodInMethodWithValidReadablePeriodAndFactorGreaterThanOne() {
 ISOChronology chronology = ISOChronology.getInstanceUTC();
 Period period = Period.parse("P1D");
 int factor = 3;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, factor);
 int expected = 3;
 assertEquals(expected, result);
 }

 @Test
 public void testStandardPeriodInMethodWithValidReadablePeriodAndFactorZero() {
 ISOChronology chronology = ISOChronology.getInstanceUTC();
 Period period = Period.parse("P1D");
 int factor = 0;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, factor);
 int expected = 0;
 assertEquals(expected, result);
 }

 @Test
 public void testStandardPeriodInMethodWithValidReadablePeriodAndFactorNegative() {
 ISOChronology chronology = ISOChronology.getInstanceUTC();
 Period period = Period.parse("P1D");
 int factor = -1;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, factor);
 int expected = -1;
 assertEquals(expected, result);
 }
}
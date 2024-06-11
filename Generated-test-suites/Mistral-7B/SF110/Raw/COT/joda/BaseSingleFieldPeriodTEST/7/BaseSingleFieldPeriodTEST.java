package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod testObject;

 @Before
 public void setUp() {
 testObject = new MySingleFieldPeriod(5);
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType expected = new DayOfMonthField();
 DurationFieldType actual = testObject.getFieldType();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetPeriodType() {
 PeriodType expected = PeriodType.INSTANCE;
 PeriodType actual = testObject.getPeriodType();
 assertEquals(expected, actual);
 }

 @Test
 public void testSize() {
 int expected = 1;
 int actual = testObject.size();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetFieldTypeAtIndex() {
 DurationFieldType expected = new DayOfMonthField();
 DurationFieldType actual = testObject.getFieldType(0);
 assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeAtInvalidIndex() {
 testObject.getFieldType(1);
 }

 @Test
 public void testGetValue() {
 int expected = 5;
 int actual = testObject.getValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetValue() {
 int newValue = 10;
 testObject.setValue(newValue);
 int actual = testObject.getValue();
 assertEquals(newValue, actual);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod another = new MySingleFieldPeriod(5);
 assertTrue(testObject.equals(another));
 }

 @Test
 public void testNotEquals() {
 BaseSingleFieldPeriod another = new MySingleFieldPeriod(6);
 assertFalse(testObject.equals(another));
 }

 @Test
 public void testHashCode() {
 int firstHashCode = testObject.hashCode();
 testObject.setValue(6);
 int secondHashCode = testObject.hashCode();
 assertNotEquals(firstHashCode, secondHashCode);
 }

 @Test
 public void testCompareToZero() {
 BaseSingleFieldPeriod another = new MySingleFieldPeriod(0);
 int result = testObject.compareTo(another);
 assertTrue(result > 0);
 }

 @Test
 public void testCompareToSame() {
 BaseSingleFieldPeriod another = new MySingleFieldPeriod(5);
 int result = testObject.compareTo(another);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToNegative() {
 BaseSingleFieldPeriod another = new MySingleFieldPeriod(1);
 int result = testObject.compareTo(another);
 assertTrue(result < 0);
 }

 @Test
 public void testBetween() {
 Instant start = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
 Instant end = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
 ReadableInstant readableStart = new InstantMillis(start.getMillis());
 ReadableInstant readableEnd = new InstantMillis(end.getMillis());
 DurationFieldType durationFieldType = new DaysField();
 int result = BaseSingleFieldPeriod.between(readableStart, readableEnd, durationFieldType);
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadableInstant() {
 DurationFieldType durationFieldType = new DaysField();
 BaseSingleFieldPeriod.between(null, new InstantMillis(System.currentTimeMillis()), durationFieldType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadableInstant2() {
 DurationFieldType durationFieldType = new DaysField();
 BaseSingleFieldPeriod.between(new InstantMillis(System.currentTimeMillis()), null, durationFieldType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenDifferentChronology() {
 DateTimeZone chronology1 = DateTimeZone.forID("America/Los_Angeles");
 DateTimeZone chronology2 = DateTimeZone.forID("Europe/London");
 ReadableInstant readableStart = new InstantMillis(System.currentTimeMillis());
 ReadableInstant readableEnd = new InstantMillis(System.currentTimeMillis());
 BaseSingleFieldPeriod.between(readableStart, readableEnd, new DaysField());
 // Set chronology for readableStart
 readableStart = new InstantMillis(System.currentTimeMillis());
 readableStart = new DateTime(2022, 1, 1, 0, 0, 0, 0, chronology1).toInstant().toReadableInstant();
 // Set chronology for readableEnd
 readableEnd = new InstantMillis(System.currentTimeMillis());
 readableEnd = new DateTime(2022, 1, 1, 0, 0, 0, 0, chronology2).toInstant().toReadableInstant();
 BaseSingleFieldPeriod.between(readableStart, readableEnd, new DaysField());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenDifferentFieldTypes() {
 ReadableInstant readableStart = new InstantMillis(System.currentTimeMillis());
 ReadableInstant readableEnd = new InstantMillis(System.currentTimeMillis());
 DurationFieldType durationFieldType1 = new DaysField();
 DurationFieldType durationFieldType2 = new HoursField();
 BaseSingleFieldPeriod.between(readableStart, readableEnd, durationFieldType1);
 BaseSingleFieldPeriod.between(readableStart, readableEnd, durationFieldType2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNonContiguous() {
 ReadablePartial readablePartial = new GeneralBaseDateTime(System.currentTimeMillis(), DateTimeZone.UTC).toReadablePartial();
 readablePartial = readablePartial.withFieldAdded(new YearField(2022), 1);
 readablePartial = readablePartial.withFieldAdded(new MonthField(2, DateTimeFieldType.monthOfYear()), 1);
 readablePartial = readablePartial.withFieldAdded(new DayOfMonthField(), 15);
 ReadablePartial readablePartial2 = new GeneralBaseDateTime(System.currentTimeMillis(), DateTimeZone.UTC).toReadablePartial();
 readablePartial2 = readablePartial2.withFieldAdded(new MonthField(2, DateTimeFieldType.monthOfYear()), 2);
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, new DaysField());
 }

 @Test
 public void testStandardPeriodIn() {
 ReadablePeriod readablePeriod = new Period(1, new DaysField(), 2);
 long n = 3;
 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 assertEquals(1, result);
 }

 @Test
 public void testStandardPeriodInNullReadablePeriod() {
 int result = BaseSingleFieldPeriod.standardPeriodIn(null, 1);
 assertEquals(0, result);
 }

 @Test
 public void testStandardPeriodInZeroValue() {
 ReadablePeriod readablePeriod = new Period(0, new DaysField(), 0);
 long n = 1;
 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 assertEquals(0, result);
 }

 @Test
 public void testStandardPeriodInInvalidN() {
 ReadablePeriod readablePeriod = new Period(1, new DaysField(), 2);
 long n = 0;
 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 assertEquals(0, result);
 }
}

 class MySingleFieldPeriod extends BaseSingleFieldPeriod {
 MySingleFieldPeriod(int iPeriod) {
 super(iPeriod);
 }
 }
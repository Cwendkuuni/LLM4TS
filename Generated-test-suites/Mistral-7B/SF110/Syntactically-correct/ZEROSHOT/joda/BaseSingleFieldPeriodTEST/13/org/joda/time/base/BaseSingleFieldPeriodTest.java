package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.period.*;

import org.junit.*;

public class BaseSingleFieldPeriodTest {

 @BeforeClass
 public static void setupClass() {
 // Set up any static variables or initialize any static objects here
 }

 @AfterClass
 public static void tearDownClass() {
 // Teardown any static variables or objects here
 }

 @Before
 public void setup() {
 // Set up any instance variables or objects here
 }

 @After
 public void tearDown() {
 // Teardown any instance variables or objects here
 }

 @Test
 public void testBetween() {
 // Test the between method with valid arguments
 int hoursBetween = BaseSingleFieldPeriod.between(new Instant(0L * 1000), new Instant(1000L * 60 * 60), DurationFieldType.hours());
 assertEquals(1, hoursBetween);

 // Test the between method with null arguments
 try {
 BaseSingleFieldPeriod.between(null, new Instant(1000L * 60 * 60), DurationFieldType.hours());
 fail("Expected IllegalArgumentException with null ReadableInstant argument");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(new Instant(1000L * 60 * 60), null, DurationFieldType.hours());
 fail("Expected IllegalArgumentException with null ReadableInstant argument");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(new Instant(1000L * 60 * 60), new Instant(1000L * 60 * 60), null);
 fail("Expected IllegalArgumentException with null DurationFieldType argument");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBetweenPartial() {
 // Test the between method with valid arguments
 LocalDate date1 = new LocalDate(2000, 1, 1);
 LocalDate date2 = new LocalDate(2000, 1, 2);
 int daysBetween = BaseSingleFieldPeriod.between(new LocalDatePartial(date1, new LocalDateField[] { LocalDateField.dayOfMonth() }), new LocalDatePartial(date2, new LocalDateField[] { LocalDateField.dayOfMonth() }), Period.days());
 assertEquals(1, daysBetween);

 // Test the between method with null arguments
 try {
 BaseSingleFieldPeriod.between(null, new LocalDatePartial(date1, new LocalDateField[] { LocalDateField.dayOfMonth() }), Period.days());
 fail("Expected IllegalArgumentException with null ReadablePartial argument");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(new LocalDatePartial(date1, new LocalDateField[] { LocalDateField.dayOfMonth() }), null, Period.days());
 fail("Expected IllegalArgumentException with null ReadablePartial argument");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(new LocalDatePartial(date1, new LocalDateField[] { LocalDateField.dayOfMonth() }), new LocalDatePartial(date2, new LocalDateField[] { LocalDateField.monthOfYear() }), Period.days());
 fail("Expected IllegalArgumentException with different number of fields in the ReadablePeriodals");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(new LocalDatePartial(date1, new LocalDateField[] { LocalDateField.dayOfMonth() }), new LocalDatePartial(date2, new LocalDateField[] { LocalDateField.dayOfMonth() }), null);
 fail("Expected IllegalArgumentException with null Period argument");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testStandardPeriodIn() {
 // Test the standardPeriodIn method with valid arguments
 Period period = new Period(1, PeriodUnit.DAYS, 1, PeriodUnit.HOURS, 1, PeriodUnit.MINUTES, 1, PeriodUnit.SECONDS);
 long n = 5;
 int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(period, n);
 assertEquals(25200, standardPeriodIn); // 1 day * 24 hours * 60 minutes * 60 seconds * n
 }

 @Test
 public void testConstructor() {
 // Test the constructor with valid arguments
 DurationFieldType fieldType = DurationFieldType.hours();
 int period = 3;
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(period * fieldType.getLength());
 assertEquals(period, periodObject.getValue());
 assertEquals(fieldType, periodObject.getFieldType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidArguments() {
 try {
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod("InvalidArgument");
 fail("Expected IllegalArgumentException with invalid constructor argument");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(123);
 assertEquals(DurationFieldType.hours(), periodObject.getFieldType());
 }

 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(123);
 assertEquals(PeriodType.fixed(), periodObject.getPeriodType());
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(123);
 assertEquals(1, periodObject.size());
 }

 @Test
 public void testGetFieldTypeAt() {
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(123);
 assertEquals(DurationFieldType.hours(), periodObject.getFieldType(0));
 }

 @Test
 public void testGetValueAt() {
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(123);
 assertEquals(123, periodObject.getValue(0));
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(123);
 assertEquals(123, periodObject.getValue());
 }

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(123);
 periodObject.setValue(456);
 assertEquals(456, periodObject.getValue());
 }

 @Test
 public void testToPeriod() {
 Period period = new Period(1, PeriodUnit.DAYS, 1, PeriodUnit.HOURS, 1, PeriodUnit.MINUTES, 1, PeriodUnit.SECONDS);
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(period.getStandardValues()[0]);
 Period result = periodObject.toPeriod();
 assertEquals(period, result);
 }

 @Test
 public void testToMutablePeriod() {
 Period period = new Period(1, PeriodUnit.DAYS, 1, PeriodUnit.HOURS, 1, PeriodUnit.MINUTES, 1, PeriodUnit.SECONDS);
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(period.getStandardValues()[0]);
 MutablePeriod mutablePeriod = periodObject.toMutablePeriod();
 assertEquals(period, mutablePeriod.toPeriod());
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);
 assertTrue(period1.equals(period2));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);
 int hashCode1 = period1.hashCode();
 int hashCode2 = period2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testCompareTo() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(456);
 int comparisonResult = period1.compareTo(period2);
 assertEquals(-1, comparisonResult);

 BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(123);
 BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(123);
 int comparisonResult2 = period3.compareTo(period4);
 assertEquals(0, comparisonResult2);
 }

 @Test
 public void testIsSupported() {
 BaseSingleFieldPeriod periodObject = new BaseSingleFieldPeriod(123);
 assertTrue(periodObject.isSupported(DurationFieldType.hours()));
 }
}
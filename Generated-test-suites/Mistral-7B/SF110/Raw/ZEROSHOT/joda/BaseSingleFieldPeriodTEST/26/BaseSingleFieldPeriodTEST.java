package org.joda.time.base;

import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createPeriod(int periodValue, DurationFieldType fieldType) {
 return new TestSingleFieldPeriod(periodValue, fieldType);
 }

 @Test
 public void testGetFieldType() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 assertEquals(DurationFieldType.DAYS, period.getFieldType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetFieldTypeThrowsUOE() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 try {
 period.getFieldType(1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 period.getFieldType(1);
 }

 @Test
 public void testGetPeriodType() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 assertEquals(PeriodType.INSTANCE, period.getPeriodType());
 }

 @Test
 public void testSize() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 assertEquals(1, period.size());
 }

 @Test
 public void testValue() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 assertEquals(5, period.getValue());
 }

 @Test
 public void testValueSetter() {
 TestSingleFieldPeriod period = new TestSingleFieldPeriod(10, DurationFieldType.DAYS);
 period.setValue(7);
 assertEquals(7, period.getValue());
 }

 @Test
 public void testGetValueThrowsIobIfIndexIsNotZero() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 try {
 period.getValue(1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testGetValueThrowsIobIfNegativeIndex() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 try {
 period.getValue(-1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIsSupported() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 assertTrue(period.isSupported(DurationFieldType.DAYS));
 assertFalse(period.isSupported(DurationFieldType.HOURS));
 }

 @Test
 public void testEquals() {
 TestSingleFieldPeriod period1 = createPeriod(5, DurationFieldType.DAYS);
 TestSingleFieldPeriod period2 = createPeriod(5, DurationFieldType.DAYS);
 TestSingleFieldPeriod period3 = createPeriod(6, DurationFieldType.DAYS);
 TestSingleFieldPeriod period4 = createPeriod(5, DurationFieldType.DAYS);

 assertEquals(period1, period1);
 assertEquals(period1, period2);
 assertNotSame(period1, period3);
 assertNotSame(period1, period4);

 assertNotEquals(period1, null);
 assertNotEquals(period1, new Object());
 }

 @Test
 public void testHashCode() {
 TestSingleFieldPeriod period1 = createPeriod(5, DurationFieldType.DAYS);
 TestSingleFieldPeriod period2 = createPeriod(5, DurationFieldType.DAYS);
 TestSingleFieldPeriod period3 = createPeriod(6, DurationFieldType.DAYS);
 TestSingleFieldPeriod period4 = createPeriod(5, DurationFieldType.DAYS);

 int hashCode1 = period1.hashCode();
 int hashCode2 = period2.hashCode();
 int hashCode3 = period3.hashCode();
 int hashCode4 = period4.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode4);
 }

 @Test
 public void testCompareTo() {
 TestSingleFieldPeriod period1 = createPeriod(5, DurationFieldType.DAYS);
 TestSingleFieldPeriod period2 = createPeriod(3, DurationFieldType.DAYS);
 TestSingleFieldPeriod period3 = new TestSingleFieldPeriod(5, DurationFieldType.DAYS);
 TestSingleFieldPeriod period4 = new TestSingleFieldPeriod(-3, DurationFieldType.DAYS);

 assertEquals(0, period1.compareTo(period1));
 assertEquals(-1, period1.compareTo(period2));
 assertEquals(0, period1.compareTo(period3));
 assertEquals(1, period3.compareTo(period1));
 assertEquals(-1, period1.compareTo(period4));
 }

 @Test
 public void testCompareToThrowsCceIfComparingDifferentClasses() {
 TestSingleFieldPeriod period1 = createPeriod(5, DurationFieldType.DAYS);
 BasePeriod period2 = new TestMultiFieldPeriod(new int[] { 5, 2 }, DurationFieldType.DAYS, DurationFieldType.HOURS);
 try {
 period1.compareTo(period2);
 } catch (ClassCastException e) {
 // expected
 }
 }

 @Test
 public void testToPeriod() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 Period expected = new Period(5, DurationFieldType.DAYS);
 Period actual = period.toPeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriod() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 MutablePeriod expected = new MutablePeriod().setFields(new StandardBasePeriod(5, DurationFieldType.DAYS));
 MutablePeriod actual = period.toMutablePeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToPeriodWithNegativeValue() {
 TestSingleFieldPeriod period = createPeriod(-5, DurationFieldType.DAYS);
 Period expected = new Period(-5, DurationFieldType.DAYS);
 Period actual = period.toPeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriodWithNegativeValue() {
 TestSingleFieldPeriod period = createPeriod(-5, DurationFieldType.DAYS);
 MutablePeriod expected = new MutablePeriod().setNegative(true).setFields(new StandardBasePeriod(-5, DurationFieldType.DAYS));
 MutablePeriod actual = period.toMutablePeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testBetween() {
 TestSingleFieldPeriod period1 = createPeriod(5, DurationFieldType.DAYS);
 LocalDate date1 = new LocalDate(2022, 1, 1);
 LocalDate date2 = new LocalDate(2022, 1, 6);
 int expected = 4;
 int actual = BaseSingleFieldPeriod.between(date1, date2, DurationFieldType.DAYS);
 assertEquals(expected, actual);
 }

 @Test
 public void testBetweenThrowsIaeIfFirstDateIsNull() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 try {
 BaseSingleFieldPeriod.between(null, new LocalDate(2022, 1, 1), DurationFieldType.DAYS);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBetweenThrowsIaeIfSecondDateIsNull() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 try {
 BaseSingleFieldPeriod.between(new LocalDate(2022, 1, 1), null, DurationFieldType.DAYS);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBetweenThrowsIaeIfDurationFieldTypeIsInvalid() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 try {
 BaseSingleFieldPeriod.between(new LocalDate(2022, 1, 1), new LocalDate(2022, 1, 1), DurationFieldType.MONTHS);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testStandardPeriodIn() {
 TestSingleFieldPeriod period = createPeriod(5, DurationFieldType.DAYS);
 long n = 10;
 int expected = 500;
 int actual = BaseSingleFieldPeriod.standardPeriodIn(period, n);
 assertEquals(expected, actual);
 }
}

 class TestSingleFieldPeriod extends BaseSingleFieldPeriod {

 public TestSingleFieldPeriod(int periodValue, DurationFieldType fieldType) {
 super(periodValue);
 this.setFieldType(fieldType);
 }
 }
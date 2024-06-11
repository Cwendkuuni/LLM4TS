package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod period1;
 private BaseSingleFieldPeriod period2;

 @Test
 public void testBetweenMethod() {
 period1 = new Period(2, DurationFieldType.DAYS);
 period2 = new Period(1, DurationFieldType.DAYS);

 int result = BaseSingleFieldPeriod.between(DateTimeUtils.currentTime(), DateTimeUtils.currentTime().plus(period1), DurationFieldType.DAYS);
 int expected = 2;
 assertEquals(expected, result);

 result = BaseSingleFieldPeriod.between(DateTimeUtils.currentTime(), DateTimeUtils.currentTime().plus(period2), DurationFieldType.DAYS);
 expected = 1;
 assertEquals(expected, result);

 result = BaseSingleFieldPeriod.between(DateTimeUtils.currentTime().plus(period1), DateTimeUtils.currentTime(), DurationFieldType.DAYS);
 expected = -2;
 assertEquals(expected, result);

 result = BaseSingleFieldPeriod.between(DateTimeUtils.currentTime(), null, DurationFieldType.DAYS);
 assertNull(result);

 result = BaseSingleFieldPeriod.between(null, DateTimeUtils.currentTime(), DurationFieldType.DAYS);
 assertNull(result);
 }

 @Test
 public void testBetweenMethodWithPartial() {
 LocalDate date1 = new LocalDate();
 LocalDate date2 = date1.plusDays(2);
 LocalDatePartial datePartial1 = new LocalDatePartial(new LocalDate(), new LocalDateFieldSet(LocalDateFieldSet.allFields()));
 LocalDatePartial datePartial2 = new LocalDay(2);

 int result = BaseSingleFieldPeriod.between(date1, date2, new LocalDay());
 int expected = 2;
 assertEquals(expected, result);

 result = BaseSingleFieldPeriod.between(datePartial1, datePartial2, new LocalDate());
 expected = 2;
 assertEquals(expected, result);

 result = BaseSingleFieldPeriod.between(datePartial2, datePartial1, new LocalDate());
 expected = -2;
 assertEquals(expected, result);

 result = BaseSingleFieldPeriod.between(datePartial1, null, new LocalDate());
 assertNull(result);

 result = BaseSingleFieldPeriod.between(null, datePartial1, new LocalDate());
 assertNull(result);
 }

 @Test
 public void testStandardPeriodInMethod() {
 Period period = new Period(3, DurationFieldType.DAYS);
 long n = 2;

 int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
 int expected = 1.5 * 3; // Assuming n is days
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInMethodWithNullPeriod() {
 int result = BaseSingleFieldPeriod.standardPeriodIn(null, 1);
 fail("Expected an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInMethodWithZeroPeriod() {
 Period period = Period.ZERO;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1);
 fail("Expected an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInMethodWithNonIntegerValue() {
 Period period = new Period(3.5, DurationFieldType.DAYS);
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1);
 fail("Expected an IllegalArgumentException");
 }

 @Test
 public void testGetFieldTypeMethod() {
 period1 = new Period(2, DurationFieldType.DAYS);
 assertSame(DurationFieldType.DAYS, period1.getFieldType());
 }

 @Test
 public void testGetPeriodTypeMethod() {
 period1 = new Period(2, DurationFieldType.DAYS);
 assertEquals(PeriodType.standardInstance(), period1.getPeriodType());
 }

 @Test
 public void testSizeMethod() {
 period1 = new Period(2, DurationFieldType.DAYS);
 assertEquals(1, period1.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeMethodWithInvalidIndex() {
 period1 = new Period(2, DurationFieldType.DAYS);
 try {
 period1.getFieldType(2);
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
}

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueMethodWithInvalidIndex() {
 period1 = new Period(2, DurationFieldType.DAYS);
 try {
 period1.getValue(2);
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueMethodWithNullPeriod() {
 int result = period1.getValue(null);
 fail("Expected an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueMethodWithInvalidFieldType() {
 DurationFieldType invalidFieldType = new DurationFieldType() {
 @Override public String getName() {
 return "InvalidFieldType";
 }
 };
 int result = period1.getValue(invalidFieldType);
 fail("Expected an IllegalArgumentException");
 }
}
package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

 private static final int YEARS = 3;
 private static final int MONTHS = 2;
 private static final int WEEKS = 5;
 private static final int DAYS = 7;
 private static final int HOURS = 12;
 private static final int MINUTES = 30;
 private static final int SECONDS = 45;
 private static final int MILLIS = 60;

 @Test
 public void testBaseSingleFieldPeriod_Initialization() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 assertEquals(YEARS, period.getValue());
 assertEquals(Years.INSTANCE, period.getFieldType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBaseSingleFieldPeriod_InvalidInitialization() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(-1);
 }

 @Test
 public void testBaseSingleFieldPeriod_GetValue() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 assertEquals(YEARS, period.getValue());
 }

 @Test
 public void testBaseSingleFieldPeriod_setValue() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 period.setValue(MONTHS);
 assertEquals(MONTHS, period.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBaseSingleFieldPeriod_GetValue_OutOfBounds() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 period.getValue(2);
 }

 @Test
 public void testBaseSingleFieldPeriod_GetFieldType() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 assertEquals(Years.INSTANCE, period.getFieldType());
 }

 @Test
 public void testBaseSingleFieldPeriod_GetPeriodType() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 assertEquals(PeriodType.INSTANCE, period.getPeriodType());
 }

 @Test
 public void testBaseSingleFieldPeriod_Size() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 assertEquals(1, period.size());
 }

 @Test
 public void testBaseSingleFieldPeriod_GetFieldType_OutOfBounds() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 assertEquals(Years.INSTANCE, period.getFieldType(0));
 assertNull(period.getFieldType(1));
 }

 @Test
 public void testBaseSingleFieldPeriod_getValue_OutOfBounds() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 assertEquals(YEARS, period.getValue(0));
 assertNull(period.getValue(1));
 }

 @Test
 public void testBaseSingleFieldPeriod_Get_SameObject() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = period1;
 assertEquals(YEARS, period2.getValue());
 }

 @Test
 public void testBaseSingleFieldPeriod_Get_DifferentObjects() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(MONTHS);
 assertNotEquals(YEARS, period2.getValue());
 }

 @Test
 public void testBaseSingleFieldPeriod_CompareTo_SameObject() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = period1;
 assertEquals(0, period2.compareTo(period1));
 }

 @Test
 public void testBaseSingleFieldPeriod_CompareTo_DifferentPeriods() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(MONTHS);
 assertEquals(-1, period2.compareTo(period1));
 }

 @Test
 public void testBaseSingleFieldPeriod_CompareTo_SameTypeButDifferentValue() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(YEARS + MONTHS);
 assertEquals(-1, period2.compareTo(period1));
 }

 @Test
 public void testBaseSingleFieldPeriod_CompareTo_DifferentTypes() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = new DaysPeriod(DAYS);
 assertNotEquals(0, period2.compareTo(period1));
 }

 @Test
 public void testBaseSingleFieldPeriod_Equals_SameObject() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = period1;
 assertTrue(period2.equals(period1));
 }

 @Test
 public void testBaseSingleFieldPeriod_Equals_DifferentObjectsButSameValueAndFieldType() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(MONTHS + YEARS);
 BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(YEARS);
 assertTrue(period3.equals(period1));
 assertTrue(period3.equals(period2));
 }

 @Test
 public void testBaseSingleFieldPeriod_Equals_DifferentObjectsButDifferentValueAndFieldType() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(MONTHS);
 assertFalse(period1.equals(period2));
 }

 @Test
 public void testBaseSingleFieldPeriod_HashCode_SameObjects() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = period1;
 assertEquals(period1.hashCode(), period2.hashCode());
 }

 @Test
 public void testBaseSingleFieldPeriod_HashCode_DifferentObjectsButSameValueAndFieldType() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(MONTHS + YEARS);
 BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(YEARS);
 assertEquals(period3.hashCode(), period1.hashCode());
 assertEquals(period3.hashCode(), period2.hashCode());
 }

 @Test
 public void testBaseSingleFieldPeriod_HashCode_DifferentObjectsButDifferentValueAndFieldType() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(YEARS);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(MONTHS);
 assertNotEquals(period1.hashCode(), period2.hashCode());
 }

 @Test
 public void testBaseSingleFieldPeriod_ToPeriod() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 Period periodResult = period.toPeriod();
 assertEquals(PeriodType.INSTANCE, periodResult.getPeriodType());
 assertEquals(YEARS, periodResult.getFields().get(0).getValue());
 assertEquals(Years.INSTANCE, periodResult.getFields().get(0).getFieldType());
 }

 @Test
 public void testBaseSingleFieldPeriod_ToMutablePeriod() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(YEARS);
 MutablePeriod mutablePeriod = period.toMutablePeriod();
 assertEquals(YEARS, mutablePeriod.getBasePeriod().getValue());
 assertEquals(PeriodType.INSTANCE, mutablePeriod.getBasePeriod().getPeriodType());
 assertEquals(1, mutablePeriod.size());
 assertSame(Years.INSTANCE, mutablePeriod.getBasePeriod().getFieldType());
 }

 @Test
 public void testBaseSingleFieldPeriod_Between() {
 ReadableInstant start = new DateTime(0, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 ReadableInstant end = new DateTime(1000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 BaseSingleFieldPeriod period = new YearsPeriod(YEARS);
 int result = BaseSingleFieldPeriod.between(start, end, Years.INSTANCE);
 assertEquals(YEARS, result);

 BaseSingleFieldPeriod period2 = new DaysPeriod(DAYS);
 int result2 = BaseSingleFieldPeriod.between(start, end, Days.INSTANCE);
 assertEquals(DAYS, result2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBaseSingleFieldPeriod_Between_NullArguments() {
 ReadableInstant start = null;
 ReadableInstant end = new DateTime(1000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 BaseSingleFieldPeriod period = new YearsPeriod(YEARS);
 BaseSingleFieldPeriod.between(start, end, Years.INSTANCE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBaseSingleFieldPeriod_Between_NullArguments2() {
 ReadableInstant start = new DateTime(0, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 ReadableInstant end = null;
 BaseSingleFieldPeriod period = new YearsPeriod(YEARS);
 BaseSingleFieldPeriod.between(start, end, Years.INSTANCE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBaseSingleFieldPeriod_Between_DifferentFieldTypes() {
 ReadableInstant start = new DateTime(0, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 ReadableInstant end = new DateTime(1000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 BaseSingleFieldPeriod period = new YearsPeriod(YEARS);
 BaseSingleFieldPeriod.between(start, end, Months.INSTANCE);
 }

 @Test
 public void testBaseSingleFieldPeriod_StandardPeriodIn() {
 Period period = new StandardDuration(new Millis(1000 * MILLIS));
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
 assertEquals(1, result);
 }

 @Test
 public void testBaseSingleFieldPeriod_StandardPeriodIn_ZeroPeriod() {
 Period period = Period.ZERO;
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
 assertEquals(0, result);
 }

 @Test
 public void testBaseSingleFieldPeriod_StandardPeriodIn_DifferentUnits() {
 Period period = new StandardDuration(new Millis(1000 * MILLIS));
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000 * 60);
 assertEquals(60, result);
 }

 @Test
 public void testBaseSingleFieldPeriod_StandardPeriodIn_NegativePeriod() {
 Period period = new StandardDuration(new Millis(-1000 * MILLIS));
 int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
 assertEquals(0, result);
 }
}
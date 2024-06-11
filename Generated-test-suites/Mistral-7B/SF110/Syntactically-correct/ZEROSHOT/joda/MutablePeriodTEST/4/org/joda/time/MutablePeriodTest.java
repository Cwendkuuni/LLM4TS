package org.joda.time.test;

import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodField;
import org.joda.time.PeriodType;
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

 private MutablePeriod createMutablePeriod() {
 return new MutablePeriod();
 }

 @Test
 public void testParseString() {
 MutablePeriod period = MutablePeriod.parse("P3Y2M1D1H1M1S1");
 assertEquals(3, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(1, period.getWeeks());
 assertEquals(1, period.getDays());
 assertEquals(1, period.getHours());
 assertEquals(1, period.getMinutes());
 assertEquals(1, period.getSeconds());
 assertEquals(1, period.getMillis());
 }

 @Test
 public void testParseStringWithFormatter() {
 PeriodFormatter formatter = ISOPeriodFormat.standard();
 MutablePeriod period = MutablePeriod.parse("P3Y2M1D1H1M1S1", formatter);
 assertEquals(3, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(1, period.getWeeks());
 assertEquals(1, period.getDays());
 assertEquals(1, period.getHours());
 assertEquals(1, period.getMinutes());
 assertEquals(1, period.getSeconds());
 assertEquals(1, period.getMillis());
 }

 @Test
 public void testEmptyConstructor() {
 MutablePeriod period = new MutablePeriod();
 assertEquals(0, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testPeriodTypeConstructor() {
 MutablePeriod period = new MutablePeriod(PeriodType.standard());
 assertEquals(0, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testIntConstructors() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());

 period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testLongConstructors() {
 MutablePeriod period = new MutablePeriod(123456789L);
 assertEquals(1, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(2, period.getHours());
 assertEquals(34, period.getMinutes());
 assertEquals(56, period.getSeconds());
 assertEquals(789, period.getMillis());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 MutablePeriod original = createMutablePeriod();
 MutablePeriod cloned = original.copy();
 assertEquals(original, cloned);
 }

 @Test
 public void testSetValue() {
 MutablePeriod period = createMutablePeriod();
 period.setValue(0, 1);
 assertEquals(1, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testSetField() {
 MutablePeriod period = createMutablePeriod();
 period.setField(DurationFieldType.years(), 1);
 assertEquals(1, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testSetPeriod() {
 MutablePeriod period = createMutablePeriod();
 period.setPeriod(new MutablePeriod(1, 2, 3, 4));
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testSetPeriodWithMillis() {
 MutablePeriod period = createMutablePeriod();
 period.setPeriod(1, 2, 3, 4, 5, 6, 7, 8, 9);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetPeriodWithChronology() {
 MutablePeriod period = createMutablePeriod();
 period.setPeriod(1, 2, 3, 4, 5, 6, 7, 8, null, DateTimeUtils.UTC);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetPeriodWithReadablePeriod() {
 MutablePeriod period = createMutablePeriod();
 period.setPeriod(new MutablePeriod(1, 2, 3, 4));
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testSetPeriodWithReadableInterval() {
 MutablePeriod period = createMutablePeriod();
 period.setPeriod(new Period(1, 2, 3, 4, 5, 6, 7, 8));
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetPeriodWithReadableDuration() {
 MutablePeriod period = createMutablePeriod();
 period.setPeriod(Duration.standardMillis(123456789));
 assertEquals(1, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(2, period.getHours());
 assertEquals(34, period.getMinutes());
 assertEquals(56, period.getSeconds());
 assertEquals(789, period.getMillis());
 }

 @Test
 public void testAddPeriod() {
 MutablePeriod period = createMutablePeriod();
 MutablePeriod addend = new MutablePeriod(1, 2, 3, 4);
 period.addPeriod(addend);
 assertEquals(2, period.getYears());
 assertEquals(4, period.getMonths());
 assertEquals(4, period.getWeeks());
 assertEquals(7, period.getDays());
 assertEquals(3, period.getHours());
 assertEquals(4, period.getMinutes());
 assertEquals(4, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testAddPeriodWithMillis() {
 MutablePeriod period = createMutablePeriod();
 Period addend = new Period(1, 2, 3, 4, 5, 6, 7, 8, 9);
 period.addPeriod(addend);
 assertEquals(2, period.getYears());
 assertEquals(4, period.getMonths());
 assertEquals(4, period.getWeeks());
 assertEquals(7, period.getDays());
 assertEquals(13, period.getHours());
 assertEquals(41, period.getMinutes());
 assertEquals(52, period.getSeconds());
 assertEquals(11, period.getMillis());
 }

 @Test
 public void testAddPeriodWithChronology() {
 MutablePeriod period = createMutablePeriod();
 Period addend = new Period(1, 2, 3, 4, 5, 6, 7, 8, null, DateTimeUtils.UTC);
 period.addPeriod(addend);
 assertEquals(2, period.getYears());
 assertEquals(4, period.getMonths());
 assertEquals(4, period.getWeeks());
 assertEquals(7, period.getDays());
 assertEquals(13, period.getHours());
 assertEquals(41, period.getMinutes());
 assertEquals(52, period.getSeconds());
 assertEquals(11, period.getMillis());
 }

 @Test
 public void testAddPeriodWithReadablePeriod() {
 MutablePeriod period = createMutablePeriod();
 MutablePeriod addend = new MutablePeriod(1, 2, 3, 4);
 period.addPeriod(addend);
 assertEquals(2, period.getYears());
 assertEquals(4, period.getMonths());
 assertEquals(4, period.getWeeks());
 assertEquals(7, period.getDays());
 assertEquals(3, period.getHours());
 assertEquals(4, period.getMinutes());
 assertEquals(4, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testAddPeriodWithReadableInterval() {
 MutablePeriod period = createMutablePeriod();
 Period interval = new Period(1, 2, 3, 4, 5, 6, 7, 8, null, DateTimeUtils.UTC);
 period.addPeriod(interval);
 assertEquals(2, period.getYears());
 assertEquals(4, period.getMonths());
 assertEquals(4, period.getWeeks());
 assertEquals(7, period.getDays());
 assertEquals(13, period.getHours());
 assertEquals(41, period.getMinutes());
 assertEquals(52, period.getSeconds());
 assertEquals(11, period.getMillis());
 }

 @Test
 public void testAddPeriodWithReadableDuration() {
 MutablePeriod period = createMutablePeriod();
 Duration duration = Duration.standardMillis(123456789);
 period.addPeriod(duration);
 assertEquals(2, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(2, period.getHours());
 assertEquals(34, period.getMinutes());
 assertEquals(56, period.getSeconds());
 assertEquals(789, period.getMillis());
 }

 @Test
 public void testAddPeriodWithMillisAndChronology() {
 MutablePeriod period = createMutablePeriod();
 Period periodToAdd = new Period(1, 2, 3, 4, 5, 6, 7, 8, null, DateTimeUtils.UTC);
 period.addPeriod(periodToAdd.getMillis(), DateTimeUtils.UTC);
 assertEquals(2, period.getYears());
 assertEquals(4, period.getMonths());
 assertEquals(4, period.getWeeks());
 assertEquals(7, period.getDays());
 assertEquals(13, period.getHours());
 assertEquals(41, period.getMinutes());
 assertEquals(52, period.getSeconds());
 assertEquals(11, period.getMillis());
 }

 @Test
 public void testAddPeriodWithMillisAndReadableChronology() {
 MutablePeriod period = createMutablePeriod();
 Period periodToAdd = new Period(1, 2, 3, 4, 5, 6, 7, 8, null, DateTimeUtils.UTC);
 period.addPeriod(periodToAdd.toDuration().getMillis(), null);
 assertEquals(2, period.getYears());
 assertEquals(4, period.getMonths());
 assertEquals(4, period.getWeeks());
 assertEquals(7, period.getDays());
 assertEquals(13, period.getHours());
 assertEquals(41, period.getMinutes());
 assertEquals(52, period.getSeconds());
 assertEquals(11, period.getMillis());
 }

 @Test
 public void testClear() {
 MutablePeriod period = createMutablePeriod();
 period.clear();
 assertEquals(0, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testMergePeriod() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4);
 MutablePeriod period2 = new MutablePeriod(3, 4, 5, 6);
 period1.mergePeriod(period2);
 assertEquals(4, period1.getYears());
 assertEquals(6, period1.getMonths());
 assertEquals(8, period1.getWeeks());
 assertEquals(10, period1.getDays());
 assertEquals(8, period1.getHours());
 assertEquals(10, period1.getMinutes());
 assertEquals(6, period1.getSeconds());
 assertEquals(0, period1.getMillis());
 }
}
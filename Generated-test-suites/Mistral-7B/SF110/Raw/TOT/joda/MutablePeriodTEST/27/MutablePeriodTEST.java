package org.joda.time.test;

import org.joda.time.MutablePeriod;
import org.joda.time.PeriodFieldType;
import org.joda.time.PeriodType;
import org.joda.time.base.Defaults;
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

 @Test
 public void testParseStatic() {
 String input = "PT3H2M30S";
 MutablePeriod period = MutablePeriod.parse(input);
 assertEquals(3, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(2, period.getDays());
 assertEquals(30, period.getHours());
 assertEquals(2, period.getMinutes());
 assertEquals(30, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testParseWithCustomFormatter() {
 PeriodFormatter formatter = ISOPeriodFormat.standard().withParsedMilliseconds();
 String input = "P3H2M30.3S";
 MutablePeriod period = MutablePeriod.parse(input, formatter);
 assertEquals(3, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(2, period.getDays());
 assertEquals(30, period.getHours());
 assertEquals(2, period.getMinutes());
 assertEquals(30, period.getSeconds());
 assertEquals(300, period.getMillis());
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
 public void testConstructorWithPeriodType() {
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
 public void testConstructorWithValues() {
 MutablePeriod period = new MutablePeriod(5, 11, 12, 13, 14, 15, 16, 17, PeriodType.standard());
 assertEquals(5, period.getYears());
 assertEquals(11, period.getMonths());
 assertEquals(12, period.getWeeks());
 assertEquals(13, period.getDays());
 assertEquals(14, period.getHours());
 assertEquals(15, period.getMinutes());
 assertEquals(16, period.getSeconds());
 assertEquals(17, period.getMillis());
 }

 @Test
 public void testConstructorWithLong() {
 MutablePeriod period = new MutablePeriod(123456789L);
 assertEquals(3, period.getYears());
 assertEquals(-4, period.getMonths());
 assertEquals(-11, period.getWeeks());
 assertEquals(-12, period.getDays());
 assertEquals(-13, period.getHours());
 assertEquals(-14, period.getMinutes());
 assertEquals(-15, period.getSeconds());
 assertEquals(-16, period.getMillis());
 }

 @Test
 public void testClone() {
 MutablePeriod original = new MutablePeriod(3, 2, 1, 0, 23, 59, 59, 999, PeriodType.standard());
 MutablePeriod cloned = (MutablePeriod) original.clone();
 assertEquals(original.getYears(), cloned.getYears());
 assertEquals(original.getMonths(), cloned.getMonths());
 assertEquals(original.getWeeks(), cloned.getWeeks());
 assertEquals(original.getDays(), cloned.getDays());
 assertEquals(original.getHours(), cloned.getHours());
 assertEquals(original.getMinutes(), cloned.getMinutes());
 assertEquals(original.getSeconds(), cloned.getSeconds());
 assertEquals(original.getMillis(), cloned.getMillis());
 }

 @Test
 public void testClear() {
 MutablePeriod period = new MutablePeriod();
 period.setYears(1);
 period.clear();
 assertEquals(0, period.getYears());
 }

 @Test
 public void testSetValue() {
 MutablePeriod period = new MutablePeriod();
 period.setValue(0, Defaults.INTEGER_VALUE);
 assertEquals(Defaults.INTEGER_VALUE, period.getYears());
 }

 @Test
 public void testSetField() {
 MutablePeriod period = new MutablePeriod();
 period.setField(PeriodFieldType.years(), 1);
 assertEquals(1, period.getYears());
 }

 @Test
 public void testSetPeriod() {
 MutablePeriod testPeriod = new MutablePeriod();
 testPeriod.setPeriod(new MutablePeriod(3, 2, 1, 0, 23, 59, 59, 999, PeriodType.standard()));
 assertEquals(3, testPeriod.getYears());
 assertEquals(2, testPeriod.getMonths());
 assertEquals(1, testPeriod.getWeeks());
 assertEquals(0, testPeriod.getDays());
 assertEquals(23, testPeriod.getHours());
 assertEquals(59, testPeriod.getMinutes());
 assertEquals(59, testPeriod.getSeconds());
 assertEquals(999, testPeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithStartMillis() {
 MutablePeriod testPeriod = new MutablePeriod();
 testPeriod.setPeriod(123456789L, null);
 assertEquals(2, testPeriod.getYears());
 assertEquals(-10, testPeriod.getMonths());
 assertEquals(-11, testPeriod.getWeeks());
 assertEquals(-12, testPeriod.getDays());
 assertEquals(-13, testPeriod.getHours());
 assertEquals(-14, testPeriod.getMinutes());
 assertEquals(-15, testPeriod.getSeconds());
 assertEquals(-16, testPeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithStartMillisAndChronology() {
 MutablePeriod testPeriod = new MutablePeriod();
 testPeriod.setPeriod(123456789L, Defaults.CHRONOLOGY);
 assertEquals(2, testPeriod.getYears());
 assertEquals(-10, testPeriod.getMonths());
 assertEquals(-11, testPeriod.getWeeks());
 assertEquals(-12, testPeriod.getDays());
 assertEquals(-13, testPeriod.getHours());
 assertEquals(-14, testPeriod.getMinutes());
 assertEquals(-15, testPeriod.getSeconds());
 assertEquals(-16, testPeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithReadableDuration() {
 MutablePeriod testPeriod = new MutablePeriod();
 testPeriod.setPeriod(new Duration(123456789L), null);
 assertEquals(2, testPeriod.getYears());
 assertEquals(-10, testPeriod.getMonths());
 assertEquals(-11, testPeriod.getWeeks());
 assertEquals(-12, testPeriod.getDays());
 assertEquals(-13, testPeriod.getHours());
 assertEquals(-14, testPeriod.getMinutes());
 assertEquals(-15, testPeriod.getSeconds());
 assertEquals(-16, testPeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithReadableDurationAndChronology() {
 MutablePeriod testPeriod = new MutablePeriod();
 testPeriod.setPeriod(new Duration(123456789L), Defaults.CHRONOLOGY);
 assertEquals(2, testPeriod.getYears());
 assertEquals(-10, testPeriod.getMonths());
 assertEquals(-11, testPeriod.getWeeks());
 assertEquals(-12, testPeriod.getDays());
 assertEquals(-13, testPeriod.getHours());
 assertEquals(-14, testPeriod.getMinutes());
 assertEquals(-15, testPeriod.getSeconds());
 assertEquals(-16, testPeriod.getMillis());
 }

 @Test
 public void testSetPeriodWithLongAndChronology() {
 MutablePeriod testPeriod = new MutablePeriod();
 testPeriod.setPeriod(123456789L, Defaults.CHRONOLOGY);
 assertEquals(2, testPeriod.getYears());
 assertEquals(-10, testPeriod.getMonths());
 assertEquals(-11, testPeriod.getWeeks());
 assertEquals(-12, testPeriod.getDays());
 assertEquals(-13, testPeriod.getHours());
 assertEquals(-14, testPeriod.getMinutes());
 assertEquals(-15, testPeriod.getSeconds());
 assertEquals(-16, testPeriod.getMillis());
 }

 @Test
 public void testAddField() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(new MutablePeriod(3, 2, 1, 0, 23, 59, 59, 999, PeriodType.standard()));
 period.addField(DurationFieldType.years(), 1);
 assertEquals(4, period.getYears());
 }

 @Test
 public void testAddPeriod() {
 MutablePeriod period1 = new MutablePeriod();
 period1.setPeriod(new MutablePeriod(3, 2, 1, 0, 23, 59, 59, 999, PeriodType.standard()));
 MutablePeriod period2 = new MutablePeriod();
 period2.setPeriod(new MutablePeriod(1, 0, 0, 0, 1, 0, 0, 0, PeriodType.standard()));
 period1.addPeriod(period2);
 assertEquals(4, period1.getYears());
 assertEquals(3, period1.getMonths());
 assertEquals(1, period1.getWeeks());
 assertEquals(1, period1.getDays());
 assertEquals(24, period1.getHours());
 assertEquals(60, period1.getMinutes());
 assertEquals(60, period1.getSeconds());
 assertEquals(999, period1.getMillis());
 }

 @Test
 public void testAddPeriodWithCustomChronology() {
 MutablePeriod period1 = new MutablePeriod();
 period1.setPeriod(new MutablePeriod(3, 2, 1, 0, 23, 59, 59, 999, PeriodType.standard()));
 MutablePeriod period2 = new MutablePeriod();
 period2.setPeriod(new MutablePeriod(1, 0, 0, 0, 1, 0, 0, 0, Defaults.CHRONOLOGY));
 period1.addPeriod(period2);
 assertEquals(4, period1.getYears());
 assertEquals(3, period1.getMonths());
 assertEquals(1, period1.getWeeks());
 assertEquals(1, period1.getDays());
 assertEquals(24, period1.getHours());
 assertEquals(60, period1.getMinutes());
 assertEquals(60, period1.getSeconds());
 assertEquals(999, period1.getMillis());
 }

 @Test
 public void testMergerPeriod() {
 MutablePeriod period1 = new MutablePeriod();
 period1.setPeriod(new MutablePeriod(3, 2, 1, 0, 23, 59, 59, 999, PeriodType.standard()));
 MutablePeriod period2 = new MutablePeriod();
 period2.setPeriod(new MutablePeriod(1, 0, 0, 0, 1, 0, 0, 0, PeriodType.standard()));
 period1.mergePeriod(period2);
 assertEquals(4, period1.getYears());
 assertEquals(3, period1.getMonths());
 assertEquals(1, period1.getWeeks());
 assertEquals(1, period1.getDays());
 assertEquals(24, period1.getHours());
 assertEquals(60, period1.getMinutes());
 assertEquals(60, period1.getSeconds());
 assertEquals(999, period1.getMillis());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidArguments() {
 new MutablePeriod(-1, 0, 0, 0, 23, 59, 59, 999, PeriodType.standard());
 }
}
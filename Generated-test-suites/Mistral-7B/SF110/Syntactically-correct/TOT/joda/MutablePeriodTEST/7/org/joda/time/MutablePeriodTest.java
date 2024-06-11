package org.joda.time.test;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.format.*;
import org.joda.time.field.*;
import org.junit.*;

import static org.junit.Assert.*;

public class MutablePeriodTest {

 @BeforeClass
 public static void setUp() {
 System.out.println("Running MutablePeriodTests...");
 }

 @AfterClass
 public static void tearDown() {
 System.out.println("MutablePeriodTests completed.");
 }

 @Test
 public void testParseString() {
 String dateString = "P3Y2M1DT2H"; // 3 years, 2 months, 1 day, 2 hours
 MutablePeriod period = MutablePeriod.parse(dateString);
 assertEquals("3 years, 2 months, 1 day, 2 hours", period.toString());
 }

 @Test
 public void testParseStringWithFormatter() {
 ISOPeriodFormat formatter = ISOPeriodFormat.standard();
 String dateString = "P3Y2M1DT2H";
 MutablePeriod period = MutablePeriod.parse(dateString, formatter);
 assertEquals("3 years, 2 months, 1 day, 2 hours", period.toString());
 }

 @Test
 public void testEmptyConstructor() {
 MutablePeriod period = new MutablePeriod();
 assertEquals("Duration: 0 days, 0 hours:0 minutes, 0 seconds.0 milliseconds", period.toString());
 }

 @Test
 public void testConstructorWithPeriodType() {
 MutablePeriod period = new MutablePeriod(PeriodType.standard());
 assertEquals("Duration: 0 days, 0 hours:0 minutes, 0 seconds.0 milliseconds", period.toString());
 }

 @Test
 public void testConstructorsWithValues() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
 assertEquals("Duration: 1 year, 2 months, 3 weeks, 4 days", period.toString());
 }

 @Test
 public void testConstructorsWithValuesAndPeriodType() {
 PeriodType periodType = PeriodType.standard();
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, periodType);
 assertEquals("Duration: 1 year, 2 months, 3 weeks, 4 days", period.toString());
 }

 @Test
 public void testConstructorsWithLong() {
 MutablePeriod period = new MutablePeriod(1234567890L);
 assertEquals("Duration: 3 years, 11 months, 2 weeks, 0 days, 1 hour, 9 minutes, 56 seconds.897 seconds", period.toString());
 }

 @Test
 public void testConstructorsWithLongAndPeriodType() {
 PeriodType periodType = PeriodType.standard();
 MutablePeriod period = new MutablePeriod(1234567890L, periodType);
 assertEquals("Duration: 3 years, 11 months, 2 weeks, 0 days, 1 hour, 9 minutes, 56 seconds.897 seconds", period.toString());
 }

 @Test
 public void testConstructorsWithReadableInstants() {
 DateTime baseDate = new DateTime("2018-01-01T00:00:00.000Z");
 DateTime endDate = new DateTime("2018-01-05T12:30:15.000Z");
 MutablePeriod period = new MutablePeriod(baseDate, endDate);
 assertEquals("Duration: 4 days, 12 hours:30 minutes.15 seconds", period.toString());
 }

 @Test
 public void testConstructorsWithReadableInstantAndReadableDuration() {
 DateTime baseDate = new DateTime("2018-01-01T00:00:00.000Z");
 ReadableDuration duration = Duration.standardHours(2);
 MutablePeriod period = new MutablePeriod(baseDate, duration);
 assertEquals("Duration: 2 hours", period.toString());
 }

 @Test
 public void testConstructorsWithReadableDurationAndReadableInstant() {
 ReadableDuration duration = Duration.standardHours(2);
 DateTime baseDate = new DateTime("2018-01-01T00:00:00.000Z");
 MutablePeriod period = new MutablePeriod(duration, baseDate);
 assertEquals("Duration: 2 hours", period.toString());
 }

 @Test
 public void testSetValue() {
 MutablePeriod period = new MutablePeriod();
 period.setValues(new int[6]);
 period.setValue(PeriodType.YEAR_INDEX, 3);
 period.setValue(PeriodType.MONTH_INDEX, 2);
 assertEquals("Duration: 3 years, 2 months", period.toString());
 }

 @Test
 public void testSetPeriod() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4);
 MutablePeriod period2 = new MutablePeriod(5, 6, 7, 8);
 period1.setPeriod(period2);
 assertEquals("Duration: 11 years, 8 months, 3 weeks, 4 days, 1 hour, 6 minutes, 7 seconds.0 milliseconds", period1.toString());
 }

 @Test
 public void testSetPeriodWithNull() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(null);
 assertEquals("Duration: 0 days, 0 hours:0 minutes, 0 seconds.0 milliseconds", period.toString());
 }

 @Test
 public void testSetPeriodWithInvalidArgumentType() {
 MutablePeriod period = new MutablePeriod();
 assertFalse("Invalid argument", period.setPeriod("Invalid string"));
 }

 @Test
 public void testAddField() {
 MutablePeriod period = new MutablePeriod();
 period.addField(DurationFieldType.years(), 3);
 period.addField(DurationFieldType.months(), 2);
 assertEquals("Duration: 3 years, 2 months", period.toString());
 }

 @Test
 public void testAddPeriod() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4);
 MutablePeriod period2 = new MutablePeriod(5, 6, 7, 8);
 period1.addPeriod(period2);
 assertEquals("Duration: 11 years, 8 months, 3 weeks, 4 days, 1 hour, 6 minutes, 7 seconds.0 milliseconds", period1.toString());
 }

 @Test
 public void testAddPeriodWithNull() {
 MutablePeriod period = new MutablePeriod();
 period.addPeriod(null);
 assertEquals("Duration: 0 days, 0 hours:0 minutes, 0 seconds.0 milliseconds", period.toString());
 }

 @Test
 public void testAddPeriodWithInvalidArgumentType() {
 MutablePeriod period = new MutablePeriod();
 assertFalse("Invalid argument", period.addPeriod("Invalid string"));
 }

 @Test
 public void testAdd() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4);
 MutablePeriod period2 = new MutablePeriod(5, 6, 7, 8);
 period1.add(period2);
 assertEquals("Duration: 11 years, 8 months, 3 weeks, 4 days, 1 hour, 6 minutes, 7 seconds.0 milliseconds", period1.toString());
 }

 @Test
 public void testAddWithLong() {
 MutablePeriod period = new MutablePeriod();
 long duration = 1234567890L;
 period.add(duration);
 assertEquals("Duration: 3 years, 11 months, 2 weeks, 0 days, 1 hour, 9 minutes, 56 seconds.897 seconds", period.toString());
 }

 @Test
 public void testAddWithLongAndChronology() {
 MutablePeriod period = new MutablePeriod();
 long duration = 1234567890L;
 Chronology chronology = DateTimeUtils.getUTC();
 period.add(duration, chronology);
 assertEquals("Duration: 3 years, 11 months, 2 weeks, 0 days, 1 hour, 9 minutes, 56 seconds.897 seconds", period.toString());
 }

 @Test
 public void testAddWithReadableDuration() {
 MutablePeriod period = new MutablePeriod();
 ReadableDuration duration = Duration.standardHours(2);
 period.add(duration);
 assertEquals("Duration: 2 hours", period.toString());
 }

 @Test
 public void testAddWithReadableDurationAndChronology() {
 MutablePeriod period = new MutablePeriod();
 ReadableDuration duration = Duration.standardHours(2);
 Chronology chronology = DateTimeUtils.getUTC();
 period.add(duration, chronology);
 assertEquals("Duration: 2 hours", period.toString());
 }

 @Test
 public void testMergePeriod() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4);
 MutablePeriod period2 = new MutablePeriod(5, 6, 7, 8);
 period1.mergePeriod(period2);
 assertEquals("Duration: 11 years, 8 months, 3 weeks, 4 days, 1 hour, 6 minutes, 7 seconds.0 milliseconds", period1.toString());
 }

 @Test
 public void testMergePeriodWithNull() {
 MutablePeriod period = new MutablePeriod();
 period.mergePeriod(null);
 assertEquals("Duration: 0 days, 0 hours:0 minutes, 0 seconds.0 milliseconds", period.toString());
 }

 @Test
 public void testMergePeriodWithInvalidArgumentType() {
 MutablePeriod period = new MutablePeriod();
 assertFalse("Invalid argument", period.mergePeriod("Invalid string"));
 }

 @Test
 public void testClear() {
 MutablePeriod period = new MutablePeriod();
 period.clear();
 assertEquals("Duration: 0 days, 0 hours:0 minutes, 0 seconds.0 milliseconds", period.toString());
 }

 @Test
 public void testSet() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(PeriodType.YEAR_INDEX, 3);
 period.setPeriod(PeriodType.MONTH_INDEX, 2);
 assertEquals("Duration: 3 years, 2 months", period.toString());
 }

 @Test
 public void testSetPeriodWithNull() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(PeriodType.YEAR_INDEX, null);
 assertEquals("Duration: 0 days, 0 hours:0 minutes, 0 seconds.0 milliseconds", period.toString());
 }

 @Test
 public void testSetPeriodWithInvalidArgumentType() {
 MutablePeriod period = new MutablePeriod();
 assertFalse("Invalid argument", period.setPeriod(PeriodType.YEAR_INDEX, "Invalid string"));
 }

 @Test
 public void testSetPeriodWithInvalidIndex() {
 MutablePeriod period = new MutablePeriod();
 assertFalse("Invalid index", period.setPeriod(10, 3));
 }

 @Test
 public void testSetPeriodWithInvalidValue() {
 MutablePeriod period = new MutablePeriod();
 assertFalse("Invalid value", period.setPeriod(PeriodType.YEAR_INDEX, -1));
 }

 @Test
 public void testSetPeriodWithInvalidValueAndChronology() {
 MutablePeriod period = new MutablePeriod();
 assertFalse("Invalid value and chronology", period.setPeriod(PeriodType.YEAR_INDEX, -1, DateTimeUtils.getUTC()));
 }

 @Test
 public void testSetPeriodWithChronology() {
 MutablePeriod period = new MutablePeriod();
 DateTime baseDate = new DateTime("2018-01-01T00:00:00.000Z");
 DateTime endDate = new DateTime("2018-01-05T12:30:15.000Z");
 period.setPeriod(baseDate, endDate);
 assertEquals("Duration: 4 days, 12 hours:30 minutes.15 seconds", period.toString());
 }

 @Test
 public void testSetPeriodWithChronologyAndInvalidArgumentType() {
 MutablePeriod period = new MutablePeriod();
 DateTime baseDate = new DateTime("2018-01-01T00:00:00.000Z");
 assertFalse("Invalid argument", period.setPeriod(baseDate, "Invalid string"));
 }

 @Test
 public void testSetPeriodWithChronologyAndInvalidPeriodType() {
 MutablePeriod period = new MutablePeriod();
 DateTime baseDate = new DateTime("2018-01-01T00:00:00.000Z");
 assertFalse("Invalid period type", period.setPeriod(baseDate, PeriodType.DAY_OF_WEEK));
 }

 @Test
 public void testClone() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
 MutablePeriod clonedPeriod = (MutablePeriod) period.clone();
 assertEquals("Duration: 1 year, 2 months, 3 weeks, 4 days", period.toString());
 assertEquals("Duration: 1 year, 2 months, 3 weeks, 4 days", clonedPeriod.toString());
 }

 @Test
 public void testCloneWithNull() {
 MutablePeriod period = new MutablePeriod();
 MutablePeriod clonedPeriod = (MutablePeriod) period.clone();
 assertEquals("Duration: 0 days, 0 hours:0 minutes, 0 seconds.0 milliseconds", period.toString());
 assertEquals("Duration: 0 days, 0 hours:0 minutes, 0 seconds.0 milliseconds", clonedPeriod.toString());
 }
}
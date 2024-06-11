package org.joda.time.base;

import org.joda.time.BaseDuration;
import org.joda.time.BaseLocalDateTime;
import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypelp;
import org.joda.time.LocalDateTime;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriod_Instants_SameChronology() {
 final LocalDateTime instant1 = new LocalDateTime(2012, 1, 1, 0, 0);
 final LocalDateTime instant2 = new LocalDateTime(2012, 1, 1, 1, 0);
 final BasePeriod period = new BasePeriod(instant1, instant2, PeriodType.standard());
 assertEquals("Period should have 1 hour", 1, period.getValue(DurationFieldType.hours()));
 }

 @Test
 public void testBasePeriod_Instants_DifferentChronology() {
 final LocalDateTime instant1 = new LocalDateTime(2012, 1, 1, 0, 0, ISOChronology.getInstanceUTC());
 final LocalDateTime instant2 = new LocalDateTime(2012, 1, 1, 1, 0, ISOChronology.getInstanceUTC().withOffsetAtUTC(-1, "UTC"));
 final BasePeriod period = new BasePeriod(instant1, instant2, PeriodType.standard());
 assertEquals("Period should have 1 hour", 1, period.getValue(DurationFieldType.hours()));
 }

 @Test
 public void testBasePeriod_Durations() {
 final Duration duration = new BaseDuration(1, DurationFieldType.days());
 final BasePeriod period = new BasePeriod(duration, PeriodType.standard());
 assertEquals("Period should have 1 day", 1, period.getValue(DurationFieldType.days()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriod_NullReadablePartial() {
 new BasePeriod(null, null, PeriodType.standard());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriod_DifferentReadablePartialTypes() {
 final BaseLocalDateTime dateTime1 = new LocalDateTime(2012, 1, 1, 0, 0);
 final BaseLocalDateTime dateTime2 = new LocalDateTime(2012, 1, 1, 0, 1);
 final BasePeriod period = new BasePeriod(dateTime1, dateTime2, PeriodType.standard());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriod_InvalidReadablePartialValues() {
 final BaseLocalDateTime dateTime1 = new LocalDateTime(2012, 1, 1, 0, 0);
 final BaseLocalDateTime dateTime2 = new LocalDateTime(2012, 13, 1, 0, 0);
 final BasePeriod period = new BasePeriod(dateTime1, dateTime2, PeriodType.standard());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriod_InvalidReadablePeriodType() {
 new BasePeriod(new LocalDateTime(2012, 1, 1, 0, 0), new LocalDateTime(2012, 1, 1, 0, 0), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriod_InvalidChronology() {
 new BasePeriod(new LocalDateTime(2012, 1, 1, 0, 0), new LocalDateTime(2012, 1, 1, 0, 0), new StringChronology());
 }

 @Test
 public void testBasePeriod_SetGetValues() {
 final BasePeriod period = new BasePeriod(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, PeriodType.standard());
 assertEquals("Period should have 1 year, 2 months, 3 weeks, 4 days, 5 hours, 6 minutes, 7 seconds, 8 milliseconds", 1, period.getValue(DurationFieldType.years()));
 assertEquals("Period should have 2 months", 2, period.getValue(DurationFieldType.months()));
 assertEquals("Period should have 3 weeks", 3, period.getValue(DurationFieldType.weeks()));
 assertEquals("Period should have 4 days", 4, period.getValue(DurationFieldType.days()));
 assertEquals("Period should have 5 hours", 5, period.getValue(DurationFieldType.hours()));
 assertEquals("Period should have 6 minutes", 6, period.getValue(DurationFieldType.minutes()));
 assertEquals("Period should have 7 seconds", 7, period.getValue(DurationFieldType.seconds()));
 assertEquals("Period should have 8 milliseconds", 8, period.getValue(DurationFieldType.millis()));
 }

 @Test
 public void testBasePeriod_SetField() {
 final BasePeriod period = new BasePeriod(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, PeriodType.standard());
 period.setField(DurationFieldType.days(), 10);
 assertEquals("Period should have 10 days after setting it", 10, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriod_AddField() {
 final BasePeriod period = new BasePeriod(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, PeriodType.standard());
 period.addField(DurationFieldType.days(), 10);
 assertEquals("Period should have 11 days after adding 10 days", 11, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriod_MergePeriod() {
 final BasePeriod period1 = new BasePeriod(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, PeriodType.standard());
 final BasePeriod period2 = new BasePeriod(new int[] { 2, 3, 4, 5, 6, 7, 8, 9 }, PeriodType.standard());
 period1.mergePeriod(period2);
 assertEquals("Period should have 3 years, 5 months, 7 weeks, 9 days after merging", 3, period1.getValue(DurationFieldType.years()));
 assertEquals("Period should have 5 months", 5, period1.getValue(DurationFieldType.months()));
 assertEquals("Period should have 7 weeks", 7, period1.getValue(DurationFieldType.weeks()));
 assertEquals("Period should have 9 days", 9, period1.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriod_AddPeriod() {
 final BasePeriod period1 = new BasePeriod(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, PeriodType.standard());
 final BasePeriod period2 = new BasePeriod(new int[] { 2, 3, 4, 5, 6, 7, 8, 9 }, PeriodType.standard());
 period1.addPeriod(period2);
 assertEquals("Period should have 3 years, 5 months, 7 weeks, 18 days after adding", 3, period1.getValue(DurationFieldType.years()));
 assertEquals("Period should have 5 months", 5, period1.getValue(DurationFieldType.months()));
 assertEquals("Period should have 7 weeks", 7, period1.getValue(DurationFieldType.weeks()));
 assertEquals("Period should have 18 days", 18, period1.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriod_ToDurationFrom() {
 final LocalDateTime instant = new LocalDateTime(2012, 1, 1, 0, 0);
 final BasePeriod period = new BasePeriod(instant, instante.plus(new Duration(1, DurationFieldType.days())), PeriodType.standard());
 final Duration duration = period.toDurationFrom(instant);
 assertEquals("Duration should have 1 day", 1, duration.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriod_ToDurationTo() {
 final LocalDateTime instant = new LocalDateTime(2012, 1, 1, 0, 0);
 final BasePeriod period = new BasePeriod(instant, instante.plus(new Duration(1, DurationFieldType.days())), PeriodType.standard());
 final Duration duration = period.toDurationTo(instant.plus(new Duration(2, DurationFieldType.days())));
 assertEquals("Duration should have 2 days", 2, duration.getValue(DurationFieldType.days()));
 }
}
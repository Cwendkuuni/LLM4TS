package org.joda.time.base;

import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriod_ints() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 assertEquals("Wrong period type", PeriodType.standard(), p.getPeriodType());
 assertEquals("Wrong value for year", 1, p.getValue(DurationFieldType.years()));
 assertEquals("Wrong value for month", 2, p.getValue(DurationFieldType.months()));
 // ... add assertions for all other fields
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriod_invalidValues_ints() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 p.setValue(DurationFieldType.days(), 37);
 }

 @Test
 public void testBasePeriod_instantInstants() {
 Instant startInstant = new Date().toInstant();
 Instant endInstant = startInstant.plus(Duration.standardDays(1));
 Period p = new BasePeriod(startInstant, endInstant, PeriodType.standard());
 assertEquals("Wrong period type", PeriodType.standard(), p.getPeriodType());
 // ... add assertions for all other fields
 }

 @Test
 public void testBasePeriod_partials() {
 LocalDateTime start = new LocalDateTime();
 LocalDateTime end = start.plusDays(1);
 Period p = new BasePeriod(start.toInstant(), end.toInstant(), PeriodType.standard());
 assertEquals("Wrong period type", PeriodType.standard(), p.getPeriodType());
 // ... add assertions for all other fields
 }

 @Test
 public void testBasePeriod_durationInstant() {
 Instant instant = new Date().toInstant();
 Period p = new BasePeriod(instant, Duration.standardDays(1), PeriodType.standard());
 assertEquals("Wrong period type", PeriodType.standard(), p.getPeriodType());
 // ... add assertions for all other fields
 }

 @Test
 public void testBasePeriod_instantDuration() {
 Instant instant = new Date().toInstant();
 Period p = new BasePeriod(Duration.standardDays(1), instant, PeriodType.standard());
 assertEquals("Wrong period type", PeriodType.standard(), p.getPeriodType());
 // ... add assertions for all other fields
 }

 @Test
 public void testBasePeriod_object() {
 Period p = new Period(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Period q = Period.standardDays(2);
 Period r = p.plus(q);
 Period s = new BasePeriod(r.toComponentIterator(), PeriodType.standard());
 assertEquals("Wrong period type", PeriodType.standard(), s.getPeriodType());
 // ... add assertions for all other fields
 }

 @Test
 public void testBasePeriod_setValues() {
 Period p = new BasePeriod(new int[8], PeriodType.standard());
 p.setValue(DurationFieldType.years(), 1);
 p.setValue(DurationFieldType.months(), 2);
 // ... set values for all other fields
 assertEquals("Wrong period type", PeriodType.standard(), p.getPeriodType());
 // ... add assertions for all other fields
 }

 @Test
 public void testBasePeriod_mergePeriods() {
 Period p1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Period p2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
 Period merged = new BasePeriod(p1, p2);
 Period expected = new Period(4, 6, 8, 10, 7, 13, 9, 11, PeriodType.standard());
 assertEquals("Wrong merged period", expected, merged);
 }

 @Test
 public void testBasePeriod_addPeriods() {
 Period p1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Period p2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
 Period sum = new BasePeriod(p1, p2);
 Period expected = new Period(4, 6, 8, 11, 12, 14, 16, 12, PeriodType.standard());
 assertEquals("Wrong sum period", expected, sum);
 }
}
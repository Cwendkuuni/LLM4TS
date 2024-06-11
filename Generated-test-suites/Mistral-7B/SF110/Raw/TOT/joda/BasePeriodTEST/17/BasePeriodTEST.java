package org.joda.time.base;

import org.junit.Test;
import org.joda.time.BasePeriod;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BasePeriodTest {

 @Test
 public void testEmptyPeriod() {
 BasePeriod period = new BasePeriod(PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertNull(period.toDurationFrom(null));
 assertNull(period.toDurationTo(null));
 }

 @Test
 public void testPeriodFromInstant() {
 BasePeriod period = new BasePeriod(LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-01T13:00:00.000Z").toInstant(), PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 Duration duration = period.toDurationFrom(LocalDateTime.parse("2012-01-01T12:00:00.000Z"));
 assertEquals(Duration.standardHours(1), duration);
 duration = period.toDurationTo(LocalDateTime.parse("2012-01-01T13:00:00.000Z"));
 assertEquals(Duration.standardHours(-1), duration);
 }

 @Test
 public void testPeriodFromInstantWithChronology() {
 BasePeriod period = new BasePeriod(LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-01T13:00:00.000Z").toInstant(), Chronology.ISO);
 assertEquals(PeriodType.standard(), period.getPeriodType());
 Duration duration = period.toDurationFrom(LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant());
 assertEquals(Duration.standardHours(1), duration);
 duration = period.toDurationTo(LocalDateTime.parse("2012-01-01T13:00:00.000Z").toInstant());
 assertEquals(Duration.standardHours(-1), duration);
 }

 @Test
 public void testPeriodFromReadableInstant() {
 BasePeriod period = new BasePeriod(LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-01T13:00:00.000Z").toInstant(), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(new Duration(Duration.standardHours(1)), PeriodType.standard());
 assertEquals(period, period2);
 }

 @Test
 public void testPeriodFromReadablePartial() {
 BasePeriod period = new BasePeriod(LocalDate.parse("2012-01-01").toLocalDateTime().toInstant(), LocalDate.parse("2012-02-02").toLocalDateTime().toInstant(), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(new BaseLocalDate(2012, 1, 1), new BaseLocalDate(2012, 2, 2), PeriodType.standard());
 assertEquals(period, period2);
 }

 @Test
 public void testPeriodFromReadablePartialWithChronology() {
 BasePeriod period = new BasePeriod(LocalDate.parse("2012-01-01").toLocalDateTime().toInstant(), LocalDate.parse("2012-02-02").toLocalDateTime().toInstant(), Chronology.ISO);
 BasePeriod period2 = new BasePeriod(new BaseLocalDate(2012, 1, 1, Chronology.ISO), new BaseLocalDate(2012, 2, 2, Chronology.ISO), PeriodType.standard());
 assertEquals(period, period2);
 }

 @Test
 public void testPeriodFromReadableDuration() {
 BasePeriod period = new BasePeriod(Duration.standardHours(1), LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant(), PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 Duration duration = period.toDurationFrom(LocalDateTime.parse("2012-01-01T12:00:00.000Z"));
 assertEquals(Duration.standardHours(1), duration);
 duration = period.toDurationTo(LocalDateTime.parse("2012-01-01T13:00:00.000Z"));
 assertEquals(Duration.standardHours(-1), duration);
 }

 @Test
 public void testPeriodFromReadableDurationWithChronology() {
 BasePeriod period = new BasePeriod(Duration.standardHours(1), LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant(), Chronology.ISO);
 assertEquals(PeriodType.standard(), period.getPeriodType());
 Duration duration = period.toDurationFrom(LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant());
 assertEquals(Duration.standardHours(1), duration);
 duration = period.toDurationTo(LocalDateTime.parse("2012-01-01T13:00:00.000Z").toInstant());
 assertEquals(Duration.standardHours(-1), duration);
 }

 @Test
 public void testPeriodFromObject() {
 BasePeriod period = new BasePeriod(Duration.standardHours(2), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(new Duration(Duration.standardHours(2)), PeriodType.standard());
 assertEquals(period, period2);
 }

 @Test
 public void testPeriodFromObjectWithChronology() {
 BasePeriod period = new BasePeriod(Duration.standardHours(2), Chronology.ISO, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(new MutablePeriod(Duration.standardHours(2), Chronology.ISO, PeriodType.standard()).toPeriod(), PeriodType.standard());
 assertEquals(period, period2);
 }

 @Test
 public void testPeriodFromArray() {
 BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 }

 @Test
 public void testPeriodFromArrayWithInvalidValue() {
 BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 9}, PeriodType.standard());
 assertNull(period);
 }

 @Test
 public void testPeriodFromArrayWithInvalidPeriodType() {
 BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.dayTime());
 assertNull(period);
 }

 @Test
 public void testPeriodAddition() {
 BasePeriod period1 = new BasePeriod(LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-01T13:00:00.000Z").toInstant(), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(LocalDateTime.parse("2012-01-02T12:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-02T13:00:00.000Z").toInstant(), PeriodType.standard());
 BasePeriod period3 = new BasePeriod(period1);
 period3.addPeriod(period2);
 BasePeriod period4 = new BasePeriod(LocalDateTime.parse("2012-01-03T12:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-03T13:00:00.000Z").toInstant(), PeriodType.standard());
 assertEquals(period4, period3);
 }

 @Test
 public void testPeriodMerging() {
 BasePeriod period1 = new BasePeriod(LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-02T12:00:00.000Z").toInstant(), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(LocalDateTime.parse("2012-01-02T13:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-03T12:00:00.000Z").toInstant(), PeriodType.standard());
 BasePeriod period3 = new BasePeriod(period1);
 period3.mergePeriod(period2);
 BasePeriod period4 = new BasePeriod(LocalDateTime.parse("2012-01-03T13:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-04T12:00:00.000Z").toInstant(), PeriodType.standard());
 assertEquals(period4, period3);
 }

 @Test
 public void testPeriodSubtraction() {
 BasePeriod period1 = new BasePeriod(LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-02T12:00:00.000Z").toInstant(), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(LocalDateTime.parse("2012-01-02T13:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-03T12:00:00.000Z").toInstant(), PeriodType.standard());
 BasePeriod period3 = new BasePeriod(period1);
 period3.addPeriod(period2);
 BasePeriod period4 = new BasePeriod(LocalDateTime.parse("2012-01-01T13:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-03T12:00:00.000Z").toInstant(), PeriodType.standard());
 BasePeriod period5 = new BasePeriod(period4);
 period5.mergePeriod(period1.negate());
 assertEquals(period5, period3);
 }

 @Test
 public void testPeriodSetValue() {
 BasePeriod period = new BasePeriod(LocalDateTime.parse("2012-01-01T12:00:00.000Z").toInstant(), LocalDateTime.parse("2012-01-02T12:00:00.000Z").toInstant(), PeriodType.standard());
 period.setField(DurationFieldType.years(), 1);
 assertEquals(1, period.getValue(DurationFieldType.years().getIndex()));
 period.setValue(DurationFieldType.years().getIndex(), 2);
 assertEquals(2, period.getValue(DurationFieldType.years().getIndex()));
 }
}
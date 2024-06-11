package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseDurationFieldType;
import org.joda.time.BaseLocalDate;
import org.joda.time.BaseLocalDateTime;
import org.joda.time.BaseLocalTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;

public class BasePeriodTest {

    @org.junit.Test
    public void testBasePeriod_ints() {
        PeriodType pt = PeriodType.standard();
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, pt);

        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.months()));
        assertEquals(3, period.getValue(DurationFieldType.weeks()));
        assertEquals(4, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(8, period.getValue(DurationFieldType.millis()));

        Duration d1 = period.toDurationFrom(new LocalDateTime());
        Duration d2 = period.toDurationTo(new LocalDateTime());
        assertEquals(d1, d2.negate());

        Period p = new Period(d1);
        assertEquals(period, p);
    }

    @org.junit.Test
    public void testBasePeriod_long() {
        PeriodType pt = PeriodType.standard();
        BasePeriod period = new BasePeriod(123456L, pt);

        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.months()));
        assertEquals(3, period.getValue(DurationFieldType.weeks()));
        assertEquals(4, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(56, period.getValue(DurationFieldType.millis()));

        LocalDateTime dt1 = new LocalDateTime();
        LocalDateTime dt2 = dt1.plus(period);
        Duration d1 = new Duration(dt1.toInstant(), dt2.toInstant());
        Duration d2 = period.toDurationFrom(dt1);
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testBasePeriod_ints_chronology() {
        PeriodType pt = PeriodType.standard();
        Chronology chronology = DateTimeUtils.getISOChronology();
        BasePeriod period = new BasePeriod(new LocalDate(2011, 12, 31), new LocalDate(2012, 1, 1), pt, chronology);

        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(1, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(1, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));

        Duration d1 = period.toDurationFrom(new LocalDateTime());
        Duration d2 = period.toDurationTo(new LocalDateTime());
        assertEquals(d1, d2.negate());

        Period p = new Period(d1);
        assertEquals(period, p);
    }

    @org.junit.Test
    public void testBasePeriod_ReadablePartial() {
        PeriodType pt = PeriodType.standard();
        BasePeriod period = new BasePeriod(new LocalDate(), new LocalDate().plusDays(2), pt);

        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(2, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));

        LocalDateTime dt1 = new LocalDateTime();
        LocalDateTime dt2 = dt1.plusDays(2);
        Duration d1 = new Duration(dt1.toInstant(), dt2.toInstant());
        Duration d2 = period.toDurationFrom(dt1);
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testBasePeriod_ReadableInstant_ReadableDuration() {
        PeriodType pt = PeriodType.standard();
        BasePeriod period = new BasePeriod(new LocalDateTime(), new Duration(1, 2, 3, 4, 5, 6, 7), pt);

        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(0, period.getValue(DurationFieldType.days()));
        assertEquals(1, period.getValue(DurationFieldType.hours()));
        assertEquals(2, period.getValue(DurationFieldType.minutes()));
        assertEquals(3, period.getValue(DurationFieldType.seconds()));
        assertEquals(4, period.getValue(DurationFieldType.millis()));

        Duration d1 = new Duration(new LocalDateTime(), new LocalDateTime().plusHours(1).plusMinutes(2).plusSeconds(3).plusMillis(4));
        Duration d2 = period.toDurationFrom(new LocalDateTime());
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testBasePeriod_ReadableDuration_ReadableInstant() {
        PeriodType pt = PeriodType.standard();
        BasePeriod period = new BasePeriod(new Duration(1, 2, 3, 4, 5, 6, 7), new LocalDateTime(), pt);

        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(0, period.getValue(DurationFieldType.days()));
        assertEquals(1, period.getValue(DurationFieldType.hours()));
        assertEquals(2, period.getValue(DurationFieldType.minutes()));
        assertEquals(3, period.getValue(DurationFieldType.seconds()));
        assertEquals(4, period.getValue(DurationFieldType.millis()));

        LocalDateTime dt1 = new LocalDateTime();
        LocalDateTime dt2 = dt1.plusHours(1).plusMinutes(2).plusSeconds(3).plusMillis(4);
        Duration d1 = new Duration(dt1.toInstant(), dt2.toInstant());
        Duration d2 = period.toDurationFrom(dt1);
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testBasePeriod_ReadablePeriod() {
        PeriodType pt = PeriodType.standard();
        BasePeriod period1 = new BasePeriod(new LocalDateTime(), new LocalDateTime().plusDays(2), pt);
        BasePeriod period2 = new BasePeriod(new LocalDateTime().plusDays(1), new LocalDateTime().plusDays(3), pt);

        period2.addPeriod(period1);

        assertEquals(PeriodType.standard(), period2.getPeriodType());
        assertEquals(2, period2.getValue(DurationFieldType.days()));

        LocalDateTime dt1 = new LocalDateTime();
        LocalDateTime dt2 = dt1.plusDays(3);
        Duration d1 = new Duration(dt1.toInstant(), dt2.toInstant());
        Duration d2 = period2.toDurationFrom(dt1);
        assertEquals(d1, d2);
    }

    @org.junit.Test
    public void testBasePeriod_ReadableInstant_ReadablePeriod() {
        PeriodType pt = PeriodType.standard();
        BasePeriod period1 = new BasePeriod(new LocalDateTime(), new LocalDateTime().plusDays(2), pt);
        BasePeriod period2 = new BasePeriod(new LocalDateTime().plusHours(1), new LocalDateTime().plusHours(3), pt);

        period2.mergePeriod(period1);

        assertEquals(PeriodType.standard(), period2.getPeriodType());
        assertEquals(3, period2.getValue(DurationFieldType.days())); // 2 days from period1 + 1 day from period2

        LocalDateTime dt1 = new LocalDateTime();
        LocalDateTime dt2 = dt1.plusHours(3).plusDays(2);
        Duration d1 = new Duration(dt1.toInstant(), dt2.toInstant());
        Duration d2 = period2.toDurationFrom(dt1);
        assertEquals(d1, d2);
    }
}
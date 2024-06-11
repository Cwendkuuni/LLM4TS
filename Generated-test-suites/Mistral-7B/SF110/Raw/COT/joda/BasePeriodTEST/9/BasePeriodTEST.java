package org.joda.time.base;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.format.*;

public class BasePeriodTest {

    BasePeriod period;
    BaseLocal localDate;
    BaseLocal localDateTime;
    Instant instant;
    DateTimeZone DateTimeZone;
    Chronology chronology;
    ReadableDuration readableDuration;
    ReadableInstant readableInstant;
    ReadablePartial readablePartial;
    ReadablePeriod readablePeriod;

    @Before
    public void setUp() {
        DateTimeZone = DateTimeZone.forID("UTC");
        chronology = GregorianChronology.getInstance(DateTimeZone);
        localDate = new LocalDate(2023, 3, 10, chronology);
        localDateTime = new LocalDateTime(2023, 3, 10, 10, 30, 0, DateTimeZone);
        instant = new DateTime(2023, 3, 10, 10, 30, 0, DateTimeZone).toInstant();
        readableDuration = Duration.standardDays(2);
        readableInstant = new DateTime(2023, 3, 11, 10, 30, 0, DateTimeZone).toInstant();
        readablePartial = new LocalDate(2023, 3, 12).withTimeAtSet(10, 30, 0);
        readablePeriod = new Period(2, 3, 1, 2, 4, 5, 6, 7, PeriodType.standard());
    }

    @Test
    public void testBasePeriodConstructorWithInts() {
        period = new BasePeriod(7, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{7, 2, 3, 4, 5, 6, 7, 8}, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithLong() {
        period = new BasePeriod(123456789);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructorWithNullChronology() {
        new BasePeriod(new int[8], null);
    }

    @Test
    public void testBasePeriodConstructorWithObject() {
        period = new BasePeriod(readablePeriod, PeriodType.standard(), chronology);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{2, 3, 1, 2, 4, 5, 6, 7}, period.getValues());
    }

    @Test
    public void testBasePeriodGetters() {
        period = new BasePeriod(123456789, PeriodType.standard(), chronology);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(123456789, period.toMillis());
        assertEquals(new Period(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()), period);
        assertEquals(new Period(1, 2, 3, 4, 5, 6, 7, 8, PeriodType. standard()), period.toPeriod());
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
    public void testBasePeriodToDurationFrom() {
        period = new BasePeriod(123456789, PeriodType.standard(), chronology);
        Duration duration = period.toDurationFrom(readableInstant);
        assertEquals(readableDuration, duration);
    }

    @Test
    public void testBasePeriodToDurationTo() {
        period = new BasePeriod(123456789, PeriodType.standard(), chronology);
        Duration duration = period.toDurationTo(readableInstant);
        assertEquals(readableDuration.negate(), duration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodCheckAndUpdateWithNullDurationFieldType() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.checkAndUpdate(null, new int[8], 1);
    }

    @Test
    public void testBasePeriodCheckAndUpdate() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.checkAndUpdate(DurationFieldType.years(), new int[8], 2);
        assertEquals(2, period.getYears());
    }

    @Test
    public void testBasePeriodSetPeriod() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.setPeriod(readablePeriod);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{2, 3, 1, 2, 4, 5, 6, 7}, period.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodSetPeriodWithNullPeriod() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.setPeriod(null);
    }

    @Test
    public void testBasePeriodSetPeriodInternal() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.setPeriodInternal(readablePeriod);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{2, 3, 1, 2, 4, 5, 6, 7}, period.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodSetPeriodInternalWithNullPeriod() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.setPeriodInternal(null);
    }

    @Test
    public void testBasePeriodSetPeriodWithSamePeriod() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.setPeriod(period);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[8], period.getValues());
    }

    @Test
    public void testBasePeriodMergePeriod() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.mergePeriod(readablePeriod);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{4, 5, 1, 2, 6, 7, 8, 9}, period.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodMergePeriodWithNullPeriod() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.mergePeriod(null);
    }

    @Test
    public void testBasePeriodAddPeriod() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.addPeriod(readablePeriod);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{3, 6, 1, 3, 8, 11, 13, 14}, period.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodAddPeriodWithNullPeriod() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.addPeriod(null);
    }

    @Test
    public void testBasePeriodAddField() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.addField(DurationFieldType.days(), 5);
        assertEquals(5, period.getDays());
    }

    @Test
    public void testBasePeriodAddFieldWithNegativeValue() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.addField(DurationFieldType.days(), -5);
        assertEquals(-5, period.getDays());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodAddFieldWithInvalidDurationFieldType() {
        period = new BasePeriod(new int[8], PeriodType.standard());
        period.addField(DurationFieldType.ZONES_AVAILABLE, 5);
    }
}
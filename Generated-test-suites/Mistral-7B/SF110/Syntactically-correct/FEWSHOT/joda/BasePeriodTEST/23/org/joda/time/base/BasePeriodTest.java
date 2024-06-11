package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testBasePeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(8, period.size());
        assertEquals(1, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(2, period.getValue(DurationFieldType.months().getCode()));
        assertEquals(3, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(5, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(6, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(7, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(8, period.getValue(DurationFieldType.millis().getCode()));

        period = new BasePeriod(1L, PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(8, period.size());

        period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01").toInstant(), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(8, period.size());

        period = new BasePeriod(DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:01.000Z").toInstant(), Duration.standardHours(1), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(8, period.size());

        period = new BasePeriod(Duration.standardDays(1), PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(8, period.size());

        period = new BasePeriod(Duration.standardHours(1), PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(8, period.size());

        period = new BasePeriod(Duration.standardMinutes(1), PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(8, period.size());

        period = new BasePeriod(Duration.standardMillis(1), PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(8, period.size());
    }

    @Test
    public void testBasePeriodConstructorNull() {
        try {
            new BasePeriod(null, PeriodType.standard(), DateTimeUtils.UTC);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new BasePeriod(new Object(), PeriodType.standard(), DateTimeUtils.UTC);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToDurationFrom() {
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationFrom(DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00Z").toInstant());
        assertEquals(Duration.standardDays(1).plus(Duration.standardHours(2)).plus(Duration.standardMinutes(3)).plus(Duration.standardSeconds(4)).plus(Duration.standardMillis(5)), duration);
    }

    @Test
    public void testToDurationTo() {
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationTo(DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00Z").toInstant());
        assertEquals(Duration.standardDays(1).minus(Duration.standardHours(2)).minus(Duration.standardMinutes(3)).minus(Duration.standardSeconds(4)).minus(Duration.standardMillis(5)), duration);
    }

    @Test
    public void testSetPeriod() {
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        DurationFieldType[] fields = {DurationFieldType.years(), DurationFieldType.months(), DurationFieldType.weeks(), DurationFieldType.days(),
                DurationFieldType.hours(), DurationFieldType.minutes(), DurationFieldType.seconds(), DurationFieldType.millis()};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9};
        ReadablePeriod readablePeriod = new BasePeriod(values, PeriodType.standard());

        period.setPeriod(readablePeriod);
        for (int i = 0; i < fields.length; ++i) {
            assertEquals(values[i], period.getValue(fields[i].getCode()));
        }

        period.setPeriod(null);
        for (int i = 0; i < fields.length; ++i) {
            assertEquals(0, period.getValue(fields[i].getCode()));
        }
    }

    @Test
    public void testSetField() {
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        DurationFieldType[] fields = {DurationFieldType.years(), DurationFieldType.months(), DurationFieldType.weeks(), DurationFieldType.days(),
                DurationFieldType.hours(), DurationFieldType.minutes(), DurationFieldType.seconds(), DurationFieldType.millis()};

        period.setField(DurationFieldType.years(), 2);
        assertEquals(2, period.getValue(DurationFieldType.years().getCode()));

        period.setField(DurationFieldType.months(), 3);
        assertEquals(3, period.getValue(DurationFieldType.months().getCode()));

        period.setField(DurationFieldType.weeks(), 4);
        assertEquals(4, period.getValue(DurationFieldType.weeks().getCode()));

        period.setField(DurationFieldType.days(), 5);
        assertEquals(5, period.getValue(DurationFieldType.days().getCode()));

        period.setField(DurationFieldType.hours(), 6);
        assertEquals(6, period.getValue(DurationFieldType.hours().getCode()));

        period.setField(DurationFieldType.minutes(), 7);
        assertEquals(7, period.getValue(DurationFieldType.minutes().getCode()));

        period.setField(DurationFieldType.seconds(), 8);
        assertEquals(8, period.getValue(DurationFieldType.seconds().getCode()));

        period.setField(DurationFieldType.millis(), 9);
        assertEquals(9, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testAddField() {
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        DurationFieldType[] fields = {DurationFieldType.years(), DurationFieldType.months(), DurationFieldType.weeks(), DurationFieldType.days(),
                DurationFieldType.hours(), DurationFieldType.minutes(), DurationFieldType.seconds(), DurationFieldType.millis()};

        period.addField(DurationFieldType.years(), 2);
        assertEquals(3, period.getValue(DurationFieldType.years().getCode()));

        period.addField(DurationFieldType.months(), 3);
        assertEquals(5, period.getValue(DurationFieldType.months().getCode()));

        period.addField(DurationFieldType.weeks(), 4);
        assertEquals(7, period.getValue(DurationFieldType.weeks().getCode()));

        period.addField(DurationFieldType.days(), 5);
        assertEquals(12, period.getValue(DurationFieldType.days().getCode()));

        period.addField(DurationFieldType.hours(), 6);
        assertEquals(18, period.getValue(DurationFieldType.hours().getCode()));

        period.addField(DurationFieldType.minutes(), 7);
        assertEquals(25, period.getValue(DurationFieldType.minutes().getCode()));

        period.addField(DurationFieldType.seconds(), 8);
        assertEquals(33, period.getValue(DurationFieldType.seconds().getCode()));

        period.addField(DurationFieldType.millis(), 9);
        assertEquals(42, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testMergePeriod() {
        Period p1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Period p2 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());

        p1.mergePeriod(p2);
        for (DurationFieldType field : DurationFieldType.values()) {
            assertEquals(p1.getValue(field.getCode()) + p2.getValue(field.getCode()), p1.getValue(field.getCode()));
        }
    }

    @Test
    public void testAddPeriod() {
        Period p1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Period p2 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());

        p1.addPeriod(p2);
        for (DurationFieldType field : DurationFieldType.values()) {
            assertEquals(p1.getValue(field.getCode()) + p2.getValue(field.getCode()), p1.getValue(field.getCode()));
        }
    }
}
package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.duration.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructionWithValues() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(4, 2, 1, 3, 5, 6, 7, 8, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(new int[]{4, 2, 1, 3, 5, 6, 7, 8}, period.getValues());
    }

    @Test
    public void testBasePeriodConstructionWithInstant() {
        PeriodType checkPeriodType = PeriodType.standard();
        Chronology chronology = GregorianChronology.getInstance();
        BasePeriod period = new BasePeriod(DateTimeConstants.MILLIS_PER_DAY * 30, DateTimeConstants.MILLIS_PER_DAY * 60, checkPeriodType, chronology);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(new Duration(DateTimeConstants.MILLIS_PER_DAY * 30, DateTimeConstants.MILLIS_PER_DAY * 60, chronology), period.toDurationFrom(DateTimeConstants.MINUTE));
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstant() {
        PeriodType checkPeriodType = PeriodType.standard();
        DateTimeZone utc = DateTimeZone.UTC;
        Instant instant = new DateTime(2023, 1, 1, 0, 0, 0, 0, utc).toInstant();
        BasePeriod period = new BasePeriod(instant, instant.plus(Duration.standardDays(30)), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(Duration.standardDays(30), period.toDurationFrom(instant));
    }

    @Test
    public void testBasePeriodConstructionWithReadablePartial() {
        PeriodType checkPeriodType = PeriodType.standard();
        BaseLocalDate baseLocalDate1 = new LocalDate(2023, 1, 1);
        BaseLocalDate baseLocalDate2 = new LocalDate(2023, 2, 1);
        BasePeriod period = new BasePeriod(baseLocalDate1, baseLocalDate2, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(DurationFieldType.years(), period.getFieldType(0));
        assertEquals(DurationFieldType.months(), period.getFieldType(1));
        assertEquals(DurationFieldType.days(), period.getFieldType(2));
        assertEquals(2, period.getValue(DurationFieldType.years()));
        assertEquals(1, period.getValue(DurationFieldType.months()));
        assertEquals(-31, period.getValue(DurationFieldType.days()));
    }

    @Test
    public void testBasePeriodConstructionWithReadableDurationAndInstant() {
        PeriodType checkPeriodType = PeriodType.standard();
        Instant instant = new DateTime(2023, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        Duration duration = Duration.standardHours(2).plus(Duration.standardMinutes(30));
        BasePeriod period = new BasePeriod(duration, instant, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(Duration.standardHours(2).plus(Duration.standardMinutes(30)), period.toDurationFrom(instant));
    }

    @Test
    public void testBasePeriodConstructionWithReadableDurationAndReadableInstant() {
        PeriodType checkPeriodType = PeriodType.standard();
        DateTimeZone utc = DateTimeZone.UTC;
        Instant instant1 = new DateTime(2023, 1, 1, 0, 0, 0, 0, utc).toInstant();
        Instant instant2 = new DateTime(2023, 1, 2, 0, 0, 0, 0, utc).toInstant();
        Duration duration = Duration.standardHours(2).plus(Duration.standardMinutes(30));
        BasePeriod period = new BasePeriod(duration, instant1, instant2, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(Duration.standardHours(2).plus(Duration.standardMinutes(30)), period.toDurationFrom(instant1));
    }

    @Test
    public void testBasePeriodConstructionWithObject() {
        PeriodType checkPeriodType = PeriodType.standard();
        String periodString = "PT2H30M";
        BasePeriod period = new BasePeriod(Duration.parse(periodString), checkPeriodType, GregorianChronology.getInstance());
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(Duration.standardHours(2).plus(Duration.standardMinutes(30)), period.toDurationFrom(DateTimeConstants.MINUTE));
    }

    @Test
    public void testBasePeriodWithGetters() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(4, 2, 1, 3, 5, 6, 7, 8, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(4, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.months()));
        assertEquals(1, period.getValue(DurationFieldType.weeks()));
        assertEquals(3, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(8, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testBasePeriodWithToDuration() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(4, 2, 1, 3, 5, 6, 7, 8, checkPeriodType);
        Duration durationFrom = period.toDurationFrom(DateTimeConstants.MINUTE);
        Duration durationTo = period.toDurationTo(DateTimeConstants.MINUTE);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(durationFrom, Duration.standardDays(4).plus(Duration.standardWeeks(1)).plus(Duration.standardMonths(2)).plus(Duration.standardHours(5))
                .plus(Duration.standardMinutes(6)).plus(Duration.standardSeconds(7)).plus(Duration.standardMillis(8)));
        assertEquals(durationTo, Duration.standardDays(-4).plus(Duration.standardWeeks(-1)).plus(Duration.standardMonths(-2)).plus(Duration.standardHours(-5))
                .plus(Duration.standardMinutes(-6)).plus(Duration.standardSeconds(-7)).plus(Duration.standardMillis(-8)));
    }

    @Test
    public void testBasePeriodWithSettingFields() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(4, 2, 1, 3, 5, 6, 7, 8, checkPeriodType);
        int[] values = period.getValues();
        period.setValue(DurationFieldType.years(), 7);
        period.setValue(DurationFieldType.months(), 8);
        period.setValue(DurationFieldType.days(), 9);
        period.setValue(DurationFieldType.hours(), 10);
        period.setValue(DurationFieldType.minutes(), 11);
        period.setValue(DurationFieldType.seconds(), 12);
        period.setValue(DurationFieldType.millis(), 13);
        assertEquals(7, period.getValue(DurationFieldType.years()));
        assertEquals(8, period.getValue(DurationFieldType.months()));
        assertEquals(9, period.getValue(DurationFieldType.days()));
        assertEquals(10, period.getValue(DurationFieldType.hours()));
        assertEquals(11, period.getValue(DurationFieldType.minutes()));
        assertEquals(12, period.getValue(DurationFieldType.seconds()));
        assertEquals(13, period.getValue(DurationFieldType.millis()));
        assertEquals(new int[]{7, 8, 9, 10, 11, 12, 13}, period.getValues());
    }

    @Test
    public void testBasePeriodWithSettingFieldsWithInvalidPeriod() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(4, 2, 1, 3, 5, 6, 7, 8, checkPeriodType);
        try {
            period.setValue(DurationFieldType.years(), 14);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBasePeriodWithSettingFieldsWithNullPeriod() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(4, 2, 1, 3, 5, 6, 7, 8, checkPeriodType);
        try {
            period.setValue(null, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBasePeriodWithSettingPeriod() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period1 = new BasePeriod(4, 2, 1, 3, 5, 6, 7, 8, checkPeriodType);
        BasePeriod period2 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period1.setPeriod(period2);
        assertEquals(PeriodType.standard(), period1.getPeriodType());
        assertEquals(1, period1.getValue(DurationFieldType.years()));
        assertEquals(2, period1.getValue(DurationFieldType.months()));
        assertEquals(3, period1.getValue(DurationFieldType.weeks()));
        assertEquals(4, period1.getValue(DurationFieldType.days()));
        assertEquals(5, period1.getValue(DurationFieldType.hours()));
        assertEquals(6, period1.getValue(DurationFieldType.minutes()));
        assertEquals(7, period1.getValue(DurationFieldType.seconds()));
        assertEquals(8, period1.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testBasePeriodWithSettingPeriodWithNullPeriod() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(4, 2, 1, 3, 5, 6, 7, 8, checkPeriodType);
        period.setPeriod(null);
        assertEquals(new int[8], period.getValues());
    }
}
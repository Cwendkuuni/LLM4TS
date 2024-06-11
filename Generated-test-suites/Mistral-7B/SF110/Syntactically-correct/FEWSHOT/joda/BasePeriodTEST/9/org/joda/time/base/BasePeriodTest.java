package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testBasePeriod_ints_PeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(7, 2, 3, 4, 5, 6, 1, 2, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());

        int[] values = period.getValues();
        assertEquals(8, values.length);
        assertEquals(7, values[0]);
        assertEquals(2, values[1]);
        assertEquals(3, values[2]);
        assertEquals(4, values[3]);
        assertEquals(5, values[4]);
        assertEquals(6, values[5]);
        assertEquals(1, values[6]);
        assertEquals(2, values[7]);
    }

    @Test
    public void testBasePeriod_long_PeriodType_Chronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        Chronology chronology = GregorianChronology.getInstance();
        BasePeriod period = new BasePeriod(DateTimeUtils.currentTimeMillis(), 1000L, checkPeriodType, chronology);
        assertEquals(checkPeriodType, period.getPeriodType());

        int[] values = period.getValues();
        assertEquals(8, values.length);
        assertNotNull(values);
    }

    @Test
    public void testBasePeriod_ReadableInstant_ReadableInstant_PeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), DateTimeUtils.currentTime().plus(Duration.standardDays(1)), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
    }

    @Test
    public void testBasePeriod_ReadablePartial_ReadablePartial_PeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(new LocalDateTime(2023, 1, 1, 12, 0, 0).toInstant(), new LocalDateTime(2023, 1, 2, 12, 0, 0).toInstant(), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
    }

    @Test
    public void testBasePeriod_ReadableInstant_ReadableDuration_PeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), Duration.standardDays(1), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
    }

    @Test
    public void testBasePeriod_ReadableDuration_ReadableInstant_PeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(Duration.standardDays(1), DateTimeUtils.currentTime(), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
    }

    @Test
    public void testBasePeriod_long() {
        BasePeriod period = new BasePeriod(1234567890L);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriod_long_PeriodType_Chronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        Chronology chronology = GregorianChronology.getInstance();
        BasePeriod period = new BasePeriod(1234567890L, checkPeriodType, chronology);
        assertEquals(checkPeriodType, period.getPeriodType());
    }

    @Test
    public void testBasePeriod_Object_PeriodType_Chronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        Chronology chronology = GregorianChronology.getInstance();
        BasePeriod period = new BasePeriod(Duration.standardDays(1), null, checkPeriodType, chronology);
        assertEquals(checkPeriodType, period.getPeriodType());
    }

    @Test
    public void testBasePeriod_ints() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(new int[]{7, 2, 3, 4, 5, 6, 1, 2});
        assertEquals(checkPeriodType, period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), DateTimeUtils.currentTime().plus(Duration.standardDays(1)), checkPeriodType);
        assertEquals(1, period.getValue(DurationFieldType.days()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue_invalidField() {
        BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), DateTimeUtils.currentTime().plus(Duration.standardDays(1)), PeriodType.standard());
        period.getValue(DurationFieldType.years());
    }

    @Test
    public void testToDurationFrom() {
        BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), DateTimeUtils.currentTime().plus(Duration.standardDays(1)), PeriodType.standard());
        assertEquals(Duration.standardDays(1), period.toDurationFrom(DateTimeUtils.currentTime()));
    }

    @Test
    public void testToDurationTo() {
        BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), DateTimeUtils.currentTime().plus(Duration.standardDays(1)), PeriodType.standard());
        assertEquals(Duration.standardDays(1), period.toDurationTo(DateTimeUtils.currentTime().plus(Duration.standardDays(1))));
    }
}
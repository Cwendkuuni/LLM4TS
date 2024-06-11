package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.format.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstruction() {
        PeriodType type = PeriodType.standard();
        BasePeriod period = new BasePeriod(5, 2, 1, 3, 4, 5, 6, 7, type);
        assertEquals("Incorrect type", type, period.getPeriodType());
        assertEquals("Incorrect length", 8, period.size());
        assertEquals("Incorrect values", new int[]{5, 2, 1, 3, 4, 5, 6, 7}, period.getValues());
    }

    @Test
    public void testBasePeriodFromInstant() {
        DateTimeZone zone = DateTimeZone.forID("Europe/Paris");
        DateTime start = new DateTime(zone, 2015, 1, 1, 0, 0, 0);
        DateTime end = new DateTime(zone, 2015, 1, 1, 1, 0, 0);
        BasePeriod period = new BasePeriod(start.toInstant(), end.toInstant(), PeriodType.standard());
        assertEquals("Incorrect type", PeriodType.standard(), period.getPeriodType());
        assertEquals("Incorrect length", 8, period.size());
        assertEquals("Incorrect values", period.getValues(), new int[]{1, 0, 0, 1, 0, 0, 1, 0});
    }

    @Test
    public void testBasePeriodFromPartial() {
        BaseDateTime baseDateTime1 = new BaseDateTime(DateTimeZone.forID("Europe/Paris"), 2015, 1, 1, 0, 0, 0, 0);
        BaseDateTime baseDateTime2 = new BaseDateTime(DateTimeZone.forID("Europe/Paris"), 2015, 1, 1, 1, 0, 0, 0);
        BasePeriod period = new BasePeriod(baseDateTime1.toLocalDateTime(), baseDateTime2.toLocalDateTime(), PeriodType.standard());
        assertEquals("Incorrect type", PeriodType.standard(), period.getPeriodType());
        assertEquals("Incorrect length", 8, period.size());
        assertEquals("Incorrect values", period.getValues(), new int[]{1, 0, 0, 1, 0, 0, 1, 0});
    }

    @Test
    public void testBasePeriodFromDurationAndInstant() {
        DateTimeZone zone = DateTimeZone.forID("Europe/Paris");
        DateTime start = new DateTime(zone, 2015, 1, 1, 0, 0, 0);
        Duration duration = new Duration(3, 0, 0, 0);
        BasePeriod period = new BasePeriod(duration, start.toInstant(), PeriodType.standard());
        assertEquals("Incorrect type", PeriodType.standard(), period.getPeriodType());
        assertEquals("Incorrect length", 8, period.size());
        assertEquals("Incorrect values", period.getValues(), new int[]{0, 0, 3, 0, 0, 0, 0, 0});
    }

    @Test
    public void testBasePeriodFromDurationAndDateTime() {
        DateTimeZone zone = DateTimeZone.forID("Europe/Paris");
        DateTime start = new DateTime(zone, 2015, 1, 1, 0, 0, 0);
        Duration duration = new Duration(3, 0, 0, 0);
        BasePeriod period = new BasePeriod(duration, start, PeriodType.standard());
        assertEquals("Incorrect type", PeriodType.standard(), period.getPeriodType());
        assertEquals("Incorrect length", 8, period.size());
        assertEquals("Incorrect values", period.getValues(), new int[]{0, 0, 3, 0, 0, 0, 0, 0});
    }

    @Test
    public void testBasePeriodFromObject() {
        Period period = new Period(3, 2, 1, 3, 4, 5, 6, 7);
        BasePeriod basePeriod = new BasePeriod(period, PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals("Incorrect type", PeriodType.standard(), basePeriod.getPeriodType());
        assertEquals("Incorrect length", 8, basePeriod.size());
        assertEquals("Incorrect values", basePeriod.getValues(), period.toArray());
    }

    @Test
    public void testBasePeriodFromIntArray() {
        int[] values = {3, 2, 1, 3, 4, 5, 6, 7};
        BasePeriod basePeriod = new BasePeriod(values, PeriodType.standard());
        assertEquals("Incorrect type", PeriodType.standard(), basePeriod.getPeriodType());
        assertEquals("Incorrect length", 8, basePeriod.size());
        assertEquals("Incorrect values", basePeriod.getValues(), values);
    }

    @Test
    public void testBasePeriodToDurationFrom() {
        DateTimeZone zone = DateTimeZone.forID("Europe/Paris");
        DateTime start = new DateTime(zone, 2015, 1, 1, 0, 0, 0);
        BasePeriod period = new BasePeriod(start.toInstant(), end.toInstant(), PeriodType.standard());
        Duration duration = period.toDurationFrom(start);
        assertEquals("Incorrect duration", new Duration(3, 0, 0, 0), duration);
    }

    @Test
    public void testBasePeriodToDurationTo() {
        DateTimeZone zone = DateTimeZone.forID("Europe/Paris");
        DateTime start = new DateTime(zone, 2015, 1, 1, 0, 0, 0);
        BasePeriod period = new BasePeriod(start.toInstant(), end.toInstant(), PeriodType.standard());
        Duration duration = period.toDurationTo(end);
        assertEquals("Incorrect duration", new Duration(-3, 0, 0, 0), duration);
    }

    @Test
    public void testBasePeriodSetPeriod() {
        Period period = new Period(3, 2, 1, 3, 4, 5, 6, 7);
        BasePeriod basePeriod = new BasePeriod(new int[8], PeriodType.standard());
        basePeriod.setPeriod(period);
        assertEquals("Incorrect type", PeriodType.standard(), basePeriod.getPeriodType());
        assertEquals("Incorrect length", 8, basePeriod.size());
        assertEquals("Incorrect values", basePeriod.getValues(), period.toArray());
    }

    @Test
    public void testBasePeriodSetPeriodInternal() {
        Period period = new Period(3, 2, 1, 3, 4, 5, 6, 7);
        BasePeriod basePeriod = new BasePeriod(new int[8], PeriodType.standard());
        basePeriod.setPeriodInternal(period);
        assertEquals("Incorrect type", PeriodType.standard(), basePeriod.getPeriodType());
        assertEquals("Incorrect length", 8, basePeriod.size());
        assertEquals("Incorrect values", basePeriod.getValues(), period.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodInvalidPeriod() {
        BasePeriod basePeriod = new BasePeriod(new int[8], PeriodType.standard());
        basePeriod.setPeriod(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodInvalidFieldType() {
        DateTimeZone zone = DateTimeZone.forID("Europe/Paris");
        DateTime start = new DateTime(zone, 2015, 1, 1, 0, 0, 0);
        BasePeriod basePeriod = new BasePeriod(start.toInstant(), end.toInstant(), PeriodType.standard());
        basePeriod.checkAndUpdate(DurationFieldType.years(), new int[8], 10);
    }
}
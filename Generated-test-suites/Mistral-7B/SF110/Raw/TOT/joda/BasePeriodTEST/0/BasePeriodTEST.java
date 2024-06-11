package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.duration.*;

public class BasePeriodTest {

    @org.junit.Test
    public void testBasePeriod_Instants() {
        LocalDateTime dt1 = new LocalDateTime(2012, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        LocalDateTime dt2 = new LocalDateTime(2012, 1, 2, 13, 30, 0, 0, DateTimeZone.UTC);
        BasePeriod period = new BasePeriod(dt1, dt2, PeriodType.standard());

        // test getters
        assertEquals("Wrong period type", PeriodType.standard(), period.getPeriodType());
        assertEquals("Wrong year value", 0, period.getValue(DurationFieldType.years()));
        assertEquals("Wrong month value", 1, period.getValue(DurationFieldType.months()));
        assertEquals("Wrong day of year value", 365, period.getValue(DurationFieldType.daysOfYear()));
        assertEquals("Wrong day value", 1, period.getValue(DurationFieldType.days()));
        assertEquals("Wrong hour value", 12, period.getValue(DurationFieldType.hours()));
        assertEquals("Wrong minute value", 0, period.getValue(DurationFieldType.minutes()));
        assertEquals("Wrong second value", 0, period.getValue(DurationFieldType.seconds()));
        assertEquals("Wrong millisecond value", 0, period.getValue(DurationFieldType.millis()));

        // test methods
        Duration d1 = period.toDurationFrom(dt1);
        Duration d2 = period.toDurationTo(dt2);
        assertEquals("Wrong duration from", d1, new Duration(Period.standard().getMillis()));
        assertEquals("Wrong duration to", d2, new Duration(Period.standard().getMillis()));
    }

    @org.junit.Test
    public void testBasePeriod_Durations() {
        BasePeriod period1 = new BasePeriod(Duration.standard(1, TimeUnit.DAYS));
        BasePeriod period2 = new BasePeriod(Duration.standard(1, TimeUnit.HOURS, 2, TimeUnit.MINUTES));

        // test getters
        assertEquals("Wrong period type", PeriodType.standard(), period1.getPeriodType());
        assertEquals("Wrong year value", 0, period1.getValue(DurationFieldType.years()));
        assertEquals("Wrong month value", 0, period1.getValue(DurationFieldType.months()));
        assertEquals("Wrong day of year value", 1, period1.getValue(DurationFieldType.daysOfYear()));
        assertEquals("Wrong day value", 0, period1.getValue(DurationFieldType.days()));
        assertEquals("Wrong hour value", 0, period1.getValue(DurationFieldType.hours()));
        assertEquals("Wrong minute value", 0, period1.getValue(DurationFieldType.minutes()));
        assertEquals("Wrong second value", 0, period1.getValue(DurationFieldType.seconds()));
        assertEquals("Wrong millisecond value", 0, period1.getValue(DurationFieldType.millis()));

        assertEquals("Wrong period type", PeriodType.standard(), period2.getPeriodType());
        assertEquals("Wrong year value", 0, period2.getValue(DurationFieldType.years()));
        assertEquals("Wrong month value", 0, period2.getValue(DurationFieldType.months()));
        assertEquals("Wrong day of year value", 0, period2.getValue(DurationFieldType.daysOfYear()));
        assertEquals("Wrong day value", 0, period2.getValue(DurationFieldType.days()));
        assertEquals("Wrong hour value", 1, period2.getValue(DurationFieldType.hours()));
        assertEquals("Wrong minute value", 2, period2.getValue(DurationFieldType.minutes()));
        assertEquals("Wrong second value", 0, period2.getValue(DurationFieldType.seconds()));
        assertEquals("Wrong millisecond value", 0, period2.getValue(DurationFieldType.millis()));

        // test methods
        LocalDateTime dt1 = new LocalDateTime(2012, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        LocalDateTime dt2 = new LocalDateTime(2012, 1, 1, 13, 2, 0, 0, DateTimeZone.UTC);
        period1 = new BasePeriod(dt1, dt2, PeriodType.standard());
        period2 = new BasePeriod(period1.toDurationFrom(dt1), PeriodType.standard());
        assertEquals("Periods are not equal", period1, period2);
    }

    @org.junit.Test
    public void testBasePeriod_InvalidInput() {
        try {
            new BasePeriod(null, null, PeriodType.standard());
            fail("Should not be able to create a period with null inputs");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new BasePeriod(new LocalDateTime(), null, PeriodType.standard());
            fail("Should not be able to create a period with a null end instant");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}
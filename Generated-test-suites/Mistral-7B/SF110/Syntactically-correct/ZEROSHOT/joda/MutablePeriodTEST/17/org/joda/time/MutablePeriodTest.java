package org.joda.time.test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.field.*;

public class MutablePeriodTest {

    @org.junit.Test
    public void testMutablePeriodCreation() {
        // Test default constructor
        MutablePeriod mutablePeriod = new MutablePeriod();
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());

        // Test constructors with various arguments
        MutablePeriod mutablePeriod1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod mutablePeriod2 = new MutablePeriod(1, 2, PeriodType.standard());
        MutablePeriod mutablePeriod3 = new MutablePeriod(1234567890L);
        MutablePeriod mutablePeriod4 = new MutablePeriod(1234567890L, PeriodType.standard());
        MutablePeriod mutablePeriod5 = new MutablePeriod(new DateTime(2022, 1, 1, 12, 30, 45, 60, DateTimeZone.UTC), new DateTime(2022, 1, 2, 13, 45, 0, 0, DateTimeZone.UTC));
        MutablePeriod mutablePeriod6 = new MutablePeriod(new Period(1, 2, 3, 4, 5, 6, 7, 8), PeriodType.standard());
        MutablePeriod mutablePeriod7 = new MutablePeriod(new Object(), PeriodType.standard());
        MutablePeriod mutablePeriod8 = new MutablePeriod(new Object(), new Chronology("Etc/GMT+1"), PeriodType.standard());

        // Test parsing from strings
        MutablePeriod mutablePeriod9 = MutablePeriod.parse("P1Y2M3DT4H5M6S7MS8");
        MutablePeriod mutablePeriod10 = MutablePeriod.parse("P1Y2M3DT4H5M6.7S", ISOPeriodFormat.standard());

        // Test setting and getting values
        mutablePeriod1.clear();
        mutablePeriod1.setValue(3, 1);
        mutablePeriod1.set(DurationFieldType.years(), 1);
        mutablePeriod1.set(DurationFieldType.months(), 2);
        mutablePeriod1.set(DurationFieldType.weeks(), 3);
        mutablePeriod1.set(DurationFieldType.days(), 4);
        mutablePeriod1.set(DurationFieldType.hours(), 5);
        mutablePeriod1.set(DurationFieldType.minutes(), 6);
        mutablePeriod1.set(DurationFieldType.seconds(), 7);
        mutablePeriod1.set(DurationFieldType.millis(), 8);

        assertEquals(1, mutablePeriod1.getYears());
        assertEquals(2, mutablePeriod1.getMonths());
        assertEquals(3, mutablePeriod1.getWeeks());
        assertEquals(4, mutablePeriod1.getDays());
        assertEquals(5, mutablePeriod1.getHours());
        assertEquals(6, mutablePeriod1.getMinutes());
        assertEquals(7, mutablePeriod1.getSeconds());
        assertEquals(8, mutablePeriod1.getMillis());

        mutablePeriod1.setPeriod(new Period(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()));
        assertEquals(1, mutablePeriod1.getYears());
        assertEquals(2, mutablePeriod1.getMonths());
        assertEquals(3, mutablePeriod1.getWeeks());
        assertEquals(4, mutablePeriod1.getDays());
        assertEquals(5, mutablePeriod1.getHours());
        assertEquals(6, mutablePeriod1.getMinutes());
        assertEquals(7, mutablePeriod1.getSeconds());
        assertEquals(8, mutablePeriod1.getMillis());

        // Test adding periods
        MutablePeriod mutablePeriod11 = new MutablePeriod();
        mutablePeriod11.add(mutablePeriod1);
        assertEquals(2, mutablePeriod11.getYears());
        assertEquals(4, mutablePeriod11.getMonths());
        assertEquals(6, mutablePeriod11.getWeeks());
        assertEquals(7, mutablePeriod11.getDays());
        assertEquals(10, mutablePeriod11.getHours());
        assertEquals(11, mutablePeriod11.getMinutes());
        assertEquals(13, mutablePeriod11.getSeconds());
        assertEquals(16, mutablePeriod11.getMillis());

        MutablePeriod mutablePeriod12 = new MutablePeriod();
        mutablePeriod12.add(new Period(1, 1, 1, 1, 1, 1, 1, 1, PeriodType.standard()));
        mutablePeriod12.add(mutablePeriod1);
        assertEquals(3, mutablePeriod12.getYears());
        assertEquals(5, mutablePeriod12.getMonths());
        assertEquals(8, mutablePeriod12.getWeeks());
        assertEquals(11, mutablePeriod12.getDays());
        assertEquals(16, mutablePeriod12.getHours());
        assertEquals(17, mutablePeriod12.getMinutes());
        assertEquals(18, mutablePeriod12.getSeconds());
        assertEquals(22, mutablePeriod12.getMillis());

        // Test merging periods
        MutablePeriod mutablePeriod13 = new MutablePeriod();
        mutablePeriod13.mergePeriod(mutablePeriod1);
        assertEquals(1, mutablePeriod13.getYears());
        assertEquals(2, mutablePeriod13.getMonths());
        assertEquals(3, mutablePeriod13.getWeeks());
        assertEquals(4, mutablePeriod13.getDays());
        assertEquals(5, mutablePeriod13.getHours());
        assertEquals(6, mutablePeriod13.getMinutes());
        assertEquals(7, mutablePeriod13.getSeconds());
        assertEquals(8, mutablePeriod13.getMillis());

        MutablePeriod mutablePeriod14 = new MutablePeriod();
        mutablePeriod14.mergePeriod(mutablePeriod1);
        mutablePeriod14.mergePeriod(mutablePeriod1);
        assertEquals(2, mutablePeriod14.getYears());
        assertEquals(4, mutablePeriod14.getMonths());
        assertEquals(6, mutablePeriod14.getWeeks());
        assertEquals(8, mutablePeriod14.getDays());
        assertEquals(10, mutablePeriod14.getHours());
        assertEquals(12, mutablePeriod14.getMinutes());
        assertEquals(13, mutablePeriod14.getSeconds());
        assertEquals(16, mutablePeriod14.getMillis());

        // Test setting and getting fields
        mutablePeriod1.setField(DurationFieldType.years(), -1);
        assertEquals(-1, mutablePeriod1.getYears());
        mutablePeriod1.setField(DurationFieldType.years(), Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, mutablePeriod1.getYears());

        mutablePeriod1.setField(DurationFieldType.months(), -1);
        assertEquals(-1, mutablePeriod1.getMonths());
        mutablePeriod1.setField(DurationFieldType.months(), Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, mutablePeriod1.getMonths());

        mutablePeriod1.setField(DurationFieldType.weeks(), -1);
        assertEquals(-1, mutablePeriod1.getWeeks());
        mutablePeriod1.setField(DurationFieldType.weeks(), Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, mutablePeriod1.getWeeks());

        mutablePeriod1.setField(DurationFieldType.days(), -1);
        assertEquals(-1, mutablePeriod1.getDays());
        mutablePeriod1.setField(DurationFieldType.days(), Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, mutablePeriod1.getDays());

        mutablePeriod1.setField(DurationFieldType.hours(), -1);
        assertEquals(-1, mutablePeriod1.getHours());
        mutablePeriod1.setField(DurationFieldType.hours(), Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, mutablePeriod1.getHours());

        mutablePeriod1.setField(DurationFieldType.minutes(), -1);
        assertEquals(-1, mutablePeriod1.getMinutes());
        mutablePeriod1.setField(DurationFieldType.minutes(), Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, mutablePeriod1.getMinutes());

        mutablePeriod1.setField(DurationFieldType.seconds(), -1);
        assertEquals(-1, mutablePeriod1.getSeconds());
        mutablePeriod1.setField(DurationFieldType.seconds(), Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, mutablePeriod1.getSeconds());

        mutablePeriod1.setField(DurationFieldType.millis(), -1);
        assertEquals(-1, mutablePeriod1.getMillis());
        mutablePeriod1.setField(DurationFieldType.millis(), Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, mutablePeriod1.getMillis());

        // Test setting and getting periods with different chronologies
        MutablePeriod mutablePeriod15 = new MutablePeriod();
        mutablePeriod15.setPeriod(new Period(1, 1, 1, 1, 1, 1, 1, 1, DateTimeUtils.getDateTime("2022-01-01T01:01:01Z").getChronology()));
        mutablePeriod15.setPeriod(new Period(1, 1, 1, 1, 1, 1, 1, 1, DateTimeUtils.getDateTime("2022-01-01T01:01:01+01:00").getChronology()));
        assertEquals(0, mutablePeriod15.getYears());
        assertEquals(0, mutablePeriod15.getMonths());
        assertEquals(0, mutablePeriod15.getWeeks());
        assertEquals(0, mutablePeriod15.getDays());
        assertEquals(0, mutablePeriod15.getHours());
        assertEquals(0, mutablePeriod15.getMinutes());
        assertEquals(0, mutablePeriod15.getSeconds());
        assertEquals(0, mutablePeriod15.getMillis());
    }
}
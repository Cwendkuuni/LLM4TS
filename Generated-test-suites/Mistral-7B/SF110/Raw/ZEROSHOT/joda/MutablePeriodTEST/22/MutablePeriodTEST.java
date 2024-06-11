package org.joda.time.test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.field.*;

public class MutablePeriodTest {

    @org.junit.Test
    public void testMutablePeriodDefaultConstructor() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodWithPeriodType() {
        PeriodType periodType = PeriodType.standard();
        MutablePeriod mutablePeriod = new MutablePeriod(periodType);
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodWithValues() {
        MutablePeriod mutablePeriod = new MutablePeriod(3, 2, 5, 12, 15, 25, 30, 45);
        assertEquals(3, mutablePeriod.getYears());
        assertEquals(2, mutablePeriod.getMonths());
        assertEquals(5, mutablePeriod.getWeeks());
        assertEquals(12, mutablePeriod.getDays());
        assertEquals(15, mutablePeriod.getHours());
        assertEquals(25, mutablePeriod.getMinutes());
        assertEquals(30, mutablePeriod.getSeconds());
        assertEquals(45, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodWithLongAndPeriodType() {
        PeriodType periodType = PeriodType.standard();
        MutablePeriod mutablePeriod = new MutablePeriod(1234567890123L, periodType);
        assertEquals(13662, mutablePeriod.getYears());
        assertEquals(159, mutablePeriod.getMonths());
        assertEquals(247, mutablePeriod.getWeeks());
        assertEquals(867, mutablePeriod.getDays());
        assertEquals(3504, mutablePeriod.getHours());
        assertEquals(21024, mutablePeriod.getMinutes());
        assertEquals(126144, mutablePeriod.getSeconds());
        assertEquals(42566400, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodWithLongAndChronologyAndPeriodType() {
        PeriodType periodType = PeriodType.standard();
        Chronology chronology = ISOChronology.getInstance();
        MutablePeriod mutablePeriod = new MutablePeriod(1234567890123L, periodType, chronology);
        assertEquals(13662, mutablePeriod.getYears());
        assertEquals(159, mutablePeriod.getMonths());
        assertEquals(247, mutablePeriod.getWeeks());
        assertEquals(867, mutablePeriod.getDays());
        assertEquals(3504, mutablePeriod.getHours());
        assertEquals(21024, mutablePeriod.getMinutes());
        assertEquals(126144, mutablePeriod.getSeconds());
        assertEquals(42566400, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodWithReadableInstantAndReadableInstant() {
        DateTimeZone dateTimeZone = DateTimeZone.forID("America/New_York");
        DateTime datetime1 = new DateTime(2022, 1, 1, 12, 0, 0, 0, dateTimeZone);
        DateTime datetime2 = new DateTime(2022, 1, 1, 13, 0, 0, 0, dateTimeZone);
        MutablePeriod mutablePeriod = new MutablePeriod(datetime1, datetime2);
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(1, mutablePeriod.getDays());
        assertEquals(1, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodWithReadableInstantAndReadableDuration() {
        DateTimeZone dateTimeZone = DateTimeZone.forID("America/New_York");
        DateTime datetime = new DateTime(2022, 1, 1, 12, 0, 0, 0, dateTimeZone);
        Duration duration = Duration.standardHours(1);
        MutablePeriod mutablePeriod = new MutablePeriod(datetime, duration);
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(1, mutablePeriod.getDays());
        assertEquals(1, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testParseMutablePeriodFromString() {
        String periodString = "P3Y2M4D5H";
        MutablePeriod mutablePeriod = MutablePeriod.parse(periodString);
        assertEquals(3, mutablePeriod.getYears());
        assertEquals(2, mutablePeriod.getMonths());
        assertEquals(4, mutablePeriod.getWeeks());
        assertEquals(5, mutablePeriod.getDays());
        assertEquals(5, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testParseMutablePeriodFromStringWithFormat() {
        String periodString = "P3Y2M4D5H";
        PeriodFormatter periodFormatter = ISOPeriodFormat.standard();
        MutablePeriod mutablePeriod = MutablePeriod.parse(periodString, periodFormatter);
        assertEquals(3, mutablePeriod.getYears());
        assertEquals(2, mutablePeriod.getMonths());
        assertEquals(4, mutablePeriod.getWeeks());
        assertEquals(5, mutablePeriod.getDays());
        assertEquals(5, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testParseMutablePeriodFromISOPeriodString() {
        String periodString = "PT3Y2M4DT5H";
        MutablePeriod mutablePeriod = MutablePeriod.parse(periodString).toMutablePeriod();
        assertEquals(3, mutablePeriod.getYears());
        assertEquals(2, mutablePeriod.getMonths());
        assertEquals(4, mutablePeriod.getWeeks());
        assertEquals(5, mutablePeriod.getDays());
        assertEquals(5, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testTestMutablePeriodWithReadablePeriod() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        Period period = Period.standardDays(1);
        mutablePeriod.setPeriod(period);
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(1, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testTestMutablePeriodWithValuesSet() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.setYears(3);
        mutablePeriod.setMonths(2);
        mutablePeriod.setWeeks(4);
        mutablePeriod.setDays(5);
        mutablePeriod.setHours(5);
        mutablePeriod.setMinutes(25);
        mutablePeriod.setSeconds(30);
        mutablePeriod.setMillis(45);
        assertEquals(3, mutablePeriod.getYears());
        assertEquals(2, mutablePeriod.getMonths());
        assertEquals(4, mutablePeriod.getWeeks());
        assertEquals(5, mutablePeriod.getDays());
        assertEquals(5, mutablePeriod.getHours());
        assertEquals(25, mutablePeriod.getMinutes());
        assertEquals(30, mutablePeriod.getSeconds());
        assertEquals(45, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testTestMutablePeriodAdd() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        Period period = Period.standardDays(1);
        mutablePeriod.add(period);
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(1, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testTestMutablePeriodAddWithPeriod() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        Period period = Period.standardDays(1);
        mutablePeriod.add(period);
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(1, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testTestMutablePeriodAddWithValues() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.addYears(3);
        mutablePeriod.addMonths(2);
        mutablePeriod.addWeeks(4);
        mutablePeriod.addDays(5);
        mutablePeriod.addHours(5);
        mutablePeriod.addMinutes(25);
        mutablePeriod.addSeconds(30);
        mutablePeriod.addMillis(45);
        assertEquals(3, mutablePeriod.getYears());
        assertEquals(2, mutablePeriod.getMonths());
        assertEquals(4, mutablePeriod.getWeeks());
        assertEquals(5, mutablePeriod.getDays());
        assertEquals(5, mutablePeriod.getHours());
        assertEquals(25, mutablePeriod.getMinutes());
        assertEquals(30, mutablePeriod.getSeconds());
        assertEquals(45, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testTestMutablePeriodAddWithInterval() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        DateTimeZone dateTimeZone = DateTimeZone.forID("America/New_York");
        DateTime datetime1 = new DateTime(2022, 1, 1, 12, 0, 0, 0, dateTimeZone);
        DateTime datetime2 = new DateTime(2022, 1, 2, 13, 0, 0, 0, dateTimeZone);
        mutablePeriod.add(datetime1, datetime2);
        assertEquals(1, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(1, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testTestMutablePeriodMergePeriod() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        Period period = Period.standardDays(1);
        mutablePeriod.mergePeriod(period);
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(1, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testTestMutablePeriodClear() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.clear();
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testTestMutablePeriodClone() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        Period period = Period.standardDays(1);
        mutablePeriod.add(period);
        MutablePeriod clonedPeriod = mutablePeriod.copy();
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(1, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(1, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
        assertEquals(mutablePeriod.hashCode(), clonedPeriod.hashCode());
    }
}
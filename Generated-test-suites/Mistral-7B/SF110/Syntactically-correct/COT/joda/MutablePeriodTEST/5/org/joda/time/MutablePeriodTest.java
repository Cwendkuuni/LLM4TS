package org.joda.time.test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.BasicPeriod;
import org.joda.time.chronology.ISOChronology;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.field.*;

import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1D");
        assertEquals(1, period.getDays());
    }

    @Test
    public void testParseWithFormat() {
        MutablePeriod period = MutablePeriod.parse("P1D", ISOPeriodFormat.standard());
        assertEquals(1, period.getDays());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() {
        MutablePeriod.parse("InvalidFormat");
    }

    @Test
    public void testConstructorWithValues() {
        MutablePeriod period = new MutablePeriod(3, 2, 1, 4, 5, 6, 7, 8);
        assertEquals(3, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(1, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }

    @Test
    public void testConstructorWithLong() {
        MutablePeriod period = new MutablePeriod(123456789L);
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
    public void testClear() {
        MutablePeriod period = new MutablePeriod(3, 2, 1, 4, 5, 6, 7, 8);
        period.clear();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testSetValue() {
        MutablePeriod period = new MutablePeriod();
        period.setValue(DurationFieldType.years().getIndex(), 3);
        assertEquals(3, period.getYears());
    }

    @Test
    public void testSetField() {
        MutablePeriod period = new MutablePeriod();
        period.setField(DurationFieldType.years(), 3);
        assertEquals(3, period.getYears());
    }

    @Test
    public void testSetPeriod() {
        MutablePeriod period = new MutablePeriod();
        Period testPeriod = new BasicPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        period.setPeriod(testPeriod);
        assertEquals(3, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(1, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }

    @Test
    public void testSetPeriodWithInterval() {
        MutablePeriod period = new MutablePeriod();
        Instant start = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        Instant end = new DateTime(2022, 1, 1, 1, 0, 0, 0, DateTimeZone.UTC).toInstant();
        ReadableInterval testInterval = new Interval(start, end);
        period.setPeriod(testInterval);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(1, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testSetPeriodWithInstantToInstant() {
        MutablePeriod period = new MutablePeriod();
        Instant start = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        Instant end = new DateTime(2022, 1, 1, 1, 0, 0, 0, DateTimeZone.UTC).toInstant();
        period.setPeriod(start, end);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(1, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testSetPeriodWithLongToLong() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(123456789);
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
    public void testSetPeriodWithLongAndChronology() {
        MutablePeriod period = new MutablePeriod();
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        Chronology chronology = ISOChronology.getInstance();
        period.setPeriod(instant, 123456789, chronology);
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
    public void testAddField() {
        MutablePeriod period = new MutablePeriod();
        period.addField(DurationFieldType.years(), 1);
        assertEquals(1, period.getYears());
    }

    @Test
    public void testAddPeriod() {
        MutablePeriod period1 = new MutablePeriod();
        period1.setPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        MutablePeriod period2 = new MutablePeriod();
        period2.setPeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period1.addPeriod(period2);
        assertEquals(4, period1.getYears());
        assertEquals(4, period1.getMonths());
        assertEquals(4, period1.getWeeks());
        assertEquals(8, period1.getDays());
        assertEquals(11, period1.getHours());
        assertEquals(12, period1.getMinutes());
        assertEquals(13, period1.getSeconds());
        assertEquals(14, period1.getMillis());
    }

    @Test
    public void testAdd() {
        MutablePeriod period1 = new MutablePeriod();
        period1.setPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        MutablePeriod period2 = new MutablePeriod();
        period2.setPeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period1.add(period2);
        assertEquals(4, period1.getYears());
        assertEquals(4, period1.getMonths());
        assertEquals(4, period1.getWeeks());
        assertEquals(8, period1.getDays());
        assertEquals(11, period1.getHours());
        assertEquals(12, period1.getMinutes());
        assertEquals(13, period1.getSeconds());
        assertEquals(14, period1.getMillis());
    }

    @Test
    public void testAddYears() {
        MutablePeriod period = new MutablePeriod();
        period.addYears(3);
        assertEquals(3, period.getYears());
    }

    @Test
    public void testAddMonths() {
        MutablePeriod period = new MutablePeriod();
        period.addMonths(2);
        assertEquals(2, period.getMonths());
    }

    @Test
    public void testAddWeeks() {
        MutablePeriod period = new MutablePeriod();
        period.addWeeks(3);
        assertEquals(3, period.getWeeks());
    }

    @Test
    public void testAddDays() {
        MutablePeriod period = new MutablePeriod();
        period.addDays(4);
        assertEquals(4, period.getDays());
    }

    @Test
    public void testAddHours() {
        MutablePeriod period = new MutablePeriod();
        period.addHours(5);
        assertEquals(5, period.getHours());
    }

    @Test
    public void testAddMinutes() {
        MutablePeriod period = new MutablePeriod();
        period.addMinutes(6);
        assertEquals(6, period.getMinutes());
    }

    @Test
    public void testAddSeconds() {
        MutablePeriod period = new MutablePeriod();
        period.addSeconds(7);
        assertEquals(7, period.getSeconds());
    }

    @Test
    public void testAddMillis() {
        MutablePeriod period = new MutablePeriod();
        period.addMillis(8);
        assertEquals(8, period.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMergePeriodNull() {
        MutablePeriod period = new MutablePeriod();
        period.mergePeriod(null);
    }

    @Test
    public void testGetYears() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        assertEquals(3, period.getYears());
    }

    @Test
    public void testGetMonths() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        assertEquals(2, period.getMonths());
    }

    @Test
    public void testGetWeeks() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        assertEquals(1, period.getWeeks());
    }

    @Test
    public void testGetDays() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        assertEquals(4, period.getDays());
    }

    @Test
    public void testGetHours() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        assertEquals(5, period.getHours());
    }

    @Test
    public void testGetMinutes() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        assertEquals(6, period.getMinutes());
    }

    @Test
    public void testGetSeconds() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        assertEquals(7, period.getSeconds());
    }

    @Test
    public void testGetMillis() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(3, 2, 1, 4, 5, 6, 7, 8);
        assertEquals(8, period.getMillis());
    }

    @Test
    public void testSetYears() {
        MutablePeriod period = new MutablePeriod();
        period.setYears(3);
        assertEquals(3, period.getYears());
    }

    @Test
    public void testSetMonths() {
        MutablePeriod period = new MutablePeriod();
        period.setMonths(2);
        assertEquals(2, period.getMonths());
    }

    @Test
    public void testSetWeeks() {
        MutablePeriod period = new MutablePeriod();
        period.setWeeks(1);
        assertEquals(1, period.getWeeks());
    }

    @Test
    public void testSetDays() {
        MutablePeriod period = new MutablePeriod();
        period.setDays(4);
        assertEquals(4, period.getDays());
    }

    @Test
    public void testSetHours() {
        MutablePeriod period = new MutablePeriod();
        period.setHours(5);
        assertEquals(5, period.getHours());
    }

    @Test
    public void testSetMinutes() {
        MutablePeriod period = new MutablePeriod();
        period.setMinutes(6);
        assertEquals(6, period.getMinutes());
    }

    @Test
    public void testSetSeconds() {
        MutablePeriod period = new MutablePeriod();
        period.setSeconds(7);
        assertEquals(7, period.getSeconds());
    }

    @Test
    public void testSetMillis() {
        MutablePeriod period = new MutablePeriod();
        period.setMillis(8);
        assertEquals(8, period.getMillis());
    }
}
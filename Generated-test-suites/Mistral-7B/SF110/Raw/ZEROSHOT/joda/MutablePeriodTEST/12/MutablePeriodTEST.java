package org.joda.time.test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.field.*;
import org.joda.time.format.*;

import java.io.*;
import java.text.ParseException;

public class MutablePeriodTest {

    @org.junit.Test
    public void testMutablePeriodConstruction() {
        MutablePeriod mp = new MutablePeriod();
        assertNull(mp.getYears());
        assertNull(mp.getMonths());
        assertNull(mp.getWeeks());
        assertNull(mp.getDays());
        assertNull(mp.getHours());
        assertNull(mp.getMinutes());
        assertNull(mp.getSeconds());
        assertNull(mp.getMillis());

        MutablePeriod mp2 = new MutablePeriod(PeriodType.standard());
        assertNull(mp2.getYears());
        assertNull(mp2.getMonths());
        assertNull(mp2.getWeeks());
        assertNull(mp2.getDays());
        assertNull(mp2.getHours());
        assertNull(mp2.getMinutes());
        assertNull(mp2.getSeconds());
        assertNull(mp2.getMillis());

        MutablePeriod mp3 = new MutablePeriod(1, PeriodType.standard());
        assertEquals(1, mp3.getYears());
        assertNull(mp3.getMonths());
        assertNull(mp3.getWeeks());
        assertNull(mp3.getDays());
        assertNull(mp3.getHours());
        assertNull(mp3.getMinutes());
        assertNull(mp3.getSeconds());
        assertNull(mp3.getMillis());

        MutablePeriod mp4 = new MutablePeriod(1, 2, PeriodType.standard());
        assertEquals(1, mp4.getYears());
        assertEquals(2, mp4.getMonths());
        assertNull(mp4.getWeeks());
        assertNull(mp4.getDays());
        assertNull(mp4.getHours());
        assertNull(mp4.getMinutes());
        assertNull(mp4.getSeconds());
        assertNull(mp4.getMillis());

        MutablePeriod mp5 = new MutablePeriod(1, 2, 3, 4, PeriodType.standard());
        assertEquals(1, mp5.getYears());
        assertEquals(2, mp5.getMonths());
        assertEquals(3, mp5.getWeeks());
        assertEquals(4, mp5.getDays());
        assertNull(mp5.getHours());
        assertNull(mp5.getMinutes());
        assertNull(mp5.getSeconds());
        assertNull(mp5.getMillis());

        MutablePeriod mp6 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(1, mp6.getYears());
        assertEquals(2, mp6.getMonths());
        assertEquals(3, mp6.getWeeks());
        assertEquals(4, mp6.getDays());
        assertEquals(5, mp6.getHours());
        assertEquals(6, mp6.getMinutes());
        assertEquals(7, mp6.getSeconds());
        assertEquals(8, mp6.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodParse() throws ParseException {
        String input = "P1Y2M3DT4H5M6.7S";
        MutablePeriod mp = MutablePeriod.parse(input);
        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(3, mp.getWeeks());
        assertEquals(4, mp.getDays());
        assertEquals(5, mp.getHours());
        assertEquals(6, mp.getMinutes());
        assertEquals(7, mp.getSeconds());
        assertEquals(700, mp.getMillis());

        input = "P1Y2M";
        mp = MutablePeriod.parse(input);
        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(0, mp.getWeeks());
        assertEquals(0, mp.getDays());
        assertEquals(0, mp.getHours());
        assertEquals(0, mp.getMinutes());
        assertEquals(0, mp.getSeconds());
        assertEquals(0, mp.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodSetters() {
        MutablePeriod mp = new MutablePeriod();

        mp.setYears(1);
        assertEquals(1, mp.getYears());
        mp.setMonths(2);
        assertEquals(2, mp.getMonths());
        mp.setWeeks(3);
        assertEquals(3, mp.getWeeks());
        mp.setDays(4);
        assertEquals(4, mp.getDays());
        mp.setHours(5);
        assertEquals(5, mp.getHours());
        mp.setMinutes(6);
        assertEquals(6, mp.getMinutes());
        mp.setSeconds(7);
        assertEquals(7, mp.getSeconds());
        mp.setMillis(8);
        assertEquals(8, mp.getMillis());

        mp.clear();
        assertNull(mp.getYears());
        assertNull(mp.getMonths());
        assertNull(mp.getWeeks());
        assertNull(mp.getDays());
        assertNull(mp.getHours());
        assertNull(mp.getMinutes());
        assertNull(mp.getSeconds());
        assertNull(mp.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodAdd() {
        MutablePeriod mp1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        MutablePeriod mp2 = new MutablePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());

        MutablePeriod mp3 = mp1.add(mp2);
        assertEquals(3, mp3.getYears());
        assertEquals(5, mp3.getMonths());
        assertEquals(7, mp3.getWeeks());
        assertEquals(9, mp3.getDays());
        assertEquals(11, mp3.getHours());
        assertEquals(12, mp3.getMinutes());
        assertEquals(13, mp3.getSeconds());
        assertEquals(17, mp3.getMillis());

        MutablePeriod mp4 = new MutablePeriod();
        mp4.setPeriod(mp1);
        mp4.add(mp2);
        assertEquals(3, mp4.getYears());
        assertEquals(5, mp4.getMonths());
        assertEquals(7, mp4.getWeeks());
        assertEquals(9, mp4.getDays());
        assertEquals(11, mp4
>getHours());
        assertEquals(12, mp4.getMinutes());
        assertEquals(13, mp4.getSeconds());
        assertEquals(17, mp4.getMillis());

        MutablePeriod mp5 = new MutablePeriod();
        mp5.setPeriod(mp1);
        mp5.mergePeriod(mp2);
        assertEquals(3, mp5.getYears());
        assertEquals(5, mp5.getMonths());
        assertEquals(7, mp5.getWeeks());
        assertEquals(9, mp5.getDays());
        assertEquals(11, mp5.getHours());
        assertEquals(12, mp5.getMinutes());
        assertEquals(13, mp5.getSeconds());
        assertEquals(17, mp5.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodGetters() {
        MutablePeriod mp = new MutablePeriod();

        mp.setYears(1);
        assertEquals(1, mp.getYears());
        assertEquals(0, mp.getMonths());
        assertEquals(0, mp.getWeeks());
        assertEquals(0, mp.getDays());
        assertEquals(0, mp.getHours());
        assertEquals(0, mp.getMinutes());
        assertEquals(0, mp.getSeconds());
        assertEquals(0, mp.getMillis());

        mp.setMonths(2);
        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(0, mp.getWeeks());
        assertEquals(0, mp.getDays());
        assertEquals(0, mp.getHours());
        assertEquals(0, mp.getMinutes());
        assertEquals(0, mp.getSeconds());
        assertEquals(0, mp.getMillis());

        mp.setWeeks(3);
        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(3, mp.getWeeks());
        assertEquals(0, mp.getDays());
        assertEquals(0, mp.getHours());
        assertEquals(0, mp.getMinutes());
        assertEquals(0, mp.getSeconds());
        assertEquals(0, mp.getMillis());

        mp.setDays(4);
        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(3, mp.getWeeks());
        assertEquals(4, mp.getDays());
        assertEquals(0, mp.getHours());
        assertEquals(0, mp.getMinutes());
        assertEquals(0, mp.getSeconds());
        assertEquals(0, mp.getMillis());

        mp.setHours(5);
        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(3, mp.getWeeks());
        assertEquals(4, mp.getDays());
        assertEquals(5, mp.getHours());
        assertEquals(0, mp.getMinutes());
        assertEquals(0, mp.getSeconds());
        assertEquals(0, mp.getMillis());

        mp.setMinutes(6);
        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(3, mp.getWeeks());
        assertEquals(4, mp.getDays());
        assertEquals(5, mp.getHours());
        assertEquals(6, mp.getMinutes());
        assertEquals(0, mp.getSeconds());
        assertEquals(0, mp.getMillis());

        mp.setSeconds(7);
        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(3, mp.getWeeks());
        assertEquals(4, mp.getDays());
        assertEquals(5, mp.getHours());
        assertEquals(6, mp.getMinutes());
        assertEquals(7, mp.getSeconds());
        assertEquals(0, mp.getMillis());

        mp.setMillis(8);
        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(3, mp.getWeeks());
        assertEquals(4, mp.getDays());
        assertEquals(5, mp.getHours());
        assertEquals(6, mp.getMinutes());
        assertEquals(7, mp.getSeconds());
        assertEquals(8, mp.getMillis());
    }
}
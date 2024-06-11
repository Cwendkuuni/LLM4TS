package org.joda.time.test; //Assuming you have a test package "org.joda.time.test"

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.format.*;
import org.joda.time.field.*;
import org.joda.convert.*;

public class MutablePeriodTest {

    @Test
    public void testMutablePeriodCreation() {
        MutablePeriod mp = new MutablePeriod();
        MutablePeriod mp1 = new MutablePeriod(PeriodType.standard());
        MutablePeriod mp2 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        MutablePeriod mp3 = new MutablePeriod(123456789L);
        MutablePeriod mp4 = new MutablePeriod(123456789L, PeriodType.standard());
        MutablePeriod mp5 = new MutablePeriod(123456789L, DateTimeUtils.getChronology(DateTimeUtils.UTC));
        MutablePeriod mp6 = new MutablePeriod(123456789L, PeriodType.standard(), DateTimeUtils.getChronology(DateTimeUtils.UTC));

        MutablePeriod mp7 = new MutablePeriod(-123456789L);
        MutablePeriod mp8 = new MutablePeriod(-123456789L, PeriodType.standard());
        MutablePeriod mp9 = new MutablePeriod(-123456789L, DateTimeUtils.getChronology(DateTimeUtils.UTC));
        MutablePeriod mp10 = new MutablePeriod(-123456789L, PeriodType.standard(), DateTimeUtils.getChronology(DateTimeUtils.UTC));

        MutablePeriod mp11 = new MutablePeriod(new Instant(), new Instant());
        MutablePeriod mp12 = new MutablePeriod(new Instant(), new Instant(), PeriodType.standard());
        MutablePeriod mp13 = new MutablePeriod(new Instant(), new Duration(123456789L), null);
        MutablePeriod mp14 = new MutablePeriod(new Instant(), new Duration(123456789L), PeriodType.standard());
        MutablePeriod mp15 = new MutablePeriod(new Duration(123456789L), new Instant());
        MutablePeriod mp16 = new MutablePeriod(new Duration(123456789L), new Instant(), PeriodType.standard());

        MutablePeriod mp17 = new MutablePeriod("1Y");
        MutablePeriod mp18 = new MutablePeriod("1Y", ISOPeriodFormat.standard());
        MutablePeriod mp19 = new MutablePeriod("PT1Y");
        MutablePeriod mp20 = new MutablePeriod("PT1Y", ISOPeriodFormat.standard());
    }

    @Test
    public void testMutablePeriodSetters() {
        MutablePeriod mp = new MutablePeriod();

        mp.setValues(new int[10]);
        mp.setValue(0, 1);
        mp.set(DurationFieldType.years(), 2);
        mp.set(DurationFieldType.months(), 3);
        mp.set(DurationFieldType.weeks(), 4);
        mp.set(DurationFieldType.days(), 5);
        mp.set(DurationFieldType.hours(), 6);
        mp.set(DurationFieldType.minutes(), 7);
        mp.set(DurationFieldType.seconds(), 8);
        mp.set(DurationFieldType.millis(), 9);

        assertEquals(1, mp.getValue(0));
        assertEquals(2, mp.getYears());
        assertEquals(3, mp.getMonths());
        assertEquals(4, mp.getWeeks());
        assertEquals(5, mp.getDays());
        assertEquals(6, mp.getHours());
        assertEquals(7, mp.getMinutes());
        assertEquals(8, mp.getSeconds());
        assertEquals(9, mp.getMillis());
    }

    @Test
    public void testMutablePeriodSetPeriod() {
        MutablePeriod mp = new MutablePeriod();

        ReadablePeriod pr = new GeneralPeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        mp.setPeriod(pr);

        MutablePeriod mp1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        mp.setPeriod(mp1);

        ReadableInterval ri = new Duration(123456789L, PeriodType.standard());
        mp.setPeriod(ri);

        ReadableInstant ri1 = new DateTime(System.currentTimeMillis());
        ReadableInstant ri2 = new DateTime(System.currentTimeMillis() + 1000L);
        mp.setPeriod(ri1, ri2);

        long durationMillis = 123456789L;
        mp.setPeriod(durationMillis);

        Chronology chronology = DateTimeUtils.getChronology(DateTimeUtils.UTC);
        mp.setPeriod(durationMillis, chronology);

        ReadableDuration rd = Duration.standardMillis(123456789L);
        mp.setPeriod(rd);

        mp.setPeriod(rd, chronology);
    }

    @Test
    public void testMutablePeriodAdd() {
        MutablePeriod mp = new MutablePeriod();

        mp.add(DurationFieldType.years(), 1);
        mp.add(DurationFieldType.months(), 2);
        mp.add(DurationFieldType.weeks(), 3);
        mp.add(DurationFieldType.days(), 4);
        mp.add(DurationFieldType.hours(), 5);
        mp.add(DurationFieldType.minutes(), 6);
        mp.add(DurationFieldType.seconds(), 7);
        mp.add(DurationFieldType.millis(), 8);

        MutablePeriod mp1 = new MutablePeriod();
        mp1.setPeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        mp.add(mp1);

        ReadablePeriod pr = new GeneralPeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        mp.add(pr);

        long durationMillis = 123456789L;
        mp.add(durationMillis);

        Chronology chronology = DateTimeUtils.getChronology(DateTimeUtils.UTC);
        mp.add(durationMillis, chronology);

        ReadableDuration rd = Duration.standardMillis(123456789L);
        mp.add(rd);

        mp.add(rd, chronology);

        assertEquals(2, mp.getYears());
        assertEquals(4, mp.getMonths());
        assertEquals(7, mp.getWeeks());
        assertEquals(13, mp.getDays());
        assertEquals(11, mp.getHours());
        assertEquals(12, mp.getMinutes());
        assertEquals(16, mp.getSeconds());
        assertEquals(24, mp.getMillis());
    }

    @Test
    public void testMutablePeriodMergePeriod() {
        MutablePeriod mp = new MutablePeriod();

        MutablePeriod mp1 = new MutablePeriod();
        mp1.setPeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());

        MutablePeriod mp2 = new MutablePeriod();
        mp2.setPeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());

        mp.mergePeriod(mp1);
        mp.mergePeriod(mp2);

        assertEquals(3, mp.getYears());
        assertEquals(7, mp.getMonths());
        assertEquals(7, mp.getWeeks());
        assertEquals(13, mp.getDays());
        assertEquals(11, mp.getHours());
        assertEquals(12, mp.getMinutes());
        assertEquals(16, mp.getSeconds());
        assertEquals(24, mp.getMillis());
    }

    @Test
    public void testMutablePeriodGetters() {
        MutablePeriod mp = new MutablePeriod();

        mp.setPeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());

        assertEquals(1, mp.getValue(DurationFieldType.years().getIndex()));
        assertEquals(2, mp.getValue(DurationFieldType.months().getIndex()));
        assertEquals(3, mp.getValue(DurationFieldType.weeks().getIndex()));
        assertEquals(4, mp.getValue(DurationFieldType.days().getIndex()));
        assertEquals(5, mp.getValue(DurationFieldType.hours().getIndex()));
        assertEquals(6, mp.getValue(DurationFieldType.minutes().getIndex()));
        assertEquals(7, mp.getValue(DurationFieldType.seconds().getIndex()));
        assertEquals(8, mp.getValue(DurationFieldType.millis().getIndex()));

        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(3, mp.getWeeks());
        assertEquals(4, mp.getDays());
        assertEquals(5, mp.getHours());
        assertEquals(6, mp.getMinutes());
        assertEquals(7, mp.getSeconds());
        assertEquals(8, mp.getMillis());
    }

    @Test
    public void testMutablePeriodClone() {
        MutablePeriod mp = new MutablePeriod();
        mp.setPeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());

        MutablePeriod mp1 = (MutablePeriod)mp.clone();

        assertEquals(mp.toString(), mp1.toString());
        assertEquals(mp.hashCode(), mp1.hashCode());

        mp.add(DurationFieldType.years(), 1);
        assertNotEquals(mp.toString(), mp1.toString());
    }
}
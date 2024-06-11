package org.joda.time.test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.format.*;

import java.io.*;
import java.text.ParseException;

public class MutablePeriodTest {

    @org.junit.Test
    public void testMutablePeriodConstruction() {
        // Test default constructor
        MutablePeriod mp = new MutablePeriod();
        assertEquals(0, mp.getYears());
        assertEquals(0, mp.getMonths());
        assertEquals(0, mp.getWeeks());
        assertEquals(0, mp.getDays());
        assertEquals(0, mp.getHours());
        assertEquals(0, mp.getMinutes());
        assertEquals(0, mp.getSeconds());
        assertEquals(0, mp.getMillis());

        // Test constructor with PeriodType
        MutablePeriod mp2 = new MutablePeriod(PeriodType.standard());
        assertEquals(0, mp2.getYears());
        assertEquals(0, mp2.getMonths());
        assertEquals(0, mp2.getWeeks());
        assertEquals(0, mp2.getDays());
        assertEquals(0, mp2.getHours());
        assertEquals(0, mp2.getMinutes());
        assertEquals(0, mp2.getSeconds());
        assertEquals(0, mp2.getMillis());

        // Test constructors with various arguments
        MutablePeriod mp3 = new MutablePeriod(1, 2, 3, 4);
        MutablePeriod mp4 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod mp5 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        MutablePeriod mp6 = new MutablePeriod(1234567890123L);
        MutablePeriod mp7 = new MutablePeriod(1234567890123L, PeriodType.standard());
        MutablePeriod mp8 = new MutablePeriod(new DateTime(2022, 1, 1), new DateTime(2022, 1, 10));
        MutablePeriod mp9 = new MutablePeriod(new DateTime(2022, 1, 1), new Duration(1, TimeUnit.HOURS), PeriodType.standard());
        MutablePeriod mp10 = new MutablePeriod(new Duration(1, TimeUnit.DAYS), new DateTime(2022, 1, 1));
        MutablePeriod mp11 = new MutablePeriod(new Duration(1234567890123L), PeriodType.standard());

        // Test parsing a string
        MutablePeriod mp12 = MutablePeriod.parse("P3Y2M1W3D4H5M6S7MS8");
        MutablePeriod mp13 = MutablePeriod.parse("PT3H2M");
        MutablePeriod mp14 = MutablePeriod.parse("P1Y2M3W4D5H6M7S8MS9", ISOPeriodFormat.standard());
    }

    @org.junit.Test
    public void testMutablePeriodGetters() {
        MutablePeriod mp = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);

        assertEquals(1, mp.getYears());
        assertEquals(2, mp.getMonths());
        assertEquals(3, mp.getWeeks());
        assertEquals(4, mp.getDays());
        assertEquals(5, mp.getHours());
        assertEquals(6, mp.getMinutes());
        assertEquals(7, mp.getSeconds());
        assertEquals(8, mp.getMillis());

        // Test negative values
        MutablePeriod mpNeg = new MutablePeriod(-1, -2, -3, -4, -5, -6, -7, -8);
        assertEquals(-1, mpNeg.getYears());
        assertEquals(-2, mpNeg.getMonths());
        assertEquals(-3, mpNeg.getWeeks());
        assertEquals(-4, mpNeg.getDays());
        assertEquals(-5, mpNeg.getHours());
        assertEquals(-6, mpNeg.getMinutes());
        assertEquals(-7, mpNeg.getSeconds());
        assertEquals(-8, mpNeg.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodSetters() {
        MutablePeriod mp = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);

        // Test setting individual fields
        mp.setYears(2);
        mp.setMonths(3);
        mp.setWeeks(4);
        mp.setDays(5);
        mp.setHours(6);
        mp.setMinutes(7);
        mp.setSeconds(8);
        mp.setMillis(9);

        assertEquals(2, mp.getYears());
        assertEquals(3, mp.getMonths());
        assertEquals(4, mp.getWeeks());
        assertEquals(5, mp.getDays());
        assertEquals(6, mp.getHours());
        assertEquals(7, mp.getMinutes());
        assertEquals(8, mp.getSeconds());
        assertEquals(9, mp.getMillis());

        // Test setting entire period
        MutablePeriod mp2 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        mp.setPeriod(mp2);

        assertEquals(2, mp.getYears());
        assertEquals(3, mp.getMonths());
        assertEquals(4, mp.getWeeks());
        assertEquals(5, mp.getDays());
        assertEquals(6, mp.getHours());
        assertEquals(7, mp.getMinutes());
        assertEquals(8, mp.getSeconds());
        assertEquals(8, mp.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodAddition() {
        MutablePeriod mp1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod mp2 = new MutablePeriod(2, 3, 4, 5, 6, 7, 8, 9);

        // Test addition with another MutablePeriod
        MutablePeriod mp3 = mp1.add(mp2);
        assertEquals(3, mp3.getYears());
        assertEquals(5, mp3.getMonths());
        assertEquals(7, mp3.getWeeks());
        assertEquals(9, mp3.getDays());
        assertEquals(11, mp3.getHours());
        assertEquals(13, mp3.getMinutes());
        assertEquals(14, mp3.getSeconds());
        assertEquals(9, mp3.getMillis());

        // Test addition with a Duration
        MutablePeriod mp4 = mp1.add(new Duration(1, TimeUnit.DAYS));
        assertEquals(2, mp4.getYears());
        assertEquals(3, mp4.getMonths());
        assertEquals(4, mp4.getWeeks());
        assertEquals(5, mp4.getDays());
        assertEquals(6, mp4.getHours());
        assertEquals(7, mp4.getMinutes());
        assertEquals(8, mp4.getSeconds());
        assertEquals(9, mp4.getMillis());

        // Test adding a negative duration
        MutablePeriod mp5 = mp1.add(new Duration(-1, TimeUnit.DAYS));
        assertEquals(0, mp5.getYears());
        assertEquals(2, mp5.getMonths());
        assertEquals(3, mp5.getWeeks());
        assertEquals(4, mp5.getDays());
        assertEquals(5, mp5.getHours());
        assertEquals(6, mp5.getMinutes());
        assertEquals(7, mp5.getSeconds());
        assertEquals(25, mp5.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodMerging() {
        MutablePeriod mp1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod mp2 = new MutablePeriod(2, 3, 4, 5, 6, 7, 8, 9);

        // Test merging with another MutablePeriod
        mp1.mergePeriod(mp2);
        assertEquals(3, mp1.getYears());
        assertEquals(5, mp1.getMonths());
        assertEquals(7, mp1.getWeeks());
        assertEquals(9, mp1.getDays());
        assertEquals(11, mp1.getHours());
        assertEquals(13, mp1.getMinutes());
        assertEquals(14, mp1.getSeconds());
        assertEquals(9, mp1.getMillis());

        // Test merging with a ReadablePeriod
        MutablePeriod mp3 = new MutablePeriod();
        mp3.setPeriod(mp1);
        mp3.mergePeriod(mp2);
        assertEquals(3, mp3.getYears());
        assertEquals(5, mp3.getMonths());
        assertEquals(7, mp3.getWeeks());
        assertEquals(9, mp3.getDays());
        assertEquals(11, mp3.getHours());
        assertEquals(13, mp3.getMinutes());
        assertEquals(14, mp3.getSeconds());
        assertEquals(9, mp3.getMillis());
    }

    @org.junit.Test
    public void testMutablePeriodParsing() throws ParseException {
        String periodString1 = "P3Y2M1W3D4H5M6S7MS8";
        String periodString2 = "PT3H2M";
        String periodString3 = "P1Y2M3W4D5H6M7S8MS9";

        MutablePeriod mp1 = MutablePeriod.parse(periodString1);
        MutablePeriod mp2 = MutablePeriod.parse(periodString2, ISOPeriodFormat.standard());
        MutablePeriod mp3 = MutablePeriod.parse(periodString3, ISOPeriodFormat.standard());

        assertEquals(3, mp1.getYears());
        assertEquals(2, mp1.getMonths());
        assertEquals(1, mp1.getWeeks());
        assertEquals(3, mp1.getDays());
        assertEquals(5, mp1.getHours());
        assertEquals(6, mp1.getMinutes());
        assertEquals(7, mp1.getSeconds());
        assertEquals(8, mp1.getMillis());

        assertEquals(0, mp2.getYears());
        assertEquals(0, mp2.getMonths());
        assertEquals(0, mp2.getWeeks());
        assertEquals(0, mp2.getDays());
        assertEquals(3, mp2.getHours());
        assertEquals(2, mp2.getMinutes());

        assertEquals(1, mp3.getYears());
        assertEquals(2, mp3.getMonths());
        assertEquals(3, mp3.getWeeks());
        assertEquals(4, mp3.getDays());
        assertEquals(5, mp3.getHours());
        assertEquals(6, mp3.getMinutes());
        assertEquals(7, mp3.getSeconds());
        assertEquals(8, mp3.getMillis());
        assertEquals(9, mp3.getMillisOfSecond());
    }

    @org.junit.Test
    public void testMutablePeriodSerialization() throws IOException {
        MutablePeriod mp = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutput output = new DataOutputStream(byteStream);
        BinarySerializer serializer = new BinarySerializer();
        serializer.serialize(mp, output);
        output.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        DataInput dataInput = new DataInputStream(byteInput);
        MutablePeriod mpDeserialized = (MutablePeriod) serializer.deserialize(MutablePeriod.class, dataInput);

        assertEquals(1, mpDeserialized.getYears());
        assertEquals(2, mpDeserialized.getMonths());
        assertEquals(3, mpDeserialized.getWeeks());
        assertEquals(4, mpDeserialized.getDays());
        assertEquals(5, mpDeserialized.getHours());
        assertEquals(6, mpDeserialized.getMinutes());
        assertEquals(7, mpDeserialized.getSeconds());
        assertEquals(8, mpDeserialized.getMillis());
    }
}
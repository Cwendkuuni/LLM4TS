package org.joda.time.test;

import static org.junit.Assert.*;

import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.base.Defaults;
import org.joda.time.chronology.ISOChronology;
import org.joda.time.format.ISOPeriodFormat;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;

public class MutablePeriodTest {

    @org.junit.Test
    public void testMutablePeriodParsing() {
        String periodString = "P3Y2M1DT4H30M";
        MutablePeriod parsedPeriod = MutablePeriod.parse(periodString);
        assertEquals(3, parsedPeriod.getYears());
        assertEquals(2, parsedPeriod.getMonths());
        assertEquals(1, parsedPeriod.getDays());
        assertEquals(4, parsedPeriod.getHours());
        assertEquals(30, parsedPeriod.getMinutes());

        String invalidPeriodString = "P3Y2M1DT4H30M3";
        try {
            MutablePeriod.parse(invalidPeriodString);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        String periodStringWithChronology = "P3Y2M1DT4H30M in America/Los_Angeles";
        MutablePeriod parsedPeriodWithChronology = MutablePeriod.parse(periodStringWithChronology, ISOPeriodFormat.standard().withChronology(ISOChronology.getInstance("America/Los_Angeles")));
        assertEquals(3, parsedPeriodWithChronology.getYears());
        assertEquals(2, parsedPeriodWithChronology.getMonths());
        assertEquals(1, parsedPeriodWithChronology.getDays());
        assertEquals(4, parsedPeriodWithChronology.getHours());
        assertEquals(30, parsedPeriodWithChronology.getMinutes());
        assertEquals(ISOChronology.getInstance("America/Los_Angeles"), parsedPeriodWithChronology.getChronology());
    }

    @org.junit.Test
    public void testMutablePeriodConstruction() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.clear();

        MutablePeriod periodicYear = new MutablePeriod(PeriodType.YEARS);
        MutablePeriod periodicMonth = new MutablePeriod(PeriodType.MONTHS);
        MutablePeriod periodicWeek = new MutablePeriod(PeriodType.WEEKS);
        MutablePeriod periodicDay = new MutablePeriod(PeriodType.DAYS);
        MutablePeriod periodicHour = new MutablePeriod(PeriodType.HOURS);
        MutablePeriod periodicMinute = new MutablePeriod(PeriodType.MINUTES);
        MutablePeriod periodicSecond = new MutablePeriod(PeriodType.SECONDS);
        MutablePeriod periodicMilli = new MutablePeriod(PeriodType.MILLISECONDS);

        MutablePeriod mutablePeriodWithValues = new MutablePeriod(3, 2, 1, 4, 30, 30, 30, 30, PeriodType.standard());

        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());

        mutablePeriod.setValue(DurationFieldType.years().getIndex(), 3);
        mutablePeriod.setValue(DurationFieldType.months().getIndex(), 2);
        mutablePeriod.setValue(DurationFieldType.weeks().getIndex(), 1);
        mutablePeriod.setValue(DurationFieldType.days().getIndex(), 4);
        mutablePeriod.setValue(DurationFieldType.hours().getIndex(), 30);
        mutablePeriod.setValue(DurationFieldType.minutes().getIndex(), 30);
        mutablePeriod.setValue(DurationFieldType.seconds().getIndex(), 30);
        mutablePeriod.setValue(DurationFieldType.millis().getIndex(), 30);

        assertEquals(3, mutablePeriod.getYears());
        assertEquals(2, mutablePeriod.getMonths());
        assertEquals(1, mutablePeriod.getWeeks());
        assertEquals(4, mutablePeriod.getDays());
        assertEquals(30, mutablePeriod.getHours());
        assertEquals(30, mutablePeriod.getMinutes());
        assertEquals(30, mutablePeriod.getSeconds());
        assertEquals(30, mutablePeriod.getMillis());

        assertEquals(mutablePeriod, mutablePeriodWithValues);

        mutablePeriod.add(new Period(2, PeriodType.YEARS));
        mutablePeriod.add(new Period(1, PeriodType.MONTHS));
        mutablePeriod.add(new Period(2, PeriodType.WEEKS));
        mutablePeriod.add(new Period(1, PeriodType.DAYS));
        mutablePeriod.add(new Period(1, PeriodType.HOURS));
        mutablePeriod.add(new Period(30, PeriodType.MINUTES));
        mutablePeriod.add(new Period(30, PeriodType.SECONDS));
        mutablePeriod.add(new Period(30, PeriodType.MILLISECONDS));

        assertEquals(5, mutablePeriod.getYears());
        assertEquals(3, mutablePeriod.getMonths());
        assertEquals(3, mutablePeriod.getWeeks());
        assertEquals(5, mutablePeriod.getDays());
        assertEquals(41, mutablePeriod.getHours());
        assertEquals(60 * 30 + 30, mutablePeriod.getMinutes());
        assertEquals(60 * 30 * 30 + 60 * 30 + 30, mutablePeriod.getSeconds());
        assertEquals(60 * 60 * 60 * 30 * 30 + 60 * 60 * 30 * 30 + 60 * 30 * 30, mutablePeriod.getMillis());

        // Test with chronology
        MutablePeriod mutablePeriodWithChronology = new MutablePeriod(3, 2, 1, 4, 30, 30, 30, 30, PeriodType.standard(), ISOChronology.getInstance("America/Los_Angeles"));
        assertEquals(ISOChronology.getInstance("America/Los_Angeles"), mutablePeriodWithChronology.getChronology());
    }

    @org.junit.Test
    public void testMutablePeriodConversion() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.setYears(3);
        mutablePeriod.setMonths(2);
        mutablePeriod.setDays(1);
        mutablePeriod.setHours(4);
        mutablePeriod.setMinutes(30);
        mutablePeriod.setSeconds(30);
        mutablePeriod.setMillis(30);

        StringWriter stringWriter = new StringWriter();
        StringReader stringReader = new StringReader(ISOPeriodFormat.standard().print(mutablePeriod, stringWriter));
        MutablePeriod parsedPeriod = MutablePeriod.parse(stringReader);

        assertEquals(mutablePeriod, parsedPeriod);

        String stringRepresentation = stringWriter.toString();
        assertEquals("P3Y2M1DT4H30M30S", stringRepresentation);

        // Test with chronology
        MutablePeriod mutablePeriodWithChronology = new MutablePeriod(3, 2, 1, 4, 30, 30, 30, 30, PeriodType.standard(), ISOChronology.getInstance("America/Los_Angeles"));
        StringWriter stringWriterWithChronology = new StringWriter();
        StringReader stringReaderWithChronology = new StringReader(ISOPeriodFormat.standard().withChronology(ISOChronology.getInstance("America/Los_Angeles")).print(mutablePeriodWithChronology, stringWriterWithChronology));
        MutablePeriod parsedPeriodWithChronology = MutablePeriod.parse(stringReaderWithChronology);

        assertEquals(mutablePeriodWithChronology, parsedPeriodWithChronology);

        String stringRepresentationWithChronology = stringWriterWithChronology.toString();
        assertEquals("P3Y2M1DT4H30M30S in America/Los_Angeles", stringRepresentationWithChronology);
    }
}
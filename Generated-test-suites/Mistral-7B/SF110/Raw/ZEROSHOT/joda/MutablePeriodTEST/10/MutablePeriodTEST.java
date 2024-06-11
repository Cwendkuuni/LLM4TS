package org.joda.time.test;

import static org.junit.Assert.*;

import org.joda.time.BasePeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodField;
import org.joda.time.PeriodType;
import org.joda.time.format.ISOPeriodFormat;

import java.io.StringReader;
import java.text.ParseException;

public class MutablePeriodTest {

    @org.junit.Test
    public void testParse() {
        String periodString = "P3Y2M4D1W";
        MutablePeriod mutablePeriod = MutablePeriod.parse(periodString);

        assertEquals("Wrong number of years", 3, mutablePeriod.getYears());
        assertEquals("Wrong number of months", 2, mutablePeriod.getMonths());
        assertEquals("Wrong number of days", 4, mutablePeriod.getDays());
        assertEquals("Wrong number of weeks", 1, mutablePeriod.getWeeks());

        PeriodType periodType = PeriodType.standard();
        MutablePeriod parsedPeriod = MutablePeriod.parse(periodString, ISOPeriodFormat.standard());
        assertEquals("Parsed incorrectly", mutablePeriod, parsedPeriod);
    }

    @org.junit.Test
    public void testEmptyConstructor() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        assertEquals("Default period should be zero", 0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testBasicConstructor() {
        MutablePeriod mutablePeriod = new MutablePeriod(5, 2, 1, 3, 4, 5, 6, 7);
        assertEquals("Constructor did not set values correctly", 5, mutablePeriod.getYears());
        assertEquals("Constructor did not set values correctly", 2, mutablePeriod.getMonths());
        assertEquals("Constructor did not set values correctly", 1, mutablePeriod.getWeeks());
        assertEquals("Constructor did not set values correctly", 3, mutablePeriod.getDays());
        assertEquals("Constructor did not set values correctly", 4, mutablePeriod.getHours());
        assertEquals("Constructor did not set values correctly", 5, mutablePeriod.getMinutes());
        assertEquals("Constructor did not set values correctly", 6, mutablePeriod.getSeconds());
        assertEquals("Constructor did not set values correctly", 7, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testFromMillis() {
        MutablePeriod mutablePeriod = new MutablePeriod(123456789L);
        assertEquals("Period created from millis did not have the expected values", 1, mutablePeriod.getYears());
        assertEquals("Period created from millis did not have the expected values", 2, mutablePeriod.getMonths());
        assertEquals("Period created from millis did not have the expected values", 3, mutablePeriod.getWeeks());
        assertEquals("Period created from millis did not have the expected values", 4, mutablePeriod.getDays());
        assertEquals("Period created from millis did not have the expected values", 5, mutablePeriod.getHours());
        assertEquals("Period created from millis did not have the expected values", 6, mutablePeriod.getMinutes());
        assertEquals("Period created from millis did not have the expected values", 7, mutablePeriod.getSeconds());
        assertEquals("Period created from millis did not have the expected values", 8, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testSettingValues() {
        MutablePeriod mutablePeriod = new MutablePeriod();

        mutablePeriod.setValue(PeriodField.years(), 5);
        mutablePeriod.setValue(PeriodField.months(), 2);
        mutablePeriod.setValue(PeriodField.weeks(), 1);
        mutablePeriod.setValue(PeriodField.days(), 3);
        mutablePeriod.setValue(PeriodField.hours(), 4);
        mutablePeriod.setValue(PeriodField.minutes(), 5);
        mutablePeriod.setValue(PeriodField.seconds(), 6);
        mutablePeriod.setValue(PeriodField.millis(), 7);

        assertEquals("Values not set correctly", 5, mutablePeriod.getYears());
        assertEquals("Values not set correctly", 2, mutablePeriod.getMonths());
        assertEquals("Values not set correctly", 1, mutablePeriod.getWeeks());
        assertEquals("Values not set correctly", 3, mutablePeriod.getDays());
        assertEquals("Values not set correctly", 4, mutablePeriod.getHours());
        assertEquals("Values not set correctly", 5, mutablePeriod.getMinutes());
        assertEquals("Values not set correctly", 6, mutablePeriod.getSeconds());
        assertEquals("Values not set correctly", 7, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testSettingFields() {
        MutablePeriod mutablePeriod = new MutablePeriod();

        mutablePeriod.setField(DurationFieldType.years(), 5);
        mutablePeriod.setField(DurationFieldType.months(), 2);
        mutablePeriod.setField(DurationFieldType.weeks(), 1);
        mutablePeriod.setField(DurationFieldType.days(), 3);
        mutablePeriod.setField(DurationFieldType.hours(), 4);
        mutablePeriod.setField(DurationFieldType.minutes(), 5);
        mutablePeriod.setField(DurationFieldType.seconds(), 6);
        mutablePeriod.setField(DurationFieldType.millis(), 7);

        assertEquals("Fields not set correctly", 5, mutablePeriod.getYears());
        assertEquals("Fields not set correctly", 2, mutablePeriod.getMonths());
        assertEquals("Fields not set correctly", 1, mutablePeriod.getWeeks());
        assertEquals("Fields not set correctly", 3, mutablePeriod.getDays());
        assertEquals("Fields not set correctly", 4, mutablePeriod.getHours());
        assertEquals("Fields not set correctly", 5, mutablePeriod.getMinutes());
        assertEquals("Fields not set correctly", 6, mutablePeriod.getSeconds());
        assertEquals("Fields not set correctly", 7, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testSettingPeriod() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        Period period = new Period(5, 2, 1, 3, 4, 5, 6, 7);
        mutablePeriod.setPeriod(period);

        assertEquals("Period not set correctly", 5, mutablePeriod.getYears());
        assertEquals("Period not set correctly", 2, mutablePeriod.getMonths());
        assertEquals("Period not set correctly", 1, mutablePeriod.getWeeks());
        assertEquals("Period not set correctly", 3, mutablePeriod.getDays());
        assertEquals("Period not set correctly", 4, mutablePeriod.getHours());
        assertEquals("Period not set correctly", 5, mutablePeriod.getMinutes());
        assertEquals("Period not set correctly", 6, mutablePeriod.getSeconds());
        assertEquals("Period not set correctly", 7, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testAddingPeriods() {
        MutablePeriod base = new MutablePeriod();
        MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod period2 = new MutablePeriod(2, 3, 4, 5, 6, 7, 8, 9);

        base.addPeriod(period1);
        base.addPeriod(period2);

        assertEquals("Adding periods did not work correctly", 3, base.getYears());
        assertEquals("Adding periods did not work correctly", 5, base.getMonths());
        assertEquals("Adding periods did not work correctly", 7, base.getWeeks());
        assertEquals("Adding periods did not work correctly", 7, base.getDays());
        assertEquals("Adding periods did not work correctly", 11, base.getHours());
        assertEquals("Adding periods did not work correctly", 13, base.getMinutes());
        assertEquals("Adding periods did not work correctly", 14, base.getSeconds());
        assertEquals("Adding periods did not work correctly", 17, base.getMillis());
    }

    @org.junit.Test
    public void testAddingMillis() {
        MutablePeriod mutablePeriod = new MutablePeriod();

        mutablePeriod.add(DurationFieldType.millis(), 1);
        assertEquals("Adding millis did not work correctly", 1, mutablePeriod.getMillis());

        mutablePeriod.add(DurationFieldType.millis(), 1000);
        assertEquals("Adding millis did not work correctly", 1001, mutablePeriod.getMillis());

        mutablePeriod.add(DurationFieldType.millis(), 86400000);
        assertEquals("Adding millis did not work correctly", 86400001, mutablePeriod.getMillis());

        mutablePeriod.add(DurationFieldType.millis(), 525600000);
        assertEquals("Adding millis did not work correctly", 525600001, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testAddingYears() {
        MutablePeriod mutablePeriod = new MutablePeriod();

        mutablePeriod.addYears(1);
        assertEquals("Adding years did not work correctly", 1, mutablePeriod.getYears());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getMonths());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getWeeks());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getDays());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getHours());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getMinutes());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getSeconds());
        assertEquals("Adding years did not work correctly", 86400000, mutablePeriod.getMillis());

        mutablePeriod.addYears(2);
        assertEquals("Adding years did not work correctly", 3, mutablePeriod.getYears());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getMonths());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getWeeks());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getDays());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getHours());
        assertEquals("Adding years did not work correctly", 0, mutablePeriod.getMinutes());
        assertEquals("Adding years did not work correctly", 1728000000, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testMergePeriods() {
        MutablePeriod base = new MutablePeriod();
        MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod period2 = new MutablePeriod(2, 3, 4, 5, 6, 7, 8, 9);

        base.mergePeriod(period1);
        base.mergePeriod(period2);

        assertEquals("Merging periods did not work correctly", 3, base.getYears());
        assertEquals("Merging periods did not work correctly", 5, base.getMonths());
        assertEquals("Merging periods did not work correctly", 7, base.getWeeks());
        assertEquals("Merging periods did not work correctly", 7, base.getDays());
        assertEquals("Merging periods did not work correctly", 11, base.getHours());
        assertEquals("Merging periods did not work correctly", 13, base.getMinutes());
        assertEquals("Merging periods did not work correctly", 14, base.getSeconds());
        assertEquals("Merging periods did not work correctly", 17, base.getMillis());
    }

    @org.junit.Test
    public void testParsingFromStream() throws ParseException {
        String periodString = "P3Y2M4D1W";
        PeriodFormat format = ISOPeriodFormat.standard();
        StringReader reader = new StringReader(periodString);
        MutablePeriod parsedPeriod = format.parsePeriod(reader);

        assertEquals("Parsed incorrectly", 3, parsedPeriod.getYears());
        assertEquals("Parsed incorrectly", 2, parsedPeriod.getMonths());
        assertEquals("Parsed incorrectly", 1, parsedPeriod.getWeeks());
        assertEquals("Parsed incorrectly", 4, parsedPeriod.getDays());
        assertEquals("Parsed incorrectly", 0, parsedPeriod.getHours());
        assertEquals("Parsed incorrectly", 0, parsedPeriod.getMinutes());
        assertEquals("Parsed incorrectly", 0, parsedPeriod.getSeconds());
        assertEquals("Parsed incorrectly", 0, parsedPeriod.getMillis());
    }

    @org.junit.Test
    public void testClone() {
        MutablePeriod mutablePeriod = new MutablePeriod(5, 2, 1, 3, 4, 5, 6, 7);
        MutablePeriod clonedPeriod = (MutablePeriod) mutablePeriod.clone();

        assertEquals("Cloned incorrectly", 5, clonedPeriod.getYears());
        assertEquals("Cloned incorrectly", 2, clonedPeriod.getMonths());
        assertEquals("Cloned incorrectly", 1, clonedPeriod.getWeeks());
        assertEquals("Cloned incorrectly", 3, clonedPeriod.getDays());
        assertEquals("Cloned incorrectly", 4, clonedPeriod.getHours());
        assertEquals("Cloned incorrectly", 5, clonedPeriod.getMinutes());
        assertEquals("Cloned incorrectly", 6, clonedPeriod.getSeconds());
        assertEquals("Cloned incorrectly", 7, clonedPeriod.getMillis());

        mutablePeriod.add(DurationFieldType.years(), 1);
        assertEquals("Cloned incorrectly", 5, clonedPeriod.getYears());
        assertEquals("Cloned incorrectly", 2, clonedPeriod.getMonths());
        assertEquals("Cloned incorrectly", 1, clonedPeriod.getWeeks());
        assertEquals("Cloned incorrectly", 3, clonedPeriod.getDays());
        assertEquals("Cloned incorrectly", 4, clonedPeriod.getHours());
        assertEquals("Cloned incorrectly", 5, clonedPeriod.getMinutes());
        assertEquals("Cloned incorrectly", 6, clonedPeriod.getSeconds());
        assertEquals("Cloned incorrectly", 7, clonedPeriod.getMillis());
    }
}
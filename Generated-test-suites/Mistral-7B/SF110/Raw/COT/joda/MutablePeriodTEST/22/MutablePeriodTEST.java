package org.junit.test;

import static org.junit.Assert.*;

import org.joda.time.MutablePeriod;
import org.joda.time.base.PeriodType;
import org.joda.time.chronology.ISOChronology;
import org.joda.time.format.ISOPeriodFormat;

public class MutablePeriodTest {

    @org.junit.Before
    public void setUp() {
        // Set up any initialization needed for your test cases
    }

    // Test constructor with no arguments
    @org.junit.Test
    public void testMutablePeriodConstructor() {
        MutablePeriod period = new MutablePeriod();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    // Test constructor with PeriodType argument
    @org.junit.Test
    public void testMutablePeriodConstructorWithPeriodType() {
        MutablePeriod period = new MutablePeriod(PeriodType.standard());
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    // Test parsed methods
    @org.junit.Test
    public void testParseMethods() {
        String periodString = "P3Y2M4D1H";
        MutablePeriod parsedPeriod = MutablePeriod.parse(periodString);
        assertEquals(3, parsedPeriod.getYears());
        assertEquals(2, parsedPeriod.getMonths());
        assertEquals(4, parsedPeriod.getDays());
        assertEquals(1, parsedPeriod.getHours());
        
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        String formattedPeriodString = formatter.print(parsedPeriod);
        assertEquals(periodString, formattedPeriodString);

        ISOPeriodFormat formatterWithTimeZone = ISOPeriodFormat.standard().withZoneUTC();
        String formattedPeriodStringWithTimeZone = formatterWithTimeZone.print(parsedPeriod);
        assertTrue(formattedPeriodStringWithTimeZone.startsWith("PT"));
    }

    // Test setting and getting values
    @org.junit.Test
    public void testSetAndGetValues() {
        MutablePeriod period = new MutablePeriod();
        period.setValue(0, 1);
        assertEquals(1, period.getValue(0));
        period.setValue(1, 2);
        assertEquals(2, period.getValue(1));

        int[] values = new int[period.size()];
        period.getValues(values);
        assertEquals(1, values[0]);
        assertEquals(2, values[1]);
    }

    // Test setting and getting fields
    @org.junit.Test
    public void testSetAndGetFields() {
        MutablePeriod period = new MutablePeriod();
        period.setField(PeriodFieldType.years(), 3);
        assertEquals(3, period.getYears());
        period.setField(PeriodFieldType.months(), 2);
        assertEquals(2, period.getMonths());

        period.set(PeriodFieldType.days(), 4);
        assertEquals(4, period.getDays());
        period.set(PeriodFieldType.hours(), 1);
        assertEquals(1, period.getHours());
        period.set(PeriodFieldType.minutes(), 2);
        assertEquals(2, period.getMinutes());
        period.set(PeriodFieldType.seconds(), 3);
        assertEquals(3, period.getSeconds());
        period.set(PeriodFieldType.millis(), 4);
        assertEquals(4, period.getMillis());
    }

    // Test setting and getting period
    @org.junit.Test
    public void testSetAndGetPeriod() {
        Period period = new Period(3, 2, 1, 2, 3, 4, 5, 6, PeriodType.standard());
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.setPeriod(period);

        assertEquals(3, mutablePeriod.getYears());
        assertEquals(2, mutablePeriod.getMonths());
        assertEquals(1, mutablePeriod.getWeeks());
        assertEquals(2, mutablePeriod.getDays());
        assertEquals(3, mutablePeriod.getHours());
        assertEquals(4, mutablePeriod.getMinutes());
        assertEquals(5, mutablePeriod.getSeconds());
        assertEquals(6, mutablePeriod.getMillis());

        Period expectedPeriod = new Period(3, 2, 1, 2, 3, 4, 5, 6, PeriodType.standard());
        Period actualPeriod = mutablePeriod.toPeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    // Test setting and getting period with Chronology
    @org.junit.Test
    public void testSetAndGetPeriodWithChronology() {
        Period period = new Period(3, 2, 1, 2, 3, 4, 5, 6, PeriodType.standard());
        ISOChronology chronology = ISOChronology.getInstance();

        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.setPeriod(period, chronology);

        assertEquals(3, mutablePeriod.getYears(chronology));
        assertEquals(2, mutablePeriod.getMonths(chronology));
        assertEquals(1, mutablePeriod.getWeeks(chronology));
        assertEquals(2, mutablePeriod.getDays(chronology));
        assertEquals(3, mutablePeriod.getHours(chronology));
        assertEquals(4, mutablePeriod.getMinutes(chronology));
        assertEquals(5, mutablePeriod.getSeconds(chronology));
        assertEquals(6, mutablePeriod.getMillis(chronology));

        Period expectedPeriod = new Period(3, 2, 1, 2, 3, 4, 5, 6, PeriodType.standard());
        Period actualPeriod = mutablePeriod.toPeriod(chronology);
        assertEquals(expectedPeriod, actualPeriod);
    }

    // Test merging period
    @org.junit.Test
    public void testMergePeriod() {
        Period basePeriod = new Period(3, 2, 1, 2, 3, 4, 5, 6, PeriodType.standard());
        Period addingPeriod = new Period(1, 1, 0, 0, 0, 0, 0, 0, PeriodType.standard());

        MutablePeriod mutablePeriod = new MutablePeriod(basePeriod);
        mutablePeriod.mergePeriod(addingPeriod);

        Period expectedPeriod = new Period(4, 3, 1, 2, 3, 4, 5, 6, PeriodType.standard());
        Period actualPeriod = mutablePeriod.toPeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    // Test adding years
    @org.junit.Test
    public void testAddYears() {
        MutablePeriod period = new MutablePeriod();
        period.addYears(3);
        assertEquals(3, period.getYears());

        period.addYears(-2);
        assertEquals(1, period.getYears());
    }

    // Test adding months
    @org.junit.Test
    public void testAddMonths() {
        MutablePeriod period = new MutablePeriod();
        period.addMonths(3);
        assertEquals(3, period.getMonths());

        period.addMonths(-2);
        assertEquals(1, period.getMonths());
    }

    // Test adding weeks
    @org.junit.Test
    public void testAddWeeks() {
        MutablePeriod period = new MutablePeriod();
        period.addWeeks(3);
        assertEquals(3, period.getWeeks());

        period.addWeeks(-2);
        assertEquals(1, period.getWeeks());
    }

    // Test adding days
    @org.junit.Test
    public void testAddDays() {
        MutablePeriod period = new MutablePeriod();
        period.addDays(3);
        assertEquals(3, period.getDays());

        period.addDays(-2);
        assertEquals(1, period.getDays());
    }

    // Test adding hours
    @org.junit.Test
    public void testAddHours() {
        MutablePeriod period = new MutablePeriod();
        period.addHours(3);
        assertEquals(3, period.getHours());

        period.addHours(-2);
        assertEquals(1, period.getHours());
    }

    // Test adding minutes
    @org.junit.Test
    public void testAddMinutes() {
        MutablePeriod period = new MutablePeriod();
        period.addMinutes(3);
        assertEquals(3, period.getMinutes());

        period.addMinutes(-2);
        assertEquals(1, period.getMinutes());
    }

    // Test adding seconds
    @org.junit.Test
    public void testAddSeconds() {
        MutablePeriod period = new MutablePeriod();
        period.addSeconds(3);
        assertEquals(3, period.getSeconds());

        period.addSeconds(-2);
        assertEquals(1, period.getSeconds());
    }

    // Test adding milliseconds
    @org.junit.Test
    public void testAddMillis() {
        MutablePeriod period = new MutablePeriod();
        period.addMillis(3);
        assertEquals(3, period.getMillis());

        period.addMillis(-2);
        assertEquals(1, period.getMillis());
    }

    // Test adding periods
    @org.junit.Test
    public void testAddPeriods() {
        MutablePeriod period1 = new MutablePeriod();
        period1.setYears(3);
        period1.setMonths(2);
        period1.setWeeks(1);

        MutablePeriod period2 = new MutablePeriod();
        period2.setYears(1);
        period2.setMonths(1);

        period1.add(period2);

        Period expectedPeriod = new Period(4, 3, 1, 2, 0, 0, 0, 0, PeriodType.standard());
        Period actualPeriod = period1.toPeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    // Test adding durations
    @org.junit.Test
    public void testAddDurations() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(new Period(3, 2, 1, 2, 3, 4, 5, 6, PeriodType.standard()));

        ReadableDuration duration = new GeneralDuration(3, TimeUnit.HOURS);

        period.add(duration);

        Period expectedPeriod = new Period(6, 2, 1, 2, 3, 4, 5, 6, PeriodType.standard());
        Period actualPeriod = period.toPeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    // Test adding long values
    @org.junit.Test
    public void testAddLongValues() {
        MutablePeriod period = new MutablePeriod();

        period.add(1234567890L);

        Period expectedPeriod = new Period(1234567890L, PeriodType.standard());
        Period actualPeriod = period.toPeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    // Test adding long values with Chronology
    @org.junit.Test
    public void testAddLongValuesWithChronology() {
        MutablePeriod period = new MutablePeriod();
        ISOChronology chronology = ISOChronology.getInstance();

        period.setPeriod(new Period(3, 2, 1, 2, 3, 4, 5, 6, PeriodType.standard()));
        period.setPeriod(chronology);

        long valueToAdd = 1234567890L;

        period.add(valueToAdd, chronology);

        Period expectedPeriod = new Period(3 + valueToAdd / 3652L, 2, 1, 2, 3 + (valueToAdd % 3652L) / 30, 4, 5, 6 + valueToAdd % 30, PeriodType.standard());
        Period actualPeriod = period.toPeriod(chronology);
        assertEquals(expectedPeriod, actualPeriod);
    }

    // Test parsing invalid strings
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStrings() {
        MutablePeriod.parse("PX3Y");
    }

    // Test parsing strings with unsupported fields
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseStringsWithUnsupportedFields() {
        MutablePeriod.parse("P3Y2M4S");
    }

    // Test parsing strings with negative fields
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseStringsWithNegativeFields() {
        MutablePeriod.parse("P-3Y2M4S");
    }

    // Test parsing strings with invalid chronology
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseStringsWithInvalidChronology() {
        MutablePeriod.parse("P3Y2M4S", new LocalDateTimeChronology());
    }

    // Test setting fields out of bounds
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetFieldsOutOfBounds() {
        MutablePeriod period = new MutablePeriod();

        period.set(PeriodFieldType.years(), 10000);
    }

    // Test setting fields with invalid values
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetFieldsWithInvalidValues() {
        MutablePeriod period = new MutablePeriod();

        period.set(PeriodFieldType.years(), 0);
        period.set(PeriodFieldType.months(), 13);
    }

    // Test setting fields with negative values
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetFieldsWithNegativeValues() {
        MutablePeriod period = new MutablePeriod();

        period.set(PeriodFieldType.years(), 1);
        period.set(PeriodFieldType.months(), -2);
    }

    // Test setting period with null argument
    @org.junit.Test(expected = NullPointerException.class)
    public void testSetPeriodWithNullArgument() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(null);
    }

    // Test setting period with invalid argument
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetPeriodWithInvalidArgument() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(new Object());
    }

    // Test setting period with Chronology argument and null value
    @org.junit.Test(expected = NullPointerException.class)
    public void testSetPeriodWithChronologyAndNullValue() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(new Period(3, 2, 1, 2, 3, 4, 5, 6, PeriodType.standard()), null);
    }

    // Test setting period with Chronology argument and invalid Chronology
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetPeriodWithChronologyAndInvalidChronology() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(new Period(3, 2, 1, 2, 3, 4, 5, 6, PeriodType.standard()), new LocalDateTimeChronology());
    }
}
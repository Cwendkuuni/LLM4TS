package org.junit.joda.time;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.format.*;
import org.joda.time.field.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    @BeforeClass
    public static void beforeClass() {
        // Initialize any static variables or setup shared resources here.
    }

    @AfterClass
    public static void afterClass() {
        // Release any shared resources or tear down the test environment here.
    }

    @Test
    public void testParse() {
        String validPeriodStrings[] = {"P1Y2M3D", "PT3H"};
        PeriodFormatter standardFormatter = ISOPeriodFormat.standard();
        PeriodFormatter customFormatter = new BasicDateTimeFormatter("PT%d%sh%sm%sm.%dS");

        for (String validPeriodString : validPeriodStrings) {
            MutablePeriod mutablePeriod = MutablePeriod.parse(validPeriodString);
            MutablePeriod parsedMutablePeriod = MutablePeriod.parse(validPeriodString, standardFormatter);
            assertEquals(parsedMutablePeriod, mutablePeriod);
            MutablePeriod customParsedMutablePeriod = MutablePeriod.parse(validPeriodString, customFormatter);
            assertEquals(parsedMutablePeriod, customParsedMutablePeriod);
        }
    }

    @Test
    public void testConstructorWithLong() {
        MutablePeriod mutablePeriod = new MutablePeriod(123456789012L);
        int expectedYears = 3564;
        int expectedMonths = 108;
        int expectedWeeks = 158;
        int expectedDays = 11321;
        int expectedHours = 337;
        int expectedMinutes = 222;
        int expectedSeconds = 78;
        int expectedMillis = 12;

        assertEquals(expectedYears, mutablePeriod.getYears());
        assertEquals(expectedMonths, mutablePeriod.getMonths());
        assertEquals(expectedWeeks, mutablePeriod.getWeeks());
        assertEquals(expectedDays, mutablePeriod.getDays());
        assertEquals(expectedHours, mutablePeriod.getHours());
        assertEquals(expectedMinutes, mutablePeriod.getMinutes());
        assertEquals(expectedSeconds, mutablePeriod.getSeconds());
        assertEquals(expectedMillis, mutablePeriod.getMillis());
    }

    @Test
    public void testAddNegativeDuration() {
        MutablePeriod mutablePeriod = new MutablePeriod(123456789012L, DateTimeUtils.UTC);
        MutablePeriod negativeDurationPeriod = new MutablePeriod(Duration.standardNegativeOne().toMillis(), DateTimeUtils.UTC);

        mutablePeriod.add(negativeDurationPeriod);
        Period expectedNegativeDuration = new Period(Duration.standardNegativeOne().toMillis(), DateTimeUtils.UTC);

        assertEquals(expectedNegativeDuration, mutablePeriod);
    }

    @Test
    public void testAddEmptyReadablePeriod() {
        MutablePeriod mutablePeriod = new MutablePeriod(123456789012L, DateTimeUtils.UTC);
        MutablePeriod emptyReadablePeriod = new MutablePeriod();

        mutablePeriod.add(emptyReadablePeriod);
        assertEquals(mutablePeriod, new MutablePeriod(123456789012L, DateTimeUtils.UTC));
    }

    @Test
    public void testAddLongerDuration() {
        MutablePeriod mutablePeriod = new MutablePeriod(123456789012L, DateTimeUtils.UTC);
        MutablePeriod longerDurationPeriod = new MutablePeriod(Duration.standardHours(1000).toMillis(), DateTimeUtils.UTC);

        mutablePeriod.add(longerDurationPeriod);
        Period expectedLongerDuration = new Period(Duration.standardHours(1000).toMillis(), DateTimeUtils.UTC);

        assertEquals(expectedLongerDuration, mutablePeriod);
    }

    @Test
    public void testAddReadableDurationWithDifferentChronology() {
        MutablePeriod mutablePeriod = new MutablePeriod(123456789012L, DateTimeUtils.UTC);
        MutablePeriod readableDurationPeriod = new MutablePeriod(Duration.standardDays(1).toMillis(), DateTimeUtils.PROLEPTIC_GREGORIAN);

        mutablePeriod.add(readableDurationPeriod);
        Period expectedAddedPeriod = new Period(Duration.standardDays(1).toMillis(), DateTimeUtils.UTC);

        assertEquals(expectedAddedPeriod, mutablePeriod);
        assertEquals(mutablePeriod, new MutablePeriod(123456788012L, DateTimeUtils.UTC));
    }

    @After
    public void afterEach() {
        // Perform any necessary cleanup or reset after each test.
    }
}
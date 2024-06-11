package org.junit.jupiter.demo;

import org.joda.time.*;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        final DateTime start = new DateTime(2022, 1, 1, 0, 0);
        final DateTime end = new DateTime(2022, 1, 2, 0, 0);
        final int expected = 86400000; // 1 day in milliseconds
        final DurationFieldType durationFieldType = DateTimeFields.DAYS;

        int actual = BaseSingleFieldPeriod.between(start, end, durationFieldType);

        assertEquals(expected, actual);
    }

    @Test
    public void testStandardPeriodIn() {
        final Period period = new Period(1, DateTimeFields.DAYS);
        final long n = 2;
        final int expected = 7 * 24 * 60 * 60 * 1000; // 2 days in milliseconds

        int actual = BaseSingleFieldPeriod.standardPeriodIn(period, n);

        assertEquals(expected, actual);
    }

    @Test
    public void testConstructor() {
        final int iPeriod = 123;
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(iPeriod);

        assertEquals(iPeriod, period.getValue());
    }

    // Add more test cases as needed
}
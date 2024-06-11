package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.Period;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.format.DateTimeFormatter;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, 2);

    @Test
    public void testIsLenient() {
        assertFalse(testField.isLenient());
    }

    @Test
    public void testGet() {
        MutableDateTime dateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, JulianChronology.INSTANCE);
        LocalDateTime expectedDateTime = LocalDateTime.of(1, 2, 1, 0, 0);
        assertEquals(expectedDateTime.getMonthValue(), testField.get(dateTime.getMillis()));

        dateTime.setField(DateTimeFieldType.monthOfYear(), 3);
        expectedDateTime = LocalDateTime.of(1, 2, 3, 0, 0);
        assertEquals(expectedDateTime.getMonthValue(), testField.get(dateTime.getMillis()));
    }

    @Test
    public void testAdd() {
        long initialMillis = 0;
        long lastMillis = Long.MAX_VALUE;

        for (long addAmount = 1; addAmount <= 12; addAmount++) {
            MutableDateTime dateTime = new MutableDateTime(initialMillis, JulianChronology.INSTANCE);
            long resultMillis = testField.add(dateTime.getMillis(), addAmount);
            MutableDateTime resultDateTime = new MutableDateTime(resultMillis, JulianChronology.INSTANCE);

            assertEquals(addAmount, testField.get(resultMillis) - testField.get(initialMillis));
            assertEquals(resultDateTime.getYear(), dateTime.getYear() + addAmount / 12);
            assertEquals(resultDateTime.getMonthOfYear(), dateTime.getMonthOfYear() + addAmount % 12 + 1);
            assertEquals(resultDateTime.getDayOfMonth(), 1);
            initialMillis = resultMillis;
        }

        MutableDateTime dateTime = new MutableDateTime(lastMillis, JulianChronology.INSTANCE);
        assertThrows(IllegalArgumentException.class, () -> testField.add(dateTime.getMillis(), 13));
    }

    @Test
    public void testAddLong() {
        long initialMillis = 0;
        long lastMillis = Long.MAX_VALUE;

        for (long addAmount = 1; addAmount <= 12; addAmount++) {
            MutableDateTime dateTime = new MutableDateTime(initialMillis, JulianChronology.INSTANCE);
            long resultMillis = testField.add(dateTime.getMillis(), addAmount);
            MutableDateTime resultDateTime = new MutableDateTime(resultMillis, JulianChronology.INSTANCE);

            assertEquals(addAmount, testField.get(resultMillis) - testField.get(initialMillis));
            assertEquals(resultDateTime.getYear(), dateTime.getYear() + addAmount / 12);
            assertEquals(resultDateTime.getMonthOfYear(), dateTime.getMonthOfYear() + addAmount % 12 + 1);
            assertEquals(resultDateTime.getDayOfMonth(), 1);
            initialMillis = resultMillis;
        }

        MutableDateTime dateTime = new MutableDateTime(lastMillis, JulianChronology.INSTANCE);
        assertThrows(IllegalArgumentException.class, () -> testField.add(dateTime.getMillis(), 13L));
    }

    @Test
    public void testAddReadablePartial() {
        LocalDateTime dateTime = LocalDateTime.of(1, 1, 1, 0, 0);
        int[] expectedValues = {1, 2, 3}; // 1 year, 1 month, and 1 day

        assertEquals(expectedValues, testField.add(new LocalDateTime[]{dateTime}, 1, new int[3], 0));

        expectedValues = new int[]{3, 2, 1}; // 1 year, -1 month, and 1 day

        assertEquals(expectedValues, testField.add(new LocalDateTime[]{dateTime}, -1, new int[3], 1));

        LocalDateTime dateTime2 = LocalDateTime.of(1, 2, 28, 0, 0);
        assertEquals(new int[]{1, 3, 1}, testField.add(new LocalDateTime[]{dateTime, dateTime2}, 1, new int[3], 0));

        LocalDateTime dateTime3 = LocalDateTime.of(1, 3, 30, 0, 0); // Invalid date
        assertThrows(IllegalArgumentException.class, () -> testField.add(new LocalDateTime[]{dateTime, dateTime3}, 1, new int[3], 0));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long date1Millis = 0;
        long date2Millis = Long.MAX_VALUE;

        assertEquals(12, testField.getDifferenceAsLong(date2Millis, date1Millis));

        MutableDateTime date1 = new MutableDateTime(date1Millis, JulianChronology.INSTANCE);
        MutableDateTime date2 = new MutableDateTime(date2Millis, JulianChronology.INSTANCE);

        assertEquals(12, testField.getDifferenceAsLong(date2.getMillis(), date1.getMillis()));

        assertEquals(12, testField.getDifferenceAsLong(date1.getMillis(), date2.getMillis()));
    }

    @Test
    public void testSet() {
        long initialMillis = 0;
        long targetMillis = 1;

        MutableDateTime dateTime = new MutableDateTime(initialMillis, JulianChronology.INSTANCE);
        long resultMillis = testField.set(dateTime.getMillis(), 2);
        MutableDateTime resultDateTime = new MutableDateTime(resultMillis, JulianChronology.INSTANCE);

        assertEquals(2, testField.get(resultMillis));
        assertEquals(resultDateTime.getYear(), dateTime.getYear() + 1);
        assertEquals(resultDateTime.getMonthOfYear(), 2);
        assertEquals(1, resultDateTime.getDayOfMonth());

        initialMillis = resultMillis;

        testField.set(initialMillis, 14);
        resultDateTime = new MutableDateTime(initialMillis, JulianChronology.INSTANCE);

        assertEquals(14, testField.get(initialMillis));
        assertEquals(resultDateTime.getYear(), dateTime.getYear() + 2);
        assertEquals(resultDateTime.getMonthOfYear(), 14);
        assertEquals(1, resultDateTime.getDayOfMonth());
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField expectedDurationField = JulianChronology.INSTANCE.years();

        assertEquals(expectedDurationField, testField.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        assertTrue(testField.isLeap(LocalDateTime.of(1, 2, 29, 0, 0, 0, 0, JulianChronology.INSTANCE).getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        assertEquals(1, testField.getLeapAmount(LocalDateTime.of(1, 2, 29, 0, 0, 0, 0, JulianChronology.INSTANCE).getMillis()));
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField expectedDurationField = JulianChronology.INSTANCE.days();

        assertEquals(expectedDurationField, testField.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, testField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, testField.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long initialMillis = 0;
        long finalMillis = 2 * (12 * JulianChronology.INSTANCE.getMillisPerYear()) + 2 * JulianChronology.INSTANCE.getMillisPerDay();

        assertEquals(initialMillis, testField.roundFloor(initialMillis));
        assertEquals(finalMillis, testField.roundFloor(finalMillis));
    }

    @Test
    public void testRemainder() {
        long initialMillis = 0;
        long finalMillis = 2 * (12 * JulianChronology.INSTANCE.getMillisPerYear()) + (JulianChronology.INSTANCE.getMillisPerDay() + 500);

        assertEquals(0, testField.remainder(initialMillis));
        long remainder = testField.remainder(finalMillis);

        assertEquals(500, remainder);
        assertEquals(1, testField.get(finalMillis));
    }
}
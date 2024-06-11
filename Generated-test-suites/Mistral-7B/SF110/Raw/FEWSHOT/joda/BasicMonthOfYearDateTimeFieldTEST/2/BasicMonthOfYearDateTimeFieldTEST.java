package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Partial;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.DateTimeField;

import java.util.Arrays;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicConstruction() {
        JulianChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(chronology, field.getChronology());
        assertEquals(DateTimeFieldType.MONTHS, field.getFieldType().getBaseUnit());
        assertEquals(12, field.getMaximumValue());
        assertEquals(1, field.getMinimumValue());
        assertFalse(field.isLenient());
    }

    @Test
    public void testGetSet() {
        JulianChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        LocalDateTime dateTime = new LocalDateTime(chronology, new LocalDate(2022, 1, 1), new LocalTime());
        long millis = dateTime.getMillis();
        int initialValue = field.get(millis);
        int setValue = 12;
        long setMillis = field.set(millis, setValue);
        int newValue = field.get(setMillis);
        assertEquals(setValue, newValue);
        assertEquals(millis + getMonthMillisAdjustment(chronology, 11), setMillis);
    }

    @Test
    public void testAdd() {
        JulianChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        LocalDateTime dateTime = new LocalDateTime(chronology, new LocalDate(2022, 1, 1), new LocalTime());
        long millis = dateTime.getMillis();
        int addAmount = 5;

        long resultMillis = field.add(millis, addAmount);
        int resultValue = field.get(resultMillis);
        int expectedValue = (1 + 5) % 12 + 1; // 6 -> 6 % 12 + 1 = 7

        assertEquals(expectedValue, resultValue);
        assertEquals(millis + getMonthMillisAdjustment(chronology, addAmount), resultMillis);

        // Test negative add amount
        long negativeResultMillis = field.add(millis, -addAmount);
        int negativeResultValue = field.get(negativeResultMillis);
        int negativeExpectedValue = (11 - 5) % 12 + 1; // 11 -> 11 % 12 + 1 = 13 -> 13 % 12 + 1 = 1

        assertEquals(negativeExpectedValue, negativeResultValue);
        assertEquals(millis + getMonthMillisAdjustment(chronology, -addAmount), negativeResultMillis);
    }

    @Test
    public void testAddWrapField() {
        JulianChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        LocalDateTime dateTime = new LocalDateTime(chronology, new LocalDate(2022, 1, 1), new LocalTime());
        long millis = dateTime.getMillis();
        int addAmount = 13;

        long resultMillis = field.addWrapField(millis, addAmount);
        int resultValue = field.get(resultMillis);
        int expectedValue = 1;

        assertEquals(expectedValue, resultValue);
        assertEquals(millis + getMonthMillisAdjustment(chronology, 12), resultMillis);
    }

    @Test
    public void testAddWithPartial() {
        JulianChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        LocalDateTime dateTime = new LocalDateTime(chronology, new LocalDate(2022, 1, 1), new LocalTime());
        long millis = dateTime.getMillis();
        int addAmount = 3;
        int partialValue = 10;
        int arrayLength = 2;
        int[] expectedArray = {13, 11}; // 10 + 3 -> 13, 1 -> 11

        Partial partial = new Partial(DateTimeFieldType.monthOfYear(), partialValue);
        int[] resultArray = field.add(partial, addAmount, new int[arrayLength], 0);

        assertEquals(expectedArray[0], resultArray[0]);
        assertEquals(expectedArray[1], resultArray[1]);
    }

    @Test
    public void testGetDifferenceAsLong() {
        JulianChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        LocalDateTime dateTime1 = new LocalDateTime(chronology, new LocalDate(2022, 1, 1), new LocalTime());
        long millis1 = dateTime1.getMillis();
        LocalDateTime dateTime2 = new LocalDateTime(chronology, new LocalDate(2022, 3, 1), new LocalTime());
        long millis2 = dateTime2.getMillis();

        long difference = field.getDifferenceAsLong(millis1, millis2);
        int expectedDifference = 2;

        assertEquals(expectedDifference, difference);
    }

    @Test
    public void testIsLeap() {
        JulianChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);

        LocalDateTime dateTime = new LocalDateTime(chronology, new LocalDate(2022, 2, 28), new LocalTime());
        long millis = dateTime.getMillis();

        boolean isLeap = field.isLeap(millis);
        boolean expectedLeap = chronology.isLeapYear(2022);

        assertEquals(expectedLeap, isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        JulianChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);

        LocalDateTime dateTime = new LocalDateTime(chronology, new LocalDate(2022, 2, 28), new LocalTime());
        long millis = dateTime.getMillis();

        int leapAmount = field.getLeapAmount(millis);
        int expectedLeapAmount = 0;

        assertEquals(expectedLeapAmount, leapAmount);
    }

    @Test
    public void testGetMaximumValueForLeapYear() {
        GregorianChronology gregorianChronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChronology, DateTimeConstants.FEBRUARY);

        int maxValue = field.getMaximumValue();
        int expectedMaxValue = 29;

        assertEquals(expectedMaxValue, maxValue);
    }

    @Test
    public void testRoundFloor() {
        JulianChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        LocalDateTime dateTime = new LocalDateTime(chronology, new LocalDate(2022, 1, 1), new LocalTime());
        long millis = dateTime.getMillis();

        long roundedFloor = field.roundFloor(millis);
        long expectedRoundedFloor = getYearMonthMillis(chronology, 2022, 1);

        assertEquals(expectedRoundedFloor, roundedFloor);
    }

    @Test
    public void testRemainder() {
        JulianChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        LocalDateTime dateTime = new LocalDateTime(chronology, new LocalDate(2022, 1, 1), new LocalTime());
        long millis = dateTime.getMillis();
        int remainAmount = 3;

        long remainder = field.remainder(millis);
        long expectedRemainder = getMonthMillisAdjustment(chronology, remainAmount);

        assertEquals(expectedRemainder, remainder);
    }

    private long getMonthMillisAdjustment(Chronology chronology, int months) {
        int year = chronology.getYear(System.currentTimeMillis());
        int monthOfYear = chronology.getMonthOfYear(System.currentTimeMillis(), year);
        int daysInYearMonth = chronology.getDaysInYearMonth(year, monthOfYear);
        int daysToAdd = (months + 1) * chronology.getMillisPerDay();

        return daysToAdd - (chronology.getMillisOfDay(System.currentTimeMillis()) % daysToAdd);
    }

    private long getYearMonthMillis(Chronology chronology, int year, int monthOfYear) {
        return chronology.getMillisOfYear(year) + chronology.getMillisPerMonth() * (monthOfYear - 1);
    }
}
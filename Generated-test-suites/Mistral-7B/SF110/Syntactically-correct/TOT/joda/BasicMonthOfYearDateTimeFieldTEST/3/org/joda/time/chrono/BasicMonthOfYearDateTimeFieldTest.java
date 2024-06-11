package org.joda.time.chrono.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
@Category(org.joda.time.LocalDateTest.class)
public class BasicMonthOfYearDateTimeFieldTest {

    private final Chronology chronology;
    private final int leapMonth;

    public BasicMonthOfYearDateTimeFieldTest(Chronology chronology, int leapMonth) {
        this.chronology = chronology;
        this.leapMonth = leapMonth;
    }

    @Parameterized.Parameters(name = "{0} with leap month {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {new GregorianChronology(), 2},
            {ISOChronology.INSTANCE, 2},
            {JulianChronology.INSTANCE, 2},
            {JulianLeapChronology.INSTANCE, 2},
            {JulianPersianChronology.INSTANCE, 12},
            {PersianChronology.INSTANCE, 12}
        });
    }

    @Test
    public void testNewInstance() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(this.chronology, this.leapMonth);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
        assertEquals(this.chronology.getAverageMillisPerMonth(), field.getPrecision());
        assertEquals(this.chronology, field.getChronology());
        assertEquals(this.leapMonth, field.getLeapMonth());
        assertEquals(false, field.isLenient());
    }

    @Test
    public void testGet() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        int month = this.chronology.getMonthOfYear(date.getMillis());
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(this.chronology, this.leapMonth);
        int actual = field.get(date.getMillis());
        assertEquals(month, actual);
    }

    @Test
    public void testAdd() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        int month = this.chronology.getMonthOfYear(date.getMillis());
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(this.chronology, this.leapMonth);

        // Add one month
        long expected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()), month + 1);
        long result = field.add(date.getMillis(), 1);
        assertEquals(expected, result);

        // Test adding a negative value
        long negativeResult = field.add(date.getMillis(), -1);
        long negativeExpected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()), month - 1);
        assertEquals(negativeExpected, negativeResult);

        // Test adding a large value
        long largeValue = 12 * this.chronology.getMillisPerYear();
        long largeExpected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()) + 12, 1);
        long largeResult = field.add(date.getMillis(), largeValue);
        assertEquals(largeExpected, largeResult);
    }

    @Test
    public void testAddWrapField() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        int month = this.chronology.getMonthOfYear(date.getMillis());
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(this.chronology, this.leapMonth);

        // Test wrapping around the minimum value
        int minValue = this.chronology.getMinimumValue(DateTimeFieldType.monthOfYear());
        long result = field.addWrapField(date.getMillis(), minValue - 1);
        long expected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()), minValue);
        assertEquals(expected, result);

        // Test wrapping around the maximum value
        int maxValue = this.chronology.getMaximumValue(DateTimeFieldType.monthOfYear());
        result = field.addWrapField(date.getMillis(), maxValue + 1);
        expected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()) + 1, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        MutableDateTime date1 = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        MutableDateTime date2 = new MutableDateTime(new BaseDateTime(Instant.now().minus(1, Duration.millis(365 * 24 * 30 * 2))), this.chronology);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(this.chronology, this.leapMonth);

        long result = field.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
        int expected = (int)(Math.abs(this.chronology.getYear(date1.getMillis()) - this.chronology.getYear(date2.getMillis())) * this.chronology.getMonthsPerYear() + this.chronology.getMonthOfYear(date1.getMillis()) - this.chronology.getMonthOfYear(date2.getMillis()));
        assertEquals(expected, result);
    }

    @Test
    public void testSet() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        int month = this.chronology.getMonthOfYear(date.getMillis());
        int newMonth = month + 2;
        long expected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()), newMonth);

        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(this.chronology, this.leapMonth);
        long result = field.set(date.getMillis(), newMonth);
        assertEquals(expected, result);

        // Test setting a value out of range
        int maxValue = this.chronology.getMaximumValue(DateTimeFieldType.monthOfYear());
        int minValue = this.chronology.getMinimumValue(DateTimeFieldType.monthOfYear());
        long outOfRangeResult = field.set(date.getMillis(), minValue - 1);
        long outOfRangeExpected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()), minValue);
        assertEquals(outOfRangeExpected, outOfRangeResult);

        long maxResult = field.set(date.getMillis(), maxValue + 1);
        long maxExpected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()) + 1, 1);
        assertEquals(maxExpected, maxResult);
    }

    @Test
    public void testAddWrapFieldWithNegativeValue() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        int month = this.chronology.getMonthOfYear(date.getMillis());
        int newMonth = month - 2;
        long expected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()), newMonth);

        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(this.chronology, this.leapMonth);
        long result = field.set(date.getMillis(), newMonth);
        assertEquals(expected, result);

        // Test wrapping around the minimum value with a negative value
        int minValue = this.chronology.getMinimumValue(DateTimeFieldType.monthOfYear());
        long minResult = field.addWrapField(date.getMillis(), minValue - 1);
        long minExpected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()), minValue);
        assertEquals(minExpected, minResult);

        // Test wrapping around the maximum value with a negative value
        int maxValue = this.chronology.getMaximumValue(DateTimeFieldType.monthOfYear());
        long maxResult = field.addWrapField(date.getMillis(), maxValue + 1);
        long maxExpected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()) + 1, 1);
        assertEquals(maxExpected, maxResult);
    }

    @Test
    public void testAddWrapFieldWithZeroValue() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        int month = this.chronology.getMonthOfYear(date.getMillis());
        long result = field.addWrapField(date.getMillis(), 0);
        long expected = date.getMillis();
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapFieldWithValueGreaterThanOneYear() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        int month = this.chronology.getMonthOfYear(date.getMillis());
        long oneYearMillis = this.chronology.getMillisPerYear();
        long result = field.addWrapField(date.getMillis(), (month + 12) % 12 + 1);
        long expected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()) + 1, (month + 12) % 12 + 1);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLeap() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        int year = this.chronology.getYear(date.getMillis());
        boolean expected = this.chronology.isLeapYear(year) && this.chronology.getMonthOfYear(date.getMillis(), year) == this.leapMonth;
        boolean result = field.isLeap(date.getMillis());
        assertEquals(expected, result);
    }

    @Test
    public void testGetLeapAmount() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        int year = this.chronology.getYear(date.getMillis());
        boolean isLeap = this.chronology.isLeapYear(year);
        int leapAmount = isLeap ? 1 : 0;
        int expected = leapAmount;
        int result = field.getLeapAmount(date.getMillis());
        assertEquals(expected, result);
    }

    @Test
    public void testGetLeapDurationField() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        DurationFieldType days = DurationFieldType.days();
        DurationField leapDurationField = field.getLeapDurationField();
        long expectedMillis = this.chronology.getDaysInYear(this.chronology.getYear(date.getMillis()), this.leapMonth);
        long expected = leapDurationField.getMillisWithoutAdjustments(expectedMillis);
        long resultMillis = leapDurationField.getMillisWithoutAdjustments(date.getMillis());
        long result = resultMillis / this.chronology.getMillisPerDay();
        assertEquals(expected, result);
    }

    @Test
    public void testGetMinimumValue() {
        int expected = this.chronology.getMinimumValue(DateTimeFieldType.monthOfYear());
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        int expected = this.chronology.getMaximumValue(DateTimeFieldType.monthOfYear());
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        long result = field.roundFloor(date.getMillis());
        long expected = this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()), this.chronology.getMonthOfYear(date.getMillis(), this.chronology.getYear(date.getMillis())));
        assertEquals(expected, result);
    }

    @Test
    public void testRemainder() {
        MutableDateTime date = new MutableDateTime(new BaseDateTime(Instant.now(), this.chronology));
        long result = field.remainder(date.getMillis());
        long expected = date.getMillis() % this.chronology.getYearMonthMillis(this.chronology.getYear(date.getMillis()), this.chronology.getMonthOfYear(date.getMillis(), this.chronology.getYear(date.getMillis())));
        assertEquals(expected, result);
    }
}
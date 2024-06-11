package org.joda.time.test.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypeImpl;
import org.joda.time.FieldUtils;
import org.joda.time.Fields;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.TestUtils;
import org.joda.time.chrono.BasicGregorianChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private Chronology chronology;

    @Before
    public void setUp() {
        chronology = new BasicGregorianChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long testDate = TestUtils.getUTCMilliseconds();
        int month = field.get(testDate);
        assertEquals("Month should be retrieved correctly", chronology.getMonthOfYear(testDate), month);
    }

    @Test
    public void testAdd() {
        long testDate = TestUtils.getUTCMilliseconds();
        long newDate = field.add(testDate, 1);
        assertEquals("Date should be incremented by 1 month", testDate + chronology.getMillisPerMonth(), newDate);

        testDate = newDate;
        newDate = field.add(testDate, -1);
        assertEquals("Date should be decremented by 1 month", testDate - chronology.getMillisPerMonth(), newDate);

        testDate = TestUtils.getUTCMilliseconds();
        newDate = field.add(testDate, 12 * 12 + 11);
        assertEquals("Date should be incremented by 1 year and 11 months", testDate + (chronology.getMillisPerYear() * 12 + chronology.getMillisPerMonth() * 11), newDate);

        testDate = newDate;
        newDate = field.add(testDate, -12 * 12);
        assertEquals("Date should be decremented by 1 year", testDate - (chronology.getMillisPerYear() * 12), newDate);

        testDate = TestUtils.getUTCMilliseconds();
        newDate = field.add(testDate, Integer.MAX_VALUE);
        long maxDate = chronology.getMaximumValue().getMillis();
        assertTrue("Date should be wrapped around to minimum value", maxDate >= newDate && newDate >= chronology.getMinimumValue().getMillis());

        testDate = newDate;
        newDate = field.add(testDate, Integer.MIN_VALUE);
        long minDate = chronology.getMinimumValue().getMillis();
        assertTrue("Date should be wrapped around to maximum value", minDate <= newDate && newDate <= maxDate);
    }

    @Test
    public void testAddWrapField() {
        long testDate = TestUtils.getUTCMilliseconds();
        long newDate = field.addWrapField(testDate, 13);
        assertEquals("Date should be incremented by 1 month and wrapped around if necessary", testDate + chronology.getMillisPerMonth(), newDate);

        testDate = newDate;
        newDate = field.addWrapField(testDate, -13);
        assertEquals("Date should be decremented by 1 month and wrapped around if necessary", testDate - chronology.getMillisPerMonth(), newDate);
    }

    @Test
    public void testAddWithPartial() {
        long testDate = TestUtils.getUTCMilliseconds();
        ReadablePartial readablePartial = Fields.aMonthOfYear().setValue(12);
        int[] newValues = field.add(readablePartial, 1, new int[1], 1);
        assertEquals("Month of year should be incremented by 1", 13, newValues[0]);

        testDate = new Date(testDate + Fields.aMonthOfYear().getMillis(12)).getTime();
        readablePartial = Fields.aMonthOfYear().setValues(readablePartial, newValues);
        newValues = field.add(readablePartial, 1, new int[1], 1);
        assertEquals("Month of year should be incremented by another 1", 14, newValues[0]);

        readablePartial = Fields.aMonthOfYear().setValues(readablePartial, newValues);
        newValues = field.add(readablePartial, -2, new int[1], 1);
        assertEquals("Month of year should be decremented by 2", 12, newValues[0]);

        readablePartial = Fields.aMonthOfYear().setValues(readablePartial, newValues);
        newValues = field.add(readablePartial, 12 * 12, new int[1], 1);
        assertEquals("Month of year should be incremented by 1 year", 13, newValues[0]);

        readablePartial = Fields.aMonthOfYear().setValues(readablePartial, newValues);
        newValues = field.add(readablePartial, -12 * 12, new int[1], 1);
        assertEquals("Month of year should be decremented by 1 year", 1, newValues[0]);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long testDate1 = TestUtils.getUTCMilliseconds();
        long testDate2 = testDate1 + Fields.aMonthOfYear().getMillis(1);
        long difference = field.getDifferenceAsLong(testDate1, testDate2);
        assertEquals("Difference should be 1 month", 1, difference);

        testDate2 = testDate1 + Fields.aMonthOfYear().getMillis(12 * 12);
        difference = field.getDifferenceAsLong(testDate1, testDate2);
        assertEquals("Difference should be 1 year", 12 * 12, difference);

        testDate2 = testDate1 + Fields.aMonthOfYear().getMillis(-1);
        difference = field.getDifferenceAsLong(testDate2, testDate1);
        assertEquals("Difference should be -1 month", -1, difference);

        testDate2 = testDate1 + Fields.aMonthOfYear().getMillis(-12 * 12);
        difference = field.getDifferenceAsLong(testDate2, testDate1);
        assertEquals("Difference should be -1 year", -12 * 12, difference);

        testDate1 = TestUtils.getUTCMilliseconds();
        testDate2 = testDate1 + Fields.aMonthOfYear().getMillis(Integer.MAX_VALUE);
        difference = field.getDifferenceAsLong(testDate1, testDate2);
        assertTrue("Difference should be wrapped around to minimum value", difference >= Fields.aMonthOfYear().getMillis(Integer.MIN_VALUE) && difference <= Fields.aMonthOfYear().getMillis(Integer.MAX_VALUE));

        testDate2 = testDate1 + Fields.aMonthOfYear().getMillis(Integer.MIN_VALUE);
        difference = field.getDifferenceAsLong(testDate1, testDate2);
        assertTrue("Difference should be wrapped around to maximum value", difference >= Fields.aMonthOfYear().getMillis(Integer.MIN_VALUE) && difference <= Fields.aMonthOfYear().getMillis(Integer.MAX_VALUE));
    }

    @Test
    public void testSet() {
        long testDate = TestUtils.getUTCMilliseconds();
        long newDate = field.set(testDate, 13);
        assertEquals("Date should be set to first day of the next month", testDate + (chronology.getMillisPerMonth() * (chronology.getMonthOfYear(testDate) >= 12 ? 12 : 1)), newDate);

        testDate = newDate;
        newDate = field.set(testDate, 1);
        assertEquals("Date should be set to first day of the current month", testDate, newDate);

        testDate = TestUtils.getUTCMilliseconds();
        newDate = field.set(testDate, 0);
        assertEquals("Date should be set to the first day of the minimum year", TestUtils.getMillis(new LocalDate(chronology.getMinimumValue().getYear(), 1, 1)), newDate);

        testDate = newDate;
        newDate = field.set(testDate, 12 * 12 + 11);
        assertEquals("Date should be set to the first day of the next year", TestUtils.getMillis(new LocalDate(TestUtils.getYear(testDate) + 1, 1, 1)), newDate);

        testDate = newDate;
        newDate = field.set(testDate, Integer.MAX_VALUE);
        long maxDate = chronology.getMaximumValue().getMillis();
        assertTrue("Date should be set to the maximum value", maxDate >= newDate && newDate >= chronology.getMinimumValue().getMillis());

        testDate = newDate;
        newDate = field.set(testDate, Integer.MIN_VALUE);
        long minDate = chronology.getMinimumValue().getMillis();
        assertTrue("Date should be set to the minimum value", minDate <= newDate && newDate <= maxDate);
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals("Range duration field should be years", DurationFieldType.YEARS, rangeDurationField.getType());
    }

    @Test
    public void testIsLeap() {
        long testDate = TestUtils.getUTCMilliseconds();
        boolean isLeap = field.isLeap(testDate);
        assertFalse("Date should not be a leap date", isLeap);

        testDate += Fields.aMonthOfYear().getMillis(DateTimeConstants.JANUARY) + Fields.aDayOfYear().getMillis(DateTimeConstants.FEBRUARY_LAST_DAY);
        isLeap = field.isLeap(testDate);
        assertTrue("Date should be a leap date", isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        long testDate = TestUtils.getUTCMilliseconds();
        int leapAmount = field.getLeapAmount(testDate);
        assertEquals("Leap amount should be 0 for non-leap years", 0, leapAmount);

        testDate += Fields.aMonthOfYear().getMillis(DateTimeConstants.JANUARY) + Fields.aDayOfYear().getMillis(DateTimeConstants.FEBRUARY_LAST_DAY);
        leapAmount = field.getLeapAmount(testDate);
        assertEquals("Leap amount should be 1 for leap years", 1, leapAmount);
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals("Leap duration field should be days", DurationFieldType.standard().withMillisPerDay(1), leapDurationField);
    }

    @Test
    public void testGetMinimumValue() {
        int minimumValue = field.getMinimumValue();
        assertEquals("Minimum value should be 1", 1, minimumValue);
    }

    @Test
    public void testGetMaximumValue() {
        int maximumValue = field.getMaximumValue();
        assertEquals("Maximum value should be the number of months in a year", 12, maximumValue);
    }

    @Test
    public void testRoundFloor() {
        long testDate = TestUtils.getUTCMilliseconds();
        long roundedFloor = field.roundFloor(testDate);
        assertEquals("Rounded floor should be the start of the current month", testDate - (testDate % Fields.aMonthOfYear().getMillis()), roundedFloor);
    }

    @Test
    public void testRemainder() {
        long testDate = TestUtils.getUTCMilliseconds();
        long remainder = field.remainder(testDate);
        assertEquals("Remainder should be the number of days in the current month", testDate % Fields.aMonthOfYear().getMillis(), remainder);
    }

    @Test
    public void testSetWithNegativeValue() {
        long testDate = TestUtils.getUTCMilliseconds();
        long newDate = field.set(testDate, -1);
        assertEquals("Setting to a negative value should result in the same date", testDate, newDate);
    }
}
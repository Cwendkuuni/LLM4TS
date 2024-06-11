package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.GenericMonthOfYearField;
import org.joda.time.field.Months;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);

    @Test
    public void testIsLenient() {
        assertFalse(monthOfYear.isLenient());
    }

    @Test
    public void testGet() {
        long instant = BaseDateTime.UTC.getMillis();
        int month = monthOfYear.get(instant);
        assertEquals(DateTimeConstants.DECEMBER, month);
        instant += DateTimeConstants.ONE_MONTH;
        month = monthOfYear.get(instant);
        assertEquals(DateTimeConstants.JANUARY, month);
    }

    @Test
    public void testAdd() {
        long instant = BaseDateTime.UTC.getMillis();
        long result = monthOfYear.add(instant, 1);
        assertEquals(result, BaseDateTime.UTC.getMillis() + DateTimeConstants.ONE_MONTH);
        result = monthOfYear.add(instant, -1);
        assertEquals(result, BaseDateTime.UTC.getMillis() - DateTimeConstants.ONE_MONTH);
        result = monthOfYear.add(instant, 12);
        assertEquals(result, BaseDateTime.UTC.getMillis() + DateTimeConstants.ONE_YEAR);
        result = monthOfYear.add(instant, -12);
        assertEquals(result, BaseDateTime.UTC.getMillis() - DateTimeConstants.ONE_YEAR);
    }

    @Test
    public void testAddWrapField() {
        long instant = BaseDateTime.UTC.getMillis();
        long result = monthOfYear.addWrapField(instant, 13);
        // The result should be the same value, wrapping around to the previous year
        assertEquals(result, BaseDateTime.UTC.getMillis() - DateTimeConstants.ONE_YEAR + DateTimeConstants.MONTHS_PER_YEAR);
    }

    @Test
    public void testAddWrapFieldNegative() {
        long instant = BaseDateTime.UTC.getMillis();
        long result = monthOfYear.addWrapField(instant, -13);
        // The result should be the same value, wrapping around to the next year
        assertEquals(result, BaseDateTime.UTC.getMillis() + DateTimeConstants.ONE_YEAR - DateTimeConstants.MONTHS_PER_YEAR);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = BaseDateTime.UTC.getMillis() + DateTimeConstants.ONE_MONTH;
        long instant2 = BaseDateTime.UTC.getMillis() - DateTimeConstants.ONE_MONTH;
        long difference = monthOfYear.getDifferenceAsLong(instant1, instant2);
        assertEquals(difference, 2 * DateTimeConstants.MONTHS_PER_YEAR);
    }

    @Test
    public void testSet() {
        long instant = BaseDateTime.UTC.getMillis();
        long result = monthOfYear.set(instant, DateTimeConstants.MARCH);
        assertEquals(result, BaseDateTime.UTC.getMillis() + DateTimeConstants.MARCH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        long instant = BaseDateTime.UTC.getMillis();
        long result = monthOfYear.set(instant, DateTimeConstants.FEBRUARY_30);
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField rangeDurationField = monthOfYear.getRangeDurationField();
        assertEquals(DurationFieldType.YEARS, rangeDurationField.getFieldType());
    }

    @Test
    public void testIsLeap() {
        long instant = BaseDateTime.UTC.getMillis();
        assertTrue(monthOfYear.isLeap(instant));
        instant += DateTimeConstants.ONE_MONTH;
        assertFalse(monthOfYear.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        long instant = BaseDateTime.UTC.getMillis();
        int leapAmount = monthOfYear.getLeapAmount(instant);
        assertEquals(leapAmount, monthOfYear.isLeap(instant) ? 1 : 0);
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField leapDurationField = monthOfYear.getLeapDurationField();
        assertEquals(DurationFieldType.DAYS, leapDurationField.getFieldType());
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, monthOfYear.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.DECEMBER, monthOfYear.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long instant = BaseDateTime.UTC.getMillis();
        long result = monthOfYear.roundFloor(instant);
        assertEquals(result, BaseDateTime.UTC.getMillis() - (instant % DateTimeConstants.ONE_MONTH));
    }

    @Test
    public void testRemainder() {
        long instant = BaseDateTime.UTC.getMillis();
        long result = monthOfYear.remainder(instant);
        assertEquals(result, instant % DateTimeConstants.ONE_MONTH);
    }
}
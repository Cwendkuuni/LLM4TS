package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.chrono.JulianChronology;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        long instant = DateTimeConstants.JANUARY_1_2000.getMillis();
        int expected = DateTimeConstants.JANUARY;
        int result = field.get(instant);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        long instant = DateTimeConstants.JANUARY_1_2000.getMillis();
        int months = 3;
        long expected = DateTimeConstants.APRIL_1_2000.getMillis();
        long result = field.add(instant, months);
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        long instant = DateTimeConstants.JANUARY_1_2000.getMillis();
        int months = 13;
        long expected = DateTimeConstants.MARCH_1_2001.getMillis();
        long result = field.addWrapField(instant, months);
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        long minuendInstant = DateTimeConstants.JANUARY_1_2000.getMillis();
        long subtrahendInstant = DateTimeConstants.APRIL_1_2000.getMillis();
        int expected = 3;
        long result = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, result);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        long instant = DateTimeConstants.JANUARY_1_2000.getMillis();
        int month = DateTimeConstants.APRIL;
        long expected = DateTimeConstants.APRIL_1_2000.getMillis();
        long result = field.set(instant, month);
        assertEquals(expected, result);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        DurationField durationField = field.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), durationField.getFieldType());
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        long instant = DateTimeConstants.FEBRUARY_29_2000.getMillis();
        assertTrue(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        long instant = DateTimeConstants.FEBRUARY_29_2000.getMillis();
        int expected = 1;
        int result = field.getLeapAmount(instant);
        assertEquals(expected, result);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        DurationField durationField = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), durationField.getFieldType());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        int expected = DateTimeConstants.JANUARY;
        int result = field.getMinimumValue();
        assertEquals(expected, result);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        int expected = DateTimeConstants.DECEMBER;
        int result = field.getMaximumValue();
        assertEquals(expected, result);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        long instant = DateTimeConstants.JANUARY_31_2000.getMillis();
        long expected = DateTimeConstants.JANUARY_1_2000.getMillis();
        long result = field.roundFloor(instant);
        assertEquals(expected, result);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), JulianChronology.FEBRUARY);
        long instant = DateTimeConstants.JANUARY_31_2000.getMillis();
        long expected = 0;
        long result = field.remainder(instant);
        assertEquals(expected, result);
    }
}
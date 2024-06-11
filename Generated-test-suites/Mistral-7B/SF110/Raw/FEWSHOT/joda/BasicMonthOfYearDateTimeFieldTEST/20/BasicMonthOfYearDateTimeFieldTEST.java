package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertEquals(1, field.get(0));

        DateTime date = new DateTime(2022, 2, 1, ISOChronology.getInstance());
        assertEquals(2, field.get(date.getMillis()));

        date = new DateTime(2022, 1, 31, ISOChronology.getInstance());
        assertEquals(1, field.get(date.getMillis()));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        long initialMillis = new DateTime(2022, 2, 1, ISOChronology.getInstance()).getMillis();
        long resultMillis = field.add(initialMillis, 1);
        DateTime resultDateTime = new DateTime(resultMillis, ISOChronology.getInstance());
        assertEquals(3, field.get(resultMillis));
        assertEquals(new DateTime(2022, 3, 1, ISOChronology.getInstance()), resultDateTime);

        resultMillis = field.add(initialMillis, -1);
        resultDateTime = new DateTime(resultMillis, ISOChronology.getInstance());
        assertEquals(2, field.get(resultMillis));
        assertEquals(new DateTime(2022, 2, 1, ISOChronology.getInstance()), resultDateTime);

        resultMillis = field.add(initialMillis, 12);
        resultDateTime = new DateTime(resultMillis, ISOChronology.getInstance());
        assertEquals(3, field.get(resultMillis));
        assertEquals(new DateTime(2022, 3, 1, ISOChronology.getInstance()), resultDateTime);

        resultMillis = field.add(initialMillis, 13);
        resultDateTime = new DateTime(resultMillis, ISOChronology.getInstance());
        assertEquals(4, field.get(resultMillis));
        assertEquals(new DateTime(2022, 4, 1, ISOChronology.getInstance()), resultDateTime);

        resultMillis = field.add(initialMillis, -12);
        resultDateTime = new DateTime(resultMillis, ISOChronology.getInstance());
        assertEquals(1, field.get(resultMillis));
        assertEquals(new DateTime(2022, 2, 1, ISOChronology.getInstance()), resultDateTime);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        long initialMillis = new DateTime(2022, 12, 31, ISOChronology.getInstance()).getMillis();
        long resultMillis = field.addWrapField(initialMillis, 1);
        DateTime resultDateTime = new DateTime(resultMillis, ISOChronology.getInstance());
        assertEquals(1, field.get(resultMillis));
        assertEquals(new DateTime(2023, 1, 1, ISOChronology.getInstance()), resultDateTime);

        resultMillis = field.addWrapField(initialMillis, -1);
        resultDateTime = new DateTime(resultMillis, ISOChronology.getInstance());
        assertEquals(12, field.get(resultMillis));
        assertEquals(new DateTime(2022, 12, 31, ISOChronology.getInstance()), resultDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidArgument() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long initialMillis = new DateTime(2022, 2, 1, ISOChronology.getInstance()).getMillis();
        field.add(initialMillis, Integer.MAX_VALUE);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        long initialMillis1 = new DateTime(2022, 2, 1, ISOChronology.getInstance()).getMillis();
        long initialMillis2 = new DateTime(2022, 1, 31, ISOChronology.getInstance()).getMillis();
        long difference = field.getDifferenceAsLong(initialMillis1, initialMillis2);
        assertEquals(1, difference);

        initialMillis1 = new DateTime(2022, 2, 1, ISOChronology.getInstance()).getMillis();
        initialMillis2 = new DateTime(2023, 3, 1, ISOChronology.getInstance()).getMillis();
        difference = field.getDifferenceAsLong(initialMillis1, initialMillis2);
        assertEquals(-11, difference);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        long initialMillis = new DateTime(2022, 2, 1, ISOChronology.getInstance()).getMillis();
        long resultMillis = field.set(initialMillis, 3);
        DateTime resultDateTime = new DateTime(resultMillis, ISOChronology.getInstance());
        assertEquals(3, field.get(resultMillis));
        assertEquals(new DateTime(2022, 3, 1, ISOChronology.getInstance()), resultDateTime);

        resultMillis = field.set(initialMillis, 13);
        resultDateTime = new DateTime(resultMillis, ISOChronology.getInstance());
        assertEquals(4, field.get(resultMillis));
        assertEquals(new DateTime(2022, 4, 1, ISOChronology.getInstance()), resultDateTime);

        resultMillis = field.set(initialMillis, 0);
        resultDateTime = new DateTime(resultMillis, ISOChronology.getInstance());
        assertEquals(1, field.get(resultMillis));
        assertEquals(new DateTime(2022, 2, 1, ISOChronology.getInstance()), resultDateTime);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertEquals(DurationFieldType.years(), field.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertFalse(field.isLeap(new DateTime(2022, 2, 28, ISOChronology.getInstance()).getMillis()));
        assertFalse(field.isLeap(new DateTime(2022, 2, 29, ISOChronology.getInstance()).getMillis()));
        assertTrue(field.isLeap(new DateTime(2022, 2, 29, ISOChronology.getInstance(2022, 2, 29)).getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertEquals(0, field.getLeapAmount(new DateTime(2022, 2, 28, ISOChronology.getInstance()).getMillis()));
        assertEquals(0, field.getLeapAmount(new DateTime(2022, 2, 29, ISOChronology.getInstance()).getMillis()));
        assertEquals(1, field.getLeapAmount(new DateTime(2022, 2, 29, ISOChronology.getInstance(2022, 2, 29)).getMillis()));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertEquals(DurationFieldType.days(), field.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        long initialMillis = new DateTime(2022, 2, 1, ISOChronology.getInstance()).getMillis();
        long roundedMillis = field.roundFloor(initialMillis);
        DateTime roundedDateTime = new DateTime(roundedMillis, ISOChronology.getInstance());
        assertEquals(1, field.get(roundedMillis));
        assertEquals(new DateTime(2022, 2, 1, ISOChronology.getInstance()), roundedDateTime);

        initialMillis = new DateTime(2022, 3, 31, ISOChronology.getInstance()).getMillis();
        roundedMillis = field.roundFloor(initialMillis);
        roundedDateTime = new DateTime(roundedMillis, ISOChronology.getInstance());
        assertEquals(3, field.get(roundedMillis));
        assertEquals(new DateTime(2022, 3, 1, ISOChronology.getInstance()), roundedDateTime);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);

        long initialMillis = new DateTime(2022, 2, 1, ISOChronology.getInstance()).getMillis();
        long remainder = field.remainder(initialMillis);
        assertEquals(0, remainder);

        initialMillis = new DateTime(2022, 3, 31, ISOChronology.getInstance()).getMillis();
        remainder = field.remainder(initialMillis);
        assertEquals(0, remainder);

        initialMillis = new DateTime(2022, 2, 30, ISOChronology.getInstance()).getMillis();
        remainder = field.remainder(initialMillis);
        assertEquals(-1, remainder);

        initialMillis = new DateTime(2022, 2, 29, ISOChronology.getInstance()).getMillis();
        remainder = field.remainder(initialMillis);
        assertEquals(-1, remainder);
    }
}
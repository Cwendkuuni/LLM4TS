package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        DateTime testDateTime = new DateTime(2022, 2, 1, 0, 0, 0, 0, ISOChronology.getInstance());
        assertEquals(2, field.get(testDateTime.getMillis()));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        DateTime testDateTime = new DateTime(2022, 2, 1, 0, 0, 0, 0, ISOChronology.getInstance());
        long newMillis = field.add(testDateTime.getMillis(), 1);
        DateTime newDateTime = new DateTime(newMillis, ISOChronology.getInstance());
        assertEquals(3, field.get(newMillis));
        assertEquals(2022, newDateTime.getYear());
        assertEquals(3, newDateTime.getMonthOfYear());
        assertEquals(1, newDateTime.getDayOfMonth());

        testDateTime = new DateTime(2022, 12, 31, 0, 0, 0, 0, ISOChronology.getInstance());
        newMillis = field.add(testDateTime.getMillis(), 12);
        newDateTime = new DateTime(newMillis, ISOChronology.getInstance());
        assertEquals(1, field.get(newMillis));
        assertEquals(2023, newDateTime.getYear());
        assertEquals(1, newDateTime.getMonthOfYear());
        assertEquals(1, newDateTime.getDayOfMonth());

        testDateTime = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstance());
        newMillis = field.add(testDateTime.getMillis(), 13);
        newDateTime = new DateTime(newMillis, ISOChronology.getInstance());
        assertEquals(1, field.get(newMillis));
        assertEquals(2022, newDateTime.getYear());
        assertEquals(1, newDateTime.getMonthOfYear());
        assertEquals(1, newDateTime.getDayOfMonth());
    }

    @Test
    public void testAddWithLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        DateTime testDateTime = new DateTime(2022, 2, 1, 0, 0, 0, 0, ISOChronology.getInstance());
        long newMillis = field.add(testDateTime.getMillis(), 123456789);
        DateTime newDateTime = new DateTime(newMillis, ISOChronology.getInstance());
        assertEquals(3, field.get(newMillis));
        assertEquals(2022, newDateTime.getYear());
        assertEquals(3, newDateTime.getMonthOfYear());
        assertEquals(1, newDateTime.getDayOfMonth());
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        DateTime testDateTime = new DateTime(2022, 12, 31, 0, 0, 0, 0, ISOChronology.getInstance());
        long newMillis = field.addWrapField(testDateTime.getMillis(), 13);
        DateTime newDateTime = new DateTime(newMillis, ISOChronology.getInstance());
        assertEquals(1, field.get(newMillis));
        assertEquals(2023, newDateTime.getYear());
        assertEquals(1, newDateTime.getMonthOfYear());
        assertEquals(1, newDateTime.getDayOfMonth());
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        DateTime testDateTime1 = new DateTime(2022, 2, 1, 0, 0, 0, 0, ISOChronology.getInstance());
        DateTime testDateTime2 = new DateTime(2022, 3, 1, 0, 0, 0, 0, ISOChronology.getInstance());
        long difference = field.getDifferenceAsLong(testDateTime1.getMillis(), testDateTime2.getMillis());
        assertEquals(1, difference);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long testMillis = new DateTime(2022, 2, 1, 0, 0, 0, 0, ISOChronology.getInstance()).getMillis();
        long newMillis = field.set(testMillis, 3);
        DateTime newDateTime = new DateTime(newMillis, ISOChronology.getInstance());
        assertEquals(3, field.get(newMillis));
        assertEquals(2022, newDateTime.getYear());
        assertEquals(3, newDateTime.getMonthOfYear());
        assertEquals(1, newDateTime.getDayOfMonth());
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
        DateTime testDateTime = new DateTime(2022, 2, 31, 0, 0, 0, 0, ISOChronology.getInstance());
        long roundedMillis = field.roundFloor(testDateTime.getMillis());
        DateTime roundedDateTime = new DateTime(roundedMillis, ISOChronology.getInstance());
        assertEquals(2, field.get(roundedMillis));
        assertEquals(2022, roundedDateTime.getYear());
        assertEquals(2, roundedDateTime.getMonthOfYear());
        assertEquals(28, roundedDateTime.getDayOfMonth());
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        DateTime testDateTime = new DateTime(2022, 2, 28, 0, 0, 0, 0, ISOChronology.getInstance());
        long remainderMillis = field.remainder(testDateTime.getMillis());
        DateTime remainderDateTime = new DateTime(remainderMillis, ISOChronology.getInstance());
        assertEquals(0, field.get(remainderMillis));
        assertEquals(2022, remainderDateTime.getYear());
        assertEquals(2, remainderDateTime.getMonthOfYear());
        assertEquals(28, remainderDateTime.getDayOfMonth());
        assertEquals(0, remainderMillis % (31 * 24 * 60 * 60 * 1000));
    }
}
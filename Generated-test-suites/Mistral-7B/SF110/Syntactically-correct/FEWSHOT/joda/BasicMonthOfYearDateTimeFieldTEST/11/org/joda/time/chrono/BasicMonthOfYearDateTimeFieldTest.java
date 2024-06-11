package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testNewInstance() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(-8258715387168736L, field.serialVersionUID, 0);
        assertEquals(chronology, field.iChronology);
        assertEquals(2, field.iLeapMonth);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        DateTime date = new DateTime(0, chronology);
        date = date.withFieldAdded(field, 1);
        assertEquals(1, field.get(date.getMillis()));

        date = date.withFieldAdded(field, -1);
        assertEquals(0, field.get(date.getMillis()));
    }

    @Test
    public void testAdd() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        DateTime date = new DateTime(0, chronology);
        long result = field.add(date.getMillis(), 1);
        DateTime resultDateTime = new DateTime(chronology, result);
        assertEquals(1, field.get(resultDateTime.getMillis()));

        result = field.add(date.getMillis(), -1);
        DateTime resultDateTime2 = new DateTime(chronology, result);
        assertEquals(0, field.get(resultDateTime2.getMillis()));

        date = new DateTime(0, chronology);
        result = field.add(date.getMillis(), 12);
        DateTime resultDateTime3 = new DateTime(chronology, result);
        assertEquals(12, field.get(resultDateTime3.getMillis()));

        date = new DateTime(0, chronology);
        result = field.add(date.getMillis(), 13);
        DateTime resultDateTime4 = new DateTime(chronology, result);
        assertEquals(1, field.get(resultDateTime4.getMillis())); // rolls over

        date = new DateTime(0, chronology);
        result = field.add(date.getMillis(), -13);
        DateTime resultDateTime5 = new DateTime(chronology, result);
        assertEquals(11, field.get(resultDateTime5.getMillis())); // rolls over
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        long rollOver = field.getMaximumValue() + 1;
        long wrapValue = FieldUtils.getWrappedValue(1, rollOver, 1, field.getMaximumValue());

        DateTime date = new DateTime(0, chronology);
        long result = field.addWrapField(date.getMillis(), rollOver);
        DateTime resultDateTime = new DateTime(chronology, result);
        assertEquals(wrapValue, field.get(resultDateTime.getMillis()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidAmount() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        DateTime date = new DateTime(0, chronology);
        long result = field.add(date.getMillis(), Integer.MAX_VALUE + 1);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        DateTime date1 = new DateTime(0, chronology);
        DateTime date2 = new DateTime(365 * 1000 * 24 * 60 * 60, chronology);

        long difference = field.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
        assertEquals(11, difference);
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        DateTime date = new DateTime(0, chronology);
        long result = field.set(date.getMillis(), 12);
        DateTime resultDateTime = new DateTime(chronology, result);
        assertEquals(12, field.get(resultDateTime.getMillis()));

        date = new DateTime(0, chronology);
        result = field.set(date.getMillis(), 0);
        DateTime resultDateTime2 = new DateTime(chronology, result);
        assertEquals(0, field.get(resultDateTime2.getMillis()));

        date = new DateTime(0, chronology);
        result = field.set(date.getMillis(), 13);
        DateTime resultDateTime3 = new DateTime(chronology, result);
        assertEquals(1, field.get(resultDateTime3.getMillis())); // rolls over

        date = new DateTime(0, chronology);
        result = field.set(date.getMillis(), 121);
        DateTime resultDateTime4 = new DateTime(chronology, result);
        assertEquals(1, field.get(resultDateTime4.getMillis())); // rolls over
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        DateTime date = new DateTime(0, chronology);

        assertTrue(field.isLeap(date.getMillis()));
        date = date.withFieldAdded(DateTimeFieldType.year(), 2000);
        assertFalse(field.isLeap(date.getMillis()));
        date = date.withFieldAdded(DateTimeFieldType.monthOfYear(), 2);
        assertTrue(field.isLeap(date.getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        DateTime date = new DateTime(0, chronology);

        assertEquals(0, field.getLeapAmount(date.getMillis()));
        date = date.withFieldAdded(DateTimeFieldType.year(), 2000);
        assertEquals(0, field.getLeapAmount(date.getMillis()));
        date = date.withFieldAdded(DateTimeFieldType.monthOfYear(), 2);
        assertEquals(1, field.getLeapAmount(date.getMillis()));
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        assertEquals(12, field.getMaximumValue());
    }
}
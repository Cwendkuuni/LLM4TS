package org.joda.time.chrono.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.Months;
import org.joda.time.chrono.ISOChronology;

import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

    @org.junit.Test
    public void testGet() {
        final Chronology chrono = new ISOChronology();
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        assertEquals(1, field.get(BaseDateTime.UTC.getMillis()));
        assertEquals(2, field.get(Days.UTC.plusDays(DateTimeConstants.DAYS_IN_FEBRUARY).getMillis()));
        assertEquals(12, field.get(Days.UTC.plusDays(DateTimeConstants.DAYS_IN_A_YEAR * 11).getMillis()));
    }

    @org.junit.Test
    public void testSet() {
        final Chronology chrono = new ISOChronology();
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        assertEquals(1, field.get(BaseDateTime.UTC.getMillis()));
        field.set(BaseDateTime.UTC.getMillis(), 13);
        assertEquals(DateTimeConstants.JANUARY, field.get(BaseDateTime.UTC.getMillis()));
        assertEquals(13, FieldUtils.getWrappedValue(1, 13, 1, 12));
    }

    @org.junit.Test
    public void testAdd() {
        final Chronology chrono = new ISOChronology();
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        assertEquals(BaseDateTime.UTC.getMillis(), field.add(BaseDateTime.UTC.getMillis(), 0));
        assertEquals(Days.UTC.plusDays(DateTimeConstants.DAYS_IN_JANUARY).getMillis(), field.add(BaseDateTime.UTC.getMillis(), 31));
        assertEquals(Days.UTC.plusDays(DateTimeConstants.DAYS_IN_JANUARY + DateTimeConstants.DAYS_IN_FEBRUARY).getMillis(), field.add(BaseDateTime.UTC.getMillis(), 61));
        assertEquals(Days.UTC.plusDays(DateTimeConstants.DAYS_IN_JANUARY + DateTimeConstants.DAYS_IN_FEBRUARY + DateTimeConstants.DAYS_IN_MARCH).getMillis(), field.add(BaseDateTime.UTC.getMillis(), 91));
        final long testDate = Days.UTC.plusDays(DateTimeConstants.DAYS_IN_JANUARY + DateTimeConstants.DAYS_IN_FEBRUARY + DateTimeConstants.DAYS_IN_MARCH).getMillis();
        assertThrows(IllegalArgumentException.class, () -> field.add(BaseDateTime.UTC.getMillis(), 123));
        assertThrows(IllegalArgumentException.class, () -> field.add(testDate, -123));
    }

    @org.junit.Test
    public void testAddWrapField() {
        final Chronology chrono = new ISOChronology();
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        assertEquals(BaseDateTime.UTC.getMillis(), field.addWrapField(BaseDateTime.UTC.getMillis(), 0));
        assertEquals(Days.UTC.plusDays(DateTimeConstants.DAYS_IN_JANUARY).getMillis(), field.addWrapField(BaseDateTime.UTC.getMillis(), 31));
        assertEquals(Days.UTC.plusDays(DateTimeConstants.DAYS_IN_JANUARY + DateTimeConstants.DAYS_IN_FEBRUARY).getMillis(), field.addWrapField(BaseDateTime.UTC.getMillis(), 62));
        final long testDate = Days.UTC.plusDays(DateTimeConstants.DAYS_IN_JANUARY + DateTimeConstants.DAYS_IN_FEBRUARY + DateTimeConstants.DAYS_IN_MARCH).getMillis();
        assertEquals(testDate, field.addWrapField(testDate, 92));
    }

    @org.junit.Test
    public void testIsLeap() {
        final Chronology chrono = new ISOChronology();
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        assertFalse(field.isLeap(BaseDateTime.UTC.getMillis()));
        assertTrue(field.isLeap(Days.UTC.plusYears(2020).getMillis()));
        assertFalse(field.isLeap(Days.UTC.plusYears(2021).getMillis()));
    }

    @org.junit.Test
    public void testGetLeapAmount() {
        final Chronology chrono = new ISOChronology();
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        assertEquals(0, field.getLeapAmount(BaseDateTime.UTC.getMillis()));
        assertEquals(1, field.getLeapAmount(Days.UTC.plusYears(2020).getMillis()));
        assertEquals(0, field.getLeapAmount(Days.UTC.plusYears(2021).getMillis()));
    }

    @org.junit.Test
    public void testGetMaximumValue() {
        final Chronology chrono = new ISOChronology();
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        assertEquals(12, field.getMaximumValue());
    }
}
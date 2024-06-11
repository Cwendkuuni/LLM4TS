package org.joda.time.chrono.test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

import java.util.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @org.junit.Test
    public void testGet() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = new DateTime(2015, 2, 15).getMillis();
        int month = field.get(date);
        assertEquals(2, month);
    }

    @org.junit.Test
    public void testAdd() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = new DateTime(2015, 2, 15).getMillis();
        long newDate = field.add(date, 2);
        DateTime newDateTime = new DateTime(newDate);
        DateTime expectedDateTime = new DateTime(2015, 4, 15);
        assertEquals(expectedDateTime, newDateTime);
    }

    @org.junit.Test
    public void testAddWrapField() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = new DateTime(2015, 12, 31).getMillis();
        long newDate = field.addWrapField(date, 12);
        DateTime newDateTime = new DateTime(newDate);
        DateTime expectedDateTime = new DateTime(2016, 2, 29); // In leap year
        assertTrue(expectedDateTime.isBefore(newDateTime) || expectedDateTime.isEqual(newDateTime));
    }

    @org.junit.Test
    public void testAddLong() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = new DateTime(2015, 2, 15).getMillis();
        long newDate = field.add(date, 30 * 12 * 60 * 60 * 1000); // Thirty six months
        DateTime newDateTime = new DateTime(newDate);
        DateTime expectedDateTime = new DateTime(2018, 2, 15);
        assertEquals(expectedDateTime, newDateTime);
    }

    @org.junit.Test
    public void testAddLongOutOfRange() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = new DateTime(2015, 2, 15).getMillis();
        long newDate = field.add(date, Long.MAX_VALUE);
        assertTrue(Long.MIN_VALUE >= newDate);
    }

    @org.junit.Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
        int minimum = field.getMinimumValue();
        assertEquals(1, minimum);
    }

    @org.junit.Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
        int maximum = field.getMaximumValue();
        assertEquals(12, maximum);
    }

    @org.junit.Test
    public void testIsLeap() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = new DateTime(2016, 2, 29).getMillis();
        boolean isLeap = field.isLeap(date);
        assertTrue(isLeap);
    }

    @org.junit.Test
    public void testIsLeapFalse() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = new DateTime(2015, 2, 15).getMillis();
        boolean isLeap = field.isLeap(date);
        assertFalse(isLeap);
    }

    @org.junit.Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = new DateTime(2016, 2, 29).getMillis();
        int leapAmount = field.getLeapAmount(date);
        assertEquals(1, leapAmount);
    }

    @org.junit.Test
    public void testGetLeapAmountZero() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = new DateTime(2015, 1, 1).getMillis();
        int leapAmount = field.getLeapAmount(date);
        assertEquals(0, leapAmount);
    }

    @org.junit.Test
    public void testGetRangeDurationField() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals(DurationFieldType.years(), rangeDurationField.getType());
    }
}
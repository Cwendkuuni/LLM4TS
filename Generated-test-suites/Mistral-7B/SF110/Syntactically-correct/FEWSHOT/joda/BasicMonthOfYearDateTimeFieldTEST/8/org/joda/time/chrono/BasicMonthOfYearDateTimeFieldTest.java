package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
        assertEquals(chronology, field.getChronology());
        assertEquals(false, field.isLenient());
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(1, field.get(0L));
        assertEquals(2, field.get(1L));
        assertEquals(12, field.get(31200L));
        assertEquals(1, field.get(31539999999L));
    }

    @Test
    public void testAdd() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long testValue = 31539999999L;
        long result = field.add(testValue, 1);
        assertEquals(31540000000L, result);
        result = field.add(result, 11);
        assertEquals(31540100000L, result);
        result = field.add(result, -13);
        assertEquals(31539987001L, result);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long testValue = 31539999999L;
        long result = field.addWrapField(testValue, 13);
        assertEquals(31540100000L, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long testValue1 = 31539999999L;
        long testValue2 = 31540100000L;
        long result = field.getDifferenceAsLong(testValue1, testValue2);
        assertEquals(14 * 30 * 24 * 60 * 60 * 1000L, result);
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long testValue = 31539999999L;
        long result = field.set(testValue, 5);
        assertEquals(31539999999L + (5 - 1) * 30 * 24 * 60 * 60 * 1000, result);
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long testValue = 20000229L;
        boolean result = field.isLeap(testValue);
        assertFalse(result);
        testValue = 20000229L + 366 * 24 * 60 * 60 * 1000;
        result = field.isLeap(testValue);
        assertTrue(result);
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long testValue = 20000229L;
        int result = field.getLeapAmount(testValue);
        assertFalse(result > 0);
        testValue += 366 * 24 * 60 * 60 * 1000;
        result = field.getLeapAmount(testValue);
        assertEquals(1, result);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        DurationField result = field.getLeapDurationField();
        assertEquals(DurationFieldType.days(), result.getDataType());
        assertEquals(1, result.getLength());
    }
}
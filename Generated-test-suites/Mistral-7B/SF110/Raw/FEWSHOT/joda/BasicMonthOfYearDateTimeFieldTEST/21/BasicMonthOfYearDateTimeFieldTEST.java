package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.ISO_INSTANT, 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);

        long testDate = chronology.getMillis("2015-11-30");
        int result = field.get(testDate);
        assertEquals(11, result);
    }

    @Test
    public void testAdd() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);

        long testDate = chronology.getMillis("2015-11-30");
        long result = field.add(testDate, 1);
        assertEquals(chronology.getMillis("2015-12-31"), result);

        result = field.add(testDate, -1);
        assertEquals(chronology.getMillis("2015-10-31"), result);

        result = field.add(testDate, 13);
        assertEquals(chronology.getMillis("2016-01-31"), result);

        result = field.add(testDate, -12);
        assertEquals(chronology.getMillis("2015-10-31"), result);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);

        long testDate = chronology.getMillis("2015-11-30");
        long result = field.addWrapField(testDate, 13);
        assertEquals(chronology.getMillis("2016-01-31"), result);

        result = field.addWrapField(testDate, -13);
        assertEquals(chronology.getMillis("2015-10-31"), result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);

        long testDate1 = chronology.getMillis("2015-11-30");
        long testDate2 = chronology.getMillis("2015-10-31");
        long result = field.getDifferenceAsLong(testDate1, testDate2);
        assertEquals(1, result);

        testDate2 = chronology.getMillis("2015-11-31");
        result = field.getDifferenceAsLong(testDate1, testDate2);
        assertEquals(0, result);

        testDate2 = chronology.getMillis("2016-01-31");
        result = field.getDifferenceAsLong(testDate1, testDate2);
        assertEquals(2, result);
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);

        long testDate = chronology.getMillis("2015-11-30");
        long result = field.set(testDate, 12);
        assertEquals(chronology.getMillis("2015-12-31"), result);

        result = field.set(testDate, 0);
        assertEquals(chronology.getMillis("2015-11-30"), result);
    }

    @Test
    public void testAddWrapFieldWithNegativeValue() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);

        long testDate = chronology.getMillis("2015-11-30");
        long result = field.addWrapField(testDate, -13);
        assertEquals(chronology.getMillis("2015-10-31"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithOutOfRangeValue() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);

        long testDate = chronology.getMillis("2015-11-30");
        long result = field.add(testDate, 13 * 12 + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithOutOfRangeValueLong() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);

        long testDate = chronology.getMillis("2015-11-30");
        long result = field.add(testDate, Long.MAX_VALUE);
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);

        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);

        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);

        long testDate = chronology.getMillis("2015-02-29");
        assertTrue(field.isLeap(testDate));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);

        long testDate = chronology.getMillis("2015-02-29");
        assertEquals(1, field.getLeapAmount(testDate));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);

        DurationField result = field.getLeapDurationField();
        assertEquals(new Days(1), result);
    }
}
package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology gregorian = GregorianChronology.INSTANCE;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, GregorianChronology.JANUARY);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertEquals(365000L, field.getAverageMillisPerDay());
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());
        assertFalse(field.isLenient());
        assertNull(field.getRangeDurationField());
        assertNotNull(field.getLeapDurationField());
        assertEquals(1, field.getLeapAmount(DateTimeUtils.parseLocalDate("2000-02-29").getMillis()));
        assertEquals(0, field.getLeapAmount(DateTimeUtils.parseLocalDate("2000-02-28").getMillis()));
        assertEquals(1, field.getLeapAmount(DateTimeUtils.parseLocalDate("2000-02-29").getMillis()));
        assertEquals(0, field.getLeapAmount(DateTimeUtils.parseLocalDate("2000-02-28").getMillis()));
    }

    @Test
    public void testGet() {
        BasicChronology gregorian = GregorianChronology.INSTANCE;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, GregorianChronology.JANUARY);

        long date1 = DateTimeUtils.parseLocalDate("2000-01-31").getMillis();
        int month1 = field.get(date1);
        assertEquals(1, month1);

        long date2 = DateTimeUtils.parseLocalDate("2000-02-28").getMillis();
        int month2 = field.get(date2);
        assertEquals(2, month2);

        long date3 = DateTimeUtils.parseLocalDate("2000-02-29").getMillis();
        int month3 = field.get(date3);
        assertEquals(2, month3);

        long date4 = DateTimeUtils.parseLocalDate("2000-03-31").getMillis();
        int month4 = field.get(date4);
        assertEquals(3, month4);
    }

    @Test
    public void testAdd() {
        BasicChronology gregorian = GregorianChronology.INSTANCE;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, GregorianChronology.JANUARY);

        long date1 = DateTimeUtils.parseLocalDate("2000-01-31").getMillis();
        long date2 = field.add(date1, 1);
        long date3 = field.add(date2, 12);

        assertEquals("2001-01-31", DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTimeUtils.asLocalDate(date1)));
        assertEquals("2000-12-31", DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTimeUtils.asLocalDate(date2)));
        assertEquals("2001-01-31", DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTimeUtils.asLocalDate(date3)));
    }

    @Test
    public void testAddWrapField() {
        BasicChronology gregorian = GregorianChronology.INSTANCE;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, GregorianChronology.JANUARY);

        long date1 = DateTimeUtils.parseLocalDate("2000-01-31").getMillis();
        long date2 = field.addWrapField(date1, 13);

        assertEquals("2000-01-31", DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTimeUtils.asLocalDate(date1)));
        assertEquals("2001-01-31", DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTimeUtils.asLocalDate(date2)));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology gregorian = GregorianChronology.INSTANCE;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, GregorianChronology.JANUARY);

        long date1 = DateTimeUtils.parseLocalDate("2000-01-31").getMillis();
        long date2 = DateTimeUtils.parseLocalDate("2000-02-28").getMillis();
        long difference = field.getDifferenceAsLong(date1, date2);

        assertEquals(29, difference);
    }

    @Test
    public void testSet() {
        BasicChronology gregorian = GregorianChronology.INSTANCE;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, GregorianChronology.JANUARY);

        long date1 = DateTimeUtils.parseLocalDate("2000-01-31").getMillis();
        long date2 = field.set(date1, 2);

        assertEquals("2000-02-28", DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTimeUtils.asLocalDate
                (date1)));
        assertEquals("2000-02-29", DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTimeUtils.asLocalDate(date2)));
    }

    @Test
    public void testIsLeap() {
        BasicChronology gregorian = GregorianChronology.INSTANCE;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, GregorianChronology.FEBRUARY);

        long date1 = DateTimeUtils.parseLocalDate("2000-02-28").getMillis();
        long date2 = DateTimeUtils.parseLocalDate("2000-02-29").getMillis();

        assertTrue(field.isLeap(date2));
        assertFalse(field.isLeap(date1));
    }
}
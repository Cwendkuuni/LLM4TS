package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.BasicDateTimeChronology;
import org.joda.time.field.*;
import org.joda.time.util.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        assertEquals(2, field.getMaximumValue());
        assertEquals(1, field.getMinimumValue());
        assertFalse(field.isLenient());
        assertNull(field.add(Long.MIN_VALUE, 1));
        assertNull(field.add(Long.MAX_VALUE, 1));
        assertEquals(1, field.get(Long.MIN_VALUE));
        assertEquals(12, field.get(Long.MAX_VALUE - 1));
        assertEquals(2, field.get(Long.MAX_VALUE));
        assertEquals(2, field.getLeapAmount(Long.MAX_VALUE));
        assertEquals(1, field.getLeapAmount(Long.MIN_VALUE));
        assertEquals(DurationFieldType.days(), field.getRangeDurationField());
        assertEquals(DurationFieldType.days(), field.getLeapDurationField());
        assertFalse(field.isLeap(Long.MIN_VALUE));
        assertTrue(field.isLeap(Long.MAX_VALUE - 1));
        assertFalse(field.isLeap(Long.MAX_VALUE));
    }

    @Test
    public void testAdd() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date = DateTimeConstants.MINUTE_OF_DAY;
        long result = field.add(date, 1);
        assertEquals(DateTimeConstants.DAY_OF_MONTH, result);
        result = field.add(result, 28);
        assertEquals(DateTimeConstants.DAY_OF_MONTH + 28, result);
        result = field.add(result, -1);
        assertEquals(DateTimeConstants.DAY_OF_MONTH + 27, result);
        result = field.add(result, -10);
        assertEquals(DateTimeConstants.DAY_OF_MONTH + 17, result);

        date = DateTimeConstants.DAY_OF_YEAR;
        result = field.add(date, 12);
        assertEquals(DateTimeConstants.DAY_OF_YEAR + 12, result);
        result = field.add(result, -11);
        assertEquals(DateTimeConstants.DAY_OF_YEAR - 1, result);

        date = DateTimeConstants.MILLIS_PER_DAY;
        result = field.add(date, 365 * 100);
        assertEquals(365 * 100L * DateTimeConstants.MILLIS_PER_DAY, result);
        result = field.add(result, -Math.abs(365 * 100));
        assertEquals(0L, result);

        date = DateTimeConstants.MILLIS_PER_DAY;
        result = field.add(date, 400 * 100 - 1);
        assertEquals((400 * 100 - 1) * DateTimeConstants.MILLIS_PER_DAY, result);
        result = field.add(result, 1);
        assertEquals((400 * 100) * DateTimeConstants.MILLIS_PER_DAY, result);

        result = field.add(Long.MIN_VALUE, 1);
        assertEquals(Long.MIN_VALUE + 1, result);
        result = field.add(Long.MAX_VALUE, 1);
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testSet() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date = Long.MIN_VALUE;
        long result = field.set(date, 1);
        assertEquals(1L, result);
        result = field.set(result, 30);
        assertEquals(1L + 30L, result);
        result = field.set(result, 61);
        assertEquals(1L + 30L + 30L, result);
        result = field.set(result, -2);
        assertEquals(1L + 30L + 30L - 2L, result);

        date = Long.MAX_VALUE - 1;
        result = field.set(date, 12);
        assertEquals(12L, result);
        result = field.set(result, -1);
        assertEquals(11L, result);
        result = field.set(result, 13);
        assertEquals(12L + 1L, result);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date = Long.MIN_VALUE;
        long result = field.addWrapField(date, 1);
        assertEquals(Long.MIN_VALUE + 1, result);
        result = field.addWrapField(result, 123);
        assertEquals(Long.MIN_VALUE + 123, result);
        result = field.addWrapField(result, -124);
        assertEquals(Long.MIN_VALUE, result);

        date = Long.MAX_VALUE - 1;
        result = field.addWrapField(date, -1);
        assertEquals(Long.MAX_VALUE - 1 + this.iChronology.getMillisOfADay(), result);
        result = field.addWrapField(result, 12345);
        assertEquals(Long.MAX_VALUE + 12345 - this.iChronology.getMillisOfADay(), result);
        result = field.addWrapField(result, -6060606060L);
        assertEquals(Long.MAX_VALUE - 6060606060L + this.iChronology.getMillisOfADay(), result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date1 = 2000 - 01 - 31;
        long date2 = 2000 - 01 - 28;
        long difference = field.getDifferenceAsLong(date1, date2);
        assertEquals(1L, difference);

        date1 = 2000 - 02 - 29;
        date2 = 2000 - 02 - 28;
        difference = field.getDifferenceAsLong(date1, date2);
        assertEquals(1L, difference);

        date1 = 2000 - 02 - 28;
        date2 = 2000 - 02 - 28;
        difference = field.getDifferenceAsLong(date1, date2);
        assertEquals(0L, difference);

        date1 = 2000 - 02 - 28;
        date2 = 2001 - 01 - 01;
        difference = field.getDifferenceAsLong(date1, date2);
        assertEquals(12L * 30L + 28L, difference);

        date1 = 2000 - 02 - 28;
        date2 = 2100 - 02 - 28;
        difference = field.getDifferenceAsLong(date1, date2);
        assertEquals((2100 - 2000) * 365L * 12L + 28L, difference);

        date1 = Long.MAX_VALUE;
        date2 = Long.MIN_VALUE;
        try {
            difference = field.getDifferenceAsLong(date1, date2);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddWrapFieldWithNegativeValue() {
        BasicDateTimeChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date = Long.MAX_VALUE - 1;
        long result = field.addWrapField(date, -1);
        assertEquals(date - 1 + gregorian.getMillisOfADay(), result);
    }

    @Test
    public void testRoundFloor() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date = DateTimeConstants.DAY_OF_MONTH;
        long result = field.roundFloor(date);
        assertEquals(DateTimeConstants.DAY_OF_MONTH, result);

        date += 1;
        result = field.roundFloor(date);
        assertEquals(DateTimeConstants.DAY_OF_MONTH, result);

        date += 1;
        result = field.roundFloor(date);
        assertEquals(DateTimeConstants.DAY_OF_MONTH, result);

        date += 1;
        result = field.roundFloor(date);
        assertEquals(DateTimeConstants.DAY_OF_MONTH + 1, result);

        date -= 1;
        result = field.roundFloor(date);
        assertEquals(DateTimeConstants.DAY_OF_MONTH, result);
    }

    @Test
    public void testRemainder() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date = DateTimeConstants.DAY_OF_MONTH;
        long result = field.remainder(date);
        assertEquals(0L, result);

        date += 1;
        result = field.remainder(date);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY % field.iChronology.getMillisPerDay(), result);

        date += 1;
        result = field.remainder(date);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY % field.iChronology.getMillisPerDay(), result);

        date += 1;
        result = field.remainder(date);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY % field.iChronology.getMillisPerDay(), result);

        date += 1;
        result = field.remainder(date);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY % field.iChronology.getMillisPerDay() - 1, result);
    }
}
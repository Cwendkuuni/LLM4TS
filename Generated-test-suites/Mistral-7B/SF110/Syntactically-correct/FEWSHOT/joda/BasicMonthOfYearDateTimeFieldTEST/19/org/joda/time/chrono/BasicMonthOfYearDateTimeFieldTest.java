package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.field.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeFieldConstruction() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(chronology, field.getChronology());
        assertEquals(2, field.getLeapMonth());
    }

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
        DateTime date = new DateTime(2015, 2, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(2, field.get(date.getMillis()));

        date = new DateTime(2015, 2, 29, 0, 0, 0, 0, new GregorianChronology());
        assertTrue(field.isLeap(date.getMillis()));
        assertEquals(2, field.getLeapAmount(date.getMillis()));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);

        DateTime date = new DateTime(2015, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        long result = field.add(date.getMillis(), 1);
        DateTime expected = new DateTime(2015, 2, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        result = field.add(date.getMillis(), 1);
        expected = new DateTime(2016, 1, 31, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        result = field.add(date.getMillis(), -1);
        expected = new DateTime(2015, 11, 30, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 2, 29, 0, 0, 0, 0, new GregorianChronology());
        result = field.add(date.getMillis(), 2);
        expected = new DateTime(2015, 4, 30, 0, 0, 0, 0, new GregorianChronology());
        assertTrue(expected.isSet(DateTimeConstants.MILLIS_IN_DAY));
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 2, 28, 0, 0, 0, 0, new GregorianChronology());
        result = field.add(date.getMillis(), 2);
        expected = new DateTime(2015, 4, 29, 0, 0, 0, 0, new GregorianChronology());
        assertTrue(expected.isSet(DateTimeConstants.MILLIS_IN_DAY));
        assertEquals(expected.getMillis(), result);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);

        DateTime date = new DateTime(2015, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        long result = field.addWrapField(date.getMillis(), 12);
        DateTime expected = new DateTime(2016, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        result = field.addWrapField(date.getMillis(), -12);
        expected = new DateTime(2014, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);

        DateTime date1 = new DateTime(2015, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        DateTime date2 = new DateTime(2015, 3, 31, 0, 0, 0, 0, new GregorianChronology());
        long result = field.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
        assertEquals(1, result);

        date1 = new DateTime(2015, 3, 31, 0, 0, 0, 0, new GregorianChronology());
        date2 = new DateTime(2015, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        result = field.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
        assertEquals(-1, result);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);

        DateTime date = new DateTime(2015, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        long result = field.set(date.getMillis(), 12);
        DateTime expected = new DateTime(2015, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        result = field.set(date.getMillis(), 2);
        expected = new DateTime(2016, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        result = field.set(date.getMillis(), 13);
        expected = new DateTime(2016, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 2, 29, 0, 0, 0, 0, new GregorianChronology());
        result = field.set(date.getMillis(), 13);
        expected = new DateTime(2016, 3, 31, 0, 0, 0, 0, new GregorianChronology());
        assertTrue(expected.isSet(DateTimeConstants.MILLIS_IN_DAY));
        assertEquals(expected.getMillis(), result);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
        assertEquals(DurationFieldType.months(), field.getRangeDurationField().getType());
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);

        DateTime date = new DateTime(2015, 2, 1, 0, 0, 0, 0, new GregorianChronology());
        assertFalse(field.isLeap(date.getMillis()));

        date = new DateTime(2016, 2, 1, 0, 0, 0, 0, new GregorianChronology());
        assertTrue(field.isLeap(date.getMillis()));

        date = new DateTime(2016, 2, 29, 0, 0, 0, 0, new GregorianChronology());
        assertTrue(field.isLeap(date.getMillis()));

        date = new DateTime(2015, 2, 29, 0, 0, 0, 0, new GregorianChronology());
        assertFalse(field.isLeap(date.getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);

        DateTime date = new DateTime(2015, 2, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(0, field.getLeapAmount(date.getMillis()));

        date = new DateTime(2016, 2, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(1, field.getLeapAmount(date.getMillis()));

        date = new DateTime(2016, 2, 29, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(1, field.getLeapAmount(date.getMillis()));

        date = new DateTime(2015, 2, 29, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(0, field.getLeapAmount(date.getMillis()));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
        assertEquals(DurationFieldType.days(), field.getLeapDurationField().getType());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);

        DateTime date = new DateTime(2015, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        long result = field.roundFloor(date.getMillis());
        DateTime expected = new DateTime(2015, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 2, 1, 0, 0, 0, 0, new GregorianChronology());
        result = field.roundFloor(date.getMillis());
        expected = new DateTime(2015, 2, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 3, 1, 0, 0, 0, 0, new GregorianChronology());
        result = field.roundFloor(date.getMillis());
        expected = new DateTime(2015, 3, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        result = field.roundFloor(date.getMillis());
        expected = new DateTime(2015, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 12, 31, 23, 59, 59, 999, new GregorianChronology());
        result = field.roundFloor(date.getMillis());
        expected = new DateTime(2016, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);

        date = new DateTime(2015, 12, 31, 23, 59, 58, 998, new GregorianChronology());
        result = field.roundFloor(date.getMillis());
        expected = new DateTime(2015, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        assertEquals(expected.getMillis(), result);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);

        DateTime date = new DateTime(2015, 1, 1, 0, 0, 0, 0, new GregorianChronology());
        long result = field.remainder(date.getMillis());
        assertEquals(0, result);

        date = new DateTime(2015, 2, 1, 0, 0, 0, 0, new GregorianChronology());
        result = field.remainder(date.getMillis());
        assertEquals(0, result);

        date = new DateTime(2015, 3, 1, 0, 0, 0, 0, new GregorianChronology());
        result = field.remainder(date.getMillis());
        assertEquals(0, result);

        date = new DateTime(2015, 12, 31, 0, 0, 0, 0, new GregorianChronology());
        result = field.remainder(date.getMillis());
        assertEquals(0, result);

        date = new DateTime(2015, 12, 31, 23, 59, 59, 999, new GregorianChronology());
        result = field.remainder(date.getMillis());
        assertEquals(0, result);

        date = new DateTime(2015, 12, 31, 23, 59, 58, 998, new GregorianChronology());
        result = field.remainder(date.getMillis());
        assertEquals(-1, result);
    }
}
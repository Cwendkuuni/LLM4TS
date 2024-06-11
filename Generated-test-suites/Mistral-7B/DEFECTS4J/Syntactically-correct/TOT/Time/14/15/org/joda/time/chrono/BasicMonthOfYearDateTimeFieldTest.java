package org.joda.time.chrono;

import org.joda.time.BasicDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.ReadablePartial;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class BasicMonthOfYearDateTimeFieldTest {

    private Chronology gregorian;
    private BasicMonthOfYearDateTimeField monthOfYear;

    @Before
    public void setUp() {
        gregorian = DateTimeConstants.GREGORIAN;
        monthOfYear = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
    }

    @Test
    public void testGetMinValue() {
        assertEquals(DateTimeConstants.JANUARY, monthOfYear.getMinimumValue());
    }

    @Test
    public void testGetMaxValue() {
        assertEquals(DateTimeConstants.DECEMBER, monthOfYear.getMaximumValue());
    }

    @Test
    public void testGet() {
        long instant = new BasicDateTime(1546304000000L, gregorian).getMillis();
        assertEquals(DateTimeConstants.FEBRUARY, monthOfYear.get(instant));

        instant = new BasicDateTime(1546323200000L).getMillis();
        assertEquals(DateTimeConstants.MARCH, monthOfYear.get(instant));
    }

    @Test
    public void testAdd() {
        long instant = new BasicDateTime(1546304000000L).getMillis();
        long result = monthOfYear.add(instant, 3);
        assertEquals(new BasicDateTime(1546340800000L).getMillis(), result);

        result = monthOfYear.add(instant, -3);
        assertEquals(new BasicDateTime(1546267200000L).getMillis(), result);
    }

    @Test
    public void testAddWrapField() {
        long instant = new BasicDateTime(1546304000000L).getMillis();
        long result = monthOfYear.addWrapField(instant, 13);
        assertEquals(new BasicDateTime(1546374400000L).getMillis(), result);

        result = monthOfYear.addWrapField(instant, -12);
        assertEquals(new BasicDateTime(1546226800000L).getMillis(), result);
    }

    @Test
    public void testSet() {
        long instant = new BasicDateTime(1546304000000L).getMillis();
        long result = monthOfYear.set(instant, DateTimeConstants.APRIL);
        assertEquals(new BasicDateTime(1546345600000L).getMillis(), result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long subtrahend = new BasicDateTime(1546304000000L).getMillis();
        long minuend = new BasicDateTime(1546345600000L).getMillis();
        long result = monthOfYear.getDifferenceAsLong(minuend, subtrahend);
        assertEquals(2L, result);
    }

    @Test
    public void testIsLeap() {
        long instant = new BasicDateTime(1600010100L).getMillis();
        assertTrue(monthOfYear.isLeap(instant));

        instant = new BasicDateTime(1500010100L).getMillis();
        assertFalse(monthOfYear.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        long instant = new BasicDateTime(1600010100L).getMillis();
        assertEquals(1, monthOfYear.getLeapAmount(instant));
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField result = monthOfYear.getLeapDurationField();
        assertEquals(DurationFieldType.days(), result.getFieldType());
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField result = monthOfYear.getRangeDurationField();
        assertEquals(DurationFieldType.years(), result.getFieldType());
    }

    @Test
    public void testRoundFloor() {
        long instant = new BasicDateTime(1546304000000L).getMillis();
        long result = monthOfYear.roundFloor(instant);
        assertEquals(new BasicDateTime(1546307200000L).getMillis(), result);
    }

    @Test
    public void testRemainder() {
        long instant = new BasicDateTime(1546304000000L).getMillis();
        long result = monthOfYear.remainder(instant);
        assertEquals(0L, result);

        instant = new BasicDateTime(1546307200000L).getMillis();
        result = monthOfYear.remainder(instant);
        assertEquals(7200000L, result);
    }

    @Test
    public void testAddWithLeapYear() {
        long instant = new BasicDateTime(1600010100L).getMillis();
        long result = monthOfYear.add(instant, 1);
        assertEquals(new BasicDateTime(1600020100L).getMillis(), result);

        result = monthOfYear.add(instant, DateTimeConstants.FEBRUARY);
        assertEquals(new BasicDateTime(1600022900L).getMillis(), result);
    }

    @Test
    public void testAddWithLeapYearNegative() {
        long instant = new BasicDateTime(1600022900L).getMillis();
        long result = monthOfYear.add(instant, -1);
        assertEquals(new BasicDateTime(1600020100L).getMillis(), result);

        result = monthOfYear.add(instant, -DateTimeConstants.FEBRUARY);
        assertEquals(new BasicDateTime(1600010100L).getMillis(), result);
    }

    @Test
    public void testAddMonths() {
        long instant = new BasicDateTime(1546304000000L).getMillis();
        long result = monthOfYear.addWrapField(instant, 36);
        assertEquals(new BasicDateTime(1546467200000L).getMillis(), result);

        result = monthOfYear.addWrapField(instant, -36);
        assertEquals(new BasicDateTime(1546226800000L).getMillis(), result);
    }

    @Test
    public void testAddMonthsWithLeapYear() {
        long instant = new BasicDateTime(1600010100L).getMillis();
        long result = monthOfYear.addWrapField(instant, 13);
        assertEquals(new BasicDateTime(1600042900L).getMillis(), result);

        result = monthOfYear.addWrapField(instant, -13);
        assertEquals(new BasicDateTime(1600020100L).getMillis(), result);
    }

    @Test
    public void testAddMonthsWithNegativeValue() {
        long instant = new BasicDateTime(1546304000000L).getMillis();
        long result = monthOfYear.addWrapField(instant, -12);
        assertEquals(new BasicDateTime(1546226800000L).getMillis(), result);

        result = monthOfYear.addWrapField(instant, 12);
        assertEquals(new BasicDateTime(1546345600000L).getMillis(), result);
    }

    @Test
    public void testAddMonthsWithNegativeValueLeapYear() {
        long instant = new BasicDateTime(1600010100L).getMillis();
        long result = monthOfYear.addWrapField(instant, -12);
        assertEquals(new BasicDateTime(1599123100L).getMillis(), result);

        result = monthOfYear.addWrapField(instant, 12);
        assertEquals(new BasicDateTime(1601010100L).getMillis(), result);
    }

    @Test
    public void testAddMonthsToImmutableDateTime() {
        LocalDateTime dt = new LocalDateTime(2012, 11, 15, 14, 30);
        LocalDateTime result = monthOfYear.addWrapField(dt.toMillis(), 2);
        assertEquals(new LocalDateTime(2013, 01, 15, 14, 30).getMillis(), result.getMillis());
    }

    @Test
    public void testAddMonthsToImmutableDateTimeNegative() {
        LocalDateTime dt = new LocalDateTime(2012, 11, 15, 14, 30);
        LocalDateTime result = monthOfYear.addWrapField(dt.toMillis(), -2);
        assertEquals(new LocalDateTime(2012, 09, 15, 14, 30).getMillis(), result.getMillis());
    }

    @Test
    public void testAddMonthsToImmutableDate() {
        LocalDate date = new LocalDate(2012, 11, 15);
        LocalDate result = monthOfYear.addWrapField(date.toDate().getTime(), 2);
        assertEquals(new LocalDate(2013, 01, 15).toDate().getTime(), result.toDate().getTime());
    }

    @Test
    public void testAddMonthsToImmutableDateNegative() {
        LocalDate date = new LocalDate(2012, 11, 15);
        LocalDate result = monthOfYear.addWrapField(date.toDate().getTime(), -2);
        assertEquals(new LocalDate(2012, 09, 15).toDate().getTime(), result.toDate().getTime());
    }

    @Test
    public void testAddMonthsToReadablePartial() {
        ReadablePartial readablePartial = new LocalDate(2012, 11, 15).toDateMidnight().withFieldAdded(Months.MONTHS, 2);
        ReadablePartial result = monthOfYear.addWrapField(readablePartial.getMillis(), 2);
        assertEquals(new LocalDate(2013, 01, 15).toDateMidnight().getMillis(), result.getMillis());
    }

    @Test
    public void testAddMonthsToReadablePartialNegative() {
        ReadablePartial readablePartial = new LocalDate(2012, 11, 15).toDateMidnight().withFieldAdded(Months.MONTHS, -2);
        ReadablePartial result = monthOfYear.addWrapField(readablePartial.getMillis(), -2);
        assertEquals(new LocalDate(2012, 09, 15).toDateMidnight().getMillis(), result.getMillis());
    }
}
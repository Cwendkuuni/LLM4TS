package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test of the between method with Instant instant types and DurationFieldType.
     */
    @Test
    public void testBetweenInstant() {
        Chronology iso = ISOChronology.getInstanceUTC();
        ReadableInstant start = new BaseDateTime(0L, iso).withZone(DateTimeZone.UTC);
        ReadableInstant end = new BaseDateTime(3600000L, iso).withZone(DateTimeZone.UTC);
        int hours = BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours());
        assertEquals(1, hours);
    }

    /**
     * Test of the between method with Instant instant types and invalid DurationFieldType.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInstantInvalidType() {
        Chronology iso = ISOChronology.getInstanceUTC();
        ReadableInstant start = new BaseDateTime(0L, iso).withZone(DateTimeZone.UTC);
        ReadableInstant end = new BaseDateTime(3600000L, iso).withZone(DateTimeZone.UTC);
        int minutes = BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours());
    }

    /**
     * Test of the between method with Partial types and Period zeroInstance.
     */
    @Test
    public void testBetweenPartial() {
        LocalDate start = new LocalDate();
        LocalDate end = new LocalDate().withDayOfMonth(1).plusDays(1);
        Period zero = Period.ZERO;
        int days = BaseSingleFieldPeriod.between(start, end, zero);
        assertEquals(1, days);
    }

    /**
     * Test of the between method with Partial types and invalid zeroInstance.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenPartialInvalidZero() {
        LocalDate start = new LocalDate();
        LocalDate end = new LocalDate().withDayOfMonth(1).plusDays(1);
        Period zero = new Period(2, 3, 4, 5);
        int days = BaseSingleFieldPeriod.between(start, end, zero);
    }

    /**
     * Test of the standardPeriodIn method with Period and valid millisPerUnit.
     */
    @Test
    public void testStandardPeriodIn() {
        DurationFieldType hours = DurationFieldType.hours();
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8, 9);
        long millisPerUnit = 1000 * 60 * 60 * 24; // 1 day
        int hoursPeriod = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(1 + 2, hoursPeriod);
    }

    /**
     * Test of the standardPeriodIn method with Period and invalid millisPerUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidMillisPerUnit() {
        DurationFieldType hours = DurationFieldType.hours();
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8, 9);
        long millisPerUnit = 1000 * 60; // 1 minute
        int hoursPeriod = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    /**
     * Test of the constructor with valid period.
     */
    @Test
    public void testConstructor() {
        DurationFieldType hours = DurationFieldType.hours();
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8, 9);
        BaseSingleFieldPeriod test = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(period.getMillis(), 0L));
        assertEquals(hours, test.getFieldType());
        assertEquals(PeriodType.standard(), test.getPeriodType());
        assertEquals(1 + 2, test.getValue());
    }

    /**
     * Test of the getValue method with valid and invalid index.
     */
    @Test
    public void testGetValue() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        assertEquals(1 + 2, test.getValue());
        try {
            test.getValue(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
        }
    }

    /**
     * Test of the getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        assertEquals(hours, test.getFieldType());
    }

    /**
     * Test of the size method.
     */
    @Test
    public void testSize() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        assertEquals(1, test.size());
    }

    /**
     * Test of the toPeriod method.
     */
    @Test
    public void testToPeriod() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        Period period = test.toPeriod();
        assertEquals(hours, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1 + 2, period.getHours());
    }

    /**
     * Test of the toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        MutablePeriod mutablePeriod = test.toMutablePeriod();
        mutablePeriod.add(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertEquals(hours, mutablePeriod.getFieldType());
        assertEquals(PeriodType.standard(), mutablePeriod.getPeriodType());
        assertEquals(1 + 2 + 3, mutablePeriod.getHours());
    }

    /**
     * Test of the equals method with valid objects.
     */
    @Test
    public void testEquals() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test1 = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        BaseSingleFieldPeriod test2 = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        assertEquals(test1, test2);
    }

    /**
     * Test of the equals method with null object.
     */
    @Test
    public void testEqualsNull() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        assertFalse(test.equals(null));
    }

    /**
     * Test of the equals method with different objects.
     */
    @Test
    public void testEqualsDifferent() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test1 = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        BaseSingleFieldPeriod test2 = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 3, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        assertFalse(test1.equals(test2));
    }

    /**
     * Test of the hashCode method.
     */
    @Test
    public void testHashCode() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        int hashCode = test.hashCode();
        BaseSingleFieldPeriod test2 = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        assertEquals(hashCode, test2.hashCode());
    }

    /**
     * Test of the compareTo method with valid objects.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test1 = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        BaseSingleFieldPeriod test2 = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 3, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        assertGreater(test1, test2);
    }

    /**
     * Test of the compareTo method with equal objects.
     */
    @Test
    public void testCompareToEqual() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test1 = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        BaseSingleFieldPeriod test2 = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        assertEquals(0, test1.compareTo(test2));
    }

    /**
     * Test of the compareTo method with null object.
     */
    @Test(expected = NullPointerException.class)
    public void testCompareToNull() {
        DurationFieldType hours = DurationFieldType.hours();
        BaseSingleFieldPeriod test = new BaseSingleFieldPeriod(hours.getField(ISOChronology.getInstanceUTC()).getDifference(new Period(1, 2, 3, 4, 5, 6, 7, 8, 9).getMillis(), 0L));
        test.compareTo(null);
    }
}
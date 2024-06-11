package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        // Test with ReadableInstant
        ReadableInstant start = DateTimeUtils.parseLocalDateTime("1972-01-01T00:00:00.000Z").toInstant();
        ReadableInstant end = DateTimeUtils.parseLocalDateTime("1972-01-02T00:00:01.000Z").toInstant();
        assertEquals(24 * 60 * 60 * 1000, BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS));

        // Test with ReadablePartial
        ReadablePartial startPartial = new LocalDate();
        ReadablePartial endPartial = new LocalDate().plusDays(1);
        assertEquals(24 * 60 * 60 * 1000, BaseSingleFieldPeriod.between(startPartial, endPartial, new Period(1, 0, 0, 0, 0).toPeriod()));

        // Test with null inputs
        assertNull(BaseSingleFieldPeriod.between(null, null, DurationFieldType.HOURS));
        assertNull(BaseSingleFieldPeriod.between(start, null, DurationFieldType.HOURS));
        assertNull(BaseSingleFieldPeriod.between(null, end, DurationFieldType.HOURS));
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(7, 23, 59, 59, 59, 59, 59);
        assertEquals(604800000, BaseSingleFieldPeriod.standardPeriodIn(period, 1000));

        // Test with null period
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 1000));
    }

    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertEquals(123, period.getValue());

        // Test with wrong constructor arguments
        try {
            new BaseSingleFieldPeriod(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("period must not be null", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(456);
        assertEquals(456, period.getValue());

        // Test with null getters
        assertNull(new BaseSingleFieldPeriod(0).getFieldType(1));
        assertNull(new BaseSingleFieldPeriod(0).getPeriodType().getField(null));
    }

    @Test
    public void testGetFieldType() {
        DurationFieldType fieldType = DurationFieldType.HOURS;
        BaseSingleFieldPeriod period = new Hours(2);
        assertEquals(fieldType, period.getFieldType());

        // Test with null field type
        assertNull(new BaseSingleFieldPeriod(0).getFieldType(null));
    }

    @Test
    public void testGetPeriodType() {
        PeriodType periodType = PeriodType.standard();
        BaseSingleFieldPeriod period = new Hours(2);
        assertEquals(periodType, period.getPeriodType());

        // Test with null period type
        assertNull(new BaseSingleFieldPeriod(0).getPeriodType().getField(null));
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new Hours(1);
        assertEquals(1, period.size());

        // Test with wrong size
        try {
            new BaseSingleFieldPeriod(0).size(2);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        DurationFieldType fieldType = DurationFieldType.HOURS;
        BaseSingleFieldPeriod period = new Hours(2);
        assertEquals(fieldType, period.getFieldType(0));

        // Test with wrong index
        try {
            period.getFieldType(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new Hours(2);
        assertEquals(2, period.getValue(0));

        // Test with wrong index
        try {
            period.getValue(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetValueOfFieldType() {
        BaseSingleFieldPeriod period = new Hours(2);
        assertEquals(2, period.get(DurationFieldType.HOURS));

        // Test with wrong field type
        assertEquals(0, period.get(DurationFieldType.MINUTES));

        // Test with null field type
        assertNull(period.get(null));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new Hours(2);
        assertTrue(period.isSupported(DurationFieldType.HOURS));
        assertFalse(period.isSupported(DurationFieldType.MINUTES));

        // Test with null field type
        assertFalse(period.isSupported(null));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new Hours(2);
        Period expected = new Period(0, 0, 2, 0, 0, 0, 0);
        Period actual = period.toPeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new Hours(2);
        MutablePeriod expected = new MutablePeriod().setHours(2);
        MutablePeriod actual = period.toMutablePeriod();
        assertSame(expected, actual);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new Hours(2);
        BaseSingleFieldPeriod period2 = new Hours(2);
        assertTrue(period1.equals(period2));

        BaseSingleFieldPeriod period3 = new Minutes(1);
        assertFalse(period1.equals(period3));

        // Test with null period
        assertFalse(period1.equals(null));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new Hours(2);
        BaseSingleFieldPeriod period2 = new Hours(2);
        assertEquals(period1.hashCode(), period2.hashCode());

        BaseSingleFieldPeriod period3 = new Minutes(1);
        assertNotEquals(period1.hashCode(), period3.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new Hours(2);
        BaseSingleFieldPeriod period2 = new Hours(3);
        assertTrue(period1.compareTo(period2) < 0);

        BaseSingleFieldPeriod period3 = new Hours(2);
        assertEquals(0, period1.compareTo(period3));

        BaseSingleFieldPeriod period4 = new Minutes(1);
        assertTrue(period1.compareTo(period4) > 0);
    }
}
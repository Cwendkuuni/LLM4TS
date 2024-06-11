package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BasicPeriod;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;

/**
 * Test cases for the BaseSingleFieldPeriod abstract class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test between method with ReadableInstant.
     */
    @Test
    public void testBetween_Instant() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        ReadableInstant start = ISOChronology.getInstanceUTC().getUTCInstance();
        ReadableInstant end = start.plus(1, period.getFieldType());
        int result = BaseSingleFieldPeriod.between(start, end, period.getFieldType());
        assertEquals(1, result);
    }

    /**
     * Test between method with ReadableInstant and null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_Instant_Null() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        ReadableInstant start = null;
        BaseSingleFieldPeriod.between(start, null, period.getFieldType());
    }

    /**
     * Test between method with ReadablePartial.
     */
    @Test
    public void testBetween_Partial() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        ReadablePartial start = new LocalDate(2012, 1, 1).toDateTimeAtStartOfDay();
        ReadablePartial end = start.plus(1, period.getFieldType());
        int result = BaseSingleFieldPeriod.between(start, end, period);
        assertEquals(1, result);
    }

    /**
     * Test between method with ReadablePartial and null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_Partial_Null() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        ReadablePartial start = null;
        BaseSingleFieldPeriod.between(start, null, period);
    }

    /**
     * Test standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        long millisPerUnit = 1000 * 60 * 60 * 24;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(1, result);
    }

    /**
     * Test standardPeriodIn method with null period.
     */
    @Test
    public void testStandardPeriodIn_NullPeriod() {
        long millisPerUnit = 1000 * 60 * 60 * 24;
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, millisPerUnit);
        assertEquals(0, result);
    }

    /**
     * Test creating a new instance from a period.
     */
    @Test
    public void testNewInstanceFromPeriod() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        BaseSingleFieldPeriod newInstance = new BaseSingleFieldPeriod(period.getValue());
        assertEquals(period, newInstance);
    }

    /**
     * Test getting the single duration field type.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        DurationFieldType result = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, result);
    }

    /**
     * Test getting the period type which matches the duration field type.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        PeriodType result = period.getPeriodType();
        assertEquals(PeriodType.DAYS, result);
    }

    /**
     * Test getting the number of fields that this period supports.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        int result = period.size();
        assertEquals(1, result);
    }

    /**
     * Test getting the field type at the specified index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_InvalidIndex() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        period.getFieldType(1);
    }

    /**
     * Test getting the value at the specified index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_InvalidIndex() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        period.getValue(1);
    }

    /**
     * Test getting the value of a duration field represented by this period.
     */
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        int result = period.get(DurationFieldType.DAYS);
        assertEquals(1, result);
    }

    /**
     * Test checking whether the duration field specified is supported by this period.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    /**
     * Test getting this period as an immutable Period object.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        Period result = period.toPeriod();
        assertEquals(new Period(1, 0, 0, 0), result);
    }

    /**
     * Test getting this object as a MutablePeriod.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(new MutablePeriod(1, 0, 0, 0), result);
    }

    /**
     * Test equality with null.
     */
    @Test
    public void testEquals_Null() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        assertFalse(period.equals(null));
    }

    /**
     * Test equality with different types.
     */
    @Test
    public void testEquals_DifferentTypes() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        assertFalse(period.equals(new BasicDateTime(1)));
    }

    /**
     * Test equality with the same period.
     */
    @Test
    public void testEquals_SamePeriod() {
        BaseSingleFieldPeriod period1 = new BasicPeriod(1);
        BaseSingleFieldPeriod period2 = new BasicPeriod(1);
        assertTrue(period1.equals(period2));
    }

    /**
     * Test equality with a different period of the same type.
     */
    @Test
    public void testEquals_DifferentPeriod() {
        BaseSingleFieldPeriod period1 = new BasicPeriod(1);
        BaseSingleFieldPeriod period2 = new BasicPeriod(2);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test hash code for the period.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BasicPeriod(1);
        int expected = 17;
        int result = period.hashCode();
        assertEquals(expected, result);
    }
}
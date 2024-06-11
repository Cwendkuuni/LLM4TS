package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseDateTime;
import org.joda.time.BaseDuration;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

/**
 * Test for BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}.
     */
    @Test
    public void testBetweenInstant() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, chrono).toInstant();
        ReadableInstant end = new BaseDateTime(1, 0, 0, 0, 0, 0, 0, chrono).toInstant();
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}.
     */
    @Test
    public void testBetweenInstantNegative() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant end = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, chrono).toInstant();
        ReadableInstant start = new BaseDateTime(1, 0, 0, 0, 0, 0, 0, chrono).toInstant();
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(-1, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}.
     */
    @Test
    public void testBetweenInstantZero() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, chrono).toInstant();
        ReadableInstant end = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, chrono).toInstant();
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(0, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}.
     */
    @Test
    public void testBetweenPartial() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadablePartial start = new BaseDateTime(2000, 1, 1, 0, 0, 0, 0, chrono);
        ReadablePartial end = new BaseDateTime(2000, 1, 2, 0, 0, 0, 0, chrono);
        int result = BaseSingleFieldPeriod.between(start, end, PeriodType.daysInstance());
        assertEquals(1, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}.
     */
    @Test
    public void testBetweenPartialNegative() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadablePartial end = new BaseDateTime(2000, 1, 1, 0, 0, 0, 0, chrono);
        ReadablePartial start = new BaseDateTime(2000, 1, 2, 0, 0, 0, 0, chrono);
        int result = BaseSingleFieldPeriod.between(start, end, PeriodType.daysInstance());
        assertEquals(-1, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}.
     */
    @Test
    public void testBetweenPartialZero() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadablePartial start = new BaseDateTime(2000, 1, 1, 0, 0, 0, 0, chrono);
        ReadablePartial end = new BaseDateTime(2000, 1, 1, 0, 0, 0, 0, chrono);
        int result = BaseSingleFieldPeriod.between(start, end, PeriodType.daysInstance());
        assertEquals(0, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)}.
     */
    @Test
    public void testStandardPeriodIn() {
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, PeriodType.daysTime());
        long millisPerUnit = 86400000L;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(13, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidPeriod() {
        Period period = new Period(1, 2, new BaseDuration(3, PeriodType.MONTHS), 4, PeriodType.daysTime());
        long millisPerUnit = 86400000L;
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#BaseSingleFieldPeriod(int)}.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue()}.
     */
    @Test
    public void testValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#setValue(int)}.
     */
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType()}.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        assertEquals(fieldType, period.getFieldType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getPeriodType()}.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = PeriodType.daysTime();
        assertEquals(periodType, period.getPeriodType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#size()}.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#get(DurationFieldType)}.
     */
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        int result = period.get(fieldType);
        assertEquals(5, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#get(DurationFieldType)}.
     */
    @Test
    public void testGetZero() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        int result = period.get(fieldType);
        assertEquals(0, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#isSupported(DurationFieldType)}.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        assertTrue(period.isSupported(fieldType));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#isSupported(DurationFieldType)}.
     */
    @Test
    public void testIsSupportedInvalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.WEEKS;
        assertFalse(period.isSupported(fieldType));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toPeriod()}.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(new Period(5, PeriodType.daysTime()), result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toMutablePeriod()}.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(new MutablePeriod(5, PeriodType.daysTime()), result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(Object)}.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(Object)}.
     */
    @Test
    public void testEqualsDifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        Object period2 = new Object();
        assertFalse(period1.equals(period2));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(Object)}.
     */
    @Test
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertFalse(period.equals(null));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(BaseSingleFieldPeriod)}.
     */
    @Test
    public void testEqualsSameInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.equals(period));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(BaseSingleFieldPeriod)}.
     */
    @Test
    public void testEqualsDifferentInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#hashCode()}.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.hashCode();
        assertEquals(27 * (27 * 5 + BaseSingleFieldPeriod.class.hashCode()) + 5, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#compareTo(BaseSingleFieldPeriod)}.
     */
    @Test
    public void testCompareToSameInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.compareTo(period);
        assertEquals(0, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#compareTo(BaseSingleFieldPeriod)}.
     */
    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        int result = period1.compareTo(period2);
        assertEquals(1, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#compareTo(BaseSingleFieldPeriod)}.
     */
    @Test
    public void testCompareToSmaller() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }

}
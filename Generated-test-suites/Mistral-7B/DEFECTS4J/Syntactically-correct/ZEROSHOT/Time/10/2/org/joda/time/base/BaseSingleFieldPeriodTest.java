package org.joda.time.base;

import org.joda.time.BaseDateTime;
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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link BaseSingleFieldPeriod} class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}.
     */
    @Test
    public void testBetweenInstant() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new BaseDateTime(chrono, 2018, 1, 1, 0, 0, 0, 0).toInstant();
        ReadableInstant end = new BaseDateTime(chrono, 2018, 1, 1, 1, 0, 0, 0).toInstant();
        int days = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, days);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}.
     */
    @Test
    public void testBetweenPartial() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadablePartial start = new BaseDateTime(chrono, 2018, 1, 1).toLocalDate();
        ReadablePartial end = new BaseDateTime(chrono, 2018, 1, 1).plusDays(1).toLocalDate();
        int days = BaseSingleFieldPeriod.between(start, end, new Period(1, 0, 0));
        assertEquals(1, days);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)}.
     */
    @Test
    public void testStandardPeriodIn() {
        Period period = new Period(3, 2, 30, 15, 30);
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, 3600 * 1000);
        assertEquals(3 + 2 + 0.5, hours, 0.01); //rounding error
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#BaseSingleFieldPeriod(int)} constructor.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(10);
        assertEquals(10, p.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue()}.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(5, p.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#setValue(int)}.
     */
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(2);
        p.setValue(4);
        assertEquals(4, p.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType()}.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = p.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getPeriodType()}.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        PeriodType periodType = p.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#size()}.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        int size = p.size();
        assertEquals(1, size);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType(int)}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeIndexOutOfBounds() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        p.getFieldType(2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue(int)}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueIndexOutOfBounds() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        p.getValue(2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#get(DurationFieldType)}.
     */
    @Test
    public void testGet() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        int value = p.get(fieldType);
        assertEquals(5, value);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#isSupported(DurationFieldType)}.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = DurationFieldType.HOURS;
        boolean supported = p.isSupported(fieldType);
        assertFalse(supported);

        fieldType = DurationFieldType.DAYS;
        supported = p.isSupported(fieldType);
        assertTrue(supported);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toPeriod()}.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(3);
        Period period = p.toPeriod();
        assertEquals(new Period(3, 0, 0), period);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toMutablePeriod()}.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(4);
        MutablePeriod mutablePeriod = p.toMutablePeriod();
        assertEquals(new MutablePeriod(4, 0, 0), mutablePeriod);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(Object)}.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1);
        assertTrue(p1.equals(p2));

        Object obj = new Object();
        assertFalse(p1.equals(obj));

        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(2);
        assertFalse(p1.equals(p3));

        BaseSingleFieldPeriod p4 = new BaseSingleFieldPeriod(1);
        assertTrue(p1.equals(p4));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#hashCode()}.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1);
        assertEquals(p1.hashCode(), p2.hashCode());

        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(2);
        int hashCode1 = p1.hashCode();
        int hashCode2 = p3.hashCode();
        assertNotEquals(hashCode1, hashCode2);

        p3.setValue(1);
        int hashCode3 = p3.hashCode();
        assertEquals(hashCode1, hashCode3);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#compareTo(BaseSingleFieldPeriod)}.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(2);
        int comparison = p1.compareTo(p2);
        assertEquals(-1, comparison);

        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(1);
        comparison = p1.compareTo(p3);
        assertEquals(0, comparison);

        BaseSingleFieldPeriod p4 = new BaseSingleFieldPeriod(3);
        comparison = p1.compareTo(p4);
        assertEquals(-1, comparison);
    }
}
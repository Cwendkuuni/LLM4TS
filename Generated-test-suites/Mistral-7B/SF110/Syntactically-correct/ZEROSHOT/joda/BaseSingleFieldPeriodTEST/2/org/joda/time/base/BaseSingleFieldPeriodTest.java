package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        ReadableInstant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC());

        DurationFieldType durationFieldType = FieldType.dayOfYear();
        int result = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
        int expected = 1;
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullInstants() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC());

        DurationFieldType durationFieldType = FieldType.dayOfYear();
        BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullDurationFieldType() {
        ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        ReadableInstant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC());

        BaseSingleFieldPeriod.between(instant1, instant2, null);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Duration.standardDays(3);

        long n = 2;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        int expected = 1; // (3 days * 24 hours * 60 minutes * 60 seconds) / (24 hours * 60 minutes * 60 seconds * n)
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullPeriod() {
        long n = 2;
        BaseSingleFieldPeriod.standardPeriodIn(null, n);
    }

    @Test
    public void testConstructor() {
        int iPeriod = 5;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(iPeriod);
        int expected = iPeriod;
        assertEquals(expected, baseSingleFieldPeriod.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueNegativeIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        baseSingleFieldPeriod.getValue(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValuePositiveIndexGreaterThanOne() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        baseSingleFieldPeriod.getValue(2);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        DurationFieldType expected = FieldType.dayOfMonth();
        DurationFieldType result = baseSingleFieldPeriod.getFieldType();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        PeriodType expected = PeriodType.standard();
        PeriodType result = baseSingleFieldPeriod.getPeriodType();
        assertEquals(expected, result);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        int expected = 1;
        int result = baseSingleFieldPeriod.size();
        assertEquals(expected, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeNegativeIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        baseSingleFieldPeriod.getFieldType(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypePositiveIndexGreaterThanOne() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        baseSingleFieldPeriod.getFieldType(2);
    }

    @Test
    public void testGetValueNegativeIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        int expected = 5;
        int result = baseSingleFieldPeriod.getValue(-1);
        assertEquals(expected, result);
    }

    @Test
    public void testGetValuePositiveIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        int expected = 5;
        int result = baseSingleFieldPeriod.getValue(0);
        assertEquals(expected, result);
    }

    @Test
    public void testGetValueSetValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        int newValue = 10;
        baseSingleFieldPeriod.setValue(newValue);
        int expected = newValue;
        int result = baseSingleFieldPeriod.getValue();
        assertEquals(expected, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueSetValueNegativeIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        baseSingleFieldPeriod.setValue(-1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueSetValuePositiveIndexGreaterThanOne() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        baseSingleFieldPeriod.setValue(2, 10);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(5);
        assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
    }

    @Test
    public void testEqualsDifferentClasses() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        Object obj = new Object();
        assertFalse(baseSingleFieldPeriod.equals(obj));
    }

    @Test
    public void testEqualsNull() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5);
        assertFalse(baseSingleFieldPeriod.equals(null));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(5);
        int expected = 27 * (27 * 17 + 5) + baseSingleFieldPeriod1.getFieldType().hashCode();
        int result1 = baseSingleFieldPeriod1.hashCode();
        int result2 = baseSingleFieldPeriod2.hashCode();
        assertEquals(expected, result1);
        assertEquals(expected, result2);
    }

    @Test
    public void testCompareToEqual() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(5);
        int expected = 0;
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToLess() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(5);
        int expected = -2;
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(3);
        int expected = 2;
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        assertEquals(expected, result);
    }
}
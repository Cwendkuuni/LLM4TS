package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        assertEquals(1, p.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodNull() {
        new BaseSingleFieldPeriod(null);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = p.getFieldType();
        assertEquals(DurationFieldType.days(), fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        PeriodType periodType = p.getPeriodType();
        assertEquals(PeriodType.days(), periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        assertEquals(1, p.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        p.getFieldType(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testgetValueInvalidIndex() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        p.getValue(2);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        assertEquals(1, p.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        p.setValue(2);
        assertEquals(2, p.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNegative() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        p.setValue(-1);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1);
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testNotEqualsDifferentClass() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        String s = new String("foo");
        assertFalse(p1.equals(s));
    }

    @Test
    public void testNotEqualsDifferentValue() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(2);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testHashCodeEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testHashCodeDifferentValues() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(2);
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testCompareToEqual() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1);
        assertEquals(0, p1.compareTo(p2));
    }

    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(2);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1);
        assertEquals(1, p1.compareTo(p2));
    }

    @Test
    public void testCompareToLess() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(2);
        assertEquals(-1, p1.compareTo(p2));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        assertTrue(p.isSupported(DurationFieldType.days()));
        assertFalse(p.isSupported(DurationFieldType.hours()));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentClass() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        Object o = new String("foo");
        p1.compareTo(o);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        Period p1 = p.toPeriod();
        assertEquals(1, p1.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        MutablePeriod mp = p.toMutablePeriod();
        mp.add(1, DurationFieldType.days());
        assertEquals(2, mp.getDays());
        assertEquals(p, mp.toPeriod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullReadableInstant() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        long n = 1;
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod.between(null, new Instant(0), durationFieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullReadablePartial() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        long n = 1;
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod.between(new Instant(0), null, durationFieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenDifferentChronologies() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        long n = 1;
        DurationFieldType durationFieldType = DurationFieldType.days();
        BaseSingleFieldPeriod.between(DateTimeUtils.currentTime().toInstant(), new Instant(0), durationFieldType);
    }

    @Test
    public void testBetweenSameChronologies() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        long n = 1;
        DurationFieldType durationFieldType = DurationFieldType.days();
        int result = BaseSingleFieldPeriod.between(DateTimeUtils.currentTime().toInstant(), new Instant(0), durationFieldType);
        assertEquals(1, result);
    }

    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        long n = 2;
        int result = BaseSingleFieldPeriod.standardPeriodIn(p, n);
        assertEquals(2, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullPeriod() {
        long n = 2;
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInZeroPeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(0);
        long n = 2;
        int result = BaseSingleFieldPeriod.standardPeriodIn(p, n);
        assertEquals(0, result);
    }

    @Test
    public void testStandardPeriodInNegativeN() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1);
        long n = -2;
        int result = BaseSingleFieldPeriod.standardPeriodIn(p, n);
        assertEquals(0, result);
    }
}
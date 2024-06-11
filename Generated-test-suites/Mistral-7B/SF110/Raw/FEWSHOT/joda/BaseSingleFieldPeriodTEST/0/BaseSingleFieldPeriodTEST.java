package org.joda.time.base;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(PeriodType.fixedPeriod, period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testGetFieldTypeAt() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, period.getFieldType(0));
    }

    @Test
    public void testGetValueAt() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        Period expected = new Period(5, DurationFieldType.DAYS);
        Period actual = period.toPeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        MutablePeriod expected = new MutablePeriod();
        expected.add(DurationFieldType.DAYS, 5);
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testNotEquals() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(6);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        assertEquals(period1.hashCode(), period2.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(6);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareToSameInstance() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        int result = period.compareTo(period);
        assertEquals(0, result);
    }

    @Test
    public void testCompareToZero() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        int result = period.compareTo(Period.ZERO);
        assertEquals(5, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentClass() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        Period other = new Period(5, DurationFieldType.HOURS);
        period.compareTo(other);
    }

    private static class MySingleFieldPeriod extends BaseSingleFieldPeriod {
        MySingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.DAYS;
        }
    }
}
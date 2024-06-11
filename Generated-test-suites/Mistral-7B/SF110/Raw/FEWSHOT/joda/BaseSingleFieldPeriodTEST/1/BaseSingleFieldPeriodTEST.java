package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public abstract class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = createPeriodInstance();
        assertSame(period.getFieldType(), period.getFieldType());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetFieldTypeThrowsExceptionIfSubclassDoesNotImplementGetFieldTypeMethod() {
        new BaseSingleFieldPeriod() {}.getFieldType();
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = createPeriodInstance();
        assertSame(period.getPeriodType(), period.getPeriodType());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPeriodTypeThrowsExceptionIfSubclassDoesNotImplementGetPeriodTypeMethod() {
        new BaseSingleFieldPeriod() {}.getPeriodType();
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = createPeriodInstance();
        assertEquals(1, period.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueThrowsExceptionIfIndexIsNotZero() {
        BaseSingleFieldPeriod period = createPeriodInstance();
        period.getValue(1);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = createPeriodInstance();
        int value = period.getValue();
        assertEquals(period.getValue(), value);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = createPeriodInstance();
        DurationFieldType fieldType = period.getFieldType();
        int actual = period.get(fieldType);
        int expected = period.getValue();
        assertEquals("Unexpected value", expected, actual);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = createPeriodInstance();
        DurationFieldType fieldType = period.getFieldType();
        assertTrue(period.isSupported(fieldType));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = createPeriodInstance();
        Period actual = period.toPeriod();
        Period expected = Period.singleton(period.getFieldType().getMillisPerPeriod(), period.getValue());
        assertEquals(expected, actual);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = createPeriodInstance();
        MutablePeriod actual = period.toMutablePeriod();
        MutablePeriod expected = new MutablePeriod().set(period.getFieldType(), period.getValue());
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = createPeriodInstance();
        BaseSingleFieldPeriod period2 = createPeriodInstance();
        assertTrue(period1.equals(period2));
        assertTrue(period2.equals(period1));

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10);
        assertFalse(period1.equals(period3));
        assertFalse(period3.equals(period1));

        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(15);
        assertFalse(period1.equals(period4));
        assertFalse(period4.equals(period1));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = createPeriodInstance();
        BaseSingleFieldPeriod period2 = createPeriodInstance();
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(15);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period3.hashCode();
        int hashCode4 = period4.hashCode();

        BaseSingleFieldPeriod clonePeriod1 = new BaseSingleFieldPeriod(period1.getValue());
        int hashCodeClone1 = clonePeriod1.hashCode();

        assertEquals("Unexpected hash code for period1", hashCode1, hashCode1);
        assertEquals("Unexpected hash code for period2", hashCode2, hashCode2);
        assertNotEquals("Unexpected hash code for period1 and period3", hashCode1, hashCode3);
        assertNotEquals("Unexpected hash code for period1 and period4", hashCode1, hashCode4);
        assertEquals("Unexpected hash code for period1 and its clone", hashCode1, hashCodeClone1);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = createPeriodInstance();
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2);

        int result12 = period1.compareTo(period2);
        int result13 = period1.compareTo(period3);
        int result21 = period2.compareTo(period1);
        int result31 = period3.compareTo(period1);

        assertEquals("Unexpected comparison result between period1 and period2", 0, result12);
        assertEquals("Unexpected comparison result between period1 and period3", -1, result13);
        assertEquals("Unexpected comparison result between period2 and period1", 1, result21);
        assertEquals("Unexpected comparison result between period3 and period1", 1, result31);
    }

    @Test
    public void testBetween() {
        Instant instant1 = new DateTime(2023, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        Instant instant2 = new DateTime(2023, 1, 1, 1, 0, 0, 0, DateTimeZone.UTC).toInstant();

        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(3);

        int result1 = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.HOURS);
        int result2 = BaseSingleFieldPeriod.between(period1, period2, period1.getFieldType());
        int result3 = BaseSingleFieldPeriod.between(period2, period1, period1.getFieldType());
        int result4 = BaseSingleFieldPeriod.between(period1, period3, period1.getFieldType());

        assertEquals("Unexpected result of between method for Instant objects", 1, result1);
        assertEquals("Unexpected result of between method for BaseSingleFieldPeriod objects (period1 and period2)", 1, result2);
        assertEquals("Unexpected result of between method for BaseSingleFieldPeriod objects (period2 and period1)", -1, result3);
        assertEquals("Unexpected result of between method for BaseSingleFieldPeriod objects (period1 and period3)", 1, result4);
    }

    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1000);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2000);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(3000);

        long n = 10;

        int result1 = BaseSingleFieldPeriod.standardPeriodIn(period1, n);
        int result2 = BaseSingleFieldPeriod.standardPeriodIn(period2, n);
        int result3 = BaseSingleFieldPeriod.standardPeriodIn(period3, n);

        assertEquals("Unexpected result of standardPeriodIn method for period1", 100, result1);
        assertEquals("Unexpected result of standardPeriodIn method for period2", 200, result2);
        assertEquals("Unexpected result of standardPeriodIn method for period3", 300, result3);
    }

    protected abstract BaseSingleFieldPeriod createPeriodInstance();
}
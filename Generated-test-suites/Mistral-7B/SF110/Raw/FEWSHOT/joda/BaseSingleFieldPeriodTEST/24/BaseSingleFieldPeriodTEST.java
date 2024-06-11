package org.joda.time.base;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

public class BaseSingleFieldPeriodTest {
    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod testPeriod = new Daily(1);
        assertEquals(Daily.class, testPeriod.getFieldType().getClass());
        assertEquals(PeriodType.daily(), testPeriod.getPeriodType());
        assertEquals(1, testPeriod.getValue());
        assertEquals(1, testPeriod.size());
        assertEquals(testPeriod.getClass(), testPeriod.getFieldType().getField(ISOChronology.getInstanceUTC()).getFieldClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodWithInvalidArgument() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(-1);
    }

    @Test
    public void testGetFieldType() {
        Daily testPeriod = new Daily(1);
        assertEquals(Daily.class, testPeriod.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        Daily testPeriod = new Daily(1);
        assertEquals(PeriodType.daily(), testPeriod.getPeriodType());
    }

    @Test
    public void testSize() {
        Daily testPeriod = new Daily(1);
        assertEquals(1, testPeriod.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        Daily testPeriod = new Daily(1);
        testPeriod.getFieldType(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        Daily testPeriod = new Daily(1);
        testPeriod.getValue(2);
    }

    @Test
    public void testGetValue() {
        Daily testPeriod = new Daily(1);
        assertEquals(1, testPeriod.getValue());
    }

    @Test
    public void testSetValue() {
        Daily testPeriod = new Daily(1);
        testPeriod.setValue(2);
        assertEquals(2, testPeriod.getValue());
    }

    @Test
    public void testToPeriod() {
        Daily testPeriod = new Daily(1);
        Period period = testPeriod.toPeriod();
        assertEquals(Period.ONE_DAY, period);
    }

    @Test
    public void testToMutablePeriod() {
        Daily testPeriod = new Daily(1);
        MutablePeriod mutablePeriod = testPeriod.toMutablePeriod();
        assertEquals(Period.ONE_DAY, mutablePeriod.toPeriod());
    }

    @Test
    public void testEquals() {
        Daily testPeriod1 = new Daily(1);
        Daily testPeriod2 = new Daily(1);
        assertTrue(testPeriod1.equals(testPeriod2));
        assertFalse(testPeriod1.equals(new Hourly(1)));
    }

    @Test
    public void testHashCode() {
        Daily testPeriod1 = new Daily(1);
        Daily testPeriod2 = new Daily(1);
        Daily testPeriod3 = new Hourly(1);
        assertEquals(testPeriod1.hashCode(), testPeriod1.hashCode());
        assertEquals(testPeriod1.hashCode(), testPeriod2.hashCode());
        assertNotEquals(testPeriod1.hashCode(), testPeriod3.hashCode());
    }

    @Test
    public void testCompareTo() {
        Daily testPeriod1 = new Daily(1);
        Daily testPeriod2 = new Daily(2);
        Daily testPeriod3 = new Daily(1);
        assertEquals(0, testPeriod1.compareTo(testPeriod3));
        assertEquals(1, testPeriod2.compareTo(testPeriod1));
        assertEquals(-1, testPeriod3.compareTo(testPeriod2));
    }

    @Test
    public void testBetween() {
        Instant instant1 = new DateTime(2022, 1, 1).toInstant();
        Instant instant2 = new DateTime(2022, 1, 2).toInstant();
        DurationFieldType durationFieldType = Minutes.getInstance();
        assertEquals(1440, BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullInstant() {
        DurationFieldType durationFieldType = Minutes.getInstance();
        BaseSingleFieldPeriod.between(null, new DateTime(2022, 1, 1).toInstant(), durationFieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullInstant2() {
        DurationFieldType durationFieldType = Minutes.getInstance();
        BaseSingleFieldPeriod.between(new DateTime(2022, 1, 1).toInstant(), null, durationFieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithInvalidDurationFieldType() {
        Instant instant1 = new DateTime(2022, 1, 1).toInstant();
        Instant instant2 = new DateTime(2022, 1, 2).toInstant();
        assertEquals(1440, BaseSingleFieldPeriod.between(instant1, instant2, Seconds.getInstance()));
        assertNotEquals(1440, BaseSingleFieldPeriod.between(instant1, instant2, Minutes.getInstance()));
    }

    @Test
    public void testStandardPeriodIn() {
        DurationFieldType days = Days.getInstance();
        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(Period.ONE_DAY, 7);
        assertEquals(7, standardPeriodIn);
    }

    @Test
    public void testStandardPeriodInWithNullPeriod() {
        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(null, 7);
        assertEquals(0, standardPeriodIn);
    }
}
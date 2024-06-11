package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;

import java.io.Serializable;

/**
 * Test cases for BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test standardPeriodIn method with valid arguments.
     */
    @Test
    public void testStandardPeriodIn() {
        // Test with Period instance
        Period p = new Period(7, 3, 2, 15, 30, 12, 456);
        int result = BaseSingleFieldPeriod.standardPeriodIn(p, 1000L);
        assertEquals(7 * 24 * 60 * 60 * 1000 + 3 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000 + 15 * 60 * 60 * 1000 + 30 * 60 * 1000 + 12 * 60 * 1000 + 456 * 1000, result);

        // Test with MutablePeriod instance
        MutablePeriod mp = new MutablePeriod();
        mp.setYears(1).setMonths(2).setDays(3).setHours(4).setMinutes(5).setSeconds(6).setMillis(7);
        int result2 = BaseSingleFieldPeriod.standardPeriodIn(mp, 1000L);
        assertEquals(mp.toPeriod().getMillis(), result2 * 1000);

        // Test with ISOChronology instance
        ISOChronology chrono = ISOChronology.getInstanceUTC();
        ReadablePeriod rp = chrono.getPrototype(PeriodType.standard());
        rp = rp.withFieldsAdded(new DurationFieldType[]{DurationFieldType.DAYS, DurationFieldType.HOURS}, new int[]{7, 3});
        int result3 = BaseSingleFieldPeriod.standardPeriodIn(rp, 1000L);
        assertEquals(rp.toPeriod().getMillis(), result3 * 1000);
    }

    /**
     * Test standardPeriodIn method with null period argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithNullPeriod() {
        BaseSingleFieldPeriod.standardPeriodIn(null, 1000L);
    }

    /**
     * Test between method with valid arguments.
     */
    @Test
    public void testBetween() {
        long startMillis = 1623379200000L; // 2021-03-01 00:00:00 UTC
        long endMillis = 1623395600000L; // 2021-03-01 02:10:00 UTC

        // Test with ReadableInstant argument
        ReadableInstant start = ISOChronology.getInstanceUTC().getInstant(startMillis);
        ReadableInstant end = ISOChronology.getInstanceUTC().getInstant(endMillis);
        DurationFieldType field = DurationFieldType.DAYS;
        int result = BaseSingleFieldPeriod.between(start, end, field);
        assertEquals(1, result);

        // Test with ReadablePartial argument
        ReadablePartial startDate = ISOChronology.getInstanceUTC().getPrototype(org.joda.time.LocalDate.class).withDate(2021, 3, 1);
        ReadablePartial endDate = ISOChronology.getInstanceUTC().getPrototype(org.joda.time.LocalDate.class).withDate(2021, 3, 1);
        endDate = endDate.plusDays(1);
        int result2 = BaseSingleFieldPeriod.between(startDate, endDate, new PeriodType[]{PeriodType.standard()}.iterator().next());
        assertEquals(1, result2);
    }

    /**
     * Test between method with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullArguments() {
        BaseSingleFieldPeriod.between(null, null, null);
    }

    /**
     * Test getValue method with valid arguments.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod testInstance = new TestSingleFieldPeriod(5);
        int result = testInstance.getValue();
        assertEquals(5, result);
    }

    /**
     * Test getValue method with invalid arguments.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetValueWithInvalidArguments() {
        BaseSingleFieldPeriod testInstance = new TestSingleFieldPeriod(-5);
        testInstance.setValue(-10);
        int result = testInstance.getValue();
    }

    /**
     * Test setValue method with valid arguments.
     */
    @Test
    public void testSetValue() {
        TestSingleFieldPeriod testInstance = new TestSingleFieldPeriod(5);
        testInstance.setValue(10);
        assertEquals(10, testInstance.getValue());
    }

    /**
     * Test setValue method with invalid arguments.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetValueWithInvalidArguments() {
        TestSingleFieldPeriod testInstance = new TestSingleFieldPeriod(5);
        testInstance.setValue(-10);
    }

    /**
     * Test getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        TestSingleFieldPeriod testInstance = new TestSingleFieldPeriod(5);
        DurationFieldType result = testInstance.getFieldType();
        assertEquals(DurationFieldType.DAYS, result);
    }

    /**
     * Test getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        TestSingleFieldPeriod testInstance = new TestSingleFieldPeriod(5);
        PeriodType result = testInstance.getPeriodType();
        assertEquals(PeriodType.standard(), result);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        TestSingleFieldPeriod testInstance = new TestSingleFieldPeriod(5);
        int result = testInstance.size();
        assertEquals(1, result);
    }

    /**
     * Test getFieldType method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        TestSingleFieldPeriod testInstance = new TestSingleFieldPeriod(5);
        testInstance.getFieldType(1);
    }

    /**
     * Test getValue method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        TestSingleFieldPeriod testInstance = new TestSingleFieldPeriod(5);
        testInstance.getValue(1);
    }

    /**
     * Test equals method with equal arguments.
     */
    @Test
    public void testEqualsWithEqualArguments() {
        TestSingleFieldPeriod testInstance1 = new TestSingleFieldPeriod(5);
        TestSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(5);
        boolean result = testInstance1.equals(testInstance2);
        assertTrue(result);
    }

    /**
     * Test equals method with null argument.
     */
    @Test
    public void testEqualsWithNullArgument() {
        TestSingleFieldPeriod testInstance = new TestSingleFieldPeriod(5);
        boolean result = testInstance.equals(null);
        assertFalse(result);
    }

    /**
     * Test equals method with different classes.
     */
    @Test
    public void testEqualsWithDifferentClasses() {
        TestSingleFieldPeriod testInstance = new TestSingleFieldPeriod(5);
        Object otherInstance = new Object();
        boolean result = testInstance.equals(otherInstance);
        assertFalse(result);
    }

    /**
     * Test equals method with different values.
     */
    @Test
    public void testEqualsWithDifferentValues() {
        TestSingleFieldPeriod testInstance1 = new TestSingleFieldPeriod(5);
        TestSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(3);
        boolean result = testInstance1.equals(testInstance2);
        assertFalse(result);
    }

    /**
     * Test hashCode method with equal arguments.
     */
    @Test
    public void testHashCodeWithEqualArguments() {
        TestSingleFieldPeriod testInstance1 = new TestSingleFieldPeriod(5);
        TestSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(5);
        int result1 = testInstance1.hashCode();
        int result2 = testInstance2.hashCode();
        assertEquals(result1, result2);
    }

    /**
     * Test hashCode method with different arguments.
     */
    @Test
    public void testHashCodeWithDifferentArguments() {
        TestSingleFieldPeriod testInstance1 = new TestSingleFieldPeriod(5);
        TestSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(3);
        int result1 = testInstance1.hashCode();
        int result2 = testInstance2.hashCode();
        assertNotEquals(result1, result2);
    }

    /**
     * Test compareTo method with equal arguments.
     */
    @Test
    public void testCompareToWithEqualArguments() {
        TestSingleFieldPeriod testInstance1 = new TestSingleFieldPeriod(5);
        TestSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(5);
        int result = testInstance1.compareTo(testInstance2);
        assertEquals(0, result);
    }

    /**
     * Test compareTo method with greater arguments.
     */
    @Test
    public void testCompareToWithGreaterArguments() {
        TestSingleFieldPeriod testInstance1 = new TestSingleFieldPeriod(5);
        TestSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(3);
        int result = testInstance1.compareTo(testInstance2);
        assertEquals(1, result);
    }

    /**
     * Test compareTo method with less arguments.
     */
    @Test
    public void testCompareToWithLessArguments() {
        TestSingleFieldPeriod testInstance1 = new TestSingleFieldPeriod(3);
        TestSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(5);
        int result = testInstance1.compareTo(testInstance2);
        assertEquals(-1, result);
    }

    /**
     * A test class for BaseSingleFieldPeriod.
     */
    static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        TestSingleFieldPeriod(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.DAYS;
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
}
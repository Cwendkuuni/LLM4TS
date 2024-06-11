package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
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

/**
 * Tests for the BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /** A test period instance. */
    private static final BaseSingleFieldPeriod TEST_PERIOD = new TestSingleDayPeriod(1);

    /** A test immutable period instance. */
    private static final Period TEST_IMPERIOD = new Period(1, DurationFieldType.DAYS);

    /** A test mutable period instance. */
    private static final MutablePeriod TEST_MUTPERIOD = new MutablePeriod();

    /** A test chronology instance. */
    private static final Chronology TEST_CHRONO = ISOChronology.getInstanceUTC();

    /**
     * Test construction.
     */
    @Test
    public void testConstruction() {
        assertEquals(1, TEST_PERIOD.getValue());
        assertEquals(DurationFieldType.DAYS, TEST_PERIOD.getFieldType());
        assertEquals(PeriodType.standard(), TEST_PERIOD.getPeriodType());
        assertEquals(TEST_PERIOD.getClass(), TEST_PERIOD.getClass());
    }

    /**
     * Test construction with zero.
     */
    @Test
    public void testConstructionZero() {
        BaseSingleFieldPeriod testZeroPeriod = new TestSingleDayPeriod(0);
        assertEquals(0, testZeroPeriod.getValue());
        assertEquals(DurationFieldType.DAYS, testZeroPeriod.getFieldType());
        assertEquals(PeriodType.standard(), testZeroPeriod.getPeriodType());
    }

    /**
     * Test getting size.
     */
    @Test
    public void testSize() {
        assertEquals(1, TEST_PERIOD.size());
    }

    /**
     * Test getting field type at index zero.
     */
    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.DAYS, TEST_PERIOD.getFieldType(0));
    }

    /**
     * Test getting value at index zero.
     */
    @Test
    public void testGetValue() {
        assertEquals(1, TEST_PERIOD.getValue(0));
    }

    /**
     * Test getting value for unsupported field type.
     */
    @Test
    public void testGetValueUnsupported() {
        assertEquals(0, TEST_PERIOD.getValue(DurationFieldType.MONTHS));
    }

    /**
     * Test getting a period as an immutable Period.
     */
    @Test
    public void testToPeriod() {
        Period testPeriod = TEST_PERIOD.toPeriod();
        assertEquals(TEST_IMPERIOD, testPeriod);
    }

    /**
     * Test getting a mutable period.
     */
    @Test
    public void testToMutablePeriod() {
        MutablePeriod testMutPeriod = TEST_PERIOD.toMutablePeriod();
        assertEquals(TEST_MUTPERIOD, testMutPeriod);
    }

    /**
     * Test equals with null.
     */
    @Test
    public void testEqualsNull() {
        assertNull(TEST_PERIOD.equals(null));
    }

    /**
     * Test equals with different classes.
     */
    @Test
    public void testEqualsDifferentClass() {
        assertFalse(TEST_PERIOD.equals(new Object()));
    }

    /**
     * Test equals with different values.
     */
    @Test
    public void testEqualsDifferentValues() {
        BaseSingleFieldPeriod testDifferentPeriod = new TestSingleDayPeriod(2);
        assertFalse(TEST_PERIOD.equals(testDifferentPeriod));
    }

    /**
     * Test equals with same values.
     */
    @Test
    public void testEqualsSameValues() {
        BaseSingleFieldPeriod testSamePeriod = new TestSingleDayPeriod(1);
        assertTrue(TEST_PERIOD.equals(testSamePeriod));
    }

    /**
     * Test hash code.
     */
    @Test
    public void testHashCode() {
        int testHashCode = TEST_PERIOD.hashCode();
        BaseSingleFieldPeriod testHashCodeObject = new TestSingleDayPeriod(1);
        assertEquals(testHashCode, testHashCodeObject.hashCode());
    }

    /**
     * Test comparison with null.
     */
    @Test
    public void testCompareToNull() {
        assertThrows(NullPointerException.class, () -> TEST_PERIOD.compareTo(null));
    }

    /**
     * Test comparison with different classes.
     */
    @Test
    public void testCompareToDifferentClasses() {
        assertThrows(ClassCastException.class, () -> TEST_PERIOD.compareTo(new Object()));
    }

    /**
     * Test comparison with smaller value.
     */
    @Test
    public void testCompareToSmaller() {
        BaseSingleFieldPeriod testSmallerPeriod = new TestSingleDayPeriod(0);
        int comparisonResult = TEST_PERIOD.compareTo(testSmallerPeriod);
        assertTrue(comparisonResult > 0);
    }

    /**
     * Test comparison with larger value.
     */
    @Test
    public void testCompareToLarger() {
        BaseSingleFieldPeriod testLargerPeriod = new TestSingleDayPeriod(2);
        int comparisonResult = TEST_PERIOD.compareTo(testLargerPeriod);
        assertTrue(comparisonResult < 0);
    }

    /**
     * Test comparison with equal value.
     */
    @Test
    public void testCompareToEqual() {
        BaseSingleFieldPeriod testEqualPeriod = new TestSingleDayPeriod(1);
        int comparisonResult = TEST_PERIOD.compareTo(testEqualPeriod);
        assertEquals(0, comparisonResult);
    }

    /**
     * Test between method with ReadableInstant.
     */
    @Test
    public void testBetweenReadableInstant() {
        BaseDateTime start = new BaseDateTime(0L, TEST_CHRONO);
        BaseDateTime end = new BaseDateTime(1L, TEST_CHRONO);
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS));
    }

    /**
     * Test between method with ReadablePartial.
     */
    @Test
    public void testBetweenReadablePartial() {
        ReadablePartial start = new BaseLocalDate(2000, 1, 1, TEST_CHRONO);
        ReadablePartial end = new BaseLocalDate(2000, 1, 2, TEST_CHRONO);
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, TEST_IMPERIOD));
    }

    /**
     * Test standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod testPeriod = new Period(1, DurationFieldType.DAYS, PeriodType.standard());
        assertEquals(1, BaseSingleFieldPeriod.standardPeriodIn(testPeriod, 86400000));
    }

}

/**
 * A test implementation of BaseSingleFieldPeriod.
 */
class TestSingleDayPeriod extends BaseSingleFieldPeriod {

    /**
     * Constructor.
     *
     * @param period the period to represent
     */
    public TestSingleDayPeriod(int period) {
        super(period);
    }

}
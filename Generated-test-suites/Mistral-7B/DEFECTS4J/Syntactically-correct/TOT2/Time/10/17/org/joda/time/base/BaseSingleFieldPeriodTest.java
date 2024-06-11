package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

import org.junit.Test;

/**
 * Tests for the {@code BaseSingleFieldPeriod} class.
 */
public class BaseSingleFieldPeriodTest {

    /** The chronology used for testing. */
    private final Chronology iso = ISOChronology.getInstanceUTC();

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        // Create an instance and check its properties
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(1, period.size());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(7, period.getValue());
    }

    /**
     * Tests the between method with two instants.
     */
    @Test
    public void testBetweenInstant() {
        // Create test data
        ReadableInstant instant1 = new BaseDateTime(iso, 2005, 1, 1, 0, 0, 0, 0).toInstant();
        ReadableInstant instant2 = new BaseDateTime(iso, 2005, 1, 1, 1, 0, 0, 0).toInstant();

        // Test the method
        int result = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.DAYS);
        assertEquals(1, result);
    }

    /**
     * Tests the between method with two partials.
     */
    @Test
    public void testBetweenPartial() {
        // Create test data
        ReadablePartial partial1 = new BaseDateTime(iso, 2005, 1, 1).toLocalDate();
        ReadablePartial partial2 = new BaseDateTime(iso, 2005, 1, 2).toLocalDate();

        // Test the method
        int result = BaseSingleFieldPeriod.between(partial1, partial2, new Period(1, DurationFieldType.DAYS));
        assertEquals(1, result);
    }

    /**
     * Tests the standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        // Create test data
        ReadablePeriod period = new Period(7, DurationFieldType.DAYS, PeriodType.standard());

        // Test the method
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, FieldUtils.MILLIS_PER_DAY);
        assertEquals(7 * 24 * 60 * 60 * 1000, result * FieldUtils.MILLIS_PER_DAY);
    }

    /**
     * Tests the getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        // Create test instance
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);

        // Test the method
        assertNotNull(period.getFieldType());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Tests the getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        // Create test instance
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);

        // Test the method
        assertNotNull(period.getPeriodType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Tests the getValue method.
     */
    @Test
    public void testGetValue() {
        // Create test instance
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);

        // Test the method
        assertEquals(7, period.getValue());
    }

    /**
     * Tests the get method.
     */
    @Test
    public void testGet() {
        // Create test instance
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);

        // Test the method
        assertEquals(7, period.get(DurationFieldType.DAYS));
        assertEquals(0, period.get(DurationFieldType.HOURS));
    }

    /**
     * Tests the isSupported method.
     */
    @Test
    public void testIsSupported() {
        // Create test instance
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);

        // Test the method
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    /**
     * Tests the toPeriod method.
     */
    @Test
    public void testToPeriod() {
        // Create test instance
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);

        // Test the method
        Period result = period.toPeriod();
        assertEquals(7, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(PeriodType.standard(), result.getPeriodType());
    }

    /**
     * Tests the toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        // Create test instance
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);

        // Test the method
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(7, result.getDays());
        assertEquals(0, result.getHours());
    }

    /**
     * Tests the equals method with an equal object.
     */
    @Test
    public void testEqualsSame() {
        // Create test instances
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);

        // Test the method
        assertTrue(period1.equals(period2));
    }

    /**
     * Tests the equals method with a null object.
     */
    @Test
    public void testEqualsNull() {
        // Create test instance
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);

        // Test the method
        assertFalse(period.equals(null));
    }

    /**
     * Tests the equals method with a different object.
     */
    @Test
    public void testEqualsDifferent() {
        // Create test instances
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        Object period2 = new Object();

        // Test the method
        assertFalse(period1.equals(period2));
    }

    /**
     * Tests the hashCode method.
     */
    @Test
    public void testHashCode() {
        // Create test instance
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);

        // Test the method
        int hashCode1 = period.hashCode();
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    /**
     * Tests the compareTo method with a lesser object.
     */
    @Test
    public void testCompareToLesser() {
        // Create test instances
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);

        // Test the method
        int comparison = period1.compareTo(period2);
        assertEquals(-4, comparison);
    }

    /**
     * Tests the compareTo method with an equal object.
     */
    @Test
    public void testCompareToEqual() {
        // Create test instances
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);

        // Test the method
        int comparison = period1.compareTo(period2);
        assertEquals(0, comparison);
    }

    /**
     * Tests the compareTo method with a greater object.
     */
    @Test
    public void testCompareToGreater() {
        // Create test instances
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(11);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);

        // Test the method
        int comparison = period1.compareTo(period2);
        assertEquals(4, comparison);
    }

    /**
     * Tests the add method with a positive value.
     */
    @Test
    public void testAddPositive() {
        // Create test instance
        MutablePeriod period = new MutablePeriod();

        // Test the method
        period.add(new BaseSingleFieldPeriod(7), DurationFieldType.DAYS);
        assertEquals(7, period.getDays());
    }

    /**
     * Tests the add method with a negative value.
     */
    @Test
    public void testAddNegative() {
        // Create test instance
        MutablePeriod period = new MutablePeriod();

        // Test the method
        period.add(new BaseSingleFieldPeriod(7), -DurationFieldType.DAYS);
        assertEquals(-7, period.getDays());
    }

    /**
     * Tests the add method with a zero value.
     */
    @Test
    public void testAddZero() {
        // Create test instance
        MutablePeriod period = new MutablePeriod();

        // Test the method
        period.add(new BaseSingleFieldPeriod(7), DurationFieldType.DAYS);
        period.add(new BaseSingleFieldPeriod(0), DurationFieldType.DAYS);
        assertEquals(7, period.getDays());
    }

    /**
     * Tests the add method with an incorrect period type.
     */
    @Test
    public void testAddIncorrectType() {
        // Create test instance
        MutablePeriod period = new MutablePeriod();

        // Test the method
        assertThrows(IllegalArgumentException.class, () -> {
            period.add(new BaseSingleFieldPeriod(7), DurationFieldType.HOURS);
        });
    }
}
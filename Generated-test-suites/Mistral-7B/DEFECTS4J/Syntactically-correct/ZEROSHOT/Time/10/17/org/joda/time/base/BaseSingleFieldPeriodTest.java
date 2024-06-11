package org.junit.joda.time.base;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test instance.
     */
    private BaseSingleFieldPeriod testInstance;

    /**
     * Set up test.
     */
    @Test
    public void setUp() {
        testInstance = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
    }

    /**
     * Test between method with Instant values.
     */
    @Test
    public void testBetweenWithInstantValues() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = chrono.getInstant(0L);
        ReadableInstant end = chrono.getInstant(1000L * 3600 * 24 * 7);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(7, result);
    }

    /**
     * Test between method with Partial values.
     */
    @Test
    public void testBetweenWithPartialValues() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadablePeriod zeroInstance = Period.ZERO;
        ReadablePartial start = new MutableLocalDateTime(chrono, 2000, 1, 1, 0, 0, 0).toLocalDate();
        ReadablePartial end = new MutableLocalDateTime(chrono, 2000, 1, 8, 0, 0, 0).toLocalDate();
        int result = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        assertEquals(7, result);
    }

    /**
     * Test standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.days(7);
        long millisPerUnit = 1000L * 60L * 60L * 24L;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(7, result);
    }

    /**
     * Test constructor with int argument.
     */
    @Test
    public void testConstructorWithIntArgument() {
        BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(7);
        assertNotNull(instance1);
        assertEquals(7, instance1.getValue());

        BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(instance1);
        assertNotNull(instance2);
        assertEquals(7, instance2.getValue());
        assertSame(instance1.getClass(), instance2.getClass());

        BaseSingleFieldPeriod instance3 = new BaseSingleFieldPeriod(instance1.getClass(), 12);
        assertNotNull(instance3);
        assertEquals(12, instance3.getValue());
        assertNotSame(instance1.getClass(), instance3.getClass());
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        assertEquals(7, testInstance.getValue());
    }

    /**
     * Test setValue method.
     */
    @Test
    public void testSetValue() {
        testInstance.setValue(12);
        assertEquals(12, testInstance.getValue());
    }

    /**
     * Test getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.DAYS, testInstance.getFieldType());
    }

    /**
     * Test getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), testInstance.getPeriodType());
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        assertEquals(1, testInstance.size());
    }

    /**
     * Test getFieldType method with index.
     */
    @Test
    public void testGetFieldTypeWithIndex() {
        assertEquals(DurationFieldType.DAYS, testInstance.getFieldType(0));
    }

    /**
     * Test getValue method with index.
     */
    @Test
    public void testGetValueWithIndex() {
        assertEquals(7, testInstance.getValue(0));
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        assertEquals(7, testInstance.get(DurationFieldType.DAYS));
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported() {
        assertTrue(testInstance.isSupported(DurationFieldType.DAYS));
        assertFalse(testInstance.isSupported(DurationFieldType.HOURS));
    }

    /**
     * Test toPeriod method.
     */
    @Test
    public void testToPeriod() {
        Period result = testInstance.toPeriod();
        assertEquals(7, result.getDays());
    }

    /**
     * Test toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        MutablePeriod result = testInstance.toMutablePeriod();
        assertEquals(7, result.getDays());
    }

    /**
     * Test equals method with null.
     */
    @Test
    public void testEqualsWithNull() {
        assertFalse(testInstance.equals(null));
    }

    /**
     * Test equals method with different types.
     */
    @Test
    public void testEqualsWithDifferentTypes() {
        assertFalse(testInstance.equals("Test"));
    }

    /**
     * Test equals method with different values.
     */
    @Test
    public void testEqualsWithDifferentValues() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(5);
        assertFalse(testInstance.equals(other));
    }

    /**
     * Test equals method with same values.
     */
    @Test
    public void testEqualsWithSameValues() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(7);
        assertTrue(testInstance.equals(other));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        int hashCode1 = testInstance.hashCode();
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(7);
        int hashCode2 = other.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}
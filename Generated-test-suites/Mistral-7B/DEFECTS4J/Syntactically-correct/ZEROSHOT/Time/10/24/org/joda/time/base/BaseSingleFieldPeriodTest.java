package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.test.BaseTest;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest extends BaseTest {

    /**
     * Tests constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        assertEquals(3, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Tests getValue method.
     */
    @org.junit.Test
    public void testGetValue() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        assertEquals(3, period.getValue());
    }

    /**
     * Tests setValue method.
     */
    @org.junit.Test
    public void testSetValue() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        period.setValue(2);
        assertEquals(2, period.getValue());
    }

    /**
     * Tests getFieldType method.
     */
    @org.junit.Test
    public void testGetFieldType() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3, fieldType);
        assertEquals(fieldType, period.getFieldType());
    }

    /**
     * Tests getPeriodType method.
     */
    @org.junit.Test
    public void testGetPeriodType() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3, fieldType);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Tests size method.
     */
    @org.junit.Test
    public void testSize() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3, fieldType);
        assertEquals(1, period.size());
    }

    /**
     * Tests getFieldType method with invalid index.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3, fieldType);
        period.getFieldType(2);
    }

    /**
     * Tests getValue method with invalid index.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3, fieldType);
        period.getValue(2);
    }

    /**
     * Tests equals method with same instance.
     */
    @org.junit.Test
    public void testEqualsSameInstance() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3, fieldType);
        assertTrue(period.equals(period));
    }

    /**
     * Tests equals method with different type.
     */
    @org.junit.Test
    public void testEqualsDifferentType() {
        DurationFieldType fieldType1 = DurationFieldType.days();
        DurationFieldType fieldType2 = DurationFieldType.weeks();
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3, fieldType1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3, fieldType2);
        assertFalse(period1.equals(period2));
    }

    /**
     * Tests equals method with different value.
     */
    @org.junit.Test
    public void testEqualsDifferentValue() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3, fieldType);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(4, fieldType);
        assertFalse(period1.equals(period2));
    }

    /**
     * Tests equals method with null object.
     */
    @org.junit.Test
    public void testEqualsNullObject() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3, fieldType);
        assertFalse(period.equals(null));
    }

    /**
     * Tests hashCode method.
     */
    @org.junit.Test
    public void testHashCode() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3, fieldType);
        int hashCode = period.hashCode();
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3, fieldType);
        assertEquals(hashCode, period2.hashCode());
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(4, fieldType);
        assertNotSame(hashCode, period3.hashCode());
    }

    /**
     * Tests compareTo method with same instance.
     */
    @org.junit.Test
    public void testCompareToSameInstance() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3, fieldType);
        assertEquals(0, period.compareTo(period));
    }

    /**
     * Tests compareTo method with different instance of the same type.
     */
    @org.junit.Test
    public void testCompareToSameTypeDifferentValue() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3, fieldType);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(4, fieldType);
        assertEquals(-1, period1.compareTo(period2));
        assertEquals(1, period2.compareTo(period1));
    }

    /**
     * Tests compareTo method with different type.
     */
    @org.junit.Test(expected = ClassCastException.class)
    public void testCompareToDifferentType() {
        DurationFieldType fieldType1 = DurationFieldType.days();
        DurationFieldType fieldType2 = DurationFieldType.weeks();
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3, fieldType1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3, fieldType2);
        period1.compareTo(period2);
    }

    /**
     * Tests between method with ReadableInstant.
     */
    @org.junit.Test
    public void testBetweenReadableInstant() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5, fieldType);
        ReadableInstant start = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BaseDateTime(1, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        int result = BaseSingleFieldPeriod.between(start, end, fieldType);
        assertEquals(5, result);
    }

    /**
     * Tests between method with invalid start or end.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidInstants() {
        DurationFieldType fieldType = DurationFieldType.days();
        ReadableInstant start = null;
        ReadableInstant end = new BaseDateTime(1, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        BaseSingleFieldPeriod.between(start, end, fieldType);
    }

    /**
     * Tests between method with ReadablePartial.
     */
    @org.junit.Test
    public void testBetweenReadablePartial() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5, fieldType);
        ReadablePartial start = new BaseDateTime(0, 0, 0, 1, 1, 1, 1, ISOChronology.getInstanceUTC());
        ReadablePartial end = new BaseDateTime(1, 0, 0, 2, 2, 2, 2, ISOChronology.getInstanceUTC());
        int result = BaseSingleFieldPeriod.between(start, end, period);
        assertEquals(5, result);
    }

    /**
     * Tests between method with invalid start or end.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidPartials() {
        DurationFieldType fieldType = DurationFieldType.days();
        ReadablePartial start = null;
        ReadablePartial end = new BaseDateTime(1, 0, 0, 2, 2, 2, 2, ISOChronology.getInstanceUTC());
        BaseSingleFieldPeriod.between(start, end, fieldType);
    }

    /**
     * Tests standardPeriodIn method.
     */
    @org.junit.Test
    public void testStandardPeriodIn() {
        DurationFieldType fieldType = DurationFieldType.days();
        Period period = Period.days(5);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 86400000);
        assertEquals(5, result);
    }

    /**
     * Tests standardPeriodIn method with null period.
     */
    @org.junit.Test
     public void testStandardPeriodInNullPeriod() {
        long millisPerUnit = 86400000;
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, millisPerUnit);
        assertEquals(0, result);
    }

}
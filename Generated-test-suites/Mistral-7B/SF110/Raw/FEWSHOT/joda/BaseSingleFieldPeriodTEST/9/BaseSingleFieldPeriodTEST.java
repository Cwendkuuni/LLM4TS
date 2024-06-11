package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod_getValue() {
        BaseSingleFieldPeriod instance = new DailyFields(1);
        int actualValue = instance.getValue();
        int expectedValue = 1;
        assertEquals("Expected value is: " + expectedValue, expectedValue, actualValue);
    }

    @Test
    public void testBaseSingleFieldPeriod_equals() {
        BaseSingleFieldPeriod instance1 = new DailyFields(1);
        BaseSingleFieldPeriod instance2 = new DailyFields(1);
        assertTrue("Expected both instances to be equal", instance1.equals(instance2));
    }

    @Test
    public void testBaseSingleFieldPeriod_inequality() {
        BaseSingleFieldPeriod instance1 = new DailyFields(1);
        BaseSingleFieldPeriod instance2 = new DailyFields(2);
        assertFalse("Expected instances to be unequal", instance1.equals(instance2));
    }

    @Test
    public void testBaseSingleFieldPeriod_inequalityWithDifferentClass() {
        BaseSingleFieldPeriod instance1 = new DailyFields(1);
        Object instance2 = new Integer(1);
        assertFalse("Expected instances to be unequal", instance1.equals(instance2));
    }

    @Test
    public void testBaseSingleFieldPeriod_compareToSameInstance() {
        BaseSingleFieldPeriod instance = new DailyFields(1);
        int comparisonResult = instance.compareTo(instance);
        assertEquals("Expected comparison result to be: 0", 0, comparisonResult);
    }

    @Test
    public void testBaseSingleFieldPeriod_compareToDifferentValue() {
        BaseSingleFieldPeriod instance1 = new DailyFields(1);
        BaseSingleFieldPeriod instance2 = new DailyFields(2);
        int comparisonResult = instance1.compareTo(instance2);
        assertEquals("Expected comparison result to be: -1", -1, comparisonResult);
    }

    @Test
    public void testBaseSingleFieldPeriod_compareToLargerValue() {
        BaseSingleFieldPeriod instance1 = new DailyFields(2);
        BaseSingleFieldPeriod instance2 = new DailyFields(1);
        int comparisonResult = instance1.compareTo(instance2);
        assertEquals("Expected comparison result to be: 1", 1, comparisonResult);
    }

    @Test
    public void testBaseSingleFieldPeriod_compareToNull() {
        BaseSingleFieldPeriod instance = new DailyFields(1);
        int comparisonResult = instance.compareTo(null);
        assertNull("Expected comparison result to be null", comparisonResult);
    }

    @Test
    public void testBaseSingleFieldPeriod_getFieldType() {
        BaseSingleFieldPeriod instance = new DailyFields(1);
        DurationFieldType expectedFieldType = DailyFields.DAYS;
        DurationFieldType actualFieldType = instance.getFieldType();
        assertEquals("Expected field type to be: " + expectedFieldType, expectedFieldType, actualFieldType);
    }

    @Test
    public void testBaseSingleFieldPeriod_getPeriodType() {
        BaseSingleFieldPeriod instance = new DailyFields(1);
        PeriodType expectedPeriodType = DailyFields.INSTANCE;
        PeriodType actualPeriodType = instance.getPeriodType();
        assertEquals("Expected period type to be: " + expectedPeriodType, expectedPeriodType, actualPeriodType);
    }

    @Test
    public void testBaseSingleFieldPeriod_size() {
        BaseSingleFieldPeriod instance = new DailyFields(1);
        int expectedSize = 1;
        int actualSize = instance.size();
        assertEquals("Expected size to be: " + expectedSize, expectedSize, actualSize);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBaseSingleFieldPeriod_getFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod instance = new DailyFields(1);
        instance.getFieldType(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBaseSingleFieldPeriod_getValueInvalidIndex() {
        BaseSingleFieldPeriod instance = new DailyFields(1);
        instance.getValue(2);
    }

    @Test
    public void testBaseSingleFieldPeriod_toPeriod() {
        BaseSingleFieldPeriod instance = new DailyFields(1);
        Period expectedPeriod = new Period(new DailyFields(1));
        Period actualPeriod = instance.toPeriod();
        assertEquals("Expected period to be: " + expectedPeriod, expectedPeriod, actualPeriod);
    }

    @Test
    public void testBaseSingleFieldPeriod_toMutablePeriod() {
        BaseSingleFieldPeriod instance = new DailyFields(1);
        MutablePeriod expectedMutablePeriod = new MutablePeriod();
        expectedMutablePeriod.add(new DailyFields(1));
        MutablePeriod actualMutablePeriod = instance.toMutablePeriod();
        assertTrue("Expected mutable period to have the same fields as the original instance", actualMutablePeriod.isValid()
                && actualMutablePeriod.getBasePeriod().getPeriodType() == DailyFields.INSTANCE
                && actualMutablePeriod.getBasePeriod().getValue(0) == 1);
    }
}
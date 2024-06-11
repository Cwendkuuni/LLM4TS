package org.junit.test;

import org.joda.time.base.BaseSingleFieldPeriod;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    // Test getValue()
    @Test
    public void testGetValue() {
        // Test with an instance having a value
        BaseSingleFieldPeriod testObject = new TestBaseSingleFieldPeriod(5);
        int actual = testObject.getValue();
        int expected = 5;
        assertEquals(expected, actual);

        // Test edge case with an instance having a value of 0
        BaseSingleFieldPeriod testObjectZero = new TestBaseSingleFieldPeriod(0);
        int actualZero = testObjectZero.getValue();
        int expectedZero = 0;
        assertEquals(expectedZero, actualZero);

        // Test exception handling with a null instance
        BaseSingleFieldPeriod testObjectNull = null;
        try {
            testObjectNull.getValue();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // Test getFieldType()
    @Test
    public void testGetFieldType() {
        // Test with an instance
        BaseSingleFieldPeriod testObject = new TestBaseSingleFieldPeriod(5);
        DurationFieldType actual = testObject.getFieldType();
        DurationFieldType expected = DurationFieldType.MILLISECONDS; // Assuming BaseSingleFieldPeriod is a TestBaseSingleFieldPeriod implementation that uses MILLISECONDS as its field type
        assertEquals(expected, actual);

        // Test edge case with a null instance
        BaseSingleFieldPeriod testObjectNull = null;
        try {
            testObjectNull.getFieldType();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // Test size()
    @Test
    public void testSize() {
        // Test with an instance
        BaseSingleFieldPeriod testObject = new TestBaseSingleFieldPeriod(5);
        int actual = testObject.size();
        int expected = 1;
        assertEquals(expected, actual);

        // Test edge case with a null instance
        BaseSingleFieldPeriod testObjectNull = null;
        try {
            testObjectNull.size();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // Test isSupported()
    @Test
    public void testIsSupported() {
        // Test with an instance and a supported field type
        BaseSingleFieldPeriod testObject = new TestBaseSingleFieldPeriod(5);
        DurationFieldType testFieldType = DurationFieldType.MILLISECONDS;
        boolean actual = testObject.isSupported(testFieldType);
        boolean expected = true;
        assertTrue(expected, actual);

        // Test edge case with an unsupported field type
        BaseSingleFieldPeriod testObject = new TestBaseSingleFieldPeriod(5);
        DurationFieldType testFieldType = DurationFieldType.YEARS;
        boolean actual = testObject.isSupported(testFieldType);
        boolean expected = false;
        assertFalse(expected, actual);

        // Test exception handling with a null field type
        BaseSingleFieldPeriod testObject = new TestBaseSingleFieldPeriod(5);
        DurationFieldType testFieldTypeNull = null;
        try {
            testObject.isSupported(testFieldTypeNull);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // Test compareTo()
    @Test
    public void testCompareTo() {
        // Test with two instances with different values
        BaseSingleFieldPeriod testObject1 = new TestBaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod testObject2 = new TestBaseSingleFieldPeriod(3);
        int actual = testObject1.compareTo(testObject2);
        int expected = 1;
        assertEquals(expected, actual);

        // Test edge case with two equal instances
        BaseSingleFieldPeriod testObject1 = new TestBaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod testObject2 = new TestBaseSingleFieldPeriod(5);
        int actual = testObject1.compareTo(testObject2);
        int expected = 0;
        assertEquals(expected, actual);

        // Test exception handling with a null instance
        BaseSingleFieldPeriod testObject1 = new TestBaseSingleFieldPeriod(5);
        Object testObject2Null = null;
        try {
            testObject1.compareTo(testObject2Null);
        } catch (ClassCastException e) {
            assertTrue(true);
        }
    }
}
package org.joda.time.field.test;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedDurationField() {
        DurationFieldType type = DurationFieldType.instance(UnsupportedDurationField.MINUTES);
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        // Test methods that return suitable values
        assertEquals(type, unsupportedField.getType());
        assertEquals(type.getName(), unsupportedField.getName());
        assertEquals(false, unsupportedField.isSupported());
        assertEquals(true, unsupportedField.isPrecise());

        // Test methods that throw UnsupportedOperationException
        try {
            unsupportedField.getValue(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getValueAsLong(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getValue(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getValueAsLong(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getMillis(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getMillis(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getMillis(1, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getMillis(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.add(1L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.add(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getDifference(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getDifferenceAsLong(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getUnitMillis();
            fail("Expected zero, but got: " + unsupportedField.getUnitMillis());
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.compareTo(null);
            fail("Expected zero, but got: " + unsupportedField.compareTo(null));
        } catch (NullPointerException e) {
            // Expected, but not what we were testing for
        }

        try {
            unsupportedField.compareTo(new UnsupportedDurationField(type));
            fail("Expected zero, but got: " + unsupportedField.compareTo(new UnsupportedDurationField(type)));
        } catch (AssertionError e) {
            // Expected
        }

        // Test equals and hashcode
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);

        assertEquals(unsupportedField, unsupportedField1);
        assertEquals(unsupportedField.hashCode(), unsupportedField1.hashCode());
        assertNotEquals(unsupportedField, unsupportedField2);
        assertNotEquals(unsupportedField.hashCode(), unsupportedField2.hashCode());
        assertNotEquals(unsupportedField, unsupportedField3);
        assertNotEquals(unsupportedField.hashCode(), unsupportedField3.hashCode());
    }
}
package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testUnsupportedMethods() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);

        try {
            field.getValue(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.getValueAsLong(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.getValue(1, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.getValueAsLong(1, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.getMillis(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.getMillis(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.getMillis(1, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.getMillis(1L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.add(1, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.add(1, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.getDifference(1, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            field.getDifferenceAsLong(1, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);

        assertEquals(field1.hashCode(), field1.hashCode());
        assertEquals(field1.hashCode(), field2.hashCode());
        assertNotEquals(field1.hashCode(), field3.hashCode());
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);

        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
        assertNotEquals(field1, null);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[null]", field.toString());
    }

}
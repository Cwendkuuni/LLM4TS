package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertNotNull(field1);
        assertNotNull(field2);
        assertEquals(field1, field2);
    }

    @Test
    public void testUnsupportedMethods() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);

        try {
            field.getValue(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.getValueAsLong(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.getValue(1L, 2L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.getValueAsLong(1L, 2L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.getMillis(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.getMillis(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.getMillis(1, 2L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.getMillis(1L, 2L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.add(1L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.add(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.getDifference(1L, 2L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            field.getDifferenceAsLong(1L, 2L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testEqualsHashCodeToString() {
        DurationFieldType type1 = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        DurationFieldType type2 = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);

        assertEquals(field1, field1);
        assertEquals(field1, field1);
        assertEquals(field1, field1);

        assertNotEquals(field1, null);
        assertNotEquals(field1, new Object());
        assertNotEquals(field1, field2);

        assertEquals(field1.hashCode(), field1.hashCode());
        assertEquals(field1.hashCode(), field1.hashCode());
        assertNotEquals(field1.hashCode(), field2.hashCode());

        assertEquals("UnsupportedDurationField[INSTANT_SECONDS]", field1.toString());
        assertEquals("UnsupportedDurationField[INSTANT_SECONDS]", field1.toString());
        assertNotEquals("UnsupportedDurationField[MILLISECONDS]", field1.toString());
    }
}
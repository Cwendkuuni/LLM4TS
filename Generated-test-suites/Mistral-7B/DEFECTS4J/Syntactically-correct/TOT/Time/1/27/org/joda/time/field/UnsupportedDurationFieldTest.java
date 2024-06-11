package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link UnsupportedDurationField}.
 */
public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedField() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES_30);
        assertEquals(DurationFieldType.MINUTES_30, field.getType());
        assertEquals("Minutes_30", field.getName());
        assertFalse(field.isSupported());
        assertTrue(field.isPrecise());

        try {
            int value = field.getValue(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long valueAsLong = field.getValueAsLong(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            int value = field.getValue(1000L, 123456789L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long valueAsLong = field.getValueAsLong(1000L, 123456789L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long millis = field.getMillis(5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long millis = field.getMillis(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long millis = field.getMillis(5, 123456789L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long millis = field.getMillis(1000L, 123456789L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long add = field.add(123456789L, 5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long add = field.add(123456789L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            int difference = field.getDifference(123456789L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long differenceAsLong = field.getDifferenceAsLong(123456789L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long unitMillis = field.getUnitMillis();
            fail("Expected zero");
        } catch (AssertionError e) {
            // Expected
        }

        try {
            int compareTo = field.compareTo(new ShortSecondsField(1));
            fail("Expected zero");
        } catch (AssertionError e) {
            // Expected
        }

        try {
            field.setValue(123456789L, 5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            field.setValueAsLong(123456789L, 5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testUnsupportedFieldException() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES_30);
        try {
            field.getValue(1000L);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.getValueAsLong(1000L);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.getValue(1000L, 123456789L);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.getValueAsLong(1000L, 123456789L);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.getMillis(5);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.getMillis(1000L);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.getMillis(5, 123456789L);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.getMillis(1000L, 123456789L);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.add(123456789L, 5);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.add(123456789L, 1000L);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.getDifference(123456789L, 1000L);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }

        try {
            field.getDifferenceAsLong(123456789L, 1000L);
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes_30 field is unsupported", e.getMessage());
        }
    }
}
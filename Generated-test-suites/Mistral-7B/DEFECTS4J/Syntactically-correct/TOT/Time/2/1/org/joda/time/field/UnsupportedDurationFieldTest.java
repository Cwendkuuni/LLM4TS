package org.joda.time.field.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

/**
 * Tests for the UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Tests the getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.WEEKS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, other);
    }

    /**
     * Tests the isSupported method.
     */
    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS);
        assertFalse(unsupported.isSupported());
    }

    /**
     * Tests the getName method.
     */
    @Test
    public void testGetName() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS);
        assertEquals("weeks", unsupported.getName());
    }

    /**
     * Tests the equals method.
     */
    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported4 = new UnsupportedDurationField(DurationFieldType.WEEKS());

        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported2);
        assertNotEquals(unsupported1, unsupported3);
        assertNotEquals(unsupported1, unsupported4);
    }

    /**
     * Tests the hashCode method.
     */
    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

        int hashCode1 = unsupported1.hashCode();
        int hashCode2 = unsupported2.hashCode();
        int hashCode3 = unsupported3.hashCode();

        // Test hashcode equality
        assertEquals(hashCode1, hashCode2);

        // Test different hashcodes for different UnsupportedDurationFields
        assertNotEquals(hashCode1, hashCode3);
    }

    /**
     * Tests the toString method.
     */
    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS);
        String toString = unsupported.toString();
        assertTrue(toString.startsWith("UnsupportedDurationField["));
        assertTrue(toString.endsWith("]"));
    }
}
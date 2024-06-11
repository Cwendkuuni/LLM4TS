package org.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.dayOfYear());
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testGetInstanceSameType() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.dayOfYear());
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.dayOfYear());
        assertEquals(unsupportedField1, unsupportedField2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceUnsupportedType() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(null);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueNegativeDuration() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.days());
        throw unsupported();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueUnsupportedInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        throw unsupported();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferenceNegativeValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        long minuendInstant = 0;
        long subtrahendInstant = -1;
        throw unsupported();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferenceSameInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        long minuendInstant = 0;
        long subtrahendInstant = 0;
        throw unsupported();
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(iType + " field is unsupported");
    }
}
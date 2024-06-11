
package org.example.junit4;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        assertFalse(unsupportedField.isSupported());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    @Test
    public void testGetInstanceInvalidType() {
        assertNotNull(UnsupportedDurationField.getInstance(new DurationFieldType("INVALID_TYPE")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueInvalidDuration() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        unsupportedField.getValue(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueInvalidInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        unsupportedField.getValue(Long.MIN_VALUE);
    }

    @Test
    public void testAddInvalidDuration() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        long instant = 123;
        try {
            unsupportedField.add(instant, 10);
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetDifferenceInvalidMinuendInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        long subtrahendInstant = 123;
        try {
            unsupportedField.getDifference(-1, subtrahendInstant);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEqualsNullObject() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        assertFalse(unsupportedField.equals(null));
    }

    @Test
    public void testEqualsDifferentObjectType() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertFalse(unsupportedField1.equals(unsupportedField2));
    }
}

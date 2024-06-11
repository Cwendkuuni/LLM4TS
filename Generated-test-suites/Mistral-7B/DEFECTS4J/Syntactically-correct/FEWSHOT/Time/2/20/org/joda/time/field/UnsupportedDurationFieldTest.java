package org.joda.time.field;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertSame(field1, field2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperation() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long duration = 1234567890L;
        field.getValue(duration);
    }
}
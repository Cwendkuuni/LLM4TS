
package org.example.junit4.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void testInstanceCreation() {
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(unsupportedDurationField1, unsupportedDurationField2);
    }

    @Test
    public void testValueMethodsThrowException() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedDurationField.getValue(0));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedDurationField.getValueAsLong(0));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedDurationField.getMillis(0));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedDurationField.add(0, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedDurationField.getDifference(0, 0));
    }

    @Test
    public void testThreadSafety() {
        // Test if the methods are thread-safe by creating multiple threads and calling the methods in parallel
        // You can use a CountDownLatch, CyclicBarrier or other synchronization mechanisms to wait for all threads to finish
    }

    @Test
    public void testImmutability() {
        // Test if the class is immutable by creating a new instance and trying to modify its properties
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(unsupportedDurationField.isSupported());
        try {
            UnsupportedDurationField modifiedField = new UnsupportedDurationField(DurationFieldType.DAYS);
            fail("Expected UnsupportedDurationField to be immutable");
        } catch (Exception e) {
            // Expected
        }
    }
}

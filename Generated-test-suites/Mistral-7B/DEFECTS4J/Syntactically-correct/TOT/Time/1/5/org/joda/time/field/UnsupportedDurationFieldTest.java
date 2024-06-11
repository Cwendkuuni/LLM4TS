import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test for UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedDurationField() {
        // Get an instance of UnsupportedDurationField
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);

        // Test getters
        assertEquals(DurationFieldType.SECONDS, unsupportedField.getType());
        assertEquals("seconds", unsupportedField.getName());

        // Test methods that throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getUnitMillis());
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.compareTo(null));
    }
}
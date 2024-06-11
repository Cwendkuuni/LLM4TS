import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Tests the getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        assertEquals(DurationFieldType.milliseconds(), field.getType());
        assertEquals("milliseconds", field.getName());
        assertEquals(false, field.isSupported());
        assertEquals(true, field.isPrecise());
    }

    /**
     * Tests the getValue method with a long duration.
     */
    @Test
    public void testGetValueLongDuration() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertThrows(UnsupportedOperationException.class, () -> field.getValue((long) 12345));
    }

    /**
     * Tests the getValue method with a long instant.
     */
    @Test
    public void testGetValueLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        assertThrows(UnsupportedOperationException.class, () -> field.getValue((long) 12345, 123456789));
    }

    /**
     * Tests the getValueAsLong method with a long duration.
     */
    @Test
    public void testGetValueAsLongLongDuration() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong((long) 12345));
    }

    /**
     * Tests the getValueAsLong method with a long instant.
     */
    @Test
    public void testGetValueAsLongLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong((long) 12345, 123456789));
    }

    /**
     * Tests the getMillis method with an int value.
     */
    @Test
    public void testGetMillisIntValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis((int) 12345));
    }

    /**
     * Tests the getMillis method with a long value.
     */
    @Test
    public void testGetMillisLongValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis((long) 12345));
    }

    /**
     * Tests the getMillis method with an int value and a long instant.
     */
    @Test
    public void testGetMillisIntValueLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis((int) 12345, 123456789));
    }

    /**
     * Tests the getMillis method with a long value and a long instant.
     */
    @Test
    public void testGetMillisLongValueLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis((long) 12345, 123456789));
    }

    /**
     * Tests the add method with a long instant and an int value.
     */
    @Test
    public void testAddLongInstantIntValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertThrows(UnsupportedOperationException.class, () -> field.add(123456789, (int) 12345));
    }

    /**
     * Tests the add method with a long instant and a long value.
     */
    @Test
    public void testAddLongInstantLongValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertThrows(UnsupportedOperationException.class, () -> field.add(123456789, (long) 12345));
    }

    /**
     * Tests the getDifference method with two long instants.
     */
    @Test
    public void testGetDifferenceLongInstant1LongInstant2() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(123456789, 123456788));
    }

    /**
     * Tests the getDifferenceAsLong method with two long instants.
     */
    @Test
    public void testGetDifferenceAsLongLongInstant1LongInstant2() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(123456789, 123456788));
    }
}
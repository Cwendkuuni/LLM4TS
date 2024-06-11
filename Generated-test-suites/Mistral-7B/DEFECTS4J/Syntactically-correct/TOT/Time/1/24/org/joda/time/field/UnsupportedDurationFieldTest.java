import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DateTimeConstants;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    @Test
    public void testInstance() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(DurationFieldType.MILLISECONDS, unsupportedField.getType());
        assertEquals("Milliseconds", unsupportedField.getName());
        assertEquals(false, unsupportedField.isSupported());
        assertEquals(true, unsupportedField.isPrecise());
    }

    @Test
    public void testValue() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(DateTimeConstants.MILLIS_PER_DAY));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(DateTimeConstants.MILLIS_PER_DAY));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_DAY));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_DAY));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(DateTimeConstants.MILLIS_PER_DAY));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(DateTimeConstants.MILLIS_PER_DAY * 2));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_DAY));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(DateTimeConstants.MILLIS_PER_DAY * 2, DateTimeConstants.MILLIS_PER_DAY));
    }

    @Test
    public void testAdd() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_MINUTE));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_MINUTE * 2));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_MINUTE, DateTimeConstants.MILLIS_PER_DAY));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_MINUTE * 2, DateTimeConstants.MILLIS_PER_DAY));
    }

    @Test
    public void testDifference() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_DAY));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_DAY));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(DateTimeConstants.MILLIS_PER_DAY * 2, DateTimeConstants.MILLIS_PER_DAY));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(DateTimeConstants.MILLIS_PER_DAY * 2, DateTimeConstants.MILLIS_PER_DAY));
    }

    @Test
    public void testCompareTo() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        DurationField supportedField = new DurationField(DateTimeConstants.MILLISECONDS, false);
        assertEquals(-1, unsupportedField.compareTo(supportedField));
    }
}
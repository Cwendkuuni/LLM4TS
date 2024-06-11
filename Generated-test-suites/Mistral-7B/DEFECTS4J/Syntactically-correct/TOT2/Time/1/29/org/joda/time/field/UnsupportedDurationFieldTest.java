
import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupportedField.getType());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertFalse(unsupportedField.isSupported());
    }

    // Test other methods following the same pattern as above.

    @Test(expected = NullPointerException.class)
    public void testConstructorNullArguments() {
        new UnsupportedDurationField(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceNullArguments() {
        UnsupportedDurationField.getInstance(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidDurationFieldType() {
        DurationFieldType invalidType = new DurationFieldType("InvalidType");
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(invalidType);
        unsupportedField.isSupported();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetInstanceInvalidDurationFieldType() {
        DurationFieldType invalidType = new DurationFieldType("InvalidType");
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(invalidType);
        unsupportedField.isSupported();
    }

    @Test
    public void testAddLongOverflow() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long maxLong = Long.MAX_VALUE;
        long instant = maxLong + 1;
        long value = Long.MAX_VALUE;
        try {
            long result = unsupportedField.add(instant, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testAddLongUnderflow() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long minLong = Long.MIN_VALUE;
        long instant = minLong - 1;
        long value = Long.MIN_VALUE;
        try {
            long result = unsupportedField.add(instant, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}
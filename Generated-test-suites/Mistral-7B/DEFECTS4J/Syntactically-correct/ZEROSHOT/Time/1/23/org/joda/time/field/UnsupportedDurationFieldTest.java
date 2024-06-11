import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest extends BaseTestCase {

    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertSame(type, unsupported.getType());
        assertEquals("instant.seconds", unsupported.getName());
        assertFalse(unsupported.isSupported());
        assertTrue(unsupported.isPrecise());
    }

    public void testGetInstanceNullType() {
        try {
            UnsupportedDurationField.getInstance(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testGetValueUnsupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 123456L;
        try {
            unsupported.getValue(duration);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    public void testGetValueAsLongUnsupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 123456L;
        try {
            unsupported.getValueAsLong(duration);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    public void testGetValueNullDuration() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        try {
            unsupported.getValue((long) 0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    public void testGetValueInstantNull() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        try {
            unsupported.getValueAsLong(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testAddUnsupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        int value = 456;
        try {
            unsupported.add(instant, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    public void testAddLongUnsupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        long value = 456L;
        try {
            unsupported.add(instant, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    public void testGetDifferenceUnsupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1234567890123L;
        long subtrahendInstant = 1234567890122L;
        try {
            unsupported.getDifference(minuendInstant, subtrahendInstant);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    public void testGetDifferenceAsLongUnsupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1234567890123L;
        long subtrahendInstant = 1234567890122L;
        try {
            unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    public void testCompareToSupported() {
        DurationFieldType type1 = DurationFieldType.INSTANT_SECS;
        DurationField field1 = new InstantFields(type1, 10, 20, 30, 40, 50);

        DurationFieldType type2 = DurationFieldType.INSTANT_MILLIS;
        DurationField field2 = new InstantFields(type2, 10000);

        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type1);
        int comparison = unsupported.compareTo(field1);
        assertEquals(1, comparison);
    }

    public void testCompareToUnsupported() {
        DurationFieldType type1 = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);

        DurationFieldType type2 = DurationFieldType.INSTANT_MILLIS;
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        int comparison = unsupported1.compareTo(unsupported2);
        assertEquals(0, comparison);
    }

    public void testHashCode() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String name = unsupported.getName();
        int hashCode = unsupported.hashCode();
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        int hashCode2 = unsupported2.hashCode();
        assertEquals(hashCode, hashCode2);
        assertNotSame(name.hashCode(), hashCode);
    }

    public void testToString() {
        DurationFieldType type = DurationFieldType.INSTANT_SECS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String actual = unsupported.toString();
        String expected = "UnsupportedDurationField[instant.seconds]";
        assertEquals(expected, actual);
    }
}
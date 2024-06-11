import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest extends BaseTestCase {

    @org.junit.Test
    public void testUnsupportedDurationField() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        // Test getters
        assertEquals(type, unsupportedField.getType());
        assertEquals(type.getName(), unsupportedField.getName());
        assertEquals(false, unsupportedField.isSupported());
        assertEquals(true, unsupportedField.isPrecise());

        // Test methods that throw UnsupportedOperationException
        try {
            unsupportedField.getValue(1L);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getValueAsLong(1L);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getValue(1L, 1L);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getValueAsLong(1L, 1L);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getMillis(1);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getMillis(1L);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getMillis(1, 1L);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getMillis(1L, 1L);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.add(1L, 1);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.add(1L, 1L);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getDifference(1L, 1L);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getDifferenceAsLong(1L, 1L);
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getUnitMillis();
            fail("Unexpected result");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.compareTo(null);
            fail("Unexpected result");
        } catch (ClassCastException e) {
            // Expected
        }

        try {
            unsupportedField.compareTo(new UnsupportedDurationField(DurationFieldType.DAYS));
            fail("Unexpected result");
        } catch (AssertionError e) {
            // Expected
        }

        // Test equals, hashCode and toString
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedField, unsupportedField2);
        assertEquals(unsupportedField.hashCode(), unsupportedField2.hashCode());
        assertEquals("UnsupportedDurationField[INSTANT_SECONDS]", unsupportedField.toString());
    }
}
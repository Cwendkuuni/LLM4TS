import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        DurationFieldType expectedType = DurationFieldType.MILLISECONDS;
        DurationFieldType actualType = unsupportedDurationField.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testgetName() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        String expectedName = "milliseconds";
        String actualName = unsupportedDurationField.getName();
        assertEquals(expectedName, actualName);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullType() {
        DurationFieldType type = null;
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithInvalidType() {
        DurationFieldType type = new DurationFieldType("invalid");
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(type);
    }

    @Test
    public void testFirstCallToGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedDurationField1, unsupportedDurationField2);
    }

    @Test
    public void testUnsupportedMethods() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        try {
            unsupportedDurationField.getValue(0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testMethodsThatReturnZero() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(0, unsupportedDurationField.getUnitMillis());
        assertEquals(0, unsupportedDurationField.compareTo(null));
    }

    @Test
    public void testGetInstanceWithEmptyCache() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedDurationField1, unsupportedDurationField2);
        UnsupportedDurationField unsupportedDurationField3 = UnsupportedDurationField.getInstance(type);
        assertNotSame(unsupportedDurationField1, unsupportedDurationField3);
    }

    @Test
    public void testGetInstanceWithSameTypeTwice() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedDurationField1, unsupportedDurationField2);
    }

    @Test
    public void testGetInstanceWithDifferentTypes() {
        DurationFieldType type1 = DurationFieldType.MILLISECONDS;
        DurationFieldType type2 = DurationFieldType.SECONDS;
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(type2);
        assertNotSame(unsupportedDurationField1, unsupportedDurationField2);
    }
}
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals(DurationFieldType.HOURS, unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertEquals("Minutes", unsupportedDurationField.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        Object obj = new Object();

        assertTrue(unsupportedDurationField1.equals(unsupportedDurationField1));
        assertTrue(unsupportedDurationField1.equals(unsupportedDurationField2));
        assertFalse(unsupportedDurationField1.equals(obj));
    }

    @Test
    public void testCompareTo() {
        DurationField supportedDurationField = new DurationField(1, DurationFieldType.HOURS);
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);

        assertFalse(unsupportedDurationField1.compareTo(supportedDurationField) > 0);
        assertTrue(unsupportedDurationField1.compareTo(unsupportedDurationField2) == 0);
        assertTrue(unsupportedDurationField2.compareTo(unsupportedDurationField1) == 0);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);

        int hashCode1 = unsupportedDurationField1.hashCode();
        int hashCode2 = unsupportedDurationField2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[Days]", unsupportedDurationField.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedDurationField.getValue(1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedDurationField.getName(null);
    }
}
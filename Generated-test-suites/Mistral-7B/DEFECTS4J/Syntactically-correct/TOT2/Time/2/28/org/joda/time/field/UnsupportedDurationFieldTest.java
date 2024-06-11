
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        DurationFieldType type = unsupportedDurationField.getType();
        assertEquals(DurationFieldType.MILLISECONDS, type);
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        String name = unsupportedDurationField.getName();
        assertEquals("MILLISECONDS", name);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueNegativeDuration() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getValue(-1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueMaxValueDuration() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getValue(Long.MAX_VALUE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueMinValueDuration() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getValue(Long.MIN_VALUE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddNegativeInstant() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.add(-1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddMaxValueInstant() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.add(Long.MAX_VALUE, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddMinValueInstant() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.add(Long.MIN_VALUE, 0);
    }
}
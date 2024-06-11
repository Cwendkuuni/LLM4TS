import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.Duration;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

    @Test
    public void testInstance() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        assertEquals("UnsupportedDurationField[instant]", unsupportedField.toString());
    }

    @Test
    public void testGetType() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        assertEquals(DurationFieldType.INSTANT, unsupportedField.getType());
    }

    @Test
    public void testGetName() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        assertEquals("instant", unsupportedField.getName());
    }

    @Test
    public void testIsSupported() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        assertTrue(unsupportedField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.getValue(Duration.standardHours(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.getValueAsLong(Duration.standardHours(1).getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.getValue(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstantAndDuration() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.getValue(0L, Duration.standardHours(1).getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.getMillis(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstantAndDuration() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.getMillis(0L, Duration.standardHours(1).getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.add(0L, Duration.standardHours(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        unsupportedField.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void testGetUnitMillis() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        DurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
    }

    @Test
    public void testEquals() {
        DurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        DurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        assertTrue(unsupportedField1.equals(unsupportedField2));
    }

    @Test
    public void testHashCode() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        assertEquals(unsupportedField.hashCode(), unsupportedField.hashCode());
    }

    @Test
    public void testToString() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        assertEquals("UnsupportedDurationField[instant]", unsupportedField.toString());
    }

    @Test
    public void testReadResolve() {
        DurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANT);
        assertEquals("UnsupportedDurationField[instant]", unsupportedField.toString());
        unsupportedField = (DurationField) unsupportedField.readResolve();
        assertEquals("UnsupportedDurationField[instant]", unsupportedField.toString());
    }

    @Test
    public void testUnsupportedOperationException() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT);
        assertEquals("instant field is unsupported", unsupportedField.unsupported().getMessage());
    }
}
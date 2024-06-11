import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, unsupported2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.isSupported();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getValue(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getValueAsLong(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.add(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(unsupported1, unsupported2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int hashCode = unsupported.hashCode();
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(hashCode, unsupported2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[DAYS]", unsupported.toString());
    }

}
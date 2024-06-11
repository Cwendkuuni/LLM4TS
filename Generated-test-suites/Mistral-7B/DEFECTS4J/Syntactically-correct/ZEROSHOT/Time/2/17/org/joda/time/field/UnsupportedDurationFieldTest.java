import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

    private static UnsupportedDurationField unsupportedField;

    @BeforeClass
    public static void setup() {
        unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.HOURS_30);
    }

    @Test
    public void testIsSupported() {
        assertEquals(false, unsupportedField.isSupported());
    }

    @Test
    public void testGetValue() {
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1, 1L));

        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1, 1, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1, 1, 1L));

        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1, 1L));
    }

    @Test
    public void testAdd() {
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1L));
    }

    @Test
    public void testGetDifference() {
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(1L, 1L));
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationField supportedField = DurationFieldType.MILLISECONDS.getStandardField();
        assertEquals(0, unsupportedField.compareTo(supportedField));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.HOURS_30);
        assertEquals(other, unsupportedField);
        assertNotEquals(new Object(), unsupportedField);
        assertNotEquals(new DurationField() {
            @Override
            public DurationFieldType getType() {
                return DurationFieldType.HOURS_31;
            }
        }, unsupportedField);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.HOURS_30);
        assertEquals(unsupportedField.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[HOURS_30]", unsupportedField.toString());
    }

}
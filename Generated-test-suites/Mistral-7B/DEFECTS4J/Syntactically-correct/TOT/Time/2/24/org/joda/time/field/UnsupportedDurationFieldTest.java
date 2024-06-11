import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldInstance;
import org.joda.time.BaseInterval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedDurationField_getInstance() {
        // Test getting an instance of UnsupportedDurationField
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);
        assertEquals(DurationFieldType.INSTANCE_DAY_OF_MONTH, unsupportedField.getType());
        assertEquals("dayOfMonth", unsupportedField.getName());
        assertEquals(false, unsupportedField.isSupported());
        assertEquals(true, unsupportedField.isPrecise());

        // Test getting a new instance for the same type
        DurationField sameUnsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);
        assertEquals(unsupportedField, sameUnsupportedField);
    }

    @Test
    public void testUnsupportedDurationField_getValue() {
        // Test getting value with UnsupportedDurationField
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_WEEK);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(1L, 1L));
    }

    @Test
    public void testUnsupportedDurationField_add() {
        // Test addition with UnsupportedDurationField
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_WEEK);
        BaseInterval interval = new BaseInterval(1L, unsupportedField);
        assertThrows(UnsupportedOperationException.class, () -> interval.add(DurationFieldInstance.of(DurationFieldType.INSTANCE_DAYS, 1)));
        assertThrows(UnsupportedOperationException.class, () -> interval.add(DurationFieldInstance.of(DurationFieldType.INSTANCE_MINUTES, 1)));
        assertThrows(UnsupportedOperationException.class, () -> interval.add(DurationFieldInstance.of(DurationFieldType.INSTANCE_SECONDS, 1)));
        assertThrows(UnsupportedOperationException.class, () -> interval.add(DurationFieldInstance.of(DurationFieldType.INSTANCE_MILLIS, 1)));
    }

    @Test
    public void testUnsupportedDurationField_compareTo() {
        // Test compareTo with UnsupportedDurationField
        DurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_WEEK);
        DurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);
        assertEquals(0, unsupportedField1.compareTo(unsupportedField2));
    }

    @Test
    public void testUnsupportedDurationField_toString() {
        // Test toString with UnsupportedDurationField
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_WEEK);
        assertEquals("UnsupportedDurationField[dayOfWeek]", unsupportedField.toString());
    }
}
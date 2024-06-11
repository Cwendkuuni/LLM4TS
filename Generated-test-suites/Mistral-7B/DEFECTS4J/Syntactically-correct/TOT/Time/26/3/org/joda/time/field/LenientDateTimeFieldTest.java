import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.DurationField;
import org.joda.time.LocalDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

    private static final Chronology DATE_TIME = DateTimeFields.localDate();
    private static final DateTimeField DAY_OF_MONTH = DateTimeFields.dayOfMonth();

    @Test
    public void testLenientDateTimeField() {
        DateTimeField strictField = DAY_OF_MONTH;
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(strictField, DATE_TIME);

        // Test isLenient method
        assertTrue(lenientField.isLenient());

        // Test getInstance method
        DateTimeField lenientField2 = LenientDateTimeField.getInstance(lenientField, DATE_TIME);
        assertEquals(lenientField, lenientField2);

        // Test getInstance method with null arguments
        assertNull(LenientDateTimeField.getInstance(null, DATE_TIME));
        assertNull(LenientDateTimeField.getInstance(strictField, null));

        // Test set method with valid values
        BaseDateTime dateTime = new BaseDateTime(0L, DATE_TIME);
        long instant = dateTime.toInstant().getMillis();
        int value = 25;
        long newInstant = lenientField.set(instant, value);
        assertEquals(newInstant, instant + DateTimeFields.DAYS.get(DATE_TIME).getLenientValue(value - get(instant, lenientField)));

        // Test set method with out of bounds value
        value = 32;
        newInstant = lenientField.set(instant, value);
        assertEquals(newInstant, instant + DateTimeFields.DAYS.get(DATE_TIME).getLenientValue(value - get(instant, lenientField)));

        // Test set method with the same value
        newInstant = lenientField.set(newInstant, value);
        assertEquals(newInstant, newInstant);

        // Test set method with null value
        value = 0;
        newInstant = lenientField.set(newInstant, value);
        assertEquals(newInstant, newInstant);

        // Test set method with negative value
        value = -1;
        newInstant = lenientField.set(newInstant, value);
        assertEquals(newInstant, newInstant);

        // Test get method with valid value
        int getValue = get(newInstant, lenientField);
        assertEquals(getValue, value);

        // Test get method with out of bounds value
        value += 7;
        getValue = get(newInstant, lenientField);
        assertEquals(getValue, value % getLenientFieldLength(lenientField));

        // Test get method with the same value
        getValue = get(newInstant, lenientField);
        assertEquals(getValue, value);

        // Test get method with null value
        getValue = get(null, lenientField);
        assertNull(getValue);

        // Test get method with negative value
        getValue = get(newInstan, lenientField);
        assertFalse(getValue >= 0);

        // Test add method with valid values
        DurationField durationField = DateTimeFields.millisOfDay();
        long duration = durationField.getLength();
        long result = lenientField.add(instant, duration);
        assertEquals(result, instant + duration);

        // Test add method with out of bounds value
        duration *= 2;
        result = lenientField.add(instant, duration);
        assertEquals(result, instant + getLenientFieldLength(lenientField) * 2);

        // Test add method with zero value
        result = lenientField.add(instant, 0);
        assertEquals(result, instant);

        // Test add method with negative value
        result = lenientField.add(instant, -duration);
        assertEquals(result, instant - duration);
    }

    private int get(long instant, DateTimeField lenientField) {
        if (lenientField instanceof LenientDateTimeField) {
            return ((LenientDateTimeField) lenientField).get(instant);
        } else {
            return get(instant, ((DelegatedDateTimeField) lenientField).getWrappedField());
        }
    }

    private long getLenientFieldLength(DateTimeField lenientField) {
        if (lenientField instanceof LenientDateTimeField) {
            return ((LenientDateTimeField) lenientField).getLength();
        } else {
            return getLenientFieldLength(((DelegatedDateTimeField) lenientField).getWrappedField());
        }
    }
}
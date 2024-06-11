package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalTime;
import org.joda.time.ZoneUtils;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private Chronology base;
    private DateTimeField field;
    private LenientDateTimeField lenientField;

    @Before
    public void setup() {
        base = mock(Chronology.class);
        field = mock(DateTimeField.class);
        when(field.getBaseDateTime()).thenReturn(new MutableDateTime());
        when(field.getType()).thenReturn(DateTimeFieldType.dayOfMonth());
        when(field.getValue(new MutableDateTime())).thenReturn(1);
        lenientField = new LenientDateTimeField(field, base);
    }

    @Test
    public void isLenient() {
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void setValue_validValue() {
        MutableDateTime datetime = new MutableDateTime();
        long instant = datetime.getMillis();

        int newValue = 1;
        long newInstant = lenientField.set(instant, newValue);

        assertEquals(instant + FieldUtils.safeSubtract(newValue, getFieldValue(instant)), newInstant);
        assertEquals(newValue, getFieldValue(newInstant));
    }

    @Test
    public void setValue_negativeValue() {
        MutableDateTime datetime = new MutableDateTime();
        long instant = datetime.getMillis();

        int newValue = -1;
        long newInstant = lenientField.set(instant, newValue);

        long expectedNewInstant = instant - FieldUtils.safeSubtract(getFieldValue(instant), newValue);
        assertEquals(expectedNewInstant, newInstant);
        assertEquals(getFieldValue(instant) + newValue, getFieldValue(newInstant));
    }

    @Test
    public void setValue_largerValue() {
        MutableDateTime datetime = new MutableDateTime();
        long instant = datetime.getMillis();

        int newValue = 31;
        long newInstant = lenientField.set(instant, newValue);

        long expectedNewInstant = instant + (30 * getFieldType().getMillisPerValue() + 1);
        assertEquals(expectedNewInstant, newInstant);
        assertEquals(newValue, getFieldValue(newInstant));
    }

    @Test
    public void setValue_largerValue_rollsOver() {
        MutableDateTime datetime = new MutableDateTime();
        long instant = datetime.getMillis();

        int newValue = 123;
        long newInstant = lenientField.set(instant, newValue);

        long expectedNewInstant = instant + getFieldType().getMillisPerValue() * getFieldValue(instant);
        assertEquals(expectedNewInstant, newInstant);
        assertEquals(newValue % getFieldValue(instant), getFieldValue(newInstant));
    }

    @Test
    public void setValue_outOfBoundsValue() {
        MutableDateTime datetime = new MutableDateTime();
        long instant = datetime.getMillis();

        int newValue = 32;
        long newInstant = lenientField.set(instant, newValue);

        long expectedNewInstant = instant + (31 * getFieldType().getMillisPerValue() + 1);
        assertEquals(expectedNewInstant, newInstant);
        assertEquals(newValue % getFieldValue(instant), getFieldValue(newInstant));
    }

    @Test
    public void setValue_valueIsZero_returnsInstantly() {
        MutableDateTime datetime = new MutableDateTime();
        long instant = datetime.getMillis();

        int newValue = 0;
        long newInstant = lenientField.set(instant, newValue);

        assertEquals(instant, newInstant);
        assertEquals(newValue, getFieldValue(newInstant));
    }

    @Test
    public void setValue_nullInput() {
        long instant = 0;

        try {
            lenientField.set(instant, null);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void setValue_nullInput_withChronology() {
        long instant = 0;

        try {
            LenientDateTimeField.getInstance(null, base);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSetValueWithChronology() {
        long instant = 1615626945000L; // Thu Jan 06 11:42:25 CET 2022

        LocalDateTime dateTime = new LocalDateTime(instant, base);
        int value = 31;
        long newInstant = lenientField.set(instant, value, base);

        LocalDateTime newDateTime = new LocalDateTime(newInstant, base);
        assertEquals(dateTime.withFieldAdded(DateTimeFieldType.dayOfMonth(), value), newDateTime);
    }

    @Test
    public void testSetValueWithChronology_outOfBoundsValue() {
        long instant = 1615626945000L; // Thu Jan 06 11:42:25 CET 2022

        LocalDateTime dateTime = new LocalDateTime(instant, base);
        int value = 33;
        long newInstant = lenientField.set(instant, value, base);

        LocalDateTime newDateTime = new LocalDateTime(newInstant, base);
        assertEquals(dateTime.withFieldAdded(DateTimeFieldType.dayOfMonth(), value % getFieldValue(instant)), newDateTime);
    }

    @Test
    public void testSetValueWithChronology_negativeValue() {
        long instant = 1615626945000L; // Thu Jan 06 11:42:25 CET 2022

        LocalDateTime dateTime = new LocalDateTime(instant, base);
        int value = -1;
        long newInstant = lenientField.set(instant, value, base);

        LocalDateTime newDateTime = new LocalDateTime(newInstant, base);
        assertEquals(dateTime.withFieldSubtracted(DateTimeFieldType.dayOfMonth(), Math.abs(value) % getFieldValue(instant)), newDateTime);
    }

    @Test
    public void testSetValueWithChronology_largerValue() {
        long instant = 1615626945000L; // Thu Jan 06 11:42:25 CET 2022

        LocalDateTime dateTime = new LocalDateTime(instant, base);
        int value = 32;
        long newInstant = lenientField.set(instant, value, base);

        LocalDateTime newDateTime = new LocalDateTime(newInstant, base);
        assertEquals(dateTime.withFieldAdded(DateTimeFieldType.dayOfMonth(), value % getFieldValue(instant)), newDateTime);
    }

    @Test
    public void testSetValueWithChronology_negativeValue_rollsOver() {
        long instant = 1615626945000L; // Thu Jan 06 11:42:25 CET 2022

        LocalDateTime dateTime = new LocalDateTime(instant, base);
        int value = -3;
        long newInstant = lenientField.set(instant, value, base);

        LocalDateTime newDateTime = new LocalDateTime(newInstant, base);
        assertEquals(dateTime.withFieldSubtracted(DateTimeFieldType.dayOfMonth(), (getFieldValue(instant) + value) % getFieldValue(instant)), newDateTime);
    }

    @Test
    public void testSetValueWithChronology_nullInput() {
        long instant = 1615626945000L; // Thu Jan 06 11:42:25 CET 2022

        try {
            lenientField.set(instant, 0, null);
        } catch (NullPointerException e) {
            // expected
        }
    }

    private int getFieldValue(long instant) {
        return getField().getValue(new MutableDateTime(instant));
    }

    private DateTimeFieldType getFieldType() {
        return getField().getType();
    }

    private DateTimeField getField() {
        return lenientField;
    }
}
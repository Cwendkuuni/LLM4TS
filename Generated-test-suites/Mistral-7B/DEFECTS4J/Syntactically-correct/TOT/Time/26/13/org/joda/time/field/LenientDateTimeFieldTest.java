package org.joda.time.field.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.joda.time.field.fieldvalues.LocalDateField;
import org.joda.time.field.fieldvalues.LocalDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private static final Chronology UTC = DateTimeConstants.UTC;

    @Test
    public void testLenientDateTimeField() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, UTC);

        MutableDateTime dateTime = new MutableDateTime(1152900000000L, UTC); // Mon Apr 01 01:00:00 2002

        // Test typical use case
        assertEquals(31, lenientField.get(dateTime.toLocalDateTime().toDate().getTime()));
        lenientField.set(dateTime, 32);
        assertEquals(32, lenientField.get(dateTime.toLocalDateTime().toDate().getTime()));

        // Test edge case: maximum value
        long maxValue = FieldUtils.safeGetLong(DateTimeFieldType.dayOfMonth().getMaximumValue(), null);
        lenientField.set(dateTime, maxValue + 1);
        assertEquals(maxValue + 1, lenientField.get(dateTime.toLocalDateTime().toDate().getTime()));

        // Test edge case: minimum value
        lenientField.set(dateTime, DateTimeFieldType.dayOfMonth().getMinimumValue());
        assertEquals(DateTimeFieldType.dayOfMonth().getMinimumValue(), lenientField.get(dateTime.toLocalDateTime().toDate().getTime()));

        // Test error scenario: negative value
        lenientField.set(dateTime, -1);
        assertEquals(DateTimeFieldType.dayOfMonth().getMinimumValue(), lenientField.get(dateTime.toLocalDateTime().toDate().getTime()));

        // Test error scenario: value larger than maximum value
        lenientField.set(dateTime, maxValue + 100);
        assertEquals(maxValue, lenientField.get(dateTime.toLocalDateTime().toDate().getTime()));

        // Test method isLenient()
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testLenientDateTimeFieldWithLocalDateTimeField() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.minuteOfHour(), UTC);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, UTC);

        LocalDateTime localDateTime = new LocalDateTime(2002, 4, 1, 0, 0, 0, UTC);

        // Test typical use case
        assertEquals(0, lenientField.get(localDateTime));
        lenientField.set(localDateTime, 60);
        assertEquals(60, lenientField.get(localDateTime));

        // Test edge case: maximum value
        int maxValue = DateTimeFieldType.minuteOfHour().getMaximumValue();
        lenientField.set(localDateTime, maxValue + 1);
        assertEquals(maxValue + 1, lenientField.get(localDateTime));

        // Test edge case: minimum value
        lenientField.set(localDateTime, DateTimeFieldType.minuteOfHour().getMinimumValue());
        assertEquals(DateTimeFieldType.minuteOfHour().getMinimumValue(), lenientField.get(localDateTime));

        // Test error scenario: negative value
        lenientField.set(localDateTime, -1);
        assertEquals(DateTimeFieldType.minuteOfHour().getMinimumValue(), lenientField.get(localDateTime));

        // Test error scenario: value larger than maximum value
        lenientField.set(localDateTime, maxValue + 100);
        assertEquals(maxValue, lenientField.get(localDateTime));

        // Test method isLenient()
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testLenientDateTimeFieldWithLocalDateField() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, UTC);

        LocalDateField localDateField = new LocalDateField(DateTimeFieldType.dayOfMonth(), UTC);

        // Test typical use case
        LocalDate date = new LocalDate(2002, 4, 1, UTC);
        assertEquals(1, lenientField.get(date));
        lenientField.set(date, 32);
        assertEquals(32, lenientField.get(date));

        // Test edge case: maximum value
        int maxValue = DateTimeFieldType.dayOfMonth().getMaximumValue();
        lenientField.set(date, maxValue + 1);
        assertEquals(maxValue + 1, lenientField.get(date));

        // Test edge case: minimum value
        lenientField.set(date, DateTimeFieldType.dayOfMonth().getMinimumValue());
        assertEquals(DateTimeFieldType.dayOfMonth().getMinimumValue(), lenientField.get(date));

        // Test error scenario: negative value
        lenientField.set(date, -1);
        assertEquals(DateTimeFieldType.dayOfMonth().getMinimumValue(), lenientField.get(date));

        // Test error scenario: value larger than maximum value
        lenientField.set(date, maxValue + 100);
        assertEquals(maxValue, lenientField.get(date));

        // Test method isLenient()
        assertTrue(lenientField.isLenient());
    }
}
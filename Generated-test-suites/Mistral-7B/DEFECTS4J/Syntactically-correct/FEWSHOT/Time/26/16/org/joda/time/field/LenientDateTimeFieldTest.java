package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testLenientInstance() {
        DateTimeField field = LenientDateTimeField.getInstance(new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null), Chronology.UTC);
        assertEquals(true, field.isLenient());
    }

    @Test
    public void testLenientSetOutOfBounds() {
        Chronology base = Chronology.of("America/New_York");
        DateTimeField field = LenientDateTimeField.getInstance(new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null), base);

        MutableDateTime dateTime = new MutableDateTime(1568928000000L, base);
        long initialValue = field.get(dateTime.toLocalDateTime().toDateMilliseconds());

        // set a value out of bounds
        long newValue = initialValue + 365 * 2;
        long result = field.set(dateTime.toLocalDateTime().toDateMilliseconds(), (int) newValue);

        // check the result is within bounds
        MutableDateTime newDateTime = new MutableDateTime(result, base);
        int newFieldValue = field.get(newDateTime.toLocalDateTime().toDateMilliseconds());
        assertEquals(initialValue + 365 * 2, newFieldValue);
    }

    @Test
    public void testLenientSetWithinBounds() {
        Chronology base = Chronology.UTC;
        DateTimeField field = LenientDateTimeField.getInstance(new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null), base);

        MutableDateTime dateTime = new MutableDateTime(1568928000000L, base);
        long initialValue = field.get(dateTime.toLocalDateTime().toDateMilliseconds());

        // set a value within bounds
        long newValue = initialValue + 365;
        long result = field.set(dateTime.toLocalDateTime().toDateMilliseconds(), (int) newValue);

        // check the result is the same as the new value
        MutableDateTime newDateTime = new MutableDateTime(result, base);
        int newFieldValue = field.get(newDateTime.toLocalDateTime().toDateMilliseconds());
        assertEquals(newValue, newFieldValue);
    }

    @Test
    public void testLenientSetNull() {
        Chronology base = Chronology.UTC;
        DateTimeField field = LenientDateTimeField.getInstance(new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null), base);

        MutableDateTime dateTime = null;
        long result = field.set(dateTime == null ? 0 : dateTime.toLocalDateTime().toDateMilliseconds(), 0);

        // check the result is the same as the initial value
        assertEquals(0, result);
    }
}
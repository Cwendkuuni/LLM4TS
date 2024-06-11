package org.joda.time.field.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZonedDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

    private static final Chronology UTC_CHRONOLOGY = DateTimeFields.UTC;

    @Test
    public void testLenientDateTimeField() {
        // Test with UTC
        DateTimeField lenientYear = LenientDateTimeField.getInstance(DateTimeFields.YEAR, UTC_CHRONOLOGY);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, UTC_CHRONOLOGY);

        // Test valid value
        long result = lenientYear.set(dateTime.toLong(), 1970);
        assertEquals(1970, lenientYear.get(result));
        assertEquals(1970, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test out of bounds value (negative)
        result = lenientYear.set(dateTime.toLong(), 1969);
        assertEquals(1970, lenientYear.get(result));
        assertEquals(1969, new DateTimeField(DateTimeFields.YEAR).get(result) - 1);

        // Test out of bounds value (positive)
        result = lenientYear.set(dateTime.toLong(), 1971);
        assertEquals(1971, lenientYear.get(result));
        assertEquals(1971, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test setting to the minimum value
        result = lenientYear.set(dateTime.toLong(), DateTimeFields.MIN_VALUE);
        assertEquals(DateTimeFields.MIN_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MIN_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test setting to the maximum value
        result = lenientYear.set(dateTime.toLong(), DateTimeFields.MAX_VALUE);
        assertEquals(DateTimeFields.MAX_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MAX_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test setting to a value outside the range of the Chronology
        result = lenientYear.set(dateTime.toLong(), Integer.MIN_VALUE);
        assertEquals(DateTimeFields.MIN_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MIN_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));

        result = lenientYear.set(dateTime.toLong(), Integer.MAX_VALUE);
        assertEquals(DateTimeFields.MAX_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MAX_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));
    }

    @Test
    public void testLenientDateTimeFieldWithLocalDateTime() {
        // Test with UTC
        DateTimeField lenientYear = LenientDateTimeField.getInstance(DateTimeFields.YEAR, UTC_CHRONOLOGY);
        LocalDateTime localDateTime = new LocalDateTime(UTC_CHRONOLOGY);

        // Test valid value
        long result = lenientYear.set(localDateTime.toLong(), 1970);
        assertEquals(1970, lenientYear.get(result));
        assertEquals(1970, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test out of bounds value (negative)
        result = lenientYear.set(localDateTime.toLong(), 1969);
        assertEquals(1970, lenientYear.get(result));
        assertEquals(1969, new DateTimeField(DateTimeFields.YEAR).get(result) - 1);

        // Test out of bounds value (positive)
        result = lenientYear.set(localDateTime.toLong(), 1971);
        assertEquals(1971, lenientYear.get(result));
        assertEquals(1971, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test setting to the minimum value
        result = lenientYear.set(localDateTime.toLong(), DateTimeFields.MIN_VALUE);
        assertEquals(DateTimeFields.MIN_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MIN_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test setting to the maximum value
        result = lenientYear.set(localDateTime.toLong(), DateTimeFields.MAX_VALUE);
        assertEquals(DateTimeFields.MAX_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MAX_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test setting to a value outside the range of the Chronology
        result = lenientYear.set(localDateTime.toLong(), Integer.MIN_VALUE);
        assertEquals(DateTimeFields.MIN_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MIN_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));

        result = lenientYear.set(localDateTime.toLong(), Integer.MAX_VALUE);
        assertEquals(DateTimeFields.MAX_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MAX_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));
    }

    @Test
    public void testLenientDateTimeFieldWithZonedDateTime() {
        // Test with UTC
        DateTimeField lenientYear = LenientDateTimeField.getInstance(DateTimeFields.YEAR, UTC_CHRONOLOGY);
        ZonedDateTime zonedDateTime = new ZonedDateTime(UTC_CHRONOLOGY);

        // Test valid value
        long result = lenientYear.set(zonedDateTime.toInstant().toEpochMilli(), 1970);
        assertEquals(1970, lenientYear.get(result));
        assertEquals(1970, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test out of bounds value (negative)
        result = lenientYear.set(zonedDateTime.toInstant().toEpochMilli(), 1969);
        assertEquals(1970, lenientYear.get(result));
        assertEquals(1969, new DateTimeField(DateTimeFields.YEAR).get(result) - 1);

        // Test out of bounds value (positive)
        result = lenientYear.set(zonedDateTime.toInstant().toEpochMilli(), 1971);
        assertEquals(1971, lenientYear.get(result));
        assertEquals(1971, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test setting to the minimum value
        result = lenientYear.set(zonedDateTime.toInstant().toEpochMilli(), DateTimeFields.MIN_VALUE);
        assertEquals(DateTimeFields.MIN_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MIN_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test setting to the maximum value
        result = lenientYear.set(zonedDateTime.toInstant().toEpochMilli(), DateTimeFields.MAX_VALUE);
        assertEquals(DateTimeFields.MAX_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MAX_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));

        // Test setting to a value outside the range of the Chronology
        result = lenientYear.set(zonedDateTime.toInstant().toEpochMilli(), Integer.MIN_VALUE);
        assertEquals(DateTimeFields.MIN_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MIN_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));

        result = lenientYear.set(zonedDateTime.toInstant().toEpochMilli(), Integer.MAX_VALUE);
        assertEquals(DateTimeFields.MAX_VALUE, lenientYear.get(result));
        assertEquals(DateTimeFields.MAX_VALUE, new DateTimeField(DateTimeFields.YEAR).get(result));
    }
}
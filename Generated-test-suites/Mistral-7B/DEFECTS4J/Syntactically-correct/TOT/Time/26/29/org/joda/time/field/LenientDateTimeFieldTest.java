package org.joda.time.field.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private DateTimeField testField;
    private Chronology testChronology;
    private Instant testStart;
    private Instant testEnd;

    @Before
    public void setUp() {
        testField = DateTimeFieldType.millisecondOfMinute();
        testChronology = DateTimeUtils.forPattern("dd-MM-yyyy HH:mm:ss.SSS");
        testStart = testChronology.instant();
        testEnd = testChronology.plus(new Duration(1, DurationFieldType.HOUR));
    }

    @Test
    public void testLenientSetWithinBounds() {
        MutableDateTime dateTime = new MutableDateTime(testChronology, testStart);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(testField, testChronology);

        // Set a value within bounds
        long value = dateTime.getMillis() + 500;
        Instant result = lenientField.set(dateTime.toInstant(), value);
        assertEquals(testChronology.getZone().getOffset(result), 0);
        assertEquals(dateTime.getMillis(), result.getMillis() - 500);
    }

    @Test
    public void testLenientSetWithinBoundsRounded() {
        MutableDateTime dateTime = new MutableDateTime(testChronology, testStart);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(testField, testChronology);

        // Set a value within bounds, rounded to the nearest minute
        long value = dateTime.getMillis() + 63000;
        Instant result = lenientField.set(dateTime.toInstant(), value);
        long roundedValue = testChronology.getField(DateTimeFieldType.minuteOfHour()).get(result);
        assertEquals(testChronology.getField(DateTimeFieldType.minuteOfHour()).get(dateTime), roundedValue);
        assertEquals(testChronology.getZone().getOffset(result), 0);
    }

    @Test
    public void testLenientSetOutOfBounds() {
        MutableDateTime dateTime = new MutableDateTime(testChronology, testStart);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(testField, testChronology);

        // Set a value out of bounds
        long value = dateTime.getMillis() + 86400001;
        Instant result = lenientField.set(dateTime.toInstant(), value);
        // The result should be the next day
        Instant expected = testChronology.plus(new Duration(1, DurationFieldType.DAY)).instant();
        assertEquals(testChronology.getZone().getOffset(expected), 0);
        assertTrue(expected.getMillis() > result.getMillis());
        assertTrue(Math.abs(expected.getMillis() - result.getMillis()) < 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLenientSetWithNullDateTime() {
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(testField, testChronology);
        lenientField.set(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLenientSetWithNullChronology() {
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(testField, null);
        lenientField.set(testStart, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLenientSetWithInvalidValue() {
        MutableDateTime dateTime = new MutableDateTime(testChronology, testStart);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(testField, testChronology);

        // Set a negative value
        long value = dateTime.getMillis() - 1;
        Instant result = lenientField.set(dateTime.toInstant(), value);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLenientSetWithInvalidChronology() {
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(testField, null);
        lenientField.set(testStart, 0);
        fail("Expected IllegalArgumentException");
    }
}
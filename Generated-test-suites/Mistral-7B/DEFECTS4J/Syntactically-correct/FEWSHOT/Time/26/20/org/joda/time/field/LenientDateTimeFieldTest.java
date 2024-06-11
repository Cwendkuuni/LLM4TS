package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeField;
import org.joda.time.LocalDateTimeFieldType;
import org.joda.time.ZoneUtils;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private Chronology chronology;
    private DateTimeFieldType type;
    private LocalDateTimeField localField;
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        chronology = mock(Chronology.class);
        type = new LocalDateTimeFieldType("TestType", DateTimeFieldType.INTEGER_FIELD);
        localField = new LocalDateTimeField(type, chronology);
        lenientField = new LenientDateTimeField(localField, chronology);
    }

    @Test
    public void testInstance() {
        DateTimeField field = LenientDateTimeField.getInstance(localField, chronology);
        assertEquals(lenientField, field);

        field = LenientDateTimeField.getInstance(lenientField, chronology);
        assertEquals(lenientField, field);

        field = LenientDateTimeField.getInstance(new StrictLocalDateTimeField(type, chronology), chronology);
        assertNotSame(lenientField, field);

        field = LenientDateTimeField.getInstance(null, chronology);
        assertNull(field);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0, chronology);
        long instant = localDateTime.getMillis();

        int newValue = 5;

        long expectedResult = instant + FieldUtils.getDays(1) * 86400000L; // 1 day
        long result = lenientField.set(instant, newValue);

        assertEquals(expectedResult, result);

        LocalDateTime expectedLocalDateTime = new LocalDateTime(2022, 1, 2, 0, 0, 0, 0, chronology);
        LocalDateTime actualLocalDateTime = new LocalDateTime(result, chronology);
        assertEquals(expectedLocalDateTime, actualLocalDateTime);

        // Test setting a value that is out of bounds negatively
        newValue = -5;
        expectedResult = instant - FieldUtils.getDays(1) * 86400000L; // 1 day
        result = lenientField.set(instant, newValue);
        assertEquals(expectedResult, result);
        expectedLocalDateTime = new LocalDateTime(2021, 12, 31, 0, 0, 0, 0, chronology);
        actualLocalDateTime = new LocalDateTime(result, chronology);
        assertEquals(expectedLocalDateTime, actualLocalDateTime);
    }
}